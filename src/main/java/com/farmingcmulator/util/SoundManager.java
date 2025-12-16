package com.farmingcmulator.util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

/**
 * SoundManager - Manages all game audio including background music and sound effects.
 * Singleton pattern for global access across all controllers.
 */
public class SoundManager {
    
    private static SoundManager instance;
    
    // Background music
    private MediaPlayer bgmPlayer;
    private boolean bgmEnabled = true;
    private double bgmVolume = 0.5;
    
    // Sound effects
    private boolean sfxEnabled = true;
    private double sfxVolume = 0.5;
    
    // Sound effect clips (preloaded for instant playback)
    private AudioClip sfxHover;
    private AudioClip sfxClick;
    private AudioClip sfxPopup;
    private AudioClip sfxPurchase;
    private AudioClip sfxPlant;
    private AudioClip sfxWater;
    private AudioClip sfxHarvest;
    private AudioClip sfxSuccess;
    private AudioClip sfxCoins;
    private AudioClip sfxError;
    
    private SoundManager() {
        loadSounds();
    }
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Load all sound files from resources
     */
    private void loadSounds() {
        try {
            // Load background music
            URL bgmUrl = getClass().getResource("/audio/bgm_farm.mp3");
            if (bgmUrl != null) {
                Media bgmMedia = new Media(bgmUrl.toExternalForm());
                bgmPlayer = new MediaPlayer(bgmMedia);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
                bgmPlayer.setVolume(bgmVolume);
            }
            
            // Load sound effects
            sfxHover = loadAudioClip("/audio/sfx_hover.wav");
            sfxClick = loadAudioClip("/audio/sfx_click.wav");
            sfxPopup = loadAudioClip("/audio/sfx_popup.wav");
            sfxPurchase = loadAudioClip("/audio/sfx_purchase.wav");
            sfxPlant = loadAudioClip("/audio/sfx_plant.wav");
            sfxWater = loadAudioClip("/audio/sfx_water.wav");
            sfxHarvest = loadAudioClip("/audio/sfx_harvest.wav");
            sfxSuccess = loadAudioClip("/audio/sfx_success.wav");
            sfxCoins = loadAudioClip("/audio/sfx_coins.wav");
            sfxError = loadAudioClip("/audio/sfx_error.wav");
            
        } catch (Exception e) {
            System.err.println("Warning: Could not load some audio files. Game will continue without sound.");
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to load AudioClip from resources
     */
    private AudioClip loadAudioClip(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                return new AudioClip(url.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Could not load audio: " + path);
        }
        return null;
    }
    
    // ==================== BACKGROUND MUSIC ====================
    
    /**
     * Start playing background music (loops indefinitely)
     */
    public void playBGM() {
        if (bgmPlayer != null && bgmEnabled) {
            bgmPlayer.seek(Duration.ZERO);
            bgmPlayer.play();
        }
    }
    
    /**
     * Stop background music
     */
    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }
    
    /**
     * Pause background music
     */
    public void pauseBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.pause();
        }
    }
    
    /**
     * Resume background music
     */
    public void resumeBGM() {
        if (bgmPlayer != null && bgmEnabled) {
            bgmPlayer.play();
        }
    }
    
    /**
     * Set background music volume (0.0 to 1.0)
     */
    public void setBgmVolume(double volume) {
        this.bgmVolume = Math.max(0.0, Math.min(1.0, volume));
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(bgmVolume);
        }
    }
    
    /**
     * Toggle background music on/off
     */
    public void toggleBGM() {
        bgmEnabled = !bgmEnabled;
        if (bgmEnabled) {
            resumeBGM();
        } else {
            pauseBGM();
        }
    }
    
    public boolean isBgmEnabled() {
        return bgmEnabled;
    }
    
    // ==================== SOUND EFFECTS ====================
    
    /**
     * Play a sound effect
     */
    private void playSFX(AudioClip clip) {
        if (clip != null && sfxEnabled) {
            clip.play(sfxVolume);
        }
    }
    
    /**
     * Set sound effects volume (0.0 to 1.0)
     */
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }
    
    /**
     * Toggle sound effects on/off
     */
    public void toggleSFX() {
        sfxEnabled = !sfxEnabled;
    }
    
    public boolean isSfxEnabled() {
        return sfxEnabled;
    }
    
    // ==================== SPECIFIC SOUND METHODS ====================
    
    /**
     * Play hover sound (when mouse enters a button)
     */
    public void playHover() {
        playSFX(sfxHover);
    }
    
    /**
     * Play click sound (when button is pressed)
     */
    public void playClick() {
        playSFX(sfxClick);
    }
    
    /**
     * Play popup sound (when info/dialog popup appears)
     */
    public void playPopup() {
        playSFX(sfxPopup);
    }
    
    /**
     * Play purchase sound (when buying seed box)
     */
    public void playPurchase() {
        playSFX(sfxPurchase);
    }
    
    /**
     * Play plant sound (when planting a seed)
     */
    public void playPlant() {
        playSFX(sfxPlant);
    }
    
    /**
     * Play water sound (when watering a crop)
     */
    public void playWater() {
        playSFX(sfxWater);
    }
    
    /**
     * Play harvest sound (when harvesting a crop)
     */
    public void playHarvest() {
        playSFX(sfxHarvest);
    }
    
    /**
     * Play success sound (general success/completion)
     */
    public void playSuccess() {
        playSFX(sfxSuccess);
    }
    
    /**
     * Play coins sound (when receiving money)
     */
    public void playCoins() {
        playSFX(sfxCoins);
    }
    
    /**
     * Play error sound (when action fails)
     */
    public void playError() {
        playSFX(sfxError);
    }
    
    // ==================== UTILITY ====================
    
    /**
     * Stop all sounds and release resources
     */
    public void dispose() {
        stopBGM();
        if (bgmPlayer != null) {
            bgmPlayer.dispose();
        }
    }
}
