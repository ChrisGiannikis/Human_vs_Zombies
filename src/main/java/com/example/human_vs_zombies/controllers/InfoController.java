package com.example.human_vs_zombies.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/info")
public class InfoController {

    @GetMapping
    public ResponseEntity getAdmin(@AuthenticationPrincipal Jwt jwt){
        String arrayList = jwt.getClaimAsString("roles");
        return ResponseEntity.ok(arrayList.contains("ADMIN"));
    }
}
