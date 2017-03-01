package com.example.nazenani.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = MyBroadcastReceiver.class.getSimpleName();

    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: pid=" + android.os.Process.myPid());

        int bid = intent.getIntExtra("intentId", 0);
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        Intent newIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, bid, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 通知マネージャーインスタンスを生成
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title + bid)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(description)
                .setDefaults(Notification.DEFAULT_ALL) // 音、バイブレート、LEDで通知
                .setContentIntent(pendingIntent) // 通知をタップしたときにMainActivityを立ち上げる
                .setAutoCancel(true) // タップで通知領域から削除
                .setVibrate(new long[]{0, 200, 100, 200, 100, 200}) // バイブレータの振動を設定
                .setSound(getAlarmUri()) // 音URIを設定
                .setLights(0xff0000ff, 3000, 1000) // LEDを青色に点滅させ、点灯時間は3000ミリ秒、消灯時間は1000ミリ秒
                .build();

        // サウンド関連
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            notification.audioAttributes = audioAttributes;
        } else {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            notification.audioStreamType = audioManager.STREAM_NOTIFICATION;
        }

        notification.flags |= Notification.FLAG_SHOW_LIGHTS; // LED点灯のフラグを追加する

        // 古い通知を削除
        notificationManager.cancelAll();
        // 通知
        notificationManager.notify(R.string.app_name, notification);
    }


    /**
     * NOTIFICATION => ALERM => RINGTONEの順にアラームのURIを調べる
     * @return Uri アラームのURI
     */
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}