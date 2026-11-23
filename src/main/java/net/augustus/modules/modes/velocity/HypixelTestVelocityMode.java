package net.augustus.modules.modes.velocity;

import net.augustus.events.EventPacket;
import net.augustus.events.listeners.PacketListener;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class HypixelTestVelocityMode extends VelocityMode implements PacketListener {

   public HypixelTestVelocityMode(Velocity velocity, DoubleValue doubleValue) {
      super(velocity, doubleValue, "HypixelTest");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 26;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new HypixelTestVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S12PacketEntityVelocity && this.canApplyVelocity()) {
         S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
         if (packet2.getEntityID() == mc.thePlayer.getEntityId()) {
            event.cancel();
            if (mc.thePlayer.onGround && (double)packet2.getMotionY() / 8000.0 < 0.2 && (double)packet2.getMotionY() / 8000.0 > 0.41995) {
               mc.thePlayer.motionY = (double)packet2.getMotionY() / 8000.0;
            }
         }
      }
   }

}
