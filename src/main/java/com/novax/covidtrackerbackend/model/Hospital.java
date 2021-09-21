package com.novax.covidtrackerbackend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int hospital_id;

    @NotEmpty(message = "name cannot be null")
    private String name;

    @NotEmpty(message = "address cannot be null")
    private String address;

    @NotEmpty(message = "Telephone number cannot be null")
    @Size(min = 10,max = 10,message = "telephone number size is invalid. size should be 10")
    private String telephone;

    @NotNull
    private int capacity;

}