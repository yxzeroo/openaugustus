package net.augustus.vendor.launcher.util;

public class ShadedHeapByteBuffer extends ShadedByteBuffer {

   @Override
   public ShadedByteBuffer put(byte[] bytes, int n, int n2) {
      ShadedByteBuffer.checkBounds(n, n2, bytes.length);
      int position2 = this.getPosition();
      if (n2 > this.getLimit() - position2) {
         throw new RuntimeException();
      } else {
         System.arraycopy(bytes, n, this.hb, this.ix(position2), n2);
         this.position(position2 + n2);
         return this;
      }
   }

   public int ix(int n) {
      return n + this.offset;
   }

   @Override
   public ShadedByteBuffer put(byte b) {
      this.hb[this.ix(this.nextPutIndex())] = b;
      return this;
   }

   public ShadedHeapByteBuffer(int n, int n2) {
       super(-1, 0, n2, n, new byte[n], 0);
   }

   @Override
   public ShadedByteBuffer get(byte[] bytes, int n, int n2) {
      ShadedByteBuffer.checkBounds(n, n2, bytes.length);
      int position2 = this.getPosition();
      if (n2 > this.getLimit() - position2) {
         throw new RuntimeException();
      } else {
         System.arraycopy(this.hb, this.ix(position2), bytes, n, n2);
         this.position(position2 + n2);
         return this;
      }
   }

   @Override
   public byte get() {
      return this.hb[this.ix(this.nextGetIndex())];
   }

   public ShadedHeapByteBuffer(byte[] bytes, int n, int n2) {
       super(-1, n, n + n2, bytes.length, bytes, 0);
   }
}
