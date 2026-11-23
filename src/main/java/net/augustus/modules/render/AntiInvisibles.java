package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.settings.TargetSelectionSettings;
import net.minecraft.entity.Entity;

public class AntiInvisibles extends Module {
   private final TargetSelectionSettings entities;

   public AntiInvisibles() {
       super("AntiInvisibles", "GegenUnsichtbare", new Color(87, 247, 196), Categorys.RENDER);
      this.entities = new TargetSelectionSettings(this, TargetFilterPresets.createRenderFilter()).withTargetsDisplayName("Entities");
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
   }

   public boolean shouldRevealEntity(Entity entity) {
      if (this.entities.getTargetFilter().matches(entity)) {
         if (this.isLicenseTimestampValid()) {
            return true;
         }
      }

      return false;
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
