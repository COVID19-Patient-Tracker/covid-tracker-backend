package com.novax.covidtrackerbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "ward")
public class Ward {


    @Column(name="hospital_id")
    private int HospitalId;
    @Id
    private int ward_id;
    private String ward_name;

}
