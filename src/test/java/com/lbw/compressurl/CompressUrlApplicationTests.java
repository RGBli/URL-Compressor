package com.lbw.compressurl;

import com.lbw.compressurl.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompressUrlApplicationTests {

    @Autowired
    private RedisService redisService;

    @Test
    void contextLoads() {
        redisService.set("test", "0");
        System.out.println(redisService.getAndIncr("test"));
    }

}
