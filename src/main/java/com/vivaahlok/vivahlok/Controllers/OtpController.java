package com.vivaahlok.vivahlok.Controllers;

import com.vivaahlok.vivahlok.Service.OtpService;
import com.vivaahlok.vivahlok.dto.OtpSendRequest;
import com.vivaahlok.vivahlok.dto.OtpVerifyRequest;
import com.vivaahlok.vivahlok.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    // SEND OTP
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody OtpSendRequest request) {

        otpService.sendOtp(request.getPhone());

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "OTP sent successfully");

        return ResponseEntity.ok(response);
    }

    // VERIFY OTP
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {

        boolean isValid = otpService.verifyOtp(request.getPhone(), request.getOtp());

        Map<String, Object> response = new HashMap<>();

        if (isValid) {
            String token = JwtUtil.generateToken(request.getPhone());
            response.put("status", true);
            response.put("token", token);
            response.put("message", "OTP Verified Successfully");
            return ResponseEntity.ok(response);
        }

        response.put("status", false);
        response.put("message", "Invalid OTP");

        return ResponseEntity.badRequest().body(response);
    }
}
