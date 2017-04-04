package edu.gatech.waterapp.Models;

import java.util.Date;
import java.util.Map;

/**
 * @author Daphne
 */

public class PurityReport extends Report {

        private float virusCount;
        private float contaminantCount;

    public PurityReport(Date d, String s, Place p) {
        super(d, s, p);
    }

    public float getVirusCount() {
        return virusCount;
    }

    public void setVirusCount(float virusCount) {
        this.virusCount = virusCount;
    }

    public float getContaminantCount() {
        return contaminantCount;
    }

    public void setContaminantCount(float contaminantCount) {
        this.contaminantCount = contaminantCount;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> m = super.toMap();
        m.put("virusCount", virusCount);
        m.put("contaminantCount", contaminantCount);
        return m;
    }
}
