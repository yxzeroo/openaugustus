package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ServerCommandPacket implements VendorPacket {
   private String targetArgument;
   private String commandName;
   private byte[] sessionToken;
   private String commandPayload;

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.commandName = buf.readUtfString();
      this.targetArgument = buf.readNullTerminatedAscii();
      this.commandPayload = buf.readUtfString();
   }

   public String getTargetArgument() {
      return this.targetArgument;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeUtfString(this.commandName);
      buf.writeNullTerminatedAscii(this.targetArgument);
      buf.writeUtfString(this.commandPayload);
   }

   public ServerCommandPacket(byte[] bytes, String str, String str2, String str3) {
      this.sessionToken = bytes;
      this.commandName = str;
      this.targetArgument = str2;
      this.commandPayload = str3;
   }

   public String getCommandPayload() {
      return this.commandPayload;
   }

   public String getCommandName() {
      return this.commandName;
   }
}
