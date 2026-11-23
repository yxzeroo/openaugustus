package net.augustus.vendor;

import net.augustus.utils.TuplePair;
import java.util.concurrent.ConcurrentHashMap;
import net.augustus.vendor.packet.PlayerPresenceClearPacket;
import net.augustus.vendor.packet.IrcChatTogglePacket;
import net.augustus.vendor.packet.PlayerPresenceUpdatePacket;
import net.augustus.vendor.packet.FriendRemoveC2SPacket;
import net.augustus.vendor.packet.FriendAcceptPacket;
import net.augustus.vendor.packet.IrcChatSendPacket;
import net.augustus.vendor.packet.IrcVisibilityUpdatePacket;
import net.augustus.vendor.packet.ServerCommandPacket;
import net.augustus.vendor.packet.FriendRequestPacket;
import net.augustus.vendor.packet.TelemetryLogPacket;

public class VendorPacketDispatcher {
   private static int nextAckId = 0;
   private static boolean dispatcherFlag = false;
   public static final ConcurrentHashMap pendingAckPackets = new ConcurrentHashMap();

   public static synchronized void sendFriendAccept(String str) {
      int n = 0;
      FriendAcceptPacket packet = null;
      byte b = 2;

      while (4 < b) {
         if (b >= 6) {
            if (b <= 6) {
               return;
            }

            if (b <= 7) {
               return;
            }

            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            FriendAcceptPacket packet2 = packet;
            activeHandler.sendPacket(packet2);
         } else if (VendorSessionTicker.sessionToken.length >= 32) {
            while (true) {
               while (b < 1) {
                  packet = new FriendAcceptPacket(VendorSessionTicker.sessionToken, str, n);
               }

               if (b <= 1) {
                  if (!AugustusServerHandler.isConnectedAndReady()) {
                     ;
                  }
                  break;
               }

               if (b <= 2) {
                  n = nextAckId();
               } else {
                  pendingAckPackets.put(n, new TuplePair(System.currentTimeMillis(), packet));
               }
            }
         }
      }
   }

   public static void sendIrcMessage(String str) {
      byte b = 4;

      while (true) {
         while (2 >= b) {
            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            IrcChatSendPacket packet = new IrcChatSendPacket(VendorSessionTicker.sessionToken, str, "");
            activeHandler.sendPacket(packet);
         }

         if (3 < b) {
            return;
         }

         if (VendorSessionTicker.sessionToken.length < 32) {
            break;
         }

         if (!AugustusServerHandler.isConnectedAndReady()) {
            break;
         }
      }
   }

   public static synchronized void sendPresenceClear() {
      int n = 0;
      PlayerPresenceClearPacket packet = null;
      byte b = 1;

      while (true) {
         if (1 <= b) {
            if (1 < b) {
               if (b <= 2) {
                  pendingAckPackets.put(n, new TuplePair(System.currentTimeMillis(), packet));
               } else {
                  packet = new PlayerPresenceClearPacket(VendorSessionTicker.sessionToken, n);
               }
               continue;
            }

            if (!AugustusServerHandler.isConnectedAndReady()) {
               ;
            }
         } else if (VendorSessionTicker.sessionToken.length < 32) {
            ;
         }

         while (4 < b) {
            if (b < 6) {
               return;
            }

            if (6 < b) {
               if (b <= 7) {
                  return;
               }

               return;
            }

            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            PlayerPresenceClearPacket packet2 = packet;
            activeHandler.sendPacket(packet2);
         }

         n = nextAckId();
      }
   }

   public static void sendIrcWhisper(String str, String str2) {
      byte b = 0;

      do {
         if (0 >= b) {
            if (!AugustusServerHandler.isConnectedAndReady()) {
               ;
            }
            break;
         }
      } while (((byte[])VendorSessionTicker.sessionToken).length >= 32);

      while (2 >= b) {
         AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
         IrcChatSendPacket packet = new IrcChatSendPacket(VendorSessionTicker.sessionToken, str2, str);
         activeHandler.sendPacket(packet);
      }

      if (b > 3) {
         ;
      }
   }

