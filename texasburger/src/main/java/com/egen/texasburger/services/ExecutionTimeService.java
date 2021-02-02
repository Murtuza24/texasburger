package com.egen.texasburger.services;

import com.egen.texasburger.models.Statistics;
import com.egen.texasburger.repositories.ApiStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Murtuza
 */

@Service(value = "executionTimeService")
public class ExecutionTimeService {

    @Autowired
    ApiStatisticsRepository apiStatisticsRepository;

    public List<Statistics> getAllStatistics() {
        return apiStatisticsRepository.findAll();
    }

    public Statistics saveStatistics(Statistics executionStatistics) {
        return apiStatisticsRepository.save(executionStatistics);
    }

    public List<Statistics> getStatsByStatus(Integer status) {
        return apiStatisticsRepository.getStatsByStatus(status);
    }

    public List<Statistics> getStatsByMethod(String method) {
        return apiStatisticsRepository.getStatsByMethod(method);
    }


}
