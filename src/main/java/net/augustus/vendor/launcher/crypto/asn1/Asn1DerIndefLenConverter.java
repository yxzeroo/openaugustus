package net.augustus.vendor.launcher.crypto.asn1;

import java.io.IOException;
import java.util.ArrayList;

public class Asn1DerIndefLenConverter {
   private ArrayList<Object> ndefsList;
   private static final int TAG_MASK = 0;
   private static final int FORM_MASK = 0;
   private int dataPos;
   private int unresolved;
   private static final int CLASS_MASK = 0;
   private int newDataPos;
   private static final int LEN_LONG = 0;
   private static final int LEN_MASK = 0;
   private int numOfTotalLenBytes;
   private static final int SKIP_EOC_BYTES = 0;
   private int index;
   private byte[] data;
   private int dataSize;
   private byte[] newData;

   private void writeLengthAndValue() throws IOException {
      if (this.dataPos != this.dataSize) {
         int n = 0;
         byte[] data2 = this.data;
         int dataPos2 = this.dataPos;
         this.dataPos = this.dataPos + 1;
         int n3 = data2[dataPos2] & 255;
         if (isIndefinite(n3)) {
            ArrayList ndefsList2 = this.ndefsList;
            int index2 = this.index;
            this.index = this.index + 1;
            byte[] bytes = (byte[])ndefsList2.get(index2);
            System.arraycopy(bytes, 0, this.newData, this.newDataPos, bytes.length);
            this.newDataPos = this.newDataPos + bytes.length;
         } else {
            if (!isLongForm(n3)) {
               n = n3 & 127;
            } else {
               n3 &= 127;

               for (int i = 0; i < n3; i++) {
                  int n6 = n << 8;
                  byte[] data3 = this.data;
                  int dataPos3 = this.dataPos;
                  this.dataPos = this.dataPos + 1;
                  n = n6 + (data3[dataPos3] & 255);
               }

               if (n < 0) {
                  throw new IOException("Invalid length bytes");
               }
            }

            this.writeLength(n);
            this.writeValue(n);
         }
      }
   }

   public static boolean isIndefinite(int n) {
      return isLongForm(n) && (n & 127) == 0;
   }

   private void writeTag() {
      if (this.dataPos != this.dataSize) {
         byte[] data2 = this.data;
         int dataPos2 = this.dataPos;
         this.dataPos = this.dataPos + 1;
         byte b = data2[dataPos2];
         if (this.isEOC(b) && this.data[this.dataPos] == 0) {
            this.dataPos = this.dataPos + 1;
            this.writeTag();
         } else {
            data2 = this.newData;
            int newDataPos2 = this.newDataPos;
            this.newDataPos = this.newDataPos + 1;
            data2[newDataPos2] = b;
         }
      }
   }

   private void writeValue(int n) {

      for (int i = 0; i < n; i++) {
         byte[] newData2 = this.newData;
         int newDataPos2 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         byte[] data2 = this.data;
         int dataPos2 = this.dataPos;
         this.dataPos = this.dataPos + 1;
         newData2[newDataPos2] = data2[dataPos2];
      }
   }

   public static boolean isLongForm(int n) {
      return (n & 128) == 128;
   }

   private void parseValue(int n) {
      this.dataPos = this.dataPos + n;
   }

   private boolean isEOC(int n) {
      return (n & 31) == 0 && (n & 32) == 0 && (n & 192) == 0;
   }

   private int getNumOfLenBytes(char ch, int n, int n2, char ch2) {
      int n3;
      if (n < 0) {
         n3 = 1;
         if (ch2 > 0) {
            return 1;
         }
      } else {
         n3 = n;
      }

      if (n3 < 0) {
         n3 = 2;
         if (n2 > 0) {
            return 2;
         }
      } else {
         n3 = n;
      }

      if (n3 < 0) {
         n3 = 3;
         if (ch2 > 0) {
            return 3;
         }
      } else {
         n3 = n;
      }

      byte b;
      if (n3 < 0) {
         b = 4;
      } else {
         b = 5;
      }

      return b;
   }

   private void parseTag() throws IOException {
      if (this.dataPos != this.dataSize) {
         try {
            if (this.isEOC(this.data[this.dataPos]) && this.data[this.dataPos + 1] == 0) {
               int n = 0;
               Object obj = null;

               int n2;
               for (n2 = this.ndefsList.size() - 1; n2 >= 0; n2--) {
                  obj = this.ndefsList.get(n2);
                  if (obj instanceof Integer) {
                     break;
                  }

                  n += ((byte[])obj).length - 3;
               }

               if (n2 < 0) {
                  throw new IOException("EOC does not have matching indefinite-length tag");
               }

               byte[] lengthBytes = this.getLengthBytes(this.dataPos - (Integer)obj + n);
               this.ndefsList.set(n2, lengthBytes);
               this.unresolved = this.unresolved - 1;
               this.numOfTotalLenBytes = this.numOfTotalLenBytes + (lengthBytes.length - 3);
            }

            this.dataPos = this.dataPos + 1;
         } catch (IndexOutOfBoundsException e) {
            throw new IOException(e);
         }
      }
   }

   public Asn1DerIndefLenConverter() {
      this.unresolved = (byte)0;
      this.ndefsList = new ArrayList();
      this.numOfTotalLenBytes = (byte)0;
   }

