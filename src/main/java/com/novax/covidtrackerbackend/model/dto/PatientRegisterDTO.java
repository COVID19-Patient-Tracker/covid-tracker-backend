package com.novax.covidtrackerbackend.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
public class PatientRegisterDTO {
    @Getter
    @Setter
    @NotNull(message = "patient id cannot be null")
    private Long patient_id;

    @Getter
    @Setter
    @NotNull(message = "nic cannot be null")
    private String nic;

    @Getter
    @Setter
    @NotEmpty(message = "email cannot be null")
    @Email
    private String email;

    @Getter
    @Setter
    @Size(min = 8,max = 16,message = "size does not match")
    private String password;
}
