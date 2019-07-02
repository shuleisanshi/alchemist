package com.youngboss.ccutil.perf;

import com.youngboss.ccutil.time.SystemTimer;
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
 * @date 19-4-15
 * @contact yangbingdong1994@gmail.com
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 3)
@Threads(10)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SystemTimePerfTest {

	@Benchmark
	public long systemTime() {
		return System.currentTimeMillis();
	}

	@Benchmark
	public long customTime() {
		return SystemTimer.now();
	}

	public static void main(String[] args) throws RunnerException {
		Options options = new OptionsBuilder().include(SystemTimePerfTest.class.getSimpleName())
											  .build();
		new Runner(options).run();
	}
}
