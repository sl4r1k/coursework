package com.ebuy.userservice.resource;

import com.ebuy.userservice.entity.SupplierSearchCriteria;
import com.ebuy.userservice.entity.User;
import com.ebuy.userservice.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/suppliers")
@RestController
public class SupplierResource extends CrudController<User> {
    private final SupplierService service;

    @Autowired
    public SupplierResource(SupplierService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/criteria/{criteriaId}")
    public ResponseEntity<List<User>> getByCriteriaId(@PathVariable Long criteriaId) {
        return new ResponseEntity<>(this.service.getByCriteriaId(criteriaId), HttpStatus.OK);
    }

    @GetMapping("/criteria")
    public ResponseEntity<List<User>> getByCriteria(SupplierSearchCriteria criteria) {
        return new ResponseEntity<>(this.service.getByCriteria(criteria), HttpStatus.OK);
    }
}
