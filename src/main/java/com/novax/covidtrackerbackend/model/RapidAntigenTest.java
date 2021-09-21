package com.novax.covidtrackerbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "rapidantigentest")
public class RapidAntigenTest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int antigen_test_id;
    private Long Patient_id;
    private int hospital_id;
    private Date test_date;
    private String test_result;
}
