package net.augustus.settings;

import net.augustus.ClientManagers;
import net.augustus.utils.interfaces.MC;

public class SettingVisibilityRegistry implements MC {
   private static int[] controlFlowState;

   public static void registerAll() {
      int[] controlFlowState2 = getControlFlowState();
      BooleanValue intave13 = ClientManagers.moduleManager.killAura.intave13;
      boolean flag = !ClientManagers.moduleManager.killAura.noRotation.getValue() && ClientManagers.moduleManager.killAura.intave13.getValue();
      intave13.setValue(flag);
      RangeSetting rangeSetting = ClientManagers.moduleManager.antiFireBall.yawSpeedInRotationModule;
      SettingVisibilityCondition condition = () -> {
         int[] controlFlowState3 = getControlFlowState();
         return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
      };
      rangeSetting.visibleWhen(condition);
      RangeSetting rangeSetting2 = ClientManagers.moduleManager.antiFireBall.pitchSpeedInRotationModule;
      SettingVisibilityCondition condition2 = () -> isAntiFireBallPitchSpeedVisible();
      rangeSetting2.visibleWhen(condition2);
      BooleansSetting booleansSetting = ClientManagers.moduleManager.antiFireBall.randomizeInRotationModule;
      SettingVisibilityCondition condition3 = () -> isAntiFireBallRandomizeVisible();
      booleansSetting.withVisibility(condition3);
      BooleansSetting booleansSetting2 = ClientManagers.moduleManager.antiFireBall.randomTypeInRotationModule;
      SettingVisibilityCondition condition4 = () -> {
         int[] controlFlowState4 = getControlFlowState();
         return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
      };
      booleansSetting2.withVisibility(condition4);
      StringValue stringValue = ClientManagers.moduleManager.antiFireBall.randomStrengthInRotationModule;
      SettingVisibilityCondition condition5 = () -> isAntiFireBallRandomStrengthVisible();
      stringValue.visibleWhen(condition5);
      intave13 = ClientManagers.moduleManager.antiFireBall.advancedRotsInRotationModule;
      SettingVisibilityCondition condition6 = () -> isAntiFireBallAdvancedRotsVisible();
      intave13.withVisibility(condition6);
      intave13 = ClientManagers.moduleManager.antiFireBall.interpolationInRotationModule;
      SettingVisibilityCondition condition7 = () -> isAntiFireBallInterpolationVisible();
      intave13.withVisibility(condition7);
      intave13 = ClientManagers.moduleManager.antiFireBall.slowDown;
      SettingVisibilityCondition condition8 = () -> isAntiFireBallSlowDownVisible();
      intave13.withVisibility(condition8);
      intave13 = ClientManagers.moduleManager.antiFireBall.moveFixInRotationModule;
      SettingVisibilityCondition condition9 = () -> isAntiFireBallMoveFixVisible();
      intave13.withVisibility(condition9);
      BooleansSetting booleansSetting3 = ClientManagers.moduleManager.antiFireBall.rotationModeInEntityRotationModule;
      SettingVisibilityCondition condition10 = () -> isAntiFireBallRotationModeVisible();
      booleansSetting3.withVisibility(condition10);
      StringValue stringValue2 = ClientManagers.moduleManager.antiFireBall.hitBoxPercentageVerticalInEntityRotationModule;
      SettingVisibilityCondition condition11 = () -> isAntiFireBallHitBoxVerticalVisible();
      stringValue2.visibleWhen(condition11);
      StringValue stringValue3 = ClientManagers.moduleManager.antiFireBall.hitBoxPercentageHorizontalInEntityRotationModule;
      SettingVisibilityCondition condition12 = () -> isAntiFireBallHitBoxHorizontalVisible();
      stringValue3.visibleWhen(condition12);
      StringValue stringValue4 = ClientManagers.moduleManager.antiFireBall.hitBoxPercentageVerticalInEntityRotationModule1;
      SettingVisibilityCondition condition13 = () -> isAntiFireBallAltHitBoxVerticalVisible();
      stringValue4.visibleWhen(condition13);
      StringValue stringValue5 = ClientManagers.moduleManager.antiFireBall.hitBoxPercentageHorizontalInEntityRotationModule1;
      SettingVisibilityCondition condition14 = () -> isAntiFireBallAltHitBoxHorizontalVisible();
      stringValue5.visibleWhen(condition14);
      RangeSetting rangeSetting3 = ClientManagers.moduleManager.autoWalk.yawSpeedInRotationModule;
      SettingVisibilityCondition condition15 = () -> {
         return ClientManagers.moduleManager.autoWalk.pathFinding.getValue();
      };
      rangeSetting3.visibleWhen(condition15);
      RangeSetting rangeSetting4 = ClientManagers.moduleManager.autoWalk.pitchSpeedInRotationModule;
      SettingVisibilityCondition condition16 = () -> isAutoWalkPitchSpeedVisible();
      rangeSetting4.visibleWhen(condition16);
      BooleansSetting booleansSetting4 = ClientManagers.moduleManager.autoWalk.randomizeInRotationModule;
      SettingVisibilityCondition condition17 = () -> {
         return ClientManagers.moduleManager.autoWalk.pathFinding.getValue();
      };
      booleansSetting4.withVisibility(condition17);
      BooleansSetting booleansSetting5 = ClientManagers.moduleManager.autoWalk.randomTypeInRotationModule;
      SettingVisibilityCondition condition18 = () -> isAutoWalkRandomTypeVisible();
      booleansSetting5.withVisibility(condition18);
      StringValue stringValue6 = ClientManagers.moduleManager.autoWalk.randomStrengthInRotationModule;
      SettingVisibilityCondition condition19 = () -> isAutoWalkRandomStrengthVisible();
      stringValue6.visibleWhen(condition19);
      intave13 = ClientManagers.moduleManager.autoWalk.advancedRotsInRotationModule;
      SettingVisibilityCondition condition20 = () -> isAutoWalkAdvancedRotsVisible();
      intave13.withVisibility(condition20);
      intave13 = ClientManagers.moduleManager.autoWalk.interpolationInRotationModule;
      SettingVisibilityCondition condition21 = () -> {
         return ClientManagers.moduleManager.autoWalk.pathFinding.getValue();
      };
      intave13.withVisibility(condition21);
      intave13 = ClientManagers.moduleManager.autoWalk.moveFixInRotationModule;
      SettingVisibilityCondition condition22 = () -> false;
      intave13.withVisibility(condition22);
      String str = "None";
      ClientManagers.moduleManager.autoWalk.silentMoveFix.setSelectedValue(str);
      StringValue timerTicksForward = ClientManagers.moduleManager.timerRange.timerTicksForward;
      double d = Math.max(1.0, ClientManagers.moduleManager.timerRange.timerTicksForward.getValue());
      timerTicksForward.setValue(d);
      intave13 = ClientManagers.moduleManager.aimBot.moveFixInRotationModule;
      SettingVisibilityCondition condition23 = () -> false;
      intave13.withVisibility(condition23);
   }

