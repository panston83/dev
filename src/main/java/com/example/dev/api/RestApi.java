package com.example.dev.api;

import com.example.dev.endtity.BoxOfficeRepository;
import com.example.dev.endtity.Boxoffice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

@RestController
public class RestApi {

    @Autowired
    private BoxOfficeRepository boxOfficeRepository;

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
           // jsonString = mapper.writeValueAsString(resultMap.getBody());
          //  System.out.println("jsonString = " + resultMap.getBody());
            System.out.println(Charset.defaultCharset().displayName());

            JSONParser jsonParser = new JSONParser();
            // JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            //  JSONArray boxOffice = (JSONArray) jsonObject.get("boxOfficeResult");

            LinkedHashMap boxOfficeResult = (LinkedHashMap) resultMap.getBody().get("boxOfficeResult");

            System.out.println(boxOfficeResult);

            ArrayList<Map> dayList = (ArrayList<Map>) boxOfficeResult.get("dailyBoxOfficeList");

            LinkedHashMap mDayList = new LinkedHashMap<>();

            System.out.println(dayList);

            for (Map obj : dayList) {
                mDayList.put(obj.get("rnum"),obj.get("movieNm"));

                Boxoffice boxoffice = Boxoffice.builder()
                        .rank(Integer.parseInt((String) obj.get("rnum")))
                        .name(String.valueOf(obj.get("movieNm")))
                        .build();

                boxOfficeRepository.save(boxoffice);
            }
            ObjectMapper movieMapper = new ObjectMapper();
            jsonString = movieMapper.writeValueAsString(mDayList);

            System.out.println("데이터 = " + jsonString);

          // System.out.println("jsonObject = " + jsonObject.get("boxOfficeResult"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}