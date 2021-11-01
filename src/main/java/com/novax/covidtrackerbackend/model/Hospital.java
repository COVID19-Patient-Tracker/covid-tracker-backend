package com.novax.covidtrackerbackend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int hospital_id;

    @Unique
    @NotEmpty(message = "name cannot be null")
    private String name;

    @NotEmpty(message = "address cannot be null")
    private String address;

    @NotEmpty(message = "Telephone number cannot be null")
    @Size(min = 10,max = 10,message = "telephone number size is invalid. size should be 10")
    private String telephone;

    public Hospital(int hospital_id, @Unique String name, String address, String telephone, int capacity) {
        this.hospital_id = hospital_id;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.capacity = capacity;
    }


    @NotNull
    @Min(value = 49,message = "There Should be a capacity value")
    private int capacity;

    @ManyToMany
    @JoinTable(
            name = "hospital_user",
            joinColumns = @JoinColumn(name = "hospital_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> hospitalUsers;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy="hospital")
    private List<HospitalVisitHistory> hospitalVisitHistories;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy="hospital")
    private List<CovidPatient> covidPatients;


    public Hospital(@Unique String name, String address, String telephone, int capacity) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.capacity = capacity;
    }
}
