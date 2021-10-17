package com.ebuy.userservice.resource;

import com.ebuy.userservice.entity.VendorCommunication;
import com.ebuy.userservice.service.VendorCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/vendor-communications")
@RestController
public class VendorCommunicationResource extends CrudController<VendorCommunication> {
    @Autowired
    public VendorCommunicationResource(VendorCommunicationService service) {
        super(service);
    }
}
