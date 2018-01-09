package com.github.wxz.common.excel.annotation;

import java.lang.annotation.*;

/**
 * 列属性信息
 * <p>
 * 支持Java对象数据类型：Boolean、String、Short、Integer、Long、Float、Double、Date、LocalDateTime
 * 支持Excel的Cell类型为：String
 *
 * @author xianzhi.wang
 * @date 2017/12/19 -9:36
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelField {

    /**
     * 列名称
     *
     * @return
     */
    String name() default "";

    /**
     * 列宽 (大于0时生效; 如果不指定列宽，将会自适应调整宽度；)
     *
     * @return
     */
    int width() default 0;

    /**
     * 时间格式化，日期类型时生效
     *
     * @return
     */
    String dateformat() default "yyyy-MM-dd HH:mm:ss";

}
