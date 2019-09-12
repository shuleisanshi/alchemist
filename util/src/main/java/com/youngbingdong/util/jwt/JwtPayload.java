package com.youngbingdong.util.jwt;

/**
 * @author ybd
 * @date 2019/9/12
 * @contact yangbingdong1994@gmail.com
 */
public class JwtPayload<T extends JwtPayload<T>> {
    private Long id;

    @SuppressWarnings("unchecked")
    private final T thisAsT = (T) this;

    public Long getId() {
        return id;
    }

    public T setId(Long id) {
        this.id = id;
        return thisAsT;
    }
}
