package com.lbw.compressurl.service;

public interface RedisService {
    void set(String key, String value);
    void set(String key, String value, long expire);
    void setExpire(String key, long expire);
    String get(String key);
    boolean hasKey(String key);
    void persist(String key);
    void increaseSequence();
}
