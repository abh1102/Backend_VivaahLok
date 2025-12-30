package com.vivaahlok.vivahlok.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.vivaahlok.vivahlok.dto.UserDTO;
import com.vivaahlok.vivahlok.dto.response.AuthResponse;
import com.vivaahlok.vivahlok.dto.response.TokenResponse;
import com.vivaahlok.vivahlok.entity.RefreshToken;
import com.vivaahlok.vivahlok.entity.User;
import com.vivaahlok.vivahlok.exception.BadRequestException;
import com.vivaahlok.vivahlok.repository.RefreshTokenRepository;
import com.vivaahlok.vivahlok.repository.UserRepository;
import com.vivaahlok.vivahlok.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FirebaseAuthService firebaseAuthService;
    
    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;
    
    public AuthResponse signup(String firebaseIdToken) {
        try {
            FirebaseToken firebaseToken = firebaseAuthService.verifyToken(firebaseIdToken);
            
            String firebaseUid = firebaseToken.getUid();
            String email = firebaseToken.getEmail();
            String phoneNumber = null; // Firebase tokens may not have phone number
            
            Optional<User> existingUser = userRepository.findByFirebaseUid(firebaseUid);
            if (existingUser.isPresent()) {
                throw new BadRequestException("User already exists. Please login instead.");
            }
            
            User user = User.builder()
                    .firebaseUid(firebaseUid)
                    .email(email)
                    .phone(phoneNumber)
                    .isVerified(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            user = userRepository.save(user);
            
            String token = jwtTokenProvider.generateToken(user.getId());
            String refreshToken = createRefreshToken(user.getId());
            
            return AuthResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .user(mapToUserDTO(user))
                    .isNewUser(true)
                    .build();
            
        } catch (FirebaseAuthException e) {
            throw new BadRequestException("Invalid Firebase token: " + e.getMessage());
        }
    }
    
    public AuthResponse login(String firebaseIdToken) {
        try {
            FirebaseToken firebaseToken = firebaseAuthService.verifyToken(firebaseIdToken);
            
            String firebaseUid = firebaseToken.getUid();
            
            User user = userRepository.findByFirebaseUid(firebaseUid)
                    .orElseThrow(() -> new BadRequestException("User not found. Please signup first."));
            
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            
            String token = jwtTokenProvider.generateToken(user.getId());
            String refreshToken = createRefreshToken(user.getId());
            
            return AuthResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .user(mapToUserDTO(user))
                    .isNewUser(false)
                    .build();
            
        } catch (FirebaseAuthException e) {
            throw new BadRequestException("Invalid Firebase token: " + e.getMessage());
        }
    }
    
    public void logout(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
    
    public TokenResponse refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
        
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token expired");
        }
        
        String newToken = jwtTokenProvider.generateToken(token.getUserId());
        String newRefreshToken = createRefreshToken(token.getUserId());
        
        refreshTokenRepository.delete(token);
        
        return TokenResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .build();
    }
    
    private String createRefreshToken(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
        
        RefreshToken token = RefreshToken.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .createdAt(LocalDateTime.now())
                .build();
        
        token = refreshTokenRepository.save(token);
        return token.getToken();
    }
    
    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .occupation(user.getOccupation())
                .profileImage(user.getProfileImage())
                .build();
    }
}
