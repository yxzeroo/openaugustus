package net.minecraft.client.renderer;

import net.minecraft.item.EnumAction;

public class ItemActionSwitchMap {
   public static final ItemActionSwitchMap INSTANCE = new ItemActionSwitchMap();
   public final int[] actionOrdinalMap = new int[EnumAction.values().length];

   public ItemActionSwitchMap() {
      try {
         this.actionOrdinalMap[EnumAction.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.actionOrdinalMap[EnumAction.EAT.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.actionOrdinalMap[EnumAction.DRINK.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.actionOrdinalMap[EnumAction.BLOCK.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         this.actionOrdinalMap[EnumAction.BOW.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }
   }
}
