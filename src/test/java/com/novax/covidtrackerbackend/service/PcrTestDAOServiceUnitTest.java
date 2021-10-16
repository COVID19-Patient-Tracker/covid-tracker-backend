package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddPcrTestRequestDTO;
import com.novax.covidtrackerbackend.repository.PcrTestDAORepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PcrTestDAOServiceUnitTest {

    @Mock private PcrTestDAORepository pcrTestDAORepository;
    private PcrTestDAOService underTest;

    @BeforeEach
    void setUp(){
        underTest = new PcrTestDAOService(pcrTestDAORepository);
    }
    @Test
    void isAbleToAddPcrTest() throws SQLException {
        // when
        AddPcrTestRequestDTO addPcr = new AddPcrTestRequestDTO();
        PcrTestDAO pcr = underTest.addPcrTest(addPcr);
        // then
        verify(pcrTestDAORepository).save(any());
    }

    @Test
    void UnableToUpdatePcrTestStatus() throws SQLException {

        given(pcrTestDAORepository.findById(anyInt()))
                .willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> {
            PcrTestDAO pcrTestDao = underTest.updatePcrTestStatus(4,"ABCDEF");
        }).isInstanceOf(EntityNotFoundException.class).hasMessage("Invalid Test ID Provided");
    }

    @Test
    void isAbleToUpdatePcrTestStatus() throws SQLException {
        PcrTestDAO pcr = new PcrTestDAO(
                4L,
                4,
                new Date(2090-12-12),
                "Negative"
        );
        given(pcrTestDAORepository.findById(anyInt()))
                .willReturn(Optional.of(pcr));

        PcrTestDAO pcrTestDao = underTest.updatePcrTestStatus(4,"ABCDEF");
        verify(pcrTestDAORepository).findById(anyInt());
        verify(pcrTestDAORepository).save(any());
    }
}