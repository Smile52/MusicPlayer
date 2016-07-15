package com.dandy.musicplayer.mode;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.os.Handler;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dandy.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smile on 2016/7/13.
 */
public class MusicMode implements View.OnClickListener {
    private View[] mViews;
    private Context mContext;
    private List<Music> mPlayList;//播放列表
    private MusicPlayer mMusicPlayer;//播放者
    private Cursor mCursor;
    private MusicPlayer.PlayerListener mPlayerListener;//播放监听
    private int mSeekPosition;
    private SeekBar mSeekBar;//进度条
    private ImageView mPlayButton,mMusicPhoto;
    private Handler mHandler;
    private TextView mMusicTotalTime,mCurrentPlayTime,mSongName,mSingerName;//第一个全部时间，第二个播放当前时间
    private List<Music> musicList=new ArrayList<>();
    private int count=0;
    private ScanMusicTask mScanMusicTask=null;
    private   Music music;
    public MusicMode(final Context mContext, final View[] views) {
        this.mContext=mContext;
        this.mViews = views;

        mScanMusicTask=new ScanMusicTask();
        mScanMusicTask.execute();
        mScanMusicTask.setOnDataFinishedListener(new OnDataFinishedListener() {
            @Override
            public void onDataSuccessfully(Object data) {
                musicList = (List<Music>) data;
                if (musicList.size()>0){
                music = musicList.get(0);
                mMusicPlayer = MusicPlayer.getInstance();
                mMusicPlayer.setPlayList(musicList);
                mMusicPlayer.setListener(getPlayerListener());
                initViews(views);
                }
            }
            @Override
            public void onDataFailed() {

            }
        });

    }

