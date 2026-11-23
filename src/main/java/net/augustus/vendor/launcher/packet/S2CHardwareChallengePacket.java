package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CHardwareChallengePacket implements LauncherPacket {
   private byte[] encryptedAesKey;
   private byte[] encodedPublicKey;

   public byte[] getEncodedPublicKey() {
      return this.encodedPublicKey;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.encryptedAesKey);
      nettyPacketBuf.writeByteArray(this.encodedPublicKey);
   }

   public byte[] getEncryptedAesKey() {
      return this.encryptedAesKey;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.encryptedAesKey = nettyPacketBuf.readByteArray();
      this.encodedPublicKey = nettyPacketBuf.readByteArray();
   }




}
