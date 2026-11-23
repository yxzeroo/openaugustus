package net.augustus.modules.modes.clicking;

import net.minecraft.util.MovingObjectPosition;
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
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class SimpleV2ClickPatternMode extends AbstractClickPatternMode {
   private final RandomizedRangeSettings simpleV2StartCPS;
   private final RandomizedRangeSettings simpleV2DefaultCPS;
   private final RandomizedRangeSettings simpleV2StartCPSDuration;
   private final RandomizedRangeSettings simpleV2RecalculateDelay;
   private final RangeSetting simpleV2ClickDutyCycle;
   private final BooleanValue simpleV2DoubleClicks;
   private final BooleanValue simpleV2ExtraDoubleClickRandomize;
   private final SimpleClickPatternSimulator simpleClickPatternSimulator;
   private final TickCounter tickCounter;
   private final TimeHelper combatElapsedTimer;
   private final TimeHelper lastTargetAimTimer;

   public SimpleV2ClickPatternMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "SimpleV2");
      this.simpleClickPatternSimulator = new SimpleClickPatternSimulator();
      this.tickCounter = new TickCounter();
      this.combatElapsedTimer = new TimeHelper();
      this.lastTargetAimTimer = new TimeHelper();
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(module, "SimpleV2StartCPS", 15.0, 17.0, 0.0, 20.0, 0, doubleValue);
      String str = "StartCPS";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.simpleV2StartCPS = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      randomizedRangeSettings = new RandomizedRangeSettings(module, "SimpleV2DefaultCPS", 10.0, 12.0, 0.0, 20.0, 0, doubleValue);
      String str2 = "DefaultCPS";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.simpleV2DefaultCPS = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      randomizedRangeSettings = new RandomizedRangeSettings(module, "SimpleV2StartCPSDuration", 15.0, 20.0, 0.0, 40.0, 0, doubleValue);
      String str3 = "StartCPSDuration";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str3);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.simpleV2StartCPSDuration = ((RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition3)).withoutNotInARow();
      randomizedRangeSettings = new RandomizedRangeSettings(module, "SimpleV2RecalculateDelay", 4.0, 8.0, 0.0, 20.0, 0, doubleValue);
      String str4 = "RecalculateDelay";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str4);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.simpleV2RecalculateDelay = ((RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition4)).withoutNotInARow();
      RangeSetting rangeSetting = new RangeSetting(module, "SimpleV2ClickDutyCycle", 0.35, 0.65, 0.0, 1.0, 2, doubleValue);
      String str5 = "ClickDutyCycle";
      RangeSetting rangeSetting2 = rangeSetting.named(str5);
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      this.simpleV2ClickDutyCycle = rangeSetting2.visibleWhen(condition5);
      BooleanValue booleanValue = new BooleanValue(module, "SimpleV2DoubleClicks", true, doubleValue);
      String str6 = "DoubleClicks";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str6);
      SettingVisibilityCondition condition6 = () -> this.isDoubleClicksVisible();
      this.simpleV2DoubleClicks = (BooleanValue)booleanValue2.withVisibility(condition6);
      BooleanValue booleanValue3 = new BooleanValue(module, "SimpleV2ExtraDoubleClickRandomize", false, doubleValue);
      String str7 = "ExtraDoubleClickRandomize";
      BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str7);
      SettingVisibilityCondition condition7 = () -> this.isExtraDoubleClickRandomizeVisible();
      this.simpleV2ExtraDoubleClickRandomize = (BooleanValue)booleanValue4.withVisibility(condition7);
   }

   @Override
   public void updateEventSubscriptions() {
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

   @Override
   public int computeClicks(EventClick event) {
      TickCounter tickCounter2 = this.tickCounter;
      int lastIntValue = this.simpleV2RecalculateDelay.getLastIntValue();
      if (tickCounter2.hasReached2(lastIntValue)) {
         this.simpleV2RecalculateDelay.nextValue();
         boolean flag = true;
         this.simpleV2StartCPS.nextValue(flag);
         boolean flag2 = true;
         this.simpleV2DefaultCPS.nextValue(flag2);
         this.tickCounter.reset();
      }

      int time = (int)this.combatElapsedTimer.getTime();
      int n = MathHelper.clamp_int(this.computeDecayedCps(time), 0, 40);
      SimpleClickPatternSimulator simulator = this.simpleClickPatternSimulator;
      double minValue = this.simpleV2ClickDutyCycle.getMinValue();
      double maxValue = this.simpleV2ClickDutyCycle.getMaxValue();
      boolean flag3 = this.simpleV2DoubleClicks.getValue() && this.simpleV2DoubleClicks.isVisible();
      boolean value = this.simpleV2ExtraDoubleClickRandomize.getValue();
      double d = 0.76;
      boolean flag4 = value;
      boolean flag5 = flag3;
      double d2 = maxValue;
      double d3 = minValue;
      TuplePair tuplePair = simulator.simulateTick(n, d3, d2, flag5, flag4, d);
      int first = (Integer)tuplePair.getFirst();
      ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
      boolean second = (Boolean)tuplePair.getSecond();
      parentModule2.setHoldingAttack(second);
      first = ((ClickModuleContext)this.parentModule).applyHitReduction(first);
      if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks() || first != first) {
         first = 0;
         ClickModuleContext parentModule3 = (ClickModuleContext)this.parentModule;
         boolean flag6 = false;
         parentModule3.setHoldingAttack(flag6);
      }

      return first;
   }

   @Override
   public void onTick2() {
      this.tickCounter.increment();
      if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
         this.lastTargetAimTimer.reset();
      }

      long l = 175L;
      if (this.lastTargetAimTimer.reached2(l)) {
         this.combatElapsedTimer.reset();
      }
   }

   @Override
   public boolean simulatesMousePress() {
      return true;
   }

   private int computeDecayedCps(int n) {
      int lastIntValue = this.simpleV2DefaultCPS.getLastIntValue();
      int lastIntValue2 = this.simpleV2StartCPS.getLastIntValue();
      double d = this.simpleV2StartCPSDuration.getLastValue() * 50.0;
      return (int)Math.round((double)lastIntValue + (double)(lastIntValue2 - lastIntValue) * Math.exp((double)(-n) / d));
   }

   private boolean isExtraDoubleClickRandomizeVisible() {
      return this.isSelected() && this.simpleV2DoubleClicks.isVisible() && this.simpleV2DoubleClicks.getValue();
   }

   private boolean isDoubleClicksVisible() {
      if (this.isSelected()) {
         if (this.simpleV2StartCPS.getMaxValue() <= 16.0 && this.simpleV2StartCPS.getMinValue() >= 10.0) {
            if (this.simpleV2DefaultCPS.getMaxValue() <= 16.0 && this.simpleV2DefaultCPS.getMinValue() >= 10.0) {
               return true;
            }
         }
      }

      return false;
   }

}
