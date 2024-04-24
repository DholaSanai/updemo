package com.backend.demo.service.contest;

import com.backend.demo.dto.contest.ContestRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContestService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;
    public Boolean submitContestEntry(ContestRequestBody contestRequestBody) {
        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo("marketing@chillowapp.com");
            mailMessage.setText("Story: "+contestRequestBody.getStory()+"\n"
            +"Images links:\n"+contestRequestBody.getImages());
            mailMessage.setSubject("Chillow contest");

            javaMailSender.send(mailMessage);
            System.out.print("Mail Sent Successfully...");
            return true;
        }
        catch (Exception e) {
            System.out.print("Error while Sending Mail");
            return false;
        }

    }

}
