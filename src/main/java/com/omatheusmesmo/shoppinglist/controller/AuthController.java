package com.omatheusmesmo.shoppinglist.controller;

import com.omatheusmesmo.shoppinglist.dtos.LoginRequest;
import com.omatheusmesmo.shoppinglist.entity.User;
import com.omatheusmesmo.shoppinglist.service.JwtService;
import com.omatheusmesmo.shoppinglist.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsService userDetailsService;

    @Operation(summary = "Register a User")
    @PostMapping("/register/userDetailsService")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        userService.addUser(user);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "User's login")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){

        UserDetails user = userDetailsService.loadUserByUsername(loginRequest.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
