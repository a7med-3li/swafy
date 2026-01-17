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


    public void sendOtp(String phoneNumber){
        Twilio.init("AC97967ca449c66f56e0ef100d50ebe7ba", "f8150791a58581d17fdfb31dab07b3df");
        Verification.creator(
                verifyServiceSid,
                phoneNumber,
                "sms"
        ).create();
    }

    public boolean verifyOtp(String phoneNumber, String code){
        Twilio.init("AC97967ca449c66f56e0ef100d50ebe7ba", "f8150791a58581d17fdfb31dab07b3df");
        VerificationCheck check =
                VerificationCheck.creator(verifyServiceSid)
                        .setTo(phoneNumber)
                        .setCode(code)
                        .create();

        return "approved".equals(check.getStatus());
    }
}
