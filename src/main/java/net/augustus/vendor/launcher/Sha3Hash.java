package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.crypto.keccak.KeccakAbsorber;
import net.augustus.vendor.launcher.crypto.keccak.KeccakPadder;
import net.augustus.vendor.launcher.crypto.keccak.KeccakParameters;
import net.augustus.vendor.launcher.crypto.keccak.KeccakSqueezer;
import net.augustus.vendor.launcher.crypto.keccak.Sha3Type;
import java.io.IOException;
import java.io.InputStream;

public class Sha3Hash {
   private final KeccakPadder padder;
   private final int bitrateBits;
   private final KeccakSqueezer squeezer;
   private final long[][] state;
   private final KeccakAbsorber absorber;
   private final Sha3Type type;
   private final int capacityBits;

   private byte[] resizeToBlockMultiple(byte[] bytes, int n, int n2) {
      int rate = KeccakParameters.getRate(this.type);
      int n3 = this.ceilDiv(n2, rate);
      byte[] bytes2 = new byte[n3 * KeccakParameters.getRate(this.type) / 8];
      byte b = 8;
      int n4 = this.ceilDiv(n, b);
      System.arraycopy(bytes, 0, bytes2, 0, n4);
      return bytes2;
   }

   private int ceilDiv(int n, int n2) {
      if (n == 0) {
         return 0;
      } else {
         return n % n2 == 0 ? n / n2 : 1 + n / n2;
      }
   }

   private byte[] allocateBitBuffer(int n) {
      int n2 = n / 8;
      if (n % 8 != 0) {
         n2++;
      }

      return new byte[n2];
   }

   public byte[] digestStream(InputStream in) throws IOException {
      if (in == null) {
         return null;
      } else {
         this.resetState();
         int rate = KeccakParameters.getRate(this.type);
         byte[] bytes = this.allocateBitBuffer(rate);
         long[][] state2 = this.state;
         int n = this.absorber.absorbStream(in, state2, bytes);
         byte[] bytes2 = this.prepareFinalBlock(bytes, n);
         this.appendDomainSuffix(bytes2, n);
         this.padder.pad(bytes2, n);
         long[][] state3 = this.state;
         this.absorber.absorb(bytes2, state3);
         int digestBitLength = this.type.getDigestBitLength();
         byte[] bytes3 = this.allocateBitBuffer(digestBitLength);
         long[][] state4 = this.state;
         return this.squeezer.squeeze(state4, bytes3);
      }
   }

   private byte[] prepareFinalBlock(byte[] bytes, int n) {
      int n2 = this.computePaddedBitLength(n);
      byte[] bytes2;
      if (n2 > KeccakParameters.getRate(this.type)) {
         bytes2 = this.resizeToBlockMultiple(bytes, n, n2);
      } else {
         bytes2 = bytes;
      }

      return bytes2;
   }

   private void copyInputBytes(byte[] bytes, byte[] bytes2) {
      System.arraycopy(bytes, 0, bytes2, 0, bytes.length);
   }

   private void appendDomainSuffix(byte[] bytes, int n) {

      for (int i = 0; i < "01".length(); i++) {
         if ("01".charAt(i) == '1') {
            int n2 = n + i;
            int n3 = n2 / 8;
            int n4 = n2 % 8;
            bytes[n3] = (byte)(bytes[n3] + (1 << n4));
         }
      }
   }

   public int computePaddedBitLength(int n) {
      int n2 = n + 1 + "01".length() + 1;
      return n2 % this.bitrateBits == 0 ? n2 : n2 + this.bitrateBits - n2 % this.bitrateBits;
   }

   public byte[] digest(byte[] bytes) {
      if (bytes == null) {
         return null;
      } else {
         this.resetState();
         int n = bytes.length << 3;
         int n2 = this.computePaddedBitLength(n);
         byte[] bytes2 = this.allocateBitBuffer(n2);
         this.copyInputBytes(bytes, bytes2);
         this.appendDomainSuffix(bytes2, n);
         this.padder.pad(bytes2, n);
         long[][] state2 = this.state;
         this.absorber.absorb(bytes2, state2);
         int digestBitLength = this.type.getDigestBitLength();
         byte[] bytes3 = this.allocateBitBuffer(digestBitLength);
         long[][] state3 = this.state;
         return this.squeezer.squeeze(state3, bytes3);
      }
   }

   private void resetState() {

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 5; j++) {
            this.state[i][j] = 0L;
         }
      }
   }

   public Sha3Hash(Sha3Type sha3Type) {
       super();
      this.state = new long[5][5];
      this.type = sha3Type;
      this.bitrateBits = KeccakParameters.getRate(sha3Type);
      this.capacityBits = KeccakParameters.getCapacity(sha3Type);
      this.padder = new KeccakPadder(sha3Type);
      this.absorber = new KeccakAbsorber(sha3Type);
      this.squeezer = new KeccakSqueezer(sha3Type);
   }

}
