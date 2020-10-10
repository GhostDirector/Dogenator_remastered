package fi.jt.dogenator.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import static fi.jt.dogenator.Constants.MUSIC_DEFAULT;

public class MusicPlayer {

    private static MusicPlayer mInstance;
    private Music mMusic;

    private boolean isMuted;
    private int mOriginalVolume = 20;

    // TODO: implement music null check
    private MusicPlayer() {
        mMusic = Gdx.audio.newMusic(Gdx.files.internal(MUSIC_DEFAULT));

        // TODO: refactor and improve
        isMuted = false;
    }

    public static MusicPlayer getInstance() {
        if(mInstance == null) {
            mInstance = new MusicPlayer();
        }
        return mInstance;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        if(muted) {
            isMuted = muted;
            setVolume(0);
        } else {
            isMuted = muted;
            setVolume(mOriginalVolume);
        }
    }

    public void setTrack(String track) {
        if(mMusic.isPlaying()) {
            mMusic.stop();
        }
        mMusic = Gdx.audio.newMusic(Gdx.files.internal(track));
    }

    public void playMusic() {
        if(!mMusic.isPlaying()) {
            mMusic.play();
        }
    }

    public void pauseMusic() {
        if(mMusic.isPlaying()) {
            mMusic.pause();
        }
    }

    public void stopMusic() {
        if(mMusic.isPlaying()) {
            mMusic.stop();
        }
    }

    public void setVolume(float volume) {
        if(isMuted && volume > 0) {
            isMuted = false;
        }
        mMusic.setVolume(volume);
    }

    public boolean isPlaying() {
        return mMusic.isPlaying();
    }
}
