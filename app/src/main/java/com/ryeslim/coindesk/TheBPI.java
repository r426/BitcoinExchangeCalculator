package com.ryeslim.coindesk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TheBPI {
    TheCurrency USD;
    TheCurrency GBP;
    TheCurrency EUR;

    public TheBPI(TheCurrency USD, TheCurrency GBP, TheCurrency EUR) {
        this.USD = USD;
        this.GBP = GBP;
        this.EUR = EUR;
    }

    public TheBPI() {
    }

    @JsonProperty("USD")
    public TheCurrency USD() {
        return USD;
    }
    @JsonProperty("GBP")
    public TheCurrency GBP() {
        return GBP;
    }
    @JsonProperty("EUR")
    public TheCurrency EUR() {
        return EUR;
    }

    public void setUSD(TheCurrency USD) {
        this.USD = USD;
    }

    public void setGBP(TheCurrency GBP) {
        this.GBP = GBP;
    }

    public void setEUR(TheCurrency EUR) {
        this.EUR = EUR;
    }
}
