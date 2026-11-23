package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SHandshakeInitPacket implements LauncherPacket {
   private byte[] encryptedKeyExchange;
   private int encodedKeyIndex;

   public C2SHandshakeInitPacket(int n, byte[] bytes) {
      this.encodedKeyIndex = n;
      this.encryptedKeyExchange = bytes;
   }

   public byte[] getEncryptedKeyExchange(int n, short s, short s2) {
      return this.encryptedKeyExchange;
   }

   public C2SHandshakeInitPacket() {
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeInt(this.encodedKeyIndex);
      nettyPacketBuf.writeByteArray(this.encryptedKeyExchange);
   }

   public int getEncodedKeyIndex(int n, char ch, char ch2) {
      return this.encodedKeyIndex;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.encodedKeyIndex = nettyPacketBuf.readInt();
      this.encryptedKeyExchange = nettyPacketBuf.readByteArray();
   }




}
