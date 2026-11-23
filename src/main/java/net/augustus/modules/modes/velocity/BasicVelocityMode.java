package net.augustus.modules.modes.velocity;

import net.augustus.events.EventPacket;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class BasicVelocityMode extends VelocityMode implements PacketListener {
   private final DoubleValue velocityPacket;
   public final StringValue xzVelocityInAir;
   public final StringValue yVelocityInAir;
   public final BooleanValue cancelAir;
   public final StringValue xzVelocityOnGround;
   public final StringValue yVelocityOnGround;
   public final BooleanValue cancelGround;
   private final DoubleValue explosionPacket;
   public final BooleanValue explosion;
   public final StringValue xzVelocityInAirExplosion;
   public final StringValue yVelocityInAirExplosion;
   public final BooleanValue cancelAirExplosion;
   public final StringValue xzVelocityOnGroundExplosion;
   public final StringValue yVelocityOnGroundExplosion;
   public final BooleanValue cancelGroundExplosion;
   private final DoubleValue intervalOptions;
   private final BooleanValue intervals;
   private final StringValue intervalVelocity;
   private final StringValue intervalExplosion;
   private int velocityPacketCount;
   private int explosionPacketCount;

   public BasicVelocityMode(Velocity velocity, DoubleValue doubleValue) {
       super(velocity, doubleValue, "Basic");
      DoubleValue doubleValue2 = new DoubleValue(this.parentModule, "VelocityPacket", true, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.velocityPacket = doubleValue2.visibleWhen(condition);
      this.xzVelocityInAir = new StringValue(this.parentModule, "XZVelocityInAir", 0.0, -100.0, 100.0, 0, this.velocityPacket);
      this.yVelocityInAir = new StringValue(this.parentModule, "YVelocityInAir", 0.0, -100.0, 100.0, 0, this.velocityPacket);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "CancelAir", true, this.velocityPacket);
      SettingVisibilityCondition condition2 = () -> this.isCancelAirVisible();
      this.cancelAir = (BooleanValue)booleanValue.withVisibility(condition2);
      this.xzVelocityOnGround = new StringValue(this.parentModule, "XZVelocityOnGround", 0.0, -100.0, 100.0, 0, this.velocityPacket);
      this.yVelocityOnGround = new StringValue(this.parentModule, "YVelocityOnGround", 0.0, -100.0, 100.0, 0, this.velocityPacket);
      BooleanValue booleanValue2 = new BooleanValue(this.parentModule, "CancelGround", true, this.velocityPacket);
      SettingVisibilityCondition condition3 = () -> this.isCancelGroundVisible();
      this.cancelGround = (BooleanValue)booleanValue2.withVisibility(condition3);
      DoubleValue doubleValue3 = new DoubleValue(this.parentModule, "ExplosionPacket", true, this.modeSetting);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.explosionPacket = doubleValue3.visibleWhen(condition4);
      this.explosion = new BooleanValue(this.parentModule, "Explosion", true, this.explosionPacket);
      StringValue stringValue = new StringValue(this.parentModule, "XZVelocityInAirExplosion", 0.0, -100.0, 100.0, 0, this.explosionPacket);
      BooleanValue explosion2 = this.explosion;
      this.explosion.getClass();
      BooleanValue booleanValue3 = explosion2;
      SettingVisibilityCondition condition5 = () -> booleanValue3.getValue();
      this.xzVelocityInAirExplosion = stringValue.visibleWhen(condition5);
      StringValue stringValue2 = new StringValue(this.parentModule, "YVelocityInAirExplosion", 0.0, -100.0, 100.0, 0, this.explosionPacket);
      explosion2 = this.explosion;
      this.explosion.getClass();
      BooleanValue booleanValue4 = explosion2;
      SettingVisibilityCondition condition6 = () -> booleanValue4.getValue();
      this.yVelocityInAirExplosion = stringValue2.visibleWhen(condition6);
      BooleanValue booleanValue5 = new BooleanValue(this.parentModule, "CancelAirExplosion", true, this.explosionPacket);
      SettingVisibilityCondition condition7 = () -> this.isCancelAirExplosionVisible();
      this.cancelAirExplosion = (BooleanValue)booleanValue5.withVisibility(condition7);
      StringValue stringValue3 = new StringValue(this.parentModule, "XZVelocityOnGroundExplosion", 0.0, -100.0, 100.0, 0, this.explosionPacket);
      explosion2 = this.explosion;
      this.explosion.getClass();
      BooleanValue booleanValue6 = explosion2;
      SettingVisibilityCondition condition8 = () -> booleanValue6.getValue();
      this.xzVelocityOnGroundExplosion = stringValue3.visibleWhen(condition8);
      StringValue stringValue4 = new StringValue(this.parentModule, "YVelocityOnGroundExplosion", 0.0, -100.0, 100.0, 0, this.explosionPacket);
      explosion2 = this.explosion;
      this.explosion.getClass();
      BooleanValue booleanValue7 = explosion2;
      SettingVisibilityCondition condition9 = () -> booleanValue7.getValue();
      this.yVelocityOnGroundExplosion = stringValue4.visibleWhen(condition9);
      BooleanValue booleanValue8 = new BooleanValue(this.parentModule, "CancelGroundExplosion", true, this.explosionPacket);
      SettingVisibilityCondition condition10 = () -> this.isCancelGroundExplosionVisible();
      this.cancelGroundExplosion = (BooleanValue)booleanValue8.withVisibility(condition10);
      DoubleValue doubleValue4 = new DoubleValue(this.parentModule, "IntervalOptions", true, this.modeSetting);
      SettingVisibilityCondition condition11 = () -> this.isSelected();
      this.intervalOptions = doubleValue4.visibleWhen(condition11);
      this.intervals = new BooleanValue(this.parentModule, "Intervals", false, this.intervalOptions);
      StringValue stringValue5 = new StringValue(this.parentModule, "IntervalVelocity", 1.0, 1.0, 10.0, 0, this.intervalOptions);
      explosion2 = this.intervals;
      this.intervals.getClass();
      BooleanValue booleanValue9 = explosion2;
      SettingVisibilityCondition condition12 = () -> booleanValue9.getValue();
      this.intervalVelocity = stringValue5.visibleWhen(condition12);
      StringValue stringValue6 = new StringValue(this.parentModule, "IntervalExplosion", 1.0, 1.0, 10.0, 0, this.intervalOptions);
      SettingVisibilityCondition condition13 = () -> this.isExplosionIntervalVisible();
      this.intervalExplosion = stringValue6.visibleWhen(condition13);
      this.velocityPacketCount = 0;
      this.explosionPacketCount = 0;
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
      return (obj, obj2) -> new BasicVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onPacket(EventPacket event) {
      if (this.canApplyVelocity()) {
         Packet packet = event.getPacket();
         if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
            if (packet2.getEntityID() == mc.thePlayer.getEntityId()) {
               this.velocityPacketCount++;
               if ((double)this.velocityPacketCount % this.intervalVelocity.getValue() != 0.0 && this.intervals.getValue()) {
                  return;
               }

               if (mc.thePlayer.onGround) {
                  if (this.cancelGround.getValue()) {
                     if (this.xzVelocityOnGround.getValue() == 0.0) {
                        packet2.setApplyHorizontalVelocity(false);
                     }

                     if (this.yVelocityOnGround.getValue() == 0.0) {
                        packet2.setApplyVerticalVelocity(false);
                     }
                  }

                  packet2.setMotionX((int)((double)packet2.getMotionX() * this.xzVelocityOnGround.getValue() / 100.0));
                  packet2.setMotionY((int)((double)packet2.getMotionY() * this.yVelocityOnGround.getValue() / 100.0));
                  packet2.setMotionZ((int)((double)packet2.getMotionZ() * this.xzVelocityOnGround.getValue() / 100.0));
               } else {
                  if (this.cancelAir.getValue()) {
                     if (this.xzVelocityInAir.getValue() == 0.0) {
                        packet2.setApplyHorizontalVelocity(false);
                     }

                     if (this.yVelocityInAir.getValue() == 0.0) {
                        packet2.setApplyVerticalVelocity(false);
                     }
                  }

                  packet2.setMotionX((int)((double)packet2.getMotionX() * this.xzVelocityInAir.getValue() / 100.0));
                  packet2.setMotionY((int)((double)packet2.getMotionY() * this.yVelocityInAir.getValue() / 100.0));
                  packet2.setMotionZ((int)((double)packet2.getMotionZ() * this.xzVelocityInAir.getValue() / 100.0));
               }
            }
         } else if (packet instanceof S27PacketExplosion && this.explosion.getValue()) {
            this.explosionPacketCount++;
            if ((double)this.explosionPacketCount % this.intervalExplosion.getValue() != 0.0 && this.intervals.getValue()) {
               return;
            }

            S27PacketExplosion packet3 = (S27PacketExplosion)packet;
            if (mc.thePlayer.onGround) {
               packet3.setMotionX((float)((double)packet3.getMotionX() * this.xzVelocityOnGroundExplosion.getValue() / 100.0));
               packet3.setMotionY((float)((double)packet3.getMotionY() * this.yVelocityOnGroundExplosion.getValue() / 100.0));
               packet3.setMotionZ((float)((double)packet3.getMotionZ() * this.xzVelocityOnGroundExplosion.getValue() / 100.0));
               if (!this.cancelGroundExplosion.getValue()) {
                  if (this.xzVelocityOnGroundExplosion.getValue() == 0.0) {
                     packet3.setResetHorizontalMotion(true);
                  }

                  if (this.yVelocityOnGroundExplosion.getValue() == 0.0) {
                     packet3.setResetVerticalMotion(true);
                  }
               }
            } else {
               packet3.setMotionX((float)((double)packet3.getMotionX() * this.xzVelocityInAirExplosion.getValue() / 100.0));
               packet3.setMotionY((float)((double)packet3.getMotionY() * this.yVelocityInAirExplosion.getValue() / 100.0));
               packet3.setMotionZ((float)((double)packet3.getMotionZ() * this.xzVelocityInAirExplosion.getValue() / 100.0));
               if (!this.cancelAirExplosion.getValue()) {
                  if (this.xzVelocityInAirExplosion.getValue() == 0.0) {
                     packet3.setResetHorizontalMotion(true);
                  }

                  if (this.yVelocityInAirExplosion.getValue() == 0.0) {
                     packet3.setResetVerticalMotion(true);
                  }
               }
            }
         }
      }
   }

   private boolean isExplosionIntervalVisible() {
      return this.intervals.getValue() && this.explosion.getValue();
   }

   private boolean isCancelGroundExplosionVisible() {
      return (this.xzVelocityOnGroundExplosion.getValue() == 0.0 || this.yVelocityOnGroundExplosion.getValue() == 0.0) && this.explosion.getValue();
   }

   private boolean isCancelAirExplosionVisible() {
      return (this.xzVelocityInAirExplosion.getValue() == 0.0 || this.yVelocityInAirExplosion.getValue() == 0.0) && this.explosion.getValue();
   }

   private boolean isCancelGroundVisible() {
      return this.xzVelocityOnGround.getValue() == 0.0 || this.yVelocityOnGround.getValue() == 0.0;
   }

   private boolean isCancelAirVisible() {
      return this.xzVelocityInAir.getValue() == 0.0 || this.yVelocityInAir.getValue() == 0.0;
   }

}
