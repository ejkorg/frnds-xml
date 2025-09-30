package com.onsemi.cim.apps.frends.to.xml.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author fg7x8c
 */
public class Chip {
    
    private Short hBin;
    private Short sBin;
    private final Map<Short, String> testResults = new LinkedHashMap<>();

    public Chip() {
    }

    public Chip(Short hBin, Short sBin) {
        this.hBin = hBin;
        this.sBin = sBin;
    }

    public Short gethBin() {
        return hBin;
    }

    public void sethBin(Short hBin) {
        this.hBin = hBin;
    }

    public Short getsBin() {
        return sBin;
    }

    public void setsBin(Short sBin) {
        this.sBin = sBin;
    }

    public Map<Short, String> getTestResults() {
        return testResults;
    }

}
