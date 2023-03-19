package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.SquadDTO;
import com.example.human_vs_zombies.dto.player.PlayerAdminDTO;
import com.example.human_vs_zombies.dto.player.PlayerSimpleDTO;
import com.example.human_vs_zombies.dto.user.UserDTO;
import com.example.human_vs_zombies.dto.user.UserPostDTO;
import com.example.human_vs_zombies.dto.user.UserPutDTO;
import com.example.human_vs_zombies.mappers.UserMapper;
import com.example.human_vs_zombies.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

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

    @Operation(summary = "Finds all the users of the game.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content( mediaType = "application/json",
                            array = @ArraySchema( schema = @Schema(implementation = UserDTO.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "Did not find any users",
                    content = @Content)
    })
    @GetMapping//GET: localhost:8080/api/users
    public ResponseEntity findAll(){
        Collection<UserDTO> userDTOS = userMapper.UserToUserDTO(userService.findAll());
        if(userDTOS.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok( userDTOS);
    }

    @Operation(summary = "Finds the user with the given id.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content( mediaType = "application/json",
                            schema = @Schema(implementation = PlayerSimpleDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "User with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("{user_id}")//GET: localhost:8080/api/users/id
    public ResponseEntity findById(@PathVariable int user_id){
        return ResponseEntity.ok( userMapper.UserToUserDTO( userService.findById(user_id)));
    }

    @Operation(summary = "Creates a new user.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "201", description = "User created", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content })
    })
    @PostMapping//POST: localhost:8080/api/users
    public ResponseEntity createUser(@RequestBody UserPostDTO user) throws URISyntaxException {
        if (isNull(user.getFirst_name()))
            return ResponseEntity.badRequest().build();
        userService.add( userMapper.UserPostDTOToUser(user));
        int user_id = userMapper.UserPostDTOToUser(user).getUser_id();
        URI uri = new URI("api/users/" + user_id);
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Updates the user with the given id.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "204", description = "User updated", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
            @ApiResponse( responseCode = "404", description = "User not found", content = { @Content })
    })
    @PutMapping("{user_id}")//PUT: localhost:8080/api/users/id
    public ResponseEntity updateUserById(@RequestBody UserPutDTO user, @PathVariable int user_id){
        if (user_id != user.getUser_id())                   //checking if the given id is not name as the given user id
            return  ResponseEntity.badRequest().build();    //if ids are different returns bad request response
        if ( isNull( userService.findById(user_id)) )       //checking if the requested mission exists
            return ResponseEntity.notFound().build();       //it is not exists so return notFound exception
        userService.update( userMapper.UserPutDTOToUser(user));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletes the user with the given id.")
    @ApiResponses(value = {
            @ApiResponse( responseCode =  "200", description = "User deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerSimpleDTO.class))}),
            @ApiResponse( responseCode = "404", description = "User with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))})
    @DeleteMapping("{user_id}")//DELETE: localhost:8080/api/users/id
    public ResponseEntity deleteUserById(@PathVariable int user_id){
        userService.deleteById(user_id);
        return ResponseEntity.ok("User deleted successfully!");
    }
}
