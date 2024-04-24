package com.backend.demo.entity.property;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "listed_property_image")
public class ListedPropertyImage {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "file", length = 4500)
    private String file;

    @Column(name = "deleted")
    @Where(clause = "deleted = false")
    private Boolean isDeleted;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "listed_property_id", updatable = false)
    private ListedProperty listedProperty;
}

