package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.repository.HospitalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HospitalServiceUnitTest {

    @Mock private HospitalRepository hospitalRepository;
    private HospitalService underTest;

    @BeforeEach
    void setUp(){
        underTest = new HospitalService(hospitalRepository);
    }

    @Test
    void isAbleToSave() {

        Hospital h = new Hospital(
                "hos_name",
                "No.xxx,abcd Road,abcd.",
                "9999999999",
                90000
        );

        // when
        Hospital hospital = underTest.save(h);

        // then
        verify(hospitalRepository).save(any());
    }

    @Test
    void isAbleToGetAllHospitals() {

        //when
        List<Hospital> hospitals = underTest.getAllHospitals();

        //then
        verify(hospitalRepository).findAll();
    }

    @Test
    void isAbleToGetHospitalById() {

        //when
        Optional<Hospital> hospitals = underTest.getHospitalById(anyInt());

        //then
        verify(hospitalRepository).findById(anyInt());
    }

    @Test
    void isAbleToDeleteHospitalById() {

        //when
        underTest.deleteHospitalById(anyInt());

        //then
        verify(hospitalRepository).deleteById(anyInt());
    }
}