package com.pixel.jda.bot.mapper;

import com.pixel.jda.bot.dto.MapleCharacterDTO;
import org.apache.ibatis.annotations.Param;
import org.json.simple.JSONObject;

public interface MapleCharacterMapper {
    void insertMainCharacter(MapleCharacterDTO mapleCharacterDTO);

    void updateCharacter();

    void insertMapleNicknameChangeLog(@Param("jo") JSONObject jsonObject);
}
