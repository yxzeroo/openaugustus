package net.augustus.vendor.launcher.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64EncodingOutputStream extends FilterOutputStream {
   private final boolean doPadding;
   private boolean closed;
   private int b2;
   private int linepos;
   private int b0;
   private final byte[] newline;
   private int leftover;
   private final int linemax;
   private final char[] base64;
   private int b1;

   public Base64EncodingOutputStream(OutputStream out2, char[] chars, byte[] bytes, int n, boolean flag) {
      super(out2);
      this.leftover = (byte)0;
      this.closed = false;
      this.linepos = (byte)0;
      this.base64 = chars;
      this.newline = bytes;
      this.linemax = n;
      this.doPadding = flag;
   }

   @Override
   public void write(byte[] bytes, int n, int n2) throws IOException {
      if (this.closed) {
         throw new IOException("Stream is closed");
      } else if (n < 0 || n2 < 0 || n2 > bytes.length - n) {
         throw new ArrayIndexOutOfBoundsException();
      } else if (n2 != 0) {
         if (this.leftover != 0) {
            if (this.leftover == 1) {
               this.b1 = bytes[n++] & 255;
               if (--n2 == 0) {
                  this.leftover = this.leftover + 1;
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
            this.linepos = this.linepos + 4;
         }

         int n7 = n2 / 3;
         this.leftover = n2 - n7 * 3;

         while (n7-- > 0) {
            this.checkNewline();
            int n9 = (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255;
            this.out.write(this.base64[n9 >>> 18 & 63]);
            this.out.write(this.base64[n9 >>> 12 & 63]);
            this.out.write(this.base64[n9 >>> 6 & 63]);
            this.out.write(this.base64[n9 & 63]);
            this.linepos = this.linepos + 4;
         }

         if (this.leftover == 1) {
            this.b0 = bytes[n++] & 255;
         } else if (this.leftover == 2) {
            this.b0 = bytes[n++] & 255;
            this.b1 = bytes[n++] & 255;
         }
      }
   }

   private void checkNewline() throws IOException {
      if (this.linepos == this.linemax) {
         this.out.write(this.newline);
         this.linepos = (byte)0;
      }
   }

   @Override
   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         if (this.leftover == 1) {
            this.checkNewline();
            this.out.write(this.base64[this.b0 >> 2]);
            this.out.write(this.base64[this.b0 << 4 & 63]);
            if (this.doPadding) {
               this.out.write(61);
               this.out.write(61);
            }
         } else if (this.leftover == 2) {
            this.checkNewline();
            this.out.write(this.base64[this.b0 >> 2]);
            this.out.write(this.base64[this.b0 << 4 & 63 | this.b1 >> 4]);
            this.out.write(this.base64[this.b1 << 2 & 63]);
            if (this.doPadding) {
               this.out.write(61);
            }
         }

         this.leftover = (byte)0;
         this.out.close();
      }
   }

   @Override
   public void write(int n) throws IOException {
      this.write(new byte[]{(byte)(n & 0xFF)}, (byte)0, (byte)1);
   }

}
