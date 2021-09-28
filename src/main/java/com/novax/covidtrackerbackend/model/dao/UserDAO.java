package com.novax.covidtrackerbackend.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@NoArgsConstructor
@Table(name = "user")
public class UserDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long user_id;

    @Getter
    @Setter
    private String role;

    @Getter
    @Setter
    private String password;

    @Email
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String first_name;

    @Getter
    @Setter
    private String last_name;

    @Getter
    @Setter
    private String nic;

    public UserDAO(Long user_id, String role, String password, String email, String first_name, String last_name, String nic) {
        this.user_id = user_id;
        this.role = role;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.nic = nic;
    }
}
