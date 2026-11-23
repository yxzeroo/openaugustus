package net.augustus.utils;

import net.augustus.Augustus;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.random.DeterministicRandomProvider;
import java.util.concurrent.ConcurrentHashMap;

public class AntiSamplerGuard {
   private static final ConcurrentHashMap ACTIVE_GUARDS;

   private static void disableGuardEntry(Integer num, Boolean bool) {
      applyGuardState(num, false);
   }

   public static void clearAll() {
      ACTIVE_GUARDS.forEach((obj, obj2) -> {
         disableGuardEntry((Integer)obj, (Boolean)obj2);
      });
      ACTIVE_GUARDS.clear();
   }

   public static boolean isGuardActive(int n) {
      if (!ACTIVE_GUARDS.containsKey(n)) {
         return false;
      } else {
         Augustus.getInstance().getClass();
         return true;
      }
   }

   public static void activateGuard(int n) {
      if (!ACTIVE_GUARDS.containsKey(n)) {
         ACTIVE_GUARDS.put(n, true);
         applyGuardState(n, true);
      }
   }

   private static void applyGuardState(int n, boolean flag) {
      if (n == 1) {
         if (flag) {
            DefaultRandomGenerator.instance = new DeterministicRandomProvider();
         } else {
            DefaultRandomGenerator.instance = new DefaultRandomGenerator();
         }
      }
   }




   static {
      ACTIVE_GUARDS = new ConcurrentHashMap();
   }
}
