package com.backend.demo.repository.user;

import com.backend.demo.entity.user.ChillowUserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChillowUserPreferenceRepository extends JpaRepository<ChillowUserPreferences, String> {
}
