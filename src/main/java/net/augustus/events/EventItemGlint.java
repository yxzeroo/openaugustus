package net.augustus.events;

import net.augustus.events.listeners.ItemGlintListener;
import de.florianmichael.dietrichevents2.CancellableEvent;
import net.minecraft.item.ItemStack;

public class EventItemGlint extends CancellableEvent {
   public static final int ID = 12;
   private int color;
   private ItemStack itemStack;
   private double speed;

   public static void reset(EventItemGlint event, ItemStack stack, int n, double d) {
      event.reset(stack, n, d);
   }

   public void call2(ItemGlintListener listener) {
      listener.onItemGlint(this);
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public void setSpeed(double d) {
      this.speed = d;
   }

   public double getSpeed() {
      return this.speed;
   }

   @Override
   public void call(Object obj) {
      this.call2((ItemGlintListener)obj);
   }

   public void setColor(int n) {
      this.color = n;
   }

   private void reset(ItemStack stack, int n, double d) {
      this.setCancelled(false);
      this.itemStack = stack;
      this.color = n;
      this.speed = d;
   }

   public int getColor() {
      return this.color;
   }
}
