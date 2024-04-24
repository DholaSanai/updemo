package com.backend.demo.repository.property;

import com.backend.demo.entity.property.UserPreferredPropertyPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPrefferedPropertyPartnerRepository extends JpaRepository<UserPreferredPropertyPartner, String> {

    List<UserPreferredPropertyPartner> findByUserIdContainingAndIsPreferenceDeletedFalse(String userId);

    @Query(value = "Select u FROM UserPreferredPropertyPartner u WHERE u.userId = :userid ")
    List<UserPreferredPropertyPartner> findByUser(@Param("userid") String userId);

}
