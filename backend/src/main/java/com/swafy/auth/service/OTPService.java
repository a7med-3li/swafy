package com.swafy.auth.service;

import com.swafy.user.service.UserService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPService {

    String verifyServiceSid="";
    public void sendOtp(String phoneNumber){
        Twilio.init("", "");
        Verification.creator(
                verifyServiceSid,
                phoneNumber,
                "sms"
        ).create();
    }

    public boolean verifyOtp(String phoneNumber, String code){
        Twilio.init("", "");
        VerificationCheck check =
                VerificationCheck.creator(verifyServiceSid)
                        .setTo(phoneNumber)
                        .setCode(code)
                        .create();

        return "approved".equals(check.getStatus());
    }
}
