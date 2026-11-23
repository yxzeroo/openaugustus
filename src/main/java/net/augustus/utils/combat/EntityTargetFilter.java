package net.augustus.utils.combat;

import net.augustus.settings.options.EntityTargetFilterOption;
import net.augustus.utils.target.TargetConditionOperator;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityTargetFilter {
   private final EntityTargetFilterOption[] options;
   private Predicate predicate;

   private Predicate combineAnd(Predicate predicate2) {
      return this.predicate == null ? predicate2 : this.predicate.and(predicate2);
   }

   public Predicate getPredicate() {
      return this.predicate;
   }

   public boolean matches(Entity entity) {
      return this.predicate.test(entity) && (!(entity instanceof EntityPlayer) || !CombatTargetUtil.hasProtectionEffect((EntityPlayer)entity));
   }

   private Predicate combineOr(Predicate predicate2) {
      return this.predicate == null ? predicate2 : this.predicate.or(predicate2);
   }

   public EntityTargetFilterOption[] getOptions() {
      return this.options;
   }

   public EntityTargetFilter(EntityTargetFilterOption[] entityTargetFilterOptions) {
       super();
      this.options = entityTargetFilterOptions;

      for (EntityTargetFilterOption entityTargetFilterOption : entityTargetFilterOptions) {
         if (entityTargetFilterOption.operator == TargetConditionOperator.OR) {
            entityTargetFilterOption.getClass();
            EntityTargetFilterOption entityTargetFilterOption2 = entityTargetFilterOption;
            Predicate predicate2 = obj -> entityTargetFilterOption2.matches((Entity)obj);
            this.predicate = this.combineOr(predicate2);
         } else {
            entityTargetFilterOption.getClass();
            EntityTargetFilterOption entityTargetFilterOption3 = entityTargetFilterOption;
            Predicate predicate3 = obj2 -> entityTargetFilterOption3.matches((Entity)obj2);
            this.predicate = this.combineAnd(predicate3);
         }
      }
   }
}
