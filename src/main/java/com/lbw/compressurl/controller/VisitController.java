package com.lbw.compressurl.controller;

import com.lbw.compressurl.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VisitController {
    @Autowired
    private RedisService redisService;

    private final static String REDIRECT_PREFIX = "redirect:";

    @RequestMapping("/api/{shortUrl}")
    public String visit(@PathVariable String shortUrl) {
        String longUrl = redisService.get(shortUrl);
        if (longUrl == null) {
            return "exceptions/notexist";
        } else {
            return REDIRECT_PREFIX + longUrl;
        }
    }
}
