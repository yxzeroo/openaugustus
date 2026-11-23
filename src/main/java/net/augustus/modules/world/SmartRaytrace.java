package net.augustus.modules.world;

import java.awt.Color;
import java.util.ArrayList;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.player.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

public class SmartRaytrace extends Module {

   public SmartRaytrace() {
       super("SmartRaytrace", "SchlauerRaytrace", Color.black, Categorys.WORLD);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
   }

   public void updateSmartRaytrace() {
      if (mc.thePlayer != null && mc.entityRenderer != null && this.isLicenseValid()) {
         ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
         if (!PlayerUtil.canPlaceHeldItemAtMouseOver(currentEquippedItem) && (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)) {
            boolean flag = false;
            ArrayList list = new ArrayList();
            MovingObjectPosition hit = null;

            for (float i = 0.05F; i < 1.0F; i += 0.05F) {
               mc.entityRenderer.getMouseOver(i, false);
               currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
               if (PlayerUtil.canPlaceHeldItemAtMouseOver(currentEquippedItem)) {
                  if (hit == null) {
                     hit = mc.objectMouseOver;
                  }

                  if (mc.objectMouseOver.sideHit != EnumFacing.UP) {
                     flag = true;
                     list.add(mc.objectMouseOver);
                  }
               }
            }

            if (flag) {
               Minecraft mc2 = mc;
               RandomNumberGenerator instance = DefaultRandomGenerator.instance;
               int n = list.size() - 1;
               byte b = 0;
               MovingObjectPosition hit2 = (MovingObjectPosition)list.get(instance.nextInt2(b, n));
               mc2.objectMouseOver = hit2;
            }

            if (hit != null && !flag) {
               mc.objectMouseOver = hit;
            }

            if (!flag && hit == null) {
               mc.entityRenderer.getMouseOver(1.0F, false);
            }
         }
      }
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
