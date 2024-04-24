package com.backend.demo.repository.user;

import com.backend.demo.entity.user.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, String> {
    Optional<OTP> findByPinCodeAndPhoneNumberAndIsConsumedFalseAndIsExpiredFalse(Integer pinCode, String phoneNumber);

    List<OTP> findAllByEmailAndIsExpiredFalse(String email);

    List<OTP> findAllByPhoneNumber(String phoneNumber);

    Optional<OTP> findByPhoneNumber(String phoneNumber);

    Optional<OTP> findByPinCodeAndPhoneNumberAndIsConsumedFalseAndIsExpiredFalse(int pinCode, String phoneNumber);
}
