package net.augustus.vendor.crypto;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CustomBase64OutputStream extends FilterOutputStream {
   private int linepos;
   private int b2;
   private boolean closed;
   private int b1;
   private final boolean doPadding;
   private final int linemax;
   private int b0;
   private final byte[] newline;
   private int leftover = 0;
   private final char[] base64;

   public CustomBase64OutputStream(OutputStream out2, char[] chars, byte[] bytes, int n, boolean flag) {
      super(out2);
      this.closed = false;
      this.linepos = 0;
      this.base64 = chars;
      this.newline = bytes;
      this.linemax = n;
      this.doPadding = flag;
   }

   @Override
   public void write(int n) throws IOException {
      Object obj = null;
      obj = new byte[1];
      ((byte[])obj)[0] = (byte)(n & 0xFF);
      CustomBase64OutputStream customBase64OutputStream = this;
      byte[] bytes = (byte[])obj;
      byte b = 1;
      byte b3 = 0;
      byte[] bytes2 = bytes;
      customBase64OutputStream.write(bytes2, b3, b);
   }

   @Override
   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         if (this.leftover != 1) {
            if (this.leftover == 2) {
               this.checkNewline();
               this.out.write(this.base64[this.b0 >> 2]);
               this.out.write(this.base64[this.b0 << 4 & 63 | this.b1 >> 4]);
               this.out.write(this.base64[this.b1 << 2 & 63]);
               if (this.doPadding) {
                  this.out.write(61);
               }
            }
         } else {
            this.checkNewline();
            this.out.write(this.base64[this.b0 >> 2]);
            this.out.write(this.base64[this.b0 << 4 & 63]);
            if (this.doPadding) {
               this.out.write(61);
               this.out.write(61);
            }
         }

         this.leftover = 0;
         this.out.close();
      }
   }

   @Override
   public void write(byte[] bytes, int n, int n2) throws IOException {
      int n3 = 0;
      int n4 = 0;
      if (this.closed) {
         throw new IOException("Stream is closed");
      } else if (n < 0 || n2 < 0 || n2 > bytes.length - n) {
         throw new ArrayIndexOutOfBoundsException();
      } else if (n2 != 0) {
         if (this.leftover != 0) {
            if (this.leftover == 1) {
               this.b1 = bytes[n++] & 255;
               if (--n2 == 0) {
                  this.leftover++;
                  return;
               }
            }

            this.b2 = bytes[n++] & 255;
            n2--;
            this.checkNewline();
            this.out.write(this.base64[this.b0 >> 2]);
            this.out.write(this.base64[this.b0 << 4 & 63 | this.b1 >> 4]);
            this.out.write(this.base64[this.b1 << 2 & 63 | this.b2 >> 6]);
            this.out.write(this.base64[this.b2 & 63]);
            this.linepos += 4;
         }

         n4 = n2 / 3;

         for (this.leftover = n2 - n4 * 3; n4-- > 0; this.linepos += 4) {
            this.checkNewline();
            n3 = (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255;
            this.out.write(this.base64[n3 >>> 18 & 63]);
            this.out.write(this.base64[n3 >>> 12 & 63]);
            this.out.write(this.base64[n3 >>> 6 & 63]);
            this.out.write(this.base64[n3 & 63]);
         }

         if (this.leftover != 1) {
            if (this.leftover == 2) {
               this.b0 = bytes[n++] & 255;
               this.b1 = bytes[n++] & 255;
            }
         } else {
            this.b0 = bytes[n++] & 255;
         }
      }
   }

   private void checkNewline() throws IOException {
      if (this.linepos == this.linemax) {
         this.out.write(this.newline);
         this.linepos = 0;
      }
   }

}
