package com.ryeslim.coindesk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TheTime {

    private String updated;
    private String updatedISO;
    private String updateduk;

    public TheTime(String updated, String updatedISO, String updateduk) {
        this.updated = updated;
        this.updatedISO = updatedISO;
        this.updateduk = updateduk;
    }

    public TheTime() {
    }

    public String getUpdated() {
        return updated;
    }

    public String getUpdatedISO() {
        return updatedISO;
    }

    public String getUpdateduk() {
        return updateduk;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setUpdatedISO(String updatedISO) {
        this.updatedISO = updatedISO;
    }

    public void setUpdateduk(String updateduk) {
        this.updateduk = updateduk;
    }
}
