package com.backend.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class ChillowUserPayment {
    @Id
    @Column(name = "payment_id", nullable = false)
    private String id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "receipt")
    private String receipt;

    @Column(name = "deleted")
    @Where(clause = "deleted = false")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private ChillowUser user;

}
