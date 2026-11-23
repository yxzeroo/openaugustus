package net.augustus.settings.options;

import net.augustus.settings.SelectableOption;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S48PacketResourcePackSend;

public enum VelocityCancelPacketOption implements SelectableOption {
   S00PacketKeepAlive("S00PacketKeepAlive", S00PacketKeepAlive.class),
   S32PacketConfirmTransaction("S32PacketConfirmTransaction", S32PacketConfirmTransaction.class),
   S48PacketResourcePackSend("S48PacketResourcePackSend", S48PacketResourcePackSend.class);

private String name;
   private boolean selected;
   private Class packetClass;
   private BooleansSetting cancelTypeSetting;
   private StringValue packetCountSetting;
   private int remainingCancelCount = 0;
private VelocityCancelPacketOption(String str, Class clazz) {
      this.name = str;
      this.packetClass = clazz;
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

   public static BooleansSetting setCancelTypeSetting(VelocityCancelPacketOption option, BooleansSetting booleansSetting) {
      return option.cancelTypeSetting = booleansSetting;
   }

   public static StringValue setPacketCountSetting(VelocityCancelPacketOption option, StringValue stringValue) {
      return option.packetCountSetting = stringValue;
   }

   public static int setRemainingCancelCount(VelocityCancelPacketOption option, int n) {
      return option.remainingCancelCount = n;
   }

   public static StringValue getPacketCountSetting(VelocityCancelPacketOption option) {
      return option.packetCountSetting;
   }

   public static Class getPacketClass(VelocityCancelPacketOption option) {
      return option.packetClass;
   }

   public static int getRemainingCancelCount(VelocityCancelPacketOption option) {
      return option.remainingCancelCount;
   }

   public static int decrementRemainingCancelCount(VelocityCancelPacketOption option) {
      return option.remainingCancelCount--;
   }

   public static BooleansSetting getCancelTypeSetting(VelocityCancelPacketOption option) {
      return option.cancelTypeSetting;
   }
}
