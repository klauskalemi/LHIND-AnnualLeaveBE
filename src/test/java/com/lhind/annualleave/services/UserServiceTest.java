package com.lhind.annualleave.services;

import com.lhind.annualleave.persistence.models.User;
import com.lhind.annualleave.persistence.repositories.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private IUserRepository iUserRepository;

    @Test
    void testFindAllUsersMethodFromUsersService() {
        Mockito.when(iUserRepository.findAll())
                .thenReturn(Stream.of(Mockito.mock(User.class)).collect(Collectors.toList()));
        Assertions.assertEquals(1, userService.findAll().size());
    }

    @Test
    void testSaveMethodFromUsersService() {
        User user = new User();
        user.setPassword("");
        Mockito.when(iUserRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(userService.save(user), user);
    }

    @Test
    void testUpdateMethodFromUsersService() {
        User user = Mockito.mock(User.class);
        Mockito.when(iUserRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(userService.update(user), user);
    }
}
