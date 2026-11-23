package net.augustus.commands.impl;

import net.augustus.ClientManagers;
import net.augustus.commands.Command;
import net.augustus.utils.TextFormatUtil;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;

public class FriendCommand extends Command {

   public void commandAction(String[] strings, String str) {
      if (strings.length != 3) {
         if (strings.length == 2 && strings[1].equalsIgnoreCase("removeall")) {
            ClientManagers.moduleManager.midClick.clearAllFriends();
            String str2 = "Removed all friends from your friend list successfully.";
            this.sendChat(str2);
         } else {
            this.sendUsageMessage();
         }
      } else {
         String str3 = strings[2];
         if (!TextFormatUtil.isCleanPlayerName(str3)) {
            String str4 = "Invalid name";
            this.sendChat(str4);
         } else {
            if (!strings[1].equalsIgnoreCase("add") && !strings[1].equalsIgnoreCase("permaAdd")) {
               if (strings[1].equalsIgnoreCase("remove")) {
                  if (ClientManagers.moduleManager.midClick.friends.contains(str3)) {
                     ClientManagers.moduleManager.midClick.friends.remove(str3);
                     ClientManagers.moduleManager.midClick.removeSavedFriend(str3);
                     String str5 = "Player §c" + str3 + "§7 successfully removed.";
                     this.sendChat(str5);
                  } else {
                     String str6 = "Player §c" + str3 + "§7 isn't in your friend list.";
                     this.sendChat(str6);
                  }
               } else if (strings[1].equalsIgnoreCase("removeall")) {
                  ClientManagers.moduleManager.midClick.clearAllFriends();
                  String str7 = "Removed all friends from your friend list successfully.";
                  this.sendChat(str7);
               } else {
                  this.sendUsageMessage();
               }
            } else {
               if (ClientManagers.moduleManager.midClick.friends.contains(str3)) {
                  String str8 = "Player §c" + str3 + "§7 is already your friend.";
                  this.sendChat(str8);
                  return;
               }

               NetHandlerPlayClient sendQueue = mc.thePlayer.sendQueue;

               for (NetworkPlayerInfo networkPlayerInfo : (Iterable<NetworkPlayerInfo>)(sendQueue.getPlayerInfoMap())) {
                  String name = networkPlayerInfo.getGameProfile().getName();
                  if (!name.equals(mc.thePlayer.getGameProfile().getName()) && str3.equals(name)) {
                     ClientManagers.moduleManager.midClick.friends.add(str3);
                     if (strings[1].equalsIgnoreCase("permaAdd")) {
                        ClientManagers.moduleManager.midClick.saveFriend(str3);
                     }

                     String str9 = "Player §c" + str3 + "§7 successfully added.";
                     this.sendChat(str9);
                     return;
                  }
               }

               String str10 = "Player §c" + str3 + "§7 isn't on the server.";
               this.sendChat(str10);
            }
         }
      }
   }

   public FriendCommand() {
      super("friend");
   }

   public void sendUsageMessage() {
      String str = "§7" + this.getCommand() + "§7 [§cadd/permaAdd/remove/removeall§7] [§2playername§7]";
      this.sendChat(str);
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Add or remove friends)";
      this.sendChat(str);
   }

}
