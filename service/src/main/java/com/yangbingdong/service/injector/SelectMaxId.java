package com.yangbingdong.service.injector;

import cn.hutool.core.text.StrFormatter;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author ybd
 * @date 2019/9/2
 * @contact yangbingdong1994@gmail.com
 */
public class SelectMaxId extends AbstractMethod {

    private static final String MAX_SQL = "SELECT IFNULL(MAX({}), 0) FROM {}";
    private static final String MAX_METHOD_NAME = "selectMaxId";

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = StrFormatter.format(MAX_SQL, tableInfo.getKeyColumn(), tableInfo.getTableName());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, MAX_METHOD_NAME, sqlSource, Long.class);
    }
}
