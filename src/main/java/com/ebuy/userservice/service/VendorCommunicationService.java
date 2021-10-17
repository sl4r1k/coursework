package com.ebuy.userservice.service;

import com.ebuy.userservice.entity.VendorCommunication;
import org.springframework.stereotype.Service;

@Service
public class VendorCommunicationService extends Dao<VendorCommunication> {
    protected VendorCommunicationService() {
        super(VendorCommunication.class);
    }

    @Override
    public void save(VendorCommunication communication) {
        throwExceptionIfCommunicationIsNotWithVendor(communication);
        super.save(communication);
    }

    private void throwExceptionIfCommunicationIsNotWithVendor(VendorCommunication communication) {
        if (!communication.getVendor().getRole().getTitle().equals("VENDOR")) {
            throw new IllegalArgumentException("Communication can only be with the vendor");
        }
    }
}
