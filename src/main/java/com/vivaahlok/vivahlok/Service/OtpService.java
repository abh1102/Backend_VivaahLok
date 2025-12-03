package com.vivaahlok.vivahlok.Service;

import org.springframework.beans.factory.annotation.Value;     // ✅ correct import
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;            // ✅ correct Twilio import
import com.twilio.type.PhoneNumber;                          // ✅ correct PhoneNumber

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Value("${twilio.phone_number}")
    private String fromNumber;

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    public void sendOtp(String phoneNumber) {
        String otp = generateOtp();
        otpStore.put(phoneNumber, otp);

        String messageBody = "Your OTP is: " + otp + " (valid for 5 minutes)";

        Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(fromNumber),    // sender (Twilio number)
                messageBody
        ).create();
    }

    public boolean verifyOtp(String phoneNumber, String userOtp) {
        String storedOtp = otpStore.get(phoneNumber);

        if (storedOtp != null && storedOtp.equals(userOtp)) {
            otpStore.remove(phoneNumber);
            return true;
        }

        return false;
    }
}
