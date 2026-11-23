package net.augustus.modules.modes;

import net.augustus.settings.RangeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;

public class MotionModifierRuleSettings {
   public StringValue yAddFinish;
   public RangeSetting staticXZ;
   public BooleanValue editAddY;
   public BooleanValue finishPacket;
   public BooleanValue onlyOnGround;
   public RangeSetting multiplyY;
   public RangeSetting timerSpeed;
   public RangeSetting multiplyXZ;
   public StringValue yAddInLoopSecond;
   public RangeSetting staticY;
   public RangeSetting speedInAir;
   public StringValue motion;
   public BooleanValue editMultiplyXZ;
   public RangeSetting addXZ;
   public BooleanValue blockMovementInput;
   public BooleanValue editTimerSpeed;
   public BooleanValue onlyAtLadder;
   public BooleanValue spoofGround;
   public BooleanValue resetAtOppositeState;
   public BooleanValue onlyInWeb;
   public BooleanValue editStaticY;
   public BooleanValue editSpeedInAir;
   public RangeSetting addY;
   public StringValue setPositionYAdd;
   public BooleansSetting selfDamageMode;
   public BooleanValue onlyInWater;
   public BooleanValue editMultiplyY;
   public RangeSetting resetTicksAt;
   public BooleanValue notWhileKB;
   public BooleanValue editTick;
   public BooleanValue editAddXZ;
   public BooleanValue jump;
   public BooleanValue secondPacket;
   public StringValue yAddInLoopFirst;
   public BooleanValue editStaticXZ;
   public StringValue loops;
   public StringValue strafePercentage;

   private boolean isYAddInLoopFirstVisible() {
      return this.selfDamageMode.getSelectedValue().equals("Custom");
   }

   private boolean isEditAddXZVisible() {
      return this.editTick.getValue();
   }

   private boolean isAddXZRangeVisible() {
      return this.editAddXZ.getValue() && this.editTick.getValue();
   }

   private boolean isBlockMovementInputVisible() {
      return this.selfDamageMode.getSelectedValue().equals("3Jump");
   }

   private boolean isStrafePercentageVisible() {
      return this.editTick.getValue();
   }

   private boolean isYAddFinishVisible() {
      return this.selfDamageMode.getSelectedValue().equals("Custom") && this.finishPacket.getValue();
   }

   private boolean isEditSpeedInAirVisible() {
      return this.editTick.getValue();
   }

   private boolean isTickSelfDamageModeVisible() {
      return this.editTick.getValue();
   }

   private boolean isTickYAddFinishVisible() {
      return this.editTick.getValue() && this.selfDamageMode.getSelectedValue().equals("Custom") && this.finishPacket.getValue();
   }

   private boolean isEditTimerSpeedVisible() {
      return this.editTick.getValue();
   }

   private boolean isNotWhileKBVisible() {
      return this.editTick.getValue();
   }

   private boolean isSetPositionYAddVisible() {
      return this.selfDamageMode.getSelectedValue().equals("SetPosition");
   }

   private boolean isSecondPacketVisible() {
      return this.selfDamageMode.getSelectedValue().equals("Custom");
   }

   private boolean isAddYRangeVisible() {
      return this.editAddY.getValue() && this.editTick.getValue();
   }

   private boolean isOnlyAtLadderVisible() {
      return this.editTick.getValue();
   }

