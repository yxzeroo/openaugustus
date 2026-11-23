package net.augustus.vendor;

public class PlayerEffectEntry {
   private int effectSlot;
   private String effectData;
   private String effectName;
   private String playerName;

   public String getEffectData() {
      return this.effectData;
   }

   public String getEffectName() {
      return this.effectName;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public int getEffectSlot() {
      return this.effectSlot;
   }

   public PlayerEffectEntry(String str, String str2, byte b, int n, String str3, int n2, int n3) {
      PlayerEffectRegistry.getOpaqueStateTag();
      this.playerName = str;
      this.effectName = str2;
      this.effectSlot = n2;
      this.effectData = str3;
   }
}
