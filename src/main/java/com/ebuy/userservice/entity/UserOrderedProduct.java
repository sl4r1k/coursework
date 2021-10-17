package com.ebuy.userservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "user_ordered_products")
@Entity
public class UserOrderedProduct extends IdentifiableEntity {
    @Column(name = "product_id", nullable = false)
    Long productId;

    @Column(name = "price", precision = 19, scale = 4)
    BigDecimal price;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    Date date;
}
