package net.augustus.settings;

import net.augustus.font.CustomFontUtil;
import net.augustus.settings.property.DoubleProperty;
import net.augustus.utils.math.MathStatisticsUtil;
import java.io.IOException;
import java.util.function.Consumer;
import net.augustus.modules.Module;
import net.minecraft.util.MathHelper;

public class RangeSetting extends Setting {
   private Runnable valueUpdatedCallback;
   private boolean maxFirst;
   private boolean wasMaxFieldFocused;
   private boolean wasMinFieldFocused;
   private final DoubleProperty f;
   public StringValue min;
   public StringValue max;
   private final DoubleProperty s;
   private int labelWidth;

   public double getMinValue(boolean flag) {
      return flag ? MathHelper.clamp_double((Double)this.f.getValue(), this.min.getMinValue(), this.min.getMaxValue()) : (Double)this.f.getValue();
   }

   @Override
   public void render(int n, int n2, int n3, int n4, int n5, int n6, CustomFontUtil customFontUtil, boolean flag) {
      super.render(n, n2, n3, n4, n5, n6 * 2, customFontUtil, flag);
      boolean flag2 = this.labelWidth == -1;
      boolean flag3 = flag2;
      if (this.consumeLabelDirty(flag3)) {
         String str = this.min.getDisplayName() + ": ";
         String str2 = str;
         this.labelWidth = (int)customFontUtil.getStringWidth(str2);
      }

      if (this.maxFirst) {
         this.max.render(n, n2, n3, n4, n5, n6, customFontUtil, flag);
         double value = this.max.getValue();
         this.setMaxValue(value);
         StringValue min2 = this.min;
         int n7 = n4 + this.max.getHeight();
         int n8 = n7;
         min2.render(n, n2, n3, n8, n5, n6, customFontUtil, flag);
         double value2 = this.min.getValue();
         double d = value2;
         this.setMinValue(d);
      } else {
         this.min.render(n, n2, n3, n4, n5, n6, customFontUtil, flag);
         double value3 = this.min.getValue();
         double d2 = value3;
         this.setMinValue(d2);
         StringValue max2 = this.max;
         int n9 = n4 + this.min.getHeight();
         int n10 = n9;
         max2.render(n, n2, n3, n10, n5, n6, customFontUtil, flag);
         double value4 = this.max.getValue();
         this.setMaxValue(value4);
      }

      double minValue = this.getMinValue();
      if (minValue > this.getMaxValue()) {
         if (this.min.getSlider().isDragging() || this.wasMinFieldFocused && !this.min.getTextField().isFocused() || this.min.isWasArrowAdjusting() || this.min.isScrollAdjusted()) {
            StringValue max3 = this.max;
            double value5 = this.min.getValue();
            max3.setValue(value5);
            double value6 = this.max.getValue();
            this.setMaxValue(value6);
         }

         if (this.max.getSlider().isDragging() || this.wasMaxFieldFocused && !this.max.getTextField().isFocused() || this.max.isWasArrowAdjusting() || this.max.isScrollAdjusted()) {
            StringValue min3 = this.min;
            double value7 = this.max.getValue();
            min3.setValue(value7);
            double value8 = this.min.getValue();
            double d3 = value8;
            this.setMinValue(d3);
         }
      }

      this.wasMinFieldFocused = this.min.getTextField().isFocused();
      this.wasMaxFieldFocused = this.max.getTextField().isFocused();
   }

   public RangeSetting withValueUpdatedCallback(Runnable runnable) {
      this.valueUpdatedCallback = runnable;
      return this;
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.min.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.max.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
   }

   public RangeSetting(Module module, String str, String str2, double d, double d2, double d3, double d4, int n, DoubleValue doubleValue) {
       super(module, doubleValue, str + str2);
      DoubleProperty doubleProperty = new DoubleProperty(this, "F", 0.0);
      Consumer consumer = obj -> this.setMinValue((double)obj);
      this.f = (DoubleProperty)doubleProperty.withOnValueChanged(consumer);
      DoubleProperty doubleProperty2 = new DoubleProperty(this, "S", 0.0);
      Consumer consumer2 = obj2 -> this.setMaxValue((double)obj2);
      this.s = (DoubleProperty)doubleProperty2.withOnValueChanged(consumer2);
      this.maxFirst = false;
      this.labelWidth = -1;
      this.valueUpdatedCallback = null;
      this.wasMinFieldFocused = false;
      this.wasMaxFieldFocused = false;
      DoubleProperty doubleProperty3 = this.f;
      Double d5 = d;
      Double d6 = d5;
      doubleProperty3.setValue(d6);
      doubleProperty3 = this.s;
      Double d7 = d2;
      Double d8 = d7;
      doubleProperty3.setValue(d8);
      doubleProperty3 = this.f;
      Double d9 = d;
      doubleProperty3.setDefaultValue(d9);
      doubleProperty3 = this.s;
      Double d10 = d2;
      doubleProperty3.setDefaultValue(d10);
      StringValue stringValue = new StringValue(str, d, d3, d4, n);
      SettingVisibilityCondition condition = () -> this.isVisible();
      this.min = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(str2, d2, d3, d4, n);
      SettingVisibilityCondition condition2 = () -> this.isVisible();
      this.max = stringValue2.visibleWhen(condition2);
   }

