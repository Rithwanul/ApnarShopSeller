package com.bikroybaba.seller.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.ui.HomeActivity;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final Utility utility = new Utility(this);
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String body = data.get("body");
            //String image_url = data.get("image_url");
            String big_text = data.get("big_text");
            String type = data.get("type");
            String target = data.get("target");
            // String small_image = data.get("icon_path");
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            showNotification(title, body, big_text, type, target);
        }

    }

    public void showNotification(
            String title, String body,
            String big_text,
            String type,
            String target
    ) {
        createNotificationChannel();
        int notifyId = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, HomeActivity.class);
        if (type != null && !TextUtils.isEmpty(type) && target != null && !TextUtils.isEmpty(target)) {
            intent.putExtra("NOTIFICATION", "yes");
            intent.putExtra("type", type);
            intent.putExtra("target", target);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyId, intent, 0);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, KeyWord.NOTIFICATION_CHANNEL_NAME)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setColor(getApplication().getResources().getColor(R.color.app_yellow3))
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker(title)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_logo))
                /*.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))*/
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(*//*BitmapFactory.decodeResource(getResources(), R.drawable.ribbon)*//*Glide
                                    .with(this)
                                    .asBitmap()
                                    .load("http://116.212.109.34:9090/content/resources/images/banner/3/20200318_9.jpg")
                                    .submit()
                                    .get())
                            .bigLargeIcon(null))*/
                .setPriority(Notification.PRIORITY_MAX);
           /* if (image_url != null && !TextUtils.isEmpty(image_url)) {
                builder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(Glide.with(this).asBitmap().load(image_url).submit().get()));
            } else if (big_text != null && !TextUtils.isEmpty(big_text) && big_text.equalsIgnoreCase(KeyWord.YES)) {
                builder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body));

            }*/
        if (big_text != null && !TextUtils.isEmpty(big_text) &&
                big_text.equalsIgnoreCase(KeyWord.YES)) {
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(body));
        }
        notificationManager.notify(notifyId, builder.build());
    }

   /* public void showNotification(String title, String message) {

        createNotificationChannel();

        final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
        Intent intent = new Intent(this, HomePage.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        PendingIntent pendingIntent =  stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(getPackageName() , R.layout.custom_notification_layout ) ;
        contentView.setTextViewText(R.id.title,title);
        contentView.setTextViewText(R.id.body,message);
        contentView.setImageViewResource(R.id.imageNotify,R.drawable.ic_aapnarshop_svg_01);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "EbookNotifications")
                .setCustomBigContentView(contentView)
                .setCustomContentView(contentView)
                .setContentTitle(title)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setVibrate(DEFAULT_VIBRATE_PATTERN)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_aapnarshop_svg_01)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentText(message)
                .setFullScreenIntent(pendingIntent,true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());

    }*/

    @Override
    public void onNewToken(@NonNull String s) {
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {
        utility.setFirebaseToken(token);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(KeyWord.NOTIFICATION_CHANNEL_NAME,
                            KeyWord.NOTIFICATION_CHANNEL_NAME, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(R.color.yellow_500);
            channel.setVibrationPattern(new long[]{0, 250, 250, 250});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
