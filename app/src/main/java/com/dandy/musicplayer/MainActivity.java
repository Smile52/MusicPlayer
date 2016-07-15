package com.dandy.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dandy.musicplayer.mode.MusicMode;

public class MainActivity extends AppCompatActivity {
    private ImageView mSongPhoto;
    private ImageView mLast,mPause,mNext;
    private TextView mSongName,mSingerName,mStarTime,mEndTime;
    private SeekBar mProgress;
    private MusicMode musicMode;
    private View[] views;
    private MusicGroup musicGroup;
    private LayoutInflater mInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        LinearLayout layout= (LinearLayout) findViewById(R.id.act_id);
        mInflater=LayoutInflater.from(this);
        musicGroup=new MusicGroup(getApplicationContext());
        musicGroup= (MusicGroup)mInflater.inflate(R.layout.lg_musci_player,null);
        layout.addView(musicGroup);




    }

    private void initView() {
        mSongPhoto= (ImageView) findViewById(R.id.lg_music_song_photo_Img);
       // Log.i("tag","ee"+mSongPhoto.getId());
        mLast= (ImageView) findViewById(R.id.lg_music_last_img);
        mNext= (ImageView) findViewById(R.id.lg_music_next_img);
        mSongName= (TextView) findViewById(R.id.lg_music_song_name_tv);
        mSingerName= (TextView) findViewById(R.id.lg_music_singer_name_tv);
        mStarTime= (TextView) findViewById(R.id.lg_music_play_time_tv);
        mEndTime= (TextView) findViewById(R.id.lg_music_end_time_tv);
        mProgress= (SeekBar) findViewById(R.id.lg_music_seekbar);
        mPause= (ImageView) findViewById(R.id.lg_music_pause_img);
        views=new View[]{mSongPhoto,mLast,mPause,mNext,mSongName,mSingerName,mStarTime,mEndTime,mProgress};
    }
}
