package net.augustus.modules.modes.clicking;

import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.TickCounter;
import net.augustus.utils.TuplePair;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class SimpleClickPatternMode extends AbstractClickPatternMode {
   private final RandomizedRangeSettings simpleCPS;
   private final RandomizedRangeSettings simpleCPSRecalculateDelay;
   private final RangeSetting simpleClickDutyCycle;
   private final BooleanValue simpleDoubleClicks;
   private final BooleanValue simpleExtraDoubleClickRandomize;
   private final SimpleClickPatternSimulator simpleClickPatternSimulator;
   private final TickCounter tickCounter;

   public SimpleClickPatternMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "Simple");
      this.simpleClickPatternSimulator = new SimpleClickPatternSimulator();
      this.tickCounter = new TickCounter();
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(module, "SimpleCPS", 12.0, 16.0, 0.0, 40.0, 0, doubleValue);
      String str = "CPS";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.simpleCPS = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, "SimpleCPSRecalculateDelay", 3.0, 8.0, 0.0, 20.0, 0, doubleValue)).withoutNotInARow();
      String str2 = "CPSRecalculateDelay";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.simpleCPSRecalculateDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      RangeSetting rangeSetting = new RangeSetting(module, "SimpleClickDutyCycle", 0.35, 0.65, 0.0, 1.0, 2, doubleValue);
      String str3 = "ClickDutyCycle";
      RangeSetting rangeSetting2 = rangeSetting.named(str3);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.simpleClickDutyCycle = rangeSetting2.visibleWhen(condition3);
      BooleanValue booleanValue = new BooleanValue(module, "SimpleDoubleClicks", true, doubleValue);
      String str4 = "DoubleClicks";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str4);
      SettingVisibilityCondition condition4 = () -> this.isDoubleClicksVisible();
      this.simpleDoubleClicks = (BooleanValue)booleanValue2.withVisibility(condition4);
      BooleanValue booleanValue3 = new BooleanValue(module, "SimpleExtraDoubleClickRandomize", false, doubleValue);
      String str5 = "ExtraDoubleClickRandomize";
      BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str5);
      SettingVisibilityCondition condition5 = () -> this.isExtraDoubleClickRandomizeVisible();
      this.simpleExtraDoubleClickRandomize = (BooleanValue)booleanValue4.withVisibility(condition5);
   }

   @Override
   public int computeClicks(EventClick event) {
      TickCounter tickCounter2 = this.tickCounter;
      int lastIntValue = this.simpleCPSRecalculateDelay.getLastIntValue();
      if (tickCounter2.hasReached2(lastIntValue)) {
         this.simpleCPSRecalculateDelay.nextValue();
         this.simpleCPS.nextValue();
         this.tickCounter.reset();
      }

      SimpleClickPatternSimulator simulator = this.simpleClickPatternSimulator;
      int lastIntValue2 = this.simpleCPS.getLastIntValue();
      double minValue = this.simpleClickDutyCycle.getMinValue();
      double maxValue = this.simpleClickDutyCycle.getMaxValue();
      boolean flag = this.simpleDoubleClicks.getValue() && this.simpleDoubleClicks.isVisible();
      boolean value = this.simpleExtraDoubleClickRandomize.getValue();
      double d = 0.76;
      boolean flag2 = value;
      boolean flag3 = flag;
      double d2 = maxValue;
      double d3 = minValue;
      int n = lastIntValue2;
      TuplePair tuplePair = simulator.simulateTick(n, d3, d2, flag3, flag2, d);
      int first = (Integer)tuplePair.getFirst();
      ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
      boolean second = (Boolean)tuplePair.getSecond();
      parentModule2.setHoldingAttack(second);
      first = ((ClickModuleContext)this.parentModule).applyHitReduction(first);
      if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks() || first != first) {
         first = 0;
         ClickModuleContext parentModule3 = (ClickModuleContext)this.parentModule;
         boolean flag4 = false;
         parentModule3.setHoldingAttack(flag4);
      }

      return first;
   }

   @Override
   public void onTick2() {
      this.tickCounter.increment();
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public boolean simulatesMousePress() {
      return true;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
      boolean flag = false;
      parentModule2.setHoldingAttack(flag);
   }

   private boolean isExtraDoubleClickRandomizeVisible() {
      return this.isSelected() && this.simpleDoubleClicks.isVisible() && this.simpleDoubleClicks.getValue();
   }

   private boolean isDoubleClicksVisible() {
      if (this.isSelected()) {
         if (this.simpleCPS.getMaxValue() <= 16.0 && this.simpleCPS.getMinValue() >= 10.0) {
            return true;
         }
      }

      return false;
   }

}
