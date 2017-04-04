package edu.gatech.waterapp.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



/**
 * @author Derian
 */

public class Report {

    private Date timestamp;
    private int reportNumber;
    private String reporter;
    private Place location;
    private WaterType waterType;
    private WaterCondition waterCondition;


    public Report() {}

    public Report(Date timestamp, String reporter, Place location) {
        this.timestamp = timestamp;
        this.reporter = reporter;
        this.location = location;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setWaterCondition(WaterCondition waterCondition) {
        this.waterCondition = waterCondition;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
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

    public WaterCondition getWaterCondition() {
        return waterCondition;
    }

    public WaterType getWaterType() {
        return waterType;
    }

    /**
     * Creates a Map representation of the Report information
     * @return a HashMap representation of the Report information
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("reportNumber", reportNumber);
        map.put("reporter", reporter);
        map.put("location", location.toMap());
        map.put("waterCondition", waterCondition.toString());
        map.put("waterType", waterType.toString());
        map.put("timestamp", timestamp);
        return map;
    }
}
