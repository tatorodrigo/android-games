package br.com.tattobr.samples.framework.impl;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

import br.com.tattobr.samples.framework.Music;

public class AndroidMusic implements Music, MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private boolean isPrepared;

    public AndroidMusic(AssetFileDescriptor assetFileDescriptor) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength()
            );
            mediaPlayer.prepare();
            isPrepared = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        synchronized (this) {
            isPrepared = false;
        }
    }

    @Override
    public void play() {
        if (!mediaPlayer.isPlaying()) {
            try {
                synchronized (this) {
                    if (!isPrepared) {
                        mediaPlayer.prepare();
                        isPrepared = true;
                    }
                    mediaPlayer.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
        synchronized (this) {
            isPrepared = false;
        }
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public boolean isStopped() {
        return !isPrepared;
    }

    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    @Override
    public void dispose() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }
}
