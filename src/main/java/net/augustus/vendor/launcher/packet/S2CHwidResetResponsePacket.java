package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CHwidResetResponsePacket implements LauncherPacket {
   private int statusCode;
   private String message;

   public String getMessage() {
      return this.message;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
   }

   public S2CHwidResetResponsePacket(int n, String str, long l) {
      this.statusCode = n;
      this.message = str;
   }

   public S2CHwidResetResponsePacket() {
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.statusCode = nettyPacketBuf.readEnumOrdinal();
      this.message = nettyPacketBuf.readNullTerminatedString();
   }

   public int getStatusCode() {
      return this.statusCode;
   }



}
