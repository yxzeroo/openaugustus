package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.options.AutoJumpBlockOption;
import net.augustus.settings.Setting;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.minecraft.block.state.IBlockState;

public class AutoJump extends Module implements MoveInputListener {
   private final BooleanValue inInv;
   private final MultiOptionSetting blocksToAutoJump;

   public AutoJump() {
       super("AutoJump", "AutomatischerSprung", new Color(35, 52, 45), Categorys.MOVEMENT);
      Fly.getCachedSettings();
      this.inInv = new BooleanValue(this, "InInv", false);
      this.blocksToAutoJump = new MultiOptionSetting(this, "BlocksToAutoJump", AutoJumpBlockOption.values());

      for (AutoJumpBlockOption autoJumpBlockOption : AutoJumpBlockOption.values()) {
         DoubleValue doubleValue = new DoubleValue(this, autoJumpBlockOption.getName(), true);
         SettingVisibilityCondition condition = autoJumpBlockOption::isSelected;
         DoubleValue doubleValue2 = doubleValue.visibleWhen(condition);
         BooleanValue booleanValue = new BooleanValue(this, autoJumpBlockOption.getName() + "OnlyWhileMove", autoJumpBlockOption == AutoJumpBlockOption.STAIRS, doubleValue2);
         String str = "OnlyWhileMove";
         BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str);
         AutoJumpBlockOption.setOnlyWhileMovingSetting(autoJumpBlockOption, booleanValue2);
         BooleanValue booleanValue3 = new BooleanValue(this, autoJumpBlockOption.getName() + "OnlyAtSprint", autoJumpBlockOption == AutoJumpBlockOption.STAIRS, doubleValue2);
         String str2 = "OnlyAtSprint";
         BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str2);
         AutoJumpBlockOption.setOnlyWhileSprintingSetting(autoJumpBlockOption, booleanValue4);
      }
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if ((mc.currentScreen == null || this.inInv.getValue()) && mc.thePlayer.onGround) {
         IBlockState blockState = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ);

         for (AutoJumpBlockOption autoJumpBlockOption : AutoJumpBlockOption.values()) {
            if (autoJumpBlockOption.isSelected() && autoJumpBlockOption.shouldAutoJump(blockState)) {
               boolean flag = true;
               event.setJump(flag);
               break;
            }
         }
      }
   }

}
