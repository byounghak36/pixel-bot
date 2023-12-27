package com.pixel.jda.bot.util;

import java.util.UUID;

public class Util {

    public static String getUID() {
        // 랜덤한 UUID 생성
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
