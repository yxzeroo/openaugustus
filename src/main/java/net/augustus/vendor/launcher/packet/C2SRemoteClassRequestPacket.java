package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SRemoteClassRequestPacket implements LauncherPacket {
   private byte[] sessionToken;
   private String className;

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.sessionToken);
      nettyPacketBuf.writeUtf8String(this.className);
   }

   public C2SRemoteClassRequestPacket() {
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.sessionToken = nettyPacketBuf.readByteArray();
      this.className = nettyPacketBuf.readUtf8String();
   }

   public C2SRemoteClassRequestPacket(byte[] bytes, String str) {
      this.sessionToken = bytes;
      this.className = str;
   }




}
