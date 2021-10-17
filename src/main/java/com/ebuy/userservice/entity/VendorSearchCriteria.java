package com.ebuy.userservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Immutable
@Table(name = "vendor_search_criteria")
@Entity
public class VendorSearchCriteria extends IdentifiableEntity {
    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "fullName")
    String fullName;

    @Column(name = "companyName")
    String companyName;

    @Column(name = "phoneNumber")
    String phoneNumber;

    @Column(name = "emailAddress")
    String emailAddress;

    @Column(name = "faxNumber")
    String faxNumber;

    @ElementCollection
    @CollectionTable(
            name = "vendor_search_criteria_vendor_merchandises",
            joinColumns = @JoinColumn(name = "criteria_id")
    )
    @Column(name = "group_id")
    List<Long> merchandiseIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "vendor_search_criteria_vendor_orders",
            joinColumns = @JoinColumn(name = "criteria_id")
    )
    @Column(name = "group_id")
    List<Long> orderIds = new ArrayList<>();
}
