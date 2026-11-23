package net.augustus.vendor;

import net.augustus.Augustus;
import net.augustus.utils.TextFormatUtil;
import java.util.concurrent.ConcurrentHashMap;
import net.augustus.modules.render.CustomName;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class OnlineUserRegistry {
   public static final ConcurrentHashMap usersBySessionId;
   public static final ConcurrentHashMap usersByIngameName;

   public static void registerUser(
      String str, char ch, int n, String str2, String str3, int n2, int n3, String str4, String str5, String str6, long l, int n4
   ) {
      OnlineUserEntry onlineUserEntry = new OnlineUserEntry(str, str2, str3, n2, str4, str5, str6, l, n4);
      usersBySessionId.put(l, onlineUserEntry);
      usersByIngameName.put(onlineUserEntry.getIngameName(), onlineUserEntry);
   }

   public static boolean matchesPlayerIdentity(EntityPlayer player, int n, String str) {
      return player != null && player.getGameProfile() != null
         ? n == player.getEntityId()
            || Minecraft.getMinecraft().getCurrentServerData() != null
               && player.getGameProfile().getId() != null
               && player.getGameProfile().getId().toString().equals(str)
         : false;
   }

   public static void removeUser(long l) {
      OnlineUserEntry onlineUserEntry = (OnlineUserEntry)usersBySessionId.get(l);
      if (onlineUserEntry != null) {
         usersByIngameName.remove(onlineUserEntry.getIngameName());
         usersBySessionId.remove(l);
      }
   }

   public static String formatUserTag(String str, String str2, String str3, String str4, int n, String str5) {
      switch (str) {
         case "Xanax":
            return "§7[" + str4 + "§7] §c§l" + str3 + " §7[§e" + n + "§7]§r" + str5;
         case "Xanax2":
            return "§7[" + str4 + "§7] §c§l" + str2 + " §7[§e" + n + "§7]§r" + str5;
         case "Centaurus":
            return "§f[§9A§f-§6" + str4 + "§f] " + str3 + " §7[§e" + n + "§7]§r" + str5;
         case "Centaurus2":
            return "§6[§9" + str4 + "§6] §f" + str3 + " §7[§e" + n + "§7]§r" + str5;
         case "Skid":
            return "§8[§aAugi-" + str4 + "§8] §r" + str2 + " §7[§e" + n + "§7]§r" + str5;
         case "Simple":
            return str2 + " §6[§9" + str3 + "§6] §r§7[§r" + str4 + "§r§7] [§e" + n + "§7]" + str5;
         default:
            return str2;
      }
   }

   public static void clearUsers() {
      usersBySessionId.clear();
      usersByIngameName.clear();
   }

   private static String replaceUsernameOccurrences(String str, String str2, String str3, String str4, String str5, int n) {
      int n2 = str.indexOf(str2);
      if (n2 == -1) {
         return str;
      } else {
         StringBuilder sb = new StringBuilder(str.length());

         int n3;
         for (n3 = 0; n2 != -1; n2 = str.indexOf(str2, n3)) {
            sb.append(str, n3, n2);
            sb.append(formatUserTag(str4, str2, str3, str5, n, TextFormatUtil.extractFormattingSequence(str.substring(0, n2), (char)167)));
            n3 = n2 + str2.length();
         }

         sb.append(str.substring(n3));
         return sb.toString();
      }
   }

   public static String applyCustomNameTag(String str) {
      if (Minecraft.getMinecraft().session != null && Augustus.getInstance().getModuleManager().customName.isToggled()) {
         String username = Minecraft.getMinecraft().session.getUsername();
         if (!str.contains(username)) {
            return str;
         } else {
            Augustus instance = Augustus.getInstance();
            CustomName customName = instance.getModuleManager().customName;
            String displayName2 = customName.displayName2;
            String str2 = instance.getUserRankColor() + instance.getUserRank();
            int userLevel = instance.getUserLevel();
            if (!displayName2.isEmpty() && !str2.isEmpty()) {
               return replaceUsernameOccurrences(str, username, displayName2, customName.nameFormatting.getSelectedValue(), str2, userLevel);
            } else {
               return str;
            }
         }
      } else {
         return str;
      }
   }



   static {
      usersByIngameName = new ConcurrentHashMap();
      usersBySessionId = new ConcurrentHashMap();
   }
}
