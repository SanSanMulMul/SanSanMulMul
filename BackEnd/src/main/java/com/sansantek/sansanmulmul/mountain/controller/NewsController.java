package com.sansantek.sansanmulmul.mountain.controller;

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

import com.sansantek.sansanmulmul.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
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
        System.out.println(responseBody); // 네이버가 주는 결과데이터
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "뉴스 정보 요청(랜덤)")
    @GetMapping(value = "/random", produces = "application/json; charset=utf-8")
    public ResponseEntity<?> getRandomNews() {
        // 산 이름들 가져오기
        List<String> mountainNameList = mountainService.getMountainName();

        // 가져온 산 이름 기반으로 랜덤으로 인덱스 값 5개 뽑기
        Random random = new Random();
        Set<String> selectedMountains = new HashSet<>();
        while (selectedMountains.size() < 5) {
            int randomIndex = random.nextInt(mountainNameList.size());
            selectedMountains.add(mountainNameList.get(randomIndex));
        }

        // 뉴스 불러오기
        List<String> newsList = new ArrayList<>();
        for (String mountainName : selectedMountains) {
            String apiURL = "https://openapi.naver.com/v1/search/news?query=" + mountainName + "&display=1"; // JSON 결과

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);
            String responseBody = get(apiURL, requestHeaders);

            newsList.add(responseBody);
        }

        System.out.println("뉴스 불러오기 완료");
        return new ResponseEntity<>(newsList, HttpStatus.OK);
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

}
