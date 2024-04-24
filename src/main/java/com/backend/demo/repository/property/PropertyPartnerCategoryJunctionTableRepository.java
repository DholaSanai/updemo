package com.backend.demo.repository.property;

import com.backend.demo.entity.property.PropertyPartnerCategoryJunctionTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyPartnerCategoryJunctionTableRepository extends JpaRepository<PropertyPartnerCategoryJunctionTable, String> {
    Optional<PropertyPartnerCategoryJunctionTable> findById(@NonNull String id);

    List<PropertyPartnerCategoryJunctionTable> findAllByIdIn(List<String> propertyPartnerCategories);

    List<PropertyPartnerCategoryJunctionTable> findAllByPropertyPartnerId(String id);
}
