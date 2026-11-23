package net.augustus.quests;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.function.Consumer;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import net.augustus.utils.interfaces.MC;

public abstract class QuestObjective implements MC {
   private String encryptedProgressHash;
   private final double targetAmount;
   private boolean active;
   private SecretKey encryptionKey;
   private Consumer progressListener;
   private String encryptedProgress;

   public double getProgress() throws Exception {
      String encryptedProgress2 = this.encryptedProgress;
      if (!this.encryptedProgressHash.equals(this.computeSha256(encryptedProgress2))) {
         throw new SecurityException("Data integrity violation: possible tampering detected.");
      } else {
         return this.decryptProgress(this.encryptedProgress);
      }
   }

   private double bytesToDouble(byte[] bytes) {
      return ByteBuffer.wrap(bytes).getDouble();
   }

   private double decryptProgress(String str) throws Exception {
      Cipher instance = Cipher.getInstance("AES");
      instance.init(2, this.encryptionKey);
      return this.bytesToDouble(instance.doFinal(Base64.getDecoder().decode(str)));
   }

   public abstract void onActivate();

   public void setActive(boolean flag, long l) {
      if (this.active != flag) {
         this.active = flag;
         if (flag) {
            this.onActivate();
         } else {
            this.onDeactivate();
         }
      }
   }

   public abstract String getActionVerb();

   private SecretKey generateAesKey() throws Exception {
      KeyGenerator instance = KeyGenerator.getInstance("AES");
      instance.init(128);
      return instance.generateKey();
   }

   public double getTargetAmount() {
      return this.targetAmount;
   }

   public abstract String getUnitLabel();

   public abstract void onDeactivate();

   public QuestObjective(double d) {
       super();
      this.targetAmount = d;

      try {
         this.encryptionKey = this.generateAesKey();
         this.setProgress(0.0);
      } catch (Exception e) {
         this.encryptionKey = null;
      }
   }

   public void addProgress(double d) {

      try {
         this.setProgress(this.getProgress() + d);
      } catch (Exception e) {
      }
   }

   private String encryptProgress(double d) throws Exception {
      Cipher instance = Cipher.getInstance("AES");
      instance.init(1, this.encryptionKey);
      byte[] bytes = instance.doFinal(this.doubleToBytes(d));
      return Base64.getEncoder().encodeToString(bytes);
   }

   public void setProgressListener(Consumer consumer) {
      this.progressListener = consumer;
   }

   private byte[] doubleToBytes(double d) {
      return ByteBuffer.allocate(8).putDouble(d).array();
   }

   public void setProgress(double d) throws Exception {
      this.encryptedProgress = this.encryptProgress(d);
      this.encryptedProgressHash = this.computeSha256(this.encryptedProgress);
      if (this.progressListener != null && d != this.targetAmount) {
         this.progressListener.accept(d / this.targetAmount * 100.0);
      }
   }

   private String computeSha256(String str) throws Exception {
      byte[] bytes = MessageDigest.getInstance("SHA-256").digest(str.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(bytes);
   }

   public void reset() throws Exception {
      this.setProgress(0.0);
      this.onDeactivate();
   }




}
