package net.augustus.settings;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.augustus.modules.Module;
import net.augustus.font.CustomFontUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.property.BooleanProperty;
import net.augustus.font.FontManager;
import net.augustus.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;

public class BooleanValue extends Setting {
   private static final int FALSE_LABEL_WIDTH = 40;
   private int labelWidth;
   private final BooleanProperty b;
   private static final int TRUE_LABEL_WIDTH = 32;

   private BooleanValue(String str, Module module, DoubleValue doubleValue, boolean flag) {
       super(module, doubleValue, str);
      BooleanProperty booleanProperty = new BooleanProperty(this, "B", false);
      Consumer consumer = obj -> this.setValue((boolean)obj);
      this.b = (BooleanProperty)booleanProperty.withOnValueChanged(consumer);
      this.labelWidth = -1;
      BooleanProperty booleanProperty2 = this.b;
      Boolean bool = flag;
      Boolean bool2 = bool;
      booleanProperty2.setValue(bool2);
      booleanProperty2 = this.b;
      Boolean bool3 = flag;
      booleanProperty2.setDefaultValue(bool3);
   }

   @Override
   public void render(int n, int n2, int n3, int n4, int n5, int n6, CustomFontUtil customFontUtil, boolean flag) {
      customFontUtil = FontManager.consolas30BooleanValue;
      super.render(n, n2, n3, n4, n5, n6, customFontUtil, flag);
      String str = this.getDisplayName() + ": ";
      boolean flag2 = this.labelWidth == -1;
      boolean flag3 = flag2;
      if (this.consumeLabelDirty(flag3)) {
         this.labelWidth = (int)customFontUtil.getStringWidth(str);
      }

      if (flag) {
         byte b2 = 0;
         int colorInt = ClientManagers.moduleManager.clickGui.textColor.getColorInt(b2);
         double d = (double)n3;
         double d2 = (double)n4;
         double d3 = d;
         customFontUtil.drawString(str, d3, d2, colorInt);
         String str2 = String.valueOf(this.getValue());
         float f = this.getValue() ? 32.0F : 40.0F;
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 771);
         d = (double)(n3 + this.labelWidth);
         double d4 = (double)n4;
         int n7;
         if (this.getValue()) {
            byte b3 = 0;
            n7 = ClientManagers.moduleManager.clickGui.enabledColor.getColorInt(b3);
         } else {
            byte b4 = 0;
            n7 = ClientManagers.moduleManager.clickGui.disabledColor.getColorInt(b4);
         }

         int hoveredColor = RenderUtil.getHoveredColor(n7, 0.16, n, n2, n3 + this.labelWidth - 8, n4 - 4, 60, (int)(customFontUtil.getFontHeight() + 5.0F));
         double d5 = d4;
         double d6 = d;
         customFontUtil.drawString(str2, d6, d5, hoveredColor);
         this.contentWidth = (int)((float)this.labelWidth + f + 20.0F);
      }
   }

   @Override
   public void renderTooltip() {
      int labelWidth2 = this.labelWidth;
      this.drawDescriptionTooltip(labelWidth2);
   }

   public void setDefaultValue(boolean flag) {
      BooleanProperty booleanProperty = this.b;
      Boolean bool = flag;
      booleanProperty.setDefaultValue(bool);
   }

   public boolean getValue() {
      return (Boolean)this.b.getValue();
   }

   public BooleanValue(Module module, String str, boolean flag, DoubleValue doubleValue) {
       this(str, module, doubleValue, flag);
   }

   public BooleanValue(Module module, String str, boolean flag) {
       this(str, module, null, flag);
   }

   public BooleanValue(String str, boolean flag) {
       this(str, null, null, flag);
   }

   @Override
   public void resetToDefault() {
      boolean defaultValue = (Boolean)this.b.getDefaultValue();
      boolean flag = defaultValue;
      this.setValue(flag);
   }

   @Override
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (mouseButton == 0
         && RenderUtil.isHovered((float)(this.renderX + this.labelWidth - 8), (float)(this.renderY - 4), 60.0F, (float)((int)this.renderFont.getFontHeight() + 5), mouseX, mouseY)) {
         boolean flag = !this.getValue();
         this.setValue(flag);
         return true;
      } else {
         return false;
      }
   }

   public void setValue(boolean flag) {
      if (this.getValue() != flag) {
         this.notifyValueChanged();
      }

      BooleanProperty booleanProperty = this.b;
      Boolean bool = flag;
      Boolean bool2 = bool;
      booleanProperty.setValue(bool2);
   }

   @Override
   public int getHeight() {
      return 27;
   }

   @Override
   public void randomize() {
      boolean flag = ThreadLocalRandom.current().nextBoolean();
      boolean flag2 = flag;
      this.setValue(flag2);
   }

}
