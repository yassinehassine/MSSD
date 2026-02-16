package com.mssd.service;

import com.mssd.dto.LoginRequest;
import com.mssd.dto.LoginResponse;
import com.mssd.dto.UserDto;

public interface UserService {
    LoginResponse authenticate(LoginRequest loginRequest);
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto getUserByUsername(String username);
}
