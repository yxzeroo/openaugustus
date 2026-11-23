package net.augustus.modules.modes.clicking;

import net.minecraft.util.MovingObjectPosition;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.math.DefaultRandomGenerator;
import java.security.SecureRandom;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class DeviationClickPatternMode extends AbstractClickPatternMode {
   private final RandomizedRangeSettings deviationCPS;
   private final BooleanValue deviationDoubleClick;
   private final StringValue deviationAirMultiplier;
   private long nextClickTimeMs;

   public DeviationClickPatternMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "Deviation");
      this.nextClickTimeMs = 0L;
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(module, "DeviationCPS", 13.0, 20.0, 0.0, 40.0, 0, doubleValue)).withoutNotInARow();
      String str = "MaxCPS";
      String str2 = "MinCPS";
      RandomizedRangeSettings randomizedRangeSettings2 = randomizedRangeSettings.withRangeNames(str2, str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.deviationCPS = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition);
      BooleanValue booleanValue = new BooleanValue(module, "DeviationDoubleClick", true, doubleValue);
      String str3 = "DoubleClick";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str3);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.deviationDoubleClick = (BooleanValue)booleanValue2.withVisibility(condition2);
      StringValue stringValue = new StringValue(module, "DeviationAirMultiplier", 0.4, 0.0, 1.0, 2, doubleValue);
      String str4 = "AirMultiplier";
      StringValue stringValue2 = stringValue.named(str4);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.deviationAirMultiplier = stringValue2.visibleWhen(condition3);
   }

   @Override
   public int computeClicks(EventClick event) {
      float minValue = (float)this.deviationCPS.getMinValue();
      float maxValue = (float)this.deviationCPS.getMaxValue();
      float f = (minValue + maxValue) / 2.0F;
      float f2 = (maxValue - minValue) / 6.0F;
      long l = System.currentTimeMillis();

      int n;
      for (n = 0; this.nextClickTimeMs < l; n++) {
         double d = (double)((int)(1000.0F / (float)((double)f + (double)f2 * new SecureRandom().nextGaussian())));
         if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
            d /= this.deviationAirMultiplier.getValue();
         }

         this.nextClickTimeMs = this.nextClickTimeMs + Math.min((long)d, 1000L);
      }

      if (!this.deviationDoubleClick.getValue() && n > 1) {
         n = 1;
      }

      if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks()) {
         n = 0;
      }

      return ((ClickModuleContext)this.parentModule).applyHitReduction(n);
   }

   @Override
   public void onTick2() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
      this.nextClickTimeMs = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
   }

   public void resetNextClickTime() {
      long l = System.currentTimeMillis();
      byte b = 45;
      byte b2 = 0;
      this.nextClickTimeMs = l - (long)DefaultRandomGenerator.instance.nextInt2(b2, b);
   }

}
