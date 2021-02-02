package com.egen.texasburger.models;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Murtuza
 */


@Entity
@Table(name = "apistatistics")
@Data
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "statsid", nullable = false)
    private Integer statsid;
    private String apipath;
    private String method;
    private Long starttime;
    private Long endtime;
    private Long executiontime;
    private Integer status;

}
