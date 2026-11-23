package net.augustus.vendor.launcher.packet;

import net.augustus.vendor.launcher.NettyPacketBuf;
import java.util.Map;

public class C2SIntegrityProofPacket implements LauncherPacket {
   private byte[] challengeProofHash;
   private Map<String, byte[]> classHashes;
   private byte[] sessionToken;

   public C2SIntegrityProofPacket() {
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
   }

   @SuppressWarnings("unchecked")
   public C2SIntegrityProofPacket(byte[] bytes, byte[] bytes2, Map map) {
      this.sessionToken = bytes;
      this.challengeProofHash = bytes2;
      this.classHashes = (Map<String, byte[]>)map;
   }

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      nettyPacketBuf.writeByteArray(this.sessionToken);
      nettyPacketBuf.writeByteArray(this.challengeProofHash);
      nettyPacketBuf.writeInt(this.classHashes.size());
      for (Map.Entry<String, byte[]> entry : this.classHashes.entrySet()) {
         nettyPacketBuf.writeUtf8String(entry.getKey());
         nettyPacketBuf.writeByteArray(entry.getValue());
      }
   }



}
