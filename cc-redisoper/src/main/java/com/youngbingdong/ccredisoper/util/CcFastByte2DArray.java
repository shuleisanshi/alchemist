package com.youngbingdong.ccredisoper.util;

import java.util.function.Consumer;

/**
 * @author ybd
 * @date 19-3-26
 * @contact yangbingdong1994@gmail.com
 */
public class CcFastByte2DArray {

	private byte[][] byteElements;

	private int size;

	public CcFastByte2DArray(int size) {
		this.size = size;
		byteElements = new byte[size][];
	}

	public void add(byte[] b) {
		byteElements[size++] = b;
	}


	public void pageArray(int pageSize, Consumer<byte[][]> consumer) {
		int totalPage = calculateTotalPage(this.size, pageSize);
		byte[][] copiedArray = new byte[pageSize][];
		for (int i = 0; i < totalPage; i++) {
			if (i == totalPage - 1) {
				int lastPageSize = size - i * pageSize;
				byte[][] lastPage = new byte[lastPageSize][];
				System.arraycopy(byteElements, i * pageSize, lastPage, 0, lastPageSize);
				consumer.accept(lastPage);
				break;
			}
			System.arraycopy(byteElements, i * pageSize, copiedArray, 0, pageSize);
			consumer.accept(copiedArray);
		}
	}

	public byte[][] toArray() {
		return byteElements;
	}

	private static int calculateTotalPage(int total, int pageSize){
		return (total + pageSize - 1) / pageSize;
	}
}
