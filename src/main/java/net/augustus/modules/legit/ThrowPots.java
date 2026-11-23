package net.augustus.modules.legit;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.EventKey;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.KeybindSetting;
import net.augustus.events.listeners.KeyListener;
import net.augustus.utils.TuplePair;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.settings.BooleanValue;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class ThrowPots extends Module implements SilentSlotListener, ClickListener, KeyListener {
   private final RandomizedRangeSettings delayBetweenSwitchAndClick;
   private final RandomizedRangeSettings delayToSwitchToOriginalSlot;
   private final BooleanValue silentSlotSwitching;
   private final ArrayList queuedPotions;
   private final ArrayList potionKeybinds;
   private int ticksSinceSlotSwitch;
   private int ticksSinceThrow;
   private boolean potionThrown;

   public ThrowPots() {
       super("ThrowPots", "SchmeißeTränke", new Color(70, 139, 166), Categorys.LEGIT);
      this.delayBetweenSwitchAndClick = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, "DelayBetweenSwitchAndClick", 1.0, 2.0, 0.0, 5.0, 0)).withoutNotInARow().markAllVisualOnly();
      this.delayToSwitchToOriginalSlot = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, "DelayToSwitchToOriginalSlot", 1.0, 2.0, 0.0, 5.0, 0)).withoutNotInARow().markAllVisualOnly();
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.queuedPotions = new ArrayList();
      this.potionKeybinds = new ArrayList();
      this.ticksSinceSlotSwitch = 0;
      this.ticksSinceThrow = 0;
      this.potionThrown = false;
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "RegenerationPotionKey")).markVisualOnly(), 10));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "SpeedPotionKey")).markVisualOnly(), 1));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "PoisonPotionKey")).markVisualOnly(), 19));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "FireResistancePotionKey")).markVisualOnly(), 12));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "InstantHealthPotionKey")).markVisualOnly(), 6));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "NightVisionPotionKey")).markVisualOnly(), 16));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "WeaknessPotionKey")).markVisualOnly(), 18));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "StrengthPotionKey")).markVisualOnly(), 5));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "SlownessPotionKey")).markVisualOnly(), 2));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "JumpBoostPotionKey")).markVisualOnly(), 8));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "InstantDamagePotionKey")).markVisualOnly(), 7));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "WaterBreathingPotionKey")).markVisualOnly(), 13));
      this.potionKeybinds.add(new TuplePair((new KeybindSetting(this, "InvisibilityPotionKey")).markVisualOnly(), 14));
   }

   public void onEnable() {
      this.resetThrowState();
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 34;
      this.toggleEventSubscription(b2);
      byte b3 = 14;
      this.toggleEventSubscription(b3);
   }

   public void onClick(EventClick event) {
      this.ticksSinceSlotSwitch++;
      this.ticksSinceThrow++;
      if (!this.queuedPotions.isEmpty() && this.canThrowPotion()) {
         TuplePair tuplePair = (TuplePair)this.queuedPotions.get(0);
         if (this.ticksSinceSlotSwitch > this.delayBetweenSwitchAndClick.getLastIntValue() && mc.thePlayer.inventory.currentItem == (Integer)tuplePair.getSecond() && !this.potionThrown) {
            byte b = 1;
            event.addRightClicks(b);
            this.potionThrown = true;
            this.ticksSinceThrow = 0;
            this.delayBetweenSwitchAndClick.nextValue();
            this.delayToSwitchToOriginalSlot.nextValue();
         }

         if (this.potionThrown && this.ticksSinceThrow >= this.delayToSwitchToOriginalSlot.getLastIntValue()) {
            this.queuedPotions.remove(0);
            this.resetThrowState();
         }
      } else {
         this.queuedPotions.clear();
         this.resetThrowState();
      }
   }

   public void onSilentSlot() {
      if (!this.queuedPotions.isEmpty() && this.canThrowPotion()) {
         TuplePair tuplePair = (TuplePair)this.queuedPotions.get(0);
         if (mc.thePlayer.inventory.currentItem != (Integer)tuplePair.getSecond()) {
            this.resetThrowState();
         }

         int second = (Integer)tuplePair.getSecond();
         boolean value = this.silentSlotSwitching.getValue();
         int n = second;
         byte b = 0;
         SilentSlotManager.requestSlot(this, b, n, value);
      }
   }

   public void onKey2(EventKey event) {
      int keyCode = event.getKeyCode();

      for (TuplePair tuplePair : (Iterable<TuplePair>)(this.potionKeybinds)) {
         if (((KeybindSetting)tuplePair.getFirst()).getKeyCode() == keyCode) {
            int second = (Integer)tuplePair.getSecond();
            TuplePair tuplePair2 = this.findSplashPotionInHotbar(second);
            if (tuplePair2 != null) {
               this.queuedPotions.add(tuplePair2);
            }

            return;
         }
      }
   }

   private void resetThrowState() {
      this.ticksSinceThrow = 0;
      this.ticksSinceSlotSwitch = 0;
      this.potionThrown = false;
   }

   private boolean canThrowPotion() {
      return mc.isNotInGui();
   }

   private TuplePair findSplashPotionInHotbar(int n) {

      for (int i = 0; i < 9; i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();
         if (stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getMetadata())) {
            ItemPotion item = (ItemPotion)stack.getItem();
            List effects = item.getEffects(stack);
            if (!effects.isEmpty()) {
               PotionEffect potionEffect = (PotionEffect)effects.get(0);
               if (potionEffect.getPotionID() == n) {
                  return new TuplePair(stack, i);
               }
            }
         }
      }

      return null;
   }

}
