package org.etd.generate.code.plugin.context;

import com.google.common.collect.Maps;
import com.intellij.openapi.application.ApplicationManager;
import lombok.Getter;
import org.etd.generate.code.plugin.bean.Settings;
import org.etd.generate.code.plugin.storage.SettingsStorage;

import java.util.Map;


public class ApplicationContext {


    @Getter
    protected SettingsStorage settingsStorage;

    @Getter
    protected Map<String, Object> attributes;

    public ApplicationContext() {
        this.settingsStorage = ApplicationManager.getApplication().getService(SettingsStorage.class);
        this.attributes = Maps.newHashMap();
    }


    public Settings getSetting() {
        return settingsStorage.getState();
    }

    public void updateSetting(Settings settings) {
        settingsStorage.loadState(settings);
    }


    public void clean() {
        this.settingsStorage = null;
        this.attributes.clear();
    }
}
