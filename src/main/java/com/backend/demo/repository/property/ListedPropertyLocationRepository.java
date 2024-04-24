package com.backend.demo.repository.property;

import com.backend.demo.entity.property.ListedPropertyLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListedPropertyLocationRepository extends JpaRepository<ListedPropertyLocation, String> {
  Optional<ListedPropertyLocation> findByLongitudeAndLatitude(Double longitude, Double latitude);

  List<ListedPropertyLocation> findAllByLongitudeAndLatitude(Double longitude, Double latitude);

  Page<ListedPropertyLocation> findAllByListedProperty_OwnerIdContainingAndListedProperty_IsListingDeletedFalseAndListedProperty_ComplexNameIgnoreCaseContainingOrCityIgnoreCaseContaining(String userId, String valueToSearch, String valueToSearch1, Pageable pageable);
}
