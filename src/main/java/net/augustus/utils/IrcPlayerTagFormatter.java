package net.augustus.utils;

import net.augustus.Augustus;
import net.augustus.vendor.ElectricFriend;
import net.augustus.vendor.ElectricFriendManager;
import net.augustus.vendor.OnlineUserEntry;
import net.augustus.vendor.OnlineUserRegistry;
import net.minecraft.client.Minecraft;

public class IrcPlayerTagFormatter {
   public static final StringBuilder TAG_BUILDER = new StringBuilder(" [");
   public static final PlayerNameReplacerTrie PLAYER_NAME_TRIE = new PlayerNameReplacerTrie();

   private static String buildRelationTag(String str) {
      TAG_BUILDER.delete(2, TAG_BUILDER.length());
      OnlineUserEntry onlineUserEntry = (OnlineUserEntry)OnlineUserRegistry.usersByIngameName.get(str);
      if (onlineUserEntry == null) {
         return "";
      } else {
         String clientUsername = onlineUserEntry.getClientUsername();
         ElectricFriend electricFriend = null;
         boolean flag = false;
         if (clientUsername != null) {
            electricFriend = (ElectricFriend)ElectricFriendManager.getFriends2().get(clientUsername);
            if (electricFriend != null && electricFriend.getStatus() > 0) {
               TAG_BUILDER.append("F");
               flag = true;
            }
         }

         if (Augustus.getInstance().getModuleManager().irc.isToggled() && Augustus.getInstance().getModuleManager().irc.clientFriends.getValue()) {
            if (Augustus.getInstance().getModuleManager().irc.yourFriendsOnly.getValue()) {
               if (electricFriend != null && electricFriend.getStatus() > 0) {
                  if (flag) {
                     TAG_BUILDER.append(",T");
                  } else {
                     TAG_BUILDER.append("T");
                  }
               }
            } else if (clientUsername != null) {
               if (flag) {
                  TAG_BUILDER.append(",T");
               } else {
                  TAG_BUILDER.append("T");
               }

               flag = true;
            }
         }

         TAG_BUILDER.append("]");
         return flag ? TAG_BUILDER.toString() : "";
      }
   }

   public static String replacePlayerNames(String str) {
      return Minecraft.getMinecraft().session != null && !OnlineUserRegistry.usersByIngameName.isEmpty() && Augustus.getInstance().getModuleManager().irc.isToggled()
         ? PLAYER_NAME_TRIE.replaceAll(str)
         : null;
   }

   public static boolean isRegisteredPlayer(String str) {
      String str2 = (String)Minecraft.getMinecraft().getNetHandler().getPlayerUuidMap().get(str);
      if (str2 == null) {
         return false;
      } else {
         OnlineUserEntry onlineUserEntry = (OnlineUserEntry)OnlineUserRegistry.usersByIngameName.get(str);
         return onlineUserEntry != null && onlineUserEntry.getPlayerUuid().equals(str2);
      }
   }

   public static String getRelationTag(String str) {
      return buildRelationTag(str);
   }

}
