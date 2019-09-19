package com.youngbingdong.util.jwt;

/**
 * @author ybd
 * @date 2019/9/12
 * @contact yangbingdong1994@gmail.com
 */
public abstract class JwtPayload<T extends JwtPayload<T>> {
    private String id;

    @SuppressWarnings("unchecked")
    private transient T thisAsT = (T) this;

    public String getId() {
        return id;
    }

    public T setId(String id) {
        this.id = id;
        return thisAsT;
    }

    @Override
    public String toString() {
        return "JwtPayload{" +
                "id=" + id +
                '}';
    }
}
