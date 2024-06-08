package ro.rolandrosoga.Notification;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import net.sqlcipher.database.SQLiteDatabase;

import ro.rolandrosoga.Activity.EditTaskActivity;
import ro.rolandrosoga.Database.SQLiteDAO;
import ro.rolandrosoga.R;

public class AlarmReceiver extends BroadcastReceiver {
    private final String START_CHANNEL_ID = "task_start_channel_id";
    private final String END_CHANNEL_ID = "task_end_channel_id";
    SQLiteDAO sqLiteDAO;
    Context context;
    Intent currentIntent;
    int taskID, notificationID = 0;
    Ringtone ringtone;
    Vibrator vibrator;
    String taskTitle;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SQLiteDAO.enableSQLCypher) {
            SQLiteDatabase.loadLibs(context);
        }
        sqLiteDAO = SQLiteDAO.getInstance(context);
        this.context = context;
        currentIntent = intent;
        //
        if ("CANCEL_NOTIFICATION".equals(intent.getAction())) {
            notificationID = intent.getIntExtra("notificationID", 0);
            NotificationManagerCompat notificationManager = NotificationHelper.getInstance(context).getNotificationManager();
            stopRingtone();
            notificationManager.cancel(notificationID);
        } else {
            notificationID = intent.getIntExtra("notificationID", 0);
            taskID = intent.getIntExtra("taskID", 0);
            boolean isStart = intent.getBooleanExtra("isStart", true);
            taskTitle = intent.getStringExtra("taskTitle");

            String notificationTitle = taskTitle;
            if (notificationTitle.length() > 30) {
                notificationTitle = notificationTitle.substring(0, 30);
            }
            String notificationText = "Task " + notificationTitle + " " + (isStart ? "is starting now." : "has ended.");
            String channelID;
            if (isStart) {
                channelID = START_CHANNEL_ID;
            } else {
                channelID = END_CHANNEL_ID;
            }
            NotificationCompat.Builder alarmBuilder = getNotificationBuilder(context, notificationTitle, notificationText, channelID, notificationID, taskID);
            NotificationManagerCompat notificationManager = NotificationHelper.getInstance(context).getNotificationManager();
            notificationManager.notify(notificationID, alarmBuilder.build());
            if (isStart) {
                setRingtone();
            }
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context, String notificationTitle, String notificationText,
                                                              String channelID, int notificationID, int taskID) {
        Intent editTaskActivityIntent = new Intent(context, EditTaskActivity.class);
        editTaskActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        editTaskActivityIntent.putExtra("taskID", taskID);
        editTaskActivityIntent.putExtra("requestCode", 2218);
        editTaskActivityIntent.putExtra("resultCode", Activity.RESULT_OK);
        editTaskActivityIntent.putExtra("activityNumber", 37);
        PendingIntent editTaskActivityPendingIntent = PendingIntent.getBroadcast(context, notificationID, editTaskActivityIntent, PendingIntent.FLAG_IMMUTABLE);
        //
        Intent cancelIntent = new Intent(context, AlarmReceiver.class);
        cancelIntent.setAction("CANCEL_NOTIFICATION");
        cancelIntent.putExtra("notificationID", notificationID);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, notificationID, cancelIntent, PendingIntent.FLAG_IMMUTABLE);
        //

        Uri alarmUri;
        int notificationPriority = -1;
        if (channelID.equals(START_CHANNEL_ID)) {
            notificationPriority = NotificationCompat.PRIORITY_HIGH;
        } else {
            notificationPriority = NotificationCompat.PRIORITY_DEFAULT;
        }
        try {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        } catch (Exception e) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        NotificationCompat.Builder currentBuilder = new NotificationCompat.Builder(context, channelID)
                .setBadgeIconType(R.drawable.wtm_app_icon)
                .setSmallIcon(R.drawable.original_clock_icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(alarmUri)
                .addAction(R.drawable.flaticon_tasks, "View Task", editTaskActivityPendingIntent)
                .addAction(R.drawable.searchview_flaticon_cross, "Cancel", cancelPendingIntent)
                .setPriority(notificationPriority);
        return currentBuilder;
    }

    private void setRingtone() {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Uri alarmUri = null;
        try {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        } catch (Exception e) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
        long[] vibrations = new long[] { 1000, 1000, 1000, 1000, 1000 };
        vibrator.vibrate(VibrationEffect.createWaveform(vibrations, -1));

        Handler newHandler = new Handler();
        newHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRingtone();
            }
        }, 10000);
    }

    private void stopRingtone() {
        try {
            ringtone.stop();
            vibrator.cancel();
        } catch (Exception ignore) {}
    }
}