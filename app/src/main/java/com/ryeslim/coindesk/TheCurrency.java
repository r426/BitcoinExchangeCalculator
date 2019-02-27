package com.ryeslim.coindesk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TheCurrency {
    private String code;
    private String symbol;
    private String rate;
    private String description;
    private float rateFloat;


    public TheCurrency(String code, String symbol, String rate, String description, float rateFloat) {
        this.code = code;
        this.symbol = symbol;
        this.rate = rate;
        this.description = description;
        this.rateFloat = rateFloat;
    }

    public TheCurrency() {
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    public float getRateFloat() {
        return rateFloat;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRateFloat(float rateFloat) {
        this.rateFloat = rateFloat;
    }
}
