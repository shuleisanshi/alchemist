package com.yanglaoban.ccmvc.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public final class DisruptorUtil {

    public static BasicThreadFactory getThreadFactory(String pattern) {
        return new BasicThreadFactory.Builder().namingPattern(pattern)
											   .build();
    }

    public static <T> Disruptor<T> createDisruptor(EventFactory<T> eventFactory, int bufferSize) {
        Disruptor<T> disruptor = new Disruptor<>(eventFactory, bufferSize, getThreadFactory("disruptor-%d"), ProducerType.MULTI, new BlockingWaitStrategy());
        disruptor.setDefaultExceptionHandler(new DefaultDisruptorExceptionHandler<>());
        return disruptor;
    }

    public static void shutDownDisruptor(Disruptor disruptor) {
        if (disruptor != null) {
            try {
                disruptor.shutdown(5, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                log.error("Disruptor shutdown error!", e);
            }
        }
    }

    @Slf4j
    public static class DefaultDisruptorExceptionHandler<T> implements ExceptionHandler<T> {
        @Override
        public void handleEventException(Throwable ex, long sequence, T event) {
            log.error("Disruptor handler error: ", ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            log.error("Disruptor handler start error: ", ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            log.error("Disruptor handler shutdown error: ", ex);
        }
    }

}
