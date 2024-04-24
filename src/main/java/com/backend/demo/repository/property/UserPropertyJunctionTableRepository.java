package com.backend.demo.repository.property;

import com.backend.demo.entity.user.UserPropertyJunctionTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPropertyJunctionTableRepository extends JpaRepository<UserPropertyJunctionTable, String> {
}
