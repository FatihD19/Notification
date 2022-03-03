package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.os.BuildCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mNotifButton;
    private Button mUpdateButton;
    private Button mCancelButton;
    private static final String CHANNEL_ID = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_NOTIFICATION";
    private NotificationManager mNotificationManager;
    public static final int NOTIF_ID = 0;

    public static final String UPDATE_EVENT = "UPDATE_EVENT";
    private NotificationonReceiver mNotificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationReceiver = new NotificationonReceiver();
        registerReceiver(mNotificationReceiver,new IntentFilter(UPDATE_EVENT));
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"notidication", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }
        mNotifButton = findViewById(R.id.notif_btn);
        mUpdateButton = findViewById(R.id.update_btn);
        mCancelButton = findViewById(R.id.cancel_btn);

        mNotifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendNotification();
            }
        });
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotification();

            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });
        mNotifButton.setEnabled(true);
        mCancelButton.setEnabled(false);
        mUpdateButton.setEnabled(false);

    }
    private void sendNotification(){
        Intent contentIntent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        String GUIDE_URL = "https://developer.android.com/design/pattrens/notifications.html";
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GUIDE_URL));
        PendingIntent pendingIntentLearnMoreIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID,learnMoreIntent,PendingIntent.FLAG_ONE_SHOT);

        Intent updateIntent = new Intent(UPDATE_EVENT);
        PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(getApplicationContext(),NOTIF_ID, updateIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder built = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        built.setContentTitle("You have been notifed");
        built.setContentText("This is notification text");
        built.setSmallIcon(R.drawable.ic_notification);
        built.setContentIntent(pendingContentIntent);
        built.setPriority(NotificationCompat.PRIORITY_HIGH);
        built.addAction(R.drawable.ic_notification, "Learn More",pendingIntentLearnMoreIntent);
        built.addAction(R.drawable.ic_notification,"UPDATE",pendingUpdateIntent);

        Notification notif = built.build();
        mNotificationManager.notify(NOTIF_ID,notif);

        mNotifButton.setEnabled(false);
        mCancelButton.setEnabled(true);
        mUpdateButton.setEnabled(true);
    }
    private void cancelNotification(){
        mNotificationManager.cancel(NOTIF_ID);

        mNotifButton.setEnabled(true);
        mCancelButton.setEnabled(false);
        mUpdateButton.setEnabled(false);
    }
    private void updateNotification(){
        Intent contentIntent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID,contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder built = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        built.setContentTitle("Notification have been update");
        built.setContentText("This is notification text");
        built.setSmallIcon(R.drawable.ic_notification);
        built.setContentIntent(pendingContentIntent);
        built.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Bitmap mascotBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);
        built.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(mascotBitmap));


//        built.setStyle(new NotificationCompat().BigPictureStyle().bigPicture(mascotBitmap).setBigContentTitle("the notification have been update"));
//        deprecated

        Notification notif = built.build();
        mNotificationManager.notify(NOTIF_ID,notif);

        mNotifButton.setEnabled(false);
        mCancelButton.setEnabled(true);
        mUpdateButton.setEnabled(false);
    }


    public class NotificationonReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == UPDATE_EVENT){
                updateNotification();
            }
        }
    }
}