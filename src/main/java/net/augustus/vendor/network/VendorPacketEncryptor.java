package net.augustus.vendor.network;

import net.augustus.settings.Setting;
import net.augustus.vendor.crypto.VendorAesHelper;
import net.augustus.vendor.crypto.aes.PaddedAesCipher;
import net.augustus.vendor.crypto.keccak.KeccakSha3Digest;
import net.augustus.vendor.crypto.keccak.Sha3Algorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.security.InvalidKeyException;
import java.security.ProviderException;
import java.util.Arrays;
import java.util.List;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

public class VendorPacketEncryptor extends MessageToMessageEncoder {
   private byte[] derivedKeySlot76;
   private byte[] decoyKeySlot89;
   private byte[] derivedKeySlot97;
   private byte[] decoyKeySlot33;
   private byte[] derivedKeySlot81;
   private byte[] derivedKeySlot59;
   private byte[] decoyKeySlot61;
   private byte[] derivedKeySlot06;
   private byte[] derivedKeySlot53;
   private byte[] derivedKeySlot83;
   private byte[] derivedKeySlot89;
   private byte[] decoyKeySlot87;
   private byte[] decoyKeySlot93;
   private byte[] derivedKeySlot58;
   private byte[] derivedKeySlot60;
   private byte[] derivedKeySlot45;
   private byte[] derivedKeySlot77;
   private byte[] decoyKeySlot21;
   private boolean has12OrMoreRounds;
   private byte[] derivedKeySlot30;
   private int[] gfLogTable;
   private byte[] derivedKeySlot23;
   private byte[] decoyKeySlot41;
   private byte[] derivedKeySlot36;
   private byte[] cbcWorkBuffer;
   private final int[] decTable4;
   private byte[] derivedKeySlot51;
   private int[][] sessionKeySchedule;
   private int[] roundKeys;
   private byte[] derivedKeySlot33;
   private byte[] decoyKeySlot34;
   private byte[] derivedKeySlot19;
   private byte[] derivedKeySlot24;
   private byte[] derivedKeySlot92;
   private byte[] decoyKeySlot38;
   private byte[] decoyKeySlot94;
   private byte[] derivedKeySlot66;
   private byte[] decoyKeySlot40;
   private byte[] derivedKeySlot49;
   private final int[] decTable1;
   private byte[] decoyKeySlot44;
   private byte[] secondaryKeyGuard;
   private final int[] decTable2;
   private boolean has14Rounds;
   private byte[] derivedKeySlot67;
   private byte[] decoyKeySlot20;
   private byte[] decoyKeySlot43;
   private byte[] decoyKeySlot10;
   private byte[] derivedKeySlot78;
   private byte[] decoyKeySlot27;
   private byte[] derivedKeySlot84;
   private byte[] decoyKeySlot03;
   private byte[] derivedKeySlot56;
   private byte[] derivedKeySlot28;
   private byte[] decoyKeySlot75;
   private byte[] derivedKeySlot93;
   private byte[] derivedKeySlot73;
   private byte[] decoyKeySlot32;
   private final byte[] roundConstants;
   private byte[] decoyKeySlot83;
   private byte[] derivedKeySlot50;
   private byte[] decoyKeySlot14;
   private byte[] derivedKeySlot17;
   private int[] gfAntilogTable;
   private byte[] decoyKeySlot63;
   private byte[] derivedKeySlot74;
   private final PipelineTimingGuard timingGuard;
   private final int[] keyTable4;
   private byte[] decoyKeySlot98;
   private byte[] decoyKeySlot06;
   private byte[] derivedKeySlot27;
   private byte[] decoyKeySlot97;
   private byte[] derivedKeySlot03;
   private byte[] derivedKeySlot61;
   private byte[] derivedKeySlot47;
   private final int[] keyTable2;
   private byte[] derivedKeySlot35;
   private byte[] decoyKeySlot23;
   private byte[] derivedKeySlot41;
   private byte[] decoyKeySlot25;
   private int roundKeyLimit;
   private byte[] derivedKeySlot46;
   private byte[] decoyKeySlot11;
   private byte[] decoyKeySlot57;
   public byte[] cbcRegister;
   private byte[] decoyKeySlot80;
   private byte[] decoyKeySlot39;
   private byte[] decoyKeySlot66;
   private byte[] decoyKeySlot92;
   private byte[] decoyKeySlot07;
   private byte[] derivedKeySlot21;
   private byte[] derivedKeySlot15;
   private byte[] decoyKeySlot95;
   private byte[] decoyKeySlot71;
   private byte[] derivedKeySlot86;
   private byte[] decoyKeySlot59;
   private byte[] derivedKeySlot63;
   private byte[] derivedKeySlot44;
   private byte[] decoyKeySlot52;
   private byte[] decoyKeySlot79;
   private byte[] decoyKeySlot49;
   private byte[] decoyKeySlot37;
   private byte[] derivedKeySlot37;
   private byte[] derivedKeySlot34;
   private byte[] derivedKeySlot95;
   private byte[] derivedKeySlot39;
   private byte[] decoyKeySlot26;
   private byte[] decoyKeySlot88;
   private byte[] decoyKeySlot54;
   private byte[] derivedKeySlot96;
   private byte[] decoyKeySlot91;
   private byte[] derivedKeySlot69;
   private byte[] derivedKeySlot22;
   private byte[] decoyKeySlot22;
   private final int[] keyTable3;
   private byte[] decoyKeySlot74;
   private byte[] derivedKeySlot02;
   private byte[] derivedKeySlot99;
   private byte[] decoyKeySlot50;
   private byte[] decoyKeySlot84;
   private byte[] derivedKeySlot64;
   private byte[] decoyKeySlot90;
   private byte[] decoyKeySlot31;
   private byte[] decoyKeySlot08;
   private byte[] decoyKeySlot46;
   private byte[] derivedKeySlot43;
   private byte[] decoyKeySlot58;
   private byte[] primaryKeyGuard;
   private byte[] decoyKeySlot04;
   private byte[] decoyKeySlot70;
   private byte[] decoyKeySlot18;
   private byte[] derivedKeySlot07;
   private byte[] decoyKeySlot47;
   private final int[] encTable1;
   private byte[] derivedKeySlot05;
   private byte[] decoyKeySlot65;
   private byte[] decoyKeySlot36;
   private byte[] decoyKeySlot81;
   private byte[] derivedKeySlot62;
   private byte[] derivedKeySlot79;
   private byte[] decoyKeySlot48;
   private byte[] decoyKeySlot69;
   private byte[] decoyKeySlot86;
   private byte[] derivedKeySlot09;
   private byte[] sessionKey;
   private byte[] derivedKeySlot04;
   private byte[] decoyKeySlot73;
   private byte[] derivedKeySlot20;
   private byte[] decoyKeySlot51;
   private byte[] derivedKeySlot72;
   private byte[] derivedKeySlot88;
   private byte[] derivedKeySlot70;
   private byte[] derivedKeySlot31;
   private byte[] derivedKeySlot38;
   private byte[] decoyKeySlot76;
   private final byte[] sBox;
   private byte[] decoyKeySlot60;
   private byte[] decoyKeySlot02;
   private final byte[] invSBox;
   private byte[] decoyKeySlot35;
   private byte[] decoyKeySlot72;
   private byte[] derivedKeySlot55;
   private byte[] derivedKeySlot12;
   private byte[] decoyKeySlot85;
   private byte[] decoyKeySlot16;
   private byte[] derivedKeySlot57;
   private byte[] derivedKeySlot52;
   private byte[] derivedKeySlot16;
   private byte[] derivedKeySlot75;
   private byte[] decoyKeySlot13;
   private byte[] derivedKeySlot13;
   private byte[] decoyKeySlot64;
   private byte[] decoyKeySlot68;
   private byte[] derivedKeySlot68;
   private byte[] decoyKeySlot29;
   private byte[] derivedKeySlot26;
   private final int[] decTable3;
   private byte[] derivedKeySlot48;
   private byte[] decoyKeySlot15;
   private byte[] derivedKeySlot87;
   private byte[] derivedKeySlot25;
   private byte[] derivedKeySlot01;
   private byte[] derivedKeySlot85;
   private byte[] derivedKeySlot65;
   private byte[] derivedKeySlot71;
   private final int[] keyTable1;
   private final int[] encTable3;
   private byte[] decoyKeySlot99;
   private byte[] decoyKeySlot67;
   private byte[] derivedKeySlot32;
   private byte[] derivedKeySlot14;
   private byte[] decoyKeySlot05;
   private byte[] derivedKeySlot10;
   private byte[] derivedKeySlot18;
   private byte[] derivedKeySlot08;
   private byte[] derivedKeySlot94;
   private byte[] decoyKeySlot01;
   private byte[] decoyKeySlot17;
   private byte[] derivedKeySlot42;
   private final int[] encTable2;
   private byte[] decoyKeySlot30;
   private byte[] decoyKeySlot56;
   private byte[] derivedKeySlot90;
   private byte[] derivedKeySlot82;
   private byte[] decoyKeySlot55;
   private byte[] decoyKeySlot53;
   private byte[] decoyKeySlot45;
   private byte[] decoyKeySlot09;
   private byte[] derivedKeySlot11;
   private final int[] encTable4;
   private byte[] decoyKeySlot62;
   private byte[] derivedKeySlot91;
   private byte[] derivedKeySlot29;
   private byte[] decoyKeySlot96;
   private byte[] decoyKeySlot77;
   private byte[] decoyKeySlot24;
   private byte[] decoyKeySlot28;
   private byte[] decoyKeySlot82;
   private byte[] derivedKeySlot40;
   private byte[] decoyKeySlot12;
   private byte[] derivedKeySlot80;
   private byte[] derivedKeySlot98;
   private byte[] decoyKeySlot19;
   private byte[] derivedKeySlot54;
   private byte[] decoyKeySlot78;
   private byte[] decoyKeySlot42;

