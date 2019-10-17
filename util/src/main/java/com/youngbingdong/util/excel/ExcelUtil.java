package com.youngbingdong.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.List;
import java.util.function.Supplier;

import static cn.hutool.core.collection.CollUtil.isEmpty;
import static cn.hutool.core.collection.CollUtil.isNotEmpty;

/**
 * @author ybd
 * @date 2019/10/17
 * @contact yangbingdong1994@gmail.com
 */
public class ExcelUtil {

    public static void export(String fileName, List<?> data) {
        if (isEmpty(data)) {
            return;
        }
        Class<?> exportEntityClass = data.get(0).getClass();
        EasyExcel.write(fileName, exportEntityClass)
                 .registerWriteHandler(MergeColumnHandler.create(data.size()))
                 .sheet()
                 .doWrite(data);
    }

    public static void exportByDataFlow(String fileName, int dataSize, Supplier<List<?>> dataFlowSupplier) {
        List<?> data;
        ExcelWriter writer = null;
        WriteSheet sheet = EasyExcel.writerSheet().build();
        while (isNotEmpty((data = dataFlowSupplier.get()))) {
            Class<?> exportEntityClass = data.get(0).getClass();
            if (writer == null) {
                writer = EasyExcel.write(fileName, exportEntityClass)
                                 .registerWriteHandler(MergeColumnHandler.create(dataSize))
                                 .build();
            }
            writer.write(data, sheet);
        }
        if (writer != null) {
            writer.finish();
        }
    }
}
