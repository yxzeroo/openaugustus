package net.augustus.modules.modes.highjump;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.HighJump;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public abstract class HighJumpMode extends AbstractSubMode implements UpdateListener, MoveInputListener {
   public BooleanValue autoDisable;
   public BooleanValue activateWhileJumpKeyPressed;
   private boolean jumpActive;
   public int groundTicks;
   private boolean triggeredByJumpKey;

   public HighJumpMode(HighJump highJump, DoubleValue doubleValue, String str) {
       super(highJump, doubleValue, str);
      this.jumpActive = false;
      this.groundTicks = 0;
      BooleanValue booleanValue = new BooleanValue(highJump, "AutoDisable" + str, true, doubleValue);
      String str2 = "AutoDisable";
      booleanValue = (BooleanValue)booleanValue.withDisplayName(str2);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.autoDisable = (BooleanValue)booleanValue.withVisibility(condition);
      booleanValue = new BooleanValue(highJump, "ActivateWhileJumpKeyPressed" + str, false, doubleValue);
      String str3 = "ActivateWhileJumpKeyPressed";
      booleanValue = (BooleanValue)booleanValue.withDisplayName(str3);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.activateWhileJumpKeyPressed = (BooleanValue)booleanValue.withVisibility(condition2);
   }

   @Override
   public void onEnable() {
      this.groundTicks = 0;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onUpdate2() {
      this.jumpActive = true;
      if (mc.thePlayer.onGround) {
         if (this.groundTicks > 0 && this.autoDisable.getValue()) {
            HighJump parentModule2 = (HighJump)this.parentModule;
            boolean flag = false;
            parentModule2.setToggled(flag);
            this.jumpActive = false;
            return;
         }

         if (this.groundTicks > 0 && this.activateWhileJumpKeyPressed.getValue()) {
            this.triggeredByJumpKey = false;
            this.onDisable();
         }

         this.groundTicks++;
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (event.isJump() && this.activateWhileJumpKeyPressed.getValue() && !this.triggeredByJumpKey) {
         boolean flag = false;
         event.setJump(flag);
         this.onEnable();
         this.triggeredByJumpKey = true;
      }
   }

   public boolean isActivated() {
      return this.triggeredByJumpKey || !this.activateWhileJumpKeyPressed.getValue();
   }

   public boolean isJumpActive() {
      return this.jumpActive;
   }




}
