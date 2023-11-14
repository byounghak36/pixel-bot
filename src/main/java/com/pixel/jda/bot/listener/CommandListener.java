package com.pixel.jda.bot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.concurrent.TimeUnit;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("길드가입")) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setTitle("디스코드 가입")
                            .setDescription("디스코드 가입을 하려면 아래 버튼을 눌러주세요.")
                            .build()
            ).addActionRow(
                    Button.primary("guild_join", "길드 가입하기")
            ).setEphemeral(true).queue();
        }

        if (event.getName().equals("정보입력")) {

            Member member = event.getOption("유저").getAsMember();

            TextInput name = TextInput.create("id", "유저 UID(변경 X)", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setValue(member.getId())
                    .build();

            TextInput age = TextInput.create("age", "나이", TextInputStyle.SHORT)
                    .setPlaceholder("나이를 입력해 주세요.")
                    .setRequired(true)
                    .setMinLength(1)
                    .build();

            TextInput preGuildName = TextInput.create("preGuildName", "이전 길드명", TextInputStyle.SHORT)
                    .setPlaceholder("이전 길드명을 입력해 주세요.")
                    .setRequired(true)
                    .setMinLength(1)
                    .build();

            TextInput guildExitDesc = TextInput.create("guildExitDesc", "이전 길드 탈퇴 사유", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("이전 길드 탈퇴 사유를 입력해 주세요.")
                    .setRequired(true)
                    .setMinLength(1)
                    .build();

            TextInput playTime = TextInput.create("playTime", "플레이 시간대", TextInputStyle.SHORT)
                    .setPlaceholder("플레이 시간대를 입력해 주세요.")
                    .setRequired(false)
                    .setMinLength(1)
                    .build();

            Modal modal = Modal.create("modmail", "정보입력")
                    .addComponents(ActionRow.of(name), ActionRow.of(age), ActionRow.of(preGuildName), ActionRow.of(guildExitDesc), ActionRow.of(playTime))
                    .build();

            event.replyModal(modal).delay(100, TimeUnit.MILLISECONDS).queue();
        }

        if (event.getName().equals("닉네임변경")) {

        }

        if (event.getName().equals("길드설정")) {
            if (event.getSubcommandName().equals("신규가입-채널설정")) {

            }
        }
    }
}