package net.augustus.modules.misc;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.options.PanicCategoryOption;
import net.augustus.ClientManagers;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.events.listeners.TickListener;

public class Panic extends Module implements TickListener {
   public final MultiOptionSetting categoriesToDisable;
   public boolean pendingDisable;

   public Panic() {
       super("Panic", "Panik", new Color(145, 34, 224), Categorys.MISC);
      this.categoriesToDisable = (MultiOptionSetting)(new MultiOptionSetting(this, "CategoriesToDisable", PanicCategoryOption.values())).markVisualOnly();
      this.pendingDisable = false;
   }

   public void onEnable() {
      this.pendingDisable = true;
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   public void onTick5() {
      if (this.pendingDisable) {
         for (Module module : (Iterable<Module>)(ClientManagers.moduleManager.getModules())) {
            if (module.isToggled()) {
               for (PanicCategoryOption panicCategoryOption : PanicCategoryOption.values()) {
                  if (PanicCategoryOption.getCategory(panicCategoryOption).equals(module.getCategorys()) && PanicCategoryOption.isOptionSelected(panicCategoryOption)) {
                     boolean flag = false;
                     boolean flag2 = false;
                     module.setToggled(flag2, flag);
                  }
               }
            }
         }

         ClientManagers.moduleManager.sortModules();
      }

      boolean flag3 = false;
      this.setToggled(flag3);
      this.pendingDisable = false;
   }

}
