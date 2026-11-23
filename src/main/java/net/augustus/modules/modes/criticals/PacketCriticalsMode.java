package net.augustus.modules.modes.criticals;

import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.AttackEntityListener;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Criticals;
import net.augustus.settings.DoubleValue;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketCriticalsMode extends CriticalsMode implements AttackEntityListener {

   public PacketCriticalsMode(Criticals criticals, DoubleValue doubleValue) {
      super(criticals, doubleValue, "Packet");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 1;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new PacketCriticalsMode((Criticals)obj, (DoubleValue)obj2);
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      if (this.shouldCrit()) {
         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 6.0E-15, mc.thePlayer.posZ, false));
         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
      }
   }

}