   public int getBlockSize() {
      return 16;
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
      } catch (InvalidKeyException e) {
      }
   }

   public void setSessionKey(byte[] bytes) {
      PaddedAesCipher paddedAesCipher = null;
      byte b = 29;
      long l = -2500225118170331002L;
      l = 3626613575623059513L ^ (long)"\ueeff㘎趱궒鮰⠍瀑궨숶ﶰŮ\ue104䲟\ud849\ue31c焁\uef8d䠺㼴穈檚\ue35c覀ᒘ\u008c⨀賝त".hashCode();

      outerLoop:
      while (true) {
         if (b <= 51) {
            KeccakSha3Digest keccakSha3Digest = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes3 = new byte[]{
               108,
               103,
               121,
               -62,
               -33,
               82,
               96,
               86,
               -89,
               -70,
               62,
               -116,
               -77,
               -55,
               -11,
               107,
               90,
               -92,
               78,
               -13,
               -98,
               82,
               42,
               51,
               -57,
               -108,
               18,
               -95,
               -109,
               1,
               -55,
               -45
            };
            this.derivedKeySlot01 = keccakSha3Digest.digest(VendorAesHelper.encryptWithCipher(bytes, bytes3, paddedAesCipher));
            l = l + -7986808522213809061L ^ 6205067535158006013L;
         } else if (b >= 77) {
            if (77 >= b) {
               KeccakSha3Digest keccakSha3Digest2 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes7 = new byte[]{
                  -67,
                  11,
                  -107,
                  83,
                  -70,
                  -62,
                  106,
                  -83,
                  -27,
                  101,
                  -98,
                  -4,
                  31,
                  -124,
                  -19,
                  -3,
                  -124,
                  16,
                  -74,
                  14,
                  -36,
                  110,
                  48,
                  -7,
                  80,
                  11,
                  12,
                  -83,
                  96,
                  -35,
                  82,
                  -106
               };
               this.derivedKeySlot02 = keccakSha3Digest2.digest(VendorAesHelper.encryptWithCipher(bytes, bytes7, paddedAesCipher));
               l = (l ^ -2745959664548180294L) + -5545412008050478732L;
            } else if (b >= 90) {
               if (b <= 90) {
                  KeccakSha3Digest keccakSha3Digest3 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes11 = new byte[]{
                     -27,
                     72,
                     110,
                     -67,
                     5,
                     -113,
                     -80,
                     -116,
                     68,
                     91,
                     -67,
                     -78,
                     70,
                     -107,
                     101,
                     -15,
                     -121,
                     -23,
                     -125,
                     -52,
                     -67,
                     45,
                     48,
                     -70,
                     -25,
                     74,
                     72,
                     -119,
                     125,
                     -93,
                     34,
                     1
                  };
                  this.derivedKeySlot03 = keccakSha3Digest3.digest(VendorAesHelper.encryptWithCipher(bytes, bytes11, paddedAesCipher));
                  l = (l ^ 7021473993838055482L) - -2402005725846062511L;
                  continue;
               }

               if (b >= 97) {
                  if (97 >= b) {
                     KeccakSha3Digest keccakSha3Digest4 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes15 = new byte[]{
                        -118,
                        -24,
                        32,
                        90,
                        -97,
                        54,
                        91,
                        -83,
                        -43,
                        -97,
                        117,
                        -128,
                        -123,
                        9,
                        58,
                        -74,
                        4,
                        -127,
                        -105,
                        -48,
                        -39,
                        -75,
                        -33,
                        -8,
                        17,
                        99,
                        122,
                        -55,
                        2,
                        14,
                        66,
                        34
                     };
                     this.derivedKeySlot04 = keccakSha3Digest4.digest(VendorAesHelper.encryptWithCipher(bytes, bytes15, paddedAesCipher));
                     l = (l ^ 6552921527643743323L) - -2286723408696488676L;
                  } else if (100 <= b) {
                     if (100 >= b) {
                        KeccakSha3Digest keccakSha3Digest5 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes19 = new byte[]{
                           -109,
                           97,
                           104,
                           -113,
                           34,
                           78,
                           27,
                           3,
                           81,
                           -87,
                           -51,
                           48,
                           104,
                           -124,
                           -26,
                           43,
                           -2,
                           -109,
                           -21,
                           -33,
                           -83,
                           -78,
                           -98,
                           125,
                           81,
                           -49,
                           -121,
                           125,
                           -116,
                           41,
                           46,
                           13
                        };
                        this.derivedKeySlot05 = keccakSha3Digest5.digest(VendorAesHelper.encryptWithCipher(bytes, bytes19, paddedAesCipher));
                        l = (l ^ -8757115258214508351L) + -6250499930995572562L;
                        continue;
                     }

                     if (b >= 102) {
                        if (102 >= b) {
                           KeccakSha3Digest keccakSha3Digest6 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes23 = new byte[]{
                              -16,
                              22,
                              24,
                              81,
                              95,
                              -20,
                              30,
                              -39,
                              -33,
                              71,
                              -44,
                              64,
                              -118,
                              96,
                              46,
                              4,
                              115,
                              -57,
                              85,
                              66,
                              73,
                              93,
                              65,
                              -105,
                              4,
                              -107,
                              18,
                              -67,
                              74,
                              72,
                              88,
                              44
                           };
                           this.derivedKeySlot06 = keccakSha3Digest6.digest(VendorAesHelper.encryptWithCipher(bytes, bytes23, paddedAesCipher));
                           l = (l ^ -5331652082827399880L) + 6888348448540070442L;
                           continue;
                        }

                        KeccakSha3Digest keccakSha3Digest7 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes27 = new byte[]{
                           -126,
                           -34,
                           58,
                           -26,
                           -117,
                           -51,
                           -27,
                           -100,
                           21,
                           -93,
                           -77,
                           60,
                           26,
                           89,
                           -92,
                           -18,
                           -66,
                           44,
                           106,
                           117,
                           86,
                           -103,
                           32,
                           119,
                           45,
                           1,
                           7,
                           -6,
                           1,
                           -35,
                           -72,
                           -86
                        };
                        this.derivedKeySlot07 = keccakSha3Digest7.digest(VendorAesHelper.encryptWithCipher(bytes, bytes27, paddedAesCipher));
                        l = (l ^ -4097241790805408719L) - -7872380289747652382L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest8 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes31 = new byte[]{
                           55,
                           6,
                           -115,
                           94,
                           -106,
                           -11,
                           -126,
                           -113,
                           -61,
                           -7,
                           -47,
                           -81,
                           88,
                           -38,
                           85,
                           100,
                           11,
                           -110,
                           72,
                           77,
                           -62,
                           26,
                           121,
                           65,
                           -65,
                           74,
                           58,
                           -78,
                           -11,
                           -102,
                           -103,
                           33
                        };
                        this.derivedKeySlot08 = keccakSha3Digest8.digest(VendorAesHelper.encryptWithCipher(bytes, bytes31, paddedAesCipher));
                        l = l + -2641046760825027739L ^ 5761472304399380974L;
                     }
                  } else if (98 >= b) {
                     KeccakSha3Digest keccakSha3Digest9 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes35 = new byte[]{
                        -118,
                        -12,
                        -91,
                        -31,
                        5,
                        -41,
                        -65,
                        15,
                        68,
                        -38,
                        -33,
                        9,
                        -64,
                        -51,
                        -107,
                        -7,
                        -101,
                        -106,
                        125,
                        127,
                        93,
                        29,
                        -23,
                        -110,
                        -118,
                        91,
                        -44,
                        70,
                        104,
                        -23,
                        -95,
                        92
                     };
                     this.derivedKeySlot09 = keccakSha3Digest9.digest(VendorAesHelper.encryptWithCipher(bytes, bytes35, paddedAesCipher));
                     l = l - 5340209011747355886L ^ -4649082788342893999L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest10 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes39 = new byte[]{
                        78,
                        22,
                        94,
                        -25,
                        92,
                        -78,
                        -14,
                        -119,
                        97,
                        -34,
                        105,
                        -19,
                        32,
                        -84,
                        29,
                        -32,
                        78,
                        24,
                        -12,
                        104,
                        73,
                        107,
                        -116,
                        -76,
                        -47,
                        12,
                        -44,
                        -36,
                        9,
                        -66,
                        -41,
                        63
                     };
                     this.derivedKeySlot10 = keccakSha3Digest10.digest(VendorAesHelper.encryptWithCipher(bytes, bytes39, paddedAesCipher));
                     l = l - -9125040941654808501L ^ -2104240045964944441L;
                  }
               } else if (93 <= b) {
                  if (93 < b) {
                     if (95 <= b) {
                        if (b <= 95) {
                           KeccakSha3Digest keccakSha3Digest11 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes43 = new byte[]{
                              -119,
                              -122,
                              -21,
                              25,
                              -69,
                              82,
                              -84,
                              -126,
                              -84,
                              -56,
                              34,
                              108,
                              -55,
                              118,
                              120,
                              110,
                              -25,
                              124,
                              -9,
                              -66,
                              -31,
                              -31,
                              30,
                              9,
                              -59,
                              -39,
                              -14,
                              -43,
                              -43,
                              -119,
                              -63,
                              84
                           };
                           this.derivedKeySlot11 = keccakSha3Digest11.digest(VendorAesHelper.encryptWithCipher(bytes, bytes43, paddedAesCipher));
                           l = l + -7790022074543919974L ^ -135841003857522739L;
                        } else {
                           paddedAesCipher = new PaddedAesCipher();
                           l = l - 6931611821234190822L ^ -7902656901571837540L;
                        }
                     } else {
                        KeccakSha3Digest keccakSha3Digest12 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes47 = new byte[]{
                           -117,
                           22,
                           69,
                           73,
                           34,
                           -44,
                           -125,
                           -80,
                           -20,
                           48,
                           -65,
                           16,
                           -91,
                           -85,
                           -91,
                           13,
                           10,
                           -76,
                           102,
                           55,
                           -6,
                           -33,
                           -55,
                           58,
                           -121,
                           -104,
                           1,
                           96,
                           121,
                           94,
                           -40,
                           58
                        };
                        this.derivedKeySlot12 = keccakSha3Digest12.digest(VendorAesHelper.encryptWithCipher(bytes, bytes47, paddedAesCipher));
                        l = l + -8090492243191298022L ^ -5077272699678435729L;
                     }
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest13 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes51 = new byte[]{
                     -70,
                     41,
                     -69,
                     -12,
                     65,
                     -14,
                     68,
                     26,
                     74,
                     113,
                     -34,
                     -128,
                     42,
                     9,
                     -80,
                     -5,
                     -15,
                     -6,
                     -117,
                     103,
                     -87,
                     56,
                     114,
                     93,
                     -25,
                     24,
                     -65,
                     -74,
                     90,
                     -15,
                     124,
                     -118
                  };
                  this.derivedKeySlot13 = keccakSha3Digest13.digest(VendorAesHelper.encryptWithCipher(bytes, bytes51, paddedAesCipher));
                  l = l + 7909612650328397873L ^ -6825374715486408027L;
               } else {
                  if (91 >= b) {
                     return;
                  }

                  KeccakSha3Digest keccakSha3Digest14 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes55 = new byte[]{
                     7,
                     -30,
                     -58,
                     -81,
                     -28,
                     28,
                     5,
                     -50,
                     73,
                     61,
                     40,
                     24,
                     86,
                     -101,
                     78,
                     53,
                     64,
                     125,
                     114,
                     -93,
                     -76,
                     116,
                     26,
                     -116,
                     -77,
                     115,
                     -57,
                     -66,
                     76,
                     65,
                     -88,
                     22
                  };
                  this.derivedKeySlot14 = keccakSha3Digest14.digest(VendorAesHelper.encryptWithCipher(bytes, bytes55, paddedAesCipher));
                  l = l - -7568996521595307607L ^ -8131421792234908474L;
               }
            } else if (83 <= b) {
               if (83 >= b) {
                  KeccakSha3Digest keccakSha3Digest15 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes59 = new byte[]{
                     72,
                     119,
                     104,
                     21,
                     -29,
                     18,
                     -71,
                     -106,
                     -108,
                     115,
                     -53,
                     -13,
                     -75,
                     -42,
                     100,
                     122,
                     -39,
                     -51,
                     124,
                     2,
                     82,
                     95,
                     5,
                     110,
                     -128,
                     79,
                     47,
                     2,
                     -63,
                     90,
                     30,
                     -30
                  };
                  this.derivedKeySlot15 = keccakSha3Digest15.digest(VendorAesHelper.encryptWithCipher(bytes, bytes59, paddedAesCipher));
                  l = l + 7157818158814423592L ^ -4681834530166906765L;
               } else if (86 <= b) {
                  if (86 >= b) {
                     KeccakSha3Digest keccakSha3Digest16 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes63 = new byte[]{
                        33,
                        38,
                        74,
                        -83,
                        -24,
                        -15,
                        -31,
                        109,
                        -21,
                        63,
                        -98,
                        -89,
                        7,
                        -114,
                        -118,
                        68,
                        100,
                        70,
                        -97,
                        50,
                        -8,
                        -1,
                        2,
                        22,
                        35,
                        -88,
                        17,
                        -49,
                        -120,
                        122,
                        101,
                        106
                     };
                     this.derivedKeySlot16 = keccakSha3Digest16.digest(VendorAesHelper.encryptWithCipher(bytes, bytes63, paddedAesCipher));
                     l = l + 2257211593470826793L ^ 888268871900733697L;
                     continue;
                  }

                  if (b >= 88) {
                     if (b > 88) {
                        KeccakSha3Digest keccakSha3Digest17 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes67 = new byte[]{
                           -57,
                           68,
                           -19,
                           96,
                           -48,
                           9,
                           -72,
                           -109,
                           42,
                           112,
                           110,
                           123,
                           68,
                           -124,
                           -115,
                           -127,
                           65,
                           -70,
                           -71,
                           -53,
                           112,
                           107,
                           76,
                           -81,
                           -71,
                           41,
                           -48,
                           -4,
                           -117,
                           -46,
                           -25,
                           54
                        };
                        this.derivedKeySlot17 = keccakSha3Digest17.digest(VendorAesHelper.encryptWithCipher(bytes, bytes67, paddedAesCipher));
                        l = l - 3716191740456654689L ^ 1691869119346243290L;
                        continue;
                     }

                     KeccakSha3Digest keccakSha3Digest18 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes71 = new byte[]{
                        -123,
                        -26,
                        126,
                        91,
                        28,
                        -44,
                        -51,
                        102,
                        46,
                        -75,
                        -58,
                        -30,
                        113,
                        44,
                        54,
                        40,
                        -36,
                        71,
                        74,
                        -54,
                        -14,
                        -99,
                        -22,
                        -120,
                        86,
                        43,
                        80,
                        110,
                        48,
                        107,
                        107,
                        -104
                     };
                     this.derivedKeySlot18 = keccakSha3Digest18.digest(VendorAesHelper.encryptWithCipher(bytes, bytes71, paddedAesCipher));
                     l = l + 5081855605169498990L ^ 977478759016771143L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest19 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes75 = new byte[]{
                        -100,
                        91,
                        122,
                        104,
                        -91,
                        97,
                        107,
                        107,
                        6,
                        117,
                        -111,
                        -84,
                        -26,
                        14,
                        3,
                        -85,
                        -45,
                        -5,
                        76,
                        73,
                        73,
                        -88,
                        -62,
                        -40,
                        -44,
                        -75,
                        99,
                        -4,
                        104,
                        65,
                        36,
                        -84
                     };
                     this.derivedKeySlot19 = keccakSha3Digest19.digest(VendorAesHelper.encryptWithCipher(bytes, bytes75, paddedAesCipher));
                     l = l - -210273659386089655L ^ 6119088391165275754L;
                  }
               } else if (84 >= b) {
                  KeccakSha3Digest keccakSha3Digest20 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes79 = new byte[]{
                     54,
                     -71,
                     6,
                     57,
                     117,
                     -110,
                     39,
                     -20,
                     45,
                     -53,
                     -37,
                     -56,
                     -96,
                     -66,
                     126,
                     37,
                     121,
                     -10,
                     -24,
                     87,
                     64,
                     110,
                     -19,
                     117,
                     -107,
                     -58,
                     100,
                     -89,
                     120,
                     44,
                     -67,
                     -57
                  };
                  this.derivedKeySlot20 = keccakSha3Digest20.digest(VendorAesHelper.encryptWithCipher(bytes, bytes79, paddedAesCipher));
                  l = l - 4317124634977415996L ^ 2228576698002752175L;
               } else {
                  KeccakSha3Digest keccakSha3Digest21 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes83 = new byte[]{
                     -42,
                     -61,
                     47,
                     21,
                     -88,
                     122,
                     89,
                     58,
                     21,
                     -88,
                     -52,
                     -76,
                     -102,
                     -70,
                     -53,
                     -29,
                     -101,
                     -99,
                     39,
                     -57,
                     60,
                     -31,
                     58,
                     -70,
                     97,
                     -90,
                     -92,
                     7,
                     -119,
                     -59,
                     -44,
                     -65
                  };
                  this.derivedKeySlot21 = keccakSha3Digest21.digest(VendorAesHelper.encryptWithCipher(bytes, bytes83, paddedAesCipher));
                  l = (l ^ -4399336084058235318L) - -6910170973314364614L;
               }
            } else if (80 <= b) {
               if (b <= 80) {
                  KeccakSha3Digest keccakSha3Digest22 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes89 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
                     -62, 29, -98, -2, -88, 104, -96, 34, 68, 88, -16, 18, 25, 71, 82, 39, -24, 59, -51, 4, -32, -1, 59, 47, 94, 74, -99, 19, -20, 87, -29, 83
                  }, paddedAesCipher);
                  this.derivedKeySlot22 = keccakSha3Digest22.digest(bytes89);
                  l = (l ^ -698137689187377866L) - -3428299283975793880L;
                  continue;
               }

               if (81 >= b) {
                  KeccakSha3Digest keccakSha3Digest23 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes91 = new byte[]{
                     8,
                     5,
                     105,
                     52,
                     61,
                     82,
                     -87,
                     -2,
                     -100,
                     -27,
                     -34,
                     -13,
                     -81,
                     65,
                     -50,
                     65,
                     -67,
                     -68,
                     -108,
                     -70,
                     93,
                     21,
                     21,
                     -55,
                     -91,
                     -108,
                     -82,
                     -88,
                     -33,
                     19,
                     38,
                     8
                  };
                  this.derivedKeySlot23 = keccakSha3Digest23.digest(VendorAesHelper.encryptWithCipher(bytes, bytes91, paddedAesCipher));
                  l = (l ^ 3246647114490356321L) - -1309221295133033906L;
               } else {
                  KeccakSha3Digest keccakSha3Digest24 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes95 = new byte[]{
                     53,
                     71,
                     -12,
                     -116,
                     66,
                     -46,
                     -27,
                     -31,
                     -91,
                     -61,
                     80,
                     -77,
                     -44,
                     -58,
                     -108,
                     -47,
                     -34,
                     -102,
                     -71,
                     31,
                     -101,
                     -85,
                     49,
                     -39,
                     -43,
                     19,
                     9,
                     27,
                     -63,
                     81,
                     -66,
                     80
                  };
                  this.derivedKeySlot24 = keccakSha3Digest24.digest(VendorAesHelper.encryptWithCipher(bytes, bytes95, paddedAesCipher));
                  l = l - 8440343460162924481L ^ -8918842899715120271L;
               }
            } else if (78 >= b) {
               KeccakSha3Digest keccakSha3Digest25 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes99 = new byte[]{
                  17,
                  -68,
                  67,
                  -80,
                  112,
                  -43,
                  93,
                  -104,
                  -22,
                  -81,
                  -87,
                  12,
                  124,
                  18,
                  -61,
                  49,
                  -117,
                  -52,
                  119,
                  -14,
                  -122,
                  78,
                  -67,
                  -45,
                  114,
                  -49,
                  -70,
                  -125,
                  -35,
                  -94,
                  118,
                  101
               };
               this.derivedKeySlot25 = keccakSha3Digest25.digest(VendorAesHelper.encryptWithCipher(bytes, bytes99, paddedAesCipher));
               l = l + -3665092386328647754L ^ 5220188690591410727L;
            } else {
               KeccakSha3Digest keccakSha3Digest26 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes103 = new byte[]{
                  124,
                  -34,
                  23,
                  -52,
                  48,
                  5,
                  4,
                  -72,
                  86,
                  101,
                  64,
                  100,
                  66,
                  -110,
                  -60,
                  96,
                  94,
                  72,
                  19,
                  -35,
                  -95,
                  -83,
                  -42,
                  51,
                  15,
                  -122,
                  -128,
                  64,
                  58,
                  67,
                  79,
                  -67
               };
               this.derivedKeySlot26 = keccakSha3Digest26.digest(VendorAesHelper.encryptWithCipher(bytes, bytes103, paddedAesCipher));
               l = l - -3445009463937812681L ^ 7527753957078879455L;
            }
         } else if (64 <= b) {
            if (64 >= b) {
               KeccakSha3Digest keccakSha3Digest27 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes107 = new byte[]{
                  -73,
                  -95,
                  -62,
                  91,
                  -61,
                  52,
                  23,
                  91,
                  63,
                  1,
                  56,
                  124,
                  -91,
                  67,
                  62,
                  -101,
                  -30,
                  119,
                  -42,
                  -96,
                  63,
                  -95,
                  -19,
                  126,
                  52,
                  -65,
                  20,
                  3,
                  39,
                  -109,
                  95,
                  -6
               };
               this.derivedKeySlot27 = keccakSha3Digest27.digest(VendorAesHelper.encryptWithCipher(bytes, bytes107, paddedAesCipher));
               l = (l ^ 6924438730008931243L) - -4200805481876150290L;
               continue;
            }

            if (b >= 70) {
               if (70 >= b) {
                  KeccakSha3Digest keccakSha3Digest28 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes111 = new byte[]{
                     -74,
                     -89,
                     108,
                     -8,
                     -25,
                     -75,
                     -11,
                     83,
                     10,
                     -6,
                     44,
                     24,
                     -91,
                     -38,
                     44,
                     9,
                     34,
                     105,
                     -33,
                     -117,
                     -68,
                     32,
                     -74,
                     93,
                     -34,
                     -66,
                     105,
                     86,
                     99,
                     126,
                     115,
                     2
                  };
                  this.derivedKeySlot28 = keccakSha3Digest28.digest(VendorAesHelper.encryptWithCipher(bytes, bytes111, paddedAesCipher));
                  l = l - 1060393046542621305L ^ 7732686008379165921L;
               } else if (73 <= b) {
                  if (73 >= b) {
                     KeccakSha3Digest keccakSha3Digest29 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes115 = new byte[]{
                        -128,
                        -111,
                        -73,
                        102,
                        18,
                        -41,
                        -81,
                        48,
                        99,
                        -33,
                        -2,
                        93,
                        -28,
                        35,
                        -110,
                        -127,
                        61,
                        -55,
                        9,
                        51,
                        -18,
                        45,
                        46,
                        -20,
                        54,
                        56,
                        -108,
                        117,
                        -43,
                        98,
                        -86,
                        10
                     };
                     this.derivedKeySlot29 = keccakSha3Digest29.digest(VendorAesHelper.encryptWithCipher(bytes, bytes115, paddedAesCipher));
                     l = l + -9195536327832983045L ^ -8257394917794439869L;
                     continue;
                  }

                  if (b < 75) {
                     KeccakSha3Digest keccakSha3Digest30 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes119 = new byte[]{
                        -121,
                        77,
                        25,
                        -23,
                        -92,
                        10,
                        97,
                        -81,
                        72,
                        103,
                        81,
                        6,
                        107,
                        32,
                        -10,
                        -68,
                        61,
                        26,
                        -36,
                        -45,
                        99,
                        -89,
                        -26,
                        -123,
                        10,
                        32,
                        -94,
                        118,
                        98,
                        -32,
                        79,
                        40
                     };
                     this.derivedKeySlot30 = keccakSha3Digest30.digest(VendorAesHelper.encryptWithCipher(bytes, bytes119, paddedAesCipher));
                     l = l + 3775739497442842929L ^ 1184761494559588085L;
                     continue;
                  }

                  if (b <= 75) {
                     KeccakSha3Digest keccakSha3Digest31 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes123 = new byte[]{
                        66,
                        58,
                        99,
                        22,
                        -61,
                        49,
                        -96,
                        -5,
                        -84,
                        30,
                        103,
                        23,
                        47,
                        -38,
                        -61,
                        -84,
                        75,
                        -127,
                        44,
                        -121,
                        -48,
                        49,
                        -50,
                        -21,
                        -82,
                        68,
                        56,
                        8,
                        76,
                        23,
                        92,
                        100
                     };
                     this.derivedKeySlot31 = keccakSha3Digest31.digest(VendorAesHelper.encryptWithCipher(bytes, bytes123, paddedAesCipher));
                     l = (l ^ -1626176541652360345L) - -6263344247689391438L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest32 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes127 = new byte[]{
                        -28,
                        96,
                        -124,
                        -124,
                        126,
                        81,
                        81,
                        64,
                        97,
                        124,
                        10,
                        -53,
                        -82,
                        -16,
                        -35,
                        -77,
                        -43,
                        5,
                        -36,
                        105,
                        -21,
                        -109,
                        -22,
                        -102,
                        -17,
                        75,
                        76,
                        105,
                        -109,
                        -95,
                        71,
                        17
                     };
                     this.derivedKeySlot32 = keccakSha3Digest32.digest(VendorAesHelper.encryptWithCipher(bytes, bytes127, paddedAesCipher));
                     l = l - -4393533774039123569L ^ 2673816337155091017L;
                  }
               } else {
                  if (b <= 71) {
                     KeccakSha3Digest keccakSha3Digest33 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes131 = new byte[]{
                        6,
                        -61,
                        122,
                        -60,
                        -82,
                        -62,
                        -27,
                        -41,
                        57,
                        108,
                        112,
                        62,
                        -117,
                        104,
                        0,
                        -68,
                        -52,
                        -27,
                        114,
                        -83,
                        -6,
                        -18,
                        -97,
                        15,
                        -122,
                        -122,
                        -104,
                        -109,
                        127,
                        -91,
                        71,
                        -120
                     };
                     this.derivedKeySlot33 = keccakSha3Digest33.digest(VendorAesHelper.encryptWithCipher(bytes, bytes131, paddedAesCipher));
                     l = (l ^ -6306290773539064341L) + 2038572427749348972L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest34 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes135 = new byte[]{
                     21,
                     -21,
                     -36,
                     -91,
                     -50,
                     -73,
                     -39,
                     -53,
                     -52,
                     101,
                     -101,
                     -56,
                     50,
                     -12,
                     11,
                     65,
                     -125,
                     -126,
                     44,
                     91,
                     64,
                     82,
                     -17,
                     122,
                     39,
                     95,
                     112,
                     98,
                     64,
                     50,
                     -81,
                     -36
                  };
                  this.derivedKeySlot34 = keccakSha3Digest34.digest(VendorAesHelper.encryptWithCipher(bytes, bytes135, paddedAesCipher));
                  l = (l ^ 3356977145292980045L) - 2940736923964831836L;
               }
            } else if (67 <= b) {
               if (67 >= b) {
                  KeccakSha3Digest keccakSha3Digest35 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes139 = new byte[]{
                     -26,
                     51,
                     48,
                     -90,
                     112,
                     -18,
                     109,
                     -119,
                     72,
                     12,
                     63,
                     63,
                     -28,
                     27,
                     101,
                     23,
                     -80,
                     44,
                     -38,
                     57,
                     101,
                     80,
                     -98,
                     -71,
                     -10,
                     -34,
                     1,
                     -11,
                     -11,
                     90,
                     -123,
                     -34
                  };
                  this.derivedKeySlot35 = keccakSha3Digest35.digest(VendorAesHelper.encryptWithCipher(bytes, bytes139, paddedAesCipher));
                  l = l + 4887860722794432978L ^ -8983450809039931305L;
                  continue;
               }

               if (b <= 68) {
                  KeccakSha3Digest keccakSha3Digest36 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes143 = new byte[]{
                     111,
                     -9,
                     42,
                     109,
                     -27,
                     -13,
                     -105,
                     30,
                     -102,
                     -84,
                     -80,
                     66,
                     61,
                     86,
                     -91,
                     -114,
                     108,
                     -43,
                     -14,
                     6,
                     88,
                     41,
                     68,
                     24,
                     108,
                     43,
                     91,
                     -58,
                     -5,
                     26,
                     82,
                     84
                  };
                  this.derivedKeySlot36 = keccakSha3Digest36.digest(VendorAesHelper.encryptWithCipher(bytes, bytes143, paddedAesCipher));
                  l = l + 1061921537525703561L ^ 490391148695800033L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest37 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes149 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
                  61, 16, -19, -36, -102, 10, 7, 22, -21, 67, -5, 47, 126, 77, 53, -33, -95, 121, 92, -97, 59, -70, 1, -17, 2, 22, 113, -82, -88, 40, 42, 49
               }, paddedAesCipher);
               this.derivedKeySlot37 = keccakSha3Digest37.digest(bytes149);
               l = (l ^ 5425186611239573250L) + -2758272730267365956L;
            } else if (65 >= b) {
               KeccakSha3Digest keccakSha3Digest38 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes151 = new byte[]{
                  49,
                  -61,
                  31,
                  -21,
                  43,
                  -19,
                  -127,
                  78,
                  -42,
                  -75,
                  49,
                  95,
                  -121,
                  -66,
                  -61,
                  -66,
                  51,
                  114,
                  -40,
                  14,
                  -44,
                  15,
                  10,
                  -28,
                  120,
                  33,
                  -8,
                  -88,
                  37,
                  71,
                  -52,
                  24
               };
               this.derivedKeySlot38 = keccakSha3Digest38.digest(VendorAesHelper.encryptWithCipher(bytes, bytes151, paddedAesCipher));
               l = l + 5013370769007385414L ^ -8396895996954435266L;
            } else {
               KeccakSha3Digest keccakSha3Digest39 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes155 = new byte[]{
                  -94,
                  -118,
                  -60,
                  8,
                  -96,
                  33,
                  76,
                  108,
                  -123,
                  39,
                  14,
                  -19,
                  -13,
                  -14,
                  -66,
                  35,
                  -53,
                  -41,
                  -33,
                  114,
                  -13,
                  91,
                  -118,
                  114,
                  -114,
                  -86,
                  -122,
                  44,
                  33,
                  -72,
                  85,
                  -119
               };
               this.derivedKeySlot39 = keccakSha3Digest39.digest(VendorAesHelper.encryptWithCipher(bytes, bytes155, paddedAesCipher));
               l = (l ^ -7098304580835446774L) + -8699830456710923648L;
            }
         } else if (b >= 57) {
            if (b <= 57) {
               KeccakSha3Digest keccakSha3Digest40 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes161 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
                  -17, 38, 44, 79, 76, 92, -28, -10, 50, 99, 27, -2, 83, -48, 93, -7, 41, -15, -126, 106, 3, 15, 38, -115, 103, 14, 28, 84, 23, 40, 75, 31
               }, paddedAesCipher);
               this.derivedKeySlot40 = keccakSha3Digest40.digest(bytes161);
               l = l + -5944199328493159734L ^ -7308594687829920255L;
            } else {
               if (60 > b) {
                  if (58 >= b) {
                     KeccakSha3Digest keccakSha3Digest41 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes163 = new byte[]{
                        80,
                        63,
                        -36,
                        1,
                        -96,
                        -111,
                        65,
                        88,
                        -77,
                        -4,
                        -48,
                        -111,
                        28,
                        -38,
                        13,
                        -101,
                        105,
                        22,
                        -22,
                        2,
                        -15,
                        -91,
                        -65,
                        -28,
                        -66,
                        60,
                        -106,
                        20,
                        -124,
                        121,
                        -66,
                        71
                     };
                     this.derivedKeySlot41 = keccakSha3Digest41.digest(VendorAesHelper.encryptWithCipher(bytes, bytes163, paddedAesCipher));
                     l = l - -112662048748643870L ^ 8436080717178055718L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest42 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes167 = new byte[]{
                        56,
                        -37,
                        25,
                        -80,
                        83,
                        118,
                        27,
                        121,
                        -51,
                        38,
                        10,
                        -24,
                        63,
                        -91,
                        -118,
                        43,
                        -9,
                        -45,
                        -77,
                        -120,
                        -118,
                        68,
                        106,
                        49,
                        118,
                        -36,
                        5,
                        55,
                        -49,
                        -21,
                        -35,
                        -19
                     };
                     this.derivedKeySlot42 = keccakSha3Digest42.digest(VendorAesHelper.encryptWithCipher(bytes, bytes167, paddedAesCipher));
                     l = (l ^ 7883851301915956010L) - 507822881064527412L;
                  }
                  continue;
               }

               if (b <= 60) {
                  KeccakSha3Digest keccakSha3Digest43 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes171 = new byte[]{
                     -50,
                     -33,
                     -103,
                     30,
                     79,
                     -86,
                     67,
                     -127,
                     -35,
                     -27,
                     -92,
                     67,
                     96,
                     -90,
                     -62,
                     68,
                     -1,
                     112,
                     -18,
                     115,
                     104,
                     -85,
                     -108,
                     1,
                     18,
                     -37,
                     -97,
                     40,
                     58,
                     85,
                     96,
                     119
                  };
                  this.primaryKeyGuard = keccakSha3Digest43.digest(VendorAesHelper.encryptWithCipher(bytes, bytes171, paddedAesCipher));
                  l = l - -8071088480426153415L ^ -688896644474275827L;
               } else if (b >= 62) {
                  if (62 < b) {
                     KeccakSha3Digest keccakSha3Digest44 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes175 = new byte[]{
                        -72,
                        -44,
                        84,
                        -19,
                        -13,
                        70,
                        -8,
                        -94,
                        -42,
                        124,
                        45,
                        -106,
                        119,
                        75,
                        1,
                        -46,
                        -110,
                        -98,
                        29,
                        86,
                        30,
                        11,
                        -4,
                        -118,
                        58,
                        -50,
                        -41,
                        86,
                        -7,
                        -22,
                        -7,
                        -33
                     };
                     this.derivedKeySlot43 = keccakSha3Digest44.digest(VendorAesHelper.encryptWithCipher(bytes, bytes175, paddedAesCipher));
                     l = (l ^ -7468426212648463499L) - 2927053185214664568L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest45 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes179 = new byte[]{
                     59,
                     -119,
                     -105,
                     -75,
                     -120,
                     99,
                     -99,
                     43,
                     98,
                     107,
                     65,
                     104,
                     52,
                     -84,
                     -28,
                     -51,
                     -26,
                     43,
                     94,
                     -102,
                     37,
                     86,
                     -33,
                     102,
                     -65,
                     -89,
                     -93,
                     27,
                     -28,
                     -80,
                     -127,
                     -81
                  };
                  this.derivedKeySlot44 = keccakSha3Digest45.digest(VendorAesHelper.encryptWithCipher(bytes, bytes179, paddedAesCipher));
                  l = (l ^ -4832112370838259913L) - -9203549929692667552L;
               } else {
                  KeccakSha3Digest keccakSha3Digest46 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes183 = new byte[]{
                     -118,
                     86,
                     -89,
                     -70,
                     -96,
                     -52,
                     118,
                     11,
                     57,
                     -76,
                     59,
                     -42,
                     -42,
                     2,
                     -97,
                     26,
                     51,
                     -19,
                     15,
                     125,
                     95,
                     -51,
                     65,
                     15,
                     -67,
                     70,
                     25,
                     -25,
                     -2,
                     86,
                     -108,
                     43
                  };
                  this.derivedKeySlot45 = keccakSha3Digest46.digest(VendorAesHelper.encryptWithCipher(bytes, bytes183, paddedAesCipher));
                  l = l - -8361208517298855636L ^ -7421162591540896652L;
               }
            }
         } else {
            if (54 <= b) {
               if (b <= 54) {
                  this.sessionKey = bytes;
                  l = (l ^ -3623392602112547842L) + -3379154158116606284L;
               } else if (b <= 55) {
                  KeccakSha3Digest keccakSha3Digest47 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes187 = new byte[]{
                     -110,
                     83,
                     7,
                     47,
                     125,
                     101,
                     125,
                     104,
                     10,
                     -6,
                     -17,
                     100,
                     112,
                     3,
                     -51,
                     -85,
                     -23,
                     -117,
                     -61,
                     61,
                     -3,
                     126,
                     -63,
                     19,
                     75,
                     -120,
                     89,
                     90,
                     88,
                     -41,
                     -12,
                     17
                  };
                  this.derivedKeySlot46 = keccakSha3Digest47.digest(VendorAesHelper.encryptWithCipher(bytes, bytes187, paddedAesCipher));
                  l = l - 3838204844390659610L ^ -5931204305012253360L;
               } else {
                  KeccakSha3Digest keccakSha3Digest48 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes191 = new byte[]{
                     112,
                     93,
                     -72,
                     -4,
                     124,
                     67,
                     62,
                     -50,
                     -107,
                     47,
                     -13,
                     9,
                     -112,
                     101,
                     -5,
                     -29,
                     118,
                     102,
                     44,
                     82,
                     -22,
                     -24,
                     -13,
                     15,
                     -40,
                     -58,
                     -29,
                     -24,
                     -112,
                     -76,
                     -102,
                     29
                  };
                  this.derivedKeySlot47 = keccakSha3Digest48.digest(VendorAesHelper.encryptWithCipher(bytes, bytes191, paddedAesCipher));
                  l = (l ^ 907483494392843748L) + -3819352193754437341L;
               }
               continue;
            }

            if (52 < b) {
               KeccakSha3Digest keccakSha3Digest49 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes195 = new byte[]{
                  -99,
                  -38,
                  3,
                  7,
                  73,
                  -51,
                  97,
                  41,
                  124,
                  116,
                  78,
                  -12,
                  102,
                  -73,
                  111,
                  79,
                  108,
                  -8,
                  -52,
                  -35,
                  38,
                  122,
                  -4,
                  122,
                  -91,
                  -60,
                  107,
                  -82,
                  18,
                  46,
                  -24,
                  120
               };
               this.derivedKeySlot48 = keccakSha3Digest49.digest(VendorAesHelper.encryptWithCipher(bytes, bytes195, paddedAesCipher));
               l = (l ^ -8132450973123948853L) - -9149119855993534657L;
               continue;
            }

            KeccakSha3Digest keccakSha3Digest50 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes199 = new byte[]{
               26,
               66,
               41,
               -3,
               -41,
               -116,
               -50,
               -61,
               126,
               -61,
               -57,
               -126,
               -115,
               -53,
               13,
               -101,
               118,
               73,
               6,
               -84,
               120,
               66,
               -101,
               -106,
               21,
               60,
               6,
               102,
               94,
               -48,
               -43,
               -51
            };
            this.derivedKeySlot49 = keccakSha3Digest50.digest(VendorAesHelper.encryptWithCipher(bytes, bytes199, paddedAesCipher));
            l = l - -6469223552283804313L ^ 7080053335846011366L;
         }

         while (true) {
            while (25 > b) {
               if (12 <= b) {
                  if (12 >= b) {
                     KeccakSha3Digest keccakSha3Digest51 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes203 = new byte[]{
                        69,
                        126,
                        -58,
                        -71,
                        -88,
                        -104,
                        91,
                        80,
                        117,
                        37,
                        111,
                        113,
                        -7,
                        -80,
                        92,
                        -37,
                        106,
                        46,
                        -4,
                        11,
                        -105,
                        -68,
                        -106,
                        -23,
                        -124,
                        74,
                        -106,
                        123,
                        -8,
                        -49,
                        30,
                        -50
                     };
                     this.derivedKeySlot50 = keccakSha3Digest51.digest(VendorAesHelper.encryptWithCipher(bytes, bytes203, paddedAesCipher));
                     l = (l ^ 1951044457746543321L) + -3756913094423185584L;
                     continue outerLoop;
                  }

                  if (b >= 18) {
                     if (b <= 18) {
                        KeccakSha3Digest keccakSha3Digest52 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes207 = new byte[]{
                           -28,
                           -34,
                           44,
                           97,
                           60,
                           72,
                           6,
                           47,
                           0,
                           -97,
                           -8,
                           -75,
                           -51,
                           19,
                           121,
                           -15,
                           6,
                           -121,
                           -27,
                           89,
                           -9,
                           -117,
                           99,
                           3,
                           34,
                           -87,
                           19,
                           16,
                           -20,
                           70,
                           8,
                           -46
                        };
                        this.derivedKeySlot51 = keccakSha3Digest52.digest(VendorAesHelper.encryptWithCipher(bytes, bytes207, paddedAesCipher));
                        l = l - -4317592112032917601L ^ 4972708144041176185L;
                     } else {
                        if (b >= 21) {
                           if (b <= 21) {
                              KeccakSha3Digest keccakSha3Digest53 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                              byte[] bytes211 = new byte[]{
                                 -83,
                                 -57,
                                 108,
                                 -21,
                                 106,
                                 9,
                                 -86,
                                 120,
                                 -56,
                                 82,
                                 -11,
                                 78,
                                 30,
                                 72,
                                 43,
                                 65,
                                 -19,
                                 -19,
                                 62,
                                 111,
                                 106,
                                 94,
                                 71,
                                 118,
                                 120,
                                 -95,
                                 73,
                                 -127,
                                 -120,
                                 90,
                                 53,
                                 29
                              };
                              this.derivedKeySlot52 = keccakSha3Digest53.digest(VendorAesHelper.encryptWithCipher(bytes, bytes211, paddedAesCipher));
                              l = (l ^ -9119445374422243181L) + -7944628711453899325L;
                           } else if (b >= 23) {
                              if (23 >= b) {
                                 KeccakSha3Digest keccakSha3Digest54 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                                 byte[] bytes215 = new byte[]{
                                    24,
                                    72,
                                    -55,
                                    113,
                                    -56,
                                    125,
                                    -81,
                                    74,
                                    -74,
                                    -124,
                                    -30,
                                    70,
                                    -20,
                                    31,
                                    -8,
                                    72,
                                    -43,
                                    114,
                                    -20,
                                    124,
                                    66,
                                    4,
                                    -3,
                                    94,
                                    -79,
                                    67,
                                    -51,
                                    -7,
                                    3,
                                    99,
                                    -80,
                                    -66
                                 };
                                 this.derivedKeySlot53 = keccakSha3Digest54.digest(VendorAesHelper.encryptWithCipher(bytes, bytes215, paddedAesCipher));
                                 l = l - 7893603310403242132L ^ -6814244265157710210L;
                              } else {
                                 KeccakSha3Digest keccakSha3Digest55 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                                 byte[] bytes219 = new byte[]{
                                    -77,
                                    57,
                                    100,
                                    51,
                                    -102,
                                    3,
                                    -2,
                                    -112,
                                    -113,
                                    35,
                                    -73,
                                    77,
                                    -21,
                                    -77,
                                    63,
                                    -51,
                                    -5,
                                    78,
                                    113,
                                    -98,
                                    78,
                                    -72,
                                    -104,
                                    -57,
                                    -124,
                                    44,
                                    52,
                                    -40,
                                    36,
                                    116,
                                    -24,
                                    78
                                 };
                                 this.derivedKeySlot54 = keccakSha3Digest55.digest(VendorAesHelper.encryptWithCipher(bytes, bytes219, paddedAesCipher));
                                 l = l + -1514920035828031726L ^ -8172626782574005717L;
                              }
                           } else {
                              KeccakSha3Digest keccakSha3Digest56 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                              byte[] bytes223 = new byte[]{
                                 -50,
                                 -36,
                                 -113,
                                 -25,
                                 -62,
                                 24,
                                 123,
                                 -61,
                                 -66,
                                 -30,
                                 122,
                                 -24,
                                 51,
                                 34,
                                 -8,
                                 89,
                                 2,
                                 62,
                                 -121,
                                 124,
                                 -73,
                                 -84,
                                 86,
                                 43,
                                 -124,
                                 -71,
                                 -9,
                                 -8,
                                 127,
                                 -117,
                                 -103,
                                 -13
                              };
                              this.derivedKeySlot55 = keccakSha3Digest56.digest(VendorAesHelper.encryptWithCipher(bytes, bytes223, paddedAesCipher));
                              l = (l ^ 2506967418801644437L) - -1168220114964355845L;
                           }
                           continue outerLoop;
                        }

                        if (19 < b) {
                           KeccakSha3Digest keccakSha3Digest57 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes227 = new byte[]{
                              87,
                              -79,
                              48,
                              99,
                              89,
                              1,
                              53,
                              -21,
                              -85,
                              120,
                              49,
                              -114,
                              -32,
                              17,
                              19,
                              -19,
                              21,
                              -118,
                              -128,
                              -16,
                              -70,
                              -50,
                              -8,
                              -53,
                              127,
                              -77,
                              -31,
                              -32,
                              115,
                              113,
                              -11,
                              -1
                           };
                           this.derivedKeySlot56 = keccakSha3Digest57.digest(VendorAesHelper.encryptWithCipher(bytes, bytes227, paddedAesCipher));
                           l = (l ^ 6373536072842542146L) - 1850684165142419243L;
                           continue outerLoop;
                        }

                        KeccakSha3Digest keccakSha3Digest58 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes231 = new byte[]{
                           -108,
                           -87,
                           76,
                           117,
                           -61,
                           -61,
                           -70,
                           -116,
                           20,
                           30,
                           -111,
                           -32,
                           116,
                           -109,
                           -106,
                           92,
                           -28,
                           5,
                           -46,
                           -108,
                           -90,
                           -117,
                           107,
                           -32,
                           84,
                           -62,
                           55,
                           3,
                           -54,
                           -121,
                           -21,
                           5
                        };
                        this.derivedKeySlot57 = keccakSha3Digest58.digest(VendorAesHelper.encryptWithCipher(bytes, bytes231, paddedAesCipher));
                        l = l - 5748196617152749033L ^ -8659193227913504156L;
                     }
                  } else if (b >= 15) {
                     if (b <= 15) {
                        KeccakSha3Digest keccakSha3Digest59 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes235 = new byte[]{
                           26,
                           99,
                           79,
                           -96,
                           88,
                           5,
                           -38,
                           40,
                           124,
                           -45,
                           123,
                           94,
                           -99,
                           -65,
                           56,
                           -9,
                           -87,
                           62,
                           -95,
                           11,
                           -6,
                           125,
                           46,
                           15,
                           51,
                           -84,
                           -1,
                           -128,
                           18,
                           51,
                           -21,
                           -23
                        };
                        this.derivedKeySlot58 = keccakSha3Digest59.digest(VendorAesHelper.encryptWithCipher(bytes, bytes235, paddedAesCipher));
                        l = (l ^ 7950629060670796438L) - -7421530979700637415L;
                        continue outerLoop;
                     }

                     if (16 >= b) {
                        KeccakSha3Digest keccakSha3Digest60 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes239 = new byte[]{
                           -88,
                           -32,
                           1,
                           76,
                           -1,
                           12,
                           47,
                           90,
                           123,
                           -128,
                           -82,
                           -52,
                           2,
                           5,
                           -66,
                           24,
                           101,
                           -80,
                           -24,
                           -39,
                           -35,
                           -48,
                           -126,
                           -122,
                           18,
                           94,
                           -17,
                           110,
                           -23,
                           7,
                           29,
                           85
                        };
                        this.derivedKeySlot59 = keccakSha3Digest60.digest(VendorAesHelper.encryptWithCipher(bytes, bytes239, paddedAesCipher));
                        l = (l ^ 4884134586808229972L) + 2017559828709211469L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest61 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes243 = new byte[]{
                        -25,
                        28,
                        7,
                        47,
                        106,
                        14,
                        -111,
                        90,
                        -94,
                        -87,
                        112,
                        -105,
                        40,
                        -22,
                        -74,
                        22,
                        -21,
                        75,
                        107,
                        -92,
                        -126,
                        57,
                        34,
                        105,
                        75,
                        59,
                        -28,
                        -50,
                        -40,
                        -62,
                        -6,
                        11
                     };
                     this.derivedKeySlot60 = keccakSha3Digest61.digest(VendorAesHelper.encryptWithCipher(bytes, bytes243, paddedAesCipher));
                     l = (l ^ -8211057996138906365L) - -3009044707301766668L;
                  } else {
                     if (13 < b) {
                        KeccakSha3Digest keccakSha3Digest62 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes247 = new byte[]{
                           -43,
                           -16,
                           28,
                           95,
                           -69,
                           9,
                           -127,
                           97,
                           108,
                           -15,
                           -86,
                           55,
                           36,
                           88,
                           -98,
                           -77,
                           -25,
                           0,
                           -7,
                           118,
                           99,
                           47,
                           32,
                           81,
                           -47,
                           69,
                           -121,
                           -54,
                           -67,
                           -102,
                           -107,
                           53
                        };
                        this.derivedKeySlot61 = keccakSha3Digest62.digest(VendorAesHelper.encryptWithCipher(bytes, bytes247, paddedAesCipher));
                        l = l + -2917302708687950147L ^ 852058406871163207L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest63 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes251 = new byte[]{
                        -102,
                        -111,
                        -31,
                        103,
                        115,
                        112,
                        19,
                        -7,
                        -1,
                        39,
                        99,
                        -97,
                        -24,
                        -19,
                        127,
                        102,
                        121,
                        -111,
                        -55,
                        -82,
                        29,
                        39,
                        -16,
                        25,
                        112,
                        14,
                        113,
                        87,
                        -104,
                        -113,
                        -11,
                        -75
                     };
                     this.derivedKeySlot62 = keccakSha3Digest63.digest(VendorAesHelper.encryptWithCipher(bytes, bytes251, paddedAesCipher));
                     l = (l ^ 3532436445745350838L) + 4197495530241590245L;
                  }
               } else if (5 <= b) {
                  if (5 >= b) {
                     KeccakSha3Digest keccakSha3Digest64 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes255 = new byte[]{
                        71,
                        13,
                        27,
                        -39,
                        -8,
                        -31,
                        56,
                        33,
                        -78,
                        13,
                        -25,
                        -85,
                        -51,
                        -96,
                        28,
                        -42,
                        -103,
                        -55,
                        8,
                        89,
                        -58,
                        -119,
                        117,
                        -89,
                        78,
                        14,
                        -69,
                        -34,
                        -111,
                        -104,
                        -48,
                        -95
                     };
                     this.derivedKeySlot63 = keccakSha3Digest64.digest(VendorAesHelper.encryptWithCipher(bytes, bytes255, paddedAesCipher));
                     l = l + -1940595541883344492L ^ 8294023102936052606L;
                  } else if (b >= 8) {
                     if (b <= 8) {
                        KeccakSha3Digest keccakSha3Digest65 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes259 = new byte[]{
                           -105,
                           -52,
                           32,
                           -22,
                           21,
                           -123,
                           -113,
                           1,
                           -43,
                           33,
                           29,
                           -13,
                           114,
                           -35,
                           -102,
                           -1,
                           -65,
                           19,
                           86,
                           -10,
                           -112,
                           58,
                           4,
                           -103,
                           -2,
                           -2,
                           75,
                           111,
                           35,
                           21,
                           -26,
                           -40
                        };
                        this.derivedKeySlot64 = keccakSha3Digest65.digest(VendorAesHelper.encryptWithCipher(bytes, bytes259, paddedAesCipher));
                        l = (l ^ -6941664501822710767L) + 6997120058346087385L;
                        continue outerLoop;
                     }

                     if (10 <= b) {
                        if (10 >= b) {
                           KeccakSha3Digest keccakSha3Digest66 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes263 = new byte[]{
                              122,
                              -31,
                              -120,
                              -77,
                              71,
                              -12,
                              -50,
                              -19,
                              -108,
                              52,
                              -96,
                              0,
                              124,
                              68,
                              99,
                              64,
                              -2,
                              24,
                              -104,
                              3,
                              30,
                              49,
                              -47,
                              -7,
                              -114,
                              52,
                              -75,
                              -36,
                              40,
                              18,
                              -102,
                              16
                           };
                           this.derivedKeySlot65 = keccakSha3Digest66.digest(VendorAesHelper.encryptWithCipher(bytes, bytes263, paddedAesCipher));
                           l = (l ^ -5920375296776720045L) - -3176279679663970329L;
                        } else {
                           KeccakSha3Digest keccakSha3Digest67 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes267 = new byte[]{
                              83,
                              -112,
                              127,
                              -59,
                              -66,
                              -59,
                              -25,
                              1,
                              107,
                              38,
                              25,
                              -124,
                              69,
                              -21,
                              -69,
                              85,
                              -14,
                              -5,
                              64,
                              -14,
                              111,
                              41,
                              -53,
                              -89,
                              -6,
                              -5,
                              -65,
                              -78,
                              -106,
                              -20,
                              -99,
                              -64
                           };
                           this.derivedKeySlot66 = keccakSha3Digest67.digest(VendorAesHelper.encryptWithCipher(bytes, bytes267, paddedAesCipher));
                           l = l - 6814997379123175669L ^ -3133367112107790757L;
                        }
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest68 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes271 = new byte[]{
                        41,
                        110,
                        -100,
                        -1,
                        121,
                        -45,
                        108,
                        41,
                        65,
                        32,
                        94,
                        -82,
                        66,
                        -126,
                        44,
                        -38,
                        -11,
                        45,
                        80,
                        75,
                        -92,
                        86,
                        89,
                        -109,
                        16,
                        -83,
                        121,
                        -76,
                        112,
                        58,
                        -23,
                        117
                     };
                     this.derivedKeySlot67 = keccakSha3Digest68.digest(VendorAesHelper.encryptWithCipher(bytes, bytes271, paddedAesCipher));
                     l = (l ^ 1890429889908778900L) - -5520579963202255003L;
                  } else {
                     if (b > 6) {
                        KeccakSha3Digest keccakSha3Digest69 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes275 = new byte[]{
                           117,
                           -42,
                           -23,
                           20,
                           -51,
                           24,
                           110,
                           -99,
                           -102,
                           -105,
                           94,
                           -84,
                           97,
                           -72,
                           122,
                           114,
                           -68,
                           52,
                           -6,
                           -76,
                           -93,
                           -10,
                           115,
                           -24,
                           -49,
                           44,
                           21,
                           7,
                           -82,
                           47,
                           70,
                           65
                        };
                        this.derivedKeySlot68 = keccakSha3Digest69.digest(VendorAesHelper.encryptWithCipher(bytes, bytes275, paddedAesCipher));
                        l = l - -6246664982790746595L ^ -249943415210472862L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest70 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes279 = new byte[]{
                        -29,
                        80,
                        -41,
                        -128,
                        -121,
                        -11,
                        70,
                        99,
                        37,
                        97,
                        13,
                        60,
                        -2,
                        103,
                        -117,
                        -118,
                        -103,
                        -44,
                        -114,
                        -113,
                        -20,
                        -82,
                        -110,
                        -96,
                        87,
                        -43,
                        -52,
                        -110,
                        -16,
                        -38,
                        4,
                        79
                     };
                     this.derivedKeySlot69 = keccakSha3Digest70.digest(VendorAesHelper.encryptWithCipher(bytes, bytes279, paddedAesCipher));
                     l = (l ^ -1249118881011317686L) - -8965454798791409762L;
                  }
               } else {
                  if (2 > b) {
                     if (b <= 0) {
                        KeccakSha3Digest keccakSha3Digest71 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes283 = new byte[]{
                           45,
                           -19,
                           124,
                           -118,
                           -77,
                           -81,
                           -17,
                           123,
                           13,
                           107,
                           -76,
                           -103,
                           112,
                           -79,
                           -73,
                           79,
                           120,
                           -50,
                           101,
                           -21,
                           -85,
                           -105,
                           -66,
                           -9,
                           100,
                           -127,
                           23,
                           -25,
                           28,
                           5,
                           55,
                           83
                        };
                        this.derivedKeySlot70 = keccakSha3Digest71.digest(VendorAesHelper.encryptWithCipher(bytes, bytes283, paddedAesCipher));
                        l = l + -5243077581724059943L ^ -4663083365255465582L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest72 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes287 = new byte[]{
                           89,
                           70,
                           5,
                           77,
                           -118,
                           -37,
                           115,
                           -63,
                           -109,
                           -8,
                           -56,
                           111,
                           61,
                           -68,
                           -16,
                           12,
                           -99,
                           -68,
                           -16,
                           54,
                           -22,
                           -107,
                           100,
                           9,
                           76,
                           -123,
                           -56,
                           -68,
                           27,
                           47,
                           86,
                           52
                        };
                        this.derivedKeySlot71 = keccakSha3Digest72.digest(VendorAesHelper.encryptWithCipher(bytes, bytes287, paddedAesCipher));
                        l = l + -2840651977592517864L ^ 3958746340116853136L;
                     }
                     continue outerLoop;
                  }

                  if (b <= 2) {
                     KeccakSha3Digest keccakSha3Digest73 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes291 = new byte[]{
                        -23,
                        43,
                        -102,
                        55,
                        56,
                        14,
                        112,
                        19,
                        -110,
                        69,
                        -111,
                        -92,
                        14,
                        -99,
                        15,
                        7,
                        30,
                        77,
                        91,
                        -36,
                        8,
                        -68,
                        74,
                        -32,
                        -89,
                        -36,
                        -62,
                        -18,
                        105,
                        36,
                        31,
                        90
                     };
                     this.derivedKeySlot72 = keccakSha3Digest73.digest(VendorAesHelper.encryptWithCipher(bytes, bytes291, paddedAesCipher));
                     l = (l ^ -6820909398282933833L) + -4912514877868023540L;
                  } else {
                     if (b > 3) {
                        KeccakSha3Digest keccakSha3Digest74 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes295 = new byte[]{
                           -62,
                           -82,
                           69,
                           -5,
                           -41,
                           -64,
                           -89,
                           -52,
                           -44,
                           35,
                           109,
                           -32,
                           -34,
                           -65,
                           -116,
                           -18,
                           52,
                           65,
                           82,
                           -8,
                           -120,
                           -113,
                           -5,
                           -37,
                           -108,
                           -47,
                           41,
                           119,
                           -83,
                           22,
                           102,
                           -54
                        };
                        this.derivedKeySlot73 = keccakSha3Digest74.digest(VendorAesHelper.encryptWithCipher(bytes, bytes295, paddedAesCipher));
                        l = l - -8646521968280955995L ^ -8636382423424966916L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest75 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes299 = new byte[]{
                        2,
                        117,
                        -37,
                        -27,
                        92,
                        -120,
                        -80,
                        -93,
                        61,
                        -101,
                        -32,
                        -110,
                        54,
                        -99,
                        -10,
                        47,
                        -124,
                        116,
                        -30,
                        55,
                        -121,
                        101,
                        69,
                        100,
                        17,
                        -18,
                        -107,
                        -30,
                        -68,
                        -10,
                        44,
                        31
                     };
                     this.derivedKeySlot74 = keccakSha3Digest75.digest(VendorAesHelper.encryptWithCipher(bytes, bytes299, paddedAesCipher));
                     l = (l ^ -6356564311719209001L) + 6531872784490389774L;
                  }
               }
            }

            if (b <= 25) {
               KeccakSha3Digest keccakSha3Digest76 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes303 = new byte[]{
                  79,
                  -33,
                  -127,
                  125,
                  -8,
                  116,
                  -111,
                  96,
                  -119,
                  -65,
                  117,
                  55,
                  -47,
                  14,
                  117,
                  -7,
                  -35,
                  20,
                  122,
                  -14,
                  -70,
                  36,
                  -115,
                  -99,
                  -119,
                  -107,
                  122,
                  -114,
                  -71,
                  56,
                  119,
                  117
               };
               this.derivedKeySlot75 = keccakSha3Digest76.digest(VendorAesHelper.encryptWithCipher(bytes, bytes303, paddedAesCipher));
               l = (l ^ 1075960263316654728L) + -3470966794587660813L;
               break;
            }

            if (b >= 38) {
               if (b <= 38) {
                  KeccakSha3Digest keccakSha3Digest77 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes307 = new byte[]{
                     -37,
                     -92,
                     -4,
                     -82,
                     82,
                     10,
                     88,
                     86,
                     17,
                     126,
                     18,
                     -122,
                     61,
                     -75,
                     -50,
                     14,
                     94,
                     -56,
                     -32,
                     49,
                     26,
                     95,
                     -53,
                     42,
                     7,
                     49,
                     106,
                     -96,
                     -68,
                     -102,
                     -105,
                     -73
                  };
                  this.derivedKeySlot76 = keccakSha3Digest77.digest(VendorAesHelper.encryptWithCipher(bytes, bytes307, paddedAesCipher));
                  l = (l ^ -3471895905497059717L) - 4235485068898085203L;
               } else if (b >= 44) {
                  if (b <= 44) {
                     KeccakSha3Digest keccakSha3Digest78 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes311 = new byte[]{
                        74,
                        6,
                        -31,
                        -18,
                        -35,
                        -65,
                        -69,
                        10,
                        -117,
                        -41,
                        -126,
                        107,
                        -83,
                        7,
                        54,
                        8,
                        -75,
                        -66,
                        73,
                        68,
                        93,
                        -59,
                        -8,
                        -105,
                        121,
                        99,
                        42,
                        16,
                        -87,
                        -87,
                        11,
                        -90
                     };
                     this.derivedKeySlot77 = keccakSha3Digest78.digest(VendorAesHelper.encryptWithCipher(bytes, bytes311, paddedAesCipher));
                     l = (l ^ -4020891559423241849L) - 8626844526474865085L;
                  } else {
                     if (b < 47) {
                        if (45 >= b) {
                           KeccakSha3Digest keccakSha3Digest79 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes315 = new byte[]{
                              46,
                              16,
                              -99,
                              65,
                              84,
                              -87,
                              91,
                              106,
                              -64,
                              30,
                              63,
                              10,
                              -8,
                              -31,
                              36,
                              -111,
                              -117,
                              114,
                              12,
                              1,
                              -123,
                              -14,
                              78,
                              62,
                              11,
                              30,
                              -80,
                              87,
                              -66,
                              53,
                              -63,
                              125
                           };
                           this.derivedKeySlot78 = keccakSha3Digest79.digest(VendorAesHelper.encryptWithCipher(bytes, bytes315, paddedAesCipher));
                           l = (l ^ -1493873961441632129L) + -7453384512499839550L;
                        } else {
                           KeccakSha3Digest keccakSha3Digest80 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes319 = new byte[]{
                              -59,
                              87,
                              76,
                              -45,
                              -75,
                              -127,
                              -19,
                              -6,
                              -94,
                              -12,
                              -39,
                              37,
                              88,
                              28,
                              96,
                              -23,
                              71,
                              -82,
                              -30,
                              119,
                              -28,
                              83,
                              72,
                              95,
                              -48,
                              -72,
                              -84,
                              30,
                              127,
                              -79,
                              -36,
                              -72
                           };
                           this.derivedKeySlot79 = keccakSha3Digest80.digest(VendorAesHelper.encryptWithCipher(bytes, bytes319, paddedAesCipher));
                           l = l + 2062984207978547698L ^ -7855452243398317477L;
                        }
                        break;
                     }

                     if (b <= 47) {
                        KeccakSha3Digest keccakSha3Digest81 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes323 = new byte[]{
                           -21,
                           -64,
                           -31,
                           48,
                           -51,
                           1,
                           -22,
                           -22,
                           91,
                           18,
                           32,
                           28,
                           -51,
                           -8,
                           35,
                           -88,
                           87,
                           68,
                           101,
                           40,
                           -93,
                           -80,
                           -69,
                           41,
                           -99,
                           19,
                           112,
                           -100,
                           84,
                           -15,
                           -3,
                           86
                        };
                        this.derivedKeySlot80 = keccakSha3Digest81.digest(VendorAesHelper.encryptWithCipher(bytes, bytes323, paddedAesCipher));
                        l = l - 5099162654581708909L ^ -4722140217278904009L;
                     } else if (49 <= b) {
                        if (b <= 49) {
                           KeccakSha3Digest keccakSha3Digest82 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes327 = new byte[]{
                              83,
                              -9,
                              -116,
                              49,
                              69,
                              126,
                              60,
                              -75,
                              9,
                              -56,
                              -55,
                              -90,
                              27,
                              -2,
                              72,
                              -16,
                              -84,
                              80,
                              86,
                              117,
                              -52,
                              94,
                              83,
                              -123,
                              61,
                              124,
                              -82,
                              104,
                              73,
                              125,
                              108,
                              -127
                           };
                           this.derivedKeySlot81 = keccakSha3Digest82.digest(VendorAesHelper.encryptWithCipher(bytes, bytes327, paddedAesCipher));
                           l = (l ^ 2221777003225519600L) - 1814240561470846313L;
                        } else {
                           KeccakSha3Digest keccakSha3Digest83 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes331 = new byte[]{
                              21,
                              102,
                              -71,
                              -99,
                              34,
                              90,
                              108,
                              46,
                              73,
                              -113,
                              -42,
                              104,
                              -39,
                              -42,
                              -65,
                              25,
                              -70,
                              0,
                              68,
                              -97,
                              4,
                              -48,
                              102,
                              9,
                              -67,
                              37,
                              -103,
                              82,
                              100,
                              -124,
                              -104,
                              -19
                           };
                           this.derivedKeySlot82 = keccakSha3Digest83.digest(VendorAesHelper.encryptWithCipher(bytes, bytes331, paddedAesCipher));
                           l = (l ^ 5944505870081354362L) - -4398244024829531332L;
                        }
                     } else {
                        KeccakSha3Digest keccakSha3Digest84 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes335 = new byte[]{
                           36,
                           72,
                           -103,
                           54,
                           53,
                           68,
                           42,
                           -76,
                           -111,
                           116,
                           -22,
                           24,
                           -52,
                           98,
                           -6,
                           -115,
                           -70,
                           34,
                           -94,
                           -8,
                           -125,
                           54,
                           -67,
                           110,
                           -14,
                           -35,
                           80,
                           -99,
                           56,
                           -51,
                           -103,
                           -59
                        };
                        this.derivedKeySlot83 = keccakSha3Digest84.digest(VendorAesHelper.encryptWithCipher(bytes, bytes335, paddedAesCipher));
                        l = l - -3942827468246260966L ^ 22341976187843627L;
                     }
                  }
               } else {
                  if (b >= 41) {
                     if (b <= 41) {
                        KeccakSha3Digest keccakSha3Digest85 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes339 = new byte[]{
                           9,
                           42,
                           38,
                           -13,
                           -95,
                           -113,
                           15,
                           54,
                           79,
                           2,
                           61,
                           -50,
                           -121,
                           -110,
                           -100,
                           -33,
                           -9,
                           70,
                           -17,
                           -125,
                           -9,
                           -82,
                           -109,
                           -123,
                           -68,
                           -38,
                           69,
                           36,
                           -95,
                           -91,
                           -62,
                           31
                        };
                        this.derivedKeySlot84 = keccakSha3Digest85.digest(VendorAesHelper.encryptWithCipher(bytes, bytes339, paddedAesCipher));
                        l = l + 5501351323233076906L ^ 7681763475173152981L;
                     } else if (42 >= b) {
                        KeccakSha3Digest keccakSha3Digest86 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes343 = new byte[]{
                           107,
                           -27,
                           4,
                           62,
                           87,
                           -113,
                           -91,
                           -66,
                           -7,
                           30,
                           43,
                           1,
                           -37,
                           61,
                           -35,
                           64,
                           -116,
                           35,
                           -20,
                           75,
                           -44,
                           33,
                           -115,
                           -43,
                           -97,
                           -94,
                           -20,
                           -90,
                           29,
                           -72,
                           71,
                           -87
                        };
                        this.derivedKeySlot85 = keccakSha3Digest86.digest(VendorAesHelper.encryptWithCipher(bytes, bytes343, paddedAesCipher));
                        l = (l ^ 2090101526073893432L) + -1667354773554285813L;
                     } else {
                        this.deriveDecoyKeySlots(bytes);
                        l = (l ^ -8921372080232091874L) - 4482907712810183074L;
                     }
                     break;
                  }

                  if (b > 39) {
                     KeccakSha3Digest keccakSha3Digest87 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes348 = new byte[]{
                        -2,
                        -5,
                        2,
                        -85,
                        -94,
                        -34,
                        -57,
                        22,
                        -16,
                        2,
                        -76,
                        -56,
                        -20,
                        56,
                        -89,
                        -41,
                        -117,
                        -59,
                        -42,
                        93,
                        -53,
                        -22,
                        63,
                        -65,
                        -56,
                        -94,
                        119,
                        59,
                        55,
                        -95,
                        -80,
                        -48
                     };
                     this.derivedKeySlot86 = keccakSha3Digest87.digest(VendorAesHelper.encryptWithCipher(bytes, bytes348, paddedAesCipher));
                     l = l - 503631567071546967L ^ 1778306171603804288L;
                     break;
                  }

                  KeccakSha3Digest keccakSha3Digest88 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes352 = new byte[]{
                     -3,
                     10,
                     26,
                     -97,
                     -111,
                     -22,
                     92,
                     70,
                     34,
                     -74,
                     83,
                     65,
                     121,
                     47,
                     31,
                     104,
                     -56,
                     -61,
                     10,
                     22,
                     58,
                     29,
                     79,
                     -93,
                     -75,
                     79,
                     -126,
                     -81,
                     66,
                     90,
                     72,
                     66
                  };
                  this.derivedKeySlot87 = keccakSha3Digest88.digest(VendorAesHelper.encryptWithCipher(bytes, bytes352, paddedAesCipher));
                  l = l - 7405755584614123704L ^ -7977094379940475240L;
               }
            } else if (31 <= b) {
               if (b <= 31) {
                  KeccakSha3Digest keccakSha3Digest89 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes356 = new byte[]{
                     -17,
                     40,
                     62,
                     13,
                     -113,
                     95,
                     -116,
                     -108,
                     -107,
                     51,
                     -16,
                     64,
                     -77,
                     102,
                     36,
                     -69,
                     116,
                     60,
                     3,
                     -82,
                     -4,
                     90,
                     26,
                     63,
                     44,
                     -31,
                     -93,
                     54,
                     66,
                     1,
                     -9,
                     -122
                  };
                  this.derivedKeySlot88 = keccakSha3Digest89.digest(VendorAesHelper.encryptWithCipher(bytes, bytes356, paddedAesCipher));
                  l = (l ^ 2357408258712775100L) - -367889305100529207L;
                  break;
               }

               if (b >= 34) {
                  if (34 >= b) {
                     KeccakSha3Digest keccakSha3Digest90 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes360 = new byte[]{
                        -38,
                        65,
                        -117,
                        93,
                        -119,
                        -40,
                        -102,
                        -42,
                        -70,
                        -24,
                        -6,
                        -13,
                        29,
                        17,
                        78,
                        -5,
                        104,
                        8,
                        9,
                        26,
                        124,
                        -96,
                        -29,
                        59,
                        -72,
                        -128,
                        0,
                        -31,
                        -35,
                        -119,
                        -93,
                        -64
                     };
                     this.derivedKeySlot89 = keccakSha3Digest90.digest(VendorAesHelper.encryptWithCipher(bytes, bytes360, paddedAesCipher));
                     l = l - -2628632913187772338L ^ 2957131416875853187L;
                     break;
                  }

                  if (36 <= b) {
                     if (36 < b) {
                        KeccakSha3Digest keccakSha3Digest91 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes364 = new byte[]{
                           118,
                           -29,
                           97,
                           -68,
                           46,
                           -88,
                           79,
                           -105,
                           -106,
                           -34,
                           -29,
                           -31,
                           9,
                           -100,
                           -112,
                           -61,
                           9,
                           62,
                           100,
                           -71,
                           67,
                           87,
                           17,
                           80,
                           100,
                           -113,
                           -98,
                           109,
                           89,
                           -1,
                           120,
                           73
                        };
                        this.derivedKeySlot90 = keccakSha3Digest91.digest(VendorAesHelper.encryptWithCipher(bytes, bytes364, paddedAesCipher));
                        l = l + -5607724356539559479L ^ 2501326698294016159L;
                        break;
                     }

                     KeccakSha3Digest keccakSha3Digest92 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes368 = new byte[]{
                        108,
                        40,
                        -84,
                        -22,
                        108,
                        5,
                        -3,
                        -56,
                        -36,
                        56,
                        63,
                        49,
                        -5,
                        -32,
                        108,
                        81,
                        -62,
                        -31,
                        53,
                        -58,
                        -5,
                        31,
                        77,
                        -88,
                        27,
                        -4,
                        -35,
                        57,
                        -56,
                        -128,
                        -45,
                        -15
                     };
                     this.derivedKeySlot91 = keccakSha3Digest92.digest(VendorAesHelper.encryptWithCipher(bytes, bytes368, paddedAesCipher));
                     l = l - 932434793351100337L ^ 5435160282075190194L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest93 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes372 = new byte[]{
                        -28,
                        58,
                        -16,
                        82,
                        65,
                        -81,
                        -74,
                        -107,
                        94,
                        111,
                        75,
                        -29,
                        -58,
                        46,
                        55,
                        -122,
                        3,
                        -98,
                        93,
                        -90,
                        26,
                        33,
                        20,
                        81,
                        93,
                        -52,
                        52,
                        -22,
                        68,
                        -98,
                        123,
                        -66
                     };
                     this.derivedKeySlot92 = keccakSha3Digest93.digest(VendorAesHelper.encryptWithCipher(bytes, bytes372, paddedAesCipher));
                     l = (l ^ 5075171330763921355L) - 2131546806928729584L;
                  }
               } else {
                  if (32 < b) {
                     KeccakSha3Digest keccakSha3Digest94 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes376 = new byte[]{
                        -34,
                        84,
                        -94,
                        -10,
                        -19,
                        6,
                        61,
                        -28,
                        75,
                        105,
                        -15,
                        -85,
                        109,
                        -91,
                        -32,
                        125,
                        120,
                        -65,
                        51,
                        -70,
                        -21,
                        -39,
                        59,
                        -111,
                        98,
                        84,
                        26,
                        -44,
                        -52,
                        -26,
                        119,
                        -40
                     };
                     this.derivedKeySlot93 = keccakSha3Digest94.digest(VendorAesHelper.encryptWithCipher(bytes, bytes376, paddedAesCipher));
                     l = (l ^ -6258537747760750093L) + -4391601008274690996L;
                     break;
                  }

                  KeccakSha3Digest keccakSha3Digest95 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes380 = new byte[]{
                     -108,
                     -53,
                     -111,
                     9,
                     62,
                     41,
                     -123,
                     -124,
                     -3,
                     -128,
                     -67,
                     47,
                     52,
                     -95,
                     2,
                     -32,
                     -71,
                     -26,
                     -55,
                     44,
                     47,
                     34,
                     55,
                     -128,
                     -74,
                     95,
                     92,
                     -125,
                     -93,
                     -28,
                     -81,
                     84
                  };
                  this.derivedKeySlot94 = keccakSha3Digest95.digest(VendorAesHelper.encryptWithCipher(bytes, bytes380, paddedAesCipher));
                  l = l + -3701816708582627089L ^ 5775469923706729840L;
               }
            } else if (b >= 28) {
               if (28 >= b) {
                  KeccakSha3Digest keccakSha3Digest96 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes384 = new byte[]{
                     -35,
                     -92,
                     -41,
                     9,
                     -98,
                     17,
                     107,
                     19,
                     77,
                     13,
                     -28,
                     -22,
                     -86,
                     95,
                     -32,
                     -81,
                     -87,
                     97,
                     112,
                     98,
                     78,
                     101,
                     93,
                     2,
                     -111,
                     11,
                     -127,
                     -33,
                     -15,
                     110,
                     -31,
                     -46
                  };
                  this.derivedKeySlot95 = keccakSha3Digest96.digest(VendorAesHelper.encryptWithCipher(bytes, bytes384, paddedAesCipher));
                  l = (l ^ -2812036671682303206L) - -5241415405772945257L;
                  break;
               }

               if (b > 29) {
                  KeccakSha3Digest keccakSha3Digest97 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes388 = new byte[]{
                     64,
                     39,
                     66,
                     -69,
                     -1,
                     -102,
                     31,
                     -102,
                     111,
                     -85,
                     -99,
                     -110,
                     107,
                     19,
                     -92,
                     107,
                     65,
                     78,
                     65,
                     12,
                     82,
                     125,
                     -99,
                     -57,
                     127,
                     22,
                     27,
                     -99,
                     0,
                     -115,
                     -88,
                     -58
                  };
                  this.derivedKeySlot96 = keccakSha3Digest97.digest(VendorAesHelper.encryptWithCipher(bytes, bytes388, paddedAesCipher));
                  l = l + 7635202849020543717L ^ -1737655259531118864L;
                  break;
               }

               KeccakSha3Digest keccakSha3Digest98 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes392 = new byte[]{
                  -24,
                  -36,
                  14,
                  -65,
                  -96,
                  117,
                  99,
                  -27,
                  61,
                  -16,
                  -27,
                  62,
                  58,
                  34,
                  -118,
                  108,
                  -67,
                  -20,
                  -32,
                  14,
                  82,
                  83,
                  27,
                  101,
                  -73,
                  41,
                  -83,
                  122,
                  17,
                  -72,
                  -112,
                  116
               };
               this.derivedKeySlot97 = keccakSha3Digest98.digest(VendorAesHelper.encryptWithCipher(bytes, bytes392, paddedAesCipher));
               l = l - -4710735914577377162L ^ -1788386502126205162L;
            } else {
               if (26 < b) {
                  KeccakSha3Digest keccakSha3Digest99 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes396 = new byte[]{
                     123,
                     -49,
                     -40,
                     -66,
                     -58,
                     80,
                     17,
                     -41,
                     102,
                     104,
                     -71,
                     -75,
                     -47,
                     72,
                     -10,
                     -41,
                     -89,
                     5,
                     -111,
                     97,
                     -40,
                     80,
                     60,
                     67,
                     37,
                     80,
                     101,
                     -94,
                     -49,
                     -108,
                     -119,
                     47
                  };
                  this.derivedKeySlot98 = keccakSha3Digest99.digest(VendorAesHelper.encryptWithCipher(bytes, bytes396, paddedAesCipher));
                  l = (l ^ 4103995325447373153L) + 144903054812436202L;
                  break;
               }

               KeccakSha3Digest keccakSha3Digest100 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes400 = new byte[]{
                  24,
                  23,
                  -12,
                  52,
                  -122,
                  -10,
                  20,
                  -12,
                  -105,
                  -41,
                  -4,
                  71,
                  -128,
                  38,
                  17,
                  3,
                  -75,
                  -112,
                  -82,
                  24,
                  -52,
                  89,
                  -65,
                  107,
                  44,
                  -17,
                  18,
                  -29,
                  -53,
                  -25,
                  -110,
                  -91
               };
               this.derivedKeySlot99 = keccakSha3Digest100.digest(VendorAesHelper.encryptWithCipher(bytes, bytes400, paddedAesCipher));
               l = l - 737005547978029876L ^ 6283177305369372564L;
            }
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
         } catch (ShortBufferException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
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

   public int unpadPkcs5(byte[] bytes, int n, int n2) {
      byte b = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      byte b2 = 2;

      outerLoop:
      while (true) {
         if (b2 >= 4) {
            if (4 >= b2) {
               if (n2 == 0) {
                  ;
               }
            } else {
               if (6 <= b2) {
                  if (6 < b2) {
                     if (b2 <= 7) {
                        return -1;
                     }

                     return -1;
                  }

                  if (bytes[n6] != b) {
                     ;
                  }
                  continue;
               }

               if (bytes != null) {
                  continue;
               }
            }
         } else if (1 <= b2) {
            if (1 >= b2) {
               return n3;
            }

            if (2 >= b2) {
               n6++;
            } else if (n4 < 1) {
               continue;
            }
         } else {
            b = bytes[n5 - 1];
         }

         while (true) {
            while (9 < b2) {
               if (b2 >= 14) {
                  if (b2 <= 14) {
                     if (n4 > this.getBlockSize()) {
                        continue outerLoop;
                     }
                  } else if (16 <= b2) {
                     if (16 < b2) {
                        if (b2 > 17) {
                           return -1;
                        }

                        n4 = b & 255;
                        continue outerLoop;
                     }

                     n6 = n3;
                  } else if (n3 < n) {
                     ;
                  }
               } else if (b2 >= 11) {
                  if (11 >= b2) {
                     if (n6 < n5) {
                        ;
                     }
                  } else {
                     if (12 >= b2) {
                        return 0;
                     }

                     n5 = Math.addExact(n, n2);
                  }
                  continue outerLoop;
               }
            }

            n3 = n5 - n4;
         }
      }
   }

   public int getPadLength(int n) {
      return 0 - n % 0;
   }

   private int[] expandToSubkeys(int[][] intsArr, boolean flag) {
      int n = 0;
      int n2 = 0;
      int[] ints = null;
      byte b = 6;

      outerLoop:
      while (true) {
         while (3 > b) {
            if (1 <= b) {
               if (1 >= b) {
                  System.arraycopy(intsArr[n2 - 1], 0, ints, 0, 4);
               }
            } else {
               n = 1;
            }
         }

         if (b <= 3) {
            n2 = intsArr.length;
         } else {
            if (5 <= b) {
               if (5 >= b) {
                  if (n < n2) {
                     ;
                  }
               } else {
                  n++;
               }
            } else {
               ints = new int[n2 * 4];
            }

            while (b > 7) {
               if (b >= 11) {
                  if (11 >= b) {
                     System.arraycopy(intsArr[n], 0, ints, n * 4, 4);
                     continue outerLoop;
                  }

                  if (13 <= b) {
                     if (b > 13) {
                        if (14 >= b) {
                           return ints;
                        }

                        System.arraycopy(intsArr[n - 1], 0, ints, n * 4, 4);
                     }
                  } else if (flag) {
                     continue outerLoop;
                  }
               } else if (9 <= b) {
                  if (b > 9) {
                     n = 0;
                  }
               } else if (n < n2) {
                  ;
               }
            }

            n++;
         }
      }
   }

   private byte[] prepareInputBuffer(byte[] bytes, int n, int n2, boolean flag) throws IllegalBlockSizeException, ShortBufferException {
      int n3 = 0;
      byte[] bytes2 = null;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      byte b = 0;
      int n7 = 15;
      long l = 3908304567332836496L;
      long l2 = 0L;
      l = 9118192875619587059L ^ (long)"\uec19斆⛀䀹쫡醷祱縗䊟䃻⬐썂\udab4働\ud854퓈哖蹹ퟲ珺Ѩ꺮退杈ޝ윸돞끏슗".hashCode();
      l2 = -3177064728162875206L ^ (long)"䦉⨺ﮟ垴什閸\udbcfⵏ\ue2b4춺ꉓ\uf65bᆊ柈\uda54拑뫳ث宻㝡⏢".hashCode();

      while (true) {
         n7 = (int)(l % 19L);
         if (9 <= n7) {
            if (n7 <= 9) {
               n3 = this.getPadLength(n4);
               l = (l ^ -2459040986464742011L) + -1292095961178458316L;
               l2 = (l2 ^ -472352106998421117L) - 4475136405125304329L;
            } else if (14 <= n7) {
               if (n7 <= 14) {
                  if (n3 <= 0) {
                     l = (l ^ 0L) + 0L;
                     l2 = (l2 ^ 0L) + 0L;
                  } else {
                     l = (l ^ 0L) - 0L;
                     l2 = l2 - 0L ^ 0L;
                  }
               } else {
                  if (16 > n7) {
                     return bytes;
                  }

                  if (n7 <= 16) {
                     System.arraycopy(bytes, n, bytes2, b, n2);
                     l = l - 5185931017094065110L ^ -625191140924034412L;
                     l2 = l2 - 1918505943419012381L ^ -3326730860524018714L;
                  } else if (17 >= n7) {
                     n4 = n5;
                     l = (l ^ 3982861770739701333L) - 2747171300181228820L;
                     l2 = (l2 ^ -9169095502237272713L) + 4870160980313393481L;
                  } else {
                     bytes2 = new byte[Math.addExact(n5, n3)];
                     l = l + -1373517927792163715L ^ 5452180082249949358L;
                     l2 = (l2 ^ 6749286413691658998L) - -3443728647463430406L;
                  }
               }
            } else if (11 <= n7) {
               if (11 >= n7) {
                  if (flag) {
                     l = l - 0L ^ 0L;
                     l2 = l2 - 0L ^ 0L;
                  } else {
                     l = l - -8171294599293328163L ^ -4341362696596397527L;
                     l2 = (l2 ^ 948480017690328080L) - 2045313453576783403L;
                  }
               } else {
                  if (n7 <= 12) {
                     return bytes2;
                  }

                  b = 0;
                  l = (l ^ -6981236364690449673L) - -3358812095460938562L;
                  l2 = (l2 ^ -582525510350238162L) - -7750995364979782401L;
               }
            } else if (n3 == 0) {
               l = l + 9004903519760693357L ^ -4100976345014850441L;
               l2 = (l2 ^ 4007157391077575163L) + 542877908216408758L;
            } else {
               l = l + -4626006068331857383L ^ -1811109359385458990L;
               l2 = (l2 ^ -2949029318938179172L) - -4393517232961474538L;
            }
         } else if (n7 >= 4) {
            if (n7 <= 4) {
               throw new IllegalBlockSizeException("Input length must be multiple of " + n6 + " when decrypting with padded cipher");
            }

            if (6 <= n7) {
               if (6 >= n7) {
                  if (n2 == 0) {
                     l = (l ^ -6083043270915859207L) - -4765682874737023230L;
                     l2 = l2 - -7867145167762038858L ^ -7291117381780975292L;
                  } else {
                     l = (l ^ 6932009566842961343L) - 475209279673108982L;
                     l2 = l2 + 6136401895090691963L ^ -8511748806378325862L;
                  }
               } else if (n7 <= 7) {
                  if (!flag) {
                     l = (l ^ 310165430929582799L) + -149536048594717512L;
                     l2 = (l2 ^ -2941949391718101731L) - -8640504700045794010L;
                  } else {
                     l = l - 0L ^ 0L;
                     l2 = l2 - 0L ^ 0L;
                  }
               } else {
                  int n8 = n2 % n6;
                  l = (l ^ 6901154195252310133L) + -6037450986677748013L;
                  l2 = l2 - -1893754232458095015L ^ 8894231144334774978L;
               }
            } else {
               n5 = Math.addExact(b, n2);
               l = (l ^ 7886751068744371342L) + -6722488603291152000L;
               l2 = l2 - 3808712695309601293L ^ -7865625899702603429L;
            }
         } else if (1 <= n7) {
            if (n7 <= 1) {
               if (n3 == n6) {
                  l = l - 0L ^ 0L;
                  l2 = l2 + 0L ^ 0L;
               } else {
                  l = (l ^ 0L) + 0L;
                  l2 = l2 + 0L ^ 0L;
               }
            } else if (2 >= n7) {
               this.padWithLen(bytes2, Math.addExact(b, n2), n3);
               l = (l ^ 178124881294973330L) + 6177049393134686365L;
               l2 = l2 - -8587607048390537955L ^ -5548691794266986046L;
            } else {
               n3 = 0;
               l = (l ^ -7621407102624395453L) - 4484004631877086704L;
               l2 = (l2 ^ -757229577547062994L) - -8463137788954812008L;
            }
         } else {
            n6 = this.getBlockSize();
            l = (l ^ 4304350432912262014L) - -1205294293823828689L;
            l2 = l2 + -6399284070674894917L ^ -3054772840312760123L;
         }
      }
   }

   private void makeSessionKey(byte[] bytes) throws InvalidKeyException {
      int n = 0;
      int rounds = this.getRounds(bytes.length);
      int n2 = (rounds + 1) * 4;
      int[][] intsArr = new int[rounds + 1][4];
      int[][] intsArr2 = new int[rounds + 1][4];
      int n3 = bytes.length / 4;
      String opaqueStateTag = VendorPacketBuffer.getOpaqueStateTag();
      int[] ints = new int[n3];
      int n4 = 0;
      int n5 = 0;
      String str = opaqueStateTag;

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

   private int encryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = 0;
      int n5 = 0;
      int n6 = 8;
      long l = -4617071769209617143L;
      long l2 = 0L;
      l = -7586425718451596296L ^ (long)"耀ൢ窯仲鶙ዯ窟\ue1b1⩗\ue9faꊚ䞨擈롛㮆軍\udf53ົꮈ部\uf095䰥졋㱵".hashCode();
      l2 = -1738872114262909110L ^ (long)"餽萍\ue15bꗓŤ氚ଛﾩ\uf834飑㍐纻ᜲ\u0cd1쥴촯ￒ궊噙鉟黀갢妋פֿ榜땐๕醘".hashCode();

      while (true) {
         n6 = (int)(l % 13L);
         if (n6 >= 6) {
            if (6 >= n6) {
               if (n4 >= 0) {
                  l = (l ^ -5215263929452099309L) + 3754791758270307671L;
                  l2 = (l2 ^ 2763502325336816334L) + 5887010148524690196L;
               } else {
                  l = (l ^ -5781981040327385175L) - -7008052987545882113L;
                  l2 = l2 - 8368403314063379912L ^ 7322180900216557038L;
               }
            } else if (n6 >= 9) {
               if (9 >= n6) {
                  n4 = 0;
                  l = l + 8903979406069229951L ^ -6388958708631511551L;
                  l2 = l2 - -3868445116143086807L ^ -919953842619117427L;
               } else if (11 <= n6) {
                  if (11 >= n6) {
                     l = (l ^ 4130386790681647107L) + 152601027424508682L;
                     l2 = (l2 ^ -6235385109265207685L) + 9181879511794277613L;
                  } else {
                     System.arraycopy(bytes2, n3, this.cbcRegister, 0, 0);
                     l = l + 8850754646230732151L ^ -3562698011503127298L;
                     l2 = l2 + -8830751571966649786L ^ -4658566060523145495L;
                  }
               } else {
                  l = (l ^ 193549619788473730L) - -1790154730528376465L;
                  l2 = (l2 ^ 1978485310284170306L) + 7548281671218499645L;
               }
            } else if (7 >= n6) {
               n3 += 0;
               l = l - -890574695910893608L ^ 2980666251904980471L;
               l2 = l2 + -3415565542630444935L ^ -3705453163263978504L;
            } else {
               l = l + 3234280548695935496L ^ -4794160923270796472L;
               l2 = (l2 ^ -7945674821171898224L) - -1703794272114993670L;
            }
         } else if (n6 >= 2) {
            if (2 >= n6) {
               if (n >= n5) {
                  l = l + 2540584728317446749L ^ 7488286172628029783L;
                  l2 = l2 + 1688246407953822039L ^ 5484096602874281521L;
               } else {
                  l = l - -6370882440374027744L ^ 1275111837141769012L;
                  l2 = (l2 ^ -5047518303580641737L) + 6606051512780187159L;
               }
            } else if (4 <= n6) {
               if (4 >= n6) {
                  return n2;
               }

               n += 0;
               l = (l ^ 1134678201068204579L) - 2323272589302187198L;
               l2 = (l2 ^ 5726429178282008715L) + 2710339136086978259L;
            } else {
               n4++;
               l = l + 6503189301430696473L ^ -7426042036448289182L;
               l2 = l2 + 6460060027866339515L ^ 1241711367877648511L;
            }
         } else if (0 >= n6) {
            n5 = n + n2;
            l = (l ^ 4429620044433361939L) - 3665765896588545803L;
            l2 = (l2 ^ -482363912109613696L) + -1565746614825904940L;
         } else {
            this.cbcWorkBuffer[n4] = (byte)(bytes[n4 + n] ^ this.cbcRegister[n4]);
            l = (l ^ -6386192755781291009L) + 7806551349897736513L;
            l2 = l2 + -5937529860611155774L ^ -1391574099490066901L;
         }
      }
   }

   public void resetCbcState() {
      this.cbcRegister = new byte[this.getBlockSize()];
      this.cbcWorkBuffer = new byte[this.getBlockSize()];
   }

   public void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
      this.encodeBuffer(ctx, (ByteBuf)msg, out);
   }

   private void encryptBlockImpl(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 0;
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];

      while (n3 < this.roundKeyLimit) {
         int n8 = this.encTable1[n4 >>> 24]
            ^ this.encTable2[n5 >>> 16 & 0xFF]
            ^ this.encTable3[n6 >>> 8 & 0xFF]
            ^ this.encTable4[n7 & 0xFF]
            ^ this.roundKeys[n3++];
         int n9 = this.encTable1[n5 >>> 24]
            ^ this.encTable2[n6 >>> 16 & 0xFF]
            ^ this.encTable3[n7 >>> 8 & 0xFF]
            ^ this.encTable4[n4 & 0xFF]
            ^ this.roundKeys[n3++];
         int n10 = this.encTable1[n6 >>> 24]
            ^ this.encTable2[n7 >>> 16 & 0xFF]
            ^ this.encTable3[n4 >>> 8 & 0xFF]
            ^ this.encTable4[n5 & 0xFF]
            ^ this.roundKeys[n3++];
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

   private void deriveDecoyKeySlots(byte[] bytes) {
      PaddedAesCipher paddedAesCipher = null;
      byte b = 69;
      long l = 2221516029834405354L;
      l = -8405804358813482088L ^ (long)"宩蠡㼲쪂̼꒥歶䑧狍῞봴ˉﺒढ\ue60d蜿ﭣ顾Ⳟ\ue471".hashCode();

      outerLoop:
      while (true) {
         if (b >= 24) {
            if (b <= 24) {
               KeccakSha3Digest keccakSha3Digest = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes5 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
                  -50, 83, -110, 126, -89, -88, 29, 88, 106, 87, 110, -16, 92, 28, 22, 33, 82, 29, 21, 16, 49, 55, 114, 65, 66, 15, 74, -38, 22, 126, 127, 19
               }, paddedAesCipher);
               this.decoyKeySlot01 = keccakSha3Digest.digest(bytes5);
               l = (l ^ 1288302492611023791L) + 6297112630296787554L;
               continue;
            }

            if (37 <= b) {
               if (b <= 37) {
                  KeccakSha3Digest keccakSha3Digest2 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes7 = new byte[]{
                     -58,
                     12,
                     -120,
                     -105,
                     8,
                     -116,
                     44,
                     10,
                     26,
                     34,
                     -121,
                     -77,
                     33,
                     40,
                     -59,
                     1,
                     -30,
                     15,
                     41,
                     36,
                     69,
                     -75,
                     -53,
                     -116,
                     18,
                     102,
                     52,
                     -86,
                     -3,
                     95,
                     97,
                     -89
                  };
                  this.decoyKeySlot02 = keccakSha3Digest2.digest(VendorAesHelper.encryptWithCipher(bytes, bytes7, paddedAesCipher));
                  l = (l ^ -6430279877194984608L) + -7680081075079826255L;
                  continue;
               }

               if (b >= 43) {
                  if (b <= 43) {
                     KeccakSha3Digest keccakSha3Digest3 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes11 = new byte[]{
                        78,
                        2,
                        68,
                        43,
                        78,
                        -102,
                        33,
                        46,
                        -54,
                        84,
                        -128,
                        -5,
                        -93,
                        -103,
                        105,
                        -49,
                        44,
                        -19,
                        13,
                        34,
                        -112,
                        84,
                        -98,
                        -37,
                        -93,
                        -99,
                        -84,
                        0,
                        -33,
                        -68,
                        4,
                        66
                     };
                     this.decoyKeySlot03 = keccakSha3Digest3.digest(VendorAesHelper.encryptWithCipher(bytes, bytes11, paddedAesCipher));
                     l = l - 4537632753308902903L ^ -1180772146527006603L;
                     continue;
                  }

                  if (b >= 46) {
                     if (b <= 46) {
                        KeccakSha3Digest keccakSha3Digest4 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes15 = new byte[]{
                           -76,
                           -110,
                           127,
                           11,
                           10,
                           -40,
                           67,
                           -77,
                           85,
                           15,
                           68,
                           -36,
                           -94,
                           -5,
                           126,
                           -95,
                           -80,
                           122,
                           -91,
                           47,
                           16,
                           87,
                           -8,
                           -85,
                           -113,
                           -100,
                           102,
                           -45,
                           27,
                           -118,
                           116,
                           -12
                        };
                        this.decoyKeySlot04 = keccakSha3Digest4.digest(VendorAesHelper.encryptWithCipher(bytes, bytes15, paddedAesCipher));
                        l = (l ^ 2130483321685880865L) - -2807129410376764734L;
                        continue;
                     }

                     if (b >= 48) {
                        if (b > 48) {
                           KeccakSha3Digest keccakSha3Digest5 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes19 = new byte[]{
                              -32,
                              56,
                              117,
                              -47,
                              77,
                              47,
                              74,
                              -60,
                              -99,
                              -68,
                              113,
                              69,
                              -83,
                              84,
                              -28,
                              42,
                              8,
                              -23,
                              48,
                              -62,
                              -51,
                              47,
                              54,
                              -124,
                              -22,
                              -34,
                              3,
                              80,
                              94,
                              -65,
                              -43,
                              -115
                           };
                           this.decoyKeySlot05 = keccakSha3Digest5.digest(VendorAesHelper.encryptWithCipher(bytes, bytes19, paddedAesCipher));
                           l = (l ^ -8723216989421754940L) - 6151002830959181280L;
                           continue;
                        }

                        KeccakSha3Digest keccakSha3Digest6 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes23 = new byte[]{
                           15,
                           32,
                           121,
                           -73,
                           -61,
                           48,
                           -110,
                           74,
                           4,
                           65,
                           119,
                           44,
                           -96,
                           -104,
                           24,
                           64,
                           -96,
                           44,
                           -83,
                           120,
                           -42,
                           -49,
                           68,
                           -38,
                           85,
                           30,
                           126,
                           -45,
                           51,
                           -41,
                           52,
                           -105
                        };
                        this.decoyKeySlot06 = keccakSha3Digest6.digest(VendorAesHelper.encryptWithCipher(bytes, bytes23, paddedAesCipher));
                        l = l + 4763925666780074589L ^ -6688616900920689680L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest7 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes27 = new byte[]{
                           16,
                           67,
                           78,
                           -20,
                           -62,
                           2,
                           -68,
                           -26,
                           -98,
                           -68,
                           -101,
                           116,
                           110,
                           20,
                           67,
                           -67,
                           103,
                           80,
                           -97,
                           100,
                           96,
                           -17,
                           -95,
                           -63,
                           117,
                           -81,
                           40,
                           -21,
                           95,
                           -8,
                           -40,
                           2
                        };
                        this.decoyKeySlot07 = keccakSha3Digest7.digest(VendorAesHelper.encryptWithCipher(bytes, bytes27, paddedAesCipher));
                        l = (l ^ -563976087252059883L) - 5017583752196374032L;
                     }
                  } else if (b <= 44) {
                     KeccakSha3Digest keccakSha3Digest8 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes31 = new byte[]{
                        -90,
                        -99,
                        -55,
                        123,
                        51,
                        -48,
                        -65,
                        53,
                        35,
                        -14,
                        -20,
                        94,
                        -112,
                        -77,
                        47,
                        101,
                        -18,
                        -31,
                        -108,
                        -39,
                        -18,
                        66,
                        -108,
                        -105,
                        94,
                        69,
                        -18,
                        64,
                        -115,
                        -7,
                        -90,
                        13
                     };
                     this.decoyKeySlot08 = keccakSha3Digest8.digest(VendorAesHelper.encryptWithCipher(bytes, bytes31, paddedAesCipher));
                     l = (l ^ 5698820847982274660L) + -1142691793489727674L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest9 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes35 = new byte[]{
                        -14,
                        -24,
                        -115,
                        49,
                        -51,
                        84,
                        125,
                        67,
                        52,
                        6,
                        -19,
                        42,
                        74,
                        -15,
                        44,
                        -27,
                        94,
                        17,
                        -70,
                        -106,
                        90,
                        -90,
                        -71,
                        -80,
                        61,
                        -19,
                        -42,
                        -83,
                        -109,
                        94,
                        -5,
                        -75
                     };
                     this.decoyKeySlot09 = keccakSha3Digest9.digest(VendorAesHelper.encryptWithCipher(bytes, bytes35, paddedAesCipher));
                     l = (l ^ 2768664965851027954L) - -5103854269969776370L;
                  }
               } else if (40 <= b) {
                  if (40 >= b) {
                     KeccakSha3Digest keccakSha3Digest10 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes39 = new byte[]{
                        -16,
                        68,
                        -21,
                        125,
                        102,
                        25,
                        -128,
                        67,
                        -13,
                        44,
                        -45,
                        78,
                        118,
                        -32,
                        -87,
                        -118,
                        127,
                        -107,
                        -89,
                        18,
                        -48,
                        -43,
                        7,
                        -25,
                        -117,
                        -87,
                        -111,
                        96,
                        125,
                        6,
                        34,
                        -109
                     };
                     this.decoyKeySlot10 = keccakSha3Digest10.digest(VendorAesHelper.encryptWithCipher(bytes, bytes39, paddedAesCipher));
                     l = (l ^ -3893439879994986940L) + -4858995473568056355L;
                  } else {
                     if (41 < b) {
                        KeccakSha3Digest keccakSha3Digest11 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes43 = new byte[]{
                           18,
                           47,
                           19,
                           18,
                           -16,
                           7,
                           61,
                           -90,
                           125,
                           -8,
                           7,
                           -38,
                           86,
                           23,
                           124,
                           4,
                           109,
                           96,
                           17,
                           -39,
                           94,
                           -111,
                           117,
                           95,
                           62,
                           49,
                           9,
                           -127,
                           -111,
                           66,
                           75,
                           -67
                        };
                        this.decoyKeySlot11 = keccakSha3Digest11.digest(VendorAesHelper.encryptWithCipher(bytes, bytes43, paddedAesCipher));
                        l = (l ^ 6542844735656830398L) + 86883457135719630L;
                        continue;
                     }

                     KeccakSha3Digest keccakSha3Digest12 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes47 = new byte[]{
                        19,
                        -52,
                        78,
                        -4,
                        -121,
                        83,
                        125,
                        -28,
                        90,
                        -17,
                        -79,
                        -109,
                        -38,
                        41,
                        -35,
                        105,
                        -111,
                        101,
                        44,
                        -114,
                        42,
                        105,
                        27,
                        -95,
                        90,
                        123,
                        -77,
                        69,
                        54,
                        12,
                        -50,
                        105
                     };
                     this.decoyKeySlot12 = keccakSha3Digest12.digest(VendorAesHelper.encryptWithCipher(bytes, bytes47, paddedAesCipher));
                     l = (l ^ 4893343907409322227L) + 571422977524420658L;
                  }
               } else {
                  if (b <= 38) {
                     KeccakSha3Digest keccakSha3Digest13 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes51 = new byte[]{
                        96,
                        -54,
                        -31,
                        -127,
                        -34,
                        93,
                        -29,
                        -40,
                        30,
                        92,
                        -102,
                        -37,
                        -85,
                        -39,
                        -31,
                        -114,
                        112,
                        -90,
                        -35,
                        -100,
                        11,
                        13,
                        108,
                        16,
                        -17,
                        -47,
                        -122,
                        82,
                        3,
                        -38,
                        36,
                        93
                     };
                     this.decoyKeySlot13 = keccakSha3Digest13.digest(VendorAesHelper.encryptWithCipher(bytes, bytes51, paddedAesCipher));
                     l = l - -6165185413905892208L ^ 5207764867792060163L;
                     continue;
                  }

                  KeccakSha3Digest keccakSha3Digest14 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes55 = new byte[]{
                     106,
                     72,
                     -77,
                     21,
                     -92,
                     -49,
                     106,
                     11,
                     13,
                     -11,
                     -23,
                     57,
                     -112,
                     124,
                     -47,
                     32,
                     -28,
                     29,
                     -53,
                     62,
                     -115,
                     14,
                     80,
                     60,
                     -107,
                     65,
                     -85,
                     108,
                     94,
                     27,
                     77,
                     2
                  };
                  this.decoyKeySlot14 = keccakSha3Digest14.digest(VendorAesHelper.encryptWithCipher(bytes, bytes55, paddedAesCipher));
                  l = (l ^ -1562427872457366642L) - -4457343154285869678L;
               }
            } else if (b >= 30) {
               if (30 >= b) {
                  KeccakSha3Digest keccakSha3Digest15 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes59 = new byte[]{
                     17,
                     -26,
                     97,
                     -5,
                     -9,
                     -11,
                     116,
                     116,
                     77,
                     -104,
                     38,
                     -86,
                     -34,
                     -61,
                     -106,
                     71,
                     105,
                     -61,
                     76,
                     -98,
                     35,
                     -117,
                     90,
                     -75,
                     92,
                     -63,
                     110,
                     -66,
                     19,
                     53,
                     -116,
                     -78
                  };
                  this.decoyKeySlot15 = keccakSha3Digest15.digest(VendorAesHelper.encryptWithCipher(bytes, bytes59, paddedAesCipher));
                  l = (l ^ 4321422541465179578L) - -8898228943933747194L;
                  continue;
               }

               if (b >= 33) {
                  if (33 >= b) {
                     KeccakSha3Digest keccakSha3Digest16 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes63 = new byte[]{
                        64,
                        105,
                        12,
                        -82,
                        73,
                        -81,
                        28,
                        -120,
                        12,
                        -117,
                        1,
                        124,
                        -120,
                        -87,
                        61,
                        117,
                        -11,
                        -46,
                        -95,
                        93,
                        42,
                        -34,
                        -4,
                        -100,
                        -22,
                        98,
                        -125,
                        -100,
                        -103,
                        105,
                        -26,
                        90
                     };
                     this.decoyKeySlot16 = keccakSha3Digest16.digest(VendorAesHelper.encryptWithCipher(bytes, bytes63, paddedAesCipher));
                     l = (l ^ 1532613602239891457L) + -491099321910349490L;
                  } else if (b >= 35) {
                     if (35 >= b) {
                        KeccakSha3Digest keccakSha3Digest17 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes67 = new byte[]{
                           118,
                           -107,
                           25,
                           -113,
                           -75,
                           119,
                           56,
                           -37,
                           20,
                           79,
                           3,
                           -39,
                           116,
                           -66,
                           57,
                           -72,
                           -77,
                           119,
                           -101,
                           -117,
                           54,
                           55,
                           -105,
                           86,
                           77,
                           -92,
                           -109,
                           117,
                           -7,
                           63,
                           -65,
                           95
                        };
                        this.decoyKeySlot17 = keccakSha3Digest17.digest(VendorAesHelper.encryptWithCipher(bytes, bytes67, paddedAesCipher));
                        l = (l ^ -2142145431271001226L) - -7018993100115102089L;
                     } else {
                        paddedAesCipher = new PaddedAesCipher();
                        l = l - -2979600417042522597L ^ -8606559286800795001L;
                     }
                  } else {
                     KeccakSha3Digest keccakSha3Digest18 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes71 = new byte[]{
                        -45,
                        -112,
                        -30,
                        -69,
                        43,
                        34,
                        34,
                        -12,
                        -34,
                        -70,
                        -94,
                        8,
                        -49,
                        -73,
                        3,
                        -85,
                        120,
                        -30,
                        -11,
                        -20,
                        101,
                        35,
                        -85,
                        -95,
                        -95,
                        -95,
                        -70,
                        -112,
                        -122,
                        -2,
                        -13,
                        -10
                     };
                     this.decoyKeySlot18 = keccakSha3Digest18.digest(VendorAesHelper.encryptWithCipher(bytes, bytes71, paddedAesCipher));
                     l = (l ^ 2969215216773415971L) - -1484236436284679110L;
                  }
                  continue;
               }

               if (31 >= b) {
                  KeccakSha3Digest keccakSha3Digest19 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes75 = new byte[]{
                     7,
                     11,
                     -127,
                     120,
                     97,
                     50,
                     -56,
                     2,
                     -127,
                     23,
                     124,
                     -109,
                     97,
                     86,
                     121,
                     79,
                     -53,
                     21,
                     -92,
                     64,
                     -8,
                     -53,
                     88,
                     -4,
                     -29,
                     -87,
                     -20,
                     -64,
                     32,
                     85,
                     -122,
                     -35
                  };
                  this.decoyKeySlot19 = keccakSha3Digest19.digest(VendorAesHelper.encryptWithCipher(bytes, bytes75, paddedAesCipher));
                  l = (l ^ 3208285072743951518L) - -2904873378462271291L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest20 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes79 = new byte[]{
                  36,
                  -11,
                  -3,
                  71,
                  5,
                  39,
                  -59,
                  20,
                  18,
                  77,
                  90,
                  -128,
                  108,
                  73,
                  -67,
                  -7,
                  -1,
                  31,
                  -113,
                  123,
                  -25,
                  -6,
                  -102,
                  -19,
                  -75,
                  67,
                  -36,
                  -86,
                  -45,
                  110,
                  11,
                  -84
               };
               this.decoyKeySlot20 = keccakSha3Digest20.digest(VendorAesHelper.encryptWithCipher(bytes, bytes79, paddedAesCipher));
               l = (l ^ -7184305074196790917L) + 3933887231948737757L;
            } else {
               if (b >= 27) {
                  if (b <= 27) {
                     KeccakSha3Digest keccakSha3Digest21 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes83 = new byte[]{
                        -90,
                        -99,
                        67,
                        -57,
                        -16,
                        -95,
                        16,
                        -22,
                        52,
                        -117,
                        -105,
                        50,
                        -9,
                        89,
                        -54,
                        -36,
                        -73,
                        122,
                        114,
                        -16,
                        -43,
                        -12,
                        50,
                        66,
                        122,
                        120,
                        -22,
                        -128,
                        39,
                        -95,
                        -102,
                        45
                     };
                     this.decoyKeySlot21 = keccakSha3Digest21.digest(VendorAesHelper.encryptWithCipher(bytes, bytes83, paddedAesCipher));
                     l = (l ^ -2518639547032834593L) - -4978229156784419782L;
                  } else if (b <= 28) {
                     KeccakSha3Digest keccakSha3Digest22 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes87 = new byte[]{
                        -66,
                        -26,
                        -90,
                        2,
                        -62,
                        89,
                        -63,
                        -33,
                        -63,
                        -53,
                        -10,
                        100,
                        -21,
                        -56,
                        82,
                        119,
                        -90,
                        107,
                        -68,
                        -123,
                        34,
                        51,
                        120,
                        -45,
                        9,
                        101,
                        -32,
                        105,
                        -39,
                        -78,
                        -32,
                        -77
                     };
                     this.decoyKeySlot22 = keccakSha3Digest22.digest(VendorAesHelper.encryptWithCipher(bytes, bytes87, paddedAesCipher));
                     l = (l ^ 4503478771475307592L) + -3552747469180481203L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest23 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes91 = new byte[]{
                        61,
                        -57,
                        -106,
                        116,
                        113,
                        -123,
                        -54,
                        -18,
                        46,
                        104,
                        -71,
                        88,
                        44,
                        84,
                        2,
                        35,
                        111,
                        -50,
                        -91,
                        -52,
                        -119,
                        21,
                        -85,
                        -14,
                        -46,
                        66,
                        -17,
                        -86,
                        113,
                        -13,
                        -122,
                        -123
                     };
                     this.decoyKeySlot23 = keccakSha3Digest23.digest(VendorAesHelper.encryptWithCipher(bytes, bytes91, paddedAesCipher));
                     l = (l ^ 423935537410908813L) + -1820928690391449613L;
                  }
                  continue;
               }

               if (25 >= b) {
                  KeccakSha3Digest keccakSha3Digest24 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes95 = new byte[]{
                     24,
                     60,
                     -107,
                     109,
                     43,
                     -24,
                     -50,
                     -26,
                     -51,
                     -109,
                     7,
                     -64,
                     121,
                     25,
                     19,
                     -45,
                     102,
                     -55,
                     45,
                     -5,
                     106,
                     -15,
                     53,
                     -16,
                     15,
                     60,
                     105,
                     -63,
                     124,
                     -32,
                     -39,
                     -119
                  };
                  this.decoyKeySlot24 = keccakSha3Digest24.digest(VendorAesHelper.encryptWithCipher(bytes, bytes95, paddedAesCipher));
                  l = (l ^ 3317910673237226627L) - -6296311944071116532L;
               } else {
                  KeccakSha3Digest keccakSha3Digest25 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes99 = new byte[]{
                     -20,
                     36,
                     -75,
                     37,
                     -113,
                     -26,
                     -63,
                     18,
                     113,
                     -67,
                     94,
                     55,
                     -78,
                     24,
                     88,
                     27,
                     3,
                     105,
                     -15,
                     -18,
                     -48,
                     97,
                     -68,
                     28,
                     35,
                     11,
                     127,
                     -52,
                     -86,
                     -54,
                     29,
                     -39
                  };
                  this.decoyKeySlot25 = keccakSha3Digest25.digest(VendorAesHelper.encryptWithCipher(bytes, bytes99, paddedAesCipher));
                  l = (l ^ 6463470187910931199L) - 4171039124431145570L;
               }
            }
         } else if (b >= 11) {
            if (b <= 11) {
               KeccakSha3Digest keccakSha3Digest26 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes103 = new byte[]{
                  13,
                  45,
                  -124,
                  -110,
                  -94,
                  50,
                  -111,
                  22,
                  -120,
                  -104,
                  65,
                  -79,
                  58,
                  117,
                  -86,
                  -99,
                  84,
                  56,
                  -61,
                  11,
                  -42,
                  119,
                  7,
                  22,
                  97,
                  7,
                  54,
                  16,
                  18,
                  51,
                  16,
                  24
               };
               this.decoyKeySlot26 = keccakSha3Digest26.digest(VendorAesHelper.encryptWithCipher(bytes, bytes103, paddedAesCipher));
               l = l - -2451435564529089408L ^ 8946472274710221823L;
               continue;
            }

            if (b >= 17) {
               if (17 >= b) {
                  KeccakSha3Digest keccakSha3Digest27 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes107 = new byte[]{
                     9,
                     37,
                     19,
                     13,
                     -79,
                     116,
                     70,
                     -123,
                     88,
                     126,
                     4,
                     91,
                     14,
                     102,
                     96,
                     90,
                     -56,
                     -5,
                     26,
                     90,
                     -66,
                     -121,
                     -107,
                     -68,
                     22,
                     120,
                     -98,
                     16,
                     -98,
                     -69,
                     37,
                     -58
                  };
                  this.decoyKeySlot27 = keccakSha3Digest27.digest(VendorAesHelper.encryptWithCipher(bytes, bytes107, paddedAesCipher));
                  l = l - 110397462100909427L ^ 4009078156459662384L;
               } else {
                  if (b < 20) {
                     if (18 >= b) {
                        KeccakSha3Digest keccakSha3Digest28 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes111 = new byte[]{
                           36,
                           89,
                           114,
                           -58,
                           15,
                           -123,
                           -117,
                           40,
                           31,
                           -125,
                           -2,
                           -12,
                           -4,
                           70,
                           -10,
                           -83,
                           -29,
                           53,
                           -63,
                           55,
                           -16,
                           67,
                           -17,
                           116,
                           28,
                           -62,
                           2,
                           76,
                           -49,
                           -54,
                           89,
                           -103
                        };
                        this.decoyKeySlot28 = keccakSha3Digest28.digest(VendorAesHelper.encryptWithCipher(bytes, bytes111, paddedAesCipher));
                        l = (l ^ -2807372640681015626L) + 8072080599518044901L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest29 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes115 = new byte[]{
                           -24,
                           -87,
                           43,
                           10,
                           83,
                           -70,
                           83,
                           38,
                           -61,
                           91,
                           -12,
                           -28,
                           -4,
                           91,
                           -73,
                           -37,
                           27,
                           45,
                           -106,
                           109,
                           75,
                           122,
                           -115,
                           31,
                           11,
                           18,
                           69,
                           94,
                           73,
                           -10,
                           -12,
                           -104
                        };
                        this.decoyKeySlot29 = keccakSha3Digest29.digest(VendorAesHelper.encryptWithCipher(bytes, bytes115, paddedAesCipher));
                        l = (l ^ -3919484893123748470L) + -7398787113077132913L;
                     }
                     continue;
                  }

                  if (20 >= b) {
                     KeccakSha3Digest keccakSha3Digest30 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes119 = new byte[]{
                        45,
                        45,
                        -107,
                        -95,
                        -104,
                        9,
                        88,
                        -88,
                        -97,
                        -90,
                        16,
                        -53,
                        24,
                        3,
                        57,
                        6,
                        91,
                        112,
                        125,
                        70,
                        21,
                        -60,
                        -4,
                        -41,
                        -44,
                        -83,
                        -92,
                        -40,
                        -58,
                        -10,
                        61,
                        -12
                     };
                     this.decoyKeySlot30 = keccakSha3Digest30.digest(VendorAesHelper.encryptWithCipher(bytes, bytes119, paddedAesCipher));
                     l = l + -1900329101732572598L ^ 1801506683526768404L;
                  } else if (22 <= b) {
                     if (22 < b) {
                        KeccakSha3Digest keccakSha3Digest31 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes123 = new byte[]{
                           46,
                           -109,
                           -107,
                           3,
                           -71,
                           88,
                           -38,
                           2,
                           -62,
                           -113,
                           114,
                           38,
                           -104,
                           55,
                           12,
                           72,
                           102,
                           115,
                           123,
                           -112,
                           78,
                           -66,
                           -121,
                           24,
                           -31,
                           11,
                           107,
                           37,
                           -78,
                           84,
                           -39,
                           -15
                        };
                        this.decoyKeySlot31 = keccakSha3Digest31.digest(VendorAesHelper.encryptWithCipher(bytes, bytes123, paddedAesCipher));
                        l = (l ^ 8130220563127137374L) - -3596503012643134716L;
                        continue;
                     }

                     KeccakSha3Digest keccakSha3Digest32 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes127 = new byte[]{
                        120,
                        120,
                        38,
                        55,
                        -36,
                        -18,
                        -99,
                        -48,
                        35,
                        -125,
                        15,
                        -55,
                        47,
                        119,
                        -61,
                        -116,
                        -127,
                        -3,
                        -42,
                        -8,
                        -114,
                        118,
                        -119,
                        -16,
                        88,
                        15,
                        109,
                        91,
                        123,
                        109,
                        -1,
                        44
                     };
                     this.decoyKeySlot32 = keccakSha3Digest32.digest(VendorAesHelper.encryptWithCipher(bytes, bytes127, paddedAesCipher));
                     l = l - 409188718145313024L ^ 8519898873573627031L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest33 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes131 = new byte[]{
                        41,
                        -86,
                        22,
                        100,
                        96,
                        -106,
                        -60,
                        -56,
                        40,
                        -110,
                        -28,
                        83,
                        -42,
                        -29,
                        -29,
                        -121,
                        103,
                        -124,
                        99,
                        -46,
                        80,
                        -39,
                        -68,
                        25,
                        7,
                        53,
                        -66,
                        -76,
                        75,
                        126,
                        28,
                        -90
                     };
                     this.decoyKeySlot33 = keccakSha3Digest33.digest(VendorAesHelper.encryptWithCipher(bytes, bytes131, paddedAesCipher));
                     l = (l ^ -6721010099707373536L) + -6566097175632757300L;
                  }
               }
            } else {
               if (b < 14) {
                  if (b <= 12) {
                     KeccakSha3Digest keccakSha3Digest34 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes135 = new byte[]{
                        66,
                        -26,
                        51,
                        -93,
                        2,
                        -67,
                        -65,
                        93,
                        56,
                        -111,
                        -12,
                        109,
                        77,
                        85,
                        75,
                        -11,
                        77,
                        114,
                        101,
                        93,
                        88,
                        -93,
                        51,
                        -92,
                        -91,
                        -35,
                        -110,
                        18,
                        127,
                        -116,
                        126,
                        -21
                     };
                     this.decoyKeySlot34 = keccakSha3Digest34.digest(VendorAesHelper.encryptWithCipher(bytes, bytes135, paddedAesCipher));
                     l = l + 7655260353175038808L ^ -5590257945173628554L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest35 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes139 = new byte[]{
                        -45,
                        -30,
                        -105,
                        49,
                        -87,
                        80,
                        1,
                        45,
                        0,
                        -120,
                        122,
                        -61,
                        80,
                        7,
                        46,
                        77,
                        -69,
                        70,
                        -3,
                        -89,
                        -6,
                        8,
                        33,
                        118,
                        16,
                        83,
                        -85,
                        79,
                        -20,
                        45,
                        -102,
                        -118
                     };
                     this.decoyKeySlot35 = keccakSha3Digest35.digest(VendorAesHelper.encryptWithCipher(bytes, bytes139, paddedAesCipher));
                     l = l + 4343670730110557714L ^ 9127858748431561545L;
                  }
                  continue;
               }

               if (14 >= b) {
                  KeccakSha3Digest keccakSha3Digest36 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes143 = new byte[]{
                     116,
                     20,
                     12,
                     114,
                     107,
                     117,
                     -31,
                     -119,
                     69,
                     2,
                     -86,
                     -73,
                     -13,
                     -120,
                     -10,
                     -92,
                     -123,
                     101,
                     -70,
                     13,
                     89,
                     -118,
                     56,
                     -21,
                     -65,
                     -69,
                     31,
                     -91,
                     81,
                     24,
                     -46,
                     41
                  };
                  this.decoyKeySlot36 = keccakSha3Digest36.digest(VendorAesHelper.encryptWithCipher(bytes, bytes143, paddedAesCipher));
                  l = l + 9144531553326299988L ^ -7430467674368807514L;
                  continue;
               }

               if (15 >= b) {
                  KeccakSha3Digest keccakSha3Digest37 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes147 = new byte[]{
                     83,
                     120,
                     70,
                     13,
                     -72,
                     100,
                     -37,
                     93,
                     -50,
                     -21,
                     -44,
                     18,
                     -80,
                     41,
                     18,
                     -59,
                     100,
                     55,
                     11,
                     29,
                     20,
                     -1,
                     -128,
                     67,
                     -80,
                     68,
                     -45,
                     39,
                     74,
                     69,
                     -122,
                     48
                  };
                  this.decoyKeySlot37 = keccakSha3Digest37.digest(VendorAesHelper.encryptWithCipher(bytes, bytes147, paddedAesCipher));
                  l = (l ^ 2659682750569845579L) + -3842660886632532553L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest38 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes151 = new byte[]{
                  95,
                  -113,
                  13,
                  57,
                  -76,
                  -35,
                  71,
                  -83,
                  -42,
                  -127,
                  73,
                  -45,
                  -103,
                  115,
                  69,
                  18,
                  -30,
                  85,
                  -9,
                  109,
                  -73,
                  113,
                  69,
                  51,
                  68,
                  109,
                  -96,
                  120,
                  -7,
                  111,
                  91,
                  66
               };
               this.decoyKeySlot38 = keccakSha3Digest38.digest(VendorAesHelper.encryptWithCipher(bytes, bytes151, paddedAesCipher));
               l = (l ^ -1697914019208804055L) - 6172801172049142906L;
            }
         } else if (5 <= b) {
            if (b <= 5) {
               KeccakSha3Digest keccakSha3Digest39 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes155 = new byte[]{
                  85,
                  -31,
                  -32,
                  -41,
                  -83,
                  -89,
                  -107,
                  -27,
                  118,
                  -8,
                  82,
                  109,
                  80,
                  -17,
                  -18,
                  -64,
                  -77,
                  37,
                  119,
                  5,
                  -116,
                  -88,
                  -90,
                  -121,
                  47,
                  -108,
                  33,
                  -111,
                  36,
                  67,
                  26,
                  80
               };
               this.decoyKeySlot39 = keccakSha3Digest39.digest(VendorAesHelper.encryptWithCipher(bytes, bytes155, paddedAesCipher));
               l = (l ^ -5029148139513305373L) + 7605288965253048361L;
            } else if (b >= 8) {
               if (b <= 8) {
                  KeccakSha3Digest keccakSha3Digest40 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes159 = new byte[]{
                     97,
                     -9,
                     46,
                     -2,
                     -3,
                     -3,
                     -81,
                     53,
                     111,
                     -125,
                     -122,
                     115,
                     35,
                     -43,
                     -22,
                     37,
                     -90,
                     8,
                     19,
                     -35,
                     3,
                     22,
                     5,
                     17,
                     -59,
                     -66,
                     -89,
                     51,
                     -38,
                     -93,
                     23,
                     -21
                  };
                  this.decoyKeySlot40 = keccakSha3Digest40.digest(VendorAesHelper.encryptWithCipher(bytes, bytes159, paddedAesCipher));
                  l = l - 9071383073451186497L ^ -8623227599859123561L;
               } else if (9 >= b) {
                  KeccakSha3Digest keccakSha3Digest41 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes163 = new byte[]{
                     96,
                     3,
                     -13,
                     3,
                     69,
                     11,
                     60,
                     -38,
                     36,
                     58,
                     79,
                     -100,
                     -41,
                     -46,
                     -58,
                     116,
                     127,
                     125,
                     71,
                     105,
                     113,
                     -101,
                     -12,
                     3,
                     85,
                     30,
                     -40,
                     -68,
                     -68,
                     33,
                     29,
                     -82
                  };
                  this.decoyKeySlot41 = keccakSha3Digest41.digest(VendorAesHelper.encryptWithCipher(bytes, bytes163, paddedAesCipher));
                  l = l + -6565082610685085473L ^ 4297766437509820618L;
               } else {
                  KeccakSha3Digest keccakSha3Digest42 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes167 = new byte[]{
                     -118,
                     -37,
                     -50,
                     104,
                     127,
                     -62,
                     -18,
                     73,
                     -47,
                     26,
                     58,
                     -93,
                     57,
                     57,
                     12,
                     -115,
                     -82,
                     97,
                     27,
                     23,
                     8,
                     -60,
                     82,
                     -96,
                     -119,
                     -109,
                     82,
                     -76,
                     -102,
                     78,
                     -7,
                     51
                  };
                  this.decoyKeySlot42 = keccakSha3Digest42.digest(VendorAesHelper.encryptWithCipher(bytes, bytes167, paddedAesCipher));
                  l = l + -749406212679545322L ^ 7594883115338236730L;
               }
            } else {
               if (b <= 6) {
                  KeccakSha3Digest keccakSha3Digest43 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes171 = new byte[]{
                     -11,
                     43,
                     -30,
                     -65,
                     2,
                     -71,
                     43,
                     -60,
                     110,
                     94,
                     75,
                     86,
                     1,
                     113,
                     -13,
                     1,
                     75,
                     23,
                     107,
                     40,
                     -13,
                     -44,
                     124,
                     -87,
                     86,
                     25,
                     -78,
                     11,
                     -78,
                     61,
                     105,
                     86
                  };
                  this.decoyKeySlot43 = keccakSha3Digest43.digest(VendorAesHelper.encryptWithCipher(bytes, bytes171, paddedAesCipher));
                  l = l - 407302774948780279L ^ -3566843610659280382L;
                  continue;
               }

               KeccakSha3Digest keccakSha3Digest44 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes177 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
                  9, -111, 26, 18, -59, 55, -97, -95, 20, 51, 13, 97, 107, -50, -51, 6, -99, 34, -79, 24, -108, 16, 77, 63, -95, -95, 63, 125, 72, -10, 23, -13
               }, paddedAesCipher);
               this.decoyKeySlot44 = keccakSha3Digest44.digest(bytes177);
               l = l + 8140761138090997642L ^ -6321334082742547837L;
            }
         } else if (2 <= b) {
            if (b <= 2) {
               KeccakSha3Digest keccakSha3Digest45 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes179 = new byte[]{
                  6,
                  -30,
                  112,
                  1,
                  -88,
                  84,
                  103,
                  97,
                  127,
                  -62,
                  -77,
                  3,
                  120,
                  -30,
                  -121,
                  -67,
                  -125,
                  -29,
                  66,
                  -97,
                  -110,
                  9,
                  -94,
                  58,
                  -103,
                  -49,
                  108,
                  78,
                  -96,
                  82,
                  -75,
                  -56
               };
               this.decoyKeySlot45 = keccakSha3Digest45.digest(VendorAesHelper.encryptWithCipher(bytes, bytes179, paddedAesCipher));
               l = l - -2083719393262731769L ^ 2453596786904166097L;
               continue;
            }

            if (3 < b) {
               KeccakSha3Digest keccakSha3Digest46 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes183 = new byte[]{
                  7,
                  -99,
                  -14,
                  -29,
                  76,
                  36,
                  -63,
                  69,
                  -22,
                  9,
                  40,
                  -38,
                  -90,
                  -33,
                  67,
                  41,
                  -1,
                  -55,
                  -93,
                  -75,
                  91,
                  19,
                  69,
                  -4,
                  -43,
                  108,
                  106,
                  -112,
                  -127,
                  47,
                  20,
                  -15
               };
               this.decoyKeySlot46 = keccakSha3Digest46.digest(VendorAesHelper.encryptWithCipher(bytes, bytes183, paddedAesCipher));
               l = l - 9192494022148688184L ^ -5838367061849216529L;
               continue;
            }

            KeccakSha3Digest keccakSha3Digest47 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes189 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
               -87, -112, 68, 120, -35, 123, 111, 79, 124, 89, -43, -91, -6, -95, 69, -107, 20, -59, -90, -53, 35, -93, 62, 34, -36, 77, 19, -64, 28, 6, 36, 8
            }, paddedAesCipher);
            this.decoyKeySlot47 = keccakSha3Digest47.digest(bytes189);
            l = l - 6167082612985468797L ^ 8198093657590204292L;
         } else if (b <= 0) {
            KeccakSha3Digest keccakSha3Digest48 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes191 = new byte[]{
               63,
               -14,
               122,
               2,
               6,
               -88,
               -63,
               -123,
               -37,
               -13,
               -46,
               -28,
               -15,
               -51,
               66,
               -43,
               -43,
               15,
               66,
               60,
               33,
               -60,
               -117,
               -35,
               73,
               13,
               68,
               15,
               -79,
               50,
               -96,
               -58
            };
            this.decoyKeySlot48 = keccakSha3Digest48.digest(VendorAesHelper.encryptWithCipher(bytes, bytes191, paddedAesCipher));
            l = l + 4044679493248547379L ^ 6140635849655358188L;
         } else {
            KeccakSha3Digest keccakSha3Digest49 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
            byte[] bytes195 = new byte[]{
               -32,
               85,
               -98,
               -83,
               111,
               -18,
               -71,
               -67,
               98,
               38,
               -117,
               81,
               108,
               103,
               -66,
               15,
               -64,
               -16,
               6,
               -108,
               -114,
               59,
               49,
               -93,
               53,
               35,
               117,
               92,
               105,
               -128,
               -42,
               15
            };
            this.decoyKeySlot49 = keccakSha3Digest49.digest(VendorAesHelper.encryptWithCipher(bytes, bytes195, paddedAesCipher));
            l = (l ^ 459654855958842292L) - -1093373110523113706L;
         }

         while (b > 50) {
            if (76 <= b) {
               if (76 >= b) {
                  KeccakSha3Digest keccakSha3Digest50 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes199 = new byte[]{
                     125,
                     122,
                     110,
                     -71,
                     -53,
                     -59,
                     23,
                     33,
                     62,
                     -28,
                     -27,
                     -73,
                     -44,
                     -19,
                     91,
                     -42,
                     -53,
                     28,
                     99,
                     27,
                     32,
                     -22,
                     87,
                     -126,
                     -66,
                     -55,
                     122,
                     55,
                     19,
                     -119,
                     -88,
                     -25
                  };
                  this.decoyKeySlot50 = keccakSha3Digest50.digest(VendorAesHelper.encryptWithCipher(bytes, bytes199, paddedAesCipher));
                  l = l - -945426091198833544L ^ 8244936440846298720L;
               } else if (89 <= b) {
                  if (b <= 89) {
                     KeccakSha3Digest keccakSha3Digest51 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes203 = new byte[]{
                        85,
                        117,
                        119,
                        39,
                        -108,
                        89,
                        100,
                        16,
                        -16,
                        102,
                        115,
                        28,
                        69,
                        87,
                        70,
                        28,
                        80,
                        67,
                        13,
                        -50,
                        -94,
                        92,
                        -120,
                        -37,
                        -76,
                        -68,
                        -104,
                        -95,
                        -124,
                        -126,
                        -57,
                        73
                     };
                     this.decoyKeySlot51 = keccakSha3Digest51.digest(VendorAesHelper.encryptWithCipher(bytes, bytes203, paddedAesCipher));
                     l = l + 5522352417610726531L ^ -4479692571602416610L;
                     continue outerLoop;
                  }

                  if (95 <= b) {
                     if (95 >= b) {
                        KeccakSha3Digest keccakSha3Digest52 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes207 = new byte[]{
                           -110,
                           14,
                           114,
                           -96,
                           15,
                           49,
                           -106,
                           0,
                           62,
                           80,
                           -18,
                           90,
                           74,
                           110,
                           117,
                           20,
                           -18,
                           -124,
                           106,
                           50,
                           -56,
                           112,
                           -24,
                           -67,
                           16,
                           4,
                           35,
                           19,
                           100,
                           -52,
                           -112,
                           -39
                        };
                        this.decoyKeySlot52 = keccakSha3Digest52.digest(VendorAesHelper.encryptWithCipher(bytes, bytes207, paddedAesCipher));
                        l = (l ^ 9149767583971577217L) - 2001400528394407019L;
                        continue outerLoop;
                     }

                     if (b >= 98) {
                        if (b <= 98) {
                           KeccakSha3Digest keccakSha3Digest53 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes211 = new byte[]{
                              47,
                              117,
                              -31,
                              -107,
                              -29,
                              -124,
                              13,
                              -83,
                              8,
                              -111,
                              -102,
                              101,
                              58,
                              -115,
                              -15,
                              38,
                              127,
                              -10,
                              -54,
                              -57,
                              -30,
                              13,
                              75,
                              82,
                              -106,
                              108,
                              -52,
                              121,
                              24,
                              81,
                              -41,
                              -59
                           };
                           this.decoyKeySlot53 = keccakSha3Digest53.digest(VendorAesHelper.encryptWithCipher(bytes, bytes211, paddedAesCipher));
                           l = l - 3228902231319292301L ^ 5588923515247538820L;
                        } else {
                           if (b < 100) {
                              KeccakSha3Digest keccakSha3Digest54 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                              byte[] bytes215 = new byte[]{
                                 88,
                                 93,
                                 109,
                                 -72,
                                 -96,
                                 3,
                                 44,
                                 -10,
                                 -48,
                                 -77,
                                 93,
                                 98,
                                 -36,
                                 115,
                                 113,
                                 -19,
                                 100,
                                 50,
                                 43,
                                 24,
                                 -17,
                                 54,
                                 -22,
                                 127,
                                 -51,
                                 -66,
                                 46,
                                 55,
                                 84,
                                 -79,
                                 44,
                                 -111
                              };
                              this.decoyKeySlot54 = keccakSha3Digest54.digest(VendorAesHelper.encryptWithCipher(bytes, bytes215, paddedAesCipher));
                              l = l - 4398399842183414859L ^ -1651689633316355049L;
                              continue outerLoop;
                           }

                           if (100 < b) {
                              KeccakSha3Digest keccakSha3Digest55 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                              byte[] bytes219 = new byte[]{
                                 -115,
                                 41,
                                 -16,
                                 -57,
                                 84,
                                 -92,
                                 -101,
                                 -56,
                                 103,
                                 66,
                                 -103,
                                 102,
                                 -126,
                                 32,
                                 -69,
                                 -90,
                                 -126,
                                 31,
                                 4,
                                 -11,
                                 -102,
                                 77,
                                 -3,
                                 -90,
                                 12,
                                 126,
                                 -58,
                                 86,
                                 69,
                                 -95,
                                 70,
                                 67
                              };
                              this.decoyKeySlot55 = keccakSha3Digest55.digest(VendorAesHelper.encryptWithCipher(bytes, bytes219, paddedAesCipher));
                              l = (l ^ -7291377338939210893L) + -6965390008472469923L;
                              continue outerLoop;
                           }

                           KeccakSha3Digest keccakSha3Digest56 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes223 = new byte[]{
                              114,
                              36,
                              74,
                              91,
                              -19,
                              -22,
                              102,
                              -91,
                              -100,
                              -27,
                              -18,
                              -72,
                              119,
                              100,
                              -51,
                              54,
                              100,
                              76,
                              6,
                              -118,
                              -53,
                              10,
                              -98,
                              -10,
                              58,
                              -77,
                              20,
                              70,
                              116,
                              73,
                              -114,
                              56
                           };
                           this.decoyKeySlot56 = keccakSha3Digest56.digest(VendorAesHelper.encryptWithCipher(bytes, bytes223, paddedAesCipher));
                           l = l - 8510397492967398503L ^ -6218105504266184151L;
                        }
                     } else {
                        if (b <= 96) {
                           KeccakSha3Digest keccakSha3Digest57 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes227 = new byte[]{
                              77,
                              -73,
                              80,
                              -112,
                              -64,
                              -40,
                              12,
                              -128,
                              51,
                              -108,
                              93,
                              1,
                              36,
                              115,
                              -65,
                              31,
                              114,
                              -109,
                              8,
                              -30,
                              57,
                              43,
                              -98,
                              -127,
                              88,
                              94,
                              -46,
                              119,
                              -60,
                              26,
                              113,
                              -35
                           };
                           this.decoyKeySlot57 = keccakSha3Digest57.digest(VendorAesHelper.encryptWithCipher(bytes, bytes227, paddedAesCipher));
                           l = l + 3660839041687911916L ^ 5479202462647288960L;
                           continue outerLoop;
                        }

                        KeccakSha3Digest keccakSha3Digest58 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes231 = new byte[]{
                           -9,
                           75,
                           -37,
                           46,
                           18,
                           -116,
                           2,
                           118,
                           -20,
                           17,
                           -127,
                           125,
                           -105,
                           124,
                           -38,
                           -30,
                           -62,
                           -79,
                           -42,
                           -9,
                           97,
                           20,
                           -29,
                           -34,
                           -55,
                           37,
                           -100,
                           59,
                           71,
                           -69,
                           94,
                           -29
                        };
                        this.decoyKeySlot58 = keccakSha3Digest58.digest(VendorAesHelper.encryptWithCipher(bytes, bytes231, paddedAesCipher));
                        l = (l ^ -406765921670926750L) + -8286687918534149552L;
                     }
                  } else if (92 <= b) {
                     if (92 >= b) {
                        KeccakSha3Digest keccakSha3Digest59 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes235 = new byte[]{
                           54,
                           45,
                           -106,
                           -21,
                           111,
                           15,
                           13,
                           19,
                           -77,
                           -117,
                           -71,
                           -89,
                           -118,
                           92,
                           122,
                           -7,
                           53,
                           -51,
                           -43,
                           -15,
                           91,
                           -103,
                           121,
                           79,
                           -13,
                           5,
                           -74,
                           34,
                           -52,
                           -5,
                           -27,
                           75
                        };
                        this.decoyKeySlot59 = keccakSha3Digest59.digest(VendorAesHelper.encryptWithCipher(bytes, bytes235, paddedAesCipher));
                        l = (l ^ -3191840993018617525L) + -2384277721318016654L;
                     } else {
                        if (93 >= b) {
                           KeccakSha3Digest keccakSha3Digest60 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes239 = new byte[]{
                              76,
                              -36,
                              -14,
                              85,
                              103,
                              13,
                              -90,
                              84,
                              52,
                              120,
                              7,
                              18,
                              -11,
                              61,
                              104,
                              35,
                              -112,
                              115,
                              -51,
                              -117,
                              30,
                              -10,
                              -20,
                              -72,
                              10,
                              -14,
                              -25,
                              -40,
                              -23,
                              124,
                              -98,
                              100
                           };
                           this.decoyKeySlot60 = keccakSha3Digest60.digest(VendorAesHelper.encryptWithCipher(bytes, bytes239, paddedAesCipher));
                           l = (l ^ 3669972108836751282L) + -3455299026138217661L;
                           continue outerLoop;
                        }

                        KeccakSha3Digest keccakSha3Digest61 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes243 = new byte[]{
                           121,
                           -4,
                           4,
                           97,
                           -28,
                           -124,
                           93,
                           -81,
                           122,
                           -17,
                           76,
                           6,
                           24,
                           83,
                           -23,
                           -45,
                           -41,
                           62,
                           -106,
                           23,
                           -48,
                           17,
                           -125,
                           96,
                           89,
                           127,
                           -11,
                           61,
                           -82,
                           127,
                           91,
                           -88
                        };
                        this.decoyKeySlot61 = keccakSha3Digest61.digest(VendorAesHelper.encryptWithCipher(bytes, bytes243, paddedAesCipher));
                        l = (l ^ -8757216269173514471L) - -7792835671530488954L;
                     }
                  } else {
                     if (90 < b) {
                        KeccakSha3Digest keccakSha3Digest62 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes247 = new byte[]{
                           -122,
                           43,
                           -72,
                           -56,
                           -86,
                           16,
                           -122,
                           26,
                           96,
                           -98,
                           29,
                           -61,
                           27,
                           74,
                           54,
                           80,
                           65,
                           69,
                           -58,
                           -41,
                           1,
                           -75,
                           67,
                           108,
                           64,
                           46,
                           48,
                           42,
                           -49,
                           -124,
                           103,
                           -87
                        };
                        this.decoyKeySlot62 = keccakSha3Digest62.digest(VendorAesHelper.encryptWithCipher(bytes, bytes247, paddedAesCipher));
                        l = l - -62072571079727799L ^ 8734325011221519760L;
                        continue outerLoop;
                     }

                     KeccakSha3Digest keccakSha3Digest63 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes251 = new byte[]{
                        -97,
                        54,
                        -20,
                        -52,
                        116,
                        86,
                        -88,
                        20,
                        97,
                        -51,
                        -45,
                        -19,
                        -111,
                        -10,
                        12,
                        -36,
                        11,
                        64,
                        30,
                        -14,
                        -101,
                        -90,
                        -99,
                        -85,
                        115,
                        89,
                        112,
                        -1,
                        -73,
                        -68,
                        114,
                        54
                     };
                     this.decoyKeySlot63 = keccakSha3Digest63.digest(VendorAesHelper.encryptWithCipher(bytes, bytes251, paddedAesCipher));
                     l = l - -9187116350119271074L ^ -5120459850512249757L;
                  }
               } else if (82 <= b) {
                  if (b <= 82) {
                     KeccakSha3Digest keccakSha3Digest64 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes255 = new byte[]{
                        52,
                        78,
                        -37,
                        -110,
                        81,
                        56,
                        8,
                        -66,
                        -105,
                        -68,
                        -15,
                        82,
                        122,
                        60,
                        83,
                        120,
                        -19,
                        98,
                        -112,
                        18,
                        64,
                        -23,
                        0,
                        116,
                        -114,
                        -97,
                        -117,
                        48,
                        -97,
                        -26,
                        -49,
                        -89
                     };
                     this.decoyKeySlot64 = keccakSha3Digest64.digest(VendorAesHelper.encryptWithCipher(bytes, bytes255, paddedAesCipher));
                     l = (l ^ -8719073705153031677L) - -3808601601124380481L;
                  } else if (b >= 85) {
                     if (85 >= b) {
                        KeccakSha3Digest keccakSha3Digest65 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes259 = new byte[]{
                           -70,
                           4,
                           31,
                           115,
                           -67,
                           79,
                           -42,
                           -78,
                           12,
                           110,
                           -99,
                           -76,
                           104,
                           95,
                           120,
                           59,
                           -29,
                           -117,
                           50,
                           -41,
                           -93,
                           -123,
                           30,
                           -11,
                           -70,
                           110,
                           9,
                           53,
                           -71,
                           -9,
                           -41,
                           94
                        };
                        this.decoyKeySlot65 = keccakSha3Digest65.digest(VendorAesHelper.encryptWithCipher(bytes, bytes259, paddedAesCipher));
                        l = l + -1601303969163886138L ^ 1149516813588887269L;
                     } else {
                        if (b < 87) {
                           KeccakSha3Digest keccakSha3Digest66 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes263 = new byte[]{
                              -95,
                              -58,
                              1,
                              -36,
                              4,
                              123,
                              33,
                              105,
                              3,
                              -11,
                              99,
                              87,
                              -25,
                              30,
                              -13,
                              -60,
                              21,
                              -21,
                              26,
                              78,
                              -101,
                              -25,
                              -35,
                              84,
                              -122,
                              40,
                              -124,
                              81,
                              76,
                              -55,
                              -36,
                              74
                           };
                           this.decoyKeySlot66 = keccakSha3Digest66.digest(VendorAesHelper.encryptWithCipher(bytes, bytes263, paddedAesCipher));
                           l = (l ^ -7902427178399481516L) + 8910317915512412501L;
                           continue outerLoop;
                        }

                        if (87 >= b) {
                           KeccakSha3Digest keccakSha3Digest67 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes267 = new byte[]{
                              127,
                              23,
                              103,
                              -101,
                              -58,
                              74,
                              -78,
                              82,
                              -45,
                              30,
                              -82,
                              36,
                              51,
                              -65,
                              53,
                              105,
                              -35,
                              -93,
                              -100,
                              15,
                              -37,
                              -99,
                              47,
                              -59,
                              -58,
                              92,
                              71,
                              -128,
                              69,
                              41,
                              -28,
                              78
                           };
                           this.decoyKeySlot67 = keccakSha3Digest67.digest(VendorAesHelper.encryptWithCipher(bytes, bytes267, paddedAesCipher));
                           l = l - -6326411582749954091L ^ -2180109640713172485L;
                        } else {
                           KeccakSha3Digest keccakSha3Digest68 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                           byte[] bytes271 = new byte[]{
                              48,
                              -61,
                              -99,
                              -47,
                              -39,
                              39,
                              -33,
                              105,
                              57,
                              19,
                              -93,
                              115,
                              75,
                              18,
                              43,
                              23,
                              82,
                              -68,
                              22,
                              114,
                              107,
                              96,
                              -70,
                              -89,
                              121,
                              -53,
                              -108,
                              -71,
                              110,
                              75,
                              93,
                              -112
                           };
                           this.decoyKeySlot68 = keccakSha3Digest68.digest(VendorAesHelper.encryptWithCipher(bytes, bytes271, paddedAesCipher));
                           l = (l ^ 5911535300311200030L) - 8192541706426941377L;
                        }
                     }
                  } else if (b <= 83) {
                     KeccakSha3Digest keccakSha3Digest69 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes275 = new byte[]{
                        31,
                        -99,
                        6,
                        -5,
                        111,
                        -51,
                        14,
                        -23,
                        24,
                        -70,
                        -68,
                        -85,
                        103,
                        96,
                        21,
                        55,
                        41,
                        35,
                        24,
                        112,
                        76,
                        38,
                        -81,
                        -27,
                        -29,
                        -29,
                        -66,
                        -95,
                        68,
                        12,
                        27,
                        -107
                     };
                     this.decoyKeySlot69 = keccakSha3Digest69.digest(VendorAesHelper.encryptWithCipher(bytes, bytes275, paddedAesCipher));
                     l = l - 5078003086483344914L ^ -7436897581444929157L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest70 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes279 = new byte[]{
                        110,
                        113,
                        91,
                        71,
                        20,
                        117,
                        -116,
                        99,
                        41,
                        65,
                        -40,
                        96,
                        61,
                        53,
                        47,
                        -80,
                        -96,
                        51,
                        -52,
                        -56,
                        -38,
                        -58,
                        -93,
                        -19,
                        54,
                        -22,
                        -11,
                        0,
                        -98,
                        63,
                        -67,
                        -94
                     };
                     this.decoyKeySlot70 = keccakSha3Digest70.digest(VendorAesHelper.encryptWithCipher(bytes, bytes279, paddedAesCipher));
                     l = l + -5018030444266095452L ^ -8087709000094592984L;
                  }
               } else if (79 <= b) {
                  if (b <= 79) {
                     KeccakSha3Digest keccakSha3Digest71 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes283 = new byte[]{
                        -23,
                        43,
                        -101,
                        94,
                        -83,
                        31,
                        -24,
                        -16,
                        -60,
                        87,
                        68,
                        -7,
                        -91,
                        5,
                        94,
                        -52,
                        -127,
                        127,
                        -48,
                        88,
                        -42,
                        -115,
                        -72,
                        -42,
                        -41,
                        126,
                        -64,
                        6,
                        77,
                        61,
                        -117,
                        108
                     };
                     this.decoyKeySlot71 = keccakSha3Digest71.digest(VendorAesHelper.encryptWithCipher(bytes, bytes283, paddedAesCipher));
                     l = l - 8908232427807137118L ^ -1645127317099859042L;
                  } else if (80 >= b) {
                     KeccakSha3Digest keccakSha3Digest72 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes289 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
                        -45, 36, -50, 26, 75, -94, 72, 58, 73, 35, 7, 93, 93, 23, 78, 19, 5, -1, 7, 120, 0, -68, 43, -122, 67, -5, -92, -23, -27, 42, 91, -66
                     }, paddedAesCipher);
                     this.decoyKeySlot72 = keccakSha3Digest72.digest(bytes289);
                     l = l + -8072596477259606841L ^ -2858394918304566668L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest73 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes291 = new byte[]{
                        -57,
                        -64,
                        -64,
                        127,
                        -59,
                        -36,
                        -67,
                        -7,
                        77,
                        11,
                        -2,
                        4,
                        13,
                        0,
                        85,
                        40,
                        80,
                        73,
                        107,
                        5,
                        -4,
                        -83,
                        37,
                        39,
                        -47,
                        74,
                        76,
                        101,
                        37,
                        31,
                        -54,
                        -59
                     };
                     this.decoyKeySlot73 = keccakSha3Digest73.digest(VendorAesHelper.encryptWithCipher(bytes, bytes291, paddedAesCipher));
                     l = (l ^ -1311882125331937968L) + 5721429155732842731L;
                  }
               } else {
                  if (77 >= b) {
                     KeccakSha3Digest keccakSha3Digest74 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes295 = new byte[]{
                        -44,
                        -68,
                        99,
                        -77,
                        -125,
                        -7,
                        -52,
                        79,
                        33,
                        -18,
                        -102,
                        -6,
                        37,
                        -25,
                        -71,
                        -114,
                        46,
                        88,
                        87,
                        114,
                        89,
                        -17,
                        -110,
                        -94,
                        -118,
                        89,
                        -3,
                        108,
                        -119,
                        -49,
                        27,
                        -71
                     };
                     this.decoyKeySlot74 = keccakSha3Digest74.digest(VendorAesHelper.encryptWithCipher(bytes, bytes295, paddedAesCipher));
                     l = l - -5182268550096608943L ^ 5031337263082781885L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest75 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes299 = new byte[]{
                     -62,
                     -65,
                     32,
                     -71,
                     9,
                     127,
                     118,
                     -35,
                     -49,
                     -26,
                     -12,
                     71,
                     -70,
                     107,
                     103,
                     98,
                     -96,
                     -27,
                     122,
                     122,
                     74,
                     -79,
                     67,
                     40,
                     -54,
                     -117,
                     -124,
                     60,
                     -22,
                     -110,
                     -107,
                     97
                  };
                  this.decoyKeySlot75 = keccakSha3Digest75.digest(VendorAesHelper.encryptWithCipher(bytes, bytes299, paddedAesCipher));
                  l = (l ^ -7602888792581493177L) + 8769377381140958101L;
               }
            } else if (63 <= b) {
               if (b <= 63) {
                  KeccakSha3Digest keccakSha3Digest76 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes303 = new byte[]{
                     -9,
                     92,
                     -15,
                     56,
                     -123,
                     -102,
                     -58,
                     -56,
                     -120,
                     79,
                     32,
                     110,
                     106,
                     -11,
                     -11,
                     93,
                     -36,
                     62,
                     -127,
                     100,
                     64,
                     -64,
                     96,
                     95,
                     12,
                     -13,
                     108,
                     -104,
                     -100,
                     22,
                     -75,
                     -15
                  };
                  this.secondaryKeyGuard = keccakSha3Digest76.digest(VendorAesHelper.encryptWithCipher(bytes, bytes303, paddedAesCipher));
                  l = (l ^ 708368228492720706L) - -7130148880662502219L;
                  continue outerLoop;
               }

               if (69 <= b) {
                  if (69 >= b) {
                     KeccakSha3Digest keccakSha3Digest77 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes307 = new byte[]{
                        26,
                        -34,
                        96,
                        -81,
                        108,
                        15,
                        -45,
                        38,
                        -99,
                        -118,
                        63,
                        51,
                        35,
                        108,
                        37,
                        -106,
                        56,
                        -72,
                        -127,
                        73,
                        -18,
                        55,
                        -48,
                        -101,
                        81,
                        -118,
                        -13,
                        -90,
                        41,
                        16,
                        25,
                        0
                     };
                     this.decoyKeySlot76 = keccakSha3Digest77.digest(VendorAesHelper.encryptWithCipher(bytes, bytes307, paddedAesCipher));
                     l = (l ^ 119799203142989611L) + -2908285320019668118L;
                     continue outerLoop;
                  }

                  if (b >= 72) {
                     if (72 >= b) {
                        KeccakSha3Digest keccakSha3Digest78 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes311 = new byte[]{
                           -45,
                           -86,
                           -65,
                           -70,
                           28,
                           -1,
                           52,
                           -54,
                           112,
                           21,
                           -56,
                           124,
                           -36,
                           -23,
                           65,
                           -20,
                           -17,
                           104,
                           -53,
                           12,
                           21,
                           117,
                           28,
                           11,
                           68,
                           -118,
                           17,
                           -71,
                           -45,
                           -40,
                           -41,
                           -46
                        };
                        this.decoyKeySlot77 = keccakSha3Digest78.digest(VendorAesHelper.encryptWithCipher(bytes, bytes311, paddedAesCipher));
                        l = (l ^ -6067718800089852090L) - -4641193463148803793L;
                     } else if (b >= 74) {
                        if (b > 74) {
                           return;
                        }

                        KeccakSha3Digest keccakSha3Digest79 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes315 = new byte[]{
                           59,
                           -80,
                           -36,
                           -116,
                           -12,
                           95,
                           80,
                           123,
                           111,
                           113,
                           63,
                           -2,
                           -92,
                           43,
                           -122,
                           120,
                           120,
                           75,
                           -103,
                           -127,
                           7,
                           -106,
                           121,
                           105,
                           127,
                           -84,
                           -128,
                           95,
                           42,
                           41,
                           124,
                           118
                        };
                        this.decoyKeySlot78 = keccakSha3Digest79.digest(VendorAesHelper.encryptWithCipher(bytes, bytes315, paddedAesCipher));
                        l = (l ^ 2332901192750524431L) - -220155268810218733L;
                     } else {
                        KeccakSha3Digest keccakSha3Digest80 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                        byte[] bytes319 = new byte[]{
                           15,
                           114,
                           -3,
                           -4,
                           70,
                           -118,
                           102,
                           -57,
                           104,
                           -109,
                           69,
                           -88,
                           54,
                           111,
                           43,
                           9,
                           -109,
                           39,
                           -118,
                           16,
                           81,
                           -24,
                           -112,
                           35,
                           71,
                           -31,
                           -13,
                           -106,
                           -72,
                           7,
                           57,
                           115
                        };
                        this.decoyKeySlot79 = keccakSha3Digest80.digest(VendorAesHelper.encryptWithCipher(bytes, bytes319, paddedAesCipher));
                        l = (l ^ -8435420231099402261L) + -1771708687508903087L;
                     }
                  } else if (b <= 70) {
                     KeccakSha3Digest keccakSha3Digest81 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes323 = new byte[]{
                        -124,
                        -34,
                        43,
                        -63,
                        -112,
                        44,
                        68,
                        71,
                        -57,
                        44,
                        -19,
                        16,
                        -72,
                        127,
                        -53,
                        81,
                        -94,
                        -94,
                        69,
                        90,
                        -12,
                        90,
                        109,
                        -121,
                        -103,
                        -18,
                        83,
                        -126,
                        -94,
                        39,
                        -44,
                        35
                     };
                     this.decoyKeySlot80 = keccakSha3Digest81.digest(VendorAesHelper.encryptWithCipher(bytes, bytes323, paddedAesCipher));
                     l = l + 4536519968733724716L ^ 5735668161456345241L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest82 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes327 = new byte[]{
                        -71,
                        -52,
                        63,
                        97,
                        121,
                        -127,
                        -36,
                        111,
                        -124,
                        22,
                        -38,
                        122,
                        80,
                        104,
                        60,
                        63,
                        113,
                        -47,
                        -77,
                        -2,
                        108,
                        94,
                        -47,
                        110,
                        68,
                        71,
                        -100,
                        -64,
                        -19,
                        -8,
                        -28,
                        -19
                     };
                     this.decoyKeySlot81 = keccakSha3Digest82.digest(VendorAesHelper.encryptWithCipher(bytes, bytes327, paddedAesCipher));
                     l = l + 1041052224621710155L ^ -8610360991516565922L;
                  }
               } else if (b >= 66) {
                  if (b <= 66) {
                     KeccakSha3Digest keccakSha3Digest83 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes331 = new byte[]{
                        -37,
                        33,
                        119,
                        -15,
                        40,
                        81,
                        -97,
                        50,
                        -120,
                        107,
                        116,
                        33,
                        102,
                        -128,
                        40,
                        27,
                        -84,
                        -30,
                        -28,
                        51,
                        20,
                        103,
                        -85,
                        13,
                        104,
                        59,
                        90,
                        57,
                        -106,
                        -8,
                        -104,
                        56
                     };
                     this.decoyKeySlot82 = keccakSha3Digest83.digest(VendorAesHelper.encryptWithCipher(bytes, bytes331, paddedAesCipher));
                     l = (l ^ -2993354438063449637L) - -7884029912094793854L;
                     continue outerLoop;
                  }

                  if (67 >= b) {
                     KeccakSha3Digest keccakSha3Digest84 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes335 = new byte[]{
                        -41,
                        74,
                        -71,
                        50,
                        -79,
                        121,
                        -114,
                        -96,
                        -70,
                        10,
                        124,
                        -104,
                        -27,
                        57,
                        31,
                        -60,
                        -67,
                        76,
                        -57,
                        -19,
                        86,
                        48,
                        9,
                        5,
                        -99,
                        62,
                        72,
                        -1,
                        -110,
                        -85,
                        61,
                        -17
                     };
                     this.decoyKeySlot83 = keccakSha3Digest84.digest(VendorAesHelper.encryptWithCipher(bytes, bytes335, paddedAesCipher));
                     l = l - -1363824544029079458L ^ 1975885279501938477L;
                  } else {
                     KeccakSha3Digest keccakSha3Digest85 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes339 = new byte[]{
                        -100,
                        10,
                        -57,
                        27,
                        -63,
                        41,
                        -22,
                        74,
                        99,
                        55,
                        -6,
                        19,
                        69,
                        -74,
                        -89,
                        54,
                        -56,
                        -125,
                        85,
                        28,
                        3,
                        -127,
                        -8,
                        -36,
                        89,
                        -85,
                        106,
                        92,
                        -84,
                        -111,
                        -66,
                        -59
                     };
                     this.decoyKeySlot84 = keccakSha3Digest85.digest(VendorAesHelper.encryptWithCipher(bytes, bytes339, paddedAesCipher));
                     l = l + -4966568069560667548L ^ 6586001805426017734L;
                  }
               } else {
                  if (64 < b) {
                     KeccakSha3Digest keccakSha3Digest86 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes343 = new byte[]{
                        -73,
                        -127,
                        91,
                        -81,
                        98,
                        -80,
                        -114,
                        114,
                        -54,
                        24,
                        18,
                        -119,
                        67,
                        9,
                        17,
                        -77,
                        32,
                        42,
                        103,
                        -88,
                        -26,
                        -47,
                        -101,
                        -86,
                        -71,
                        -68,
                        -18,
                        -101,
                        106,
                        -44,
                        -122,
                        121
                     };
                     this.decoyKeySlot85 = keccakSha3Digest86.digest(VendorAesHelper.encryptWithCipher(bytes, bytes343, paddedAesCipher));
                     l = l - -5171691546686218947L ^ -3429443662246146125L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest87 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes347 = new byte[]{
                     -62,
                     66,
                     18,
                     -109,
                     11,
                     -107,
                     68,
                     -115,
                     20,
                     -91,
                     -18,
                     33,
                     -90,
                     -68,
                     112,
                     127,
                     -55,
                     -81,
                     -62,
                     52,
                     -16,
                     -120,
                     32,
                     -28,
                     93,
                     -47,
                     -121,
                     104,
                     58,
                     -74,
                     -59,
                     1
                  };
                  this.decoyKeySlot86 = keccakSha3Digest87.digest(VendorAesHelper.encryptWithCipher(bytes, bytes347, paddedAesCipher));
                  l = l - -2328901015880447506L ^ -1139463180724449826L;
               }
            } else if (56 <= b) {
               if (b <= 56) {
                  KeccakSha3Digest keccakSha3Digest88 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes351 = new byte[]{
                     -85,
                     114,
                     26,
                     -64,
                     -40,
                     -114,
                     -89,
                     -46,
                     -114,
                     83,
                     -74,
                     34,
                     -10,
                     76,
                     45,
                     46,
                     -120,
                     9,
                     -74,
                     121,
                     17,
                     117,
                     6,
                     -62,
                     114,
                     -38,
                     39,
                     -104,
                     -31,
                     110,
                     -28,
                     -48
                  };
                  this.decoyKeySlot87 = keccakSha3Digest88.digest(VendorAesHelper.encryptWithCipher(bytes, bytes351, paddedAesCipher));
                  l = l - 3258899298644772513L ^ -3338643253405750118L;
                  continue outerLoop;
               }

               if (59 <= b) {
                  if (b <= 59) {
                     KeccakSha3Digest keccakSha3Digest89 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes355 = new byte[]{
                        40,
                        112,
                        -64,
                        -45,
                        102,
                        -16,
                        85,
                        -8,
                        92,
                        -32,
                        36,
                        -123,
                        -45,
                        -17,
                        -21,
                        -68,
                        -45,
                        -1,
                        -23,
                        -3,
                        121,
                        -48,
                        -64,
                        -108,
                        36,
                        -118,
                        -1,
                        22,
                        75,
                        86,
                        37,
                        -66
                     };
                     this.decoyKeySlot88 = keccakSha3Digest89.digest(VendorAesHelper.encryptWithCipher(bytes, bytes355, paddedAesCipher));
                     l = l + -5020508856717909491L ^ 9071544885768350165L;
                     continue outerLoop;
                  }

                  if (b < 61) {
                     KeccakSha3Digest keccakSha3Digest90 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes359 = new byte[]{
                        -71,
                        95,
                        -15,
                        73,
                        32,
                        -119,
                        77,
                        -120,
                        -18,
                        -28,
                        24,
                        -103,
                        -39,
                        -117,
                        16,
                        56,
                        66,
                        56,
                        -126,
                        -13,
                        104,
                        34,
                        -41,
                        -19,
                        2,
                        -72,
                        35,
                        -33,
                        -54,
                        46,
                        54,
                        16
                     };
                     this.decoyKeySlot89 = keccakSha3Digest90.digest(VendorAesHelper.encryptWithCipher(bytes, bytes359, paddedAesCipher));
                     l = l - -6069710714462439114L ^ -5972643526883352220L;
                     continue outerLoop;
                  }

                  if (b <= 61) {
                     KeccakSha3Digest keccakSha3Digest91 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes363 = new byte[]{
                        -104,
                        -116,
                        -26,
                        21,
                        -62,
                        -80,
                        97,
                        -115,
                        58,
                        34,
                        25,
                        5,
                        -108,
                        29,
                        73,
                        -78,
                        -117,
                        126,
                        -87,
                        -128,
                        -15,
                        73,
                        28,
                        -86,
                        -40,
                        16,
                        -35,
                        -16,
                        67,
                        13,
                        35,
                        118
                     };
                     this.decoyKeySlot90 = keccakSha3Digest91.digest(VendorAesHelper.encryptWithCipher(bytes, bytes363, paddedAesCipher));
                     l = l + 2601962773303538576L ^ -2855100614095505648L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest92 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes367 = new byte[]{
                     -106,
                     9,
                     71,
                     47,
                     -6,
                     6,
                     64,
                     -6,
                     98,
                     59,
                     -56,
                     -100,
                     -127,
                     49,
                     -102,
                     35,
                     -22,
                     123,
                     -67,
                     -7,
                     85,
                     -113,
                     98,
                     32,
                     13,
                     86,
                     -26,
                     -93,
                     -81,
                     -47,
                     -107,
                     45
                  };
                  this.decoyKeySlot91 = keccakSha3Digest92.digest(VendorAesHelper.encryptWithCipher(bytes, bytes367, paddedAesCipher));
                  l = (l ^ -7213083845140743953L) - 2581713361620663196L;
               } else {
                  if (57 >= b) {
                     KeccakSha3Digest keccakSha3Digest93 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                     byte[] bytes371 = new byte[]{
                        -23,
                        -20,
                        -88,
                        79,
                        -119,
                        -69,
                        -110,
                        -48,
                        103,
                        6,
                        99,
                        79,
                        -91,
                        -24,
                        36,
                        61,
                        -107,
                        -22,
                        99,
                        -58,
                        -102,
                        -119,
                        -108,
                        -4,
                        -71,
                        20,
                        20,
                        -57,
                        104,
                        -14,
                        110,
                        -127
                     };
                     this.decoyKeySlot92 = keccakSha3Digest93.digest(VendorAesHelper.encryptWithCipher(bytes, bytes371, paddedAesCipher));
                     l = l - 8896598969667048187L ^ -6718941141329961483L;
                     continue outerLoop;
                  }

                  KeccakSha3Digest keccakSha3Digest94 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes375 = new byte[]{
                     9,
                     -99,
                     53,
                     -38,
                     -60,
                     -47,
                     -53,
                     -78,
                     59,
                     -39,
                     34,
                     -58,
                     13,
                     -10,
                     49,
                     53,
                     28,
                     89,
                     30,
                     94,
                     -80,
                     -118,
                     51,
                     54,
                     47,
                     24,
                     -117,
                     64,
                     -92,
                     -121,
                     -13,
                     109
                  };
                  this.decoyKeySlot93 = keccakSha3Digest94.digest(VendorAesHelper.encryptWithCipher(bytes, bytes375, paddedAesCipher));
                  l = (l ^ -90458757856481544L) - -5846685304651271537L;
               }
            } else if (b >= 53) {
               if (53 >= b) {
                  KeccakSha3Digest keccakSha3Digest95 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes379 = new byte[]{
                     98,
                     -67,
                     0,
                     -109,
                     -21,
                     57,
                     114,
                     46,
                     121,
                     -73,
                     -1,
                     14,
                     -44,
                     -44,
                     93,
                     87,
                     -31,
                     -33,
                     -85,
                     30,
                     104,
                     -104,
                     121,
                     23,
                     125,
                     -50,
                     -62,
                     116,
                     -22,
                     44,
                     77,
                     109
                  };
                  this.decoyKeySlot94 = keccakSha3Digest95.digest(VendorAesHelper.encryptWithCipher(bytes, bytes379, paddedAesCipher));
                  l = l - -6976434436422987488L ^ -7106799899322776472L;
                  continue outerLoop;
               }

               if (b <= 54) {
                  KeccakSha3Digest keccakSha3Digest96 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes383 = new byte[]{
                     -110,
                     113,
                     -70,
                     -84,
                     48,
                     -38,
                     -117,
                     4,
                     -94,
                     -125,
                     -21,
                     -22,
                     66,
                     7,
                     79,
                     31,
                     -65,
                     -79,
                     44,
                     53,
                     37,
                     -87,
                     75,
                     48,
                     -113,
                     73,
                     -16,
                     76,
                     120,
                     -5,
                     35,
                     64
                  };
                  this.decoyKeySlot95 = keccakSha3Digest96.digest(VendorAesHelper.encryptWithCipher(bytes, bytes383, paddedAesCipher));
                  l = l - -329782099568941145L ^ 2064510023316541265L;
                  continue outerLoop;
               }

               KeccakSha3Digest keccakSha3Digest97 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes387 = new byte[]{
                  -30,
                  -39,
                  86,
                  -66,
                  116,
                  -125,
                  37,
                  51,
                  47,
                  -23,
                  5,
                  -96,
                  57,
                  -102,
                  38,
                  -10,
                  -48,
                  -18,
                  124,
                  -14,
                  -109,
                  34,
                  -38,
                  -109,
                  4,
                  124,
                  -14,
                  -20,
                  -43,
                  -9,
                  -43,
                  -35
               };
               this.decoyKeySlot96 = keccakSha3Digest97.digest(VendorAesHelper.encryptWithCipher(bytes, bytes387, paddedAesCipher));
               l = (l ^ -442672351012318947L) + -5229107053585263611L;
            } else {
               if (b > 51) {
                  KeccakSha3Digest keccakSha3Digest98 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
                  byte[] bytes391 = new byte[]{
                     -104,
                     43,
                     -13,
                     120,
                     -23,
                     88,
                     -11,
                     -25,
                     29,
                     -86,
                     92,
                     -106,
                     112,
                     -88,
                     -60,
                     72,
                     -56,
                     30,
                     45,
                     45,
                     33,
                     118,
                     65,
                     -125,
                     51,
                     89,
                     17,
                     74,
                     33,
                     127,
                     -24,
                     -25
                  };
                  this.decoyKeySlot97 = keccakSha3Digest98.digest(VendorAesHelper.encryptWithCipher(bytes, bytes391, paddedAesCipher));
                  l = (l ^ 7667232452411622553L) + 3904905873987927043L;
                  continue outerLoop;
               }

               KeccakSha3Digest keccakSha3Digest99 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
               byte[] bytes395 = new byte[]{
                  34,
                  127,
                  -57,
                  -34,
                  -86,
                  -115,
                  120,
                  -27,
                  72,
                  -75,
                  -24,
                  -57,
                  67,
                  -127,
                  -91,
                  61,
                  59,
                  -20,
                  -10,
                  -6,
                  66,
                  -100,
                  112,
                  -109,
                  -11,
                  -33,
                  36,
                  -114,
                  85,
                  8,
                  -15,
                  2
               };
               this.decoyKeySlot98 = keccakSha3Digest99.digest(VendorAesHelper.encryptWithCipher(bytes, bytes395, paddedAesCipher));
               l = (l ^ -6267029551755163643L) + 7481478331514590426L;
            }
         }

         KeccakSha3Digest keccakSha3Digest100 = new KeccakSha3Digest(Sha3Algorithm.SHA3_256);
         byte[] bytes401 = VendorAesHelper.encryptWithCipher(bytes, new byte[]{
            46, 30, -81, 114, -65, -57, 110, 65, -4, 92, 105, 97, 18, -58, 125, 110, 1, 63, -31, -84, 109, -24, -68, 18, 115, -68, 96, -1, -112, 23, 47, -102
         }, paddedAesCipher);
         this.decoyKeySlot99 = keccakSha3Digest100.digest(bytes401);
         l = (l ^ -5039876201842451885L) + -9129066416698354503L;
      }
   }

   private int mul4(int n, byte[] bytes) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      byte b = 3;

      int n6;
      while (true) {
         while (2 >= b) {
            if (n == 0) {
               return 0;
            }

            n = this.gfLogTable[n & 0xFF];
         }

         if (b <= 3) {
            n6 = this.gfAntilogTable[(n + this.gfLogTable[((byte[])bytes)[0] & 255]) % 255] & 0xFF;
            break;
         }

         if (bytes[0] == 0) {
            n6 = 0;
            break;
         }
      }

      n4 = n6;
      n5 = ((byte[])bytes)[1] != 0 ? this.gfAntilogTable[(n + this.gfLogTable[((byte[])bytes)[1] & 255]) % 255] & 0xFF : 0;
      n3 = ((byte[])bytes)[2] != 0 ? this.gfAntilogTable[(n + this.gfLogTable[((byte[])bytes)[2] & 255]) % 255] & 0xFF : 0;
      n2 = ((byte[])bytes)[3] != 0 ? this.gfAntilogTable[(n + this.gfLogTable[((byte[])bytes)[3] & 255]) % 255] & 0xFF : 0;
      return n4 << 24 | n5 << 16 | n3 << 8 | n2;
   }

   public void decryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.decryptBlockImpl(bytes, n, bytes2, n2);
   }

   private int getRounds(int n) {
      return (n >> 2) + 6;
   }

   public void padWithLen(byte[] bytes, int n, int n2) throws ShortBufferException {
      byte b = 0;
      int n3 = 0;
      byte b2 = 2;

      while (true) {
         while (true) {
            if (3 >= b2) {
               Arrays.fill(bytes, n, n3, b);
               break;
            }

            if (b2 >= 5) {
               if (5 >= b2) {
                  if (bytes == null) {
                     break;
                  }
               } else {
                  if (6 >= b2) {
                     throw new ShortBufferException("Buffer too small to hold padding");
                  }

                  if (n3 <= bytes.length) {
                     break;
                  }
               }
            } else {
               n3 = Math.addExact(n, n2);
            }
         }

         if (b2 >= 1) {
            if (1 >= b2) {
               return;
            }

            return;
         }

         b = (byte)(n2 & 0xFF);
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

   private ByteBuf encryptBuffer(ByteBuf buf) {
      ByteBuf buf2 = null;
      byte[] bytes = null;
      byte[] bytes2 = null;
      byte b = 5;
      long l = -709133871989894080L;
      l = -7532957059474622662L ^ (long)"迃\uf3be\ue232问䞃Ὸﰞ\ue212\u1759ᦀ㮐弼袶譾갡\uf782晭複㥿燇䔕敭㑗傓".hashCode();

      outerLoop:
      while (true) {
         if (b <= 6) {
            l = l - 1639893961190029540L ^ 2345957453961336763L;
         } else if (b >= 9) {
            if (9 >= b) {
               this.initCipher(false, this.sessionKey);
               l = l + -4666247049778223485L ^ -4114117634688248922L;
            } else if (b >= 11) {
               if (b > 11) {
                  bytes = new byte[buf.readableBytes()];
                  l = (l ^ -8079355781018221629L) - 6833112469095425151L;
                  continue;
               }

               buf.readBytes(bytes);
               l = l + -7659084079034440311L ^ -3426349729558332918L;
            } else {
               this.resetCbcState();
               l = (l ^ -1195693494201717227L) - -3864967788396235019L;
            }
         } else {
            if (b > 7) {
               buf2 = null;
               l = (l ^ 666852004077374800L) + -1310607112485357883L;
               continue;
            }

            if (this.primaryKeyGuard.length >= 32) {
               l = (l ^ 3374185199483948446L) - -841523495884037882L;
               continue;
            }

            l = (l ^ -2496785093890477179L) - -6761084536524300170L;
         }

         while (2 <= b) {
            if (b <= 2) {
               this.timingGuard.enterStep2();
               l = l + -3295264550567271028L ^ -60304680995282423L;
               continue outerLoop;
            }

            if (b >= 4) {
               if (b > 4) {
                  return buf2;
               }

               if (this.secondaryKeyGuard.length >= 32) {
                  l = (l ^ -4597207442699973942L) - -4274072351268393933L;
                  continue outerLoop;
               }

               l = l - -7338350616805465333L ^ -6931808908108259444L;
            } else {
               buf2 = Unpooled.copiedBuffer(bytes2);
               l = (l ^ -4964262670329088842L) + 8018560695944553664L;
            }
         }

         if (b <= 0) {
            buf2 = Unpooled.copiedBuffer(bytes);
            l = (l ^ -1043148515387680852L) - -5508832852666363928L;
         } else {
            bytes2 = this.encryptBytes(bytes, (byte)0, bytes.length);
            l = l - -4563026084181840952L ^ 7183580633520798832L;
         }
      }
   }

   public void checkBlockAlignment(int n, int n2) {
      if (n % n2 != 0) {
         throw new ProviderException("Internal error in input buffering");
      }
   }

   public byte[] decryptBytes(byte[] bytes, int n, int n2) {
      byte[] bytes2 = null;
      int n3 = 0;
      byte b = 0;
      long l = 6592012946553243227L;
      l = 7950137402060150363L ^ (long)"俥崑䎢帥炷肈ᰪ┅ㅜ궍㹈踰曛応\uef14⡂枔Ꮐ臉茗ᓻ㸼耩".hashCode();

      while (3 < b) {
         if (5 <= b) {
            if (b > 5) {
               return null;
            }

            if (n2 > 0) {
               l = (l ^ 5306558445932242433L) - -2452354113613580275L;
            } else {
               l = l + 4195423196473930149L ^ -4542644553519478980L;
            }
         } else {
            bytes2 = new byte[n2];
            l = l - -3534550851448625565L ^ 4799318846441650702L;

            while (true) {
               while (b < 1) {
                  this.checkBlockAlignment(n2, this.getBlockSize());
                  l = l - 2084707472068793156L ^ 7906626699993439817L;
               }

               if (1 >= b) {
                  n3 = this.unpadPkcs5(bytes2, (byte)0, bytes.length);
                  l = l + -5074330563884915622L ^ -5017916064050849079L;
                  break;
               }

               this.decryptCbc(bytes, n, n2, bytes2, (byte)0);
               l = (l ^ -7416258388968080812L) + 5661562293899121800L;
            }
         }
      }

      return Arrays.copyOf(bytes2, n3);
   }

   public VendorPacketEncryptor(PipelineTimingGuard pipelineTimingGuard) {
       super();
      int n = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0, n6 = 0, n7 = 0, n8 = 0;
      this.primaryKeyGuard = new byte[1];
      this.derivedKeySlot61 = new byte[1];
      this.derivedKeySlot36 = new byte[1];
      this.derivedKeySlot14 = new byte[1];
      this.derivedKeySlot62 = new byte[1];
      this.derivedKeySlot86 = new byte[1];
      this.derivedKeySlot38 = new byte[1];
      this.derivedKeySlot84 = new byte[1];
      this.derivedKeySlot12 = new byte[1];
      this.derivedKeySlot46 = new byte[1];
      this.derivedKeySlot45 = new byte[1];
      this.derivedKeySlot63 = new byte[1];
      this.derivedKeySlot80 = new byte[1];
      this.derivedKeySlot97 = new byte[1];
      this.derivedKeySlot75 = new byte[1];
      String opaqueStateTag = VendorPacketBuffer.getOpaqueStateTag();
      this.derivedKeySlot49 = new byte[1];
      this.derivedKeySlot92 = new byte[1];
      this.derivedKeySlot83 = new byte[1];
      this.derivedKeySlot78 = new byte[1];
      this.derivedKeySlot05 = new byte[1];
      this.derivedKeySlot35 = new byte[1];
      this.derivedKeySlot30 = new byte[1];
      this.derivedKeySlot16 = new byte[1];
      this.derivedKeySlot25 = new byte[1];
      this.derivedKeySlot66 = new byte[1];
      this.derivedKeySlot48 = new byte[1];
      this.derivedKeySlot07 = new byte[1];
      this.derivedKeySlot91 = new byte[1];
      this.derivedKeySlot60 = new byte[1];
      this.derivedKeySlot69 = new byte[1];
      this.derivedKeySlot96 = new byte[1];
      this.derivedKeySlot15 = new byte[1];
      this.derivedKeySlot64 = new byte[1];
      this.derivedKeySlot09 = new byte[1];
      this.derivedKeySlot90 = new byte[1];
      this.derivedKeySlot04 = new byte[1];
      this.derivedKeySlot76 = new byte[1];
      this.derivedKeySlot82 = new byte[1];
      this.derivedKeySlot56 = new byte[1];
      this.derivedKeySlot31 = new byte[1];
      this.derivedKeySlot65 = new byte[1];
      this.derivedKeySlot39 = new byte[1];
      this.derivedKeySlot54 = new byte[1];
      this.derivedKeySlot23 = new byte[1];
      this.derivedKeySlot99 = new byte[1];
      this.derivedKeySlot51 = new byte[1];
      this.derivedKeySlot72 = new byte[1];
      this.derivedKeySlot81 = new byte[1];
      this.derivedKeySlot94 = new byte[1];
      this.derivedKeySlot87 = new byte[1];
      this.derivedKeySlot58 = new byte[1];
      this.derivedKeySlot27 = new byte[1];
      this.derivedKeySlot02 = new byte[1];
      this.derivedKeySlot57 = new byte[1];
      this.derivedKeySlot79 = new byte[1];
      this.derivedKeySlot42 = new byte[1];
      this.derivedKeySlot13 = new byte[1];
      this.derivedKeySlot74 = new byte[1];
      this.derivedKeySlot89 = new byte[1];
      this.derivedKeySlot11 = new byte[1];
      this.derivedKeySlot32 = new byte[1];
      this.derivedKeySlot50 = new byte[1];
      this.derivedKeySlot08 = new byte[1];
      this.derivedKeySlot73 = new byte[1];
      this.derivedKeySlot10 = new byte[1];
      this.derivedKeySlot88 = new byte[1];
      this.derivedKeySlot03 = new byte[1];
      this.derivedKeySlot19 = new byte[1];
      this.derivedKeySlot95 = new byte[1];
      this.derivedKeySlot26 = new byte[1];
      this.derivedKeySlot53 = new byte[1];
      this.derivedKeySlot21 = new byte[1];
      this.derivedKeySlot52 = new byte[1];
      this.derivedKeySlot29 = new byte[1];
      this.derivedKeySlot43 = new byte[1];
      this.derivedKeySlot41 = new byte[1];
      this.derivedKeySlot28 = new byte[1];
      this.derivedKeySlot77 = new byte[1];
      this.derivedKeySlot55 = new byte[1];
      this.derivedKeySlot34 = new byte[1];
      this.derivedKeySlot67 = new byte[1];
      this.derivedKeySlot59 = new byte[1];
      this.derivedKeySlot17 = new byte[1];
      this.derivedKeySlot24 = new byte[1];
      this.derivedKeySlot70 = new byte[1];
      this.derivedKeySlot01 = new byte[1];
      this.derivedKeySlot98 = new byte[1];
      this.derivedKeySlot20 = new byte[1];
      this.derivedKeySlot68 = new byte[1];
      this.derivedKeySlot47 = new byte[1];
      this.derivedKeySlot33 = new byte[1];
      this.derivedKeySlot22 = new byte[1];
      this.derivedKeySlot44 = new byte[1];
      this.derivedKeySlot93 = new byte[1];
      this.derivedKeySlot06 = new byte[1];
      this.derivedKeySlot18 = new byte[1];
      this.derivedKeySlot71 = new byte[1];
      this.derivedKeySlot40 = new byte[1];
      this.derivedKeySlot85 = new byte[1];
      this.derivedKeySlot37 = new byte[1];
      this.decoyKeySlot13 = new byte[1];
      this.decoyKeySlot17 = new byte[1];
      this.decoyKeySlot04 = new byte[1];
      this.decoyKeySlot41 = new byte[1];
      this.decoyKeySlot61 = new byte[1];
      this.decoyKeySlot58 = new byte[1];
      this.decoyKeySlot85 = new byte[1];
      this.decoyKeySlot07 = new byte[1];
      this.decoyKeySlot53 = new byte[1];
      this.decoyKeySlot60 = new byte[1];
      this.decoyKeySlot46 = new byte[1];
      this.decoyKeySlot16 = new byte[1];
      this.decoyKeySlot18 = new byte[1];
      this.decoyKeySlot39 = new byte[1];
      this.decoyKeySlot92 = new byte[1];
      this.decoyKeySlot23 = new byte[1];
      this.decoyKeySlot33 = new byte[1];
      this.decoyKeySlot88 = new byte[1];
      this.decoyKeySlot47 = new byte[1];
      this.decoyKeySlot78 = new byte[1];
      this.decoyKeySlot84 = new byte[1];
      this.decoyKeySlot64 = new byte[1];
      this.decoyKeySlot87 = new byte[1];
      this.decoyKeySlot02 = new byte[1];
      this.decoyKeySlot42 = new byte[1];
      this.decoyKeySlot93 = new byte[1];
      this.decoyKeySlot52 = new byte[1];
      this.decoyKeySlot29 = new byte[1];
      this.decoyKeySlot15 = new byte[1];
      this.decoyKeySlot28 = new byte[1];
      this.decoyKeySlot14 = new byte[1];
      this.decoyKeySlot62 = new byte[1];
      this.decoyKeySlot30 = new byte[1];
      this.decoyKeySlot99 = new byte[1];
      this.decoyKeySlot45 = new byte[1];
      this.decoyKeySlot22 = new byte[1];
      this.decoyKeySlot24 = new byte[1];
      this.decoyKeySlot77 = new byte[1];
      this.decoyKeySlot79 = new byte[1];
      this.decoyKeySlot51 = new byte[1];
      this.decoyKeySlot11 = new byte[1];
      this.decoyKeySlot06 = new byte[1];
      this.decoyKeySlot71 = new byte[1];
      this.decoyKeySlot83 = new byte[1];
      this.decoyKeySlot66 = new byte[1];
      this.decoyKeySlot49 = new byte[1];
      this.decoyKeySlot90 = new byte[1];
      this.decoyKeySlot31 = new byte[1];
      this.decoyKeySlot26 = new byte[1];
      this.decoyKeySlot01 = new byte[1];
      this.decoyKeySlot44 = new byte[1];
      this.decoyKeySlot89 = new byte[1];
      this.decoyKeySlot20 = new byte[1];
      this.decoyKeySlot72 = new byte[1];
      this.decoyKeySlot69 = new byte[1];
      this.decoyKeySlot57 = new byte[1];
      this.decoyKeySlot25 = new byte[1];
      this.decoyKeySlot73 = new byte[1];
      this.decoyKeySlot68 = new byte[1];
      this.decoyKeySlot98 = new byte[1];
      this.decoyKeySlot91 = new byte[1];
      this.decoyKeySlot76 = new byte[1];
      this.decoyKeySlot10 = new byte[1];
      this.decoyKeySlot94 = new byte[1];
      this.decoyKeySlot05 = new byte[1];
      this.decoyKeySlot27 = new byte[1];
      this.decoyKeySlot63 = new byte[1];
      this.decoyKeySlot96 = new byte[1];
      this.decoyKeySlot55 = new byte[1];
      this.decoyKeySlot12 = new byte[1];
      this.decoyKeySlot70 = new byte[1];
      this.decoyKeySlot86 = new byte[1];
      this.decoyKeySlot97 = new byte[1];
      this.decoyKeySlot40 = new byte[1];
      this.decoyKeySlot82 = new byte[1];
      this.decoyKeySlot37 = new byte[1];
      this.decoyKeySlot21 = new byte[1];
      this.decoyKeySlot48 = new byte[1];
      this.decoyKeySlot74 = new byte[1];
      this.decoyKeySlot19 = new byte[1];
      this.decoyKeySlot36 = new byte[1];
      this.decoyKeySlot32 = new byte[1];
      this.decoyKeySlot65 = new byte[1];
      this.decoyKeySlot50 = new byte[1];
      this.decoyKeySlot56 = new byte[1];
      this.decoyKeySlot75 = new byte[1];
      String str = opaqueStateTag;
      this.decoyKeySlot95 = new byte[1];
      this.decoyKeySlot43 = new byte[1];
      this.decoyKeySlot35 = new byte[1];
      this.decoyKeySlot08 = new byte[1];
      this.decoyKeySlot59 = new byte[1];
      this.decoyKeySlot81 = new byte[1];
      this.decoyKeySlot80 = new byte[1];
      this.secondaryKeyGuard = new byte[1];
      this.decoyKeySlot34 = new byte[1];
      this.decoyKeySlot38 = new byte[1];
      this.decoyKeySlot54 = new byte[1];
      this.decoyKeySlot03 = new byte[1];
      this.decoyKeySlot09 = new byte[1];
      this.decoyKeySlot67 = new byte[1];
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

      if (Setting.getControlFlowMarker() != null) {
         VendorPacketBuffer.setOpaqueStateTag("ukLBKb");
      }
   }

   public void encodeBuffer(ChannelHandlerContext ctx, ByteBuf buf, List list) {
      list.add(this.encryptBuffer(buf));
   }

   public void encryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.encryptBlockImpl(bytes, n, bytes2, n2);
   }

   private int decryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = 0;
      int n5 = 0;
      int n6 = 1;
      long l = 1435321059130011527L;
      long l2 = 0L;
      l = 1960731779548591250L ^ (long)"Щ\ue990럩ꨗ䰓濾躺ﯰ퍤➓㾖\uea7a뷧Ợ㥭翏墸嫶餌ണ闀㡑ꯙ\ueac7".hashCode();
      l2 = -1689907796374971336L ^ (long)"䝕푃\uf7b5⸶\ue81e妝禖㥢ו\ue16b좙\ueb64᧬㩮允퇞⽋쨍䙎觺幈鍢".hashCode();

      while (true) {
         n6 = (int)(l % 13L);
         if (n6 >= 6) {
            if (n6 <= 6) {
               l = (l ^ -3686181877964649297L) - 4970035046704094442L;
               l2 = l2 + -9142267349446882512L ^ 842871525746095148L;
            } else if (n6 >= 9) {
               if (9 >= n6) {
                  return n2;
               }

               if (n6 >= 11) {
                  if (n6 <= 11) {
                     if (n >= n5) {
                        l = l + -2260369823146820143L ^ 769916527961352050L;
                        l2 = (l2 ^ 3931546523583087789L) - 7620279933029927910L;
                     } else {
                        l = l + -1573762377780249242L ^ 6623342662610866369L;
                        l2 = l2 - -2876695940389522342L ^ 7460141937735820312L;
                     }
                  } else {
                     bytes2[n4 + n3] = (byte)(this.cbcWorkBuffer[n4] ^ this.cbcRegister[n4]);
                     l = l + -8938987532801991217L ^ -8196662517184593937L;
                     l2 = l2 - 8239489227826545196L ^ 3926071942293934656L;
                  }
               } else {
                  l = (l ^ 417235126316155935L) - -1786093539578439851L;
                  l2 = (l2 ^ 900140679313550048L) + 8238562781221978989L;
               }
            } else if (7 >= n6) {
               n5 = n + n2;
               l = (l ^ 8927129579530490654L) - 2274842304267313640L;
               l2 = (l2 ^ 3550880474359603668L) + -3615222063899978911L;
            } else {
               n3 += 0;
               l = l - -4710733023159111706L ^ -20270531189859811L;
               l2 = l2 + -2121207512515806412L ^ -3442681351804815642L;
            }
         } else if (2 <= n6) {
            if (2 >= n6) {
               n4 = 0;
               l = l - 3791187369550906286L ^ 5714207727895137680L;
               l2 = (l2 ^ -312771016759067481L) + -5252166598503617980L;
            } else if (n6 >= 4) {
               if (4 >= n6) {
                  l = l + -5733184243715827416L ^ 5000340795241881374L;
                  l2 = (l2 ^ 823549930613906494L) + -5948481405656249983L;
               } else {
                  n4++;
                  l = l + 1172480986647764518L ^ 3939196719187288625L;
                  l2 = l2 + 1554061949897941651L ^ 7788979272237408949L;
               }
            } else {
               System.arraycopy(bytes, n, this.cbcRegister, 0, 0);
               l = l - 3570827513461766881L ^ 2477291249068070925L;
               l2 = (l2 ^ -8561809755890365502L) + 5103064570437121979L;
            }
         } else if (0 >= n6) {
            n += 0;
            l = (l ^ 8307689839485089034L) - 808385886918586549L;
            l2 = l2 - -1840514669037580782L ^ 8314925188309453364L;
         } else if (n4 >= 0) {
            l = (l ^ 1996716014287174071L) + -324218526089453935L;
            l2 = l2 + -1048382463466143155L ^ -3007948043893545814L;
         } else {
            l = (l ^ -5825810301693402689L) - -8567596550523228440L;
            l2 = l2 + 5633971823681389516L ^ 1119508799947915259L;
         }
      }
   }

   private int gfMul(int n, int n2) {
      int n3 = 1;
      long l = 1529640426863803003L;
      long l2 = 0L;
      l = 1141214139357693664L ^ (long)"\ue8e8\uf612\ue94a\udbc2痃圖沔펽맀븦驶⡶퐫ੀ餫桚\ue0c4쏰ヾꨅ\uf720ꈻ數뇧灚".hashCode();
      l2 = -3788460680153458273L ^ (long)"韄茍纃눮䅥鞊袊ி\ue985\uf359弬뗝坁뺘㪯㮫돧薟\ue62d뜽뒠챇ݒ䨛".hashCode();

      int n4;
      while (true) {
         n3 = (int)(l % 3L);
         if (n3 >= 1) {
            if (1 >= n3) {
               if (n != 0) {
                  l = l - 2975227650882937902L ^ -2777386777687204709L;
                  l2 = l2 + -1391847470360916646L ^ 7924283496727336313L;
                  continue;
               }

               l = (l ^ 0L) + 0L;
               l2 = l2 + 0L ^ 0L;
            } else {
               if (n2 != 0) {
                  l = (l ^ 8576675610551894404L) - 1763767071645691675L;
                  l2 = l2 + 4969561109710963369L ^ -1299612782706851892L;
                  continue;
               }

               l = l + -3747448611347074345L ^ 4283775614136377998L;
               l2 = (l2 ^ -5411239598006563300L) + -8942107401786258231L;
            }

            n4 = 0;
            break;
         }

         n4 = this.gfAntilogTable[(this.gfLogTable[n & 0xFF] + this.gfLogTable[n2 & 0xFF]) % 255];
         break;
      }

      return n4;
   }


}
