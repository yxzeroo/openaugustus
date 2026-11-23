package net.augustus.settings;

import net.augustus.ClientManagers;
import net.augustus.clickgui.components.TextFieldComponent;
import net.augustus.font.CustomFontUtil;
import net.augustus.render.RenderUtil;
import net.augustus.settings.property.StringProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.augustus.modules.Module;
import net.augustus.modules.misc.Dummy;

public class TextInputSetting extends Setting {
   private final StringProperty n;
   private int labelWidth;
   private final TextFieldComponent textField;
   private long lastClickTimeMs;

   public TextInputSetting(Module module, String str, String str2, boolean flag, DoubleValue doubleValue) {
       super(module, doubleValue, str);
      this.labelWidth = -1;
      this.lastClickTimeMs = 0L;
      this.textField = new TextFieldComponent(str2, flag ? StringInputMode.HIDDEN : StringInputMode.DEFAULT);
      StringProperty stringProperty = new StringProperty(this, "N", str2, str2);
      Consumer consumer = obj -> this.setTextFieldValue((String)obj);
      this.n = (StringProperty)stringProperty.withOnValueChanged(consumer);
      if (module != null && !(module instanceof Dummy)) {
         ClientManagers.settingManager.registerSetting(this);
         ArrayList list = doubleValue != null ? doubleValue.getChildren() : module.getSettings();
         list.removeIf(obj2 -> this.matchesSettingName((Setting)obj2));
         list.add(this);
      }
   }

   @Override
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.textField.mouseClicked(mouseX, mouseY, mouseButton);
      long l = System.currentTimeMillis();
      float f = (float)(this.renderX - 2);
      float renderY2 = (float)this.renderY;
      CustomFontUtil renderFont2 = this.renderFont;
      String text = this.textField.getText();
      String str = text;
      if (RenderUtil.isHovered(f, renderY2, renderFont2.getStringWidth(str) + 2.0F + (float)this.labelWidth, this.renderFont.getFontHeight() + 1.0F, mouseX, mouseY)) {
         if (this.lastClickTimeMs + 400L > l) {
            boolean flag = true;
            this.textField.setFocused(flag);
         }

         this.lastClickTimeMs = l;
      }

      return false;
   }

   public TextInputSetting(Module module, String str, boolean flag) {
       this(module, str, "", flag);
   }

   private boolean matchesSettingName(Setting setting) {
      return setting.getName().equals(this.getName());
   }

   public void setValue(String str) {
      this.n.setValue(str);
      this.textField.setText(str);
   }

   @Override
   public void render(int n2, int n3, int n4, int n5, int n6, int n7, CustomFontUtil customFontUtil, boolean flag) {
      super.render(n2, n3, n4, n5, n6, n7, customFontUtil, flag);
      String str = this.getDisplayName() + ": ";
      boolean flag2 = this.labelWidth == -1;
      boolean flag3 = flag2;
      if (this.consumeLabelDirty(flag3)) {
         this.labelWidth = (int)customFontUtil.getStringWidth(str);
      }

      if (flag) {
         byte b = 0;
         int colorInt = ClientManagers.moduleManager.clickGui.textColor.getColorInt(b);
         double d = (double)n4;
         double d2 = (double)n5;
         double d3 = d;
         customFontUtil.drawString(str, d3, d2, colorInt);
         int n8 = n4 - 2;
         String text = this.textField.getText();
         String str2 = text;
         int hoveredColor = RenderUtil.getHoveredColor(
            colorInt, 0.2, n2, n3, n8, n5, (int)(customFontUtil.getStringWidth(str2) + 2.0F + (float)this.labelWidth), (int)(customFontUtil.getFontHeight() + 1.0F)
         );
         int n9 = n4 + this.labelWidth;
         int n10 = n9;
         this.textField.drawTextField(n10, n5, n2, n3, customFontUtil, hoveredColor);
         StringProperty stringProperty = this.n;
         String text2 = this.textField.getText();
         String str3 = text2;
         stringProperty.setValue(str3);
      }
   }

   public TextInputSetting(Module module, String str, String str2, DoubleValue doubleValue) {
       this(module, str, str2, false, doubleValue);
   }

   @Override
   public int getHeight() {
      return 27;
   }

   public TextInputSetting(Module module, String str, String str2, boolean flag) {
       this(module, str, str2, flag, null);
   }

   public TextInputSetting(Module module, String str, DoubleValue doubleValue) {
       this(module, str, "", false, doubleValue);
   }

   public TextInputSetting(Module module, String str, String str2) {
       this(module, str, str2, false, null);
   }

   public TextInputSetting(Module module, String str, boolean flag, DoubleValue doubleValue) {
       this(module, str, "", flag, doubleValue);
   }

   public TextInputSetting(Module module, String str) {
       this(module, str, "");
   }

   public String getValue() {
      return (String)this.n.getValue();
   }

   @Override
   public void resetToDefault() {
      this.n.resetToDefault();
      TextFieldComponent textField2 = this.textField;
      String value = this.getValue();
      String str = value;
      textField2.setText(str);
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      this.textField.keyTyped(typedChar, keyCode);
   }

   @Override
   public void renderTooltip() {
      int labelWidth2 = this.labelWidth;
      this.drawDescriptionTooltip(labelWidth2);
   }

   public void setTextFieldValue(String str) {
      this.textField.setText(str);
   }

}
