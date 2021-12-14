package org.meicode.doan_android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Date;

public class Receiver extends BroadcastReceiver {

    public static final String CHANNEL_ID="CHANNEL_1";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent = new Intent(context, TaskManagement.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(CreateTask.NOTIFICICATION_ID , PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification= new NotificationCompat.Builder(context,CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(CreateTask.edt_tittle)
                .setContentText(CreateTask.edt_message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(CreateTask.edt_message))
                .setContentIntent(resultPendingIntent)
                .setSound(uri)
                .build();
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(CreateTask.NOTIFICICATION_ID ,notification);
    }
}
