package net.augustus.modules.modes.velocity;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.PacketListener;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class SpoofVelocityMode extends VelocityMode implements PacketListener {

   public SpoofVelocityMode(Velocity velocity, DoubleValue doubleValue) {
      super(velocity, doubleValue, "Spoof");
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
      return (obj, obj2) -> new SpoofVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S12PacketEntityVelocity && this.canApplyVelocity()) {
         S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
         if (packet2.getEntityID() == mc.thePlayer.getEntityId()) {
            event.cancel();
            mc.getNetHandler()
               .addToSendQueue(
                  new C03PacketPlayer.C04PacketPlayerPosition(
                     mc.thePlayer.posX + (double)packet2.getMotionX() / 8000.0,
                     mc.thePlayer.posY + (double)packet2.getMotionY() / 8000.0,
                     mc.thePlayer.posZ + (double)packet2.getMotionZ() / 8000.0,
                     false
                  )
               );
         }
      }
   }

}
