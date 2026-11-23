package net.augustus.modules.modes.velocity;

import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class CustomMotionVelocityMode extends VelocityMode implements PacketListener, MoveEntityListener {
   public final StringValue xzMotionInAir;
   public final StringValue xzMotionOnGround;
   private boolean pendingVelocity;

   public CustomMotionVelocityMode(Velocity velocity, DoubleValue doubleValue) {
       super(velocity, doubleValue, "CustomMotion");
      StringValue stringValue = new StringValue(this.parentModule, "XZMotionInAir", 0.2, 0.0, 1.0, 4, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.xzMotionInAir = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this.parentModule, "XZMotionOnGround", 0.2, 0.0, 1.0, 4, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.xzMotionOnGround = stringValue2.visibleWhen(condition2);
      this.pendingVelocity = true;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 26;
      this.toggleEventSubscription(b);
      byte b2 = 20;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new CustomMotionVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S12PacketEntityVelocity && this.canApplyVelocity()) {
         S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
         if (packet2.getEntityID() == mc.thePlayer.getEntityId()) {
            this.pendingVelocity = true;
         }
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      if (this.pendingVelocity) {
         this.pendingVelocity = false;
         double direction = MoveUtil.getDirection();
         double d = mc.thePlayer.onGround ? this.xzMotionOnGround.getValue() : this.xzMotionInAir.getValue();
         double d2 = -(Math.sin(direction) * d);
         event.setX(d2);
         double d3 = Math.cos(direction) * d;
         event.setZ(d3);
      }
   }

}
