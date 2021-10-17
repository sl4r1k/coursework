package com.ebuy.userservice.resource;

import com.ebuy.userservice.entity.*;
import com.ebuy.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/api/v1/users")
@RestController
public class UserResource extends CrudController<User> {
    private final UserService service;

    @Autowired
    public UserResource(UserService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<List<User>> getByLogin(@PathVariable String login) {
        return new ResponseEntity<>(
                this.service.getByLogin(login),
                HttpStatus.OK
        );
    }

    @GetMapping("/full-name/{fullName}")
    public ResponseEntity<List<User>> getByFullName(@PathVariable String fullName) {
        return new ResponseEntity<>(
                this.service.getByFullName(fullName),
                HttpStatus.OK
        );
    }

    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<List<User>> getByPhoneNumber(@PathVariable String phoneNumber) {
        return new ResponseEntity<>(
                this.service.getByPhoneNumber(phoneNumber),
                HttpStatus.OK
        );
    }

    @GetMapping("/fax-number/{faxNumber}")
    public ResponseEntity<List<User>> getByFaxNumber(@PathVariable String faxNumber) {
        return new ResponseEntity<>(
                this.service.getByFaxNumber(faxNumber),
                HttpStatus.OK
        );
    }

    @GetMapping("/email-address/{emailAddress}")
    public ResponseEntity<List<User>> getByEmailAddress(@PathVariable String emailAddress) {
        return new ResponseEntity<>(
                this.service.getByEmailAddress(emailAddress),
                HttpStatus.OK
        );
    }

    @GetMapping("/company-name/{companyName}")
    public ResponseEntity<List<User>> getByCompanyName(@PathVariable String companyName) {
        return new ResponseEntity<>(
                this.service.getByCompanyName(companyName),
                HttpStatus.OK
        );
    }

    @GetMapping("/individual-taxpayer-number/{individualTaxpayerNumber}")
    public ResponseEntity<List<User>> getByIndividualTaxpayerNumber(@PathVariable String individualTaxpayerNumber) {
        return new ResponseEntity<>(
                this.service.getByIndividualTaxpayerNumber(individualTaxpayerNumber),
                HttpStatus.OK
        );
    }

    @GetMapping("/bank-card-number/{bankCardNumber}")
    public ResponseEntity<List<User>> getByBankCardNumber(@PathVariable String bankCardNumber) {
        return new ResponseEntity<>(
                this.service.getByBankCardNumber(bankCardNumber),
                HttpStatus.OK
        );
    }

    @GetMapping("/criteria/{id}")
    public ResponseEntity<List<User>> getByCriteriaId(@PathVariable Long id) {
        return new ResponseEntity<>(
                this.service.getByCriteriaId(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/criteria")
    public ResponseEntity<List<User>> getByCriteria(UserSearchCriteria criteria) {
        return new ResponseEntity<>(
                this.service.getByCriteria(criteria),
                HttpStatus.OK
        );
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> postRole(@RequestBody Role role, HttpServletRequest request) {
        this.service.saveRole(role);
        return new ResponseEntity<>(
                this.buildLocationHeader(role.getId(), request),
                HttpStatus.CREATED
        );
    }

    @PatchMapping(value = "/roles/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Role> patchRole(@PathVariable Long id, @RequestBody JsonPatch patch)
            throws JsonPatchException, JsonProcessingException {
        this.service.updateById(id, this.patchEntity(id, patch));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(this.service.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/roles/{roleId}")
    public ResponseEntity<Role> getRoleByRoleId(@PathVariable Long roleId) {
        return new ResponseEntity<>(this.service.getRoleByRoleId(roleId), HttpStatus.OK);
    }
}
