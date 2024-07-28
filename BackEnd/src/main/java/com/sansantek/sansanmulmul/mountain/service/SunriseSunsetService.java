package com.sansantek.sansanmulmul.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class SunriseSunsetService {

    @Value("${sun.api-key}")
    private String apiKey;

    public Map<String, String> getSunriseSunsetTimes(double latitude, double longitude) throws Exception {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String apiUrl = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo";
        String date = now.format(formatter);
        String lat = String.format("%02d%02d", (int) latitude, (int) ((latitude - (int) latitude) * 60));
        String lon = String.format("%03d%02d", (int) longitude, (int) ((longitude - (int) longitude) * 60));

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("locdate", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dnYn", "UTF-8") + "=" + URLEncoder.encode("N", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return parseSunriseSunsetTimes(sb.toString());
    }

    private Map<String, String> parseSunriseSunsetTimes(String xml) throws Exception {
        Map<String, String> times = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new java.io.ByteArrayInputStream(xml.getBytes()));

        NodeList itemList = doc.getElementsByTagName("item");
        if (itemList.getLength() > 0) {
            Element item = (Element) itemList.item(0);
            String sunrise = item.getElementsByTagName("sunrise").item(0).getTextContent().trim();
            String sunset = item.getElementsByTagName("sunset").item(0).getTextContent().trim();

            times.put("sunrise", sunrise);
            times.put("sunset", sunset);
        }

        return times;
    }
}
