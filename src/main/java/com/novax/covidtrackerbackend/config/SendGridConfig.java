package com.novax.covidtrackerbackend.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${EMAIL_API_KEY}")
    private String sendGridAPIKey;

    private static SendGrid sendGrid;

    @Bean
    public SendGrid getSendGridClient() {
        if (sendGrid != null) {
            return sendGrid;
        }
        sendGrid = new SendGrid(sendGridAPIKey);
        return sendGrid;
    }
}
