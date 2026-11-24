package net.augustus.modules;

import java.util.ArrayList;
import java.util.List;

public enum Categorys {
   COMBAT,
   LEGIT,
   LATENCY,
   MOVEMENT,
   PLAYER,
   RENDER,
   WORLD,
   MISC,
   FUN;

private final List modules = new ArrayList();
   private final List displayModules;
   public final boolean special;
   public final boolean hidden;
   private static boolean controlFlowFlag;

   private Categorys() {
      this.displayModules = new ArrayList();
      this.special = false;
      this.hidden = false;
   }

   private Categorys(int n, boolean flag) {
      this.displayModules = new ArrayList();
      this.special = flag;
      this.hidden = flag;
   }

   public List getModules() {
      return this.modules;
   }

   public List getDisplayModules() {
      return this.displayModules;
   }

   static {
      if (isControlFlowFlagCleared()) {
         setControlFlowFlag(true);
      }
   }



   public static void setControlFlowFlag(boolean flag) {
      controlFlowFlag = flag;
   }

   public static boolean isControlFlowFlag() {
      return controlFlowFlag;
   }

   public static boolean isControlFlowFlagCleared() {
      return !isControlFlowFlag();
   }
}
