package ru.mirea.ivlevdaniil.serviceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class PlayerService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static MediaPlayer mediaPlayerInstance;

    private MediaPlayer mediaPlayer;
    private final String SONG_TITLE = "Прекрасное далеко - Песня из к/ф \"Гостья из будущего\"";

    @Override
    public void onCreate() {
        super.onCreate();

        // Уведомление
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Music Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("MIREA Music Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Player")
                .setContentText("Playing: " + SONG_TITLE)
                .setSmallIcon(R.drawable.ic_music_note)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Now playing: " + SONG_TITLE));

        // Android 10+ требует тип
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, builder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
        } else {
            startForeground(1, builder.build());
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayerInstance = mediaPlayer;
        mediaPlayer.setLooping(false);

        mediaPlayer.setOnCompletionListener(mp -> {
            stopForeground(true);
            stopSelf();
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayerInstance = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
