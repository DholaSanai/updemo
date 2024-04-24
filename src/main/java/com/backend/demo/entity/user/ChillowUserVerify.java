package com.backend.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "chillow_user_verify")
@AllArgsConstructor
@NoArgsConstructor
public class ChillowUserVerify {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "phone_verified")
    private boolean phoneVerified;
    @Column(name = "email_verified")
    private boolean emailVerified;
    @Column(name = "id_verified")
    private boolean idVerified;
    @Column(name = "verified_id")
    private String verifiedId;
    @Column(name = "verified_id_token", columnDefinition = "TEXT")
    private String verifiedIdToken;
    @Column(name = "payment_source")
    private String paymentSource;
    @Column(name = "payment_verification_id", columnDefinition = "TEXT")
    private String paymentVerificationId;
    @Column(name = "purchase_id")
    private String purchaseId;
    @Column(name = "payment_date_time")
    private LocalDateTime paymentDateTime;
    @Column(name = "is_payment_done")
    private Boolean isPaymentDone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ChillowUser user;

}