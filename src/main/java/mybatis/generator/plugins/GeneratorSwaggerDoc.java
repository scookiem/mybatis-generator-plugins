package mybatis.generator.plugins;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.TableConfiguration;

import java.util.List;


/**
 * @author Created by rui.zhang on 2018/4/9.
 * @version ver1.0
 * email misterchangray@hotmail.com
 * description 根据数据库注释对实体类增加swagger2文档注解
 */
public class GeneratorSwaggerDoc extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String classAnnotation = "@ApiModel";
        String fieldAnnotation = "@ApiModelProperty($(content))";
        String apiModelAnnotationPackage = properties.getProperty("apiModelAnnotationPackage");
        String apiModelPropertyAnnotationPackage = properties.getProperty("apiModelPropertyAnnotationPackage");
        String ignoreField = properties.getProperty("ignoreField");

        if (!topLevelClass.getAnnotations().contains(classAnnotation)) {
            topLevelClass.addAnnotation(classAnnotation);
        }

        String[] ignoreFieldArray = null;
        if (ignoreField != null && ignoreField.trim().length() > 0) {
            ignoreFieldArray = ignoreField.split(",");
        }
        if (null == apiModelAnnotationPackage) apiModelAnnotationPackage = "io.swagger.annotations.ApiModel";
        if (null == apiModelPropertyAnnotationPackage)
            apiModelPropertyAnnotationPackage = "io.swagger.annotations.ApiModelProperty";
        topLevelClass.addImportedType(apiModelAnnotationPackage);
        topLevelClass.addImportedType(apiModelPropertyAnnotationPackage);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("value = \"");
        stringBuilder.append(introspectedColumn.getRemarks());
        stringBuilder.append("\"");
        /*如果是字符串类型，添加示例*/
        if (String.class.getName().equals(field.getType().getFullyQualifiedName())) {
            stringBuilder.append(", example = \"");
            stringBuilder.append(introspectedColumn.getRemarks());
            stringBuilder.append("\"");
        }
        if (ignoreFieldArray != null && ignoreFieldArray.length > 0) {
            for (String ignore : ignoreFieldArray) {
                if (ignore.equalsIgnoreCase(introspectedColumn.getActualColumnName())) {
                    stringBuilder.append(", ");
                    stringBuilder.append("hidden = true");
                }
            }
        }
        field.addAnnotation(fieldAnnotation.replace("$(content)", stringBuilder.toString()));
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
