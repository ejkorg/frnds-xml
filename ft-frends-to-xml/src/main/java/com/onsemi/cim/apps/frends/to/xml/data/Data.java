package com.onsemi.cim.apps.frends.to.xml.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author fg7x8c
 */
public class Data {
    
    private final Map<String, String> metadata = new LinkedHashMap<>();
    private final Map<Coords, Chip> chips = new LinkedHashMap<>();
    private final Map<Short, Short> hBins = new LinkedHashMap<>();
    private final Map<Short, Short> sBins = new LinkedHashMap<>();
    private final Map<Short, Test> tests = new LinkedHashMap<>();

    public Data() {
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Map<Coords, Chip> getChips() {
        return chips;
    }

    public Map<Short, Short> gethBins() {
        return hBins;
    }

    public Map<Short, Short> getsBins() {
        return sBins;
    }

    public Map<Short, Test> getTests() {
        return tests;
    }

}
