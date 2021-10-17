package com.ebuy.userservice.resource;

import com.ebuy.userservice.entity.Dialog;
import com.ebuy.userservice.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/dialogs")
@RestController
public class DialogResource extends CrudController<Dialog> {
    @Autowired
    public DialogResource(DialogService service) {
        super(service);
    }
}
