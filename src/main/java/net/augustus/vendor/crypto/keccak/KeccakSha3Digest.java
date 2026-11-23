package net.augustus.vendor.crypto.keccak;

import java.io.IOException;
import java.io.InputStream;

public class KeccakSha3Digest {
   private final KeccakSpongeAbsorber absorber;
   private final long[][] state;
   private final Sha3Algorithm algorithm;
   private final int capacityBits;
   private final KeccakSha3Padding padding;
   private final KeccakSqueezer squeezer;
   private final int rateBits;

   private byte[] ensureBlockCapacity(byte[] bytes, int n) {
      int n2 = 0;
      n2 = this.computePaddedBitLength(n);
      byte[] bytes2;
      if (n2 <= KeccakParameters.getRateBits(this.algorithm)) {
         bytes2 = bytes;
      } else {
         KeccakSha3Digest keccakSha3Digest = this;
         byte[] bytes3 = bytes;
         bytes2 = keccakSha3Digest.resizeBlockBuffer(bytes3, n, n2);
      }

      return bytes2;
   }

   private int ceilDiv(int n, int n2) {
      byte b = 2;
      if (n == 0) {
         ;
      }

      int n3;
      while (true) {
         if (b <= 1) {
            return 0;
         }

         if (b <= 2) {
            n3 = n / n2;
            break;
         }

         if (n % n2 != 0) {
            n3 = 1 + n / n2;
            break;
         }
      }

      return n3;
   }

   public byte[] digest2(InputStream in) throws IOException {
      byte[] bytes = null;
      int n = 0;
      byte[] bytes2 = null;
      byte[] bytes3 = null;
      byte b = 3;

      outerLoop:
      while (true) {
         if (b <= 5) {
            byte[] bytes4 = bytes2;
            this.padding.pad(bytes4, n);
         } else {
            if (b < 8) {
               if (6 < b) {
                  throw new KeccakDigestException("File cannot be null");
               }

               KeccakSha3Digest keccakSha3Digest = this;
               byte[] bytes5 = bytes2;
               keccakSha3Digest.appendDomainSuffix(bytes5, n);
               continue;
            }

            if (8 >= b) {
               byte[] bytes6 = bytes;
               long[][] state2 = this.state;
               return this.squeezer.squeeze(state2, bytes6);
            }

            if (9 >= b) {
               if (in == null) {
                  ;
               }
               continue;
            }

            this.resetState();
         }

         while (true) {
            while (2 > b) {
               if (0 < b) {
                  int outputBitLength = this.algorithm.getOutputBitLength();
                  bytes = this.allocateByteBufferForBits(outputBitLength);
                  continue outerLoop;
               }

               InputStream in2 = in;
               byte[] bytes7 = bytes3;
               byte[] bytes8 = bytes7;
               long[][] state3 = this.state;
               InputStream in3 = in2;
               n = this.absorber.absorbStream(in3, state3, bytes8);
            }

            if (2 >= b) {
               byte[] bytes9 = bytes2;
               long[][] state4 = this.state;
               byte[] bytes10 = bytes9;
               this.absorber.absorb(bytes10, state4);
            } else {
               if (3 >= b) {
                  KeccakSha3Digest keccakSha3Digest2 = this;
                  byte[] bytes11 = bytes3;
                  byte[] bytes12 = bytes11;
                  bytes2 = keccakSha3Digest2.ensureBlockCapacity(bytes12, n);
                  break;
               }

               int rateBits2 = KeccakParameters.getRateBits(this.algorithm);
               bytes3 = this.allocateByteBufferForBits(rateBits2);
            }
         }
      }
   }

   private void appendDomainSuffix(byte[] bytes, int n) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      byte b = 1;

