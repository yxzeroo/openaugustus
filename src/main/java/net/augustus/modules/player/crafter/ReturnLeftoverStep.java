package net.augustus.modules.player.crafter;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.Item;

public class ReturnLeftoverStep implements CraftingStep {
   public boolean execute(CraftingInventoryActions craftingInventoryActions, GuiCrafting guiCrafting) {
      if (!craftingInventoryActions.hasHeldCursorStack()) {
         return true;
      } else {
         byte b = 0;
         Object obj = null;
         int n = craftingInventoryActions.findInventorySlot(guiCrafting, (Item)obj, b);
         return n < 0 ? false : craftingInventoryActions.pickupFromSlot(guiCrafting, n);
      }
   }
}
