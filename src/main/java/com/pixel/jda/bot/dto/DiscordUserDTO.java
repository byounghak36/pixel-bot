package com.pixel.jda.bot.dto;

import com.pixel.jda.bot.mapper.DiscordUserMapper;
import com.pixel.jda.bot.util.SqlUtil;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class DiscordUserDTO {

    private String id;
    private String name;
    private int age;
    private String preGuild;
    private String preLeaveReason;
    private String playTime;
    private Date joinDate;
    private Date exitDate;

    /**
     * Discord User를 가져옴
     *
     * @param id         Discord ID
     * @return DiscordUser
     */
    public static DiscordUserDTO getDiscordUser(String id) {
        try (SqlSession sqlSession = SqlUtil.getSqlSession()) {
            return getDiscordUser(id, sqlSession);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Discord User를 가져옴
     *
     * @param id         Discord ID
     * @param sqlSession SqlSession
     * @return DiscordUser
     */
    public static DiscordUserDTO getDiscordUser(String id, SqlSession sqlSession) {
        try {
            DiscordUserMapper mapper = sqlSession.getMapper(DiscordUserMapper.class);
            return mapper.getDiscordUserById(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 유저가 존재하는지 확인
     * @param id
     * @return
     */
    public static int getCountDiscordUserById(String id) {
        try (SqlSession sqlSession = SqlUtil.getSqlSession()) {
            return getCountDiscordUserById(id, sqlSession);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    /**
     * 유저가 존재하는지 확인
     * @param id
     * @param sqlSession
     * @return
     */
    public static int getCountDiscordUserById(String id, SqlSession sqlSession) {
        try {
            return sqlSession.selectOne("com.pixel.getCountDiscordUserById", id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    public String getJoinDateFormatted() {
        return getJoinDateFormatted("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 포맷된 날짜로 출력
     *
     * @param pattern 기본값 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getJoinDateFormatted(String pattern) {
        if (joinDate == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(joinDate);
    }


    public String getExitDateFormatted() {
        return getExitDateFormatted("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 포맷된 날짜로 출력
     *
     * @param pattern 기본값 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getExitDateFormatted(String pattern) {
        if (exitDate == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(exitDate);
    }

}