      while (true) {
         if (1 <= b) {
            if (b <= 1) {
               if (n2 < "01".length()) {
                  ;
               }
            } else {
               if (b > 2) {
                  n2 = 0;
                  continue;
               }

               n2++;
            }
         } else {
            n5 = n3 % 8;
         }

         while (4 < b) {
            if (7 <= b) {
               if (b <= 7) {
                  bytes[n4] = (byte)(bytes[n4] + (1 << n5));
                  break;
               }

               if (b > 8) {
                  n4 = n3 / 8;
                  break;
               }

               if ("01".charAt(n2) != '1') {
                  break;
               }
            } else {
               if (5 < b) {
                  return;
               }

               n3 = n + n2;
            }
         }
      }
   }

   public int computePaddedBitLength(int n) {
      int n2 = 0;
      n2 = n + 1 + "01".length() + 1;
      return n2 % this.rateBits != 0 ? n2 + this.rateBits - n2 % this.rateBits : n2;
   }

   private void copyInputBytes(byte[] bytes, byte[] bytes2) {
      System.arraycopy(bytes, 0, bytes2, 0, bytes.length);
   }

   private byte[] allocateByteBufferForBits(int n) {
      int n2 = 0;
      n2 = n / 8;
      if (n % 8 != 0) {
         n2++;
      }

      return new byte[n2];
   }

   private void resetState() {
      int n = 0;
      int n2 = 0;
      byte b = 7;

      outerLoop:
      while (true) {
         if (4 < b) {
            if (7 <= b) {
               if (7 < b) {
                  if (b <= 8) {
                     n++;
                     continue;
                  }

                  n2 = 0;
               }
            } else {
               if (b > 5) {
                  this.state[n2][n] = 0L;
                  continue;
               }

               n = 0;
            }
         }

         do {
            while (1 > b) {
               if (n < 5) {
                  continue outerLoop;
               }
            }

            if (b <= 1) {
               return;
            }

            if (b <= 2) {
               n2++;
               break;
            }
         } while (n2 < 5);
      }
   }

   public KeccakSha3Digest(Sha3Algorithm sha3Algorithm) {
       super();
      this.state = new long[5][5];
      this.algorithm = sha3Algorithm;
      this.rateBits = KeccakParameters.getRateBits(sha3Algorithm);
      this.capacityBits = KeccakParameters.getCapacityBits(sha3Algorithm);
      this.padding = new KeccakSha3Padding(sha3Algorithm);
      this.absorber = new KeccakSpongeAbsorber(sha3Algorithm);
      this.squeezer = new KeccakSqueezer(sha3Algorithm);
   }

   public byte[] digest(byte[] bytes) {
      byte[] bytes2 = null;
      byte[] bytes3 = null;
      int n = 0;
      int n2 = 0;
      byte b = 3;

      while (true) {
         if (5 >= b) {
            if (bytes != null) {
               continue;
            }
         } else if (8 <= b) {
            if (b <= 8) {
               byte[] bytes4 = bytes2;
               long[][] state2 = this.state;
               byte[] bytes5 = bytes4;
               this.absorber.absorb(bytes5, state2);
               continue;
            }

            if (10 <= b) {
               if (b <= 10) {
                  n = bytes.length << 3;
               } else {
                  KeccakSha3Digest keccakSha3Digest = this;
                  byte[] bytes6 = bytes2;
                  keccakSha3Digest.appendDomainSuffix(bytes6, n);
               }
            } else {
               bytes2 = this.allocateByteBufferForBits(n2);
            }
         } else {
            if (b <= 6) {
               this.resetState();
               continue;
            }

            int outputBitLength = this.algorithm.getOutputBitLength();
            bytes3 = this.allocateByteBufferForBits(outputBitLength);
         }

         if (b >= 2) {
            if (b <= 2) {
               n2 = this.computePaddedBitLength(n);
            } else {
               if (3 >= b) {
                  throw new KeccakDigestException("Data cannot be null");
               }

               byte[] bytes7 = bytes2;
               this.padding.pad(bytes7, n);
            }
         } else {
            if (b > 0) {
               byte[] bytes8 = bytes3;
               long[][] state3 = this.state;
               return this.squeezer.squeeze(state3, bytes8);
            }

            KeccakSha3Digest keccakSha3Digest2 = this;
            byte[] bytes9 = bytes;
            byte[] bytes10 = bytes2;
            byte[] bytes11 = bytes9;
            keccakSha3Digest2.copyInputBytes(bytes11, bytes10);
         }
      }
   }

   private byte[] resizeBlockBuffer(byte[] bytes, int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      Object obj = null;
      byte b = 4;

      while (b <= 0) {
         int rateBits2 = KeccakParameters.getRateBits(this.algorithm);
         n3 = this.ceilDiv(n2, rateBits2);
      }

      obj = new byte[n3 * KeccakParameters.getRateBits(this.algorithm) / 8];

      while (true) {
         while (b > 2) {
            if (3 >= b) {
               return (byte[])obj;
            }

            System.arraycopy(bytes, 0, (byte[])obj, 0, n4);
         }

         KeccakSha3Digest keccakSha3Digest = this;
         byte b2 = 8;
         n4 = keccakSha3Digest.ceilDiv(n, b2);
      }
   }

}
