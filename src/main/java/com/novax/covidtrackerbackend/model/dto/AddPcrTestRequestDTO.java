package com.novax.covidtrackerbackend.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
public class AddPcrTestRequestDTO {

    @Getter
    @Setter
    @NotNull(message = "patient id cannot be null")
    private Long Patient_id;

    @Getter
    @Setter
    @NotNull(message = "hospital id cannot be null")
    private int hospital_id;

    @Getter
    @Setter
    @NotNull
    private Date test_data;

    @Getter
    @Setter
    @NotEmpty(message = "test result cannot be null")
    private String test_result;

}
