package com.btd.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btd.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
