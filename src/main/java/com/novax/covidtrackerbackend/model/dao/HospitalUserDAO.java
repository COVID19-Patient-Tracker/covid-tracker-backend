package com.novax.covidtrackerbackend.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "hospital_user")
public class HospitalUserDAO {

    @Getter
    @Setter
    @Column(name = "user_id")
    @Id
    private Long id;

    @Getter
    @Setter
    @Column(name = "hospital_id")
    private int hospital;

    @OneToOne(mappedBy = "hospitalusers")
    @MapsId
    @JoinColumn(name = "user_id")
    private UserDAO user;

}
