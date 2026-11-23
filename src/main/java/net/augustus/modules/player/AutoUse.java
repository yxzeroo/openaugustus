package net.augustus.modules.player;

import java.awt.Color;
import java.util.Arrays;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.options.AutoUsePotionTriggerOption;
import net.augustus.utils.TickCounter;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class AutoUse extends Module implements SilentSlotListener, ClickListener {
   private final RandomizedRangeSettings minimumDelayBetweenUsages;
   private final RandomizedRangeSettings tickDelayAfterSlotSwitch;
   private final RandomizedRangeSettings tickDelayBeforeBackswitch;
   private final StringValue itemId;
   private final BooleanValue silentSlotSwitching;
   private final MultiOptionSetting potionEffectToTriggerUsage;
   private final TickCounter ticksAfterSwitch;
   private final TickCounter totalUsageTicks;
   private final TimeHelper timeHelper;
   private int itemHotbarSlot;
   private boolean usingItem;

   public AutoUse() {
       super("AutoUse", "AutomatischeBenutzung", new Color(162, 242, 184), Categorys.PLAYER);
      this.minimumDelayBetweenUsages = new RandomizedRangeSettings(this, "Minimum delay between usages", 10.0, 20.0, 0.0, 200.0, 2);
      this.tickDelayAfterSlotSwitch = new RandomizedRangeSettings(this, "Tick delay after slot switch", 2.0, 4.0, 0.0, 10.0, 0);
      this.tickDelayBeforeBackswitch = new RandomizedRangeSettings(this, "Tick delay before backswitch", 2.0, 4.0, 0.0, 10.0, 0);
      this.itemId = new StringValue(this, "Item ID", 26.0, 0.0, 300.0, 0);
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.potionEffectToTriggerUsage = new MultiOptionSetting(this, "Potion effect to trigger usage", Arrays.stream(AutoUsePotionTriggerOption.values()).sorted().toArray(AutoUsePotionTriggerOption[]::new));
      this.ticksAfterSwitch = new TickCounter();
      this.totalUsageTicks = new TickCounter();
      this.timeHelper = new TimeHelper();
      this.itemHotbarSlot = -1;
      this.usingItem = false;
      AutoUsePotionTriggerOption.initSettings(this);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 34;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onClick(EventClick event) {
      this.ticksAfterSwitch.increment();
      this.totalUsageTicks.increment();
      if (mc.currentScreen != null) {
         this.ticksAfterSwitch.reset();
         this.totalUsageTicks.reset();
      }

      if (this.shouldUseItem()) {
         if (this.ticksAfterSwitch.getTicks() == (long)this.tickDelayAfterSlotSwitch.getLastIntValue()) {
            byte b = 1;
            event.addRightClicks(b);
         }

         TickCounter totalUsageTicks2 = this.totalUsageTicks;
         int n = this.tickDelayBeforeBackswitch.getLastIntValue() + this.tickDelayAfterSlotSwitch.getLastIntValue();
         if (totalUsageTicks2.hasReached2(n)) {
            this.timeHelper.reset();
            this.totalUsageTicks.reset();
            this.ticksAfterSwitch.reset();
            this.tickDelayBeforeBackswitch.nextValue();
            this.tickDelayAfterSlotSwitch.nextValue();
            this.minimumDelayBetweenUsages.nextValue();
         }
      }

      if (!this.isTriggerConditionMet()) {
         this.totalUsageTicks.reset();
         this.ticksAfterSwitch.reset();
      }
   }

   @Override
   public void onSilentSlot() {
      this.itemHotbarSlot = this.findTargetItemHotbarSlot();
      if (this.shouldUseItem()) {
         boolean value = this.silentSlotSwitching.getValue();
         byte b = 1;
         byte b2 = 19;
         SilentSlotManager.requestSlot(this, b2, b, value);
      }
   }

   private boolean isTriggerConditionMet() {
      boolean flag = false;
      boolean flag2 = false;

      for (AutoUsePotionTriggerOption option : AutoUsePotionTriggerOption.values()) {
         if (option.isSelected()) {
            flag2 = true;
            int id = AutoUsePotionTriggerOption.getPotion(option).getId();
            if (!mc.thePlayer.isPotionActive(id)) {
               flag = true;
               break;
            }

            PotionEffect activePotionEffect = mc.thePlayer.getActivePotionEffect(AutoUsePotionTriggerOption.getPotion(option));
            int n = activePotionEffect.getDuration() / 20;
            if ((double)n < AutoUsePotionTriggerOption.getMinDurationSetting(option).getValue()) {
               flag = true;
               break;
            }
         }
      }

      TimeHelper timeHelper2 = this.timeHelper;
      double d = this.minimumDelayBetweenUsages.getLastValue() * 1000.0;
      return timeHelper2.reached(d) && (flag || !flag2);
   }

   private boolean shouldUseItem() {
      if (this.itemHotbarSlot >= 0 && mc.isNotInGui()) {
         if (this.isTriggerConditionMet()) {
            return true;
         }
      }

      return false;
   }

   private int findTargetItemHotbarSlot() {
      Item itemById = Item.getItemById((int)this.itemId.getValue());
      if (itemById != null) {
         for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();
            if (stack != null && stack.getItem().equals(itemById)) {
               return i;
            }
         }
      }

      return -1;
   }

}
