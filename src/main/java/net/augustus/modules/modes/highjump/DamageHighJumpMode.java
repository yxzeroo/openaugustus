package net.augustus.modules.modes.highjump;

import net.augustus.events.EventMotion;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.player.PlayerUtil;
import java.util.function.BiFunction;
import net.augustus.modules.movement.HighJump;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class DamageHighJumpMode extends HighJumpMode implements MotionListener {
   public final StringValue damageHeight;

   public DamageHighJumpMode(HighJump highJump, DoubleValue doubleValue) {
      super(highJump, doubleValue, "Damage");
      StringValue stringValue = new StringValue(this.parentModule, "DamageHeight", 1.0, 0.42, 5.0, 2, this.modeSetting);
      String str = "Height";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.damageHeight = stringValue2.visibleWhen(condition);
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new DamageHighJumpMode((HighJump)obj, (DoubleValue)obj2);
   }

   @Override
   public void onEnable() {
      super.onEnable();
      if (mc.thePlayer != null) {
         PlayerUtil.damageSelfShort();
      }
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 18;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onMotion(EventMotion event) {
      if (mc.thePlayer.onGround && mc.thePlayer.hurtTime != 0) {
         mc.thePlayer.motionY = this.damageHeight.getValue();
         this.groundTicks = 1;
      } else {
         this.groundTicks = 0;
      }
   }

}
