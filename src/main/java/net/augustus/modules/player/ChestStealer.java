package net.augustus.modules.player;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.ClientManagers;
import net.augustus.settings.StringListSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.player.ContainerUtil;
import net.augustus.utils.TuplePair;
import net.augustus.render.RenderUtil;
import net.augustus.settings.options.InvManagerActionOption;
import net.augustus.utils.TickCounter;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.Augustus;
import net.augustus.settings.ItemFilterSetting;
import net.augustus.utils.player.InventoryUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ChestStealer extends Module implements TickListener, RenderListener {
   private final BooleansSetting mode;
   private final RandomizedRangeSettings startDelay;
   private final BooleanValue instant;
   private final StringValue maxMouseMoveSpeed;
   private final RandomizedRangeSettings mouseMoveRandom;
   private final RandomizedRangeSettings cps;
   private final RandomizedRangeSettings cpsRecalculate;
   private final RandomizedRangeSettings doubleClickRate;
   private final RandomizedRangeSettings failRate;
   private final RandomizedRangeSettings missClickRate;
   private final RandomizedRangeSettings cpsNew;
   private final RandomizedRangeSettings cpsRecalculateNew;
   private final RandomizedRangeSettings doubleClickRateNew;
   private final RandomizedRangeSettings sideStopTicks;
   private final RandomizedRangeSettings emptySlotFailRate;
   private final BooleanValue removeEmptyRows;
   private final BooleanValue clickWindow;
   private final BooleanValue stealZeroStackSizeItems;
   private final BooleanValue intelligent;
   private final BooleanValue ignoreBadPotions;
   private final DoubleValue itemBlackList;
   private final BooleanValue autoSort;
   private final BooleanValue autoClose;
   private final BooleanValue autoCloseIfInvFull;
   private final RandomizedRangeSettings closeDelay;
   private final RandomizedRangeSettings chestOpenTime;
   public final BooleanValue visualize;
   public final BooleansSetting whereToSteal;
   private final BooleanValue ignoreEnderChests;
   private final BooleansSetting sequence;
   private final BooleansSetting clickMode;
   private final BooleanValue toggleStorageESP;
   private final BooleanValue toggleChestAura;
   public final StringListSetting chestStorage;
   public final BooleanValue silent;
   private final BooleanValue stealIndicator;
   private final ColorSetting color2;
   private final BooleanValue backGround;
   private final ColorSetting backgroundColor;
   private final BlurShadowSettings blurShadowSettings;
   private boolean wasChestOpen;
   private int nextTargetSlot;
   private int ticksSinceLastClick;
   private final TimeHelper startDelayTimer;
   private final TimeHelper closeDelayTimer;
   private final TimeHelper chestOpenTimer;
   private final ActionRateAccumulator doubleClickAccumulator;
   private final ActionRateAccumulator failClickAccumulator;
   private final ActionRateAccumulator missClickAccumulator;
   private final ActionRateAccumulator cpsAccumulator;
   private final TickCounter cpsRecalculateCounter;
   private final TickCounter cpsNewRecalculateCounter;
   private final TickCounter secondaryTickCounter;
   private final TickCounter sideStopCounter;
   private final ActionRateAccumulator cpsNewAccumulator;
   private final ActionRateAccumulator doubleClickNewAccumulator;
   private final ActionRateAccumulator emptySlotFailAccumulator;
   private final ArrayList failedEmptySlots;
   private final ArrayList visitedSlots;
   private ArrayList bestArmorStacks;
   private List cachedRandomSlotOrder;
   private final ItemFilterSetting items;
   private int lastRowIndex;
   private int initialItemCount;
   private int stolenItemCount;
   private boolean wasChestOpenLastTick;
   private boolean startedStealing;

   @Override
   public void onEnable() {
      boolean flag = true;
      this.toggleLinkedModules(flag);
      this.resetRandomizers();
   }

   @Override
   public void onDisable() {
      boolean flag = false;
      this.toggleLinkedModules(flag);
      this.nextTargetSlot = -1;
      this.bestArmorStacks.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 10;
      byte b2 = 40;
      this.toggleEventSubscription(b2, b);
      byte b3 = -2;
      byte b4 = 32;
      this.toggleEventSubscription(b4, b3);
   }

   public ChestStealer() {
       super("ChestStealer", "TruhenAusbeutung", new Color(196, 184, 249, 255), Categorys.PLAYER);
      this.mode = new BooleansSetting(this, "Mode", "Default", new String[]{"Default", "Experimental"});
      this.startDelay = (new RandomizedRangeSettings(this, "StartDelay", 125.0, 175.0, 0.0, 1000.0, 0)).withoutNotInARow();
      this.instant = new BooleanValue(this, "Instant", false);
      StringValue stringValue = new StringValue(this, "MaxMouseMoveSpeed", 19.0, 0.0, 200.0, 0);
      SettingVisibilityCondition condition = () -> this.isMaxMouseMoveSpeedVisible();
      this.maxMouseMoveSpeed = stringValue.visibleWhen(condition);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "MouseMoveRandom", 0.0, 0.0, -1.0, 2.0, 0);
      SettingVisibilityCondition condition2 = () -> {
         return !this.instant.getValue() && this.isDefaultMode();
      };
      this.mouseMoveRandom = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      RandomizedRangeSettings randomizedRangeSettings2 = new RandomizedRangeSettings(this, "CPS", 17.0, 20.0, 0.0, 40.0, 0);
      SettingVisibilityCondition condition3 = () -> {
         return !this.instant.getValue() && this.isDefaultMode();
      };
      this.cps = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "CPSRecalculate", 6.0, 8.0, 0.0, 40.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> this.isCpsRecalculateVisible();
      this.cpsRecalculate = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this, "DoubleClickRate", 5.0, 10.0, 0.0, 100.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition5 = () -> this.isDoubleClickRateVisible();
      this.doubleClickRate = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition5);
      RandomizedRangeSettings randomizedRangeSettings5 = (new RandomizedRangeSettings(this, "FailRate", 5.0, 10.0, 0.0, 100.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition6 = () -> this.isFailRateVisible();
      this.failRate = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition6);
      RandomizedRangeSettings randomizedRangeSettings6 = (new RandomizedRangeSettings(this, "MissClickRate", 0.0, 0.0, 0.0, 100.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition7 = () -> this.isMissClickRateVisible();
      this.missClickRate = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition7);
      RandomizedRangeSettings randomizedRangeSettings7 = (new RandomizedRangeSettings(this, "CPSNew", 15.0, 20.0, 0.0, 40.0, 0)).withoutNotInARow();
      String str = "CPS";
      RandomizedRangeSettings randomizedRangeSettings8 = (RandomizedRangeSettings)randomizedRangeSettings7.withDisplayNamePrefix(str);
      SettingVisibilityCondition condition8 = () -> this.isCpsNewVisible();
      this.cpsNew = (RandomizedRangeSettings)randomizedRangeSettings8.withVisibility(condition8);
      RandomizedRangeSettings randomizedRangeSettings9 = new RandomizedRangeSettings(this, "CPSRecalculateNew", 6.0, 8.0, 0.0, 60.0, 0);
      String str2 = "CPSRecalculate";
      RandomizedRangeSettings randomizedRangeSettings10 = ((RandomizedRangeSettings)randomizedRangeSettings9.withDisplayNamePrefix(str2)).withoutNotInARow();
      SettingVisibilityCondition condition9 = () -> this.isCpsRecalculateNewVisible();
      this.cpsRecalculateNew = (RandomizedRangeSettings)randomizedRangeSettings10.withVisibility(condition9);
      RandomizedRangeSettings randomizedRangeSettings11 = (new RandomizedRangeSettings(this, "DoubleClickRateNew", 6.0, 12.0, 0.0, 100.0, 0)).withoutNotInARow();
      String str3 = "DoubleClickRate";
      RandomizedRangeSettings randomizedRangeSettings12 = (RandomizedRangeSettings)randomizedRangeSettings11.withDisplayNamePrefix(str3);
      SettingVisibilityCondition condition10 = () -> this.isDoubleClickRateNewVisible();
      this.doubleClickRateNew = (RandomizedRangeSettings)randomizedRangeSettings12.withVisibility(condition10);
      RandomizedRangeSettings randomizedRangeSettings13 = (new RandomizedRangeSettings(this, "SideStopTicks", 3.0, 6.0, 0.0, 20.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition11 = () -> this.isSideStopTicksVisible();
      this.sideStopTicks = (RandomizedRangeSettings)randomizedRangeSettings13.withVisibility(condition11);
      RandomizedRangeSettings randomizedRangeSettings14 = (new RandomizedRangeSettings(this, "EmptySlotFailRate", 5.0, 10.0, 0.0, 100.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition12 = () -> {
         return !this.isDefaultMode();
      };
      this.emptySlotFailRate = (RandomizedRangeSettings)randomizedRangeSettings14.withVisibility(condition12);
      BooleanValue booleanValue = new BooleanValue(this, "RemoveEmptyRows", true);
      SettingVisibilityCondition condition13 = () -> this.isRemoveEmptyRowsVisible();
      this.removeEmptyRows = (BooleanValue)booleanValue.withVisibility(condition13);
      this.clickWindow = new BooleanValue(this, "ClickWindow", true);
      this.stealZeroStackSizeItems = new BooleanValue(this, "StealZeroStackSizeItems", false);
      this.intelligent = new BooleanValue(this, "Intelligent", true);
      BooleanValue booleanValue2 = new BooleanValue(this, "IgnoreBadPotions", false);
      BooleanValue intelligent2 = this.intelligent;
      this.intelligent.getClass();
      BooleanValue booleanValue3 = intelligent2;
      SettingVisibilityCondition condition14 = () -> booleanValue3.getValue();
      this.ignoreBadPotions = (BooleanValue)booleanValue2.withVisibility(condition14);
      DoubleValue doubleValue = new DoubleValue(this, "ItemBlackList");
      intelligent2 = this.intelligent;
      this.intelligent.getClass();
      BooleanValue booleanValue4 = intelligent2;
      SettingVisibilityCondition condition15 = () -> booleanValue4.getValue();
      this.itemBlackList = doubleValue.visibleWhen(condition15);
      BooleanValue booleanValue5 = new BooleanValue(this, "AutoSort", true);
      SettingVisibilityCondition condition16 = () -> this.isAutoSortVisible();
      this.autoSort = (BooleanValue)booleanValue5.withVisibility(condition16);
      this.autoClose = new BooleanValue(this, "AutoClose", true);
      BooleanValue booleanValue6 = new BooleanValue(this, "AutoCloseIfInvFull", false);
      intelligent2 = this.autoClose;
      this.autoClose.getClass();
      BooleanValue booleanValue7 = intelligent2;
      SettingVisibilityCondition condition17 = () -> booleanValue7.getValue();
      this.autoCloseIfInvFull = (BooleanValue)booleanValue6.withVisibility(condition17);
      RandomizedRangeSettings randomizedRangeSettings15 = (new RandomizedRangeSettings(this, "CloseDelay", 50.0, 50.0, 0.0, 1000.0, 0)).withoutNotInARow();
      intelligent2 = this.autoClose;
      this.autoClose.getClass();
      BooleanValue booleanValue8 = intelligent2;
      SettingVisibilityCondition condition18 = () -> booleanValue8.getValue();
      this.closeDelay = (RandomizedRangeSettings)randomizedRangeSettings15.withVisibility(condition18);
      RandomizedRangeSettings randomizedRangeSettings16 = (new RandomizedRangeSettings(this, "ChestOpenTime", 160.0, 260.0, 0.0, 1000.0, 0)).withoutNotInARow();
      intelligent2 = this.autoClose;
      this.autoClose.getClass();
      BooleanValue booleanValue9 = intelligent2;
      SettingVisibilityCondition condition19 = () -> booleanValue9.getValue();
      this.chestOpenTime = (RandomizedRangeSettings)randomizedRangeSettings16.withVisibility(condition19);
      BooleanValue booleanValue10 = new BooleanValue(this, "Visualize", true);
      SettingVisibilityCondition condition20 = () -> this.isVisualizeVisible();
      this.visualize = (BooleanValue)booleanValue10.withVisibility(condition20);
      this.whereToSteal = new BooleansSetting(this, "WhereToSteal", "ChestsOnly", new String[]{"Always", "ChestsOnly", "CustomChest"});
      BooleanValue booleanValue11 = new BooleanValue(this, "IgnoreEnderChests", false);
      SettingVisibilityCondition condition21 = () -> this.isIgnoreEnderChestsVisible();
      this.ignoreEnderChests = (BooleanValue)booleanValue11.withVisibility(condition21);
      BooleansSetting booleansSetting = new BooleansSetting(this, "Sequence", "Nearest", new String[]{"Basic", "Nearest", "Random"});
      SettingVisibilityCondition condition22 = () -> this.isDefaultMode();
      this.sequence = (BooleansSetting)booleansSetting.withVisibility(condition22);
      BooleansSetting booleansSetting2 = new BooleansSetting(this, "ClickMode", "Basic", new String[]{"Basic"});
      SettingVisibilityCondition condition23 = () -> this.isDefaultMode();
      this.clickMode = (BooleansSetting)booleansSetting2.withVisibility(condition23);
      this.toggleStorageESP = new BooleanValue(this, "ToggleStorageESP", false);
      this.toggleChestAura = new BooleanValue(this, "ToggleChestAura", false);
      this.chestStorage = new StringListSetting(this, "ChestStorage", new ArrayList());
      this.silent = new BooleanValue(this, "Silent", false);
      BooleanValue booleanValue12 = new BooleanValue(this, "StealIndicator", true);
      intelligent2 = this.silent;
      this.silent.getClass();
      BooleanValue booleanValue13 = intelligent2;
      SettingVisibilityCondition condition24 = () -> booleanValue13.getValue();
      this.stealIndicator = (BooleanValue)booleanValue12.withVisibility(condition24);
      ColorSetting colorSetting = new ColorSetting(this, "Color", Color.WHITE);
      SettingVisibilityCondition condition25 = () -> this.isIndicatorColorVisible();
      this.color2 = (ColorSetting)colorSetting.withVisibility(condition25);
      BooleanValue booleanValue14 = new BooleanValue(this, "BackGround", true);
      SettingVisibilityCondition condition26 = () -> this.isBackgroundVisible();
      this.backGround = (BooleanValue)booleanValue14.withVisibility(condition26);
      ColorSetting colorSetting2 = new ColorSetting(this, "BackGroundColor", new Color(0, 0, 0, 100));
      SettingVisibilityCondition condition27 = () -> {
         return this.silent.getValue() && this.stealIndicator.getValue() && this.backGround.getValue();
      };
      this.backgroundColor = (ColorSetting)colorSetting2.withVisibility(condition27);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      BlurShadowSettings blurShadowSettings3 = blurShadowSettings2.withCustomShadowColor(black);
      SettingVisibilityCondition condition28 = () -> {
         return this.silent.getValue() && this.stealIndicator.getValue();
      };
      this.blurShadowSettings = (BlurShadowSettings)blurShadowSettings3.withVisibility(condition28);
      this.wasChestOpen = false;
      this.nextTargetSlot = -1;
      this.ticksSinceLastClick = 0;
      this.startDelayTimer = new TimeHelper();
      this.closeDelayTimer = new TimeHelper();
      this.chestOpenTimer = new TimeHelper();
      this.doubleClickAccumulator = new ActionRateAccumulator(100, 0.0);
      this.failClickAccumulator = new ActionRateAccumulator(100, 0.0);
      this.missClickAccumulator = new ActionRateAccumulator(100, 0.0);
      this.cpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.cpsRecalculateCounter = new TickCounter();
      this.cpsNewRecalculateCounter = new TickCounter();
      this.secondaryTickCounter = new TickCounter();
      this.sideStopCounter = new TickCounter();
      this.cpsNewAccumulator = new ActionRateAccumulator(20, 0.0);
      this.doubleClickNewAccumulator = new ActionRateAccumulator(20, 0.0);
      this.emptySlotFailAccumulator = new ActionRateAccumulator(100, 0.0);
      this.failedEmptySlots = new ArrayList();
      this.visitedSlots = new ArrayList();
      this.bestArmorStacks = new ArrayList();
      this.cachedRandomSlotOrder = new ArrayList();
      DoubleValue itemBlackList2 = this.itemBlackList;
      BooleanValue intelligent3 = this.intelligent;
      this.intelligent.getClass();
      BooleanValue booleanValue15 = intelligent3;
      this.items = new ItemFilterSetting(this, "Items", itemBlackList2, () -> booleanValue15.getValue(), obj -> {
         return !this.isGearItem((Item)obj) && !(obj instanceof ItemPotion) && InventoryUtil.isCleanableItem((Item)obj);
      });
      this.lastRowIndex = 0;
      this.initialItemCount = 0;
      this.stolenItemCount = 0;
      this.wasChestOpenLastTick = false;
      this.startedStealing = false;
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.silent.getValue() && this.stealIndicator.getValue()) {
         if (mc.currentScreen instanceof GuiChest) {
            String str = "Stealing (" + this.stolenItemCount + "/" + this.initialItemCount + ")";
            boolean isBlurActive = this.blurShadowSettings.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
            BlurShadowManager.submitRenderTask(false, shaderRenderPass -> {
               int stringWidth = mc.fontRendererObj.getStringWidth(str);
               int n = sr.getScaledWidth() / 2 - stringWidth / 2;
               int n2 = sr.getScaledHeight() / 2 - 4;
               if (this.backGround.getValue()) {
                  byte b = 0;
                  int colorInt = this.backgroundColor.getColorInt(b);
                  int n3 = n - 2;
                  int n4 = n2 - 2;
                  int n5 = n + stringWidth + 1;
                  int n6 = n2 + 9;
                  BlurShadowSettings blurShadowSettings2 = this.blurShadowSettings;
                  int n7 = ColorUtil.resolvePassColor(shaderRenderPass, colorInt);
                  RenderUtil.drawRect3(n3, n4, n5, n6, blurShadowSettings2.resolvePassColor(shaderRenderPass, n7));
               }

               if (shaderRenderPass == ShaderRenderPass.NONE || !this.backGround.getValue() && shaderRenderPass == ShaderRenderPass.SHADOW) {
                  byte b2 = 0;
                  mc.fontRendererObj.drawString(str, n, n2, this.color2.getColorInt(b2));
               }
            });
         }

         RenderUtil.resetGlColorState();
      }
   }

   @Override
   public void onTick() {
      this.processChestSteal();
      this.cpsRecalculateCounter.increment();
      this.cpsNewRecalculateCounter.increment();
      this.secondaryTickCounter.increment();
      this.sideStopCounter.increment();
      if (mc.currentScreen instanceof GuiChest) {
         GuiChest currentScreen = (GuiChest)mc.currentScreen;
         ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(currentScreen.inventorySlots);
         int n = currentScreen.inventorySlots.inventorySlots.size() - 36;
         containerSlotQuery = containerSlotQuery.toSlot(n);
         boolean value = this.stealZeroStackSizeItems.getValue();
         long count = containerSlotQuery.nonEmptyOrNonNull(value).count();
         if (!this.wasChestOpenLastTick) {
            this.initialItemCount = (int)count;
         }

         this.stolenItemCount = Math.max(this.initialItemCount - (int)count, 0);
      }

      this.wasChestOpenLastTick = mc.currentScreen instanceof GuiChest;
   }

   private void processChestSteal() {
      this.handleChestOpenTransition();
      if (!(mc.currentScreen instanceof GuiChest)) {
         this.chestOpenTimer.reset();
         this.startDelayTimer.reset();
         this.closeDelayTimer.reset();
         this.nextTargetSlot = -1;
         this.resetClickTickCounter();
         this.startedStealing = false;
         this.bestArmorStacks.clear();
      } else {
         GuiChest currentScreen = (GuiChest)mc.currentScreen;
         if (this.shouldStealFromChest(currentScreen)) {
            TimeHelper startDelayTimer2 = this.startDelayTimer;
            long lastIntValue = (long)this.startDelay.getLastIntValue();
            if (startDelayTimer2.reached2(lastIntValue) && !mc.currentScreen.isAllowNativeMouseClick()) {
               if (mc.thePlayer.inventory.getItemStack() != null) {
                  return;
               }

               if (this.intelligent.getValue()) {
                  ArrayList list = new ArrayList();

                  for (Slot slot : (Iterable<Slot>)(currentScreen.inventorySlots.inventorySlots)) {
                     ItemStack stack = slot.getStack();
                     if (stack != null) {
                        Item item = stack.getItem();
                        if (item instanceof ItemArmor) {
                           list.add(stack);
                        }
                     }
                  }

                  for (int i = 0; i < 9; i++) {
                     ItemStack stack2 = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                     if (stack2 != null) {
                        Item item2 = stack2.getItem();
                        if (item2 instanceof ItemArmor) {
                           list.add(stack2);
                        }
                     }
                  }

                  boolean flag = ClientManagers.moduleManager.invManager.isToggled() && InvManagerActionOption.PUT_ON_ARMOR.isSelected() && ClientManagers.moduleManager.invManager.ignoreDurability.getValue();
                  this.bestArmorStacks = InventoryUtil.findBestArmorSetFromList(list, flag);
               }

               if (this.isDefaultMode()) {
                  this.stealDefault(currentScreen);
               } else {
                  this.stealExperimental(currentScreen);
               }

               return;
            }
         }
      }
   }

   private void stealExperimental(GuiChest guiChest) {
      Container inventorySlots = guiChest.inventorySlots;
      List list = InventoryUtil.sortSlotsSerpentine(this.collectExperimentalSlots(guiChest));
      Collections.reverse(list);
      ArrayList<Runnable> list2 = new ArrayList<>();
      ArrayList list3 = new ArrayList();

      for (int n : (Iterable<Integer>)(list)) {
         list2.add(() -> this.executeClickSlotExperimental(n, guiChest));
         list3.add(n);
      }

      if (!this.checkAutoClose(list2)) {
         this.startedStealing = true;
         ActionRateAccumulator cpsNewAccumulator2 = this.cpsNewAccumulator;
         int lastIntValue = this.cpsNew.getLastIntValue();
         int n2 = cpsNewAccumulator2.accumulate(lastIntValue);
         if (this.instant.getValue()) {
            list2.forEach(Runnable::run);
            this.updateCpsRecalculation();
         } else {
            while (n2 > 0 && !list2.isEmpty()) {
               int n3 = (Integer)list3.get(0);
               int n4 = (int)((double)n3 / 9.0);
               if (n4 != this.lastRowIndex) {
                  TickCounter sideStopCounter2 = this.sideStopCounter;
                  int lastIntValue2 = this.sideStopTicks.getLastIntValue();
                  if (!sideStopCounter2.hasReached2(lastIntValue2)) {
                     if (this.clickWindow.getValue()) {
                        RandomizedRangeSettings doubleClickRateNew2 = this.doubleClickRateNew;
                        ActionRateAccumulator actionRateAccumulator = this.doubleClickNewAccumulator;
                        if (this.rollAccumulator(actionRateAccumulator, doubleClickRateNew2)) {
                           mc.playerController.windowClick(guiChest.inventorySlots.windowId, -999, 0, 4, mc.thePlayer);
                        }
                     }

                     if (this.clickWindow.getValue()) {
                        mc.playerController.windowClick(guiChest.inventorySlots.windowId, -999, 0, 4, mc.thePlayer);
                     }

                     n2--;
                     continue;
                  }

                  this.sideStopTicks.nextValue();
               }

               this.lastRowIndex = n4;
               this.sideStopCounter.reset();
               if (this.failedEmptySlots.contains(n3)) {
                  list2.remove(0);
                  list3.remove(0);
                  this.visitedSlots.add(n3);
                  break;
               }

               ((Runnable)list2.get(0)).run();
               RandomizedRangeSettings doubleClickRateNew3 = this.doubleClickRateNew;
               ActionRateAccumulator actionRateAccumulator2 = this.doubleClickNewAccumulator;
               if (this.rollAccumulator(actionRateAccumulator2, doubleClickRateNew3)) {
                  ((Runnable)list2.get(0)).run();
               }

               ItemStack stack = inventorySlots.getSlot(n3).getStack();
               if (stack == null) {
                  this.visitedSlots.add(n3);
               }

               n2--;
               list2.remove(0);
               list3.remove(0);
            }

            this.updateCpsRecalculation();
         }
      }
   }

   private void stealDefault(GuiChest guiChest) {
      this.ticksSinceLastClick++;
      Container inventorySlots = guiChest.inventorySlots;
      List list;
      if (this.sequence.getSelectedValue().equals("Random") && !this.cachedRandomSlotOrder.isEmpty()) {
         list = this.cachedRandomSlotOrder;
      } else {
         List list2 = this.collectDefaultSlots(guiChest);
         String selectedValue = this.sequence.getSelectedValue();
         int containerGuiHeight = this.getContainerGuiHeight(inventorySlots);
         String str = selectedValue;
         List list3 = list2;
         list = InventoryUtil.sortSlotsByMode(inventorySlots, list3, str, containerGuiHeight);
      }

      ArrayList<Runnable> list4 = new ArrayList<>();
      ArrayList list5 = new ArrayList();

      for (int n : (Iterable<Integer>)(list)) {
         if (this.intelligent.getValue() && this.autoSort.getValue()) {
            ItemStack stack = inventorySlots.getSlot(n).getStack();
            String itemCategoryName = InventoryUtil.getItemCategoryName(stack);
            ArrayList hotbarSlotsForCategory = ClientManagers.moduleManager.invManager.getHotbarSlotsForCategory(itemCategoryName);
            if (!hotbarSlotsForCategory.isEmpty()) {
               int n2 = (Integer)hotbarSlotsForCategory.get(0);
               if (!itemCategoryName.equals("Sword") && !itemCategoryName.equals("Bow") && !itemCategoryName.equals("Rod")) {
                  ItemStack stack2 = mc.thePlayer.inventoryContainer.getSlot(n2 + 36).getStack();
                  String itemCategoryName2 = InventoryUtil.getItemCategoryName(stack2);
                  if (itemCategoryName2.equals(itemCategoryName)) {
                     list4.add(() -> {
                        this.clickSlotDefault(n, guiChest);
                     });
                     list5.add(n);
                     continue;
                  }
               }

               list4.add(() -> this.executeHotbarSwapSlot(n2, n, guiChest));
               list5.add(n);
               continue;
            }
         }

         list4.add(() -> {
            this.clickSlotDefault(n, guiChest);
         });
         list5.add(n);
      }

      if (!this.checkAutoClose(list4)) {
         this.startedStealing = true;
         ActionRateAccumulator cpsAccumulator2 = this.cpsAccumulator;
         RandomizedRangeSettings doubleClickRate2 = this.doubleClickRate;
         RandomizedRangeSettings failRate2 = this.failRate;
         RandomizedRangeSettings missClickRate2 = this.missClickRate;
         boolean value = this.instant.getValue();
         int lastIntValue = this.cps.getLastIntValue();
         int ticksSinceLastClick2 = this.ticksSinceLastClick;
         int n3 = lastIntValue;
         boolean flag = value;
         RandomizedRangeSettings randomizedRangeSettings = missClickRate2;
         RandomizedRangeSettings randomizedRangeSettings2 = failRate2;
         RandomizedRangeSettings randomizedRangeSettings3 = doubleClickRate2;
         ActionRateAccumulator actionRateAccumulator = cpsAccumulator2;
         this.executeStealActions(list4, list5, actionRateAccumulator, randomizedRangeSettings3, randomizedRangeSettings2, randomizedRangeSettings, inventorySlots, guiChest, flag, n3, ticksSinceLastClick2);
         this.updateCpsRecalculation();
      }
   }

   private boolean checkAutoClose(ArrayList list) {
      boolean isEmpty = list.isEmpty();
      if (this.autoClose.getValue() && this.autoCloseIfInvFull.getValue()) {
         boolean flag = true;

         for (int i = 9; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack == null) {
               flag = false;
               break;
            }
         }

         if (!isEmpty) {
            isEmpty = flag;
         }
      }

      if (isEmpty && this.autoClose.getValue() && this.startedStealing) {
         TimeHelper closeDelayTimer2 = this.closeDelayTimer;
         long lastIntValue = (long)this.closeDelay.getLastIntValue();
         if (closeDelayTimer2.reached2(lastIntValue)) {
            closeDelayTimer2 = this.chestOpenTimer;
            long lastIntValue2 = (long)this.chestOpenTime.getLastIntValue();
            if (closeDelayTimer2.reached2(lastIntValue2)) {
               mc.thePlayer.closeScreen();
            }
         }

         return true;
      } else {
         this.closeDelayTimer.reset();
         return false;
      }
   }

   private void executeStealActions(
      ArrayList list,
      ArrayList list2,
      ActionRateAccumulator actionRateAccumulator,
      RandomizedRangeSettings randomizedRangeSettings,
      RandomizedRangeSettings randomizedRangeSettings2,
      RandomizedRangeSettings randomizedRangeSettings3,
      Container container,
      GuiChest guiChest,
      boolean flag,
      int n,
      int n2
   ) {
      if (flag) {
         list.forEach(obj -> ((Runnable)obj).run());
      } else {
         if (!list2.isEmpty()) {
            this.nextTargetSlot = (Integer)list2.get(list2.size() > 1 ? 1 : 0);
         }

         ActionRateAccumulator failClickAccumulator2 = this.failClickAccumulator;
         if (!this.rollAccumulator(failClickAccumulator2, randomizedRangeSettings2)) {
            int n3 = actionRateAccumulator.accumulate(n);
            boolean flag2 = false;

            while (n3 > 0 && !list.isEmpty()) {
               if (this.tryMissClick(randomizedRangeSettings3, container, guiChest)) {
                  n3--;
               } else {
                  double d = (double)n2;
                  int n4 = (Integer)list2.get(0);
                  double d2 = d;
                  n3 = this.filterClicksByMouseSpeed(d2, container, n4, n3);
                  if (n3 <= 0) {
                     break;
                  }

                  Runnable runnable = (Runnable)list.get(0);
                  runnable.run();
                  flag2 = true;
                  ActionRateAccumulator doubleClickAccumulator2 = this.doubleClickAccumulator;
                  if (this.rollAccumulator(doubleClickAccumulator2, randomizedRangeSettings)) {
                     runnable.run();
                  }

                  list.remove(0);
                  list2.remove(0);
                  n3--;
               }
            }

            if (flag2 && this.sequence.getSelectedValue().equals("Random")) {
               List list3 = this.collectDefaultSlots(guiChest);
               String selectedValue = this.sequence.getSelectedValue();
               int containerGuiHeight = this.getContainerGuiHeight(container);
               String str = selectedValue;
               List list4 = list3;
               this.cachedRandomSlotOrder = InventoryUtil.sortSlotsByMode(container, list4, str, containerGuiHeight);
            }
         }
      }
   }

   private boolean tryMissClick(RandomizedRangeSettings randomizedRangeSettings, Container container, GuiChest guiChest) {
      if (InventoryUtil.getLastClickedSlot() > 0) {
         Slot slot = container.getSlot(InventoryUtil.getLastClickedSlot());
         int xDisplayPosition = slot.xDisplayPosition;
         int yDisplayPosition = slot.yDisplayPosition;
         double maxvalue = Double.MAX_VALUE;
         int n = -1;

         for (int i = 5; i < container.inventorySlots.size(); i++) {
            ItemStack stack = container.getSlot(i).getStack();
            if (stack == null && i != InventoryUtil.getLastClickedSlot() && i != n) {
               Slot slot2 = container.getSlot(i);
               int n2 = xDisplayPosition - slot2.xDisplayPosition;
               int n3 = yDisplayPosition - slot2.yDisplayPosition;
               double d = Math.sqrt((double)(n2 * n2 + n3 * n3));
               if (d < maxvalue) {
                  maxvalue = d;
                  n = i;
               }
            }
         }

         if (maxvalue < 36.0) {
            ActionRateAccumulator missClickAccumulator2 = this.missClickAccumulator;
            if (this.rollAccumulator(missClickAccumulator2, randomizedRangeSettings)) {
               this.clickSlotDefault(n, guiChest);
               return true;
            }
         }
      }

      return false;
   }

   private int filterClicksByMouseSpeed(double d, Container container, int n, int n2) {
      Slot slot = container.getSlot(n);
      ScaledResolution sr = new ScaledResolution(mc);
      int n3 = (sr.getScaledWidth() - 176) / 2;
      int n4 = (sr.getScaledHeight() - this.getContainerGuiHeight(container)) / 2;
      double d2 = Math.abs((double)(slot.xDisplayPosition + 2 + n3) - InventoryUtil.getLastCursorPos().x);
      double d3 = Math.abs((double)(slot.yDisplayPosition + 2 + n4) - InventoryUtil.getLastCursorPos().y);
      double d4 = Math.sqrt(d2 * d2 + d3 * d3);
      boolean isGuardActive = AntiSamplerGuard.isGuardActive(1);
      boolean flag = true;
      int n5 = (int)(d4 / (this.maxMouseMoveSpeed.getValue(flag) + (double)(isGuardActive ? 40 : 0)));
      if (n5 > 0) {
         n5 += this.mouseMoveRandom.getLastIntValue();
      }

      if (d > (double)n5) {
         if (d4 > 19.0) {
            this.mouseMoveRandom.nextValue();
         }

         return n2;
      } else {
         return 0;
      }
   }

   private boolean rollAccumulator(ActionRateAccumulator actionRateAccumulator, RandomizedRangeSettings randomizedRangeSettings) {
      if (randomizedRangeSettings == null) {
         return false;
      } else {
         int lastIntValue = randomizedRangeSettings.getLastIntValue();
         boolean flag = actionRateAccumulator.shouldTrigger(lastIntValue);
         if (flag) {
            randomizedRangeSettings.nextValue();
         }

         return flag;
      }
   }

   private ArrayList collectExperimentalSlots(GuiChest guiChest) {
      Container inventorySlots = guiChest.inventorySlots;
      ArrayList list = InventoryUtil.createSlotIndexRange(0, inventorySlots.inventorySlots.size() - 36);
      list.removeAll(this.visitedSlots);
      if (this.removeEmptyRows.getValue()) {
         ArrayList list2 = new ArrayList();
         ArrayList list3 = new ArrayList();

         for (int n : (Iterable<Integer>)(list)) {
            if (n % 9 == 0) {
               if (list3.size() == 9) {
                  list2.addAll(list3);
               }

               list3.clear();
            }

            ItemStack stack = guiChest.inventorySlots.getSlot(n).getStack();
            if (stack == null) {
               list3.add(n);
            }
         }

         list.removeAll(list2);
      }

      if (!this.intelligent.getValue()) {
         return list;
      } else {
         ArrayList list4 = new ArrayList();
         ArrayList list5;
         if (this.ignoreBadPotions.getValue()) {
            byte b = 0;
            list5 = InventoryUtil.getGoodPotionSlots(inventorySlots, b);
         } else {
            list5 = new ArrayList();
         }

         ArrayList list6 = list5;

         for (int n2 : (Iterable<Integer>)(list)) {
            ItemStack stack2 = inventorySlots.getSlot(n2).getStack();
            if (stack2 != null) {
               if (!this.isBestItemInChest(stack2, inventorySlots) || !this.isUpgradeOverInventory(stack2)) {
                  continue;
               }

               ItemFilterSetting items2 = this.items;
               Item item = stack2.getItem();
               Item item2 = item;
               if (items2.isItemAllowed(item2) && !list6.contains(n2)) {
                  continue;
               }
            }

            list4.add(n2);
         }

         return list4;
      }
   }

   private List collectDefaultSlots(GuiChest guiChest) {
      Container inventorySlots = guiChest.inventorySlots;
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(inventorySlots);
      int n = inventorySlots.inventorySlots.size() - 36;
      ContainerSlotQuery containerSlotQuery2 = containerSlotQuery.toSlot(n);
      boolean value = this.stealZeroStackSizeItems.getValue();
      List list = containerSlotQuery2.nonEmptyOrNonNull(value).toSlotIndexList();
      if (!this.intelligent.getValue()) {
         return list;
      } else {
         ArrayList list2 = new ArrayList();
         ArrayList list3;
         if (this.ignoreBadPotions.getValue()) {
            byte b = 0;
            list3 = InventoryUtil.getGoodPotionSlots(inventorySlots, b);
         } else {
            list3 = new ArrayList();
         }

         ArrayList list4 = list3;

         for (int n2 : (Iterable<Integer>)(list)) {
            ItemStack stack = inventorySlots.getSlot(n2).getStack();
            if (this.isBestItemInChest(stack, inventorySlots) && this.isUpgradeOverInventory(stack)) {
               ItemFilterSetting items2 = this.items;
               Item item = stack.getItem();
               Item item2 = item;
               if (!items2.isItemAllowed(item2) || list4.contains(n2)) {
                  list2.add(n2);
               }
            }
         }

         return list2;
      }
   }

   public boolean isBestItemInChest(ItemStack stack, Container container) {
      if (!this.isGearItemStack(stack) && this.isLicenseValid()) {
         return true;
      } else {
         for (int i = 0; i < container.inventorySlots.size() - 36; i++) {
            ItemStack stack2 = container.getSlot(i).getStack();
            if (stack2 != null) {
               Item item = stack.getItem();
               Item item2 = stack2.getItem();
               if (item instanceof ItemSword && item2 instanceof ItemSword && InventoryUtil.getItemScore(stack) < InventoryUtil.getItemScore(stack2)) {
                  return false;
               }

               if (item instanceof ItemBow && item2 instanceof ItemBow && InventoryUtil.getItemScore(stack) < InventoryUtil.getItemScore(stack2)) {
                  return false;
               }

               if (item instanceof ItemArmor && item2 instanceof ItemArmor && !this.bestArmorStacks.contains(stack)) {
                  return false;
               }

               if (item instanceof ItemFishingRod && item2 instanceof ItemFishingRod && InventoryUtil.getItemScore(stack) < InventoryUtil.getItemScore(stack2)) {
                  return false;
               }

               if (item instanceof ItemAxe && item2 instanceof ItemAxe) {
                  boolean flag = true;
                  double itemScore = InventoryUtil.getItemScore(stack, flag);
                  boolean flag2 = true;
                  if (itemScore < InventoryUtil.getItemScore(stack2, flag2)) {
                     return false;
                  }
               }

               if (item instanceof ItemPickaxe && item2 instanceof ItemPickaxe) {
                  boolean flag3 = true;
                  double itemScore2 = InventoryUtil.getItemScore(stack, flag3);
                  boolean flag4 = true;
                  if (itemScore2 < InventoryUtil.getItemScore(stack2, flag4)) {
                     return false;
                  }
               }

               if (item instanceof ItemSpade && item2 instanceof ItemSpade) {
                  boolean flag5 = true;
                  double itemScore3 = InventoryUtil.getItemScore(stack, flag5);
                  boolean flag6 = true;
                  if (itemScore3 < InventoryUtil.getItemScore(stack2, flag6)) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   public boolean isUpgradeOverInventory(ItemStack stack) {
      if (!this.isGearItemStack(stack)) {
         return true;
      } else {
         for (int i = 0; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
            ItemStack stack2 = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack2 != null) {
               Item item = stack2.getItem();
               Item item2 = stack.getItem();
               if (item2 instanceof ItemSword && item instanceof ItemSword && InventoryUtil.getItemScore(stack) <= InventoryUtil.getItemScore(stack2)) {
                  return false;
               }

               if (item2 instanceof ItemBow && item instanceof ItemBow && InventoryUtil.getItemScore(stack) <= InventoryUtil.getItemScore(stack2)) {
                  return false;
               }

               if (item2 instanceof ItemArmor
                  && item instanceof ItemArmor
                  && (
                     !this.bestArmorStacks.contains(stack)
                        || ((ItemArmor)item2).armorType == ((ItemArmor)item).armorType && InventoryUtil.getItemScore(stack2) == InventoryUtil.getItemScore(stack)
                  )) {
                  return false;
               }

               if (item2 instanceof ItemFishingRod && item instanceof ItemFishingRod && InventoryUtil.getItemScore(stack) <= InventoryUtil.getItemScore(stack2)) {
                  return false;
               }

               if (item2 instanceof ItemAxe && item instanceof ItemAxe) {
                  boolean flag = true;
                  double itemScore = InventoryUtil.getItemScore(stack, flag);
                  boolean flag2 = true;
                  if (itemScore < InventoryUtil.getItemScore(stack2, flag2)) {
                     return false;
                  }
               }

               if (item2 instanceof ItemPickaxe && item instanceof ItemPickaxe) {
                  boolean flag3 = true;
                  double itemScore2 = InventoryUtil.getItemScore(stack, flag3);
                  boolean flag4 = true;
                  if (itemScore2 < InventoryUtil.getItemScore(stack2, flag4)) {
                     return false;
                  }
               }

               if (item2 instanceof ItemSpade && item instanceof ItemSpade) {
                  boolean flag5 = true;
                  double itemScore3 = InventoryUtil.getItemScore(stack, flag5);
                  boolean flag6 = true;
                  if (itemScore3 < InventoryUtil.getItemScore(stack2, flag6)) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private boolean isGearItemStack(ItemStack stack) {
      Item item = stack.getItem();
      Item item2 = item;
      return this.isGearItem(item2);
   }

   private boolean isGearItem(Item item) {
      return item instanceof ItemSword
         || item instanceof ItemBow
         || item instanceof ItemArmor
         || item instanceof ItemAxe
         || item instanceof ItemPickaxe
         || item instanceof ItemSpade
         || item instanceof ItemFishingRod;
   }

   private void clickSlotExperimental(int n, GuiChest guiChest) {
      Slot slot = guiChest.inventorySlots.getSlot(n);
      Container inventorySlots = guiChest.inventorySlots;
      this.updateVirtualCursor(inventorySlots, n);

      try {
         guiChest.forceShiftClick = true;
         guiChest.mouseClicked(slot.xDisplayPosition + 2 + guiChest.guiLeft, slot.yDisplayPosition + 2 + guiChest.guiTop, 0);
         this.resetClickTickCounter();
      } catch (IOException e) {
      }
   }

   private void clickSlotDefault(int n, GuiChest guiChest) {
      Slot slot = guiChest.inventorySlots.getSlot(n);
      Container inventorySlots = guiChest.inventorySlots;
      this.updateVirtualCursor(inventorySlots, n);

      try {
         String selectedValue = this.clickMode.getSelectedValue();
         switch (selectedValue) {
            case "Basic":
               guiChest.forceShiftClick = true;
               guiChest.mouseClicked(slot.xDisplayPosition + 2 + guiChest.guiLeft, slot.yDisplayPosition + 2 + guiChest.guiTop, 0);
               break;
            case "Other":
               Augustus.getInstance().getClass();
               break;
            case "Exploit":
               Augustus.getInstance().getClass();
         }

         this.resetClickTickCounter();
      } catch (IOException e) {
      }
   }

   private void hotbarSwapSlot(int n, int n2, GuiChest guiChest) {
      ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(n + 36).getStack();
      if (stack != null) {
         stack.setSlotIndex(n2);
      }

      Container inventorySlots = guiChest.inventorySlots;
      this.updateVirtualCursor(inventorySlots, n2);
      String selectedValue = this.clickMode.getSelectedValue();
      switch (selectedValue) {
         case "Exploit":
            Augustus.getInstance().getClass();
            break;
         case "Basic":
         case "Other":
            mc.playerController.windowClick(guiChest.inventorySlots.windowId, n2, n, 2, mc.thePlayer);
      }

      this.resetClickTickCounter();
   }

   private void updateVirtualCursor(Container container, int n) {
      ScaledResolution sr = new ScaledResolution(mc);
      int n2 = (sr.getScaledWidth() - 176) / 2;
      int n3 = (sr.getScaledHeight() - this.getContainerGuiHeight(container)) / 2;
      Slot slot = container.getSlot(n);
      InventoryUtil.setLastCursorPos((double)(slot.xDisplayPosition + 2 + n2), (double)(slot.yDisplayPosition + 2 + n3));
      InventoryUtil.setLastClickedSlot(n);
   }

   private ArrayList findDestinationSlots(int n, Container container) {
      ItemStack stack = container.getSlot(n).getStack();
      if (stack == null) {
         return new ArrayList();
      } else {
         int stackSize = stack.stackSize;
         ArrayList list = new ArrayList();
         if (stack.getMaxStackSize() > 1) {
            for (TuplePair tuplePair : (Iterable<TuplePair>)(this.findPartialStackSlots(stack, container))) {
               stackSize -= ((Number)tuplePair.getSecond()).intValue();
               list.add(tuplePair.getFirst());
               if (stackSize <= 0) {
                  return list;
               }
            }
         }

         for (int i = container.inventorySlots.size() - 1; i >= container.inventorySlots.size() - 36; i--) {
            ItemStack stack2 = container.getSlot(i).getStack();
            if (stack2 == null) {
               list.add(i);
               break;
            }
         }

         return list;
      }
   }

   private ArrayList findPartialStackSlots(ItemStack stack, Container container) {
      if (stack.getMaxStackSize() <= 1) {
         return new ArrayList();
      } else {
         ArrayList list = new ArrayList();

         for (int i = container.inventorySlots.size() - 36; i < container.inventorySlots.size(); i++) {
            ItemStack stack2 = container.getSlot(i).getStack();
            if (stack2 != null && stack2.stackSize < stack2.getMaxStackSize()) {
               Item item = stack2.getItem();
               if (item.equals(stack.getItem())) {
                  list.add(new TuplePair(i, stack2.getMaxStackSize() - stack2.stackSize));
               }
            }
         }

         return list;
      }
   }

   private void updateCpsRecalculation() {
      TickCounter cpsRecalculateCounter2 = this.cpsRecalculateCounter;
      int lastIntValue = this.cpsRecalculate.getLastIntValue();
      if (cpsRecalculateCounter2.hasReached2(lastIntValue)) {
         this.cps.nextValue();
         this.cpsRecalculate.nextValue();
         this.cpsRecalculateCounter.reset();
      }

      cpsRecalculateCounter2 = this.cpsNewRecalculateCounter;
      int lastIntValue2 = this.cpsRecalculateNew.getLastIntValue();
      if (cpsRecalculateCounter2.hasReached2(lastIntValue2)) {
         this.cpsNew.nextValue();
         this.cpsRecalculateNew.nextValue();
         this.cpsNewRecalculateCounter.reset();
      }
   }

   private void resetRandomizers() {
      this.cps.nextValue();
      this.cpsRecalculate.nextValue();
      this.missClickRate.nextValue();
      this.doubleClickRate.nextValue();
      this.failRate.nextValue();
      this.startDelay.nextValue();
      this.closeDelay.nextValue();
      this.chestOpenTime.nextValue();
      this.cpsNew.nextValue();
      this.cpsRecalculateNew.nextValue();
      this.emptySlotFailRate.nextValue();
      this.sideStopTicks.nextValue();
      this.doubleClickRateNew.nextValue();
      this.nextTargetSlot = -1;
      this.lastRowIndex = 0;
      this.startedStealing = false;
      this.wasChestOpenLastTick = false;
      this.visitedSlots.clear();
      this.failedEmptySlots.clear();
      this.resetClickTickCounter();
   }

   public void resetClickTickCounter() {
      this.ticksSinceLastClick = 0;
   }

   private void toggleLinkedModules(boolean flag) {
      if (this.toggleStorageESP.getValue()) {
         ClientManagers.moduleManager.storageESP.setToggled(flag);
      }

      if (this.toggleChestAura.getValue()) {
         ClientManagers.moduleManager.chestAura.setToggled(flag);
      }
   }

   public int getNextTargetSlot() {
      return this.nextTargetSlot;
   }

   private int getContainerGuiHeight(Container container) {
      return 114 + (container.inventorySlots.size() - 36) / 9 * 18;
   }

   private void handleChestOpenTransition() {
      boolean flag = mc.currentScreen instanceof GuiChest;
      if (flag && !this.wasChestOpen) {
         this.startDelay.nextValue();
         this.closeDelay.nextValue();
         this.chestOpenTime.nextValue();
         this.visitedSlots.clear();
         if (!this.isDefaultMode()) {
            GuiChest currentScreen = (GuiChest)mc.currentScreen;
            List list = ContainerSlotQuery.of(currentScreen.inventorySlots).emptyOnly().toSlotIndexList();
            this.failedEmptySlots.clear();

            for (int n : (Iterable<Integer>)(list)) {
               RandomizedRangeSettings emptySlotFailRate2 = this.emptySlotFailRate;
               ActionRateAccumulator actionRateAccumulator = this.emptySlotFailAccumulator;
               if (this.rollAccumulator(actionRateAccumulator, emptySlotFailRate2)) {
                  this.failedEmptySlots.add(n);
               }
            }
         }
      }

      this.wasChestOpen = flag;
   }

   private boolean isDefaultMode() {
      return this.mode.getSelectedValue().equals("Default");
   }

   public boolean shouldStealFromChest(GuiChest guiChest) {
      String selectedValue = this.whereToSteal.getSelectedValue();
      boolean value = this.ignoreEnderChests.getValue();
      StringListSetting chestStorage2 = this.chestStorage;
      boolean flag = value;
      String str = selectedValue;
      return ContainerUtil.isValidChest(guiChest, str, flag, chestStorage2);
   }

   private void executeHotbarSwapSlot(int n, int n2, GuiChest guiChest) {
      this.hotbarSwapSlot(n, n2, guiChest);
   }

   private void executeClickSlotExperimental(int n, GuiChest guiChest) {
      this.clickSlotExperimental(n, guiChest);
   }

   private boolean isBackgroundVisible() {
      return this.silent.getValue() && this.stealIndicator.getValue();
   }

   private boolean isIndicatorColorVisible() {
      return this.silent.getValue() && this.stealIndicator.getValue();
   }

   private boolean isIgnoreEnderChestsVisible() {
      return !this.whereToSteal.getSelectedValue().equals("Always");
   }

   private boolean isVisualizeVisible() {
      return !this.silent.getValue();
   }

   private boolean isAutoSortVisible() {
      return this.intelligent.getValue()
         && Augustus.getInstance().getModuleManager() != null
         && Augustus.getInstance().getModuleManager().invManager != null
         && Augustus.getInstance().getModuleManager().invManager.isToggled()
         && InvManagerActionOption.SORT.isSelected();
   }

   private boolean isRemoveEmptyRowsVisible() {
      return !this.isDefaultMode();
   }

   private boolean isSideStopTicksVisible() {
      return !this.isDefaultMode();
   }

   private boolean isDoubleClickRateNewVisible() {
      return !this.isDefaultMode();
   }

   private boolean isCpsRecalculateNewVisible() {
      return !this.isDefaultMode();
   }

   private boolean isCpsNewVisible() {
      return !this.isDefaultMode();
   }

   private boolean isMissClickRateVisible() {
      return !this.instant.getValue() && this.isDefaultMode();
   }

   private boolean isFailRateVisible() {
      return !this.instant.getValue() && this.isDefaultMode();
   }

   private boolean isDoubleClickRateVisible() {
      return !this.instant.getValue() && this.isDefaultMode();
   }

   private boolean isCpsRecalculateVisible() {
      return !this.instant.getValue() && this.isDefaultMode();
   }

   private boolean isMaxMouseMoveSpeedVisible() {
      return !this.instant.getValue() && this.isDefaultMode();
   }

   private boolean isLicenseValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
