package net.augustus.modules.modes.velocity;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.MoveUtil;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;

public class OldMinemenVelocityMode extends VelocityMode implements MoveInputListener {
   private int hurtCounter = 0;

   public OldMinemenVelocityMode(Velocity velocity, DoubleValue doubleValue) {
      super(velocity, doubleValue, "OldMinemen");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      this.hurtCounter = 0;
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new OldMinemenVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.canApplyVelocity() && mc.thePlayer != null) {
         if (mc.thePlayer.hurtTime == 9 && this.hurtCounter++ % 4 == 0) {
            MoveUtil.setSpeed2(0.2F);
         }

         if (mc.thePlayer.hurtTime == 5) {
            MoveUtil.setSpeed2(0.1F);
         }
      }
   }

}
