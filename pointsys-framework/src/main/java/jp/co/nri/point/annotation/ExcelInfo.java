package jp.co.nri.point.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelInfo {

    public int columnIndex() default 0;

    public String columnName() default "";
}
