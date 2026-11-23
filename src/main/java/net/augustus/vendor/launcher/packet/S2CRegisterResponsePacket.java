package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CRegisterResponsePacket implements LauncherPacket {
   private int statusCode;
   private String message;

   public String getMessage() {
      return this.message;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public S2CRegisterResponsePacket(long l, int n, String str) {
      this.statusCode = n;
      this.message = str;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.statusCode = nettyPacketBuf.readEnumOrdinal();
      this.message = nettyPacketBuf.readNullTerminatedString();
   }

   public S2CRegisterResponsePacket() {
   }




}
