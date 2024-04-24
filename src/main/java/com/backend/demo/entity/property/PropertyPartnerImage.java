package com.backend.demo.entity.property;

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
@Table(name = "property_partner_image")
public class PropertyPartnerImage {
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_partner_id", updatable = false)
    private PropertyPartner propertyPartner;
}

