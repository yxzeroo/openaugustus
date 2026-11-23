package net.augustus.vendor.launcher.packet;



import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CClientArchiveChunkPacket implements LauncherPacket {
   private String clientName;
   private byte[] encryptedArchiveKey;
   private int totalSize;
   private byte[] chunkData;

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      byte b = 3;
      nettyPacketBuf.writeInt(this.totalSize);

      while (true) {
         while (b > 2) {
            if (b > 3) {
               return;
            }

            nettyPacketBuf.writeByteArray(this.encryptedArchiveKey);
            nettyPacketBuf.writeNullTerminatedString(this.clientName);
         }

         nettyPacketBuf.writeByteArray(this.chunkData);
      }
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      byte b = 2;
      S2CClientArchiveChunkPacket packet = this;
      int n = nettyPacketBuf.readInt();
      packet.totalSize = n;

      while (true) {
         while (b > 2) {
            if (3 < b) {
               packet = this;
               String str = nettyPacketBuf.readNullTerminatedString();
               packet.clientName = str;
               return;
            }

            packet = this;
            byte[] bytes = nettyPacketBuf.readByteArray();
            packet.chunkData = bytes;
         }

         packet = this;
         byte[] bytes2 = nettyPacketBuf.readByteArray();
         packet.encryptedArchiveKey = bytes2;
      }
   }

   public S2CClientArchiveChunkPacket(long l, int n, byte[] bytes, byte[] bytes2, String str) {
      this.totalSize = n;
      this.chunkData = bytes;
      this.encryptedArchiveKey = bytes2;
      this.clientName = str;
   }

   public S2CClientArchiveChunkPacket() {
   }

   public byte[] getEncryptedArchiveKey() {
      return this.encryptedArchiveKey;
   }

   public String getClientName() {
      return this.clientName;
   }

   public int getTotalSize() {
      return this.totalSize;
   }

   public byte[] getChunkData() {
      return this.chunkData;
   }


}
