package com.pixel.jda.bot.util.api;

import com.pixel.jda.bot.BotBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static com.pixel.jda.bot.util.api.ApiEndpoint.*;

public class NexonAPI {

    private static String API_SERVER;
    private static String API_KEY;

    public NexonAPI() {
        Properties properties = new Properties();

        String mapleAPIServer = null;
        String mapleAPIKey = null;

        try (InputStream input = BotBuilder.class.getClassLoader().getResourceAsStream("pixel.properties")) {
            // 리소스에서 속성 로드
            if (input == null) {
                System.err.println("pixel.properties 파일을 찾을 수 없습니다.");

                return;
            }
            properties.load(input);

            mapleAPIServer = properties.getProperty("mapleAPIServer");
            mapleAPIKey = properties.getProperty("mapleAPIKey");

        } catch (IOException e) {
            API_SERVER = null;
            API_KEY = null;
            System.err.println(e.getMessage());
        }

        API_SERVER = mapleAPIServer;
        API_KEY = mapleAPIKey;
    }

    /**
     * API 서버에 연결할 수 있는지 확인하는 메소드
     *
     * @param API_SERVER API 서버의 URL
     * @param API_KEY    API 서버의 API Key
     * @return
     */
    public static boolean mapleAPICheck(final String API_SERVER, final String API_KEY) {
        try {
            String guildName = URLEncoder.encode("픽셀", StandardCharsets.UTF_8);
            String worldName = URLEncoder.encode("리부트2", StandardCharsets.UTF_8);
            String urlString = API_SERVER + GUILD_OCID.getUrl() + "?guild_name=" + guildName + "&world_name=" + worldName;

            URL url = new URL(urlString);

            // HTTP connection 설정
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode = connection.getResponseCode();

            BufferedReader in;
            return responseCode == 200;
        } catch (Exception exception) {
            return false;
        }
    }

    public JSONObject getCharaterInfo() {

        return null;
    }

    public JSONObject getOcid(String characterName) {
        characterName = URLEncoder.encode(characterName, StandardCharsets.UTF_8);

        JSONObject params = new JSONObject();
        params.put("character_name", characterName);

        return sendAPI(CHAR_OCID, params);
    }

    public JSONObject getBasicCharater(String characterName) {

        JSONObject params = new JSONObject();
        params.put("character_name", characterName);

        return sendAPI(CHAR_BASIC, params);
    }

    public String getErrorMessages() {


        return null;
    }

    /**
     * API 서버에 요청을 보내는 공통 메소드
     *
     * @param api    API 서버의 URL
     * @param params API 서버에 전달할 파라미터
     * @return API 서버의 응답
     */
    private JSONObject sendAPI(ApiEndpoint api, JSONObject params) {

        try {
            String urlString = API_SERVER + api.getUrl();

            // params 세팅
            if (params != null && !params.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder("?");
                for (Object key : params.keySet()) {
                    stringBuilder.append((String) key).append("=").append(params.get(key)).append("&");
                }
                // 마지막의 '&' 문자를 제거하여 올바른 쿼리 문자열을 형성합니다.
                urlString += stringBuilder.substring(0, stringBuilder.length() - 1);
            }

            URL url = new URL(urlString);

            // HTTP connection 설정
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode = connection.getResponseCode();

            BufferedReader in;
            if (responseCode == 200) {
                // responseCode 200 정상응답
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                // responseCode 200 이외의 코드가 반환되었을 경우
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuffer tmp = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                tmp.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();

            JSONObject response = (JSONObject) parser.parse(tmp.toString());
            response.put("status", responseCode);

            return response;
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            return null;
        }
    }
}