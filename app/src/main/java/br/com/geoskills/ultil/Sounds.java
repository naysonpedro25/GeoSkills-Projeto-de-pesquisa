package br.com.geoskills.ultil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;


import br.com.geoskills.R;

public class Sounds {
   @SuppressLint("StaticFieldLeak")
   private static Sounds instance;
   private final SoundPool soundPool;
   private final MediaPlayer mediaPlayer;
   private final int clickSoundId;
   private final int transitSoundId;
   private boolean isClickSoundEnabled = true;
   private boolean isMusicEnabled = true;

   public boolean getIsMusicEnabled() {
      return isMusicEnabled;
   }

   public boolean getIsClickSoundEnabled() {
      return isClickSoundEnabled;
   }


   private Sounds(Context context) {

      AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
      soundPool = new SoundPool.Builder().setMaxStreams(2).setAudioAttributes(audioAttributes).build();
      clickSoundId = soundPool.load(context, R.raw.click_sound_best, 1);
      transitSoundId = soundPool.load(context, R.raw.transition_sound, 1);

      mediaPlayer = MediaPlayer.create(context, R.raw.matue666777);
      mediaPlayer.setLooping(true);

   }

   public static Sounds getInstance(Context context) {
      if (instance == null) {
         instance = new Sounds(context);
      }
      return instance;
   }

   public void clickSound() {
      if (isClickSoundEnabled) {
         soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1);
      }
   }

   public void transitSound() {
      if (isClickSoundEnabled) {
         soundPool.play(transitSoundId, 1f, 1f, 1, 0, 1);
      }
   }

   public void setClickSoundEnabled(boolean isClickSoundEnabled) {
      this.isClickSoundEnabled = isClickSoundEnabled;
   }


   public void setMusicEnabled(boolean isMusicEnabled) {
      this.isMusicEnabled = isMusicEnabled;
      if (isMusicEnabled) {
         if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
         }
      } else {
         if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
         }
      }
   }
//
//   public void setMusicEnabled(boolean isMusicEnabled) {
//      this.isMusicEnabled = isMusicEnabled;
//      if (isMusicEnabled && mediaPlayer != null) {
//         mediaPlayer.start();
//      }else
//      if (mediaPlayer != null) {
//         mediaPlayer.pause();
//      }
// }

   // hint pra eu mesmo, colocar o som de click no construtor do fragment e não no addOnclickListener

}


