package com.novax.covidtrackerbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "covidpatient")
public class CovidPatient {

    @Id
    private Long patient_id;

    @ManyToOne
    @JoinColumn(name="hospital_id", nullable=false)
    private Hospital hospital;

    private Date verified_date;

    private String patient_status;

}