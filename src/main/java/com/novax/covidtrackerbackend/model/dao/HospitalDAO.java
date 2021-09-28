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

@Entity
@NoArgsConstructor
@Table(name = "hospital")
public class HospitalDAO {

    @Column(name = "hospital_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String telephone;

    @Getter
    @Setter
    private int capacity;

    public HospitalDAO(int id, String name, String address, String telephone, int capacity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.capacity = capacity;
    }
}
