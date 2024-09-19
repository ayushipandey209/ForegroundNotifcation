package com.bservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class MyForegroundService extends Service {
    public MyForegroundService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int a = 0;
                        while (true) {
                            Log.e("Foreground service", "Foreground service is running: " + a);
                            a++;

                            // Get the current time
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);

                            // Check if it's 4:15 PM
                            if (hour == 16 && minute == 12) {
                                // Create and trigger a notification at 4:15 PM
                                sendNotification("Time Alert", "It's 4:15 PM!");
                            }

                            try {
                                Thread.sleep(5000); // Sleep for 5 seconds
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

        final String CHANNELID = "foreground_service_id";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNELID, CHANNELID, NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                    .setContentText("Service is running..")
                    .setContentTitle("Service enabled")
                    .setSmallIcon(R.drawable.ic_launcher_background);
            startForeground(1001, notification.build());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // Method to trigger a notification
    private void sendNotification(String title, String message) {
        final String CHANNELID = "time_alert_channel";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNELID, "Time Alert", NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_launcher_background);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.notify(1002, notification.build());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
