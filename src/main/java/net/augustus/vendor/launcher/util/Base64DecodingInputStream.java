package net.augustus.vendor.launcher.util;

import java.io.IOException;
import java.io.InputStream;

public class Base64DecodingInputStream extends InputStream {
   private int nextin;
   private final boolean isMIME;
   private final InputStream is;
   private int bits;
   private boolean eof;
   private final int[] base64;
   private boolean closed;
   private int nextout;
   private byte[] sbBuf;

   @Override
   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.is.close();
      }
   }

   @Override
   public int read(byte[] bytes, int n, int n2) throws IOException {
      if (this.closed) {
         throw new IOException("Stream is closed");
      } else if (this.eof && this.nextout < 0) {
         return -1;
      } else if (n >= 0 && n2 >= 0 && n2 <= bytes.length - n) {
         int n3 = n;
         if (this.nextout >= 0) {
            do {
               if (n2 == 0) {
                  return n - n3;
               }

               bytes[n++] = (byte)(this.bits >> this.nextout);
               n2--;
               this.nextout = this.nextout - 8;
            } while (this.nextout >= 0);

            this.bits = (byte)0;
         }

         while (n2 > 0) {
            int n5 = this.is.read();
            if (n5 == -1) {
               this.eof = true;
               if (this.nextin != 18) {
                  if (this.nextin == 12) {
                     throw new IOException("Base64 stream has one un-decoded dangling byte.");
                  }

                  bytes[n++] = (byte)(this.bits >> 16);
                  n2--;
                  if (this.nextin == 0) {
                     if (n2 == 0) {
                        this.bits = this.bits >> 8;
                        this.nextout = (byte)0;
                     } else {
                        bytes[n++] = (byte)(this.bits >> 8);
                     }
                  }
               }

               if (n == n3) {
                  return -1;
               }

               return n - n3;
            }

            if (n5 == 61) {
               if (this.nextin == 18 || this.nextin == 12 || this.nextin == 6 && this.is.read() != 61) {
                  throw new IOException("Illegal base64 ending sequence:" + this.nextin);
               }

               bytes[n++] = (byte)(this.bits >> 16);
               n2--;
               if (this.nextin == 0) {
                  if (n2 == 0) {
                     this.bits = this.bits >> 8;
                     this.nextout = (byte)0;
                  } else {
                     bytes[n++] = (byte)(this.bits >> 8);
                  }
               }

               this.eof = true;
               break;
            }

            if ((n5 = this.base64[n5]) == -1) {
               if (!this.isMIME) {
                  throw new IOException("Illegal base64 character " + Integer.toString(n5, 16));
               }
            } else {
               this.bits = this.bits | n5 << this.nextin;
               if (this.nextin != 0) {
                  this.nextin = this.nextin - 6;
               } else {
                  this.nextin = (byte)18;
                  this.nextout = (byte)16;

                  while (this.nextout >= 0) {
                     bytes[n++] = (byte)(this.bits >> this.nextout);
                     n2--;
                     this.nextout = this.nextout - 8;
                     if (n2 == 0 && this.nextout >= 0) {
                        return n - n3;
                     }
                  }

                  this.bits = (byte)0;
               }
            }
         }

         return n - n3;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   @Override
   public int available() throws IOException {
      if (this.closed) {
         throw new IOException("Stream is closed");
      } else {
         return this.is.available();
      }
   }

   public Base64DecodingInputStream(InputStream in, int[] ints, boolean flag) {
      this.bits = (byte)0;
      this.nextin = (byte)18;
      this.nextout = (byte)-8;
      this.eof = false;
      this.closed = false;
      this.sbBuf = new byte[1];
      this.is = in;
      this.base64 = ints;
      this.isMIME = flag;
   }

   @Override
   public int read() throws IOException {
      return this.read(this.sbBuf, (byte)0, (byte)1) == -1 ? -1 : this.sbBuf[0] & 0xFF;
   }

}
