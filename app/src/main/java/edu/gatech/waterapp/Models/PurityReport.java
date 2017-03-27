package edu.gatech.waterapp.Models;

import java.util.Map;

/**
 * Created by daphnechen on 3/13/17.
 */

public class PurityReport extends Report {

        private float virusCount;
        private float contaminantCount;

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
