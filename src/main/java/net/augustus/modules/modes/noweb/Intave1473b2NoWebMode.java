package net.augustus.modules.modes.noweb;

import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class Intave1473b2NoWebMode extends AbstractNoWebMode implements UpdateListener {
   private final BooleanValue intave1473b2StopOnHit;

   public Intave1473b2NoWebMode(NoWeb noWeb, DoubleValue doubleValue) {
       super(noWeb, doubleValue, "Intave 14.7.3-b2");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "Intave1473b2StopOnHit", false, this.modeSetting);
      String str = "StopOnHit";
      booleanValue = (BooleanValue)booleanValue.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.intave1473b2StopOnHit = (BooleanValue)booleanValue.withVisibility(condition);
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onUpdate2() {
      if (mc.thePlayer.isInWeb() && MoveUtil.isMoving() && mc.thePlayer.ticksExisted % 4 != 0 && this.wasInWeb) {
         if (mc.thePlayer.hurtTime > 0) {
            if (!this.intave1473b2StopOnHit.getValue()) {
               MoveUtil.setSpeed(0.2F, false);
               mc.thePlayer.setSpeedInAir(0.02F);
            }
         } else {
            MoveUtil.setSpeed(0.3F, false);
            mc.thePlayer.setSpeedInAir(0.08F);
         }
      }
   }

}
