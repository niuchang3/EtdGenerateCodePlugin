package org.etd.generate.code.plugin.listener;

import com.intellij.formatting.service.DocumentMerger;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.merge.MultipleFileMergeDialog;
import com.intellij.psi.PsiPackage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.context.GenerateCodeContext;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PackageButtonActionListener implements ActionListener {

    private ListenerCallback callback;

    public PackageButtonActionListener(ListenerCallback callback) {
        this.callback = callback;
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {
        GenerateCodeContext context = GenerateCodeContextHelper.getContext();
        Project project = context.getProject();
        PackageChooserDialog packageChooser = new PackageChooserDialog("PackageChooser", project);
        packageChooser.show();
        PsiPackage selectedPackage = packageChooser.getSelectedPackage();
        if (ObjectUtils.isEmpty(selectedPackage)) {
            return;
        }
        String packageName = selectedPackage.getQualifiedName();
        callback.call(packageName);
//        MultipleFileMergeDialog fileMergeDialog = new MultipleFileMergeDialog();
    }
}
