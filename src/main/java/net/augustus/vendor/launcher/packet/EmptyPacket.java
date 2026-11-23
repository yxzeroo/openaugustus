package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class EmptyPacket implements LauncherPacket {
   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
   }
}