    /**
     * 初始化View
     * @param views
     */
    public void initViews(View[] views){
        if (views != null) {
            for (int i = 0; i < views.length; i++) {
                switch (views[i].getId()){
                    case R.id.lg_music_song_photo_Img:
                        views[i].setOnClickListener(this);
                        mMusicPhoto= (ImageView) views[i];
                        Bitmap bitmap=MediaUtils.getArtwork(mContext,music.getMusicId(),music.getmAlbuId(),true,false);
                        mMusicPhoto.setImageBitmap(bitmap);
                        break;
                    //上一首
                    case R.id.lg_music_last_img:
                        views[i].setOnClickListener(this);
                        break;
                    //下一首
                    case R.id.lg_music_next_img:
                        views[i].setOnClickListener(this);
                        break;
                    //暂停
                    case R.id.lg_music_pause_img:
                        views[i].setOnClickListener(this);
                        mPlayButton = (ImageView) views[i];
                        break;
                    //进度条
                    case R.id.lg_music_seekbar:
                        mSeekBar= (SeekBar) views[i];
                        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    mSeekPosition = progress;
                                    Log.i("tag","进度条"+mSeekPosition);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                if(count==0){
                                    mMusicPlayer.play(0);
                                    count++;
                                }
                                mMusicPlayer.setCurrentProgress(mSeekPosition);
                                updateUI();
                            }
                        });
                        break;
                    case R.id.lg_music_singer_name_tv:
                        mSingerName= (TextView) views[i];
                        mSingerName.setText(music.getmSingerName());
                        break;
                    case R.id.lg_music_song_name_tv:
                        mSongName= (TextView) views[i];
                        mSongName.setText(music.getMusicName());
                        break;
                    case R.id.lg_music_end_time_tv:
                        mMusicTotalTime= (TextView) views[i];
                        break;
                    case R.id.lg_music_play_time_tv:
                        mCurrentPlayTime= (TextView) views[i];
                        break;

                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lg_music_song_photo_Img:
                Toast.makeText(mContext,"点击了图片哦",Toast.LENGTH_SHORT).show();
                break;
            case R.id.lg_music_last_img:
                  int result=mMusicPlayer.getIndex();
                    if(result==0){
                    Toast.makeText(mContext,"已经到第一首歌了",Toast.LENGTH_SHORT).show();
                     }else {
                        mMusicPlayer.playLast();
                    }
                break;
            case R.id.lg_music_next_img:
                    mMusicPlayer.playNext();

                break;
            //暂停
            case R.id.lg_music_pause_img:

                if (mMusicPlayer.isPlaying()) {
                    mMusicPlayer.pause();
                } else if(count==0) {
                    mMusicPlayer.play(mMusicPlayer.getIndex());
                    count++;
                }else {
                    mMusicPlayer.resume();
                }
                break;
        }
    }


    /**
     * 更新UI
     */
    public void updateUI() {
        mHandler=new Handler(mContext.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mMusicPlayer.isPlaying()) {
                    mPlayButton.setImageResource(R.drawable.lg_music_start);
                } else {
                    mPlayButton.setImageResource(R.drawable.lg_music_pause);
                }

                int progress = mMusicPlayer.getCurrentProgress();
                mSeekBar.setMax(mMusicPlayer.getTotalTime());
                mSeekBar.setProgress(progress);
                mCurrentPlayTime.setText(MediaUtils.formatTime(progress));
                mMusicTotalTime.setText(MediaUtils.formatTime(mMusicPlayer.getTotalTime()));
                mSongName.setText(mMusicPlayer.getMusicName());
                mSingerName.setText(mMusicPlayer.getSingerName());
                Bitmap bitmap=MediaUtils.getArtwork(mContext,mMusicPlayer.getSongId(),mMusicPlayer.getAlbumId(),true,false);
                mMusicPhoto.setImageBitmap(bitmap);
            }
        });
    }

    public MusicPlayer.PlayerListener getPlayerListener() {
        if (mPlayerListener == null) {
            mPlayerListener = new MusicPlayer.PlayerListener() {
                @Override
                public void onPlay() {
                    updateUI();
                }
                @Override
                public void onPause() {
                    updateUI();
                }

                @Override
                public void onResume() {
                    updateUI();
                }

                @Override
                public void onPlayNext() {
                    updateUI();
                }

                @Override
                public void onPlayPrev() {
                    updateUI();
                }

                @Override
                public void onProgressUpdate(int progress) {
                    updateUI();
                }

                @Override
                public void onStop() {

                }
            };
        }
        return mPlayerListener;
    }
    class ScanMusicTask extends AsyncTask<Void,Integer,List<Music>>{
        OnDataFinishedListener onDataFinishedListener;

        public void setOnDataFinishedListener(
                OnDataFinishedListener onDataFinishedListener) {
            this.onDataFinishedListener = onDataFinishedListener;
        }

        @Override
        protected List<Music> doInBackground(Void... params) {
            // 惰性初始化
            if (mPlayList != null) {
                return mPlayList;
            }
            mPlayList = new ArrayList<>();
            if (mCursor != null) {
                mCursor.close();
                mCursor = null;
            }
            // 查询音乐
            mCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,new String[] { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.YEAR,
                    MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST }, null, null, null);

            if (mCursor == null || mCursor.moveToFirst() == false) {
                //Toast.makeText(mContext, "没有音乐", Toast.LENGTH_LONG).show();
                return mPlayList;
            }
            int index = 0;
            mCursor.moveToFirst();
            while (mCursor.moveToNext()) {
                Music song = new Music();
                song.setMusicId(index);
                song.setFileName(mCursor.getString(1));
                song.setMusicName(mCursor.getString(2));
                song.setMusicDuration(mCursor.getInt(3));
                song.setMusicArtist(mCursor.getString(4));
                song.setMusicAlbum(mCursor.getString(5));
                song.setMusicYear(mCursor.getString(6));
                song.setmAlbuId(mCursor.getInt(10));
                song.setmSingerName(mCursor.getString(11));
                //Log.i("tag","name"+mCursor.getString(11));
                // file type
                if ("audio/mpeg".equals(mCursor.getString(7).trim())) {
                    song.setFileType("mp3");
                } else if ("audio/x-ms-wma".equals(mCursor.getString(7).trim())){
                    song.setFileType("wma");
                }
                song.setFileType(mCursor.getString(7));
                song.setFileSize(mCursor.getString(8));
                if (mCursor.getString(9) != null) {
                    song.setFilePath(mCursor.getString(9));
                }
                index++;
                mPlayList.add(song);
            }
            mCursor.close();
            mCursor = null;

            return mPlayList;
        }

        @Override
        protected void onPostExecute(List<Music> musics) {
            super.onPostExecute(musics);
            if(musics!=null){
                onDataFinishedListener.onDataSuccessfully(musics);
            }else {
                onDataFinishedListener.onDataFailed();
            }

        }
    }

    public interface OnDataFinishedListener {

        public void onDataSuccessfully(Object data);

        public void onDataFailed();

    }
}
