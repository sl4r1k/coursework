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
        name = UserSegment.GET_BY_TITLE,
        query = "SELECT segment"
                + " FROM UserSegment AS segment"
                + " WHERE segment.title = :title"
)
@Table(name = "user_segments")
@Entity
public class UserSegment extends IdentifiableEntity {
    public static final String GET_BY_TITLE = "UserSegment.getByTitle";

    @Column(name = "title", unique = true, nullable = false)
    String title;

    @JoinTable(
            name = "user_segments_users",
            joinColumns = @JoinColumn(name = "segment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<User> users = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false)
    Availability availability;

    @Column(name = "is_every_condition_matched", nullable = false)
    boolean isEveryConditionMatched;

    @OneToMany(mappedBy = "segment", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UserSegmentFilter> filters = new ArrayList<>();

    public void addUsers(List<User> users) {
        users.forEach(this::addUser);
    }

    public void addUser(User user) {
        this.users.add(user);
        user.getSegments().add(this);
    }

    public void removeUsers() {
        this.users.forEach(this::removeUser);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getSegments().remove(this);
    }
}
