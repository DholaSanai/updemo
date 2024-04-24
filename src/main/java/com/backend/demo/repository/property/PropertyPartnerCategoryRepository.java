package com.backend.demo.repository.property;

import com.backend.demo.entity.property.PropertyPartnerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyPartnerCategoryRepository extends JpaRepository<PropertyPartnerCategory, String> {
    Optional<PropertyPartnerCategory> findById(@NonNull String id);

    List<PropertyPartnerCategory> findAllByIdIn(List<String> propertyPartnerCategories);

    PropertyPartnerCategory findByTypeName(String generalLiving);

    PropertyPartnerCategory findByTypeNameContaining(String generalLiving);

//    List<PropertyPartnerCategory> findByTypeNameContaining(String typeName);

//    PropertyPartnerCategory findByPropertyPartnerId(String id);

//    List<PropertyPartnerCategory> findAllByPropertyPartnerId(String id);
}
