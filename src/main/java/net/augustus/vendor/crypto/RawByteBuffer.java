package net.augustus.vendor.crypto;

public abstract class RawByteBuffer {
   public boolean isReadOnly;
   private int limit;
   public long address;
   private int capacity;
   private int mark;
   public int offset;
   private int position;
   public byte[] hb;

   public RawByteBuffer(int n, int n2, int n3, int n4) {
      super();
      this.mark = -1;
      this.position = 0;
      if (n4 < 0) {
         throw new RuntimeException();
      } else {
         this.capacity = n4;
         this.limit(n3);
         this.position(n2);
         if (n >= 0) {
            if (n > n2) {
               throw new RuntimeException();
            }

            this.mark = n;
         }
      }
   }

   public final int remaining() {
      int n = 0;
      n = this.limit - this.position;
      return n <= 0 ? 0 : n;
   }

   public abstract byte get();

   public final byte[] array() {
      if (this.hb != null) {
         if (!this.isReadOnly) {
            return this.hb;
         } else {
            throw new RuntimeException();
         }
      } else {
         throw new RuntimeException();
      }
   }

   public static RawByteBuffer wrap(byte[] bytes, int n, int n2) {

      try {
         return new RawHeapByteBuffer(bytes, n, n2);
      } catch (IllegalArgumentException e) {
         throw new RuntimeException();
      }
   }

   public final int getCapacity() {
      return this.capacity;
   }

   public RawByteBuffer put(byte[] bytes, int n, int n2) {
      boolean flag = false;
      int n3 = 0;
      int length = bytes.length;
      checkBounds(n, n2, length);
      if (n2 > this.remaining()) {
         throw new RuntimeException();
      } else {
         n3 = n + n2;

         for (int i = n; i < n3; i++) {
            RawByteBuffer rawByteBuffer = this;
            byte b = bytes[i];
            rawByteBuffer.put(b);
         }

         return this;
      }
   }

   public final int getPosition() {
      return this.position;
   }

   public static void checkBounds(int n, int n2, int n3) {
      if ((n | n2 | n + n2 | n3 - (n + n2)) < 0) {
         throw new RuntimeException();
      }
   }

   public static RawByteBuffer wrap(byte[] bytes) {
      int length = bytes.length;
      byte b = 0;
      return wrap(bytes, b, length);
   }

   public RawByteBuffer get(byte[] bytes, int n, int n2) {
      boolean flag = false;
      int n3 = 0;
      checkBounds(n, n2, bytes.length);
      if (n2 > this.remaining()) {
         throw new RuntimeException();
      } else {
         n3 = n + n2;

         for (int i = n; i < n3; i++) {
            bytes[i] = this.get();
         }

         return this;
      }
   }

   public final RawByteBuffer put2(byte[] bytes) {
      int length = bytes.length;
      byte b = 0;
      return this.put(bytes, b, length);
   }

   public RawByteBuffer(int n, int n2, int n3, int n4, byte[] bytes, int n5) {
      this(n, n2, n3, n4);
      this.hb = bytes;
      this.offset = n5;
   }

   public final RawByteBuffer position(int n) {
      if (n <= this.limit && n >= 0) {
         if (this.mark > n) {
            this.mark = -1;
         }

         this.position = n;
         return this;
      } else {
         throw new RuntimeException();
      }
   }

   public static RawHeapByteBuffer allocate(int n) {
      if (n >= 0) {
         return new RawHeapByteBuffer(n, n);
      } else {
         throw new RuntimeException();
      }
   }

   public abstract RawByteBuffer put(byte b);

   public final RawByteBuffer limit(int n) {
      if (n <= this.capacity && n >= 0) {
         this.limit = n;
         if (this.position > n) {
            this.position = n;
         }

         if (this.mark > n) {
            this.mark = -1;
         }

         return this;
      } else {
         throw new RuntimeException();
      }
   }

   public final int nextPutIndex() {
      int n = 0;
      n = this.position;
      if (n < this.limit) {
         this.position = n + 1;
         return n;
      } else {
         throw new RuntimeException();
      }
   }

   public final int nextGetIndex() {
      int n = 0;
      n = this.position;
      if (n < this.limit) {
         this.position = n + 1;
         return n;
      } else {
         throw new RuntimeException();
      }
   }

   public RawByteBuffer get(byte[] bytes) {
      int length = bytes.length;
      byte b = 0;
      return this.get(bytes, b, length);
   }

   public final int getLimit() {
      return this.limit;
   }

   public final RawByteBuffer mark() {
      this.mark = this.position;
      return this;
   }




}
