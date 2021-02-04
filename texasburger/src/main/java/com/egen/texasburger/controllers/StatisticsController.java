package com.egen.texasburger.controllers;

import com.egen.texasburger.services.ExecutionTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Murtuza
 */

@RestController
@RestControllerAdvice
@RequestMapping(value = "/api/admin/statistics")
@Log4j2
@Api(value = "API Statistics")
public class StatisticsController {

    @Resource
    private ExecutionTimeService executionTimeService;

    @GetMapping("/")
    @ApiOperation(value = "Get All Api Statistics", notes = "Get All Api Statistics.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<?> getAllStatistics(@RequestParam(value = "statId", required = false) String statId) {
        try {
            if (statId == null || statId.equalsIgnoreCase("")) {
                log.info("getting all statistics");
                return new ResponseEntity<>(executionTimeService.getAllStatistics(), HttpStatus.OK);
            } else {
                log.info("getting stats by id");
                return new ResponseEntity<>(executionTimeService.getStatisticsById(Integer.valueOf(statId)), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("nothing found");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/filterStats/{filterBy}/{value}")
    @ApiOperation(value = "Filter Statistics by method/status code", notes = "filterBy: (method/GET,POST,PUT)," +
            " status:(200,201,204,500,404)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<?> getStatsBy(@PathVariable(value = "filterBy") String filterBy,
                                        @PathVariable(value = "value") String value) {
        try {
            if (filterBy.equalsIgnoreCase("status") && !value.isEmpty()) {
                log.info("getting statistics by status");
                return new ResponseEntity<>(executionTimeService.getStatsByStatus(Integer.parseInt(value)),
                        HttpStatus.OK);
            } else if (filterBy.equalsIgnoreCase("method") && !value.isEmpty()) {
                log.info("getting statistics by request methods");
                return new ResponseEntity<>(executionTimeService.getStatsByMethod(value), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.info("nothing found: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
