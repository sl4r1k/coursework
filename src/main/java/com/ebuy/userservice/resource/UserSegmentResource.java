package com.ebuy.userservice.resource;

import com.ebuy.userservice.entity.UserSegment;
import com.ebuy.userservice.entity.UserSegmentFilter;
import com.ebuy.userservice.service.UserSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user-segments")
@RestController
public class UserSegmentResource extends CrudController<UserSegment> {
    private final UserSegmentService service;

    @Autowired
    public UserSegmentResource(UserSegmentService service) {
        super(service);
        this.service = service;
    }

    @PutMapping("/{id}/filters")
    public ResponseEntity<UserSegment> putFiltersById(@PathVariable Long id, @RequestBody List<UserSegmentFilter> filters) {
        this.service.updateFiltersById(id, filters);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<UserSegment>> getByTitle(@PathVariable String title) {
        return new ResponseEntity<>(this.service.getByTitle(title), HttpStatus.OK);
    }
}
