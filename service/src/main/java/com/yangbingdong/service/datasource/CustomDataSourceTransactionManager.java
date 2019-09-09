package com.yangbingdong.service.datasource;

import com.youngbingdong.redisoper.core.RedisTransactionResourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author ybd
 * @date 19-5-14
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class CustomDataSourceTransactionManager extends DataSourceTransactionManager {

	private static final long serialVersionUID = -5831041749053502702L;

	public CustomDataSourceTransactionManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		super.doCleanupAfterCompletion(transaction);
		RedisTransactionResourceHolder.clear();
	}
}
