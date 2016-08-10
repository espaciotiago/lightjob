package com.ufo.smartin.workid;

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by 'Santiago on 22/7/2016.
 */
public class GCMPushReciverService extends GcmListenerService{
    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        String message = bundle.getString("message");
        sendNotification(message);
    }
    private void sendNotification(String message){
        Intent intent = new Intent(this, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode=0; //Request code
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,intent,PendingIntent.FLAG_ONE_SHOT);
        //Setup Notification
        //Sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Build notification
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.noti_icon)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000})
                .setSound(sound)
                .setContentTitle("Light Job")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,noBuilder.build());

    }
}
