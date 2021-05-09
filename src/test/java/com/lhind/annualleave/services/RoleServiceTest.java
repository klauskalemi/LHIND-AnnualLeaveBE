package com.lhind.annualleave.services;

import com.lhind.annualleave.persistence.models.Role;
import com.lhind.annualleave.persistence.repositories.IRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class RoleServiceTest {
    @Autowired
    private RoleService roleService;
    @MockBean
    private IRoleRepository iRoleRepository;

    @Test
    void testFindAllRolesMethodFromRolesService() {
        Mockito.when(iRoleRepository.findAll()).thenReturn(Stream.of(
                Mockito.mock(Role.class),
                Mockito.mock(Role.class),
                Mockito.mock(Role.class),
                Mockito.mock(Role.class)
        ).collect(Collectors.toList()));

        Assertions.assertEquals(4, roleService.findAll().size());
    }
}
