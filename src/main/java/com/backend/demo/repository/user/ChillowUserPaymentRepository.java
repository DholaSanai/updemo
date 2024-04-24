package com.backend.demo.repository.user;

import com.backend.demo.entity.user.ChillowUserPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChillowUserPaymentRepository extends JpaRepository<ChillowUserPayment, String> {
}
