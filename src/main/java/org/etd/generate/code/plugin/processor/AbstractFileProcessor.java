package org.etd.generate.code.plugin.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import org.etd.generate.code.plugin.context.GenerateCodeContext;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;

import java.io.IOException;

public abstract class AbstractFileProcessor implements FileProcessor {


    protected VirtualFile createFile(VirtualFile directory, String fileName) {
        GenerateCodeContext context = GenerateCodeContextHelper.getContext();
        return WriteCommandAction.runWriteCommandAction(context.getProject(), (Computable<VirtualFile>) () -> {
            try {
                VirtualFile childData = directory.createChildData(new Object(), fileName);
                childData.setBOM(CharsetToolkit.UTF8_BOM);
                return childData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }


    public Document writeFile(VirtualFile file, String text) {
        GenerateCodeContext context = GenerateCodeContextHelper.getContext();
        WriteCommandAction.runWriteCommandAction(context.getProject(), () -> {
            try {
                file.setBinaryContent(text.getBytes());
            } catch (IOException e) {
                throw new IllegalStateException("二进制文件写入失败，fileName：" + file.getName());
            }
        });
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        return fileDocumentManager.getDocument(file);
    }
}
