package net.augustus.modules.modes.velocity;

import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.MoveUtil;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;

public class Intave13VelocityMode extends VelocityMode implements UpdateListener {

   public Intave13VelocityMode(Velocity velocity, DoubleValue doubleValue) {
      super(velocity, doubleValue, "Intave13");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new Intave13VelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onUpdate2() {
      if (this.canApplyVelocity()) {
         if (mc.thePlayer.hurtTime != 0 && mc.thePlayer.fallDistance < 3.0F && mc.thePlayer.isCollidedHorizontally) {
            MoveUtil.setSpeed2(0.195F);
         }
      }
   }

}
