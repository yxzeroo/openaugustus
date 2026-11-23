package net.augustus.settings.options;

import net.augustus.Augustus;
import net.augustus.settings.SelectableOption;
import java.util.function.Supplier;
import net.augustus.modules.render.ClickGui;

public enum CustomGlintFakeEnchantOption implements SelectableOption {
   SWORDS("Swords", () -> isSwordsVisible(0L)),
   TOOLS("Tools", () -> isToolsVisible(0L)),
   ARMOR("Armor", () -> isArmorVisible(0L)),
   REST("Rest", () -> isRestVisible(0, 0, 0));

   private final Supplier visibilitySupplier;
   private final String name;
   private boolean selected;

   private CustomGlintFakeEnchantOption(String str, Supplier supplier) {
      this.name = str;
      this.visibilitySupplier = supplier;
   }

   public String getName() {
      return this.name;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean flag) {
      this.selected = flag;
   }

   public boolean isVisible() {
      return !Augustus.getInstance().getModuleManager().customGlint.remove.getValue() || (Boolean)this.visibilitySupplier.get();
   }

   private static Boolean isRestVisible(int n, int n2, int n3) {
      return !CustomGlintRemoveItemOption.REST.isSelected();
   }

   private static Boolean isArmorVisible(long l) {
      return !CustomGlintRemoveItemOption.ARMOR.isSelected();
   }

   private static Boolean isToolsVisible(long l) {
      return !CustomGlintRemoveItemOption.TOOLS.isSelected();
   }

   private static Boolean isSwordsVisible(long l) {
      return !CustomGlintRemoveItemOption.SWORDS.isSelected();
   }


}
