package net.augustus.settings.options;

import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.SelectableOption;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import net.augustus.modules.Module;
import net.augustus.modules.world.FastBreak;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.block.Block;

public class FastBreakDedicatedItemOption implements SelectableOption {
   private boolean selected;
   private final String name;
   private final Module parentModule;
   private final HashMap blockOptions;
   private BooleansSetting modeSetting;
   private DoubleValue simpleSettingsGroup;
   private BooleanValue customInAirMultiplierSetting;
   private StringValue inAirMultiplierSetting;
   private StringValue multiplierSetting;
   private StringValue artificialHasteSetting;
   private BooleanValue instantBreakSetting;
   public final FastBreak fastBreak;

   public FastBreakDedicatedItemOption(FastBreak fastBreak2, Module module, String str) {
      this.fastBreak = fastBreak2;
      this.selected = false;
      this.blockOptions = new HashMap();
      this.name = str;
      this.parentModule = module;
   }

   public void initSettings() {
      DoubleValue doubleValue = new DoubleValue(this.parentModule, this.name, true, FastBreak.accessDedicatedItemsSetting(this.fastBreak));
      SettingVisibilityCondition condition = () -> this.isItemGroupVisible();
      DoubleValue doubleValue2 = doubleValue.visibleWhen(condition);
      BooleansSetting booleansSetting = new BooleansSetting(this.parentModule, this.name + "Mode", "Simple", new String[]{"Simple", "Expert"}, doubleValue2);
      String str = "Mode";
      BooleansSetting booleansSetting2 = (BooleansSetting)booleansSetting.withDisplayName(str);
      SettingVisibilityCondition condition2 = () -> this.selected;
      this.modeSetting = (BooleansSetting)booleansSetting2.withVisibility(condition2);
      DoubleValue doubleValue3 = new DoubleValue(this.parentModule, this.name + "SimpleSettings", true, doubleValue2);
      String str2 = "SimpleSettings";
      DoubleValue doubleValue4 = (DoubleValue)doubleValue3.withDisplayName(str2);
      SettingVisibilityCondition condition3 = () -> this.isSimpleModeVisible();
      this.simpleSettingsGroup = doubleValue4.visibleWhen(condition3);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, this.name + "CustomInAirMultiplier", false, this.simpleSettingsGroup);
      String str3 = "CustomInAirMultiplier";
      this.customInAirMultiplierSetting = (BooleanValue)booleanValue.withDisplayName(str3);
      StringValue stringValue = new StringValue(this.parentModule, this.name + "InAirMultiplier", 0.2, 0.0, 1.0, 2, this.simpleSettingsGroup);
      String str4 = "InAirMultiplier";
      StringValue stringValue2 = stringValue.named(str4);
      BooleanValue booleanValue2 = this.customInAirMultiplierSetting;
      this.customInAirMultiplierSetting.getClass();
      BooleanValue booleanValue3 = booleanValue2;
      SettingVisibilityCondition condition4 = () -> booleanValue3.getValue();
      this.inAirMultiplierSetting = stringValue2.visibleWhen(condition4);
      StringValue stringValue3 = new StringValue(this.parentModule, this.name + "Multiplier", 2.0, 1.0, 2.0, 2, this.simpleSettingsGroup);
      String str5 = "Multiplier";
      this.multiplierSetting = stringValue3.named(str5);
      StringValue stringValue4 = new StringValue(this.parentModule, this.name + "ArtificialMiningSpeedEffect", 0.0, 0.0, 3.0, 0, this.simpleSettingsGroup);
      String str6 = "ArtificialMiningSpeedEffect";
      this.artificialHasteSetting = stringValue4.named(str6);
      BooleanValue booleanValue4 = new BooleanValue(this.parentModule, this.name + "Instant", false, this.simpleSettingsGroup);
      String str7 = "Instant";
      this.instantBreakSetting = (BooleanValue)booleanValue4.withDisplayName(str7);
      HashMap blockOptions2 = this.blockOptions;
      FastBreak.accessPopulateBlockOptions(this.fastBreak, blockOptions2);
      FastBreakBlockOption[] fastBreakBlockOptions = (FastBreakBlockOption[])this.blockOptions.values().toArray(new FastBreakBlockOption[0]);
      Arrays.sort(fastBreakBlockOptions, Comparator.comparing(fastBreakBlockOption -> fastBreakBlockOption.getName()));
      MultiOptionSetting multiOptionSetting = new MultiOptionSetting(this.parentModule, this.name + "BlocksToFastbreak", fastBreakBlockOptions, doubleValue2);
      String str8 = "BlocksToFastbreak";
      MultiOptionSetting multiOptionSetting2 = multiOptionSetting.named(str8);
      SettingVisibilityCondition condition5 = () -> this.selected;
      multiOptionSetting2.visibleWhen(condition5);
      ((Collection<FastBreakBlockOption>)this.blockOptions.values()).stream().sorted(Comparator.comparing(fastBreakBlockOption2 -> fastBreakBlockOption2.getName())).forEach(fastBreakBlockOption3 -> this.initBlockOptionSettings(doubleValue2, fastBreakBlockOption3));
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

