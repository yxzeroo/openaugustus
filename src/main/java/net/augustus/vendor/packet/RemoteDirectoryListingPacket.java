package net.augustus.vendor.packet;

import java.util.ArrayList;
import net.augustus.vendor.RemoteFileMetadata;
import net.augustus.vendor.network.VendorPacketBuffer;

public class RemoteDirectoryListingPacket implements VendorPacket {
   private byte[] sessionToken;
   private final ArrayList fileEntries;
   private int requestId;

   public ArrayList getFileEntries() {
      return this.fileEntries;
   }

   public RemoteDirectoryListingPacket(ArrayList list, int n, byte[] bytes) {
      this.fileEntries = list;
      this.requestId = n;
      this.sessionToken = bytes;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.requestId = buf.readInt();
      int n = buf.readInt();
      for (int i = 0; i < n; i++) {
         this.fileEntries.add(new RemoteFileMetadata(buf.readUtfString(), buf.readUtfString(), buf.readLong(), buf.readBoolean()));
      }
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeInt(this.requestId);
      buf.writeInt(this.fileEntries.size());
      for (Object obj : this.fileEntries) {
         RemoteFileMetadata remoteFileMetadata = (RemoteFileMetadata)obj;
         buf.writeUtfString(remoteFileMetadata.getAbsolutePath());
         buf.writeUtfString(remoteFileMetadata.getFileName());
         buf.writeLong(remoteFileMetadata.getFileSize());
         buf.writeBoolean(remoteFileMetadata.isDirectory());
      }
   }
}
