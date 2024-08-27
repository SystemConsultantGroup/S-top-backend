package com.scg.stop.global.excel;

import com.scg.stop.global.excel.annotation.ExcelColumn;
import com.scg.stop.global.excel.annotation.ExcelDownload;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ExcelUtil {
    public static final int MAX_ROW_ACCESS_SIZE = 10000;

    public Excel of(String filename, Workbook workbook) {
        return new Excel(filename, workbook);
    }



    public <T> String getFilename(Workbook workbook, Class<T> clazz) {
        LocalDateTime time = LocalDateTime.now();
        if( workbook instanceof SXSSFWorkbook) {
            return String.format("%s-%s.xlsx",clazz.getDeclaredAnnotation(ExcelDownload.class).fileName(), time);
        }
        return String.format("%s-%s.xls",clazz.getDeclaredAnnotation(ExcelDownload.class).fileName(), time);
    }

    public <T> Workbook createExcel(List<T> lists, Class<T> clazz) {
        SXSSFWorkbook workbook = createWorkBook();
        workbook.setCompressTempFiles(true);
        SXSSFSheet sheet = workbook.createSheet(clazz.getDeclaredAnnotation(ExcelDownload.class).sheetName());
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        List<String> headers = Arrays.stream(clazz.getFields())
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

    public <T> void append(Workbook workbook, List<T> data, Class<T> clazz) {

    }

    private SXSSFWorkbook createWorkBook() {
        return new SXSSFWorkbook(MAX_ROW_ACCESS_SIZE);
    }

    private void renderHeaderRow(Row firstRow, List<String> headers) {
        int cellIdx = 0;
        for(String header: headers) {
            Cell headerCell = firstRow.createCell(cellIdx++);
            renderCellValue(headerCell, header);
            setHeaderCellStyle(headerCell);
        }
    }

    private <T> void renderBodyRow(Row row, T data, Class<T> clazz) {
        Field[] fields = clazz.getFields();
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
