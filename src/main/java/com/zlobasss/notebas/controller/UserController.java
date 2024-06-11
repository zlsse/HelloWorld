package com.zlobasss.notebas.controller;

import com.zlobasss.notebas.dto.RegisterForm;
import com.zlobasss.notebas.dto.RegisterResponse;
import com.zlobasss.notebas.security.JwtHelper;
import com.zlobasss.notebas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @GetMapping("/sign-up")
    public String signup(String name, Model model) {
        model.addAttribute("name", name);
        return "auth/sign-up.html";
    }

    @PostMapping(value = "/sign-up", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody RegisterForm form) {
        return userService.create(form);
    }

    @PostMapping(value = "/sign-in", consumes = "application/json")
    public ResponseEntity<?> auth(@RequestBody RegisterForm request) {

        RegisterResponse response = this.doAuthenticate(request.getUsername(), request.getPassword());
        if (response.getMessage().startsWith("Invalid")) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.helper.generateToken(userDetails);
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private RegisterResponse doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);
            return new RegisterResponse("Successfully");
        } catch (Exception e) {
            return new RegisterResponse("Invalid Username or Password!");
        }

    }
}
