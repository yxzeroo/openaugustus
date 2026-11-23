package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public interface LauncherPacket {
   void write(NettyPacketBuf nettyPacketBuf) throws Exception;

   void read(NettyPacketBuf nettyPacketBuf) throws Exception;
}
