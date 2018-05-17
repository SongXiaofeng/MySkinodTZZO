package com.skinod.tzzo.skinod.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

import com.skinod.tzzo.R;

import java.util.Timer;
import java.util.TimerTask;


public class PlayMusicActivity extends Activity implements View.OnClickListener,MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private final String TAG="PlayMusicActivity";
    private String mypath;
    private MediaPlayer mediaPlayer ;
    private Button begin_play;
    private SeekBar skbProgress=null;
    private TimerTask mTimerTask;
    private final  int BEGIN_PLAYER=2;
    private final  int NOW_PLAYER=0;
    private final  int FINISH_PLAYER=1;
    private int playFlage=1;
    private final  int PLAY_NOW=1;
    private final  int PLAY_STOP=2;



    private Timer mTimer=new Timer();


    Handler handleProgress = new Handler() {


        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            if(msg.what==NOW_PLAYER) {

                if (duration > 0) {
                    long pos = skbProgress.getMax() * position / duration;
                    skbProgress.setProgress((int) pos);
                }



            }else if(msg.what==FINISH_PLAYER){
                skbProgress.setProgress(0);

                playFlage=PLAY_NOW;
                Drawable drawable = ContextCompat.getDrawable(PlayMusicActivity.this, R.mipmap.play_list_button_play);
                begin_play.setBackground(drawable);


                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                // finish();


            }else if(msg.what==BEGIN_PLAYER){
                skbProgress.setProgress(0);



                playFlage=PLAY_STOP;
                Drawable drawable = ContextCompat.getDrawable(PlayMusicActivity.this, R.mipmap.play_list_button_stop);
                begin_play.setBackground(drawable);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_play_music);

        initViews();

        initMediaPlay();
        Bundle bundle = this.getIntent().getExtras();
        mypath= bundle.getString("music_path");
        Log.i(TAG,"music_path="+mypath);



    }

    private void initViews() {
        begin_play= (Button)findViewById(R.id.begin_play);
        begin_play.setOnClickListener(this);
        skbProgress=(SeekBar)findViewById(R.id.sb_music);
    }


    private void initMediaPlay() {

    /*    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},200);
        }
*/

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    handleProgress.sendEmptyMessage(FINISH_PLAYER);
                }
            });

        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }




        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(mediaPlayer==null)
                    return;
                if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                    handleProgress.sendEmptyMessage(NOW_PLAYER);
                }
            }
        };

        mTimer.schedule(mTimerTask, 0, 500);

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.begin_play){
            if(playFlage==PLAY_NOW) {

                try {
                    if (mediaPlayer == null) {
                        try {
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setOnBufferingUpdateListener(this);
                            mediaPlayer.setOnPreparedListener(this);
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    handleProgress.sendEmptyMessage(FINISH_PLAYER);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(mypath);
                    mediaPlayer.prepare();
                    handleProgress.sendEmptyMessage(BEGIN_PLAYER);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            if(playFlage==PLAY_STOP) {
                handleProgress.sendEmptyMessage(FINISH_PLAYER);

            }



        }
    }

    @Override
    protected void onStop() {
        super.onStop();


        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        skbProgress.setSecondaryProgress(bufferingProgress);
        int currentProgress=skbProgress.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
        Log.e(currentProgress+"% play", bufferingProgress + "% buffer");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("onKeyUp","event.getKeyCode()="+event.getKeyCode());
        if(event.getKeyCode()==KeyEvent.KEYCODE_F1){
            if(playFlage==PLAY_NOW) {

                try {
                    if (mediaPlayer == null) {
                        try {
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setOnBufferingUpdateListener(this);
                            mediaPlayer.setOnPreparedListener(this);
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    handleProgress.sendEmptyMessage(FINISH_PLAYER);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(mypath);
                    mediaPlayer.prepare();
                    handleProgress.sendEmptyMessage(BEGIN_PLAYER);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            if(playFlage==PLAY_STOP) {
                handleProgress.sendEmptyMessage(FINISH_PLAYER);

            }

        }
        return super.onKeyUp(keyCode, event);
    }

}
