package com.yangbingdong.example.controller;

import com.yangbingdong.mvc.log.OpLogContext;
import com.yangbingdong.mvc.log.core.OpLogEventSourceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 2019/9/10
 * @contact yangbingdong1994@gmail.com
 */
@Component
@Slf4j
public class CustomOpLogEventContextHandler implements OpLogEventSourceHandler<OpLogContext> {
    @Override
    public void handlerSource(OpLogContext source) throws Exception {
        source.parseOperatingSystemAndBrowser()
              .parseReceiveData();
        log.info("This is custom opLog: {}", source);
    }
}
