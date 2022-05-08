package com.lbw.compressurl.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbw.compressurl.entity.Receiver;
import com.lbw.compressurl.service.ReceiverService;
import com.lbw.compressurl.service.RedisService;
import com.lbw.compressurl.utils.Constant;
import com.lbw.compressurl.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class VisitController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ReceiverService receiverService;

    private final static String REDIRECT_PREFIX = "redirect:";

    @GetMapping("favicon.ico")
    @ResponseBody
    public void dummyFavicon() {
        // 空响应处理 favicon.ico 请求
    }

    @RequestMapping("/{url}")
    public String visit(@PathVariable("url") String url) throws Exception {
        System.out.println(url);
        if (url.length() != Converter.LENGTH) {
            throw new Exception("Wrong url");
        }

        String longUrl = redisService.get(url);
        // 如果已经存入 Redis，则按照长链接重定向访问
        if (longUrl == null) {
            QueryWrapper<Receiver> queryWrapper = new QueryWrapper<>();
            Receiver receiver = receiverService.getOne(queryWrapper.eq("short_url", url));
            long now = System.currentTimeMillis();
            if (receiver == null || receiver.getExpireAt() < now) {
                return "404";
            }

            redisService.set(url, receiver.getLongUrl(), receiver.getExpireAt() - now > Constant.RedisExpire ? Constant.RedisExpire : receiver.getExpireAt() - now);
            return REDIRECT_PREFIX + receiver.getLongUrl();
        }
        // 302临时重定向
        return REDIRECT_PREFIX + longUrl;
    }
}
