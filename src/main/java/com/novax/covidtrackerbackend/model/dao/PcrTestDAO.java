package com.novax.covidtrackerbackend.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@NoArgsConstructor
@Table(name = "pcrtests")
public class PcrTestDAO {

    @Getter
    @Setter
    @Column(name = "test_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Setter
    private Long patient_id;

    @Getter
    @Setter
    private int hospital_id;

    @Getter
    @Setter
    private Date test_data;

    @Getter
    @Setter
    private String test_result;

    public PcrTestDAO(Long patient_id, int hospital_id, Date test_data, String test_result) {
        this.patient_id = patient_id;
        this.hospital_id = hospital_id;
        this.test_data = test_data;
        this.test_result = test_result;
    }
}