package com.pixel.jsoup.charactersearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UserSearch {
    private static final String NexonMapleData_URL = "https://maplestory.nexon.com/Ranking/World/Total?c=";
    private static final String NexonMapleHome_URL = "https://maplestory.nexon.com/";

    public Map<String, String> searchCharacter(String characterName) {
        try {
            // 닉네임 Quoto 변경
            characterName = URLEncoder.encode(characterName, "UTF-8");

            // URL 합치기
            String NexonMapleData_URL_N = NexonMapleData_URL + characterName;

            // 메이플스토리 랭킹 검색 페이지 열기
            URL url = new URL(NexonMapleData_URL_N);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);

            // 메이플스토리 랭킹 검색 페이지 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Jsoup을 사용하여 HTML 파싱
            Document doc = Jsoup.parse(response.toString());

            // 메이플스토리 전적검색 정리 데이터
            Element Find_Maple = doc.selectFirst(".search_com_chk");
            Element Find_Maple_charLink = Find_Maple.selectFirst("td:eq(1) dl dt a");

            // 메이플스토리 전적검색 - 개인 프로필 링크
            String Find_Maple_charLink_Link = NexonMapleHome_URL + Find_Maple_charLink.attr("href");

            // 메이플스토리 전적검색 서버 아이콘
            String Find_Maple_charLink_Severlcon = Find_Maple_charLink.select("img").attr("src");

            // 메이플스토리 전적검색 - 길드 명
            String Find_Maple_GuildName = Find_Maple.select("td:eq(5)").text();

            if (Find_Maple_GuildName.isEmpty()) {
                Find_Maple_GuildName = "- 내용 없음 -";
            }

            // 함수 리턴 값 정보
            Map<String, String> result = new HashMap<>();
            result.put("HomeURL", NexonMapleHome_URL);
            result.put("charLink", Find_Maple_charLink_Link);
            result.put("ServerIcon", Find_Maple_charLink_Severlcon);
            result.put("GuildName", Find_Maple_GuildName);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        UserSearch characterSearch = new UserSearch();
        Map<String, String> characterInfo = characterSearch.searchCharacter("YourCharacterName");

        if (characterInfo != null) {
            System.out.println("HomeURL: " + characterInfo.get("HomeURL"));
            System.out.println("charLink: " + characterInfo.get("charLink"));
            System.out.println("ServerIcon: " + characterInfo.get("ServerIcon"));
            System.out.println("GuildName: " + characterInfo.get("GuildName"));
        } else {
            System.out.println("Character search failed.");
        }
    }
}
