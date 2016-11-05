package com.example.wangyunwen.ex06;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.AudioManager;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    private Button play, stop, quit;
    private TextView status, currentTime, totalTime;
    private SeekBar seekBar;
    private MusicService musicService;
    private ImageView img;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private int degree = 0; //rotate degree
    private boolean isRotating = false;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder)(service)).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            currentTime.setText(time.format(musicService.mp.getCurrentPosition()));
            totalTime.setText(time.format(musicService.mp.getDuration()));

            if(isRotating) img.setRotation(degree++);

            seekBar.setProgress(musicService.mp.getCurrentPosition());
            seekBar.setMax(musicService.mp.getDuration());

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) {
                        musicService.mp.seekTo(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            mHandler.postDelayed(mRunnable, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        musicService = new MusicService();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, sc, BIND_AUTO_CREATE);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.play();
                if(musicService.mp.isPlaying()) {
                    play.setText("PAUSE");
                    status.setText("Playing");
                    isRotating = true;
                    mHandler.post(mRunnable);
                } else {
                    play.setText("PLAY");
                    status.setText("Pausing");
                    isRotating = false;
                }
                currentTime.setText(time.format(musicService.mp.getCurrentPosition()));
                totalTime.setText(time.format(musicService.mp.getDuration()));
                seekBar.setMax(musicService.mp.getDuration());
                seekBar.setProgress(musicService.mp.getCurrentPosition());
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(mRunnable);
                unbindService(sc);
                try {
                    MainActivity.this.finish();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        unbindService(sc);
        super.onDestroy();
    }

    void findView() {
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        quit = (Button) findViewById(R.id.quit);
        status = (TextView) findViewById(R.id.status);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        img = (ImageView) findViewById(R.id.img);
        currentTime = (TextView) findViewById(R.id.currentTime);
        totalTime = (TextView) findViewById(R.id.totalTime);
    }
}
