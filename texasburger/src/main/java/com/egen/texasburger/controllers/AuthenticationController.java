package com.egen.texasburger.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Murtuza
 */

@RestController
@RequestMapping("/auth-failed")
public class AuthenticationController {

    @GetMapping
    public ResponseEntity<Object> authFailed() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}