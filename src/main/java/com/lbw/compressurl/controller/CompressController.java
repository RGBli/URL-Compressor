package com.lbw.compressurl.controller;

import com.alibaba.fastjson.JSONObject;
import com.lbw.compressurl.domain.Receiver;
import com.lbw.compressurl.service.RedisService;
import com.lbw.compressurl.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompressController {

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/zip")
    public String compressAndSave(@RequestBody JSONObject json) throws Exception {
        Receiver receiver = json.toJavaObject(Receiver.class);
        String longUrl = receiver.getUrl();
        long expire = receiver.getExpire();

        String shortUrl = redisService.get("@" + longUrl);
        if (shortUrl != null) {
            return shortUrl;
        }

        long n = redisService.getAndIncr("sequence");
        shortUrl = Converter.convertSequenceToBase62(n);
        // 添加 key-value 对，设置过期时间并更新序号
        redisService.set(shortUrl, longUrl, expire);
        redisService.set("@" + longUrl, shortUrl, expire);
        return shortUrl;
    }
}
