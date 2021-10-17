package com.ebuy.userservice.entity;

import com.ebuy.userservice.embedded.AccountType;
import com.ebuy.userservice.embedded.Availability;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@NamedQuery(
        name = User.UPDATE_PASSWORD_BY_ID,
        query = "UPDATE User AS user"
                + " SET user.password = :newPassword"
                + " WHERE user.id = :id"
)
@NamedQuery(
        name = User.UPDATE_ROLE_BY_ID,
        query = "UPDATE User AS user"
                + " SET user.role = :newRole"
                + " WHERE user.id = :id"
)
@NamedNativeQuery(
        name = User.GET_BY_LOGIN,
        query = "SELECT *"
                + " FROM users AS u"
                + " WHERE compare(u.login, :login)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_BY_FULL_NAME,
        query = "SELECT * FROM users AS u"
                + " WHERE compare_by_full_text(concat(u.first_name, ' ', u.last_name), :fullName)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_BY_PHONE_NUMBER,
        query = "SELECT * FROM users AS u"
                + " WHERE compare(u.phone_number, :phoneNumber)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_BY_FAX_NUMBER,
        query = "SELECT * FROM users AS u"
                + " WHERE compare(u.fax_number, :faxNumber)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_BY_EMAIL_ADDRESS,
        query = "SELECT * FROM users AS u"
                + " WHERE compare(u.email_address, :emailAddress)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_BY_COMPANY_NAME,
        query = "SELECT * FROM users AS u"
                + " WHERE compare(u.company_name, :companyName)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_BY_INDIVIDUAL_TAXPAYER_NUMBER,
        query = "SELECT * FROM users AS u"
                + " WHERE compare(u.individual_taxpayer_number, :individualTaxpayerNumber)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_BY_BANK_CARD_NUMBER,
        query = "SELECT * FROM users AS u"
                + " WHERE compare(u.bank_card_number, :bankCardNumber)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_BY_CRITERIA,
        query = "SELECT *"
                + " FROM users u"
                + "         INNER JOIN user_tags t ON u.id = t.user_id"
                + "         INNER JOIN user_ordered_products p ON u.id = p.user_id"
                + "         INNER JOIN user_groups_users g ON u.id = g.user_id"
                + "         INNER JOIN user_segments_users s ON u.id = s.user_id"
                + " WHERE "
                + "      compare(login, :login)"
                + "  AND account_type = :accountType"
                + "  AND compare_by_full_text(concat(first_name, ' ', last_name), :fullName)"
                + "  AND compare(company_name, :companyName)"
                + "  AND compare(role_id, :roleId)"
                + "  AND compare(phone_number, :phoneNumber)"
                + "  AND compare(fax_number, :faxNumber)"
                + "  AND compare(email_address, :emailAddress)"
                + "  AND compare(url_address, :urlAddress)"
                + "  AND compare(individual_taxpayer_number, :individualTaxpayerNumber)"
                + "  AND compare(bank_card_number, :bankCardNumber)"
                + "  AND compare(postal_code, :postalCode)"
                + "  AND compare(country, :country)"
                + "  AND compare(region, :region)"
                + "  AND compare(city, :city)"
                + "  AND compare(street, :street)"
                + "  AND compare(house_number, :houseNumber)"
                + "  AND compare_with_array(text, (:tags))"
                + "  AND registration_date BETWEEN :beginRegistrationDate AND :endRegistrationDate"
                + "  AND product_id IN (:productIds)"
                + "  AND group_id IN (:groupIds)"
                + "  AND segment_id IN (:segmentIds)",
        resultClass = User.class
)
@NamedQuery(
        name = User.UPDATE_VENDOR_MERCHANDISES_BY_ID,
        query = "UPDATE User AS vendor"
                + " SET vendor.vendorMerchandiseIds = (:merchandiseIds)"
                + " WHERE vendor.role.title = 'VENDOR'"
                + " AND vendor.id = :id"
)
@NamedQuery(
        name = User.UPDATE_VENDOR_BALANCE_BY_ID,
        query = "UPDATE User AS vendor"
                + " SET vendor.vendorBalance = :newBalance"
                + " WHERE vendor.role.title = 'VENDOR'"
                + " AND vendor.id = :id"
)
@NamedQuery(
        name = User.UPDATE_VENDOR_AVAILABILITY_BY_ID,
        query = "UPDATE User AS vendor"
                + " SET vendor.availability = :newAvailability"
                + " WHERE vendor.role.title = 'VENDOR'"
                + " AND vendor.id = :id"
)
@NamedQuery(
        name = User.GET_VENDOR_BY_FULL_NAME,
        query = "SELECT vendor"
                + " FROM User AS vendor"
                + " WHERE vendor.role.title = 'VENDOR'"
                + " AND concat(vendor.firstName, ' ', vendor.lastName) = :fullName"
)
@NamedQuery(
        name = User.GET_VENDOR_BY_COMPANY_NAME,
        query = "SELECT vendor"
                + " FROM User AS vendor"
                + " WHERE vendor.role.title = 'VENDOR'"
                + " AND vendor.companyName = :companyName"
)
@NamedQuery(
        name = User.GET_VENDOR_BY_PHONE_NUMBER,
        query = "SELECT vendor"
                + " FROM User AS vendor"
                + " WHERE vendor.role.title = 'VENDOR'"
                + " AND vendor.phoneNumber = :phoneNumber"
)
@NamedQuery(
        name = User.GET_VENDOR_BY_EMAIL_ADDRESS,
        query = "SELECT vendor"
                + " FROM User AS vendor"
                + " WHERE vendor.role.title = 'VENDOR'"
                + " AND vendor.emailAddress = :emailAddress"
)
@NamedQuery(
        name = User.GET_VENDOR_BY_FAX_NUMBER,
        query = "SELECT vendor"
                + " FROM User AS vendor"
                + " WHERE vendor.role.title = 'VENDOR'"
                + " AND vendor.faxNumber = :faxNumber"
)
@NamedNativeQuery(
        name = User.GET_VENDOR_BY_CRITERIA,
        query = "SELECT *"
                + " FROM users AS v"
                + "         INNER JOIN roles AS r ON v.role_id = r.id"
                + "         INNER JOIN vendors_merchandises AS m ON v.id = m.vendor_id"
                + "         INNER JOIN vendors_orders AS o ON v.id = o.vendor_id"
                + " WHERE"
                + "     r.title = 'VENDOR'"
                + " AND compare_by_full_text(concat(v.first_name, ' ', v.last_name), :fullName)"
                + " AND compare(v.company_name, :companyName)"
                + " AND compare(v.phone_number, :phoneNumber)"
                + " AND compare(v.email_address, :email_address)"
                + " AND compare(v.fax_number, :faxNumber)"
                + " AND compare(v.url_address, :urlAddress)"
                + " AND m.merchandise_id IN (:merchandiseIds)"
                + " AND o.order_id IN (:orderIds)",
        resultClass = User.class
)
@NamedNativeQuery(
        name = User.GET_SUPPLIER_BY_CRITERIA,
        query = "SELECT *"
                + " FROM users AS s"
                + "         INNER JOIN roles AS r ON s.role_id = r.id"
                + "         INNER JOIN suppliers_products AS p ON s.id = p.supplier_id"
                + " WHERE"
                + "     r.title = 'SUPPLIER'"
                + " AND compare_by_full_text(concat(s.first_name, ' ', s.last_name), :fullName)"
                + " AND compare(s.company_name, :companyName)"
                + " AND compare(s.phone_number, :phoneNumber)"
                + " AND compare(s.email_address, :email_address)"
                + " AND compare(s.fax_number, :faxNumber)"
                + " AND compare(s.url_address, :urlAddress)"
                + " AND p.product_id IN (:productIds)",
        resultClass = User.class
)
@Table(name = "users", indexes = {
        @Index(columnList = "last_name, first_name", name = "users_last_name_first_name_idx"),
        @Index(columnList = "company_name", name = "users_company_name_idx")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User extends IdentifiableEntity {
    public static final String UPDATE_PASSWORD_BY_ID = "User.updatePasswordById";
    public static final String UPDATE_ROLE_BY_ID = "User.updateRoleById";
    public static final String GET_BY_LOGIN = "User.getByLogin";
    public static final String GET_BY_FULL_NAME = "User.getByFullName";
    public static final String GET_BY_PHONE_NUMBER = "User.getByPhoneNumber";
    public static final String GET_BY_FAX_NUMBER = "User.getByFaxNumber";
    public static final String GET_BY_EMAIL_ADDRESS = "User.getByEmailAddress";
    public static final String GET_BY_COMPANY_NAME = "User.getByCompanyName";
    public static final String GET_BY_INDIVIDUAL_TAXPAYER_NUMBER = "User.getByIndividualTaxpayerNumber";
    public static final String GET_BY_BANK_CARD_NUMBER = "User.getByBankCardNumber";
    public static final String GET_BY_CRITERIA = "User.getByCriteria";
    public static final String UPDATE_VENDOR_MERCHANDISES_BY_ID = "Vendor.updateMerchandisesById";
    public static final String UPDATE_VENDOR_BALANCE_BY_ID = "Vendor.updateBalanceById";
    public static final String UPDATE_VENDOR_AVAILABILITY_BY_ID = "Vendor.updateAvailabilityById";
    public static final String GET_VENDOR_BY_FULL_NAME = "Vendor.getByFullName";
    public static final String GET_VENDOR_BY_COMPANY_NAME = "Vendor.getByCompanyName";
    public static final String GET_VENDOR_BY_PHONE_NUMBER = "Vendor.getByPhoneNumb  er";
    public static final String GET_VENDOR_BY_EMAIL_ADDRESS = "Vendor.getByEmailAddress";
    public static final String GET_VENDOR_BY_FAX_NUMBER = "Vendor.getByFaxNumber";
    public static final String GET_VENDOR_BY_CRITERIA = "Vendor.getByCriteria";
    public static final String GET_SUPPLIER_BY_CRITERIA = "Supplier.getByCriteria";

    @Column(name = "login", unique = true, nullable = false)
    String login;

    @Column(name = "password", nullable = false)
    char[] password;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type"/*, columnDefinition = "account_type"*/, nullable = false)
//    @Type( type = "pgsql_enum" )
    AccountType accountType;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "company_name")
    String companyName;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    Role role;

    @Column(name = "phone_number", unique = true, nullable = false, length = 15)
    String phoneNumber;

    @Column(name = "fax_number", unique = true)
    String faxNumber;

    @Column(name = "email_address", unique = true)
    String emailAddress;

    @Column(name = "url_address")
    String urlAddress;

    @Column(name = "individual_taxpayer_number", unique = true, nullable = false, length = 12)
    String individualTaxpayerNumber;

    @Column(name = "bank_card_number", unique = true, nullable = false, length = 16)
    String bankCardNumber;

    @Column(name = "vendor_commission")
    BigDecimal vendorCommission;

    @Column(name = "is_fixed_commission")
    Boolean isFixedCommission;

    @Column(name = "vendor_balance", precision = 19, scale = 4)
    BigDecimal vendorBalance;

    @Column(name = "postal_code", nullable = false, length = 12)
    String postalCode;

    @Column(name = "country", nullable = false)
    String country;

    @Column(name = "region", nullable = false)
    String region;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "street", nullable = false)
    String street;

    @Column(name = "house_number", nullable = false)
    String houseNumber;

    @CollectionTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @ElementCollection
    @Column(name = "text")
    List<String> tags = new ArrayList<>();

    @Column(name = "note", length = 5000)
    String note;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login", nullable = false)
    Date lastLogin;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_date", nullable = false)
    Date registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability")
    Availability availability;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    List<UserGroup> groups = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    List<UserSegment> segments = new ArrayList<>();

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<UserOrderedProduct> orderedProducts = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "vendors_merchandises",
            joinColumns = @JoinColumn(name = "vendor_id")
    )
    @Column(name = "merchandise_id")
    List<Long> vendorMerchandiseIds = new ArrayList<>();

    @JsonIgnore
    @ElementCollection
    @CollectionTable(
            name = "vendors_orders",
            joinColumns = @JoinColumn(name = "vendor_id")
    )
    @Column(name = "order_id")
    List<Long> vendorOrderIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "suppliers_products",
            joinColumns = @JoinColumn(name = "supplier_id")
    )
    @Column(name = "product_id")
    List<Long> supplierProductIds = new ArrayList<>();

    public void removeSegments() {
        this.segments.forEach(this::removeSegment);
    }

    public void removeSegment(UserSegment segment) {
        this.segments.remove(segment);
        segment.getUsers().remove(this);
    }
}
