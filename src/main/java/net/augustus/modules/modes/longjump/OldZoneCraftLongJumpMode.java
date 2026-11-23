package net.augustus.modules.modes.longjump;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.player.PlayerUtil;
import java.util.function.BiFunction;
import net.augustus.modules.movement.LongJump;
import net.augustus.settings.DoubleValue;

public class OldZoneCraftLongJumpMode extends AbstractLongJumpMode implements UpdateListener {
   private boolean boosted = false;

   public OldZoneCraftLongJumpMode(LongJump longJump, DoubleValue doubleValue) {
      super(longJump, doubleValue, "OldZoneCraft");
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      this.boosted = false;
      if (mc.thePlayer != null) {
         PlayerUtil.damageSelfShort();
      }
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new OldZoneCraftLongJumpMode((LongJump)obj, (DoubleValue)obj2);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      float f = this.boosted ? 1.0F : 0.0F;
      event.setMoveForward(f);
   }

   @Override
   public void onUpdate2() {
      if (mc.thePlayer.hurtTime > 0) {
         mc.getTimer().timerSpeed = 1.2F;
         MoveUtil.setSpeed2(0.48F);
         mc.thePlayer.motionY = 0.62F;
         this.boosted = true;
      } else {
         mc.getTimer().timerSpeed = 1.0F;
      }

      if (this.boosted && mc.thePlayer.fallDistance > 0.0F) {
         mc.thePlayer.motionY = -0.1;
      }
   }

}
