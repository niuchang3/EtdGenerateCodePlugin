package org.etd.generate.code.plugin.processor.extend;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.bean.Callback;
import org.etd.generate.code.plugin.bean.TableInfo;
import org.etd.generate.code.plugin.processor.AbstractFileProcessor;
import org.etd.generate.code.plugin.processor.FileProcessor;
import org.etd.generate.code.plugin.utils.NotificationMessageUtils;

import java.io.StringWriter;

public class FileProcessorImpl extends AbstractFileProcessor implements FileProcessor {

    @SneakyThrows
    @Override
    public void write(TableInfo tableInfo, StringWriter codeWriter) {
        String codeStr = deleteWhitespace(codeWriter);
        Callback callback = tableInfo.getCallback();
        String savePath = getSavePath(callback);
        VirtualFile directory = VfsUtil.createDirectoryIfMissing(savePath);


        VirtualFile file = directory.findChild(callback.getFileName());
        String msg = String.format("File %s Exists, Select Operate Mode?", directory.getPath() + "/" + callback.getFileName());
        if (ObjectUtils.isNotEmpty(file)) {
            boolean yesNo = NotificationMessageUtils.yesNo(msg, "Cover", "Cancel");
            if (!yesNo) {
                return;
            }
        }
        if (ObjectUtils.isEmpty(file)) {
            file = createFile(directory, callback.getFileName());
        }
        Document document = writeFile(file, codeStr);


    }

    private String deleteWhitespace(StringWriter codeStr) {
        StringBuilder stringBuilder = new StringBuilder(codeStr.toString());
        while (stringBuilder.length() > 0 && Character.isWhitespace(stringBuilder.charAt(0))) {
            stringBuilder.deleteCharAt(0);
        }
        return stringBuilder.toString();
    }

    private String getSavePath(Callback callback) {
        // 将包名替换成路径
        String packagePath = callback.getPackageName().replaceAll("\\.", "/");
        String basicPath = callback.getPath();
        return basicPath + "/" + packagePath;
    }

}
