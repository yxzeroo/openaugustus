package net.augustus.ui.sidebar;

import net.augustus.ui.menu.ContextMenuItem;
import net.augustus.ui.widgets.ScrollBar;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CycleOptionSidebarItem implements ContextMenuItem {
   private final List options;
   private final Supplier indexGetter;
   private final Consumer indexSetter;
   private final String label;

   @Override
   public String getDisplayText() {
      return this.label + ": §6" + (String)this.options.get((Integer)this.indexGetter.get());
   }

   public CycleOptionSidebarItem(String str, List list, Supplier supplier, Consumer consumer) {
      this.label = str;
      this.options = list;
      this.indexGetter = supplier;
      this.indexSetter = consumer;
   }

   @Override
   public void onClick(String str) {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      int n = (Integer)this.indexGetter.get();
      if (++n >= this.options.size()) {
         n = 0;
      }

      this.indexSetter.accept(n);
   }
}
