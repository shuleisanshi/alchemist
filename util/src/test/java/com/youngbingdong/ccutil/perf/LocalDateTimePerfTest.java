package com.youngbingdong.ccutil.perf;

import com.youngbingdong.ccutil.time.SystemTimer;
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

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author ybd
 * @date 19-4-15
 * @contact yangbingdong1994@gmail.com
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 3)
@Threads(10)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class LocalDateTimePerfTest {

	@Benchmark
	public long nativeTimeMillis() {
		return System.currentTimeMillis();
	}

	@Benchmark
	public long customTimeMillis() {
		return SystemTimer.now();
	}

	@Benchmark
	public LocalDateTime nativeLocalDateTime() {
		return LocalDateTime.now();
	}

	@Benchmark
	public LocalDateTime customLocalDateTime() {
		return SystemTimer.nowDateTime();
	}

	public static void main(String[] args) throws RunnerException {
		Options options = new OptionsBuilder().include(LocalDateTimePerfTest.class.getSimpleName())
											  .build();
		new Runner(options).run();
	}
}
