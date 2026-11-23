package net.augustus.vendor;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ElectricFriendManager {
   private static final ConcurrentHashMap friends;
   private static int opaqueStateFlag;

   public static void removeFriend(String str) {
      friends.remove(str);
   }

   public static void clearFriends() {
      friends.clear();
   }

   public static ConcurrentHashMap getFriends2() {
      return friends;
   }

   public static Collection getFriends() {
      return friends.values();
   }

   public static void upsertFriend(String str, int n, int n2) {
      int opaqueStateFlag2 = getOpaqueStateFlag();
      ElectricFriend electricFriend = (ElectricFriend)friends.get(str);
      if (opaqueStateFlag2 != 0) {
         if (electricFriend != null) {
            friends.remove(str);
            electricFriend.setStatus(n);
            friends.put(str, electricFriend);
         } else {
            friends.put(str, new ElectricFriend(str, n, n2));
         }
      }
   }

   public static void setOpaqueStateFlag(int n) {
      opaqueStateFlag = n;
   }

   public static int getOpaqueStateFlag() {
      return opaqueStateFlag;
   }

   public static int getOpaqueGuardValue() {
      return getOpaqueStateFlag() == 0 ? 78 : 0;
   }

   static {
      friends = new ConcurrentHashMap();
      setOpaqueStateFlag(6);
   }
}
