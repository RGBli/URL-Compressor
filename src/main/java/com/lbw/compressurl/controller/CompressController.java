package com.lbw.compressurl.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.lbw.compressurl.entity.Receiver;
import com.lbw.compressurl.service.ReceiverService;
import com.lbw.compressurl.service.RedisService;
import com.lbw.compressurl.utils.Converter;
import com.lbw.compressurl.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@RestController
public class CompressController {

    @Value("${server.port}")
    private int port;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ReceiverService receiverService;

    // 限流，每秒最多50个请求，预热期为3s
    private RateLimiter rateLimiter = RateLimiter.create(50, 3, TimeUnit.SECONDS);

    @RequestMapping(value = "/zip")
    public String compressAndSave(@RequestParam("url") String longUrl, @RequestParam("expire") long expire) throws Exception {
        boolean flag = rateLimiter.tryAcquire(0, TimeUnit.SECONDS);
        if (!flag) {
            throw new Exception("访问受限");
        }

        long now = System.currentTimeMillis();
        if (expire < now) {
            throw new Exception("Wrong expire time");
        }

        // 判断是否对本服务器的 URL 进行压缩
        InetAddress address = InetAddress.getLocalHost();
        String prefix = "http://" + address.getHostAddress() + ":" + port;
        if (longUrl.startsWith(prefix)) {
            throw new Exception("不能对本服务的 URL 进行压缩");
        }

        long n = redisService.getAndIncr(Constant.COUNTER_KEY);
        String shortUrl = Converter.convertSequenceToBase62(n);

        // 如果过期时间超过7天，则存入数据库和 redis，否则仅存入 redis
        if (expire - now > Constant.RedisExpire) {
            redisService.set(shortUrl, longUrl, now + Constant.RedisExpire);
            Receiver receiver = new Receiver(shortUrl, longUrl, expire);
            receiverService.save(receiver);
        } else {
            redisService.set(shortUrl, longUrl, expire);
        }
        return shortUrl;
    }
}
