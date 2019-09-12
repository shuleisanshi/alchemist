package com.yangbingdong.example.controller;

import com.youngbingdong.util.jwt.JwtPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 2019/9/12
 * @contact yangbingdong1994@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MyJwtPayload extends JwtPayload<MyJwtPayload> {

    private String name;

}
