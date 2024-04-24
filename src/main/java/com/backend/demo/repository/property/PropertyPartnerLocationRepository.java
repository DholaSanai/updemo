package com.backend.demo.repository.property;

import com.backend.demo.entity.property.PropertyPartnerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyPartnerLocationRepository extends JpaRepository<PropertyPartnerLocation, String> {
}
