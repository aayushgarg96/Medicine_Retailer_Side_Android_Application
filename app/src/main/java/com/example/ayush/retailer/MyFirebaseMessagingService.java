package com.example.ayush.retailer;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ayush on 12/21/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("FCM", "From: " + remoteMessage.getFrom());

        if(remoteMessage.getNotification()!=null){
            Log.d("FCM", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        }

        if(remoteMessage.getData().containsKey("post_id") && remoteMessage.getData().containsKey("post_title")){
            Log.d("Post ID",remoteMessage.getData().get("post_id").toString());
            Log.d("Post Title",remoteMessage.getData().get("post_title").toString());
            // eg. Server Send Structure data:{"post_id":"12345","post_title":"A Blog Post"}
        }
    }
}
