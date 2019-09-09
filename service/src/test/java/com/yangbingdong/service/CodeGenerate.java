package com.yangbingdong.service;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.IdType.INPUT;

/**
 * 这个是新版本的
 *
 * @author ybd
 * @date 18-7-9
 * @contact yangbingdong1994@gmail.com
 * <p>
 * 代码生成器配置 https://mp.baomidou.com/config/generator-config.html#%E5%9F%BA%E6%9C%AC%E9%85%8D%E7%BD%AE
 */
public class CodeGenerate {

    private final static String AUTHOR = "yangbingdong";
    private final static String OUTPUT_DIR = "/home/ybd/data/git-repo/github/own/alchemist/service/src/main";
    private final static String BASE_PACKAGE = "com.yangbingdong.service.gen";


    private final static String[] TABLES = {"test_user"};
    private final static String DB_URL = "jdbc:mysql://127.0.0.1:3306/service_test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true";
    private final static String DB_USERNAME = "root";
    private final static String DB_PASSWORD = "root";

    private final static boolean IS_USE_REDIS_CACHE = false;

    @Test
    @Ignore
    public void generateCode() {
        generateByTables();
    }

    private void generateByTables() {
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine()) // 选择 freemarker 引擎，默认 Veloctiy
                     .setPackageInfo(getPackageConfig())
                     .setDataSource(getDataSourceConfig())
                     .setStrategy(getStrategyConfig())
                     .setCfg(getFileOutConfig())
                     .setTemplate(new TemplateConfig().setXml(null).setController(null))
                     .setGlobalConfig(getGlobalConfig());
        autoGenerator.execute();
    }

    private InjectionConfig getFileOutConfig() {
        // 自定义xml输出目录配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                /*Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);*/
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return OUTPUT_DIR + "/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    private GlobalConfig getGlobalConfig() {
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(false)
              .setBaseResultMap(true)
              .setBaseColumnList(true)
              .setAuthor(AUTHOR)
              .setOutputDir(OUTPUT_DIR + "/java")
              .setFileOverride(true)
              .setIdType(INPUT)
              .setOpen(false);
        config.setServiceName("%sService");
        return config;
    }

    private StrategyConfig getStrategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setSkipView(true)
                      // .setTablePrefix("test_")
                      .setEntityLombokModel(true)
                      .setNaming(NamingStrategy.underline_to_camel)
                      .setEntityColumnConstant(true)
                      .setEntityBooleanColumnRemoveIsPrefix(false)
                      .setRestControllerStyle(true)
                      .setInclude(TABLES)
                      .setControllerMappingHyphenStyle(true)
                      .setLogicDeleteFieldName("deleted");

        List<TableFill> tableFills = new ArrayList<>();
        tableFills.add(new TableFill("create_time", FieldFill.INSERT));
        tableFills.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
        strategyConfig.setTableFillList(tableFills);

        if (IS_USE_REDIS_CACHE) {
            strategyConfig.setSuperServiceClass("com.yangbingdong.service.core.Service")
                          .setSuperServiceImplClass("com.yangbingdong.service.core.ServiceImpl")
                          .setSuperMapperClass("com.yangbingdong.service.core.CustomBaseMapper");
        }
        return strategyConfig;
    }

    private DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                        .setUrl(DB_URL)
                        .setUsername(DB_USERNAME)
                        .setPassword(DB_PASSWORD)
                        .setDriverName("com.mysql.jdbc.Driver")
                        .setTypeConvert(new CustomMySqlTypeConvert());
        return dataSourceConfig;
    }

    private PackageConfig getPackageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(BASE_PACKAGE)
                     .setController("mvc.controller")
                     .setEntity("domain.entity")
                     .setMapper("domain.mapper")
                     .setService("domain.service")
                     .setServiceImpl("domain.service.impl")
                     .setModuleName("user");
        return packageConfig;
    }

    static class CustomMySqlTypeConvert implements ITypeConvert {

        @Override
        public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
            String t = fieldType.toLowerCase();
            if (t.contains("char")) {
                return DbColumnType.STRING;
            } else if (t.contains("bigint")) {
                return DbColumnType.LONG;
            } else if (t.contains("tinyint(1)")) {
                return DbColumnType.BOOLEAN;
            } else if (t.contains("tinyint(4)")) {
                return DbColumnType.BYTE;
            } else if (t.contains("int")) {
                return DbColumnType.INTEGER;
            } else if (t.contains("text")) {
                return DbColumnType.STRING;
            } else if (t.contains("bit")) {
                return DbColumnType.BOOLEAN;
            } else if (t.contains("decimal")) {
                return DbColumnType.BIG_DECIMAL;
            } else if (t.contains("clob")) {
                return DbColumnType.CLOB;
            } else if (t.contains("blob")) {
                return DbColumnType.BLOB;
            } else if (t.contains("binary")) {
                return DbColumnType.BYTE_ARRAY;
            } else if (t.contains("float")) {
                return DbColumnType.FLOAT;
            } else if (t.contains("double")) {
                return DbColumnType.DOUBLE;
            } else if (t.contains("json") || t.contains("enum")) {
                return DbColumnType.STRING;
            } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
                switch (globalConfig.getDateType()) {
                    case ONLY_DATE:
                        return DbColumnType.DATE;
                    case SQL_PACK:
                        switch (t) {
                            case "date":
                            case "year":
                                return DbColumnType.DATE_SQL;
                            case "time":
                                return DbColumnType.TIME;
                            default:
                                return DbColumnType.TIMESTAMP;
                        }
                    case TIME_PACK:
                        switch (t) {
                            case "date":
                                return DbColumnType.LOCAL_DATE;
                            case "time":
                                return DbColumnType.LOCAL_TIME;
                            case "year":
                                return DbColumnType.YEAR;
                            default:
                                return DbColumnType.LOCAL_DATE_TIME;
                        }
                }
            }
            return DbColumnType.STRING;
        }
    }
}
