package org.etd.generate.code.plugin.utils;

import com.intellij.diff.DiffManager;
import com.intellij.diff.DiffRequestFactory;
import com.intellij.diff.InvalidDiffRequestException;
import com.intellij.diff.merge.MergeRequest;
import com.intellij.diff.merge.MergeWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.compress.utils.Lists;

import java.io.IOException;
import java.util.List;


public class CompareUtils {


    /**
     * 对比文件
     *
     * @param project
     * @param oldFile
     * @param newFile
     */
    public static void showCompare(Project project, VirtualFile outFile, VirtualFile oldFile, VirtualFile newFile) throws InvalidDiffRequestException, IOException {

        show2(project, outFile, oldFile, newFile);
    }

    private static void show1(Project project, VirtualFile outFile, VirtualFile oldFile, VirtualFile newFile) throws InvalidDiffRequestException {
        List<VirtualFile> files = Lists.newArrayList();
        files.add(oldFile);
        files.add(outFile);
        files.add(newFile);
        DiffManager instance = DiffManager.getInstance();
        DiffRequestFactory requestFactory = DiffRequestFactory.getInstance();
        MergeRequest mergeRequest = requestFactory.createMergeRequestFromFiles(project, outFile, files, (applyCallback) -> {

        });
        instance.showMerge(project, mergeRequest);
    }

    private static void show2(Project project, VirtualFile outFile, VirtualFile oldFile, VirtualFile newFile) throws InvalidDiffRequestException, IOException {
        DiffRequestFactory diffRequestFactory = DiffRequestFactory.getInstance();
        byte[]  oldBytes = oldFile.contentsToByteArray();

        byte[]  outBytes = outFile.contentsToByteArray();

        byte[]  newBytes = newFile.contentsToByteArray();

        List<byte[]> byteContents = Lists.newArrayList();
        byteContents.add(oldBytes);
        byteContents.add(outBytes);
        byteContents.add(newBytes);

        List<String> titles = Lists.newArrayList();


        titles.add(diffRequestFactory.getContentTitle(oldFile));
        titles.add(diffRequestFactory.getContentTitle(outFile));
        titles.add(diffRequestFactory.getContentTitle(newFile));

        MergeRequest mergeRequest = diffRequestFactory.createMergeRequest(project,outFile,byteContents,"merge",titles);
        DiffManager.getInstance().showMerge(project,mergeRequest);
    }

}
