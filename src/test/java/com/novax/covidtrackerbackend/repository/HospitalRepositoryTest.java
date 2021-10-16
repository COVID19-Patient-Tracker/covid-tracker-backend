package com.novax.covidtrackerbackend.repository;
import com.novax.covidtrackerbackend.model.Hospital;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HospitalRepositoryTest {

    @Autowired
    private HospitalRepository underTest;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    // adding hospitals to the database
    @Test
    void itShouldCheckIfHospitalSavesCorrectly() {

        // given
        Hospital h = new Hospital();
        h.setAddress("address/address/address");
        h.setCapacity(99);
        h.setTelephone("9999999999");
        h.setName("name");
        h.setHospital_id(99);

        // when
        Hospital saved_h = underTest.save(h);

        // then
        assertThat(saved_h).usingRecursiveComparison().ignoringFields("hospital_id").isEqualTo(h);
    }
}