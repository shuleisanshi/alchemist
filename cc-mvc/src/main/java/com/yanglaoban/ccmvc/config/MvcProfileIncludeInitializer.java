package com.yanglaoban.ccmvc.config;

import com.youngboss.ccutil.spring.SystemProfileAppender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author ybd
 * @date 19-5-20
 * @contact yangbingdong1994@gmail.com
 */
public class MvcProfileIncludeInitializer implements SpringApplicationRunListener {

	public static final String PROFILE_MVC = "mvc";

	public MvcProfileIncludeInitializer(SpringApplication application, String[] args) {
	}

	@Override
	public void starting() {
		SystemProfileAppender.appendProfile(PROFILE_MVC);
	}

	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {

	}

	@Override
	public void contextPrepared(ConfigurableApplicationContext context) {

	}

	@Override
	public void contextLoaded(ConfigurableApplicationContext context) {

	}

	@Override
	public void started(ConfigurableApplicationContext context) {

	}

	@Override
	public void running(ConfigurableApplicationContext context) {

	}

	@Override
	public void failed(ConfigurableApplicationContext context, Throwable exception) {

	}
}
