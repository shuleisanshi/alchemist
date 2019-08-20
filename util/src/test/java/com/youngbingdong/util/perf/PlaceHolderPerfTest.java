package com.youngbingdong.util.perf;

import cn.hutool.core.util.StrUtil;
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

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author ybd
 * @date 19-4-23
 * @contact yangbingdong1994@gmail.com
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 3)
@Threads(10)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class PlaceHolderPerfTest {

	private static final String java_tmp = "{0}-{1}";
	private static final String tmp = "{}-{}";
	private static final Object[] args = new Object[]{"first", "second"};

	@Benchmark
	public String javaReplace() {
		return MessageFormat.format(java_tmp, args);
	}

    @Benchmark
    public String hutoolReplace() {
        return StrUtil.format(tmp, args);
    }

	public static void main(String[] args) throws RunnerException {
		Options options = new OptionsBuilder().include(PlaceHolderPerfTest.class.getSimpleName())
											  .build();
		new Runner(options).run();
	}
}
