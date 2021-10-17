package com.ebuy.userservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Immutable
@Table(name = "messages")
@Entity
public class Message extends IdentifiableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Column(name = "text", nullable = false, length = 5000)
    String text;

    @ElementCollection
    @CollectionTable(
            name = "messages_documents",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Column(name = "document_id")
    List<Long> documentIds = new ArrayList<>();

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    Date date;
}
