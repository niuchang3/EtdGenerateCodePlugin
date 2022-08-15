package org.etd.generate.code.plugin.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.options.Configurable;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.bean.Template;
import org.etd.generate.code.plugin.constants.BaseConstants;
import org.etd.generate.code.plugin.context.ApplicationContext;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.storage.SettingsStorage;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * 基础的Configurable
 */
public abstract class BaseConfigurable implements Configurable {

    @Getter
    protected SettingsStorage settingsStorage;


    @Override
    public @Nullable JComponent createComponent() {
        init();
        return getMainPanel();
    }

    protected abstract JComponent getMainPanel();


    private void init() {
        GenerateCodeContextHelper.clean();
        ApplicationContext context = new ApplicationContext();
        GenerateCodeContextHelper.setContext(context);

        Map<String, List<Template>> templates = Maps.newHashMap(context.getSetting().getTemplates());
        GenerateCodeContextHelper.setAttribute(BaseConstants.SYS_TEMPLATE_CODE, templates);
        GenerateCodeContextHelper.setAttribute(BaseConstants.SYS_AUTHOR_CODE, context.getSetting().getAuthor());
    }


    protected List<Template> selectTemplatesByKey(String key) {
        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);

        return (ObjectUtils.isNotEmpty(templates) && templates.containsKey(key)) ? templates.get(key) : Lists.newArrayList();
    }

    protected void addTemplates(String key, Template template) {
        List<Template> templateList = selectTemplatesByKey(key);
        if (CollectionUtils.isNotEmpty(templateList) && templateList.contains(template)) {
            return;
        }
        templateList.add(template);
        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
        templates.put(key, templateList);
    }

    protected void removeTemplate(String key) {
        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
        templates.remove(key);
    }

    protected String getDbAuthor() {
        return (String) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_AUTHOR_CODE);
    }

}
