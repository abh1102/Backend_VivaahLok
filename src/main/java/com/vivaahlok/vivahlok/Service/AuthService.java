package com.vivaahlok.vivahlok.service;

import com.vivaahlok.vivahlok.dto.UserDTO;
import com.vivaahlok.vivahlok.dto.request.*;
import com.vivaahlok.vivahlok.dto.response.AuthResponse;
import com.vivaahlok.vivahlok.dto.response.OtpResponse;
import com.vivaahlok.vivahlok.dto.response.TokenResponse;
import com.vivaahlok.vivahlok.entity.RefreshToken;
import com.vivaahlok.vivahlok.entity.User;
import com.vivaahlok.vivahlok.exception.BadRequestException;
import com.vivaahlok.vivahlok.exception.ResourceNotFoundException;
import com.vivaahlok.vivahlok.repository.RefreshTokenRepository;
import com.vivaahlok.vivahlok.repository.UserRepository;
import com.vivaahlok.vivahlok.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    
    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;
    
    private static final String OTP_PREFIX = "otp:";
    private static final long OTP_EXPIRY_MINUTES = 5;
    
    public OtpResponse sendOtp(SendOtpRequest request) {
        String otp = generateOtp();
        String otpId = UUID.randomUUID().toString();
        
        String key = OTP_PREFIX + request.getPhone();
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(OTP_PREFIX + "id:" + request.getPhone(), otpId, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        
        // In production, send OTP via SMS service
        System.out.println("OTP for " + request.getPhone() + ": " + otp);
        
        return OtpResponse.builder()
                .success(true)
                .message("OTP sent successfully")
                .otpId(otpId)
                .build();
    }
    
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        String key = OTP_PREFIX + request.getPhone();
        String storedOtp = redisTemplate.opsForValue().get(key);
        
        if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
            throw new BadRequestException("Invalid or expired OTP");
        }
        
        redisTemplate.delete(key);
        redisTemplate.delete(OTP_PREFIX + "id:" + request.getPhone());
        
        Optional<User> existingUser = userRepository.findByPhone(request.getPhone());
        boolean isNewUser = existingUser.isEmpty();
        
        User user;
        if (isNewUser) {
            user = User.builder()
                    .phone(request.getPhone())
                    .isVerified(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            user = userRepository.save(user);
        } else {
            user = existingUser.get();
            user.setVerified(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
        
        String token = jwtTokenProvider.generateToken(user.getId());
        String refreshToken = createRefreshToken(user.getId());
        
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(mapToUserDTO(user))
                .isNewUser(isNewUser)
                .build();
    }
    
    public OtpResponse resendOtp(ResendOtpRequest request) {
        String otp = generateOtp();
        String otpId = UUID.randomUUID().toString();
        
        String key = OTP_PREFIX + request.getPhone();
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(OTP_PREFIX + "id:" + request.getPhone(), otpId, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        
        System.out.println("Resent OTP for " + request.getPhone() + ": " + otp);
        
        return OtpResponse.builder()
                .success(true)
                .message("OTP resent successfully")
                .otpId(otpId)
                .build();
    }
    
    public String registerUser(RegisterUserRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User not found. Please verify OTP first."));
        
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCity(request.getCity());
        user.setUpdatedAt(LocalDateTime.now());
        
        user = userRepository.save(user);
        return user.getId();
    }
    
    public void logout(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
    
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
        
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token expired");
        }
        
        String newToken = jwtTokenProvider.generateToken(refreshToken.getUserId());
        String newRefreshToken = createRefreshToken(refreshToken.getUserId());
        
        refreshTokenRepository.delete(refreshToken);
        
        return TokenResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .build();
    }
    
    private String createRefreshToken(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
        
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .createdAt(LocalDateTime.now())
                .build();
        
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
    
    private String generateOtp() {
        return String.format("%06d", (int) (Math.random() * 1000000));
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
