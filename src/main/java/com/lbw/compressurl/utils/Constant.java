package com.lbw.compressurl.utils;


public class Constant {
    // 自增计数器的 key
    public static final String COUNTER_KEY = "sequence";

    // redis 键值的过期时间7天
    public static final Long RedisExpire = 7 * 24 * 60 * 60 * 1000L;
}
