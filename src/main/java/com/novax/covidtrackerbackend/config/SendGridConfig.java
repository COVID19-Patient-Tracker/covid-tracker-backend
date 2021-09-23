package com.novax.covidtrackerbackend.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    private static final String sendGridAPIKey = "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 ";

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
