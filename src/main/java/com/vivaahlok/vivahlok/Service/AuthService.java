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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final OtpServiceNew otpService;
    
    private static final String OTP_PREFIX = "otp:";
    private static final long OTP_EXPIRY_MINUTES = 5;
    
    public OtpResponse sendOtp(SendOtpRequest request) {
        String otpId = UUID.randomUUID().toString();
        String otp = generateOtp();
        
        String key = OTP_PREFIX + otpId;
        String value = request.getPhone() + ":" + otp;
        redisTemplate.opsForValue().set(key, value, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        
        otpService.sendOtp(request.getPhone(), otp);
        
        return OtpResponse.builder()
                .success(true)
                .message("OTP sent successfully")
                .otpId(otpId)
                .build();
    }
    
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        String key = OTP_PREFIX + request.getOtpId();
        String storedValue = redisTemplate.opsForValue().get(key);
        
        if (storedValue == null) {
            throw new BadRequestException("OTP expired or invalid");
        }
        
        String[] parts = storedValue.split(":");
        String storedPhone = parts[0];
        String storedOtp = parts[1];
        
        if (!storedPhone.equals(request.getPhone()) || !storedOtp.equals(request.getOtp())) {
            throw new BadRequestException("Invalid OTP");
        }
        
        redisTemplate.delete(key);
        
        User user = userRepository.findByPhone(request.getPhone()).orElse(null);
        boolean isNewUser = false;
        
        if (user == null) {
            user = User.builder()
                    .phone(request.getPhone())
                    .isVerified(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            user = userRepository.save(user);
            isNewUser = true;
        } else {
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
        String oldKey = OTP_PREFIX + request.getOtpId();
        redisTemplate.delete(oldKey);
        
        String newOtpId = UUID.randomUUID().toString();
        String otp = generateOtp();
        
        String key = OTP_PREFIX + newOtpId;
        String value = request.getPhone() + ":" + otp;
        redisTemplate.opsForValue().set(key, value, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        
        otpService.sendOtp(request.getPhone(), otp);
        
        return OtpResponse.builder()
                .success(true)
                .message("OTP resent successfully")
                .otpId(newOtpId)
                .build();
    }
    
    public String registerUser(RegisterUserRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already registered");
        }
        
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword() != null ? passwordEncoder.encode(request.getPassword()) : null)
                .address(request.getAddress())
                .createdAt(LocalDateTime.now())
                .build();
        
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
                .expiryDate(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();
        
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
    
    private String generateOtp() {
        return String.format("%04d", (int) (Math.random() * 10000));
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
