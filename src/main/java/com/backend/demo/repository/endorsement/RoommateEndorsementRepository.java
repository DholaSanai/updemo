package com.backend.demo.repository.endorsement;

import com.backend.demo.entity.endorsement.RoommateEndorsement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoommateEndorsementRepository extends JpaRepository<RoommateEndorsement, String> {

}
