package net.augustus.modules.modes.anticheat;

public enum HitbugFlagType {
   HT_LIGHT("HitLight"),
   HT_MEDIUM("HitMedium"),
   DMG_MEDIUM("HitMedium"),
   DM_LIGHT("DMGLight"),
   GARBAGE_HIT("GarbageHit"),
   BLOCKING("BLOCKING"),
   CANCEL("cancel"),
   CANCEL_FIRST("CancelFist");

   private final String label;

   private HitbugFlagType(String str) {
      this.label = str;
   }

   public String getLabel() {
      return this.label;
   }
}
