package com.youngbingdong.redisoper.perf;

import com.youngbingdong.redisoper.serilize.ProtostuffSerializer;
import com.youngbingdong.redisoper.serilize.Serializer;
import com.youngbingdong.redisoper.vo.TestUser;
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
 * @date 19-4-28
 * @contact yangbingdong1994@gmail.com
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1, time = 3)
@Measurement(iterations = 3, time = 5)
@Threads(15)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ProtostuffPerfTest {

	private static Serializer serializer = new ProtostuffSerializer();
	private static TestUser user = TestUser.buildUserById(1L);

	@Benchmark
	public byte[] protostuff() {
		return serializer.serialize(user);
	}

	public static void main(String[] args) throws RunnerException {
		Options options = new OptionsBuilder().include(ProtostuffPerfTest.class.getSimpleName())
											  .build();
		new Runner(options).run();
	}
}
