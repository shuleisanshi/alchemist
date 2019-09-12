package com.youngbingdong.util.jwt;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 2019/9/12
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class Jwt<T extends JwtPayload> {

    private JwtHeader jwtHeader;

    private T payload;

    private String sign;

}
