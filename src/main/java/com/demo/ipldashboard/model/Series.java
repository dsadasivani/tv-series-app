package com.demo.ipldashboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Series {
    @Id
    private long serialNumber;
    private String posterLink;
    private String seriesTitle;
    private String runtimeOfSeries;
    private String certificate;
    private String runtimeOfEpisodes;
    private String genre;
    @Column(precision = 1)
    private double imdbRating;
    @Column(length = 1000)
    private String overview;
    private String star1;
    private String star2;
    private String star3;
    private String star4;
    private long noOfVotes;
}
