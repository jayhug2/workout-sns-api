package com.workout.api.dto;


import com.workout.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResult {

    private User user;
    private String token;
}
