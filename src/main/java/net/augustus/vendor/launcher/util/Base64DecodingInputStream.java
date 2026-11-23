package net.augustus.vendor.launcher.util;

import net.augustus.vendor.launcher.crypto.Sha256Digest;
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
      int[] flowState = Sha256Digest.getFlowState();
      if (!this.closed) {
         boolean flag = true;
         this.closed = flag;
         this.is.close();
      }
   }

   @Override
   public int read(byte[] bytes, int n, int n2) throws IOException {
      int[] flowState = Sha256Digest.getFlowState();
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
               int n4 = this.nextout - 8;
               this.nextout = n4;
            } while (this.nextout >= 0);

            byte b = 0;
            this.bits = b;
         }

         while (n2 > 0) {
            int n5 = this.is.read();
            if (n5 == -1) {
               boolean flag = true;
               this.eof = flag;
               if (this.nextin != 18) {
                  if (this.nextin == 12) {
                     throw new IOException("Base64 stream has one un-decoded dangling byte.");
                  }

                  bytes[n++] = (byte)(this.bits >> 16);
                  n2--;
                  if (this.nextin == 0) {
                     if (n2 == 0) {
                        int n6 = this.bits >> 8;
                        this.bits = n6;
                        byte b2 = 0;
                        this.nextout = b2;
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
                     int n7 = this.bits >> 8;
                     this.bits = n7;
                     byte b3 = 0;
                     this.nextout = b3;
                  } else {
                     bytes[n++] = (byte)(this.bits >> 8);
                  }
               }

               boolean flag2 = true;
               this.eof = flag2;
               break;
            }

            if ((n5 = this.base64[n5]) == -1) {
               if (!this.isMIME) {
                  throw new IOException("Illegal base64 character " + Integer.toString(n5, 16));
               }
            } else {
               int n8 = this.bits | n5 << this.nextin;
               this.bits = n8;
               if (this.nextin != 0) {
                  int n9 = this.nextin - 6;
                  this.nextin = n9;
               } else {
                  byte b4 = 18;
                  this.nextin = b4;
                  byte b5 = 16;
                  this.nextout = b5;

                  while (this.nextout >= 0) {
                     bytes[n++] = (byte)(this.bits >> this.nextout);
                     n2--;
                     int n10 = this.nextout - 8;
                     this.nextout = n10;
                     if (n2 == 0 && this.nextout >= 0) {
                        return n - n3;
                     }
                  }

                  byte b6 = 0;
                  this.bits = b6;
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
      int[] flowState = Sha256Digest.getFlowState();
      if (this.closed) {
         throw new IOException("Stream is closed");
      } else {
         return this.is.available();
      }
   }

   public Base64DecodingInputStream(InputStream in, int[] ints, boolean flag) {
      byte b = 0;
      this.bits = b;
      byte b2 = 18;
      this.nextin = b2;
      byte b3 = -8;
      this.nextout = b3;
      boolean flag2 = false;
      this.eof = flag2;
      boolean flag3 = false;
      this.closed = flag3;
      byte[] bytes = new byte[1];
      this.sbBuf = bytes;
      this.is = in;
      this.base64 = ints;
      this.isMIME = flag;
   }

   @Override
   public int read() throws IOException {
      int[] flowState = Sha256Digest.getFlowState();
      byte b = 1;
      byte b2 = 0;
      byte[] sbBuf2 = this.sbBuf;
      return this.read(sbBuf2, b2, b) == -1 ? -1 : this.sbBuf[0] & 0xFF;
   }

}
