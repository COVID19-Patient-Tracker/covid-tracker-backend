package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.config.SendGridConfig;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendGridEmailServiceTest {

    @Mock
    ModelMapper modelMapper;

    @Spy
    private SendGridConfig sendGridConfig;

    @Value("${ADMIN_EMAIL_ADDRESS}")
    private String adminEmailAddress;

    private SendGridEmailService sendGridEmailService;

    @BeforeEach
    void setUp(){
        sendGridEmailService = new SendGridEmailService();
    }

    @Test
    void sendTextEmail() throws IOException {
    }

    @Test
    void sendHTMLEmail() {
    }
}