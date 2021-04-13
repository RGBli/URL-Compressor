package com.lbw.compressurl.service;

public interface RedisService {
    void set(String shortUrl, String longUrl);
    void set(String shortUrl, String longUrl, long expire);
    void setExpire(String key, long expire);
    String get(String shortUrl);
    boolean hasKey(String key);
    void persist(String key);
    void increaseSequence();
}
