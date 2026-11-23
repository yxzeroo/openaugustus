package net.augustus.modules.legit;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.events.EventInventoryClose;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.utils.player.ContainerUtil;
import net.augustus.viaversion.ViaAugustus;
import net.augustus.utils.player.InventoryUtil;
import net.augustus.settings.options.RefillItemTypeOption;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.events.listeners.InventoryCloseListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Refill extends Module implements SilentSlotListener, InventoryCloseListener, RenderListener {
   private final BooleanValue autoOpen;
   private final RandomizedRangeSettings autoOpenDelay;
   private final BooleanValue autoClose;
   private final RandomizedRangeSettings autoCloseDelay;
   private final RandomizedRangeSettings startDelay;
   private final MultiOptionSetting types;
   private final BooleanValue blockInvActions;
   private final DoubleValue craftGroup;
   private final BooleanValue craft;
   private final StringValue reCraftIfSoupsLowerThan;
   private final BooleanValue craftInstant;
   private final StringValue maxMouseMoveSpeedCraft;
   private final RandomizedRangeSettings craftCPS;
   private final DoubleValue refillGroup;
   private final BooleanValue refillInstant;
   private final StringValue maxMouseMoveSpeedRefill;
   private final RandomizedRangeSettings refillCPS;
   private final RandomizedRangeSettings missClickRate;
   private final RandomizedRangeSettings failRate;
   private final RandomizedRangeSettings doubleClickRate;
   private final BooleansSetting sequence;
   private final TimeHelper inventoryOpenTimer;
   private final ActionRateAccumulator missClickAccumulator;
   private final ActionRateAccumulator failClickAccumulator;
   private final ActionRateAccumulator doubleClickAccumulator;
   private int lastClickedSlot;
   private final ActionRateAccumulator clickRateAccumulator;
   private double refillMouseMoveBudget;
   private double craftMouseMoveBudget;
   private boolean performedAction;
   private final TimeHelper autoOpenTimer;
   private final TimeHelper autoCloseTimer;
   private boolean awaitingAutoCloseStart;
   private final TimeHelper lastInventoryActionTimer;

   public Refill() {
       super("Refill", "Auffüllen", new Color(66, 203, 245), Categorys.LEGIT);
      this.autoOpen = new BooleanValue(this, "AutoOpen", false);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "AutoOpenDelay", 150.0, 250.0, 0.0, 1000.0, 0)).withoutNotInARow();
      BooleanValue autoOpen2 = this.autoOpen;
      this.autoOpen.getClass();
      BooleanValue booleanValue = autoOpen2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.autoOpenDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      this.autoClose = new BooleanValue(this, "AutoClose", true);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "AutoCloseDelay", 150.0, 250.0, 0.0, 1000.0, 0)).withoutNotInARow();
      autoOpen2 = this.autoClose;
      this.autoClose.getClass();
      BooleanValue booleanValue2 = autoOpen2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.autoCloseDelay = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition2);
      this.startDelay = (new RandomizedRangeSettings(this, "StartDelay", 200.0, 260.0, 0.0, 1000.0, 0)).withoutNotInARow();
      this.types = new MultiOptionSetting(this, "Types", RefillItemTypeOption.values());
      this.blockInvActions = new BooleanValue(this, "BlockInvActions", false);
      DoubleValue doubleValue = new DoubleValue(this, "CraftGroup", true);
      String str = "Craft";
      DoubleValue doubleValue2 = (DoubleValue)doubleValue.withDisplayName(str);
      SettingVisibilityCondition condition3 = RefillItemTypeOption.SOUPS::isSelected;
      this.craftGroup = doubleValue2.visibleWhen(condition3);
      this.craft = new BooleanValue(this, "Craft", true, this.craftGroup);
      StringValue stringValue = new StringValue(this, "ReCraftIfSoupsLowerThan", 4.0, 0.0, 20.0, 0, this.craftGroup);
      autoOpen2 = this.craft;
      this.craft.getClass();
      BooleanValue booleanValue3 = autoOpen2;
      SettingVisibilityCondition condition4 = () -> booleanValue3.getValue();
      this.reCraftIfSoupsLowerThan = stringValue.visibleWhen(condition4);
      BooleanValue booleanValue4 = new BooleanValue(this, "CraftInstant", false, this.craftGroup);
      autoOpen2 = this.craft;
      this.craft.getClass();
      BooleanValue booleanValue5 = autoOpen2;
      SettingVisibilityCondition condition5 = () -> booleanValue5.getValue();
      this.craftInstant = (BooleanValue)booleanValue4.withVisibility(condition5);
      StringValue stringValue2 = new StringValue(this, "MaxMouseMoveSpeedCraft", 19.0, 0.0, 200.0, 0, this.craftGroup);
      String str2 = "MaxMouseMoveSpeed";
      StringValue stringValue3 = stringValue2.named(str2);
      SettingVisibilityCondition condition6 = () -> this.isCraftMouseSpeedVisible();
      this.maxMouseMoveSpeedCraft = stringValue3.visibleWhen(condition6);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "CraftCPS", 13.0, 16.0, 0.0, 20.0, 0, this.craftGroup)).withoutNotInARow();
      SettingVisibilityCondition condition7 = () -> {
         return this.craft.getValue() && !this.craftInstant.getValue();
      };
      this.craftCPS = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition7);
      DoubleValue doubleValue3 = new DoubleValue(this, "RefillGroup", true);
      String str3 = "Refill";
      this.refillGroup = (DoubleValue)doubleValue3.withDisplayName(str3);
      this.refillInstant = new BooleanValue(this, "RefillInstant", false, this.refillGroup);
      StringValue stringValue4 = new StringValue(this, "MaxMouseMoveSpeedRefill", 19.0, 0.0, 200.0, 0, this.refillGroup);
      String str4 = "MaxMouseMoveSpeed";
      StringValue stringValue5 = stringValue4.named(str4);
      SettingVisibilityCondition condition8 = () -> this.isRefillMouseSpeedVisible();
      this.maxMouseMoveSpeedRefill = stringValue5.visibleWhen(condition8);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this, "RefillCPS", 10.0, 15.0, 0.0, 20.0, 0, this.refillGroup)).withoutNotInARow();
      SettingVisibilityCondition condition9 = () -> this.isRefillCpsVisible();
      this.refillCPS = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition9);
      this.missClickRate = (new RandomizedRangeSettings(this, "MissClickRate", 5.0, 10.0, 0.0, 100.0, 0, this.refillGroup)).withoutNotInARow();
      this.failRate = (new RandomizedRangeSettings(this, "FailRate", 5.0, 10.0, 0.0, 100.0, 0, this.refillGroup)).withoutNotInARow();
      this.doubleClickRate = (new RandomizedRangeSettings(this, "DoubleClickRate", 5.0, 10.0, 0.0, 100.0, 0, this.refillGroup)).withoutNotInARow();
      this.sequence = new BooleansSetting(this, "Sequence", "Basic", new String[]{"Basic", "Nearest", "Random"}, this.refillGroup);
      this.inventoryOpenTimer = new TimeHelper();
      this.missClickAccumulator = new ActionRateAccumulator(100, 0.0);
      this.failClickAccumulator = new ActionRateAccumulator(100, 0.0);
      this.doubleClickAccumulator = new ActionRateAccumulator(100, 0.0);
      this.lastClickedSlot = -1;
      this.clickRateAccumulator = new ActionRateAccumulator(20, 0.0);
      this.refillMouseMoveBudget = 0.0;
      this.craftMouseMoveBudget = 0.0;
      this.performedAction = false;
      this.autoOpenTimer = new TimeHelper();
      this.autoCloseTimer = new TimeHelper();
      this.awaitingAutoCloseStart = true;
      this.lastInventoryActionTimer = new TimeHelper();
   }

   @Override
   public void onEnable() {
      this.performedAction = false;
      this.awaitingAutoCloseStart = true;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
      byte b2 = 2;
      byte b3 = 52;
      this.toggleEventSubscription(b3, b2);
      byte b4 = 34;
      this.toggleEventSubscription(b4);
   }

   @Override
   public void onSilentSlot() {
      if (this.autoOpen.getValue() && mc.currentScreen == null && !mc.playerController.isInCreativeMode()) {
         RefillItemTypeOption soups = RefillItemTypeOption.SOUPS;
         int size = mc.thePlayer.inventoryContainer.inventorySlots.size();
         byte b = 36;
         int size2 = soups.findMatchingSlots(b, size).size();
         soups = RefillItemTypeOption.HEAL_POTS;
         int size3 = mc.thePlayer.inventoryContainer.inventorySlots.size();
         byte b2 = 36;
         int size4 = soups.findMatchingSlots(b2, size3).size();
         int n;
         if (RefillItemTypeOption.SOUPS.isSelected()) {
            byte b3 = 36;
            byte b4 = 9;
            n = RefillItemTypeOption.SOUPS.findMatchingSlots(b4, b3).size();
         } else {
            n = 0;
         }

         int n2 = n;
         int n3;
         if (RefillItemTypeOption.HEAL_POTS.isSelected()) {
            byte b5 = 36;
            byte b6 = 9;
            n3 = RefillItemTypeOption.HEAL_POTS.findMatchingSlots(b6, b5).size();
         } else {
            n3 = 0;
         }

         int n4 = n3;
         long l;
         if (RefillItemTypeOption.SOUPS.isSelected()) {
            ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
            Item bowl = Items.bowl;
            l = containerSlotQuery.filterItemExact(bowl).count();
         } else {
            l = 0L;
         }

         long l2 = l;
         long l3;
         if (RefillItemTypeOption.SOUPS.isSelected()) {
            ContainerSlotQuery containerSlotQuery2 = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
            Item itemById = Item.getItemById(40);
            l3 = containerSlotQuery2.filterItemExact(itemById).count();
         } else {
            l3 = 0L;
         }

         long l4 = l3;
         long l5;
         if (RefillItemTypeOption.SOUPS.isSelected()) {
            ContainerSlotQuery containerSlotQuery3 = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
            Item itemById2 = Item.getItemById(39);
            l5 = containerSlotQuery3.filterItemExact(itemById2).count();
         } else {
            l5 = 0L;
         }

         long l6 = l5;
         if (size2 == 0 && (n2 > 0 || l6 > 0L && l4 > 0L && l2 > 0L && this.craft.getValue()) || size4 == 0 && n4 > 0) {
            boolean flag = false;

            for (int i = 36; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
               ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (stack == null || stack.getItem() == Items.bowl) {
                  flag = true;
                  break;
               }
            }

            if (flag) {
               TimeHelper autoOpenTimer2 = this.autoOpenTimer;
               long lastIntValue = (long)this.autoOpenDelay.getLastIntValue();
               if (autoOpenTimer2.reached2(lastIntValue)) {
                  ViaAugustus.sendOpenInventoryPacketIfLegacy();
                  Minecraft mc2 = mc;
                  GuiInventory guiInventory = new GuiInventory(mc.thePlayer);
                  mc2.displayGuiScreen(guiInventory);
                  this.autoOpenTimer.reset();
                  this.autoOpenDelay.nextValue();
               }
            }
         } else {
            this.autoOpenTimer.reset();
         }
      } else {
         this.autoOpenTimer.reset();
      }
   }

   @Override
   public boolean onInventoryClose() {
      if (!ContainerUtil.isInventoryOpen()) {
         this.awaitingAutoCloseStart = true;
         this.performedAction = false;
         this.lastClickedSlot = -1;
         this.inventoryOpenTimer.reset();
         this.resetMouseMoveBudgets();
         if (mc.previousScreen != null) {
            this.startDelay.nextValue();
         }

         return false;
      } else {
         TimeHelper inventoryOpenTimer2 = this.inventoryOpenTimer;
         long lastIntValue = (long)this.startDelay.getLastIntValue();
         if (!inventoryOpenTimer2.reached2(lastIntValue)) {
            this.lastInventoryActionTimer.reset();
            return true;
         } else {
            boolean flag = this.processRefill();
            if (flag) {
               this.lastInventoryActionTimer.reset();
            }

            return flag;
         }
      }
   }

   @Override
   public void onInventoryClose(EventInventoryClose event) {
      if (!mc.isNotInGui()) {
         if (this.autoClose.getValue() && this.performedAction) {
            double lastIntValue = (double)this.autoCloseDelay.getLastIntValue();
            double d = lastIntValue;
            if (event.canCloseAfterDelay(d)) {
               mc.thePlayer.closeScreen();
               this.awaitingAutoCloseStart = true;
            }
         }
      }
   }

   private boolean processRefill() {
      this.craftMouseMoveBudget = this.craftMouseMoveBudget + this.maxMouseMoveSpeedCraft.getValue();
      if (!this.processSoupCrafting()) {
         return true;
      } else {
         this.refillMouseMoveBudget = this.refillMouseMoveBudget + this.maxMouseMoveSpeedRefill.getValue();
         boolean flag = false;

         for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack == null || stack.getItem().equals(Items.bowl) && stack.stackSize < stack.getMaxStackSize()) {
               flag = true;
               break;
            }
         }

         if (flag) {
            for (int j = 36; j < mc.thePlayer.inventoryContainer.inventorySlots.size(); j++) {
               ItemStack stack2 = mc.thePlayer.inventoryContainer.getSlot(j).getStack();
               if (stack2 != null && stack2.getItem().equals(Items.bowl)) {
                  if (this.isRefillClickDelayed(j)) {
                     return true;
                  }

                  this.clickRefillSlot(j);
               }
            }
         }

         int n = 0;

         for (int k = 36; k < mc.thePlayer.inventoryContainer.inventorySlots.size(); k++) {
            ItemStack stack3 = mc.thePlayer.inventoryContainer.getSlot(k).getStack();
            if (stack3 == null) {
               n++;
            }
         }

         ArrayList list = new ArrayList();
         int n2 = n;

         for (RefillItemTypeOption refillItemTypeOption : RefillItemTypeOption.values()) {
            byte b = 36;
            byte b2 = 9;

            for (int n3 : (Iterable<Integer>)(refillItemTypeOption.findMatchingSlots(b2, b))) {
               n2--;
               list.add(n3);
            }

            if (n2 != n) {
               break;
            }
         }

         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         String selectedValue = this.sequence.getSelectedValue();
         String str = selectedValue;
         List list2 = InventoryUtil.sortSlotsByMode(inventoryContainer, list, str);

         for (int m = 0; m < list2.size() && n > 0; m++) {
            int n4 = (Integer)list2.get(m);
            if (this.isRefillClickDelayed(n4)) {
               return true;
            }

            this.clickRefillSlot(n4);
            n--;
         }

         if (this.autoClose.getValue() && !mc.isNotInGui() && this.performedAction) {
            if (this.awaitingAutoCloseStart) {
               this.autoCloseTimer.reset();
               this.awaitingAutoCloseStart = false;
               this.autoCloseDelay.nextValue();
            }

            return false;
         } else {
            return false;
         }
      }
   }

   private boolean processSoupCrafting() {
      if (!RefillItemTypeOption.SOUPS.isSelected()) {
         return true;
      } else if (mc.currentScreen instanceof GuiInventory && this.craft.getValue()) {
         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         int[] ints = this.countSoupIngredients(inventoryContainer);
         if (ints[1] != 0 && ints[2] != 0 && ints[3] != 0) {
            int n = Math.min(ints[1], Math.min(ints[2], ints[3]));
            int n2 = ints[0];
            if (n <= 0) {
               return true;
            } else {
               int n3 = n > n2 ? 1 : 0;
               Item[] items = new Item[]{Items.bowl, Item.getItemById(40), Item.getItemById(39)};
               boolean[] flags = new boolean[]{false, false, false};
               boolean flag = false;

               for (int i = 1; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
                  if (i == 1) {
                     flags = new boolean[]{false, false, false};
                     Container inventoryContainer2 = mc.thePlayer.inventoryContainer;
                     n2 = this.countSoupIngredients(inventoryContainer2)[0];
                  }

                  ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                  if (stack != null) {
                     Item item = stack.getItem();
                     if (i < 5) {
                        for (int j = 0; j < items.length; j++) {
                           if (items[j].equals(item)) {
                              flags[j] = true;
                           }
                        }
                     }

                     if (n2 > 0 && (double)ints[4] <= this.reCraftIfSoupsLowerThan.getValue()) {
                        if (mc.thePlayer.inventory.getItemStack() == null) {
                           if (i > 8 && (item.equals(items[0]) && !flags[0] || item.equals(items[1]) && !flags[1] || item.equals(items[2]) && !flags[2])
                              )
                            {
                              if (this.isCraftClickDelayed(i)) {
                                 return false;
                              }

                              Container inventoryContainer3 = mc.thePlayer.inventoryContainer;
                              this.clickContainerSlot(inventoryContainer3, i, n3);
                              flag = true;
                              i = 0;
                           }

                           if (flags[0] && flags[1] && flags[2]) {
                              byte b = 0;
                              if (this.isCraftClickDelayed(b)) {
                                 return false;
                              }

                              byte b2 = 0;
                              this.simulateInventorySlotClick(b2);
                              flag = true;
                              i = 0;
                           }
                        } else {
                           ItemStack itemStack = mc.thePlayer.inventory.getItemStack();
                           Item item2 = itemStack.getItem();

                           for (int k = 0; k < 3; k++) {
                              if (item2.equals(items[k]) && !flags[k]) {
                                 int n4 = k + 1;
                                 if (this.isCraftClickDelayed(n4)) {
                                    return false;
                                 }

                                 int n5 = k + 1;
                                 byte b3 = 0;
                                 int n6 = n5;
                                 Container inventoryContainer4 = mc.thePlayer.inventoryContainer;
                                 this.clickContainerSlot(inventoryContainer4, n6, b3);
                                 flag = true;
                                 i = 0;
                              }
                           }
                        }
                     } else if (i > 5) {
                        for (int m = 0; m < items.length; m++) {
                           if (flags[m]) {
                              ItemStack stack2 = null;
                              int n7 = 0;

                              for (int idx = 0; idx < 5; idx++) {
                                 ItemStack stack3 = mc.thePlayer.inventoryContainer.getSlot(idx).getStack();
                                 if (stack3 != null && stack3.getItem().equals(items[m])) {
                                    stack2 = stack3;
                                    n7 = idx;
                                    break;
                                 }
                              }

                              for (int index = 9; index < mc.thePlayer.inventoryContainer.inventorySlots.size(); index++) {
                                 ItemStack stack4 = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                                 if (stack2 != null && (stack4 == null || stack4.getItem().equals(stack2.getItem()) && stack4.stackSize < stack2.getMaxStackSize())) {
                                    if (this.isCraftClickDelayed(n7)) {
                                       return false;
                                    }

                                    this.simulateInventorySlotClick(n7);
                                    flag = true;
                                    i = 0;
                                    break;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               return !flag;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private int[] countSoupIngredients(Container container) {
      int[] ints = new int[5];

      for (int i = -1; i < container.inventorySlots.size(); i++) {
         ItemStack stack = i == -1 ? mc.thePlayer.inventory.getItemStack() : container.getSlot(i).getStack();
         if (stack == null) {
            if (i > 8) {
               ints[0]++;
            }
         } else {
            Item item = stack.getItem();
            if (item.equals(Items.bowl)) {
               ints[1] += stack.stackSize;
            } else if (item.equals(Item.getItemById(40))) {
               ints[2] += stack.stackSize;
            } else if (item.equals(Item.getItemById(39))) {
               ints[3] += stack.stackSize;
            } else if (item.equals(Items.mushroom_stew) && i != 0) {
               ints[4]++;
            }
         }
      }

      return ints;
   }

   private boolean isCraftClickDelayed(int n) {
      if (this.craftInstant.getValue()) {
         return false;
      } else {
         ActionRateAccumulator clickRateAccumulator2 = this.clickRateAccumulator;
         int lastIntValue = this.craftCPS.getLastIntValue();
         int n2 = clickRateAccumulator2.accumulate(lastIntValue);
         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         double craftMouseMoveBudget2 = this.craftMouseMoveBudget;
         n2 = this.filterClicksByMouseSpeed(craftMouseMoveBudget2, inventoryContainer, n, n2);
         if (n2 > 0) {
            this.craftCPS.nextValue();
         }

         return n2 <= 0;
      }
   }

   private boolean isRefillClickDelayed(int n) {
      if (this.refillInstant.getValue()) {
         return false;
      } else {
         ActionRateAccumulator clickRateAccumulator2 = this.clickRateAccumulator;
         int lastIntValue = this.refillCPS.getLastIntValue();
         int n2 = clickRateAccumulator2.accumulate(lastIntValue);
         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         double refillMouseMoveBudget2 = this.refillMouseMoveBudget;
         n2 = this.filterClicksByMouseSpeed(refillMouseMoveBudget2, inventoryContainer, n, n2);
         if (n2 > 0) {
            this.refillCPS.nextValue();
         }

         return n2 <= 0;
      }
   }

   private int filterClicksByMouseSpeed(double d, Container container, int n, int n2) {
      Slot slot = container.getSlot(n);
      ScaledResolution sr = new ScaledResolution(mc);
      int n3 = (sr.getScaledWidth() - 176) / 2;
      int n4 = (sr.getScaledHeight() - this.getContainerGuiHeight(container)) / 2;
      double d2 = Math.abs((double)(slot.xDisplayPosition + 2 + n3) - InventoryUtil.getLastCursorPos().x);
      double d3 = Math.abs((double)(slot.yDisplayPosition + 2 + n4) - InventoryUtil.getLastCursorPos().y);
      double d4 = Math.sqrt(d2 * d2 + d3 * d3);
      return d > d4 ? n2 : 0;
   }

   private int getContainerGuiHeight(Container container) {
      return 114 + (container.inventorySlots.size() - 36) / 9 * 18;
   }

   private void clickContainerSlot(Container container, int n, int n2) {
      mc.playerController.windowClick(container.windowId, n, n2, 0, mc.thePlayer);
      this.updateVirtualCursorToSlot(container, n);
      this.performedAction = true;
   }

   private void clickRefillSlot(int n) {
      this.performedAction = true;
      if (!this.rollFailClick() || this.refillInstant.getValue()) {
         boolean flag = !this.refillInstant.getValue() && this.rollMissClick() && this.lastClickedSlot > 0;
         if (flag) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(this.lastClickedSlot);
            int xDisplayPosition = slot.xDisplayPosition;
            int yDisplayPosition = slot.yDisplayPosition;
            double maxvalue = Double.MAX_VALUE;
            int n2 = -1;

            for (int i = 9; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
               ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
               if (stack == null && i != this.lastClickedSlot && i != n2) {
                  Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(i);
                  int n3 = xDisplayPosition - slot2.xDisplayPosition;
                  int n4 = yDisplayPosition - slot2.yDisplayPosition;
                  double d = Math.sqrt((double)(n3 * n3 + n4 * n4));
                  if (d < maxvalue) {
                     maxvalue = d;
                     n2 = i;
                  }
               }
            }

            if (maxvalue < 36.0) {
               this.simulateInventorySlotClick(n2);
               this.lastClickedSlot = n;
               return;
            }
         }

         for (int j = this.rollDoubleClick() ? 2 : 1; j > 0; j--) {
            this.simulateInventorySlotClick(n);
            this.lastClickedSlot = n;
         }
      }
   }

   private void simulateInventorySlotClick(int n) {
      Container inventoryContainer = mc.thePlayer.inventoryContainer;
      this.updateVirtualCursorToSlot(inventoryContainer, n);

      try {
         Slot slot = mc.thePlayer.inventoryContainer.getSlot(n);
         GuiInventory currentScreen = (GuiInventory)mc.currentScreen;
         currentScreen.forceShiftClick = true;
         currentScreen.mouseClicked(slot.xDisplayPosition + 2 + currentScreen.guiLeft, slot.yDisplayPosition + 2 + currentScreen.guiTop, 0);
      } catch (IOException e) {
         e.printStackTrace();
      }

      this.performedAction = true;
   }

   private boolean rollFailClick() {
      ActionRateAccumulator failClickAccumulator2 = this.failClickAccumulator;
      int lastIntValue = this.failRate.getLastIntValue();
      boolean flag = failClickAccumulator2.shouldTrigger(lastIntValue);
      if (flag) {
         this.failRate.nextValue();
      }

      return flag;
   }

   private boolean rollMissClick() {
      ActionRateAccumulator missClickAccumulator2 = this.missClickAccumulator;
      int lastIntValue = this.missClickRate.getLastIntValue();
      boolean flag = missClickAccumulator2.shouldTrigger(lastIntValue);
      if (flag) {
         this.missClickRate.nextValue();
      }

      return flag;
   }

   private boolean rollDoubleClick() {
      ActionRateAccumulator doubleClickAccumulator2 = this.doubleClickAccumulator;
      int lastIntValue = this.doubleClickRate.getLastIntValue();
      boolean flag = doubleClickAccumulator2.shouldTrigger(lastIntValue);
      if (flag) {
         this.doubleClickRate.nextValue();
      }

      return flag;
   }

   private void resetMouseMoveBudgets() {
      this.craftMouseMoveBudget = 0.0;
      this.refillMouseMoveBudget = 0.0;
   }

   private void updateVirtualCursorToSlot(Container container, int n) {
      this.resetMouseMoveBudgets();
      ScaledResolution sr = new ScaledResolution(mc);
      int n2 = (sr.getScaledWidth() - 176) / 2;
      int n3 = (sr.getScaledHeight() - this.getContainerGuiHeight(container)) / 2;
      Slot slot = container.getSlot(n);
      InventoryUtil.setLastCursorPos((double)(slot.xDisplayPosition + 2 + n2), (double)(slot.yDisplayPosition + 2 + n3));
      InventoryUtil.setLastClickedSlot(n);
      this.lastInventoryActionTimer.reset();
   }

   public boolean shouldBlockInventoryActions() {
      if (mc.thePlayer != null && mc.theWorld != null && this.blockInvActions.getValue()) {
         long l = 500L;
         if (!this.lastInventoryActionTimer.reached2(l) && mc.currentScreen instanceof GuiInventory) {
            return true;
         }
      }

      return false;
   }

   private boolean isRefillCpsVisible() {
      return !this.refillInstant.getValue();
   }

   private boolean isRefillMouseSpeedVisible() {
      return !this.refillInstant.getValue();
   }

   private boolean isCraftMouseSpeedVisible() {
      return this.craft.getValue() && !this.craftInstant.getValue();
   }

}
