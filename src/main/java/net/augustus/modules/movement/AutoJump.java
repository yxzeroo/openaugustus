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
import net.augustus.settings.SettingGroup;
import net.minecraft.block.state.IBlockState;

public class AutoJump extends Module implements MoveInputListener {
   private final BooleanValue inInv;
   private final MultiOptionSetting blocksToAutoJump;

   public AutoJump() {
       super("AutoJump", "AutomatischerSprung", new Color(35, 52, 45), Categorys.MOVEMENT);
      this.inInv = new BooleanValue(this, "InInv", false);
      this.blocksToAutoJump = new MultiOptionSetting(this, "BlocksToAutoJump", AutoJumpBlockOption.values());

      for (AutoJumpBlockOption autoJumpBlockOption : AutoJumpBlockOption.values()) {
         SettingGroup doubleValue2 = new SettingGroup(this, autoJumpBlockOption.getName(), true).visibleWhen(autoJumpBlockOption::isSelected);
         AutoJumpBlockOption.setOnlyWhileMovingSetting(autoJumpBlockOption, (BooleanValue)new BooleanValue(this, autoJumpBlockOption.getName() + "OnlyWhileMove", autoJumpBlockOption == AutoJumpBlockOption.STAIRS, doubleValue2).withDisplayName("OnlyWhileMove"));
         AutoJumpBlockOption.setOnlyWhileSprintingSetting(autoJumpBlockOption, (BooleanValue)new BooleanValue(this, autoJumpBlockOption.getName() + "OnlyAtSprint", autoJumpBlockOption == AutoJumpBlockOption.STAIRS, doubleValue2).withDisplayName("OnlyAtSprint"));
      }
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)21);
   }

   public void onMoveInput3(EventMoveInput event) {
      if ((mc.currentScreen == null || this.inInv.getValue()) && mc.thePlayer.onGround) {
         IBlockState blockState = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ);

         for (AutoJumpBlockOption autoJumpBlockOption : AutoJumpBlockOption.values()) {
            if (autoJumpBlockOption.isSelected() && autoJumpBlockOption.shouldAutoJump(blockState)) {
               event.setJump(true);
               break;
            }
         }
      }
   }

}
