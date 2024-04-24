package com.backend.demo.repository.endorsement;

import com.backend.demo.entity.endorsement.Endorsement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EndorsementRepository extends JpaRepository<Endorsement, String> {

    List<Endorsement> findAllByGivenToUserId(String userId);

    List<Endorsement> findAllByGivenByUserId(String userId);

    Optional<Endorsement> findByGivenByUserIdAndGivenToUserId(String endorsementGivenBy, String endorsementReceiverId);
}
