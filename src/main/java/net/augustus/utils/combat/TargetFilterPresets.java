package net.augustus.utils.combat;


import net.augustus.settings.options.EntityTargetFilterOption;
import net.augustus.utils.target.TargetConditionOperator;
import net.augustus.utils.target.TargetFilterPreset;
public class TargetFilterPresets {

   public static EntityTargetFilter createRenderFilter() {
      EntityTargetFilterOption[] entityTargetFilterOptions = new EntityTargetFilterOption[11];
      boolean flag = false;
      entityTargetFilterOptions[0] = TargetFilterPreset.MYSELF.createOption(flag);
      boolean flag2 = true;
      entityTargetFilterOptions[1] = TargetFilterPreset.PLAYER.createOption(flag2);
      boolean flag3 = true;
      entityTargetFilterOptions[2] = TargetFilterPreset.MOB.createOption(flag3);
      boolean flag4 = false;
      entityTargetFilterOptions[3] = TargetFilterPreset.ANIMAL.createOption(flag4);
      boolean flag5 = true;
      entityTargetFilterOptions[4] = TargetFilterPreset.VILLAGER.createOption(flag5);
      boolean flag6 = false;
      entityTargetFilterOptions[5] = TargetFilterPreset.ARMOR_STAND.createOption(flag6);
      boolean flag7 = true;
      entityTargetFilterOptions[6] = TargetFilterPreset.TEAMMATE.createOption(flag7);
      boolean flag8 = true;
      entityTargetFilterOptions[7] = TargetFilterPreset.FRIEND.createOption(flag8);
      boolean flag9 = false;
      entityTargetFilterOptions[8] = TargetFilterPreset.ANTI_BOT.createOption(flag9);
      boolean flag10 = true;
      entityTargetFilterOptions[9] = TargetFilterPreset.INVISIBLE.createOption(flag10);
      boolean flag11 = true;
      entityTargetFilterOptions[10] = TargetFilterPreset.SLEEPING.createOption(flag11);
      return createFilter(entityTargetFilterOptions);
   }

   public static EntityTargetFilter createRenderFilterWithSelf() {
      EntityTargetFilterOption[] entityTargetFilterOptions = new EntityTargetFilterOption[11];
      boolean flag = false;
      EntityTargetFilterOption entityTargetFilterOption = TargetFilterPreset.MYSELF.createOption(flag);
      boolean flag2 = true;
      entityTargetFilterOptions[0] = entityTargetFilterOption.withVisible(flag2);
      boolean flag3 = true;
      entityTargetFilterOptions[1] = TargetFilterPreset.PLAYER.createOption(flag3);
      boolean flag4 = true;
      entityTargetFilterOptions[2] = TargetFilterPreset.MOB.createOption(flag4);
      boolean flag5 = false;
      entityTargetFilterOptions[3] = TargetFilterPreset.ANIMAL.createOption(flag5);
      boolean flag6 = true;
      entityTargetFilterOptions[4] = TargetFilterPreset.VILLAGER.createOption(flag6);
      boolean flag7 = false;
      entityTargetFilterOptions[5] = TargetFilterPreset.ARMOR_STAND.createOption(flag7);
      boolean flag8 = true;
      entityTargetFilterOptions[6] = TargetFilterPreset.TEAMMATE.createOption(flag8);
      boolean flag9 = true;
      entityTargetFilterOptions[7] = TargetFilterPreset.FRIEND.createOption(flag9);
      boolean flag10 = false;
      entityTargetFilterOptions[8] = TargetFilterPreset.ANTI_BOT.createOption(flag10);
      boolean flag11 = true;
      entityTargetFilterOptions[9] = TargetFilterPreset.INVISIBLE.createOption(flag11);
      boolean flag12 = true;
      entityTargetFilterOptions[10] = TargetFilterPreset.SLEEPING.createOption(flag12);
      return createFilter(entityTargetFilterOptions);
   }

   public static EntityTargetFilter createFilter(EntityTargetFilterOption[] entityTargetFilterOptions) {
      EntityTargetFilterOption[] entityTargetFilterOptions2 = new EntityTargetFilterOption[entityTargetFilterOptions.length];
      TargetConditionOperator or = TargetConditionOperator.OR;

      for (int i = 0; i < entityTargetFilterOptions.length; i++) {
         EntityTargetFilterOption entityTargetFilterOption = entityTargetFilterOptions[i];
         if (entityTargetFilterOption.operator == TargetConditionOperator.OR && or == TargetConditionOperator.AND) {
            throw new RuntimeException("Wrong init order for the targets. Or conditions have to be first!");
         }

         entityTargetFilterOptions2[i] = entityTargetFilterOption;
         or = entityTargetFilterOption.operator;
      }

      return new EntityTargetFilter(entityTargetFilterOptions2);
   }

   public static EntityTargetFilter createCombatFilter() {
      EntityTargetFilterOption[] entityTargetFilterOptions = new EntityTargetFilterOption[13];
      boolean flag = false;
      entityTargetFilterOptions[0] = TargetFilterPreset.MYSELF.createOption(flag);
      boolean flag2 = true;
      entityTargetFilterOptions[1] = TargetFilterPreset.PLAYER.createOption(flag2);
      boolean flag3 = true;
      entityTargetFilterOptions[2] = TargetFilterPreset.MOB.createOption(flag3);
      boolean flag4 = false;
      entityTargetFilterOptions[3] = TargetFilterPreset.ANIMAL.createOption(flag4);
      boolean flag5 = false;
      entityTargetFilterOptions[4] = TargetFilterPreset.VILLAGER.createOption(flag5);
      boolean flag6 = false;
      entityTargetFilterOptions[5] = TargetFilterPreset.ARMOR_STAND.createOption(flag6);
      boolean flag7 = false;
      entityTargetFilterOptions[6] = TargetFilterPreset.TEAMMATE.createOption(flag7);
      boolean flag8 = false;
      entityTargetFilterOptions[7] = TargetFilterPreset.FRIEND.createOption(flag8);
      boolean flag9 = false;
      entityTargetFilterOptions[8] = TargetFilterPreset.SHOP.createOption(flag9);
      boolean flag10 = false;
      entityTargetFilterOptions[9] = TargetFilterPreset.ANTI_BOT.createOption(flag10);
      boolean flag11 = true;
      entityTargetFilterOptions[10] = TargetFilterPreset.INVISIBLE.createOption(flag11);
      boolean flag12 = true;
      entityTargetFilterOptions[11] = TargetFilterPreset.SLEEPING.createOption(flag12);
      boolean flag13 = true;
      entityTargetFilterOptions[12] = TargetFilterPreset.DEAD.createOption(flag13);
      return createFilter(entityTargetFilterOptions);
   }

}
