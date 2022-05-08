package com.lbw.compressurl.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbw.compressurl.entity.Receiver;
import com.lbw.compressurl.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoClean {
    @Autowired
    private ReceiverService receiverService;

    // 每天凌晨三点清除数据库中过期 URL
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanEveryDay() {
        QueryWrapper<Receiver> queryWrapper = new QueryWrapper<>();
        receiverService.remove(queryWrapper.lt("expire", System.currentTimeMillis()));
    }
}
