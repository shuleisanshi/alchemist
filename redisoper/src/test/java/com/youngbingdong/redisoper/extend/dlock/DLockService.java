package com.youngbingdong.redisoper.extend.dlock;

import com.youngbingdong.redisoper.extend.dlock.annotated.Lock;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class DLockService {

	@Lock(key = "'BuziKey:' + #args[0]")
	public String lockTest(Long id) {
		return id.toString();
	}
}
