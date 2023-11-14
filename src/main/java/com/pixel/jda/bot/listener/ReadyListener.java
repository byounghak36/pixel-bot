package com.pixel.jda.bot.listener;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.List;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        // 커맨드 예시
        event.getJDA().updateCommands().addCommands(
                Commands.slash("길드설정", "길드를 설정합니다.")
                        .addSubcommands(
                                new SubcommandData("신규가입-채널설정", "길드원이 신규가입 시 알림을 받을 채널을 설정합니다.")
                                        .addOption(OptionType.CHANNEL, "채널", "채널을 설정해 주세요", true)
                        ),
                Commands.slash("길드가입", "길드 가입을 하기 위한 명령어 입니다."),
                Commands.slash("정보입력", "가입 유저에 대한 정보입력 명령어 입니다.")
                        .addOption(OptionType.USER, "유저", "정보를 추가/수정 할 유저를 선택해 주세요.", true),
                Commands.slash("닉네임변경", "닉네임 변경을 위한 명령어 입니다.")
                        .addOption(OptionType.STRING, "변경닉네임", "어떤 닉네임으로 변경하였는지 입력해 주세요.", true)
        ).queue();

        System.out.println("봇이 준비되었습니다.");


    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        // 서버 별 멤버 정보
        List<Member> members = event.getGuild().getMembers();

        // 서버 별 권한 정보
        List<Role> roles = event.getGuild().getRoles();

        event.getGuild().loadMembers().onSuccess(members1 -> {
            System.out.println("멤버 로드 성공");
        }).onError(error -> {
            System.out.println("멤버 로드 실패");
        });

        // 서버 별 권한 확인
        roles.forEach(role -> {
            System.out.println(event.getGuild().getName() + " _권한: " + role.getName());
        });

        // 서버 별 유저 확인
        members.forEach(member -> {
            System.out.println(event.getGuild().getName() + " _유저: " + member.getNickname());
        });
    }
}