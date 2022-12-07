package com.example.springscuritybe.controller;

import com.example.springscuritybe.dto.LoginDto;
import com.example.springscuritybe.dto.TestDto;
import com.example.springscuritybe.dto.TestEnum;
import com.example.springscuritybe.dto.TestResponse;
import com.example.springscuritybe.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping("/test")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = TestResponse.class))),
    })
    public TestResponse getTest(@RequestBody TestDto testDto){
        TestResponse testResponse = new TestResponse();
        testResponse.setTest(TestEnum.NO);
        return testResponse;
    }
}
