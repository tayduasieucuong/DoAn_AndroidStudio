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

import androidx.core.app.NotificationCompat;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent = new Intent(context, TaskManagement.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification= new NotificationCompat.Builder(context,"Notification")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("This is tittle")
                .setContentText("Du me me kho qua :))")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Du me me kho qua :))"))
                .setContentIntent(resultPendingIntent)
                .setSound(uri)
                .build();
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }

}
