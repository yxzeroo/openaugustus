package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.settings.Setting;
import net.augustus.events.EventSafeWalk;
import net.augustus.events.listeners.SafeWalkListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class SafeWalk extends Module implements SafeWalkListener, MoveInputListener {
   private final BooleansSetting mode;
   private final BooleanValue inAir;
   private final BooleanValue edgeCheck;
   private final BooleanValue activateOnlyIfLookingDown;
   private final StringValue pitchToActivate;

   public SafeWalk() {
       super("SafeWalk", "SichererWeg", new Color(115, 135, 26), Categorys.MOVEMENT);
      this.mode = new BooleansSetting(this, "Mode", "Basic", new String[]{"Basic", "Sneak"});
      BooleanValue booleanValue = new BooleanValue(this, "InAir", true);
      String str = "In air";
      this.inAir = (BooleanValue)booleanValue.withDisplayName(str);
      BooleanValue booleanValue2 = new BooleanValue(this, "EdgeCheck", true);
      BooleanValue inAir2 = this.inAir;
      this.inAir.getClass();
      BooleanValue booleanValue3 = inAir2;
      SettingVisibilityCondition condition = () -> booleanValue3.getValue();
      BooleanValue booleanValue4 = (BooleanValue)booleanValue2.withVisibility(condition);
      String str2 = "Edge Check";
      this.edgeCheck = (BooleanValue)booleanValue4.withDisplayName(str2);
      this.activateOnlyIfLookingDown = new BooleanValue(this, "Activate only if looking down", false);
      this.pitchToActivate = new StringValue(this, "Pitch to activate", 70.0, 0.0, 90.0, 0);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 33;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onSafeWalk(EventSafeWalk event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.passesPitchCondition()) {
         Block block = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ).getBlock();
         Block block2 = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ).getBlock();
         if (mc.thePlayer.onGround && this.isLicenseTimestampValid()) {
            if (this.mode.getSelectedValue().equals("Basic")) {
               boolean flag = true;
               event.setSafeWalk(flag);
            }
         } else if (this.inAir.getValue() && (block instanceof BlockAir && block2 instanceof BlockAir || !this.edgeCheck.getValue())) {
            boolean flag2 = true;
            event.setSafeWalk(flag2);
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.mode.getSelectedValue().equals("Sneak") && !this.passesPitchCondition()) {
         Block block = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ).getBlock();
         Block block2 = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ).getBlock();
         double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
         float f = mc.thePlayer.width / 2.0F;
         AxisAlignedBB box = new AxisAlignedBB(
            doubles[0] - (double)f,
            doubles[1],
            doubles[2] - (double)f,
            doubles[0] + (double)f,
            doubles[1] + (double)mc.thePlayer.height,
            doubles[2] + (double)f
         );
         boolean flag = false;
         if (!mc.thePlayer.onGround && block instanceof BlockAir && block2 instanceof BlockAir || !this.edgeCheck.getValue()) {
            box.offset(0.0, mc.thePlayer.lastGroundY - mc.thePlayer.getEntityBoundingBox().minY, 0.0);
            flag = true;
         }

         if (mc.thePlayer.onGround) {
            flag = true;
         }

         if (flag && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, box.offset(0.0, -0.51, 0.0).expand(-0.22, 0.0, -0.22)).isEmpty()) {
            boolean flag2 = true;
            event.setSneak(flag2);
         }
      }
   }

   private boolean passesPitchCondition() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.activateOnlyIfLookingDown.getValue() || (double)mc.thePlayer.rotationPitch <= this.pitchToActivate.getValue();
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      Fly.getCachedSettings();
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
