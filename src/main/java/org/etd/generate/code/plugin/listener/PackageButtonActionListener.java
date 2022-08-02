package org.etd.generate.code.plugin.listener;

import com.intellij.openapi.project.Project;
import lombok.SneakyThrows;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class PackageButtonActionListener implements ActionListener {

    private ListenerCallback callback;

    public PackageButtonActionListener(ListenerCallback callback) {
        this.callback = callback;
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        Project project = GenerateCodeContextHelper.getContext().getProject();
        Class<?> cls = Class.forName("com.intellij.ide.util.PackageChooserDialog");
        Constructor<?> constructor = cls.getConstructor(String.class, Project.class);
        Object dialog = constructor.newInstance("Package Chooser", project);
        Method showMethod = cls.getMethod("show");
        showMethod.invoke(dialog);
        Method getSelectedPackageMethod = cls.getMethod("getSelectedPackage");
        Object psiPackage = getSelectedPackageMethod.invoke(dialog);
        if (psiPackage != null) {
            Method getQualifiedNameMethod = psiPackage.getClass().getMethod("getQualifiedName");
            String packageName = (String) getQualifiedNameMethod.invoke(psiPackage);
            callback.call(packageName);
        }
    }
}
