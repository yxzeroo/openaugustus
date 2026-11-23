package net.augustus.settings.options;

import net.augustus.settings.SelectableOption;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;

public enum GuiMoveScreenOption implements SelectableOption {
   INVENTORY("Inventory", true),
   CONTAINER("Container", true),
   DEATH_SCREEN("DeathScreen", true),
   OTHER_SCREENS("OtherScreens", true);

private final String name;
   private boolean selected;
   private BooleansSetting modeSetting;
   private BooleanValue sneakSetting;
   private BooleanValue jumpSetting;
   private BooleanValue rotateSetting;
   private BooleanValue rotateOnlyAtClickSetting;
   private BooleanValue moveOnlyOnGroundSetting;
   private BooleanValue sneakMotionWhileMovingSetting;
   private BooleansSetting sprintModeSetting;
   private BooleanValue sprintJumpBoostSetting;
   private StringValue forwardMotionMultiplierSetting;
   private StringValue diagonalMotionMultiplierSetting;
private GuiMoveScreenOption(String str, boolean flag) {
      this.name = str;
      this.selected = flag;
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

   public static BooleansSetting setModeSetting(GuiMoveScreenOption guiMoveScreenOption, BooleansSetting booleansSetting) {
      return guiMoveScreenOption.modeSetting = booleansSetting;
   }

   public static BooleanValue setSneakSetting(GuiMoveScreenOption guiMoveScreenOption, BooleanValue booleanValue) {
      return guiMoveScreenOption.sneakSetting = booleanValue;
   }

   public static BooleanValue setJumpSetting(GuiMoveScreenOption guiMoveScreenOption, BooleanValue booleanValue) {
      return guiMoveScreenOption.jumpSetting = booleanValue;
   }

   public static BooleanValue setRotateSetting(GuiMoveScreenOption guiMoveScreenOption, BooleanValue booleanValue) {
      return guiMoveScreenOption.rotateSetting = booleanValue;
   }

   public static BooleanValue setRotateOnlyAtClickSetting(GuiMoveScreenOption guiMoveScreenOption, BooleanValue booleanValue) {
      return guiMoveScreenOption.rotateOnlyAtClickSetting = booleanValue;
   }

   public static BooleanValue getRotateSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.rotateSetting;
   }

   public static BooleanValue setMoveOnlyOnGroundSetting(GuiMoveScreenOption guiMoveScreenOption, BooleanValue booleanValue) {
      return guiMoveScreenOption.moveOnlyOnGroundSetting = booleanValue;
   }

   public static BooleanValue setSneakMotionWhileMovingSetting(GuiMoveScreenOption guiMoveScreenOption, BooleanValue booleanValue) {
      return guiMoveScreenOption.sneakMotionWhileMovingSetting = booleanValue;
   }

   public static BooleansSetting setSprintModeSetting(GuiMoveScreenOption guiMoveScreenOption, BooleansSetting booleansSetting) {
      return guiMoveScreenOption.sprintModeSetting = booleansSetting;
   }

   public static BooleanValue setSprintJumpBoostSetting(GuiMoveScreenOption guiMoveScreenOption, BooleanValue booleanValue) {
      return guiMoveScreenOption.sprintJumpBoostSetting = booleanValue;
   }

   public static StringValue setForwardMotionMultiplierSetting(GuiMoveScreenOption guiMoveScreenOption, StringValue stringValue) {
      return guiMoveScreenOption.forwardMotionMultiplierSetting = stringValue;
   }

   public static StringValue setDiagonalMotionMultiplierSetting(GuiMoveScreenOption guiMoveScreenOption, StringValue stringValue) {
      return guiMoveScreenOption.diagonalMotionMultiplierSetting = stringValue;
   }

   public static BooleanValue getRotateOnlyAtClickSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.rotateOnlyAtClickSetting;
   }

   public static BooleanValue getMoveOnlyOnGroundSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.moveOnlyOnGroundSetting;
   }

   public static BooleansSetting getModeSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.modeSetting;
   }

   public static BooleanValue getSneakSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.sneakSetting;
   }

   public static BooleanValue getJumpSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.jumpSetting;
   }

   public static BooleanValue getSneakMotionWhileMovingSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.sneakMotionWhileMovingSetting;
   }

   public static BooleansSetting getSprintModeSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.sprintModeSetting;
   }

   public static BooleanValue getSprintJumpBoostSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.sprintJumpBoostSetting;
   }

   public static StringValue getForwardMotionMultiplierSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.forwardMotionMultiplierSetting;
   }

   public static StringValue getDiagonalMotionMultiplierSetting(GuiMoveScreenOption guiMoveScreenOption) {
      return guiMoveScreenOption.diagonalMotionMultiplierSetting;
   }
}
