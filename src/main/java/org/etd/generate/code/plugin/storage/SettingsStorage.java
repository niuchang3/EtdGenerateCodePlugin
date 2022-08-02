package org.etd.generate.code.plugin.storage;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.etd.generate.code.plugin.bean.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "EtdGenerateCodeSetting", storages = @Storage("etd-generate-code-setting.xml"))
public class SettingsStorage implements PersistentStateComponent<Settings> {

    private Settings settings = Settings.getSingleton();

    @Override
    public @Nullable Settings getState() {
        return settings;
    }


    @Override
    public void loadState(@NotNull Settings state) {
        this.settings = state;
    }


}
