package com.pixel.jda.bot.listener;

public class CommandList {

    /**
     * 커맨드
     * 명령어 추가 시 공백 금지
     * <p>/길드가입 [메이플닉네임]
     * <p>/캐릭터추가 [길드] [메이플닉네임]
     * <p>/캐릭터삭제 [메이플닉네임]
     * <p>/유저정보입력 [유저] [태어난연도] [이전길드명] [길드탈퇴사유] [플레이시간]
     * <p>/닉네임변경 [변경닉네임]
     * <p>/길드원조회 [유저]
     * <p>/길드원전체조회
     */
    public static final String GUILD_JOIN = "길드가입";

    public static final String ADD_CHARACTER = "캐릭터추가";
    public static final String DELETE_CHARACTER = "캐릭터삭제";

    public static final String INSERT_USER_INFO = "유저정보입력";
    public static final String MAPLE_CHARACTER_CHANGE = "닉네임변경";

    public static final String SELECT_GUILD_USER = "길드원조회";
    public static final String SELECT_ALL_GUILD_USER = "길드원전체조회";

    /**
     * 커멘드 옵션
     */
    public static final String DISCORD_USER = "유저";
    public static final String CHARACTER_NAME = "메이플닉네임";
    public static final String GUILD_NAME = "길드";

    public static final String BIRTH_YEAR = "태어난연도";
    public static final String PRE_GUILD_NAME = "이전길드명";
    public static final String PRE_GUILD_LEAVE_REASON = "길드탈퇴사유";
    public static final String PLAY_TIME = "플레이시간";

    public static final String CHANGE_CHARACTER_NAME = "변경닉네임";
}
