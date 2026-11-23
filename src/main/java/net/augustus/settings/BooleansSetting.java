package net.augustus.settings;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.augustus.modules.Module;
import net.augustus.font.CustomFontUtil;
import net.augustus.ClientManagers;
import net.augustus.font.FontManager;
import net.augustus.settings.property.StringProperty;
import net.augustus.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketThreadUtil;

public class BooleansSetting extends Setting {
   private final ConcurrentHashMap optionWidthCache;
   private int labelWidth;
   private final StringProperty s;
   private static final int SEPARATOR_WIDTH;
   private int renderedHeight;
   private String[] options;

   public String getSelectedValue() {
      return (String)this.s.getValue();
   }

   @Override
   public int getHeight() {
      return this.renderedHeight;
   }

   public BooleansSetting(Module module, String str, String str2, String[] strings, DoubleValue doubleValue) {
       this(str, module, doubleValue, str2, strings);
   }

   public void setOptions(String[] strings) {
      if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
         this.applyOptions(strings);
      } else {
         PacketThreadUtil.runOnMainThreadImmediate(() -> {
            this.applyOptions(strings);
         });
      }
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

   public BooleansSetting(Module module, String str, String str2, String[] strings) {
       this(str, module, null, str2, strings);
   }

   @Override
   public void resetToDefault() {
      String defaultValue = (String)this.s.getDefaultValue();
      this.setSelectedValue(defaultValue);
   }

   public void setSelectedValue(String str) {
      if (!str.equals(this.getSelectedValue())) {
         this.notifyValueChanged();
      }

      this.s.setValue(str);
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

            for (String str2 : this.getOptions()) {
               try {
                  Integer num = (Integer)this.optionWidthCache.get(str2);
                  if (num != null) {
                     if (n7 + num + (n9 < this.getOptions().length - 1 ? SEPARATOR_WIDTH : 0) > n5) {
                        n8 += (int)(customFontUtil.getFontHeight() + 1.0F);
                        n7 = n3 + this.labelWidth;
                     }

                     boolean flag2 = str2.equals(this.getSelectedValue());
                     int n10 = flag2
                        ? RenderUtil.getHoveredColor(colorInt2, 0.1294, n, n2, n7 - 6, n8, num + 12, (int)(customFontUtil.getFontHeight() + 1.0F))
                        : RenderUtil.getHoveredColor(colorInt, 0.2, n, n2, n7 - 6, n8, num + 12, (int)(customFontUtil.getFontHeight() + 1.0F));
                     boolean flag3 = n9 < this.getOptions().length - 1;
                     d = (double)n7;
                     double d4 = (double)n8;
                     double d5 = d;
                     customFontUtil.drawString(str2, d5, d4, n10);
                     n7 += num;
                     if (flag3) {
                        d = (double)n7;
                        double d6 = (double)n8;
                        double d7 = d;
                        String str3 = ", ";
                        customFontUtil.drawString(str3, d7, d6, colorInt);
                        n7 += SEPARATOR_WIDTH;
                     }

                     if (n9 > 150) {
                        customFontUtil = FontManager.consolas30Default;
                     }

                     n9++;
                  }
               } catch (Exception e) {
                  String str4 = this.getName() + " " + this.getParentModuleName() + " " + Arrays.toString((Object[])this.getOptions()) + " " + customFontUtil;
                  throw new RuntimeException(str4, e);
               }
            }

            this.renderedHeight = (int)((float)n8 + customFontUtil.getFontHeight() + 12.0F - (float)n4);
            this.contentWidth = 150;
         }
      } catch (Exception e2) {
         String str5 = this.getName() + " " + this.getParentModuleName() + " " + Arrays.toString((Object[])this.getOptions()) + " " + customFontUtil;
         throw new RuntimeException(str5, e2);
      }
   }

   @Override
   public void randomize() {
      if (!this.getDisplayName().contains("Point selection mode")) {
         String str = this.options[ThreadLocalRandom.current().nextInt(this.options.length)];
         this.setSelectedValue(str);
      }
   }

   private BooleansSetting(String str, Module module, DoubleValue doubleValue, String str2, String[] strings) {
       super(module, doubleValue, str);
      StringProperty stringProperty = new StringProperty(this, "S", "");
      Consumer consumer = obj -> this.setSelectedValue((String)obj);
      this.s = (StringProperty)stringProperty.withOnValueChanged(consumer);
      this.renderedHeight = 0;
      this.labelWidth = -1;
      this.optionWidthCache = new ConcurrentHashMap();
      this.s.setValue(str2);
      this.s.setDefaultValue(str2);
      this.setOptions(strings);
   }

   static {
      String str = ", ";
      SEPARATOR_WIDTH = (int)FontManager.consolas30Booleans.getStringWidth(str);
   }

   public int getSelectedIndex() {
      return Arrays.asList(this.options).indexOf(this.getSelectedValue());
   }

   public String[] getOptions() {
      return this.options;
   }

   @Override
   public void renderTooltip() {
      int labelWidth2 = this.labelWidth;
      this.drawDescriptionTooltip(labelWidth2);
   }

   private void applyOptions(String[] strings) {
      this.options = strings;
      this.optionWidthCache.clear();

      for (String str : strings) {
         if (str != null) {
            this.optionWidthCache.put(str, (int)FontManager.consolas30Booleans.getStringWidth(str));
         }
      }
   }

   @Override
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      boolean flag = false;
      if (mouseButton == 0 || mouseButton == 1) {
         int n = this.renderX + this.labelWidth;
         int renderY2 = this.renderY;
         int n2 = 0;

         for (String str : this.getOptions()) {
            Integer num = (Integer)this.optionWidthCache.get(str);
            if (num != null) {
               if (n + num + (n2 < this.getOptions().length - 1 ? SEPARATOR_WIDTH : 0) > this.boundsWidth) {
                  renderY2 += (int)(this.renderFont.getFontHeight() + 1.0F);
                  n = this.renderX + this.labelWidth;
               }

               if (RenderUtil.isHovered((float)(n - 6), (float)renderY2, (float)(num + 12), (float)((int)(this.renderFont.getFontHeight() + 1.0F)), mouseX, mouseY)) {
                  this.notifyUserChanged();
                  this.setSelectedValue(str);
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

}
