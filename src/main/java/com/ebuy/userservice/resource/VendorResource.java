package com.ebuy.userservice.resource;

import com.ebuy.userservice.entity.User;
import com.ebuy.userservice.entity.VendorSearchCriteria;
import com.ebuy.userservice.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/vendors")
@RestController
public class VendorResource extends CrudController<User> {
    private final VendorService service;

    @Autowired
    public VendorResource(VendorService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/full-name/{fullName}")
    public ResponseEntity<List<User>> getByFullName(@PathVariable String fullName) {
        return new ResponseEntity<>(this.service.getByFullName(fullName), HttpStatus.OK);
    }

    @GetMapping("/company-name/{companyName}")
    public ResponseEntity<List<User>> getByCompanyName(@PathVariable String companyName) {
        return new ResponseEntity<>(this.service.getByCompanyName(companyName), HttpStatus.OK);
    }

    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<List<User>> getByPhoneNumber(@PathVariable String phoneNumber) {
        return new ResponseEntity<>(this.service.getByPhoneNumber(phoneNumber), HttpStatus.OK);
    }

    @GetMapping("/fax-number/{phoneNumber}")
    public ResponseEntity<List<User>> getByFaxNumber(@PathVariable String faxNumber) {
        return new ResponseEntity<>(this.service.getByFaxNumber(faxNumber), HttpStatus.OK);
    }

    @GetMapping("/criteria/{criteriaId}")
    public ResponseEntity<List<User>> getByCriteriaId(@PathVariable Long criteriaId) {
        return new ResponseEntity<>(this.service.getByCriteriaId(criteriaId), HttpStatus.OK);
    }

    @GetMapping("/criteria")
    public ResponseEntity<List<User>> getByCriteria(VendorSearchCriteria criteria) {
        return new ResponseEntity<>(this.service.getByCriteria(criteria), HttpStatus.OK);
    }
}
