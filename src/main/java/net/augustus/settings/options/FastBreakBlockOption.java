package net.augustus.settings.options;

import net.augustus.settings.SelectableOption;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.Module;
import net.augustus.modules.world.FastBreak;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class FastBreakBlockOption implements SelectableOption {
   private boolean selected;
   private final String name;
   private final Module parentModule;
   private BooleanValue customInAirMultiplierSetting;
   private StringValue inAirMultiplierSetting;
   private StringValue multiplierSetting;
   private StringValue artificialHasteSetting;
   private BooleanValue instantBreakSetting;
   public final FastBreak fastBreak;

   public FastBreakBlockOption(FastBreak fastBreak2, Module module, String str) {
      this.fastBreak = fastBreak2;
      this.selected = true;
      this.name = str;
      this.parentModule = module;
   }

   public void initSettings(String str, BooleansSetting booleansSetting, DoubleValue doubleValue, SettingVisibilityCondition condition) {
      DoubleValue doubleValue2;
      if (doubleValue == null) {
         doubleValue2 = new DoubleValue(this.parentModule, str + this.name);
         SettingVisibilityCondition condition2 = () -> this.isExpertGroupVisible(booleansSetting, condition);
         doubleValue2 = doubleValue2.visibleWhen(condition2);
         String name2 = this.name;
         doubleValue2 = (DoubleValue)doubleValue2.withDisplayName(name2);
      } else {
         doubleValue2 = new DoubleValue(this.parentModule, str + this.name, doubleValue);
         SettingVisibilityCondition condition3 = () -> this.isNestedExpertGroupVisible(booleansSetting, condition);
         doubleValue2 = doubleValue2.visibleWhen(condition3);
         String name3 = this.name;
         doubleValue2 = (DoubleValue)doubleValue2.withDisplayName(name3);
      }

      DoubleValue doubleValue3 = doubleValue2;
      BooleanValue booleanValue = new BooleanValue(this.parentModule, str + this.name + "CustomInAirMultiplier", false, doubleValue3);
      String str2 = "CustomInAirMultiplier";
      this.customInAirMultiplierSetting = (BooleanValue)booleanValue.withDisplayName(str2);
      StringValue stringValue = new StringValue(this.parentModule, str + this.name + "InAirMultiplier", 0.2, 0.0, 1.0, 2, doubleValue3);
      String str3 = "InAirMultiplier";
      StringValue stringValue2 = stringValue.named(str3);
      BooleanValue booleanValue2 = this.customInAirMultiplierSetting;
      this.customInAirMultiplierSetting.getClass();
      BooleanValue booleanValue3 = booleanValue2;
      SettingVisibilityCondition condition4 = () -> booleanValue3.getValue();
      this.inAirMultiplierSetting = stringValue2.visibleWhen(condition4);
      StringValue stringValue3 = new StringValue(this.parentModule, str + this.name + "Multiplier", 1.0, 1.0, 2.0, 2, doubleValue3);
      String str4 = "Multiplier";
      this.multiplierSetting = stringValue3.named(str4);
      StringValue stringValue4 = new StringValue(this.parentModule, str + this.name + "ArtificialMiningSpeedEffect", 0.0, 0.0, 3.0, 0, doubleValue3);
      String str5 = "ArtificialMiningSpeedEffect";
      this.artificialHasteSetting = stringValue4.named(str5);
      BooleanValue booleanValue4 = new BooleanValue(this.parentModule, str + this.name + "Instant", false, doubleValue3);
      String str6 = "Instant";
      this.instantBreakSetting = (BooleanValue)booleanValue4.withDisplayName(str6);
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public boolean isSelected() {
      return this.selected;
   }

   @Override
   public void setSelected(boolean flag) {
      this.selected = flag;
   }

   private boolean isNestedExpertGroupVisible(BooleansSetting booleansSetting, SettingVisibilityCondition condition) {
      return this.selected && booleansSetting.getSelectedValue().equals("Expert") && condition.isVisible();
   }

   private boolean isExpertGroupVisible(BooleansSetting booleansSetting, SettingVisibilityCondition condition) {
      return this.selected && booleansSetting.getSelectedValue().equals("Expert") && condition.isVisible();
   }

   public static BooleanValue getCustomInAirMultiplierSetting(FastBreakBlockOption fastBreakBlockOption) {
      return fastBreakBlockOption.customInAirMultiplierSetting;
   }

   public static StringValue getInAirMultiplierSetting(FastBreakBlockOption fastBreakBlockOption) {
      return fastBreakBlockOption.inAirMultiplierSetting;
   }

   public static StringValue getMultiplierSetting(FastBreakBlockOption fastBreakBlockOption) {
      return fastBreakBlockOption.multiplierSetting;
   }

   public static StringValue getArtificialHasteSetting(FastBreakBlockOption fastBreakBlockOption) {
      return fastBreakBlockOption.artificialHasteSetting;
   }

   public static BooleanValue getInstantBreakSetting(FastBreakBlockOption fastBreakBlockOption) {
      return fastBreakBlockOption.instantBreakSetting;
   }

   public static boolean isOptionSelected(FastBreakBlockOption fastBreakBlockOption) {
      return fastBreakBlockOption.selected;
   }

}
