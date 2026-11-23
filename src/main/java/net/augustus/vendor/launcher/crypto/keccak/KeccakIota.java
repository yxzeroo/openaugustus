package net.augustus.vendor.launcher.crypto.keccak;

public class KeccakIota {
   private static final long[] ROUND_CONSTANTS;

   public void apply(long[][] longsArr, int n) {
      longsArr[0][0] = longsArr[0][0] ^ ROUND_CONSTANTS[n];
   }

   static {
      long[] longs = new long[]{
         7037389349985452056L,
         1133558632395309075L,
         3240919404585680916L,
         -9136898608426123243L,
         -4375263474830278646L,
         -3580805206377496563L,
         3507687141964513296L,
         3493601002148331538L,
         -3236502636312133626L,
         -7596149721723305977L,
         -6510878324305166321L,
         -118247252887076856L,
         -104442064061595631L,
         2347010180551540758L,
         8070801151903137804L,
         -4785417522859999223L,
         -6821701899176116210L,
         -8378979808603799541L,
         -102552669293379561L
      };
      long[] longs2 = new long[]{
         -9223372036854775669L,
         -9223372036854743038L,
         -9223372036854743037L,
         -9223372034707259384L,
         -9223372036854742903L,
         2147483649L,
         -9223372034707259263L,
         -9223372036854742912L,
         -9223372036854775680L,
         2147516555L,
         32898L,
         -9223372034707292150L,
         -9223372036854742902L,
         32778L,
         2147483658L,
         -9223372034707259392L,
         -9223372036854743031L,
         32907L,
         2147483649L,
         138L,
         136L,
         2147516425L,
         -9223372034707259263L
      };
      long[] longs3 = new long[(int)longs[0]];
      longs3[0] = 1L;
      longs3[1] = longs2[10];
      longs3[2] = longs2[12];
      longs3[3] = longs2[15];
      longs3[4] = longs2[17];
      longs3[5] = longs2[5];
      longs3[(int)longs[8]] = longs2[6];
      longs3[(int)longs[9]] = longs2[16];
      longs3[(int)longs[11]] = longs2[19];
      longs3[(int)longs[15]] = longs2[20];
      longs3[(int)longs[4]] = longs2[21];
      longs3[(int)longs[17]] = longs2[14];
      longs3[(int)longs[14]] = longs2[9];
      longs3[(int)longs[5]] = longs2[0];
      longs3[(int)longs[16]] = longs2[4];
      longs3[(int)longs[10]] = longs2[2];
      longs3[(int)longs[6]] = longs2[1];
      longs3[(int)longs[12]] = longs2[8];
      longs3[(int)longs[7]] = longs2[13];
      longs3[(int)longs[1]] = longs2[11];
      longs3[(int)longs[2]] = longs2[22];
      longs3[(int)longs[3]] = longs2[7];
      longs3[(int)longs[13]] = longs2[18];
      longs3[(int)longs[18]] = longs2[3];
      ROUND_CONSTANTS = longs3;
   }
}
