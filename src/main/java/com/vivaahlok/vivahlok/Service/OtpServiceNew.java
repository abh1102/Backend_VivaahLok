package com.vivaahlok.vivahlok.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OtpServiceNew {
    
    @Value("${phoneemail.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public void sendOtp(String phoneNumber, String otp) {
        String url = "https://www.phone.email/api/send-otp";
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("apikey", apiKey);
            body.add("number", phoneNumber);
            body.add("otp", otp);
            
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
            
            log.info("Sending OTP to phone: {}", phoneNumber);
            
            String response = restTemplate.postForObject(url, entity, String.class);
            
            log.info("OTP send response: {}", response);
            
        } catch (Exception e) {
            log.error("Error sending OTP: {}", e.getMessage());
        }
    }
}
