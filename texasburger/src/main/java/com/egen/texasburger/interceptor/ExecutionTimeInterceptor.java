package com.egen.texasburger.interceptor;

import com.egen.texasburger.models.Statistics;
import com.egen.texasburger.repositories.ApiStatisticsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Murtuza
 */

@Component
@Log4j2
public class ExecutionTimeInterceptor implements HandlerInterceptor {


    @Autowired
    ApiStatisticsRepository apiStatisticsRepository;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        String url = String.valueOf(request.getRequestURL());
        System.out.println(url);
//        String apiPath = url.substring(url.indexOf("api"));
//        System.out.println(apiPath);
        String method = request.getMethod();
        int status = response.getStatus();

        log.info("Request URL: {}", url);
        log.info("Total Time Taken: {} ms", (endTime - startTime));

        Statistics executionStatistics = new Statistics();
        executionStatistics.setApipath(url);
        executionStatistics.setStarttime(startTime);
        executionStatistics.setEndtime(endTime);
        executionStatistics.setExecutiontime(endTime - startTime);
        executionStatistics.setMethod(method);
        executionStatistics.setStatus(status);

        try {
            Statistics _executionStatistics = apiStatisticsRepository.save(executionStatistics);

            log.info("stat created! ID: {}", _executionStatistics.getStatsid());
            if (_executionStatistics != null) {
                log.info("Execution saved in DB!! {}", _executionStatistics);
            } else {
                log.info("Execution time not saved in DB!!");
            }
        } catch (Exception e) {
            log.info("error saving stats: {}", e.getMessage());
        }

    }
}
