package com.pixel.jda.bot.mapper;

import com.pixel.jda.bot.dto.DiscordUserDTO;

public interface DiscordUserMapper {
    DiscordUserDTO getDiscordUserById(String id);

    int getCountDiscordUserById(String id);

    void insertDiscordUser(DiscordUserDTO discordUserDTO);

    void updateDiscordUser(DiscordUserDTO discordUserDTO);
}
