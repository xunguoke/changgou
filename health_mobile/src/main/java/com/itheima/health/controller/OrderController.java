package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.OrderService;
import com.itheima.health.service.SetmealService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    /**
     * 预约提交
     * @param orderInfo
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> orderInfo){
        // 验证校验
        //    验证前端提交过来的验证码与redis的验证码是否一致
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + orderInfo.get("telephone");
        //1. 先从redis中取出验证码codeInRedis key=手机号码
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(key);
        //2. codeInRedis没值，提示用户重新获取验证码
        if(StringUtils.isEmpty(codeInRedis)){
            return new Result(false, "请重新获取验证码");
        }
        //3. codeInRedis有值, 则比较前端的验证码是否一致
        if(!codeInRedis.equals(orderInfo.get("validateCode"))) {
            //   不一样，则返回验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 防止重复提交, 移除验证码
        jedis.del(key);
        //   一样，则继续执行调用服务的方法预约
        // 设置预约类型 health_mobile给手机端微信公众号使用的，写死它的类型为微信预约
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        Order order = orderService.submit(orderInfo);
        //4. 返回订单信息给页面
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 订单详情
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,Object> orderInfo = orderService.findById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }

    /**
     * 导出预约成功信息pdf
     */
    @GetMapping("/exportSetmealInfo")
    public void exportSetmealInfo(int id, HttpServletResponse res){
        // 查询订单详情
        Map<String, Object> orderInfo = orderService.findById(id);
        // 获取套餐的id
        Integer setmealId = (Integer)orderInfo.get("setmeal_id");
        // 查询套餐详情
        Setmeal setmealDetail = setmealService.findDetailById(setmealId);
        // 创建pdf文档
        Document doc = new Document();
        // 设置响应头信息
        res.setHeader("Content-Disposition","attachment;filename=orderInfo.pdf");
        // 设置内容体格式
        res.setContentType("application/pdf");
        // pdf关联响应流
        try {
            PdfWriter.getInstance(doc, res.getOutputStream());
            // 打开文档
            doc.open();
            // 中文字体
            Font font = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
            // 填充数据
            doc.add(new Paragraph("预约信息:",font));
            doc.add(new Paragraph("体检人：" + orderInfo.get("member"),font));
            doc.add(new Paragraph("体检套餐：" + orderInfo.get("setmeal"),font));
            doc.add(new Paragraph("体检日期：" + orderInfo.get("orderDate"),font));
            doc.add(new Paragraph("预约类型：" + orderInfo.get("orderType"),font));
            doc.add(new Paragraph(""));
            doc.add(new Paragraph("套餐信息:",font));

            // 表格3列
            Table table = new Table(3);
            // ======== 表格的样式 =============
            table.setWidth(80); // 宽度
            table.setBorder(1); // 边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            /*设置表格属性*/
            table.setBorderColor(new Color(0, 0, 255)); //将边框的颜色设置为蓝色
            table.setPadding(5);//设置表格与字体间的间距
            table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式

            table.addCell(buildCell("项目名称",font));
            table.addCell(buildCell("项目内容",font));
            table.addCell(buildCell("项目解读",font));
            // 检查组
            List<CheckGroup> checkGroups = setmealDetail.getCheckGroups();
            if(null != checkGroups){
                for (CheckGroup checkGroup : checkGroups) {
                    // 检查组名称
                    table.addCell(buildCell(checkGroup.getName(),font));
                    // 检查项的名称 拼接起来的
                    StringBuilder sb = new StringBuilder();
                    List<CheckItem> checkItems = checkGroup.getCheckItems();
                    if(null != checkItems){
                        for (CheckItem checkItem : checkItems) {
                            sb.append(checkItem.getName()).append(" ");
                        }
                        // 去除最后的空格
                        sb.setLength(sb.length()-1);
                    }
                    table.addCell(buildCell(sb.toString(),font));
                    // 解读
                    table.addCell(buildCell(checkGroup.getRemark(),font));
                }
            }
            doc.add(table);
            // 关闭文档
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 传递内容和字体样式，生成单元格
    private Cell buildCell(String content, Font font)
        throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }
}
