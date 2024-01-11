package com.pixel.jda.bot.listener;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

import static com.pixel.jda.bot.listener.CommandList.*;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        // 커맨드 구성
        commandConfigure(event);
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        // 서버 별 권한 정보
        List<Role> roles = event.getGuild().getRoles();

        event.getGuild().loadMembers().onSuccess(success -> {
            success.forEach(member -> {
                System.out.println(member.getEffectiveName());
            });
        }).onError(error -> {
            System.out.println("멤버 로드 실패");
        });
    }

    private void commandConfigure(ReadyEvent event) {
        // 커맨드 구성
        event.getJDA().updateCommands().addCommands(

                // /길드가입 [메이플닉네임]
                Commands.slash(GUILD_JOIN, "길드 가입을 하기 위한 명령어 입니다.")
                        .addOptions(
                                new OptionData(OptionType.STRING, CHARACTER_NAME, "메이플 닉네임", true)
                        ),
                // /유저정보입력 [유저] [태어난연도] [이전길드명] [길드탈퇴사유] [플레이시간]
                Commands.slash(INSERT_USER_INFO, "가입 유저에 대한 정보입력 명령어 입니다.")
                        .addOptions(
                                new OptionData(OptionType.USER, DISCORD_USER, "유저", true),
                                new OptionData(OptionType.INTEGER, BIRTH_YEAR, "태어난 연도", true),
                                new OptionData(OptionType.STRING, PRE_GUILD_NAME, "이전 길드명", true)
                                        .setMaxLength(12),
                                new OptionData(OptionType.STRING, PRE_GUILD_LEAVE_REASON, "길드 탈퇴 사유", true)
                                        .setMaxLength(30),
                                new OptionData(OptionType.STRING, PLAY_TIME, "플레이 시간대", false)
                        ),
                // /캐릭터추가 [길드] [메이플닉네임]
                Commands.slash(ADD_CHARACTER, "길드에 캐릭터를 추가하기 위한 명령어 입니다.")
                        .addOptions(
                                new OptionData(OptionType.STRING, GUILD_NAME, "추가할 길드", true)
                                        .addChoice("픽셀", "픽셀")
                                        .addChoice("PIXEL", "PIXEL"),
                                new OptionData(OptionType.STRING, CHARACTER_NAME, "메이플 닉네임", true)
                        ),

                // /캐릭터삭제 [메이플닉네임]
                Commands.slash(DELETE_CHARACTER, "길드에 캐릭터를 추가하기 위한 명령어 입니다.")
                        .addOptions(
                                new OptionData(OptionType.STRING, CHARACTER_NAME, "메이플 닉네임", true, true)
                        ),


                // /닉네임변경 [메이플닉네임] [변경닉네임]
                Commands.slash(MAPLE_CHARACTER_CHANGE, "메이플스토리의 닉네임 변경을 위한 명령어 입니다.")
                        .addOptions(
                                new OptionData(OptionType.STRING, CHARACTER_NAME, "기존 닉네임", true, true),
                                new OptionData(OptionType.STRING, CHANGE_CHARACTER_NAME, "변경한 닉네임", true)
                        ),

                // /길드원전체조회
                Commands.slash(SELECT_ALL_GUILD_USER, "길드원 전체를 조회하기 위한 명령어 입니다."),

                // /길드원조회 [유저]
                Commands.slash(SELECT_GUILD_USER, "길드원을 조회하기 위한 명령어 입니다.")
                        .addOptions(
                                new OptionData(OptionType.USER, DISCORD_USER, "유저", true)
                        )


        ).queue();
    }
}