package com.novax.covidtrackerbackend.model;

import javax.annotation.Nullable;
import javax.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
public class User {
    public interface WithoutPasswordView {};
    public interface WithoutPasswordViewAndHospitalInfoForHospitalUsers {};

    @JsonView(WithoutPasswordViewAndHospitalInfoForHospitalUsers.class)
    public HashMap<String,Object> getHospitalUserOrAdminDetails() {

        HashMap<String,Object> detailsArr = new HashMap<String,Object>();
        detailsArr.put("email",this.email);
        detailsArr.put("first_name",this.first_name);
        detailsArr.put("last_name",this.last_name);
        detailsArr.put("nic",this.nic);
        detailsArr.put("hospital",this.hospital);
        detailsArr.put("role",this.role);
        detailsArr.put("user_id",this.user_id);
        return detailsArr;

    }

    @JsonView(WithoutPasswordView.class)
    public HashMap<String,Object> getUserDetails() {

        HashMap<String,Object> detailsArr = new HashMap<String,Object>(6);
        detailsArr.put("email",this.email);
        detailsArr.put("first_name",this.first_name);
        detailsArr.put("last_name",this.last_name);
        detailsArr.put("nic",this.nic);
        detailsArr.put("role",this.role);
        detailsArr.put("user_id",this.user_id);
        return detailsArr;

    }

    @ManyToMany(mappedBy = "hospitalUsers")
    private List<Hospital> hospital;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

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

    @NotEmpty(message = "role cannot be null")
    private String role;

    @NotEmpty(message = "nic cannot be null")
    @Size(min = 10,max = 10,message = "size does not match")
    @Pattern(regexp = "^[0-9]{9}v$",message = "format should be 99999999v {10 digits followed by \"v\"}")
    private String nic;

    @Nullable
    @Transient
    private int hospital_id = 0;

    @Nullable
    @Transient
    @Size(min = 8,max = 100,message = "size does not match")
    private String new_password = null;

    public User(Long user_id, String password, String email, String first_name, String last_name, String role, String nic) {
        this.user_id = user_id;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.nic = nic;
    }

    public User(Long user_id, String password, String email, String first_name, String last_name, String role, String nic, int hospital_id) {
        this.user_id = user_id;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.nic = nic;
        this.hospital_id = hospital_id;
    }

}