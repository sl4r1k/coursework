package com.ebuy.userservice.entity;

import com.ebuy.userservice.embedded.EmailEvent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@NamedQuery(
        name = EmailTemplate.GET_BY_EVENT,
        query = "SELECT template"
                + " FROM EmailTemplate AS template"
                + " WHERE template.event = :event"
)
@Table(name = "email_templates")
@Entity
public class EmailTemplate extends IdentifiableEntity {
    public static final String GET_BY_EVENT = "EmailTemplate.getByEvent";

    @Column(name = "event", nullable = false)
    EmailEvent event;

    @Column(name = "subject", nullable = false)
    String subject;

    @Column(name = "text", nullable = false)
    String text;
}
