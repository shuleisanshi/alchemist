package com.youngboss.ccredisoper;


import com.youngboss.ccredisoper.serilize.DefaultJDKSerializer;
import com.youngboss.ccredisoper.serilize.FSTSerializer;
import com.youngboss.ccredisoper.serilize.KryoSerializer;
import com.youngboss.ccredisoper.serilize.ProtostuffSerializer;
import com.youngboss.ccredisoper.serilize.Serializer;
import com.youngboss.ccredisoper.vo.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author ybd
 * @date 19-1-29
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class SerializeTest {

	static Serializer jdkSerializer = new DefaultJDKSerializer();
	static Serializer protostuffSerializer = new ProtostuffSerializer();
	static Serializer kryoSerializer = new KryoSerializer();
	static Serializer fSTSerializer = new FSTSerializer();

	static TestUser pojoBean = TestUser.buildUserByName("ybd");

	static byte[] jdkSerializeBytes = jdkSerializer.serialize(pojoBean);
	static byte[] protostuffSerializeBytes = protostuffSerializer.serialize(pojoBean);
	static byte[] kryoSerializeBytes = kryoSerializer.serialize(pojoBean);
	static byte[] fSTSerializeBytes = fSTSerializer.serialize(pojoBean);

	/**
	 * Java-Serialize序列化bytes长度:-> 667
	 * Protostuff序列化bytes长度:-> 81
	 * Kryo序列化bytes长度:-> 156
	 * FST序列化bytes长度:-> 207
	 */
	@Test
	public void showSerializeSize() {
		log.info("Java-Serialize序列化bytes长度:-> " + jdkSerializeBytes.length);
		log.info("Protostuff序列化bytes长度:-> " + protostuffSerializeBytes.length);
		log.info("Kryo序列化bytes长度:-> " + kryoSerializeBytes.length);
		log.info("FST序列化bytes长度:-> " + fSTSerializeBytes.length);
	}

	@Test
	public void deserializeTest() {
		TestUser jdkUser = jdkSerializer.deserialize(jdkSerializeBytes, TestUser.class);
		assertUser(jdkUser);

		TestUser protostuffUser = protostuffSerializer.deserialize(protostuffSerializeBytes, TestUser.class);
		assertUser(protostuffUser);

		TestUser kryoUser = kryoSerializer.deserialize(kryoSerializeBytes, TestUser.class);
		assertUser(kryoUser);

		TestUser fstUser = fSTSerializer.deserialize(fSTSerializeBytes, TestUser.class);
		assertUser(fstUser);
	}

	private void assertUser(TestUser user) {
		Assertions.assertThat(user)
				  .isNotNull()
				  .extracting(TestUser::getAge)
				  .isEqualTo(20);
		Assertions.assertThat(user.getFriends())
				  .hasSize(1)
				  .element(0)
				  .extracting(TestUser::getName)
				  .isEqualTo("yqy");
	}

}
