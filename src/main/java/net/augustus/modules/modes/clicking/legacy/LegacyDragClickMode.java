package net.augustus.modules.modes.clicking.legacy;

import net.augustus.ClientManagers;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.utils.TickCounter;
import net.augustus.utils.combat.CombatOptimizationUtil;
import java.util.ArrayList;
import java.util.function.BiFunction;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class LegacyDragClickMode extends AbstractLegacyClickMode {
   private final RandomizedRangeSettings dragCPSInAir;
   private final RandomizedRangeSettings dragCPSInAirRecalculateTickDelay;
   private final RandomizedRangeSettings dragMaxCPS;
   private final RandomizedRangeSettings dragMaxCPSRecalculateTickDelay;
   private final StringValue dragMinStandardDeviation;
   private final ActionRateAccumulator actionRateAccumulator;
   private final ArrayList recentClickCounts;
   private final TickCounter maxCpsRecalcCounter;
   private final TickCounter airCpsRecalcCounter;

   public LegacyDragClickMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "DragClick");
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this.parentModule, "DragCPSInAir", 4.0, 8.0, 0.0, 20.0, 0, this.modeSetting);
      String str = "CPSInAir";
      this.dragCPSInAir = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      RandomizedRangeSettings randomizedRangeSettings2 = new RandomizedRangeSettings(this.parentModule, "DragCPSInAirRecalculateTickDelay", 10.0, 30.0, 0.0, 60.0, 0, this.modeSetting);
      String str2 = "CPSInAirRecalculateTickDelay";
      this.dragCPSInAirRecalculateTickDelay = ((RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str2)).withoutNotInARow();
      randomizedRangeSettings2 = new RandomizedRangeSettings(this.parentModule, "DragMaxCPS", 18.0, 20.0, 1.0, 40.0, 0, this.modeSetting);
      String str3 = "MaxCPS";
      this.dragMaxCPS = (RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str3);
      randomizedRangeSettings2 = (new RandomizedRangeSettings(this.parentModule, "DragMaxCPSRecalculateTickDelay", 3.0, 8.0, 1.0, 40.0, 0, this.modeSetting)).withoutNotInARow();
      String str4 = "MaxCPSRecalculateTickDelay";
      this.dragMaxCPSRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str4);
      StringValue stringValue = new StringValue(this.parentModule, "DragMinStandardDeviation", 0.5, 0.0, 1.0, 2, this.modeSetting);
      String str5 = "MinStandardDeviation";
      this.dragMinStandardDeviation = stringValue.named(str5);
      this.actionRateAccumulator = new ActionRateAccumulator(20, 0.0);
      this.recentClickCounts = new ArrayList();
      this.maxCpsRecalcCounter = new TickCounter();
      this.airCpsRecalcCounter = new TickCounter();
   }

   @Override
   public int computeClicks(EntityLivingBase entity) {
      MovingObjectPosition objectMouseOver = mc.objectMouseOver;
      int n;
      if (this.recentClickCounts.size() < 5) {
         n = 1;
      } else {
         TickCounter maxCpsRecalcCounter2 = this.maxCpsRecalcCounter;
         int lastIntValue = this.dragMaxCPSRecalculateTickDelay.getLastIntValue();
         if (maxCpsRecalcCounter2.hasReached2(lastIntValue)) {
            this.maxCpsRecalcCounter.reset();
            this.dragMaxCPSRecalculateTickDelay.nextSecureValue();
            this.dragMaxCPS.nextSecureValue();
         }

         ArrayList recentClickCounts2 = this.recentClickCounts;
         double value = this.dragMinStandardDeviation.getValue();
         double lastIntValue2 = (double)this.dragMaxCPS.getLastIntValue();
         double d = value;
         byte b = 21;
         n = CombatOptimizationUtil.chooseOptimalClickCount(recentClickCounts2, b, d, lastIntValue2);
      }

      if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks()) {
         n = 0;
      }

      if (objectMouseOver == null || objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
         TickCounter airCpsRecalcCounter2 = this.airCpsRecalcCounter;
         int lastIntValue3 = this.dragCPSInAirRecalculateTickDelay.getLastIntValue();
         if (airCpsRecalcCounter2.hasReached2(lastIntValue3)) {
            this.airCpsRecalcCounter.reset();
            this.dragCPSInAirRecalculateTickDelay.nextSecureValue();
            this.dragCPSInAir.nextSecureValue();
         }

         ActionRateAccumulator actionRateAccumulator2 = this.actionRateAccumulator;
         int lastIntValue4 = this.dragCPSInAir.getLastIntValue();
         n = actionRateAccumulator2.accumulate(lastIntValue4);
      }

      this.recentClickCounts.add(n);

      while (this.recentClickCounts.size() > 21) {
         this.recentClickCounts.remove(0);
      }

      return n;
   }

   @Override
   public void onClickPerformed(EntityLivingBase entity) {
   }

   @Override
   public void onTickUpdate(long l) {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new LegacyDragClickMode((Module)obj, (DoubleValue)obj2);
   }

}
