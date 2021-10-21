package com.novax.covidtrackerbackend.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "hospitalvisithistory")
public class HospitalVisitHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long visit_id;
    private Long patient_id;

    @ManyToOne
    @JoinColumn(name="hospital_id", nullable=false)
    private Hospital hospital;

    private Date visit_date;

    private int ward_id;

    private String data;

    private String visit_status;

}
