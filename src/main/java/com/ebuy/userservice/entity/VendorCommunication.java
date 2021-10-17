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
@Table(name = "vendor_communications")
@Entity
public class VendorCommunication extends IdentifiableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    User vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    User customer;

    @Column(name = "product_id", nullable = false)
    Long productId;

    @JoinTable(
            name = "vendor_communications_messages",
            joinColumns = @JoinColumn(name = "communication_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Message> messages = new ArrayList<>();
}
