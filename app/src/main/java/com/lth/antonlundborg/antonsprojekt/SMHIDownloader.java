package com.lth.antonlundborg.antonsprojekt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;


public class SMHIDownloader {
    public SMHIDownloader(){}

    private JSONObject getJsonData(String parameterKey, String stationKey, String periodName) throws IOException, JSONException {
        return readJsonFromUrl("http://opendata-download-metobs.smhi.se/api" + "/version/latest/parameter/" + parameterKey + "/station/" + stationKey + "/period/" + periodName + "/data.json");
    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        String text = readStringFromUrl(url);
        return new JSONObject(text);
    }


    private String readStringFromUrl(String url) throws IOException {

        InputStream inputStream = new URL(url).openStream();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder stringBuilder = new StringBuilder();
            int cp;
            while ((cp = reader.read()) != -1) {
                stringBuilder.append((char) cp);
            }
            return stringBuilder.toString();
        } finally {
            inputStream.close();
        }
    }

    public double getWindParameter(String parameter, String station, String period) throws JSONException, IOException {
        JSONObject speedData = getJsonData(parameter, station, period);
        JSONArray speedArray = speedData.getJSONArray("value");
        JSONObject windSpeed = speedArray.getJSONObject(0);
        String windSpeedString = windSpeed.getString("value");
        return Double.valueOf(windSpeedString);
    }
}
