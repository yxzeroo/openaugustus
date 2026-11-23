package net.augustus.settings;

import net.augustus.ClientManagers;
import net.augustus.modules.Module;
import net.augustus.utils.interfaces.MC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LookDownDisableSettings extends SettingComposite implements MC {
   private final BooleansSetting disableWhileLookingDown;
   private final StringValue lookDownPitch;

   private boolean isHoldingBlock() {
      if (mc.thePlayer == null) {
         return false;
      } else {
         ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
         if (currentEquippedItem == null) {
            return false;
         } else {
            Item item = currentEquippedItem.getItem();
            return item instanceof ItemBlock;
         }
      }
   }

   private boolean isPitchSettingVisible() {
      return !this.disableWhileLookingDown.getSelectedValue().equals("None");
   }

   public LookDownDisableSettings(Module module, String str, DoubleValue doubleValue) {
       super();
      BooleansSetting booleansSetting = new BooleansSetting(
         module, "_DisableWhileLookingDown" + str, "None", new String[]{"None", "OnlyHoldingBlock", "Always"}, doubleValue
      );
      String str2 = "DisableWhileLookingDown";
      Setting setting = booleansSetting.withDisplayName(str2);
      this.disableWhileLookingDown = (BooleansSetting)this.registerSetting(setting);
      StringValue stringValue = new StringValue(module, "_LookDownPitch" + str, 70.0, 0.0, 90.0, 0, doubleValue);
      String str3 = "LookDownPitch";
      StringValue stringValue2 = stringValue.named(str3);
      SettingVisibilityCondition condition = () -> this.isPitchSettingVisible();
      StringValue stringValue3 = stringValue2.visibleWhen(condition);
      this.lookDownPitch = (StringValue)this.registerSetting(stringValue3);
   }

   public LookDownDisableSettings(Module module, String str) {
       this(module, str, null);
   }

   public boolean shouldDisable() {
      String selectedValue = this.disableWhileLookingDown.getSelectedValue();
      switch (selectedValue) {
         case "None":
            return false;
         case "OnlyHoldingBlock":
            return this.isPitchBelowThreshold() && this.isHoldingBlock();
         default:
            return this.isPitchBelowThreshold();
      }
   }

   public LookDownDisableSettings(Module module) {
       this(module, "", null);
   }

   private boolean isPitchBelowThreshold() {
      return (double)ClientManagers.rotationController.getPreviousRotation().pitch < this.lookDownPitch.getValue();
   }

   public LookDownDisableSettings(Module module, DoubleValue doubleValue) {
       this(module, "", doubleValue);
   }


}
