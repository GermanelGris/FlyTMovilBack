package com.example.flytbackend.service;

import com.example.flytbackend.controller.dto.AuthResponse;
import com.example.flytbackend.controller.dto.LoginRequest;
import com.example.flytbackend.controller.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}