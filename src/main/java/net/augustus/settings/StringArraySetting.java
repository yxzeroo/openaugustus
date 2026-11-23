package net.augustus.settings;

import net.augustus.settings.property.StringArrayProperty;
import net.augustus.modules.Module;

public class StringArraySetting extends AbstractArraySetting {
   private final StringArrayProperty a;

   @Override
   public Object[] getValues() {
      return this.getStringValues();
   }

   @Override
   public void resetToDefault() {
      this.a.resetToDefault();
   }

   public void setStringValues(String[] strings) {
      this.a.setValue(strings);
   }

   @Override
   public void setValues(Object[] array) {
      String[] strings = (String[])array;
      this.setStringValues(strings);
   }

   public StringArraySetting(Module module, String str) {
       super(module, str);
      this.a = new StringArrayProperty(this, "A", new String[0]);
   }

   public StringArraySetting(Module module, String str, String[] strings) {
       super(module, str);
      this.a = new StringArrayProperty(this, "A", new String[0]);
      this.setStringValues(strings);
      this.a.setDefaultValue(strings);
   }

   public String[] getStringValues() {
      return (String[])this.a.getValue();
   }
}
