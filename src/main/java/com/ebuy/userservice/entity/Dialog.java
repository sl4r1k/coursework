package com.ebuy.userservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "dialogs")
@Entity
public class Dialog extends IdentifiableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    User firstUser;

    @ManyToOne(fetch = FetchType.LAZY)
    User secondUser;

    @JoinTable(
            name = "dialogs_messages",
            joinColumns = @JoinColumn(name = "dialog_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Message> messages = new ArrayList<>();
}
