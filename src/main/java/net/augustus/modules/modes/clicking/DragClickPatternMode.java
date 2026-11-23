package net.augustus.modules.modes.clicking;

import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.utils.TickCounter;
import net.augustus.utils.combat.CombatOptimizationUtil;
import java.util.ArrayList;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

public class DragClickPatternMode extends AbstractClickPatternMode {
   private final RandomizedRangeSettings dragCPSInAir;
   private final RandomizedRangeSettings dragCPSInAirRecalculateTickDelay;
   private final RandomizedRangeSettings maxCPS;
   private final RandomizedRangeSettings maxCPSRecalculateTickDelay;
   private final StringValue minStandardDeviation;
   private final ArrayList recentClickCounts;
   private final ActionRateAccumulator actionRateAccumulator;
   private final TickCounter airCpsRecalcCounter;
   private final TickCounter maxCpsRecalcCounter;

   public DragClickPatternMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "DragClick");
      this.recentClickCounts = new ArrayList();
      this.actionRateAccumulator = new ActionRateAccumulator(20, 0.0);
      this.airCpsRecalcCounter = new TickCounter();
      this.maxCpsRecalcCounter = new TickCounter();
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(module, "DragCPSInAir", 4.0, 8.0, 0.0, 20.0, 0, doubleValue);
      String str = "CPSInAir";
      RandomizedRangeSettings randomizedRangeSettings2 = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.dragCPSInAir = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition);
      RandomizedRangeSettings randomizedRangeSettings3 = new RandomizedRangeSettings(module, "DragCPSInAirRecalculateTickDelay", 10.0, 30.0, 0.0, 60.0, 0, doubleValue);
      String str2 = "CPSInAirRecalculateTickDelay";
      RandomizedRangeSettings randomizedRangeSettings4 = ((RandomizedRangeSettings)randomizedRangeSettings3.withDisplayNamePrefix(str2)).withoutNotInARow();
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.dragCPSInAirRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition2);
      RandomizedRangeSettings randomizedRangeSettings5 = new RandomizedRangeSettings(module, "MaxCPS", 18.0, 20.0, 1.0, 40.0, 0, doubleValue);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.maxCPS = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings6 = (new RandomizedRangeSettings(module, "MaxCPSRecalculateTickDelay", 3.0, 8.0, 1.0, 40.0, 0, doubleValue)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.maxCPSRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition4);
      StringValue stringValue = new StringValue(module, "MinStandardDeviation", 0.5, 0.0, 1.0, 2, doubleValue);
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      this.minStandardDeviation = stringValue.visibleWhen(condition5);
   }

   @Override
   public int computeClicks(EventClick event) {
      MovingObjectPosition objectMouseOver = mc.objectMouseOver;
      int n;
      if (this.recentClickCounts.size() < 5) {
         n = 1;
      } else {
         TickCounter maxCpsRecalcCounter2 = this.maxCpsRecalcCounter;
         int lastIntValue = this.maxCPSRecalculateTickDelay.getLastIntValue();
         if (maxCpsRecalcCounter2.hasReached2(lastIntValue)) {
            this.maxCpsRecalcCounter.reset();
            this.maxCPSRecalculateTickDelay.nextSecureValue();
            this.maxCPS.nextSecureValue();
         }

         ArrayList recentClickCounts2 = this.recentClickCounts;
         double value = this.minStandardDeviation.getValue();
         double lastIntValue2 = (double)this.maxCPS.getLastIntValue();
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
      } else if (n == 0) {
         ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
         if (parentModule2.isCritFixEnabled() && mc.objectMouseOver != null) {
            boolean flag = ClientManagers.moduleManager.criticals.isToggled()
               || mc.thePlayer.fallDistance > 0.0F
                  && !mc.thePlayer.onGround
                  && !mc.thePlayer.isOnLadder()
                  && !mc.thePlayer.isInWater()
                  && !mc.thePlayer.isPotionActive(Potion.blindness)
                  && mc.thePlayer.ridingEntity == null
                  && mc.objectMouseOver.entityHit instanceof EntityLivingBase;
            if (!((ClickModuleContext)this.parentModule).hasLandedCrit() && flag) {
               n = 1;
            }
         }
      }

      this.recentClickCounts.add(n);

      while (this.recentClickCounts.size() > 21) {
         this.recentClickCounts.remove(0);
      }

      return n;
   }

   @Override
   public void onTick2() {
      this.airCpsRecalcCounter.increment();
      this.maxCpsRecalcCounter.increment();
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
      this.recentClickCounts.clear();
   }

   @Override
   public void onDisable() {
      this.recentClickCounts.clear();
   }

}
