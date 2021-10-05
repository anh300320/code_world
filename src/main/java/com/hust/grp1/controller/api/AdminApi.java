package com.hust.grp1.controller.api;

import com.hust.grp1.dto.ApiResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    @GetMapping("/delete-user/{id}")
    public ApiResponseDto deleteUser(@PathVariable int id) {

        //TODO delete user logic ...
        return null;
    }
}