   private byte[] getLengthBytes(int n) {
      byte b = 0;
      byte[] bytes;
      if (n < 128) {
         bytes = new byte[1];
         int n2 = b + 1;
         bytes[b] = (byte)n;
      } else if (n < 256) {
         bytes = new byte[2];
         int n3 = b + 1;
         bytes[b] = -127;
         bytes[n3++] = (byte)n;
      } else if (n < 65536) {
         bytes = new byte[3];
         int n4 = b + 1;
         bytes[b] = -126;
         bytes[n4++] = (byte)(n >> 8);
         bytes[n4++] = (byte)n;
      } else if (n < 16777216) {
         bytes = new byte[4];
         int n5 = b + 1;
         bytes[b] = -125;
         bytes[n5++] = (byte)(n >> 16);
         bytes[n5++] = (byte)(n >> 8);
         bytes[n5++] = (byte)n;
      } else {
         bytes = new byte[5];
         int n6 = b + 1;
         bytes[b] = -124;
         bytes[n6++] = (byte)(n >> 24);
         bytes[n6++] = (byte)(n >> 16);
         bytes[n6++] = (byte)(n >> 8);
         bytes[n6++] = (byte)n;
      }

      return bytes;
   }

   public byte[] convert(byte[] bytes) throws IOException {
      this.data = bytes;
      this.dataPos = (byte)0;
      this.index = (byte)0;
      this.dataSize = this.data.length;
      int n = 0;

      while (this.dataPos < this.dataSize) {
         this.parseTag();
         this.parseValue(this.parseLength());
         if (this.unresolved == 0) {
            n = this.dataSize - this.dataPos;
            this.dataSize = this.dataPos;
            break;
         }
      }

      if (this.unresolved != 0) {
         throw new IOException("not all indef len BER resolved");
      } else {
         this.newData = new byte[this.dataSize + this.numOfTotalLenBytes + n];
         this.dataPos = (byte)0;
         this.newDataPos = (byte)0;
         this.index = (byte)0;

         while (this.dataPos < this.dataSize) {
            this.writeTag();
            this.writeLengthAndValue();
         }

         System.arraycopy(bytes, this.dataSize, this.newData, this.dataSize + this.numOfTotalLenBytes, n);
         return this.newData;
      }
   }

   private void writeLength(int n) {
      if (n < 128) {
         byte[] newData2 = this.newData;
         int newDataPos2 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData2[newDataPos2] = (byte)n;
      } else if (n < 256) {
         byte[] newData3 = this.newData;
         int newDataPos3 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData3[newDataPos3] = -127;
         newData3 = this.newData;
         newDataPos3 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData3[newDataPos3] = (byte)n;
      } else if (n < 65536) {
         byte[] newData4 = this.newData;
         int newDataPos4 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData4[newDataPos4] = -126;
         newData4 = this.newData;
         newDataPos4 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData4[newDataPos4] = (byte)(n >> 8);
         newData4 = this.newData;
         newDataPos4 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData4[newDataPos4] = (byte)n;
      } else if (n < 16777216) {
         byte[] newData5 = this.newData;
         int newDataPos5 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData5[newDataPos5] = -125;
         newData5 = this.newData;
         newDataPos5 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData5[newDataPos5] = (byte)(n >> 16);
         newData5 = this.newData;
         newDataPos5 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData5[newDataPos5] = (byte)(n >> 8);
         newData5 = this.newData;
         newDataPos5 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData5[newDataPos5] = (byte)n;
      } else {
         byte[] newData6 = this.newData;
         int newDataPos6 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData6[newDataPos6] = -124;
         newData6 = this.newData;
         newDataPos6 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData6[newDataPos6] = (byte)(n >> 24);
         newData6 = this.newData;
         newDataPos6 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData6[newDataPos6] = (byte)(n >> 16);
         newData6 = this.newData;
         newDataPos6 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData6[newDataPos6] = (byte)(n >> 8);
         newData6 = this.newData;
         newDataPos6 = this.newDataPos;
         this.newDataPos = this.newDataPos + 1;
         newData6[newDataPos6] = (byte)n;
      }
   }

   private int parseLength() throws IOException {
      int n = 0;
      if (this.dataPos == this.dataSize) {
         return n;
      } else {
         byte[] data2 = this.data;
         int dataPos2 = this.dataPos;
         this.dataPos = this.dataPos + 1;
         int n3 = data2[dataPos2] & 255;
         if (isIndefinite(n3)) {
            this.ndefsList.add(new Integer(this.dataPos));
            this.unresolved = this.unresolved + 1;
            return n;
         } else {
            if (isLongForm(n3)) {
               n3 &= 127;
               if (n3 > 4) {
                  throw new IOException("Too much data");
               }

               if (this.dataSize - this.dataPos < n3 + 1) {
                  throw new IOException("Too little data");
               }

               for (int i = 0; i < n3; i++) {
                  int n5 = n << 8;
                  byte[] data3 = this.data;
                  int dataPos3 = this.dataPos;
                  this.dataPos = this.dataPos + 1;
                  n = n5 + (data3[dataPos3] & 255);
               }

               if (n < 0) {
                  throw new IOException("Invalid length bytes");
               }
            } else {
               n = n3 & 127;
            }

            return n;
         }
      }
   }

}
