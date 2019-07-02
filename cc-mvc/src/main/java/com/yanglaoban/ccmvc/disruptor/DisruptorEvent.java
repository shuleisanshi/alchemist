package com.yanglaoban.ccmvc.disruptor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 *
 * SOURCE 事件源
 */
@Slf4j
public class DisruptorEvent<SOURCE> {
	private SOURCE s;

	public SOURCE getSource() {
		return this.s;
	}

	public void setSource(SOURCE s) {
		this.s = s;
	}

	public Class getSourceClass() {
		return s.getClass();
	}

	public void clean() {
		this.s = null;
	}
}
