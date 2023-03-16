package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.user.UserDTO;
import com.example.human_vs_zombies.dto.user.UserPostDTO;
import com.example.human_vs_zombies.dto.user.UserPutDTO;
import com.example.human_vs_zombies.mappers.UserMapper;
import com.example.human_vs_zombies.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity findAll(){
        return ResponseEntity.ok( userMapper.UserToUserDTO( userService.findAll()));
    }

    @GetMapping("{user_id}")
    public ResponseEntity findById(@PathVariable int user_id){
        return ResponseEntity.ok( userMapper.UserToUserDTO( userService.findById(user_id)));
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserPostDTO user) throws URISyntaxException {
        userService.add( userMapper.UserPostDTOToUser(user));
        URI uri = new URI("api/users" + user.getUser_id());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("{user_id}")
    public ResponseEntity updateUserById(@RequestBody UserPutDTO user, @PathVariable int user_id){
        if (user_id != user.getUser_id())  //checking if the given id is not name as the given player id
            return  ResponseEntity.badRequest().build();  //if ids are different returns bad request response
        userService.update( userMapper.UserPutDTOToUser(user));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{user_id}")
    public ResponseEntity deleteUserById(@PathVariable int user_id){
        //check for foreign keys
        //check if player exists
        if ( isNull( userService.findById(user_id)) )
            return ResponseEntity.notFound().build();
        userService.deleteById(user_id);
        return ResponseEntity.ok("User deleted successfully!");
    }
}
