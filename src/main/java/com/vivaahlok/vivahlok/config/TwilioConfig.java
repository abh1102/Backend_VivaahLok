//package com.vivaahlok.vivahlok.config;
//
//import com.twilio.Twilio;
//import org.springframework.beans.factory.annotation.Value;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TwilioConfig {
//
//    @Value("${twilio.account_sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth_token}")
//    private String authToken;
//
//    @PostConstruct
//    public void initTwilio() {
//        Twilio.init(accountSid, authToken);
//        System.out.println("Twilio Initialized Successfully");
//    }
//}
