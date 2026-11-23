package net.augustus.modules.legit;

import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class ClickDensitySpike {
   private StringValue strength;
   private RandomizedRangeSettings time;

   public ClickDensitySpike(Module module, DoubleValue doubleValue, int n) {
       super();
      DoubleValue doubleValue2 = new DoubleValue(module, "Spike " + n, doubleValue);
      StringValue stringValue = new StringValue(module, "Strength" + n + doubleValue.getName(), 50.0, 0.0, 100.0, 0, doubleValue2);
      String str = "Strength";
      this.strength = stringValue.named(str);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(module, "Time" + n + doubleValue.getName(), 50.0, 100.0, 0.0, 200.0, 0, doubleValue2)).withoutNotInARow();
      boolean flag = true;
      boolean flag2 = true;
      double d = 2.0;
      double d2 = 70.0;
      double d3 = 0.2;
      double d4 = 1.0;
      RandomizedRangeSettings randomizedRangeSettings2 = randomizedRangeSettings.withNormalDistribution(d4, d3, d2, d, flag2, flag);
      String str2 = "Time";
      this.time = (RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str2);
   }

   public double getStrength() {
      return this.strength.getValue();
   }

   public int getTime() {
      return this.time.nextInt();
   }

   public void randomize() {
      this.strength.randomize();
      this.time.randomize();
   }
}
