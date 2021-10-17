package com.ebuy.userservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "id")
@MappedSuperclass
public class IdentifiableEntity {
    @GeneratedValue(generator = "increment")
    @Id
    @Column(name = "id")
    private Long id;
}
