package com.example.dev.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestApi {

    @GetMapping(value = "/getkobisData")
    public String callAPI() {

        LocalDate now = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        //  String pastDay = now.minusDays(7);
        String formattedNow = now.minusDays(1).format(formatter);

        LocalDate parisNow = LocalDate.now(ZoneId.of("Europe/Paris"));

        System.out.println("parisNow = " + parisNow);

        HashMap<String, Object> result = new HashMap<String, Object>();

        String jsonString = "";

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";

        String targetDt = formattedNow;

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url+"?"+"key=0d106bc79d921f8dc35ed4ba3a471e11&targetDt=" + targetDt).build();

        ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
        result.put("statusCode", resultMap.getStatusCodeValue());
        result.put("header", resultMap.getHeaders());
        result.put("body", resultMap.getBody());

        ObjectMapper mapper = new ObjectMapper();

        try {
            jsonString = mapper.writeValueAsString(resultMap.getBody());
            System.out.println("jsonString = " + resultMap.getBody());
            System.out.println(Charset.defaultCharset().displayName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            //  JSONArray boxOffice = (JSONArray) jsonObject.get("boxOfficeResult");
            System.out.println("jsonObject = " + jsonObject.get("boxOfficeResult"));
            System.out.println("실시간 테스트 테스트");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}