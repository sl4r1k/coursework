package com.ebuy.userservice.entity;

import com.ebuy.userservice.embedded.Privilege;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User role. Represents a set of privileges.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@NamedQuery(
        name = Role.GET_ALL,
        query = "SELECT role"
                + " FROM Role AS role"
)
@NamedQuery(
        name = Role.GET_BY_ID,
        query = "SELECT role"
                + " FROM Role AS role"
                + " WHERE role.id = :id"
)
@Table(name = "roles")
@Entity
public class Role extends IdentifiableEntity {
    public static final String GET_ALL = "Role.getAll";
    public static final String GET_BY_ID = "Role.getById";

    @Column(name = "title", unique = true, nullable = false)
    String title;

    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id")
    )
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Privilege.class)
    @Column(name = "privilege", nullable = false)
    List<Privilege> privileges = new ArrayList<>();
}
