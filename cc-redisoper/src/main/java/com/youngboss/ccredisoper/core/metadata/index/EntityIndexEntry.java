package com.youngboss.ccredisoper.core.metadata.index;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 19-3-23
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class EntityIndexEntry<T> {

	private String indexField;

	private String indexFieldCap;

	private int order;

	private boolean unique = false;

	private IndexReader<T> reader;

	private IndexWriter<T> writer;
}
