package org.etd.generate.code.plugin.tool;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

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
}
