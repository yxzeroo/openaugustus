package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CActivateClientResponsePacket implements LauncherPacket {
   private int statusCode;
   private String message;

   public int getStatusCode() {
      return this.statusCode;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.statusCode = nettyPacketBuf.readEnumOrdinal();
      this.message = nettyPacketBuf.readNullTerminatedString();
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
   }

   public S2CActivateClientResponsePacket(int n, String str, long l) {
      this.statusCode = n;
      this.message = str;
   }

   public S2CActivateClientResponsePacket() {
   }

   public String getMessage() {
      return this.message;
   }




}
