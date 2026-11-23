package net.augustus.settings;

import net.augustus.ClientManagers;
import net.augustus.font.CustomFontUtil;
import net.augustus.font.FontManager;
import net.augustus.render.RenderUtil;
import net.augustus.settings.property.StringProperty;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.augustus.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketThreadUtil;

public class NamedOptionSetting extends Setting {
   private EnumSettingOption selectedOption;
   private final ConcurrentHashMap optionWidthCache;
   private static final int SEPARATOR_WIDTH;
   private int labelWidth;
   private int renderedHeight;
   private final Map optionsByName;
   private final StringProperty s;
   private EnumSettingOption[] options;

   private EnumSettingOption findOptionByName(String str) {
      return (EnumSettingOption)this.optionsByName.get(str);
   }

   private void selectOptionByName(String str) {
      EnumSettingOption enumSettingOption = this.findOptionByName(str);
      this.setSelectedOption(enumSettingOption);
   }

   public NamedOptionSetting(Module module, String str, EnumSettingOption enumSettingOption, EnumSettingOption[] enumSettingOptions) {
       this(str, module, null, enumSettingOption, enumSettingOptions);
   }

   private void updateLabelWidth() {
      boolean flag = this.labelWidth == -1;
      boolean flag2 = flag;
      if (this.consumeLabelDirty(flag2)) {
         CustomFontUtil consolas30Booleans = FontManager.consolas30Booleans;
         String str = this.getDisplayName() + ": ";
         String str2 = str;
         this.labelWidth = (int)consolas30Booleans.getStringWidth(str2);
      }
   }

   private NamedOptionSetting(String str, Module module, DoubleValue doubleValue, EnumSettingOption enumSettingOption, EnumSettingOption[] enumSettingOptions) {
       super(module, doubleValue, str);
      this.optionsByName = new HashMap();
      StringProperty stringProperty = new StringProperty(this, "S", "");
      Consumer consumer = obj -> this.selectOptionByName((String)obj);
      StringProperty stringProperty2 = (StringProperty)stringProperty.withOnValueChanged(consumer);
      Supplier supplier = () -> this.selectedOption.getDisplayName();
      this.s = (StringProperty)stringProperty2.withValueSupplier(supplier);
      this.renderedHeight = 0;
      this.labelWidth = -1;
      this.optionWidthCache = new ConcurrentHashMap();
      this.selectedOption = enumSettingOption;
      StringProperty stringProperty3 = this.s;
      String displayName2 = enumSettingOption.getDisplayName();
      String str2 = displayName2;
      stringProperty3.setValue(str2);
      stringProperty3 = this.s;
      String displayName3 = enumSettingOption.getDisplayName();
      stringProperty3.setDefaultValue(displayName3);
      this.setOptions(enumSettingOptions);
   }

   private void applyOptionsOnMainThread(EnumSettingOption[] enumSettingOptions) {
      this.applyOptions(enumSettingOptions);
   }

   @Override
   public void render(int n, int n2, int n3, int n4, int n5, int n6, CustomFontUtil customFontUtil, boolean flag) {

      try {
         customFontUtil = FontManager.consolas30Booleans;
         super.render(n, n2, n3, n4, n5, n6, customFontUtil, flag);
         this.updateLabelWidth();
         if (flag) {
            String str = this.getDisplayName() + ": ";
            byte b = 0;
            int colorInt = ClientManagers.moduleManager.clickGui.textColor.getColorInt(b);
            byte b2 = 0;
            int colorInt2 = ClientManagers.moduleManager.clickGui.hoverTextColor.getColorInt(b2);
            double d = (double)n3;
            double d2 = (double)n4;
            double d3 = d;
            customFontUtil.drawString(str, d3, d2, colorInt);
            int n7 = n3 + this.labelWidth;
            int n8 = n4;
            int n9 = 0;

            for (EnumSettingOption enumSettingOption : this.getOptions()) {
               String displayName2 = enumSettingOption.getDisplayName();

               try {
                  Integer num = (Integer)this.optionWidthCache.get(displayName2);
                  if (num != null) {
                     if (n7 + num + (n9 < this.getOptions().length - 1 ? SEPARATOR_WIDTH : 0) > n5) {
                        n8 += (int)(customFontUtil.getFontHeight() + 1.0F);
                        n7 = n3 + this.labelWidth;
                     }

                     boolean flag2 = enumSettingOption.equals(this.getSelectedOption());
                     int n10 = flag2
                        ? RenderUtil.getHoveredColor(colorInt2, 0.1294, n, n2, n7 - 6, n8, num + 12, (int)(customFontUtil.getFontHeight() + 1.0F))
                        : RenderUtil.getHoveredColor(colorInt, 0.2, n, n2, n7 - 6, n8, num + 12, (int)(customFontUtil.getFontHeight() + 1.0F));
                     boolean flag3 = n9 < this.getOptions().length - 1;
                     d = (double)n7;
                     double d4 = (double)n8;
                     double d5 = d;
                     customFontUtil.drawString(displayName2, d5, d4, n10);
                     n7 += num;
                     if (flag3) {
                        d = (double)n7;
                        double d6 = (double)n8;
                        double d7 = d;
                        String str2 = ", ";
                        customFontUtil.drawString(str2, d7, d6, colorInt);
                        n7 += SEPARATOR_WIDTH;
                     }

                     if (n9 > 150) {
                        customFontUtil = FontManager.consolas30Default;
                     }

                     n9++;
                  }
               } catch (Exception e) {
                  String str3 = this.getName() + " " + this.getParentModuleName() + " " + Arrays.toString((Object[])this.getOptions()) + " " + customFontUtil;
                  throw new RuntimeException(str3, e);
               }
            }

            this.renderedHeight = (int)((float)n8 + customFontUtil.getFontHeight() + 12.0F - (float)n4);
            this.contentWidth = 150;
         }
      } catch (Exception e2) {
         String str4 = this.getName() + " " + this.getParentModuleName() + " " + Arrays.toString((Object[])this.getOptions()) + " " + customFontUtil;
         throw new RuntimeException(str4, e2);
      }
   }

