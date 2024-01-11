package com.pixel.jda.bot.listener;

import com.pixel.jda.bot.dto.CharacterDTO;
import com.pixel.jda.bot.dto.DiscordUserDTO;
import com.pixel.jda.bot.mapper.CharacterMapper;
import com.pixel.jda.bot.mapper.DiscordUserMapper;
import com.pixel.jda.bot.util.api.NexonAPI;
import com.pixel.jda.bot.util.exception.NexonApiException;
import com.pixel.jda.bot.util.exception.PixelException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.List;

import static com.pixel.jda.bot.listener.CommandList.*;
import static com.pixel.jda.bot.util.SqlUtil.getSqlSession;
import static com.pixel.jda.bot.util.Util.getUID;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        JDA jda = event.getJDA();
        Guild guild = event.getGuild();
        Member member = event.getMember();

        String discordId = member.getId();

        // 사용자 정보
        DiscordUserDTO user = DiscordUserDTO.getDiscordUser(discordId);
        List<CharacterDTO> mainCharacter = CharacterDTO.getCharacterByDiscordId(discordId);

        // 사용된 커멘드
        String cmd = event.getName();

        // 커멘드 옵션을 담을 JSONObject
        JSONObject cmdParams = new JSONObject();

        SqlSession sqlSession = null;

        try {
            // SQL 세션 생성
            sqlSession = getSqlSession(false);

            DiscordUserMapper discordUserMapper = sqlSession.getMapper(DiscordUserMapper.class);
            CharacterMapper characterMapper = sqlSession.getMapper(CharacterMapper.class);


            switch (cmd) {
                // 길드 가입
                case GUILD_JOIN -> {
                    event.getOptions().forEach(option -> {
                        if (option.getName().equals(CHARACTER_NAME)) {
                            cmdParams.put(CHARACTER_NAME, option.getAsString());
                        }
                    });

                    if (user != null) {
                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("이미 길드에 가입되어 있습니다.")
                                        .setColor(Color.RED)
                                        .build()
                        ).queue();
                        return;
                    }

                    // 메이플 서버에 해당 캐릭터가 존재하는지 확인
                    checkCharacter(event, (String) cmdParams.get(CHARACTER_NAME));

                    // Discord User 정보 세팅
                    DiscordUserDTO targetUser = new DiscordUserDTO();
                    targetUser.setId(discordId);
                    targetUser.setName(member.getEffectiveName());

                    // 캐릭터 정보 세팅
                    CharacterDTO targetCharacter = new CharacterDTO();
                    targetCharacter.setId(getUID());
                    targetCharacter.setDiscordId(discordId);
                    targetCharacter.setGuildName("픽셀");
                    targetCharacter.setCharacterName((String) cmdParams.get(CHARACTER_NAME));
                    targetCharacter.setMainYn(true);
                    targetCharacter.setSubYn(false);

                    int result1 = discordUserMapper.insertDiscordUser(targetUser);
                    int result2 = characterMapper.insertCharacter(targetCharacter);

                    if (result1 == 1 && result2 == 1) {
                        sqlSession.commit();

                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("길드 가입이 완료되었습니다.")
                                        .setColor(Color.GREEN)
                                        .addField("캐릭터명", "`" + targetCharacter.getCharacterName() + "`", false)
                                        .build()
                        ).queue();
                    } else {
                        sqlSession.rollback();
                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("길드 가입에 실패하였습니다.")
                                        .setColor(Color.RED)
                                        .addField("캐릭터명", "`" + targetCharacter.getCharacterName() + "`", false)
                                        .build()
                        ).queue();
                    }
                }

                // 길드 내 캐릭터 추가
                case ADD_CHARACTER -> {
                    event.getOptions().forEach(option -> {
                        if (option.getName().equals(GUILD_NAME)) {
                            cmdParams.put(GUILD_NAME, option.getAsString());
                        } else if (option.getName().equals(CHARACTER_NAME)) {
                            cmdParams.put(CHARACTER_NAME, option.getAsString());
                        }
                    });

                    // 메이플 서버에 해당 캐릭터가 존재하는지 확인
                    checkCharacter(event, (String) cmdParams.get(CHARACTER_NAME));

                    // 중복 등록 확인
                    checkOverlapCharacter(event, (String) cmdParams.get(CHARACTER_NAME));

                    // 캐릭터 정보 입력
                    CharacterDTO targetCharacter = new CharacterDTO();
                    targetCharacter.setId(getUID());
                    targetCharacter.setDiscordId(discordId);
                    targetCharacter.setGuildName((String) cmdParams.get(GUILD_NAME));
                    targetCharacter.setCharacterName((String) cmdParams.get(CHARACTER_NAME));
                    targetCharacter.setMainYn(false);
                    targetCharacter.setSubYn(true);

                    // 메인 길드에 추가하는 경우 최대 가입 가능한 개수 확인
                    if (cmdParams.get(GUILD_NAME).equals("픽셀")) {
                        checkMaxCharacter(event, discordId);
                    } else {
                        targetCharacter.setMainYn(false);
                        targetCharacter.setSubYn(true);
                    }

                    int result = characterMapper.insertCharacter(targetCharacter);

                    // 캐릭터 추가 성공
                    if (result == 1) {
                        sqlSession.commit();
                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("캐릭터 추가가 완료되었습니다.")
                                        .setColor(Color.GREEN)
                                        .addField("캐릭터명", "`" + targetCharacter.getCharacterName() + "`", false)
                                        .build()
                        ).queue();
                    } else {
                        sqlSession.rollback();
                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("캐릭터 추가에 실패하였습니다.")
                                        .setColor(Color.RED)
                                        .addField("캐릭터명", "`" + targetCharacter.getCharacterName() + "`", false)
                                        .build()
                        ).queue();
                    }
                }

                // 길드 내 캐릭터 삭제
                case DELETE_CHARACTER -> {
                    event.getOptions().forEach(option -> {
                        if (option.getName().equals(CHARACTER_NAME)) {
                            cmdParams.put(CHARACTER_NAME, option.getAsString());
                        }
                    });

                    CharacterDTO targetCharacter = CharacterDTO.getCharacterByName((String) cmdParams.get(CHARACTER_NAME));

                    // DB에 캐릭터가 존재 하는지?
                    if (targetCharacter == null) {
                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("캐릭터가 존재 하지 않습니다.")
                                        .setColor(Color.RED)
                                        .build()
                        ).queue();
                        return;
                    }

                    // 메인 캐릭터가 아닌 경우 삭제
                    if (!targetCharacter.isMainYn()) {
                        int result = characterMapper.deleteCharacter(targetCharacter);

                        if (result == 1) {
                            sqlSession.commit();
                            event.replyEmbeds(
                                    new EmbedBuilder()
                                            .setTitle("캐릭터 삭제가 완료되었습니다.")
                                            .setColor(Color.GREEN)
                                            .addField("캐릭터명", "`" + targetCharacter.getCharacterName() + "`", false)
                                            .build()
                            ).queue();
                        } else {
                            sqlSession.rollback();
                            event.replyEmbeds(
                                    new EmbedBuilder()
                                            .setTitle("캐릭터 삭제에 실패하였습니다.")
                                            .setColor(Color.RED)
                                            .addField("캐릭터명", "`" + targetCharacter.getCharacterName() + "`", false)
                                            .build()
                            ).queue();
                        }
                    } else {
                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("메인 캐릭터는 삭제할 수 없습니다.")
                                        .setColor(Color.RED)
                                        .addField("캐릭터명", "`" + targetCharacter.getCharacterName() + "`", false)
                                        .build()
                        ).queue();
                    }
                }

                // 길드원 정보 입력
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

                    DiscordUserDTO targetUser = DiscordUserDTO.getDiscordUser((String) cmdParams.get(DISCORD_USER), sqlSession);

                    if (targetUser == null) {
                        event.replyEmbeds(new EmbedBuilder()
                                        .setTitle("해당 유저는 길드에 가입되어 있지 않습니다.")
                                        .setColor(Color.RED)
                                        .build())
                                .queue();
                        return;
                    }

                    targetUser.setBirthYear((int) cmdParams.get(BIRTH_YEAR));
                    targetUser.setPreGuild((String) cmdParams.get(PRE_GUILD_NAME));
                    targetUser.setPreLeaveReason((String) cmdParams.get(PRE_GUILD_LEAVE_REASON));
                    targetUser.setPlayTime((String) cmdParams.get(PLAY_TIME));

                    discordUserMapper.updateDiscordUser(targetUser);

                    event.replyEmbeds(new EmbedBuilder()
                                    .setTitle("정보가 업데이트되었습니다.")
                                    .setColor(Color.GREEN)
                                    .build())
                            .queue();

                }
            }

        } catch (NexonApiException | PixelException e) {
            if (sqlSession != null) {
                sqlSession.rollback();
            }

            System.err.println(e.getMessage());
        } catch (
                SqlSessionException e) {
            if (sqlSession != null) {
                sqlSession.rollback();
            }

            System.err.println(e.getMessage());

            event.replyEmbeds(new EmbedBuilder()
                            .setTitle("시스템에 오류가 발생하였습니다.")
                            .setColor(Color.RED)
                            .addField("개발자에게 문의 부탁드립니다.", e.getMessage(), false)
                            .build())
                    .queue();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }

    }

    /**
     * 메이플 서버에 해당 캐릭터가 존재하는지 확인
     *
     * @param e
     * @param characterName
     * @throws NexonApiException
     */
    private void checkCharacter(SlashCommandInteractionEvent e, String characterName) throws NexonApiException {
        NexonAPI nexonAPI = new NexonAPI();

        // 메이플 서버에 해당 캐릭터가 존재하는지 확인
        JSONObject ocid = nexonAPI.getOcid(characterName);
        int ocid_status = (int) ocid.get("status");

        // 메이플 서버에 해당 캐릭터가 존재하지 않을 시
        if (ocid.containsKey("error")) {
            JSONObject errorJson = (JSONObject) ocid.get("error");

            String errorName = (String) errorJson.get("name");
            String errorMessage = (String) errorJson.get("message");

            e.replyEmbeds(new EmbedBuilder()
                            .setTitle("메이플 서버에 해당 캐릭터가 존재하지 않습니다.")
                            .setColor(Color.RED)
                            .addField("캐릭터명", characterName, false)
                            .build())
                    .queue();

            throw new NexonApiException("메이플 서버에 해당 캐릭터가 존재하지 않습니다.", errorName, errorMessage);
        }
    }


    /**
     * 캐릭터 등록 최대 개수 확인
     *
     * @param discordId
     * @throws PixelException
     */
    private void checkMaxCharacter(SlashCommandInteractionEvent e, String discordId) throws PixelException {

        List<CharacterDTO> characters = CharacterDTO.getCharacterByDiscordId(discordId, true);

        StringBuilder message = new StringBuilder();

        for (CharacterDTO character : characters) {
            message.append("- ").append(character.getCharacterName()).append("\n");
        }

        if (characters.size() >= 3) {
            e.replyEmbeds(new EmbedBuilder()
                    .setTitle("메인 길드에 최대 3개까지 캐릭터 등록이 가능 합니다.")
                    .setColor(Color.RED)
                    .addField("등록된 캐릭터", message.toString(), false)
                    .build()).queue();

            throw new PixelException("캐릭터는 최대 3개까지만 등록 가능합니다.");
        }
    }

    /**
     * 중복 캐릭터 등록 확인
     *
     * @throws PixelException
     */
    private void checkOverlapCharacter(SlashCommandInteractionEvent e, String characterName) throws PixelException {
        CharacterDTO character = CharacterDTO.getCharacterByName(characterName);

        if (character != null) {
            List<CharacterDTO> charactersDTO = CharacterDTO.getCharacterByDiscordId(character.getDiscordId());

            JSONObject characters = CharacterDTO.getCharacterSortByGuild(charactersDTO);

            JSONArray guild1 = (JSONArray) characters.get("픽셀");
            JSONArray guild2 = (JSONArray) characters.get("PIXEL");

            StringBuilder str1 = new StringBuilder();
            StringBuilder str2 = new StringBuilder();

            for (Object o : guild1) {
                str1.append("- ").append(o).append("\n");
            }

            for (Object o : guild2) {
                str2.append("- ").append(o).append("\n");
            }

            e.replyEmbeds(new EmbedBuilder()
                            .setTitle("이미 등록된 캐릭터 입니다.")
                            .setColor(Color.RED)
                            .addField("입력한 메이플 닉네임", "> `" + characterName + "`", false)
                            .addBlankField(true)
                            .addField("등록된 캐릭터 목록", "등록된 캐릭터를 삭제하려면 `/캐릭터삭제 [메이플닉네임]` 명령어를 사용해 주세요", false)
                            .addField("픽셀", str1.toString(), true)
                            .addField("PIXEL", str2.toString(), true)
                            .build())
                    .queue();

            throw new PixelException("이미 등록된 캐릭터 입니다.");
        }
    }

}