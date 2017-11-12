package com.commandcenter.devchat.MessagingService;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.commandcenter.devchat.Controller.Chatbox_Activity;
import com.commandcenter.devchat.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Command Center on 11/11/2017.
 */

public class DevChatMessagingService extends FirebaseMessagingService {

    private static final String TAG = "DevChat Messaging";

    public DevChatMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        sendNotification(title, message);
    }

    @Override
    public void onMessageSent(String s) {

    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendNotification(String title, String messageBody) {

        Intent intent = new Intent(this, Chatbox_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUrl)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


    }
}
