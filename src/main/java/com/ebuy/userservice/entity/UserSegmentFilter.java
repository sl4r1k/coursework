package com.ebuy.userservice.entity;

import com.ebuy.userservice.embedded.FilterCondition;
import com.ebuy.userservice.embedded.UserField;
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
@Table(name = "user_segment_filters")
@Entity
public class UserSegmentFilter extends IdentifiableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    UserSegment segment;

    @Enumerated(EnumType.STRING)
    @Column(name = "field", nullable = false)
    UserField field;

    @CollectionTable(
            name = "user_segment_filter_values",
            joinColumns = @JoinColumn(name = "filter_id")
    )
    @ElementCollection
    @Column(name = "value")
    List<String> values = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false)
    FilterCondition condition;

    @JoinColumn(name = "filter_id")
    @OneToMany(fetch = FetchType.LAZY)
    List<UserSegmentFilter> filters = new ArrayList<>();
}
