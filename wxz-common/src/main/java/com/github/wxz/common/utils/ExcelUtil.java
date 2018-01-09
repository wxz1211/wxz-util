package com.github.wxz.common.utils;

import com.github.wxz.common.excel.annotation.ExcelField;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -9:43
 */
public class ExcelUtil {
    private ExcelUtil() {
    }

    public static Byte parseByte(String value) {
        try {
            value = value.replaceAll("　", "");
            return Byte.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("parseByte but input illegal input=" + value, e);
        }
    }

    public static Boolean parseBoolean(String value) {
        value = value.replaceAll("　", "");
        if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        } else if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        } else {
            throw new RuntimeException("parseBoolean but input illegal input=" + value);
        }
    }

    public static Integer parseInt(String value) {
        try {
            value = value.replaceAll("　", "");
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("parseInt but input illegal input=" + value, e);
        }
    }

    public static Short parseShort(String value) {
        try {
            value = value.replaceAll("　", "");
            return Short.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("parseShort but input illegal input=" + value, e);
        }
    }

    public static Long parseLong(String value) {
        try {
            value = value.replaceAll("　", "");
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("parseLong but input illegal input=" + value, e);
        }
    }

    public static Float parseFloat(String value) {
        try {
            value = value.replaceAll("　", "");
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("parseFloat but input illegal input=" + value, e);
        }
    }

    public static Double parseDouble(String value) {
        try {
            value = value.replaceAll("　", "");
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("parseDouble but input illegal input=" + value, e);
        }
    }

    public static Date parseDate(String value, ExcelField excelField) {
        try {
            String datePattern = "yyyy-MM-dd HH:mm:ss";
            if (excelField != null) {
                datePattern = excelField.dateformat();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("parseDate but input illegal input=" + value, e);
        }
    }

    public static LocalDateTime parseLocalDateTime(String value, ExcelField excelField) {
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        if (excelField != null && StringUtils.isNotEmpty(excelField.dateformat())) {
            datePattern = excelField.dateformat();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(datePattern);
        return LocalDateTime.parse(value, df);
    }

    /**
     * 参数解析 （支持：Byte、Boolean、String、Short、Integer、Long、Float、Double、Date,LocalDateTime）
     *
     * @param field
     * @param value
     * @return
     */
    public static Object parseValue(Field field, String value) {
        Class<?> fieldType = field.getType();

        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        value = value.trim();

		/*if (Byte.class.equals(fieldType) || Byte.TYPE.equals(fieldType)) {
            return parseByte(path);
		} else */
        if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
            return parseBoolean(value);
        }/* else if (Character.class.equals(fieldType) || Character.TYPE.equals(fieldType)) {
             return path.toCharArray()[0];
		}*/ else if (String.class.equals(fieldType)) {
            return value;
        } else if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
            return parseShort(value);
        } else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
            return parseInt(value);
        } else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
            return parseLong(value);
        } else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
            return parseFloat(value);
        } else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
            return parseDouble(value);
        } else if (Date.class.equals(fieldType)) {
            return parseDate(value, excelField);
        } else if (LocalDateTime.class.equals(fieldType)) {
            return parseLocalDateTime(value, excelField);
        } else {
            throw new RuntimeException("request illeagal type, type must be Integer not int Long not long etc, type=" + fieldType);
        }
    }

    /**
     * 参数格式化为String
     *
     * @param field
     * @param value
     * @return
     */
    public static String formatValue(Field field, Object value) {
        Class<?> fieldType = field.getType();

        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (value == null) {
            return null;
        }

        if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
            return String.valueOf(value);
        } else if (String.class.equals(fieldType)) {
            return String.valueOf(value);
        } else if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
            return String.valueOf(value);
        } else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
            return String.valueOf(value);
        } else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
            return String.valueOf(value);
        } else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
            return String.valueOf(value);
        } else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
            return String.valueOf(value);
        } else if (Date.class.equals(fieldType)) {
            String datePattern = "yyyy-MM-dd HH:mm:ss";
            if (excelField != null && StringUtils.isNotEmpty(excelField.dateformat())) {
                datePattern = excelField.dateformat();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            return dateFormat.format(value);
        } else if (LocalDateTime.class.equals(fieldType)) {
            String datePattern = "yyyy-MM-dd HH:mm:ss";
            if (excelField != null && StringUtils.isNotEmpty(excelField.dateformat())) {
                datePattern = excelField.dateformat();
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern(datePattern);
            return df.format((TemporalAccessor) value);
        } else {
            throw new RuntimeException("request illeagal type, type must be Integer not int Long not long etc, type=" + fieldType);
        }
    }
}
