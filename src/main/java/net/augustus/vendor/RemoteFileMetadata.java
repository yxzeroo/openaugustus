package net.augustus.vendor;

public class RemoteFileMetadata {
   private final String fileName;
   private final String absolutePath;
   private final long fileSize;
   private final boolean directory;

   public String getFileName() {
      return this.fileName;
   }

   public boolean isDirectory() {
      return this.directory;
   }

   public long getFileSize() {
      return this.fileSize;
   }

   public String getAbsolutePath() {
      return this.absolutePath;
   }

   public RemoteFileMetadata(String str, String str2, long l, boolean flag) {
      this.absolutePath = str;
      this.fileName = str2;
      this.fileSize = l;
      this.directory = flag;
   }
}
