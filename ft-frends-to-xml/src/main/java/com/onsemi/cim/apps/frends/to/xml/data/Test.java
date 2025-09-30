package com.onsemi.cim.apps.frends.to.xml.data;

/**
 *
 * @author fg7x8c
 */
public class Test {
    
    private String name;
    private String lowSpecLimit;
    private String highSpecLimit;
    private Double lowSpecLimitAsNumber;
    private Double highSpecLimitAsNumber;
    private String unit;
    private Short failCount;
    private Short execCount;

    public Test() {
    }

    public Test(String name, String lowSpecLimit, String highSpecLimit, String unit) {
        this.name = name;
        this.lowSpecLimit = lowSpecLimit;
        this.highSpecLimit = highSpecLimit;
        if(lowSpecLimit != null && !lowSpecLimit.isEmpty()){
            this.lowSpecLimitAsNumber = Double.parseDouble(lowSpecLimit);
        }
        if(highSpecLimit != null && !highSpecLimit.isEmpty()){
            this.highSpecLimitAsNumber = Double.parseDouble(highSpecLimit);
        }
        this.unit = unit;
        this.failCount = 0;
        this.execCount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLowSpecLimit() {
        return lowSpecLimit;
    }

    public void setLowSpecLimit(String lowSpecLimit) {
        this.lowSpecLimit = lowSpecLimit;
    }

    public String getHighSpecLimit() {
        return highSpecLimit;
    }

    public void setHighSpecLimit(String highSpecLimit) {
        this.highSpecLimit = highSpecLimit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setExecCount(Short execCount) {
        this.execCount = execCount;
    }

    public Short getFailCount() {
        return failCount;
    }
    
     public void setFailCount(Short failCount) {
        this.failCount = failCount;
    }
    
    public Short getExecCount() {
        return execCount;
    }

    public Double getLowSpecLimitAsNumber() {
        return lowSpecLimitAsNumber;
    }

    public void setLowSpecLimitAsNumber(Double lowSpecLimitAsNumber) {
        this.lowSpecLimitAsNumber = lowSpecLimitAsNumber;
    }

    public Double getHighSpecLimitAsNumber() {
        return highSpecLimitAsNumber;
    }

    public void setHighSpecLimitAsNumber(Double highSpecLimitAsNumber) {
        this.highSpecLimitAsNumber = highSpecLimitAsNumber;
    }

}
