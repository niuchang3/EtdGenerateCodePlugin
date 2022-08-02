package org.etd.generate.code.plugin.listener;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.context.GenerateCodeContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PathButtonActionListener implements ActionListener {

    private ListenerCallback callback;

    public PathButtonActionListener(ListenerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GenerateCodeContext context = GenerateCodeContextHelper.getContext();
        String basePath = context.getProject().getBasePath();
        VirtualFile fileByPath = LocalFileSystem.getInstance().findFileByPath(basePath);
        VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), context.getProject(), fileByPath);
        callback.call(virtualFile.getPath());
    }
}
