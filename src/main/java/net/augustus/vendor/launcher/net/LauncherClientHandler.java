package net.augustus.vendor.launcher.net;

import net.augustus.vendor.launcher.ClientArchiveHolder;
import net.augustus.vendor.launcher.CredentialStorage;
import net.augustus.vendor.launcher.ElectricLauncherController;
import net.augustus.vendor.launcher.ElectricLauncherMain;
import net.augustus.vendor.launcher.EncryptedClassLoader;
import net.augustus.vendor.launcher.HardwareFingerprintCollector;
import net.augustus.vendor.launcher.RemoteClassFetcher;
import net.augustus.vendor.launcher.crypto.AesCipherUtil;
import net.augustus.vendor.launcher.crypto.EntropyXorShiftRandom;
import net.augustus.vendor.launcher.crypto.Pkcs1Padding;
import net.augustus.vendor.launcher.crypto.Sha256Digest;
import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import net.augustus.vendor.launcher.crypto.rsa.RsaPublicKeyCipher;
import net.augustus.vendor.launcher.packet.C2SHandshakeCompletePacket;
import net.augustus.vendor.launcher.packet.C2SHandshakeInitPacket;
import net.augustus.vendor.launcher.packet.C2SHardwareInfoPacket;
import net.augustus.vendor.launcher.packet.C2SIntegrityProofPacket;
import net.augustus.vendor.launcher.packet.C2SRemoteClassRequestPacket;
import net.augustus.vendor.launcher.packet.C2SResourcesReadyPacket;
import net.augustus.vendor.launcher.packet.LauncherPacket;
import net.augustus.vendor.launcher.packet.S2CActivateClientResponsePacket;
import net.augustus.vendor.launcher.packet.S2CClassLoaderKeyPacket;
import net.augustus.vendor.launcher.packet.S2CClientArchiveChunkPacket;
import net.augustus.vendor.launcher.packet.S2CFailureMessagePacket;
import net.augustus.vendor.launcher.packet.S2CHandshakeChallengePacket;
import net.augustus.vendor.launcher.packet.S2CHardwareChallengePacket;
import net.augustus.vendor.launcher.packet.S2CHwidResetResponsePacket;
import net.augustus.vendor.launcher.packet.S2CIntegrityChallengePacket;
import net.augustus.vendor.launcher.packet.S2CLoginResponsePacket;
import net.augustus.vendor.launcher.packet.S2CRegisterResponsePacket;
import net.augustus.vendor.launcher.packet.S2CRemoteClassResponsePacket;
import net.augustus.vendor.launcher.packet.S2CResourceChunkPacket;
import net.augustus.vendor.launcher.security.ArchiveAllocationGuard;
import net.augustus.vendor.launcher.security.ClassIntegrityWatchdog;
import net.augustus.vendor.launcher.security.CodeSourceIntegrityHasher;
import net.augustus.vendor.launcher.security.LaunchEnvironmentCheck;
import net.augustus.vendor.launcher.util.Base64Codec;
import net.augustus.vendor.launcher.util.Base64Encoder;
import net.augustus.vendor.launcher.util.Base64VarIntCodec;
import net.augustus.vendor.launcher.util.ShadedByteBuffer;
import net.augustus.vendor.launcher.util.ShadedHeapByteBuffer;
import net.augustus.vendor.launcher.util.ZipArchiveExtractor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javafx.application.Platform;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class LauncherClientHandler extends SimpleChannelInboundHandler {
   private static final int AES_BLOCK_SIZE = 0;
   private boolean added;
   private volatile boolean autoRelease;
   private static final int GCM_IV_LENGTH = 0;
   private static final int GCM_TAG_LENGTH = 0;
   private static final int AES_KEY_LENGTH = 0;
   private boolean nativesReady;
   private final ReentrantReadWriteLock packetQueueLock;
   private static volatile LauncherClientHandler instance = null;
   public static volatile ClientArchiveHolder archiveHolder = null;
   private Channel channel;
   public volatile byte[] sessionSecret;
   private int archiveBytesReceived;
   private byte[] inboundSessionKey;
   private static final int[] AES_SBOX = new int[]{
      99,
      124,
      119,
      123,
      242,
      107,
      111,
      197,
      48,
      1,
      103,
      43,
      254,
      215,
      171,
      118,
      202,
      130,
      201,
      125,
      250,
      89,
      71,
      240,
      173,
      212,
      162,
      175,
      156,
      164,
      114,
      192,
      183,
      253,
      147,
      38,
      54,
      63,
      247,
      204,
      52,
      165,
      229,
      241,
      113,
      216,
      49,
      21,
      4,
      199,
      35,
      195,
      24,
      150,
      5,
      154,
      7,
      18,
      128,
      226,
      235,
      39,
      178,
      117,
      9,
      131,
      44,
      26,
      27,
      110,
      90,
      160,
      82,
      59,
      214,
      179,
      41,
      227,
      47,
      132,
      83,
      209,
      0,
      237,
      32,
      252,
      177,
      91,
      106,
      203,
      190,
      57,
      74,
      76,
      88,
      207,
      208,
      239,
      170,
      251,
      67,
      77,
      51,
      133,
      69,
      249,
      2,
      127,
      80,
      60,
      159,
      168,
      81,
      163,
      64,
      143,
      146,
      157,
      56,
      245,
      188,
      182,
      218,
      33,
      16,
      255,
      243,
      210,
      205,
      12,
      19,
      236,
      95,
      151,
      68,
      23,
      196,
      167,
      126,
      61,
      100,
      93,
      25,
      115,
      96,
      129,
      79,
      220,
      34,
      42,
      144,
      136,
      70,
      238,
      184,
      20,
      222,
      94,
      11,
      219,
      224,
      50,
      58,
      10,
      73,
      6,
      36,
      92,
      194,
      211,
      172,
      98,
      145,
      149,
      228,
      121,
      231,
      200,
      55,
      109,
      141,
      213,
      78,
      169,
      108,
      86,
      244,
      234,
      101,
      122,
      174,
      8,
      186,
      120,
      37,
      46,
      28,
      166,
      180,
      198,
      232,
      221,
      116,
      31,
      75,
      189,
      139,
      138,
      112,
      62,
      181,
      102,
      72,
      3,
      246,
      14,
      97,
      53,
      87,
      185,
      134,
      193,
      29,
      158,
      225,
      248,
      152,
      17,
      105,
      217,
      142,
      148,
      155,
      30,
      135,
      233,
      206,
      85,
      40,
      223,
      140,
      161,
      137,
      13,
      191,
      230,
      66,
      104,
      65,
      153,
      45,
      15,
      176,
      84,
      187,
      22
   };
   private int resourcesBytesReceived;
   private byte[] nativesArchiveBuffer;
   private boolean resourcesReady;
   private boolean loggedIn;
   private int pendingPacketCounter;
   private final PacketCipherEncoder packetCipherEncoder;
   private String availableClientsList;
   private int handshakeRetryCount;
   public static volatile boolean connected = false;
   private static final int[] AES_RCON = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 27, 54};
   private int nativesBytesReceived;
   private final Sha256Digest sha256Digest;
   private final PacketCipherDecoder packetCipherDecoder;
   private final Queue<LauncherPacket> outboundPacketQueue;
   private static final int[] AES_INV_SBOX = new int[]{
      82,
      9,
      106,
      213,
      48,
      54,
      165,
      56,
      191,
      64,
      163,
      158,
      129,
      243,
      215,
      251,
      124,
      227,
      57,
      130,
      155,
      47,
      255,
      135,
      52,
      142,
      67,
      68,
      196,
      222,
      233,
      203,
      84,
      123,
      148,
      50,
      166,
      194,
      35,
      61,
      238,
      76,
      149,
      11,
      66,
      250,
      195,
      78,
      8,
      46,
      161,
      102,
      40,
      217,
      36,
      178,
      118,
      91,
      162,
      73,
      109,
      139,
      209,
      37,
      114,
      248,
      246,
      100,
      134,
      104,
      152,
      22,
      212,
      164,
      92,
      204,
      93,
      101,
      182,
      146,
      108,
      112,
      72,
      80,
      253,
      237,
      185,
      218,
      94,
      21,
      70,
      87,
      167,
      141,
      157,
      132,
      144,
      216,
      171,
      0,
      140,
      188,
      211,
      10,
      247,
      228,
      88,
      5,
      184,
      179,
      69,
      6,
      208,
      44,
      30,
      143,
      202,
      63,
      15,
      2,
      193,
      175,
      189,
      3,
      1,
      19,
      138,
      107,
      58,
      145,
      17,
      65,
      79,
      103,
      220,
      234,
      151,
      242,
      207,
      206,
      240,
      180,
      230,
      115,
      150,
      172,
      116,
      34,
      231,
      173,
      53,
      133,
      226,
      249,
      55,
      232,
      28,
      117,
      223,
      110,
      71,
      241,
      26,
      113,
      29,
      41,
      197,
      137,
      111,
      183,
      98,
      14,
      170,
      24,
      190,
      27,
      252,
      86,
      62,
      75,
      198,
      210,
      121,
      32,
      154,
      219,
      192,
      254,
      120,
      205,
      90,
      244,
      31,
      221,
      168,
      51,
      136,
      7,
      199,
      49,
      177,
      18,
      16,
      89,
      39,
      128,
      236,
      95,
      96,
      81,
      127,
      169,
      25,
      181,
      74,
      13,
      45,
      229,
      122,
      159,
      147,
      201,
      156,
      239,
      160,
      224,
      59,
      77,
      174,
      42,
      245,
      176,
      200,
      235,
      187,
      60,
      131,
      83,
      153,
      97,
      23,
      43,
      4,
      126,
      186,
      119,
      214,
      38,
      225,
      105,
      20,
      99,
      85,
      33,
      12,
      125
   };
   private byte[] resourcesJarBuffer;
   public volatile byte[] remoteClassResponseBytes;
   private byte[] outboundSessionKey;
   private static final int HANDSHAKE_KEY_OFFSET = 0;
   public volatile boolean handshakeCompleted;
   private final PipelineStageGuard stageGuard;
   private String lastDownloadPercent;
   public volatile byte[] sessionToken;
   private static final long a = 104488865256865L;

   public LauncherClientHandler(PacketCipherEncoder packetCipherEncoder2, PacketCipherDecoder packetCipherDecoder2, PipelineStageGuard pipelineStageGuard) {
       super();
      byte[] bytes = new byte[0];
      this.sessionToken = bytes;
      Object obj = null;
      this.sessionSecret = (byte[])obj;
      boolean flag = false;
      this.added = flag;
      byte b = 0;
      this.pendingPacketCounter = b;
      byte b2 = 0;
      this.handshakeRetryCount = b2;
      boolean flag2 = true;
      this.autoRelease = flag2;
      String str = "";
      this.lastDownloadPercent = str;
      boolean flag3 = false;
      this.loggedIn = flag3;
      Object obj2 = null;
      this.remoteClassResponseBytes = (byte[])obj2;
      Object obj3 = null;
      this.availableClientsList = (String)obj3;
      this.outboundPacketQueue = new ConcurrentLinkedQueue<>();
      this.packetQueueLock = new ReentrantReadWriteLock();
      this.sha256Digest = new Sha256Digest();
      this.packetCipherEncoder = packetCipherEncoder2;
      this.packetCipherDecoder = packetCipherDecoder2;
      this.stageGuard = pipelineStageGuard;
   }

   public boolean isAutoRelease() {
      return this.autoRelease;
   }

   private static void showStartingClientStatus(long l, short s) {
      ElectricLauncherController.getInstance().infoText.setText("Starting client...");
   }

   private static void addClientListEntry(long l, String str) {
      ElectricLauncherController.getInstance().clientList.getItems().addAll(str);
   }

   private void dispatchPacket(LauncherPacket packet) {
      byte b = 1;

      while (b > 0) {
         if (this.channel.eventLoop().inEventLoop()) {
            ;
         }

         while (b > 2) {
            if (3 < b) {
               this.channel.eventLoop().execute(this::sleepBriefly);
               break;
            }

            this.channel.writeAndFlush(packet);
         }
      }
   }

   private static void showWaitingForServerStatus(long l) {
      ElectricLauncherController.getInstance().infoText.setText("Waiting for server...");
   }

   public void setAutoRelease(boolean flag) {
      this.autoRelease = flag;
   }

   private void writeFileBytes(Path path, byte[] bytes, OpenOption... openOptions) {
      try {
         Files.write(path, bytes, openOptions);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void initiateHandshake() {
      Object obj = null;
      int n = 0;
      Object obj2 = null;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      Object obj3 = null;
      int n5 = 0;
      int n6 = 0;
      Object obj4 = null;
      long l = 0L;
      Object obj5 = null;
      int n7 = 0;
      Object obj6 = null;
      int n8 = 0;
      String str = null;
      byte b = 0;
      int n9 = 0;
      long l2 = 0L;
      byte[] bytes = null;
      int n10 = 0;
      int[][] intsArr = null;
      int n11 = 0;
      long l3 = 0L;
      Object obj7 = null;
      Object obj8 = null;
      Object obj9 = null;
      Object obj10 = null;
      int n12 = 0;
      long l4 = 0L;
      int n13 = 0;
      int n14 = 0;
      Object obj11 = null;
      byte[] bytes2 = null;
      int n15 = 0;
      byte[] bytes3 = null;
      int n16 = 0;
      int n17 = 0;
      int n18 = 0;
      int n19 = 0;
      int n20 = 0;
      byte b2 = 0;
      Object obj12 = null;
      byte[] bytes4 = null;
      long l5 = 0L;
      int n21 = 0;
      int n22 = 0;
      int n23 = 0;
      int n24 = 0;
      long l6 = 0L;
      int n25 = 0;
      int n26 = 0;
      int n27 = 0;
      Object obj13 = null;
      byte b3 = 19;
      long l7 = 0L;
      l7 = 5139743428587397741L ^ (long)"᩶੮\ue29aꡎ୵✘ᬦ碼薘튁㲏Ꝭ僈⬬Ⳮ\u1bf5棈ࣱᵎ蛽政뺑鸦햆⚊\ud8bd瘛".hashCode();

      outerLoop:
      while (true) {
         exitBlock: {
            short s;
            outerLoop2:
            while (true) {
               if (58 <= b3) {
                  if (58 >= b3) {
                     if (n3 >= n10) {
                        l7 = (l7 ^ 5481035037060218813L) - -6933993249282215279L;
                        continue;
                     }

                     l7 = l7 + 6811980683246409234L ^ 1024459080314817956L;
                  } else if (68 <= b3) {
                     if (68 >= b3) {
                        if (l6 >= 0L) {
                           l7 = l7 - -8319536774034530432L ^ -2907002036957094139L;
                           break exitBlock;
                        }

                        l7 = l7 - 5150731881139570488L ^ 551070841678055970L;
                     } else if (b3 >= 73) {
                        if (b3 <= 73) {
                           if (n3 < n10) {
                              l7 = (l7 ^ 522743620893075034L) - 1214121588810989579L;
                              continue;
                           }

                           l7 = (l7 ^ 634309205051052434L) - -578053211349629083L;
                        } else if (b3 >= 75) {
                           if (b3 > 75) {
                              if (76 >= b3) {
                                 if (n15 >= 4) {
                                    l7 = l7 - -7374037670938930460L ^ 6309961589670709392L;
                                 } else {
                                    l7 = (l7 ^ 6787295884202048L) - -7833260438207363002L;
                                 }
                              } else {
                                 n15 = 0;
                                 l7 = l7 - 4044968152298124656L ^ 1901613182639529045L;
                              }
                              continue;
                           }

                           n9--;
                           l7 = l7 - 5225150139207118204L ^ -8097777423521322441L;
                        } else {
                           if (n14 >= n8) {
                              l7 = (l7 ^ -7652214810768451574L) + -6061570459798408519L;
                              continue;
                           }

                           l7 = (l7 ^ 3373894693010713739L) - 4856082109488585724L;
                        }
                     } else {
                        if (70 <= b3) {
                           if (b3 <= 70) {
                              l7 = (l7 ^ 1504027841823036144L) - -9008079695498038632L;
                              continue;
                           }

                           if (71 < b3) {
                              bytes4[n9] = (byte)((int)(l & 255L));
                              l7 = l7 - -810478685431003997L ^ 1546189964768618175L;
                              continue;
                           }

                           s = 1;
                           break;
                        }

                        if (n9 >= 32) {
                           l7 = l7 + -6647165011218938700L ^ -1926263844565134812L;
                           continue;
                        }

                        l7 = l7 - -8248432688997494962L ^ -2164212704004001788L;
                     }
                  } else if (63 <= b3) {
                     if (63 >= b3) {
                        if (str.isEmpty()) {
                           l7 = (l7 ^ 5766116219251878775L) - -8629211057661706887L;
                           continue;
                        }

                        l7 = l7 + -8333987585288814379L ^ -2741731311597684824L;
                     } else {
                        if (b3 >= 65) {
                           if (65 >= b3) {
                              if (str.charAt(n10 - 1) != '=') {
                                 l7 = l7 - -9086283376263563386L ^ 598256323479120555L;
                              } else {
                                 l7 = l7 + -2617708569173339672L ^ 5333223432837398008L;
                              }
                           } else if (66 >= b3) {
                              n23--;
                              l7 = (l7 ^ -6608396563132633396L) - 3393413398263696926L;
                           } else {
                              n15++;
                              l7 = (l7 ^ 712359469213378096L) + 8985005614710142919L;
                           }
                           continue;
                        }

                        if (n5 >= 32) {
                           l7 = (l7 ^ -6875217524817452336L) - 6621072121241811545L;
                        } else {
                           l7 = (l7 ^ 555487148344811025L) - -1486197539144414811L;
                        }
                     }
                  } else {
                     if (60 > b3) {
                        n8 = n10 * 3 / 4;
                        l7 = l7 + -6222044886841285095L ^ -5545986898905204278L;
                        continue;
                     }

                     if (b3 <= 60) {
                        str = HandshakeKeyTable.CLIENT_KEYS[n26];
                        l7 = l7 + 3493741923298469204L ^ 7458269279703767304L;
                     } else {
                        if (b3 <= 61) {
                           bytes[n14++] = (byte)(n27 >>> 16 & 0xFF);
                           l7 = l7 - -586386471315261704L ^ -4352205299537468651L;
                           continue;
                        }

                        l6 = (l6 * 1103515245L + 12345L) % (long)n24;
                        l7 = (l7 ^ 8908997335182297468L) + -2647714167219433741L;
                     }
                  }
               } else if (48 <= b3) {
                  if (48 >= b3) {
                     l7 = l7 - -1803109543953777874L ^ 5690029928745849913L;
                  } else if (53 <= b3) {
                     if (b3 <= 53) {
                        l5 = System.nanoTime();
                        l7 = (l7 ^ -5658994944258428035L) + 2655507201334768743L;
                        continue;
                     }

                     if (55 > b3) {
                        l2 >>= 8;
                        l7 = (l7 ^ -147970117011575352L) + 7158253307156404210L;
                        continue;
                     }

                     if (55 >= b3) {
                        l4 = System.currentTimeMillis();
                        l7 = (l7 ^ -324430720442138463L) + -5575723738186351680L;
                     } else if (b3 <= 56) {
                        if (n14 >= n8) {
                           l7 = l7 + -7090440970083682145L ^ -6057804621218614820L;
                           continue;
                        }

                        l7 = l7 + 2501950423741516531L ^ 930342085601173375L;
                     } else {
                        n14 = 0;
                        l7 = (l7 ^ 7776294851388425193L) + 5237363073237384320L;
                     }
                  } else {
                     if (b3 < 50) {
                        bytes3[b / 2] = bytes4[b];
                        l7 = l7 + 325770578781227850L ^ 7900388916541015982L;
                        continue;
                     }

                     if (b3 <= 50) {
                        l >>= 8;
                        l7 = (l7 ^ -428765861210828252L) + -8107553654607886389L;
                        continue;
                     }

                     if (b3 > 51) {
                        n3++;
                        l7 = (l7 ^ -8206958864521653164L) - -3499696055425303756L;
                        continue;
                     }

                     l7 = (l7 ^ 91048429561176318L) - -8349293246774057442L;
                  }
               } else if (b3 >= 43) {
                  if (b3 <= 43) {
                     l6 *= -1L;
                     l7 = (l7 ^ -3657040511721587290L) - -4054647245949344425L;
                  } else {
                     if (b3 < 45) {
                        bytes = new byte[n8];
                        l7 = (l7 ^ -4191513746479961737L) + -6479916458835786808L;
                        continue;
                     }

                     if (b3 <= 45) {
                        l6 = (l6 * 1103515245L + 12345L) % (long)n4;
                        l7 = l7 + 762553839991595873L ^ -5162725637846098245L;
                        continue;
                     }

                     if (b3 <= 46) {
                        n10--;
                        l7 = l7 + -5908050494144754702L ^ -3628008233828330029L;
                        continue;
                     }

                     intsArr = new int[0][0];
                     l7 = (l7 ^ 6795526822664367349L) - 6806334928389203303L;
                  }
               } else {
                  if (b3 >= 40) {
                     if (40 < b3) {
                        if (b3 <= 41) {
                           n27 = 0;
                           l7 = l7 - -3843353999131575872L ^ -5371051787673639863L;
                        } else {
                           l7 = (l7 ^ 4441408797479790306L) - -3708013152464459207L;
                        }
                        continue;
                     }

                     if ((bytes[n27++] & 1) == 1) {
                        l7 = l7 - -2988435521475116394L ^ 7628792690349441776L;
                        continue;
                     }

                     l7 = l7 - -7968669338042963299L ^ 3241538908964907753L;
                     s = 0;
                     break;
                  }

                  bytes2[b / 2] = bytes4[b + 1];
                  l7 = l7 + 7873391468977473146L ^ -4951219175325488075L;
               }

               while (true) {
                  while (b3 < 18) {
                     if (b3 >= 8) {
                        if (8 >= b3) {
                           n10--;
                           l7 = l7 - 3020729961569671748L ^ -7384864145655880238L;
                        } else if (b3 >= 13) {
                           if (b3 <= 13) {
                              bytes[n14++] = (byte)(n27 & 0xFF);
                              l7 = (l7 ^ 6683589778034650396L) + 5300732234789551386L;
                              continue outerLoop2;
                           }

                           if (15 > b3) {
                              b += 2;
                              l7 = l7 - -5847523084743373236L ^ 4474500401732151570L;
                              continue outerLoop2;
                           }

                           if (15 < b3) {
                              if (b3 <= 16) {
                                 bytes4[n23] = (byte)((int)(l2 & 255L));
                                 l7 = l7 + -1300605359425904258L ^ -901545819689431292L;
                              } else {
                                 n3 = 0;
                                 l7 = l7 + 7271060874997513280L ^ 4902543781000940432L;
                              }
                              continue outerLoop2;
                           }

                           if (b < 31) {
                              l7 = l7 - 5025296781106214170L ^ -8206349651061167132L;
                              continue outerLoop2;
                           }

                           l7 = (l7 ^ -3070343446093653374L) - -845387907297494597L;
                        } else if (b3 >= 10) {
                           if (b3 <= 10) {
                              if (n23 < 40) {
                                 l7 = (l7 ^ -5948879277305201435L) - -6510439541460848988L;
                              } else {
                                 l7 = (l7 ^ -2758730323562202910L) - -1417511906283011620L;
                              }
                           } else {
                              if (11 < b3) {
                                 l7 = (l7 ^ 740277256982515734L) + 261041769227368013L;
                                 break outerLoop;
                              }

                              bytes4 = new byte[56];
                              l7 = l7 + 4803220323971133256L ^ 6073082513134814289L;
                           }
                        } else {
                           n5++;
                           l7 = l7 - 264275088912902693L ^ -7329954803064012626L;
                        }
                     } else if (3 <= b3) {
                        if (b3 <= 3) {
                           if (l6 < 0L) {
                              l7 = (l7 ^ 4866096626083899200L) + 5664586371218375768L;
                              continue outerLoop2;
                           }

                           l7 = (l7 ^ 6377881324209629947L) + -5278741184238018098L;
                        } else if (5 <= b3) {
                           if (b3 <= 5) {
                              l2 = l5;
                              l7 = l7 - -1236840962349408770L ^ -2480504136577230217L;
                           } else {
                              if (b3 <= 6) {
                                 n24 = b - b2 + 1;
                                 l7 = (l7 ^ -7553935811604260093L) - 5554217382022204277L;
                                 continue outerLoop2;
                              }

                              s = 31680;
                              bytes2 = new byte[32];
                              l7 = (l7 ^ 3408632456183452148L) - 6577912556595936364L;
                           }
                        } else {
                           bytes4[n5] = (byte)((byte)((int)l6) + b2);
                           l7 = (l7 ^ 9135017858683199259L) + 8734621247199848445L;
                        }
                     } else {
                        if (b3 < 1) {
                           l6 *= -1L;
                           l7 = l7 - 3422960021021106460L ^ -1396868809514653173L;
                           break exitBlock;
                        }

                        if (b3 > 1) {
                           n4 = n11 - n23 + 1;
                           l7 = l7 - 2978325441077330086L ^ 3059209158624238268L;
                           continue outerLoop2;
                        }

                        n23 = 47;
                        l7 = (l7 ^ -2405210721168582506L) + -6670937967147810423L;
                     }
                  }

                  if (18 >= b3) {
                     l7 = (l7 ^ -3090860793399179011L) + -5762010599767741999L;
                     break;
                  }

                  if (b3 >= 28) {
                     if (b3 <= 28) {
                        if (str == null) {
                           l7 = l7 - -1954823722216498989L ^ -8888170497782247380L;
                        } else {
                           l7 = (l7 ^ -5466418415989917373L) - -3143720634532222506L;
                        }
                        break;
                     }

                     if (b3 >= 33) {
                        if (b3 <= 33) {
                           n5 = 0;
                           l7 = l7 - 1060192852764072231L ^ -4597118601686432073L;
                           break;
                        }

                        if (b3 < 35) {
                           if (n10 <= 0) {
                              l7 = (l7 ^ 8745150756469300084L) + 348658224105889052L;
                           } else {
                              l7 = l7 - -4047538343364548331L ^ 3524176652117695820L;
                           }
                           break;
                        }

                        if (b3 <= 35) {
                           n11 = HandshakeKeyTable.CLIENT_KEYS.length;
                           l7 = l7 - -5267443585953305813L ^ 6038424185968788698L;
                        } else if (b3 <= 36) {
                           n10 = str.length();
                           l7 = (l7 ^ 7432132259535852357L) - -7971281384615008296L;
                        } else {
                           l = l4;
                           l7 = l7 - 2302440046302132924L ^ -694499396476793178L;
                        }
                     } else if (30 <= b3) {
                        if (30 >= b3) {
                           b = 127;
                           l7 = l7 + -2442998318917000120L ^ 5260743142093063067L;
                        } else if (b3 <= 31) {
                           n23 = 0;
                           l7 = l7 + 5464469687462890771L ^ -7501480125997422331L;
                        } else if (str.charAt(n10 - 1) != '=') {
                           l7 = l7 + -8592900623889257783L ^ -1328154642802232813L;
                        } else {
                           l7 = l7 - -7876818572425375568L ^ 8563217183026948350L;
                        }
                     } else {
                        l6 = l4 ^ l5;
                        l7 = (l7 ^ 1579130078904597994L) - 7535645894091803864L;
                     }
                  } else if (23 <= b3) {
                     if (23 >= b3) {
                        bytes3 = new byte[32];
                        l7 = (l7 ^ -3048912948356139278L) - 4295961465324122180L;
                     } else {
                        if (25 > b3) {
                           n27 |= Base64VarIntCodec.BASE64_DECODE_TABLE[n] << 18 - 6 * n15;
                           l7 = (l7 ^ 7362320948296064898L) - -6342456728948780811L;
                           break;
                        }

                        if (25 >= b3) {
                           l7 = l7 - -7155485770361412753L ^ 5307573409986832540L;
                           break;
                        }

                        if (26 >= b3) {
                           n9 = 39;
                           l7 = (l7 ^ 6220976003818540767L) + 2845548385622398037L;
                           break;
                        }

                        b = 0;
                        l7 = l7 - -6589496360599749085L ^ 6708809219503185023L;
                     }
                  } else if (20 <= b3) {
                     if (20 < b3) {
                        if (21 >= b3) {
                           n27 = 0;
                           l7 = l7 + 2708502538253299326L ^ 4709377811505158461L;
                        } else {
                           bytes[n14++] = (byte)(n27 >>> 8 & 0xFF);
                           l7 = l7 - -8594226734695246522L ^ -5487310106340764147L;
                        }
                        break;
                     }

                     b2 = -128;
                     l7 = (l7 ^ 3320783901715392564L) + -7553697136946376811L;
                  } else {
                     n = str.charAt(n3);
                     l7 = l7 + 6251768144221119806L ^ -8769609263985780429L;
                  }
               }
            }

            boolean flag = (s != 0);
            obj11 = Base64VarIntCodec.readVarInt(bytes, n27);
            n18 = ((int[])obj11)[0];
            n27 = ((int[])obj11)[1];
            obj4 = new int[n18][];
            if (flag && n18 > 0) {
               obj11 = Base64VarIntCodec.readVarInt(bytes, n27);
               n25 = ((int[])obj11)[0];
               n27 = ((int[])obj11)[1];

               for (int i = 0; i < n18; i++) {
                  ((int[][])obj4)[i] = new int[n25];
               }

               for (int j = 0; j < n18; j++) {
                  for (int k = 0; k < n25; k++) {
                     obj11 = Base64VarIntCodec.readVarInt(bytes, n27);
                     ((int[][])obj4)[j][k] = ((int[])obj11)[0];
                     n27 = ((int[])obj11)[1];
                  }
               }
            } else {
               for (int m = 0; m < n18; m++) {
                  obj11 = Base64VarIntCodec.readVarInt(bytes, n27);
                  n17 = ((int[])obj11)[0];
                  n27 = ((int[])obj11)[1];
                  ((int[][])obj4)[m] = new int[n17];

                  for (int idx = 0; idx < n17; idx++) {
                     obj11 = Base64VarIntCodec.readVarInt(bytes, n27);
                     ((int[][])obj4)[m][idx] = ((int[])obj11)[0];
                     n27 = ((int[])obj11)[1];
                  }
               }
            }

            intsArr = (int[][])obj4;
            break;
         }

         n26 = (int)((long)n23 + l6);
         l7 = (l7 ^ 4632656843156732874L) - -7525571536850459757L;
      }

      obj13 = new byte[((int[][])intsArr).length];

      for (int index = 0; index < ((int[][])intsArr).length - 1; index += 2) {
         int[] ints = intsArr[index];
         obj = intsArr[index + 1];
         n3 = ints[0] >> 6;
         n27 = ints[1] >> 6;
         n15 = ints[2] >> 6;
         n = ((int[])obj)[0] >> 6;
         n18 = ((int[])obj)[1] >> 6;
         n12 = ((int[])obj)[2] >> 6;
         n25 = n3 * n;
         n17 = n27 * n;
         n20 = n15 * n;
         int n28 = n * n3;
         n21 = n18 * n3;
         n16 = n12 * n3;
         n2 = n21 - n17;
         n19 = n16 - n20;
         n13 = n19 / n2;
         n6 = n15 - n27 * n13;
         n22 = n6 / n3;
         ((byte[])obj13)[index] = (byte)n22;
         ((byte[])obj13)[index + 1] = (byte)n13;
      }

      if (((byte[])obj13).length % 2 != 0) {
         ((byte[])obj13)[((byte[])obj13).length - 1] = (byte)intsArr[((int[][])intsArr).length - 1][0];
      }

      obj8 = ShadedByteBuffer.wrap((byte[])obj13);
      n7 = (((ShadedByteBuffer)obj8).get() & 255) << 24
         | (((ShadedByteBuffer)obj8).get() & 255) << 16
         | (((ShadedByteBuffer)obj8).get() & 255) << 8
         | ((ShadedByteBuffer)obj8).get() & 255;
      obj = new byte[n7];
      ShadedByteBuffer shadedByteBuffer = (ShadedByteBuffer)obj8;
      byte[] bytes5 = (byte[])obj;
      shadedByteBuffer.get(bytes5);
      obj2 = new ShadedBigInteger((byte[])obj);
      n7 = (((ShadedByteBuffer)obj8).get() & 255) << 24
         | (((ShadedByteBuffer)obj8).get() & 255) << 16
         | (((ShadedByteBuffer)obj8).get() & 255) << 8
         | ((ShadedByteBuffer)obj8).get() & 255;
      obj = new byte[n7];
      shadedByteBuffer = (ShadedByteBuffer)obj8;
      byte[] bytes6 = (byte[])obj;
      shadedByteBuffer.get(bytes6);
      obj12 = new ShadedBigInteger((byte[])obj);
      n15 = (((ShadedBigInteger)obj2).bitLength() + 7) / 8;
      short s2 = 18521;
      l3 = System.nanoTime();

      for (int i2 = 55; i2 >= 48; i2--) {
         bytes4[i2] = (byte)((int)(l3 & 255L));
         l3 >>= 8;
      }

      byte[] bytes7 = bytes4;
      obj4 = Pkcs1Padding.pad(bytes7, n15);
      obj6 = new ShadedBigInteger(1, (byte[])obj4);
      ShadedBigInteger shadedBigInteger = (ShadedBigInteger)obj6;
      ShadedBigInteger shadedBigInteger2 = (ShadedBigInteger)obj12;
      ShadedBigInteger shadedBigInteger3 = (ShadedBigInteger)obj2;
      ShadedBigInteger shadedBigInteger4 = shadedBigInteger2;
      obj7 = shadedBigInteger.modPow(shadedBigInteger4, shadedBigInteger3);
      ShadedBigInteger shadedBigInteger5 = (ShadedBigInteger)obj7;
      obj10 = shadedBigInteger5.toByteArray();
      if (((byte[])obj10)[0] == 0 && ((byte[])obj10).length > n7) {
         obj9 = new byte[((byte[])obj10).length - 1];
         System.arraycopy((byte[])obj10, 1, (byte[])obj9, 0, ((byte[])obj9).length);
         obj10 = (byte[])obj9;
      }

      if (((byte[])obj10).length < n7) {
         obj9 = new byte[n7];
         System.arraycopy((byte[])obj10, 0, (byte[])obj9, n7 - ((byte[])obj10).length, ((byte[])obj10).length);
         obj10 = (byte[])obj9;
      }

      obj3 = ShadedByteBuffer.allocate(1 + ((byte[])obj10).length);
      ShadedHeapByteBuffer shadedHeapByteBuffer = (ShadedHeapByteBuffer)obj3;
      byte b4 = 1;
      shadedHeapByteBuffer.put(b4);
      ShadedHeapByteBuffer shadedHeapByteBuffer2 = (ShadedHeapByteBuffer)obj3;
      byte[] bytes8 = (byte[])obj10;
      shadedHeapByteBuffer2.put2(bytes8);
      obj5 = ((ShadedHeapByteBuffer)obj3).array();
      LauncherClientHandler launcherClientHandler = this;
      C2SHandshakeInitPacket packet = new C2SHandshakeInitPacket((n26 ^ 116) << 5, (byte[])obj5);
      launcherClientHandler.sendPacket(packet);
      LauncherClientHandler launcherClientHandler2 = this;
      byte[] bytes9 = bytes2;
      launcherClientHandler2.inboundSessionKey = bytes9;
      LauncherClientHandler launcherClientHandler3 = this;
      byte[] bytes10 = bytes3;
      launcherClientHandler3.outboundSessionKey = bytes10;
      this.sleepBriefly();
      if (l5 < System.nanoTime()
         && l4 < System.currentTimeMillis()
         && l5 > 0L
         && l4 > 0L
         && System.nanoTime() > 0L
         && System.currentTimeMillis() > 0L) {
         LauncherClientHandler launcherClientHandler4 = this;
         boolean flag2 = false;
         launcherClientHandler4.handshakeCompleted = flag2;
      } else {
         LauncherClientHandler launcherClientHandler5 = this;
         boolean flag3 = true;
         launcherClientHandler5.handshakeCompleted = flag3;
      }
   }

   private static void updateResourcesDownloadText(String str, long l) {
      ElectricLauncherController.getInstance().infoText.setText("Resources download: " + str + "%");
   }

   public static String formatDecimal(double d, int n) {
      double d2 = (double)((int)Math.pow(10.0, (double)n));
      return String.valueOf((double)Math.round(d * d2) / d2);
   }

   public static void flushQueuedPacketsIfReady() {
      long l = 0L;
      l = 2642944991790040651L ^ (long)"又맂饗菬쨸়퓄厦嗶茩氌\uab1c\udc33ᥦ䥖蝼詥낢螫鱍䔚䕶\ue238⛎媀".hashCode();
      if (getInstance() == null) {
         l = (l ^ -8632073834291174243L) - -8882418271208740780L;
      } else {
         l = l + -5129206639868748358L ^ 5829998392733203603L;
         LauncherClientHandler instance2 = getInstance();
         instance2.flushOutboundQueue();
         l = (l ^ -184521238059119404L) - 5262840157763303892L;
      }
   }

   public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
      long l = a ^ 46830477429449L;
      long l2 = l ^ 104777076537036L;
      LauncherPacket packet = (LauncherPacket)msg;
      this.handleIncomingPacket(l2, ctx, packet);
   }

   private void clearDirectoryFiles(File file) {
      File[] files = null;
      int n = 0;
      int n2 = 0;
      File file2 = null;
      File[] files2 = null;
      byte b = 12;

      outerLoop:
      while (true) {
         if (b >= 9) {
            if (9 >= b) {
               n++;
            } else if (11 <= b) {
               if (b > 11) {
                  files = file.listFiles();
                  continue;
               }

               if (files != null) {
                  ;
               }
            } else if (file.exists()) {
               ;
            }
         } else {
            if (7 >= b) {
               file2.delete();
               continue;
            }

            if (n < n2) {
               ;
            }
         }

         while (b >= 2) {
            if (2 >= b) {
               file2 = files2[n];
               continue outerLoop;
            }

            if (b >= 4) {
               if (4 < b) {
                  return;
               }

               if (file.isDirectory()) {
                  continue outerLoop;
               }
            } else {
               files2 = files;
            }
         }

         if (0 >= b) {
            n2 = files2.length;
            n = 0;
         }
      }
   }

   public byte[] buildHardwareIdBytes() {
      String str = "00-00-00-00-00-00";
      FileStore fileStore = FileSystems.getDefault().getFileStores().iterator().next();
      Base64Encoder rfc46482 = Base64Codec.getRfc46482();
      String str2 = System.getenv("PROCESSOR_LEVEL")
         + System.getenv("NUMBER_OF_PROCESSORS")
         + System.getenv("PROCESSOR_IDENTIFIER")
         + File.listRoots()[0].getTotalSpace()
         + System.getenv("PROCESSOR_REVISION");

      try {
         StringBuilder sb = new StringBuilder()
            .append(str2)
            .append(System.getenv("PROCESSOR_LEVEL"))
            .append(System.getenv("PROCESSOR_ARCHITECTURE"))
            .append(System.getenv("HOME"))
            .append(System.getenv("NUMBER_OF_PROCESSORS"))
            .append(System.getenv("TMP"))
            .append(System.getenv("PROCESSOR_REVISION"))
            .append(str)
            .append(System.getenv("COMPUTERNAME"))
            .append(System.getenv("APPDATA"))
            .append(File.listRoots()[0].getTotalSpace())
            .append(System.getenv("DISPLAY"))
            .append(System.getenv("PROCESSOR_IDENTIFIER"))
            .append(System.getenv("USERNAME"))
            .append(System.getenv("HOMEPATH"));
         byte[] bytes = fileStore.toString().getBytes(StandardCharsets.UTF_8);
         sb = sb.append(rfc46482.encodeToString(bytes));
         byte[] bytes2 = fileStore.getAttribute("volume:vsn").toString().getBytes(StandardCharsets.UTF_8);
         return sb.append(rfc46482.encodeToString(bytes2)).toString().getBytes(StandardCharsets.UTF_8);
      } catch (IOException e) {
         return null;
      }
   }

   public String buildHardwareIdString() {
      String str = "00-00-00-00-00-00";
      FileStore fileStore = FileSystems.getDefault().getFileStores().iterator().next();
      Base64Encoder rfc46482 = Base64Codec.getRfc46482();
      String str2 = System.getenv("PROCESSOR_LEVEL")
         + System.getenv("NUMBER_OF_PROCESSORS")
         + System.getenv("PROCESSOR_IDENTIFIER")
         + File.listRoots()[0].getTotalSpace()
         + System.getenv("PROCESSOR_REVISION");

      try {
         StringBuilder sb = new StringBuilder()
            .append(str2)
            .append(System.getenv("PROCESSOR_LEVEL"))
            .append(System.getenv("PROCESSOR_ARCHITECTURE"))
            .append(System.getenv("HOME"))
            .append(System.getenv("NUMBER_OF_PROCESSORS"))
            .append(System.getenv("TMP"))
            .append(System.getenv("PROCESSOR_REVISION"))
            .append(str)
            .append(System.getenv("COMPUTERNAME"))
            .append(System.getenv("APPDATA"))
            .append(File.listRoots()[0].getTotalSpace())
            .append(System.getenv("DISPLAY"))
            .append(System.getenv("PROCESSOR_IDENTIFIER"))
            .append(System.getenv("USERNAME"))
            .append(System.getenv("HOMEPATH"));
         byte[] bytes = fileStore.toString().getBytes(StandardCharsets.UTF_8);
         sb = sb.append(rfc46482.encodeToString(bytes));
         byte[] bytes2 = fileStore.getAttribute("volume:vsn").toString().getBytes(StandardCharsets.UTF_8);
         return sb.append(rfc46482.encodeToString(bytes2)).toString();
      } catch (IOException e) {
         return null;
      }
   }

   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      this.channelInactive(ctx);
      if (!this.added) {
         System.exit(0);
      }
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      this.exceptionCaught(ctx, cause);
      ctx.close();
   }

   private void flushOutboundQueue() {
      if (this.isChannelOpen()) {
         this.packetQueueLock.readLock().lock();

         try {
            while (!this.outboundPacketQueue.isEmpty()) {
               LauncherPacket packet = this.outboundPacketQueue.poll();
               this.dispatchPacket(packet);
            }
         } finally {
            this.packetQueueLock.readLock().unlock();
         }
      }
   }

   private void handleRemoteClassResponse(S2CRemoteClassResponsePacket packet) {
      synchronized (RemoteClassFetcher.RESPONSE_LOCK) {
         byte[] classData = packet.getClassData();
         this.remoteClassResponseBytes = classData;
         RemoteClassFetcher.RESPONSE_LOCK.notifyAll();
      }
   }

   private void handleClassLoaderKey(S2CClassLoaderKeyPacket packet) {
      EncryptedClassLoader.serverClassLoaderKey = packet.getClassLoaderKey();
      Platform.runLater(ElectricLauncherMain.getStage()::close);
   }

   private void spinWaitNanos() {
      long l = System.nanoTime();
      while (l - System.nanoTime() == 0L) {
      }
   }

   public static LauncherClientHandler getInstance() {
      byte b = 2;
      long l = 0L;
      l = 4431694817191686250L ^ (long)"⪛¾ꖰ觠햽ࠧ馐ꇾ璑蜋\ue463\udd0a錮搪尛郎ꊗ낧䨩➠栩\ud9d7䧮ˬ".hashCode();

      while (true) {
         while (1 < b) {
            if (b > 2) {
               return instance;
            }

            if (instance == null) {
               l = (l ^ -4587546042054707259L) + -7111359119239823860L;
            } else {
               l = (l ^ -1041582466171344358L) + -8348852522986709264L;
            }
         }

         LauncherClientHandler launcherClientHandler = instance;
         if (ClassIntegrityWatchdog.matchesRegisteredInstance("1", launcherClientHandler)) {
            l = (l ^ 1249591944826871788L) + -3586695658819510481L;
         } else {
            l = (l ^ -1602875449330366361L) + -3235923124367721373L;
            LauncherClientHandler launcherClientHandler2 = instance;
            C2SRemoteClassRequestPacket packet = new C2SRemoteClassRequestPacket(instance.sessionToken, "net/l/");
            launcherClientHandler2.sendPacket(packet);
            l = (l ^ -7744044562147811602L) - -3967721576262509231L;
         }
      }
   }

   public void handleIncomingPacket(long l, ChannelHandlerContext ctx, LauncherPacket packet) {
      l = a ^ l;
      long l2 = l ^ 27960622061000L;
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      long l3 = 0L;
      Object obj3 = null;
      int n5 = 0;
      int n6 = 0;
      Object obj4 = null;
      Object obj5 = null;
      int n7 = 0;
      Object obj6 = null;
      int n8 = 0;
      Object obj7 = null;
      int n9 = 0;
      Object obj8 = null;
      byte b = 0;
      Object obj9 = null;
      Object obj10 = null;
      int n10 = 0;
      Object obj11 = null;
      Object obj12 = null;
      Object obj13 = null;
      long l4 = 0L;
      Object obj14 = null;
      int n11 = 0;
      boolean flag = false;
      boolean flag2 = false;
      int n12 = 0;
      Object obj15 = null;
      Object obj16 = null;
      Object obj17 = null;
      Object obj18 = null;
      Object obj19 = null;
      int n13 = 0;
      boolean flag3 = false;
      int n14 = 0;
      int n15 = 0;
      boolean flag4 = false;
      boolean flag5 = false;
      int n16 = 0;
      int n17 = 0;
      Object obj20 = null;
      int n18 = 0;
      Object obj21 = null;
      int n19 = 0;
      long l5 = 0L;
      int n20 = 0;
      int n21 = 0;
      Object obj22 = null;
      Object obj23 = null;
      int n22 = 0;
      boolean flag6 = false;
      int n23 = 0;
      int[][] intsArr = null;
      Object obj24 = null;
      byte[] bytes = null;
      boolean flag7 = false;
      Object obj25 = null;
      int n24 = 0;
      int n25 = 0;
      int n26 = 0;
      Object obj26 = null;
      int n27 = 0;
      int n28 = 0;
      int n29 = 0;
      int n30 = 0;
      Object obj27 = null;
      Object obj28 = null;
      Object obj29 = null;
      int n31 = 0;
      int n32 = 0;
      int n33 = 0;
      int n34 = 0;
      int n35 = 0;
      long l6 = 0L;
      boolean flag8 = false;
      Object obj30 = null;
      Object obj31 = null;
      Object obj32 = null;
      Object obj33 = null;
      Object obj34 = null;
      int n36 = 0;
      int n37 = 0;
      Object obj35 = null;
      Object obj36 = null;
      Object obj37 = null;
      Object obj38 = null;
      Object obj39 = null;
      int n38 = 0;
      int n39 = 0;
      int n40 = 0;
      int n41 = 0;
      String str = null;
      int n42 = 0;
      boolean flag9 = false;
      Object obj40 = null;
      Object obj41 = null;
      Object obj42 = null;
      byte b2 = 0;
      Object obj43 = null;
      long l7 = 0L;
      S2CHandshakeChallengePacket packet2 = null;
      long l8 = 0L;
      int n43 = 0;
      int n44 = 0;
      int n45 = 0;
      long l9 = 0L;
      Object obj44 = null;
      int n46 = 0;
      Object obj45 = null;
      Object obj46 = null;
      int n47 = 0;
      int n48 = 0;
      Object obj47 = null;
      Object obj48 = null;
      int n49 = 0;
      Object obj49 = null;
      Object obj50 = null;
      Object obj51 = null;
      int n50 = 0;
      Object obj52 = null;
      int n51 = 0;
      int n52 = 0;
      long l10 = 0L;
      int n53 = 0;
      Object obj53 = null;
      Object obj54 = null;
      byte b3 = 37;
      long l11 = 0L;
      l11 = 7927232453811536000L ^ (long)"\u0b8c妖铆䤙띧齞\udc53\ue6c3晦䌚陁圂\ue8bb샳穜㾖\uec16㔥촀殼ӎ\ue7a6".hashCode();
      this.stageGuard.enterStage4();
      l11 = (l11 ^ 3385500248120751163L) - 7482287870113965269L;

      outerLoop:
      while (true) {
         if (28 <= b3) {
            if (28 >= b3) {
               byte[] bytes2 = bytes;
               byte b4 = b;
               b += 1;
               bytes2[b4] = (byte)(n43 & 0xFF);
               l11 = l11 - 1235807754139326356L ^ 8105705599158312637L;
            } else if (33 <= b3) {
               if (33 >= b3) {
                  if (str.charAt(n27 - 1) == '=') {
                     l11 = l11 - 2247403822323687632L ^ 2662184758920250585L;
                     continue;
                  }

                  l11 = (l11 ^ -8523755616719706788L) - -8976129323644272235L;
               } else if (b3 >= 35) {
                  if (b3 > 35) {
                     if (b3 <= 36) {
                        packet2 = (S2CHandshakeChallengePacket)packet;
                        l11 = (l11 ^ -2718715730565951669L) - -7358343265248602505L;
                        continue;
                     }

                     if (!(packet instanceof S2CHandshakeChallengePacket)) {
                        l11 = (l11 ^ -4788510322761513412L) - 4015709183784311288L;
                        break;
                     }

                     l11 = (l11 ^ -2810913706094284852L) + -1397157081683893874L;
                     continue;
                  }

                  n27 -= 1;
                  l11 = l11 + 7004566796608574814L ^ 3436329888609528384L;
               } else {
                  byte[] bytes3 = bytes;
                  byte b5 = b;
                  b += 1;
                  bytes3[b5] = (byte)(n43 >>> 8 & 0xFF);
                  l11 = l11 - 8594630197343101473L ^ -6339609508467259862L;
               }
            } else if (b3 >= 30) {
               if (30 >= b3) {
                  if (str == null) {
                     l11 = l11 + -8421171081997088381L ^ 1568397339617607393L;
                     continue;
                  }

                  l11 = (l11 ^ -950462833306273653L) - -8089004406581996415L;
               } else if (31 >= b3) {
                  if (str.charAt(n27 - 1) != '=') {
                     l11 = (l11 ^ 767248074403785430L) - 2649432912120493067L;
                     continue;
                  }

                  l11 = l11 + 4813017296097323452L ^ -4733914320443701189L;
               } else {
                  n52 = 0;
                  l11 = l11 - 5351917632432990440L ^ -6758113258443419513L;
               }
            } else {
               n45 = packet2.getEncodedKeyIndex() >> 2 ^ 52;
               l11 = (l11 ^ -698712839782948303L) + -4260680165302426418L;
            }
         } else if (b3 >= 23) {
            if (23 >= b3) {
               if (n11 >= 4) {
                  l11 = (l11 ^ -7539794570702976286L) - -8248994353910214606L;
               } else {
                  l11 = l11 - -1964990876004946471L ^ 3295014894501549268L;
               }
            } else if (b3 >= 25) {
               if (b3 <= 25) {
                  n20 = n27 * 3 / 4;
                  l11 = l11 + -3317071799056613596L ^ 8525160705557261794L;
                  continue;
               }

               if (26 >= b3) {
                  if (b < n20) {
                     l11 = (l11 ^ -4059837358127093880L) - -5525178965484911393L;
                     continue;
                  }

                  l11 = l11 + -6421448881834077819L ^ 1972882806127226661L;
               } else {
                  n52 += 1;
                  l11 = l11 + 65648552176163675L ^ -6727841705613740198L;
               }
            } else {
               intsArr = new int[0][0];
               l11 = (l11 ^ -1467786875866614612L) + -4896518316478295188L;
            }
         } else if (20 <= b3) {
            if (b3 <= 20) {
               n43 |= Base64VarIntCodec.BASE64_DECODE_TABLE[n13] << 18 - 6 * n11;
               l11 = (l11 ^ 8352651527130220629L) - 4470459918962584034L;
            } else {
               if (21 >= b3) {
                  n13 = str.charAt(n52);
                  l11 = l11 - 2044863681843206137L ^ -2323491806052328942L;
                  continue;
               }

               bytes = new byte[n20];
               l11 = l11 + -2998248745028346854L ^ 6061824423970159655L;
            }
         } else {
            n43 = 0;
            l11 = (l11 ^ 6304944303617358076L) - 6587403487244836184L;
         }

         while (true) {
            if (b3 >= 8) {
               boolean flag10;
               if (8 >= b3) {
                  byte[] bytes4 = bytes;
                  int n54 = n43;
                  n43 += 1;
                  if ((bytes4[n54] & 1) == 1) {
                     l11 = (l11 ^ -616995516027981297L) - -687647808444579013L;
                     continue;
                  }

                  l11 = (l11 ^ 3375468774159936392L) - -3610590501172872012L;
                  flag10 = false;
               } else {
                  if (b3 < 13) {
                     if (10 <= b3) {
                        if (b3 <= 10) {
                           if (n52 < n27) {
                              l11 = (l11 ^ 1816111248726122063L) - 6428317801277394605L;
                              continue outerLoop;
                           }

                           l11 = l11 + -5367392104549819405L ^ -4458290953796344354L;
                        } else if (11 >= b3) {
                           if (b < n20) {
                              l11 = (l11 ^ 7512286007462424305L) + 1293146052586030137L;
                              continue outerLoop;
                           }

                           l11 = l11 - -8568207975361368990L ^ -9074593008913984629L;
                        } else {
                           if (n52 < n27) {
                              l11 = (l11 ^ -5921354630846515821L) + -2162740247069508763L;
                              continue outerLoop;
                           }

                           l11 = l11 - -1510169236692619283L ^ 5330603224043702573L;
                        }
                     } else {
                        if (str.isEmpty()) {
                           l11 = l11 - 1958243199523111693L ^ -3377082091038026215L;
                           continue outerLoop;
                        }

                        l11 = l11 - -6505022960439850100L ^ -4747188571181240574L;
                     }
                     continue;
                  }

                  if (b3 <= 13) {
                     str = HandshakeKeyTable.SERVER_KEYS[n45];
                     l11 = l11 + 2903360019029283582L ^ 1527968084404346966L;
                     continue outerLoop;
                  }

                  if (b3 < 15) {
                     l11 = (l11 ^ 2510256802818128902L) + 5996152784852840341L;
                     continue outerLoop;
                  }

                  if (15 >= b3) {
                     if (n27 <= 0) {
                        l11 = (l11 ^ -8466813452973329394L) - 3321526466443357494L;
                     } else {
                        l11 = (l11 ^ 6750960341964771790L) + 3781310401696322147L;
                     }
                     continue outerLoop;
                  }

                  if (b3 <= 16) {
                     n27 -= 1;
                     l11 = (l11 ^ -1429348284500781385L) + -2614515605992102041L;
                     continue outerLoop;
                  }

                  flag10 = true;
               }

               boolean flag11 = flag10;
               obj35 = Base64VarIntCodec.readVarInt(bytes, n43);
               n32 = ((int[])obj35)[0];
               n43 = ((int[])obj35)[1];
               obj53 = new int[n32][];
               if (flag11 && n32 > 0) {
                  obj35 = Base64VarIntCodec.readVarInt(bytes, n43);
                  n24 = ((int[])obj35)[0];
                  n43 = ((int[])obj35)[1];

                  for (int i = 0; i < n32; i += 1) {
                     ((int[][])obj53)[i] = new int[n24];
                  }

                  for (int j = 0; j < n32; j += 1) {
                     for (int k = 0; k < n24; k += 1) {
                        obj35 = Base64VarIntCodec.readVarInt(bytes, n43);
                        ((int[][])obj53)[j][k] = ((int[])obj35)[0];
                        n43 = ((int[])obj35)[1];
                     }
                  }
               } else {
                  for (int m = 0; m < n32; m += 1) {
                     obj35 = Base64VarIntCodec.readVarInt(bytes, n43);
                     n40 = ((int[])obj35)[0];
                     n43 = ((int[])obj35)[1];
                     ((int[][])obj53)[m] = new int[n40];

                     for (int idx = 0; idx < n40; idx += 1) {
                        obj35 = Base64VarIntCodec.readVarInt(bytes, n43);
                        ((int[][])obj53)[m][idx] = ((int[])obj35)[0];
                        n43 = ((int[])obj35)[1];
                     }
                  }
               }

               intsArr = (int[][])obj53;
               break;
            } else {
               if (3 <= b3) {
                  if (b3 > 3) {
                     if (b3 >= 5) {
                        if (b3 <= 5) {
                           b = 0;
                           l11 = l11 + -2129159678183845136L ^ 4254863992972640117L;
                        } else if (b3 <= 6) {
                           n11 = 0;
                           l11 = (l11 ^ -5716569615617151565L) - -4884952291127604068L;
                        } else {
                           n27 = str.length();
                           l11 = (l11 ^ 6635834944120184311L) - -1710019365142443434L;
                        }
                     } else {
                        byte[] bytes5 = bytes;
                        byte b6 = b;
                        b += 1;
                        bytes5[b6] = (byte)(n43 >>> 16 & 0xFF);
                        l11 = (l11 ^ -1251525634338984611L) - -7478568765841436027L;
                     }
                     continue outerLoop;
                  }

                  l11 = (l11 ^ 8241976243717046424L) + -7642834764832894089L;
                  break;
               }

               if (b3 >= 1) {
                  if (b3 > 1) {
                     n11 += 1;
                     l11 = l11 - 8497444656196169868L ^ 1567537911446721545L;
                     continue outerLoop;
                  }

                  l11 = l11 - -8562574603993734127L ^ 437047217209894253L;
               } else {
                  n43 = 0;
                  l11 = (l11 ^ -4105537871017650372L) - -6674251698576912882L;
               }
            }
         }

         obj12 = new byte[((int[][])intsArr).length];

         for (int index = 0; index < ((int[][])intsArr).length - 1; index += 2) {
            int[] ints = intsArr[index];
            obj9 = intsArr[index + 1];
            n52 = ints[0] >> 6;
            n43 = ints[1] >> 6;
            n11 = ints[2] >> 6;
            n13 = ((int[])obj9)[0] >> 6;
            n32 = ((int[])obj9)[1] >> 6;
            n26 = ((int[])obj9)[2] >> 6;
            n24 = n52 * n13;
            n40 = n43 * n13;
            n = n11 * n13;
            int n55 = n13 * n52;
            n46 = n32 * n52;
            n47 = n26 * n52;
            n39 = n46 - n40;
            n49 = n47 - n;
            n51 = n49 / n39;
            n41 = n11 - n43 * n51;
            n5 = n41 / n52;
            ((byte[])obj12)[index] = (byte)n5;
            ((byte[])obj12)[index + 1] = (byte)n51;
         }

         if (((byte[])obj12).length % 2 != 0) {
            ((byte[])obj12)[((byte[])obj12).length - 1] = (byte)intsArr[((int[][])intsArr).length - 1][0];
         }

         obj16 = ShadedByteBuffer.wrap((byte[])obj12);
         n14 = (((ShadedByteBuffer)obj16).get() & 255) << 24
            | (((ShadedByteBuffer)obj16).get() & 255) << 16
            | (((ShadedByteBuffer)obj16).get() & 255) << 8
            | ((ShadedByteBuffer)obj16).get() & 255;
         obj9 = new byte[n14];
         ShadedByteBuffer shadedByteBuffer = (ShadedByteBuffer)obj16;
         byte[] bytes6 = (byte[])obj9;
         shadedByteBuffer.get(bytes6);
         obj = new ShadedBigInteger((byte[])obj9);
         n14 = (((ShadedByteBuffer)obj16).get() & 255) << 24
            | (((ShadedByteBuffer)obj16).get() & 255) << 16
            | (((ShadedByteBuffer)obj16).get() & 255) << 8
            | ((ShadedByteBuffer)obj16).get() & 255;
         obj9 = new byte[n14];
         shadedByteBuffer = (ShadedByteBuffer)obj16;
         byte[] bytes7 = (byte[])obj9;
         shadedByteBuffer.get(bytes7);
         obj38 = new ShadedBigInteger((byte[])obj9);
         S2CHandshakeChallengePacket packet3 = packet2;
         obj44 = packet3.getEncryptedAesKey();
         obj35 = new byte[((byte[])obj44).length - 1];
         System.arraycopy((byte[])obj44, 1, (byte[])obj35, 0, ((byte[])obj35).length);
         n32 = (((ShadedBigInteger)obj).bitLength() + 7) / 8;
         obj47 = new ShadedBigInteger(1, (byte[])obj35);
         ShadedBigInteger shadedBigInteger = (ShadedBigInteger)obj47;
         ShadedBigInteger shadedBigInteger2 = (ShadedBigInteger)obj38;
         ShadedBigInteger shadedBigInteger3 = (ShadedBigInteger)obj;
         ShadedBigInteger shadedBigInteger4 = shadedBigInteger2;
         obj46 = shadedBigInteger.modPow(shadedBigInteger4, shadedBigInteger3);
         ShadedBigInteger shadedBigInteger5 = (ShadedBigInteger)obj46;
         obj42 = shadedBigInteger5.toByteArray();
         if (((byte[])obj42)[0] == 0 && ((byte[])obj42).length > n32) {
            obj3 = new byte[((byte[])obj42).length - 1];
            System.arraycopy((byte[])obj42, 1, (byte[])obj3, 0, ((byte[])obj3).length);
            obj42 = (byte[])obj3;
         }

         if (((byte[])obj42).length < n32) {
            obj3 = new byte[n32];
            System.arraycopy((byte[])obj42, 0, (byte[])obj3, n32 - ((byte[])obj42).length, ((byte[])obj42).length);
            obj42 = (byte[])obj3;
         }

         obj3 = Pkcs1Padding.unpad((byte[])obj42);
         obj31 = (byte[])obj3;
         short s = 2763;
         obj22 = packet2.getEncryptedPayload();
         obj41 = new byte[12];
         System.arraycopy((byte[])obj22, 0, (byte[])obj41, 0, ((byte[])obj41).length);
         n39 = ((byte[])obj22).length - ((byte[])obj41).length - 16;
         short s2 = 10696;
         obj17 = new byte[n39];
         System.arraycopy((byte[])obj22, ((byte[])obj41).length, (byte[])obj17, 0, n39);
         obj36 = new byte[16];
         System.arraycopy((byte[])obj22, ((byte[])obj41).length + n39, (byte[])obj36, 0, 16);
         byte b7;
         if (((byte[])obj31).length == 16) {
            b7 = 10;
         } else {
            int length = ((byte[])obj31).length;
            short s3 = 16874;
            if (length == 24) {
               b7 = 12;
            } else {
               b7 = 14;
            }
         }

         n5 = ((byte[])obj31).length / 4;
         n3 = 4 * (b7 + 1);
         obj7 = new int[n3];

         for (int i2 = 0; i2 < n5; i2 += 1) {
            ((int[])obj7)[i2] = (((byte[])obj31)[4 * i2] & 255) << 24
               | (((byte[])obj31)[4 * i2 + 1] & 255) << 16
               | (((byte[])obj31)[4 * i2 + 2] & 255) << 8
               | ((byte[])obj31)[4 * i2 + 3] & 255;
         }

         for (int i3 = n5; i3 < n3; i3 += 1) {
            n35 = ((int[])obj7)[i3 - 1];
            if (i3 % n5 == 0) {
               n35 = n35 << 8 & -256 | n35 >>> 24 & 0xFF;
               n35 = AES_SBOX[n35 >>> 24 & 0xFF] << 24 | AES_SBOX[n35 >>> 16 & 0xFF] << 16 | AES_SBOX[n35 >>> 8 & 0xFF] << 8 | AES_SBOX[n35 & 0xFF];
               n35 ^= AES_RCON[i3 / n5] << 24;
            } else if (n5 > 6 && i3 % n5 == 4) {
               n35 = AES_SBOX[n35 >>> 24 & 0xFF] << 24 | AES_SBOX[n35 >>> 16 & 0xFF] << 16 | AES_SBOX[n35 >>> 8 & 0xFF] << 8 | AES_SBOX[n35 & 0xFF];
            }

            ((int[])obj7)[i3] = ((int[])obj7)[i3 - n5] ^ n35;
         }

         obj39 = new int[n3 * 4];

         for (int i4 = 0; i4 < n3; i4 += 1) {
            ((int[])obj39)[4 * i4] = ((int[])obj7)[i4] >>> 24 & 0xFF;
            ((int[])obj39)[4 * i4 + 1] = ((int[])obj7)[i4] >>> 16 & 0xFF;
            ((int[])obj39)[4 * i4 + 2] = ((int[])obj7)[i4] >>> 8 & 0xFF;
            ((int[])obj39)[4 * i4 + 3] = ((int[])obj7)[i4] & 0xFF;
         }

         obj32 = new byte[16];
         obj13 = new int[16];

         for (int i5 = 0; i5 < 16; i5 += 1) {
            ((int[])obj13)[i5] = ((int[])obj39)[i5];
         }

         for (int i6 = 1; i6 < b7; i6 += 1) {
            for (int i7 = 0; i7 < 16; i7 += 1) {
               ((int[])obj13)[i7] = AES_SBOX[((int[])obj13)[i7]];
            }

            n18 = ((int[])obj13)[1];
            ((int[])obj13)[1] = ((int[])obj13)[5];
            ((int[])obj13)[5] = ((int[])obj13)[9];
            ((int[])obj13)[9] = ((int[])obj13)[13];
            ((int[])obj13)[13] = n18;
            n18 = ((int[])obj13)[2];
            ((int[])obj13)[2] = ((int[])obj13)[10];
            ((int[])obj13)[10] = n18;
            n18 = ((int[])obj13)[6];
            ((int[])obj13)[6] = ((int[])obj13)[14];
            ((int[])obj13)[14] = n18;
            n18 = ((int[])obj13)[15];
            ((int[])obj13)[15] = ((int[])obj13)[11];
            ((int[])obj13)[11] = ((int[])obj13)[7];
            ((int[])obj13)[7] = ((int[])obj13)[3];
            ((int[])obj13)[3] = n18;

            for (int i8 = 0; i8 < 4; i8 += 1) {
               n33 = ((int[])obj13)[i8 * 4];
               n6 = ((int[])obj13)[i8 * 4 + 1];
               n48 = ((int[])obj13)[i8 * 4 + 2];
               n21 = ((int[])obj13)[i8 * 4 + 3];
               n38 = (n33 & 128) != 0 ? (n33 << 1 ^ 27) & 0xFF : n33 << 1 & 0xFF;
               n4 = (n6 & 128) != 0 ? (n6 << 1 ^ 27) & 0xFF : n6 << 1 & 0xFF;
               n38 ^= n4 ^ n6 ^ n48 ^ n21;
               n4 = (n6 & 128) != 0 ? (n6 << 1 ^ 27) & 0xFF : n6 << 1 & 0xFF;
               n9 = n33 ^ n4;
               n4 = (n48 & 128) != 0 ? (n48 << 1 ^ 27) & 0xFF : n48 << 1 & 0xFF;
               n9 ^= n4 ^ n48 ^ n21;
               n37 = n33 ^ n6;
               n4 = (n48 & 128) != 0 ? (n48 << 1 ^ 27) & 0xFF : n48 << 1 & 0xFF;
               n37 ^= n4;
               n4 = (n21 & 128) != 0 ? (n21 << 1 ^ 27) & 0xFF : n21 << 1 & 0xFF;
               n37 ^= n4 ^ n21;
               n4 = (n33 & 128) != 0 ? (n33 << 1 ^ 27) & 0xFF : n33 << 1 & 0xFF;
               n31 = n4 ^ n33 ^ n6 ^ n48;
               n4 = (n21 & 128) != 0 ? (n21 << 1 ^ 27) & 0xFF : n21 << 1 & 0xFF;
               n31 ^= n4;
               ((int[])obj13)[i8 * 4] = n38;
               ((int[])obj13)[i8 * 4 + 1] = n9;
               ((int[])obj13)[i8 * 4 + 2] = n37;
               ((int[])obj13)[i8 * 4 + 3] = n31;
            }

            n36 = i6 * 16;

            for (int i9 = 0; i9 < 16; i9 += 1) {
               ((int[])obj13)[i9] = ((int[])obj13)[i9] ^ ((int[])obj39)[n36 + i9];
            }
         }

         for (int i10 = 0; i10 < 16; i10 += 1) {
            ((int[])obj13)[i10] = AES_SBOX[((int[])obj13)[i10]];
         }

         n8 = ((int[])obj13)[1];
         ((int[])obj13)[1] = ((int[])obj13)[5];
         ((int[])obj13)[5] = ((int[])obj13)[9];
         ((int[])obj13)[9] = ((int[])obj13)[13];
         ((int[])obj13)[13] = n8;
         n8 = ((int[])obj13)[2];
         ((int[])obj13)[2] = ((int[])obj13)[10];
         ((int[])obj13)[10] = n8;
         n8 = ((int[])obj13)[6];
         ((int[])obj13)[6] = ((int[])obj13)[14];
         ((int[])obj13)[14] = n8;
         n8 = ((int[])obj13)[15];
         ((int[])obj13)[15] = ((int[])obj13)[11];
         ((int[])obj13)[11] = ((int[])obj13)[7];
         ((int[])obj13)[7] = ((int[])obj13)[3];
         ((int[])obj13)[3] = n8;
         n18 = b7 * 16;

         for (int i11 = 0; i11 < 16; i11 += 1) {
            ((int[])obj13)[i11] = ((int[])obj13)[i11] ^ ((int[])obj39)[n18 + i11];
         }

         obj37 = new byte[16];

         for (int i12 = 0; i12 < 16; i12 += 1) {
            ((byte[])obj37)[i12] = (byte)((int[])obj13)[i12];
         }

         obj28 = null;
         n6 = obj28 != null ? ((Object[])obj28).length : 0;
         n48 = n6 + 15 & -16;
         n21 = ((byte[])obj17).length + 15 & -16;
         obj10 = new byte[n48 + n21 + 16];
         if (obj28 != null && n6 > 0) {
            System.arraycopy(obj28, 0, (byte[])obj10, 0, n6);
         }

         System.arraycopy((byte[])obj17, 0, (byte[])obj10, n48, ((byte[])obj17).length);
         n18 = n48 + n21;
         l7 = (long)n6 * 8L;
         l4 = (long)((byte[])obj17).length * 8L;
         short s4 = 7544;

         for (int i13 = 7; i13 >= 0; i13 -= 1) {
            ((byte[])obj10)[n18 + i13] = (byte)((int)(l7 & 255L));
            l7 >>>= 8;
            ((byte[])obj10)[n18 + 8 + i13] = (byte)((int)(l4 & 255L));
            l4 >>>= 8;
         }

         obj23 = new byte[16];

         for (int i14 = 0; i14 < ((byte[])obj10).length; i14 += 16) {
            for (int i15 = 0; i15 < 16 && i14 + i15 < ((byte[])obj10).length; i15 += 1) {
               ((byte[])obj23)[i15] = (byte)(((byte[])obj23)[i15] ^ ((byte[])obj10)[i14 + i15]);
            }

            obj21 = new byte[16];
            short s5 = 10696;
            obj27 = new byte[16];
            System.arraycopy((byte[])obj23, 0, (byte[])obj27, 0, 16);

            for (int i16 = 0; i16 < 16; i16 += 1) {
               for (int i17 = 0; i17 < 8; i17 += 1) {
                  if ((((byte[])obj37)[i16] & 1 << 7 - i17) != 0) {
                     for (int i18 = 0; i18 < 16; i18 += 1) {
                        ((byte[])obj21)[i18] = (byte)(((byte[])obj21)[i18] ^ ((byte[])obj27)[i18]);
                     }
                  }

                  n19 = ((((byte[])obj27)[0] & 128) != 0) ? 1 : 0;

                  for (int i19 = 0; i19 < 15; i19 += 1) {
                     ((byte[])obj27)[i19] = (byte)((((byte[])obj27)[i19] & 255) << 1 | (((byte[])obj27)[i19 + 1] & 255) >>> 7);
                  }

                  ((byte[])obj27)[15] = (byte)((((byte[])obj27)[15] & 255) << 1);
                  if (n19 != 0) {
                     ((byte[])obj27)[15] = (byte)(((byte[])obj27)[15] ^ 135);
                  }
               }
            }

            System.arraycopy((byte[])obj21, 0, (byte[])obj23, 0, 16);
         }

         obj8 = new byte[16];
         System.arraycopy((byte[])obj41, 0, (byte[])obj8, 0, ((byte[])obj41).length);
         ((byte[])obj8)[15] = 1;
         obj21 = new int[16];

         for (int i20 = 0; i20 < 16; i20 += 1) {
            ((int[])obj21)[i20] = ((byte[])obj8)[i20] & 255;
         }

         for (int i21 = 0; i21 < 16; i21 += 1) {
            ((int[])obj21)[i21] = ((int[])obj21)[i21] ^ ((int[])obj39)[i21];
         }

         for (int i22 = 1; i22 < b7; i22 += 1) {
            for (int i23 = 0; i23 < 16; i23 += 1) {
               ((int[])obj21)[i23] = AES_SBOX[((int[])obj21)[i23]];
            }

            n8 = ((int[])obj21)[1];
            ((int[])obj21)[1] = ((int[])obj21)[5];
            ((int[])obj21)[5] = ((int[])obj21)[9];
            ((int[])obj21)[9] = ((int[])obj21)[13];
            ((int[])obj21)[13] = n8;
            n8 = ((int[])obj21)[2];
            ((int[])obj21)[2] = ((int[])obj21)[10];
            ((int[])obj21)[10] = n8;
            n8 = ((int[])obj21)[6];
            ((int[])obj21)[6] = ((int[])obj21)[14];
            ((int[])obj21)[14] = n8;
            n8 = ((int[])obj21)[15];
            ((int[])obj21)[15] = ((int[])obj21)[11];
            ((int[])obj21)[11] = ((int[])obj21)[7];
            ((int[])obj21)[7] = ((int[])obj21)[3];
            ((int[])obj21)[3] = n8;

            for (int i24 = 0; i24 < 4; i24 += 1) {
               n15 = ((int[])obj21)[i24 * 4];
               n19 = ((int[])obj21)[i24 * 4 + 1];
               n42 = ((int[])obj21)[i24 * 4 + 2];
               n34 = ((int[])obj21)[i24 * 4 + 3];
               n44 = (n15 & 128) != 0 ? (n15 << 1 ^ 27) & 0xFF : n15 << 1 & 0xFF;
               n17 = (n19 & 128) != 0 ? (n19 << 1 ^ 27) & 0xFF : n19 << 1 & 0xFF;
               n44 ^= n17 ^ n19 ^ n42 ^ n34;
               n17 = (n19 & 128) != 0 ? (n19 << 1 ^ 27) & 0xFF : n19 << 1 & 0xFF;
               n29 = n15 ^ n17;
               n17 = (n42 & 128) != 0 ? (n42 << 1 ^ 27) & 0xFF : n42 << 1 & 0xFF;
               n29 ^= n17 ^ n42 ^ n34;
               n22 = n15 ^ n19;
               n17 = (n42 & 128) != 0 ? (n42 << 1 ^ 27) & 0xFF : n42 << 1 & 0xFF;
               n22 ^= n17;
               n17 = (n34 & 128) != 0 ? (n34 << 1 ^ 27) & 0xFF : n34 << 1 & 0xFF;
               n22 ^= n17 ^ n34;
               n17 = (n15 & 128) != 0 ? (n15 << 1 ^ 27) & 0xFF : n15 << 1 & 0xFF;
               n30 = n17 ^ n15 ^ n19 ^ n42;
               n17 = (n34 & 128) != 0 ? (n34 << 1 ^ 27) & 0xFF : n34 << 1 & 0xFF;
               n30 ^= n17;
               ((int[])obj21)[i24 * 4] = n44;
               ((int[])obj21)[i24 * 4 + 1] = n29;
               ((int[])obj21)[i24 * 4 + 2] = n22;
               ((int[])obj21)[i24 * 4 + 3] = n30;
            }

            n18 = i22 * 16;

            for (int i25 = 0; i25 < 16; i25 += 1) {
               ((int[])obj21)[i25] = ((int[])obj21)[i25] ^ ((int[])obj39)[n18 + i25];
            }
         }

         for (int i26 = 0; i26 < 16; i26 += 1) {
            ((int[])obj21)[i26] = AES_SBOX[((int[])obj21)[i26]];
         }

         n8 = ((int[])obj21)[1];
         ((int[])obj21)[1] = ((int[])obj21)[5];
         ((int[])obj21)[5] = ((int[])obj21)[9];
         ((int[])obj21)[9] = ((int[])obj21)[13];
         ((int[])obj21)[13] = n8;
         n8 = ((int[])obj21)[2];
         ((int[])obj21)[2] = ((int[])obj21)[10];
         ((int[])obj21)[10] = n8;
         n8 = ((int[])obj21)[6];
         ((int[])obj21)[6] = ((int[])obj21)[14];
         ((int[])obj21)[14] = n8;
         n8 = ((int[])obj21)[15];
         ((int[])obj21)[15] = ((int[])obj21)[11];
         ((int[])obj21)[11] = ((int[])obj21)[7];
         ((int[])obj21)[7] = ((int[])obj21)[3];
         ((int[])obj21)[3] = n8;
         n18 = b7 * 16;

         for (int i27 = 0; i27 < 16; i27 += 1) {
            ((int[])obj21)[i27] = ((int[])obj21)[i27] ^ ((int[])obj39)[n18 + i27];
         }

         obj27 = new byte[16];

         for (int i28 = 0; i28 < 16; i28 += 1) {
            ((byte[])obj27)[i28] = (byte)((int[])obj21)[i28];
         }

         obj24 = new byte[16];

         for (int i29 = 0; i29 < 16; i29 += 1) {
            ((byte[])obj24)[i29] = (byte)(((byte[])obj23)[i29] ^ ((byte[])obj27)[i29]);
         }

         n15 = 0;

         for (int i30 = 0; i30 < 16; i30 += 1) {
            n15 |= ((byte[])obj36)[i30] ^ ((byte[])obj24)[i30];
         }

         obj34 = new byte[16];
         System.arraycopy((byte[])obj41, 0, (byte[])obj34, 0, ((byte[])obj41).length);
         ((byte[])obj34)[15] = 1;
         obj14 = new byte[n39];

         for (int i31 = 0; i31 < ((byte[])obj17).length; i31 += 16) {
            short s6 = 10696;
            obj43 = new int[16];

            for (int i32 = 0; i32 < 16; i32 += 1) {
               ((int[])obj43)[i32] = ((byte[])obj34)[i32] & 255;
            }

            for (int i33 = 0; i33 < 16; i33 += 1) {
               ((int[])obj43)[i33] = ((int[])obj43)[i33] ^ ((int[])obj39)[i33];
            }

            for (int i34 = 1; i34 < b7; i34 += 1) {
               for (int i35 = 0; i35 < 16; i35 += 1) {
                  ((int[])obj43)[i35] = AES_SBOX[((int[])obj43)[i35]];
               }

               n8 = ((int[])obj43)[1];
               ((int[])obj43)[1] = ((int[])obj43)[5];
               ((int[])obj43)[5] = ((int[])obj43)[9];
               ((int[])obj43)[9] = ((int[])obj43)[13];
               ((int[])obj43)[13] = n8;
               n8 = ((int[])obj43)[2];
               ((int[])obj43)[2] = ((int[])obj43)[10];
               ((int[])obj43)[10] = n8;
               n8 = ((int[])obj43)[6];
               ((int[])obj43)[6] = ((int[])obj43)[14];
               ((int[])obj43)[14] = n8;
               n8 = ((int[])obj43)[15];
               ((int[])obj43)[15] = ((int[])obj43)[11];
               ((int[])obj43)[11] = ((int[])obj43)[7];
               ((int[])obj43)[7] = ((int[])obj43)[3];
               ((int[])obj43)[3] = n8;

               for (int i36 = 0; i36 < 4; i36 += 1) {
                  n30 = ((int[])obj43)[i36 * 4];
                  n17 = ((int[])obj43)[i36 * 4 + 1];
                  n10 = ((int[])obj43)[i36 * 4 + 2];
                  n2 = ((int[])obj43)[i36 * 4 + 3];
                  n28 = (n30 & 128) != 0 ? (n30 << 1 ^ 27) & 0xFF : n30 << 1 & 0xFF;
                  n25 = (n17 & 128) != 0 ? (n17 << 1 ^ 27) & 0xFF : n17 << 1 & 0xFF;
                  n28 ^= n25 ^ n17 ^ n10 ^ n2;
                  n25 = (n17 & 128) != 0 ? (n17 << 1 ^ 27) & 0xFF : n17 << 1 & 0xFF;
                  n50 = n30 ^ n25;
                  n25 = (n10 & 128) != 0 ? (n10 << 1 ^ 27) & 0xFF : n10 << 1 & 0xFF;
                  n50 ^= n25 ^ n10 ^ n2;
                  n16 = n30 ^ n17;
                  n25 = (n10 & 128) != 0 ? (n10 << 1 ^ 27) & 0xFF : n10 << 1 & 0xFF;
                  n16 ^= n25;
                  n25 = (n2 & 128) != 0 ? (n2 << 1 ^ 27) & 0xFF : n2 << 1 & 0xFF;
                  n16 ^= n25 ^ n2;
                  n25 = (n30 & 128) != 0 ? (n30 << 1 ^ 27) & 0xFF : n30 << 1 & 0xFF;
                  n12 = n25 ^ n30 ^ n17 ^ n10;
                  n25 = (n2 & 128) != 0 ? (n2 << 1 ^ 27) & 0xFF : n2 << 1 & 0xFF;
                  n12 ^= n25;
                  ((int[])obj43)[i36 * 4] = n28;
                  ((int[])obj43)[i36 * 4 + 1] = n50;
                  ((int[])obj43)[i36 * 4 + 2] = n16;
                  ((int[])obj43)[i36 * 4 + 3] = n12;
               }

               n18 = i34 * 16;

               for (int i37 = 0; i37 < 16; i37 += 1) {
                  ((int[])obj43)[i37] = ((int[])obj43)[i37] ^ ((int[])obj39)[n18 + i37];
               }
            }

            for (int i38 = 0; i38 < 16; i38 += 1) {
               ((int[])obj43)[i38] = AES_SBOX[((int[])obj43)[i38]];
            }

            n8 = ((int[])obj43)[1];
            ((int[])obj43)[1] = ((int[])obj43)[5];
            ((int[])obj43)[5] = ((int[])obj43)[9];
            ((int[])obj43)[9] = ((int[])obj43)[13];
            ((int[])obj43)[13] = n8;
            n8 = ((int[])obj43)[2];
            ((int[])obj43)[2] = ((int[])obj43)[10];
            ((int[])obj43)[10] = n8;
            n8 = ((int[])obj43)[6];
            ((int[])obj43)[6] = ((int[])obj43)[14];
            ((int[])obj43)[14] = n8;
            n8 = ((int[])obj43)[15];
            ((int[])obj43)[15] = ((int[])obj43)[11];
            ((int[])obj43)[11] = ((int[])obj43)[7];
            ((int[])obj43)[7] = ((int[])obj43)[3];
            ((int[])obj43)[3] = n8;
            n18 = b7 * 16;

            for (int i39 = 0; i39 < 16; i39 += 1) {
               ((int[])obj43)[i39] = ((int[])obj43)[i39] ^ ((int[])obj39)[n18 + i39];
            }

            n29 = Math.min(16, ((byte[])obj17).length - i31);

            for (int i40 = 0; i40 < n29; i40 += 1) {
               ((byte[])obj14)[i31 + i40] = (byte)(((byte[])obj17)[i31 + i40] ^ (byte)((int[])obj43)[i40]);
            }

            n22 = ((byte[])obj34).length - 1;

            while (n22 >= 0 && ++((byte[])obj34)[n22] == 0) {
               n22 -= 1;
            }
         }

         l6 = System.nanoTime();
         short s7 = 19614;
         l5 = System.currentTimeMillis();
         l3 = l5 ^ l6;
         obj18 = new byte[56];

         for (int i41 = 0; i41 < 32; i41 += 1) {
            byte b8 = -128;
            short s8 = 19725;
            byte b9 = 127;
            n16 = b9 - b8 + 1;
            l3 = (l3 * 1103515245L + 12345L) % (long)n16;
            if (l3 < 0L) {
               l3 *= -1L;
            }

            ((byte[])obj18)[i41] = (byte)((byte)((int)l3) + b8);
         }

         obj48 = new byte[32];
         obj19 = new byte[32];

         for (int i42 = 0; i42 < 31; i42 += 2) {
            ((byte[])obj48)[i42 / 2 + 16] = ((byte[])obj18)[i42];
            ((byte[])obj19)[i42 / 2 + 16] = ((byte[])obj18)[i42 + 1];
         }

         l10 = l5;

         for (int i43 = 39; i43 >= 32; i43 -= 1) {
            ((byte[])obj18)[i43] = (byte)((int)(l10 & 255L));
            l10 >>= 8;
         }

         l9 = l6;

         for (int i44 = 47; i44 >= 40; i44 -= 1) {
            ((byte[])obj18)[i44] = (byte)((int)(l9 & 255L));
            l9 >>= 8;
         }

         obj45 = ShadedByteBuffer.wrap((byte[])obj14);
         n23 = (((ShadedByteBuffer)obj45).get() & 255) << 24
            | (((ShadedByteBuffer)obj45).get() & 255) << 16
            | (((ShadedByteBuffer)obj45).get() & 255) << 8
            | ((ShadedByteBuffer)obj45).get() & 255;
         obj5 = new byte[n23];
         shadedByteBuffer = (ShadedByteBuffer)obj45;
         byte[] bytes8 = (byte[])obj5;
         shadedByteBuffer.get(bytes8);
         obj54 = new ShadedBigInteger((byte[])obj5);
         n23 = (((ShadedByteBuffer)obj45).get() & 255) << 24
            | (((ShadedByteBuffer)obj45).get() & 255) << 16
            | (((ShadedByteBuffer)obj45).get() & 255) << 8
            | ((ShadedByteBuffer)obj45).get() & 255;
         obj5 = new byte[n23];
         shadedByteBuffer = (ShadedByteBuffer)obj45;
         byte[] bytes9 = (byte[])obj5;
         shadedByteBuffer.get(bytes9);
         obj2 = new ShadedBigInteger((byte[])obj5);
         n7 = (((ShadedBigInteger)obj54).bitLength() + 7) / 8;
         short s9 = 15877;
         l8 = System.nanoTime();

         for (int i45 = 55; i45 >= 48; i45 -= 1) {
            ((byte[])obj18)[i45] = (byte)((int)(l8 & 255L));
            l8 >>= 8;
         }

         for (int i46 = 0; i46 < 16; i46 += 1) {
            ((byte[])obj48)[i46] = this.outboundSessionKey[i46];
         }

         for (int i47 = 0; i47 < 16; i47 += 1) {
            ((byte[])obj19)[i47] = this.inboundSessionKey[i47];
         }

         obj52 = new byte[n7];
         ((byte[])obj52)[0] = 0;
         ((byte[])obj52)[1] = 2;
         obj6 = new EntropyXorShiftRandom();
         n53 = n7 - ((byte[])obj18).length - 3;

         for (int i48 = 0; i48 < n53; i48 += 1) {
            do {
               obj50 = new byte[1];
               EntropyXorShiftRandom entropyXorShiftRandom = (EntropyXorShiftRandom)obj6;
               byte[] bytes10 = (byte[])obj50;
               entropyXorShiftRandom.nextBytes(bytes10);
               b2 = ((byte[])obj50)[0];
            } while (b2 == 0);

            ((byte[])obj52)[i48 + 2] = b2;
         }

         ((byte[])obj52)[n53 + 2] = 0;
         System.arraycopy((byte[])obj18, 0, (byte[])obj52, n53 + 3, ((byte[])obj18).length);
         obj26 = new ShadedBigInteger(1, (byte[])obj52);
         ShadedBigInteger shadedBigInteger6 = (ShadedBigInteger)obj26;
         ShadedBigInteger shadedBigInteger7 = (ShadedBigInteger)obj2;
         ShadedBigInteger shadedBigInteger8 = (ShadedBigInteger)obj54;
         ShadedBigInteger shadedBigInteger9 = shadedBigInteger7;
         obj11 = shadedBigInteger6.modPow(shadedBigInteger9, shadedBigInteger8);
         ShadedBigInteger shadedBigInteger10 = (ShadedBigInteger)obj11;
         obj50 = shadedBigInteger10.toByteArray();
         if (((byte[])obj50)[0] == 0 && ((byte[])obj50).length > n23) {
            obj49 = new byte[((byte[])obj50).length - 1];
            System.arraycopy((byte[])obj50, 1, (byte[])obj49, 0, ((byte[])obj49).length);
            obj50 = (byte[])obj49;
         }

         if (((byte[])obj50).length < n23) {
            obj49 = new byte[n23];
            System.arraycopy((byte[])obj50, 0, (byte[])obj49, n23 - ((byte[])obj50).length, ((byte[])obj50).length);
            obj50 = (byte[])obj49;
         }

         obj51 = ShadedByteBuffer.allocate(1 + ((byte[])obj50).length);
         ShadedHeapByteBuffer shadedHeapByteBuffer = (ShadedHeapByteBuffer)obj51;
         byte b10 = 1;
         shadedHeapByteBuffer.put(b10);
         ShadedHeapByteBuffer shadedHeapByteBuffer2 = (ShadedHeapByteBuffer)obj51;
         byte[] bytes11 = (byte[])obj50;
         shadedHeapByteBuffer2.put2(bytes11);
         obj29 = ((ShadedHeapByteBuffer)obj51).array();
         obj4 = CodeSourceIntegrityHasher.computeCodeSourceHash(this.sha256Digest);
         LauncherClientHandler launcherClientHandler = this;
         C2SHandshakeCompletePacket packet4 = new C2SHandshakeCompletePacket((byte[])obj29, (byte[])obj4);
         launcherClientHandler.sendPacket(packet4);
         this.packetCipherDecoder.setSessionKey((byte[])obj19);
         this.packetCipherEncoder.setSessionKey((byte[])obj48);
         this.sleepBriefly();
         if (l6 < System.nanoTime()
            && l5 < System.currentTimeMillis()
            && l6 > 0L
            && l5 > 0L
            && System.nanoTime() > 0L
            && System.currentTimeMillis() > 0L) {
            LauncherClientHandler launcherClientHandler2 = this;
            boolean flag12 = false;
            launcherClientHandler2.handshakeCompleted = flag12;
            break;
         }

         LauncherClientHandler launcherClientHandler3 = this;
         boolean flag13 = true;
         launcherClientHandler3.handshakeCompleted = flag13;
         break;
      }

      if (packet instanceof S2CLoginResponsePacket) {
         S2CLoginResponsePacket packet5 = (S2CLoginResponsePacket)packet;
         if (packet5.getStatusCode() == 0) {
            if (this.loggedIn) {
               return;
            }

            LauncherClientHandler launcherClientHandler4 = this;
            byte[] sessionToken2 = packet5.getSessionToken();
            launcherClientHandler4.sessionToken = sessionToken2;
            launcherClientHandler4 = this;
            byte[] sessionSecret2 = packet5.getSessionSecret();
            launcherClientHandler4.sessionSecret = sessionSecret2;
            launcherClientHandler4 = this;
            String availableClients = packet5.getAvailableClients();
            launcherClientHandler4.availableClientsList = availableClients;
            Text loginFailedText = ElectricLauncherController.getInstance().getLoginFailedText();
            S2CLoginResponsePacket packet6 = packet5;
            loginFailedText.setText(packet6.getMessage());
            ElectricLauncherController.getInstance().getLoginFailedText().setFill(Paint.valueOf("#22d451"));
            launcherClientHandler4 = this;
            boolean flag14 = true;
            launcherClientHandler4.loggedIn = flag14;
         } else {
            Text loginFailedText2 = ElectricLauncherController.getInstance().getLoginFailedText();
            S2CLoginResponsePacket packet7 = packet5;
            loginFailedText2.setText(packet7.getMessage());
            ElectricLauncherController.getInstance().getLoginFailedText().setFill(Paint.valueOf("#f70202"));
         }

         LauncherClientHandler launcherClientHandler5 = this;
         boolean flag15 = true;
         launcherClientHandler5.added = flag15;
      }

      if (packet instanceof S2CIntegrityChallengePacket) {
         S2CIntegrityChallengePacket packet8 = (S2CIntegrityChallengePacket)packet;
         Sha256Digest sha256Digest2 = this.sha256Digest;
         LauncherClientHandler launcherClientHandler6 = this;
         byte[] bytes12 = launcherClientHandler6.buildHardwareIdBytes();
         byte[] bytes13 = bytes12;
         obj25 = sha256Digest2.digest(bytes13);
         obj40 = AesCipherUtil.concatByteArrays(l2, new byte[][]{packet8.getChallengeNonce(), this.sessionToken, this.sessionSecret, (byte[])obj25});
         byte[] bytes14 = (byte[])obj40;
         byte[] bytes15 = bytes14;
         byte[] bytes16 = this.sha256Digest.digest(bytes15);
         obj20 = ClassIntegrityWatchdog.getClassHashes();
         C2SIntegrityProofPacket packet9 = new C2SIntegrityProofPacket(this.sessionToken, bytes16, (Map)obj20);
         this.sendPacket(packet9);
         obj33 = this.availableClientsList == null ? null : this.availableClientsList.split(":");
         if ((String[])obj33 != null && ((String[])obj33).length > 0) {
            ElectricLauncherController.getInstance().clientList.getItems().clear();
            Arrays.<String>stream((String[])obj33).forEach(str2 -> addClientListEntry(0L, str2));
            ElectricLauncherController.getInstance().clientList.getSelectionModel().select(0);
            ElectricLauncherController.getInstance().startClientButton.setVisible(true);
         } else {
            ElectricLauncherController.getInstance().startClientButton.setVisible(false);
            ElectricLauncherController.getInstance().startText.setText("All of your clients are expired. Please redeem a license key");
            ElectricLauncherController.getInstance().startText.setFill(Paint.valueOf("#f70202"));
         }

         ElectricLauncherController.getInstance().registerPane.setVisible(false);
         ElectricLauncherController.getInstance().clientTabPane.setVisible(true);
      }

      if (packet instanceof S2CRegisterResponsePacket) {
         S2CRegisterResponsePacket packet10 = (S2CRegisterResponsePacket)packet;
         ElectricLauncherController.getInstance().getRegisterFailedText().setText(packet10.getMessage());
         Text registerFailedText = ElectricLauncherController.getInstance().getRegisterFailedText();
         S2CRegisterResponsePacket packet11 = packet10;
         String str3;
         if (packet11.getStatusCode() == 1) {
            str3 = "#f70202";
         } else {
            short s10 = 19697;
            str3 = "#22d451";
         }

         registerFailedText.setFill(Paint.valueOf(str3));
         LauncherClientHandler launcherClientHandler7 = this;
         boolean flag16 = true;
         launcherClientHandler7.added = flag16;
      }

      if (packet instanceof S2CHwidResetResponsePacket) {
         S2CHwidResetResponsePacket packet12 = (S2CHwidResetResponsePacket)packet;
         ElectricLauncherController.getInstance().getHwidErrorMessage().setText(packet12.getMessage());
         Text hwidErrorMessage = ElectricLauncherController.getInstance().getHwidErrorMessage();
         S2CHwidResetResponsePacket packet13 = packet12;
         String str4;
         if (packet13.getStatusCode() == 1) {
            str4 = "#f70202";
         } else {
            short s11 = 19697;
            str4 = "#22d451";
         }

         hwidErrorMessage.setFill(Paint.valueOf(str4));
         LauncherClientHandler launcherClientHandler8 = this;
         boolean flag17 = true;
         launcherClientHandler8.added = flag17;
      }

      if (packet instanceof S2CActivateClientResponsePacket) {
         S2CActivateClientResponsePacket packet14 = (S2CActivateClientResponsePacket)packet;
         ElectricLauncherController.getInstance().activateClientResponse.setText(packet14.getMessage());
         Text activateClientResponse = ElectricLauncherController.getInstance().activateClientResponse;
         S2CActivateClientResponsePacket packet15 = packet14;
         String str5;
         if (packet15.getStatusCode() == 1) {
            str5 = "#f70202";
         } else {
            short s12 = 19697;
            str5 = "#22d451";
         }

         activateClientResponse.setFill(Paint.valueOf(str5));
      }

      if (packet instanceof S2CResourceChunkPacket) {
         S2CResourceChunkPacket packet16 = (S2CResourceChunkPacket)packet;
         obj25 = packet16.getNativesChunk();
         obj40 = packet16.getResourcesChunk();
         if (this.resourcesJarBuffer == null && ((byte[])obj40).length > 1) {
            LauncherClientHandler launcherClientHandler9 = this;
            byte[] bytes17 = new byte[packet16.getResourcesTotalSize()];
            launcherClientHandler9.resourcesJarBuffer = bytes17;
         } else {
            LauncherClientHandler launcherClientHandler10 = this;
            boolean flag18 = true;
            launcherClientHandler10.resourcesReady = flag18;
         }

         if (this.nativesArchiveBuffer == null && ((byte[])obj25).length > 1) {
            LauncherClientHandler launcherClientHandler11 = this;
            byte[] bytes18 = new byte[packet16.getNativesTotalSize()];
            launcherClientHandler11.nativesArchiveBuffer = bytes18;
         } else {
            LauncherClientHandler launcherClientHandler12 = this;
            boolean flag19 = true;
            launcherClientHandler12.nativesReady = flag19;
         }

         if (this.resourcesJarBuffer != null) {
            System.arraycopy((byte[])obj40, 0, this.resourcesJarBuffer, this.resourcesBytesReceived, ((byte[])obj40).length);
            int n56 = this.resourcesBytesReceived + ((byte[])obj40).length;
            this.resourcesBytesReceived = n56;
            if (this.resourcesBytesReceived == this.resourcesJarBuffer.length) {
               StringBuilder sb = new StringBuilder().append(CredentialStorage.dataDirectory).append("data/");
               S2CResourceChunkPacket packet17 = packet16;
               CredentialStorage.ensureDirectoryExists(sb.append(packet17.getClientName()).toString());
               StringBuilder sb2 = new StringBuilder().append(CredentialStorage.dataDirectory).append("data/");
               S2CResourceChunkPacket packet18 = packet16;
               obj15 = new File(sb2.append(packet18.getClientName()).append("/resources.jar").toString());
               if (((File)obj15).exists()) {
                  ((File)obj15).delete();
               }

               LauncherClientHandler launcherClientHandler13 = this;
               StringBuilder sb3 = new StringBuilder().append(CredentialStorage.dataDirectory).append("/data/");
               S2CResourceChunkPacket packet19 = packet16;
               Path path = new File(sb3.append(packet19.getClientName()).append("/resources.jar").toString()).toPath();
               byte[] resourcesJarBuffer2 = this.resourcesJarBuffer;
               OpenOption[] openOptions = new OpenOption[]{StandardOpenOption.CREATE};
               byte[] bytes19 = resourcesJarBuffer2;
               Path path2 = path;
               launcherClientHandler13.writeFileBytes(path2, bytes19, openOptions);
               LauncherClientHandler launcherClientHandler14 = this;
               boolean flag20 = true;
               launcherClientHandler14.resourcesReady = flag20;
            }
         }

         if (this.nativesArchiveBuffer != null) {
            System.arraycopy((byte[])obj25, 0, this.nativesArchiveBuffer, this.nativesBytesReceived, ((byte[])obj25).length);
            int n57 = this.nativesBytesReceived + ((byte[])obj25).length;
            this.nativesBytesReceived = n57;
            if (this.nativesBytesReceived == this.nativesArchiveBuffer.length) {
               exitBlock: {
                  StringBuilder sb4 = new StringBuilder().append(CredentialStorage.dataDirectory).append("data/");
                  S2CResourceChunkPacket packet20 = packet16;
                  obj15 = sb4.append(packet20.getClientName()).toString();
                  CredentialStorage.ensureDirectoryExists((String)obj15);
                  obj20 = new File((String)obj15 + "/nativezip.zip");
                  if (((File)obj20).exists()) {
                     LauncherClientHandler launcherClientHandler15 = this;
                     File file = (File)obj20;
                     if (!launcherClientHandler15.isFileWritable(file)) {
                        break exitBlock;
                     }
                  }

                  LauncherClientHandler launcherClientHandler16 = this;
                  Path path3 = Paths.get((String)obj15 + "/nativezip.zip");
                  byte[] nativesArchiveBuffer2 = this.nativesArchiveBuffer;
                  OpenOption[] openOptions2 = new OpenOption[]{StandardOpenOption.CREATE};
                  byte[] bytes20 = nativesArchiveBuffer2;
                  Path path4 = path3;
                  launcherClientHandler16.writeFileBytes(path4, bytes20, openOptions2);
               }

               obj16 = new File((String)obj15 + "/natives");
               LauncherClientHandler launcherClientHandler17 = this;
               File file2 = (File)obj16;
               if (launcherClientHandler17.areAllFilesWritable(file2)) {
                  launcherClientHandler17 = this;
                  File file3 = (File)obj16;
                  launcherClientHandler17.clearDirectoryFiles(file3);
                  launcherClientHandler17 = this;
                  String str6 = (String)obj15 + "/natives";
                  ZipArchiveExtractor.extractZipBytes(launcherClientHandler17.nativesArchiveBuffer, str6);
               }

               launcherClientHandler17 = this;
               boolean flag21 = true;
               launcherClientHandler17.nativesReady = flag21;
            }
         }

         if (this.nativesReady
            && this.resourcesReady
            && (this.nativesBytesReceived == packet16.getNativesTotalSize() || packet16.getNativesTotalSize() == 1)
            && (this.resourcesBytesReceived == packet16.getResourcesTotalSize() || packet16.getResourcesTotalSize() == 1)) {
            if (this.nativesArchiveBuffer == null) {
               StringBuilder sb5 = new StringBuilder().append(CredentialStorage.dataDirectory).append("data/");
               S2CResourceChunkPacket packet21 = packet16;
               obj15 = new File(sb5.append(packet21.getClientName()).append("/natives").toString());
               LauncherClientHandler launcherClientHandler18 = this;
               File file4 = (File)obj15;
               if (launcherClientHandler18.areAllFilesWritable(file4)) {
                  launcherClientHandler18 = this;
                  File file5 = (File)obj15;
                  launcherClientHandler18.clearDirectoryFiles(file5);
                  launcherClientHandler18 = this;
                  StringBuilder sb6 = new StringBuilder().append(CredentialStorage.dataDirectory).append("data/");
                  S2CResourceChunkPacket packet22 = packet16;
                  Path path5 = Paths.get(sb6.append(packet22.getClientName()).append("/nativezip.zip").toString());
                  byte[] bytes21 = launcherClientHandler18.readFileBytes(path5);
                  StringBuilder sb7 = new StringBuilder().append(CredentialStorage.dataDirectory).append("data/");
                  S2CResourceChunkPacket packet23 = packet16;
                  String str7 = sb7.append(packet23.getClientName()).append("/natives").toString();
                  ZipArchiveExtractor.extractZipBytes(bytes21, str7);
               }
            }

            LauncherClientHandler launcherClientHandler19 = this;
            S2CResourceChunkPacket packet24 = packet16;
            String clientName = packet24.getClientName();
            Sha256Digest sha256Digest3 = this.sha256Digest;
            byte[] bytes22 = this.buildHardwareIdString().getBytes(StandardCharsets.UTF_8);
            byte[] bytes23 = bytes22;
            C2SResourcesReadyPacket packet25 = new C2SResourcesReadyPacket(
               clientName,
               sha256Digest3.digest(bytes23),
               this.sessionToken,
               System.currentTimeMillis(),
               LaunchEnvironmentCheck.isEnvironmentClean()
            );
            launcherClientHandler19.sendPacket(packet25);
            Platform.runLater(() -> showWaitingForServerStatus(0L));
         }

         if (this.resourcesJarBuffer != null) {
            obj15 = formatDecimal((double)this.resourcesBytesReceived / (double)this.resourcesJarBuffer.length * 100.0, 1);
            if (!this.lastDownloadPercent.equals((String)obj15)) {
               String var281f = (String)obj15; Platform.runLater(() -> updateResourcesDownloadText(var281f, 0L));
            }

            LauncherClientHandler launcherClientHandler20 = this;
            String str8 = (String)obj15;
            launcherClientHandler20.lastDownloadPercent = str8;
         }
      }

      if (packet instanceof S2CClientArchiveChunkPacket) {
         S2CClientArchiveChunkPacket packet26 = (S2CClientArchiveChunkPacket)packet;
         ClientArchiveHolder.encryptedArchiveKey = packet26.getEncryptedArchiveKey();
         if (ClientArchiveHolder.archiveBytes == null) {
            byte[] sessionToken3 = this.sessionToken;
            int totalSize = packet26.getTotalSize();
            ClientArchiveHolder.archiveBytes = ArchiveAllocationGuard.allocateArchiveBuffer(sessionToken3, totalSize);
            if (ClientArchiveHolder.archiveBytes.length != packet26.getTotalSize()) {
               this.channel.close();
            }
         }

         System.arraycopy(packet26.getChunkData(), 0, ClientArchiveHolder.archiveBytes, this.archiveBytesReceived, packet26.getChunkData().length);
         int n58 = this.archiveBytesReceived + packet26.getChunkData().length;
         this.archiveBytesReceived = n58;
         obj30 = formatDecimal((double)this.archiveBytesReceived / (double)ClientArchiveHolder.archiveBytes.length * 100.0, 1);
         if (!this.lastDownloadPercent.equals((String)obj30)) {
            String var228f = (String)obj30; Platform.runLater(() -> updateClientDownloadText(var228f, 0L));
         }

         LauncherClientHandler launcherClientHandler21 = this;
         String str9 = (String)obj30;
         launcherClientHandler21.lastDownloadPercent = str9;
         if (this.archiveBytesReceived == ClientArchiveHolder.archiveBytes.length) {
            Platform.runLater(() -> showStartingClientStatus(0L, (short)0));
            S2CClientArchiveChunkPacket packet27 = packet26;
            ClientArchiveHolder.clientName = packet27.getClientName();
            archiveHolder = new ClientArchiveHolder();
            archiveHolder.start();
         }
      }

      if (packet instanceof S2CClassLoaderKeyPacket) {
         S2CClassLoaderKeyPacket packet28 = (S2CClassLoaderKeyPacket)packet;
         LauncherClientHandler launcherClientHandler22 = this;
         S2CClassLoaderKeyPacket packet29 = packet28;
         launcherClientHandler22.handleClassLoaderKey(packet29);
      }

      if (packet instanceof S2CHardwareChallengePacket) {
         S2CHardwareChallengePacket packet30 = (S2CHardwareChallengePacket)packet;
         S2CHardwareChallengePacket packet31 = packet30;
         obj30 = decodeRsaPublicKey(packet31.getEncodedPublicKey());
         RsaPublicKeyCipher rsaPublicKeyCipher = new RsaPublicKeyCipher();
         RSAPublicKey rsaPublicKey = (RSAPublicKey)obj30;
         byte[] encryptedAesKey = packet30.getEncryptedAesKey();
         RSAPublicKey rsaPublicKey2 = rsaPublicKey;
         obj40 = rsaPublicKeyCipher.decrypt(rsaPublicKey2, encryptedAesKey);
         if ((byte[])obj40 == null) {
            return;
         }

         LauncherClientHandler launcherClientHandler23 = this;
         C2SHardwareInfoPacket packet32 = HardwareFingerprintCollector.collectHardwareInfo((byte[])obj40);
         launcherClientHandler23.sendPacket(packet32);
         LauncherClientHandler launcherClientHandler24 = this;
         boolean flag22 = false;
         launcherClientHandler24.setAutoRelease(flag22);
      }

      if (packet instanceof S2CRemoteClassResponsePacket) {
         S2CRemoteClassResponsePacket packet33 = (S2CRemoteClassResponsePacket)packet;
         LauncherClientHandler launcherClientHandler25 = this;
         S2CRemoteClassResponsePacket packet34 = packet33;
         S2CRemoteClassResponsePacket packet35 = packet34;
         launcherClientHandler25.handleRemoteClassResponse(packet35);
         ctx.close();
      }

      if (packet instanceof S2CFailureMessagePacket) {
         S2CFailureMessagePacket packet36 = (S2CFailureMessagePacket)packet;
         obj30 = new JOptionPane(packet36.getMessage(), 0);
         JDialog jDialog = ((JOptionPane)obj30).createDialog("Failure");
         jDialog.setAlwaysOnTop(true);
         jDialog.setVisible(true);
         this.channel.close();
      }
   }

   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      this.channelActive(ctx);
      LauncherClientHandler launcherClientHandler = this;
      Channel channel2 = ctx.channel();
      launcherClientHandler.channel = channel2;
      instance = this;
      LauncherClientHandler launcherClientHandler2 = this;
      ClassIntegrityWatchdog.registerInstance("1", launcherClientHandler2);
      connected = true;
      launcherClientHandler = this;
      launcherClientHandler.initiateHandshake();
   }

   public void sendPacket(LauncherPacket packet) {
      byte b = 5;
      if (!this.isChannelOpen()) {
         LauncherClientHandler launcherClientHandler = this;
         LauncherPacket packet2 = packet;
         launcherClientHandler.enqueueOutboundPacket(packet2);
      }

      while (true) {
         if (b > 2) {
            if (4 <= b) {
               if (4 < b) {
                  return;
               }

               LauncherClientHandler launcherClientHandler2 = this;
               launcherClientHandler2.flushOutboundQueue();
            } else {
               LauncherClientHandler launcherClientHandler3 = this;
               LauncherPacket packet3 = packet;
               launcherClientHandler3.dispatchPacket(packet3);
            }
         }
      }
   }

   private static RSAPublicKey decodeRsaPublicKey(byte[] bytes) {
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
      KeyFactory keyFactory = null;

      try {
         keyFactory = KeyFactory.getInstance("RSA");
         return (RSAPublicKey)keyFactory.generatePublic(x509EncodedKeySpec);
      } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
         throw new RuntimeException(e);
      }
   }

   private byte[] readFileBytes(Path path) {
      try {
         return Files.readAllBytes(path);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private boolean isChannelOpen() {
      byte b = 2;

      boolean flag;
      while (true) {
         if (1 >= b) {
            if (this.channel != null) {
               continue;
            }
         } else if (this.channel.isOpen()) {
            flag = true;
            break;
         }

         flag = false;
         break;
      }

      return flag;
   }

   private boolean areAllFilesWritable(File file) {
      File[] files = null;
      byte b = 1;

      while (2 < b) {
         if (4 <= b) {
            if (4 >= b) {
               return true;
            }

            if (files == null) {
               continue;
            }
         } else if (!file.exists()) {
            continue;
         }

         if (b > 0) {
            return Arrays.stream(files).allMatch(file2 -> this.isFileWritable(file2));
         }

         files = file.listFiles(File::isFile);
      }

      return true;
   }

   public boolean isFileWritable(File file) {
      if (!file.exists()) {
         return false;
      }
      try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
         return true;
      } catch (IOException e) {
         return false;
      }
   }

   private void writeAndFlushPacket(long l, LauncherPacket packet) {
      this.channel.writeAndFlush(packet);
   }

   private void sleepBriefly() {
      try {
         Thread.sleep(1L);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
   }

   private void enqueueOutboundPacket(LauncherPacket packet) {
      this.packetQueueLock.writeLock().lock();

      try {
         this.outboundPacketQueue.add(packet);
      } finally {
         this.packetQueueLock.writeLock().unlock();
      }
   }

   private static void updateClientDownloadText(String str, long l) {
      ElectricLauncherController.getInstance().infoText.setText("Client download: " + str + "%");
   }





}
