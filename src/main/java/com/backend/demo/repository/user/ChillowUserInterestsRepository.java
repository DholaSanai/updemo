package com.backend.demo.repository.user;

import com.backend.demo.entity.user.ChillowUserInterests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChillowUserInterestsRepository extends JpaRepository<ChillowUserInterests, String> {
}
