package net.augustus.modules.modes.noweb;

import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class Intave1477NoWebMode extends AbstractNoWebMode implements UpdateListener {
   private final BooleanValue intave1477StopOnHit;

   public Intave1477NoWebMode(NoWeb noWeb, DoubleValue doubleValue) {
       super(noWeb, doubleValue, "Intave 14.7.7");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "Intave1477StopOnHit", false, this.modeSetting);
      String str = "StopOnHit";
      booleanValue = (BooleanValue)booleanValue.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.intave1477StopOnHit = (BooleanValue)booleanValue.withVisibility(condition);
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
      if (!mc.thePlayer.isInWeb()) {
         this.timeHelper.reset();
      }

      if (!mc.thePlayer.isInWeb() && this.wasInWeb) {
         mc.thePlayer.setSpeedInAir(0.02F);
      } else {
         if (MoveUtil.isMoving() && mc.thePlayer.ticksExisted % 4 != 0) {
            long l = 250L;
            if (this.timeHelper.reached2(l)) {
               if (mc.thePlayer.hurtTime > 0) {
                  if (!this.intave1477StopOnHit.getValue()) {
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
   }

}
