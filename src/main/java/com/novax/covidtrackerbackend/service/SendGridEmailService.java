package com.novax.covidtrackerbackend.service;

import com.sendgrid.*;
import com.novax.covidtrackerbackend.config.SendGridConfig;
import com.novax.covidtrackerbackend.model.dto.EmailResponseDTO;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import java.io.IOException;

@Service
public class SendGridEmailService implements EmailService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private SendGridConfig sendGridConfig;

    @Autowired
    EmailService emailService;

    /*@Value("${ADMIN_EMAIL_ADDRESS}")
    private String adminEmailAddress;*/
    private static final String adminEmailAddress = "team.novax18@gmail.com";

    @Override
    public EmailResponseDTO sendTextEmail(String from, String to, String templateId, Map<String, String> dynamic_data) throws IOException {
        Response response = sendEmail(from, to, templateId, dynamic_data);
        return convertToResponse(response);
    }

    @Override
    public EmailResponseDTO sendHTMLEmail(String from, String to, String templateId, Map<String, String> dynamic_data) throws IOException {
        Response response = sendEmail(from, to, templateId, dynamic_data);
        return convertToResponse(response);
    }

    private Response sendEmail(String from, String to, String templateId, Map<String, String> dynamic_data) throws IOException {
        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);

        Mail mail = new Mail();
        mail.setTemplateId(templateId);
        mail.setFrom(fromEmail);
        DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
        personalization.addTo(toEmail);
        personalization.addDynamicTemplateData(dynamic_data);
        mail.addPersonalization(personalization);

        SendGrid sg = this.sendGridConfig.getSendGridClient();
        Request request = new Request();
        Response response;
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response = sg.api(request);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return response;
    }

    private static class DynamicTemplatePersonalization extends Personalization {

        @JsonProperty(value = "dynamic_template_data")
        private Map<String, String> dynamic_template_data;

        @JsonProperty("dynamic_template_data")
        public Map<String, String> getDynamicTemplateData() {
            if (dynamic_template_data == null) {
                return Collections.<String, String>emptyMap();
            }
            return dynamic_template_data;
        }

        public void addDynamicTemplateData(Map<String, String> dynamic_data) {
            for (Map.Entry<String, String> set :
                    dynamic_data.entrySet()) {
                if (dynamic_template_data == null) {
                    dynamic_template_data = new HashMap<String, String>();
                    dynamic_template_data.put(set.getKey(), set.getValue());
                } else {
                    dynamic_template_data.put(set.getKey(), set.getValue());
                }
            }
        }

    }

    private EmailResponseDTO convertToResponse(Response response) {
        return modelMapper.map(response, EmailResponseDTO.class);
    }

}
