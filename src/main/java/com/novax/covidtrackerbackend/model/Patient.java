package com.novax.covidtrackerbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long patient_id;

    @NotEmpty(message = "nic cannot be null")
    @Size(min = 10,max = 10,message = "size does not match")
    @Pattern(regexp = "^[0-9]{9}v$",message = "format should be 99999999v {10 digits followed by \"v\"}")
    private String nic;

//    @Transient
    @NotNull(message = "hospital id  cannot be null")
    private int hospital_id;

    @NotEmpty(message = "address cannot be null")
    private String address;

    @NotEmpty(message = "gender cannot be null")
    private String gender;

    @NotEmpty(message = "date of birth does not match")
    private String dob;

    @Nullable
    private int age;

    @NotEmpty(message = "Contact number number cannot be null")
    @Size(min = 10,max = 10,message = "Contact number size is invalid. size should be 10")
    private String contact_no;

    @NotEmpty(message = "first name cannot be null")
    @Size(min = 5,max = 100,message = "size does not match")
    private String first_name;

    @Size(min = 0,max = 100,message = "size does not match")
    private String last_name;

    @Nullable
    private int is_user;

    @Nullable
    private String is_child;

}