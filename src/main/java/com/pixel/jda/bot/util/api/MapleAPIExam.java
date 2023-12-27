package com.pixel.jda.bot.util.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MapleAPIExam {
    /**
     * 마비노기영웅전 캐릭터명으로 캐릭터 식별자(ocid)를 조회합니다.
     * API 호출에 필요한 API Key 를 발급 받은 후 요청 헤더에 넣어 RESTful API 방식으로 호출합니다.
     * 해당 코드의 경우 응답코드가 200인경우 응답을, 이외의 경우 에러를 출력합니다.
     * */
    public static void main(String[] args) {
        try {
            String API_KEY = "test_1adc182bc9472180a83ac79c8ee8dc448da575bbbf996e60c984c790375b5a83d89fbc514e9b7a2481680aa4e9ae026a";
            String characterName = URLEncoder.encode("Big뭉탱이", StandardCharsets.UTF_8);

            String urlString = "https://open.api.nexon.com/maplestory/v1/id?character_name=" + characterName;
            URL url = new URL(urlString);

            // HTTP connection 설정
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode = connection.getResponseCode();

            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            if(responseCode == 200) {
                // responseCode 200 정상응답
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                // responseCode 200 이외의 코드가 반환되었을 경우
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}