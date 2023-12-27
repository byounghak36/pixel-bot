package com.pixel.jda.bot.util.api;

public enum ApiEndpoint {

    // 계정 식별자(ouid) 조회
    OUID("/ouid"),

    // 캐릭터 식별자
    CHAR_OCID("/id"),

    // 기본 정보 조회
    CHAR_BASIC("/character/basic"),

    // 인기도 정보 조회
    CHAR_POPULARITY("/character/popularity"),

    // 종합 능력치 정보 조회
    CHAR_STAT("/character/stat"),

    // 하이퍼스텟 정보 조회
    CHAR_HYPER_STAT("/character/hyper-stat"),

    // 성향 정보 조회
    CHAR_PROPENSITY("/character/propensity"),

    // 어빌리티 정보 조회
    CHAR_ABILITY("/character/ability"),

    // 장착 장비 정보 조회(캐시 장비 제외)
    CHAR_ITEM_EQUIPMENT("/character/item-equipment"),

    // 장착 캐시 장비 정보 조회
    CHAR_CASHITEM_EQUIPMENT("/character/cashitem-equipment"),

    // 장착 심볼 정보 조회
    CHAR_SYMBOL_EQUIPMENT("/character/symbol-equipment"),

    // 적용 세트 효과 정보 조회
    CHAR_SET_EFFECT("/character/set-effect"),

    // 장착 헤어, 성형, 피부 정보 조회
    CHAR_BEAUTY_EQUIPMENT("/character/beauty-equipment"),

    // 장착 안드로이드 정보 조회
    CHAR_ANDROID_EQUIPMENT("/character/android-equipment"),

    // 장착 펫 정보 조회
    CHAR_PET_EQUIPMENT("/character/pet-equipment"),

    // 스킬 정보 조회
    CHAR_SKILL("/character/skill"),

    // 장착 링크 스킬 정보 조회
    CHAR_LINK_SKILL("/character/link-skill"),

    // V매트릭스 정보 조회
    CHAR_VMATRIX("/character/vmatrix"),

    // HEXA 코어 정보 조회
    CHAR_HEXAMATRIX("/character/hexametrix"),

    // HEXA 매트릭스 설정 HEXA 스탯 정보 조회
    CHAR_HEXAMATRIX_STAT("/character/hexametrix-stat"),

    // 무릉도장 최고 기록 정보 조회
    CHAR_DOJANG("/character/dojang"),


    // 유니온 정보 조회
    UNION("/user/union"),

    // 유니온 공격대 정보 조회
    UNION_RAIDER("/user/union-raider"),


    // 길드 식별자(oguild_id) 정보 조회
    GUILD_OCID("/guild/id"),

    // 기본 정보 조회
    GUILD_BASIC("/guild/basic"),


    // 큐브 사용 결과 조회
    HISTORY_CUBE("/history/cube"),


    // 종합 랭킹 정보 조회
    RANKING_OVERALL("/ranking/overall"),

    // 유니온 랭킹 정보 조회
    RANKING_UNION("/ranking/union"),

    // 길드 랭킹 정보 조회
    RANKING_GUILD("/ranking/guild"),

    // 무릉도장 랭킹 정보 조회
    RANKING_DOJANG("/ranking/dojang"),

    // 더 시드 랭킹 정보 조회
    RANKING_THESEED("/ranking/theseed"),

    // 업적 랭킹 정보 조회
    RANKING_ACHIEVEMENT("/ranking/achievement");

    private final String url;

    ApiEndpoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
