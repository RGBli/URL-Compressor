package com.lbw.compressurl.service.impl;

import com.lbw.compressurl.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置 key-value 为短链接-长链接
     * 设置永不过期
     */
    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置 key-value 为短链接-长链接
     * 并且设置过期时间
     * expire 表示有效期为多少分钟，如果为-1则表示永不过期
     */
    @Override
    public void set(String key, String value, long expire) {
        if (!hasKey(key)) {
            increaseSequence();
        }
        if (expire == -1) {
            stringRedisTemplate.opsForValue().set(key, value);
        } else {
            stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.MINUTES);
        }
    }

    /**
     * 设置 key 的过期时间
     * expire 表示有效期为多少分钟，如果为-1则表示永不过期
     */
    @Override
    public void setExpire(String key, long expire) {
        if (expire == -1) {
            persist(key);
        } else {
            stringRedisTemplate.expire(key, expire, TimeUnit.MINUTES);
        }
    }

    /**
     * 从 Redis 中根据短链接获取长链接
     */
    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 判断 key 是否存在
     */
    @Override
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 设置 key 永不过期
     */
    @Override
    public void persist(String key) {
        stringRedisTemplate.persist(key);
    }

    /**
     * 使发号器加一
     * 发号器的 key 为 sequence，初始 value 为1
     */
    @Override
    public void increaseSequence() {
        stringRedisTemplate.opsForValue().increment("sequence");
    }
}
