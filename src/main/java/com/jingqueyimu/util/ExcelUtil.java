package com.jingqueyimu.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.jingqueyimu.handler.excel.IExcelExportHandler;
import com.jingqueyimu.handler.excel.IExcelImportHandler;

/**
 * Excel工具类
 *
 * @author zhuangyilian
 */
public class ExcelUtil {
    
    /**
     * 导入Excel
     *
     * @param is 输入流
     * @param keys 表头映射key
     * @param handler 导入处理器
     * @return
     */
    public static String importExcel(InputStream is, String[] keys, IExcelImportHandler handler) {
        StringBuffer errMsg = new StringBuffer();
        int succCount = 0;
        int failCount = 0;
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            // 首行数
            int firstRowNum = sheet.getFirstRowNum();
            // 尾行数
            int lastRowNum = sheet.getLastRowNum();
            // 表头行
            HSSFRow headerRow = sheet.getRow(firstRowNum);
            // 行
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                HSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                try {
                    JSONObject data = new JSONObject();
                    // 列
                    for (int j = 0; j < keys.length; j++) {
                        // 表头单元格
                        HSSFCell headerCell = headerRow.getCell((short) j);
                        if (headerCell == null) {
                            continue;
                        }
                        // 数据单元格
                        HSSFCell cell = row.getCell((short) j);
                        if (cell == null) {
                            continue;
                        }
                        data.put(keys[j], getCellValue(cell));
                    }
                    handler.handle(data);
                    succCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                    failCount++;
                    errMsg.append("第").append(i).append("行数据处理失败：").append(e.getMessage()).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String msg = "成功：" + succCount + "，失败：" + failCount + "！" + System.lineSeparator() 
            + "失败信息——" + System.lineSeparator() + errMsg.toString();
        if (msg.length() > 1024) {
            msg = msg.substring(0, 1021) + "...";
        }
        return msg;
    }

    /**
     * 导出Excel
     *
     * @param sheetName 表名
     * @param dataList 数据集
     * @param headers 表头
     * @param os 输出流
     */
    public static <T> void exportExcel(String sheetName, List<T> dataList, String[] headers, OutputStream os, IExcelExportHandler<T> handler) {
        if (sheetName == null || "".equals(sheetName.trim())) {
            sheetName = "sheet1";
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 样式
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setFontName("微软雅黑");
        style.setFont(font);
        // 表头行
        HSSFRow headerRow = sheet.createRow(0);
        for(int i = 0; i < headers.length; i++) {
            setCellValue(headerRow.createCell((short) i), headers[i], style);
        }
        // 数据行
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow row = sheet.createRow(i+1);
            List<Object> rowDatas = handler.handle(dataList.get(i));
            for (int j = 0; j < rowDatas.size(); j++) {
                Object value = rowDatas.get(j);
                if (value == null || "".equals(value.toString().trim())) {
                    setCellValue(row.createCell((short) j), "", style);
                } else {
                    setCellValue(row.createCell((short) j), value.toString(), style);
                }
            }
        }
        
        try {
            wb.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(HSSFCell cell) {
        Object value = null;
        switch (cell.getCellType()) {
        case NUMERIC:
            value = cell.getNumericCellValue();
            break;
        case STRING:
            value = cell.getStringCellValue();
            break;
        case BOOLEAN:
            value = cell.getBooleanCellValue();
            break;
        default:
            value = cell.getStringCellValue();
            break;
        }
        return value;
    }

    /**
     * 设置单元格的值
     *
     * @param cell
     * @param value
     * @param style
     */
    private static void setCellValue(HSSFCell cell, String value, HSSFCellStyle style) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }
}
