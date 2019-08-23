package com.youngbingdong.util.http;

/**
 * @author ybd
 * @date 2019/8/23
 * @contact yangbingdong1994@gmail.com
 */
public class HttpAccessException extends RuntimeException {
    private static final long serialVersionUID = -8944871301130324240L;

    public HttpAccessException(Exception e) {
        super(e);
    }
}
