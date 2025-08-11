package com.mssd.service.impl;

import com.mssd.dto.LoginRequest;
import com.mssd.dto.LoginResponse;
import com.mssd.dto.UserDto;
import com.mssd.mapper.UserMapper;
import com.mssd.model.User;
import com.mssd.repository.UserRepository;
import com.mssd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In a real application, you should use BCrypt to hash and verify passwords
            if (user.getPassword().equals(loginRequest.getPassword())) {
                UserDto userDto = userMapper.toDto(user);
                // Don't return password in response
                userDto.setPassword(null);
                
                return LoginResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .user(userDto)
                    .build();
            }
        }
        
        return LoginResponse.builder()
            .success(false)
            .message("Invalid email or password")
            .build();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername()) || 
            userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        UserDto result = userMapper.toDto(savedUser);
        result.setPassword(null); // Don't return password
        return result;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto result = userMapper.toDto(user);
        result.setPassword(null);
        return result;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto result = userMapper.toDto(user);
        result.setPassword(null);
        return result;
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto result = userMapper.toDto(user);
        result.setPassword(null);
        return result;
    }
}
