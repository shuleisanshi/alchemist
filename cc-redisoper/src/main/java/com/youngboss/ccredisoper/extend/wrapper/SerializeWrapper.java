package com.youngboss.ccredisoper.extend.wrapper;

import lombok.Data;

/**
 * @author ybd
 * @date 19-4-13
 * @contact yangbingdong1994@gmail.com
 */
@Data
public class SerializeWrapper {
	private Object data;

	public SerializeWrapper(Object data) {
		this.data = data;
	}

	public static SerializeWrapper of(Object data) {
		return new SerializeWrapper(data);
	}



}

