package net.augustus.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.utils.TuplePair;
import net.augustus.events.EventAttackEntity;
import net.augustus.utils.player.InventoryUtil;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.settings.BooleanValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class GommeMode extends Module implements AttackEntityListener, SilentSlotListener {
   private final BooleanValue silentSlotSwitching;
   private Entity lastAttackedEntity;

   public GommeMode() {
       super("GommeMode", "GommeModus", new Color(167, 85, 250), Categorys.COMBAT);
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.lastAttackedEntity = null;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
   }

   public void onAttackEntity(EventAttackEntity event) {
      this.lastAttackedEntity = event.getTarget();
   }

   public void onSilentSlot() {
      if (this.lastAttackedEntity != null && this.lastAttackedEntity instanceof EntityLivingBase) {
         EntityLivingBase lastAttackedEntity2 = (EntityLivingBase)this.lastAttackedEntity;
         if (lastAttackedEntity2.hurtTime < 6) {
            this.lastAttackedEntity = null;
         } else {
            ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
            if ((currentEquippedItem == null || currentEquippedItem.getItem() instanceof ItemSword || !mc.thePlayer.isUsingItem()) && !mc.playerController.isHittingBlock) {
               byte b = 36;
               ArrayList sortedWeapons = InventoryUtil.getSortedWeapons(mc.thePlayer.inventoryContainer, b);
               if (!sortedWeapons.isEmpty()) {
                  TuplePair tuplePair = (TuplePair)sortedWeapons.get(0);
                  if ((Integer)tuplePair.getSecond() - 36 != mc.thePlayer.inventory.currentItem) {
                     int n = (Integer)tuplePair.getSecond() - 36;
                     boolean value = this.silentSlotSwitching.getValue();
                     int n2 = n;
                     byte b2 = 8;
                     SilentSlotManager.requestSlot(this, b2, n2, value);
                  }
               }
            }
         }
      }
   }

}
