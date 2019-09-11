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
public class GeneratorOtherAnnotation extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        /*不序列化null*/
        String jsonInclude = "@JsonInclude(JsonInclude.Include.NON_NULL)";
        if (!topLevelClass.getAnnotations().contains(jsonInclude)) {
            topLevelClass.addAnnotation(jsonInclude);
        }
        topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonInclude");
        /*不接受前台传值*/
        String[] mustNullArray = properties.getProperty("nullColumns").split(",");
        for (String mustNull : mustNullArray) {
            if (introspectedColumn.getActualColumnName().equalsIgnoreCase(mustNull)) {
                field.addAnnotation("@Null");
                topLevelClass.addImportedType("javax.validation.constraints.Null");
            }
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
