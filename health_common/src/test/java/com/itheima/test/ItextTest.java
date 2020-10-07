package com.itheima.test;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Description: No Description
 * User: Eric
 */
public class ItextTest {

    @Test
    public void testItext() throws Exception {
        // 创建文档
        Document doc = new Document();
        // 设置文档的保存路径
        PdfWriter.getInstance(doc,new FileOutputStream(new File("d:\\itextdemo.pdf")));
        // 打开文档
        doc.open();
        // 添加内容
        BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        doc.add(new Paragraph("Hello 你好 World!",new Font(font)));
        // 关闭文档
        doc.close();
    }
}
