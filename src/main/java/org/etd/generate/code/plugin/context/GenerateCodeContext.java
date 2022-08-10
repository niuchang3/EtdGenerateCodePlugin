package org.etd.generate.code.plugin.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.bean.Settings;
import org.etd.generate.code.plugin.bean.Template;
import org.etd.generate.code.plugin.converter.BaseConverter;
import org.etd.generate.code.plugin.converter.extend.ColumnConverter;
import org.etd.generate.code.plugin.converter.extend.TableConverter;
import org.etd.generate.code.plugin.converter.extend.TypeMappingConverter;
import org.etd.generate.code.plugin.processor.GenerateProcessor;
import org.etd.generate.code.plugin.storage.SettingsStorage;
import org.etd.generate.code.plugin.utils.CacheDataUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成器上下文
 */
@EqualsAndHashCode
@ToString
@Data
public class GenerateCodeContext {

    /**
     * 转换器
     */
    private Map<Class<?>, BaseConverter> converterMap = Maps.newConcurrentMap();
    /**
     * 缓存工具类
     */
    private CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();
    /**
     * 当前工程信息
     */
    @Getter
    @Setter
    private Project project;
    /**
     * 当前触发Action的事件
     */
    @Getter
    @Setter
    private AnActionEvent event;

    @Getter
    private SettingsStorage settingsStorage;

    @Getter
    private GenerateProcessor generateProcessor;


    public GenerateCodeContext() {
    }


    public GenerateCodeContext(AnActionEvent event) {
        this.event = event;
        this.project = event.getProject();
        this.settingsStorage = ApplicationManager.getApplication().getService(SettingsStorage.class);
        this.generateProcessor = event.getProject().getService(GenerateProcessor.class);

        converterMap.put(TableConverter.class, TableConverter.getSingleton());
        converterMap.put(ColumnConverter.class, ColumnConverter.getSingleton());
        converterMap.put(TypeMappingConverter.class, TypeMappingConverter.getSingleton());
    }


    /**
     * 获取转换器
     *
     * @return
     */
    public BaseConverter getConverter(Class<? extends BaseConverter> converter) {
        return converterMap.containsKey(converter) ? converterMap.get(converter) : null;
    }

    /**
     * 从上下文获取数据库Tables信息
     *
     * @return
     */
    public List<DasTable> getTables() {
        BaseConverter converter = getConverter(TableConverter.class);
        List<DasTable> tables = (List<DasTable>) converter.convert(event);
        for (DasTable table : tables) {
            if (!cacheDataUtils.containsKey(table.getName())) {
                cacheDataUtils.put(table.getName(), table);
            }
        }
        return tables;
    }

    /**
     * 根据表名获取数据库表对象
     *
     * @param tableName
     * @return
     */
    public DasTable getTables(String tableName) {
        DasTable dasTable = cacheDataUtils.get(tableName, DasTable.class);
        if (ObjectUtils.isNotEmpty(dasTable)) {
            return dasTable;
        }
        List<DasTable> tables = getTables();
        for (DasTable table : tables) {
            if (table.getName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }


    /**
     * 从Table中解析Column信息
     *
     * @param dasTable
     * @return
     */
    public List<DasColumn> getColumns(DasTable dasTable) {
        BaseConverter converter = getConverter(ColumnConverter.class);
        return (List<DasColumn>) converter.convert(dasTable);
    }

    public Settings getSetting() {
        return settingsStorage.getState();
    }


    public void clean() {
        this.project = null;
        this.event = null;
        this.converterMap.clear();
        this.generateProcessor = null;
        this.settingsStorage = null;
    }

    public List<String> getTemplateCode(String group, List<String> codes) {
        List<Template> templateList = getSetting().getTemplates().get(group);
        if (CollectionUtils.isEmpty(templateList)) {
            return Lists.newArrayList();
        }
        Map<String, String> collect = templateList.stream().collect(Collectors.toMap(Template::getName, Template::getCode));
        List<String> resCode = Lists.newArrayList();
        for (String code : codes) {
            if (!collect.containsKey(code)) {
                continue;
            }
            resCode.add(collect.get(code));
        }
        return resCode;
    }

}
