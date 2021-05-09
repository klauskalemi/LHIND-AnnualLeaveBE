package com.lhind.annualleave.services;

import com.lhind.annualleave.persistence.models.Role;
import com.lhind.annualleave.persistence.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final IRoleRepository iRoleRepository;

    @Autowired
    public RoleService(IRoleRepository iRoleRepository) {
        this.iRoleRepository = iRoleRepository;
    }

    public List<Role> findAll() {
        return iRoleRepository.findAll();
    }
}
