package org.etd.generate.code.plugin.context;

import com.google.common.collect.Maps;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import lombok.*;
import org.etd.generate.code.plugin.bean.Settings;
import org.etd.generate.code.plugin.converter.BaseConverter;
import org.etd.generate.code.plugin.converter.extend.ColumnConverter;
import org.etd.generate.code.plugin.converter.extend.TableConverter;
import org.etd.generate.code.plugin.storage.SettingsStorage;
import org.etd.generate.code.plugin.tool.CacheDataUtils;

import java.util.List;
import java.util.Map;

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


    public GenerateCodeContext() {
    }


    public GenerateCodeContext(AnActionEvent event) {
        this.event = event;
        this.project = event.getProject();
        this.settingsStorage = event.getProject().getService(SettingsStorage.class);
        converterMap.put(TableConverter.class, TableConverter.getSingleton());
        converterMap.put(ColumnConverter.class, ColumnConverter.getSingleton());
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
        return (List<DasTable>) converter.convert(event);
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
    }

}
