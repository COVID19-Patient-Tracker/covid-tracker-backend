package com.novax.covidtrackerbackend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfigurer {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}