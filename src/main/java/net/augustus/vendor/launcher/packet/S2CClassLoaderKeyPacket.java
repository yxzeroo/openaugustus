package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CClassLoaderKeyPacket implements LauncherPacket {
   private byte[] classLoaderKey;

   public S2CClassLoaderKeyPacket(short s, byte[] bytes, long l) {
      this.classLoaderKey = bytes;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.classLoaderKey);
   }

   public byte[] getClassLoaderKey() {
      return this.classLoaderKey;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.classLoaderKey = nettyPacketBuf.readByteArray();
   }

   public S2CClassLoaderKeyPacket() {
   }




}
