package com.youngbingdong.util.perf.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.youngbingdong.util.excel.MergeColumn;
import lombok.Data;

import java.util.Date;

/**
 * 基础数据类
 *
 **/
@Data
public class DemoData {

    @MergeColumn(sameAs = {"数字标题"})
    @ExcelProperty("字符串标题")
    private String string;

    @ColumnWidth(30)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ExcelProperty("日期标题")
    private Date date;

    @NumberFormat("#.##%")
    @ExcelProperty("数字标题")
    private Double doubleData;

}
