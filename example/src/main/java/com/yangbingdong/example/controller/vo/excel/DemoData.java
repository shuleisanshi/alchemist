package com.yangbingdong.example.controller.vo.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.youngbingdong.util.excel.MergeColumn;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基础数据类
 *
 **/
@Data
public class DemoData {

    @ExcelIgnore
    public static final String DOUBEL_DATA_HEAD = "数字标题";

    @MergeColumn(sameAs = {DOUBEL_DATA_HEAD})
    @ExcelProperty("字符串标题")
    private String string;

    @ColumnWidth(30)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ExcelProperty("日期标题")
    private Date date;

    @NumberFormat("#.##%")
    @ExcelProperty(DOUBEL_DATA_HEAD)
    private Double doubleData;


    public static List<DemoData> data1() {
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

    public static List<DemoData> data2() {
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

    private static DemoData data(int i) {
        DemoData data = new DemoData();
        data.setString("字符串" + i);
        data.setDate(new Date());
        data.setDoubleData(0.5679);
        return data;
    }

}
