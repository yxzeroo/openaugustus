package net.augustus.vendor.launcher.packet;


import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CIntegrityChallengePacket implements LauncherPacket {
   private byte[] challengeNonce;

   public S2CIntegrityChallengePacket(byte[] bytes, long l) {
      this.challengeNonce = bytes;
   }

   public byte[] getChallengeNonce() {
      return this.challengeNonce;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
   }

   public S2CIntegrityChallengePacket() {
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      this.challengeNonce = nettyPacketBuf.readByteArray();
   }



}
