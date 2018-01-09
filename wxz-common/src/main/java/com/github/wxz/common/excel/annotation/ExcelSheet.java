package com.github.wxz.common.excel.annotation;

import org.apache.poi.hssf.util.HSSFColor;

import java.lang.annotation.*;

/**
 * 表信息
 *
 * @author xianzhi.wang
 * @date 2017/12/19 -9:48
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelSheet {

    /**
     * 表名称
     *
     * @return
     */
    String name() default "";

    /**
     * 表头/首行的颜色
     *
     * @return
     */
    HSSFColor.HSSFColorPredefined headColor() default HSSFColor.HSSFColorPredefined.LIGHT_GREEN;
}
