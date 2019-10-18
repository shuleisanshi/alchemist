package com.youngbingdong.util.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;

/**
 * @author ybd
 * @date 2019/10/16
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class MergeColumnHandler implements CellWriteHandler, RowWriteHandler, SheetWriteHandler {
    private int headSize;
    private Set<String> mergeColumnRegister;
    private Map<String, List<String>> sameAsMergeMap = new HashMap<>(16);
    private Map<String, Integer> headerColumnIndex = new HashMap<>(16);
    private Map<String, List<Integer>> sameAsMergeColumnIndexMap = new HashMap<>(16);
    private int needMergeColumnIndexFlag;
    private int dataFlowSizeAccumulator;
    private Map<String, Object[]> needMergeValueAndIndexPair = new HashMap<>(16);

    private final int totalSize;

    private MergeColumnHandler(int totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, int relativeRowIndex, boolean isHead) {
        String header = head.getHeadNameList().get(0);
        if (isHead) {
            headerColumnIndex.put(header, head.getColumnIndex());
            if (mergeColumnRegister.contains(header)) {
                needMergeColumnIndexFlag |= 1 << head.getColumnIndex();
            }
            headSize++;
            if (writeSheetHolder.getExcelWriteHeadProperty().getHeadMap().size() == headSize) {
                if (CollUtil.isNotEmpty(sameAsMergeMap)) {
                    sameAsMergeMap.forEach((k, v) -> {
                        List<Integer> indexList = v.stream()
                                                   .map(e -> headerColumnIndex.get(e))
                                                   .filter(Objects::nonNull)
                                                   .collect(toList());
                        if (CollUtil.isNotEmpty(indexList)) {
                            sameAsMergeColumnIndexMap.put(k, indexList);
                        }
                    });
                }
            }
            return;
        }
        int index = 1 << head.getColumnIndex();
        if ((needMergeColumnIndexFlag & index) == index) {
            Object[] pair = needMergeValueAndIndexPair.get(header);
            int currentRowIndex = cell.getRowIndex();
            String currentRwoValue = cellData.getStringValue();
            if (pair != null) {
                String value = (String) pair[0];
                Integer startMergeRow = (Integer) pair[1];
                if (value.equals(currentRwoValue)) {
                    if (totalSize == dataFlowSizeAccumulator) {
                        merge(writeSheetHolder, cell, startMergeRow, currentRowIndex, true, header);
                    }
                } else {
                    pair[0] = currentRwoValue;
                    pair[1] = currentRowIndex;
                    if (startMergeRow + 1 == currentRowIndex) {
                        return;
                    }
                    merge(writeSheetHolder, cell, startMergeRow, currentRowIndex, false, header);

                }
            } else {
                Object[] array = new Object[]{currentRwoValue, currentRowIndex};
                needMergeValueAndIndexPair.put(header, array);
            }
        }
    }

    private void merge(WriteSheetHolder writeSheetHolder, Cell cell, Integer startMergeRow, int currentRowIndex, boolean lastRow, String header) {
        int endMergeRwo = lastRow ? currentRowIndex : currentRowIndex - 1;
        CellRangeAddress cellRangeAddress = new CellRangeAddress(startMergeRow, endMergeRwo, cell.getColumnIndex(), cell.getColumnIndex());
        writeSheetHolder.getSheet().addMergedRegionUnsafe(cellRangeAddress);
        List<Integer> sameAsMergeList = sameAsMergeColumnIndexMap.get(header);
        if (sameAsMergeList != null) {
            for (Integer columnIndex : sameAsMergeList) {
                cellRangeAddress = new CellRangeAddress(startMergeRow, endMergeRwo, columnIndex, columnIndex);
                writeSheetHolder.getSheet().addMergedRegionUnsafe(cellRangeAddress);
            }
        }
    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, int relativeRowIndex, boolean isHead) {
        if (isHead) {
            return;
        }
        dataFlowSizeAccumulator++;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Class clazz = writeSheetHolder.getClazz();
        Field[] declaredFields = clazz.getDeclaredFields();
        mergeColumnRegister = new HashSet<>(declaredFields.length);

        Map<Field, ExcelProperty> fieldExcelPropertyMap = new HashMap<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            fieldExcelPropertyMap.put(declaredField, getMergedAnnotation(declaredField, ExcelProperty.class));
        }
        fieldExcelPropertyMap = checkAnnotation(declaredFields);

        for (Field declaredField : declaredFields) {
            MergeColumn mergeColumn = getMergedAnnotation(declaredField, MergeColumn.class);
            if (mergeColumn != null) {
                ExcelProperty excelProperty = fieldExcelPropertyMap.get(declaredField);
                if (excelProperty == null) {
                    log.warn("@ExcelProperty not found");
                    return;
                }
                String key = excelProperty.value()[0];
                mergeColumnRegister.add(key);
                if (mergeColumn.sameAs().length > 0) {
                    sameAsMergeMap.put(key, Arrays.asList(mergeColumn.sameAs()));
                }
            }
        }
    }

    private Map<Field, ExcelProperty> checkAnnotation(Field[] declaredFields) {
        Map<Field, ExcelProperty> fieldExcelPropertyMap = new HashMap<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            ExcelProperty excelProperty = getMergedAnnotation(declaredField, ExcelProperty.class);
            ExcelIgnore excelIgnore = getMergedAnnotation(declaredField, ExcelIgnore.class);
            if (excelProperty == null && excelIgnore == null) {
                throw new IllegalArgumentException("POI 实体类每个字段必须贴有@ExcelProperty或@ExcelIgnore注解!");
            }
            if (excelProperty != null) {
                if (excelProperty.value().length != 1 || StrUtil.isBlank(excelProperty.value()[0])) {
                    throw new IllegalArgumentException("@ExcelProperty 必须有且只有一个 value");
                }
                fieldExcelPropertyMap.put(declaredField, excelProperty);
            }
        }
        return fieldExcelPropertyMap;
    }

    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, int rowIndex, int relativeRowIndex, boolean isHead) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, int relativeRowIndex, boolean isHead) {
    }

    public static MergeColumnHandler create(int totalSize) {
        return new MergeColumnHandler(totalSize);
    }
}
