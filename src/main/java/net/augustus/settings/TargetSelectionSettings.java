package net.augustus.settings;

import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.utils.combat.TargetSortMode;
import net.augustus.utils.rotation.Rotation;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Module;
import net.minecraft.entity.Entity;

public class TargetSelectionSettings extends ModuleSettingGroupBase {
   private NamedOptionSetting targetSortingSetting;
   private BooleanValue switchOnlyToTargetsInRange;
   private final String namePrefix;
   private Entity currentTarget;
   private final EntityTargetFilter targetFilter;
   private final MultiOptionSetting targets;

   public TargetSelectionSettings(Module module, String str, EntityTargetFilter entityTargetFilter) {
       this(module, str, entityTargetFilter, null);
   }

   public TargetSelectionSettings withSorting() {
      NamedOptionSetting namedOptionSetting = (NamedOptionSetting)(new NamedOptionSetting(
            this.getParentModule(), this.namePrefix + "TargetSorting", TargetSortMode.DISTANCE, TargetSortMode.values(), this.getParentGroup()
         ))
         .markVisualOnly();
      String str = "TargetSorting";
      this.targetSortingSetting = (NamedOptionSetting)namedOptionSetting.withDisplayName(str);
      BooleanValue booleanValue = (BooleanValue)(new BooleanValue(this.getParentModule(), this.namePrefix + "SwitchOnlyToTargetsInRange", false, this.getParentGroup())).markVisualOnly();
      SettingVisibilityCondition condition = () -> this.isSwitchInRangeVisible();
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withVisibility(condition);
      String str2 = "SwitchOnlyToTargetsInRange";
      this.switchOnlyToTargetsInRange = (BooleanValue)((BooleanValue)booleanValue2.withDisplayName(str2)).markVisualOnly();
      return this;
   }

   private boolean isSwitchInRangeVisible() {
      return this.targetSortingSetting.getSelectedOption() == TargetSortMode.ULTIMATE_TARGET && this.targetSortingSetting.isVisible();
   }

   public TargetSelectionSettings(Module module, EntityTargetFilter entityTargetFilter) {
       this(module, "", entityTargetFilter);
   }

   public void setCurrentTarget(Entity entity) {
      this.currentTarget = entity;
   }

   public ToDoubleFunction createTargetComparatorKey(TargetSortMode targetSortMode, Rotation rotation, double d) {
      if (targetSortMode != null) {
         Entity currentTarget2 = this.currentTarget;
         boolean value = this.switchOnlyToTargetsInRange.getValue();
         Entity entity = currentTarget2;
         return targetSortMode.createComparatorKey(rotation, entity, d, value);
      } else {
         return obj -> 100000.0;
      }
   }

   public EntityTargetFilter getTargetFilter() {
      return this.targetFilter;
   }

   public TargetSelectionSettings(Module module, String str, EntityTargetFilter entityTargetFilter, DoubleValue doubleValue) {
       super(module, doubleValue);
      MultiOptionSetting multiOptionSetting = new MultiOptionSetting(this.getParentModule(), str + "Targets", entityTargetFilter.getOptions(), doubleValue);
      String str2 = "Targets";
      this.targets = (MultiOptionSetting)multiOptionSetting.named(str2).markVisualOnly();
      this.namePrefix = str;
      this.targetFilter = entityTargetFilter;
   }

   public TargetSelectionSettings withTargetsDisplayName(String str) {
      this.targets.named(str);
      return this;
   }

   public ToDoubleFunction createTargetComparatorKey(Rotation rotation, double d) {
      if (this.targetSortingSetting == null) {
         throw new UnsupportedOperationException("Sorting not activated in the target selection");
      } else {
         TargetSortMode selectedOption = (TargetSortMode)this.targetSortingSetting.getSelectedOption();
         return this.createTargetComparatorKey(selectedOption, rotation, d);
      }
   }

   public TargetSelectionSettings withVisibility(SettingVisibilityCondition condition) {
      if (this.targetSortingSetting != null) {
         this.targetSortingSetting.withVisibility(condition);
      }

      this.targets.visibleWhen(condition);
      return this;
   }

   public TargetSelectionSettings(Module module, EntityTargetFilter entityTargetFilter, DoubleValue doubleValue) {
       this(module, "", entityTargetFilter, doubleValue);
   }


}
