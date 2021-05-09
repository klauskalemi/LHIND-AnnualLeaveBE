package com.lhind.annualleave.http.controllers;

import com.lhind.annualleave.http.dto.UserDTO;
import com.lhind.annualleave.persistence.models.User;
import com.lhind.annualleave.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = new ArrayList<>();
        for (User user : userService.findAll()) {
            list.add(convertUserToDTO(user));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/users")
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {
        User user = userService.save(convertDtoToUser(userDTO));
        return new ResponseEntity<>(convertUserToDTO(user), HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/users")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO) {
        Optional<User> userByIdOptional = userService.findUserByID(userDTO.getId());
        if (!userByIdOptional.isPresent()) {
            throw new RuntimeException("Cannot find user to update");
        }
        User userToUpdate = userByIdOptional.get();
        userToUpdate.setUsername(userDTO.getUsername());
        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setRoles(userDTO.getRoles());

        User user = userService.update(userToUpdate);
        return new ResponseEntity<>(convertUserToDTO(user), HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        return userService.delete(id)
                ? new ResponseEntity<>("", HttpStatus.OK)
                : new ResponseEntity<>("", HttpStatus.CONFLICT);
    }

    private UserDTO convertUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setLeaveRequests(user.getLeaveRequests());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

    private User convertDtoToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setLeaveRequests(userDTO.getLeaveRequests());
        user.setRoles(userDTO.getRoles());

        return user;
    }

}