   public static synchronized void sendIrcVisibilityUpdate(boolean flag, int n) {
      int n2 = 0;
      IrcVisibilityUpdatePacket packet = null;
      byte b = 7;
      long l = 0L;
      l = 4173819457830755296L ^ (long)"Ⲏ鿓║㾜怘⎲㈁䔚\ueef7뺜\udba6㬤⇏耄帆㮻ﱆ抮\ue9e8㧛რप\udf6d㆝".hashCode();

      while (true) {
         if (4 >= b) {
            if (VendorSessionTicker.sessionToken.length < 32) {
               l = (l ^ -6029582713415680444L) + 3023950089126803639L;
               break;
            }

            l = (l ^ -7885373173390083170L) - 5349307468624796334L;
         } else if (6 <= b) {
            if (b <= 6) {
               if (AugustusServerHandler.isConnectedAndReady()) {
                  l = (l ^ -373216561181985510L) + -2596392656960736819L;
               } else {
                  l = l - -8299418197814342478L ^ -2684520239104215406L;
               }
               break;
            }

            if (b <= 7) {
               pendingAckPackets.put(n2, new TuplePair(System.currentTimeMillis(), packet));
               l = (l ^ -3144386594109497686L) - 7891668131580063748L;
            } else {
               n2 = nextAckId();
               l = (l ^ -3105124343337052923L) - -3259992693611806458L;
            }
         } else {
            packet = new IrcVisibilityUpdatePacket(VendorSessionTicker.sessionToken, flag, n, n2);
            l = l + -4985758251368503541L ^ 6220015269361315213L;
         }
      }

      while (1 <= b) {
         if (b <= 1) {
            return;
         }

         if (b > 2) {
            return;
         }

         AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
         IrcVisibilityUpdatePacket packet2 = packet;
         activeHandler.sendPacket(packet2);
         l = l + -6049759494623358567L ^ -8533797956038573432L;
      }
   }

   public static void sendServerCommand(String str, String str2, String str3) {
      byte b = 3;

      outerLoop:
      while (true) {
         if (0 >= b) {
            if (VendorSessionTicker.sessionToken.length >= 32) {
               ;
            }
         } else {
            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            ServerCommandPacket packet = new ServerCommandPacket(VendorSessionTicker.sessionToken, str, str2, str3);
            activeHandler.sendPacket(packet);
         }

         while (2 < b) {
            if (3 >= b) {
               return;
            }

            if (AugustusServerHandler.isConnectedAndReady()) {
               continue outerLoop;
            }
         }

         return;
      }
   }

   public static synchronized void sendFriendRequest(String str) {
      int n = 0;
      FriendRequestPacket packet = null;
      byte b = 3;

      while (true) {
         if (4 >= b) {
            if (VendorSessionTicker.sessionToken.length >= 32) {
               continue;
            }
         } else {
            if (6 > b) {
               return;
            }

            if (6 >= b) {
               n = nextAckId();
               continue;
            }

            if (7 >= b) {
               packet = new FriendRequestPacket(VendorSessionTicker.sessionToken, str, n);
            } else if (AugustusServerHandler.isConnectedAndReady()) {
            }
         }

         if (b < 1) {
            return;
         }

         if (1 >= b) {
            return;
         }

         if (b <= 2) {
            pendingAckPackets.put(n, new TuplePair(System.currentTimeMillis(), packet));
         } else {
            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            FriendRequestPacket packet2 = packet;
            activeHandler.sendPacket(packet2);
         }
      }
   }

   public static synchronized void sendIrcEnable() {
      byte b = 1;

      outerLoop:
      while (true) {
         if (0 >= b) {
            if (VendorSessionTicker.sessionToken.length >= 32) {
               ;
            }
         } else {
            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            IrcChatTogglePacket packet = new IrcChatTogglePacket(VendorSessionTicker.sessionToken, true);
            activeHandler.sendPacket(packet);
         }

         while (2 < b) {
            if (b <= 3) {
               return;
            }

            if (AugustusServerHandler.isConnectedAndReady()) {
               continue outerLoop;
            }
         }

         return;
      }
   }

   public static synchronized void sendFriendRemove(String str) {
      int n = 0;
      Object obj = null;
      byte b = 1;
      long l = 0L;
      l = 298039362505713086L ^ (long)"ℓ뮚\ude02虆厀谚헢뜟븀\ue526犂쑬䈧臈䚓㤃\u19cbꃸ\ud917뾃嚖兒ુ際".hashCode();

      while (true) {
         while (4 < b) {
            if (b < 6) {
               return;
            }

            if (b > 6) {
               if (b <= 7) {
                  return;
               }

               return;
            }

            obj = new FriendRemoveC2SPacket(VendorSessionTicker.sessionToken, str, n);
            l = (l ^ -4987978970986876230L) - 4153348299538267298L;
            pendingAckPackets.put(n, new TuplePair(System.currentTimeMillis(), (FriendRemoveC2SPacket)obj));
            l = (l ^ 5233668941387324278L) + 1412335866377477451L;
            if (AugustusServerHandler.isConnectedAndReady()) {
               l = l + -33699786205082705L ^ 2273859395011844257L;
               AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
               FriendRemoveC2SPacket packet = (FriendRemoveC2SPacket)obj;
               activeHandler.sendPacket(packet);
               l = l + 40913739074716577L ^ -7280564067895542136L;
            } else {
               l = (l ^ -1498820367456127309L) + -2476515143691231501L;
            }
         }

         if (VendorSessionTicker.sessionToken.length >= 32) {
            l = (l ^ -6804283934864063892L) + 3794228376433918381L;
            n = nextAckId();
            l = l - 6678292230394626022L ^ 6373587695906873340L;
         } else {
            l = (l ^ -8486347268308172312L) + 8143311822621017571L;
         }
      }
   }

