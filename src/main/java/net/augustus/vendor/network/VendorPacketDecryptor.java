package net.augustus.vendor.network;

import net.augustus.settings.Setting;
import net.augustus.vendor.crypto.VendorAesHelper;
import net.augustus.vendor.crypto.aes.PaddedAesCipher;
import net.augustus.vendor.crypto.keccak.KeccakSha3Digest;
import net.augustus.vendor.crypto.keccak.Sha3Algorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.security.InvalidKeyException;
import java.security.ProviderException;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class VendorPacketDecryptor extends MessageToMessageDecoder {
   private byte[] decoyKeySlot31;
   private byte[] derivedKeySlot56;
   private byte[] decoyKeySlot71;
   private byte[] decoyKeySlot84;
   private byte[] decoyKeySlot52;
   private byte[] derivedKeySlot15;
   private byte[] derivedKeySlot66;
   private byte[] derivedKeySlot17;
   private byte[] decoyKeySlot22;
   private byte[] decoyKeySlot45;
   private byte[] derivedKeySlot70;
   private byte[] derivedKeySlot53;
   private byte[] derivedKeySlot71;
   private byte[] derivedKeySlot25;
   private byte[] decoyKeySlot10;
   private byte[] derivedKeySlot20;
   private byte[] decoyKeySlot89;
   private byte[] derivedKeySlot01;
   private byte[] derivedKeySlot12;
   private byte[] derivedKeySlot87;
   private byte[] decoyKeySlot92;
   private boolean has14Rounds;
   private byte[] decoyKeySlot61;
   private byte[] derivedKeySlot11;
   private byte[] derivedKeySlot75;
   private byte[] decoyKeySlot69;
   private byte[] derivedKeySlot34;
   private byte[] derivedKeySlot52;
   private byte[] decoyKeySlot06;
   private byte[] derivedKeySlot77;
   private byte[] derivedKeySlot23;
   private byte[] decoyKeySlot64;
   private byte[] secondaryKeyGuard;
   private byte[] decoyKeySlot97;
   private byte[] decoyKeySlot18;
   private byte[] derivedKeySlot58;
   private byte[] derivedKeySlot29;
   private byte[] derivedKeySlot47;
   private byte[] derivedKeySlot85;
   private byte[] decoyKeySlot88;
   private byte[] decoyKeySlot26;
   private byte[] decoyKeySlot42;
   private byte[] derivedKeySlot96;
   private byte[] derivedKeySlot21;
   private byte[] derivedKeySlot88;
   private byte[] derivedKeySlot91;
   private byte[] derivedKeySlot89;
   private byte[] derivedKeySlot83;
   private byte[] decoyKeySlot25;
   private byte[] decoyKeySlot13;
   private byte[] decoyKeySlot23;
   private byte[] decoyKeySlot86;
   private byte[] derivedKeySlot16;
   private byte[] cbcWorkBuffer;
   private byte[] decoyKeySlot36;
   private byte[] derivedKeySlot59;
   private byte[] decoyKeySlot56;
   private byte[] derivedKeySlot03;
   private byte[] decoyKeySlot24;
   private byte[] derivedKeySlot51;
   private final int[] encTable2;
   private byte[] derivedKeySlot32;
   private byte[] decoyKeySlot99;
   private byte[] derivedKeySlot05;
   private byte[] derivedKeySlot45;
   private byte[] derivedKeySlot36;
   private byte[] decoyKeySlot60;
   private byte[] decoyKeySlot12;
   private byte[] derivedKeySlot02;
   private byte[] derivedKeySlot10;
   private byte[] derivedKeySlot18;
   private byte[] primaryKeyGuard;
   private final int[] decTable1;
   public byte[] cbcRegister;
   private byte[] derivedKeySlot31;
   private final int[] keyTable2;
   private byte[] derivedKeySlot80;
   private final int[] keyTable3;
   private byte[] derivedKeySlot72;
   private byte[] decoyKeySlot58;
   private byte[] decoyKeySlot48;
   private byte[] decoyKeySlot01;
   private byte[] decoyKeySlot11;
   private byte[] derivedKeySlot37;
   private byte[] derivedKeySlot94;
   private byte[] derivedKeySlot49;
   private byte[] decoyKeySlot32;
   private byte[] derivedKeySlot38;
   private byte[] derivedKeySlot68;
   private byte[] decoyKeySlot46;
   private byte[] decoyKeySlot20;
   private byte[] derivedKeySlot27;
   private byte[] derivedKeySlot78;
   private byte[] decoyKeySlot93;
   private byte[] derivedKeySlot62;
   private byte[] decoyKeySlot38;
   private byte[] derivedKeySlot76;
   private byte[] decoyKeySlot95;
   private int[] gfAntilogTable;
   private byte[] derivedKeySlot14;
   private byte[] decoyKeySlot72;
   private byte[] decoyKeySlot82;
   private byte[] derivedKeySlot50;
   private final int[] keyTable4;
   private byte[] decoyKeySlot57;
   private byte[] decoyKeySlot49;
   private byte[] decoyKeySlot81;
   private byte[] derivedKeySlot13;
   private byte[] sessionKey;
   private byte[] decoyKeySlot90;
   private byte[] decoyKeySlot67;
   private byte[] decoyKeySlot75;
   private byte[] decoyKeySlot35;
   private byte[] decoyKeySlot66;
   private byte[] derivedKeySlot60;
   private byte[] derivedKeySlot55;
   private byte[] decoyKeySlot96;
   private byte[] derivedKeySlot07;
   private byte[] decoyKeySlot94;
   private byte[] derivedKeySlot22;
   private byte[] derivedKeySlot98;
   private byte[] derivedKeySlot93;
   private byte[] decoyKeySlot02;
   private byte[] decoyKeySlot85;
   private byte[] decoyKeySlot08;
   private byte[] decoyKeySlot68;
   private byte[] derivedKeySlot39;
   private byte[] derivedKeySlot42;
   private byte[] derivedKeySlot19;
   private byte[] decoyKeySlot33;
   private byte[] derivedKeySlot64;
   private byte[] decoyKeySlot30;
   private byte[] decoyKeySlot34;
   private int[] gfLogTable;
   private byte[] decoyKeySlot54;
   private byte[] decoyKeySlot27;
   private byte[] decoyKeySlot78;
   private byte[] derivedKeySlot48;
   private byte[] decoyKeySlot73;
   private byte[] decoyKeySlot83;
   private final byte[] roundConstants;
   private byte[] derivedKeySlot95;
   private byte[] derivedKeySlot92;
   private byte[] derivedKeySlot97;
   private byte[] decoyKeySlot28;
   private byte[] decoyKeySlot41;
   private final int[] decTable2;
   private byte[] decoyKeySlot87;
   private byte[] decoyKeySlot14;
   private byte[] derivedKeySlot81;
   private byte[] derivedKeySlot28;
   private byte[] decoyKeySlot39;
   private byte[] decoyKeySlot59;
   private int[][] sessionKeySchedule;
   private byte[] derivedKeySlot43;
   private byte[] decoyKeySlot91;
   private byte[] decoyKeySlot98;
   private byte[] derivedKeySlot79;
   private byte[] derivedKeySlot67;
   private byte[] derivedKeySlot74;
   private byte[] derivedKeySlot54;
   private byte[] decoyKeySlot65;
   private byte[] derivedKeySlot57;
   private byte[] decoyKeySlot40;
   private byte[] derivedKeySlot73;
   private byte[] decoyKeySlot53;
   private int[] roundKeys;
   private byte[] derivedKeySlot33;
   private byte[] decoyKeySlot19;
   private final int[] encTable3;
   private byte[] decoyKeySlot21;
   private byte[] derivedKeySlot26;
   private final byte[] sBox;
   private byte[] decoyKeySlot51;
   private byte[] decoyKeySlot09;
   private int roundKeyLimit;
   private byte[] derivedKeySlot46;
   private byte[] derivedKeySlot84;
   private final int[] encTable1;
   private final int[] encTable4;
   private boolean has12OrMoreRounds;
   private byte[] decoyKeySlot50;
   private byte[] derivedKeySlot61;
   private byte[] derivedKeySlot35;
   private byte[] decoyKeySlot16;
   private byte[] decoyKeySlot63;
   private byte[] derivedKeySlot90;
   private byte[] derivedKeySlot06;
   private byte[] derivedKeySlot30;
   private byte[] decoyKeySlot15;
   private byte[] derivedKeySlot86;
   private byte[] decoyKeySlot62;
   private byte[] derivedKeySlot69;
   private byte[] decoyKeySlot80;
   private byte[] decoyKeySlot55;
   private final byte[] invSBox;
   private final int[] decTable3;
   private byte[] decoyKeySlot70;
   private byte[] derivedKeySlot41;
   private byte[] derivedKeySlot82;
   private byte[] decoyKeySlot04;
   private byte[] decoyKeySlot43;
   private byte[] decoyKeySlot17;
   private byte[] decoyKeySlot29;
   private byte[] decoyKeySlot37;
   private final int[] keyTable1;
   private byte[] decoyKeySlot44;
   private byte[] decoyKeySlot05;
   private byte[] decoyKeySlot07;
   private byte[] derivedKeySlot24;
   private byte[] decoyKeySlot74;
   private byte[] derivedKeySlot04;
   private byte[] derivedKeySlot40;
   private byte[] derivedKeySlot08;
   private byte[] derivedKeySlot65;
   private final PipelineTimingGuard timingGuard;
   private byte[] decoyKeySlot79;
   private byte[] derivedKeySlot44;
   private byte[] decoyKeySlot47;
   private byte[] decoyKeySlot77;
   private byte[] derivedKeySlot63;
   private final int[] decTable4;
   private byte[] derivedKeySlot99;
   private byte[] decoyKeySlot76;
   private byte[] decoyKeySlot03;
   private byte[] derivedKeySlot09;
   private static final long[] e = null;
   private static final Long[] f = null;

   private void deriveDecoyKeySlots(byte[] bytes) {
      PaddedAesCipher paddedAesCipher = null;
      byte b = 66;
      long l = -7594646151003029446L;
      l = 2148900864658672570L ^ (long)"錇㦋﹍\ue263짠ꕹ\uf5a9륦嫶㕱桮䤹⩇少绶焄똱쓕观鑿".hashCode();

      outerLoop:
      while (true) {
         if (b <= 50) {
            KeccakSha3Digest keccakSha3Digest = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes3 = new byte[]{
               50,
               69,
               90,
               -55,
               -14,
               -57,
               -93,
               82,
               59,
               -91,
               -81,
               78,
               -121,
               53,
               -87,
               85,
               56,
               -124,
               74,
               -67,
               -103,
               -92,
               -36,
               41,
               -8,
               124,
               -94,
               55,
               67,
               -18,
               -57,
               -103
            };
            this.decoyKeySlot01 = keccakSha3Digest.digest(VendorAesHelper.encryptWithCipher(bytes, bytes3, paddedAesCipher));
            l = l - -5780585840102717735L ^ -437949923804073647L;
         } else if (b >= 76) {
            if (b <= 76) {
               KeccakSha3Digest keccakSha3Digest2 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes7 = new byte[]{
                  96,
                  -69,
                  -88,
                  -88,
                  106,
                  12,
                  35,
                  -75,
                  -58,
                  116,
                  -78,
                  -85,
                  48,
                  21,
                  -97,
                  106,
                  28,
                  38,
                  -50,
                  -118,
                  106,
                  -31,
                  4,
                  50,
                  -73,
                  -41,
                  42,
                  105,
                  23,
                  -51,
                  -18,
                  -128
               };
               this.decoyKeySlot02 = keccakSha3Digest2.digest(VendorAesHelper.encryptWithCipher(bytes, bytes7, paddedAesCipher));
               l = (l ^ -3472767071932529615L) + -6824003403014408003L;
            } else if (89 <= b) {
               if (89 >= b) {
                  KeccakSha3Digest keccakSha3Digest3 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes11 = new byte[]{
                     123,
                     116,
                     59,
                     -37,
                     9,
                     28,
                     -33,
                     47,
                     62,
                     -53,
                     -86,
                     -58,
                     -30,
                     98,
                     -1,
                     19,
                     125,
                     62,
                     -12,
                     -58,
                     112,
                     -88,
                     100,
                     1,
                     -9,
                     -117,
                     69,
                     124,
                     46,
                     -117,
                     95,
                     50
                  };
                  this.decoyKeySlot03 = keccakSha3Digest3.digest(VendorAesHelper.encryptWithCipher(bytes, bytes11, paddedAesCipher));
                  l = l - -5596364877613067472L ^ -1545176694274283841L;
               } else if (95 <= b) {
                  if (b <= 95) {
                     KeccakSha3Digest keccakSha3Digest4 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes15 = new byte[]{
                        88,
                        -47,
                        -72,
                        -36,
                        -87,
                        -63,
                        72,
                        115,
                        108,
                        -11,
                        -47,
                        -114,
                        -97,
                        -6,
                        -32,
                        -50,
                        86,
                        118,
                        7,
                        3,
                        -78,
                        86,
                        117,
                        -27,
                        -28,
                        65,
                        -82,
                        -104,
                        -23,
                        -13,
                        96,
                        -92
                     };
                     this.decoyKeySlot04 = keccakSha3Digest4.digest(VendorAesHelper.encryptWithCipher(bytes, bytes15, paddedAesCipher));
                     l = l - 1930021570765236691L ^ 743173615180188738L;
                  } else if (98 <= b) {
                     if (b <= 98) {
                        KeccakSha3Digest keccakSha3Digest5 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes19 = new byte[]{
                           73,
                           104,
                           36,
                           -61,
                           110,
                           -13,
                           91,
                           -69,
                           -118,
                           -61,
                           24,
                           18,
                           -88,
                           11,
                           -103,
                           -120,
                           -125,
                           -17,
                           24,
                           -91,
                           -92,
                           -126,
                           -19,
                           -42,
                           -89,
                           62,
                           69,
                           -19,
                           -119,
                           114,
                           -16,
                           35
                        };
                        this.decoyKeySlot05 = keccakSha3Digest5.digest(VendorAesHelper.encryptWithCipher(bytes, bytes19, paddedAesCipher));
                        l = l + -4473381678511363978L ^ -5615716312940935320L;
                        continue;
                     }

                     if (b < 100) {
                        KeccakSha3Digest keccakSha3Digest6 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes23 = new byte[]{
                           -57,
                           6,
                           60,
                           -104,
                           -12,
                           -116,
                           -110,
                           90,
                           -117,
                           -35,
                           100,
                           -18,
                           65,
                           -110,
                           64,
                           59,
                           110,
                           -70,
                           78,
                           58,
                           -39,
                           103,
                           -40,
                           112,
                           -46,
                           -55,
                           -122,
                           -39,
                           28,
                           80,
                           54,
                           -28
                        };
                        this.decoyKeySlot06 = keccakSha3Digest6.digest(VendorAesHelper.encryptWithCipher(bytes, bytes23, paddedAesCipher));
                        l = l - 5011468507026129211L ^ 2641763841159871870L;
                        continue;
                     }

                     if (b <= 100) {
                        KeccakSha3Digest keccakSha3Digest7 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes27 = new byte[]{
                           74,
                           -72,
                           -80,
                           -115,
                           -70,
                           -23,
                           -23,
                           100,
                           -28,
                           -68,
                           -89,
                           -87,
                           29,
                           -33,
                           -4,
                           111,
                           -56,
                           -120,
                           -65,
                           41,
                           -42,
                           -96,
                           67,
                           -46,
                           39,
                           40,
                           -6,
                           57,
                           -34,
                           4,
                           -110,
                           -69
                        };
                        this.decoyKeySlot07 = keccakSha3Digest7.digest(VendorAesHelper.encryptWithCipher(bytes, bytes27, paddedAesCipher));
                        l = (l ^ 1437884991266768972L) + 4160560320020586745L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest8 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes31 = new byte[]{
                           -82,
                           -123,
                           14,
                           -39,
                           -86,
                           -2,
                           59,
                           100,
                           -80,
                           -105,
                           56,
                           124,
                           33,
                           -126,
                           86,
                           82,
                           119,
                           -35,
                           19,
                           -36,
                           99,
                           51,
                           -54,
                           57,
                           -28,
                           125,
                           125,
                           -112,
                           25,
                           -19,
                           16,
                           -105
                        };
                        this.decoyKeySlot08 = keccakSha3Digest8.digest(VendorAesHelper.encryptWithCipher(bytes, bytes31, paddedAesCipher));
                        l = l + -1891054050962841044L ^ 5346528054391743617L;
                     }
                  } else {
                     if (96 >= b) {
                        KeccakSha3Digest keccakSha3Digest9 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes35 = new byte[]{
                           75,
                           44,
                           -16,
                           -69,
                           -100,
                           46,
                           -72,
                           -32,
                           -61,
                           -92,
                           -16,
                           -6,
                           -105,
                           -41,
                           -112,
                           32,
                           -37,
                           111,
                           127,
                           30,
                           -100,
                           -120,
                           118,
                           122,
                           -21,
                           -76,
                           -39,
                           112,
                           -113,
                           -102,
                           -72,
                           -95
                        };
                        this.decoyKeySlot09 = keccakSha3Digest9.digest(VendorAesHelper.encryptWithCipher(bytes, bytes35, paddedAesCipher));
                        l = l - -635376448834493091L ^ 3134013591020908474L;
                        continue;
                     }

                     KeccakSha3Digest keccakSha3Digest10 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes39 = new byte[]{
                        -14,
                        84,
                        -33,
                        51,
                        28,
                        74,
                        -24,
                        -22,
                        -59,
                        -84,
                        32,
                        -57,
                        -14,
                        67,
                        -6,
                        -100,
                        -119,
                        -33,
                        67,
                        78,
                        -72,
                        -30,
                        3,
                        -10,
                        12,
                        -24,
                        91,
                        19,
                        29,
                        68,
                        -54,
                        -39
                     };
                     this.decoyKeySlot10 = keccakSha3Digest10.digest(VendorAesHelper.encryptWithCipher(bytes, bytes39, paddedAesCipher));
                     l = (l ^ -6017211179613597345L) - 3796010040120130124L;
                  }
               } else if (b >= 92) {
                  if (b <= 92) {
                     KeccakSha3Digest keccakSha3Digest11 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes43 = new byte[]{
                        82,
                        -89,
                        -31,
                        14,
                        -90,
                        -56,
                        72,
                        -67,
                        68,
                        -53,
                        -112,
                        -92,
                        -108,
                        64,
                        -80,
                        -16,
                        104,
                        97,
                        78,
                        114,
                        52,
                        122,
                        37,
                        -122,
                        39,
                        -26,
                        -5,
                        -48,
                        -63,
                        90,
                        123,
                        19
                     };
                     this.decoyKeySlot11 = keccakSha3Digest11.digest(VendorAesHelper.encryptWithCipher(bytes, bytes43, paddedAesCipher));
                     l = l - -8683828633900876497L ^ -6717838904209904798L;
                  } else if (b <= 93) {
                     KeccakSha3Digest keccakSha3Digest12 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes47 = new byte[]{
                        89,
                        71,
                        113,
                        -17,
                        -35,
                        17,
                        -77,
                        73,
                        75,
                        76,
                        -70,
                        27,
                        -4,
                        121,
                        -48,
                        113,
                        108,
                        -87,
                        56,
                        76,
                        73,
                        53,
                        45,
                        -31,
                        69,
                        -42,
                        -104,
                        -23,
                        112,
                        86,
                        -25,
                        -58
                     };
                     this.decoyKeySlot12 = keccakSha3Digest12.digest(VendorAesHelper.encryptWithCipher(bytes, bytes47, paddedAesCipher));
                     l = l + -8782713620366756402L ^ -931157332363900194L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest13 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes51 = new byte[]{
                        -105,
                        58,
                        118,
                        69,
                        -117,
                        -97,
                        -67,
                        -50,
                        -99,
                        124,
                        -51,
                        -51,
                        -31,
                        -35,
                        30,
                        93,
                        -127,
                        127,
                        112,
                        97,
                        28,
                        -105,
                        -71,
                        110,
                        34,
                        -51,
                        -39,
                        52,
                        3,
                        -71,
                        85,
                        101
                     };
                     this.decoyKeySlot13 = keccakSha3Digest13.digest(VendorAesHelper.encryptWithCipher(bytes, bytes51, paddedAesCipher));
                     l = l - 8595930778153981473L ^ -3172387957904244021L;
                  }
               } else {
                  if (90 < b) {
                     KeccakSha3Digest keccakSha3Digest14 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes55 = new byte[]{
                        -32,
                        20,
                        -31,
                        -22,
                        -48,
                        98,
                        -44,
                        -72,
                        -5,
                        -45,
                        120,
                        -29,
                        -60,
                        126,
                        -27,
                        121,
                        20,
                        117,
                        25,
                        -1,
                        -6,
                        118,
                        91,
                        90,
                        34,
                        -113,
                        14,
                        -106,
                        -32,
                        -70,
                        -43,
                        -17
                     };
                     this.decoyKeySlot14 = keccakSha3Digest14.digest(VendorAesHelper.encryptWithCipher(bytes, bytes55, paddedAesCipher));
                     l = (l ^ 1899242093990407551L) + -4814986428283603306L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest15 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes59 = new byte[]{
                     -59,
                     -34,
                     100,
                     81,
                     39,
                     -85,
                     -69,
                     120,
                     107,
                     -121,
                     -105,
                     -85,
                     89,
                     -36,
                     -57,
                     61,
                     -8,
                     12,
                     -101,
                     63,
                     -73,
                     107,
                     127,
                     105,
                     -76,
                     -76,
                     9,
                     75,
                     -28,
                     -58,
                     63,
                     -14
                  };
                  this.decoyKeySlot15 = keccakSha3Digest15.digest(VendorAesHelper.encryptWithCipher(bytes, bytes59, paddedAesCipher));
                  l = l + 2344794643305348707L ^ 2818324741803715880L;
               }
            } else if (b >= 82) {
               if (82 >= b) {
                  KeccakSha3Digest keccakSha3Digest16 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes63 = new byte[]{
                     -34,
                     -95,
                     -102,
                     86,
                     -116,
                     29,
                     68,
                     -102,
                     43,
                     9,
                     14,
                     -104,
                     -54,
                     53,
                     11,
                     -78,
                     41,
                     -68,
                     -53,
                     24,
                     6,
                     -20,
                     76,
                     39,
                     46,
                     -77,
                     71,
                     -11,
                     -33,
                     -2,
                     -58,
                     -44
                  };
                  this.decoyKeySlot16 = keccakSha3Digest16.digest(VendorAesHelper.encryptWithCipher(bytes, bytes63, paddedAesCipher));
                  l = (l ^ 8505194451109490794L) + -4569380993447640277L;
               } else if (b >= 85) {
                  if (85 >= b) {
                     KeccakSha3Digest keccakSha3Digest17 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes67 = new byte[]{
                        80,
                        -21,
                        -112,
                        -2,
                        -69,
                        -127,
                        -35,
                        65,
                        34,
                        127,
                        -86,
                        -11,
                        114,
                        -23,
                        126,
                        -28,
                        47,
                        48,
                        68,
                        123,
                        116,
                        75,
                        70,
                        0,
                        -76,
                        -77,
                        114,
                        62,
                        -106,
                        -10,
                        -71,
                        -65
                     };
                     this.decoyKeySlot17 = keccakSha3Digest17.digest(VendorAesHelper.encryptWithCipher(bytes, bytes67, paddedAesCipher));
                     l = (l ^ -308840963478251691L) + -3409104819465993077L;
                  } else if (87 <= b) {
                     if (87 >= b) {
                        KeccakSha3Digest keccakSha3Digest18 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes71 = new byte[]{
                           -31,
                           64,
                           127,
                           65,
                           77,
                           -117,
                           24,
                           -30,
                           -2,
                           67,
                           119,
                           55,
                           -12,
                           -10,
                           -12,
                           -110,
                           -54,
                           -103,
                           44,
                           92,
                           25,
                           -105,
                           0,
                           -39,
                           96,
                           94,
                           -40,
                           -15,
                           111,
                           80,
                           -21,
                           28
                        };
                        this.decoyKeySlot18 = keccakSha3Digest18.digest(VendorAesHelper.encryptWithCipher(bytes, bytes71, paddedAesCipher));
                        l = l - -6394059057626618403L ^ -1089837857702993954L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest19 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes75 = new byte[]{
                           114,
                           -108,
                           -51,
                           -16,
                           -67,
                           -21,
                           67,
                           -43,
                           40,
                           66,
                           -59,
                           58,
                           118,
                           110,
                           12,
                           -34,
                           -127,
                           100,
                           39,
                           32,
                           33,
                           -44,
                           -67,
                           -2,
                           33,
                           21,
                           -56,
                           -35,
                           -103,
                           90,
                           86,
                           -123
                        };
                        this.decoyKeySlot19 = keccakSha3Digest19.digest(VendorAesHelper.encryptWithCipher(bytes, bytes75, paddedAesCipher));
                        l = l + -4555238624224087599L ^ 2736443376087395544L;
                     }
                  } else {
                     KeccakSha3Digest keccakSha3Digest20 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes79 = new byte[]{
                        70,
                        9,
                        -102,
                        67,
                        -81,
                        -112,
                        57,
                        -40,
                        -87,
                        42,
                        41,
                        -102,
                        116,
                        81,
                        71,
                        -117,
                        125,
                        75,
                        -10,
                        7,
                        126,
                        -10,
                        -102,
                        -56,
                        70,
                        1,
                        96,
                        -84,
                        16,
                        -84,
                        -78,
                        14
                     };
                     this.decoyKeySlot20 = keccakSha3Digest20.digest(VendorAesHelper.encryptWithCipher(bytes, bytes79, paddedAesCipher));
                     l = l + -7640026083757620265L ^ 6354796537476860643L;
                  }
               } else if (83 >= b) {
                  KeccakSha3Digest keccakSha3Digest21 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes83 = new byte[]{
                     69,
                     31,
                     -42,
                     -60,
                     -19,
                     24,
                     -60,
                     108,
                     18,
                     75,
                     38,
                     -77,
                     109,
                     91,
                     -97,
                     104,
                     4,
                     116,
                     -107,
                     -120,
                     -43,
                     -61,
                     21,
                     -85,
                     111,
                     122,
                     -64,
                     -28,
                     -11,
                     75,
                     -28,
                     63
                  };
                  this.decoyKeySlot21 = keccakSha3Digest21.digest(VendorAesHelper.encryptWithCipher(bytes, bytes83, paddedAesCipher));
                  l = (l ^ -4130785545052407720L) - -9125958581512557666L;
               } else {
                  KeccakSha3Digest keccakSha3Digest22 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes87 = new byte[]{
                     -4,
                     10,
                     -76,
                     94,
                     11,
                     101,
                     -12,
                     26,
                     104,
                     -80,
                     -2,
                     116,
                     111,
                     116,
                     73,
                     64,
                     90,
                     -43,
                     -50,
                     106,
                     15,
                     43,
                     -103,
                     -97,
                     -63,
                     -36,
                     28,
                     106,
                     -125,
                     -13,
                     31,
                     -4
                  };
                  this.decoyKeySlot22 = keccakSha3Digest22.digest(VendorAesHelper.encryptWithCipher(bytes, bytes87, paddedAesCipher));
                  l = l + 5168318668281679178L ^ -6656980663347808296L;
               }
            } else {
               if (b < 79) {
                  if (b <= 77) {
                     KeccakSha3Digest keccakSha3Digest23 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes91 = new byte[]{
                        -113,
                        -22,
                        57,
                        52,
                        113,
                        -101,
                        -16,
                        -119,
                        -33,
                        98,
                        -91,
                        -24,
                        -58,
                        56,
                        66,
                        -49,
                        -57,
                        127,
                        123,
                        -88,
                        78,
                        26,
                        15,
                        -4,
                        -7,
                        7,
                        -62,
                        -116,
                        -24,
                        8,
                        -23,
                        -48
                     };
                     this.decoyKeySlot23 = keccakSha3Digest23.digest(VendorAesHelper.encryptWithCipher(bytes, bytes91, paddedAesCipher));
                     l = l + -4347086672513904282L ^ 1454318307077303360L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest24 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes95 = new byte[]{
                        12,
                        -104,
                        127,
                        36,
                        -112,
                        -101,
                        100,
                        17,
                        -125,
                        97,
                        20,
                        94,
                        82,
                        5,
                        -125,
                        82,
                        49,
                        -67,
                        27,
                        -9,
                        -112,
                        93,
                        -15,
                        126,
                        12,
                        -87,
                        -96,
                        75,
                        72,
                        -39,
                        56,
                        -62
                     };
                     this.decoyKeySlot24 = keccakSha3Digest24.digest(VendorAesHelper.encryptWithCipher(bytes, bytes95, paddedAesCipher));
                     l = l - 7746149334332404385L ^ -1052320942318865580L;
                  }
                  continue;
               }

               if (79 < b) {
                  if (b <= 80) {
                     KeccakSha3Digest keccakSha3Digest25 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes99 = new byte[]{
                        59,
                        67,
                        -86,
                        90,
                        21,
                        -57,
                        113,
                        -10,
                        119,
                        -122,
                        -107,
                        39,
                        35,
                        -86,
                        14,
                        -60,
                        -38,
                        100,
                        -106,
                        48,
                        -43,
                        96,
                        -34,
                        9,
                        -57,
                        -58,
                        93,
                        102,
                        102,
                        -65,
                        23,
                        32
                     };
                     this.decoyKeySlot25 = keccakSha3Digest25.digest(VendorAesHelper.encryptWithCipher(bytes, bytes99, paddedAesCipher));
                     l = (l ^ -7233759079459045941L) - 7671361007031554104L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest26 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes103 = new byte[]{
                        -51,
                        -67,
                        25,
                        -23,
                        120,
                        125,
                        -37,
                        101,
                        25,
                        -5,
                        94,
                        -25,
                        114,
                        103,
                        -4,
                        -110,
                        -7,
                        109,
                        -56,
                        1,
                        -101,
                        -25,
                        -82,
                        36,
                        90,
                        -66,
                        -63,
                        -77,
                        -79,
                        -49,
                        72,
                        102
                     };
                     this.decoyKeySlot26 = keccakSha3Digest26.digest(VendorAesHelper.encryptWithCipher(bytes, bytes103, paddedAesCipher));
                     l = (l ^ 2650637680683038616L) - -136461646935314723L;
                  }
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest27 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes107 = new byte[]{
                  42,
                  92,
                  -126,
                  73,
                  54,
                  16,
                  -107,
                  97,
                  -118,
                  126,
                  -62,
                  -4,
                  82,
                  -59,
                  64,
                  98,
                  86,
                  -59,
                  -4,
                  47,
                  -37,
                  -32,
                  49,
                  -26,
                  -58,
                  67,
                  -37,
                  113,
                  -121,
                  -87,
                  119,
                  -43
               };
               this.decoyKeySlot27 = keccakSha3Digest27.digest(VendorAesHelper.encryptWithCipher(bytes, bytes107, paddedAesCipher));
               l = l - -1426376832955560463L ^ 8941938876884349819L;
            }
         } else if (b >= 63) {
            if (b <= 63) {
               KeccakSha3Digest keccakSha3Digest28 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes111 = new byte[]{
                  -33,
                  -71,
                  -122,
                  95,
                  -126,
                  42,
                  -63,
                  110,
                  108,
                  71,
                  57,
                  31,
                  -3,
                  9,
                  114,
                  21,
                  -41,
                  70,
                  76,
                  41,
                  -76,
                  10,
                  -93,
                  54,
                  -1,
                  -30,
                  -86,
                  7,
                  -29,
                  -50,
                  118,
                  -59
               };
               this.decoyKeySlot28 = keccakSha3Digest28.digest(VendorAesHelper.encryptWithCipher(bytes, bytes111, paddedAesCipher));
               l = l + -7833230451252437056L ^ -4752952341145744184L;
               continue;
            }

            if (69 <= b) {
               if (69 >= b) {
                  paddedAesCipher = new PaddedAesCipher();
                  l = (l ^ -8119129134540139991L) - 3536473627577121150L;
                  continue;
               }

               if (72 <= b) {
                  if (b <= 72) {
                     KeccakSha3Digest keccakSha3Digest29 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes115 = new byte[]{
                        37,
                        49,
                        45,
                        -17,
                        -42,
                        -96,
                        -105,
                        -60,
                        -39,
                        127,
                        -128,
                        102,
                        -96,
                        -120,
                        74,
                        -20,
                        54,
                        70,
                        -42,
                        -52,
                        42,
                        118,
                        -74,
                        -55,
                        -104,
                        -66,
                        18,
                        -73,
                        34,
                        28,
                        -92,
                        75
                     };
                     this.decoyKeySlot29 = keccakSha3Digest29.digest(VendorAesHelper.encryptWithCipher(bytes, bytes115, paddedAesCipher));
                     l = l + -6053590302918736249L ^ 7785186154227200730L;
                     continue;
                  }

                  if (b < 74) {
                     KeccakSha3Digest keccakSha3Digest30 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes119 = new byte[]{
                        -89,
                        59,
                        51,
                        -73,
                        -47,
                        -87,
                        3,
                        -85,
                        62,
                        120,
                        -15,
                        104,
                        4,
                        35,
                        -49,
                        -3,
                        -44,
                        -14,
                        114,
                        71,
                        -1,
                        74,
                        -97,
                        26,
                        -13,
                        127,
                        118,
                        11,
                        -52,
                        70,
                        -46,
                        79
                     };
                     this.decoyKeySlot30 = keccakSha3Digest30.digest(VendorAesHelper.encryptWithCipher(bytes, bytes119, paddedAesCipher));
                     l = (l ^ -820277471740959861L) - -8144021946190510418L;
                     continue;
                  }

                  if (b > 74) {
                     KeccakSha3Digest keccakSha3Digest31 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes123 = new byte[]{
                        -37,
                        67,
                        67,
                        -90,
                        100,
                        -1,
                        58,
                        -15,
                        12,
                        81,
                        122,
                        -50,
                        -119,
                        4,
                        -116,
                        78,
                        83,
                        -52,
                        -67,
                        27,
                        29,
                        15,
                        73,
                        38,
                        19,
                        15,
                        -96,
                        38,
                        -37,
                        70,
                        88,
                        -34
                     };
                     this.decoyKeySlot31 = keccakSha3Digest31.digest(VendorAesHelper.encryptWithCipher(bytes, bytes123, paddedAesCipher));
                     l = l - -6421926896508936406L ^ -5466345296671079482L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest32 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes127 = new byte[]{
                     -59,
                     -127,
                     -41,
                     -68,
                     42,
                     -10,
                     53,
                     19,
                     20,
                     78,
                     100,
                     -81,
                     71,
                     -93,
                     -83,
                     -99,
                     -37,
                     31,
                     120,
                     81,
                     -94,
                     27,
                     -127,
                     6,
                     -17,
                     -99,
                     -90,
                     80,
                     -5,
                     58,
                     118,
                     9
                  };
                  this.decoyKeySlot32 = keccakSha3Digest32.digest(VendorAesHelper.encryptWithCipher(bytes, bytes127, paddedAesCipher));
                  l = l - 2087711260532842962L ^ 5513902838548656086L;
               } else {
                  if (70 >= b) {
                     KeccakSha3Digest keccakSha3Digest33 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes131 = new byte[]{
                        86,
                        -49,
                        117,
                        122,
                        83,
                        113,
                        121,
                        105,
                        126,
                        103,
                        -43,
                        61,
                        16,
                        0,
                        117,
                        -80,
                        119,
                        116,
                        77,
                        -27,
                        -88,
                        -2,
                        3,
                        -62,
                        93,
                        37,
                        -83,
                        5,
                        -76,
                        34,
                        -65,
                        -70
                     };
                     this.decoyKeySlot33 = keccakSha3Digest33.digest(VendorAesHelper.encryptWithCipher(bytes, bytes131, paddedAesCipher));
                     l = l + -2990452625725477761L ^ 8365671547427435845L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest34 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes135 = new byte[]{
                     -23,
                     -15,
                     -58,
                     -46,
                     127,
                     27,
                     -21,
                     16,
                     88,
                     -78,
                     123,
                     -95,
                     30,
                     35,
                     97,
                     -90,
                     -77,
                     91,
                     -90,
                     -114,
                     -23,
                     -117,
                     -52,
                     -31,
                     21,
                     97,
                     -75,
                     -41,
                     -3,
                     -120,
                     96,
                     9
                  };
                  this.decoyKeySlot34 = keccakSha3Digest34.digest(VendorAesHelper.encryptWithCipher(bytes, bytes135, paddedAesCipher));
                  l = (l ^ 647545836882334865L) - 2997360716848463732L;
               }
            } else {
               if (b >= 66) {
                  if (66 >= b) {
                     KeccakSha3Digest keccakSha3Digest35 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes139 = new byte[]{
                        25,
                        2,
                        26,
                        3,
                        -62,
                        -19,
                        77,
                        109,
                        70,
                        79,
                        100,
                        -92,
                        68,
                        -93,
                        -86,
                        -86,
                        22,
                        110,
                        83,
                        121,
                        78,
                        54,
                        125,
                        -85,
                        -80,
                        -52,
                        -118,
                        -87,
                        57,
                        -57,
                        -20,
                        -122
                     };
                     this.decoyKeySlot35 = keccakSha3Digest35.digest(VendorAesHelper.encryptWithCipher(bytes, bytes139, paddedAesCipher));
                     l = l - 2115450908134910522L ^ 3077935745139276498L;
                  } else if (b <= 67) {
                     KeccakSha3Digest keccakSha3Digest36 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes143 = new byte[]{
                        62,
                        116,
                        51,
                        59,
                        104,
                        -88,
                        -104,
                        -32,
                        -18,
                        -106,
                        71,
                        121,
                        71,
                        -15,
                        -126,
                        -18,
                        112,
                        -50,
                        105,
                        80,
                        -33,
                        14,
                        -43,
                        38,
                        -24,
                        -15,
                        70,
                        -25,
                        -35,
                        66,
                        -113,
                        20
                     };
                     this.decoyKeySlot36 = keccakSha3Digest36.digest(VendorAesHelper.encryptWithCipher(bytes, bytes143, paddedAesCipher));
                     l = l - -49614058196752458L ^ 4010001071941478705L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest37 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes147 = new byte[]{
                        -105,
                        -82,
                        55,
                        -68,
                        108,
                        46,
                        6,
                        63,
                        -64,
                        -106,
                        118,
                        -4,
                        42,
                        -65,
                        -25,
                        -78,
                        -72,
                        -88,
                        -66,
                        85,
                        -105,
                        119,
                        -51,
                        -88,
                        -23,
                        -75,
                        40,
                        94,
                        96,
                        -32,
                        93,
                        113
                     };
                     this.decoyKeySlot37 = keccakSha3Digest37.digest(VendorAesHelper.encryptWithCipher(bytes, bytes147, paddedAesCipher));
                     l = l + -1327109786394636657L ^ 6344174432114290101L;
                  }
                  continue;
               }

               if (b <= 64) {
                  KeccakSha3Digest keccakSha3Digest38 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes151 = new byte[]{
                     -121,
                     -29,
                     79,
                     -111,
                     6,
                     91,
                     71,
                     -96,
                     54,
                     97,
                     -26,
                     18,
                     78,
                     105,
                     69,
                     9,
                     105,
                     -42,
                     -59,
                     -86,
                     52,
                     66,
                     -70,
                     114,
                     -52,
                     -28,
                     -84,
                     105,
                     -72,
                     -59,
                     -31,
                     -120
                  };
                  this.decoyKeySlot38 = keccakSha3Digest38.digest(VendorAesHelper.encryptWithCipher(bytes, bytes151, paddedAesCipher));
                  l = (l ^ -2940195214869403492L) + -5770486965055506224L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest39 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes155 = new byte[]{
                  -119,
                  62,
                  -66,
                  -84,
                  7,
                  0,
                  47,
                  -48,
                  31,
                  -47,
                  52,
                  -2,
                  -35,
                  -36,
                  -45,
                  64,
                  105,
                  35,
                  -19,
                  -50,
                  119,
                  81,
                  -123,
                  70,
                  -65,
                  -115,
                  -59,
                  -91,
                  -24,
                  -110,
                  -127,
                  25
               };
               this.decoyKeySlot39 = keccakSha3Digest39.digest(VendorAesHelper.encryptWithCipher(bytes, bytes155, paddedAesCipher));
               l = l - 3482115955941670384L ^ 2613665718732149596L;
            }
         } else if (b >= 56) {
            if (b <= 56) {
               KeccakSha3Digest keccakSha3Digest40 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes159 = new byte[]{
                  64,
                  -68,
                  -94,
                  108,
                  -121,
                  33,
                  19,
                  7,
                  42,
                  -39,
                  -117,
                  -80,
                  120,
                  -34,
                  118,
                  -114,
                  -49,
                  -72,
                  -46,
                  -84,
                  -115,
                  122,
                  -15,
                  -101,
                  -53,
                  120,
                  10,
                  68,
                  -78,
                  -5,
                  101,
                  -27
               };
               this.decoyKeySlot40 = keccakSha3Digest40.digest(VendorAesHelper.encryptWithCipher(bytes, bytes159, paddedAesCipher));
               l = (l ^ -7183264078558077865L) - -3245041820588157697L;
            } else if (b >= 59) {
               if (b <= 59) {
                  KeccakSha3Digest keccakSha3Digest41 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes163 = new byte[]{
                     15,
                     -36,
                     52,
                     49,
                     -43,
                     -28,
                     -66,
                     18,
                     -127,
                     80,
                     107,
                     49,
                     -89,
                     -74,
                     -17,
                     43,
                     -86,
                     43,
                     -3,
                     92,
                     24,
                     -64,
                     100,
                     -73,
                     54,
                     7,
                     -43,
                     68,
                     31,
                     -58,
                     -56,
                     -77
                  };
                  this.decoyKeySlot41 = keccakSha3Digest41.digest(VendorAesHelper.encryptWithCipher(bytes, bytes163, paddedAesCipher));
                  l = (l ^ -1735319651538466262L) + 4496187246643600496L;
               } else {
                  if (61 > b) {
                     KeccakSha3Digest keccakSha3Digest42 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes167 = new byte[]{
                        -97,
                        -67,
                        -125,
                        123,
                        69,
                        57,
                        -61,
                        2,
                        -38,
                        -105,
                        109,
                        98,
                        17,
                        8,
                        -88,
                        -117,
                        -93,
                        17,
                        14,
                        94,
                        -84,
                        -38,
                        -77,
                        -113,
                        28,
                        76,
                        119,
                        -67,
                        70,
                        28,
                        -49,
                        51
                     };
                     this.decoyKeySlot42 = keccakSha3Digest42.digest(VendorAesHelper.encryptWithCipher(bytes, bytes167, paddedAesCipher));
                     l = (l ^ -4737260232878154391L) - 6754487854600343339L;
                     continue;
                  }

                  if (b > 61) {
                     KeccakSha3Digest keccakSha3Digest43 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes171 = new byte[]{
                        101,
                        -61,
                        -33,
                        -109,
                        48,
                        -49,
                        34,
                        83,
                        -125,
                        -76,
                        26,
                        22,
                        91,
                        -29,
                        -104,
                        40,
                        81,
                        -50,
                        56,
                        90,
                        -60,
                        -21,
                        -123,
                        -12,
                        1,
                        53,
                        -46,
                        118,
                        36,
                        -74,
                        -5,
                        -121
                     };
                     this.decoyKeySlot43 = keccakSha3Digest43.digest(VendorAesHelper.encryptWithCipher(bytes, bytes171, paddedAesCipher));
                     l = l - 6695055449824300379L ^ -5848243786062264286L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest44 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes175 = new byte[]{
                     47,
                     48,
                     -58,
                     -89,
                     -58,
                     23,
                     -48,
                     95,
                     -21,
                     -123,
                     -46,
                     -23,
                     -28,
                     -108,
                     -99,
                     -108,
                     -65,
                     53,
                     -65,
                     39,
                     8,
                     57,
                     -59,
                     -124,
                     -99,
                     -11,
                     126,
                     0,
                     107,
                     -13,
                     81,
                     -16
                  };
                  this.decoyKeySlot44 = keccakSha3Digest44.digest(VendorAesHelper.encryptWithCipher(bytes, bytes175, paddedAesCipher));
                  l = (l ^ 4593394219657349887L) - 3648848788252771096L;
               }
            } else {
               if (b > 57) {
                  KeccakSha3Digest keccakSha3Digest45 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes179 = new byte[]{
                     89,
                     68,
                     98,
                     4,
                     70,
                     -15,
                     49,
                     -73,
                     90,
                     -113,
                     -47,
                     -25,
                     -72,
                     -13,
                     -117,
                     -106,
                     21,
                     18,
                     -91,
                     -124,
                     23,
                     -96,
                     3,
                     -1,
                     -33,
                     112,
                     70,
                     100,
                     -10,
                     80,
                     -74,
                     -22
                  };
                  this.decoyKeySlot45 = keccakSha3Digest45.digest(VendorAesHelper.encryptWithCipher(bytes, bytes179, paddedAesCipher));
                  l = (l ^ 278978335703675784L) - -1033480036938311863L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest46 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes183 = new byte[]{
                  -65,
                  21,
                  5,
                  76,
                  -113,
                  -88,
                  -28,
                  4,
                  96,
                  100,
                  57,
                  39,
                  -57,
                  104,
                  -114,
                  44,
                  -100,
                  36,
                  75,
                  32,
                  110,
                  -8,
                  4,
                  -11,
                  38,
                  -19,
                  -113,
                  -26,
                  56,
                  89,
                  -15,
                  -4
               };
               this.decoyKeySlot46 = keccakSha3Digest46.digest(VendorAesHelper.encryptWithCipher(bytes, bytes183, paddedAesCipher));
               l = l + -7326119057633810705L ^ -4993397156170879104L;
            }
         } else if (b >= 53) {
            if (53 < b) {
               if (54 >= b) {
                  return;
               }

               KeccakSha3Digest keccakSha3Digest47 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes187 = new byte[]{
                  -44,
                  88,
                  -19,
                  -83,
                  -119,
                  96,
                  56,
                  48,
                  13,
                  87,
                  99,
                  -58,
                  -120,
                  -51,
                  39,
                  -75,
                  23,
                  89,
                  111,
                  103,
                  -81,
                  66,
                  65,
                  -50,
                  74,
                  -118,
                  -34,
                  107,
                  -118,
                  46,
                  -94,
                  37
               };
               this.decoyKeySlot47 = keccakSha3Digest47.digest(VendorAesHelper.encryptWithCipher(bytes, bytes187, paddedAesCipher));
               l = l + 33339967717838013L ^ 5983833733120945946L;
               continue;
            }

            KeccakSha3Digest keccakSha3Digest48 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes193 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
               -62, 30, 123, 121, -114, 40, 62, -125, -5, 123, -56, 43, -28, -24, -52, 13, 118, -26, 77, 4, -100, 36, 87, -85, -3, 3, 89, 52, -9, 35, -128, 92
            }, paddedAesCipher);
            this.decoyKeySlot48 = keccakSha3Digest48.digest(bytes193);
            l = (l ^ -4557535894382376226L) + 8033963733213179533L;
         } else {
            if (51 >= b) {
               KeccakSha3Digest keccakSha3Digest49 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes195 = new byte[]{
                  29,
                  124,
                  61,
                  24,
                  86,
                  49,
                  107,
                  -109,
                  -128,
                  35,
                  26,
                  43,
                  123,
                  -4,
                  -7,
                  78,
                  96,
                  -93,
                  10,
                  -47,
                  67,
                  -52,
                  -41,
                  -117,
                  66,
                  106,
                  -87,
                  -22,
                  -108,
                  -14,
                  17,
                  -23
               };
               this.decoyKeySlot49 = keccakSha3Digest49.digest(VendorAesHelper.encryptWithCipher(bytes, bytes195, paddedAesCipher));
               l = l + 5119265207193117776L ^ -6702914202204784056L;
               continue;
            }

            KeccakSha3Digest keccakSha3Digest50 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes199 = new byte[]{
               94,
               -124,
               -2,
               20,
               74,
               -78,
               -123,
               71,
               -117,
               124,
               -63,
               -48,
               -4,
               26,
               -72,
               -85,
               -8,
               -1,
               -111,
               61,
               -11,
               4,
               34,
               -85,
               -76,
               -128,
               -30,
               -109,
               70,
               -65,
               -33,
               -35
            };
            this.decoyKeySlot50 = keccakSha3Digest50.digest(VendorAesHelper.encryptWithCipher(bytes, bytes199, paddedAesCipher));
            l = l - 5005468825842217497L ^ -6944945429100987564L;
         }

         while (true) {
            while (b < 24) {
               if (b >= 11) {
                  if (b <= 11) {
                     KeccakSha3Digest keccakSha3Digest51 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes203 = new byte[]{
                        -70,
                        -81,
                        49,
                        20,
                        54,
                        12,
                        -8,
                        -115,
                        44,
                        4,
                        -62,
                        -103,
                        104,
                        113,
                        81,
                        34,
                        -55,
                        95,
                        -43,
                        17,
                        -123,
                        104,
                        39,
                        99,
                        -72,
                        -33,
                        -79,
                        47,
                        79,
                        -66,
                        -16,
                        -127
                     };
                     this.decoyKeySlot51 = keccakSha3Digest51.digest(VendorAesHelper.encryptWithCipher(bytes, bytes203, paddedAesCipher));
                     l = (l ^ -6421790703969034655L) + 6994787216519497078L;
                     continue outerLoop;
                  }

                  if (17 <= b) {
                     if (b <= 17) {
                        KeccakSha3Digest keccakSha3Digest52 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes207 = new byte[]{
                           4,
                           55,
                           39,
                           -73,
                           33,
                           70,
                           -95,
                           -36,
                           -103,
                           -44,
                           77,
                           118,
                           -29,
                           -89,
                           122,
                           -66,
                           29,
                           14,
                           -77,
                           19,
                           43,
                           12,
                           83,
                           -96,
                           7,
                           -43,
                           70,
                           28,
                           84,
                           -28,
                           -100,
                           -24
                        };
                        this.decoyKeySlot52 = keccakSha3Digest52.digest(VendorAesHelper.encryptWithCipher(bytes, bytes207, paddedAesCipher));
                        l = l - -4501058082363560202L ^ 2601443533069590634L;
                        continue outerLoop;
                     }

                     if (b >= 20) {
                        if (b <= 20) {
                           KeccakSha3Digest keccakSha3Digest53 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes211 = new byte[]{
                              -96,
                              100,
                              21,
                              -80,
                              1,
                              41,
                              84,
                              -12,
                              -97,
                              -8,
                              33,
                              7,
                              -67,
                              -17,
                              115,
                              -29,
                              3,
                              59,
                              96,
                              -97,
                              -17,
                              118,
                              -32,
                              -32,
                              109,
                              13,
                              -122,
                              -17,
                              17,
                              82,
                              -13,
                              -10
                           };
                           this.decoyKeySlot53 = keccakSha3Digest53.digest(VendorAesHelper.encryptWithCipher(bytes, bytes211, paddedAesCipher));
                           l = l + 3815650171639211282L ^ -6216841957207454095L;
                           continue outerLoop;
                        }

                        if (b >= 22) {
                           if (22 >= b) {
                              KeccakSha3Digest keccakSha3Digest54 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                              byte[] bytes215 = new byte[]{
                                 -25,
                                 -88,
                                 -36,
                                 68,
                                 -104,
                                 -53,
                                 31,
                                 -81,
                                 36,
                                 91,
                                 25,
                                 -109,
                                 -74,
                                 79,
                                 17,
                                 29,
                                 15,
                                 3,
                                 -75,
                                 33,
                                 2,
                                 -39,
                                 123,
                                 -26,
                                 77,
                                 -48,
                                 -75,
                                 -112,
                                 -52,
                                 59,
                                 60,
                                 89
                              };
                              this.decoyKeySlot54 = keccakSha3Digest54.digest(VendorAesHelper.encryptWithCipher(bytes, bytes215, paddedAesCipher));
                              l = (l ^ -8564280503006742776L) + 2344024701563788455L;
                           } else {
                              KeccakSha3Digest keccakSha3Digest55 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                              byte[] bytes219 = new byte[]{
                                 -85,
                                 63,
                                 79,
                                 -35,
                                 9,
                                 -111,
                                 -96,
                                 -35,
                                 11,
                                 -92,
                                 51,
                                 125,
                                 -52,
                                 -83,
                                 47,
                                 35,
                                 -106,
                                 13,
                                 112,
                                 -89,
                                 -66,
                                 -69,
                                 -105,
                                 -97,
                                 -55,
                                 -75,
                                 2,
                                 14,
                                 64,
                                 -56,
                                 -30,
                                 -70
                              };
                              this.decoyKeySlot55 = keccakSha3Digest55.digest(VendorAesHelper.encryptWithCipher(bytes, bytes219, paddedAesCipher));
                              l = l - 1892962980488251127L ^ -6567118133598641055L;
                           }
                           continue outerLoop;
                        }

                        KeccakSha3Digest keccakSha3Digest56 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes223 = new byte[]{
                           119,
                           -116,
                           41,
                           -60,
                           18,
                           -1,
                           -6,
                           -20,
                           91,
                           -42,
                           -102,
                           60,
                           -78,
                           -128,
                           47,
                           127,
                           46,
                           91,
                           -3,
                           -6,
                           -60,
                           -13,
                           -124,
                           39,
                           -84,
                           65,
                           -9,
                           -55,
                           60,
                           -65,
                           -31,
                           120
                        };
                        this.decoyKeySlot56 = keccakSha3Digest56.digest(VendorAesHelper.encryptWithCipher(bytes, bytes223, paddedAesCipher));
                        l = l + 155655923888370775L ^ 1945538988992116512L;
                     } else {
                        if (b > 18) {
                           KeccakSha3Digest keccakSha3Digest57 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes227 = new byte[]{
                              126,
                              8,
                              -42,
                              31,
                              38,
                              41,
                              5,
                              86,
                              64,
                              114,
                              97,
                              -109,
                              -42,
                              66,
                              82,
                              32,
                              -40,
                              -117,
                              -16,
                              -122,
                              56,
                              125,
                              -102,
                              -118,
                              125,
                              -26,
                              -59,
                              -44,
                              30,
                              27,
                              42,
                              -36
                           };
                           this.decoyKeySlot57 = keccakSha3Digest57.digest(VendorAesHelper.encryptWithCipher(bytes, bytes227, paddedAesCipher));
                           l = (l ^ 4878534795288500080L) + 115825664254453365L;
                           continue outerLoop;
                        }

                        KeccakSha3Digest keccakSha3Digest58 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes231 = new byte[]{
                           63,
                           -6,
                           80,
                           -33,
                           62,
                           -80,
                           49,
                           76,
                           94,
                           15,
                           -3,
                           16,
                           18,
                           103,
                           36,
                           1,
                           81,
                           -66,
                           -64,
                           -68,
                           36,
                           -73,
                           -93,
                           -121,
                           62,
                           -15,
                           93,
                           -50,
                           81,
                           85,
                           -75,
                           -55
                        };
                        this.decoyKeySlot58 = keccakSha3Digest58.digest(VendorAesHelper.encryptWithCipher(bytes, bytes231, paddedAesCipher));
                        l = (l ^ -8791329666497718431L) + 8959483709919801300L;
                     }
                  } else if (14 <= b) {
                     if (b <= 14) {
                        KeccakSha3Digest keccakSha3Digest59 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes235 = new byte[]{
                           -38,
                           -20,
                           89,
                           91,
                           -84,
                           -56,
                           -108,
                           -112,
                           124,
                           70,
                           103,
                           -86,
                           95,
                           7,
                           -13,
                           19,
                           3,
                           -46,
                           36,
                           -38,
                           -25,
                           -90,
                           87,
                           65,
                           -85,
                           -65,
                           110,
                           88,
                           -49,
                           37,
                           75,
                           -53
                        };
                        this.decoyKeySlot59 = keccakSha3Digest59.digest(VendorAesHelper.encryptWithCipher(bytes, bytes235, paddedAesCipher));
                        l = (l ^ -7134967581869946247L) - 8768715853725826051L;
                     } else {
                        if (15 >= b) {
                           KeccakSha3Digest keccakSha3Digest60 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes239 = new byte[]{
                              -55,
                              -25,
                              116,
                              -5,
                              -12,
                              -116,
                              -70,
                              -19,
                              -7,
                              56,
                              -95,
                              -91,
                              -16,
                              -61,
                              67,
                              -120,
                              116,
                              116,
                              76,
                              44,
                              107,
                              1,
                              -29,
                              10,
                              -37,
                              11,
                              126,
                              -44,
                              -47,
                              -81,
                              -20,
                              -53
                           };
                           this.decoyKeySlot60 = keccakSha3Digest60.digest(VendorAesHelper.encryptWithCipher(bytes, bytes239, paddedAesCipher));
                           l = l + 5926576319809484220L ^ 6250379996110977273L;
                           continue outerLoop;
                        }

                        KeccakSha3Digest keccakSha3Digest61 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes243 = new byte[]{
                           57,
                           -21,
                           -81,
                           -103,
                           -75,
                           2,
                           -89,
                           -11,
                           -127,
                           11,
                           -96,
                           118,
                           69,
                           49,
                           -53,
                           38,
                           49,
                           86,
                           105,
                           55,
                           -58,
                           96,
                           101,
                           -110,
                           0,
                           -118,
                           103,
                           -90,
                           65,
                           -72,
                           91,
                           116
                        };
                        this.decoyKeySlot61 = keccakSha3Digest61.digest(VendorAesHelper.encryptWithCipher(bytes, bytes243, paddedAesCipher));
                        l = l + 1830478135702390663L ^ 8805956416004270494L;
                     }
                  } else {
                     if (12 < b) {
                        KeccakSha3Digest keccakSha3Digest62 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes247 = new byte[]{
                           -29,
                           -51,
                           39,
                           93,
                           -48,
                           -96,
                           -125,
                           -68,
                           16,
                           17,
                           -15,
                           -116,
                           -65,
                           61,
                           -51,
                           31,
                           48,
                           -117,
                           11,
                           15,
                           -55,
                           -47,
                           22,
                           -23,
                           8,
                           -27,
                           78,
                           -115,
                           90,
                           98,
                           99,
                           -49
                        };
                        this.decoyKeySlot62 = keccakSha3Digest62.digest(VendorAesHelper.encryptWithCipher(bytes, bytes247, paddedAesCipher));
                        l = (l ^ -2441808693414178273L) - -5148703292651617905L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest63 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes251 = new byte[]{
                        -31,
                        23,
                        50,
                        81,
                        -90,
                        -96,
                        77,
                        -80,
                        100,
                        -27,
                        29,
                        -8,
                        -19,
                        -91,
                        -72,
                        -47,
                        -98,
                        111,
                        -36,
                        -24,
                        -40,
                        37,
                        80,
                        127,
                        123,
                        -22,
                        -63,
                        99,
                        76,
                        87,
                        17,
                        58
                     };
                     this.decoyKeySlot63 = keccakSha3Digest63.digest(VendorAesHelper.encryptWithCipher(bytes, bytes251, paddedAesCipher));
                     l = (l ^ 5139171629393343739L) - -5897605917039881543L;
                  }
               } else if (b >= 5) {
                  if (b <= 5) {
                     KeccakSha3Digest keccakSha3Digest64 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes255 = new byte[]{
                        -113,
                        37,
                        82,
                        -33,
                        98,
                        25,
                        23,
                        119,
                        -104,
                        44,
                        52,
                        105,
                        72,
                        4,
                        115,
                        118,
                        -5,
                        -95,
                        -80,
                        -26,
                        -106,
                        -52,
                        -8,
                        -56,
                        59,
                        -30,
                        71,
                        -35,
                        -34,
                        -51,
                        -11,
                        -35
                     };
                     this.decoyKeySlot64 = keccakSha3Digest64.digest(VendorAesHelper.encryptWithCipher(bytes, bytes255, paddedAesCipher));
                     l = l - -231243650251456859L ^ 8070314382564281983L;
                     continue outerLoop;
                  }

                  if (8 <= b) {
                     if (8 >= b) {
                        KeccakSha3Digest keccakSha3Digest65 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes259 = new byte[]{
                           -18,
                           0,
                           -84,
                           -13,
                           -77,
                           32,
                           89,
                           -90,
                           62,
                           -120,
                           66,
                           -19,
                           73,
                           87,
                           14,
                           11,
                           -120,
                           107,
                           -117,
                           -90,
                           -48,
                           71,
                           70,
                           10,
                           89,
                           5,
                           89,
                           91,
                           111,
                           9,
                           -2,
                           72
                        };
                        this.decoyKeySlot65 = keccakSha3Digest65.digest(VendorAesHelper.encryptWithCipher(bytes, bytes259, paddedAesCipher));
                        l = (l ^ -744448677843217315L) + -574903357794708192L;
                     } else {
                        if (b > 9) {
                           KeccakSha3Digest keccakSha3Digest66 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes263 = new byte[]{
                              41,
                              16,
                              116,
                              -16,
                              -3,
                              85,
                              -30,
                              -39,
                              -9,
                              50,
                              -88,
                              -14,
                              108,
                              -112,
                              36,
                              -48,
                              82,
                              -25,
                              70,
                              -67,
                              -99,
                              16,
                              35,
                              102,
                              -72,
                              -69,
                              -59,
                              -79,
                              -68,
                              1,
                              40,
                              -80
                           };
                           this.decoyKeySlot66 = keccakSha3Digest66.digest(VendorAesHelper.encryptWithCipher(bytes, bytes263, paddedAesCipher));
                           l = l - -4619086046437323842L ^ -8675657224319089439L;
                           continue outerLoop;
                        }

                        KeccakSha3Digest keccakSha3Digest67 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes267 = new byte[]{
                           -25,
                           88,
                           -2,
                           -115,
                           86,
                           -13,
                           125,
                           -115,
                           103,
                           56,
                           -42,
                           -53,
                           57,
                           -40,
                           -62,
                           -43,
                           21,
                           -71,
                           -6,
                           -77,
                           43,
                           -109,
                           -72,
                           -59,
                           31,
                           50,
                           72,
                           -120,
                           53,
                           103,
                           -112,
                           -94
                        };
                        this.decoyKeySlot67 = keccakSha3Digest67.digest(VendorAesHelper.encryptWithCipher(bytes, bytes267, paddedAesCipher));
                        l = l - -7935824658733636074L ^ -5066795347506155019L;
                     }
                  } else {
                     if (b <= 6) {
                        KeccakSha3Digest keccakSha3Digest68 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes271 = new byte[]{
                           39,
                           75,
                           127,
                           88,
                           82,
                           -88,
                           52,
                           -58,
                           -18,
                           27,
                           44,
                           -48,
                           -82,
                           127,
                           62,
                           -107,
                           20,
                           -14,
                           0,
                           93,
                           -105,
                           21,
                           -106,
                           13,
                           -38,
                           16,
                           98,
                           95,
                           -42,
                           103,
                           -22,
                           -15
                        };
                        this.decoyKeySlot68 = keccakSha3Digest68.digest(VendorAesHelper.encryptWithCipher(bytes, bytes271, paddedAesCipher));
                        l = l - -3418496404568388349L ^ -3877103150074267475L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest69 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes275 = new byte[]{
                        80,
                        92,
                        19,
                        -84,
                        -35,
                        24,
                        -126,
                        -85,
                        40,
                        -108,
                        -36,
                        65,
                        30,
                        32,
                        87,
                        -34,
                        -12,
                        88,
                        2,
                        -57,
                        48,
                        -21,
                        -51,
                        23,
                        66,
                        64,
                        -108,
                        -43,
                        -121,
                        -54,
                        107,
                        -89
                     };
                     this.decoyKeySlot69 = keccakSha3Digest69.digest(VendorAesHelper.encryptWithCipher(bytes, bytes275, paddedAesCipher));
                     l = l - 4469072392077918187L ^ 7839985145687346627L;
                  }
               } else if (b >= 2) {
                  if (2 >= b) {
                     KeccakSha3Digest keccakSha3Digest70 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes279 = new byte[]{
                        41,
                        -29,
                        21,
                        0,
                        102,
                        -125,
                        -13,
                        109,
                        116,
                        -75,
                        -45,
                        110,
                        108,
                        -108,
                        42,
                        -36,
                        -70,
                        83,
                        55,
                        43,
                        13,
                        -124,
                        2,
                        29,
                        85,
                        -41,
                        90,
                        102,
                        124,
                        124,
                        -46,
                        -64
                     };
                     this.decoyKeySlot70 = keccakSha3Digest70.digest(VendorAesHelper.encryptWithCipher(bytes, bytes279, paddedAesCipher));
                     l = (l ^ -6873815134758913153L) - -6109565397365342632L;
                     continue outerLoop;
                  }

                  if (3 < b) {
                     KeccakSha3Digest keccakSha3Digest71 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes283 = new byte[]{
                        51,
                        30,
                        -84,
                        48,
                        -47,
                        -50,
                        61,
                        61,
                        27,
                        -112,
                        89,
                        -111,
                        -15,
                        -31,
                        108,
                        -56,
                        -7,
                        -106,
                        -35,
                        47,
                        45,
                        24,
                        127,
                        -61,
                        41,
                        110,
                        -112,
                        23,
                        -52,
                        -4,
                        -54,
                        -86
                     };
                     this.decoyKeySlot71 = keccakSha3Digest71.digest(VendorAesHelper.encryptWithCipher(bytes, bytes283, paddedAesCipher));
                     l = l + 2917096176229111959L ^ 5863508889876934003L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest72 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes287 = new byte[]{
                     94,
                     9,
                     126,
                     -47,
                     68,
                     11,
                     92,
                     70,
                     113,
                     81,
                     115,
                     105,
                     64,
                     -72,
                     111,
                     79,
                     -80,
                     31,
                     93,
                     -90,
                     -104,
                     -90,
                     115,
                     62,
                     69,
                     -53,
                     36,
                     37,
                     118,
                     -63,
                     -81,
                     76
                  };
                  this.decoyKeySlot72 = keccakSha3Digest72.digest(VendorAesHelper.encryptWithCipher(bytes, bytes287, paddedAesCipher));
                  l = l - 1025441221306997586L ^ 7938053532804237257L;
               } else {
                  if (b <= 0) {
                     KeccakSha3Digest keccakSha3Digest73 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes291 = new byte[]{
                        50,
                        -67,
                        -91,
                        -114,
                        -45,
                        98,
                        -32,
                        94,
                        -59,
                        -88,
                        34,
                        -35,
                        -112,
                        -52,
                        65,
                        -111,
                        48,
                        -105,
                        58,
                        17,
                        127,
                        -2,
                        53,
                        -110,
                        47,
                        -65,
                        33,
                        82,
                        -24,
                        86,
                        -116,
                        -94
                     };
                     this.decoyKeySlot73 = keccakSha3Digest73.digest(VendorAesHelper.encryptWithCipher(bytes, bytes291, paddedAesCipher));
                     l = (l ^ -7520067355617876228L) - -8680453858783500615L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest74 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes295 = new byte[]{
                     -16,
                     -52,
                     121,
                     97,
                     -38,
                     -114,
                     50,
                     4,
                     -24,
                     -20,
                     106,
                     26,
                     12,
                     49,
                     -9,
                     103,
                     -120,
                     5,
                     -87,
                     -54,
                     114,
                     -23,
                     -23,
                     56,
                     -28,
                     125,
                     -70,
                     -40,
                     -18,
                     -84,
                     71,
                     48
                  };
                  this.decoyKeySlot74 = keccakSha3Digest74.digest(VendorAesHelper.encryptWithCipher(bytes, bytes295, paddedAesCipher));
                  l = l + -1660344594577917984L ^ 6254068203091260563L;
               }
            }

            if (24 >= b) {
               KeccakSha3Digest keccakSha3Digest75 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes299 = new byte[]{
                  -33,
                  -72,
                  13,
                  69,
                  114,
                  -88,
                  -29,
                  -4,
                  16,
                  11,
                  85,
                  -112,
                  94,
                  -73,
                  20,
                  39,
                  120,
                  -99,
                  41,
                  -60,
                  58,
                  121,
                  0,
                  -70,
                  19,
                  -92,
                  -119,
                  -112,
                  -15,
                  -94,
                  123,
                  -96
               };
               this.decoyKeySlot75 = keccakSha3Digest75.digest(VendorAesHelper.encryptWithCipher(bytes, bytes299, paddedAesCipher));
               l = l + -3311898004642506583L ^ -5129443923638453408L;
            } else if (37 <= b) {
               if (b <= 37) {
                  KeccakSha3Digest keccakSha3Digest76 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes303 = new byte[]{
                     -28,
                     -72,
                     -118,
                     70,
                     126,
                     -126,
                     -44,
                     -100,
                     46,
                     -94,
                     -67,
                     -38,
                     -40,
                     6,
                     47,
                     103,
                     -2,
                     4,
                     123,
                     -30,
                     -8,
                     -94,
                     71,
                     45,
                     15,
                     -40,
                     13,
                     82,
                     -30,
                     61,
                     95,
                     57
                  };
                  this.decoyKeySlot76 = keccakSha3Digest76.digest(VendorAesHelper.encryptWithCipher(bytes, bytes303, paddedAesCipher));
                  l = l - 890992635330765396L ^ 5591627956358602317L;
                  break;
               }

               if (b >= 43) {
                  if (43 >= b) {
                     KeccakSha3Digest keccakSha3Digest77 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes307 = new byte[]{
                        -54,
                        120,
                        -41,
                        113,
                        30,
                        -45,
                        20,
                        -23,
                        20,
                        3,
                        -93,
                        -82,
                        -12,
                        -21,
                        124,
                        -108,
                        60,
                        -107,
                        -42,
                        -106,
                        36,
                        112,
                        -66,
                        -127,
                        58,
                        -14,
                        10,
                        -104,
                        -1,
                        14,
                        -118,
                        -1
                     };
                     this.decoyKeySlot77 = keccakSha3Digest77.digest(VendorAesHelper.encryptWithCipher(bytes, bytes307, paddedAesCipher));
                     l = l - 7284925095331032176L ^ 7273812537872559253L;
                     break;
                  }

                  if (b >= 46) {
                     if (46 >= b) {
                        KeccakSha3Digest keccakSha3Digest78 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes311 = new byte[]{
                           82,
                           -67,
                           116,
                           100,
                           52,
                           26,
                           -10,
                           61,
                           104,
                           -24,
                           -23,
                           -50,
                           -38,
                           32,
                           16,
                           44,
                           -122,
                           -94,
                           84,
                           -9,
                           -113,
                           23,
                           -119,
                           96,
                           80,
                           -57,
                           64,
                           37,
                           -49,
                           40,
                           48,
                           -63
                        };
                        this.decoyKeySlot78 = keccakSha3Digest78.digest(VendorAesHelper.encryptWithCipher(bytes, bytes311, paddedAesCipher));
                        l = l - -300295589482870487L ^ 605118413185561058L;
                     } else if (48 <= b) {
                        if (b <= 48) {
                           KeccakSha3Digest keccakSha3Digest79 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes315 = new byte[]{
                              120,
                              -75,
                              -119,
                              -128,
                              111,
                              63,
                              29,
                              -108,
                              56,
                              -84,
                              -14,
                              -16,
                              -81,
                              108,
                              100,
                              91,
                              -46,
                              -111,
                              62,
                              -10,
                              9,
                              -1,
                              -120,
                              54,
                              -34,
                              51,
                              -49,
                              119,
                              4,
                              122,
                              69,
                              -104
                           };
                           this.primaryKeyGuard = keccakSha3Digest79.digest(VendorAesHelper.encryptWithCipher(bytes, bytes315, paddedAesCipher));
                           l = (l ^ 2230827217405677637L) - -2183027006890165687L;
                           break;
                        }

                        KeccakSha3Digest keccakSha3Digest80 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes319 = new byte[]{
                           -52,
                           123,
                           114,
                           -55,
                           -98,
                           104,
                           -54,
                           81,
                           15,
                           42,
                           -126,
                           6,
                           71,
                           -53,
                           89,
                           -126,
                           69,
                           -99,
                           -78,
                           21,
                           -32,
                           27,
                           103,
                           -128,
                           -6,
                           88,
                           57,
                           10,
                           123,
                           -86,
                           -33,
                           -97
                        };
                        this.decoyKeySlot79 = keccakSha3Digest80.digest(VendorAesHelper.encryptWithCipher(bytes, bytes319, paddedAesCipher));
                        l = l - 4481755589360972985L ^ 6494536252097343571L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest81 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes323 = new byte[]{
                           35,
                           47,
                           40,
                           56,
                           -101,
                           125,
                           57,
                           75,
                           19,
                           39,
                           28,
                           85,
                           -112,
                           -109,
                           91,
                           -26,
                           -100,
                           76,
                           14,
                           17,
                           -27,
                           37,
                           -110,
                           45,
                           78,
                           -28,
                           -90,
                           109,
                           98,
                           39,
                           16,
                           -41
                        };
                        this.decoyKeySlot80 = keccakSha3Digest81.digest(VendorAesHelper.encryptWithCipher(bytes, bytes323, paddedAesCipher));
                        l = (l ^ -3567394667680209124L) - 8171459968676833586L;
                     }
                  } else {
                     if (b > 44) {
                        KeccakSha3Digest keccakSha3Digest82 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes327 = new byte[]{
                           73,
                           13,
                           -61,
                           24,
                           -88,
                           -67,
                           -30,
                           69,
                           -82,
                           -31,
                           -106,
                           126,
                           -7,
                           19,
                           -67,
                           -83,
                           -34,
                           -22,
                           49,
                           1,
                           -29,
                           -56,
                           79,
                           -94,
                           40,
                           -61,
                           60,
                           -12,
                           -73,
                           55,
                           -61,
                           13
                        };
                        this.decoyKeySlot81 = keccakSha3Digest82.digest(VendorAesHelper.encryptWithCipher(bytes, bytes327, paddedAesCipher));
                        l = (l ^ -4161926383732033340L) - -8971650003744200179L;
                        break;
                     }

                     KeccakSha3Digest keccakSha3Digest83 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes331 = new byte[]{
                        107,
                        -119,
                        50,
                        -67,
                        32,
                        -39,
                        89,
                        45,
                        -75,
                        111,
                        -65,
                        -26,
                        119,
                        106,
                        13,
                        -118,
                        -111,
                        -51,
                        -16,
                        -61,
                        -78,
                        -40,
                        -119,
                        -20,
                        -125,
                        -22,
                        36,
                        76,
                        4,
                        -80,
                        -67,
                        -109
                     };
                     this.decoyKeySlot82 = keccakSha3Digest83.digest(VendorAesHelper.encryptWithCipher(bytes, bytes331, paddedAesCipher));
                     l = (l ^ 2583844317749901684L) + -2198004651095425L;
                  }
               } else if (b >= 40) {
                  if (b > 40) {
                     if (b <= 41) {
                        KeccakSha3Digest keccakSha3Digest84 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes335 = new byte[]{
                           108,
                           -11,
                           23,
                           61,
                           -81,
                           -49,
                           -59,
                           63,
                           -102,
                           124,
                           -27,
                           109,
                           -45,
                           -112,
                           78,
                           -108,
                           18,
                           97,
                           84,
                           20,
                           -97,
                           1,
                           26,
                           -21,
                           -82,
                           43,
                           92,
                           -51,
                           45,
                           -6,
                           89,
                           -58
                        };
                        this.decoyKeySlot83 = keccakSha3Digest84.digest(VendorAesHelper.encryptWithCipher(bytes, bytes335, paddedAesCipher));
                        l = (l ^ 2026261275201724852L) - -2854625909782893939L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest85 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes339 = new byte[]{
                           -50,
                           -99,
                           -45,
                           -28,
                           -112,
                           -107,
                           107,
                           71,
                           -46,
                           -21,
                           -2,
                           -104,
                           -45,
                           51,
                           -69,
                           -86,
                           20,
                           -115,
                           84,
                           37,
                           -3,
                           110,
                           52,
                           86,
                           -103,
                           87,
                           75,
                           -28,
                           43,
                           53,
                           -95,
                           -48
                        };
                        this.decoyKeySlot84 = keccakSha3Digest85.digest(VendorAesHelper.encryptWithCipher(bytes, bytes339, paddedAesCipher));
                        l = (l ^ -4825588268681520513L) + -8191260327759431732L;
                     }
                     break;
                  }

                  KeccakSha3Digest keccakSha3Digest86 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes343 = new byte[]{
                     42,
                     91,
                     100,
                     -29,
                     48,
                     23,
                     106,
                     -69,
                     -70,
                     -55,
                     61,
                     -39,
                     -21,
                     76,
                     89,
                     88,
                     -82,
                     -63,
                     -124,
                     -99,
                     -22,
                     15,
                     71,
                     -48,
                     -29,
                     95,
                     -92,
                     108,
                     51,
                     15,
                     -71,
                     68
                  };
                  this.decoyKeySlot85 = keccakSha3Digest86.digest(VendorAesHelper.encryptWithCipher(bytes, bytes343, paddedAesCipher));
                  l = (l ^ -793180367259293616L) + -6988625715818516000L;
               } else if (38 >= b) {
                  KeccakSha3Digest keccakSha3Digest87 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes347 = new byte[]{
                     -118,
                     75,
                     -88,
                     106,
                     39,
                     114,
                     23,
                     -26,
                     -46,
                     89,
                     -37,
                     115,
                     -127,
                     106,
                     20,
                     60,
                     32,
                     63,
                     19,
                     -36,
                     35,
                     -114,
                     -125,
                     -51,
                     -61,
                     -103,
                     44,
                     26,
                     -110,
                     -69,
                     -42,
                     -106
                  };
                  this.decoyKeySlot86 = keccakSha3Digest87.digest(VendorAesHelper.encryptWithCipher(bytes, bytes347, paddedAesCipher));
                  l = (l ^ -6976739723132386881L) + -7985592133028006210L;
               } else {
                  KeccakSha3Digest keccakSha3Digest88 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes351 = new byte[]{
                     -31,
                     24,
                     -119,
                     58,
                     -33,
                     64,
                     110,
                     -13,
                     -114,
                     -48,
                     -4,
                     108,
                     -128,
                     49,
                     118,
                     77,
                     118,
                     -68,
                     111,
                     116,
                     -106,
                     -127,
                     -74,
                     11,
                     -95,
                     31,
                     -70,
                     87,
                     95,
                     -110,
                     -100,
                     45
                  };
                  this.decoyKeySlot87 = keccakSha3Digest88.digest(VendorAesHelper.encryptWithCipher(bytes, bytes351, paddedAesCipher));
                  l = (l ^ 8961847039935613808L) + 121173972012726651L;
               }
            } else if (b >= 30) {
               if (30 >= b) {
                  KeccakSha3Digest keccakSha3Digest89 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes355 = new byte[]{
                     -78,
                     115,
                     -98,
                     16,
                     -75,
                     124,
                     -41,
                     -93,
                     -112,
                     -99,
                     -99,
                     62,
                     -53,
                     47,
                     98,
                     122,
                     121,
                     -34,
                     56,
                     31,
                     -46,
                     -104,
                     51,
                     80,
                     108,
                     -70,
                     -84,
                     20,
                     107,
                     121,
                     32,
                     -90
                  };
                  this.decoyKeySlot88 = keccakSha3Digest89.digest(VendorAesHelper.encryptWithCipher(bytes, bytes355, paddedAesCipher));
                  l = (l ^ -1937910040455447L) - 6015261692138117241L;
                  break;
               }

               if (33 <= b) {
                  if (33 >= b) {
                     KeccakSha3Digest keccakSha3Digest90 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes359 = new byte[]{
                        -60,
                        1,
                        -108,
                        109,
                        -105,
                        21,
                        53,
                        -97,
                        95,
                        -62,
                        -47,
                        -6,
                        4,
                        69,
                        13,
                        -109,
                        106,
                        -5,
                        -62,
                        -8,
                        -94,
                        121,
                        -74,
                        -23,
                        116,
                        118,
                        63,
                        -60,
                        115,
                        117,
                        112,
                        -67
                     };
                     this.decoyKeySlot89 = keccakSha3Digest90.digest(VendorAesHelper.encryptWithCipher(bytes, bytes359, paddedAesCipher));
                     l = (l ^ 4318243304352925480L) - -2582327214147091978L;
                  } else if (b >= 35) {
                     if (35 >= b) {
                        KeccakSha3Digest keccakSha3Digest91 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes363 = new byte[]{
                           65,
                           81,
                           -69,
                           -58,
                           -48,
                           46,
                           54,
                           108,
                           27,
                           -3,
                           -40,
                           88,
                           -69,
                           -117,
                           -107,
                           -77,
                           -31,
                           -112,
                           14,
                           -127,
                           -78,
                           55,
                           40,
                           105,
                           79,
                           -59,
                           -15,
                           -23,
                           -18,
                           97,
                           -19,
                           -104
                        };
                        this.decoyKeySlot90 = keccakSha3Digest91.digest(VendorAesHelper.encryptWithCipher(bytes, bytes363, paddedAesCipher));
                        l = l + 952721823623780555L ^ 4301195051940609773L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest92 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes367 = new byte[]{
                           -81,
                           69,
                           -24,
                           11,
                           -48,
                           -96,
                           33,
                           55,
                           48,
                           -117,
                           -22,
                           -34,
                           25,
                           55,
                           -53,
                           7,
                           -73,
                           -23,
                           27,
                           119,
                           -81,
                           18,
                           0,
                           115,
                           -77,
                           -110,
                           53,
                           21,
                           112,
                           -55,
                           -103,
                           -8
                        };
                        this.decoyKeySlot91 = keccakSha3Digest92.digest(VendorAesHelper.encryptWithCipher(bytes, bytes367, paddedAesCipher));
                        l = (l ^ -1592916009422138108L) - 5665720624099125701L;
                     }
                  } else {
                     KeccakSha3Digest keccakSha3Digest93 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes371 = new byte[]{
                        -34,
                        45,
                        33,
                        43,
                        66,
                        64,
                        -73,
                        1,
                        -73,
                        37,
                        91,
                        40,
                        61,
                        81,
                        15,
                        102,
                        -86,
                        87,
                        90,
                        -21,
                        23,
                        -12,
                        -89,
                        106,
                        -102,
                        -56,
                        69,
                        104,
                        120,
                        -126,
                        15,
                        39
                     };
                     this.decoyKeySlot92 = keccakSha3Digest93.digest(VendorAesHelper.encryptWithCipher(bytes, bytes371, paddedAesCipher));
                     l = l - 9218039153642185723L ^ -7846237594143796590L;
                  }
                  break;
               }

               if (b <= 31) {
                  KeccakSha3Digest keccakSha3Digest94 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes375 = new byte[]{
                     48,
                     69,
                     3,
                     -88,
                     68,
                     -119,
                     -28,
                     40,
                     100,
                     10,
                     27,
                     40,
                     90,
                     -80,
                     105,
                     49,
                     59,
                     125,
                     -91,
                     -60,
                     -65,
                     -89,
                     22,
                     120,
                     -108,
                     -24,
                     113,
                     -59,
                     100,
                     -62,
                     -8,
                     -76
                  };
                  this.decoyKeySlot93 = keccakSha3Digest94.digest(VendorAesHelper.encryptWithCipher(bytes, bytes375, paddedAesCipher));
                  l = l - 251173458338810380L ^ 7293446022001730821L;
                  break;
               }

               KeccakSha3Digest keccakSha3Digest95 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes379 = new byte[]{
                  -13,
                  83,
                  103,
                  107,
                  77,
                  -122,
                  -69,
                  -106,
                  27,
                  -64,
                  77,
                  95,
                  40,
                  69,
                  87,
                  95,
                  117,
                  -102,
                  -47,
                  120,
                  68,
                  -87,
                  -68,
                  -60,
                  13,
                  -25,
                  107,
                  -120,
                  18,
                  37,
                  -108,
                  -81
               };
               this.decoyKeySlot94 = keccakSha3Digest95.digest(VendorAesHelper.encryptWithCipher(bytes, bytes379, paddedAesCipher));
               l = (l ^ -1429281458960113909L) + 8186512825388767780L;
            } else if (27 <= b) {
               if (27 >= b) {
                  KeccakSha3Digest keccakSha3Digest96 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes383 = new byte[]{
                     -40,
                     -88,
                     61,
                     -22,
                     11,
                     51,
                     -22,
                     35,
                     -49,
                     -5,
                     -21,
                     -44,
                     -107,
                     -26,
                     2,
                     -12,
                     30,
                     -53,
                     -81,
                     107,
                     113,
                     69,
                     -116,
                     7,
                     92,
                     -57,
                     88,
                     90,
                     -33,
                     16,
                     -71,
                     -28
                  };
                  this.decoyKeySlot95 = keccakSha3Digest96.digest(VendorAesHelper.encryptWithCipher(bytes, bytes383, paddedAesCipher));
                  l = (l ^ 4396889301355953181L) - -1977293173161020471L;
                  break;
               }

               if (b <= 28) {
                  KeccakSha3Digest keccakSha3Digest97 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes387 = new byte[]{
                     0,
                     -114,
                     92,
                     120,
                     -67,
                     45,
                     -90,
                     -29,
                     59,
                     -67,
                     -92,
                     75,
                     57,
                     -71,
                     -64,
                     -115,
                     -88,
                     -63,
                     14,
                     50,
                     60,
                     -13,
                     -23,
                     114,
                     35,
                     120,
                     116,
                     13,
                     -104,
                     69,
                     107,
                     -44
                  };
                  this.decoyKeySlot96 = keccakSha3Digest97.digest(VendorAesHelper.encryptWithCipher(bytes, bytes387, paddedAesCipher));
                  l = (l ^ -7865920154694097123L) - -3443489612264136371L;
                  break;
               }

               KeccakSha3Digest keccakSha3Digest98 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes391 = new byte[]{
                  -58,
                  32,
                  -85,
                  109,
                  7,
                  -103,
                  62,
                  -62,
                  -26,
                  112,
                  -34,
                  -77,
                  -55,
                  -125,
                  -29,
                  -41,
                  108,
                  122,
                  -78,
                  21,
                  -116,
                  -55,
                  75,
                  -62,
                  -31,
                  -53,
                  -48,
                  40,
                  -91,
                  -110,
                  105,
                  -67
               };
               this.decoyKeySlot97 = keccakSha3Digest98.digest(VendorAesHelper.encryptWithCipher(bytes, bytes391, paddedAesCipher));
               l = (l ^ -7159860942586169246L) - 8877445668444058822L;
            } else if (b <= 25) {
               KeccakSha3Digest keccakSha3Digest99 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes395 = new byte[]{
                  -119,
                  -53,
                  1,
                  -91,
                  -2,
                  -3,
                  107,
                  126,
                  36,
                  44,
                  123,
                  -2,
                  -78,
                  13,
                  -9,
                  120,
                  86,
                  66,
                  121,
                  -41,
                  82,
                  33,
                  22,
                  -39,
                  -90,
                  83,
                  -37,
                  50,
                  72,
                  -112,
                  60,
                  -106
               };
               this.decoyKeySlot98 = keccakSha3Digest99.digest(VendorAesHelper.encryptWithCipher(bytes, bytes395, paddedAesCipher));
               l = (l ^ -3951077944302836542L) + 4893319663301289042L;
            } else {
               KeccakSha3Digest keccakSha3Digest100 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes399 = new byte[]{
                  116,
                  29,
                  -120,
                  -71,
                  13,
                  18,
                  126,
                  -42,
                  -31,
                  112,
                  28,
                  49,
                  -107,
                  109,
                  -24,
                  8,
                  102,
                  63,
                  -62,
                  26,
                  78,
                  -21,
                  -124,
                  55,
                  97,
                  1,
                  123,
                  38,
                  113,
                  56,
                  -64,
                  -4
               };
               this.decoyKeySlot99 = keccakSha3Digest100.digest(VendorAesHelper.encryptWithCipher(bytes, bytes399, paddedAesCipher));
               l = (l ^ 7730840183341353264L) + -7106165138257759472L;
            }
         }
      }
   }

   private int mul4(int n, byte[] bytes) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      byte b = 1;

      while (2 < b) {
         if (3 < b) {
            return 0;
         }

         if (n != 0) {
            n = this.gfLogTable[n & 0xFF];
         }
      }

      n5 = ((byte[])bytes)[0] == 0 ? 0 : this.gfAntilogTable[(n + this.gfLogTable[((byte[])bytes)[0] & 255]) % 255] & 0xFF;
      n2 = ((byte[])bytes)[1] != 0 ? this.gfAntilogTable[(n + this.gfLogTable[((byte[])bytes)[1] & 255]) % 255] & 0xFF : 0;
      n3 = ((byte[])bytes)[2] != 0 ? this.gfAntilogTable[(n + this.gfLogTable[((byte[])bytes)[2] & 255]) % 255] & 0xFF : 0;
      n4 = ((byte[])bytes)[3] != 0 ? this.gfAntilogTable[(n + this.gfLogTable[((byte[])bytes)[3] & 255]) % 255] & 0xFF : 0;
      return n5 << 24 | n2 << 16 | n3 << 8 | n4;
   }

   private void encryptBlockImpl(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 0;
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];

      while (n3 < this.roundKeyLimit) {
         int n8 = this.encTable1[n4 >>> 24] ^ this.encTable2[n5 >>> 16 & 0xFF] ^ this.encTable3[n6 >>> 8 & 0xFF] ^ this.encTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
         int n9 = this.encTable1[n5 >>> 24] ^ this.encTable2[n6 >>> 16 & 0xFF] ^ this.encTable3[n7 >>> 8 & 0xFF] ^ this.encTable4[n4 & 0xFF] ^ this.roundKeys[n3++];
         int n10 = this.encTable1[n6 >>> 24] ^ this.encTable2[n7 >>> 16 & 0xFF] ^ this.encTable3[n4 >>> 8 & 0xFF] ^ this.encTable4[n5 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.encTable1[n7 >>> 24] ^ this.encTable2[n4 >>> 16 & 0xFF] ^ this.encTable3[n5 >>> 8 & 0xFF] ^ this.encTable4[n6 & 0xFF] ^ this.roundKeys[n3++];
         n4 = n8;
         n5 = n9;
         n6 = n10;
      }

      int n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n7 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n4 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n5 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2] = (byte)(this.sBox[n6 & 0xFF] ^ n11);
   }

   public void checkBlockAlignment(int n, int n2) {
      if (n % n2 != 0) {
         throw new ProviderException("Internal error in input buffering");
      }
   }

   public void padWithLen(byte[] bytes, int n, int n2) throws ShortBufferException {
      byte b = 0;
      int n3 = 0;
      byte b2 = 0;

      while (true) {
         while (true) {
            if (b2 <= 3) {
               if (n3 > bytes.length) {
                  break;
               }
            } else if (5 <= b2) {
               if (5 >= b2) {
                  return;
               }

               if (b2 <= 6) {
                  if (bytes == null) {
                     ;
                  }
                  break;
               }

               Arrays.fill(bytes, n, n3, b);
            } else {
               b = (byte)(n2 & 0xFF);
            }
         }

         if (1 <= b2) {
            if (b2 <= 1) {
               return;
            }

            throw new ShortBufferException("Buffer too small to hold padding");
         }

         n3 = Math.addExact(n, n2);
      }
   }

   private ByteBuf decryptBuffer(ByteBuf buf) {
      ByteBuf buf2 = null;
      byte[] bytes = null;
      byte[] bytes2 = null;
      byte b = 2;
      long l = 514777470714725569L;
      l = 1379142124317117794L ^ (long)"溑䌵蔂묭⁾︶\ue056\ue1ae܄⍕譃앪湳ﵯ烄\ued86仫갵\ue305䇱▴劌Ế䖸ᾘ\u0e3c".hashCode();

      outerLoop:
      while (true) {
         while (true) {
            if (b >= 2) {
               if (2 >= b) {
                  buf2 = null;
                  l = l - -439932780995030341L ^ 7481038996254914757L;
               } else if (b >= 4) {
                  if (b <= 4) {
                     if (this.secondaryKeyGuard.length >= 32) {
                        l = l - -8275639293916706739L ^ -5244996155841406949L;
                     } else {
                        l = (l ^ -1092693430224918915L) - 6152490662039446741L;
                     }
                  } else {
                     bytes = this.decryptBytes(bytes2, (byte)0, bytes2.length);
                     l = (l ^ -3919110856076310988L) - 4010629820680274045L;
                  }
               } else {
                  buf2 = Unpooled.copiedBuffer(bytes2);
                  l = l - -2440957203955725549L ^ 6762748486275808314L;
               }
               break;
            }

            if (0 >= b) {
               buf2 = Unpooled.copiedBuffer(bytes);
               l = l - 8503838529027370983L ^ -565352731603910158L;
               break;
            }

            this.timingGuard.enterStep2();
            l = (l ^ 679046524988653082L) - 2502895657641452741L;
         }

         while (true) {
            while (6 < b) {
               if (b >= 9) {
                  if (9 < b) {
                     if (b < 11) {
                        return buf2;
                     }

                     if (b <= 11) {
                        buf.readBytes(bytes2);
                        l = (l ^ 4997961916752267206L) - -2212668396525115947L;
                     } else {
                        this.initCipher(true, this.sessionKey);
                        l = (l ^ -1485791407423687045L) + -7125751580547414415L;
                     }
                     continue outerLoop;
                  }

                  if (this.primaryKeyGuard.length < 32) {
                     l = l - -470241960554238135L ^ -3552543652888816211L;
                     continue outerLoop;
                  }

                  l = (l ^ 2601167337559011720L) + -3188503123524428920L;
               } else {
                  if (b > 7) {
                     this.resetCbcState();
                     l = l + 6534551215221987218L ^ -2170203237084998771L;
                     continue outerLoop;
                  }

                  l = (l ^ 8090568643744546022L) - -343157964524909008L;
               }
            }

            bytes2 = new byte[buf.readableBytes()];
            l = (l ^ -5445852562199240440L) - 7234759753445488771L;
         }
      }
   }

   public byte[] encryptBytes(byte[] bytes, int n, int n2) {
      String opaqueStateTag = VendorPacketBuffer.getOpaqueStateTag();
      if (n2 <= 0) {
         return null;
      } else {
         try {
            bytes = this.prepareInputBuffer(bytes, n, n2, false);
         } catch (ShortBufferException | IllegalBlockSizeException e2) {
            throw new RuntimeException(e2);
         }

         n2 = bytes.length;
         byte[] bytes2 = new byte[n2];
         this.checkBlockAlignment(n2, this.getBlockSize());
         this.encryptCbc(bytes, n, n2, bytes2, (byte)0);
         byte[] bytes3 = bytes2;
         if (opaqueStateTag == null) {
            if (bytes2.length % this.getBlockSize() != 0) {
               System.out.println("Writing " + bytes2.length);
            }

            bytes3 = bytes2;
         }

         return bytes3;
      }
   }

   public int getBlockSize() {
      return 16;
   }

   public void decryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.decryptBlockImpl(bytes, n, bytes2, n2);
   }

   private int gfMul(int n, int n2) {
      int n3 = 0;
      long l = 4596005159795017397L;
      long l2 = 0L;
      l = 6371702484555112091L ^ (long)"ꂿ犋\u0b9bသ◮䗩昖舤ꔅؑꠛ撠厷ऒ앾⤎ᇔ꺻縥\u20fc".hashCode();
      l2 = 4690980529040694822L ^ (long)"혃簹葙褣㰳㦞狴괠\ufae1덈\ue6caᬷ\udc0f箁䈔䊬귛涻㹥\ue649巴㐾촃嶉⽲ⳑꇀ".hashCode();

      int n4;
      while (true) {
         n3 = (int)(l % 3L);
         if (n3 >= 1) {
            if (1 >= n3) {
               if (n2 != 0) {
                  l = (l ^ -7378376023341154917L) - 7397949081569458383L;
                  l2 = (l2 ^ 8074754412552674849L) - 4282605818690216801L;
                  continue;
               }

               l = (l ^ 7913382387272445931L) + -1324628641595205439L;
               l2 = l2 + 4590493494599988734L ^ 7863613211488414528L;
            } else {
               if (n != 0) {
                  l = (l ^ -5841890808521053282L) - -5179782435644049990L;
                  l2 = l2 - -2572069333523937608L ^ 4291302028066088707L;
                  continue;
               }

               l = l + 0L ^ 0L;
               l2 = (l2 ^ 0L) + 0L;
            }

            n4 = 0;
            break;
         }

         n4 = this.gfAntilogTable[(this.gfLogTable[n & 0xFF] + this.gfLogTable[n2 & 0xFF]) % 255];
         break;
      }

      return n4;
   }

   private void makeSessionKey(byte[] bytes) throws InvalidKeyException {
      int n = 0;
      int rounds = this.getRounds(bytes.length);
      int n2 = (rounds + 1) * 4;
      String opaqueStateTag = VendorPacketBuffer.getOpaqueStateTag();
      int[][] intsArr = new int[rounds + 1][4];
      int[][] intsArr2 = new int[rounds + 1][4];
      String str = opaqueStateTag;
      int n3 = bytes.length / 4;
      int[] ints = new int[n3];
      int n4 = 0;
      int n5 = 0;

      while (n4 < n3) {
         ints[n4] = bytes[n5] << 24 | (bytes[n5 + 1] & 255) << 16 | (bytes[n5 + 2] & 255) << 8 | bytes[n5 + 3] & 255;
         n4++;
         n5 += 4;
         if (str != null) {
            break;
         }
      }

      int n6 = 0;
      n5 = 0;

      while (true) {
         if (n5 < n3) {
            n = n6;
            if (str != null || str != null) {
               break;
            }

            if (n6 < n2) {
               intsArr[n6 / 4][n6 % 4] = ints[n5];
               intsArr2[rounds - n6 / 4][n6 % 4] = ints[n5];
               n5++;
               n6++;
               if (str == null) {
                  continue;
               }
            }
         }

         n = 0;
         break;
      }

      int n7 = n;

      outerLoop:
      while (true) {
         int n8 = n6;
         int n9 = n2;

         outerLoop2:
         while (true) {
            if (n8 >= n9) {
               break outerLoop;
            }

            int n10 = ints[n3 - 1];
            ints[0] ^= this.sBox[n10 >>> 16 & 0xFF] << 24
               ^ (this.sBox[n10 >>> 8 & 0xFF] & 255) << 16
               ^ (this.sBox[n10 & 0xFF] & 255) << 8
               ^ this.sBox[n10 >>> 24] & 255
               ^ this.roundConstants[n7++] << 24;

            while (true) {
               exitBlock: {
                  exitBlock2: {
                     int n11 = n3;
                     if (str == null) {
                        if (n3 != 8) {
                           n4 = 1;
                           n5 = 0;

                           do {
                              if (n4 >= n3) {
                                 break exitBlock2;
                              }

                              ints[n4] ^= ints[n5];
                              n4++;
                              n5++;
                              if (str != null) {
                                 break exitBlock;
                              }
                           } while (str == null);
                        }

                        n4 = 1;
                        n11 = 0;
                     }

                     n5 = n11;

                     exitBlock3: {
                        while (n4 < n3 / 2) {
                           ints[n4] ^= ints[n5];
                           n4++;
                           n5++;
                           if (str != null) {
                              break exitBlock3;
                           }

                           if (str != null) {
                              break;
                           }
                        }

                        n10 = ints[n3 / 2 - 1];
                        ints[n3 / 2] = ints[n3 / 2]
                           ^ this.sBox[n10 & 0xFF] & 255
                           ^ (this.sBox[n10 >>> 8 & 0xFF] & 255) << 8
                           ^ (this.sBox[n10 >>> 16 & 0xFF] & 255) << 16
                           ^ this.sBox[n10 >>> 24] << 24;
                        n5 = n3 / 2;
                     }

                     n4 = n5 + 1;

                     while (n4 < n3) {
                        ints[n4] ^= ints[n5];
                        n4++;
                        n5++;
                        if (str != null) {
                           break exitBlock;
                        }

                        if (str != null) {
                           break;
                        }
                     }
                  }

                  n5 = 0;
               }

               while (true) {
                  if (n5 >= n3) {
                     continue outerLoop;
                  }

                  n8 = n6;
                  n9 = n2;
                  if (str != null) {
                     continue outerLoop2;
                  }

                  if (n6 >= n2) {
                     continue outerLoop;
                  }

                  intsArr[n6 / 4][n6 % 4] = ints[n5];
                  intsArr2[rounds - n6 / 4][n6 % 4] = ints[n5];
                  n5++;
                  n6++;
                  if (str != null) {
                     break;
                  }

                  if (str != null) {
                     break outerLoop;
                  }
               }
            }
         }
      }

      int n12 = 1;

      while (n12 < rounds) {
         n5 = 0;

         while (true) {
            if (n5 < ((byte)4)) {
               int n13 = intsArr2[n12][n5];
               intsArr2[n12][n5] = this.keyTable1[n13 >>> 24 & 0xFF]
                  ^ this.keyTable2[n13 >>> 16 & 0xFF]
                  ^ this.keyTable3[n13 >>> 8 & 0xFF]
                  ^ this.keyTable4[n13 & 0xFF];
               n5++;
               if (str != null) {
                  break;
               }

               if (str == null) {
                  continue;
               }
            }

            n12++;
            break;
         }

         if (str != null) {
            break;
         }
      }

      int[] ints2 = this.expandToSubkeys(intsArr, false);
      int[] ints3 = this.expandToSubkeys(intsArr2, true);
      int n14 = rounds;
      if (str == null) {
         n14 = rounds >= 12 ? 1 : 0;
      }

      this.has12OrMoreRounds = (n14 != 0);
      n14 = rounds;
      if (str == null) {
         n14 = rounds == 14 ? 1 : 0;
      }

      this.has14Rounds = (n14 != 0);
      this.roundKeyLimit = rounds * 4;
      this.sessionKeySchedule = new int[][]{ints2, ints3};
   }

   public void setSessionKey(byte[] bytes) {
      PaddedAesCipher paddedAesCipher = null;
      byte b = 99;
      long l = -5338703525866806185L;
      l = -3359461880012602788L ^ (long)"⩨⾣䋐Ħ疐編꼫ొᛝ度滩跾ᄕ쮿揾\udd9d\u0b31僿똬៑\uaacd".hashCode();

      outerLoop:
      while (true) {
         if (25 <= b) {
            if (25 >= b) {
               KeccakSha3Digest keccakSha3Digest = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes3 = new byte[]{
                  -22,
                  26,
                  112,
                  56,
                  -114,
                  -27,
                  24,
                  -119,
                  7,
                  -110,
                  -99,
                  117,
                  14,
                  40,
                  -85,
                  87,
                  75,
                  -24,
                  54,
                  -12,
                  -84,
                  2,
                  -11,
                  -52,
                  64,
                  5,
                  112,
                  85,
                  76,
                  107,
                  -67,
                  34
               };
               this.derivedKeySlot01 = keccakSha3Digest.digest(VendorAesHelper.encryptWithCipher(bytes, bytes3, paddedAesCipher));
               l = l - -392040424962461035L ^ 7351834222000429478L;
            } else if (b >= 38) {
               if (38 >= b) {
                  KeccakSha3Digest keccakSha3Digest2 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes7 = new byte[]{
                     -111,
                     -26,
                     -107,
                     91,
                     88,
                     -25,
                     -128,
                     59,
                     -83,
                     -83,
                     -77,
                     116,
                     105,
                     23,
                     8,
                     101,
                     118,
                     34,
                     28,
                     -118,
                     -94,
                     -30,
                     103,
                     39,
                     20,
                     -18,
                     63,
                     -29,
                     20,
                     61,
                     -78,
                     -45
                  };
                  this.derivedKeySlot02 = keccakSha3Digest2.digest(VendorAesHelper.encryptWithCipher(bytes, bytes7, paddedAesCipher));
                  l = (l ^ -4357513358971557046L) - -3449964697803004486L;
                  continue;
               }

               if (b < 44) {
                  if (b >= 41) {
                     if (41 >= b) {
                        KeccakSha3Digest keccakSha3Digest3 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes11 = new byte[]{
                           56,
                           70,
                           -98,
                           47,
                           -116,
                           67,
                           96,
                           -112,
                           -55,
                           40,
                           14,
                           -53,
                           64,
                           126,
                           -26,
                           37,
                           -89,
                           -115,
                           -33,
                           116,
                           -19,
                           105,
                           -26,
                           -7,
                           -78,
                           -82,
                           59,
                           74,
                           114,
                           76,
                           45,
                           -35
                        };
                        this.derivedKeySlot03 = keccakSha3Digest3.digest(VendorAesHelper.encryptWithCipher(bytes, bytes11, paddedAesCipher));
                        l = l + -8335582285565394258L ^ -3530147570632453518L;
                     } else if (42 >= b) {
                        this.deriveDecoyKeySlots(bytes);
                        l = (l ^ -2596747106849642265L) + 6009864891988070413L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest4 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes16 = new byte[]{
                           -6,
                           116,
                           95,
                           -89,
                           -65,
                           -61,
                           -59,
                           -73,
                           97,
                           -13,
                           34,
                           9,
                           -109,
                           76,
                           -93,
                           92,
                           121,
                           91,
                           -23,
                           -88,
                           -50,
                           -44,
                           -67,
                           -47,
                           71,
                           -42,
                           100,
                           -80,
                           -18,
                           -23,
                           -60,
                           -5
                        };
                        this.derivedKeySlot04 = keccakSha3Digest4.digest(VendorAesHelper.encryptWithCipher(bytes, bytes16, paddedAesCipher));
                        l = (l ^ -6086584784184652666L) + 8745578844518034079L;
                     }
                  } else if (b <= 39) {
                     KeccakSha3Digest keccakSha3Digest5 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes20 = new byte[]{
                        20,
                        -65,
                        -21,
                        30,
                        -26,
                        -67,
                        113,
                        50,
                        -13,
                        -53,
                        3,
                        78,
                        -28,
                        125,
                        -86,
                        77,
                        11,
                        -6,
                        14,
                        104,
                        115,
                        -109,
                        30,
                        31,
                        24,
                        -91,
                        -111,
                        28,
                        -82,
                        -54,
                        39,
                        -125
                     };
                     this.derivedKeySlot05 = keccakSha3Digest5.digest(VendorAesHelper.encryptWithCipher(bytes, bytes20, paddedAesCipher));
                     l = (l ^ -5443626903698008335L) + -6361847033612104176L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest6 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes24 = new byte[]{
                        122,
                        -14,
                        -35,
                        36,
                        61,
                        -33,
                        113,
                        110,
                        127,
                        67,
                        103,
                        104,
                        -120,
                        -62,
                        -75,
                        0,
                        89,
                        39,
                        -24,
                        -39,
                        -90,
                        -55,
                        -26,
                        68,
                        -96,
                        -27,
                        -103,
                        112,
                        -92,
                        107,
                        -43,
                        34
                     };
                     this.derivedKeySlot06 = keccakSha3Digest6.digest(VendorAesHelper.encryptWithCipher(bytes, bytes24, paddedAesCipher));
                     l = l - 2886526223397598871L ^ -2862852651309645764L;
                  }
                  continue;
               }

               if (b <= 44) {
                  KeccakSha3Digest keccakSha3Digest7 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes28 = new byte[]{
                     -20,
                     77,
                     -12,
                     83,
                     4,
                     39,
                     8,
                     37,
                     -66,
                     92,
                     85,
                     77,
                     -62,
                     116,
                     28,
                     -39,
                     -30,
                     42,
                     -15,
                     91,
                     -101,
                     -95,
                     37,
                     18,
                     -49,
                     -128,
                     102,
                     -16,
                     -122,
                     10,
                     19,
                     -42
                  };
                  this.derivedKeySlot07 = keccakSha3Digest7.digest(VendorAesHelper.encryptWithCipher(bytes, bytes28, paddedAesCipher));
                  l = l + 3184355357443094461L ^ 5468915652344379489L;
               } else if (47 <= b) {
                  if (47 >= b) {
                     paddedAesCipher = new PaddedAesCipher();
                     l = (l ^ -4882022528614416043L) + -5448041150498918483L;
                     continue;
                  }

                  if (49 > b) {
                     KeccakSha3Digest keccakSha3Digest8 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes32 = new byte[]{
                        -114,
                        42,
                        -104,
                        87,
                        36,
                        -80,
                        45,
                        -94,
                        2,
                        -111,
                        -22,
                        -39,
                        24,
                        -126,
                        -44,
                        -1,
                        47,
                        4,
                        -87,
                        125,
                        104,
                        19,
                        120,
                        -126,
                        -22,
                        1,
                        -112,
                        -105,
                        86,
                        113,
                        75,
                        -98
                     };
                     this.derivedKeySlot08 = keccakSha3Digest8.digest(VendorAesHelper.encryptWithCipher(bytes, bytes32, paddedAesCipher));
                     l = (l ^ -8766319943157393869L) - -5140198430257576052L;
                     continue;
                  }

                  if (b <= 49) {
                     KeccakSha3Digest keccakSha3Digest9 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes36 = new byte[]{
                        -62,
                        12,
                        -77,
                        -115,
                        -5,
                        78,
                        -62,
                        69,
                        -45,
                        49,
                        23,
                        27,
                        -50,
                        13,
                        28,
                        51,
                        -108,
                        -84,
                        -128,
                        -47,
                        119,
                        54,
                        120,
                        55,
                        -15,
                        -96,
                        -31,
                        121,
                        85,
                        -96,
                        26,
                        34
                     };
                     this.derivedKeySlot09 = keccakSha3Digest9.digest(VendorAesHelper.encryptWithCipher(bytes, bytes36, paddedAesCipher));
                     l = l - 7486053226877681399L ^ 8735820390119532007L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest10 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes40 = new byte[]{
                        -108,
                        77,
                        41,
                        42,
                        -120,
                        -100,
                        -104,
                        -36,
                        -97,
                        -110,
                        -35,
                        -89,
                        -118,
                        54,
                        57,
                        113,
                        42,
                        33,
                        104,
                        65,
                        -107,
                        21,
                        17,
                        74,
                        76,
                        -40,
                        118,
                        18,
                        -29,
                        -89,
                        -52,
                        -71
                     };
                     this.derivedKeySlot10 = keccakSha3Digest10.digest(VendorAesHelper.encryptWithCipher(bytes, bytes40, paddedAesCipher));
                     l = (l ^ 189849871555242266L) + 3490801930711997452L;
                  }
               } else {
                  if (45 >= b) {
                     KeccakSha3Digest keccakSha3Digest11 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes44 = new byte[]{
                        -108,
                        109,
                        -56,
                        -32,
                        -91,
                        -112,
                        -22,
                        -107,
                        -91,
                        67,
                        122,
                        34,
                        -85,
                        -71,
                        57,
                        119,
                        -61,
                        102,
                        99,
                        9,
                        -110,
                        60,
                        -101,
                        36,
                        109,
                        11,
                        5,
                        -127,
                        81,
                        -119,
                        -55,
                        -59
                     };
                     this.derivedKeySlot11 = keccakSha3Digest11.digest(VendorAesHelper.encryptWithCipher(bytes, bytes44, paddedAesCipher));
                     l = l + 7610156650908885949L ^ -5925937795302082368L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest12 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes48 = new byte[]{
                     46,
                     87,
                     37,
                     49,
                     -110,
                     79,
                     -124,
                     96,
                     -59,
                     72,
                     -50,
                     65,
                     92,
                     -87,
                     101,
                     -46,
                     34,
                     -105,
                     83,
                     -90,
                     -34,
                     -97,
                     -21,
                     -40,
                     16,
                     -53,
                     -9,
                     -74,
                     -123,
                     -126,
                     42,
                     -60
                  };
                  this.derivedKeySlot12 = keccakSha3Digest12.digest(VendorAesHelper.encryptWithCipher(bytes, bytes48, paddedAesCipher));
                  l = l - 125628667910497120L ^ 5206652609121481855L;
               }
            } else if (31 <= b) {
               if (b <= 31) {
                  KeccakSha3Digest keccakSha3Digest13 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes52 = new byte[]{
                     43,
                     96,
                     115,
                     -85,
                     -23,
                     54,
                     116,
                     13,
                     -14,
                     -6,
                     23,
                     124,
                     83,
                     71,
                     121,
                     -20,
                     -60,
                     -9,
                     -114,
                     27,
                     -97,
                     118,
                     -34,
                     -9,
                     -53,
                     30,
                     -63,
                     103,
                     -11,
                     -44,
                     -73,
                     -108
                  };
                  this.derivedKeySlot13 = keccakSha3Digest13.digest(VendorAesHelper.encryptWithCipher(bytes, bytes52, paddedAesCipher));
                  l = (l ^ 873120180498542534L) + 3877039850619823359L;
               } else if (b >= 34) {
                  if (b <= 34) {
                     KeccakSha3Digest keccakSha3Digest14 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes56 = new byte[]{
                        -86,
                        109,
                        78,
                        2,
                        -5,
                        -11,
                        -24,
                        22,
                        -93,
                        -30,
                        95,
                        -19,
                        -47,
                        39,
                        9,
                        8,
                        59,
                        120,
                        -95,
                        85,
                        -53,
                        -115,
                        81,
                        29,
                        64,
                        -117,
                        105,
                        -57,
                        63,
                        -32,
                        93,
                        -58
                     };
                     this.derivedKeySlot14 = keccakSha3Digest14.digest(VendorAesHelper.encryptWithCipher(bytes, bytes56, paddedAesCipher));
                     l = l - -225490506344099116L ^ 5551965864970469809L;
                     continue;
                  }

                  if (36 > b) {
                     KeccakSha3Digest keccakSha3Digest15 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes60 = new byte[]{
                        46,
                        -14,
                        31,
                        90,
                        -73,
                        78,
                        -37,
                        -44,
                        -59,
                        27,
                        104,
                        96,
                        -123,
                        53,
                        17,
                        -94,
                        -121,
                        110,
                        -16,
                        -32,
                        96,
                        16,
                        105,
                        -72,
                        116,
                        41,
                        -102,
                        -79,
                        52,
                        115,
                        -92,
                        9
                     };
                     this.derivedKeySlot15 = keccakSha3Digest15.digest(VendorAesHelper.encryptWithCipher(bytes, bytes60, paddedAesCipher));
                     l = l + 9077750864099779185L ^ -6491858618055810056L;
                     continue;
                  }

                  if (36 >= b) {
                     KeccakSha3Digest keccakSha3Digest16 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes64 = new byte[]{
                        -77,
                        86,
                        -88,
                        28,
                        -53,
                        -41,
                        45,
                        -51,
                        -23,
                        82,
                        98,
                        -6,
                        -127,
                        49,
                        77,
                        27,
                        127,
                        -51,
                        -87,
                        -101,
                        22,
                        118,
                        -19,
                        -14,
                        31,
                        -9,
                        -127,
                        -69,
                        100,
                        104,
                        42,
                        95
                     };
                     this.derivedKeySlot16 = keccakSha3Digest16.digest(VendorAesHelper.encryptWithCipher(bytes, bytes64, paddedAesCipher));
                     l = (l ^ -7077489625802973675L) - -7895681267473729205L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest17 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes68 = new byte[]{
                        -109,
                        29,
                        63,
                        92,
                        72,
                        37,
                        -26,
                        29,
                        7,
                        -8,
                        62,
                        -20,
                        -61,
                        -23,
                        67,
                        27,
                        2,
                        -79,
                        106,
                        -39,
                        -62,
                        -23,
                        -24,
                        55,
                        127,
                        -111,
                        -100,
                        -41,
                        -114,
                        111,
                        -51,
                        -62
                     };
                     this.derivedKeySlot17 = keccakSha3Digest17.digest(VendorAesHelper.encryptWithCipher(bytes, bytes68, paddedAesCipher));
                     l = (l ^ 4248716629333743191L) + -8908082058372610808L;
                  }
               } else if (32 >= b) {
                  KeccakSha3Digest keccakSha3Digest18 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes72 = new byte[]{
                     -50,
                     116,
                     12,
                     78,
                     -55,
                     120,
                     115,
                     -38,
                     35,
                     -17,
                     74,
                     -118,
                     79,
                     -20,
                     99,
                     -52,
                     -27,
                     -17,
                     -62,
                     -45,
                     -118,
                     -50,
                     103,
                     -72,
                     14,
                     -85,
                     -61,
                     0,
                     -93,
                     51,
                     -16,
                     -15
                  };
                  this.derivedKeySlot18 = keccakSha3Digest18.digest(VendorAesHelper.encryptWithCipher(bytes, bytes72, paddedAesCipher));
                  l = l + 8128133054283252902L ^ -1650876452330793859L;
               } else {
                  KeccakSha3Digest keccakSha3Digest19 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes76 = new byte[]{
                     105,
                     -82,
                     -1,
                     87,
                     -2,
                     108,
                     -44,
                     76,
                     -120,
                     -21,
                     61,
                     97,
                     126,
                     -32,
                     55,
                     -87,
                     73,
                     -56,
                     86,
                     108,
                     -28,
                     -92,
                     108,
                     69,
                     106,
                     56,
                     91,
                     -98,
                     -40,
                     119,
                     2,
                     28
                  };
                  this.derivedKeySlot19 = keccakSha3Digest19.digest(VendorAesHelper.encryptWithCipher(bytes, bytes76, paddedAesCipher));
                  l = (l ^ 7366489583795934338L) - -1361473119583814833L;
               }
            } else if (28 <= b) {
               if (b <= 28) {
                  KeccakSha3Digest keccakSha3Digest20 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes80 = new byte[]{
                     -78,
                     3,
                     61,
                     -89,
                     -1,
                     -110,
                     94,
                     95,
                     106,
                     50,
                     -113,
                     65,
                     -90,
                     33,
                     96,
                     84,
                     -74,
                     84,
                     -125,
                     -56,
                     110,
                     11,
                     97,
                     19,
                     24,
                     -64,
                     60,
                     -59,
                     51,
                     -27,
                     39,
                     73
                  };
                  this.derivedKeySlot20 = keccakSha3Digest20.digest(VendorAesHelper.encryptWithCipher(bytes, bytes80, paddedAesCipher));
                  l = l - 8073906939900875484L ^ -2594727721009245393L;
                  continue;
               }

               if (b <= 29) {
                  KeccakSha3Digest keccakSha3Digest21 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes84 = new byte[]{
                     31,
                     90,
                     -51,
                     13,
                     63,
                     -112,
                     97,
                     111,
                     -120,
                     -124,
                     -37,
                     115,
                     -115,
                     -51,
                     -68,
                     -26,
                     117,
                     -52,
                     15,
                     92,
                     -95,
                     -108,
                     127,
                     52,
                     19,
                     9,
                     -33,
                     -78,
                     -3,
                     -7,
                     111,
                     -74
                  };
                  this.derivedKeySlot21 = keccakSha3Digest21.digest(VendorAesHelper.encryptWithCipher(bytes, bytes84, paddedAesCipher));
                  l = l + -7231479605387399129L ^ -7938462242972747483L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest22 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes88 = new byte[]{
                  -51,
                  -56,
                  -52,
                  75,
                  -100,
                  13,
                  120,
                  98,
                  50,
                  108,
                  122,
                  22,
                  46,
                  -101,
                  -81,
                  93,
                  -93,
                  -121,
                  17,
                  -122,
                  86,
                  108,
                  124,
                  114,
                  82,
                  120,
                  -115,
                  -40,
                  -50,
                  -114,
                  48,
                  -53
               };
               this.derivedKeySlot22 = keccakSha3Digest22.digest(VendorAesHelper.encryptWithCipher(bytes, bytes88, paddedAesCipher));
               l = l + -3980569956796132083L ^ -7165037192310611010L;
            } else {
               if (26 >= b) {
                  KeccakSha3Digest keccakSha3Digest23 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes92 = new byte[]{
                     -102,
                     31,
                     -20,
                     91,
                     -3,
                     113,
                     -12,
                     41,
                     -80,
                     -31,
                     76,
                     100,
                     127,
                     -19,
                     46,
                     -109,
                     82,
                     -8,
                     87,
                     -118,
                     -1,
                     -60,
                     -2,
                     -88,
                     58,
                     -46,
                     84,
                     -25,
                     90,
                     -69,
                     -56,
                     3
                  };
                  this.derivedKeySlot23 = keccakSha3Digest23.digest(VendorAesHelper.encryptWithCipher(bytes, bytes92, paddedAesCipher));
                  l = (l ^ -1790763073094895715L) - 8014175766422707724L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest24 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes96 = new byte[]{
                  -84,
                  55,
                  -50,
                  -92,
                  -57,
                  -95,
                  -105,
                  8,
                  108,
                  31,
                  126,
                  95,
                  22,
                  97,
                  -43,
                  62,
                  14,
                  40,
                  82,
                  -54,
                  82,
                  -1,
                  -23,
                  -38,
                  -95,
                  115,
                  4,
                  75,
                  73,
                  -36,
                  -115,
                  -54
               };
               this.derivedKeySlot24 = keccakSha3Digest24.digest(VendorAesHelper.encryptWithCipher(bytes, bytes96, paddedAesCipher));
               l = (l ^ -5261185803717542704L) - 3899191578128909814L;
            }
         } else if (b >= 12) {
            if (b <= 12) {
               KeccakSha3Digest keccakSha3Digest25 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes100 = new byte[]{
                  83,
                  -78,
                  116,
                  -77,
                  -10,
                  71,
                  -111,
                  -89,
                  -48,
                  -110,
                  49,
                  46,
                  -122,
                  -109,
                  -27,
                  -70,
                  13,
                  -83,
                  41,
                  47,
                  48,
                  -38,
                  14,
                  50,
                  -5,
                  64,
                  -44,
                  -105,
                  -39,
                  -125,
                  -122,
                  18
               };
               this.derivedKeySlot25 = keccakSha3Digest25.digest(VendorAesHelper.encryptWithCipher(bytes, bytes100, paddedAesCipher));
               l = l - 8813802686759405560L ^ -540288738524456805L;
            } else if (18 <= b) {
               if (18 >= b) {
                  KeccakSha3Digest keccakSha3Digest26 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes104 = new byte[]{
                     -127,
                     120,
                     -121,
                     -32,
                     110,
                     50,
                     -37,
                     111,
                     9,
                     -124,
                     37,
                     123,
                     -36,
                     -32,
                     53,
                     76,
                     -116,
                     -83,
                     -70,
                     108,
                     -39,
                     16,
                     -103,
                     -86,
                     -97,
                     -3,
                     -3,
                     26,
                     3,
                     -115,
                     -52,
                     -38
                  };
                  this.derivedKeySlot26 = keccakSha3Digest26.digest(VendorAesHelper.encryptWithCipher(bytes, bytes104, paddedAesCipher));
                  l = l - 8719504739085597584L ^ -3133133899361448269L;
                  continue;
               }

               if (b >= 21) {
                  if (b <= 21) {
                     KeccakSha3Digest keccakSha3Digest27 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes108 = new byte[]{
                        -52,
                        -122,
                        -98,
                        2,
                        -127,
                        72,
                        123,
                        103,
                        -68,
                        15,
                        -104,
                        85,
                        106,
                        -96,
                        99,
                        122,
                        75,
                        -64,
                        93,
                        102,
                        -79,
                        98,
                        -65,
                        72,
                        99,
                        69,
                        -15,
                        -45,
                        -124,
                        103,
                        -70,
                        -98
                     };
                     this.derivedKeySlot27 = keccakSha3Digest27.digest(VendorAesHelper.encryptWithCipher(bytes, bytes108, paddedAesCipher));
                     l = (l ^ 7312216799125809126L) + 1180675918475195939L;
                  } else {
                     if (23 > b) {
                        KeccakSha3Digest keccakSha3Digest28 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes112 = new byte[]{
                           84,
                           103,
                           -27,
                           104,
                           51,
                           111,
                           -99,
                           35,
                           53,
                           -69,
                           100,
                           -40,
                           -25,
                           32,
                           12,
                           100,
                           97,
                           104,
                           -11,
                           -91,
                           70,
                           125,
                           -24,
                           -109,
                           -36,
                           44,
                           -20,
                           -57,
                           26,
                           -122,
                           51,
                           33
                        };
                        this.derivedKeySlot28 = keccakSha3Digest28.digest(VendorAesHelper.encryptWithCipher(bytes, bytes112, paddedAesCipher));
                        l = l + -6811564346603068302L ^ -3601034175418034572L;
                        continue;
                     }

                     if (b <= 23) {
                        this.sessionKey = bytes;
                        l = (l ^ -1403373750608748845L) - 7979083369784887779L;
                        continue;
                     }

                     KeccakSha3Digest keccakSha3Digest29 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes116 = new byte[]{
                        -61,
                        -7,
                        -12,
                        -43,
                        17,
                        117,
                        -55,
                        82,
                        -4,
                        -63,
                        123,
                        -87,
                        -79,
                        24,
                        -103,
                        93,
                        -50,
                        7,
                        116,
                        -54,
                        -29,
                        -116,
                        -30,
                        -122,
                        -59,
                        -40,
                        120,
                        12,
                        -53,
                        35,
                        45,
                        66
                     };
                     this.derivedKeySlot29 = keccakSha3Digest29.digest(VendorAesHelper.encryptWithCipher(bytes, bytes116, paddedAesCipher));
                     l = l - 4048983034484354040L ^ -4174544593793019441L;
                  }
               } else {
                  if (19 >= b) {
                     KeccakSha3Digest keccakSha3Digest30 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes120 = new byte[]{
                        34,
                        69,
                        -85,
                        -126,
                        123,
                        -8,
                        -123,
                        85,
                        -62,
                        -93,
                        -94,
                        -120,
                        -31,
                        -66,
                        17,
                        -10,
                        -38,
                        -123,
                        111,
                        55,
                        -100,
                        92,
                        -13,
                        38,
                        -65,
                        35,
                        -15,
                        -57,
                        -104,
                        -8,
                        -9,
                        -87
                     };
                     this.derivedKeySlot30 = keccakSha3Digest30.digest(VendorAesHelper.encryptWithCipher(bytes, bytes120, paddedAesCipher));
                     l = (l ^ -5284200985973413893L) + -8670625457357828720L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest31 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes124 = new byte[]{
                     -50,
                     43,
                     55,
                     -76,
                     -114,
                     94,
                     114,
                     -127,
                     -43,
                     35,
                     -112,
                     85,
                     103,
                     70,
                     48,
                     70,
                     85,
                     -18,
                     -54,
                     85,
                     -119,
                     -81,
                     34,
                     -14,
                     66,
                     -80,
                     60,
                     -103,
                     -54,
                     79,
                     -52,
                     -91
                  };
                  this.derivedKeySlot31 = keccakSha3Digest31.digest(VendorAesHelper.encryptWithCipher(bytes, bytes124, paddedAesCipher));
                  l = (l ^ 1680340093077847244L) + 948615332162827879L;
               }
            } else if (15 <= b) {
               if (15 >= b) {
                  KeccakSha3Digest keccakSha3Digest32 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes128 = new byte[]{
                     -87,
                     -36,
                     30,
                     111,
                     27,
                     -110,
                     69,
                     115,
                     -13,
                     -123,
                     -101,
                     -54,
                     -29,
                     -112,
                     -38,
                     -30,
                     112,
                     96,
                     50,
                     81,
                     -62,
                     58,
                     110,
                     54,
                     -43,
                     -115,
                     -22,
                     102,
                     79,
                     17,
                     -2,
                     -106
                  };
                  this.derivedKeySlot32 = keccakSha3Digest32.digest(VendorAesHelper.encryptWithCipher(bytes, bytes128, paddedAesCipher));
                  l = l - -4803352751775711281L ^ -7017582372186900424L;
                  continue;
               }

               if (b > 16) {
                  KeccakSha3Digest keccakSha3Digest33 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes132 = new byte[]{
                     55,
                     -48,
                     -38,
                     120,
                     -9,
                     48,
                     -37,
                     107,
                     124,
                     67,
                     -39,
                     -19,
                     -58,
                     -118,
                     1,
                     -60,
                     40,
                     19,
                     29,
                     58,
                     -58,
                     -64,
                     82,
                     66,
                     -34,
                     78,
                     12,
                     69,
                     123,
                     -101,
                     76,
                     101
                  };
                  this.derivedKeySlot33 = keccakSha3Digest33.digest(VendorAesHelper.encryptWithCipher(bytes, bytes132, paddedAesCipher));
                  l = (l ^ 113874105424259239L) + 1354302808954065730L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest34 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes136 = new byte[]{
                  -9,
                  -26,
                  122,
                  40,
                  124,
                  -24,
                  101,
                  100,
                  118,
                  78,
                  -53,
                  112,
                  98,
                  -85,
                  -95,
                  30,
                  12,
                  -82,
                  -41,
                  -109,
                  -38,
                  123,
                  38,
                  -60,
                  -93,
                  15,
                  111,
                  -1,
                  67,
                  102,
                  -123,
                  -61
               };
               this.derivedKeySlot34 = keccakSha3Digest34.digest(VendorAesHelper.encryptWithCipher(bytes, bytes136, paddedAesCipher));
               l = l + -6935179138873746534L ^ -4905010499787741476L;
            } else {
               if (b > 13) {
                  KeccakSha3Digest keccakSha3Digest35 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes142 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
                     -101, 87, 107, -82, -49, -49, 71, 76, 86, 10, 108, -22, 0, -45, 88, 8, 29, 12, 125, -96, 36, 14, 70, 58, 124, 84, 82, 70, 55, -52, -67, 3
                  }, paddedAesCipher);
                  this.derivedKeySlot35 = keccakSha3Digest35.digest(bytes142);
                  l = (l ^ 6008759964626989977L) - -998787310066307819L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest36 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes144 = new byte[]{
                  122,
                  -28,
                  -70,
                  92,
                  111,
                  28,
                  -116,
                  -104,
                  103,
                  61,
                  40,
                  86,
                  120,
                  90,
                  -94,
                  -63,
                  24,
                  89,
                  44,
                  45,
                  67,
                  100,
                  66,
                  8,
                  -34,
                  75,
                  -114,
                  -124,
                  120,
                  104,
                  121,
                  -90
               };
               this.derivedKeySlot36 = keccakSha3Digest36.digest(VendorAesHelper.encryptWithCipher(bytes, bytes144, paddedAesCipher));
               l = (l ^ -8500932627566635718L) + 560050672781346520L;
            }
         } else if (b >= 5) {
            if (b <= 5) {
               KeccakSha3Digest keccakSha3Digest37 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes148 = new byte[]{
                  -58,
                  -31,
                  -111,
                  -7,
                  -9,
                  -62,
                  31,
                  8,
                  -40,
                  -43,
                  -7,
                  1,
                  100,
                  -78,
                  -95,
                  -38,
                  -123,
                  -92,
                  80,
                  -70,
                  -64,
                  -22,
                  0,
                  36,
                  -37,
                  -80,
                  93,
                  -81,
                  114,
                  72,
                  5,
                  -35
               };
               this.derivedKeySlot37 = keccakSha3Digest37.digest(VendorAesHelper.encryptWithCipher(bytes, bytes148, paddedAesCipher));
               l = l + -6938838675492703055L ^ 2012766590128463207L;
               continue;
            }

            if (8 > b) {
               if (6 >= b) {
                  KeccakSha3Digest keccakSha3Digest38 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes152 = new byte[]{
                     31,
                     -92,
                     -117,
                     -84,
                     -58,
                     -23,
                     -89,
                     51,
                     -54,
                     -103,
                     -17,
                     3,
                     -53,
                     -26,
                     -123,
                     75,
                     -30,
                     44,
                     122,
                     29,
                     -44,
                     -64,
                     -107,
                     85,
                     23,
                     62,
                     -24,
                     -74,
                     -104,
                     -83,
                     -104,
                     59
                  };
                  this.derivedKeySlot38 = keccakSha3Digest38.digest(VendorAesHelper.encryptWithCipher(bytes, bytes152, paddedAesCipher));
                  l = (l ^ -1996229608565800625L) - -7363427880598211234L;
               } else {
                  KeccakSha3Digest keccakSha3Digest39 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes156 = new byte[]{
                     -47,
                     -62,
                     -11,
                     -67,
                     106,
                     100,
                     -21,
                     52,
                     -24,
                     -16,
                     75,
                     69,
                     32,
                     -121,
                     94,
                     39,
                     -112,
                     6,
                     -10,
                     46,
                     -95,
                     -1,
                     -100,
                     33,
                     -97,
                     -127,
                     -42,
                     -108,
                     104,
                     -33,
                     87,
                     61
                  };
                  this.derivedKeySlot39 = keccakSha3Digest39.digest(VendorAesHelper.encryptWithCipher(bytes, bytes156, paddedAesCipher));
                  l = (l ^ -3035639503870932685L) + -7244848672014804359L;
               }
               continue;
            }

            if (8 >= b) {
               KeccakSha3Digest keccakSha3Digest40 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes160 = new byte[]{
                  27,
                  32,
                  -113,
                  3,
                  122,
                  122,
                  -20,
                  -29,
                  75,
                  -44,
                  14,
                  -51,
                  60,
                  -84,
                  53,
                  92,
                  -9,
                  59,
                  71,
                  -128,
                  -32,
                  -21,
                  -1,
                  -82,
                  10,
                  -70,
                  -92,
                  -98,
                  119,
                  125,
                  -105,
                  31
               };
               this.derivedKeySlot40 = keccakSha3Digest40.digest(VendorAesHelper.encryptWithCipher(bytes, bytes160, paddedAesCipher));
               l = l - 1694498128998238761L ^ 4468072981877926161L;
               continue;
            }

            if (b < 10) {
               KeccakSha3Digest keccakSha3Digest41 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes164 = new byte[]{
                  40,
                  23,
                  99,
                  -124,
                  -20,
                  79,
                  -115,
                  -29,
                  40,
                  -10,
                  -71,
                  65,
                  31,
                  -58,
                  34,
                  17,
                  -75,
                  -122,
                  -122,
                  13,
                  -88,
                  -90,
                  -120,
                  -81,
                  108,
                  5,
                  42,
                  -114,
                  -57,
                  -121,
                  14,
                  -94
               };
               this.secondaryKeyGuard = keccakSha3Digest41.digest(VendorAesHelper.encryptWithCipher(bytes, bytes164, paddedAesCipher));
               l = (l ^ 8855283646815473689L) + -2616995745558547220L;
               continue;
            }

            if (b <= 10) {
               return;
            }

            KeccakSha3Digest keccakSha3Digest42 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes168 = new byte[]{
               70,
               82,
               -65,
               -123,
               -106,
               -96,
               -106,
               86,
               -87,
               -65,
               -46,
               45,
               -113,
               100,
               60,
               -25,
               13,
               -95,
               -15,
               -122,
               -44,
               2,
               28,
               113,
               9,
               -62,
               -35,
               -54,
               -14,
               -27,
               69,
               75
            };
            this.derivedKeySlot41 = keccakSha3Digest42.digest(VendorAesHelper.encryptWithCipher(bytes, bytes168, paddedAesCipher));
            l = l - 4029031261259158452L ^ 9052821739742191822L;
         } else if (b >= 2) {
            if (2 >= b) {
               KeccakSha3Digest keccakSha3Digest43 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes172 = new byte[]{
                  44,
                  -105,
                  -64,
                  125,
                  48,
                  22,
                  123,
                  -108,
                  41,
                  103,
                  106,
                  2,
                  -105,
                  -117,
                  74,
                  -36,
                  -122,
                  104,
                  4,
                  -15,
                  -33,
                  61,
                  33,
                  -104,
                  -25,
                  5,
                  -120,
                  115,
                  -35,
                  97,
                  3,
                  -31
               };
               this.derivedKeySlot42 = keccakSha3Digest43.digest(VendorAesHelper.encryptWithCipher(bytes, bytes172, paddedAesCipher));
               l = l + -283055625113894877L ^ 8650158698201028454L;
               continue;
            }

            if (b <= 3) {
               KeccakSha3Digest keccakSha3Digest44 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes176 = new byte[]{
                  -54,
                  14,
                  90,
                  -5,
                  -89,
                  33,
                  -64,
                  -78,
                  112,
                  -22,
                  45,
                  -75,
                  62,
                  -56,
                  -52,
                  -105,
                  64,
                  53,
                  25,
                  98,
                  -88,
                  -14,
                  -25,
                  103,
                  0,
                  -59,
                  -73,
                  83,
                  115,
                  -72,
                  52,
                  -99
               };
               this.derivedKeySlot43 = keccakSha3Digest44.digest(VendorAesHelper.encryptWithCipher(bytes, bytes176, paddedAesCipher));
               l = l + -7239005441302934305L ^ -5018388653659898357L;
               continue;
            }

            KeccakSha3Digest keccakSha3Digest45 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes180 = new byte[]{
               -95,
               -45,
               -82,
               -66,
               -13,
               -52,
               10,
               -26,
               19,
               9,
               63,
               79,
               -105,
               -12,
               -15,
               -58,
               6,
               84,
               38,
               -4,
               -95,
               67,
               119,
               53,
               45,
               -125,
               -92,
               -96,
               -14,
               81,
               111,
               -36
            };
            this.derivedKeySlot44 = keccakSha3Digest45.digest(VendorAesHelper.encryptWithCipher(bytes, bytes180, paddedAesCipher));
            l = l + -6124472867465888915L ^ -6966369579926273813L;
         } else if (b <= 0) {
            KeccakSha3Digest keccakSha3Digest46 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes186 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
               67, 1, -120, 98, -76, -73, 29, 53, -77, 38, -54, 12, 55, 78, -80, 2, 70, 52, 70, -65, 29, 105, -89, -119, 0, -45, -53, 122, -100, 50, -97, -7
            }, paddedAesCipher);
            this.derivedKeySlot45 = keccakSha3Digest46.digest(bytes186);
            l = l + 1683610966631535982L ^ -1707229660944579251L;
         } else {
            KeccakSha3Digest keccakSha3Digest47 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes188 = new byte[]{
               105,
               58,
               -10,
               102,
               24,
               -20,
               37,
               42,
               75,
               -65,
               -38,
               -49,
               -85,
               -111,
               23,
               -103,
               -106,
               -14,
               -113,
               72,
               86,
               93,
               7,
               -29,
               47,
               -49,
               -39,
               -91,
               79,
               -3,
               -4,
               108
            };
            this.derivedKeySlot46 = keccakSha3Digest47.digest(VendorAesHelper.encryptWithCipher(bytes, bytes188, paddedAesCipher));
            l = l + 4333963885021334451L ^ -797277440342553928L;
         }

         while (51 < b) {
            if (b >= 77) {
               if (b <= 77) {
                  KeccakSha3Digest keccakSha3Digest48 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes192 = new byte[]{
                     -43,
                     -68,
                     -40,
                     97,
                     10,
                     -84,
                     22,
                     68,
                     43,
                     70,
                     64,
                     18,
                     77,
                     -111,
                     -26,
                     -23,
                     36,
                     23,
                     -45,
                     -83,
                     -37,
                     16,
                     -15,
                     -14,
                     70,
                     8,
                     -108,
                     98,
                     75,
                     36,
                     -34,
                     58
                  };
                  this.derivedKeySlot47 = keccakSha3Digest48.digest(VendorAesHelper.encryptWithCipher(bytes, bytes192, paddedAesCipher));
                  l = l - 3406417711381485331L ^ 819818607623390637L;
               } else if (90 <= b) {
                  if (b <= 90) {
                     KeccakSha3Digest keccakSha3Digest49 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes196 = new byte[]{
                        -61,
                        59,
                        5,
                        -113,
                        -33,
                        10,
                        -28,
                        24,
                        113,
                        -31,
                        -118,
                        94,
                        -81,
                        -111,
                        119,
                        74,
                        -108,
                        -99,
                        -98,
                        36,
                        -126,
                        81,
                        10,
                        -122,
                        27,
                        -4,
                        -105,
                        116,
                        -18,
                        -5,
                        -108,
                        -1
                     };
                     this.derivedKeySlot48 = keccakSha3Digest49.digest(VendorAesHelper.encryptWithCipher(bytes, bytes196, paddedAesCipher));
                     l = (l ^ -9137993053106010746L) + 5132707113629159102L;
                     continue outerLoop;
                  }

                  if (97 <= b) {
                     if (b <= 97) {
                        KeccakSha3Digest keccakSha3Digest50 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes200 = new byte[]{
                           -49,
                           104,
                           -49,
                           -11,
                           -127,
                           -31,
                           -74,
                           100,
                           14,
                           56,
                           -127,
                           0,
                           66,
                           -106,
                           -49,
                           99,
                           -61,
                           9,
                           -71,
                           -104,
                           -55,
                           110,
                           81,
                           -99,
                           56,
                           105,
                           84,
                           5,
                           -28,
                           20,
                           -72,
                           103
                        };
                        this.derivedKeySlot49 = keccakSha3Digest50.digest(VendorAesHelper.encryptWithCipher(bytes, bytes200, paddedAesCipher));
                        l = l - 5343226510591856116L ^ 6244669255654791707L;
                     } else {
                        if (100 > b) {
                           if (98 >= b) {
                              KeccakSha3Digest keccakSha3Digest51 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                              byte[] bytes204 = new byte[]{
                                 -13,
                                 -117,
                                 56,
                                 -119,
                                 81,
                                 -103,
                                 -115,
                                 20,
                                 93,
                                 -81,
                                 -20,
                                 -64,
                                 120,
                                 -116,
                                 -28,
                                 87,
                                 65,
                                 46,
                                 103,
                                 -114,
                                 -80,
                                 -6,
                                 39,
                                 21,
                                 -114,
                                 -8,
                                 44,
                                 -76,
                                 -126,
                                 119,
                                 35,
                                 -39
                              };
                              this.derivedKeySlot50 = keccakSha3Digest51.digest(VendorAesHelper.encryptWithCipher(bytes, bytes204, paddedAesCipher));
                              l = l + -8699296192673395176L ^ -6496098017169605932L;
                           } else {
                              KeccakSha3Digest keccakSha3Digest52 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                              byte[] bytes208 = new byte[]{
                                 -32,
                                 69,
                                 -51,
                                 -25,
                                 33,
                                 -53,
                                 86,
                                 -98,
                                 27,
                                 15,
                                 67,
                                 51,
                                 39,
                                 72,
                                 62,
                                 -122,
                                 11,
                                 105,
                                 -99,
                                 86,
                                 -71,
                                 -4,
                                 48,
                                 18,
                                 41,
                                 70,
                                 -97,
                                 71,
                                 -10,
                                 -105,
                                 -120,
                                 44
                              };
                              this.derivedKeySlot51 = keccakSha3Digest52.digest(VendorAesHelper.encryptWithCipher(bytes, bytes208, paddedAesCipher));
                              l = (l ^ 3142967913909394611L) - -9021307274110066069L;
                           }
                           continue outerLoop;
                        }

                        if (100 >= b) {
                           KeccakSha3Digest keccakSha3Digest53 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes212 = new byte[]{
                              101,
                              -123,
                              8,
                              -11,
                              -104,
                              -82,
                              120,
                              -97,
                              54,
                              -10,
                              -69,
                              83,
                              -99,
                              -126,
                              -8,
                              -115,
                              -65,
                              86,
                              80,
                              86,
                              2,
                              66,
                              -120,
                              -60,
                              -1,
                              -128,
                              84,
                              70,
                              -77,
                              -107,
                              -68,
                              -128
                           };
                           this.derivedKeySlot52 = keccakSha3Digest53.digest(VendorAesHelper.encryptWithCipher(bytes, bytes212, paddedAesCipher));
                           l = l - 2914291794978701001L ^ 7083843270684735681L;
                        } else {
                           if (b >= 102) {
                              if (b <= 102) {
                                 KeccakSha3Digest keccakSha3Digest54 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                                 byte[] bytes216 = new byte[]{
                                    -107,
                                    99,
                                    17,
                                    20,
                                    -99,
                                    -115,
                                    -107,
                                    108,
                                    88,
                                    72,
                                    107,
                                    52,
                                    62,
                                    93,
                                    59,
                                    47,
                                    116,
                                    32,
                                    92,
                                    16,
                                    -99,
                                    -87,
                                    61,
                                    -81,
                                    48,
                                    -89,
                                    105,
                                    72,
                                    -16,
                                    -32,
                                    48,
                                    -50
                                 };
                                 this.derivedKeySlot53 = keccakSha3Digest54.digest(VendorAesHelper.encryptWithCipher(bytes, bytes216, paddedAesCipher));
                                 l = l + 358634119851431315L ^ 4164212047796003801L;
                              } else {
                                 KeccakSha3Digest keccakSha3Digest55 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                                 byte[] bytes220 = new byte[]{
                                    -41,
                                    13,
                                    -96,
                                    1,
                                    -62,
                                    106,
                                    1,
                                    108,
                                    -46,
                                    -50,
                                    109,
                                    -63,
                                    -95,
                                    98,
                                    -95,
                                    -126,
                                    74,
                                    -71,
                                    121,
                                    85,
                                    -111,
                                    71,
                                    -68,
                                    -61,
                                    38,
                                    79,
                                    -118,
                                    -68,
                                    -69,
                                    94,
                                    127,
                                    1
                                 };
                                 this.derivedKeySlot54 = keccakSha3Digest55.digest(VendorAesHelper.encryptWithCipher(bytes, bytes220, paddedAesCipher));
                                 l = (l ^ 3953999430026950182L) - -1017059695544781466L;
                              }
                              continue outerLoop;
                           }

                           KeccakSha3Digest keccakSha3Digest56 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes224 = new byte[]{
                              -15,
                              -121,
                              -28,
                              35,
                              -98,
                              -116,
                              57,
                              15,
                              34,
                              -47,
                              32,
                              -21,
                              -65,
                              17,
                              61,
                              117,
                              -31,
                              -58,
                              53,
                              65,
                              81,
                              65,
                              127,
                              83,
                              -79,
                              -65,
                              6,
                              35,
                              120,
                              -31,
                              -72,
                              -57
                           };
                           this.derivedKeySlot55 = keccakSha3Digest56.digest(VendorAesHelper.encryptWithCipher(bytes, bytes224, paddedAesCipher));
                           l = (l ^ 9141832584698422343L) + 2262949731429613046L;
                        }
                     }
                  } else if (b >= 93) {
                     if (b <= 93) {
                        KeccakSha3Digest keccakSha3Digest57 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes228 = new byte[]{
                           -46,
                           90,
                           127,
                           97,
                           -37,
                           127,
                           -58,
                           -44,
                           -107,
                           -45,
                           36,
                           -39,
                           31,
                           -93,
                           -50,
                           124,
                           10,
                           85,
                           79,
                           110,
                           43,
                           -81,
                           -10,
                           109,
                           -126,
                           -29,
                           -46,
                           -51,
                           -120,
                           76,
                           89,
                           14
                        };
                        this.derivedKeySlot56 = keccakSha3Digest57.digest(VendorAesHelper.encryptWithCipher(bytes, bytes228, paddedAesCipher));
                        l = (l ^ -5732567356339780629L) + 9085454408785600693L;
                     } else if (b >= 95) {
                        if (95 >= b) {
                           KeccakSha3Digest keccakSha3Digest58 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes232 = new byte[]{
                              64,
                              -43,
                              99,
                              -22,
                              -68,
                              -92,
                              -83,
                              -56,
                              -53,
                              -68,
                              35,
                              74,
                              6,
                              -3,
                              51,
                              113,
                              41,
                              -30,
                              -27,
                              114,
                              108,
                              98,
                              32,
                              84,
                              -72,
                              125,
                              72,
                              -92,
                              60,
                              -104,
                              -59,
                              117
                           };
                           this.derivedKeySlot57 = keccakSha3Digest58.digest(VendorAesHelper.encryptWithCipher(bytes, bytes232, paddedAesCipher));
                           l = l - 5660152852628845665L ^ -8268140413661995020L;
                        } else {
                           KeccakSha3Digest keccakSha3Digest59 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes236 = new byte[]{
                              110,
                              33,
                              -104,
                              66,
                              -67,
                              -121,
                              18,
                              113,
                              81,
                              42,
                              -3,
                              -125,
                              -76,
                              63,
                              -117,
                              -119,
                              -52,
                              8,
                              -104,
                              -75,
                              -88,
                              60,
                              14,
                              -4,
                              52,
                              -84,
                              -91,
                              112,
                              -102,
                              69,
                              -63,
                              -16
                           };
                           this.derivedKeySlot58 = keccakSha3Digest59.digest(VendorAesHelper.encryptWithCipher(bytes, bytes236, paddedAesCipher));
                           l = l + -3850202725556966599L ^ 9128475388539432882L;
                        }
                     } else {
                        KeccakSha3Digest keccakSha3Digest60 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes240 = new byte[]{
                           -79,
                           -79,
                           125,
                           121,
                           -61,
                           27,
                           -108,
                           -52,
                           -2,
                           97,
                           -112,
                           -55,
                           53,
                           5,
                           111,
                           1,
                           -76,
                           -83,
                           -69,
                           83,
                           94,
                           -110,
                           -73,
                           -41,
                           109,
                           118,
                           -7,
                           -57,
                           -19,
                           33,
                           -95,
                           20
                        };
                        this.derivedKeySlot59 = keccakSha3Digest60.digest(VendorAesHelper.encryptWithCipher(bytes, bytes240, paddedAesCipher));
                        l = l + 3178589097023318543L ^ -3656675355946131132L;
                     }
                  } else {
                     if (b > 91) {
                        KeccakSha3Digest keccakSha3Digest61 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes244 = new byte[]{
                           74,
                           -12,
                           -43,
                           38,
                           -50,
                           -127,
                           52,
                           -1,
                           -45,
                           -60,
                           -2,
                           8,
                           117,
                           -105,
                           21,
                           -15,
                           14,
                           -69,
                           94,
                           69,
                           -51,
                           -54,
                           -15,
                           -41,
                           91,
                           36,
                           103,
                           -15,
                           -92,
                           119,
                           20,
                           -7
                        };
                        this.derivedKeySlot60 = keccakSha3Digest61.digest(VendorAesHelper.encryptWithCipher(bytes, bytes244, paddedAesCipher));
                        l = (l ^ -1053175084146246223L) + 6818501129781943533L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest62 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes248 = new byte[]{
                        -7,
                        16,
                        42,
                        78,
                        25,
                        61,
                        12,
                        -5,
                        82,
                        -66,
                        97,
                        72,
                        51,
                        91,
                        115,
                        -21,
                        67,
                        -20,
                        77,
                        37,
                        60,
                        69,
                        124,
                        86,
                        -51,
                        -3,
                        -42,
                        102,
                        -98,
                        -125,
                        110,
                        54
                     };
                     this.derivedKeySlot61 = keccakSha3Digest62.digest(VendorAesHelper.encryptWithCipher(bytes, bytes248, paddedAesCipher));
                     l = (l ^ -2143435634479918837L) - -6047847661389158684L;
                  }
               } else if (83 <= b) {
                  if (b <= 83) {
                     KeccakSha3Digest keccakSha3Digest63 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes252 = new byte[]{
                        117,
                        -106,
                        73,
                        -32,
                        -97,
                        -43,
                        85,
                        112,
                        -26,
                        -50,
                        -122,
                        98,
                        3,
                        35,
                        25,
                        -46,
                        -43,
                        36,
                        -107,
                        88,
                        57,
                        7,
                        -116,
                        -97,
                        -99,
                        -97,
                        -30,
                        61,
                        59,
                        -21,
                        -37,
                        85
                     };
                     this.derivedKeySlot62 = keccakSha3Digest63.digest(VendorAesHelper.encryptWithCipher(bytes, bytes252, paddedAesCipher));
                     l = (l ^ -3774605167064525753L) + -6560447642437531592L;
                  } else if (b >= 86) {
                     if (b <= 86) {
                        KeccakSha3Digest keccakSha3Digest64 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes256 = new byte[]{
                           -97,
                           -25,
                           42,
                           -45,
                           -12,
                           78,
                           20,
                           110,
                           -59,
                           106,
                           -41,
                           -50,
                           -124,
                           -50,
                           -105,
                           -93,
                           -119,
                           -123,
                           -66,
                           62,
                           -60,
                           61,
                           33,
                           -117,
                           -64,
                           80,
                           -82,
                           -2,
                           31,
                           -12,
                           21,
                           -95
                        };
                        this.derivedKeySlot63 = keccakSha3Digest64.digest(VendorAesHelper.encryptWithCipher(bytes, bytes256, paddedAesCipher));
                        l = (l ^ -3817371431337268611L) + -7597437801110303086L;
                     } else {
                        if (b < 88) {
                           KeccakSha3Digest keccakSha3Digest65 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes260 = new byte[]{
                              66,
                              79,
                              67,
                              -35,
                              97,
                              -99,
                              67,
                              -35,
                              9,
                              90,
                              -55,
                              -113,
                              -91,
                              -55,
                              -112,
                              -35,
                              35,
                              40,
                              -46,
                              46,
                              -85,
                              113,
                              42,
                              -33,
                              125,
                              34,
                              120,
                              -32,
                              48,
                              -124,
                              -49,
                              119
                           };
                           this.derivedKeySlot64 = keccakSha3Digest65.digest(VendorAesHelper.encryptWithCipher(bytes, bytes260, paddedAesCipher));
                           l = l + 1228038491881781672L ^ 6469361736491497620L;
                           continue outerLoop;
                        }

                        if (b <= 88) {
                           KeccakSha3Digest keccakSha3Digest66 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes264 = new byte[]{
                              -21,
                              -85,
                              -30,
                              -35,
                              10,
                              -86,
                              58,
                              97,
                              -53,
                              105,
                              -69,
                              -79,
                              -5,
                              -74,
                              5,
                              -44,
                              -125,
                              -41,
                              -107,
                              -74,
                              -101,
                              -121,
                              -61,
                              -98,
                              4,
                              84,
                              41,
                              111,
                              16,
                              104,
                              122,
                              -15
                           };
                           this.derivedKeySlot65 = keccakSha3Digest66.digest(VendorAesHelper.encryptWithCipher(bytes, bytes264, paddedAesCipher));
                           l = (l ^ 3546303838722836370L) - 1430159407863480506L;
                        } else {
                           KeccakSha3Digest keccakSha3Digest67 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes268 = new byte[]{
                              -105,
                              42,
                              -123,
                              -4,
                              42,
                              -68,
                              83,
                              127,
                              -45,
                              37,
                              -78,
                              113,
                              71,
                              -58,
                              -45,
                              -85,
                              86,
                              -67,
                              -97,
                              0,
                              -31,
                              -82,
                              119,
                              64,
                              -126,
                              1,
                              -28,
                              -115,
                              96,
                              80,
                              97,
                              74
                           };
                           this.derivedKeySlot66 = keccakSha3Digest67.digest(VendorAesHelper.encryptWithCipher(bytes, bytes268, paddedAesCipher));
                           l = (l ^ 3170289478412298312L) + 3467082348893211019L;
                        }
                     }
                  } else if (b <= 84) {
                     KeccakSha3Digest keccakSha3Digest68 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes272 = new byte[]{
                        112,
                        -24,
                        -11,
                        20,
                        -16,
                        -28,
                        -81,
                        57,
                        13,
                        78,
                        54,
                        60,
                        -1,
                        38,
                        -19,
                        -78,
                        -88,
                        -59,
                        -80,
                        100,
                        -7,
                        -126,
                        61,
                        -56,
                        -21,
                        81,
                        65,
                        75,
                        -44,
                        97,
                        124,
                        31
                     };
                     this.derivedKeySlot67 = keccakSha3Digest68.digest(VendorAesHelper.encryptWithCipher(bytes, bytes272, paddedAesCipher));
                     l = l - 7664999214175644135L ^ -1686953295049008165L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest69 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes276 = new byte[]{
                        -124,
                        -117,
                        -31,
                        23,
                        119,
                        53,
                        -80,
                        -70,
                        53,
                        -2,
                        -6,
                        107,
                        126,
                        3,
                        -23,
                        75,
                        -42,
                        113,
                        -74,
                        116,
                        31,
                        35,
                        41,
                        48,
                        -101,
                        86,
                        30,
                        76,
                        -69,
                        83,
                        -66,
                        -77
                     };
                     this.derivedKeySlot68 = keccakSha3Digest69.digest(VendorAesHelper.encryptWithCipher(bytes, bytes276, paddedAesCipher));
                     l = l + -7417748269928466204L ^ -3581578467247782440L;
                  }
               } else if (80 <= b) {
                  if (80 >= b) {
                     KeccakSha3Digest keccakSha3Digest70 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes280 = new byte[]{
                        57,
                        -89,
                        101,
                        -64,
                        -119,
                        -51,
                        52,
                        67,
                        -62,
                        23,
                        32,
                        -108,
                        -92,
                        -101,
                        -104,
                        -92,
                        43,
                        -126,
                        -2,
                        122,
                        118,
                        -57,
                        23,
                        13,
                        -111,
                        -114,
                        95,
                        -12,
                        16,
                        27,
                        -34,
                        -24
                     };
                     this.derivedKeySlot69 = keccakSha3Digest70.digest(VendorAesHelper.encryptWithCipher(bytes, bytes280, paddedAesCipher));
                     l = (l ^ 607321007991402618L) + -3767385054696209057L;
                     continue outerLoop;
                  }

                  if (81 >= b) {
                     KeccakSha3Digest keccakSha3Digest71 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes284 = new byte[]{
                        42,
                        -36,
                        57,
                        22,
                        -110,
                        -91,
                        15,
                        -102,
                        108,
                        -25,
                        -90,
                        21,
                        -109,
                        35,
                        -89,
                        -98,
                        79,
                        -116,
                        67,
                        -55,
                        65,
                        -9,
                        58,
                        71,
                        73,
                        97,
                        74,
                        108,
                        45,
                        -21,
                        127,
                        44
                     };
                     this.derivedKeySlot70 = keccakSha3Digest71.digest(VendorAesHelper.encryptWithCipher(bytes, bytes284, paddedAesCipher));
                     l = (l ^ 1129587614945637727L) + -3342833887252084060L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest72 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes288 = new byte[]{
                     -27,
                     -2,
                     -59,
                     -76,
                     35,
                     106,
                     110,
                     60,
                     -50,
                     -25,
                     -46,
                     -8,
                     -97,
                     -79,
                     -109,
                     89,
                     -120,
                     91,
                     76,
                     -77,
                     -76,
                     78,
                     14,
                     -128,
                     -53,
                     62,
                     24,
                     98,
                     53,
                     -10,
                     80,
                     -81
                  };
                  this.derivedKeySlot71 = keccakSha3Digest72.digest(VendorAesHelper.encryptWithCipher(bytes, bytes288, paddedAesCipher));
                  l = (l ^ -5067355337602718219L) - -2495683610071474268L;
               } else {
                  if (78 >= b) {
                     KeccakSha3Digest keccakSha3Digest73 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes292 = new byte[]{
                        98,
                        -3,
                        -100,
                        -119,
                        8,
                        -22,
                        -57,
                        -83,
                        65,
                        23,
                        -69,
                        98,
                        -100,
                        116,
                        38,
                        30,
                        99,
                        -115,
                        110,
                        51,
                        -88,
                        107,
                        50,
                        -69,
                        -102,
                        95,
                        -3,
                        -51,
                        88,
                        -114,
                        58,
                        55
                     };
                     this.derivedKeySlot72 = keccakSha3Digest73.digest(VendorAesHelper.encryptWithCipher(bytes, bytes292, paddedAesCipher));
                     l = l - -4620687073288342014L ^ -7607108565205934950L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest74 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes298 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
                     -69, 123, -42, 88, -82, -57, -104, -57, 16, -64, 13, 62, -21, 1, 7, -118, 85, 6, 7, -53, 55, 97, 72, -8, -41, -15, -76, 96, 33, -1, 56, 73
                  }, paddedAesCipher);
                  this.derivedKeySlot73 = keccakSha3Digest74.digest(bytes298);
                  l = (l ^ 8522369290953700338L) + -4015683427126893928L;
               }
            } else if (b >= 64) {
               if (b <= 64) {
                  KeccakSha3Digest keccakSha3Digest75 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes300 = new byte[]{
                     -123,
                     -25,
                     -24,
                     -125,
                     -120,
                     33,
                     123,
                     -91,
                     -7,
                     93,
                     -80,
                     -59,
                     -126,
                     -78,
                     -103,
                     74,
                     -1,
                     68,
                     14,
                     56,
                     113,
                     -11,
                     77,
                     -40,
                     -57,
                     85,
                     107,
                     50,
                     -51,
                     31,
                     5,
                     112
                  };
                  this.derivedKeySlot74 = keccakSha3Digest75.digest(VendorAesHelper.encryptWithCipher(bytes, bytes300, paddedAesCipher));
                  l = l - 3999120625947160110L ^ 4867206523743267464L;
               } else if (b >= 70) {
                  if (70 >= b) {
                     KeccakSha3Digest keccakSha3Digest76 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes304 = new byte[]{
                        26,
                        -97,
                        44,
                        -9,
                        -28,
                        57,
                        -56,
                        43,
                        -93,
                        -66,
                        -70,
                        -13,
                        -38,
                        -116,
                        -50,
                        127,
                        -100,
                        46,
                        -124,
                        -37,
                        33,
                        42,
                        -69,
                        81,
                        -114,
                        -108,
                        46,
                        -33,
                        -11,
                        -23,
                        -29,
                        -13
                     };
                     this.derivedKeySlot75 = keccakSha3Digest76.digest(VendorAesHelper.encryptWithCipher(bytes, bytes304, paddedAesCipher));
                     l = (l ^ -8999432388970804856L) - -7827080599944673997L;
                     continue outerLoop;
                  }

                  if (73 <= b) {
                     if (b <= 73) {
                        KeccakSha3Digest keccakSha3Digest77 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes308 = new byte[]{
                           -32,
                           82,
                           -74,
                           85,
                           -69,
                           11,
                           27,
                           -70,
                           54,
                           -88,
                           98,
                           64,
                           -116,
                           -8,
                           -87,
                           -97,
                           -72,
                           -106,
                           -81,
                           -128,
                           85,
                           32,
                           125,
                           -79,
                           -44,
                           -1,
                           -126,
                           124,
                           32,
                           59,
                           56,
                           83
                        };
                        this.derivedKeySlot76 = keccakSha3Digest77.digest(VendorAesHelper.encryptWithCipher(bytes, bytes308, paddedAesCipher));
                        l = (l ^ 4209075268487941398L) - -3853264490551989978L;
                     } else {
                        if (75 > b) {
                           KeccakSha3Digest keccakSha3Digest78 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes312 = new byte[]{
                              -125,
                              110,
                              20,
                              11,
                              44,
                              11,
                              84,
                              -3,
                              42,
                              -58,
                              29,
                              -105,
                              105,
                              52,
                              -48,
                              -40,
                              33,
                              -26,
                              82,
                              67,
                              -30,
                              42,
                              56,
                              119,
                              -23,
                              -50,
                              -35,
                              15,
                              75,
                              -54,
                              26,
                              -13
                           };
                           this.derivedKeySlot77 = keccakSha3Digest78.digest(VendorAesHelper.encryptWithCipher(bytes, bytes312, paddedAesCipher));
                           l = (l ^ -3159114918553343142L) - 5506420898281323440L;
                           continue outerLoop;
                        }

                        if (b <= 75) {
                           KeccakSha3Digest keccakSha3Digest79 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes316 = new byte[]{
                              -103,
                              121,
                              88,
                              -123,
                              111,
                              -104,
                              -125,
                              65,
                              -106,
                              -84,
                              31,
                              -108,
                              29,
                              33,
                              44,
                              100,
                              -116,
                              -21,
                              1,
                              34,
                              -52,
                              95,
                              98,
                              -71,
                              -56,
                              -15,
                              37,
                              77,
                              83,
                              -102,
                              65,
                              -102
                           };
                           this.derivedKeySlot78 = keccakSha3Digest79.digest(VendorAesHelper.encryptWithCipher(bytes, bytes316, paddedAesCipher));
                           l = l - 2915794913057726747L ^ -7827444152250722532L;
                        } else {
                           KeccakSha3Digest keccakSha3Digest80 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes320 = new byte[]{
                              34,
                              111,
                              -103,
                              -50,
                              -103,
                              61,
                              49,
                              38,
                              120,
                              -10,
                              -104,
                              126,
                              -81,
                              -57,
                              67,
                              -47,
                              57,
                              -13,
                              60,
                              29,
                              68,
                              109,
                              -31,
                              80,
                              34,
                              7,
                              77,
                              -56,
                              -2,
                              -60,
                              -111,
                              -49
                           };
                           this.derivedKeySlot79 = keccakSha3Digest80.digest(VendorAesHelper.encryptWithCipher(bytes, bytes320, paddedAesCipher));
                           l = l + 7632977833528674417L ^ -6201715648004241792L;
                        }
                     }
                  } else if (b <= 71) {
                     KeccakSha3Digest keccakSha3Digest81 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes324 = new byte[]{
                        -39,
                        43,
                        107,
                        -127,
                        -125,
                        115,
                        -35,
                        -72,
                        -110,
                        -89,
                        -81,
                        -98,
                        62,
                        71,
                        -125,
                        91,
                        121,
                        -57,
                        55,
                        68,
                        104,
                        9,
                        70,
                        -65,
                        -20,
                        42,
                        85,
                        78,
                        70,
                        127,
                        34,
                        -97
                     };
                     this.derivedKeySlot80 = keccakSha3Digest81.digest(VendorAesHelper.encryptWithCipher(bytes, bytes324, paddedAesCipher));
                     l = l + -9027891105872937820L ^ -3379817509809975880L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest82 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes328 = new byte[]{
                        -112,
                        43,
                        106,
                        22,
                        33,
                        9,
                        84,
                        82,
                        114,
                        -70,
                        -79,
                        -103,
                        -87,
                        -70,
                        48,
                        119,
                        31,
                        -121,
                        113,
                        90,
                        -57,
                        109,
                        30,
                        -10,
                        107,
                        -50,
                        -57,
                        -25,
                        -102,
                        -128,
                        99,
                        -24
                     };
                     this.derivedKeySlot81 = keccakSha3Digest82.digest(VendorAesHelper.encryptWithCipher(bytes, bytes328, paddedAesCipher));
                     l = l - 857848538503967963L ^ 2458795708907349229L;
                  }
               } else if (b >= 67) {
                  if (b <= 67) {
                     KeccakSha3Digest keccakSha3Digest83 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes332 = new byte[]{
                        -28,
                        -65,
                        -18,
                        -52,
                        -127,
                        -36,
                        -125,
                        -15,
                        -16,
                        -122,
                        4,
                        28,
                        -104,
                        109,
                        119,
                        109,
                        14,
                        65,
                        125,
                        -113,
                        -84,
                        -40,
                        -126,
                        -22,
                        -71,
                        -54,
                        -45,
                        -84,
                        17,
                        -72,
                        110,
                        31
                     };
                     this.derivedKeySlot82 = keccakSha3Digest83.digest(VendorAesHelper.encryptWithCipher(bytes, bytes332, paddedAesCipher));
                     l = (l ^ -5758625436342636042L) + 4310922720562782682L;
                     continue outerLoop;
                  }

                  if (b <= 68) {
                     KeccakSha3Digest keccakSha3Digest84 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes336 = new byte[]{
                        1,
                        -93,
                        98,
                        -5,
                        71,
                        -87,
                        -99,
                        42,
                        34,
                        78,
                        -65,
                        107,
                        44,
                        46,
                        -33,
                        -6,
                        -47,
                        -125,
                        121,
                        118,
                        -66,
                        84,
                        -7,
                        31,
                        59,
                        -22,
                        63,
                        -31,
                        -91,
                        -50,
                        -13,
                        -106
                     };
                     this.derivedKeySlot83 = keccakSha3Digest84.digest(VendorAesHelper.encryptWithCipher(bytes, bytes336, paddedAesCipher));
                     l = (l ^ -4885925438417752583L) + 8294890288976004522L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest85 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes340 = new byte[]{
                        -126,
                        112,
                        22,
                        -20,
                        91,
                        -40,
                        -53,
                        4,
                        100,
                        91,
                        -6,
                        70,
                        82,
                        12,
                        39,
                        -13,
                        30,
                        -93,
                        68,
                        42,
                        -29,
                        -29,
                        -98,
                        -20,
                        61,
                        -68,
                        109,
                        -69,
                        104,
                        60,
                        -71,
                        -36
                     };
                     this.derivedKeySlot84 = keccakSha3Digest85.digest(VendorAesHelper.encryptWithCipher(bytes, bytes340, paddedAesCipher));
                     l = (l ^ 3899065847953428090L) - -2753663288812745473L;
                  }
               } else {
                  if (b <= 65) {
                     KeccakSha3Digest keccakSha3Digest86 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes344 = new byte[]{
                        -80,
                        26,
                        14,
                        -76,
                        101,
                        108,
                        -29,
                        -12,
                        -28,
                        70,
                        -114,
                        27,
                        -4,
                        -65,
                        -7,
                        -9,
                        117,
                        99,
                        25,
                        -35,
                        -17,
                        45,
                        -21,
                        97,
                        -43,
                        -87,
                        118,
                        24,
                        27,
                        88,
                        -23,
                        -70
                     };
                     this.derivedKeySlot85 = keccakSha3Digest86.digest(VendorAesHelper.encryptWithCipher(bytes, bytes344, paddedAesCipher));
                     l = (l ^ 1701150275710054018L) - 1359684306595899131L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest87 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes348 = new byte[]{
                     85,
                     61,
                     -104,
                     -97,
                     26,
                     123,
                     42,
                     -69,
                     0,
                     16,
                     120,
                     46,
                     115,
                     -59,
                     68,
                     80,
                     -92,
                     -110,
                     61,
                     75,
                     69,
                     -1,
                     -126,
                     101,
                     11,
                     126,
                     -22,
                     -82,
                     63,
                     27,
                     34,
                     -102
                  };
                  this.derivedKeySlot86 = keccakSha3Digest87.digest(VendorAesHelper.encryptWithCipher(bytes, bytes348, paddedAesCipher));
                  l = l + -3715543811203575475L ^ 3461867298930201830L;
               }
            } else if (57 <= b) {
               if (57 >= b) {
                  KeccakSha3Digest keccakSha3Digest88 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes352 = new byte[]{
                     126,
                     -67,
                     -42,
                     38,
                     -107,
                     22,
                     -94,
                     -108,
                     85,
                     27,
                     -49,
                     -11,
                     -61,
                     99,
                     79,
                     81,
                     103,
                     100,
                     20,
                     -74,
                     83,
                     -119,
                     91,
                     -18,
                     -6,
                     127,
                     -19,
                     -38,
                     99,
                     72,
                     113,
                     -97
                  };
                  this.derivedKeySlot87 = keccakSha3Digest88.digest(VendorAesHelper.encryptWithCipher(bytes, bytes352, paddedAesCipher));
                  l = (l ^ 4409811844686401590L) + -5200869742512132202L;
               } else {
                  if (b < 60) {
                     if (b <= 58) {
                        KeccakSha3Digest keccakSha3Digest89 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes356 = new byte[]{
                           41,
                           -54,
                           57,
                           -57,
                           -44,
                           -54,
                           -28,
                           37,
                           53,
                           17,
                           113,
                           -9,
                           -60,
                           74,
                           -26,
                           -36,
                           46,
                           12,
                           -62,
                           -18,
                           -41,
                           -24,
                           -90,
                           -99,
                           46,
                           36,
                           -63,
                           126,
                           57,
                           -91,
                           -120,
                           109
                        };
                        this.derivedKeySlot88 = keccakSha3Digest89.digest(VendorAesHelper.encryptWithCipher(bytes, bytes356, paddedAesCipher));
                        l = (l ^ 3680489778672013606L) + -4870912910522197023L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest90 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes360 = new byte[]{
                           -77,
                           29,
                           -105,
                           85,
                           39,
                           -29,
                           -65,
                           101,
                           -27,
                           -102,
                           14,
                           -111,
                           -17,
                           117,
                           -27,
                           -78,
                           4,
                           21,
                           -105,
                           -5,
                           -8,
                           21,
                           10,
                           -123,
                           68,
                           -11,
                           9,
                           -96,
                           110,
                           98,
                           12,
                           35
                        };
                        this.derivedKeySlot89 = keccakSha3Digest90.digest(VendorAesHelper.encryptWithCipher(bytes, bytes360, paddedAesCipher));
                        l = l + -7494440144502685072L ^ -6763218371184577595L;
                     }
                     continue outerLoop;
                  }

                  if (b <= 60) {
                     KeccakSha3Digest keccakSha3Digest91 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes364 = new byte[]{
                        36,
                        -11,
                        126,
                        -62,
                        -69,
                        -26,
                        48,
                        18,
                        69,
                        66,
                        -32,
                        -45,
                        91,
                        68,
                        23,
                        0,
                        -126,
                        -32,
                        -20,
                        -128,
                        -36,
                        90,
                        68,
                        15,
                        65,
                        41,
                        -58,
                        13,
                        72,
                        -31,
                        78,
                        46
                     };
                     this.derivedKeySlot90 = keccakSha3Digest91.digest(VendorAesHelper.encryptWithCipher(bytes, bytes364, paddedAesCipher));
                     l = l - -2381627770513955453L ^ 5849993720520887274L;
                  } else {
                     if (b < 62) {
                        KeccakSha3Digest keccakSha3Digest92 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes368 = new byte[]{
                           -100,
                           -56,
                           11,
                           -128,
                           -69,
                           -8,
                           94,
                           105,
                           101,
                           -19,
                           -121,
                           100,
                           -17,
                           31,
                           -102,
                           56,
                           93,
                           -109,
                           -52,
                           -23,
                           -42,
                           -27,
                           -27,
                           -40,
                           12,
                           -77,
                           -117,
                           -49,
                           -27,
                           16,
                           73,
                           33
                        };
                        this.derivedKeySlot91 = keccakSha3Digest92.digest(VendorAesHelper.encryptWithCipher(bytes, bytes368, paddedAesCipher));
                        l = l + 8390277886497091075L ^ 5040855350988879230L;
                        continue outerLoop;
                     }

                     if (b > 62) {
                        KeccakSha3Digest keccakSha3Digest93 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes372 = new byte[]{
                           15,
                           97,
                           66,
                           -99,
                           -61,
                           -63,
                           -8,
                           32,
                           -49,
                           -127,
                           -4,
                           25,
                           -35,
                           100,
                           -42,
                           -77,
                           -113,
                           -109,
                           -31,
                           86,
                           5,
                           -11,
                           -65,
                           -79,
                           -105,
                           -128,
                           1,
                           69,
                           -64,
                           75,
                           -22,
                           -30
                        };
                        this.derivedKeySlot92 = keccakSha3Digest93.digest(VendorAesHelper.encryptWithCipher(bytes, bytes372, paddedAesCipher));
                        l = (l ^ -5534682556528203855L) - -6461670132314407042L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest94 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes376 = new byte[]{
                        106,
                        -118,
                        -127,
                        1,
                        -49,
                        -90,
                        -20,
                        -66,
                        59,
                        -42,
                        33,
                        79,
                        1,
                        24,
                        90,
                        -123,
                        121,
                        -17,
                        -122,
                        -60,
                        49,
                        -56,
                        126,
                        26,
                        11,
                        84,
                        -16,
                        -63,
                        -55,
                        73,
                        -125,
                        -115
                     };
                     this.derivedKeySlot93 = keccakSha3Digest94.digest(VendorAesHelper.encryptWithCipher(bytes, bytes376, paddedAesCipher));
                     l = l + 6983371780495342452L ^ 2732676598876613668L;
                  }
               }
            } else if (54 <= b) {
               if (b > 54) {
                  if (b <= 55) {
                     KeccakSha3Digest keccakSha3Digest95 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes380 = new byte[]{
                        -16,
                        -15,
                        -51,
                        -44,
                        -125,
                        10,
                        -97,
                        -90,
                        21,
                        -90,
                        65,
                        -128,
                        102,
                        101,
                        98,
                        98,
                        -86,
                        -103,
                        103,
                        67,
                        -5,
                        120,
                        -99,
                        74,
                        -120,
                        30,
                        12,
                        112,
                        125,
                        122,
                        30,
                        30
                     };
                     this.derivedKeySlot94 = keccakSha3Digest95.digest(VendorAesHelper.encryptWithCipher(bytes, bytes380, paddedAesCipher));
                     l = (l ^ -3085826712083002423L) - 2927757615381914861L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest96 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes384 = new byte[]{
                        -103,
                        52,
                        -53,
                        -85,
                        89,
                        -16,
                        95,
                        63,
                        -97,
                        -56,
                        35,
                        24,
                        47,
                        9,
                        -76,
                        -4,
                        19,
                        85,
                        -82,
                        -86,
                        -24,
                        -93,
                        37,
                        -119,
                        -124,
                        -80,
                        -89,
                        4,
                        13,
                        106,
                        -23,
                        -95
                     };
                     this.derivedKeySlot95 = keccakSha3Digest96.digest(VendorAesHelper.encryptWithCipher(bytes, bytes384, paddedAesCipher));
                     l = (l ^ 6839615203740939004L) + 3655234029172801393L;
                  }
                  continue outerLoop;
               }

               KeccakSha3Digest keccakSha3Digest97 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes388 = new byte[]{
                  97,
                  -28,
                  -12,
                  4,
                  46,
                  20,
                  43,
                  -38,
                  -54,
                  54,
                  82,
                  89,
                  -48,
                  -113,
                  -60,
                  -10,
                  15,
                  32,
                  119,
                  119,
                  61,
                  -118,
                  100,
                  62,
                  17,
                  54,
                  4,
                  120,
                  119,
                  -43,
                  -125,
                  -112
               };
               this.derivedKeySlot96 = keccakSha3Digest97.digest(VendorAesHelper.encryptWithCipher(bytes, bytes388, paddedAesCipher));
               l = l + 6832986316797213611L ^ 1700859302318084463L;
            } else {
               if (b > 52) {
                  KeccakSha3Digest keccakSha3Digest98 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes392 = new byte[]{
                     -36,
                     -114,
                     -117,
                     5,
                     85,
                     24,
                     -63,
                     50,
                     -21,
                     -104,
                     -42,
                     -70,
                     -84,
                     -126,
                     -28,
                     -56,
                     108,
                     60,
                     -86,
                     6,
                     49,
                     50,
                     -116,
                     33,
                     61,
                     116,
                     41,
                     -54,
                     3,
                     122,
                     38,
                     108
                  };
                  this.derivedKeySlot97 = keccakSha3Digest98.digest(VendorAesHelper.encryptWithCipher(bytes, bytes392, paddedAesCipher));
                  l = (l ^ -5744801012399165988L) + 4837916907311881091L;
                  continue outerLoop;
               }

               KeccakSha3Digest keccakSha3Digest99 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes396 = new byte[]{
                  -32,
                  66,
                  -83,
                  -20,
                  84,
                  -126,
                  62,
                  -109,
                  42,
                  103,
                  32,
                  -97,
                  -62,
                  -96,
                  -1,
                  30,
                  -92,
                  -14,
                  -93,
                  -46,
                  -25,
                  -110,
                  12,
                  -101,
                  114,
                  56,
                  11,
                  -66,
                  -31,
                  45,
                  122,
                  69
               };
               this.derivedKeySlot98 = keccakSha3Digest99.digest(VendorAesHelper.encryptWithCipher(bytes, bytes396, paddedAesCipher));
               l = (l ^ -6015603300513958608L) - -7694728918708548155L;
            }
         }

         KeccakSha3Digest keccakSha3Digest100 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
         byte[] bytes402 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
            116, -87, -35, -89, -21, 46, -61, -40, -4, 99, 95, 35, 66, -121, 26, -22, 50, 47, -89, -94, 24, -117, 124, 16, 62, 105, 108, 73, 51, 23, 12, 51
         }, paddedAesCipher);
         this.derivedKeySlot99 = keccakSha3Digest100.digest(bytes402);
         l = (l ^ -8590132899825677837L) - -7082134770026329260L;
      }
   }

   private int decryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = 0;
      int n5 = 0;
      int n6 = 7;
      long l = 860950379188658783L;
      long l2 = 0L;
      l = 8186048303307693482L ^ (long)"ஔ뉸蟾쾅␎뽖딻ꀒ獫喾暟\ud962徥ྍ싽\u2cf6ጢﱖ䟥퀓\uf24cค貊⒛䑐\ufff8㉬䘕".hashCode();
      l2 = 8707869108214008675L ^ (long)"ꈋ弣卍䋟苓蛽洶\ue5f8\uf390ㅋ虘轊掞汗ཚﻉ\ue2db篌醛樖".hashCode();

      while (true) {
         n6 = (int)(l % 13L);
         if (n6 >= 6) {
            if (6 >= n6) {
               n4++;
               l = (l ^ 2211862047032026442L) + -1748366216931246085L;
               l2 = l2 + 1250828699969242419L ^ 161234666923250835L;
            } else if (9 <= n6) {
               if (9 >= n6) {
                  if (n >= n5) {
                     l = l + 1707468970626244387L ^ 7350941914337147610L;
                     l2 = l2 + -5107484834943031895L ^ -1741465767995125474L;
                  } else {
                     l = l + 1196144942318039484L ^ 8185348074733099676L;
                     l2 = l2 - -5623312923183491756L ^ -7745545243091870454L;
                  }
               } else if (11 <= n6) {
                  if (n6 <= 11) {
                     n3 += 0;
                     l = l - -6779170453628235805L ^ -7761713366841567791L;
                     l2 = l2 + -113166890784169999L ^ -1128657564457821838L;
                  } else if (n4 >= 0) {
                     l = l + -160442008410431609L ^ 8103206695263104734L;
                     l2 = (l2 ^ -5958269747152076511L) - -1428434636360342612L;
                  } else {
                     l = (l ^ -3637332853980912814L) + 8666907970350109433L;
                     l2 = l2 - 989661122952801235L ^ 3278330858017569196L;
                  }
               } else {
                  l = (l ^ 124268987611434520L) - 4224787009465030541L;
                  l2 = l2 + -8802326055996314089L ^ -77815575151174402L;
               }
            } else {
               if (7 < n6) {
                  return n2;
               }

               System.arraycopy(bytes, n, this.cbcRegister, 0, 0);
               l = (l ^ -3620468408900814176L) + -6533554462928648252L;
               l2 = (l2 ^ 919337332303474455L) + -2066739086622158385L;
            }
         } else if (n6 >= 2) {
            if (n6 <= 2) {
               n5 = n + n2;
               l = (l ^ -2164816559904711721L) + -5890939279768522036L;
               l2 = l2 + -7957197831431454183L ^ -7449991146286669786L;
            } else if (n6 >= 4) {
               if (4 >= n6) {
                  bytes2[n4 + n3] = (byte)(this.cbcWorkBuffer[n4] ^ this.cbcRegister[n4]);
                  l = l + 3477036079155719814L ^ -6259364179096053472L;
                  l2 = (l2 ^ -2552029988389301947L) - 5359960413941228407L;
               } else {
                  n += 0;
                  l = l - -2058327336205606467L ^ 4699633639816265933L;
                  l2 = (l2 ^ -6151833671013410122L) + -3179111946481997369L;
               }
            } else {
               n4 = 0;
               l = l - -2865682361605324017L ^ 4527393159643155613L;
               l2 = l2 - -1896295979755965997L ^ 7449364389116583357L;
            }
         } else if (0 >= n6) {
            l = (l ^ 7401614752369513663L) - 3363382037397063427L;
            l2 = (l2 ^ -8050598809918528186L) - -7432373908066927227L;
         } else {
            l = l - 3599846939878259821L ^ -2761069248302133129L;
            l2 = l2 - 9004211306160654140L ^ -1379809363706323164L;
         }
      }
   }

   private void decryptBlockImpl(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 4;
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n] & 255) ^ this.roundKeys[n3++];
      if (this.has12OrMoreRounds) {
         int n8 = this.decTable1[n4 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n6 >>> 8 & 0xFF] ^ this.decTable4[n5 & 0xFF] ^ this.roundKeys[n3++];
         int n9 = this.decTable1[n5 >>> 24] ^ this.decTable2[n4 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n6 & 0xFF] ^ this.roundKeys[n3++];
         int n10 = this.decTable1[n6 >>> 24] ^ this.decTable2[n5 >>> 16 & 0xFF] ^ this.decTable3[n4 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n6 >>> 16 & 0xFF] ^ this.decTable3[n5 >>> 8 & 0xFF] ^ this.decTable4[n4 & 0xFF] ^ this.roundKeys[n3++];
         n4 = this.decTable1[n8 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n10 >>> 8 & 0xFF] ^ this.decTable4[n9 & 0xFF] ^ this.roundKeys[n3++];
         n5 = this.decTable1[n9 >>> 24] ^ this.decTable2[n8 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n10 & 0xFF] ^ this.roundKeys[n3++];
         n6 = this.decTable1[n10 >>> 24] ^ this.decTable2[n9 >>> 16 & 0xFF] ^ this.decTable3[n8 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n10 >>> 16 & 0xFF] ^ this.decTable3[n9 >>> 8 & 0xFF] ^ this.decTable4[n8 & 0xFF] ^ this.roundKeys[n3++];
         if (this.has14Rounds) {
            n8 = this.decTable1[n4 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n6 >>> 8 & 0xFF] ^ this.decTable4[n5 & 0xFF] ^ this.roundKeys[n3++];
            n9 = this.decTable1[n5 >>> 24] ^ this.decTable2[n4 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n6 & 0xFF] ^ this.roundKeys[n3++];
            n10 = this.decTable1[n6 >>> 24] ^ this.decTable2[n5 >>> 16 & 0xFF] ^ this.decTable3[n4 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
            n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n6 >>> 16 & 0xFF] ^ this.decTable3[n5 >>> 8 & 0xFF] ^ this.decTable4[n4 & 0xFF] ^ this.roundKeys[n3++];
            n4 = this.decTable1[n8 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n10 >>> 8 & 0xFF] ^ this.decTable4[n9 & 0xFF] ^ this.roundKeys[n3++];
            n5 = this.decTable1[n9 >>> 24] ^ this.decTable2[n8 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n10 & 0xFF] ^ this.roundKeys[n3++];
            n6 = this.decTable1[n10 >>> 24] ^ this.decTable2[n9 >>> 16 & 0xFF] ^ this.decTable3[n8 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
            n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n10 >>> 16 & 0xFF] ^ this.decTable3[n9 >>> 8 & 0xFF] ^ this.decTable4[n8 & 0xFF] ^ this.roundKeys[n3++];
         }
      }

      int n11 = this.decTable1[n4 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n6 >>> 8 & 0xFF] ^ this.decTable4[n5 & 0xFF] ^ this.roundKeys[n3++];
      int n12 = this.decTable1[n5 >>> 24] ^ this.decTable2[n4 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n6 & 0xFF] ^ this.roundKeys[n3++];
      int n13 = this.decTable1[n6 >>> 24] ^ this.decTable2[n5 >>> 16 & 0xFF] ^ this.decTable3[n4 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n6 >>> 16 & 0xFF] ^ this.decTable3[n5 >>> 8 & 0xFF] ^ this.decTable4[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.decTable1[n11 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n13 >>> 8 & 0xFF] ^ this.decTable4[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.decTable1[n12 >>> 24] ^ this.decTable2[n11 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.decTable1[n13 >>> 24] ^ this.decTable2[n12 >>> 16 & 0xFF] ^ this.decTable3[n11 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n13 >>> 16 & 0xFF] ^ this.decTable3[n12 >>> 8 & 0xFF] ^ this.decTable4[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.decTable1[n4 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n6 >>> 8 & 0xFF] ^ this.decTable4[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.decTable1[n5 >>> 24] ^ this.decTable2[n4 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.decTable1[n6 >>> 24] ^ this.decTable2[n5 >>> 16 & 0xFF] ^ this.decTable3[n4 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n6 >>> 16 & 0xFF] ^ this.decTable3[n5 >>> 8 & 0xFF] ^ this.decTable4[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.decTable1[n11 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n13 >>> 8 & 0xFF] ^ this.decTable4[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.decTable1[n12 >>> 24] ^ this.decTable2[n11 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.decTable1[n13 >>> 24] ^ this.decTable2[n12 >>> 16 & 0xFF] ^ this.decTable3[n11 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n13 >>> 16 & 0xFF] ^ this.decTable3[n12 >>> 8 & 0xFF] ^ this.decTable4[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.decTable1[n4 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n6 >>> 8 & 0xFF] ^ this.decTable4[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.decTable1[n5 >>> 24] ^ this.decTable2[n4 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.decTable1[n6 >>> 24] ^ this.decTable2[n5 >>> 16 & 0xFF] ^ this.decTable3[n4 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n6 >>> 16 & 0xFF] ^ this.decTable3[n5 >>> 8 & 0xFF] ^ this.decTable4[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.decTable1[n11 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n13 >>> 8 & 0xFF] ^ this.decTable4[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.decTable1[n12 >>> 24] ^ this.decTable2[n11 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.decTable1[n13 >>> 24] ^ this.decTable2[n12 >>> 16 & 0xFF] ^ this.decTable3[n11 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n13 >>> 16 & 0xFF] ^ this.decTable3[n12 >>> 8 & 0xFF] ^ this.decTable4[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.decTable1[n4 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n6 >>> 8 & 0xFF] ^ this.decTable4[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.decTable1[n5 >>> 24] ^ this.decTable2[n4 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.decTable1[n6 >>> 24] ^ this.decTable2[n5 >>> 16 & 0xFF] ^ this.decTable3[n4 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n6 >>> 16 & 0xFF] ^ this.decTable3[n5 >>> 8 & 0xFF] ^ this.decTable4[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.decTable1[n11 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n13 >>> 8 & 0xFF] ^ this.decTable4[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.decTable1[n12 >>> 24] ^ this.decTable2[n11 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.decTable1[n13 >>> 24] ^ this.decTable2[n12 >>> 16 & 0xFF] ^ this.decTable3[n11 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n13 >>> 16 & 0xFF] ^ this.decTable3[n12 >>> 8 & 0xFF] ^ this.decTable4[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.decTable1[n4 >>> 24] ^ this.decTable2[n7 >>> 16 & 0xFF] ^ this.decTable3[n6 >>> 8 & 0xFF] ^ this.decTable4[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.decTable1[n5 >>> 24] ^ this.decTable2[n4 >>> 16 & 0xFF] ^ this.decTable3[n7 >>> 8 & 0xFF] ^ this.decTable4[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.decTable1[n6 >>> 24] ^ this.decTable2[n5 >>> 16 & 0xFF] ^ this.decTable3[n4 >>> 8 & 0xFF] ^ this.decTable4[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.decTable1[n7 >>> 24] ^ this.decTable2[n6 >>> 16 & 0xFF] ^ this.decTable3[n5 >>> 8 & 0xFF] ^ this.decTable4[n4 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.roundKeys[0];
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n12 & 0xFF] ^ n5);
      n5 = this.roundKeys[1];
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n13 & 0xFF] ^ n5);
      n5 = this.roundKeys[2];
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n7 & 0xFF] ^ n5);
      n5 = this.roundKeys[3];
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2] = (byte)(this.invSBox[n11 & 0xFF] ^ n5);
   }

   public int unpadPkcs5(byte[] bytes, int n, int n2) {
      int n3 = 0;
      byte b = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      byte b2 = 14;

      outerLoop:
      while (true) {
         if (9 >= b2) {
            if (n6 >= n) {
               continue;
            }
         } else if (b2 >= 14) {
            if (14 >= b2) {
               return n6;
            }

            if (b2 >= 16) {
               if (b2 <= 16) {
                  if (n3 >= 1) {
                     continue;
                  }
               } else {
                  if (b2 <= 17) {
                     b = bytes[n4 - 1];
                     continue;
                  }

                  if (bytes[n5] == b) {
                  }
               }
            } else if (bytes != null) {
               ;
            }
         } else if (b2 >= 11) {
            if (b2 <= 11) {
               n3 = b & 255;
               continue;
            }

            if (b2 > 12) {
               n5 = n6;
            }
         } else if (n3 > this.getBlockSize()) {
            ;
         }

         while (b2 >= 4) {
            if (4 < b2) {
               if (6 <= b2) {
                  if (b2 <= 6) {
                     return -1;
                  }

                  if (b2 <= 7) {
                     return -1;
                  }

                  return -1;
               }

               n4 = Math.addExact(n, n2);
               continue outerLoop;
            }

            if (n2 == 0) {
               ;
            }
         }

         if (1 <= b2) {
            if (b2 <= 1) {
               if (n5 < n4) {
                  ;
               }
            } else {
               if (2 < b2) {
                  return 0;
               }

               n5++;
            }
         } else {
            n6 = n4 - n3;
         }
      }
   }

   private byte[] prepareInputBuffer(byte[] bytes, int n, int n2, boolean flag) throws IllegalBlockSizeException, ShortBufferException {
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      byte b = 0;
      byte[] bytes2 = null;
      int n6 = 0;
      int n7 = 17;
      long l = 5919083419981650342L;
      long l2 = 0L;
      l = 9156780610783890277L ^ (long)"亗풂蹪檒頼Ↄ㛷똢䛃\ue963⯡䟁Ꮟ坢㷆ፚ延櫍師༔廥缱".hashCode();
      l2 = -4991507612178248793L ^ (long)"哞駼퍯\udf5f拺䅒\ueb99㗈狴誩툽\u0c4f誰\ue39a뎺茉腲₇怣\ud95f㙸•".hashCode();

      while (true) {
         n7 = (int)(l % 19L);
         if (9 <= n7) {
            if (n7 <= 9) {
               n3 = n6;
               l = l + 1596009904243288320L ^ 4659826104647970488L;
               l2 = l2 + -7804614453267296808L ^ 5397996192722904027L;
            } else if (n7 >= 14) {
               if (n7 <= 14) {
                  if (n5 == 0) {
                     l = l + -1533587432254200141L ^ 759866298263094814L;
                     l2 = l2 + -3856820617138784302L ^ -7457135176235281708L;
                  } else {
                     l = l + 3558473360297163764L ^ -5784494161223782058L;
                     l2 = l2 - 5884829167173958103L ^ 4935579341965150101L;
                  }
               } else {
                  if (16 > n7) {
                     throw new IllegalBlockSizeException("Input length must be multiple of " + n4 + " when decrypting with padded cipher");
                  }

                  if (16 >= n7) {
                     System.arraycopy(bytes, n, bytes2, b, n2);
                     l = l + -2483957093407088384L ^ 4753965613039793519L;
                     l2 = (l2 ^ 7513415117801056240L) + 3522727122423609290L;
                  } else if (17 >= n7) {
                     this.padWithLen(bytes2, Math.addExact(b, n2), n5);
                     l = l + 6699332210604850362L ^ -4656172520253847849L;
                     l2 = l2 + -7881392755509646615L ^ -2360173924715741085L;
                  } else if (n5 == n4) {
                     l = (l ^ 0L) - 0L;
                     l2 = l2 + 0L ^ 0L;
                  } else {
                     l = l + 0L ^ 0L;
                     l2 = (l2 ^ 0L) - 0L;
                  }
               }
            } else {
               if (11 > n7) {
                  return bytes;
               }

               if (11 >= n7) {
                  return bytes2;
               }

               if (n7 <= 12) {
                  if (!flag) {
                     l = l + 8890390830644036823L ^ -1694337282531273244L;
                     l2 = (l2 ^ -7530131861455042976L) - -2088863322058625929L;
                  } else {
                     l = (l ^ 0L) + 0L;
                     l2 = (l2 ^ 0L) - 0L;
                  }
               } else if (flag) {
                  l = l - 0L ^ 0L;
                  l2 = l2 - 0L ^ 0L;
               } else {
                  l = (l ^ 7147085507467829037L) + -793031550692231805L;
                  l2 = l2 - 2034259291038640346L ^ 5036870133796003660L;
               }
            }
         } else if (n7 >= 4) {
            if (n7 <= 4) {
               n5 = 0;
               l = (l ^ 2877749379949400937L) - -3060784563780302257L;
               l2 = (l2 ^ 4525622396624325864L) - -1200802507660572571L;
            } else if (n7 >= 6) {
               if (n7 <= 6) {
                  bytes2 = new byte[Math.addExact(n6, n5)];
                  l = l + 6855635667547281800L ^ -3927183552887548586L;
                  l2 = (l2 ^ -8206180639081984295L) - 2946804713217076384L;
               } else if (7 >= n7) {
                  n5 = this.getPadLength(n3);
                  l = l - 8193660766574032263L ^ -3496449846707554227L;
                  l2 = l2 + 2157069174843145888L ^ -1149288124210365859L;
               } else {
                  n6 = Math.addExact(b, n2);
                  l = (l ^ 6850658092401406471L) - -526565024961209290L;
                  l2 = (l2 ^ 1048946456464210035L) - 6375863646041899282L;
               }
            } else if (n2 == 0) {
               l = (l ^ 1804734384949001534L) + -125846514045986421L;
               l2 = l2 + 6328893153843880121L ^ 3801429693504435464L;
            } else {
               l = l + -8015140640638138229L ^ -1332736524169131907L;
               l2 = (l2 ^ 8418239556613185770L) + 2290986730017782529L;
            }
         } else if (n7 >= 1) {
            if (n7 <= 1) {
               int n9 = n2 % n4;
               l = l - -7917513689083257972L ^ -2384762974450393060L;
               l2 = l2 + 1743543664700762836L ^ -6984068443941030008L;
            } else if (2 >= n7) {
               if (n5 <= 0) {
                  l = (l ^ 0L) + 0L;
                  l2 = (l2 ^ 0L) - 0L;
               } else {
                  l = l + 0L ^ 0L;
                  l2 = l2 + 0L ^ 0L;
               }
            } else {
               b = 0;
               l = l - -2325758640678552981L ^ -825212588311354330L;
               l2 = (l2 ^ 6597711941646414869L) - 4388980971094356140L;
            }
         } else {
            n4 = this.getBlockSize();
            l = l + 8049539519477385429L ^ -1989875664809775003L;
            l2 = (l2 ^ -7848790977551972354L) + 8162234499965703501L;
         }
      }
   }

   public VendorPacketDecryptor(PipelineTimingGuard pipelineTimingGuard) {
       super();
      int n = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0, n6 = 0, n7 = 0, n8 = 0;
      this.secondaryKeyGuard = new byte[1];
      this.derivedKeySlot44 = new byte[1];
      this.derivedKeySlot88 = new byte[1];
      this.derivedKeySlot21 = new byte[1];
      this.derivedKeySlot19 = new byte[1];
      this.derivedKeySlot52 = new byte[1];
      this.derivedKeySlot77 = new byte[1];
      this.derivedKeySlot08 = new byte[1];
      String opaqueStateTag = VendorPacketBuffer.getOpaqueStateTag();
      this.derivedKeySlot39 = new byte[1];
      this.derivedKeySlot36 = new byte[1];
      this.derivedKeySlot92 = new byte[1];
      this.derivedKeySlot06 = new byte[1];
      this.derivedKeySlot11 = new byte[1];
      this.derivedKeySlot10 = new byte[1];
      this.derivedKeySlot90 = new byte[1];
      this.derivedKeySlot78 = new byte[1];
      this.derivedKeySlot79 = new byte[1];
      this.derivedKeySlot84 = new byte[1];
      this.derivedKeySlot95 = new byte[1];
      this.derivedKeySlot17 = new byte[1];
      this.derivedKeySlot50 = new byte[1];
      this.derivedKeySlot22 = new byte[1];
      this.derivedKeySlot68 = new byte[1];
      String str = opaqueStateTag;
      this.derivedKeySlot66 = new byte[1];
      this.derivedKeySlot60 = new byte[1];
      this.derivedKeySlot42 = new byte[1];
      this.derivedKeySlot41 = new byte[1];
      this.derivedKeySlot87 = new byte[1];
      this.derivedKeySlot56 = new byte[1];
      this.derivedKeySlot89 = new byte[1];
      this.derivedKeySlot12 = new byte[1];
      this.derivedKeySlot70 = new byte[1];
      this.derivedKeySlot14 = new byte[1];
      this.derivedKeySlot32 = new byte[1];
      this.derivedKeySlot29 = new byte[1];
      this.derivedKeySlot64 = new byte[1];
      this.derivedKeySlot15 = new byte[1];
      this.derivedKeySlot31 = new byte[1];
      this.derivedKeySlot80 = new byte[1];
      this.derivedKeySlot62 = new byte[1];
      this.derivedKeySlot67 = new byte[1];
      this.derivedKeySlot93 = new byte[1];
      this.derivedKeySlot75 = new byte[1];
      this.derivedKeySlot43 = new byte[1];
      this.derivedKeySlot46 = new byte[1];
      this.derivedKeySlot83 = new byte[1];
      this.derivedKeySlot94 = new byte[1];
      this.derivedKeySlot03 = new byte[1];
      this.derivedKeySlot27 = new byte[1];
      this.derivedKeySlot59 = new byte[1];
      this.derivedKeySlot49 = new byte[1];
      this.derivedKeySlot53 = new byte[1];
      this.derivedKeySlot45 = new byte[1];
      this.derivedKeySlot99 = new byte[1];
      this.derivedKeySlot07 = new byte[1];
      this.derivedKeySlot74 = new byte[1];
      this.derivedKeySlot71 = new byte[1];
      this.derivedKeySlot61 = new byte[1];
      this.derivedKeySlot98 = new byte[1];
      this.derivedKeySlot54 = new byte[1];
      this.derivedKeySlot24 = new byte[1];
      this.derivedKeySlot47 = new byte[1];
      this.derivedKeySlot57 = new byte[1];
      this.derivedKeySlot48 = new byte[1];
      this.derivedKeySlot26 = new byte[1];
      this.derivedKeySlot09 = new byte[1];
      this.derivedKeySlot55 = new byte[1];
      this.derivedKeySlot69 = new byte[1];
      this.derivedKeySlot01 = new byte[1];
      this.derivedKeySlot86 = new byte[1];
      this.derivedKeySlot76 = new byte[1];
      this.derivedKeySlot85 = new byte[1];
      this.derivedKeySlot18 = new byte[1];
      this.derivedKeySlot51 = new byte[1];
      this.derivedKeySlot37 = new byte[1];
      this.derivedKeySlot33 = new byte[1];
      this.derivedKeySlot34 = new byte[1];
      this.derivedKeySlot63 = new byte[1];
      this.derivedKeySlot97 = new byte[1];
      this.derivedKeySlot25 = new byte[1];
      this.derivedKeySlot73 = new byte[1];
      this.derivedKeySlot91 = new byte[1];
      this.derivedKeySlot35 = new byte[1];
      this.derivedKeySlot23 = new byte[1];
      this.derivedKeySlot30 = new byte[1];
      this.derivedKeySlot40 = new byte[1];
      this.derivedKeySlot38 = new byte[1];
      this.derivedKeySlot04 = new byte[1];
      this.derivedKeySlot16 = new byte[1];
      this.derivedKeySlot96 = new byte[1];
      this.derivedKeySlot72 = new byte[1];
      this.derivedKeySlot28 = new byte[1];
      this.derivedKeySlot20 = new byte[1];
      this.derivedKeySlot13 = new byte[1];
      this.derivedKeySlot81 = new byte[1];
      this.derivedKeySlot65 = new byte[1];
      this.derivedKeySlot58 = new byte[1];
      this.derivedKeySlot82 = new byte[1];
      this.derivedKeySlot05 = new byte[1];
      this.derivedKeySlot02 = new byte[1];
      this.decoyKeySlot09 = new byte[1];
      this.decoyKeySlot03 = new byte[1];
      this.decoyKeySlot96 = new byte[1];
      this.decoyKeySlot25 = new byte[1];
      this.decoyKeySlot21 = new byte[1];
      this.decoyKeySlot64 = new byte[1];
      this.decoyKeySlot47 = new byte[1];
      this.decoyKeySlot16 = new byte[1];
      this.decoyKeySlot93 = new byte[1];
      this.decoyKeySlot38 = new byte[1];
      this.decoyKeySlot35 = new byte[1];
      this.decoyKeySlot05 = new byte[1];
      this.decoyKeySlot29 = new byte[1];
      this.decoyKeySlot23 = new byte[1];
      this.decoyKeySlot08 = new byte[1];
      this.decoyKeySlot92 = new byte[1];
      this.decoyKeySlot46 = new byte[1];
      this.decoyKeySlot63 = new byte[1];
      this.decoyKeySlot53 = new byte[1];
      this.decoyKeySlot10 = new byte[1];
      this.decoyKeySlot54 = new byte[1];
      this.decoyKeySlot26 = new byte[1];
      this.decoyKeySlot50 = new byte[1];
      this.decoyKeySlot80 = new byte[1];
      this.decoyKeySlot79 = new byte[1];
      this.decoyKeySlot83 = new byte[1];
      this.decoyKeySlot20 = new byte[1];
      this.decoyKeySlot91 = new byte[1];
      this.decoyKeySlot28 = new byte[1];
      this.decoyKeySlot14 = new byte[1];
      this.decoyKeySlot02 = new byte[1];
      this.decoyKeySlot94 = new byte[1];
      this.decoyKeySlot87 = new byte[1];
      this.decoyKeySlot65 = new byte[1];
      this.decoyKeySlot76 = new byte[1];
      this.decoyKeySlot19 = new byte[1];
      this.decoyKeySlot52 = new byte[1];
      this.decoyKeySlot30 = new byte[1];
      this.decoyKeySlot24 = new byte[1];
      this.decoyKeySlot49 = new byte[1];
      this.decoyKeySlot34 = new byte[1];
      this.decoyKeySlot99 = new byte[1];
      this.decoyKeySlot61 = new byte[1];
      this.decoyKeySlot77 = new byte[1];
      this.decoyKeySlot13 = new byte[1];
      this.decoyKeySlot71 = new byte[1];
      this.decoyKeySlot42 = new byte[1];
      this.decoyKeySlot11 = new byte[1];
      this.decoyKeySlot81 = new byte[1];
      this.decoyKeySlot33 = new byte[1];
      this.decoyKeySlot40 = new byte[1];
      this.decoyKeySlot84 = new byte[1];
      this.decoyKeySlot41 = new byte[1];
      this.decoyKeySlot55 = new byte[1];
      this.decoyKeySlot12 = new byte[1];
      this.decoyKeySlot89 = new byte[1];
      this.decoyKeySlot31 = new byte[1];
      this.decoyKeySlot48 = new byte[1];
      this.decoyKeySlot85 = new byte[1];
      this.decoyKeySlot68 = new byte[1];
      this.decoyKeySlot37 = new byte[1];
      this.decoyKeySlot45 = new byte[1];
      this.decoyKeySlot17 = new byte[1];
      this.decoyKeySlot56 = new byte[1];
      this.decoyKeySlot73 = new byte[1];
      this.decoyKeySlot36 = new byte[1];
      this.decoyKeySlot22 = new byte[1];
      this.decoyKeySlot86 = new byte[1];
      this.decoyKeySlot60 = new byte[1];
      this.decoyKeySlot07 = new byte[1];
      this.decoyKeySlot97 = new byte[1];
      this.decoyKeySlot69 = new byte[1];
      this.decoyKeySlot74 = new byte[1];
      this.decoyKeySlot66 = new byte[1];
      this.decoyKeySlot18 = new byte[1];
      this.decoyKeySlot82 = new byte[1];
      this.decoyKeySlot51 = new byte[1];
      this.decoyKeySlot01 = new byte[1];
      this.decoyKeySlot88 = new byte[1];
      this.decoyKeySlot15 = new byte[1];
      this.decoyKeySlot90 = new byte[1];
      this.decoyKeySlot39 = new byte[1];
      this.decoyKeySlot62 = new byte[1];
      this.decoyKeySlot04 = new byte[1];
      this.decoyKeySlot78 = new byte[1];
      this.decoyKeySlot95 = new byte[1];
      this.decoyKeySlot27 = new byte[1];
      this.decoyKeySlot98 = new byte[1];
      this.decoyKeySlot57 = new byte[1];
      this.decoyKeySlot32 = new byte[1];
      this.decoyKeySlot70 = new byte[1];
      this.decoyKeySlot44 = new byte[1];
      this.decoyKeySlot75 = new byte[1];
      this.decoyKeySlot67 = new byte[1];
      this.decoyKeySlot59 = new byte[1];
      this.decoyKeySlot58 = new byte[1];
      this.decoyKeySlot72 = new byte[1];
      this.primaryKeyGuard = new byte[1];
      this.decoyKeySlot43 = new byte[1];
      this.decoyKeySlot06 = new byte[1];
      this.sessionKey = new byte[1];
      this.has12OrMoreRounds = false;
      this.has14Rounds = false;
      this.sessionKeySchedule = (int[][])null;
      this.roundKeys = null;
      this.roundKeyLimit = 0;
      this.gfAntilogTable = new int[256];
      this.gfLogTable = new int[256];
      this.sBox = new byte[256];
      this.invSBox = new byte[256];
      this.encTable1 = new int[256];
      this.encTable2 = new int[256];
      this.encTable3 = new int[256];
      this.encTable4 = new int[256];
      this.decTable1 = new int[256];
      this.decTable2 = new int[256];
      this.decTable3 = new int[256];
      this.decTable4 = new int[256];
      this.keyTable1 = new int[256];
      this.keyTable2 = new int[256];
      this.keyTable3 = new int[256];
      this.keyTable4 = new int[256];
      this.roundConstants = new byte[30];
      this.timingGuard = pipelineTimingGuard;
      this.cbcWorkBuffer = new byte[this.getBlockSize()];
      this.cbcRegister = new byte[this.getBlockSize()];
      int n9 = 0;
      this.gfAntilogTable[0] = 1;
      int n10 = 1;

      while (true) {
         if (n10 < 256) {
            n9 = this.gfAntilogTable[n10 - 1] << 1 ^ this.gfAntilogTable[n10 - 1];
            if (str == null) {
               n = n9 & 256;
               if (str != null) {
                  break;
               }

               if (n != 0) {
                  n9 ^= (short)283;
               }

               this.gfAntilogTable[n10] = n9;
               n10++;
            }

            if (str == null) {
               continue;
            }

            Setting.setControlFlowMarker("aniynb");
         }

         n = 1;
         break;
      }

      n10 = n;

      while (n10 < 255) {
         this.gfLogTable[this.gfAntilogTable[n10]] = n10++;
         if (str != null) {
            break;
         }
      }

      byte[][] bytesArr = new byte[][]{
         {1, 1, 1, 1, 1, 0, 0, 0},
         {0, 1, 1, 1, 1, 1, 0, 0},
         {0, 0, 1, 1, 1, 1, 1, 0},
         {0, 0, 0, 1, 1, 1, 1, 1},
         {1, 0, 0, 0, 1, 1, 1, 1},
         {1, 1, 0, 0, 0, 1, 1, 1},
         {1, 1, 1, 0, 0, 0, 1, 1},
         {1, 1, 1, 1, 0, 0, 0, 1}
      };
      byte[] bytes = new byte[]{0, 1, 1, 0, 0, 0, 1, 1};
      byte[][] bytesArr2 = new byte[256][8];
      bytesArr2[1][7] = 1;
      n10 = 2;

      while (true) {
         if (n10 < 256) {
            n9 = this.gfAntilogTable[255 - this.gfLogTable[n10]];
            n2 = 0;
            if (str != null) {
               break;
            }

            int n11 = 0;

            exitBlock: {
               while (n11 < 8) {
                  bytesArr2[n10][n11] = (byte)(n9 >>> 7 - n11 & 1);
                  n11++;
                  if (str != null) {
                     break exitBlock;
                  }

                  if (str != null) {
                     break;
                  }
               }

               n10++;
            }

            if (str == null) {
               continue;
            }
         }

         n2 = 256;
         break;
      }

      byte[][] bytesArr3 = new byte[n2][8];
      n10 = 0;

      outerLoop:
      while (true) {
         int n12 = n10;

         outerLoop2:
         while (n12 < 256) {
            n3 = 0;
            if (str != null) {
               break outerLoop;
            }

            int n13 = 0;

            while (n13 < 8) {
               bytesArr3[n10][n13] = bytes[n13];
               n12 = 0;
               if (str != null) {
                  continue outerLoop2;
               }

               n9 = 0;

               while (true) {
                  if (n9 < 8) {
                     bytesArr3[n10][n13] = (byte)(bytesArr3[n10][n13] ^ bytesArr[n13][n9] * bytesArr2[n10][n9]);
                     n9++;
                     if (str != null) {
                        break;
                     }

                     if (str == null) {
                        continue;
                     }
                  }

                  n13++;
                  break;
               }

               if (str != null) {
                  break;
               }
            }

            n10++;
            if (str == null) {
               continue outerLoop;
            }
            break;
         }

         n3 = 0;
         break;
      }

      n10 = n3;

      while (true) {
         if (n10 < 256) {
            this.sBox[n10] = (byte)(bytesArr3[n10][0] << 7);
            n4 = 1;
            if (str != null) {
               break;
            }

            int n14 = 1;

            exitBlock2: {
               while (n14 < 8) {
                  this.sBox[n10] = (byte)(this.sBox[n10] ^ bytesArr3[n10][n14] << 7 - n14);
                  n14++;
                  if (str != null) {
                     break exitBlock2;
                  }

                  if (str != null) {
                     break;
                  }
               }

               this.invSBox[this.sBox[n10] & 255] = (byte)n10;
               n10++;
            }

            if (str == null) {
               continue;
            }
         }

         n4 = 4;
         break;
      }

      byte[][] bytesArr4 = new byte[n4][];
      bytesArr4[0] = new byte[]{2, 1, 1, 3};
      bytesArr4[1] = new byte[]{3, 2, 1, 1};
      bytesArr4[2] = new byte[]{1, 3, 2, 1};
      bytesArr4[3] = new byte[]{1, 1, 3, 2};
      byte[][] bytesArr5 = bytesArr4;
      byte[][] bytesArr6 = new byte[4][8];
      n10 = 0;

      while (true) {
         if (n10 < 4) {
            n5 = 0;
            if (str != null) {
               break;
            }

            n9 = 0;

            exitBlock3: {
               while (n9 < 4) {
                  bytesArr6[n10][n9] = bytesArr5[n10][n9];
                  n9++;
                  if (str != null) {
                     break exitBlock3;
                  }

                  if (str != null) {
                     break;
                  }
               }

               bytesArr6[n10][n10 + 4] = 1;
               n10++;
            }

            if (str == null) {
               continue;
            }
         }

         n5 = 4;
         break;
      }

      byte[][] bytesArr7 = new byte[n5][4];
      n10 = 0;

      outerLoop3:
      while (true) {
         int n15 = n10;
         int n16 = 4;

         outerLoop4:
         while (n15 < n16) {
            byte b = bytesArr6[n10][n10];
            n6 = b;
            if (str != null) {
               break outerLoop3;
            }

            if (str == null) {
               exitBlock4:
               if (b == 0) {
                  int n17 = n10 + 1;

                  while (true) {
                     if (bytesArr6[n17][n10] == 0) {
                        n17++;
                        if (str != null) {
                           break;
                        }

                        if (str == null) {
                           continue;
                        }
                     }

                     n9 = 0;
                     break;
                  }

                  while (n9 < 8) {
                     byte b2 = bytesArr6[n10][n9];
                     bytesArr6[n10][n9] = bytesArr6[n17][n9];
                     bytesArr6[n17][n9] = b2;
                     n9++;
                     if (str != null) {
                        break exitBlock4;
                     }

                     if (str != null) {
                        break;
                     }
                  }

                  b = bytesArr6[n10][n10];
               }

               n6 = 0;
            }

            n9 = n6;

            while (true) {
               if (n9 < 8) {
                  exitBlock5: {
                     byte[] bytes2 = bytesArr6[n10];
                     n16 = n9;
                     if (str == null) {
                        n7 = bytes2[n9];
                        if (str != null) {
                           break;
                        }

                        if (n7 == 0) {
                           break exitBlock5;
                        }

                        bytes2 = bytesArr6[n10];
                        n16 = n9;
                     }

                     bytes2[n16] = (byte)this.gfAntilogTable[(255 + this.gfLogTable[bytesArr6[n10][n9] & 255] - this.gfLogTable[b & 255]) % 255];
                  }

                  n9++;
                  if (str == null) {
                     continue;
                  }
               }

               n7 = 0;
               break;
            }

            int n18 = n7;

            while (n18 < 4) {
               n15 = n10;
               n16 = n18;
               if (str != null) {
                  continue outerLoop4;
               }

               exitBlock6: {
                  exitBlock7: {
                     if (str == null) {
                        if (n10 == n18) {
                           break exitBlock7;
                        }

                        n15 = n10;
                        n16 = 1;
                     }

                     n9 = n15 + n16;

                     while (n9 < 8) {
                        bytesArr6[n18][n9] = (byte)(bytesArr6[n18][n9] ^ this.gfMul(bytesArr6[n10][n9], bytesArr6[n18][n10]));
                        n9++;
                        if (str != null) {
                           break exitBlock6;
                        }

                        if (str != null) {
                           break;
                        }
                     }

                     bytesArr6[n18][n10] = 0;
                  }

                  n18++;
               }

               if (str != null) {
                  break;
               }
            }

            n10++;
            if (str == null) {
               continue outerLoop3;
            }
            break;
         }

         n6 = 0;
         break;
      }

      n10 = n6;

      while (true) {
         if (n10 < 4) {
            n8 = 0;
            if (str != null) {
               break;
            }

            n9 = 0;

            exitBlock8: {
               while (n9 < 4) {
                  bytesArr7[n10][n9] = bytesArr6[n10][n9 + 4];
                  n9++;
                  if (str != null) {
                     break exitBlock8;
                  }

                  if (str != null) {
                     break;
                  }
               }

               n10++;
            }

            if (str == null) {
               continue;
            }
         }

         n8 = 0;
         break;
      }

      int n19 = n8;

      while (true) {
         if (n19 < 256) {
            byte b7 = this.sBox[n19];
            byte[] bytes4 = bytesArr5[0];
            this.encTable1[n19] = this.mul4(b7, bytes4);
            byte[] bytes5 = bytesArr5[1];
            this.encTable2[n19] = this.mul4(b7, bytes5);
            byte[] bytes6 = bytesArr5[2];
            this.encTable3[n19] = this.mul4(b7, bytes6);
            byte[] bytes7 = bytesArr5[3];
            this.encTable4[n19] = this.mul4(b7, bytes7);
            b7 = this.invSBox[n19];
            byte[] bytes8 = bytesArr7[0];
            this.decTable1[n19] = this.mul4(b7, bytes8);
            byte[] bytes9 = bytesArr7[1];
            this.decTable2[n19] = this.mul4(b7, bytes9);
            byte[] bytes10 = bytesArr7[2];
            this.decTable3[n19] = this.mul4(b7, bytes10);
            byte[] bytes11 = bytesArr7[3];
            this.decTable4[n19] = this.mul4(b7, bytes11);
            byte[] bytes12 = bytesArr7[0];
            this.keyTable1[n19] = this.mul4(n19, bytes12);
            byte[] bytes13 = bytesArr7[1];
            this.keyTable2[n19] = this.mul4(n19, bytes13);
            byte[] bytes14 = bytesArr7[2];
            this.keyTable3[n19] = this.mul4(n19, bytes14);
            byte[] bytes15 = bytesArr7[3];
            this.keyTable4[n19] = this.mul4(n19, bytes15);
            n19++;
            if (str != null) {
               break;
            }

            if (str == null) {
               continue;
            }
         }

         this.roundConstants[0] = 1;
         break;
      }

      int n20 = 1;
      n19 = 1;

      while (true) {
         if (n19 < 30) {
            n20 = this.gfMul((byte)2, n20);
            this.roundConstants[n19] = (byte)n20;
            n19++;
            if (str != null) {
               break;
            }

            if (str == null) {
               continue;
            }
         }

         this.gfLogTable = null;
         this.gfAntilogTable = null;
         break;
      }
   }

   public int getPadLength(int n) {
      return 0 - n % 0;
   }

   public void resetCbcState() {
      this.cbcRegister = new byte[this.getBlockSize()];
      this.cbcWorkBuffer = new byte[this.getBlockSize()];
   }

   public byte[] decryptBytes(byte[] bytes, int n, int n2) {
      byte[] bytes2 = null;
      int n3 = 0;
      byte b = 1;
      long l = -167993852195265354L;
      l = 2841631977261192465L ^ (long)"߬삤㪘ﻉ唄얼薯뇵誐呍胑縑\udd9d歁鈽偘馎澀斐䳫".hashCode();

      while (true) {
         while (b > 3) {
            if (5 > b) {
               n3 = this.unpadPkcs5(bytes2, (byte)0, bytes.length);
               l = (l ^ -6186037192370624549L) + -9172736541338697172L;
               return Arrays.copyOf(bytes2, n3);
            }

            if (5 < b) {
               return null;
            }

            VendorPacketDecryptor vendorPacketDecryptor2 = this;
            vendorPacketDecryptor2.checkBlockAlignment(n2, this.getBlockSize());
            l = l + -4184061566600865961L ^ 8175153446047758892L;
            vendorPacketDecryptor2 = this;
            vendorPacketDecryptor2.decryptCbc(bytes, n, n2, bytes2, (byte)0);
            l = l + 5564674026640594404L ^ -1238783150630161229L;
         }

         if (n2 > 0) {
            l = (l ^ 5019912077987814106L) - 3588767304708071814L;
            bytes2 = new byte[n2];
            l = (l ^ -7346939454461457614L) + -6314250181547733133L;
         } else {
            l = (l ^ 701029761923971185L) + -2991781974865007702L;
         }
      }
   }

   public void decodeBuffer(ChannelHandlerContext ctx, ByteBuf buf, List list) {
      list.add(this.decryptBuffer(buf));
   }

   private int[] expandToSubkeys(int[][] intsArr, boolean flag) {
      int[] ints = null;
      int n = 0;
      int n2 = 0;
      byte b = 5;

      outerLoop:
      while (true) {
         if (3 <= b) {
            if (3 >= b) {
               return ints;
            }

            if (b >= 5) {
               if (b <= 5) {
                  n2++;
                  continue;
               }

               n = intsArr.length;
            } else if (n2 < n) {
               ;
            }
         } else if (b >= 1) {
            if (b > 1) {
               n2++;
            }
            continue;
         }

         while (true) {
            while (7 < b) {
               if (b < 11) {
                  if (9 <= b) {
                     if (9 >= b) {
                        n2 = 1;
                     }
                  } else {
                     System.arraycopy(intsArr[n2], 0, ints, n2 * 4, 4);
                  }
                  continue outerLoop;
               }

               if (11 >= b) {
                  if (flag) {
                     ;
                  }
               } else if (b >= 13) {
                  if (b <= 13) {
                     if (n2 >= n) {
                        continue outerLoop;
                     }
                  } else {
                     if (14 >= b) {
                        System.arraycopy(intsArr[n2 - 1], 0, ints, n2 * 4, 4);
                        continue outerLoop;
                     }

                     ints = new int[n * 4];
                  }
               } else {
                  n2 = 0;
               }
            }

            System.arraycopy(intsArr[n - 1], 0, ints, 0, 4);
         }
      }
   }

   public void decode(ChannelHandlerContext ctx, Object in, List out) throws Exception {
      this.decodeBuffer(ctx, (ByteBuf)in, out);
   }

   private int encryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = 0;
      int n5 = 0;
      int n6 = 6;
      long l = -5827357650397337624L;
      long l2 = 0L;
      l = 3608117788060887368L ^ (long)"\ueade筅뢥ﺰ\ue471沫\ud985䭈铡筏\ue779ꡖ䣂佥頯畋ﮝ䲚治鶧凁ḏ렭\u0bbb棜툮".hashCode();
      l2 = 6614862461032979382L ^ (long)"\uf214䙬斄\u1f47䳅℞킵摊碩㩭㪷◟䵆桎ꨕ鳹楬Ṉ䇒쐫\ue8d8\ue45e皅兼ၩ䇏❗ꯞ".hashCode();

      while (true) {
         n6 = (int)(l % 13L);
         if (n6 >= 6) {
            if (6 >= n6) {
               System.arraycopy(bytes2, n3, this.cbcRegister, 0, 0);
               l = (l ^ 366744828858005873L) + 2206088760065378018L;
               l2 = (l2 ^ -5972795115998988311L) - -6561141013743563661L;
            } else if (n6 >= 9) {
               if (9 >= n6) {
                  this.cbcWorkBuffer[n5] = (byte)(bytes[n5 + n] ^ this.cbcRegister[n5]);
                  l = (l ^ 2599735220114915L) + -3114615656114013870L;
                  l2 = l2 - -7266947917957077753L ^ 3999334676821288123L;
               } else if (n6 >= 11) {
                  if (n6 <= 11) {
                     n5++;
                     l = (l ^ 3785087887121356269L) + 4817608592612460079L;
                     l2 = (l2 ^ 2990180230961667325L) + -2731752945605944896L;
                  } else {
                     n += 0;
                     l = l - -5741742049577133791L ^ -4269434055266693611L;
                     l2 = (l2 ^ -5076099263389805933L) - -7280041275560416007L;
                  }
               } else {
                  l = (l ^ 5470083236391477869L) + 222496465015348917L;
                  l2 = (l2 ^ 2935944987540186170L) + 2860605769984745947L;
               }
            } else if (7 >= n6) {
               n3 += 0;
               l = (l ^ -7541634666678849814L) + -8703123169130407202L;
               l2 = l2 + 424143631963951974L ^ 9076414996565935685L;
            } else {
               n4 = n + n2;
               l = l + -4761924154143356929L ^ -7627405245455111569L;
               l2 = l2 - -6552352055631940580L ^ 2434165139750150682L;
            }
         } else if (n6 >= 2) {
            if (2 >= n6) {
               l = (l ^ -5948760674355492623L) - -7347926679403161124L;
               l2 = l2 + 9114210671583449464L ^ -3321503937378519299L;
            } else if (n6 >= 4) {
               if (n6 <= 4) {
                  if (n5 >= 0) {
                     l = l + -32112263841439264L ^ 8901836314935402536L;
                     l2 = l2 + -8686500540377109714L ^ -8961938695819096560L;
                  } else {
                     l = l + 3747443011191689555L ^ 5051055897963655532L;
                     l2 = (l2 ^ -777285790479896110L) + 3348990448319722490L;
                  }
               } else {
                  l = l + -6962913749995673388L ^ 8151253361314291874L;
                  l2 = (l2 ^ -1420323925218641492L) - 6278606725012127031L;
               }
            } else if (n >= n4) {
               l = (l ^ 6196812316332516940L) - -2365190393883976451L;
               l2 = l2 - -7755525275249957638L ^ 7470424198019026091L;
            } else {
               l = l - -7590998995132579555L ^ -5674741861593475085L;
               l2 = l2 - -2158733815907740020L ^ -3277362342711333882L;
            }
         } else {
            if (n6 > 0) {
               return n2;
            }

            n5 = 0;
            l = l + 8194466492693584369L ^ -776083370321166045L;
            l2 = l2 + -6393185697987531165L ^ 132424658384284030L;
         }
      }
   }

   public void encryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.encryptBlockImpl(bytes, n, bytes2, n2);
   }

   private int getRounds(int n) {
      return (n >> 2) + 6;
   }

   public void initCipher(boolean flag, byte[] bytes) {
      String opaqueStateTag = VendorPacketBuffer.getOpaqueStateTag();

      try {
         this.makeSessionKey(bytes);
         int n = (flag) ? 1 : 0;
         if (opaqueStateTag == null) {
            n = flag ? 1 : 0;
         }

         this.roundKeys = this.sessionKeySchedule[n];
      } catch (InvalidKeyException e2) {
      }
   }



}
