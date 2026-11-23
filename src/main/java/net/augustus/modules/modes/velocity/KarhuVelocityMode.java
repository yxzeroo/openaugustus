package net.augustus.modules.modes.velocity;

import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.world.BlockUtil;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;
import net.minecraft.util.BlockPos;

public class KarhuVelocityMode extends VelocityMode implements UpdateListener {
   private int strafeCounter = 0;

   public KarhuVelocityMode(Velocity velocity, DoubleValue doubleValue) {
      super(velocity, doubleValue, "Karhu");
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
      return (obj, obj2) -> new KarhuVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onUpdate2() {
      if (this.canApplyVelocity()) {
         if (mc.thePlayer.hurtTime > 0
            && mc.thePlayer.motionY <= 0.0
            && (
               BlockUtil.hasCollisionBox(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))
                  || BlockUtil.hasCollisionBox(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ))
            )) {
            mc.thePlayer.motionY = -10.0;
         }

         if (mc.thePlayer.hurtTime == 5 && this.strafeCounter++ % 2 == 0) {
            MoveUtil.setSpeed2(0.15F);
         }
      }
   }

}
