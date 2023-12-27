package com.pixel.jda.bot;

import com.pixel.jda.bot.listener.ButtonListener;
import com.pixel.jda.bot.listener.CommandListener;
import com.pixel.jda.bot.listener.GuildListener;
import com.pixel.jda.bot.listener.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.pixel.jda.bot.util.api.NexonAPI.mapleAPICheck;

public class BotBuilder {

    public static void main(String[] args) {

        Properties properties = new Properties();

        String botToken = null;

        try (InputStream input = BotBuilder.class.getClassLoader().getResourceAsStream("pixel.properties")) {
            // 리소스에서 속성 로드
            if (input == null) {
                System.out.println("pixel.properties 파일을 찾을 수 없습니다.");
                return;
            }
            properties.load(input);

            // 키로 속성 검색
            botToken = properties.getProperty("botToken");

            String mapleAPIServer = properties.getProperty("mapleAPIServer");
            String mapleAPIKey = properties.getProperty("mapleAPIKey");

            if (botToken == null || botToken.isEmpty()) {
                System.out.println("botToken에 대한 값이 없습니다.");
                return;
            }
            if (!mapleAPICheck(mapleAPIServer, mapleAPIKey)) {
                System.out.println("API 서버에 연결할 수 없습니다.");
                return;
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // Discord Bot 빌드 및 이벤트 리스너 등록
        JDA jda = JDABuilder.createDefault(properties.getProperty("botToken"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .addEventListeners(new ReadyListener(), new CommandListener(), new GuildListener(), new ButtonListener())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();
    }
}
