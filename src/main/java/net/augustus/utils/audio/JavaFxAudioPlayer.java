package net.augustus.utils.audio;

import net.augustus.Augustus;
import net.augustus.utils.file.ClientFileUtil;
import javafx.scene.media.AudioClip;

public class JavaFxAudioPlayer {
   private AudioClip audioClip = null;

   public void playResource(String str, boolean flag) {
      if (str.startsWith("assets/augustus/")) {
         str = str.replace("assets/augustus/", "");
      }

      String[] strings = str.split("/");
      StringBuilder sb = new StringBuilder(Augustus.getInstance().getName().toLowerCase() + "/temp/musicplayer/");

      for (int i = 0; i < strings.length - 1; i++) {
         sb.append(strings[i]);
      }

      String str2 = "assets/augustus/" + str;
      String str3 = sb.toString();
      ClientFileUtil.extractResource(str2, str3, flag);
      this.audioClip = new AudioClip(ClientFileUtil.toFileUri(ClientFileUtil.WORKING_DIR + sb + "/" + strings[strings.length - 1]));
      this.audioClip.play();
   }

   public void stop() {

      try {
         if (this.isPlaying()) {
            this.audioClip.stop();
         }
      } catch (Throwable e) {
         System.err.println("Failed to stop music player");
      }
   }

   public void playResource(String str) {
      boolean flag = false;
      this.playResource(str, flag);
   }

   public boolean isPlaying() {
      return this.audioClip != null && this.audioClip.isPlaying();
   }

   public void playFile(String str) {
      this.audioClip = new AudioClip(ClientFileUtil.toFileUri(str));
      this.audioClip.play();
   }

   public void setCycleCount(int n) {
      if (this.audioClip != null) {
         this.audioClip.setCycleCount(n);
      }
   }

   public void setVolume(double d) {
      if (this.isPlaying()) {
         this.audioClip.setVolume(d);
      }
   }




}
