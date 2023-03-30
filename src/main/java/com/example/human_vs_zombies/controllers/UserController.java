package com.example.human_vs_zombies.controllers;

import com.example.human_vs_zombies.dto.player.PlayerDTO;
import com.example.human_vs_zombies.dto.user.UserDTO;
import com.example.human_vs_zombies.entities.AppUser;
import com.example.human_vs_zombies.mappers.PlayerMapper;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Collection;

import static java.util.Objects.isNull;

@RestController
@CrossOrigin
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PlayerMapper playerMapper;

    public UserController(UserService userService, UserMapper userMapper, PlayerMapper playerMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.playerMapper = playerMapper;
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
    @GetMapping//GET: localhost:8080/api/v1/users
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
                            schema = @Schema(implementation = PlayerDTO.class))}),
            @ApiResponse( responseCode = "404",
                    description = "User with supplied id, does not exist! ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("{user_id}")//GET: localhost:8080/api/v1/users/id
    public ResponseEntity findById(@PathVariable String user_id){
        return ResponseEntity.ok( userMapper.UserToUserDTO( userService.findByIdStr(user_id)));
    }

    @GetMapping("player") //GET: localhost:8080/api/v1/users/player
    public ResponseEntity getPlayerByUserId(@AuthenticationPrincipal Jwt jwt){
        String user_id = jwt.getClaimAsString("sub");

        PlayerDTO playerDTO = playerMapper.playerToPlayerSimpleDTO( userService.findByIdStr(user_id).getPlayer());
        return ResponseEntity.ok(playerDTO);
    }

    @Operation(summary = "Creates a new user.")
    @ApiResponses( value = {
            @ApiResponse( responseCode = "201", description = "User created", content = { @Content }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content })
    })
    @PostMapping//POST: localhost:8080/api/v1/users
    public ResponseEntity createUser(@AuthenticationPrincipal Jwt jwt) throws URISyntaxException {
        /*----------------------------------------------------------------------------------------*/
        String user_id = jwt.getClaimAsString("sub");
        String username = jwt.getClaimAsString("preferred_username");
        String first_name = jwt.getClaimAsString("given_name");
        String last_name = jwt.getClaimAsString("family_name");
        AppUser appUser = new AppUser();
        appUser.setKeycloak_id(user_id);
        appUser.setFirst_name(first_name); //username
        appUser.setLast_name(last_name);
        /*----------------------------------------------------------------------------------------*/

        if (isNull(first_name)) //username
            return ResponseEntity.badRequest().build();
        userService.add( appUser);
        return ResponseEntity.ok().build();
    }
}
//
//    @Operation(summary = "Updates the user with the given id.")
//    @ApiResponses( value = {
//            @ApiResponse( responseCode = "204", description = "User updated", content = { @Content }),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content }),
//            @ApiResponse( responseCode = "404", description = "User not found", content = { @Content })
//    })
//    @PutMapping("{user_id}")//PUT: localhost:8080/api/users/id
//    public ResponseEntity updateUserById(@RequestBody UserPutDTO user, @PathVariable int user_id){
//        if (user_id != user.getUser_id())                   //checking if the given id is not name as the given user id
//            return  ResponseEntity.badRequest().build();    //if ids are different returns bad request response
//        if ( isNull( userService.findById(user_id)) )       //checking if the requested mission exists
//            return ResponseEntity.notFound().build();       //it is not exists so return notFound exception
//        userService.update( userMapper.UserPutDTOToUser(user));
//        return ResponseEntity.noContent().build();
//    }
//
//    @Operation(summary = "Deletes the user with the given id.")
//    @ApiResponses(value = {
//            @ApiResponse( responseCode =  "200", description = "User deleted",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = PlayerDTO.class))}),
//            @ApiResponse( responseCode = "404", description = "User with supplied id, does not exist! ",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ProblemDetail.class)))})
//    @DeleteMapping("{user_id}")//DELETE: localhost:8080/api/users/id
//    public ResponseEntity deleteUserById(@PathVariable int user_id){
//        userService.deleteById(user_id);
//        return ResponseEntity.ok("User deleted successfully!");
//    }
//}