   private static boolean isAntiFireBallHitBoxHorizontalVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAntiFireBallMoveFixVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAntiFireBallPitchSpeedVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAntiFireBallRandomizeVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAntiFireBallAltHitBoxHorizontalVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAntiFireBallRandomStrengthVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAntiFireBallRotationModeVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAutoWalkRandomTypeVisible() {
      return ClientManagers.moduleManager.autoWalk.pathFinding.getValue();
   }

   private static boolean isAntiFireBallSlowDownVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAutoWalkAdvancedRotsVisible() {
      return ClientManagers.moduleManager.autoWalk.pathFinding.getValue();
   }

   private static boolean isAntiFireBallHitBoxVerticalVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAutoWalkPitchSpeedVisible() {
      return ClientManagers.moduleManager.autoWalk.pathFinding.getValue();
   }

   private static boolean isAntiFireBallAdvancedRotsVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAntiFireBallAltHitBoxVerticalVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   private static boolean isAutoWalkRandomStrengthVisible() {
      return ClientManagers.moduleManager.autoWalk.pathFinding.getValue();
   }

   private static boolean isAntiFireBallInterpolationVisible() {
      int[] controlFlowState2 = getControlFlowState();
      return !ClientManagers.moduleManager.antiFireBall.noRotate.getValue();
   }

   public static void setControlFlowState(int[] ints) {
      controlFlowState = ints;
   }

   public static int[] getControlFlowState() {
      return controlFlowState;
   }

   static {
      if (getControlFlowState() == null) {
         setControlFlowState(new int[4]);
      }
   }

}
