package com.pixel.jda.bot.listener;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

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
                        .addOption(OptionType.STRING, MAPLE_CHARACTER_NAME, "메이플스토리 닉네임을 입력해 주세요.", true),
                // /유저정보입력 [유저] [태어난연도] [이전길드명] [길드탈퇴사유] [플레이시간]
                Commands.slash(INSERT_USER_INFO, "가입 유저에 대한 정보입력 명령어 입니다.")
                        .addOptions(
                                new OptionData(OptionType.USER, DISCORD_USER, "정보를 추가/수정 할 유저를 선택해 주세요.", true),
                                new OptionData(OptionType.INTEGER, BIRTH_YEAR, "태어난 연도를 입력해 주세요.", true),
                                new OptionData(OptionType.STRING, PRE_GUILD_NAME, "이전 길드명을 입력해 주세요.", true)
                                        .setMaxLength(12),
                                new OptionData(OptionType.STRING, PRE_GUILD_LEAVE_REASON, "이전 길드 탈퇴 사유를 입력해 주세요.", true)
                                        .setMaxLength(30),
                                new OptionData(OptionType.STRING, PLAY_TIME, "플레이 시간대를 입력해 주세요.", false)
                        ),
                // /닉네임변경 [메이플닉네임] [변경닉네임]
                Commands.slash(MAPLE_CHARACTER_CHANGE, "메이플스토리의 닉네임 변경을 위한 명령어 입니다.")
                        .addOption(OptionType.STRING, MAPLE_CHARACTER_NAME, "변경 대상의 캐릭터를 선택하여 주세요", true, true)
                        .addOption(OptionType.STRING, CHANGE_MAPLE_CHARACTER_NAME, "어떤 닉네임으로 변경하였는지 입력해 주세요.", true),
                // /길드원전체조회
                Commands.slash(SELECT_ALL_GUILD_USER, "길드원 전체를 조회하기 위한 명령어 입니다."),
                // /길드원조회 [유저]
                Commands.slash(SELECT_GUILD_USER, "길드원을 조회하기 위한 명령어 입니다.")
                        .addOption(OptionType.USER, DISCORD_USER, "정보를 조회 할 유저를 선택해 주세요.", true)
        ).queue();
    }
}