package com.yangbingdong.service.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangbingdong.service.core.Service;
import com.youngbingdong.util.Pager;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author ybd
 * @date 19-4-15
 * @contact yangbingdong1994@gmail.com
 */
public final class MpPager<T> {

	private int pageSize;

	public Service<T> service;

	private QueryWrapper<T> queryWrapper;

	private int totalPage;

	private MpPager(int pageSize, Service<T> service, QueryWrapper<T> queryWrapper) {
		this.pageSize = pageSize;
		this.service = service;
		this.queryWrapper = queryWrapper;

		int count = service.count(queryWrapper);
		this.totalPage = Pager.calculateTotalPage(count, pageSize);
	}

	public static <T> MpPager<T> of(int pageSize, Service<T> service, QueryWrapper<T> queryWrapper) {
		return new MpPager<>(pageSize, service, queryWrapper);
	}

	public void doPage(Consumer<List<T>> pageConsumer) {
		for (int i = 0; i < totalPage; i++) {
			IPage<T> page = service.page(new Page<T>(i + 1, pageSize).setOptimizeCountSql(false).setSearchCount(false), queryWrapper);
			pageConsumer.accept(page.getRecords());
		}
	}

}
