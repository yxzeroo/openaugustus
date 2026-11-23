package net.augustus.vendor.crypto;


public class BitRotateChallengeHash {
   private static long XOR_MASK_A;
   private static long XOR_MASK_B;
   private static long LCG_MULTIPLIER;
   private static final int[] BYTE_PERMUTATION = new int[]{7, 0, 5, 2, 3, 6, 1, 4};

   public static long hash(long l) {
      long l2 = 0L;
      l2 = applyBitMaskAndRotate(l);
      l2 = permuteBytes(l2);
      l2 = mixAndReverseWords(l2);
      return splitRotateAndFold(l2);
   }

   private static long splitRotateAndFold(long l) {
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      l3 = l & -71777214294589696L;
      l2 = l & 71777214294589695L;
      l3 = Long.rotateRight(l3, (int)(l & 7L) + 1);
      l2 = Long.rotateLeft(l2, (int)(l >>> 3 & 7L) + 1);
      l4 = l3 ^ -81985529216486896L | l2 ^ 1317320621713003080L;
      return l4 ^ (l << 32 | l >>> 32);
   }

   private static long applyBitMaskAndRotate(long l) {
      long l2 = 0L;
      int n = 0;
      long l3 = 0L;
      l3 = -4856826358173744606L ^ (long)"駳쵳颊㋜罷ꈉ둱돶疞充\ue2eb\ue2e8䥲왊셸ⓑ㞫㗿⏆䌢ﱄ跁鄆靥늺".hashCode();
      l2 = l;
      l3 = (l3 ^ -9007663346514656584L) - -2105554773903099092L;
      n = 0;
      l3 = l3 + -3336779783744619697L ^ -4792178842341041294L;

      while (n < 64) {
         l3 = (l3 ^ 4020133109775931290L) - -3228317345936082641L;
         if ((1317320621713003080L >>> n & 1L) != 1L) {
            l3 = l3 + 7228901221214251779L ^ -4892619908538613563L;
         } else {
            l3 = l3 - 8556279937864236139L ^ -4253480769079872333L;
            l2 ^= 1L << n;
            l3 = (l3 ^ -4666049151214640444L) - 1454862859646140892L;
         }

         n++;
         l3 = l3 + 8602048760949665574L ^ 147385496823093276L;
         l3 = (l3 ^ 4114760931670954986L) - -4060976492120245481L;
      }

      l3 = (l3 ^ -2070691157301709966L) - -448263088177251520L;
      l2 ^= Long.rotateLeft(l, (int)(l & 15L));
      l3 = (l3 ^ -1498842557886750983L) - 4564650686899781979L;
      return l2;
   }


   private static long permuteBytes(long l) {
      long l2 = 0L;
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      boolean flag2 = false;
      boolean flag3 = false;
      obj2 = new byte[8];

      for (int i = 0; i < 8; i++) {
         ((byte[])obj2)[i] = (byte)((int)(l >>> i * 8 & 255L));
      }

      obj = new byte[8];

      for (int j = 0; j < 8; j++) {
         ((byte[])obj)[BYTE_PERMUTATION[j]] = (byte)(((byte[])obj2)[j] ^ 16 * (j + 1));
      }

      l2 = 0L;

      for (int k = 0; k < 8; k++) {
         l2 |= ((long)((byte[])obj)[k] & 255L) << k * 8;
      }

      return l2 ^ -81985529216486896L >>> (int)((l & 3L) * 8L);
   }

   private BitRotateChallengeHash() {
   }

   private static long mixAndReverseWords(long l) {
      long l2 = 0L;
      long l3 = 0L;
      boolean flag = false;
      l2 = l * 6364136223846793005L;
      l2 += l >>> 32 ^ l << 16;

      for (int i = 0; i < 4; i++) {
         l3 = l2 >>> i * 16 & 65535L;
         l3 = Long.reverse(l3) >>> 48;
         l2 ^= l3 << (i + 2) * 16;
      }

      return l2;
   }




}