   public static synchronized void sendIrcDisable() {
      byte b = 3;

      while (true) {
         if (2 >= b) {
            return;
         }

         if (3 >= b) {
            if (!AugustusServerHandler.isConnectedAndReady()) {
               break;
            }

            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            IrcChatTogglePacket packet = new IrcChatTogglePacket(VendorSessionTicker.sessionToken, false);
            activeHandler.sendPacket(packet);
         } else if (VendorSessionTicker.sessionToken.length < 32) {
            break;
         }
      }
   }

   public static synchronized boolean sendTelemetryLog(String str, int n) {
      int n2 = 0;
      int n3 = 0;
      Object obj = null;
      Object obj2 = null;
      short s = 0;
      byte b = 0;
      long l = 0L;
      l = 6173797786296382490L ^ (long)"구ﳤ\ue08f\ud959邷ⰰ쭟䩇Y걏牑䬇⓼偳杌슯ဩ輮츃⏃ൂ\udba0跩箁".hashCode();

      while (true) {
         while (3 >= b) {
            if (AugustusServerHandler.isConnectedAndReady()) {
               l = (l ^ 7545594127401017986L) + 4928520078080756867L;
               s = 0;
               l = l - 1509270732374274833L ^ -1354111715815657816L;
            } else {
               l = l + -6146673681648282193L ^ -7588710493681569489L;
            }
         }

         if (5 <= b) {
            if (5 >= b) {
               return false;
            }

            if (b > 6) {
               if (n2 >= 4) {
                  l = l + 7675405377282737510L ^ -4833802976177837843L;
                  break;
               }

               l = (l ^ 110403466211768019L) + -9122249901227815162L;
               continue;
            }

            String str2;
            if (str.length() > 1800) {
               l = (l ^ 7039650112563535272L) - -4260338334120633146L;
               str2 = str.substring(s, Math.min(str.length() - 1, s + 1799));
            } else {
               l = (l ^ 3356011628771632737L) - 8916945199182303871L;
               str2 = str;
            }

            obj = str2;
            s += 1800;
            n2++;
            n3 = nextAckId();
            obj2 = new TelemetryLogPacket((String)obj, n3, n);
            pendingAckPackets.put(n3, new TuplePair(System.currentTimeMillis(), (TelemetryLogPacket)obj2));
            if (!AugustusServerHandler.isConnectedAndReady()) {
               return false;
            }

            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            TelemetryLogPacket packet = (TelemetryLogPacket)obj2;
            activeHandler.sendPacket(packet);
            l = (l ^ 1088016319020558402L) - -4330668201231086018L;
         } else {
            n2 = 0;
            l = (l ^ -4535216185809756728L) - -1333492952441354420L;
         }

         if (s >= str.length()) {
            l = l + 8885296185074614357L ^ -2988451004182978987L;
            break;
         }

         l = l + 6756678450119783229L ^ -8459492524114928714L;
      }

      return true;
   }

   private static synchronized int nextAckId() {
      return nextAckId++;
   }

   public static synchronized void sendPresenceUpdate(String str, String str2, String str3, int n, boolean flag) {
      PlayerPresenceUpdatePacket packet = null;
      int n2 = 0;
      byte b = 1;

      outerLoop:
      while (b >= 1) {
         if (b <= 1) {
            if (VendorSessionTicker.sessionToken.length < 32) {
               continue;
            }
         } else {
            if (b > 2) {
               packet = new PlayerPresenceUpdatePacket(VendorSessionTicker.sessionToken, str, str2, str3, n, flag, n2);
               continue;
            }

            pendingAckPackets.put(n2, new TuplePair(System.currentTimeMillis(), packet));
         }

         while (true) {
            while (4 < b) {
               if (6 > b) {
                  return;
               }

               if (6 < b) {
                  if (7 >= b) {
                     return;
                  }

                  n2 = nextAckId();
                  continue outerLoop;
               }

               if (!AugustusServerHandler.isConnectedAndReady()) {
                  ;
               }
            }

            AugustusServerHandler activeHandler = AugustusServerHandler.getActiveHandler();
            PlayerPresenceUpdatePacket packet2 = packet;
            activeHandler.sendPacket(packet2);
         }
      }
   }

}
