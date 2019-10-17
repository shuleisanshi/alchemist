# 通用工具包

## excel

底层是 Ali Easyexcel,

导出 excel:

```java
// 直接导出
@Test
public void exportMerge() {
    String fileName = TestFileUtil.getPath() + "writeMerge" + System.currentTimeMillis() + ".xlsx";
    List<DemoData> list = data1();
    list.addAll(data2());
    ExcelUtil.export(fileName, list);
}

// 大数据分页导出
@Test
public void exportMergeByDataFlow() {
    String fileName = TestFileUtil.getPath() + "writeMerge" + System.currentTimeMillis() + ".xlsx";
    AtomicInteger page = new AtomicInteger(0);
    ExcelUtil.exportByDataFlow(fileName, 20, () -> getByPage(page.incrementAndGet()));
}
```

实体类:

```java
@Data
public class DemoData {

    public static final String DOUBEL_DATA_HEAD = "数字标题";

    // 相同值合并单元格, 只支持String类型, sameAs表示 `数字标题`这一列合并的格式与当前列一致
    @MergeColumn(sameAs = {DOUBEL_DATA_HEAD})
    // 指定head
    @ExcelProperty("字符串标题")
    private String string;

    // 列宽, 可写到类名上指定全局的列宽
    @ColumnWidth(30)
    // 日期格式化
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ExcelProperty("日期标题")
    private Date date;

    // 数字格式化
    @NumberFormat("#.##%")
    @ExcelProperty(DOUBEL_DATA_HEAD)
    private Double doubleData;

    // 不导出该列
    @ExcelIgnore
    private String ignoreValue
}
```

## function

具体看 `Trier` 类.

包装 Java8 的 `Function` 等类, 将检测异常封装为 `RuntimeException`.

## http

`HttpAccessor` 是基于 `OkHttp` 的 Http 工具类.

### jwt

`JwtUtil` 自定义实现的 jwt 工具类.

## reflect

`BeanUtil` 复制属性.

`TypeUtil` 获取接口泛型.

## spring

`RequestHolder` 获取当前请求, 响应, 以及 requestBody.

`SpELParser` 解析 SpEL.

## time

`DateUtil` & `LocalDateTimeUtil` 时间相关工具类.

`SystemTimer` 对 `System.currentTimeMillis()` 的性能优化.

## 其他

`NumericConvertUtil` 将 10 进制转成自定义 62 进制; 异或加密解密.

`Pager` 逻辑分页.