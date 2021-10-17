package com.ebuy.userservice.resource;

import com.ebuy.userservice.embedded.EmailEvent;
import com.ebuy.userservice.entity.EmailTemplate;
import com.ebuy.userservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/email-templates")
@RestController
public class EmailTemplateResource extends CrudController<EmailTemplate> {
    private final EmailService service;

    @Autowired
    public EmailTemplateResource(EmailService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/event/{event}")
    public ResponseEntity<EmailTemplate> getByEvent(EmailEvent event) {
        return new ResponseEntity<>(service.getByEvent(event), HttpStatus.OK);
    }
}
