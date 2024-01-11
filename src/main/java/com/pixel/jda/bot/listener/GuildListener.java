package com.pixel.jda.bot.listener;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String user = event.getMember().getAsMention();

        List<TextChannel> channels = event.getGuild().getTextChannels();
        for (TextChannel ch : channels) {
            ch.sendMessage("New member joined: " + user).queue();
        }
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        System.out.println(event.getMember().getEffectiveName() + " change nickname");
        super.onGuildMemberUpdateNickname(event);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        System.out.println(event.getMember().getEffectiveName() + " leave");
        super.onGuildMemberRemove(event);
    }
}
