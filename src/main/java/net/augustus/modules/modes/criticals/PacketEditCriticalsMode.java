package net.augustus.modules.modes.criticals;

import net.augustus.events.EventPacket;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.RandomNumberGenerator;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Criticals;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketEditCriticalsMode extends PacketCriticalsModeBase implements PacketListener {
   private final BooleanValue setOnGroundToFalse;
   private final RangeSetting yadd;

   public PacketEditCriticalsMode(Criticals criticals, DoubleValue doubleValue) {
       super(criticals, doubleValue, "PacketEdit");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "SetOnGroundToFalse", false, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.setOnGroundToFalse = (BooleanValue)booleanValue.withVisibility(condition);
      RangeSetting rangeSetting = new RangeSetting(this.parentModule, "Yadd", 1.0E-6, 2.0E-5, 0.0, 1.0, 12, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.yadd = rangeSetting.visibleWhen(condition2);
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
      return (obj, obj2) -> new PacketEditCriticalsMode((Criticals)obj, (DoubleValue)obj2);
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C03PacketPlayer && this.shouldCrit()) {
         C03PacketPlayer packet2 = (C03PacketPlayer)packet;
         if (this.setOnGroundToFalse.getValue()) {
            packet2.setOnGround(false);
         }

         double positionY = packet2.getPositionY();
         RandomNumberGenerator instance = DefaultRandomGenerator.instance;
         double minValue = this.yadd.getMinValue();
         double maxValue = this.yadd.getMaxValue();
         double d = maxValue;
         double d2 = minValue;
         packet2.setPositionY(positionY + instance.nextDouble(d2, d));
      }
   }

}
