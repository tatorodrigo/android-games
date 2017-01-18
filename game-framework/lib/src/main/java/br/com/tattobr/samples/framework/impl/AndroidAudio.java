package br.com.tattobr.samples.framework.impl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

import br.com.tattobr.samples.framework.Audio;
import br.com.tattobr.samples.framework.Music;
import br.com.tattobr.samples.framework.Sound;

public class AndroidAudio implements Audio {
    private AssetManager assets;
    private SoundPool soundPool;

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        } else {
            this.soundPool = new SoundPool
                    .Builder()
                    .setMaxStreams(20)
                    .setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_GAME)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
                    ).build();
        }
    }

    @Override
    public Music newMusic(String fileName) {
        return null;
    }

    @Override
    public Sound newSound(String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = assets.openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("Could not load sound '" + fileName + "'");
        }
    }
}
