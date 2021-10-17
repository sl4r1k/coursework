package com.ebuy.userservice.resource;

import com.ebuy.userservice.entity.IdentifiableEntity;
import com.ebuy.userservice.service.Dao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public abstract class CrudController<ENTITY extends IdentifiableEntity> {
    private final Dao<ENTITY> service;

    @Autowired(required = false)
    protected CrudController(Dao<ENTITY> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ENTITY> post(/*@RequestBody */ENTITY entity, HttpServletRequest request) {
        this.service.save(entity);
        HttpHeaders locationHeader = this.buildLocationHeader(entity.getId(), request);
        return new ResponseEntity<>(locationHeader, HttpStatus.CREATED);
    }

    @SneakyThrows(URISyntaxException.class)
    protected HttpHeaders buildLocationHeader(Long id, HttpServletRequest request) {
        HttpHeaders locationHeader = new HttpHeaders();
        locationHeader.setLocation(new URI(String.format(
                "%s/%d",
                request.getRequestURI(),
                id
        )));
        return locationHeader;
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<ENTITY> patch(@PathVariable Long id, @RequestBody JsonPatch patch)
            throws  JsonPatchException, JsonProcessingException {
        this.service.updateById(id, this.patchEntity(id, patch));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected ENTITY patchEntity(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        ENTITY patchedEntity = this.service.getById(id);
        patchedEntity = new EntityPatcher().patch(patch, patchedEntity);
        return patchedEntity;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ENTITY> delete(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ENTITY>> getAll() {
        return new ResponseEntity<>(this.service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ENTITY> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }
}
