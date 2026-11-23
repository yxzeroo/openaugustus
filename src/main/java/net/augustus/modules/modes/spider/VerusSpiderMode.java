package net.augustus.modules.modes.spider;

import net.augustus.events.EventMotion;
import net.augustus.events.listeners.MotionListener;
import java.util.function.BiFunction;
import net.augustus.modules.movement.Spider;
import net.augustus.settings.DoubleValue;

public class VerusSpiderMode extends AbstractSpiderMode implements MotionListener {

   public VerusSpiderMode(Spider spider, DoubleValue doubleValue) {
      super(spider, doubleValue, "Verus");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 18;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new VerusSpiderMode((Spider)obj, (DoubleValue)obj2);
   }

   @Override
   public void onMotion(EventMotion event) {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      if (this.canClimbWall()) {
         if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.ticksExisted % 4 != 0) {
            mc.thePlayer.motionY = 0.5;
         }
      }
   }

}
