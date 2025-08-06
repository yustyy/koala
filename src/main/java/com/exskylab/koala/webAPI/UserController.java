package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<SuccessDataResult<String>> getAllUsers() {
       return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessDataResult<>("çalışıyorum", "Users retrieved successfully", HttpStatus.OK, "/api/users/getAll"));
    }


}
