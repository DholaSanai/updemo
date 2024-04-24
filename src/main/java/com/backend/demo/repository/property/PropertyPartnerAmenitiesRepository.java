package com.backend.demo.repository.property;

import com.backend.demo.entity.property.PropertyPartnerAmenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyPartnerAmenitiesRepository extends JpaRepository<PropertyPartnerAmenities, String> {
}
