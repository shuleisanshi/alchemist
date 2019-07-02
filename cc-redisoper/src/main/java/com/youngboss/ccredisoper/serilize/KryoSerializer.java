package com.youngboss.ccredisoper.serilize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayOutputStream;

/**
 * @author ybd
 * @date 19-3-20
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class KryoSerializer implements Serializer {
	private static final KryoFactory factory = () -> {
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(false);
		((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
		return kryo;
	};

	private static final KryoPool pool = new KryoPool.Builder(factory).softReferences().build();

	public <T> byte[] serialize(T obj) {
		return pool.run(kryo -> {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Output output = new Output(stream);
			kryo.writeClassAndObject(output, obj);
			output.close();
			return stream.toByteArray();
		});
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(final byte[] objectData, Class<T> clazz) {
		return pool.run(kryo -> {
			Input input = new Input(objectData);
			return (T) kryo.readClassAndObject(input);
		});
	}
}
