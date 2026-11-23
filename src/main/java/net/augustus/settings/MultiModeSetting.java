package net.augustus.settings;

import net.augustus.ClientManagers;
import net.augustus.font.CustomFontUtil;
import net.augustus.font.FontManager;
import net.augustus.modules.ModuleToggleListener;
import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.render.RenderUtil;
import net.augustus.settings.property.BooleanProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.augustus.modules.Module;

public class MultiModeSetting extends Setting implements ModuleToggleListener {
   private final List modeProperties;
   private int dragToggleState;
   private final ConcurrentHashMap modeWidthCache;
   private final boolean[] enabledStates;
   private int labelWidth;
   private static final int SEPARATOR_WIDTH;
   private final Module ownerModule;
   private int renderedHeight;
   private final List subModes;

   @Override
   public int getHeight() {
      return this.renderedHeight;
   }

   public MultiModeSetting(Module module, String str, BiFunction[] biFunctions) {
       super(module, null, str);
      this.modeProperties = new ArrayList();
      this.subModes = new ArrayList();
      AbstractSubMode.getControlFlowState();
      this.labelWidth = -1;
      this.modeWidthCache = new ConcurrentHashMap();
      this.dragToggleState = 0;
      this.renderedHeight = 0;
      this.ownerModule = module;
      this.enabledStates = new boolean[biFunctions.length];

      for (int i = 0; i < biFunctions.length; i++) {
         int n = i;
         String name = ((AbstractSubMode)biFunctions[i].apply(null, null)).getName();
         DoubleValue doubleValue = (DoubleValue)(new DoubleValue(module, str + name + "Group_MCSV", true)).withDisplayName(name);
         SettingVisibilityCondition condition = () -> this.isSubModeGroupVisible(n, doubleValue);
         doubleValue.visibleWhen(condition);
         AbstractSubMode subMode = (AbstractSubMode)biFunctions[i].apply(module, doubleValue);
         Supplier supplier = () -> this.enabledStates[n];
         subMode.setVisibilitySupplier(supplier);
         this.subModes.add(subMode);
         List modeProperties2 = this.modeProperties;
         BooleanProperty booleanProperty = new BooleanProperty(this, subMode.getName(), false);
         Supplier supplier2 = () -> this.enabledStates[n];
         BooleanProperty booleanProperty2 = (BooleanProperty)booleanProperty.withValueSupplier(supplier2);
         Consumer consumer = obj -> {
            this.enabledStates[n] = (Boolean)(obj);
            if (this.ownerModule.isToggled()) {
               boolean flag = (Boolean)(obj);
               subMode.setEnabled(flag);
            }
         };
         modeProperties2.add(booleanProperty2.withOnValueApplied(consumer));
      }

      module.getToggleListeners().add(this);
   }

   public MultiModeSetting(Module module, String str, DoubleValue doubleValue, BiFunction[] biFunctions) {
       super(module, doubleValue, str);
      AbstractSubMode.getControlFlowState();
      this.modeProperties = new ArrayList();
      this.subModes = new ArrayList();
      this.labelWidth = -1;
      this.modeWidthCache = new ConcurrentHashMap();
      this.dragToggleState = 0;
      this.renderedHeight = 0;
      this.ownerModule = module;
      this.enabledStates = new boolean[biFunctions.length];

      for (int i = 0; i < biFunctions.length; i++) {
         int n = i;
         String name = ((AbstractSubMode)biFunctions[i].apply(null, null)).getName();
         DoubleValue doubleValue2 = (DoubleValue)(new DoubleValue(module, str + name + "Group_MCSV", true)).withDisplayName(name);
         SettingVisibilityCondition condition = () -> this.isNestedSubModeGroupVisible(n, doubleValue2);
         doubleValue2.visibleWhen(condition);
         AbstractSubMode subMode = (AbstractSubMode)biFunctions[i].apply(module, doubleValue2);
         Supplier supplier = () -> this.enabledStates[n];
         subMode.setVisibilitySupplier(supplier);
         this.subModes.add(subMode);
         List modeProperties2 = this.modeProperties;
         BooleanProperty booleanProperty = new BooleanProperty(this, subMode.getName(), false);
         Supplier supplier2 = () -> this.enabledStates[n];
         BooleanProperty booleanProperty2 = (BooleanProperty)booleanProperty.withValueSupplier(supplier2);
         Consumer consumer = obj -> {
            this.enabledStates[n] = (Boolean)(obj);
            if (this.ownerModule.isToggled()) {
               boolean flag = (Boolean)(obj);
               subMode.setEnabled(flag);
            }
         };
         modeProperties2.add(booleanProperty2.withOnValueApplied(consumer));
      }

      module.getToggleListeners().add(this);
   }

