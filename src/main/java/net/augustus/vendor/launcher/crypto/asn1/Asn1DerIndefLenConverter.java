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
         int n2 = this.dataPos + 1;
         this.dataPos = n2;
         int n3 = data2[dataPos2] & 255;
         if (isIndefinite(n3)) {
            ArrayList ndefsList2 = this.ndefsList;
            int index2 = this.index;
            int n4 = this.index + 1;
            this.index = n4;
            byte[] bytes = (byte[])ndefsList2.get(index2);
            System.arraycopy(bytes, 0, this.newData, this.newDataPos, bytes.length);
            int n5 = this.newDataPos + bytes.length;
            this.newDataPos = n5;
         } else {
            if (!isLongForm(n3)) {
               n = n3 & 127;
            } else {
               n3 &= 127;

               for (int i = 0; i < n3; i++) {
                  int n6 = n << 8;
                  byte[] data3 = this.data;
                  int dataPos3 = this.dataPos;
                  int n7 = this.dataPos + 1;
                  this.dataPos = n7;
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
         int n = this.dataPos + 1;
         this.dataPos = n;
         byte b = data2[dataPos2];
         if (this.isEOC(b) && this.data[this.dataPos] == 0) {
            int n2 = this.dataPos + 1;
            this.dataPos = n2;
            this.writeTag();
         } else {
            data2 = this.newData;
            int newDataPos2 = this.newDataPos;
            int n3 = this.newDataPos + 1;
            this.newDataPos = n3;
            data2[newDataPos2] = b;
         }
      }
   }

   private void writeValue(int n) {

      for (int i = 0; i < n; i++) {
         byte[] newData2 = this.newData;
         int newDataPos2 = this.newDataPos;
         int n2 = this.newDataPos + 1;
         this.newDataPos = n2;
         byte[] data2 = this.data;
         int dataPos2 = this.dataPos;
         int n3 = this.dataPos + 1;
         this.dataPos = n3;
         newData2[newDataPos2] = data2[dataPos2];
      }
   }

   public static boolean isLongForm(int n) {
      return (n & 128) == 128;
   }

   private void parseValue(int n) {
      int n2 = this.dataPos + n;
      this.dataPos = n2;
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
            byte b = this.data[this.dataPos];
            byte b2 = b;
            if (this.isEOC(b2) && this.data[this.dataPos + 1] == 0) {
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

               int n3 = this.dataPos - (Integer)obj + n;
               byte[] lengthBytes = this.getLengthBytes(n3);
               this.ndefsList.set(n2, lengthBytes);
               int n4 = this.unresolved - 1;
               this.unresolved = n4;
               int n5 = this.numOfTotalLenBytes + (lengthBytes.length - 3);
               this.numOfTotalLenBytes = n5;
            }

            int n6 = this.dataPos + 1;
            this.dataPos = n6;
         } catch (IndexOutOfBoundsException e) {
            throw new IOException(e);
         }
      }
   }

   public Asn1DerIndefLenConverter() {
      byte b = 0;
      this.unresolved = b;
      ArrayList list = new ArrayList();
      this.ndefsList = list;
      byte b2 = 0;
      this.numOfTotalLenBytes = b2;
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
      byte b = 0;
      this.dataPos = b;
      byte b2 = 0;
      this.index = b2;
      int length = this.data.length;
      this.dataSize = length;
      int n = 0;

      while (this.dataPos < this.dataSize) {
         this.parseTag();
         int n2 = this.parseLength();
         this.parseValue(n2);
         if (this.unresolved == 0) {
            n = this.dataSize - this.dataPos;
            int dataPos2 = this.dataPos;
            this.dataSize = dataPos2;
            break;
         }
      }

      if (this.unresolved != 0) {
         throw new IOException("not all indef len BER resolved");
      } else {
         byte[] bytes2 = new byte[this.dataSize + this.numOfTotalLenBytes + n];
         this.newData = bytes2;
         byte b3 = 0;
         this.dataPos = b3;
         byte b4 = 0;
         this.newDataPos = b4;
         byte b5 = 0;
         this.index = b5;

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
         int n2 = this.newDataPos + 1;
         this.newDataPos = n2;
         newData2[newDataPos2] = (byte)n;
      } else if (n < 256) {
         byte[] newData3 = this.newData;
         int newDataPos3 = this.newDataPos;
         int n3 = this.newDataPos + 1;
         this.newDataPos = n3;
         newData3[newDataPos3] = -127;
         newData3 = this.newData;
         newDataPos3 = this.newDataPos;
         int n4 = this.newDataPos + 1;
         this.newDataPos = n4;
         newData3[newDataPos3] = (byte)n;
      } else if (n < 65536) {
         byte[] newData4 = this.newData;
         int newDataPos4 = this.newDataPos;
         int n5 = this.newDataPos + 1;
         this.newDataPos = n5;
         newData4[newDataPos4] = -126;
         newData4 = this.newData;
         newDataPos4 = this.newDataPos;
         int n6 = this.newDataPos + 1;
         this.newDataPos = n6;
         newData4[newDataPos4] = (byte)(n >> 8);
         newData4 = this.newData;
         newDataPos4 = this.newDataPos;
         int n7 = this.newDataPos + 1;
         this.newDataPos = n7;
         newData4[newDataPos4] = (byte)n;
      } else if (n < 16777216) {
         byte[] newData5 = this.newData;
         int newDataPos5 = this.newDataPos;
         int n8 = this.newDataPos + 1;
         this.newDataPos = n8;
         newData5[newDataPos5] = -125;
         newData5 = this.newData;
         newDataPos5 = this.newDataPos;
         int n9 = this.newDataPos + 1;
         this.newDataPos = n9;
         newData5[newDataPos5] = (byte)(n >> 16);
         newData5 = this.newData;
         newDataPos5 = this.newDataPos;
         int n10 = this.newDataPos + 1;
         this.newDataPos = n10;
         newData5[newDataPos5] = (byte)(n >> 8);
         newData5 = this.newData;
         newDataPos5 = this.newDataPos;
         int n11 = this.newDataPos + 1;
         this.newDataPos = n11;
         newData5[newDataPos5] = (byte)n;
      } else {
         byte[] newData6 = this.newData;
         int newDataPos6 = this.newDataPos;
         int n12 = this.newDataPos + 1;
         this.newDataPos = n12;
         newData6[newDataPos6] = -124;
         newData6 = this.newData;
         newDataPos6 = this.newDataPos;
         int n13 = this.newDataPos + 1;
         this.newDataPos = n13;
         newData6[newDataPos6] = (byte)(n >> 24);
         newData6 = this.newData;
         newDataPos6 = this.newDataPos;
         int n14 = this.newDataPos + 1;
         this.newDataPos = n14;
         newData6[newDataPos6] = (byte)(n >> 16);
         newData6 = this.newData;
         newDataPos6 = this.newDataPos;
         int n15 = this.newDataPos + 1;
         this.newDataPos = n15;
         newData6[newDataPos6] = (byte)(n >> 8);
         newData6 = this.newData;
         newDataPos6 = this.newDataPos;
         int n16 = this.newDataPos + 1;
         this.newDataPos = n16;
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
         int n2 = this.dataPos + 1;
         this.dataPos = n2;
         int n3 = data2[dataPos2] & 255;
         if (isIndefinite(n3)) {
            this.ndefsList.add(new Integer(this.dataPos));
            int n4 = this.unresolved + 1;
            this.unresolved = n4;
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
                  int n6 = this.dataPos + 1;
                  this.dataPos = n6;
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
