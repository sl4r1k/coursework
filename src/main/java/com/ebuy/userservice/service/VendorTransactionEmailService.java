package com.ebuy.userservice.service;

import com.ebuy.userservice.embedded.EmailEvent;
import com.ebuy.userservice.embedded.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorTransactionEmailService {
    private final EmailService emailService;

    @Autowired
    public VendorTransactionEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendNotificationByTransactionType(TransactionType type, String address) {
        switch (type) {
            case PAYOUT:
                this.sendPayoutNotification(address);
                break;
            case TRANSFER:
                this.sendTransferNotification(address);
                break;
            default:
                throw new IllegalArgumentException(
                        "It is not necessary to notify the vendor when he withdraws funds"
                );
        }
    }

    private void sendPayoutNotification(String address) {
        emailService.sendToAddressByEventTemplate(address, EmailEvent.VENDOR_PAYOUT);
    }

    private void sendTransferNotification(String address) {
        emailService.sendToAddressByEventTemplate(address, EmailEvent.VENDOR_TRANSFER);
    }
}
