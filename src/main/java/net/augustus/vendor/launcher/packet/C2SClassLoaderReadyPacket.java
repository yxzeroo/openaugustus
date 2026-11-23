package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SClassLoaderReadyPacket implements LauncherPacket {
   private long timestamp;
   private byte[] authSessionToken;
   private byte[] loginSessionToken;

   public C2SClassLoaderReadyPacket(byte[] bytes, byte[] bytes2, long l) {
      this.loginSessionToken = bytes;
      this.authSessionToken = bytes2;
      this.timestamp = l;
   }

   public byte[] getAuthSessionToken(long l) {
      return this.authSessionToken;
   }

   public long getTimestamp(char ch, int n, char ch2) {
      return this.timestamp;
   }

   public byte[] getLoginSessionToken(long l) {
      return this.loginSessionToken;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.loginSessionToken);
      nettyPacketBuf.writeByteArray(this.authSessionToken);
      nettyPacketBuf.writeLong(this.timestamp);
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.loginSessionToken = nettyPacketBuf.readByteArray();
      this.authSessionToken = nettyPacketBuf.readByteArray();
      this.timestamp = nettyPacketBuf.readLong();
   }

   public C2SClassLoaderReadyPacket() {
   }



}
