package net.augustus.vendor.launcher.util;

import net.augustus.vendor.launcher.crypto.Sha256Digest;
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
      byte b = 0;
      this.leftover = b;
      boolean flag2 = false;
      this.closed = flag2;
      byte b3 = 0;
      this.linepos = b3;
      this.base64 = chars;
      this.newline = bytes;
      this.linemax = n;
      this.doPadding = flag;
   }

   @Override
   public void write(byte[] bytes, int n, int n2) throws IOException {
      int[] flowState = Sha256Digest.getFlowState();
      if (this.closed) {
         throw new IOException("Stream is closed");
      } else if (n < 0 || n2 < 0 || n2 > bytes.length - n) {
         throw new ArrayIndexOutOfBoundsException();
      } else if (n2 != 0) {
         if (this.leftover != 0) {
            if (this.leftover == 1) {
               int n3 = bytes[n++] & 255;
               this.b1 = n3;
               if (--n2 == 0) {
                  int n4 = this.leftover + 1;
                  this.leftover = n4;
                  return;
               }
            }

            int n5 = bytes[n++] & 255;
            this.b2 = n5;
            n2--;
            this.checkNewline();
            this.out.write(this.base64[this.b0 >> 2]);
            this.out.write(this.base64[this.b0 << 4 & 63 | this.b1 >> 4]);
            this.out.write(this.base64[this.b1 << 2 & 63 | this.b2 >> 6]);
            this.out.write(this.base64[this.b2 & 63]);
            int n6 = this.linepos + 4;
            this.linepos = n6;
         }

         int n7 = n2 / 3;
         int n8 = n2 - n7 * 3;
         this.leftover = n8;

         while (n7-- > 0) {
            this.checkNewline();
            int n9 = (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255;
            this.out.write(this.base64[n9 >>> 18 & 63]);
            this.out.write(this.base64[n9 >>> 12 & 63]);
            this.out.write(this.base64[n9 >>> 6 & 63]);
            this.out.write(this.base64[n9 & 63]);
            int n10 = this.linepos + 4;
            this.linepos = n10;
         }

         if (this.leftover == 1) {
            int n11 = bytes[n++] & 255;
            this.b0 = n11;
         } else if (this.leftover == 2) {
            int n12 = bytes[n++] & 255;
            this.b0 = n12;
            int n13 = bytes[n++] & 255;
            this.b1 = n13;
         }
      }
   }

   private void checkNewline() throws IOException {
      int[] flowState = Sha256Digest.getFlowState();
      if (this.linepos == this.linemax) {
         this.out.write(this.newline);
         byte b = 0;
         this.linepos = b;
      }
   }

   @Override
   public void close() throws IOException {
      int[] flowState = Sha256Digest.getFlowState();
      if (!this.closed) {
         boolean flag = true;
         this.closed = flag;
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

         byte b = 0;
         this.leftover = b;
         this.out.close();
      }
   }

   @Override
   public void write(int n) throws IOException {
      byte[] bytes = new byte[]{(byte)(n & 0xFF)};
      byte b = 1;
      byte b3 = 0;
      this.write(bytes, b3, b);
   }

}
