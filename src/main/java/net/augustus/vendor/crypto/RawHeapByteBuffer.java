package net.augustus.vendor.crypto;


public class RawHeapByteBuffer extends RawByteBuffer {

   @Override
   public RawByteBuffer get(byte[] bytes, int n, int n2) {
      int n3 = 0;
      RawByteBuffer.checkBounds(n, n2, bytes.length);
      n3 = this.getPosition();
      if (n2 <= this.getLimit() - n3) {
         System.arraycopy(this.hb, this.ix(n3), bytes, n, n2);
         this.position(n3 + n2);
         return this;
      } else {
         throw new RuntimeException();
      }
   }

   public int ix(int n) {
      return n + this.offset;
   }

   @Override
   public RawByteBuffer put(byte b) {
      this.hb[this.ix(this.nextPutIndex())] = b;
      return this;
   }

   @Override
   public RawByteBuffer put(byte[] bytes, int n, int n2) {
      int n3 = 0;
      RawByteBuffer.checkBounds(n, n2, bytes.length);
      n3 = this.getPosition();
      if (n2 <= this.getLimit() - n3) {
         System.arraycopy(bytes, n, this.hb, this.ix(n3), n2);
         this.position(n3 + n2);
         return this;
      } else {
         throw new RuntimeException();
      }
   }

   public RawHeapByteBuffer(byte[] bytes, int n, int n2) {
      super(-1, n, n + n2, bytes.length, bytes, 0);
   }

   public RawHeapByteBuffer(int n, int n2) {
      super(-1, 0, n2, n, new byte[n], 0);
   }

   @Override
   public byte get() {
      return this.hb[this.ix(this.nextGetIndex())];
   }

}
