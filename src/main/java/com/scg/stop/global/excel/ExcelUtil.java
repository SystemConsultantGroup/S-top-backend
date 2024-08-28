package com.scg.stop.global.excel;

import com.scg.stop.global.excel.annotation.ExcelColumn;
import com.scg.stop.global.excel.annotation.ExcelDownload;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ExcelUtil {
    public static final int MAX_ROW_ACCESS_SIZE = 10000;

    public Excel toExcel(String filename, SXSSFWorkbook workbook) {
        return new Excel(filename, workbook);
    }



    public <T> String getFilename(Workbook workbook, Class<T> clazz) {
        LocalDateTime time = LocalDateTime.now();
        if( workbook instanceof SXSSFWorkbook) {
            return String.format("%s-%s.xlsx",clazz.getDeclaredAnnotation(ExcelDownload.class).fileName(), time);
        }
        return String.format("%s-%s.xls",clazz.getDeclaredAnnotation(ExcelDownload.class).fileName(), time);
    }

    public <T> SXSSFWorkbook createExcel(List<T> lists, Class<T> clazz) {
        SXSSFWorkbook workbook = createWorkBook();
        workbook.setCompressTempFiles(true);
        SXSSFSheet sheet = workbook.createSheet(clazz.getAnnotation(ExcelDownload.class).sheetName());
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        List<String> headers = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .map( field -> field.getAnnotation(ExcelColumn.class).headerName())
                .toList();
        renderHeaderRow(row, headers);
        for(T data: lists) {
            row = sheet.createRow(rowNum++);
            renderBodyRow(row, data, clazz);
        }
        return workbook;
    }

    public <T> SXSSFWorkbook append(SXSSFWorkbook workbook, List<T> lists, Class<T> clazz) {
        SXSSFSheet sheet = workbook.getSheet(
                clazz.getAnnotation(ExcelDownload.class).sheetName()
        );
        int rowNum = sheet.getLastRowNum() + 1;
        Row row;
        for(T data: lists) {
            row = sheet.createRow(rowNum++);
            renderBodyRow(row, data, clazz);
        }
        return workbook;
    }

    public <T> List<T> fromExcel(MultipartFile file, Class<T> clazz) throws Exception {
        XSSFWorkbook workbook = null;
        List<T> result = new ArrayList<>();
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0); //header
            int columns = headerRow.getPhysicalNumberOfCells();
            Row row;

            Map<Integer, Field> columnFieldMap = new HashMap<>();
            Field[] fields = clazz.getFields();

            for (int i = 0; i < columns; i++) {
                String headerValue = headerRow.getCell(i).getStringCellValue();

                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelColumn.class)) {
                        ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                        if (excelColumn.headerName().equals(headerValue)) {
                            field.setAccessible(true);
                            columnFieldMap.put(i, field);
                            break;
                        }
                    }
                }
            }
            for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                T dto = clazz.getDeclaredConstructor().newInstance();
                for(Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {
                    int columnIdx = entry.getKey();
                    Field field = entry.getValue();
                    Cell cell = row.getCell(columnIdx);
                    if (cell != null) {
                        Object cellValue = getCellValue(cell, field.getType());
                        field.set(dto, cellValue);
                    }
                }
                result.add(dto);
            }
        } catch (Exception e) {
            if(workbook != null) {
                workbook.close();
            }
            throw new RuntimeException(e);
        }
        return result;
    }

    private Object getCellValue(Cell cell, Class<?> fieldType) {
        switch (cell.getCellType()) {
            case STRING:
                return convertValue(cell.getStringCellValue(), fieldType);
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return convertValue(cell.getDateCellValue(), fieldType);
                } else {
                    return convertValue(cell.getNumericCellValue(), fieldType);
                }
            case BOOLEAN:
                return convertValue(cell.getBooleanCellValue(), fieldType);
            case FORMULA:
                return convertValue(cell.getCellFormula(), fieldType);
            default:
                return null;
        }
    }

    private Object convertValue(Object value, Class<?> fieldType) {
        if (value == null) {
            return null;
        }

        if (fieldType.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (fieldType == String.class) {
            return value.toString();
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return ((Number) value).intValue();
        } else if (fieldType == Long.class || fieldType == long.class) {
            return ((Number) value).longValue();
        } else if (fieldType == Double.class || fieldType == double.class) {
            return ((Number) value).doubleValue();
        } else if (fieldType == Float.class || fieldType == float.class) {
            return ((Number) value).floatValue();
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.valueOf(value.toString());
        } else if (fieldType == java.util.Date.class && value instanceof java.util.Date) {
            return value;
        } else {
            return value.toString();
        }
    }

    private SXSSFWorkbook createWorkBook() {
        return new SXSSFWorkbook(MAX_ROW_ACCESS_SIZE);
    }

    private void renderHeaderRow(Row firstRow, List<String> headers) {
        int cellIdx = 0;
        for(String header: headers) {
            Cell headerCell = firstRow.createCell(cellIdx++);
            setHeaderCellStyle(headerCell);
            renderCellValue(headerCell, header);
        }
    }

    private <T> void renderBodyRow(Row row, T data, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        int cellIdx = 0;
        for(Field field: fields) {
            Cell cell = row.createCell(cellIdx++);
            Object value;
            field.setAccessible(true);
            try {
                value = field.get(data);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            field.setAccessible(false);
            renderCellValue(cell, value);
        }
    }

    private void renderCellValue(Cell cell, Object value) {
        if(value instanceof Number num) {
            cell.setCellValue(num.doubleValue());
            return;
        }
        cell.setCellValue(value == null ? "" : value.toString());
    }

    private void setHeaderCellStyle(Cell cell) {
        Workbook workbook = cell.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);

        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        cell.setCellStyle(cellStyle);
    }
}
