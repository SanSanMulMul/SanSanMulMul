package com.sansantek.sansanmulmul.mountain.controller.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sansantek.sansanmulmul.mountain.dto.response.NewsResponse;
import com.sansantek.sansanmulmul.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mountain/news")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "뉴스 컨트롤러", description = "네이버 산 뉴스를 3개 크롤링")
@Slf4j
public class NewsController {

    private final MountainService mountainService;
    @Value("${naver.client-id}")
    private String clientId; // 애플리케이션 클라이언트 아이디
    @Value("${naver.client-secret}")
    private String clientSecret; // 애플리케이션 클라이언트 시크릿


    @Operation(summary = "뉴스 정보 요청(사용자 기반)", description = "즐겨찾기 산 기반 네이버 뉴스를 요청")
    @GetMapping(value = "/{keyword}", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> getUserNews(@PathVariable("keyword") String keyword) {
        String text = null;
        try {
            text = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text + "&display=3"; // JSON 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL, requestHeaders);

        System.out.println("뉴스 불러오기 완료");

        List<Map<String, Object>> newsList = new ArrayList<>();

        try {
            // 응답 문자열을 JSON 형식으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>(){});

            NewsResponse news = mountainService.getMountainName(keyword);
            String mountainName = news.getMountainName();
            String mountainImg = news.getMountainImg();

            // 뉴스 항목이 있을 경우
            if (jsonMap.containsKey("items") && jsonMap.get("items") instanceof List) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) jsonMap.get("items");

                for (Map<String, Object> item : items) {
                    item.put("mountainName", mountainName);
                    item.put("mountainImg", mountainImg);
                    if (item.containsKey("title")) {
                        item.put("title", cleanHtml((String) item.get("title")));
                    }
                    if (item.containsKey("description")) {
                        item.put("description", cleanHtml((String) item.get("description")));
                    }
                    if (item.containsKey("originallink")) {
                        item.put("originallink", cleanHtml((String) item.get("originallink")));
                    }
                    if (item.containsKey("link")) {
                        item.put("link", cleanHtml((String) item.get("link")));
                    }
                }
                newsList.addAll(items);
            }

            // 전체 응답을 JSON 배열로 변환
            responseBody = objectMapper.writeValueAsString(newsList);

        } catch (Exception e) {
            throw new RuntimeException("JSON 처리 실패", e);
        }

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }



    @GetMapping(value = "/random", produces = "application/json; charset=utf-8")
    public ResponseEntity<?> getRandomNews() {
        // 산 이름, 이미지들 가져오기
        List<NewsResponse> mountainNameList = mountainService.getMountainName();

        // 가져온 산 이름 기반으로 랜덤으로 인덱스 값 5개 뽑기
        Random random = new Random();
        Set<Integer> selectedIndices = new HashSet<>();
        while (selectedIndices.size() < 5) {
            int randomIndex = random.nextInt(mountainNameList.size());
            selectedIndices.add(randomIndex);
        }

        // 뉴스 불러오기
        List<Map<String, Object>> newsList = new ArrayList<>();
        for (Integer index : selectedIndices) {
            String mountainName = mountainNameList.get(index).getMountainName();
            String mountainImg = mountainNameList.get(index).getMountainImg();

            // URL 인코딩
            String encodedMountainName;
            try {
                encodedMountainName = URLEncoder.encode(mountainName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                encodedMountainName = mountainName; // 인코딩 실패시 원본 사용
                System.err.println("URL 인코딩 오류: " + e.getMessage());
            }

            String apiURL = "https://openapi.naver.com/v1/search/news?query=" + encodedMountainName + "&display=1"; // JSON 결과
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);

            try {
                String responseBody = get(apiURL, requestHeaders);

                // 응답 문자열을 JSON 형식으로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>(){});

                // HTML 태그 제거
                if (jsonMap.containsKey("items") && jsonMap.get("items") instanceof List) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) jsonMap.get("items");
                    for (Map<String, Object> item : items) {
                        item.put("mountainName", mountainName);
                        item.put("mountainImg", mountainImg);
                        if (item.containsKey("title")) {
                            item.put("title", cleanHtml((String) item.get("title")));
                        }
                        if (item.containsKey("description")) {
                            item.put("description", cleanHtml((String) item.get("description")));
                        }
                        if (item.containsKey("originallink")) {
                            item.put("originallink", cleanHtml((String) item.get("originallink")));
                        }
                        if (item.containsKey("link")) {
                            item.put("link", cleanHtml((String) item.get("link")));
                        }
                    }
                    newsList.addAll(items);
                }

            } catch (Exception e) {
                System.err.println("뉴스 요청 오류: " + e.getMessage());
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("error", "뉴스 요청 오류: " + e.getMessage());
                errorMap.put("mountainName", mountainName);
                errorMap.put("mountainImg", mountainImg);
                newsList.add(errorMap);
            }
        }

        // 전체 응답을 JSON 배열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(newsList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }

        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }




    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    private String cleanHtml(String html) {
        // HTML 태그 제거
        String cleanedHtml = Jsoup.clean(html, Safelist.none());
        // HTML 엔티티 디코딩
        cleanedHtml = Jsoup.parse(cleanedHtml).text();
        // 남아있을 수 있는 &quot; 등의 엔티티 추가 처리
        cleanedHtml = cleanedHtml.replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ");
        return cleanedHtml;
    }
}
