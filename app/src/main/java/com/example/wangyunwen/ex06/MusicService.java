package com.example.wangyunwen.ex06;

import android.app.Service;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Binder;
import android.content.Intent;

/**
 * Created by wangyunwen on 16/11/3.
 */
public class MusicService extends Service{
    public static MediaPlayer mp = new MediaPlayer();
    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder{
        MusicService getService() {
            return MusicService.this;
        }
    }

    public MusicService() {
        try {
            mp.setDataSource("/data/K.Will-Melt.mp3");
            mp.prepare();
            mp.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate() {super.onCreate();}

    @Override //必须实现的方法
    public IBinder onBind(Intent intent) {
        return binder; }

    @Override //被启动时回调该方法
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;}

    @Override
    public boolean onUnbind(Intent intent) { return super.onUnbind(intent);}

    @Override // 被关闭之前回调该方法
    public void onDestroy() {
        if(mp != null) {
            mp.stop();
            mp.release();
        }
        super.onDestroy();
    }

    public void play() {
        if(mp.isPlaying()) {
            mp.pause();
        } else {
            mp.start();
        }
    }

    public void stop() {
        if(mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
