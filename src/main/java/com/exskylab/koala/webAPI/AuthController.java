package com.exskylab.koala.webAPI;


import com.exskylab.koala.core.utilities.results.DataResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<DataResult<AuthRegisterResponsetDto>> register(@RequestBody AuthRegisterRequestDto authRegisterRequestDto){




    }



}
