package net.augustus.ui.menu;

import net.augustus.ui.sidebar.BooleanSidebarItem;
import java.util.function.Supplier;
import net.augustus.settings.BooleanValue;

public class BooleanSettingMenuItem extends BooleanSidebarItem {

   public BooleanSettingMenuItem(String str, BooleanValue booleanValue) {
       super(str, (() -> (booleanValue).getValue()), obj -> (booleanValue).setValue((boolean)obj));
       booleanValue.getClass();
       Supplier supplier = () -> booleanValue.getValue();
       booleanValue.getClass();
       BooleanValue booleanValue3 = booleanValue;
   }
}
