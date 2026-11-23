package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class C2SHandshakeCompletePacket implements LauncherPacket {
   private byte[] encryptedVerification;
   private byte[] codeSourceHash;

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.encryptedVerification);
      nettyPacketBuf.writeByteArray(this.codeSourceHash);
   }

   public C2SHandshakeCompletePacket(byte[] bytes, byte[] bytes2) {
      this.encryptedVerification = bytes;
      this.codeSourceHash = bytes2;
   }

   public C2SHandshakeCompletePacket() {
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.encryptedVerification = nettyPacketBuf.readByteArray();
      this.codeSourceHash = nettyPacketBuf.readByteArray();
   }



}
