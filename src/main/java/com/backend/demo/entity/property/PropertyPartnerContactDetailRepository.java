package com.backend.demo.entity.property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyPartnerContactDetailRepository extends JpaRepository<PropertyPartnerContactDetail, String> {

    Optional<PropertyPartnerContactDetail> findByPropertyPartner(String id);

    List<PropertyPartnerContactDetail> findAllByPropertyPartner(String id);
}