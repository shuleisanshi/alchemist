package com.yangbingdong.mvc.shutdown;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;

/**
 * @author ybd
 * @date 18-5-11
 * @contact yangbingdong1994@gmail.com
 */
public class GracefulShutdownWrapper implements HandlerWrapper {
	private GracefulShutdownHandler gracefulShutdownHandler;

	@Override
	public HttpHandler wrap(HttpHandler handler) {
		if(gracefulShutdownHandler == null) {
			this.gracefulShutdownHandler = new GracefulShutdownHandler(handler);
		}
		return gracefulShutdownHandler;
	}

	public GracefulShutdownHandler getGracefulShutdownHandler() {
		return gracefulShutdownHandler;
	}
}