   @Override
   public void renderTooltip() {
      int labelWidth2 = this.labelWidth;
      this.drawDescriptionTooltip(labelWidth2);
   }

   @Override
   public void render(int n, int n2, int n3, int n4, int n5, int n6, CustomFontUtil customFontUtil, boolean flag) {
      customFontUtil = FontManager.consolas30MultiMode;
      AbstractSubMode.getControlFlowState();
      super.render(n, n2, n3, n4, n5, n6, customFontUtil, flag);
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
         boolean flag2 = this.labelWidth == -1;
         boolean flag3 = flag2;
         if (this.consumeLabelDirty(flag3)) {
            this.labelWidth = (int)customFontUtil.getStringWidth(str);
            this.modeWidthCache.clear();

            for (AbstractSubMode subMode : (Iterable<AbstractSubMode>)(this.subModes)) {
               ConcurrentHashMap modeWidthCache2 = this.modeWidthCache;
               String name = subMode.getName();
               String name2 = subMode.getName();
               String str2 = name2;
               modeWidthCache2.put(name, (int)customFontUtil.getStringWidth(str2));
            }
         }

         int n7 = n3 + this.labelWidth;
         int n8 = n4;

         for (int i = 0; i < this.subModes.size(); i++) {
            AbstractSubMode subMode2 = (AbstractSubMode)this.subModes.get(i);
            String name3 = subMode2.getName();
            if (!name3.isEmpty()) {
               Integer num = (Integer)this.modeWidthCache.get(name3);
               if (num != null) {
                  if (n7 + num + (i < this.subModes.size() - 1 ? SEPARATOR_WIDTH : 0) > n5) {
                     n8 += (int)(customFontUtil.getFontHeight() + 1.0F);
                     n7 = n3 + this.labelWidth;
                  }

                  int n9 = this.enabledStates[i]
                     ? RenderUtil.getHoveredColor(colorInt2, 0.1294, n, n2, n7 - 6, n8, num + 12, (int)(customFontUtil.getFontHeight() + 1.0F))
                     : RenderUtil.getHoveredColor(colorInt, 0.2, n, n2, n7 - 6, n8, num + 12, (int)(customFontUtil.getFontHeight() + 1.0F));
                  d = (double)n7;
                  double d4 = (double)n8;
                  double d5 = d;
                  customFontUtil.drawString(name3, d5, d4, n9);
                  n7 += num;
                  if (i < this.subModes.size() - 1) {
                     d = (double)n7;
                     double d6 = (double)n8;
                     double d7 = d;
                     String str3 = ", ";
                     customFontUtil.drawString(str3, d7, d6, colorInt);
                     n7 += SEPARATOR_WIDTH;
                  }

                  if (i > 150) {
                     customFontUtil = FontManager.consolas30MultiOption;
                  }
               }
            }
         }

         this.renderedHeight = (int)((float)n8 + customFontUtil.getFontHeight() + 12.0F - (float)n4);
      }
   }

   private boolean isNestedSubModeGroupVisible(int n, DoubleValue doubleValue) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      return this.enabledStates[n] && doubleValue.hasVisibleChildren();
   }

   @Override
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      AbstractSubMode.getControlFlowState();
      boolean flag = false;
      if (mouseButton == 0 || mouseButton == 1) {
         String str = this.getDisplayName() + ": ";
         float renderX2 = (float)this.renderX;
         int n = (int)(renderX2 + this.renderFont.getStringWidth(str));
         int renderY2 = this.renderY;

         for (int i = 0; i < this.subModes.size(); i++) {
            AbstractSubMode subMode = (AbstractSubMode)this.subModes.get(i);
            String name = subMode.getName();
            if (!name.isEmpty()) {
               int stringWidth = (int)this.renderFont.getStringWidth(name);
               renderX2 = (float)(n + stringWidth);
               CustomFontUtil renderFont2 = this.renderFont;
               String str2;
               if (i < this.subModes.size() - 1) {
                  str2 = ", ";
               } else {
                  str2 = "";
               }

               String str3 = str2;
               if (renderX2 + renderFont2.getStringWidth(str3) > (float)this.boundsWidth) {
                  renderY2 += (int)this.renderFont.getFontHeight() + 1;
                  renderX2 = (float)this.renderX;
                  n = (int)(renderX2 + this.renderFont.getStringWidth(str));
               }

               if (RenderUtil.isHovered((float)(n - 6), (float)renderY2, (float)(stringWidth + 12), (float)((int)(this.renderFont.getFontHeight() + 1.0F)), mouseX, mouseY)) {
                  this.dragToggleState = this.enabledStates[i] ? 1 : 2;
                  boolean flag2 = !this.enabledStates[i];
                  this.enabledStates[i] = flag2;
                  if (this.ownerModule.isToggled()) {
                     subMode.setEnabled(flag2);
                  }

                  this.notifyValueChanged();
                  this.notifyUserChanged();
                  flag = true;
                  break;
               }

               n += (int)this.renderFont.getStringWidth(name);
               if (i < this.subModes.size() - 1) {
                  String str4 = ", ";
                  n += (int)this.renderFont.getStringWidth(str4);
               }
            }
         }
      }

      return flag;
   }

   @Override
   public Setting withDisplayName(String str) {
      return this.named(str);
   }

   @Override
   public void onModuleToggled(boolean flag) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();

      for (int i = 0; i < this.subModes.size(); i++) {
         if (this.enabledStates[i]) {
            ((AbstractSubMode)this.subModes.get(i)).setEnabled(flag);
         }
      }
   }

   public MultiModeSetting named(String str) {
      this.setDisplayName(str);
      return this;
   }

   public boolean[] getEnabledStates() {
      return this.enabledStates;
   }

   @Override
   public void resetToDefault() {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();

      for (BooleanProperty booleanProperty : (Iterable<BooleanProperty>)(this.modeProperties)) {
         booleanProperty.resetToDefault();
      }
   }

   static {
      String str = ", ";
      SEPARATOR_WIDTH = (int)FontManager.consolas30.getStringWidth(str);
   }

   private boolean isSubModeGroupVisible(int n, DoubleValue doubleValue) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      return this.enabledStates[n] && doubleValue.hasVisibleChildren();
   }

   @Override
   public Setting withVisibility(SettingVisibilityCondition condition) {
      return this.visibleWhen(condition);
   }

   public List getSubModes() {
      return this.subModes;
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      int[] controlFlowState = AbstractSubMode.getControlFlowState();
      if (this.dragToggleState != 0) {
         if (clickedMouseButton == 0 || clickedMouseButton == 1) {
            String str = this.getDisplayName() + ": ";
            float renderX2 = (float)this.renderX;
            int n = (int)(renderX2 + this.renderFont.getStringWidth(str));
            int renderY2 = this.renderY;

            for (int i = 0; i < this.subModes.size(); i++) {
               AbstractSubMode subMode = (AbstractSubMode)this.subModes.get(i);
               String name = subMode.getName();
               if (!name.isEmpty()) {
                  int stringWidth = (int)this.renderFont.getStringWidth(name);
                  renderX2 = (float)(n + stringWidth);
                  CustomFontUtil renderFont2 = this.renderFont;
                  String str2;
                  if (i < this.subModes.size() - 1) {
                     str2 = ", ";
                  } else {
                     str2 = "";
                  }

                  String str3 = str2;
                  if (renderX2 + renderFont2.getStringWidth(str3) > (float)this.boundsWidth) {
                     renderY2 += (int)this.renderFont.getFontHeight() + 1;
                     renderX2 = (float)this.renderX;
                     n = (int)(renderX2 + this.renderFont.getStringWidth(str));
                  }

                  if (RenderUtil.isHovered((float)(n - 6), (float)renderY2, (float)(stringWidth + 12), (float)((int)(this.renderFont.getFontHeight() + 1.0F)), mouseX, mouseY)
                     && this.enabledStates[i] == (this.dragToggleState == 1)) {
                     boolean flag = !this.enabledStates[i];
                     this.enabledStates[i] = flag;
                     if (this.ownerModule.isToggled()) {
                        subMode.setEnabled(flag);
                     }

                     this.notifyValueChanged();
                     this.notifyUserChanged();
                     break;
                  }

                  n += (int)this.renderFont.getStringWidth(name);
                  if (i < this.subModes.size() - 1) {
                     String str4 = ", ";
                     n += (int)this.renderFont.getStringWidth(str4);
                  }
               }
            }
         }
      }
   }

   @Override
   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.dragToggleState = 0;
   }

   public MultiModeSetting visibleWhen(SettingVisibilityCondition condition) {
      this.visibilityCondition = condition;
      return this;
   }

}
