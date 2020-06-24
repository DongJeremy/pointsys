package com.park.core.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * カスタムlombookコメントの設定
 * 
 * @author jingshiyu
 * @date 2019/9/17 12:04:47
 * @desc
 */
public class LombokPlugin extends PluginAdapter {

    private final Collection<Annotations> annotations;

    /**
     * LombokPlugin constructor
     */
    public LombokPlugin() {
        annotations = new LinkedHashSet<>(Annotations.values().length);
    }

    /**
     * @param warnings list of warnings
     * @return always true
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Intercepts base record class generation 获取表
     *
     * @param topLevelClass     the generated base record class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addAnnotations(topLevelClass);
        return true;
    }

    /**
     * Intercepts primary key class generation
     *
     * @param topLevelClass     the generated primary key class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addAnnotations(topLevelClass);
        return true;
    }

    /**
     * Intercepts "record with blob" class generation
     *
     * @param topLevelClass     the generated record with BLOBs class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        addAnnotations(topLevelClass);
        return true;
    }

    /**
     * get setメソッドを設定します。（lombokを使用すると必要なく、直接falseに戻ります。）
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
//        return false;
        return true;
    }

    /**
     * セット方法
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
//        return false;
        return true;
    }

    /**
     * lombookのコメントを設定します <br>
     */
    private void addAnnotations(TopLevelClass topLevelClass) {
        for (Annotations annotation : annotations) {
            topLevelClass.addImportedType(annotation.javaType);
            topLevelClass.addAnnotation(annotation.asAnnotation());
        }
    }

    /**
     * mapper類設定コメント
     */
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        interfaze.addAnnotation("@Mapper");
        return true;
    }

    private enum Annotations {

        DATA("data", "@Data", "lombok.Data"), BUILDER("builder", "@Builder", "lombok.Builder"),
        ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
        NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
        ACCESSORS("accessors", "@Accessors", "lombok.experimental.Accessors"),
        TO_STRING("toString", "@ToString", "lombok.ToString"),
        DATE_TIME_FORMAT("dateTimeFormat", "@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")",
                "org.springframework.format.annotation.DateTimeFormat"),
        JSON_FORMAT("jsonFormat", "@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")",
                "com.fasterxml.jackson.annotation.JsonFormat");

        private final String paramName;
        private final String name;
        private final FullyQualifiedJavaType javaType;
        private final List<String> options;

        Annotations(String paramName, String name, String className) {
            this.paramName = paramName;
            this.name = name;
            this.javaType = new FullyQualifiedJavaType(className);
            this.options = new ArrayList<String>();
        }

        public static Annotations getValueOf(String paramName) {
            for (Annotations annotation : Annotations.values()) {
                if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                    return annotation;
                }
            }

            return null;
        }

        public static Collection<Annotations> getDependencies(Annotations annotation) {
            if (annotation == ALL_ARGS_CONSTRUCTOR) {
                return Collections.singleton(NO_ARGS_CONSTRUCTOR);
            } else {
                return Collections.emptyList();
            }
        }

        // A trivial quoting.
        // Because Lombok annotation options type is almost String or boolean.
        private static String quote(String value) {
            if (Boolean.TRUE.toString().equals(value) || Boolean.FALSE.toString().equals(value))
            // case of boolean, not passed as an array.
            {
                return value;
            }
            return value.replaceAll("[\\w]+", "\"$0\"");
        }

        public void appendOptions(String key, String value) {
            String keyPart = key.substring(key.indexOf(".") + 1);
            String valuePart = value.contains(",") ? String.format("{%s}", value) : value;
            this.options.add(String.format("%s=%s", keyPart, quote(valuePart)));
        }

        private String asAnnotation() {
            if (options.isEmpty()) {
                return name;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append("(");
            boolean first = true;
            for (String option : options) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(option);
            }
            sb.append(")");
            return sb.toString();
        }
    }
}