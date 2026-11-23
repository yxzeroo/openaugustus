package net.augustus.settings;

import net.augustus.settings.property.DoubleArrayProperty;
import net.augustus.modules.Module;

public class DoubleArraySetting extends AbstractArraySetting {
   private final DoubleArrayProperty a;

   @Override
   public void resetToDefault() {
      this.a.resetToDefault();
   }

   public DoubleArraySetting(Module module, String str, Double[] doubles) {
       super(module, str);
      this.a = new DoubleArrayProperty(this, "A", new Double[0]);
      this.setDoubleValues(doubles);
      this.a.setDefaultValue(doubles);
   }

   public Double[] getDoubleValues() {
      return (Double[])this.a.getValue();
   }

   @Override
   public Object[] getValues() {
      return this.getDoubleValues();
   }

   public void setDoubleValues(Double[] doubles) {
      this.a.setValue(doubles);
   }

   @Override
   public void renderTooltip() {
   }

   @Override
   public void setValues(Object[] array) {
      Double[] doubles = (Double[])array;
      Double[] doubles2 = doubles;
      this.setDoubleValues(doubles2);
   }

   public DoubleArraySetting(Module module, String str) {
       super(module, str);
      this.a = new DoubleArrayProperty(this, "A", new Double[0]);
   }
}
