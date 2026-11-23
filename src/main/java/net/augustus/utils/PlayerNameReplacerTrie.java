package net.augustus.utils;

import net.augustus.Augustus;
import net.augustus.utils.text.StringTrieNode;
import net.augustus.vendor.OnlineUserEntry;
import net.augustus.vendor.OnlineUserRegistry;
import net.minecraft.client.Minecraft;

public class PlayerNameReplacerTrie {
   private final StringBuilder outputBuffer;
   private final StringTrieNode root = new StringTrieNode();

   public PlayerNameReplacerTrie() {
      this.outputBuffer = new StringBuilder();
   }

   private boolean removeRecursive(StringTrieNode stringTrieNode, String str, int n) {
      if (n == str.length()) {
         if (!stringTrieNode.endOfWord) {
            return false;
         } else {
            stringTrieNode.endOfWord = false;
            stringTrieNode.word = null;
            return stringTrieNode.children.isEmpty();
         }
      } else {
         char ch = str.charAt(n);
         StringTrieNode stringTrieNode2 = (StringTrieNode)stringTrieNode.children.get(ch);
         if (stringTrieNode2 == null) {
            return false;
         } else {
            if (!this.removeRecursive(stringTrieNode2, str, n + 1)) {
               return false;
            } else {
               stringTrieNode.children.remove(ch);
               return stringTrieNode.children.isEmpty() && !stringTrieNode.endOfWord;
            }
         }
      }
   }

   private String formatMatchedName(String str) {
      if (!str.equals(Minecraft.getMinecraft().session.getUsername())) {
         if (!IrcPlayerTagFormatter.isRegisteredPlayer(str)) {
            return str;
         } else {
            OnlineUserEntry onlineUserEntry = (OnlineUserEntry)OnlineUserRegistry.usersByIngameName.get(str);
            if (onlineUserEntry != null && onlineUserEntry.getClientUsername() != null) {
               String str2 = onlineUserEntry.getClientUsername() + IrcPlayerTagFormatter.getRelationTag(str);
               return OnlineUserRegistry.formatUserTag(Augustus.getInstance().getModuleManager().irc.ircFormatting.getSelectedValue(), str, str2, onlineUserEntry.getFormattedRank(), onlineUserEntry.getLevel(), "");
            } else {
               return str;
            }
         }
      } else {
         return str;
      }
   }

   public boolean isEmpty() {
      return this.root.children.isEmpty();
   }

   public String replaceAll(String str) {
      if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
         return null;
      } else {
         this.outputBuffer.delete(0, this.outputBuffer.length());
         int n = 0;
         boolean flag = false;

         while (n < str.length()) {
            StringTrieNode root2 = this.root;
            int n2 = n;
            StringTrieNode stringTrieNode = null;

            int n3;
            for (n3 = n; n2 < str.length() && root2.children.containsKey(str.charAt(n2)); n2++) {
               root2 = (StringTrieNode)root2.children.get(str.charAt(n2));
               if (root2.endOfWord) {
                  stringTrieNode = root2;
                  n3 = n2;
               }
            }

            if (stringTrieNode != null) {
               String word = stringTrieNode.word;
               this.outputBuffer.append(this.formatMatchedName(word));
               n = n3 + 1;
               flag = true;
            } else {
               this.outputBuffer.append(str.charAt(n));
               n++;
            }
         }

         return flag ? this.outputBuffer.toString() : null;
      }
   }

   public void insert(String str) {
      StringTrieNode root2 = this.root;

      for (char ch : str.toCharArray()) {
         root2.children.putIfAbsent(ch, new StringTrieNode());
         root2 = (StringTrieNode)root2.children.get(ch);
      }

      root2.endOfWord = true;
      root2.word = str;
   }

   public boolean remove(String str) {
      return this.removeRecursive(this.root, str, (byte)0);
   }
}
