package com.dandy.musicplayer;

import android.content.Context;
import android.content.IntentSender;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dandy.musicplayer.mode.MusicMode;

/**
 * Created by Smile on 2016/7/14.
 */
public class MusicGroup extends RelativeLayout {
    private ImageView mSongPhoto;
    private ImageView mLast,mPause,mNext;
    private TextView mSongName,mSingerName,mStarTime,mEndTime;
    private SeekBar mProgress;
    private MusicMode musicMode;
    private View[] views;
    private Context mContext;
    public MusicGroup(Context context) {
        super(context);
        this.mContext=context;
    }

    public MusicGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
    }

    public MusicGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
    }
    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        initView();
        musicMode=new MusicMode(mContext,views);//音乐Model

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
