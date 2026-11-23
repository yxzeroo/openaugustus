package net.augustus.modules.modes.criticals;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.RandomNumberGenerator;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Criticals;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class CustomPacketCriticalsMode extends CriticalsMode implements AttackEntityListener {
   private final BooleanValue useC06;
   public final StringValue interval;
   private final BooleanValue sendFirst;
   private final BooleanValue onGroundFirst;
   private final RangeSetting yAddFirst;
   private final BooleanValue sendSecond;
   private final BooleanValue onGroundSecond;
   private final RangeSetting yAddSecond;
   private final BooleanValue sendThird;
   private final BooleanValue onGroundThird;
   private final RangeSetting yAddThird;
   private final BooleanValue sendFourth;
   private final BooleanValue onGroundFourth;
   private final RangeSetting yAddFourth;
   private int attackIntervalCounter;

   public CustomPacketCriticalsMode(Criticals criticals, DoubleValue doubleValue) {
       super(criticals, doubleValue, "CustomPacket");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "UseC06", false, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.useC06 = (BooleanValue)booleanValue.withVisibility(condition);
      StringValue stringValue = new StringValue(this.parentModule, "Interval", 3.0, 0.0, 10.0, 0, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.interval = stringValue.visibleWhen(condition2);
      BooleanValue booleanValue2 = new BooleanValue(this.parentModule, "SendFirst", true, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.sendFirst = (BooleanValue)booleanValue2.withVisibility(condition3);
      BooleanValue booleanValue3 = new BooleanValue(this.parentModule, "OnGroundFirst", false, this.modeSetting);
      SettingVisibilityCondition condition4 = () -> this.isFirstOnGroundVisible();
      this.onGroundFirst = (BooleanValue)booleanValue3.withVisibility(condition4);
      RangeSetting rangeSetting = new RangeSetting(this.parentModule, "YAddFirst", 0.001, 0.001, 0.0, 1.0, 12, this.modeSetting);
      SettingVisibilityCondition condition5 = () -> this.isFirstYAddVisible();
      this.yAddFirst = rangeSetting.visibleWhen(condition5);
      BooleanValue booleanValue4 = new BooleanValue(this.parentModule, "SendSecond", true, this.modeSetting);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.sendSecond = (BooleanValue)booleanValue4.withVisibility(condition6);
      BooleanValue booleanValue5 = new BooleanValue(this.parentModule, "OnGroundSecond", false, this.modeSetting);
      SettingVisibilityCondition condition7 = () -> {
         return this.isSelected() && this.sendSecond.getValue();
      };
      this.onGroundSecond = (BooleanValue)booleanValue5.withVisibility(condition7);
      RangeSetting rangeSetting2 = new RangeSetting(this.parentModule, "YAddSecond", 0.001, 0.001, 0.0, 1.0, 12, this.modeSetting);
      SettingVisibilityCondition condition8 = () -> {
         return this.isSelected() && this.sendSecond.getValue();
      };
      this.yAddSecond = rangeSetting2.visibleWhen(condition8);
      BooleanValue booleanValue6 = new BooleanValue(this.parentModule, "SendThird", true, this.modeSetting);
      SettingVisibilityCondition condition9 = () -> this.isSelected();
      this.sendThird = (BooleanValue)booleanValue6.withVisibility(condition9);
      BooleanValue booleanValue7 = new BooleanValue(this.parentModule, "OnGroundThird", false, this.modeSetting);
      SettingVisibilityCondition condition10 = () -> this.isThirdOnGroundVisible();
      this.onGroundThird = (BooleanValue)booleanValue7.withVisibility(condition10);
      RangeSetting rangeSetting3 = new RangeSetting(this.parentModule, "YAddThird", 0.001, 0.001, 0.0, 1.0, 12, this.modeSetting);
      SettingVisibilityCondition condition11 = () -> this.isThirdYAddVisible();
      this.yAddThird = rangeSetting3.visibleWhen(condition11);
      BooleanValue booleanValue8 = new BooleanValue(this.parentModule, "SendFourth", true, this.modeSetting);
      SettingVisibilityCondition condition12 = () -> this.isSelected();
      this.sendFourth = (BooleanValue)booleanValue8.withVisibility(condition12);
      BooleanValue booleanValue9 = new BooleanValue(this.parentModule, "OnGroundFourth", false, this.modeSetting);
      SettingVisibilityCondition condition13 = () -> {
         return this.isSelected() && this.sendFourth.getValue();
      };
      this.onGroundFourth = (BooleanValue)booleanValue9.withVisibility(condition13);
      RangeSetting rangeSetting4 = new RangeSetting(this.parentModule, "YAddFourth", 0.001, 0.001, 0.0, 1.0, 12, this.modeSetting);
      SettingVisibilityCondition condition14 = () -> this.isFourthYAddVisible();
      this.yAddFourth = rangeSetting4.visibleWhen(condition14);
      this.attackIntervalCounter = -1;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 1;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      this.attackIntervalCounter = -1;
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new CustomPacketCriticalsMode((Criticals)obj, (DoubleValue)obj2);
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      this.attackIntervalCounter++;
      if (this.shouldCrit() && !((double)this.attackIntervalCounter < this.interval.getValue())) {
         this.attackIntervalCounter = -1;
         if (this.sendFirst.getValue()) {
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue = this.yAddFirst.getMinValue();
            double maxValue = this.yAddFirst.getMaxValue();
            double d = maxValue;
            double d2 = minValue;
            minValue = instance.nextDouble(d2, d);
            boolean value = this.onGroundFirst.getValue();
            boolean flag = value;
            double d3 = minValue;
            this.sendOffsetPacket(d3, flag);
         }

         if (this.sendSecond.getValue()) {
            RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
            double minValue2 = this.yAddSecond.getMinValue();
            double maxValue2 = this.yAddSecond.getMaxValue();
            double d4 = maxValue2;
            double d5 = minValue2;
            minValue2 = instance2.nextDouble(d5, d4);
            boolean value2 = this.onGroundSecond.getValue();
            boolean flag2 = value2;
            double d6 = minValue2;
            this.sendOffsetPacket(d6, flag2);
         }

         if (this.sendThird.getValue()) {
            RandomNumberGenerator instance3 = DefaultRandomGenerator.instance;
            double minValue3 = this.yAddThird.getMinValue();
            double maxValue3 = this.yAddThird.getMaxValue();
            double d7 = maxValue3;
            double d8 = minValue3;
            minValue3 = instance3.nextDouble(d8, d7);
            boolean value3 = this.onGroundThird.getValue();
            boolean flag3 = value3;
            double d9 = minValue3;
            this.sendOffsetPacket(d9, flag3);
         }

         if (this.sendFourth.getValue()) {
            RandomNumberGenerator instance4 = DefaultRandomGenerator.instance;
            double minValue4 = this.yAddFourth.getMinValue();
            double maxValue4 = this.yAddFourth.getMaxValue();
            double d10 = maxValue4;
            double d11 = minValue4;
            minValue4 = instance4.nextDouble(d11, d10);
            boolean value4 = this.onGroundFourth.getValue();
            boolean flag4 = value4;
            double d12 = minValue4;
            this.sendOffsetPacket(d12, flag4);
         }
      }
   }

   private void sendOffsetPacket(double d, boolean flag) {
      if (this.useC06.getValue()) {
         mc.thePlayer
            .sendQueue
            .addToSendQueueUnchecked(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, flag));
      } else {
         mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, flag));
      }
   }

   private boolean isFourthYAddVisible() {
      return this.isSelected() && this.sendFourth.getValue();
   }

   private boolean isThirdYAddVisible() {
      return this.isSelected() && this.sendThird.getValue();
   }

   private boolean isThirdOnGroundVisible() {
      return this.isSelected() && this.sendThird.getValue();
   }

   private boolean isFirstYAddVisible() {
      return this.isSelected() && this.sendFirst.getValue();
   }

   private boolean isFirstOnGroundVisible() {
      return this.isSelected() && this.sendFirst.getValue();
   }

}
