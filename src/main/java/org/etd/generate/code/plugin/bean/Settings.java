package org.etd.generate.code.plugin.bean;

import com.google.gson.Gson;
import com.intellij.ide.fileTemplates.impl.UrlUtil;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Data
public class Settings {

    private volatile static Settings settings;

    public Settings() {

    }

    public static Settings getSingleton() {
        if (ObjectUtils.isEmpty(settings)) {
            synchronized (Settings.class) {
                if (ObjectUtils.isEmpty(settings)) {
                    settings = resetDefaultSetting();
                }
            }

        }
        return settings;
    }

    /**
     * 作者
     */
    private String author;
    /**
     * 当前使用的模板租
     */
    private String currentTemplateGroup;
    /**
     * 当前使用的映射
     */
    private String currentTypeMapping;

    /**
     * 字段映射
     */
    private Map<String, List<TypeMapping>> typeMappings;
    /**
     * vm模板
     */
    private Map<String, List<Template>> tamplates;


    private static Settings resetDefaultSetting() {
        URL configUrl = Settings.class.getResource("/defaultConfig.json");
        Settings settings = null;
        try {

            String jsonStr = UrlUtil.loadText(configUrl);
            settings = new Gson().fromJson(jsonStr, Settings.class);
        } catch (IOException e) {
            //TODO:加载默认的json文件失败，需要有一套手动触发的默认配置信息
        }
        return settings;
    }
}
