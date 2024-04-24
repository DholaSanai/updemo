package com.backend.demo.repository.property;

import com.backend.demo.entity.property.ExternalPropertyPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalPropertyRepository extends JpaRepository<ExternalPropertyPartner, String> {
    Optional<ExternalPropertyPartner> findByUserId(String userId);
}
