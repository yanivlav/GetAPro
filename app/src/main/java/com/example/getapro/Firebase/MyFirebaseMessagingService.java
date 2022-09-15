package com.example.getapro.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.getapro.Fragments.ClientDashboard;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    final String TAG = "MyFirebaseMessaging";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String message;
        Intent intent2 = new Intent("il.org.syntax.sms_received");
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


//            Bundle bundle = new Bundle();
//            bundle.putString("message",remoteMessage.getData().get("message") );
//            ClientDashboard clientDashboard = new ClientDashboard();
//            ClientDashboard.setArguments(bundle);

            Intent intent = new Intent("message_received");

            if(remoteMessage.getData().get("message") == null ){
                intent.putExtra("message","default");
                intent2.putExtra("message","default");
            }
            else{
                message = remoteMessage.getData().get("message");
                intent2.putExtra("message",remoteMessage.getData().get("message"));
                intent.putExtra("message",remoteMessage.getData().get("message"));
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            //Add if the application is not in forground post notification
            //post notification only if in forground

            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(this);

            if(Build.VERSION.SDK_INT>=26) {
                NotificationChannel channel = new NotificationChannel("id_1", "name_1", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId("id_1");
            }
            builder.setContentTitle("New Spetz request").setContentText(remoteMessage.getData().get("message")).setSmallIcon(android.R.drawable.star_on);

            manager.notify(1,builder.build());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        //context.sendBroadcast(intent2);

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
