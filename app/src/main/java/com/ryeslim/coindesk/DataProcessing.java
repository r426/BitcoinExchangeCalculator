package com.ryeslim.coindesk;


import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.text.Html;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;


public class DataProcessing {

    private Context context;

    private float rateFloat[];
    final int NUMBER_OF_CURRENCIES = 3;

    private String rate1;
    private String rate2;
    private String rate3;
    private String chartName;
    private String localTime;

    public String getLocalTime() {
        return localTime;
    }

    public String getChartName() {
        return chartName;
    }

    public String getRate1() {
        return rate1;
    }

    public String getRate2() {
        return rate2;
    }

    public String getRate3() {
        return rate3;
    }

    public float[] getRateFloat() {
        return rateFloat;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private static DataProcessing instance = null;

    public static DataProcessing getInstance() {
        if (instance == null) {
            instance = new DataProcessing();
        }
        return instance;
    }

    private DataProcessing() {
    }

    private RequestQueue loadingQue;

    public void setLoadingQue(RequestQueue loadingQue) {
        this.loadingQue = loadingQue;
        JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.GET, "https://api.coindesk.com/v1/bpi/currentprice.json",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() > 0) {
                            try {
                                JSONObject jsonObject1 = response.getJSONObject("time");

                                TheTime theTime = new TheTime(jsonObject1.getString("updated"),
                                        jsonObject1.getString("updatedISO"), jsonObject1.getString("updateduk"));

                                String theDisclaimer = response.getString("disclaimer");
                                String theChartName = response.getString("chartName");

                                JSONObject jsonObject2 = response.getJSONObject("bpi");

                                TheCurrency dollarUS = new TheCurrency(jsonObject2.getJSONObject("USD").getString("code"),
                                        jsonObject2.getJSONObject("USD").getString("symbol"),
                                        jsonObject2.getJSONObject("USD").getString("rate"),
                                        jsonObject2.getJSONObject("USD").getString("description"),
                                        toFloat(jsonObject2.getJSONObject("USD").getString("rate")));

                                TheCurrency poundUK = new TheCurrency(jsonObject2.getJSONObject("GBP").getString("code"),
                                        jsonObject2.getJSONObject("GBP").getString("symbol"),
                                        jsonObject2.getJSONObject("GBP").getString("rate"),
                                        jsonObject2.getJSONObject("GBP").getString("description"),
                                        toFloat(jsonObject2.getJSONObject("GBP").getString("rate")));

                                TheCurrency euro = new TheCurrency(jsonObject2.getJSONObject("EUR").getString("code"),
                                        jsonObject2.getJSONObject("EUR").getString("symbol"),
                                        jsonObject2.getJSONObject("EUR").getString("rate"),
                                        jsonObject2.getJSONObject("EUR").getString("description"),
                                        toFloat(jsonObject2.getJSONObject("EUR").getString("rate")));

                                TheBPI bpi = new TheBPI(dollarUS, poundUK, euro);
                                TheQuery theQuery = new TheQuery(theTime, theDisclaimer, theChartName, bpi);

                                writeToFile(theQuery);

                                chartName = theChartName;
                                localTime = localTime(theTime.getUpdated());
                                rate1 = theLineToShow(dollarUS);
                                rate2 = theLineToShow(poundUK);
                                rate3 = theLineToShow(euro);
                                rateFloat = new float[NUMBER_OF_CURRENCIES];
                                rateFloat[0] = toFloat(dollarUS.getRate());
                                rateFloat[1] = toFloat(poundUK.getRate());
                                rateFloat[2] = toFloat(euro.getRate());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        MainActivity.getInstance().goForward();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
        );
        loadingQue.add(arrReq);
    }

    public void writeToFile(TheQuery theQuery) {

        ObjectMapper mapper = new ObjectMapper();

        String querySerialized = null;
        try {
            querySerialized = mapper.writeValueAsString(theQuery);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(querySerialized);

        try {
            String fileName = "query.json";
            System.out.println(querySerialized);
            mapper.writeValue(new File(context.getFilesDir(), fileName), querySerialized);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile() {

        ObjectMapper mapper1 = new ObjectMapper();
        TheQueryFromFile theQueryFromFile = null;

        String fileName = "query.json";
        FileReader reader = null;
        try {
            reader = new FileReader(new File(context.getFilesDir(), fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(reader);
        String json = sc.nextLine();
        System.out.println(json);
        String json2 = json.substring(1, json.length() - 1).replaceAll("\\\\", "");
        System.out.println(json2);
        try {
            theQueryFromFile = mapper1.readValue(json2, TheQueryFromFile.class);
            System.out.println(theQueryFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        chartName = theQueryFromFile.getChartName();
        localTime = localTime(theQueryFromFile.time.getUpdated());
        rate1 = theLineToShow(theQueryFromFile.bpi.USD);
        rate2 = theLineToShow(theQueryFromFile.bpi.GBP);
        rate3 = theLineToShow(theQueryFromFile.bpi.EUR);
        rateFloat = new float[NUMBER_OF_CURRENCIES];
        rateFloat[0] = toFloat(theQueryFromFile.bpi.USD.getRate());
        rateFloat[1] = toFloat(theQueryFromFile.bpi.GBP.getRate());
        rateFloat[2] = toFloat(theQueryFromFile.bpi.EUR.getRate());

        MainActivity.getInstance().goForward();
    }

    public String localTime(String updated) {

        String localTime = "";

        TimeZone defaultTimeZone = TimeZone.getDefault();
        String strDefaultTimeZone = defaultTimeZone.getDisplayName(false, TimeZone.SHORT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(strDefaultTimeZone));

        try {
            localTime = simpleDateFormat.format(simpleDateFormat.parse(updated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localTime;
    }

    public float toFloat(String rate) {
        String rateString = rate.replaceAll(",", "");
        return Float.parseFloat(rateString);
    }

    public String theLineToShow(TheCurrency currencyObject) {
        String theLineToShow = "";
        String symbolToShow = Html.fromHtml(currencyObject.getSymbol()).toString();
        theLineToShow = symbolToShow + ": " + currencyObject.getRateFloat();
        return theLineToShow;
    }
}