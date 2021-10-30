package com.novax.covidtrackerbackend.service;


import com.novax.covidtrackerbackend.model.HosStatistics;
import com.novax.covidtrackerbackend.model.Statistics;
import com.novax.covidtrackerbackend.repository.HosStatisticsRepository;
import com.novax.covidtrackerbackend.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;
    @Autowired
    private HosStatisticsRepository hosStatisticsRepository;

    // get all statistics by date in ASC
    public List<HosStatistics> getAllHosStatisticsByDateAndIdASC(long id){
        return hosStatisticsRepository.findByhospital_id(id);
    }

    // get all statistics by date in ASC
    public List<Statistics> getAllStatisticsByDateASC(){
        return statisticsRepository.findAll();
    }
}
