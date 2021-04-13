package com.lbw.compressurl.controller;

import com.lbw.compressurl.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VisitController {
    @Autowired
    private RedisService redisService;

    private final static String REDIRECT_PREFIX = "redirect:";

    @RequestMapping("/api")
    public String visit(@RequestParam String url) {
        String longUrl = redisService.get(url);
        // 如果短链接没有存入 Redis，则用输入链接访问
        // 如果已经存入 Redis，则重定向到长链接
        if (longUrl == null) {
            return REDIRECT_PREFIX + url;
        } else {
            return REDIRECT_PREFIX + longUrl;
        }
    }
}