   public int getSelectedIndex(long l) {
      return Arrays.asList(this.options).indexOf(this.getSelectedOption());
   }

   @Override
   public void renderTooltip() {
      int labelWidth2 = this.labelWidth;
      this.drawDescriptionTooltip(labelWidth2);
   }

   public EnumSettingOption[] getOptions() {
      return this.options;
   }

   @Override
   public void resetToDefault() {
      String defaultValue = (String)this.s.getDefaultValue();
      EnumSettingOption enumSettingOption = this.findOptionByName(defaultValue);
      this.setSelectedOption(enumSettingOption);
   }

   @Override
   public void randomize() {
      if (!this.getDisplayName().contains("Point selection mode")) {
         EnumSettingOption enumSettingOption = this.options[ThreadLocalRandom.current().nextInt(this.options.length)];
         this.setSelectedOption(enumSettingOption);
      }
   }

   @Override
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      boolean flag = false;
      if (mouseButton == 0 || mouseButton == 1) {
         int n = this.renderX + this.labelWidth;
         int renderY2 = this.renderY;
         int n2 = 0;

         for (EnumSettingOption enumSettingOption : this.getOptions()) {
            String displayName2 = enumSettingOption.getDisplayName();
            Integer num = (Integer)this.optionWidthCache.get(displayName2);
            if (num != null) {
               if (n + num + (n2 < this.getOptions().length - 1 ? SEPARATOR_WIDTH : 0) > this.boundsWidth) {
                  renderY2 += (int)(this.renderFont.getFontHeight() + 1.0F);
                  n = this.renderX + this.labelWidth;
               }

               if (RenderUtil.isHovered((float)(n - 6), (float)renderY2, (float)(num + 12), (float)((int)(this.renderFont.getFontHeight() + 1.0F)), mouseX, mouseY)) {
                  if (!enumSettingOption.equals(this.selectedOption)) {
                     this.notifyUserChanged();
                  }

                  this.setSelectedOption(enumSettingOption);
                  flag = true;
                  break;
               }

               n += num;
               if (n2 < this.getOptions().length - 1) {
                  n += SEPARATOR_WIDTH;
               }

               n2++;
            }
         }
      }

      return flag;
   }

   @Override
   public int getHeight() {
      return this.renderedHeight;
   }

   public EnumSettingOption getSelectedOption() {
      return this.selectedOption;
   }

   public NamedOptionSetting(Module module, String str, EnumSettingOption enumSettingOption, EnumSettingOption[] enumSettingOptions, DoubleValue doubleValue) {
       this(str, module, doubleValue, enumSettingOption, enumSettingOptions);

      for (EnumSettingOption enumSettingOption2 : enumSettingOptions) {
         this.optionsByName.put(enumSettingOption2.getDisplayName(), enumSettingOption2);
      }
   }

   private void applyOptions(EnumSettingOption[] enumSettingOptions) {
      this.options = enumSettingOptions;
      this.optionWidthCache.clear();

      for (EnumSettingOption enumSettingOption : enumSettingOptions) {
         if (enumSettingOption != null) {
            String displayName2 = enumSettingOption.getDisplayName();
            if (displayName2 != null) {
               this.optionWidthCache.put(displayName2, (int)FontManager.consolas30Booleans.getStringWidth(displayName2));
            }
         }
      }
   }

   static {
      String str = ", ";
      SEPARATOR_WIDTH = (int)FontManager.consolas30Booleans.getStringWidth(str);
   }

   public void setSelectedOption(EnumSettingOption enumSettingOption) {
      if (enumSettingOption != null) {
         if (!enumSettingOption.equals(this.getSelectedOption())) {
            this.notifyValueChanged();
         }

         this.selectedOption = enumSettingOption;
         StringProperty stringProperty = this.s;
         String displayName2 = enumSettingOption.getDisplayName();
         String str = displayName2;
         stringProperty.setValue(str);
      }
   }

   public void setOptions(EnumSettingOption[] enumSettingOptions) {
      if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
         this.applyOptions(enumSettingOptions);
      } else {
         PacketThreadUtil.runOnMainThreadImmediate(() -> this.applyOptionsOnMainThread(enumSettingOptions));
      }
   }

}
