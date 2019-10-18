package com.youngbingdong.util.perf.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.youngbingdong.util.excel.ExcelUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ybd
 * @date 2019/10/15
 * @contact yangbingdong1994@gmail.com
 */
public class ExcelTest {

    @Test
    public void simpleWrite() {
        // 写法1
        String fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());

        // 写法2
        fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        excelWriter.write(data(), writeSheet);
        excelWriter.finish();
    }

    /**
     * 重复多次写入
     */
    @Test
    public void repeatedWrite() {
        // 方法1 如果写到同一个sheet
        String fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        for (int i = 0; i < 5; i++) {
            List<DemoData> data = data();
            excelWriter.write(data, writeSheet);
        }
        excelWriter.finish();

        // 方法2 如果写到不同的sheet 同一个对象
        fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        for (int i = 0; i < 5; i++) {
            writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
            List<DemoData> data = data();
            excelWriter.write(data, writeSheet);
        }
        excelWriter.finish();

        // 方法3 如果写到不同的sheet 不同的对象
        fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        excelWriter = EasyExcel.write(fileName).build();
        for (int i = 0; i < 5; i++) {
            writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoData.class).build();
            List<DemoData> data = data();
            excelWriter.write(data, writeSheet);
        }
        excelWriter.finish();
    }

    /**
     * 重复多次写入
     */
    @Test
    public void writeListener() {
        // 方法1 如果写到同一个sheet
        String fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").registerWriteHandler(new WriteDemoDataListener()).build();
        List<DemoData> data = data();
        excelWriter.write(data, writeSheet);
        excelWriter.finish();
    }

    @Test
    public void writeAndRead() throws InterruptedException {
        String fileName = TestFileUtil.getPath() + "writeAndRead" + System.currentTimeMillis() + ".xlsx";
        List<DemoData> data = data();
        DemoData demoData9 = data.get(9);
        DemoData demoData10 = new DemoData();
        demoData10.setDate(demoData9.getDate());
        demoData10.setDoubleData(demoData9.getDoubleData());
        demoData10.setString(demoData9.getString());
        data.add(demoData10);
        ExcelUtil.write(fileName, data);
        Thread.sleep(1000L);
        ExcelUtil.read(fileName, new DemoDataListener());
    }

    @Test
    public void readTest() {
        String fileName = TestFileUtil.getPath() + "writeAndRead1571393229950.xlsx";
        ExcelUtil.read(fileName, new DemoDataListener());
    }

    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.5679);
            list.add(data);
        }
        return list;
    }
}
