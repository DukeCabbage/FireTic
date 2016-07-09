package com.cabbage.firetic.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.cabbage.firetic.GameboardActivity;
import com.cabbage.firetic.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

/**
 * Created by Leo on 7/6/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Timber.d("From: " + remoteMessage.getFrom());

        String msg = remoteMessage.getData().get("message");
        Timber.d("Notification Message Body: " + msg);
        sendTrackDetailNotification(msg);
    }

    private void sendNotification(NotificationCompat.Builder builder) {
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify((int) System.currentTimeMillis(), builder.build());
    }

    private NotificationCompat.Builder getBaseNotifBuilder() {
        return new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_DEFAULT);
    }

    private void sendTrackDetailNotification(String msg) {
        Intent intent = new Intent(this, GameboardActivity.class);
        intent.putExtra("message", msg);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(GameboardActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = getBaseNotifBuilder()
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setContentIntent(contentIntent);

        sendNotification(mBuilder);
    }
}
