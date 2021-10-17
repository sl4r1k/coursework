package com.ebuy.userservice.entity;

import com.ebuy.userservice.embedded.Availability;
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
@NamedQuery(
        name = UserGroup.GET_BY_TITLE,
        query = "SELECT group"
                + " FROM UserGroup AS group"
                + " WHERE group.title = :title"
)
@Table(name = "user_groups")
@Entity
public class UserGroup extends IdentifiableEntity {
    public static final String GET_BY_TITLE = "UserGroup.getByTitle";

    @Column(name = "title", unique = true, nullable = false)
    String title;

    @JoinTable(
            name = "user_groups_users",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<User> users = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false)
    Availability availability;
}
