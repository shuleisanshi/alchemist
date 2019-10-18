package com.yangbingdong.example.controller;

import com.yangbingdong.auth.annotated.IgnoreAuth;
import com.yangbingdong.example.controller.vo.excel.DemoData;
import com.yangbingdong.example.controller.vo.excel.DemoDataListener;
import com.yangbingdong.mvc.annotated.Rest;
import com.youngbingdong.util.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author ybd
 * @date 2019/10/18
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/excel")
@Slf4j
public class ExcelController {

    @IgnoreAuth
    @GetMapping("/download")
    public void download() throws IOException {
        List<DemoData> list = DemoData.data1();
        list.addAll(DemoData.data2());
        ExcelUtil.writeToWeb("测试excel", list);
    }

    @IgnoreAuth
    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
        ExcelUtil.read(file, new DemoDataListener());
    }
}
