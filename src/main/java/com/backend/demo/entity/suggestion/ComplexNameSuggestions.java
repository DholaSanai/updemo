package com.backend.demo.entity.suggestion;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "complex_name_suggestion")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ComplexNameSuggestions {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "complex_name")
    private String complexName;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}
