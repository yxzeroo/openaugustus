package net.augustus.modules.modes.velocity;

import net.augustus.events.EventSafeWalk;
import net.augustus.events.listeners.SafeWalkListener;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;

public class SafeWalkVelocityMode extends VelocityMode implements SafeWalkListener {

   public SafeWalkVelocityMode(Velocity velocity, DoubleValue doubleValue) {
      super(velocity, doubleValue, "SafeWalk");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 33;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new SafeWalkVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onSafeWalk(EventSafeWalk event) {
      if (this.canApplyVelocity()) {
         Block block = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.2, mc.thePlayer.posZ).getBlock();
         if (block instanceof BlockAir && mc.thePlayer.hurtTime > 0) {
            boolean flag = true;
            event.setSafeWalk(flag);
         }
      }
   }

}
