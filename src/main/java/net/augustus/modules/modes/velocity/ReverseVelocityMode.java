package net.augustus.modules.modes.velocity;

import net.augustus.events.EventMotion;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.MotionListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class ReverseVelocityMode extends VelocityMode implements UpdateListener, MotionListener, PacketListener {
   public final StringValue xzVelocityStrength;
   public final StringValue yVelocityStrength;
   public final StringValue reverseStart;
   public final BooleanValue reverseStrafe;
   public final RangeSetting reverseStrafeEnd;

   public ReverseVelocityMode(Velocity velocity, DoubleValue doubleValue) {
       super(velocity, doubleValue, "Reverse");
      StringValue stringValue = new StringValue(this.parentModule, "XZ-Velocity Strength", 100.0, 0.0, 100.0, 0);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.xzVelocityStrength = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this.parentModule, "Y-Velocity Strength", 100.0, 0.0, 100.0, 0);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.yVelocityStrength = stringValue2.visibleWhen(condition2);
      StringValue stringValue3 = new StringValue(this.parentModule, "ReverseStart", 9.0, 1.0, 10.0, 0, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.reverseStart = stringValue3.visibleWhen(condition3);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "ReverseStrafe", false, this.modeSetting);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.reverseStrafe = (BooleanValue)booleanValue.withVisibility(condition4);
      RangeSetting rangeSetting = (new RangeSetting(this.parentModule, "ReverseStrafeEnd", "ReverseStrafeStart", 9.0, 7.0, 1.0, 10.0, 0, this.modeSetting)).withMaxFirst();
      SettingVisibilityCondition condition5 = () -> this.isReverseStrafeRangeVisible();
      this.reverseStrafeEnd = rangeSetting.visibleWhen(condition5);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
      byte b2 = 18;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new ReverseVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onPacket(EventPacket event) {
      if (this.canApplyVelocity() && !event.isCancelled()) {
         Packet packet = event.getPacket();
         if (mc.thePlayer != null && packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
            if (packet2.getEntityID() == mc.thePlayer.getEntityId()) {
               double motionX = (double)packet2.getMotionX();
               boolean flag = true;
               packet2.setMotionX((int)(motionX * (this.xzVelocityStrength.getValue(flag) / 100.0)));
               double motionX2 = (double)packet2.getMotionX();
               boolean flag2 = true;
               packet2.setMotionY((int)(motionX2 * (this.yVelocityStrength.getValue(flag2) / 100.0)));
               double motionZ = (double)packet2.getMotionZ();
               boolean flag3 = true;
               packet2.setMotionZ((int)(motionZ * (this.xzVelocityStrength.getValue(flag3) / 100.0)));
            }
         }
      }
   }

   @Override
   public void onUpdate2() {
      if (this.canApplyVelocity()) {
         if ((double)mc.thePlayer.hurtTime == this.reverseStart.getValue()) {
            mc.thePlayer.motionX *= -1.0;
            mc.thePlayer.motionZ *= -1.0;
            if (this.shouldReverseStrafe()) {
               MoveUtil.setSpeed2((float)MoveUtil.getSpeed());
            }
         }
      }
   }

   @Override
   public void onMotion(EventMotion event) {
   }

   @Override
   public void onMotion() {
      if (this.canApplyVelocity()) {
         if (this.shouldReverseStrafe()) {
            MoveUtil.strafe();
         }
      }
   }

   private boolean shouldReverseStrafe() {
      if (this.reverseStrafe.getValue()) {
         double hurtTime = (double)mc.thePlayer.hurtTime;
         if (hurtTime <= this.reverseStrafeEnd.getMaxValue() && (double)mc.thePlayer.hurtTime >= this.reverseStrafeEnd.getMinValue() && mc.thePlayer.hurtTime > 0) {
            return true;
         }
      }

      return false;
   }

   private boolean isReverseStrafeRangeVisible() {
      return this.isSelected() && this.reverseStrafe.getValue();
   }

}
