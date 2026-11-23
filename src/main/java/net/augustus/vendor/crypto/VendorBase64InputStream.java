package net.augustus.vendor.crypto;

import java.io.IOException;
import java.io.InputStream;

public class VendorBase64InputStream extends InputStream {
   private final InputStream is;
   private byte[] sbBuf;
   private int nextin;
   private int bits = 0;
   private final boolean isMIME;
   private final int[] base64;
   private boolean eof;
   private int nextout;
   private boolean closed;

   @Override
   public int available() throws IOException {
      VendorAesHelper.getInvertedGuardFlag();
      if (!this.closed) {
         return this.is.available();
      } else {
         throw new IOException("Stream is closed");
      }
   }

   @Override
   public int read() throws IOException {
      int guardFlag = VendorAesHelper.getGuardFlag();
      byte b = 1;
      byte b2 = 0;
      byte[] sbBuf2 = this.sbBuf;
      return this.read(sbBuf2, b2, b) == -1 ? -1 : this.sbBuf[0] & 0xFF;
   }

   public VendorBase64InputStream(InputStream in, int[] ints, boolean flag) {
      this.nextin = 0;
      this.nextout = 0;
      this.eof = false;
      this.closed = false;
      this.sbBuf = new byte[1];
      this.is = in;
      this.base64 = ints;
      this.isMIME = flag;
   }

   @Override
   public int read(byte[] bytes, int n, int n2) throws IOException {
      int n3 = 0;
      VendorAesHelper.getInvertedGuardFlag();
      int n4 = 0;
      if (!this.closed) {
         if (this.eof && this.nextout < 0) {
            return -1;
         } else if (n >= 0 && n2 >= 0 && n2 <= bytes.length - n) {
            n3 = n;
            if (this.nextout >= 0) {
               do {
                  if (n2 == 0) {
                     return n - n3;
                  }

                  bytes[n++] = (byte)(this.bits >> this.nextout);
                  n2--;
                  this.nextout -= 8;
               } while (this.nextout >= 0);

               this.bits = 0;
            }

            while (n2 > 0) {
               n4 = this.is.read();
               if (n4 == -1) {
                  this.eof = true;
                  if (this.nextin != 18) {
                     if (this.nextin == 12) {
                        throw new IOException("Base64 stream has one un-decoded dangling byte.");
                     }

                     bytes[n++] = (byte)(this.bits >> 16);
                     n2--;
                     if (this.nextin == 0) {
                        if (n2 != 0) {
                           bytes[n++] = (byte)(this.bits >> 8);
                        } else {
                           this.bits >>= 8;
                           this.nextout = 0;
                        }
                     }
                  }

                  if (n != n3) {
                     return n - n3;
                  }

                  return -1;
               }

               if (n4 == 61) {
                  if (this.nextin != 18 && this.nextin != 12 && (this.nextin != 6 || this.is.read() == 61)) {
                     bytes[n++] = (byte)(this.bits >> 16);
                     n2--;
                     if (this.nextin == 0) {
                        if (n2 != 0) {
                           bytes[n++] = (byte)(this.bits >> 8);
                        } else {
                           this.bits >>= 8;
                           this.nextout = 0;
                        }
                     }

                     this.eof = true;
                     break;
                  }

                  throw new IOException("Illegal base64 ending sequence:" + this.nextin);
               }

               if ((n4 = this.base64[n4]) == -1) {
                  if (!this.isMIME) {
                     throw new IOException("Illegal base64 character " + Integer.toString(n4, 16));
                  }
               } else {
                  this.bits = this.bits | n4 << this.nextin;
                  if (this.nextin != 0) {
                     this.nextin -= 6;
                  } else {
                     this.nextin = 18;
                     this.nextout = 16;

                     while (this.nextout >= 0) {
                        bytes[n++] = (byte)(this.bits >> this.nextout);
                        n2--;
                        this.nextout -= 8;
                        if (n2 == 0 && this.nextout >= 0) {
                           return n - n3;
                        }
                     }

                     this.bits = 0;
                  }
               }
            }

            return n - n3;
         } else {
            throw new IndexOutOfBoundsException();
         }
      } else {
         throw new IOException("Stream is closed");
      }
   }

   @Override
   public void close() throws IOException {
      VendorAesHelper.getInvertedGuardFlag();
      if (!this.closed) {
         this.closed = true;
         this.is.close();
      }
   }

}
