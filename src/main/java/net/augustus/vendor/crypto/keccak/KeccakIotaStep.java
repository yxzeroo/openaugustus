package net.augustus.vendor.crypto.keccak;

public class KeccakIotaStep {
   private static final long[] ROUND_CONSTANTS;

   public void applyIota(long[][] longsArr, int n) {
      longsArr[0][0] = longsArr[0][0] ^ ROUND_CONSTANTS[n];
   }

   static {
      long[] longs = new long[]{
         -8924479975621394425L,
         2266801855277301775L,
         6877566405651202064L,
         -3229274051734142962L,
         -4187384592937779177L,
         -1644294445060849651L,
         -7009431462235602927L,
         -8920284634386792424L,
         -4237310065880596470L,
         2882709760480641042L,
         2530144180143915030L,
         -7744393992573288428L,
         7775136681283289099L,
         3448388577208041484L,
         -3425485079986569207L,
         -3644195796930265080L,
         1004705082324811782L,
         -2461468310210871277L,
         6159602728805859349L
      };
      long[] longs2 = new long[]{
         136L,
         -9223372036854743038L,
         32907L,
         -9223372036854742902L,
         2147516425L,
         -9223372034707259384L,
         32898L,
         -9223372034707259392L,
         2147483649L,
         -9223372036854743037L,
         -9223372036854743031L,
         2147516555L,
         32778L,
         -9223372034707292150L,
         2147483649L,
         -9223372036854775669L,
         138L,
         -9223372036854775680L,
         -9223372034707259263L,
         -9223372034707259263L,
         2147483658L,
         -9223372036854742912L,
         -9223372036854742903L
      };
      long[] longs3 = new long[(int)longs[7]];
      longs3[0] = 1L;
      longs3[1] = longs2[6];
      longs3[2] = longs2[3];
      longs3[3] = longs2[7];
      longs3[4] = longs2[2];
      longs3[5] = longs2[14];
      longs3[(int)longs[16]] = longs2[19];
      longs3[(int)longs[0]] = longs2[10];
      longs3[(int)longs[15]] = longs2[16];
      longs3[(int)longs[14]] = longs2[0];
      longs3[(int)longs[8]] = longs2[4];
      longs3[(int)longs[12]] = longs2[20];
      longs3[(int)longs[13]] = longs2[11];
      longs3[(int)longs[5]] = longs2[15];
      longs3[(int)longs[3]] = longs2[22];
      longs3[(int)longs[1]] = longs2[9];
      longs3[(int)longs[2]] = longs2[1];
      longs3[(int)longs[6]] = longs2[17];
      longs3[(int)longs[9]] = longs2[12];
      longs3[(int)longs[17]] = longs2[13];
      longs3[(int)longs[11]] = longs2[18];
      longs3[(int)longs[18]] = longs2[21];
      longs3[(int)longs[10]] = longs2[8];
      longs3[(int)longs[4]] = longs2[5];
      ROUND_CONSTANTS = longs3;
   }
}
