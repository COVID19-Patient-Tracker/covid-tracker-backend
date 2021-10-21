package com.novax.covidtrackerbackend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;

import java.util.List;

@Repository
public interface HospitalVisitHistoryRepository extends JpaRepository<HospitalVisitHistory,Long>{
    @Query(value = "SELECT * FROM hospitalvisithistory WHERE patient_id = :patient_id", nativeQuery = true)
    public List<HospitalVisitHistory> findAllHistoriesByPatientId(@Param("patient_id") Long patient_id);

    @Query(value = "SELECT * FROM hospitalvisithistory WHERE patient_id = :patient_id ORDER BY visit_id DESC LIMIT 1", nativeQuery = true)
    public HospitalVisitHistory findNewestRecordByPatientId(@Param("patient_id") Long patient_id);
}
