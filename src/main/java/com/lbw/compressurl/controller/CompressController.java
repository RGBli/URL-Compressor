package com.lbw.compressurl.controller;

import com.alibaba.fastjson.JSONObject;
import com.lbw.compressurl.domain.Receiver;
import com.lbw.compressurl.service.RedisService;
import com.lbw.compressurl.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
        if (redisService.hasKey(longUrl)) {
            redisService.setExpire(longUrl, expire);
            return redisService.get(longUrl);
        } else {
            int n = Integer.parseInt(redisService.get("sequence"));
            String shortUrl = Converter.convertSequenceToBase62(n);
            redisService.set(shortUrl, longUrl, expire);
            return shortUrl;
        }
    }
}
