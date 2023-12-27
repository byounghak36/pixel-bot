package com.pixel.jda.bot.listener;

import com.pixel.jda.bot.dto.DiscordUserDTO;
import com.pixel.jda.bot.dto.MapleCharacterDTO;
import com.pixel.jda.bot.mapper.DiscordUserMapper;
import com.pixel.jda.bot.mapper.MapleCharacterMapper;
import com.pixel.jda.bot.util.api.NexonAPI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;

import static com.pixel.jda.bot.listener.CommandList.*;
import static com.pixel.jda.bot.util.SqlUtil.getSqlSession;
import static com.pixel.jda.bot.util.Util.getUID;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        JDA jda = event.getJDA();
        Guild guild = event.getGuild();
        Member member = event.getMember();

        // 사용자 정보
        DiscordUserDTO user = DiscordUserDTO.getDiscordUser(member.getId());

        // 사용된 커멘드
        String cmd = event.getName();

        // 커멘드 옵션을 담을 JSONObject
        JSONObject cmdParams = new JSONObject();

        // 길드 가입
        switch (cmd) {
            case GUILD_JOIN -> {
                event.getOptions().forEach(option -> {
                    if (option.getName().equals(MAPLE_CHARACTER_NAME)) {
                        cmdParams.put(MAPLE_CHARACTER_NAME, option.getAsString());
                    }
                });

                // SQL 세션 생성
                SqlSession sqlSession = getSqlSession(false);

                try {
                    DiscordUserMapper mapper = sqlSession.getMapper(DiscordUserMapper.class);
                    MapleCharacterMapper mapleCharacterMapper = sqlSession.getMapper(MapleCharacterMapper.class);

                    if (user == null) {

                        // 메이플 서버에 해당 캐릭터가 존재하는지 확인
                        checkCharacter(event, (String) cmdParams.get(MAPLE_CHARACTER_NAME));

                        DiscordUserDTO targetUser = new DiscordUserDTO();
                        targetUser.setId(member.getId());
                        targetUser.setName(member.getEffectiveName());

                        mapper.insertDiscordUser(targetUser);

                        MapleCharacterDTO targetCharacter = new MapleCharacterDTO();
                        targetCharacter.setId(getUID());
                        targetCharacter.setDiscordId(member.getId());
                        targetCharacter.setCharacterName((String) cmdParams.get(MAPLE_CHARACTER_NAME));

                        mapleCharacterMapper.insertMainCharacter(targetCharacter);

                        sqlSession.commit();

                        event.reply("길드 가입이 완료되었습니다.").setEphemeral(false).queue();
                    } else {
                        event.reply("이미 가입된 유저입니다.").setEphemeral(false).queue();
                    }
                } catch (Exception e) {
                    if (sqlSession != null) {
                        sqlSession.rollback();
                        sqlSession.close();
                    }
                    System.err.println(e.getMessage());
                    event.reply("시스템에 에러가 발생하였습니다.\n관리자에게 문의 부탁드립니다.").setEphemeral(false).queue();
                    // END
                } finally {
                    if (sqlSession != null) {
                        sqlSession.close();
                    }
                }
            }
            case INSERT_USER_INFO -> {  // 유저 정보 입력

                event.getOptions().forEach(option -> {
                    if (option.getName().equals(DISCORD_USER)) {
                        cmdParams.put(DISCORD_USER, option.getAsMember().getId());
                    } else if (option.getName().equals(BIRTH_YEAR)) {
                        cmdParams.put(BIRTH_YEAR, option.getAsInt());
                    } else if (option.getName().equals(PRE_GUILD_NAME)) {
                        cmdParams.put(PRE_GUILD_NAME, option.getAsString());
                    } else if (option.getName().equals(PRE_GUILD_LEAVE_REASON)) {
                        cmdParams.put(PRE_GUILD_LEAVE_REASON, option.getAsString());
                    } else if (option.getName().equals(PLAY_TIME)) {
                        cmdParams.put(PLAY_TIME, option.getAsString());
                    }
                });
                try (SqlSession sqlSession = getSqlSession()) {

                    DiscordUserMapper mapper = sqlSession.getMapper(DiscordUserMapper.class);

                    DiscordUserDTO targetUser = DiscordUserDTO.getDiscordUser((String) cmdParams.get(DISCORD_USER), sqlSession);

                    if (targetUser != null) {
                        targetUser.setAge((int) cmdParams.get(BIRTH_YEAR));
                        targetUser.setPreGuild((String) cmdParams.get(PRE_GUILD_NAME));
                        targetUser.setPreLeaveReason((String) cmdParams.get(PRE_GUILD_LEAVE_REASON));
                        targetUser.setPlayTime((String) cmdParams.get(PLAY_TIME));

                        mapper.updateDiscordUser(targetUser);
                        event.reply("정보가 업데이트되었습니다.").setEphemeral(false).queue();
                        // END
                    } else {
                        event.reply("해당 유저는 길드가입이 되어있지 않아 정보변경이 불가능합니다.").setEphemeral(false).queue();
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    event.reply("시스템에 에러가 발생하였습니다.\n관리자에게 문의 부탁드립니다.").setEphemeral(true).queue();
                    // END
                }
            }
            case MAPLE_CHARACTER_CHANGE ->  // 메이플 닉네임 변경

                    event.getOptions().forEach(option -> {
                        if (option.getName().equals(CHANGE_MAPLE_CHARACTER_NAME)) {
                            cmdParams.put("id", getUID());
                            cmdParams.put("discord_id", member.getId());
                            cmdParams.put("before_name", member.getEffectiveName());
                            cmdParams.put("after_name", option.getAsString());


                        }
                    });
            case SELECT_ALL_GUILD_USER -> {

            }
        }
    }

    private void checkCharacter(SlashCommandInteractionEvent e, String characterName) {
        NexonAPI nexonAPI = new NexonAPI();

        // 메이플 서버에 해당 캐릭터가 존재하는지 확인
        JSONObject ocid = nexonAPI.getOcid(characterName);
        int ocid_status = (int) ocid.get("status");

        // 메이플 서버에 해당 캐릭터가 존재하지 않을 시
        if (ocid.containsKey("error")) {
            JSONObject errorJson = (JSONObject) ocid.get("error");

            String errorName = (String) errorJson.get("name");
            String errorMessage = (String) errorJson.get("message");

            e.reply("메이플 서버에 해당 캐릭터가 존재하지 않습니다.\n" +
                    "캐릭터명: " + characterName + "\n" +
                    "에러코드: " + errorName + "\n" +
                    "에러메시지: " + errorMessage).setEphemeral(false).queue();
        }
    }
}