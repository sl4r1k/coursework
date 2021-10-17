package com.ebuy.userservice.resource;

import com.ebuy.userservice.entity.UserGroup;
import com.ebuy.userservice.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user-groups")
@RestController
public class UserGroupResource extends CrudController<UserGroup> {
    private final UserGroupService service;

    @Autowired
    public UserGroupResource(UserGroupService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<UserGroup>> getByTitle(@PathVariable String title) {
        return new ResponseEntity<>(service.getByTitle(title), HttpStatus.OK);
    }
}
