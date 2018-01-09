package com.github.wxz.common.excel;

import com.github.wxz.common.excel.annotation.ExcelField;
import com.github.wxz.common.excel.annotation.ExcelSheet;
import com.github.wxz.common.utils.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -9:36
 */
public class ExcelExportUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelExportUtil.class);

    /**
     * 导出Excel对象
     *
     * @param dataListArr Excel数据
     * @return
     */
    public static Workbook exportWorkbook(List<?>... dataListArr) {
        // data array valid
        if (dataListArr == null || dataListArr.length == 0) {
            throw new RuntimeException(">>>>>>>>>>> excel error, data array can not be empty.");
        }
        // book
        // HSSFWorkbook=2003/xls、XSSFWorkbook=2007/xlsx
        HSSFWorkbook workbook = new HSSFWorkbook();

        // sheet
        for (List<?> dataList : dataListArr) {
            makeSheet(workbook, dataList);
        }
        return workbook;
    }

    /**
     * makeSheet
     *
     * @param workbook
     * @param dataList
     */
    private static void makeSheet(HSSFWorkbook workbook, List<?> dataList) {
        // data
        if (dataList == null || dataList.size() == 0) {
            throw new RuntimeException(">>>>>>>>>>>excel error, data can not be empty.");
        }

        // sheet
        Class<?> sheetClass = dataList.get(0).getClass();
        ExcelSheet excelSheet = sheetClass.getAnnotation(ExcelSheet.class);

        String sheetName = dataList.get(0).getClass().getSimpleName();
        List<HSSFCellStyle> hssfCellStyleList = getHSSFCellStyle(workbook);
        HSSFColor.HSSFColorPredefined headColor = null;
        if (excelSheet != null) {
            if (excelSheet.name() != null && excelSheet.name().trim().length() > 0) {
                sheetName = excelSheet.name().trim();
            }
            headColor = excelSheet.headColor();
        }

        Sheet existSheet = workbook.getSheet(sheetName);

        if (existSheet != null) {
            for (int i = 2; i <= 1000; i++) {
                // avoid sheetName repetition
                String newSheetName = sheetName.concat(String.valueOf(i));
                existSheet = workbook.getSheet(newSheetName);
                if (existSheet == null) {
                    sheetName = newSheetName;
                    break;
                } else {
                    continue;
                }
            }
        }

        Sheet sheet = workbook.createSheet(sheetName);

        // sheet field
        List<Field> fields = new ArrayList<>();
        if (sheetClass.getDeclaredFields() != null && sheetClass.getDeclaredFields().length > 0) {
            for (Field field : sheetClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
        }

        if (fields == null || fields.size() == 0) {
            throw new RuntimeException(">>>>>>>>>>>excel error, data field can not be empty.");
        }

        // sheet header row
        if (headColor != null) {
            hssfCellStyleList.get(0).setFillForegroundColor(headColor.getIndex());
            hssfCellStyleList.get(0).setFillPattern(FillPatternType.SOLID_FOREGROUND);
            hssfCellStyleList.get(0).setFillBackgroundColor(headColor.getIndex());
        }
        Row headRow = sheet.createRow(0);
        boolean ifSetWidth = false;
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            String fieldName = (excelField != null && excelField.name() != null && excelField.name().trim().length() > 0) ? excelField.name() : field.getName();
            int fieldWidth = (excelField != null) ? excelField.width() : 0;

            Cell cellX = headRow.createCell(i, CellType.STRING);
            cellX.setCellStyle(hssfCellStyleList.get(0));
            if (fieldWidth > 0) {
                sheet.setColumnWidth(i, fieldWidth);
                ifSetWidth = true;
            }
            cellX.setCellValue(String.valueOf(fieldName));
        }

        // sheet data rows
        for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++) {
            int rowIndex = dataIndex + 1;
            Object rowData = dataList.get(dataIndex);

            Row rowX = sheet.createRow(rowIndex);

            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(rowData);

                    String fieldValueString = ExcelUtil.formatValue(field, fieldValue);

                    Cell cellX = rowX.createCell(i, CellType.STRING);
                    cellX.setCellStyle(hssfCellStyleList.get(1));
                    cellX.setCellValue(fieldValueString);
                } catch (IllegalAccessException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (!ifSetWidth) {
            for (int i = 0; i < fields.size(); i++) {
                sheet.autoSizeColumn((short) i);
            }
        }
    }

    /**
     * 样式
     *
     * @param workbook
     * @return
     */
    private static List<HSSFCellStyle> getHSSFCellStyle(HSSFWorkbook workbook) {
        List<HSSFCellStyle> styleList = new ArrayList<>();
        // 生成一个标题样式
        HSSFCellStyle headStyle = workbook.createCellStyle();
        // 居中
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置表头标题样式:微软雅黑，大小11，粗体显示
        HSSFFont headfont = workbook.createFont();

        headfont.setFontName("微软雅黑");
        // 字体大小
        headfont.setFontHeightInPoints((short) 11);
        // 粗体显示
        headfont.setBold(true);

        /**
         * 边框
         */
        // 下边框
        headStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        headStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        headStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        headStyle.setBorderRight(BorderStyle.THIN);
        // 垂直居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体样式
        headStyle.setFont(headfont);
        styleList.add(headStyle);

        // 生成一个内容样式
        HSSFCellStyle contentStyle = workbook.createCellStyle();
        // 居中
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        /**
         * 边框
         */
        // 下边框
        contentStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        contentStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        contentStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        contentStyle.setBorderRight(BorderStyle.THIN);

        HSSFFont contentFont = workbook.createFont();
        contentFont.setFontName("微软雅黑");
        // 字体大小
        contentFont.setFontHeightInPoints((short) 11);
        // 字体样式
        contentStyle.setFont(contentFont);
        styleList.add(contentStyle);

        return styleList;
    }

    /**
     * 导出Excel文件到磁盘
     *
     * @param filePath
     * @param dataList
     */
    public static void exportToFile(String filePath, List<?>... dataList) {
        // workbook
        Workbook workbook = exportWorkbook(dataList);

        FileOutputStream fileOutputStream = null;
        try {
            // workbook 2 FileOutputStream
            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);

            // flush
            fileOutputStream.flush();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 导出Excel字节数据
     *
     * @param dataList
     * @return
     */
    public static byte[] exportToBytes(List<?>... dataList) {
        // workbook
        Workbook workbook = exportWorkbook(dataList);

        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] result = null;
        try {
            // workbook 2 ByteArrayOutputStream
            byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            // flush
            byteArrayOutputStream.flush();

            result = byteArrayOutputStream.toByteArray();
            return result;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

}
