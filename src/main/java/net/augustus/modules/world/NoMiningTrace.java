package net.augustus.modules.world;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.WeaponFilterSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.settings.BooleanValue;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class NoMiningTrace extends Module {
   public final BooleanValue alwaysAtRightClicking;
   public final BooleanValue alwaysAfterBlockBreakStarted;
   public final BooleanValue onlypickaxesWhenLeftclicking;
   public final WeaponFilterSettings excludedWeapons;

   public NoMiningTrace() {
       super("NoMiningTrace", "KeineBergbauspuren", new Color(66, 135, 245), Categorys.WORLD);
      this.alwaysAtRightClicking = (BooleanValue)(new BooleanValue(this, "AlwaysAtRightClicking", true)).markVisualOnly();
      this.alwaysAfterBlockBreakStarted = (BooleanValue)(new BooleanValue(this, "AlwaysAfterBlockBreakStarted", true)).markVisualOnly();
      this.onlypickaxesWhenLeftclicking = (BooleanValue)(new BooleanValue(this, "OnlyPickaxes when leftclicking", false)).markVisualOnly();
      this.excludedWeapons = (WeaponFilterSettings)(new WeaponFilterSettings(this, "Excluded weapons")).markAllVisualOnly();
      String str = "Rest";
      this.excludedWeapons.setOptionEnabled(str, false);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
   }

   public boolean shouldIgnoreEntities() {
      if (this.isToggled()
         && this.isLicenseValid()
         && (!this.onlypickaxesWhenLeftclicking.getValue() || mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemPickaxe)) {
         if (!this.excludedWeapons.isItemAllowed(mc.thePlayer.getCurrentEquippedItem()) || mc.playerController.isHittingBlock && this.alwaysAfterBlockBreakStarted.getValue()) {
            return true;
         }
      }

      return false;
   }

   private boolean isLicenseValid() {
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