   public MotionModifierRuleSettings(Module module, String str, DoubleValue doubleValue, double d) {
       super();
      this.editSpeedInAir = null;
      BooleansSetting booleansSetting = new BooleansSetting(
         module, str + "SelfDamageMode", "None", new String[]{"None", "Motion", "Packet1", "Packet2", "Custom", "Fake", "SetPosition", "3Jump"}, doubleValue
      );
      String str2 = "SelfDamageMode";
      this.selfDamageMode = (BooleansSetting)booleansSetting.withDisplayName(str2);
      BooleanValue booleanValue = new BooleanValue(module, str + "OnlyOnGround", false, doubleValue);
      String str3 = "OnlyOnGround";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str3);
      SettingVisibilityCondition condition = () -> this.isOnlyOnGroundVisible();
      this.onlyOnGround = (BooleanValue)booleanValue2.withVisibility(condition);
      BooleanValue booleanValue3 = new BooleanValue(module, str + "BlockMovementInput", true, doubleValue);
      String str4 = "BlockMovementInput";
      BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str4);
      SettingVisibilityCondition condition2 = () -> this.isBlockMovementInputVisible();
      this.blockMovementInput = (BooleanValue)booleanValue4.withVisibility(condition2);
      StringValue stringValue = new StringValue(module, str + "SetPositionYAdd", 3.1, -4.0, 4.0, 4, doubleValue);
      String str5 = "YAdd";
      StringValue stringValue2 = stringValue.named(str5);
      SettingVisibilityCondition condition3 = () -> this.isSetPositionYAddVisible();
      this.setPositionYAdd = stringValue2.visibleWhen(condition3);
      StringValue stringValue3 = new StringValue(module, str + "Motion", 5.0, 0.0, 10.0, 2, doubleValue);
      String str6 = "Motion";
      StringValue stringValue4 = stringValue3.named(str6);
      SettingVisibilityCondition condition4 = () -> {
         return this.selfDamageMode.getSelectedValue().equals("Motion");
      };
      this.motion = stringValue4.visibleWhen(condition4);
      StringValue stringValue5 = new StringValue(module, str + "Loops", 51.0, 1.0, 100.0, 0, doubleValue);
      String str7 = "Loops";
      StringValue stringValue6 = stringValue5.named(str7);
      SettingVisibilityCondition condition5 = () -> this.isSelfDamageLoopsVisible();
      this.loops = stringValue6.visibleWhen(condition5);
      StringValue stringValue7 = new StringValue(module, str + "YAddInLoopFirst", 0.02, 0.0, 1.0, 4, doubleValue);
      String str8 = "YAddInLoopFirst";
      StringValue stringValue8 = stringValue7.named(str8);
      SettingVisibilityCondition condition6 = () -> this.isYAddInLoopFirstVisible();
      this.yAddInLoopFirst = stringValue8.visibleWhen(condition6);
      BooleanValue booleanValue5 = new BooleanValue(module, str + "SecondPacket", true, doubleValue);
      String str9 = "SecondPacket";
      BooleanValue booleanValue6 = (BooleanValue)booleanValue5.withDisplayName(str9);
      SettingVisibilityCondition condition7 = () -> this.isSecondPacketVisible();
      this.secondPacket = (BooleanValue)booleanValue6.withVisibility(condition7);
      StringValue stringValue9 = new StringValue(module, str + "YAddInLoopSecond", 0.02, 0.0, 1.0, 4, doubleValue);
      String str10 = "YAddInLoopSecond";
      StringValue stringValue10 = stringValue9.named(str10);
      SettingVisibilityCondition condition8 = () -> this.isYAddInLoopSecondVisible();
      this.yAddInLoopSecond = stringValue10.visibleWhen(condition8);
      BooleanValue booleanValue7 = new BooleanValue(module, str + "FinishPacket", true, doubleValue);
      String str11 = "FinishPacket";
      BooleanValue booleanValue8 = (BooleanValue)booleanValue7.withDisplayName(str11);
      SettingVisibilityCondition condition9 = () -> this.isFinishPacketVisible();
      this.finishPacket = (BooleanValue)booleanValue8.withVisibility(condition9);
      StringValue stringValue11 = new StringValue(module, str + "YAddFinish", 0.0, 0.0, 1.0, 4, doubleValue);
      String str12 = "YAddFinish";
      StringValue stringValue12 = stringValue11.named(str12);
      SettingVisibilityCondition condition10 = () -> this.isYAddFinishVisible();
      this.yAddFinish = stringValue12.visibleWhen(condition10);
   }

   private boolean isSpeedInAirRangeVisible() {
      return this.editTick.getValue() && this.editSpeedInAir.getValue();
   }

   private boolean isFinishPacketVisible() {
      return this.selfDamageMode.getSelectedValue().equals("Custom");
   }

   private boolean isEditStaticYVisible() {
      return this.editTick.getValue();
   }

   private boolean isEditAddYVisible() {
      return this.editTick.getValue();
   }

   private boolean isTickSelfDamageMotionVisible() {
      return this.editTick.getValue() && this.selfDamageMode.getSelectedValue().equals("Motion");
   }

   private boolean isEditMultiplyXZVisible() {
      return this.editTick.getValue();
   }

   private boolean isStaticXZRangeVisible() {
      return this.editStaticXZ.getValue() && this.editTick.getValue();
   }

   public MotionModifierRuleSettings(Module module, String str, DoubleValue doubleValue) {
       super();
      this.editSpeedInAir = null;
      RangeSetting rangeSetting = new RangeSetting(module, str + "ResetTicksAt", 0.0, 59.0, 0.0, 59.0, 0, doubleValue);
      String str2 = "ResetTicksTo";
      String str3 = "ResetTicksFrom";
      this.resetTicksAt = rangeSetting.withBoundNames(str3, str2);
      BooleanValue booleanValue = new BooleanValue(module, str + "ResetAtOppositeState", true, doubleValue);
      String str4 = "ResetAtOppositeState";
      this.resetAtOppositeState = (BooleanValue)booleanValue.withDisplayName(str4);
   }

   private boolean isEditStaticXZVisible() {
      return this.editTick.getValue();
   }

   public MotionModifierRuleSettings(Module module, String str, DoubleValue doubleValue, boolean flag) {
       super();
      this.editSpeedInAir = null;
      BooleanValue booleanValue = new BooleanValue(module, str + "EditTick", false, doubleValue);
      String str2 = "EditTick";
      this.editTick = (BooleanValue)booleanValue.withDisplayName(str2);
      BooleanValue booleanValue2 = new BooleanValue(module, str + "NotWhileKB", false, doubleValue);
      String str3 = "NotWhileKB";
      BooleanValue booleanValue3 = (BooleanValue)booleanValue2.withDisplayName(str3);
      SettingVisibilityCondition condition = () -> this.isNotWhileKBVisible();
      this.notWhileKB = (BooleanValue)booleanValue3.withVisibility(condition);
      BooleanValue booleanValue4 = new BooleanValue(module, str + "OnlyInWater", false, doubleValue);
      String str4 = "OnlyInWater";
      BooleanValue booleanValue5 = (BooleanValue)booleanValue4.withDisplayName(str4);
      SettingVisibilityCondition condition2 = () -> {
         return this.editTick.getValue();
      };
      this.onlyInWater = (BooleanValue)booleanValue5.withVisibility(condition2);
      BooleanValue booleanValue6 = new BooleanValue(module, str + "OnlyInWeb", false, doubleValue);
      String str5 = "OnlyInWeb";
      BooleanValue booleanValue7 = (BooleanValue)booleanValue6.withDisplayName(str5);
      SettingVisibilityCondition condition3 = () -> {
         return this.editTick.getValue();
      };
      this.onlyInWeb = (BooleanValue)booleanValue7.withVisibility(condition3);
      BooleanValue booleanValue8 = new BooleanValue(module, str + "OnlyAtLadder", false, doubleValue);
      String str6 = "OnlyAtLadder";
      BooleanValue booleanValue9 = (BooleanValue)booleanValue8.withDisplayName(str6);
      SettingVisibilityCondition condition4 = () -> this.isOnlyAtLadderVisible();
      this.onlyAtLadder = (BooleanValue)booleanValue9.withVisibility(condition4);
      BooleanValue booleanValue10 = new BooleanValue(module, str + "Jump", false, doubleValue);
      String str7 = "Jump";
      BooleanValue booleanValue11 = (BooleanValue)booleanValue10.withDisplayName(str7);
      SettingVisibilityCondition condition5 = () -> {
         return this.editTick.getValue();
      };
      this.jump = (BooleanValue)booleanValue11.withVisibility(condition5);
      BooleanValue booleanValue12 = new BooleanValue(module, str + "SpoofGround", false, doubleValue);
      String str8 = "SpoofGround";
      booleanValue12 = (BooleanValue)booleanValue12.withDisplayName(str8);
      SettingVisibilityCondition condition6 = () -> this.isSpoofGroundVisible();
      this.spoofGround = (BooleanValue)booleanValue12.withVisibility(condition6);
      booleanValue12 = new BooleanValue(module, str + "EditStaticY", false, doubleValue);
      String str9 = "EditStaticY";
      booleanValue12 = (BooleanValue)booleanValue12.withDisplayName(str9);
      SettingVisibilityCondition condition7 = () -> this.isEditStaticYVisible();
      this.editStaticY = (BooleanValue)booleanValue12.withVisibility(condition7);
      RangeSetting rangeSetting = new RangeSetting(module, str + "StaticY", 0.42, 0.42, 0.0, 1.0, 4, doubleValue);
      String str10 = "MaxStaticY";
      String str11 = "MinStaticY";
      RangeSetting rangeSetting2 = rangeSetting.withBoundNames(str11, str10);
      SettingVisibilityCondition condition8 = () -> this.isStaticYRangeVisible();
      this.staticY = rangeSetting2.visibleWhen(condition8);
      BooleanValue booleanValue13 = new BooleanValue(module, str + "EditStaticXZ", false, doubleValue);
      String str12 = "EditStaticXZ";
      BooleanValue booleanValue14 = (BooleanValue)booleanValue13.withDisplayName(str12);
      SettingVisibilityCondition condition9 = () -> this.isEditStaticXZVisible();
      this.editStaticXZ = (BooleanValue)booleanValue14.withVisibility(condition9);
      RangeSetting rangeSetting3 = new RangeSetting(module, str + "StaticXZ", 0.2, 0.2, 0.0, 1.0, 4, doubleValue);
      String str13 = "MaxStaticXZ";
      String str14 = "MinStaticXZ";
      RangeSetting rangeSetting4 = rangeSetting3.withBoundNames(str14, str13);
      SettingVisibilityCondition condition10 = () -> this.isStaticXZRangeVisible();
      this.staticXZ = rangeSetting4.visibleWhen(condition10);
      BooleanValue booleanValue15 = new BooleanValue(module, str + "EditAddY", false, doubleValue);
      String str15 = "EditAddY";
      BooleanValue booleanValue16 = (BooleanValue)booleanValue15.withDisplayName(str15);
      SettingVisibilityCondition condition11 = () -> this.isEditAddYVisible();
      this.editAddY = (BooleanValue)booleanValue16.withVisibility(condition11);
      RangeSetting rangeSetting5 = new RangeSetting(module, str + "AddY", 0.02, 0.02, -1.0, 1.0, 4, doubleValue);
      String str16 = "MaxAddY";
      String str17 = "MinAddY";
      RangeSetting rangeSetting6 = rangeSetting5.withBoundNames(str17, str16);
      SettingVisibilityCondition condition12 = () -> this.isAddYRangeVisible();
      this.addY = rangeSetting6.visibleWhen(condition12);
      BooleanValue booleanValue17 = new BooleanValue(module, str + "EditAddXZ", false, doubleValue);
      String str18 = "EditAddXZ";
      BooleanValue booleanValue18 = (BooleanValue)booleanValue17.withDisplayName(str18);
      SettingVisibilityCondition condition13 = () -> this.isEditAddXZVisible();
      this.editAddXZ = (BooleanValue)booleanValue18.withVisibility(condition13);
      RangeSetting rangeSetting7 = new RangeSetting(module, str + "AddXZ", 0.02, 0.02, -1.0, 1.0, 4, doubleValue);
      String str19 = "MaxAddXZ";
      String str20 = "MinAddXZ";
      RangeSetting rangeSetting8 = rangeSetting7.withBoundNames(str20, str19);
      SettingVisibilityCondition condition14 = () -> this.isAddXZRangeVisible();
      this.addXZ = rangeSetting8.visibleWhen(condition14);
      BooleanValue booleanValue19 = new BooleanValue(module, str + "EditMultiplyY", false, doubleValue);
      String str21 = "EditMultiplyY";
      BooleanValue booleanValue20 = (BooleanValue)booleanValue19.withDisplayName(str21);
      SettingVisibilityCondition condition15 = () -> {
         return this.editTick.getValue();
      };
      this.editMultiplyY = (BooleanValue)booleanValue20.withVisibility(condition15);
      RangeSetting rangeSetting9 = new RangeSetting(module, str + "MultiplyY", 1.1, 1.1, 0.0, 2.0, 4, doubleValue);
      String str22 = "MaxMultiplyY";
      String str23 = "MinMultiplyY";
      RangeSetting rangeSetting10 = rangeSetting9.withBoundNames(str23, str22);
      SettingVisibilityCondition condition16 = () -> this.isMultiplyYRangeVisible();
      this.multiplyY = rangeSetting10.visibleWhen(condition16);
      BooleanValue booleanValue21 = new BooleanValue(module, str + "EditMultiplyXZ", false, doubleValue);
      String str24 = "EditMultiplyXZ";
      BooleanValue booleanValue22 = (BooleanValue)booleanValue21.withDisplayName(str24);
      SettingVisibilityCondition condition17 = () -> this.isEditMultiplyXZVisible();
      this.editMultiplyXZ = (BooleanValue)booleanValue22.withVisibility(condition17);
      RangeSetting rangeSetting11 = new RangeSetting(module, str + "MultiplyXZ", 1.1, 1.1, 0.0, 2.0, 4, doubleValue);
      String str25 = "MaxMultiplyXZ";
      String str26 = "MinMultiplyXZ";
      RangeSetting rangeSetting12 = rangeSetting11.withBoundNames(str26, str25);
      SettingVisibilityCondition condition18 = () -> this.isMultiplyXZRangeVisible();
      this.multiplyXZ = rangeSetting12.visibleWhen(condition18);
      if (flag) {
         BooleanValue booleanValue23 = new BooleanValue(module, str + "EditSpeedInAir", false, doubleValue);
         String str27 = "EditSpeedInAir";
         BooleanValue booleanValue24 = (BooleanValue)booleanValue23.withDisplayName(str27);
         SettingVisibilityCondition condition19 = () -> this.isEditSpeedInAirVisible();
         this.editSpeedInAir = (BooleanValue)booleanValue24.withVisibility(condition19);
         RangeSetting rangeSetting13 = new RangeSetting(module, str + "SpeedInAir", 0.02, 0.02, 0.0, 1.0, 6, doubleValue);
         String str28 = "MaxSpeedInAir";
         String str29 = "MinSpeedInAir";
         RangeSetting rangeSetting14 = rangeSetting13.withBoundNames(str29, str28);
         SettingVisibilityCondition condition20 = () -> this.isSpeedInAirRangeVisible();
         this.speedInAir = rangeSetting14.visibleWhen(condition20);
      }

      BooleanValue booleanValue25 = new BooleanValue(module, str + "EditTimerSpeed", false, doubleValue);
      String str30 = "EditTimerSpeed";
      BooleanValue booleanValue26 = (BooleanValue)booleanValue25.withDisplayName(str30);
      SettingVisibilityCondition condition21 = () -> this.isEditTimerSpeedVisible();
      this.editTimerSpeed = (BooleanValue)booleanValue26.withVisibility(condition21);
      RangeSetting rangeSetting15 = new RangeSetting(module, str + "TimerSpeed", 1.0, 1.0, 0.1, 4.0, 4, doubleValue);
      String str31 = "MaxTimerSpeed";
      String str32 = "MinTimerSpeed";
      RangeSetting rangeSetting16 = rangeSetting15.withBoundNames(str32, str31);
      SettingVisibilityCondition condition22 = () -> {
         return this.editTick.getValue() && this.editTimerSpeed.getValue();
      };
      this.timerSpeed = rangeSetting16.visibleWhen(condition22);
      StringValue stringValue = new StringValue(module, str + "StrafePercentage", 0.0, 0.0, 100.0, 0, doubleValue);
      String str33 = "StrafePercentage";
      StringValue stringValue2 = stringValue.named(str33);
      SettingVisibilityCondition condition23 = () -> this.isStrafePercentageVisible();
      this.strafePercentage = stringValue2.visibleWhen(condition23);
      BooleansSetting booleansSetting = new BooleansSetting(
         module, str + "SelfDamageMode", "None", new String[]{"None", "Motion", "Packet1", "Packet2", "Custom", "Fake", "SetPosition"}, doubleValue
      );
      String str34 = "SelfDamageMode";
      BooleansSetting booleansSetting2 = (BooleansSetting)booleansSetting.withDisplayName(str34);
      SettingVisibilityCondition condition24 = () -> this.isTickSelfDamageModeVisible();
      this.selfDamageMode = (BooleansSetting)booleansSetting2.withVisibility(condition24);
      StringValue stringValue3 = new StringValue(module, str + "SetPositionYAdd", 3.1, -4.0, 4.0, 4, doubleValue);
      String str35 = "YAdd";
      StringValue stringValue4 = stringValue3.named(str35);
      SettingVisibilityCondition condition25 = () -> this.isTickSetPositionYAddVisible();
      this.setPositionYAdd = stringValue4.visibleWhen(condition25);
      StringValue stringValue5 = new StringValue(module, str + "Motion", 5.0, 0.0, 10.0, 2, doubleValue);
      String str36 = "Motion";
      StringValue stringValue6 = stringValue5.named(str36);
      SettingVisibilityCondition condition26 = () -> this.isTickSelfDamageMotionVisible();
      this.motion = stringValue6.visibleWhen(condition26);
      StringValue stringValue7 = new StringValue(module, str + "Loops", 51.0, 1.0, 100.0, 0, doubleValue);
      String str37 = "Loops";
      StringValue stringValue8 = stringValue7.named(str37);
      SettingVisibilityCondition condition27 = () -> {
         return this.editTick.getValue() && this.selfDamageMode.getSelectedValue().equals("Custom");
      };
      this.loops = stringValue8.visibleWhen(condition27);
      StringValue stringValue9 = new StringValue(module, str + "YAddInLoopFirst", 0.02, 0.0, 1.0, 4, doubleValue);
      String str38 = "YAddInLoopFirst";
      StringValue stringValue10 = stringValue9.named(str38);
      SettingVisibilityCondition condition28 = () -> this.isTickYAddInLoopFirstVisible();
      this.yAddInLoopFirst = stringValue10.visibleWhen(condition28);
      BooleanValue booleanValue27 = new BooleanValue(module, str + "SecondPacket", true, doubleValue);
      String str39 = "SecondPacket";
      BooleanValue booleanValue28 = (BooleanValue)booleanValue27.withDisplayName(str39);
      SettingVisibilityCondition condition29 = () -> this.isTickSecondPacketVisible();
      this.secondPacket = (BooleanValue)booleanValue28.withVisibility(condition29);
      StringValue stringValue11 = new StringValue(module, str + "YAddInLoopSecond", 0.02, 0.0, 1.0, 4, doubleValue);
      String str40 = "YAddInLoopSecond";
      StringValue stringValue12 = stringValue11.named(str40);
      SettingVisibilityCondition condition30 = () -> this.isTickYAddInLoopSecondVisible();
      this.yAddInLoopSecond = stringValue12.visibleWhen(condition30);
      BooleanValue booleanValue29 = new BooleanValue(module, str + "FinishPacket", true, doubleValue);
      String str41 = "FinishPacket";
      BooleanValue booleanValue30 = (BooleanValue)booleanValue29.withDisplayName(str41);
      SettingVisibilityCondition condition31 = () -> {
         return this.editTick.getValue() && this.selfDamageMode.getSelectedValue().equals("Custom");
      };
      this.finishPacket = (BooleanValue)booleanValue30.withVisibility(condition31);
      StringValue stringValue13 = new StringValue(module, str + "YAddFinish", 0.0, 0.0, 1.0, 4, doubleValue);
      String str42 = "YAddFinish";
      StringValue stringValue14 = stringValue13.named(str42);
      SettingVisibilityCondition condition32 = () -> this.isTickYAddFinishVisible();
      this.yAddFinish = stringValue14.visibleWhen(condition32);
   }

   private boolean isSpoofGroundVisible() {
      return this.editTick.getValue();
   }

   private boolean isMultiplyXZRangeVisible() {
      return this.editMultiplyXZ.getValue() && this.editTick.getValue();
   }

   private boolean isSelfDamageLoopsVisible() {
      return this.selfDamageMode.getSelectedValue().equals("Custom");
   }

   private boolean isTickSecondPacketVisible() {
      return this.editTick.getValue() && this.selfDamageMode.getSelectedValue().equals("Custom");
   }

   private boolean isOnlyOnGroundVisible() {
      return !this.selfDamageMode.getSelectedValue().equals("3Jump") && !this.selfDamageMode.getSelectedValue().equals("None");
   }

   private boolean isTickYAddInLoopSecondVisible() {
      return this.editTick.getValue() && this.selfDamageMode.getSelectedValue().equals("Custom") && this.secondPacket.getValue();
   }

   private boolean isTickYAddInLoopFirstVisible() {
      return this.editTick.getValue() && this.selfDamageMode.getSelectedValue().equals("Custom");
   }

   private boolean isTickSetPositionYAddVisible() {
      return this.editTick.getValue() && this.selfDamageMode.getSelectedValue().equals("SetPosition");
   }

   private boolean isYAddInLoopSecondVisible() {
      return this.selfDamageMode.getSelectedValue().equals("Custom") && this.secondPacket.getValue();
   }

   private boolean isMultiplyYRangeVisible() {
      return this.editMultiplyY.getValue() && this.editTick.getValue();
   }

   private boolean isStaticYRangeVisible() {
      return this.editStaticY.getValue() && this.editTick.getValue();
   }

}
