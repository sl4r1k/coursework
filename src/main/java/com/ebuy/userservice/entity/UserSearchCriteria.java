package com.ebuy.userservice.entity;

import com.ebuy.userservice.embedded.AccountType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Immutable
@Table(name = "user_search_criteria")
@Entity
public class UserSearchCriteria extends IdentifiableEntity {
    @Column(name = "title", unique = true)
    String title;

    @Column(name = "login")
    String login;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    AccountType accountType;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "company_name")
    String companyName;

    @Column(name = "role_id")
    Long roleId;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "fax_number")
    String faxNumber;

    @Column(name = "email_address")
    String emailAddress;

    @Column(name = "url_address")
    String urlAddress;

    @Column(name = "individual_taxpayer_number")
    String individualTaxpayerNumber;

    @Column(name = "bank_card_number")
    String bankCardNumber;

    @Column(name = "postal_code")
    String postalCode;

    @Column(name = "country")
    String country;

    @Column(name = "region")
    String region;

    @Column(name = "city")
    String city;

    @Column(name = "street")
    String street;

    @Column(name = "house_number")
    String houseNumber;

    @CollectionTable(
            name = "user_search_criteria_user_tags",
            joinColumns = @JoinColumn(name = "tag_id")
    )
    @ElementCollection
    @Column(name = "text")
    List<String> tags = new ArrayList<>();

    @Column(name = "begin_registration_date")
    Date beginRegistrationDate;

    @Column(name = "end_registration_date")
    Date endRegistrationDate;

    @ElementCollection
    @CollectionTable(
            name = "user_search_criteria_ordered_products",
            joinColumns = @JoinColumn(name = "criteria_id")
    )
    @Column(name = "product_id")
    List<Long> orderedProductIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "user_search_criteria_user_groups",
            joinColumns = @JoinColumn(name = "criteria_id")
    )
    @Column(name = "group_id")
    List<Long> groupIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "user_search_criteria_user_segments",
            joinColumns = @JoinColumn(name = "criteria_id")
    )
    @Column(name = "segment_id")
    List<Long> segmentIds = new ArrayList<>();
}
