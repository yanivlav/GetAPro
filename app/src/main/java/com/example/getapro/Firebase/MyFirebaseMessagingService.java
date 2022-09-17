package com.example.getapro.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.getapro.Fragments.ClientDashboard;
import com.example.getapro.MyObjects.Spetz;
import com.example.getapro.MyObjects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference users_fire = database.getReference("Users");
    ArrayList<User> users_local = new ArrayList<>();
    Spetz spetz;




    final String TAG = "MyFirebaseMessaging";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String message;
        Intent intent2 = new Intent("il.org.syntax.sms_received");
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());



            Intent intent = new Intent("message_received");

            if(remoteMessage.getData().get("message") == null ){
                intent2.putExtra("message","default");
            }
            else{
                message = remoteMessage.getData().get("message");
                intent2.putExtra("message",remoteMessage.getData().get("message"));
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);

            //Add if the application is not in forground post notification
            //post notification only if in forground



            if (user != null){
                users_fire.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        users_local.clear();
                        if(dataSnapshot.exists()) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                spetz = snapshot.getValue(Spetz.class);
                                if (spetz.getOccupation() != null) {
                                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                    Notification.Builder builder = new Notification.Builder(MyFirebaseMessagingService.this);

                                    if(Build.VERSION.SDK_INT>=26) {
                                        NotificationChannel channel = new NotificationChannel("id_1", "name_1", NotificationManager.IMPORTANCE_HIGH);
                                        manager.createNotificationChannel(channel);
                                        builder.setChannelId("id_1");
                                    }
                                    builder.setContentTitle("New Spetz request").setContentText(remoteMessage.getData().get("message")).setSmallIcon(android.R.drawable.star_on);

                                    manager.notify(1,builder.build());

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {

            }





//            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//            Notification.Builder builder = new Notification.Builder(this);
//
//            if(Build.VERSION.SDK_INT>=26) {
//                NotificationChannel channel = new NotificationChannel("id_1", "name_1", NotificationManager.IMPORTANCE_HIGH);
//                manager.createNotificationChannel(channel);
//                builder.setChannelId("id_1");
//            }
//            builder.setContentTitle("New Spetz request").setContentText(remoteMessage.getData().get("message")).setSmallIcon(android.R.drawable.star_on);
//
//            manager.notify(1,builder.build());
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
