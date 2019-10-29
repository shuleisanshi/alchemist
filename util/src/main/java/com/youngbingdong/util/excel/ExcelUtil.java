package com.youngbingdong.util.excel;

import cn.hutool.core.codec.Base64;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.youngbingdong.util.spring.RequestHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

import static cn.hutool.core.collection.CollUtil.isEmpty;
import static cn.hutool.core.collection.CollUtil.isNotEmpty;
import static com.youngbingdong.util.reflect.TypeUtil.getClassFromGenericSupperClass;

/**
 * @author ybd
 * @date 2019/10/17
 * @contact yangbingdong1994@gmail.com
 */
public class ExcelUtil {

    public static void write(String fullFilePath, List<?> data) {
        if (isEmpty(data)) {
            return;
        }
        EasyExcel.write(fullFilePath, data.get(0).getClass())
                 .registerWriteHandler(MergeColumnHandler.create(data.size()))
                 .sheet()
                 .doWrite(data);
    }

    public static void writeByDataFlow(String fullFilePath, int dataSize, Supplier<List<?>> dataFlowSupplier) {
        List<?> data;
        ExcelWriter writer = null;
        WriteSheet sheet = EasyExcel.writerSheet().build();
        while (isNotEmpty((data = dataFlowSupplier.get()))) {
            if (writer == null) {
                writer = EasyExcel.write(fullFilePath, data.get(0).getClass())
                                  .registerWriteHandler(MergeColumnHandler.create(dataSize))
                                  .build();
            }
            writer.write(data, sheet);
        }
        if (writer != null) {
            writer.finish();
        }
    }

    public static void writeToWeb(String fileName, List<?> data) throws IOException {
        if (isEmpty(data)) {
            return;
        }
        HttpServletResponse response = RequestHolder.currentResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncoding(fileName) + ".xlsx");
        EasyExcel.write(response.getOutputStream(), data.get(0).getClass())
                 .registerWriteHandler(MergeColumnHandler.create(data.size()))
                 .sheet()
                 .doWrite(data);
    }

    private static String fileNameEncoding(String fileName) throws IOException {
        String agent = RequestHolder.currentRequest().getHeader("User-Agent");
        if (agent.contains("Firefox")) {
            fileName = "=?utf-8?B?"
                    + Base64.encode(fileName.getBytes(StandardCharsets.UTF_8)) + "?=";
        } else {
            fileName = URLEncoder.encode(fileName, "utf-8");
        }
        return fileName;
    }

    public static void read(String fullFilePath, AnalysisEventListener listener) {
        Class<?> clazz = getClassFromGenericSupperClass(listener.getClass());
        EasyExcel.read(fullFilePath, clazz, listener)
                 .sheet()
                 .doRead();
    }

    public static void read(MultipartFile file, AnalysisEventListener listener) throws IOException {
        Class<?> clazz = getClassFromGenericSupperClass(listener.getClass());
        EasyExcel.read(file.getInputStream(), clazz, listener)
                 .sheet()
                 .doRead();
    }
}
