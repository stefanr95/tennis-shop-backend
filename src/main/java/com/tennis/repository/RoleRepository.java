package com.tennis.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tennis.model.ERole;
import com.tennis.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);

}