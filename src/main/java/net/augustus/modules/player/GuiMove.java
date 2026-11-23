package net.augustus.modules.player;

import java.awt.Color;
import java.util.Locale;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.clickgui.components.TextFieldComponent;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.listeners.SprintListener;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.options.GuiMoveScreenOption;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventSafeWalk;
import net.augustus.events.listeners.SafeWalkListener;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventSprint;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class GuiMove extends Module implements SprintListener, MoveInputListener, UpdateListener, PacketListener, MoveEntityListener, SafeWalkListener {
   private final MultiOptionSetting guis;
   public BooleanValue noInvPacket;
   private final TimeHelper timeHelper;
   private GuiMoveScreenOption activeScreenOption;

   public GuiMove() {
       super("GuiMove", "BenutzeroberflächenBewegung", new Color(11, 143, 125), Categorys.PLAYER);
      this.guis = new MultiOptionSetting(this, "Guis", GuiMoveScreenOption.values());
      this.timeHelper = new TimeHelper();
      this.activeScreenOption = null;
      GuiMoveScreenOption[] values = GuiMoveScreenOption.values();

      for (GuiMoveScreenOption guiMoveScreenOption : values) {
         String str = guiMoveScreenOption.getName().toLowerCase(Locale.ROOT);
         DoubleValue doubleValue = new DoubleValue(this, guiMoveScreenOption.getName());
         SettingVisibilityCondition condition = guiMoveScreenOption::isSelected;
         DoubleValue doubleValue2 = doubleValue.visibleWhen(condition);
         BooleansSetting booleansSetting = new BooleansSetting(this, guiMoveScreenOption.getName() + "Mode", "Basic", new String[]{"Basic", "Resprint", "Intave"}, doubleValue2);
         String str2 = "Mode";
         BooleansSetting booleansSetting2 = (BooleansSetting)booleansSetting.withDisplayName(str2);
         GuiMoveScreenOption.setModeSetting(guiMoveScreenOption, booleansSetting2);
         BooleanValue booleanValue = new BooleanValue(this, guiMoveScreenOption.getName() + "Sneak", false, doubleValue2);
         String str3 = "Sneak in " + str;
         BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str3);
         GuiMoveScreenOption.setSneakSetting(guiMoveScreenOption, booleanValue2);
         BooleanValue booleanValue3 = new BooleanValue(this, guiMoveScreenOption.getName() + "Jump", false, doubleValue2);
         String str4 = "Jump in " + str;
         BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str4);
         GuiMoveScreenOption.setJumpSetting(guiMoveScreenOption, booleanValue4);
         if (guiMoveScreenOption == GuiMoveScreenOption.INVENTORY) {
            BooleanValue booleanValue5 = new BooleanValue(this, "NoInvPacket", false, doubleValue2);
            String str5 = "No inventory open packet";
            this.noInvPacket = (BooleanValue)booleanValue5.withDisplayName(str5);
         }

         BooleanValue booleanValue6 = new BooleanValue(this, guiMoveScreenOption.getName() + "Rotate", false, doubleValue2);
         String str6 = "Rotate in " + str;
         BooleanValue booleanValue7 = (BooleanValue)booleanValue6.withDisplayName(str6);
         GuiMoveScreenOption.setRotateSetting(guiMoveScreenOption, booleanValue7);
         BooleanValue booleanValue8 = new BooleanValue(this, guiMoveScreenOption.getName() + "RotateOnlyAtClick", false, doubleValue2);
         String str7 = "Rotate only while click in " + str;
         BooleanValue booleanValue9 = (BooleanValue)booleanValue8.withDisplayName(str7);
         BooleanValue rotateSetting = GuiMoveScreenOption.getRotateSetting(guiMoveScreenOption);
         rotateSetting.getClass();
         BooleanValue booleanValue10 = rotateSetting;
         SettingVisibilityCondition condition2 = () -> booleanValue10.getValue();
         BooleanValue booleanValue11 = (BooleanValue)booleanValue9.withVisibility(condition2);
         GuiMoveScreenOption.setRotateOnlyAtClickSetting(guiMoveScreenOption, booleanValue11);
         BooleanValue booleanValue12 = new BooleanValue(this, guiMoveScreenOption.getName() + "MoveOnlyOnGround", false, doubleValue2);
         String str8 = "Move only on ground in " + str;
         BooleanValue booleanValue13 = (BooleanValue)booleanValue12.withDisplayName(str8);
         GuiMoveScreenOption.setMoveOnlyOnGroundSetting(guiMoveScreenOption, booleanValue13);
         BooleanValue booleanValue14 = new BooleanValue(this, guiMoveScreenOption.getName() + "SneakMotionWhileMovement", false, doubleValue2);
         String str9 = "Sneak motion while moving in " + str;
         BooleanValue booleanValue15 = (BooleanValue)booleanValue14.withDisplayName(str9);
         GuiMoveScreenOption.setSneakMotionWhileMovingSetting(guiMoveScreenOption, booleanValue15);
         BooleansSetting booleansSetting3 = new BooleansSetting(
            this, guiMoveScreenOption.getName() + "Sprint mode", "None", new String[]{"None", "Legit", "AllDirection", "AllDirectionMotion"}, doubleValue2
         );
         String str10 = "Sprint mode";
         BooleansSetting booleansSetting4 = (BooleansSetting)booleansSetting3.withDisplayName(str10);
         GuiMoveScreenOption.setSprintModeSetting(guiMoveScreenOption, booleansSetting4);
         BooleanValue booleanValue16 = new BooleanValue(this, guiMoveScreenOption.getName() + "Jump sprint", false, doubleValue2);
         String str11 = "Sprint jump boost motion";
         BooleanValue booleanValue17 = (BooleanValue)booleanValue16.withDisplayName(str11);
         GuiMoveScreenOption.setSprintJumpBoostSetting(guiMoveScreenOption, booleanValue17);
         StringValue stringValue = new StringValue(this, guiMoveScreenOption.getName() + "MotionMultiplierForward", 1.0, 0.0, 2.0, 2, doubleValue2);
         String str12 = "Motion multiplier forward in " + str;
         StringValue stringValue2 = stringValue.named(str12);
         GuiMoveScreenOption.setForwardMotionMultiplierSetting(guiMoveScreenOption, stringValue2);
         StringValue stringValue3 = new StringValue(this, guiMoveScreenOption.getName() + "MotionMultiplierDiagonal", 1.0, 0.0, 2.0, 2, doubleValue2);
         String str13 = "Motion multiplier diagonal in " + str;
         StringValue stringValue4 = stringValue3.named(str13);
         GuiMoveScreenOption.setDiagonalMotionMultiplierSetting(guiMoveScreenOption, stringValue4);
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 36;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
      byte b3 = 43;
      this.toggleEventSubscription(b3);
      byte b4 = 26;
      this.toggleEventSubscription(b4);
      byte b5 = 20;
      this.toggleEventSubscription(b5);
      byte b6 = 33;
      this.toggleEventSubscription(b6);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.activeScreenOption == GuiMoveScreenOption.OTHER_SCREENS) {
         BooleanValue booleanValue = GuiMoveScreenOption.getRotateOnlyAtClickSetting(this.activeScreenOption);
         boolean flag = true;
         booleanValue.setValue(flag);
      }

      exitBlock:
      if (this.getMode().equals("Resprint") && this.canMoveInGui()) {
         int keyCode = mc.gameSettings.keyBindForward.getKeyCode();
         if (this.isValidKeyCode(keyCode)) {
            int keyCode2 = mc.gameSettings.keyBindForward.getKeyCode();
            int keyCode3 = mc.gameSettings.keyBindForward.getKeyCode();
            int n = keyCode3;
            KeyBinding.setKeyBindState(keyCode2, this.isKeyDownSafe(n));
         }

         int keyCode4 = mc.gameSettings.keyBindBack.getKeyCode();
         if (this.isValidKeyCode(keyCode4)) {
            int keyCode5 = mc.gameSettings.keyBindBack.getKeyCode();
            int keyCode6 = mc.gameSettings.keyBindBack.getKeyCode();
            int n2 = keyCode6;
            KeyBinding.setKeyBindState(keyCode5, this.isKeyDownSafe(n2));
         }

         int keyCode7 = mc.gameSettings.keyBindRight.getKeyCode();
         if (this.isValidKeyCode(keyCode7)) {
            int keyCode8 = mc.gameSettings.keyBindRight.getKeyCode();
            int keyCode9 = mc.gameSettings.keyBindRight.getKeyCode();
            int n3 = keyCode9;
            KeyBinding.setKeyBindState(keyCode8, this.isKeyDownSafe(n3));
         }

         int keyCode10 = mc.gameSettings.keyBindLeft.getKeyCode();
         if (this.isValidKeyCode(keyCode10)) {
            int keyCode11 = mc.gameSettings.keyBindLeft.getKeyCode();
            int keyCode12 = mc.gameSettings.keyBindLeft.getKeyCode();
            int n4 = keyCode12;
            KeyBinding.setKeyBindState(keyCode11, this.isKeyDownSafe(n4));
         }

         if (this.isSneakEnabled()) {
            int keyCode13 = mc.gameSettings.keyBindSneak.getKeyCode();
            if (this.isValidKeyCode(keyCode13)) {
               int keyCode14 = mc.gameSettings.keyBindSneak.getKeyCode();
               int keyCode15 = mc.gameSettings.keyBindSneak.getKeyCode();
               int n5 = keyCode15;
               KeyBinding.setKeyBindState(keyCode14, this.isKeyDownSafe(n5));
            }
         }

         if (this.isJumpEnabled()) {
            int keyCode16 = mc.gameSettings.keyBindJump.getKeyCode();
            if (this.isValidKeyCode(keyCode16)) {
               int keyCode17 = mc.gameSettings.keyBindJump.getKeyCode();
               int keyCode18 = mc.gameSettings.keyBindJump.getKeyCode();
               int n6 = keyCode18;
               KeyBinding.setKeyBindState(keyCode17, this.isKeyDownSafe(n6));
               break exitBlock;
            }
         }

         int keyCode19 = mc.gameSettings.keyBindJump.getKeyCode();
         if (this.isValidKeyCode(keyCode19)) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
         }
      }

      if (this.canMoveInGui()) {
         if (this.isSneakMotionWhileMoving()) {
            float f = (float)((double)event.getMoveForward() * 0.3);
            event.setMoveForward(f);
            float f2 = (float)((double)event.getMoveStrafe() * 0.3);
            event.setMoveStrafe(f2);
         }
      }

      if (this.canMoveInGui() && this.getMode().equals("Intave")) {
         boolean flag2 = true;
         event.setSneak(flag2);
      }
   }

   @Override
   public void onUpdate2() {
      int n;
      GuiScreen currentScreen = mc.currentScreen;
      this.activeScreenOption = this.resolveScreenOption(currentScreen);
      if (!this.canMoveInGui()) {
         this.timeHelper.reset();
      } else {
         String mode = this.getMode();
         switch (mode) {
            case "Intave":
               long l = 300L;
               if (!this.timeHelper.reached2(l)) {
                  break;
               }
            case "Basic":
               int keyCode = mc.gameSettings.keyBindForward.getKeyCode();
               if (this.isValidKeyCode(keyCode)) {
                  KeyBinding keyBindForward = mc.gameSettings.keyBindForward;
                  n = mc.gameSettings.keyBindForward.getKeyCode();
                  int n2 = n;
                  keyBindForward.setPressed(this.isKeyDownSafe(n2));
               }

               int keyCode2 = mc.gameSettings.keyBindBack.getKeyCode();
               if (this.isValidKeyCode(keyCode2)) {
                  KeyBinding keyBindBack = mc.gameSettings.keyBindBack;
                  n = mc.gameSettings.keyBindBack.getKeyCode();
                  int n3 = n;
                  keyBindBack.setPressed(this.isKeyDownSafe(n3));
               }

               int keyCode3 = mc.gameSettings.keyBindRight.getKeyCode();
               if (this.isValidKeyCode(keyCode3)) {
                  KeyBinding keyBindRight = mc.gameSettings.keyBindRight;
                  n = mc.gameSettings.keyBindRight.getKeyCode();
                  int n4 = n;
                  keyBindRight.setPressed(this.isKeyDownSafe(n4));
               }

               int keyCode4 = mc.gameSettings.keyBindLeft.getKeyCode();
               if (this.isValidKeyCode(keyCode4)) {
                  KeyBinding keyBindLeft = mc.gameSettings.keyBindLeft;
                  n = mc.gameSettings.keyBindLeft.getKeyCode();
                  int n5 = n;
                  keyBindLeft.setPressed(this.isKeyDownSafe(n5));
               }

               int keyCode5 = mc.gameSettings.keyBindSprint.getKeyCode();
               if (this.isValidKeyCode(keyCode5)) {
                  KeyBinding keyBindSprint = mc.gameSettings.keyBindSprint;
                  n = mc.gameSettings.keyBindSprint.getKeyCode();
                  int n6 = n;
                  keyBindSprint.setPressed(this.isKeyDownSafe(n6));
               }

               int keyCode6 = mc.gameSettings.keyBindJump.getKeyCode();
               if (this.isValidKeyCode(keyCode6)) {
                  if (this.isJumpEnabled()) {
                     KeyBinding keyBindJump = mc.gameSettings.keyBindJump;
                     n = mc.gameSettings.keyBindJump.getKeyCode();
                     int n7 = n;
                     keyBindJump.setPressed(this.isKeyDownSafe(n7));
                  } else {
                     mc.gameSettings.keyBindJump.setPressed(false);
                  }
               }

               if (this.isSneakEnabled()) {
                  int keyCode7 = mc.gameSettings.keyBindSneak.getKeyCode();
                  if (this.isValidKeyCode(keyCode7)) {
                     KeyBinding keyBindSneak = mc.gameSettings.keyBindSneak;
                     n = mc.gameSettings.keyBindSneak.getKeyCode();
                     int n8 = n;
                     keyBindSneak.setPressed(this.isKeyDownSafe(n8));
                  }
               }
         }
      }
   }

   private boolean isValidKeyCode(int n) {
      return n > 0 && n < 256;
   }

   private boolean isKeyDownSafe(int n) {

      try {
         return KeyboardInputUtil.isKeyDown(n);
      } catch (Exception e) {
         return false;
      }
   }

   @Override
   public void onSprint3(EventSprint event) {
      if (this.canMoveInGui()) {
         boolean flag = !this.getSprintMode().equals("None");
         event.setSprintKeyDown(flag);
         boolean flag2 = this.getSprintMode().equals("AllDirection") && (!this.getMode().equals("Resprint") || mc.previousScreen != null);
         event.setForceSprint(flag2);
      }
   }

   @Override
   public void onSprint(EventSprint event) {
      if (this.canMoveInGui()) {
         boolean flag = this.isSprintJumpBoostEnabled() && MoveUtil.isMoving();
         event.setOmniJumpSprint(flag);
      }
   }

   @Override
   public void onSprint2(EventSprint event) {
      if (this.canMoveInGui()) {
         boolean flag = this.getSprintMode().equals("AllDirectionMotion") && (!this.getMode().equals("Resprint") || mc.previousScreen != null);
         event.setMotionSprint(flag);
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C16PacketClientStatus && this.noInvPacket.getValue()) {
         C16PacketClientStatus packet2 = (C16PacketClientStatus)packet;
         if (packet2.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
            event.cancel();
         }
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      if (this.canMoveInGui() && mc.currentScreen != null) {
         double d;
         if (RotationUtil.isNearCardinalYaw(mc.thePlayer.rotationYaw)) {
            d = this.getForwardMotionMultiplier();
         } else {
            d = this.getDiagonalMotionMultiplier();
         }

         double d2 = d;
         if (this.getMode().equals("Intave")) {
            long l = 300L;
            if (this.timeHelper.reached2(l) && mc.thePlayer.onGround) {
               float f = (float)Math.toDegrees(MoveUtil.getDirection(mc.thePlayer.rotationYaw));
               d2 = RotationUtil.isNearCardinalYaw(f) ? 2.72 : 1.0;
            }
         }

         double d3 = event.getX() * d2;
         event.setX(d3);
         double d4 = event.getZ() * d2;
         event.setZ(d4);
      }
   }

   @Override
   public void onSafeWalk(EventSafeWalk event) {
      if (this.canMoveInGui() && this.getMode().equals("Intave")) {
         boolean flag = true;
         event.setDisableSneakSafeWalk(flag);
      }
   }

   public boolean canRotateInGui() {
      return this.isRotateEnabled() && this.canMoveInGui();
   }

   public boolean canMoveInGui() {
      return mc.currentScreen != null
         && (mc.thePlayer != null && mc.thePlayer.onGround || !this.isMoveOnlyOnGround())
         && this.isLicenseValid()
         && (
            !GuiScreen.NO_TOGGLE_SCREENS.contains(mc.currentScreen) && !(mc.currentScreen instanceof ClickGuiScreen)
               || mc.currentScreen instanceof ClickGuiScreen && !TextFieldComponent.anyFieldFocused
         )
         && this.activeScreenOption != null
         && this.activeScreenOption.isSelected();
   }

   private GuiMoveScreenOption resolveScreenOption(GuiScreen screen) {
      return screen == null
         ? null
         : (
            screen instanceof GuiInventory
               ? GuiMoveScreenOption.INVENTORY
               : (screen instanceof GuiContainer ? GuiMoveScreenOption.CONTAINER : (screen instanceof GuiGameOver ? GuiMoveScreenOption.DEATH_SCREEN : GuiMoveScreenOption.OTHER_SCREENS))
         );
   }

   private boolean isMoveOnlyOnGround() {
      return this.activeScreenOption != null && GuiMoveScreenOption.getMoveOnlyOnGroundSetting(this.activeScreenOption).getValue();
   }

   private String getMode() {
      return this.activeScreenOption == null ? "" : GuiMoveScreenOption.getModeSetting(this.activeScreenOption).getSelectedValue();
   }

   private boolean isSneakEnabled() {
      return this.activeScreenOption != null && GuiMoveScreenOption.getSneakSetting(this.activeScreenOption).getValue();
   }

   private boolean isJumpEnabled() {
      return this.activeScreenOption != null && GuiMoveScreenOption.getJumpSetting(this.activeScreenOption).getValue();
   }

   private boolean isRotateEnabled() {
      return this.activeScreenOption != null && GuiMoveScreenOption.getRotateSetting(this.activeScreenOption).getValue();
   }

   public boolean isRotateOnlyWhileClicking() {
      return this.activeScreenOption != null && GuiMoveScreenOption.getRotateOnlyAtClickSetting(this.activeScreenOption).getValue();
   }

   private boolean isSneakMotionWhileMoving() {
      return this.activeScreenOption != null && GuiMoveScreenOption.getSneakMotionWhileMovingSetting(this.activeScreenOption).getValue();
   }

   private String getSprintMode() {
      return this.activeScreenOption == null ? "" : GuiMoveScreenOption.getSprintModeSetting(this.activeScreenOption).getSelectedValue();
   }

   private boolean isSprintJumpBoostEnabled() {
      return this.activeScreenOption != null && GuiMoveScreenOption.getSprintJumpBoostSetting(this.activeScreenOption).getValue();
   }

   private double getForwardMotionMultiplier() {
      return this.activeScreenOption == null ? 1.0 : GuiMoveScreenOption.getForwardMotionMultiplierSetting(this.activeScreenOption).getValue();
   }

   private double getDiagonalMotionMultiplier() {
      return this.activeScreenOption == null ? 1.0 : GuiMoveScreenOption.getDiagonalMotionMultiplierSetting(this.activeScreenOption).getValue();
   }

   private boolean isLicenseValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
