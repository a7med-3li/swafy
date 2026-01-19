package com.swafy.auth.service;

import com.swafy.user.service.UserService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPService {

    @Value("${twilio.verifyServiceSid}")
    String verifyServiceSid;

    @Value("${twilio.accountSid}")
    String accountSid;

    @Value("${twilio.authToken}")
    String authToken;

    public void sendOtp(String phoneNumber){
        Twilio.init(accountSid, authToken);
        Verification.creator(
                verifyServiceSid,
                phoneNumber,
                "sms"
        ).create();
    }

    public boolean verifyOtp(String phoneNumber, String code){
        Twilio.init(accountSid, authToken);
        VerificationCheck check =
                VerificationCheck.creator(verifyServiceSid)
                        .setTo(phoneNumber)
                        .setCode(code)
                        .create();

        return "approved".equals(check.getStatus());
    }
}
