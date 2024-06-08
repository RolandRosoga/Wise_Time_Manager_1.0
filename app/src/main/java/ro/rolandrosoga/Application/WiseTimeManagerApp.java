package ro.rolandrosoga.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ro.rolandrosoga.Notification.NotificationHelper;

public class WiseTimeManagerApp extends Application {
    private final String START_CHANNEL_ID = "task_start_channel_id";
    private final String START_CHANNEL_NAME = "start_channel_name";
    private final String END_CHANNEL_ID = "task_end_channel_id";
    private final String END_CHANNEL_NAME = "end_channel_name";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        ThreadFactory privilegedFactory = Executors.privilegedThreadFactory();
        ExecutorService appService = Executors.newCachedThreadPool(privilegedFactory);
        appService.submit(new Runnable() {
            @Override
            public void run() {
                createStartChannel();
            }
        });
        appService.submit(new Runnable() {
            @Override
            public void run() {
                createEndChannel();
            }
        });
        appService.shutdown();
    }

    public void createStartChannel() {
        NotificationChannel startChannel = new NotificationChannel(
                START_CHANNEL_ID,
                START_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        );
        startChannel.setDescription("Notifications for Task's Start Time.");
        NotificationManagerCompat notificationManager = NotificationHelper.getInstance(this).getNotificationManager();
        notificationManager.createNotificationChannel(startChannel);
    }

    public void createEndChannel() {
        NotificationChannel endChannel = new NotificationChannel(
                END_CHANNEL_ID,
                END_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        endChannel.setDescription("Notifications for Task's End Time.");
        NotificationManagerCompat notificationManager = NotificationHelper.getInstance(this).getNotificationManager();
        notificationManager.createNotificationChannel(endChannel);
    }
}
