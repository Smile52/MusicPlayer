package com.dandy.musicplayer.mode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐播放控制者
 */
public class MusicPlayer {
    public interface PlayerListener {
        void onPlay();
        void onPause();
        void onResume();
        void onPlayNext();
        void onPlayPrev();
        void onProgressUpdate(int progress);
        void onStop();
    }
    private static MusicPlayer mMusicPlayer;
    private List<Music> mPlayList;          // 播放列表
    private boolean mPlaying;               // 是否正在播放
    private int mCurrentIndex;              // 目前播放的位置
    private int mCurrentProgress;           // 播放进度
    private MediaPlayer mMediaPlayer;       // 播放器
    private PlayerListener mListener;       // 监听器
    private Timer mTimer;                   // 计时器
    private int mTotalTime;                 // 播放时间
    private Music music;
    private int count=0;
    private int index;
    public static MusicPlayer getInstance() {
        if (mMusicPlayer == null) {
            mMusicPlayer = new MusicPlayer();
        }
        return mMusicPlayer;
    }

    public void play(final int index) {
        if (index < 0 || index >= mPlayList.size()) {
            return;
        }
        // 如果播放的歌曲是目前的歌曲
        music = mPlayList.get(index);
        setIndex(index);

        if (music.equals(mPlayList.get(mCurrentIndex))) {
            // 恢复播放
            if (!mPlaying&&count>0) {
                mMediaPlayer.start();
                count++;
                return;
            }
        }
        // 先释放MediaPlayer
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mCurrentIndex = index;
        mCurrentProgress = 0;
        mPlaying = true;
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(music.getFilePath());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            startTimer();
            if (mListener != null) {
                mListener.onPlay();
            }
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playNext();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mPlaying) {
            mMediaPlayer.pause();
            mPlaying = false;
            if (mListener != null) {
                mListener.onPause();
            }
        }
    }

    public void resume() {
        if (!mPlaying) {
            mMediaPlayer.start();
            mPlaying=true;
            if (mListener != null) {
                mListener.onResume();
            }
        }
    }

    public int playNext() {
        play(mCurrentIndex + 1);
        if (mListener != null){
            mListener.onPlayNext();
        }if(mCurrentIndex+1==mPlayList.size()){
            return 1;
        }
        return 0;
    }

    public int playLast() {
        play(mCurrentIndex - 1);
        if (mListener != null){
            mListener.onPlayPrev();
        }if(mCurrentIndex -1<0){
           return 1;
        }
        return 0;
    }
    public void stop(){
        if (mPlaying) {
            mMediaPlayer.stop();
            mPlaying = false;
            if (mListener != null) {
                mListener.onStop();
            }
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Music> getPlayList() {
        return mPlayList;
    }

    public void setPlayList(List<Music> playList) {
        mPlayList = playList;
    }

    public boolean isPlaying() {
        return mPlaying;
    }

    public void setPlaying(boolean playing) {
        mPlaying = playing;
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        mCurrentProgress = currentProgress;
        if(isPlaying()){
            mMediaPlayer.seekTo(mCurrentProgress);
        }
        else if(getCurrentProgress()>0){
            try {
                mMediaPlayer.seekTo(mCurrentProgress);
            }catch (Exception e){

            }

    }
    }


    public PlayerListener getListener() {
        return mListener;
    }

    public void setListener(PlayerListener listener) {
        mListener = listener;
    }

    public int getTotalTime() {
        return mMediaPlayer.getDuration();
    }
    public String getMusicName(){
        return  music.getMusicName();
    }

    /**
     * 拿到歌手名字
     * @return
     */
    public String getSingerName(){
        return music.getmSingerName();
    }

    /**
     * 拿到专辑ID
     * @return
     */
    public int getAlbumId(){
        return music.getmAlbuId();
    }

    /**
     * 拿到歌曲ID
     * @return
     */
    public int getSongId(){
        return music.getMusicId();
    }

    /**
     * 一秒钟更新一下进度条
     */
    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mPlaying) {
                        mCurrentProgress = mMediaPlayer.getCurrentPosition();
                        if (mListener != null) {
                            mListener.onProgressUpdate(mCurrentProgress);
                        }
                    }
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }

            }
        }, 0, 1000);
    }


}
