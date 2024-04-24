package com.backend.demo.repository.user;

import com.backend.demo.entity.user.ChillowUserVerify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChillowUserVerifyRepository extends JpaRepository<ChillowUserVerify, String> {
    Optional<ChillowUserVerify> findByVerifiedId(String rpRequestId);
//    Optional<ChillowUserVerify> findByBackgroundVerifiedIdToken(String rpRequestId);
//
//    Optional<ChillowUserVerify> findByVerifiedIdToken(String rpRequestId);
}
