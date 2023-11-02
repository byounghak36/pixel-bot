package com.pixel.jda.bot;

import com.pixel.jda.listener.MessageListener;
import com.pixel.jda.listener.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BotBuilder {

    public static void main(String[] args) throws InterruptedException {

        // src/main/resources/token 파일을 읽어서 token 변수에 저장
        ClassLoader classLoader = BotBuilder.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("token");

        StringBuilder token = new StringBuilder();

        // token 파일이 없으면 프로그램 종료
        if (inputStream == null) {
            System.out.println("리소스 파일을 찾을 수 없습니다.");
            System.exit(1); // 프로그램 종료
        }

        // token 파일 읽기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                token.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Discord Bot 빌드 및 이벤트 리스너 등록
        JDA jda = JDABuilder.createDefault(token.toString())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new ReadyListener(), new MessageListener())
                .build();
    }
}
