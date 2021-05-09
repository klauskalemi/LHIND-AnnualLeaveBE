package com.lhind.annualleave.persistence.repositories;

import com.lhind.annualleave.persistence.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
}
