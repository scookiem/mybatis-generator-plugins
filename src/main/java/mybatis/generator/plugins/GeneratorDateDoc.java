package mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Date;
import java.util.List;


/**
 * @author Created by rui.zhang on 2018/4/9.
 * @version ver1.0
 * email misterchangray@hotmail.com
 * description 根据数据库注释对实体类增加swagger2文档注解
 */
public class GeneratorDateDoc extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String dateFormat = properties.getProperty("dateformat");
        String jsonFormat = properties.getProperty("jsonformat");
        String dateFormatImportType = "org.springframework.format.annotation.DateTimeFormat";
        String jsonFormatImportType = "com.fasterxml.jackson.annotation.JsonFormat";

        if (dateFormat != null && !"".equals(dateFormat.trim())) {
            if (Date.class.getName().equals(field.getType().getFullyQualifiedName())) {
                field.addAnnotation("@DateTimeFormat(pattern = \"" + dateFormat + "\")");
                topLevelClass.addImportedType(dateFormatImportType);
            }
        }
        if (jsonFormat != null && !"".equals(jsonFormat.trim())) {
            if (Date.class.getName().equals(field.getType().getFullyQualifiedName())) {
                field.addAnnotation("@JsonFormat(pattern = \"" + jsonFormat + "\")");
                topLevelClass.addImportedType(jsonFormatImportType);
            }
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
