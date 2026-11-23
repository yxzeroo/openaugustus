package net.augustus.modules.player;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.ClientManagers;
import net.augustus.settings.StringListSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.ModeSetting;
import net.augustus.utils.math.Vec2d;
import net.augustus.utils.player.ContainerUtil;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.modules.modes.stealer.StealerNoneMouseMode;
import net.augustus.modules.modes.stealer.AbstractStealerMouseMode;
import net.augustus.modules.modes.stealer.StealerLinearMouseMode;
import net.augustus.modules.modes.stealer.StealerCubicMouseMode;
import net.augustus.modules.modes.stealer.StealerMouseSimulationMode;
import net.augustus.utils.math.DoubleSampleWindow;
import net.augustus.render.RenderUtil;
import net.augustus.settings.options.InvManagerActionOption;
import net.augustus.utils.TickCounter;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.Augustus;
import net.augustus.settings.ItemFilterSetting;
import net.augustus.utils.player.InventoryUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
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

public class Stealer extends Module implements TickListener, RenderListener {
   private final RandomizedRangeSettings startDelay;
   private final BooleanValue autoClose;
   private final RandomizedRangeSettings closeDelay;
   private final RandomizedRangeSettings chestOpenTime;
   private final ModeSetting mouseMode;
   private final BooleansSetting clickCalculationMode;
   private final DoubleValue clickSettings;
   private final RandomizedRangeSettings cps;
   private final RandomizedRangeSettings delayBeforeClick;
   private final RandomizedRangeSettings delayAfterClick;
   private final RandomizedRangeSettings misClickChance;
   private final RandomizedRangeSettings failRateChance;
   private final RandomizedRangeSettings failDuration;
   private final RandomizedRangeSettings doubleClickChance;
   private final BooleanValue stealOnlyOncePerTick;
   private final BooleanValue row;
   private final DoubleValue rowSettings;
   private final RandomizedRangeSettings rowCPS;
   private final RandomizedRangeSettings rowDelayBeforeClick;
   private final RandomizedRangeSettings rowDelayAfterClick;
   private final BooleanValue stealZeroStackSizeItems;
   private final BooleanValue stealAbilityCheck;
   private final BooleanValue intelligent;
   private final BooleanValue ignoreBadPotions;
   private final DoubleValue itemBlackList;
   private final BooleanValue autoSort;
   public final BooleansSetting whereToSteal;
   private final BooleanValue ignoreEnderChests;
   private final BooleansSetting sequence;
   private final BooleansSetting clickMode;
   private final BooleanValue toggleStorageESP;
   private final BooleanValue toggleChestAura;
   public final BooleanValue silent;
   public final BooleanValue visualize;
   private final BooleanValue stealIndicator;
   private final ColorSetting color2;
   private final BooleanValue backGround;
   private final ColorSetting backgroundColor;
   private final BlurShadowSettings blurShadowSettings;
   public final StringListSetting chestStorage;
   private final ItemFilterSetting items;
   private final TickCounter startDelayCounter;
   private final TickCounter closeDelayCounter;
   private final TickCounter chestOpenCounter;
   private final TickCounter afterClickCounter;
   private final TickCounter beforeClickCounter;
   private final TickCounter failDurationCounter;
   private final ActionRateAccumulator cpsAccumulator;
   private final ActionRateAccumulator misClickAccumulator;
   private final ActionRateAccumulator failRateAccumulator;
   private final ActionRateAccumulator doubleClickAccumulator;
   private final DoubleSampleWindow clickIntervalWindow;
   private long lastClickNanoTime;
   private boolean wasEmptyLastTick;
   private List targetSlots;
   private int initialSlotCount;
   private int stolenSlotCount;
   private boolean hadItemsOnOpen;
   private boolean clickedCurrentSlot;
   private boolean wasHoveringTargetSlot;
   private int completedClickCount;
   private boolean clickedThisTick;
   private boolean chestInitialized;