   public boolean hasCustomInAirMultiplier(Block block) {
      return this.modeSetting.getSelectedValue().equals("Simple") ? this.customInAirMultiplierSetting.getValue() : FastBreakBlockOption.getCustomInAirMultiplierSetting(this.getBlockOption(block)).getValue();
   }

   public double getInAirMultiplier(Block block) {
      return this.modeSetting.getSelectedValue().equals("Simple") ? this.inAirMultiplierSetting.getValue() : FastBreakBlockOption.getInAirMultiplierSetting(this.getBlockOption(block)).getValue();
   }

   public double getSpeedMultiplier(Block block) {
      return this.modeSetting.getSelectedValue().equals("Simple") ? this.multiplierSetting.getValue() : FastBreakBlockOption.getMultiplierSetting(this.getBlockOption(block)).getValue();
   }

   public double getArtificialHasteEffect(Block block) {
      return this.modeSetting.getSelectedValue().equals("Simple") ? this.artificialHasteSetting.getValue() : FastBreakBlockOption.getArtificialHasteSetting(this.getBlockOption(block)).getValue();
   }

   public boolean isInstantBreak(Block block) {
      return this.modeSetting.getSelectedValue().equals("Simple") ? this.instantBreakSetting.getValue() : FastBreakBlockOption.getInstantBreakSetting(this.getBlockOption(block)).getValue();
   }

   public boolean isBlockEnabled(Block block) {
      FastBreakBlockOption blockOption = this.getBlockOption(block);
      return blockOption != null && FastBreakBlockOption.isOptionSelected(blockOption);
   }

   private FastBreakBlockOption getBlockOption(Block block) {
      return (FastBreakBlockOption)this.blockOptions.get(block);
   }

   private void initBlockOptionSettings(DoubleValue doubleValue, FastBreakBlockOption fastBreakBlockOption) {
      String name2 = this.name;
      BooleansSetting modeSetting2 = this.modeSetting;
      SettingVisibilityCondition condition = () -> {
         return this.selected && this.modeSetting.isVisible();
      };
      BooleansSetting booleansSetting = modeSetting2;
      String str = name2;
      fastBreakBlockOption.initSettings(str, booleansSetting, doubleValue, condition);
   }

   private boolean isSimpleModeVisible() {
      return this.modeSetting.getSelectedValue().equals("Simple") && this.selected;
   }

   private boolean isItemGroupVisible() {
      return this.selected && FastBreak.accessDedicatedItemHandlingSetting(this.fastBreak).getValue();
   }

   public static boolean isOptionSelected(FastBreakDedicatedItemOption option) {
      return option.selected;
   }

}
