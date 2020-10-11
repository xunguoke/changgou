package com.itheima.health.job;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Author:Sun FuPing
 * @Date: 2020/10/11 9:40
 */

/**
 * 定时清理历史数据的类
 */
@Component("clearCacheJob")
public class ClearCacheJob {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 定时清理历史数据的方法
     */
    public void clearCache() {
        Logger logger = LoggerFactory.getLogger(ClearCacheJob.class);
        logger.debug("定时任务启动,清理开始");

        //获取Calendar
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -6);//6个月前
        // 设置Calendar月份数为下一个月
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        // 设置Calendar日期为下一个月1号
        calendar.set(Calendar.DATE, 1);
        // 设置Calendar日期减1,为6个月前月末
        calendar.add(Calendar.DATE, -1);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDay = sdf.format(calendar.getTime());
        //定时清理
        orderSettingService.deleteCache(lastDay);
        logger.debug("清理结束");
    }
}
