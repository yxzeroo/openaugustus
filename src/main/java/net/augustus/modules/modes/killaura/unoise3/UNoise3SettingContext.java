package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;

public class UNoise3SettingContext {
   private Module module;
   private String keyPrefix = "";
   private SettingVisibilityCondition visibilityCondition;
   private DoubleValue parentGroup;
   private UNoise3PointSelector uNoise3PointSelector;
   private static int[] controlFlowState;

   public UNoise3SettingContext(Module module2, DoubleValue doubleValue) {
      this(module2, doubleValue, null, null);
   }

   public UNoise3SettingContext(Module module2, DoubleValue doubleValue, SettingVisibilityCondition condition) {
      this(module2, doubleValue, null, condition);
   }

   public DoubleValue getParentGroup() {
      return this.parentGroup;
   }

   public String getKeyPrefix() {
      return this.keyPrefix;
   }

   public UNoise3SettingContext withKeyPrefix(String str) {
      this.keyPrefix = str;
      return this;
   }

   public Module getModule() {
      return this.module;
   }

   public UNoise3PointSelector getUNoise3PointSelector() {
      return this.uNoise3PointSelector;
   }

   public SettingVisibilityCondition getVisibilityCondition() {
      return this.visibilityCondition;
   }

   public UNoise3SettingContext(Module module2, DoubleValue doubleValue, UNoise3PointSelector uNoise3PointSelector2, SettingVisibilityCondition condition) {
      this.module = module2;
      this.parentGroup = doubleValue;
      this.uNoise3PointSelector = uNoise3PointSelector2;
      this.visibilityCondition = condition;
   }

   public UNoise3SettingContext withParentGroup(DoubleValue doubleValue) {
      return new UNoise3SettingContext(this.module, doubleValue, this.uNoise3PointSelector, this.visibilityCondition);
   }

   public static void setControlFlowState(int[] ints) {
      controlFlowState = ints;
   }

   public static int[] getControlFlowState() {
      return controlFlowState;
   }

   static {
      if (getControlFlowState() == null) {
         setControlFlowState(new int[5]);
      }
   }
}
