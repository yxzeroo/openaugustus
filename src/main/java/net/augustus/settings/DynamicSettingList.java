package net.augustus.settings;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.font.CustomFontUtil;
import net.augustus.font.FontManager;
import net.augustus.render.RenderUtil;
import net.augustus.settings.property.IndexedSettingFactory;
import net.augustus.settings.property.SettingGroupListProperty;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.augustus.modules.Module;
import net.augustus.modules.misc.Dummy;

public class DynamicSettingList extends Setting {
   public int hoveredRemoveIndex;
   public static final Color REMOVE_HOVER_COLOR = new Color(200, 100, 100);
   public static final Color REMOVE_COLOR = new Color(180, 80, 80);
   public boolean addButtonHovered;
   public int labelWidth;
   public final int maxEntries;
   public static final Color ADD_COLOR = new Color(80, 180, 80);
   public final IndexedSettingFactory entryFactory;
   public final SettingGroupListProperty entriesProperty;
   private int cycleIndex;
   public static final Color ADD_HOVER_COLOR = new Color(100, 200, 100);
   public final int minEntries;
   public int renderedHeight;
   public int nextEntryId;
   public static final CustomFontUtil BUTTON_FONT = FontManager.consolas40;

   public int getEntryCount() {
      return ((List)this.entriesProperty.getValue()).size();
   }

   public Setting nextEntry() {
      this.cycleIndex++;
      if (this.cycleIndex >= this.getEntryCount()) {
         this.cycleIndex = 0;
      }

      int cycleIndex2 = this.cycleIndex;
      return this.getEntry(cycleIndex2);
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

      for (Setting setting : (Iterable<Setting>)((List)this.entriesProperty.getValue())) {
         if (setting.isVisible()) {
            setting.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
         }
      }
   }

   public boolean canAddEntry() {
      return this.getEntryCount() < this.maxEntries;
   }

   public DynamicSettingList withInitialCount(int n) {
      int n2 = Math.max(this.minEntries, Math.min(this.maxEntries, n));
      List value = (List)this.entriesProperty.getValue();

      for (int i = 0; i < n2; i++) {
         int nextEntryId2 = this.nextEntryId;
         Setting setting = this.entryFactory.createSetting(nextEntryId2);
         value.add(setting);
         this.updateEntryIndexLabel(setting, i);
         this.nextEntryId++;
      }

      this.entriesProperty.setDefaultValue(value);
      return this;
   }

