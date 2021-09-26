package com.novax.covidtrackerbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class EmailResponseDTO {
    @Getter
    @Setter
    private int statusCode;
    @Getter
    @Setter
    private String body;
    @Getter
    @Setter
    private Map<String, String> headers;
}