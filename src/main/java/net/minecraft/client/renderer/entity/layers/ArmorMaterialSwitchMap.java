package net.minecraft.client.renderer.entity.layers;

import net.minecraft.item.ItemArmor;

public class ArmorMaterialSwitchMap {
   public static final ArmorMaterialSwitchMap INSTANCE = new ArmorMaterialSwitchMap();
   public final int[] armorMaterialOrdinalMap = new int[ItemArmor.ArmorMaterial.values().length];

   public ArmorMaterialSwitchMap() {
      try {
         this.armorMaterialOrdinalMap[ItemArmor.ArmorMaterial.LEATHER.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.armorMaterialOrdinalMap[ItemArmor.ArmorMaterial.CHAIN.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.armorMaterialOrdinalMap[ItemArmor.ArmorMaterial.IRON.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.armorMaterialOrdinalMap[ItemArmor.ArmorMaterial.GOLD.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         this.armorMaterialOrdinalMap[ItemArmor.ArmorMaterial.DIAMOND.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }
   }
}
