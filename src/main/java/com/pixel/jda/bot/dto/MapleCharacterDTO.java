package com.pixel.jda.bot.dto;

import lombok.Data;

@Data
public class MapleCharacterDTO {
    private String id;
    private String discordId;
    private String characterName;

    private String guildName;

    private boolean mainYn;
    private boolean subYn;



    /**
     * 현재 레벨에서 다음 레벨까지 필요한 경험치
     *
     * @return 경험치
     */
    public long getExpForNextLevel() {
        return 0L;
    }
}
