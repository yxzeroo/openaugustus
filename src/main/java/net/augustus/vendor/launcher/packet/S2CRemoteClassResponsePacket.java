package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CRemoteClassResponsePacket implements LauncherPacket {
   private byte[] classData;

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.classData);
   }

   public byte[] getClassData() {
      return this.classData;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.classData = nettyPacketBuf.readByteArray();
   }




}
