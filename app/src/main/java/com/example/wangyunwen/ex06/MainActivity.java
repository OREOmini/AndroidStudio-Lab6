package com.example.wangyunwen.ex06;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.AudioManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button play, stop, quit;
    private TextView status;
    private SeekBar seekBar;
    private MusicService musicService;
    private ImageView img;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        quit = (Button) findViewById(R.id.quit);
        status = (TextView) findViewById(R.id.status);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        img = (ImageView) findViewById(R.id.img);

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
                } else {
                    play.setText("PLAY");
                    status.setText("Pausing");
                }
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
                //
            }
        });
    }
}
