package net.augustus.vendor.launcher.crypto.keccak;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class KeccakParameters {
   public static final String DOMAIN_SUFFIX = "01";
   public static final int STATE_WIDTH = 0;
   public static final int LANE_SIZE = 0;

   public static int getRate(Sha3Type sha3Type) {
      switch (Sha3TypeSwitchMap.ORDINAL_MAP[sha3Type.ordinal()]) {
         case 1:
            return 1152;
         case 2:
            return 1088;
         case 3:
            return 832;
         case 4:
            return 576;
         default:
            throw new IllegalArgumentException();
      }
   }

   public static int getCapacity(Sha3Type sha3Type) {
      switch (Sha3TypeSwitchMap.ORDINAL_MAP[sha3Type.ordinal()]) {
         case 1:
            return 448;
         case 2:
            return 512;
         case 3:
            return 768;
         case 4:
            return 1024;
         default:
            throw new IllegalArgumentException();
      }
   }


}
