package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CFailureMessagePacket implements LauncherPacket {
   private String message;

   public String getMessage() {
      return this.message;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.message = nettyPacketBuf.readUtf8String();
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeUtf8String(this.message);
   }

   public S2CFailureMessagePacket() {
   }

   public S2CFailureMessagePacket(long l, String str) {
      this.message = str;
   }



}
