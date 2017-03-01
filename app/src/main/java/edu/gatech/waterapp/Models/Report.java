package edu.gatech.waterapp.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.analytics.internal.zzy.p;
import static com.google.android.gms.cast.internal.zzl.pl;


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


    public Report() {}

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

    public WaterCondition getWaterCondition() {
        return waterCondition;
    }

    public WaterType getWaterType() {
        return waterType;
    }

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
