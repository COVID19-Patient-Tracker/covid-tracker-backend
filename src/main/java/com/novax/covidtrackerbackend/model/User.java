package com.novax.covidtrackerbackend.model;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    @NotEmpty(message = "role cannot be null")
    private String role;

    @Size(min = 8,max = 100,message = "size does not match")
    private String password;

    @NotEmpty(message = "email cannot be null")
    @Email
    private String email;

    @NotEmpty(message = "first name cannot be null")
    @Size(min = 5,max = 100,message = "size does not match")
    private String first_name;

    @Size(min = 0,max = 100,message = "size does not match")
    private String last_name;

    @NotEmpty(message = "nic cannot be null")
    @Size(min = 10,max = 10,message = "size does not match")
    @Pattern(regexp = "^[0-9]{9}v$",message = "format should be 99999999v {10 digits followed by \"v\"}")
    private String nic;

    @Transient
    @Nullable
    private int hospital_id;
}