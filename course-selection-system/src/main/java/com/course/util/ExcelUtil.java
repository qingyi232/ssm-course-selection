package com.course.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class ExcelUtil {
    
    // 导出Excel
    public static void exportExcel(HttpServletResponse response, String fileName, 
            String[] headers, List<Object[]> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        
        // 创建标题行样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        // 写入标题行
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 写入数据行
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Object[] rowData = data.get(i);
            for (int j = 0; j < rowData.length; j++) {
                Cell cell = row.createCell(j);
                if (rowData[j] != null) {
                    cell.setCellValue(rowData[j].toString());
                }
            }
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + 
            new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    
    // 读取Excel文件
    public static List<Map<String, String>> readExcel(InputStream is, String fileName) throws IOException {
        List<Map<String, String>> list = new ArrayList<>();
        Workbook workbook;
        
        if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(is);
        } else {
            workbook = new HSSFWorkbook(is);
        }
        
        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            workbook.close();
            return list;
        }
        
        // 读取标题
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            headers.add(getCellValue(cell));
        }
        
        // 读取数据
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            Map<String, String> map = new HashMap<>();
            boolean hasData = false;
            for (int j = 0; j < headers.size(); j++) {
                Cell cell = row.getCell(j);
                String value = getCellValue(cell);
                if (value != null && !value.isEmpty()) hasData = true;
                map.put(headers.get(j), value);
            }
            if (hasData) list.add(map);
        }
        
        workbook.close();
        return list;
    }
    
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: 
                double num = cell.getNumericCellValue();
                if (num == (long) num) return String.valueOf((long) num);
                return String.valueOf(num);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }
}
