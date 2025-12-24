package com.vivaahlok.vivahlok.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {

    @Value("${phoneemail.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // You can skip generating OTP here because phone.email sends OTP automatically
    public void sendOtp(String phoneNumber) {

        String url = "https://www.phone.email/api/send-otp";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("apikey", apiKey);
            body.add("number", phoneNumber);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            System.out.println("📤 SENDING REQUEST TO phone.email ... " + body);

            String response = restTemplate.postForObject(url, entity, String.class);

            System.out.println("📥 PHONE EMAIL SEND RESPONSE = " + response);

        } catch (Exception e) {
            System.out.println("❌ PHONE EMAIL SEND ERROR = " + e.getMessage());
            e.printStackTrace();  // 🔥 print full error
        }
    }


    public boolean verifyOtp(String phoneNumber, String otp) {

        String url = "https://phone.email/api/verify-otp";

        Map<String, String> body = new HashMap<>();
        body.put("apikey", apiKey);
        body.put("number", phoneNumber);
        body.put("otp", otp);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        String response = restTemplate.postForObject(url, entity, String.class);

        // Phone.Email returns: {"status":true} or {"status":false}
        return response != null && response.contains("\"status\":true");
    }
}