   @Override
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (mouseButton == 0 && this.addButtonHovered && this.canAddEntry()) {
         this.addEntry();
         return true;
      } else if (mouseButton == 0 && this.hoveredRemoveIndex >= 0 && this.canRemoveEntry()) {
         int hoveredRemoveIndex2 = this.hoveredRemoveIndex;
         this.removeEntry(hoveredRemoveIndex2);
         return true;
      } else {
         int n = (int)(this.renderFont.getFontHeight() + 5.0F);
         List value = (List)this.entriesProperty.getValue();

         for (int i = 0; i < value.size(); i++) {
            Setting setting = (Setting)value.get(i);
            if (setting.isVisible()) {
               if (setting.mouseClicked(mouseX, mouseY, mouseButton)) {
                  return true;
               }

               n += setting.getHeight();
            }
         }

         return false;
      }
   }

   public DynamicSettingList(Module module, String str, IndexedSettingFactory indexedSettingFactory, int n, int n2) {
       super(module, null, str);
      this.renderedHeight = 0;
      this.labelWidth = -1;
      this.hoveredRemoveIndex = -1;
      this.addButtonHovered = false;
      this.nextEntryId = 0;
      this.cycleIndex = 0;
      this.entryFactory = n3 -> {
         Setting setting = indexedSettingFactory.createSetting(n3);
         if (!setting.getName().contains("_")) {
            Augustus.getInstance().getClass();
         }

         ClientManagers.settingManager.getSettings().removeIf(obj -> obj == setting);
         if (module != null && !(module instanceof Dummy)) {
            module.getSettings().removeIf(obj2 -> obj2 == setting);
         }

         if (setting.getParentGroup() != null) {
            setting.getParentGroup().getChildren().removeIf(obj3 -> obj3 == setting);
         }

         if (setting instanceof DoubleValue) {
            DoubleValue doubleValue = (DoubleValue)setting;
            this.unregisterGroupChildren(doubleValue);
         }

         if (module != null && module.getProperties() != null) {
            List properties2 = module.getProperties();
            properties2.removeIf(obj4 -> obj4 == setting);
         }

         return setting;
      };
      this.minEntries = n;
      this.maxEntries = n2;
      this.entriesProperty = new SettingGroupListProperty(this, "L", new ArrayList(), this.entryFactory);
   }

   @Override
   public void resetToDefault() {
      this.entriesProperty.resetToDefault();
   }

   public boolean canRemoveEntry() {
      return this.getEntryCount() > this.minEntries;
   }

   @Override
   public void updateVisibility() {
      super.updateVisibility();

      for (Setting setting : (Iterable<Setting>)((List)this.entriesProperty.getValue())) {
         setting.updateVisibility();
      }
   }

   private void unregisterGroupChildren(DoubleValue doubleValue) {

      for (Setting setting : (Iterable<Setting>)(doubleValue.getChildren())) {
         ClientManagers.settingManager.getSettings().removeIf(obj -> obj == setting);
         if (this.getParentModule() != null && this.getParentModule().getProperties() != null) {
            this.getParentModule().getProperties().removeIf(obj2 -> obj2 == setting);
         }

         if (setting instanceof DoubleValue) {
            DoubleValue doubleValue2 = (DoubleValue)setting;
            this.unregisterGroupChildren(doubleValue2);
         }
      }
   }

   public void updateEntryIndexLabel(Setting setting, int n) {
      String displayName2 = setting.getDisplayName();
      if (displayName2 != null) {
         int n2 = displayName2.indexOf(95);
         String str;
         if (n2 <= 0) {
            str = displayName2.replaceAll("\\s*\\d+$", "").trim();
         } else {
            str = displayName2.substring(0, n2);
         }

         String str2 = str + " " + n;
         setting.updateDisplayName(str2);
      }

      if (setting instanceof DoubleValue) {
         for (Setting setting2 : (Iterable<Setting>)(((DoubleValue)setting).getChildren())) {
            this.updateEntryIndexLabel(setting2, n);
         }
      }
   }

   @Override
   public int getHeight() {
      return this.renderedHeight;
   }

   public SettingGroupListProperty getEntriesProperty() {
      return this.entriesProperty;
   }

   @Override
   public void render(int n, int n2, int n3, int n4, int n5, int n6, CustomFontUtil customFontUtil, boolean flag) {
      super.render(n, n2, n3, n4, n5, n6, customFontUtil, flag);
      String str = this.getDisplayName() + ":";
      boolean flag2 = this.labelWidth == -1;
      int n7;
      boolean flag3 = flag2;
      if (this.consumeLabelDirty(flag3)) {
         this.labelWidth = (int)customFontUtil.getStringWidth(str);
      }

      if (flag) {
         byte b = 0;
         int colorInt = ClientManagers.moduleManager.clickGui.textColor.getColorInt(b);
         double d = (double)n3;
         double d2 = (double)n4;
         double d3 = d;
         customFontUtil.drawString(str, d3, d2, colorInt);
         this.addButtonHovered = false;
         if (this.canAddEntry()) {
            int n8 = n3 + this.labelWidth + 10;
            String str2 = "+";
            float stringWidth = BUTTON_FONT.getStringWidth(str2);
            byte b2 = 8;
            boolean isHovered = RenderUtil.isHovered(
               (float)(n8 - b2),
               (float)(n4 - 3 - b2),
               (float)((int)stringWidth + b2 * 2),
               (float)((int)(BUTTON_FONT.getFontHeight() + (float)(b2 * 2))),
               n,
               n2
            );
            this.addButtonHovered = isHovered;
            Color color = isHovered ? ADD_HOVER_COLOR : ADD_COLOR;
            CustomFontUtil customFontUtil2 = BUTTON_FONT;
            d = (double)n8;
            double d4 = (double)(n4 - 3);
            int rgb = color.getRGB();
            double d5 = d4;
            double d6 = d;
            customFontUtil2.drawString(str2, d6, d5, rgb);
         }

         this.renderedHeight = (int)(customFontUtil.getFontHeight() + 5.0F);
         this.hoveredRemoveIndex = -1;
         List value = (List)this.entriesProperty.getValue();

         for (int i = 0; i < value.size(); i++) {
            Setting setting = (Setting)value.get(i);
            if (setting.isVisible()) {
               int n9 = n4 + this.renderedHeight;
               n7 = n3 + 20;
               int n10 = n5 - 40;
               int n11 = n10;
               int n12 = n7;
               setting.render(n, n2, n12, n9, n11, n6, customFontUtil, flag);
               String str3 = "-";
               float stringWidth2 = BUTTON_FONT.getStringWidth(str3);
               byte b3 = 8;
               if (this.canRemoveEntry()) {
                  boolean isHovered2 = RenderUtil.isHovered(
                     (float)(n3 - b3),
                     (float)(n9 - 3 - b3),
                     (float)((int)stringWidth2 + b3 * 2),
                     (float)((int)(BUTTON_FONT.getFontHeight() + (float)(b3 * 2))),
                     n,
                     n2
                  );
                  if (isHovered2) {
                     this.hoveredRemoveIndex = i;
                  }

                  Color color2 = isHovered2 ? REMOVE_HOVER_COLOR : REMOVE_COLOR;
                  CustomFontUtil customFontUtil3 = BUTTON_FONT;
                  d = (double)n3;
                  double d7 = (double)(n9 - 3);
                  int rgb2 = color2.getRGB();
                  int n13 = rgb2;
                  double d8 = d7;
                  double d9 = d;
                  customFontUtil3.drawString(str3, d9, d8, n13);
               } else {
                  d = (double)n3;
                  double d10 = (double)(n9 - 3);
                  double d11 = d10;
                  double d12 = d;
                  BUTTON_FONT.drawString(str3, d12, d11, colorInt);
               }

               this.renderedHeight = this.renderedHeight + setting.getHeight();
            }
         }

         for (Setting setting2 : (Iterable<Setting>)(value)) {
            if (setting2.isVisible()) {
               setting2.renderTooltip();
            }
         }
      }
   }

   public List getEntries() {
      return (List)this.entriesProperty.getValue();
   }

   public void removeEntry(int n) {
      if (this.canRemoveEntry() && n >= 0 && n < this.getEntryCount()) {
         List value = (List)this.entriesProperty.getValue();
         value.remove(n);
         this.refreshEntryLabels();
      }
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);

      for (Setting setting : (Iterable<Setting>)((List)this.entriesProperty.getValue())) {
         if (setting.isVisible()) {
            setting.keyTyped(typedChar, keyCode);
         }
      }
   }

   public void reindexEntryLabels() {
      List value = (List)this.entriesProperty.getValue();

      for (int i = 0; i < value.size(); i++) {
         Setting setting = (Setting)value.get(i);
         if (setting != null) {
            this.updateEntryIndexLabel(setting, i);
         }
      }
   }

   @Override
   public void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);

      for (Setting setting : (Iterable<Setting>)((List)this.entriesProperty.getValue())) {
         if (setting.isVisible()) {
            setting.mouseReleased(mouseX, mouseY, state);
         }
      }
   }

   public Setting getEntry(int n) {
      List value = (List)this.entriesProperty.getValue();
      return n >= 0 && n < value.size() ? (Setting)value.get(n) : null;
   }

   private void addEntry() {
      if (this.canAddEntry()) {
         List value = (List)this.entriesProperty.getValue();
         int nextEntryId2 = this.nextEntryId;
         Setting setting = this.entryFactory.createSetting(nextEntryId2);
         value.add(setting);
         int n = value.size() - 1;
         this.updateEntryIndexLabel(setting, n);
         this.nextEntryId++;
      }
   }

   private void refreshEntryLabels() {
      this.reindexEntryLabels();
   }

   public void setNextEntryId(int n) {
      this.nextEntryId = n;
   }

   @Override
   public void renderTooltip() {
      int labelWidth2 = this.labelWidth;
      this.drawDescriptionTooltip(labelWidth2);
   }

}
