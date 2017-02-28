package edu.gatech.waterapp.Models;

import java.util.Date;


/**
 * Created by Derian on 2/27/2017.
 */

public class Report {

    private Date timestamp;
    private static int nextReportNum = 1;
    private int reportNumber;
    private String reporter;
    private Place location;
    private WaterType waterType;
    private WaterCondition waterCondition;



    public Report(Date timestamp, String reporter, Place location) {
        this.timestamp = timestamp;
        this.reporter = reporter;
        this.location = location;
        reportNumber = nextReportNum;
        nextReportNum++;
    }

    public void setWaterCondition(WaterCondition waterCondition) {
        this.waterCondition = waterCondition;
    }

    public void setWaterType(WaterType waterType) {
        this.waterType = waterType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public Place getLocation() {
        return location;
    }

    public String getReporter() {
        return reporter;
    }


}