   public void setDecimalPlaces(long l, int n) {
      this.max.setDecimalPlaces(n);
      this.min.setDecimalPlaces(n);
   }

   public RangeSetting(Module module, String str, double d, double d2, double d3, double d4, int n) {
       super(module, null, str);
      DoubleProperty doubleProperty = new DoubleProperty(this, "F", 0.0);
      Consumer consumer = obj -> this.setMinValue((double)obj);
      this.f = (DoubleProperty)doubleProperty.withOnValueChanged(consumer);
      DoubleProperty doubleProperty2 = new DoubleProperty(this, "S", 0.0);
      Consumer consumer2 = obj2 -> this.setMaxValue((double)obj2);
      this.s = (DoubleProperty)doubleProperty2.withOnValueChanged(consumer2);
      this.maxFirst = false;
      this.labelWidth = -1;
      this.valueUpdatedCallback = null;
      this.wasMinFieldFocused = false;
      this.wasMaxFieldFocused = false;
      DoubleProperty doubleProperty3 = this.f;
      Double d5 = d;
      Double d6 = d5;
      doubleProperty3.setValue(d6);
      doubleProperty3 = this.s;
      Double d7 = d2;
      Double d8 = d7;
      doubleProperty3.setValue(d8);
      doubleProperty3 = this.f;
      Double d9 = d;
      doubleProperty3.setDefaultValue(d9);
      doubleProperty3 = this.s;
      Double d10 = d2;
      doubleProperty3.setDefaultValue(d10);
      StringValue stringValue = new StringValue("Min" + str, d, d3, d4, n);
      SettingVisibilityCondition condition = () -> this.isVisible();
      this.min = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue("Max" + str, d2, d3, d4, n);
      SettingVisibilityCondition condition2 = () -> this.isVisible();
      this.max = stringValue2.visibleWhen(condition2);
   }

   @Override
   public String[] getTranslationKeys() {
      return new String[]{this.min.getName(), this.max.getName()};
   }

   public double getMinValue() {
      boolean flag = false;
      return this.getMinValue(flag);
   }

   @Override
   public Setting withDisplayName(String str) {
      return this.named(str);
   }

   public double getMaxValue() {
      boolean flag = false;
      return this.getMaxValue(flag);
   }

   @Override
   public int getHeight() {
      return this.min.getHeight() + this.max.getHeight();
   }

   public RangeSetting named(String str) {
      String str2 = "Min" + str;
      String str3 = "Max" + str;
      String str4 = str3;
      String str5 = str2;
      return this.withBoundNames(str5, str4);
   }

   public double getMaxValue(boolean flag) {
      return flag ? MathHelper.clamp_double((Double)this.s.getValue(), this.max.getMinValue(), this.max.getMaxValue()) : (Double)this.s.getValue();
   }

   public Runnable getValueUpdatedCallback() {
      return this.valueUpdatedCallback;
   }

   public RangeSetting withMaxFirst() {
      this.maxFirst = true;
      return this;
   }

   @Override
   public void applyTranslation(String str, String str2) {
      if (str2.equals("English_Default")) {
         boolean flag = true;
         this.min.setLabelDirty(flag);
         boolean flag2 = true;
         this.max.setLabelDirty(flag2);
         String str3 = "";
         this.min.setTranslatedName(str3);
         String str4 = "";
         this.max.setTranslatedName(str4);
      } else {
         if (this.min.getName().equalsIgnoreCase(str)) {
            boolean flag3 = true;
            this.min.setLabelDirty(flag3);
            this.min.setTranslatedName(str2);
         } else if (this.max.getName().equalsIgnoreCase(str)) {
            boolean flag4 = true;
            this.max.setLabelDirty(flag4);
            this.max.setTranslatedName(str2);
         }
      }
   }

   @Override
   public void resetToDefault() {
      this.f.resetToDefault();
      this.s.resetToDefault();
      double value = (Double)this.f.getValue();
      double d = value;
      this.setMinValue(d);
      double value2 = (Double)this.s.getValue();
      this.setMaxValue(value2);
   }

   public RangeSetting(Module module, String str, String str2, double d, double d2, double d3, double d4, int n) {
       super(module, null, str + str2);
      DoubleProperty doubleProperty = new DoubleProperty(this, "F", 0.0);
      Consumer consumer = obj -> this.setMinValue((double)obj);
      this.f = (DoubleProperty)doubleProperty.withOnValueChanged(consumer);
      DoubleProperty doubleProperty2 = new DoubleProperty(this, "S", 0.0);
      Consumer consumer2 = obj2 -> this.setMaxValue((double)obj2);
      this.s = (DoubleProperty)doubleProperty2.withOnValueChanged(consumer2);
      this.maxFirst = false;
      this.labelWidth = -1;
      this.valueUpdatedCallback = null;
      this.wasMinFieldFocused = false;
      this.wasMaxFieldFocused = false;
      DoubleProperty doubleProperty3 = this.f;
      Double d5 = d;
      Double d6 = d5;
      doubleProperty3.setValue(d6);
      doubleProperty3 = this.s;
      Double d7 = d2;
      Double d8 = d7;
      doubleProperty3.setValue(d8);
      doubleProperty3 = this.f;
      Double d9 = d;
      doubleProperty3.setDefaultValue(d9);
      doubleProperty3 = this.s;
      Double d10 = d2;
      doubleProperty3.setDefaultValue(d10);
      StringValue stringValue = new StringValue(str, d, d3, d4, n);
      SettingVisibilityCondition condition = () -> this.isVisible();
      this.min = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(str2, d2, d3, d4, n);
      SettingVisibilityCondition condition2 = () -> this.isVisible();
      this.max = stringValue2.visibleWhen(condition2);
   }

