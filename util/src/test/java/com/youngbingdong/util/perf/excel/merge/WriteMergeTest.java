package com.youngbingdong.util.perf.excel.merge;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.youngbingdong.util.excel.MergeColumnHandler;
import com.youngbingdong.util.perf.excel.DemoData;
import com.youngbingdong.util.perf.excel.TestFileUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ybd
 * @date 2019/10/16
 * @contact yangbingdong1994@gmail.com
 */
public class WriteMergeTest {

    @Test
    public void writeMerge() {
        String fileName = TestFileUtil.getPath() + "writeMerge" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").registerWriteHandler(MergeColumnHandler.create(20)).build();
        excelWriter.write(data1(), writeSheet);
        excelWriter.write(data2(), writeSheet);
        excelWriter.finish();

    }

    private List<DemoData> data1() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                DemoData data = data(i);
                list.add(data);
            }
        }
        list.add(data(3));
        return list;
    }

    private List<DemoData> data2() {
        List<DemoData> list = new ArrayList<DemoData>();
        list.add(data(3));
        for (int i = 4; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                DemoData data = data(i);
                list.add(data);
            }
        }
        return list;
    }

    private DemoData data(int i) {
        DemoData data = new DemoData();
        data.setString("字符串" + i);
        data.setDate(new Date());
        data.setDoubleData(0.5679);
        return data;
    }
}
