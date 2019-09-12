package com.youngbingdong.util.perf.jwt;

import com.youngbingdong.util.jwt.JwtPayload;
import com.youngbingdong.util.jwt.JwtUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author ybd
 * @date 2019/9/12
 * @contact yangbingdong1994@gmail.com
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 4)
@Threads(10)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class JwtPerfTest {

    private static final JwtPayload defaultPayload = new JwtPayload().setId(666L);

    @Benchmark
    public String genJwt() {
        return JwtUtil.grantJwt(defaultPayload, "yangbigndong", 5000L);
    }

    @Benchmark
    public JwtPayload parse() {
        return JwtUtil
                .parseJwt("eyJleHBpcmUiOjE1NjgyNzQ4NTQ5MDZ9.eyJpZCI6NjY2fQ==.0e875bb913e1d7f900d318d6a4b33171", "yangbingdong", JwtPayload.class)
                .getPayload();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(JwtPerfTest.class.getSimpleName())
                                              .build();
        new Runner(options).run();
    }
}
