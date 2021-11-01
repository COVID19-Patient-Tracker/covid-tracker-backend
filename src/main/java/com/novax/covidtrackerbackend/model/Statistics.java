package com.novax.covidtrackerbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long total_pcrs;
    private long total_pcrs_pending;
    private long total_pcrs_positive;
    private long total_pcrs_negative;
    private Date date;
    private long total_antigens;
    private long total_antigens_pending;
    private long total_antigens_positive;
    private long total_antigens_negative;
    private long total_covid_patients;
    private long total_deaths;
    private long total_actives;
    private long total_recovered;
    private long total_positives;

}