   public Stealer() {
       super("Stealer", "Dieb", Color.black, Categorys.PLAYER);
      this.startDelay = (new RandomizedRangeSettings(this, "StartDelay", 2.0, 4.0, 0.0, 20.0, 0)).withoutNotInARow();
      this.autoClose = new BooleanValue(this, "AutoClose", true);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "CloseDelay", 1.0, 2.0, 0.0, 20.0, 0)).withoutNotInARow();
      BooleanValue autoClose2 = this.autoClose;
      this.autoClose.getClass();
      BooleanValue booleanValue = autoClose2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.closeDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "ChestOpenTime", 3.0, 4.0, 0.0, 20.0, 0)).withoutNotInARow();
      autoClose2 = this.autoClose;
      this.autoClose.getClass();
      BooleanValue booleanValue2 = autoClose2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.chestOpenTime = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition2);
      BiFunction[] biFunctions = new BiFunction[]{
         (obj, obj2) -> new StealerNoneMouseMode((Stealer)obj, (DoubleValue)obj2),
         (obj3, obj4) -> new StealerLinearMouseMode((Stealer)obj3, (DoubleValue)obj4),
         (obj5, obj6) -> new StealerCubicMouseMode((Stealer)obj5, (DoubleValue)obj6),
         null
      };
      biFunctions[3] = (obj7, obj8) -> new StealerMouseSimulationMode((Stealer)obj7, (DoubleValue)obj8);
      BiFunction[] biFunctions2 = biFunctions;
      this.mouseMode = new ModeSetting(this, "MouseMode", "Linear", biFunctions2);
      this.clickCalculationMode = new BooleansSetting(this, "ClickCalculationMode", "Default", new String[]{"Instant", "Default", "Intave"});
      DoubleValue doubleValue = new DoubleValue(this, "ClickSettings", true);
      SettingVisibilityCondition condition3 = () -> this.isClickSettingsVisible();
      this.clickSettings = doubleValue.visibleWhen(condition3);
      RandomizedRangeSettings randomizedRangeSettings3 = new RandomizedRangeSettings(this, "CPS", 8.0, 12.0, 1.0, 20.0, 0, this.clickSettings);
      SettingVisibilityCondition condition4 = () -> this.isDefaultClickMode();
      this.cps = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings4 = new RandomizedRangeSettings(this, "DelayBeforeClick", 0.0, 2.0, 0.0, 4.0, 0, this.clickSettings);
      SettingVisibilityCondition condition5 = () -> this.isDefaultClickMode();
      this.delayBeforeClick = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition5);
      RandomizedRangeSettings randomizedRangeSettings5 = new RandomizedRangeSettings(this, "DelayAfterClick", 0.0, 2.0, 0.0, 4.0, 0, this.clickSettings);
      SettingVisibilityCondition condition6 = () -> this.isDefaultClickMode();
      this.delayAfterClick = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition6);
      RandomizedRangeSettings randomizedRangeSettings6 = (new RandomizedRangeSettings(this, "MisClickChance", 0.0, 0.0, 0.0, 100.0, 0, this.clickSettings)).withoutNotInARow();
      SettingVisibilityCondition condition7 = () -> this.isDefaultClickMode();
      this.misClickChance = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition7);
      RandomizedRangeSettings randomizedRangeSettings7 = (new RandomizedRangeSettings(this, "FailRateChance", 5.0, 10.0, 0.0, 100.0, 0, this.clickSettings)).withoutNotInARow();
      SettingVisibilityCondition condition8 = () -> this.isDefaultClickMode();
      this.failRateChance = (RandomizedRangeSettings)randomizedRangeSettings7.withVisibility(condition8);
      RandomizedRangeSettings randomizedRangeSettings8 = new RandomizedRangeSettings(this, "FailDuration", 2.0, 3.0, 0.0, 5.0, 0, this.clickSettings);
      SettingVisibilityCondition condition9 = () -> this.isFailDurationVisible();
      RandomizedRangeSettings randomizedRangeSettings9 = (RandomizedRangeSettings)randomizedRangeSettings8.withVisibility(condition9);
      SettingVisibilityCondition condition10 = () -> this.isDefaultClickMode();
      this.failDuration = (RandomizedRangeSettings)randomizedRangeSettings9.withVisibility(condition10);
      RandomizedRangeSettings randomizedRangeSettings10 = (new RandomizedRangeSettings(this, "DoubleClickChance", 50.0, 70.0, 0.0, 100.0, 0, this.clickSettings)).withoutNotInARow();
      SettingVisibilityCondition condition11 = () -> this.isDefaultClickMode();
      this.doubleClickChance = (RandomizedRangeSettings)randomizedRangeSettings10.withVisibility(condition11);
      BooleanValue booleanValue3 = new BooleanValue(this, "StealOnlyOncePerTick", false, this.clickSettings);
      SettingVisibilityCondition condition12 = () -> this.isDefaultClickMode();
      this.stealOnlyOncePerTick = (BooleanValue)booleanValue3.withVisibility(condition12);
      BooleanValue booleanValue4 = new BooleanValue(this, "Row", true, this.clickSettings);
      SettingVisibilityCondition condition13 = () -> this.isDefaultClickMode();
      this.row = (BooleanValue)booleanValue4.withVisibility(condition13);
      DoubleValue doubleValue2 = new DoubleValue(this, "RowSettings", true, this.clickSettings);
      autoClose2 = this.row;
      this.row.getClass();
      BooleanValue booleanValue5 = autoClose2;
      SettingVisibilityCondition condition14 = () -> booleanValue5.getValue();
      DoubleValue doubleValue3 = doubleValue2.visibleWhen(condition14);
      SettingVisibilityCondition condition15 = () -> this.isRowSettingsVisible();
      this.rowSettings = doubleValue3.visibleWhen(condition15);
      RandomizedRangeSettings randomizedRangeSettings11 = new RandomizedRangeSettings(this, "RowCPS", 8.0, 12.0, 1.0, 20.0, 0, this.rowSettings);
      SettingVisibilityCondition condition16 = () -> this.isDefaultClickMode();
      this.rowCPS = (RandomizedRangeSettings)randomizedRangeSettings11.withVisibility(condition16);
      RandomizedRangeSettings randomizedRangeSettings12 = new RandomizedRangeSettings(this, "RowDelayBeforeClick", 0.0, 2.0, 0.0, 4.0, 0, this.rowSettings);
      SettingVisibilityCondition condition17 = () -> this.isDefaultClickMode();
      this.rowDelayBeforeClick = (RandomizedRangeSettings)randomizedRangeSettings12.withVisibility(condition17);
      RandomizedRangeSettings randomizedRangeSettings13 = new RandomizedRangeSettings(this, "RowDelayAfterClick", 0.0, 2.0, 0.0, 4.0, 0, this.rowSettings);
      SettingVisibilityCondition condition18 = () -> this.isDefaultClickMode();
      this.rowDelayAfterClick = (RandomizedRangeSettings)randomizedRangeSettings13.withVisibility(condition18);
      this.stealZeroStackSizeItems = new BooleanValue(this, "StealZeroStackSizeItems", false);
      this.stealAbilityCheck = new BooleanValue(this, "StealAbilityCheck", true);
      this.intelligent = new BooleanValue(this, "Intelligent", true);
      autoClose2 = new BooleanValue(this, "IgnoreBadPotions", false);
      BooleanValue intelligent2 = this.intelligent;
      this.intelligent.getClass();
      BooleanValue booleanValue6 = intelligent2;
      SettingVisibilityCondition condition19 = () -> booleanValue6.getValue();
      this.ignoreBadPotions = (BooleanValue)autoClose2.withVisibility(condition19);
      DoubleValue doubleValue4 = new DoubleValue(this, "ItemBlackList");
      intelligent2 = this.intelligent;
      this.intelligent.getClass();
      BooleanValue booleanValue7 = intelligent2;
      SettingVisibilityCondition condition20 = () -> booleanValue7.getValue();
      this.itemBlackList = doubleValue4.visibleWhen(condition20);
      autoClose2 = new BooleanValue(this, "AutoSort", true);
      SettingVisibilityCondition condition21 = () -> this.isAutoSortVisible();
      this.autoSort = (BooleanValue)autoClose2.withVisibility(condition21);
      this.whereToSteal = new BooleansSetting(this, "WhereToSteal", "ChestsOnly", new String[]{"Always", "ChestsOnly", "CustomChest"});
      autoClose2 = new BooleanValue(this, "IgnoreEnderChests", false);
      SettingVisibilityCondition condition22 = () -> this.isIgnoreEnderChestsVisible();
      this.ignoreEnderChests = (BooleanValue)autoClose2.withVisibility(condition22);
      this.sequence = new BooleansSetting(this, "Sequence", "Nearest", new String[]{"Basic", "Nearest", "Random"});
      this.clickMode = new BooleansSetting(this, "ClickMode", "Basic", new String[]{"Basic"});
      this.toggleStorageESP = new BooleanValue(this, "ToggleStorageESP", false);
      this.toggleChestAura = new BooleanValue(this, "ToggleChestAura", false);
      this.silent = (BooleanValue)(new BooleanValue(this, "Silent", false)).markVisualOnly();
      autoClose2 = new BooleanValue(this, "Visualize", true);
      SettingVisibilityCondition condition23 = () -> this.isVisualizeVisible();
      this.visualize = (BooleanValue)((BooleanValue)autoClose2.withVisibility(condition23)).markVisualOnly();
      autoClose2 = new BooleanValue(this, "StealIndicator", true);
      intelligent2 = this.silent;
      this.silent.getClass();
      BooleanValue booleanValue8 = intelligent2;
      SettingVisibilityCondition condition24 = () -> booleanValue8.getValue();
      this.stealIndicator = (BooleanValue)((BooleanValue)autoClose2.withVisibility(condition24)).markVisualOnly();
      ColorSetting colorSetting = new ColorSetting(this, "Color", Color.WHITE);
      SettingVisibilityCondition condition25 = () -> this.isIndicatorColorVisible();
      this.color2 = (ColorSetting)((ColorSetting)colorSetting.withVisibility(condition25)).markVisualOnly();
      autoClose2 = new BooleanValue(this, "BackGround", true);
      SettingVisibilityCondition condition26 = () -> this.isBackgroundVisible();
      this.backGround = (BooleanValue)((BooleanValue)autoClose2.withVisibility(condition26)).markVisualOnly();
      ColorSetting colorSetting2 = new ColorSetting(this, "BackGroundColor", new Color(0, 0, 0, 100));
      SettingVisibilityCondition condition27 = () -> this.isBackgroundColorVisible();
      this.backgroundColor = (ColorSetting)((ColorSetting)colorSetting2.withVisibility(condition27)).markVisualOnly();
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      BlurShadowSettings blurShadowSettings3 = blurShadowSettings2.withCustomShadowColor(black);
      SettingVisibilityCondition condition28 = () -> {
         return this.silent.getValue() && this.stealIndicator.getValue();
      };
      this.blurShadowSettings = (BlurShadowSettings)((BlurShadowSettings)blurShadowSettings3.withVisibility(condition28)).markAllVisualOnly();
      StringListSetting stringListSetting = new StringListSetting(this, "ChestStorage", new ArrayList());
      SettingVisibilityCondition condition29 = () -> false;
      this.chestStorage = (StringListSetting)stringListSetting.withVisibility(condition29);
      DoubleValue itemBlackList2 = this.itemBlackList;
      BooleanValue intelligent3 = this.intelligent;
      this.intelligent.getClass();
      BooleanValue booleanValue9 = intelligent3;
      SettingVisibilityCondition condition30 = () -> booleanValue9.getValue();
      this.items = new ItemFilterSetting(this, "Items", itemBlackList2, condition30, obj9 -> {
         return !this.isGearItem((Item)obj9) && !(obj9 instanceof ItemPotion) && InventoryUtil.isCleanableItem((Item)obj9);
      });
      this.startDelayCounter = new TickCounter();
      this.closeDelayCounter = new TickCounter();
      this.chestOpenCounter = new TickCounter();
      this.afterClickCounter = new TickCounter();
      this.beforeClickCounter = new TickCounter();
      this.failDurationCounter = new TickCounter();
      byte b = 60;
      byte b2 = 0;
      this.cpsAccumulator = new ActionRateAccumulator(60, (double)DefaultRandomGenerator.instance.nextInt2(b2, b));
      byte b3 = 100;
      byte b4 = 0;
      this.misClickAccumulator = new ActionRateAccumulator(300, (double)DefaultRandomGenerator.instance.nextInt2(b4, b3));
      byte b5 = 100;
      byte b6 = 0;
      this.failRateAccumulator = new ActionRateAccumulator(100, (double)DefaultRandomGenerator.instance.nextInt2(b6, b5));
      byte b7 = 100;
      byte b8 = 0;
      this.doubleClickAccumulator = new ActionRateAccumulator(100, (double)DefaultRandomGenerator.instance.nextInt2(b8, b7));
      this.clickIntervalWindow = new DoubleSampleWindow(30);
      this.lastClickNanoTime = System.currentTimeMillis();
      this.wasEmptyLastTick = false;
      this.targetSlots = new ArrayList();
      this.initialSlotCount = 0;
      this.stolenSlotCount = 0;
      this.hadItemsOnOpen = false;
      this.clickedCurrentSlot = false;
      this.wasHoveringTargetSlot = false;
      this.completedClickCount = 0;
      this.clickedThisTick = false;
      this.chestInitialized = false;
   }

   @Override
   public void onEnable() {
      boolean flag = true;
      this.toggleLinkedModules(flag);
      this.chestInitialized = false;
      this.completedClickCount = 0;
   }

   @Override
   public void onDisable() {
      boolean flag = false;
      this.toggleLinkedModules(flag);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 32;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.silent.getValue() && this.stealIndicator.getValue() && ContainerUtil.isChestOpen()) {
         String str = "Stealing (" + this.stolenSlotCount + "/" + this.initialSlotCount + ")";
         boolean isBlurActive = this.blurShadowSettings.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderStealIndicator(str, sr, shaderRenderPass));
         RenderUtil.resetGlColorState();
      }
   }

   private boolean isValidChestOpen() {
      if (ContainerUtil.isChestOpen()) {
         GuiChest currentScreen = (GuiChest)mc.currentScreen;
         if (this.shouldStealFromChest(currentScreen)) {
            Container inventorySlots = ((GuiChest)mc.currentScreen).inventorySlots;
            return inventorySlots.isReceivedItems();
         }
      }

      return false;
   }

   @Override
   public void onTick() {
      if (this.isValidChestOpen()) {
         Container inventorySlots = ((GuiChest)mc.currentScreen).inventorySlots;
         if (!this.chestInitialized) {
            this.refreshTargetSlots(inventorySlots);
            this.initialSlotCount = this.targetSlots.size();
            this.hadItemsOnOpen = this.hasTargetSlots();
            this.chestOpenCounter.reset();
            this.startDelayCounter.reset();
            this.startDelay.nextValue();
            ((AbstractStealerMouseMode)this.mouseMode.getAbstractSubMode()).resetCursorPosition();
            this.chestInitialized = true;
            return;
         }

         if (this.checkAutoClose()) {
            return;
         }

         try {
            GuiChest currentScreen = (GuiChest)mc.currentScreen;
            this.processChestSteal(currentScreen);
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      } else {
         this.chestInitialized = false;
         this.completedClickCount = 0;
      }

      this.incrementTickCounters();
   }

   private boolean checkAutoClose() {
      if (this.autoClose.getValue()) {
         if (!this.hasTargetSlots()) {
            if (this.hadItemsOnOpen) {
               if (!this.wasEmptyLastTick) {
                  this.closeDelayCounter.reset();
               }

               TickCounter closeDelayCounter2 = this.closeDelayCounter;
               int lastIntValue = this.closeDelay.getLastIntValue();
               if (closeDelayCounter2.hasReached2(lastIntValue)) {
                  mc.thePlayer.closeScreen();
                  this.closeDelay.nextValue();
                  return true;
               }
            } else {
               TickCounter chestOpenCounter2 = this.chestOpenCounter;
               int lastIntValue2 = this.chestOpenTime.getLastIntValue();
               if (chestOpenCounter2.hasReached2(lastIntValue2)) {
                  mc.thePlayer.closeScreen();
                  this.chestOpenTime.nextValue();
                  return true;
               }
            }

            return false;
         }
      }

      return false;
   }

   private void processChestSteal(GuiChest guiChest) throws IOException {
      TickCounter startDelayCounter2 = this.startDelayCounter;
      int lastIntValue = this.startDelay.getLastIntValue();
      if (startDelayCounter2.hasReached2(lastIntValue)) {
         Container inventorySlots = guiChest.inventorySlots;
         this.wasEmptyLastTick = !this.hasTargetSlots();
         AtomicBoolean atomicBoolean = new AtomicBoolean(false);
         this.clickedThisTick = false;
         if (!this.stealInstantly(inventorySlots)) {
            for (int i = 0; i < 3; i++) {
               if (!this.hasTargetSlots() || this.mouseMode.getAbstractSubMode() == null) {
                  return;
               }

               Slot slot = (Slot)this.targetSlots.get(0);
               AbstractStealerMouseMode abstractSubMode = (AbstractStealerMouseMode)this.mouseMode.getAbstractSubMode();
               List targetSlots2 = this.targetSlots;
               Vec2d vec2d = abstractSubMode.computeNextCursorPos(inventorySlots, targetSlots2);
               int n = (int)Math.round(vec2d.x);
               int n2 = (int)Math.round(vec2d.y);
               boolean isMouseOverSlot = guiChest.isMouseOverSlot(slot, n, n2);
               if (isMouseOverSlot) {
                  List targetSlots3 = this.targetSlots;
                  boolean flag = this.stealSlot(inventorySlots, slot, targetSlots3);
                  if (flag) {
                     this.refreshTargetSlots(inventorySlots);
                     ((AbstractStealerMouseMode)this.mouseMode.getAbstractSubMode()).onTargetSlotChanged();
                     this.cps.nextValue();
                     this.rowCPS.nextValue();
                     this.completedClickCount++;
                     this.clickedThisTick = true;
                  }
               } else {
                  if (this.misClickChance.getMaxValue() > 0.0 && !atomicBoolean.get()) {
                     List list = ContainerSlotQuery.of(inventorySlots).emptyOnly().toSlotList();
                     Stream stream = list.stream();
                     stream.filter(obj -> {
                        return guiChest.isMouseOverSlot((Slot)obj, n, n2) && ((Slot)obj).getDistanceTo(slot) < 25.0 && ((Slot)obj).slotNumber != InventoryUtil.getLastClickedSlot();
                     }).findFirst().ifPresent(obj2 -> this.tryMisClickSlot(inventorySlots, atomicBoolean, (Slot)obj2));
                  }
               }

               this.wasHoveringTargetSlot = isMouseOverSlot;
            }
         }
      }
   }

   private boolean stealInstantly(Container container) {
      if (!this.clickCalculationMode.getSelectedValue().equals("Instant")) {
         return false;
      } else {
         this.refreshTargetSlots(container);

         for (Slot slot : (Iterable<Slot>)(this.targetSlots)) {
            List targetSlots2 = this.targetSlots;
            this.stealSlot(container, slot, targetSlots2);
            this.refreshTargetSlots(container);
         }

         return true;
      }
   }

   private boolean executeClickInstant(List list, Runnable runnable) {
      runnable.run();
      return true;
   }

   private boolean executeClickAction(List list, Runnable runnable) {
      String selectedValue = this.clickCalculationMode.getSelectedValue();
      switch (selectedValue) {
         case "Default":
            return this.executeClickDefault(list, runnable);
         case "Instant":
            return this.executeClickInstant(list, runnable);
         case "Intave":
            return this.executeClickIntave(list, runnable);
         default:
            return false;
      }
   }

   private boolean executeClickIntave(List list, Runnable runnable) {
      if (!this.wasHoveringTargetSlot) {
         this.beforeClickCounter.reset();
      }

      int n = this.computeOptimalIntaveDelay();
      if (this.beforeClickCounter.hasReached2(n) && !this.clickedThisTick) {
         if (!this.clickedCurrentSlot) {
            runnable.run();
            if (this.completedClickCount > 0) {
               double d = (double)(System.nanoTime() - this.lastClickNanoTime) / 1.0E9;
               this.clickIntervalWindow.add(d);
            }

            this.lastClickNanoTime = System.nanoTime();
         }

         this.clickedCurrentSlot = true;
         this.clickedThisTick = true;
      }

      return this.beforeClickCounter.hasReached2(n) && this.clickedCurrentSlot;
   }

   private boolean executeClickDefault(List list, Runnable runnable) {
      boolean isConsecutiveRow = this.isConsecutiveRow(list);
      if (!this.wasHoveringTargetSlot) {
         this.rerollDelayBeforeClick(isConsecutiveRow);
         this.beforeClickCounter.reset();
      }

      TickCounter beforeClickCounter2 = this.beforeClickCounter;
      int delayBeforeClick2 = this.getDelayBeforeClick(isConsecutiveRow);
      if (beforeClickCounter2.hasReached2(delayBeforeClick2)) {
         ActionRateAccumulator cpsAccumulator2 = this.cpsAccumulator;
         int effectiveCps = this.getEffectiveCps(isConsecutiveRow);
         if (cpsAccumulator2.shouldTrigger(effectiveCps) && (!this.stealOnlyOncePerTick.getValue() || !this.clickedThisTick)) {
            if (!this.clickedCurrentSlot) {
               ActionRateAccumulator failRateAccumulator2 = this.failRateAccumulator;
               int lastIntValue = this.failRateChance.getLastIntValue();
               if (failRateAccumulator2.shouldTrigger(lastIntValue)) {
                  this.failDurationCounter.reset();
                  this.failRateChance.nextValue();
                  this.failDuration.nextValue();
               }
            }

            beforeClickCounter2 = this.failDurationCounter;
            int lastIntValue2 = this.failDuration.getLastIntValue();
            if (beforeClickCounter2.hasReached2(lastIntValue2)) {
               if (!this.clickedCurrentSlot) {
                  runnable.run();
               } else {
                  ActionRateAccumulator doubleClickAccumulator2 = this.doubleClickAccumulator;
                  int lastIntValue3 = this.doubleClickChance.getLastIntValue();
                  if (doubleClickAccumulator2.shouldTrigger(lastIntValue3)) {
                     runnable.run();
                     this.doubleClickChance.nextValue();
                  }
               }

               this.clickedCurrentSlot = true;
            }
         }
      }

      if (!this.clickedCurrentSlot) {
         this.afterClickCounter.reset();
         this.rerollDelayAfterClick(isConsecutiveRow);
      }

      beforeClickCounter2 = this.afterClickCounter;
      int delayAfterClick2 = this.getDelayAfterClick(isConsecutiveRow);
      if (beforeClickCounter2.hasReached2(delayAfterClick2)) {
         beforeClickCounter2 = this.beforeClickCounter;
         int delayBeforeClick3 = this.getDelayBeforeClick(isConsecutiveRow);
         if (beforeClickCounter2.hasReached2(delayBeforeClick3) && this.clickedCurrentSlot) {
            return true;
         }
      }

      return false;
   }

   private void rerollDelayAfterClick(boolean flag) {
      if (flag) {
         this.rowDelayAfterClick.nextValue();
      } else {
         this.delayAfterClick.nextValue();
      }
   }

   private void rerollDelayBeforeClick(boolean flag) {
      if (flag) {
         this.rowDelayBeforeClick.nextValue();
      } else {
         this.delayBeforeClick.nextValue();
      }
   }

   private int computeOptimalIntaveDelay() {
      int n = 0;
      double d = 0.0;

      for (int i = 2; i < 6; i++) {
         long l = System.nanoTime() + Math.max((long)i - this.beforeClickCounter.getTicks(), 0L) * 50000000L;
         double d2 = (double)(l - this.lastClickNanoTime) / 1.0E9;
         ArrayList list = new ArrayList(this.clickIntervalWindow.getSamples());
         list.add(d2);
         double d3 = this.calculateVariance(list);
         if (d3 > d) {
            d = d3;
            n = i;
         }
      }

      return n;
   }

   private int getDelayAfterClick(boolean flag) {
      int n;
      if (flag) {
         n = this.rowDelayAfterClick.nextInt();
      } else {
         n = this.delayAfterClick.nextInt();
      }

      return n;
   }

   private int getDelayBeforeClick(boolean flag) {
      int n;
      if (flag) {
         n = this.rowDelayBeforeClick.nextInt();
      } else {
         n = this.delayBeforeClick.nextInt();
      }

      return n;
   }

   private double calculateVariance(List list) {
      double d = 0.0;

      for (double d2 : (Iterable<Double>)(list)) {
         d += d2;
      }

      double d3 = d / (double)list.size();
      double d4 = 0.0;

      for (double d5 : (Iterable<Double>)(list)) {
         d4 += Math.pow(d5 - d3, 2.0);
      }

      return d4 / (double)list.size();
   }

   private int getEffectiveCps(boolean flag) {
      int n;
      if (flag) {
         n = this.rowCPS.getLastIntValue();
      } else {
         n = this.cps.nextInt();
      }

      return n * (this.stealOnlyOncePerTick.getValue() ? 1 : 3);
   }

   private boolean isConsecutiveRow(List list) {
      if (list.size() > 2 && this.row.getValue()) {
         boolean flag = false;

         for (int i = 0; i < 2; i++) {
            Slot slot = (Slot)list.get(i);
            Slot slot2 = (Slot)list.get(i + 1);
            int n = Math.abs(slot.xDisplayPosition - slot2.xDisplayPosition);
            int n2 = Math.abs(slot.yDisplayPosition - slot2.yDisplayPosition);
            if (n != 0 && n2 != 0) {
               return false;
            }

            if (i == 0) {
               flag = n != 0;
            } else if (flag) {
               if (n2 != 0) {
                  return false;
               }
            } else if (n != 0) {
               return false;
            }

            if (n > 19 || n2 > 19) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private void refreshTargetSlots(Container container) {
      this.targetSlots = this.collectOrderedTargetSlots(container);
      this.stolenSlotCount = Math.max(this.initialSlotCount - this.targetSlots.size(), 0);
      this.clickedCurrentSlot = false;
   }

   private boolean stealSlot(Container container, Slot slot, List list) {
      if (this.intelligent.getValue() && this.autoSort.getValue()) {
         ItemStack stack = slot.getStack();
         String itemCategoryName = InventoryUtil.getItemCategoryName(stack);
         ArrayList hotbarSlotsForCategory = ClientManagers.moduleManager.invManager.getHotbarSlotsForCategory(itemCategoryName);
         if (!hotbarSlotsForCategory.isEmpty()) {
            ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
            byte b = 9;
            if (containerSlotQuery.fromSlot(b).hasAnyEmptySlot()) {
               int n = (Integer)hotbarSlotsForCategory.get(0);
               if (!itemCategoryName.equals("Sword") && !itemCategoryName.equals("Bow") && !itemCategoryName.equals("Rod")) {
                  ItemStack stack2 = mc.thePlayer.inventoryContainer.getSlot(n + 36).getStack();
                  String itemCategoryName2 = InventoryUtil.getItemCategoryName(stack2);
                  if (itemCategoryName2.equals(itemCategoryName)) {
                     Runnable runnable = () -> this.clickOverflowSortSlot(container, slot);
                     return this.executeClickAction(list, runnable);
                  }
               }

               Runnable runnable2 = () -> {
                  String selectedValue = this.clickMode.getSelectedValue();
                  ContainerUtil.swapHotbarSlot(container, selectedValue, slot, n);
               };
               return this.executeClickAction(list, runnable2);
            }
         }
      }

      Runnable runnable3 = () -> this.clickTargetSlot(container, slot);
      return this.executeClickAction(list, runnable3);
   }

   private List collectOrderedTargetSlots(Container container) {
      List list = this.collectOrderedTargetSlotIndices(container);
      ArrayList list2 = new ArrayList();

      for (Integer num : (Iterable<Integer>)(list)) {
         Slot slot = container.getSlot(num);
         list2.add(slot);
      }

      return list2;
   }

   private List collectOrderedTargetSlotIndices(Container container) {
      List list = this.collectStealableSlotIndices(container);
      String selectedValue = this.sequence.getSelectedValue();
      int containerGuiHeight = InventoryUtil.getContainerGuiHeight(container);
      String str = selectedValue;
      List list2 = list;
      return InventoryUtil.sortSlotsByMode(container, list2, str, containerGuiHeight);
   }

   private List collectStealableSlotIndices(Container container) {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(container);
      int n = container.inventorySlots.size() - 36;
      containerSlotQuery = containerSlotQuery.toSlot(n);
      boolean value = this.stealZeroStackSizeItems.getValue();
      List list = containerSlotQuery.nonEmptyOrNonNull(value).toSlotIndexList();
      if (this.stealAbilityCheck.getValue()) {
         for (int i = list.size() - 1; i >= 0; i--) {
            Slot slot = container.getSlot((Integer)list.get(i));
            Slot slot2 = slot;
            if (!this.canFitInInventory(slot2)) {
               list.remove(i);
            }
         }
      }

      if (!this.intelligent.getValue()) {
         return list;
      } else {
         List list2 = this.collectBestArmorStacks(container);
         ArrayList list3 = new ArrayList();
         ArrayList list4;
         if (this.ignoreBadPotions.getValue()) {
            byte b = 0;
            list4 = InventoryUtil.getGoodPotionSlots(container, b);
         } else {
            list4 = new ArrayList();
         }

         ArrayList list5 = list4;

         for (int n2 : (Iterable<Integer>)(list)) {
            Slot slot3 = container.getSlot(n2);
            ItemStack stack = slot3.getStack();
            if (this.isBestItemInChest(stack, container, list2) && this.isUpgradeOverInventory(stack, list2)) {
               ItemFilterSetting items2 = this.items;
               Item item = stack.getItem();
               Item item2 = item;
               if (!items2.isItemAllowed(item2) || list5.contains(slot3.slotNumber)) {
                  list3.add(n2);
               }
            }
         }

         return list3;
      }
   }

   private boolean canFitInInventory(Slot slot) {
      ItemStack stack = slot.getStack();

      for (Slot slot2 : (Iterable<Slot>)(mc.thePlayer.inventoryContainer.inventorySlots)) {
         if (slot2.slotNumber >= 9 && !slot2.getHasStack()) {
            return true;
         }
      }

      return mc.thePlayer.inventoryContainer.inventorySlots.stream().anyMatch(obj -> {
         ItemStack stack2 = ((Slot)obj).getStack();
         return ((Slot)obj).slotNumber >= 9 && stack2 != null && stack2.stackSize != stack2.getMaxStackSize() && stack2.isStackable() ? stack2.isItemEqual(stack) : false;
      });
   }

   private List collectBestArmorStacks(Container container) {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(container);
      Predicate predicate = obj -> obj instanceof ItemArmor;
      List list = containerSlotQuery.filterItem(predicate).toStackList();
      ContainerSlotQuery containerSlotQuery2 = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      Predicate predicate2 = obj2 -> obj2 instanceof ItemArmor;
      list.addAll(containerSlotQuery2.filterItem(predicate2).toStackList());
      boolean flag = ClientManagers.moduleManager.invManager.isToggled() && InvManagerActionOption.PUT_ON_ARMOR.isSelected() && ClientManagers.moduleManager.invManager.ignoreDurability.getValue();
      return InventoryUtil.findBestArmorSetFromList(list, flag);
   }

   public boolean isBestItemInChest(ItemStack stack, Container container, List list) {
      if (!this.isGearItemStack(stack)) {
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

               if (item instanceof ItemArmor && item2 instanceof ItemArmor && !list.contains(stack)) {
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

   public boolean isUpgradeOverInventory(ItemStack stack, List list) {
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
                     !list.contains(stack)
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

   private void toggleLinkedModules(boolean flag) {
      if (this.toggleStorageESP.getValue()) {
         ClientManagers.moduleManager.storageESP.setToggled(flag);
      }

      if (this.toggleChestAura.getValue()) {
         ClientManagers.moduleManager.chestAura.setToggled(flag);
      }
   }

   public boolean shouldStealFromChest(GuiChest guiChest) {
      String selectedValue = this.whereToSteal.getSelectedValue();
      boolean value = this.ignoreEnderChests.getValue();
      StringListSetting chestStorage2 = this.chestStorage;
      boolean flag = value;
      String str = selectedValue;
      return ContainerUtil.isValidChest(guiChest, str, flag, chestStorage2);
   }

   private void incrementTickCounters() {
      this.startDelayCounter.increment();
      this.closeDelayCounter.increment();
      this.chestOpenCounter.increment();
      this.afterClickCounter.increment();
      this.beforeClickCounter.increment();
      this.failDurationCounter.increment();
   }

   private boolean hasTargetSlots() {
      return !this.targetSlots.isEmpty();
   }

   public Slot getNextTargetSlot() {
      return this.hasTargetSlots() ? (Slot)this.targetSlots.get(0) : null;
   }

   private boolean isDefaultClickMode() {
      return this.clickCalculationMode.getSelectedValue().equals("Default");
   }

   private void clickTargetSlot(Container container, Slot slot) {
      String selectedValue = this.clickMode.getSelectedValue();
      ContainerUtil.shiftClickSlot(container, selectedValue, slot);
   }

   private void clickOverflowSortSlot(Container container, Slot slot) {
      String selectedValue = this.clickMode.getSelectedValue();
      ContainerUtil.shiftClickSlot(container, selectedValue, slot);
   }

   private void tryMisClickSlot(Container container, AtomicBoolean atomicBoolean, Slot slot) {
      ActionRateAccumulator misClickAccumulator2 = this.misClickAccumulator;
      int lastIntValue = this.misClickChance.getLastIntValue();
      if (misClickAccumulator2.shouldTrigger(lastIntValue)) {
         this.misClickChance.nextValue();
         String selectedValue = this.clickMode.getSelectedValue();
         ContainerUtil.shiftClickSlot(container, selectedValue, slot);
         atomicBoolean.set(true);
      }
   }

   private void renderStealIndicator(String str, ScaledResolution sr, ShaderRenderPass shaderRenderPass) {
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
   }

   private boolean isBackgroundColorVisible() {
      return this.silent.getValue() && this.stealIndicator.getValue() && this.backGround.getValue();
   }

   private boolean isBackgroundVisible() {
      return this.silent.getValue() && this.stealIndicator.getValue();
   }

   private boolean isIndicatorColorVisible() {
      return this.silent.getValue() && this.stealIndicator.getValue();
   }

   private boolean isVisualizeVisible() {
      return !this.silent.getValue();
   }

   private boolean isIgnoreEnderChestsVisible() {
      return !this.whereToSteal.getSelectedValue().equals("Always");
   }

   private boolean isAutoSortVisible() {
      return this.intelligent.getValue()
         && Augustus.getInstance().getModuleManager() != null
         && Augustus.getInstance().getModuleManager().invManager != null
         && Augustus.getInstance().getModuleManager().invManager.isToggled()
         && InvManagerActionOption.SORT.isSelected();
   }

   private boolean isRowSettingsVisible() {
      return this.isDefaultClickMode() && this.row.getValue();
   }

   private boolean isFailDurationVisible() {
      return this.failRateChance.getMaxValue() > 0.0;
   }

   private boolean isClickSettingsVisible() {
      return this.isDefaultClickMode() || this.clickCalculationMode.getSelectedValue().equals("Intave");
   }

}
