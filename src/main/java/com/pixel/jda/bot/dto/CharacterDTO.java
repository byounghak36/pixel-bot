package com.pixel.jda.bot.dto;

import com.pixel.jda.bot.mapper.CharacterMapper;
import com.pixel.jda.bot.util.SqlUtil;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

@Data
public class CharacterDTO {

    /**
     * 캐릭터 정보
     * id: UID
     * discordId: 디스코드 아이디
     * guildName: 길드명
     * characterName: 캐릭터명
     * mainYn: 메인 캐릭터 여부
     * subYn: 서브 캐릭터 여부
     */
    private String id;
    private String discordId;
    private String guildName;
    private String characterName;
    private boolean mainYn;
    private boolean subYn;


    /**
     * Discord Id로 조회
     *
     * @param id
     * @return
     */
    public static List<CharacterDTO> getCharacterByDiscordId(String id) {
        try (SqlSession sqlSession = SqlUtil.getSqlSession()) {
            return getCharacterByDiscordId(id, false, sqlSession);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Discord Id로 조회
     *
     * @param id
     * @param mainGuild 메인 길드에 대한 여부
     * @return
     */
    public static List<CharacterDTO> getCharacterByDiscordId(String id, boolean mainGuild) {
        try (SqlSession sqlSession = SqlUtil.getSqlSession()) {
            return getCharacterByDiscordId(id, mainGuild, sqlSession);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Discord Id로 조회
     *
     * @param id
     * @param sqlSession
     * @return
     */
    public static List<CharacterDTO> getCharacterByDiscordId(String id, boolean mainGuild, SqlSession sqlSession) {
        try {
            CharacterMapper mapper = sqlSession.getMapper(CharacterMapper.class);
            return mapper.getCharacterByDiscordId(id, mainGuild);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 캐릭터 이름으로 조회
     *
     * @param name
     * @return
     */
    public static CharacterDTO getCharacterByName(String name) {
        try (SqlSession sqlSession = SqlUtil.getSqlSession()) {
            return getCharacterByName(name, sqlSession);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 캐릭터 이름으로 조회
     *
     * @param name
     * @param sqlSession
     * @return
     */
    public static CharacterDTO getCharacterByName(String name, SqlSession sqlSession) {
        try {
            CharacterMapper mapper = sqlSession.getMapper(CharacterMapper.class);
            return mapper.getCharacterByName(name);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 길드별 캐릭터 분류
     *
     * @param characters
     */
    public static JSONObject getCharacterSortByGuild(List<CharacterDTO> characters) {

        JSONObject result = new JSONObject();

        for (CharacterDTO character : characters) {
            if (result.containsKey(character.getGuildName())) {
                JSONArray ja = (JSONArray) result.get(character.getGuildName());
                ja.add(character.getCharacterName());
            } else {
                JSONArray ja = new JSONArray();
                ja.add(character.getCharacterName());
                result.put(character.getGuildName(), ja);
            }
        }
        return result;
    }
}
