package com.pixel.jda.bot.mapper;

import com.pixel.jda.bot.dto.CharacterDTO;
import org.apache.ibatis.annotations.Param;
import org.json.simple.JSONObject;

import java.util.List;

public interface CharacterMapper {
    List<CharacterDTO> getCharacterByDiscordId(@Param("discordId") String discordId, @Param("mainGuild") boolean mainGuild);

    CharacterDTO getCharacterByName(@Param("name") String name);

    int insertCharacter(CharacterDTO CharacterDTO);

    int deleteCharacter(CharacterDTO CharacterDTO);

    int updateCharacter();

    void insertCharacterNameChangeLog(JSONObject jo);


}
