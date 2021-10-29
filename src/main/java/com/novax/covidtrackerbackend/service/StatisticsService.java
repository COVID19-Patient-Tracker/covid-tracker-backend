package com.novax.covidtrackerbackend.service;


import com.novax.covidtrackerbackend.model.Statistics;
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

    // get all statistics by date in ascending
    public List<Statistics> getAllStatisticsByDateASC(){
        return statisticsRepository.findAll();
    }
}
