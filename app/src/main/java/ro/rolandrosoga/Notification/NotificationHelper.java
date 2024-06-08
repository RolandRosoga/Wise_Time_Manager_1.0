package ro.rolandrosoga.Notification;

import android.content.Context;

import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    private static NotificationHelper instance;
    private NotificationManagerCompat notificationManager;

    private NotificationHelper(Context context) {
        notificationManager = NotificationManagerCompat.from(context);
    }

    public static synchronized NotificationHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationHelper(context.getApplicationContext());
        }
        return instance;
    }

    public NotificationManagerCompat getNotificationManager() {
        return notificationManager;
    }
}
