package net.augustus.vendor.launcher.packet;



import net.augustus.vendor.launcher.NettyPacketBuf;
public class S2CResourceChunkPacket implements LauncherPacket {
   private byte[] resourcesChunk;
   private byte[] nativesChunk;
   private int nativesTotalSize;
   private String clientName;
   private int resourcesTotalSize;

   @Override
   public void write(NettyPacketBuf nettyPacketBuf) throws Exception {
      byte b = 5;
      nettyPacketBuf.writeNullTerminatedString(this.clientName);
      nettyPacketBuf.writeInt(this.resourcesTotalSize);

      while (true) {
         while (2 < b) {
            if (b >= 4) {
               if (4 < b) {
                  return;
               }

               nettyPacketBuf.writeInt(this.nativesTotalSize);
            } else {
               nettyPacketBuf.writeByteArray(this.resourcesChunk);
            }
         }

         nettyPacketBuf.writeByteArray(this.nativesChunk);
      }
   }

   public byte[] getNativesChunk() {
      return this.nativesChunk;
   }

   @Override
   public void read(NettyPacketBuf nettyPacketBuf) throws Exception {
      byte b = 2;

      while (true) {
         while (2 < b) {
            if (b < 4) {
               return;
            }

            if (b <= 4) {
               this.clientName = nettyPacketBuf.readNullTerminatedString();
            } else {
               S2CResourceChunkPacket packet2 = this;
               packet2.nativesTotalSize = nettyPacketBuf.readInt();
               packet2 = this;
               packet2.resourcesChunk = nettyPacketBuf.readByteArray();
               packet2 = this;
               packet2.nativesChunk = nettyPacketBuf.readByteArray();
            }
         }

         this.resourcesTotalSize = nettyPacketBuf.readInt();
      }
   }

   public int getNativesTotalSize() {
      return this.nativesTotalSize;
   }

   public S2CResourceChunkPacket() {
   }

   public int getResourcesTotalSize() {
      return this.resourcesTotalSize;
   }

   public S2CResourceChunkPacket(String str, int n, int n2, byte[] bytes, long l, byte[] bytes2) {
      this.clientName = str;
      this.resourcesTotalSize = n;
      this.nativesTotalSize = n2;
      this.resourcesChunk = bytes2;
      this.nativesChunk = bytes;
   }

   public byte[] getResourcesChunk() {
      return this.resourcesChunk;
   }

   public String getClientName() {
      return this.clientName;
   }


}
