package net.augustus.vendor;

import net.augustus.utils.TupleTriple;
import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerEffectRegistry {
   private static final HashMap playerEffects;
   private static String[] opaqueStateTag;

   public static void addPlayerEffect(String var2User, int var2Flag, String var2Tag, String var2Name, String var2Data, int var2Slot) {
      TupleTriple tupleTriple = (TupleTriple)playerEffects.get(var2User);
      if (tupleTriple == null) {
         tupleTriple = new TupleTriple(var2Flag, var2Tag, null);
      }

      HashMap third = (HashMap)tupleTriple.getThird();
      if (third == null) {
         third = new HashMap();
      }

      third.put(var2Slot, new PlayerEffectEntry(var2User, var2Name, (byte)0, 0, var2Data, var2Slot, 0));
      playerEffects.put(var2User, new TupleTriple(var2Flag, var2Tag, third));
   }

   public static void removePlayerEffect(String var2User, int var2Slot) {
      TupleTriple tupleTriple = (TupleTriple)playerEffects.get(var2User);
      if (tupleTriple != null) {
         HashMap third = (HashMap)tupleTriple.getThird();
         if (third != null) {
            third.remove(var2Slot);
            if (third.isEmpty()) {
               playerEffects.remove(var2User);
            }
         }
      }
   }

   public static void clearEffects() {
      playerEffects.clear();
   }

   public static PlayerEffectEntry getPlayerEffect(EntityPlayer player, int n) {
      String[] opaqueStateTag2 = getOpaqueStateTag();
      GameProfile gameProfile = player.getGameProfile();
      if (opaqueStateTag2 == null) {
         if (gameProfile == null) {
            return null;
         }

         gameProfile = player.getGameProfile();
      }

      String name = gameProfile.getName();
      TupleTriple playerEffectRecord = getPlayerEffectRecord(name);
      Object obj = playerEffectRecord;
      if (opaqueStateTag2 == null) {
         if (playerEffectRecord == null) {
            return null;
         }

         obj = playerEffectRecord.getThird();
      }

      HashMap map = (HashMap)obj;
      if (map != null) {
         obj = player;
         if (opaqueStateTag2 == null) {
            int first = (Integer)playerEffectRecord.getFirst();
            String second = (String)playerEffectRecord.getSecond();
            int n2 = first;
            if (!OnlineUserRegistry.matchesPlayerIdentity(player, n2, second)) {
               return null;
            }

            obj = map.get(n);
         }

         return (PlayerEffectEntry)obj;
      } else {
         return null;
      }
   }

   public static TupleTriple getPlayerEffectRecord(String str) {
      return (TupleTriple)playerEffects.get(str);
   }

   public static void setOpaqueStateTag(String[] strings) {
      opaqueStateTag = strings;
   }

   public static String[] getOpaqueStateTag() {
      return opaqueStateTag;
   }

   static {
      playerEffects = new HashMap();
      setOpaqueStateTag((String[])null);
   }
}
