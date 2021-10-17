package com.ebuy.userservice.service;

import com.ebuy.userservice.embedded.EmailEvent;
import com.ebuy.userservice.entity.EmailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService extends Dao<EmailTemplate> {
    private final MailSender sender;

    @Autowired
    public EmailService(MailSender sender) {
        super(EmailTemplate.class);
        this.sender = sender;
    }

    public void sendToAddressByEventTemplate(String address, EmailEvent event) {
        EmailTemplate template = this.getByEvent(event);
        this.sendToAddressWithSubjectAndText(
                address,
                template.getSubject(),
                template.getText()
        );
    }

    private void sendToAddressWithSubjectAndText(String address, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("commerce.ebuy@gmail.com");
        message.setTo(address);
        message.setSubject(subject);
        message.setText(text);
        this.sender.send(message);
    }

    public EmailTemplate getByEvent(EmailEvent event) {
        return this.manager.createNamedQuery(
                EmailTemplate.GET_BY_EVENT,
                EmailTemplate.class
        ).setParameter("event", event.toString())
                .getSingleResult();
    }
}
