package net.augustus.vendor.launcher.util;

public abstract class ShadedByteBuffer {
   public boolean isReadOnly;
   public long address;
   private int mark;
   public int offset;
   private int capacity;
   private int position;
   private int limit;
   public byte[] hb;

   public final byte[] array() {
      if (this.hb == null) {
         throw new RuntimeException();
      } else if (this.isReadOnly) {
         throw new RuntimeException();
      } else {
         return this.hb;
      }
   }

   public ShadedByteBuffer get(byte[] bytes) {
      return this.get(bytes, (byte)0, bytes.length);
   }

   public abstract ShadedByteBuffer put(byte b);

   public final int capacity(long l) {
      return this.capacity;
   }

   public abstract byte get();

   public static ShadedByteBuffer wrap(byte[] bytes) {
      return wrap(bytes, (byte)0, bytes.length);
   }

   public ShadedByteBuffer(int n, int n2, int n3, int n4, byte[] bytes, int n5) {
       this(n, n2, n3, n4);
      this.hb = bytes;
      this.offset = n5;
   }

   public final ShadedByteBuffer mark(long l) {
      this.mark = this.position;
      return this;
   }

   public final ShadedByteBuffer put2(byte[] bytes) {
      return this.put(bytes, (byte)0, bytes.length);
   }

   public ShadedByteBuffer(int n, int n2, int n3, int n4) {
       super();
      this.mark = (byte)-1;
      this.position = (byte)0;
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

   public final int getLimit() {
      return this.limit;
   }

   public static void checkBounds(int n, int n2, int n3) {
      if ((n | n2 | n + n2 | n3 - (n + n2)) < 0) {
         throw new RuntimeException();
      }
   }

   public ShadedByteBuffer get(byte[] bytes, int n, int n2) {
      checkBounds(n, n2, bytes.length);
      if (n2 > this.remaining()) {
         throw new RuntimeException();
      } else {
         int n3 = n + n2;

         for (int i = n; i < n3; i++) {
            bytes[i] = this.get();
         }

         return this;
      }
   }

   public final ShadedByteBuffer limit(int n) {
      if (n <= this.capacity && n >= 0) {
         this.limit = n;
         if (this.position > n) {
            this.position = n;
         }

         if (this.mark > n) {
            this.mark = (byte)-1;
         }

         return this;
      } else {
         throw new RuntimeException();
      }
   }

   public static ShadedByteBuffer wrap(byte[] bytes, int n, int n2) {

      try {
         return new ShadedHeapByteBuffer(bytes, n, n2);
      } catch (IllegalArgumentException e) {
         throw new RuntimeException();
      }
   }

   public final int nextPutIndex() {
      int position2 = this.position;
      if (position2 >= this.limit) {
         throw new RuntimeException();
      } else {
         this.position = position2 + 1;
         return position2;
      }
   }

   public final int getPosition() {
      return this.position;
   }

   public final int nextGetIndex() {
      int position2 = this.position;
      if (position2 >= this.limit) {
         throw new RuntimeException();
      } else {
         this.position = position2 + 1;
         return position2;
      }
   }

   public final ShadedByteBuffer position(int n) {
      if (n <= this.limit && n >= 0) {
         if (this.mark > n) {
            this.mark = (byte)-1;
         }

         this.position = n;
         return this;
      } else {
         throw new RuntimeException();
      }
   }

   public ShadedByteBuffer put(byte[] bytes, int n, int n2) {
      checkBounds(n, n2, bytes.length);
      if (n2 > this.remaining()) {
         throw new RuntimeException();
      } else {
         int n3 = n + n2;

         for (int i = n; i < n3; i++) {
            this.put(bytes[i]);
         }

         return this;
      }
   }

   public static ShadedHeapByteBuffer allocate(int n) {
      if (n < 0) {
         throw new RuntimeException();
      } else {
         return new ShadedHeapByteBuffer(n, n);
      }
   }

   public final int remaining() {
      int n = this.limit - this.position;
      return n > 0 ? n : 0;
   }



}
