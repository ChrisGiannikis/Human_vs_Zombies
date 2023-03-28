package com.example.human_vs_zombies.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/resources")
@CrossOrigin
public class ResourceController {

    @GetMapping("public")
    public ResponseEntity getPublic() {
        return ResponseEntity.ok("public");
    }

    @GetMapping("authenticated")
    public ResponseEntity getAuthenticated() {
        return ResponseEntity.ok("authenticated");
    }

    @GetMapping("authorized")
    public ResponseEntity getAuthorized() {
        return ResponseEntity.ok("authorized");
    }
}
