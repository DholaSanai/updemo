package com.backend.demo.service.user;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;

@Service
@Slf4j
public class TwillioSmsService implements SmsService {
    @Value("${twillio.account.sid}")
    private String twillioAccountSid;

    @Value("${twillio.auth.token}")
    private String twillioAuthToken;

    @Value("${twillio.sms.service}")
    private String twillioSmsCode;

    @Value("${twillio.phone}")
    private String phone;

    @Override
    public void sendSMS(String recepient, String message) {
        try {
            Twilio.init(twillioAccountSid, twillioAuthToken);
            log.info("Sending message...");
            Message.creator(new PhoneNumber(recepient),
                    new PhoneNumber(phone), message).create();
        }catch(Exception exception){
            throw new ForbiddenException("In-valid phone number!");
        }
        log.info("Message sent to: "+recepient);
    }
}
