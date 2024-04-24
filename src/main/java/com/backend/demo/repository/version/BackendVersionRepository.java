package com.backend.demo.repository.version;

import com.backend.demo.entity.version.BackendVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BackendVersionRepository extends JpaRepository<BackendVersion, UUID> {

    BackendVersion findFirstByOrderByCreatedAtDesc();

    Page<BackendVersion> findAllByOrderByCreatedAtDesc(Pageable pageable);

    BackendVersion findByServerVersionContaining(String version);
}
