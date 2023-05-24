package com.leapfrog.chattest.service;

import com.leapfrog.chattest.dto.*;

public interface AuthService {
    LoginResponse authenticateUser(LoginRequest loginRequest);

    RegisterResponse registerNewUser(RegisterUserRequest registerUserRequest);

    UserProfile markUserAsOnline(ConfirmationRequest confirmationRequest);

    UserProfile markUserAsOffline(ConfirmationRequest confirmationRequest);

    Message logOutUser(ConfirmationRequest confirmationRequest);

    UserProfile getUserProfile();
}
