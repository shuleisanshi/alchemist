package com.yangbingdong.example.controller.vo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ybd
 * @date 2019/10/18
 * @contact yangbingdong1994@gmail.com
 */
public class DemoDataListener extends AnalysisEventListener<DemoData> {

    private List<DemoData> list = new ArrayList<>();

    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        System.out.println("读取到数据: " + data);
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("### 所有数据读取完毕, size: " + list.size());
    }
}
