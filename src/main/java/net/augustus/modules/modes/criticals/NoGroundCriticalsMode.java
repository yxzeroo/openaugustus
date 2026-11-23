package net.augustus.modules.modes.criticals;

import net.augustus.events.EventPacket;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Criticals;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoGroundCriticalsMode extends PacketCriticalsModeBase implements PacketListener {
   private final StringValue floating;

   public NoGroundCriticalsMode(Criticals criticals, DoubleValue doubleValue) {
       super(criticals, doubleValue, "NoGround");
      StringValue stringValue = new StringValue(this.parentModule, "Floating", 0.0, 0.0, 1.0, 7, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.floating = stringValue.visibleWhen(condition);
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 26;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new NoGroundCriticalsMode((Criticals)obj, (DoubleValue)obj2);
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C03PacketPlayer && this.shouldCrit()) {
         C03PacketPlayer packet2 = (C03PacketPlayer)packet;
         packet2.setOnGround(false);
         if (this.floating.getValue() != 0.0) {
            packet2.setPositionY(packet2.getPositionY() + this.floating.getValue());
         }

         this.clearPendingAttack();
      }
   }

}