   @Override
   public Setting withVisibility(SettingVisibilityCondition condition) {
      return this.visibleWhen(condition);
   }

   @Override
   public void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
      this.min.mouseReleased(mouseX, mouseY, state);
      this.max.mouseReleased(mouseX, mouseY, state);
   }

   public int getDecimalPlaces(long l, int n) {
      return this.min.getDecimalPlaces();
   }

   public RangeSetting(Module module, String str, double d, double d2, double d3, double d4, int n, DoubleValue doubleValue) {
       super(module, doubleValue, str);
      DoubleProperty doubleProperty = new DoubleProperty(this, "F", 0.0);
      Consumer consumer = obj -> this.setMinValue((double)obj);
      this.f = (DoubleProperty)doubleProperty.withOnValueChanged(consumer);
      DoubleProperty doubleProperty2 = new DoubleProperty(this, "S", 0.0);
      Consumer consumer2 = obj2 -> this.setMaxValue((double)obj2);
      this.s = (DoubleProperty)doubleProperty2.withOnValueChanged(consumer2);
      this.maxFirst = false;
      this.labelWidth = -1;
      this.valueUpdatedCallback = null;
      this.wasMinFieldFocused = false;
      this.wasMaxFieldFocused = false;
      DoubleProperty doubleProperty3 = this.f;
      Double d5 = d;
      Double d6 = d5;
      doubleProperty3.setValue(d6);
      doubleProperty3 = this.s;
      Double d7 = d2;
      Double d8 = d7;
      doubleProperty3.setValue(d8);
      doubleProperty3 = this.f;
      Double d9 = d;
      doubleProperty3.setDefaultValue(d9);
      doubleProperty3 = this.s;
      Double d10 = d2;
      doubleProperty3.setDefaultValue(d10);
      StringValue stringValue = new StringValue("Min" + str, d, d3, d4, n);
      SettingVisibilityCondition condition = () -> this.isVisible();
      this.min = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue("Max" + str, d2, d3, d4, n);
      SettingVisibilityCondition condition2 = () -> this.isVisible();
      this.max = stringValue2.visibleWhen(condition2);
   }

   public void setMinValue(double d) {
      DoubleProperty doubleProperty = this.f;
      Double d2 = d;
      Double d3 = d2;
      doubleProperty.setValue(d3);
      this.min.setValue(d);
   }

   public void addPropertyChangeCallback(Runnable runnable) {
      this.f.addChangeListener(runnable);
      this.s.addChangeListener(runnable);
   }

   @Override
   public Setting withChangeListener(SettingChangeListener listener) {
      return this.onChange(listener);
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      this.min.keyTyped(typedChar, keyCode);
      this.max.keyTyped(typedChar, keyCode);
   }

   public void setMaxValue(double d) {
      DoubleProperty doubleProperty = this.s;
      Double d2 = d;
      Double d3 = d2;
      doubleProperty.setValue(d3);
      this.max.setValue(d);
   }

   @Override
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      this.min.mouseClicked(mouseX, mouseY, mouseButton);
      this.max.mouseClicked(mouseX, mouseY, mouseButton);
      return false;
   }

   public RangeSetting visibleWhen(SettingVisibilityCondition condition) {
      this.setVisibilityCondition(condition);
      return this;
   }

   @Override
   public void randomize() {
      double d = MathStatisticsUtil.lerp(this.min.getMinValue(), this.min.getMaxValue(), Math.random());
      double d2 = d;
      this.setMinValue(d2);
      double d3 = MathStatisticsUtil.lerp((Double)this.f.getValue(), this.max.getMaxValue(), Math.random());
      this.setMaxValue(d3);
   }

   public double getMinimumBound(long l) {
      return 0.0;
   }

   public double getMaximumBound() {
      return this.min.getMaxValue();
   }

   public RangeSetting withBoundNames(String str, String str2) {
      this.min.setDisplayName(str);
      this.max.setDisplayName(str2);
      return this;
   }

   @Override
   public void renderTooltip() {
      int labelWidth2 = this.labelWidth;
      this.drawDescriptionTooltip(labelWidth2);
   }

   public RangeSetting onChange(SettingChangeListener listener) {
      this.changeListener = listener;
      this.min = (StringValue)this.min.withChangeListener(listener);
      this.max = (StringValue)this.max.withChangeListener(listener);
      return this;
   }


}
