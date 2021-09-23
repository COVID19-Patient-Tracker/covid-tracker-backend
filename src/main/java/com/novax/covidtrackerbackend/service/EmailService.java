package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dto.EmailResponseDTO;

import java.io.IOException;
import java.util.Map;

public interface EmailService {
    EmailResponseDTO sendTextEmail(String from, String to, String templateId, Map<String, String> dynamic_data) throws IOException;

    EmailResponseDTO sendHTMLEmail(String from, String to, String templateId, Map<String, String> dynamic_data) throws IOException;
}
