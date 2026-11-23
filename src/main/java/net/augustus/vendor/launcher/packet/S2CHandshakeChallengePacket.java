package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CHandshakeChallengePacket implements LauncherPacket {
   private int encodedKeyIndex;
   private byte[] encryptedPayload;
   private byte[] encryptedAesKey;

   public byte[] getEncryptedAesKey() {
      return this.encryptedAesKey;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.encodedKeyIndex = nettyPacketBuf.readInt();
      this.encryptedPayload = nettyPacketBuf.readByteArray();
      this.encryptedAesKey = nettyPacketBuf.readByteArray();
   }

   public S2CHandshakeChallengePacket(long l, int n, byte[] bytes, byte[] bytes2) {
      this.encodedKeyIndex = n;
      this.encryptedPayload = bytes;
      this.encryptedAesKey = bytes2;
   }

   public int getEncodedKeyIndex() {
      return this.encodedKeyIndex;
   }

   public byte[] getEncryptedPayload() {
      return this.encryptedPayload;
   }

   public S2CHandshakeChallengePacket() {
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeInt(this.encodedKeyIndex);
      nettyPacketBuf.writeByteArray(this.encryptedPayload);
      nettyPacketBuf.writeByteArray(this.encryptedAesKey);
   }



}
