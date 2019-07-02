package com.youngboss.ccredisoper.extend.dlock;

/**
 * @author ybd
 * @date 19-5-16
 * @contact yangbingdong1994@gmail.com
 */
public interface DLock {

	<T> T tryLock(String lockKey, AfterAcquireCommand<T> afterAcquireCommand) throws Throwable;

	<T> T tryLock(String lockKey, long waitSecond, long leaseSecond, AfterAcquireCommand<T> afterAcquireCommand) throws Throwable;
}
