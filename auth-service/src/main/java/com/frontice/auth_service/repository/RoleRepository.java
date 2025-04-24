package com.frontice.auth_service.repository;

import com.frontice.auth_service.model.Role;
import com.frontice.auth_service.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(RoleName name);

}
