package com.lbw.compressurl.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "url")
public class Receiver {
    private int id;

    private String shortUrl;

    private String longUrl;

    private Long expireAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }

    public Receiver(String shortUrl, String longUrl, long expireAt) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.expireAt = expireAt;
    }
}
