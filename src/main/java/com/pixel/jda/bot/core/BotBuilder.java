package com.pixel.jda.bot.core;

import com.pixel.jda.bot.listener.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BotBuilder {

    public static void main(String[] args) throws InterruptedException {

        // 토큰 읽기
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
            System.err.println(e.getMessage());
        }

        // Discord Bot 빌드 및 이벤트 리스너 등록
        JDA jda = JDABuilder.createDefault(token.toString())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .addEventListeners(new ReadyListener(), new CommandListener(), new GuildListener(), new ButtonListener(), new ModalListener(), new AutoCompleteListener())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();
    }
}
