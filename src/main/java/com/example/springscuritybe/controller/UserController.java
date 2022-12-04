package com.example.springscuritybe.controller;

import com.example.springscuritybe.dto.LoginDto;
import com.example.springscuritybe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public ResponseEntity<Void> join(HttpServletRequest request){
        userService.join(null);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request, @RequestBody LoginDto loginDto){
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @GetMapping("/test")
    public String getTest(){
        return "aa";
    }
}
