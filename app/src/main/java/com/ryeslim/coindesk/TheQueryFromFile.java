package com.ryeslim.coindesk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TheQueryFromFile {
    TheBPI bpi;
    String chartName;
    String disclaimer;
    TheTime time;


    public TheQueryFromFile(TheBPI bpi, String chartName, String disclaimer, TheTime time) {
        this.time = time;
        this.disclaimer = disclaimer;
        this.chartName = chartName;
        this.bpi = bpi;
    }

    public TheQueryFromFile() {
    }

    public TheTime getTime() {
        return time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getChartName() {
        return chartName;
    }

    public TheBPI getBpi() {
        return bpi;
    }

    public void setBpi(TheBPI bpi) {
        this.bpi = bpi;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public void setTime(TheTime time) {
        this.time = time;
    }
}
