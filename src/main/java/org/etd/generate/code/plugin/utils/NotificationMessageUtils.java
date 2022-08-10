package org.etd.generate.code.plugin.utils;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NotificationMessageUtils {

    public static void notifyError(@Nullable Project project,
                                   String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("EtdGenerateCodePlugin Notification Group")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

    public static void notifyWarning(@Nullable Project project,
                                     String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("EtdGenerateCodePlugin Notification Group")
                .createNotification(content, NotificationType.WARNING)
                .notify(project);
    }


    /**
     * 提示
     *
     * @param msg
     * @param yesText
     * @param noText
     * @return
     */
    public static boolean yesNo(String msg, String yesText, String noText) {
        Object[] options = new Object[]{yesText, noText};
        return JOptionPane.showOptionDialog(null,
                msg, "EtdGenerateCode",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                UIUtil.getQuestionIcon(),
                options, options[0]) == 0;
    }
}
