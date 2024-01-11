package com.pixel.jda.bot.mapper;

import com.pixel.jda.bot.dto.DiscordUserDTO;

public interface DiscordUserMapper {
    DiscordUserDTO getDiscordUserById(String id);

    int getCountDiscordUserById(String id);

    int insertDiscordUser(DiscordUserDTO discordUserDTO);

    int updateDiscordUser(DiscordUserDTO discordUserDTO);
}
