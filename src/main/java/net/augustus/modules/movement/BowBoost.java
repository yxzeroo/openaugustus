package net.augustus.modules.movement;

import java.awt.Color;
import java.util.OptionalInt;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.Setting;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.player.ContainerSlotQuery;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;

public class BowBoost extends Module implements TickListener, SilentSlotListener, ClickListener {

   public BowBoost() {
       super("BowBoost", "BogenBeschleunigung", new Color(200, 217, 227), Categorys.MOVEMENT);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
   }

   public void onTick() {
   }

   public void onSilentSlot() {
   }

   public void onClick(EventClick event) {
   }

   private int findBowSlot() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      byte b = 36;
      containerSlotQuery = containerSlotQuery.fromSlot(b);
      ItemBow bow = Items.bow;
      OptionalInt optionalInt = containerSlotQuery.filterItemExact((Item)bow).findFirstSlotIndex();
      return optionalInt.isPresent() ? optionalInt.getAsInt() : -1;
   }

   public boolean hasArrows() {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      Item arrow = Items.arrow;
      return containerSlotQuery.hasAnyOfItem(arrow);
   }

}
