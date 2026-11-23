package net.augustus.modules.player;

import net.minecraft.util.MovingObjectPosition;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.events.EventInventoryClose;
import net.augustus.events.listeners.SprintListener;
import net.augustus.events.EventKey;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.events.listeners.KeyListener;
import net.augustus.utils.TuplePair;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.settings.options.InvManagerActionOption;
import net.augustus.utils.TickCounter;
import net.augustus.Augustus;
import net.augustus.settings.ItemFilterSetting;
import net.augustus.utils.player.InventoryUtil;
import net.augustus.utils.UnaryFunction;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.events.listeners.InventoryCloseListener;
import net.augustus.events.EventSprint;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.opengl.Display;

public class InvManager extends Module implements InventoryCloseListener, MoveInputListener, SilentSlotListener, ClickListener, SprintListener, KeyListener {
   private final String[] slotCategories;
   private final MultiOptionSetting actions;
   private final BooleansSetting mode;
   private final RandomizedRangeSettings startDelay;
   private final BooleanValue blockInputWhileManaging;
   public final BooleanValue visualize;
   private final BooleanValue notWhileScaffold;
   private final BooleanValue notWhileKillAuraHasTarget;
   private final BooleanValue notWhileEating;
   private final BooleanValue stopMovement;
   private final BooleanValue stopSprinting;
   private final BooleanValue stopMouseMovement;
   private final BooleanValue stopAllOtherKeyAndMouseInputs;
   private final BooleanValue autoCloseInv;
   private final RandomizedRangeSettings autoCloseInvDelay;
   private final DoubleValue sort;
   private final BooleanValue sortInstant;
   private final StringValue sortMaxMouseMoveSpeed;
   private final RandomizedRangeSettings sortMouseMoveRandom;
   private final RandomizedRangeSettings sortCPS;
   private final RandomizedRangeSettings sortCPSRecalculate;
   private final RandomizedRangeSettings sortFailRate;
   private final BooleansSetting sortSequence;
   private final DoubleValue slots;
   private final BooleansSetting slot1;
   private final BooleansSetting slot2;
   private final BooleansSetting slot3;
   private final BooleansSetting slot4;
   private final BooleansSetting slot5;
   private final BooleansSetting slot6;
   private final BooleansSetting slot7;
   private final BooleansSetting slot8;
   private final BooleansSetting slot9;
   private final DoubleValue merge;
   private final BooleanValue mergeNoOverflow;
   private final BooleanValue mergeInstant;
   private final StringValue mergeMaxMouseMoveSpeed;
   private final RandomizedRangeSettings mergeMouseMoveRandom;
   private final RandomizedRangeSettings mergeCPS;
   private final RandomizedRangeSettings mergeCPSRecalculate;
   private final RandomizedRangeSettings mergeDoubleClickRate;
   private final RandomizedRangeSettings mergeFailRate;
   private final RandomizedRangeSettings mergeMissClickRate;
   private final BooleansSetting mergeSequence;
   private final DoubleValue armor;
   private final BooleanValue noArmorDrop;
   private final BooleanValue onlyInHotbar;
   public final BooleanValue ignoreDurability;
   private final BooleanValue armorInstant;
   private final StringValue armorMaxMouseMoveSpeed;
   private final RandomizedRangeSettings armorMouseMoveRandom;
   private final RandomizedRangeSettings armorCPS;
   private final RandomizedRangeSettings armorCPSRecalculate;
   private final RandomizedRangeSettings armorDoubleClickRate;
   private final RandomizedRangeSettings armorFailRate;
   private final RandomizedRangeSettings armorMissClickRate;
   private final BooleansSetting armorSequence;
   private final DoubleValue clean;
   private final BooleanValue keepAllArmor;
   private final BooleanValue cleanInstant;
   private final StringValue cleanMaxMouseMoveSpeed;
   private final RandomizedRangeSettings cleanMouseMoveRandom;
   private final RandomizedRangeSettings cleanCPS;
   private final RandomizedRangeSettings cleanCPSRecalculate;
   private final RandomizedRangeSettings cleanFailRate;
   private final RandomizedRangeSettings cleanMissClickRate;
   private final BooleansSetting cleanerSequence;
   private final DoubleValue itemsToDrop;
   private final DoubleValue itemStackLimits;
   public final StringValue blockStackLimit;
   public final StringValue foodStackLimit;
   public final StringValue waterBucketLimit;
   public final StringValue lavaBucketLimit;
   public final StringValue throwablesLimit;
   private final DoubleValue craft;
   private final StringValue reCraftIfSoupsLowerThan;
   private final BooleanValue craftInstant;
   private final StringValue craftMaxMouseMoveSpeed;
   private final RandomizedRangeSettings craftMouseMoveRandom;
   private final RandomizedRangeSettings craftCPS;
   private final RandomizedRangeSettings craftCPSRecalculate;
   private final DoubleValue armorHotbar;
   private final BooleanValue qsg;
   private final RandomizedRangeSettings tickDelayBeforeSwitch;
   private final RandomizedRangeSettings tickDelayBeforeClick;
   private final RandomizedRangeSettings tickDelayAfter;
   private final BooleanValue silentSlotSwitching;
   private final BooleansSetting clickMode;
   private final TimeHelper lastActionTimer;
   private final TimeHelper startDelayTimer;
   private final ActionRateAccumulator armorCpsAccumulator;
   private final TickCounter armorCpsRecalcCounter;
   private final ActionRateAccumulator sortCpsAccumulator;
   private final TickCounter sortCpsRecalcCounter;
   private final ActionRateAccumulator cleanCpsAccumulator;
   private final TickCounter cleanCpsRecalcCounter;
   private final ActionRateAccumulator mergeCpsAccumulator;
   private final TickCounter mergeCpsRecalcCounter;
   private final ActionRateAccumulator doubleClickAccumulator;
   private final ActionRateAccumulator failRateAccumulator;
   private final ActionRateAccumulator missClickAccumulator;
   private final ActionRateAccumulator craftCpsAccumulator;
   private final TickCounter craftCpsRecalcCounter;
   private int armorTicksSinceClick;
   private int mergeTicksSinceClick;
   private int craftTicksSinceClick;
   private int sortTicksSinceClick;
   private int cleanTicksSinceClick;
   private int nextTargetSlot;
   private boolean craftingActive;
   private boolean holdingSwappedArmor;
   private final ArrayList detectedHotbarArmorSlots;
   private final ArrayList pendingHotbarArmorSlots;
   private int activeHotbarArmorSlot;
   private int previousHotbarArmorSlot;
   private int hotbarArmorDetectTick;
   private int hotbarArmorTicks;
   private boolean hotbarArmorClicked;
   private boolean spoofInvIdle;
   private boolean inventorySessionActive;
   private boolean managedActionsPerformed;
   private final ItemFilterSetting items;

   public InvManager() {
       super("InvManager", "InventarManager", new Color(32, 94, 103), Categorys.PLAYER);
      this.slotCategories = new String[]{
         "None",
         "Sword",
         "Bow",
         "Rod",
         "Pickaxe",
         "Axe",
         "Shovel",
         "Shear",
         "Throwables",
         "Blocks",
         "EnderPearls",
         "Food",
         "Gapple",
         "Soups",
         "Helmet",
         "Chestplate",
         "Trousers",
         "Boots"
      };
      this.actions = new MultiOptionSetting(this, "Actions", InvManagerActionOption.values());
      this.mode = new BooleansSetting(this, "Mode", "OpenInv", new String[]{"OpenInv", "SpoofInvOther", "SpoofInv", "Basic"});
      this.startDelay = (new RandomizedRangeSettings(this, "StartDelay", 150.0, 200.0, 0.0, 1000.0, 0)).withoutNotInARow();
      this.blockInputWhileManaging = new BooleanValue(this, "BlockInputWhileManaging", true);
      this.visualize = new BooleanValue(this, "Visualize", true);
      this.notWhileScaffold = new BooleanValue(this, "NotWhileScaffold", true);
      this.notWhileKillAuraHasTarget = new BooleanValue(this, "NotWhileKillAuraHasTarget", true);
      BooleanValue booleanValue = new BooleanValue(this, "NotWhileEating", true);
      SettingVisibilityCondition condition = () -> this.isSpoofInvMode();
      this.notWhileEating = (BooleanValue)booleanValue.withVisibility(condition);
      BooleanValue booleanValue2 = new BooleanValue(this, "StopMovement", false);
      SettingVisibilityCondition condition2 = () -> this.isSpoofInvMode();
      this.stopMovement = (BooleanValue)booleanValue2.withVisibility(condition2);
      BooleanValue booleanValue3 = new BooleanValue(this, "StopSprinting", false);
      SettingVisibilityCondition condition3 = () -> this.isSpoofInvMode();
      this.stopSprinting = (BooleanValue)booleanValue3.withVisibility(condition3);
      BooleanValue booleanValue4 = new BooleanValue(this, "StopMouseMovement", false);
      SettingVisibilityCondition condition4 = () -> this.isSpoofInvMode();
      this.stopMouseMovement = (BooleanValue)booleanValue4.withVisibility(condition4);
      BooleanValue booleanValue5 = new BooleanValue(this, "StopAllOtherKeyAndMouseInputs", false);
      SettingVisibilityCondition condition5 = () -> this.isSpoofInvMode();
      this.stopAllOtherKeyAndMouseInputs = (BooleanValue)booleanValue5.withVisibility(condition5);
      BooleanValue booleanValue6 = new BooleanValue(this, "AutoCloseInv", false);
      SettingVisibilityCondition condition6 = () -> this.isAutoCloseInvVisible();
      this.autoCloseInv = (BooleanValue)booleanValue6.withVisibility(condition6);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "AutoCloseInvDelay", 100.0, 200.0, 0.0, 1000.0, 0);
      SettingVisibilityCondition condition7 = () -> {
         return !this.isSpoofInvMode() && this.autoCloseInv.getValue();
      };
      this.autoCloseInvDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition7);
      DoubleValue doubleValue = new DoubleValue(this, "Sort");
      SettingVisibilityCondition condition8 = InvManagerActionOption.SORT::isSelected;
      this.sort = (DoubleValue)doubleValue.visibleWhen(condition8).markVisualOnly();
      BooleanValue booleanValue7 = new BooleanValue(this, "SortInstant", false, this.sort);
      String str = "Instant";
      this.sortInstant = (BooleanValue)booleanValue7.withDisplayName(str);
      StringValue stringValue = new StringValue(this, "SortMaxMouseMoveSpeed", 19.0, 0.0, 200.0, 0, this.sort);
      String str2 = "MaxMouseMoveSpeed";
      StringValue stringValue2 = stringValue.named(str2);
      SettingVisibilityCondition condition9 = () -> this.isSortMaxMouseMoveSpeedVisible();
      this.sortMaxMouseMoveSpeed = stringValue2.visibleWhen(condition9);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "SortMouseMoveRandom", 0.0, 0.0, -1.0, 2.0, 0, this.sort)).withoutNotInARow();
      String str3 = "MouseMoveRandom";
      RandomizedRangeSettings randomizedRangeSettings3 = (RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str3);
      SettingVisibilityCondition condition10 = () -> this.isSortMouseMoveRandomVisible();
      this.sortMouseMoveRandom = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition10);
      RandomizedRangeSettings randomizedRangeSettings4 = new RandomizedRangeSettings(this, "SortCPS", 17.0, 20.0, 0.0, 40.0, 0, this.sort);
      String str4 = "CPS";
      RandomizedRangeSettings randomizedRangeSettings5 = (RandomizedRangeSettings)randomizedRangeSettings4.withDisplayNamePrefix(str4);
      SettingVisibilityCondition condition11 = () -> this.isSortCpsVisible();
      this.sortCPS = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition11);
      RandomizedRangeSettings randomizedRangeSettings6 = (new RandomizedRangeSettings(this, "SortCPSRecalculate", 6.0, 8.0, 0.0, 40.0, 0, this.sort)).withoutNotInARow();
      String str5 = "CPSRecalculate";
      RandomizedRangeSettings randomizedRangeSettings7 = (RandomizedRangeSettings)randomizedRangeSettings6.withDisplayNamePrefix(str5);
      SettingVisibilityCondition condition12 = () -> this.isSortCpsRecalculateVisible();
      this.sortCPSRecalculate = (RandomizedRangeSettings)randomizedRangeSettings7.withVisibility(condition12);
      RandomizedRangeSettings randomizedRangeSettings8 = new RandomizedRangeSettings(this, "SortFailRate", 2.0, 5.0, 0.0, 100.0, 0, this.sort);
      String str6 = "FailRate";
      RandomizedRangeSettings randomizedRangeSettings9 = ((RandomizedRangeSettings)randomizedRangeSettings8.withDisplayNamePrefix(str6)).withoutNotInARow();
      SettingVisibilityCondition condition13 = () -> this.isSortFailRateVisible();
      this.sortFailRate = (RandomizedRangeSettings)randomizedRangeSettings9.withVisibility(condition13);
      this.sortSequence = new BooleansSetting(this, "SortSequence", "Nearest", new String[]{"Basic", "Nearest", "Random"}, this.sort);
      this.slots = (DoubleValue)(new DoubleValue(this, "Slots", this.sort)).markVisualOnly();
      this.slot1 = (BooleansSetting)(new BooleansSetting(this, "Slot1", "Sword", this.slotCategories, this.slots)).markVisualOnly();
      this.slot2 = (BooleansSetting)(new BooleansSetting(this, "Slot2", "Bow", this.slotCategories, this.slots)).markVisualOnly();
      this.slot3 = (BooleansSetting)(new BooleansSetting(this, "Slot3", "Throwables", this.slotCategories, this.slots)).markVisualOnly();
      this.slot4 = (BooleansSetting)(new BooleansSetting(this, "Slot4", "Blocks", this.slotCategories, this.slots)).markVisualOnly();
      this.slot5 = (BooleansSetting)(new BooleansSetting(this, "Slot5", "Blocks", this.slotCategories, this.slots)).markVisualOnly();
      this.slot6 = (BooleansSetting)(new BooleansSetting(this, "Slot6", "Pickaxe", this.slotCategories, this.slots)).markVisualOnly();
      this.slot7 = (BooleansSetting)(new BooleansSetting(this, "Slot7", "Axe", this.slotCategories, this.slots)).markVisualOnly();
      this.slot8 = (BooleansSetting)(new BooleansSetting(this, "Slot8", "Food", this.slotCategories, this.slots)).markVisualOnly();
      this.slot9 = (BooleansSetting)(new BooleansSetting(this, "Slot9", "Gapple", this.slotCategories, this.slots)).markVisualOnly();
      DoubleValue doubleValue2 = new DoubleValue(this, "Merge");
      SettingVisibilityCondition condition14 = InvManagerActionOption.MERGE::isSelected;
      this.merge = doubleValue2.visibleWhen(condition14);
      BooleanValue booleanValue8 = new BooleanValue(this, "MergeNoOverflow", true, this.merge);
      String str7 = "NoOverflow";
      this.mergeNoOverflow = (BooleanValue)booleanValue8.withDisplayName(str7);
      BooleanValue booleanValue9 = new BooleanValue(this, "MergeInstant", false, this.merge);
      String str8 = "Instant";
      this.mergeInstant = (BooleanValue)booleanValue9.withDisplayName(str8);
      StringValue stringValue3 = new StringValue(this, "MergeMaxMouseMoveSpeed", 19.0, 0.0, 200.0, 0, this.merge);
      String str9 = "MaxMouseMoveSpeed";
      StringValue stringValue4 = stringValue3.named(str9);
      SettingVisibilityCondition condition15 = () -> {
         return !this.mergeInstant.getValue();
      };
      this.mergeMaxMouseMoveSpeed = stringValue4.visibleWhen(condition15);
      RandomizedRangeSettings randomizedRangeSettings10 = (new RandomizedRangeSettings(this, "MergeMouseMoveRandom", 0.0, 0.0, -1.0, 2.0, 0, this.merge)).withoutNotInARow();
      String str10 = "MouseMoveRandom";
      RandomizedRangeSettings randomizedRangeSettings11 = (RandomizedRangeSettings)randomizedRangeSettings10.withDisplayNamePrefix(str10);
      SettingVisibilityCondition condition16 = () -> this.isMergeMouseMoveRandomVisible();
      this.mergeMouseMoveRandom = (RandomizedRangeSettings)randomizedRangeSettings11.withVisibility(condition16);
      RandomizedRangeSettings randomizedRangeSettings12 = new RandomizedRangeSettings(this, "MergeCPS", 17.0, 20.0, 0.0, 40.0, 0, this.merge);
      String str11 = "CPS";
      RandomizedRangeSettings randomizedRangeSettings13 = (RandomizedRangeSettings)randomizedRangeSettings12.withDisplayNamePrefix(str11);
      SettingVisibilityCondition condition17 = () -> this.isMergeCpsVisible();
      this.mergeCPS = (RandomizedRangeSettings)randomizedRangeSettings13.withVisibility(condition17);
      RandomizedRangeSettings randomizedRangeSettings14 = (new RandomizedRangeSettings(this, "MergeCPSRecalculate", 6.0, 8.0, 0.0, 40.0, 0, this.merge)).withoutNotInARow();
      String str12 = "CPSRecalculate";
      RandomizedRangeSettings randomizedRangeSettings15 = (RandomizedRangeSettings)randomizedRangeSettings14.withDisplayNamePrefix(str12);
      SettingVisibilityCondition condition18 = () -> this.isMergeCpsRecalculateVisible();
      this.mergeCPSRecalculate = (RandomizedRangeSettings)randomizedRangeSettings15.withVisibility(condition18);
      RandomizedRangeSettings randomizedRangeSettings16 = new RandomizedRangeSettings(this, "MergeDoubleClickRate", 0.0, 0.0, 0.0, 100.0, 0, this.merge);
      String str13 = "DoubleClickRate";
      RandomizedRangeSettings randomizedRangeSettings17 = ((RandomizedRangeSettings)randomizedRangeSettings16.withDisplayNamePrefix(str13)).withoutNotInARow();
      SettingVisibilityCondition condition19 = () -> this.isMergeDoubleClickRateVisible();
      this.mergeDoubleClickRate = (RandomizedRangeSettings)randomizedRangeSettings17.withVisibility(condition19);
      RandomizedRangeSettings randomizedRangeSettings18 = new RandomizedRangeSettings(this, "MergeFailRate", 10.0, 15.0, 0.0, 100.0, 0, this.merge);
      String str14 = "FailRate";
      RandomizedRangeSettings randomizedRangeSettings19 = ((RandomizedRangeSettings)randomizedRangeSettings18.withDisplayNamePrefix(str14)).withoutNotInARow();
      SettingVisibilityCondition condition20 = () -> {
         return !this.mergeInstant.getValue();
      };
      this.mergeFailRate = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition20);
      RandomizedRangeSettings randomizedRangeSettings20 = (new RandomizedRangeSettings(this, "MergeMissClickRate", 0.0, 0.0, 0.0, 100.0, 0, this.merge)).withoutNotInARow();
      String str15 = "MissClickRate";
      RandomizedRangeSettings randomizedRangeSettings21 = (RandomizedRangeSettings)randomizedRangeSettings20.withDisplayNamePrefix(str15);
      SettingVisibilityCondition condition21 = () -> this.isMergeMissClickRateVisible();
      this.mergeMissClickRate = (RandomizedRangeSettings)randomizedRangeSettings21.withVisibility(condition21);
      this.mergeSequence = new BooleansSetting(this, "MergeSequence", "Nearest", new String[]{"Basic", "Nearest", "Random"}, this.merge);
      DoubleValue doubleValue3 = new DoubleValue(this, "Armor");
      SettingVisibilityCondition condition22 = InvManagerActionOption.PUT_ON_ARMOR::isSelected;
      this.armor = doubleValue3.visibleWhen(condition22);
      this.noArmorDrop = new BooleanValue(this, "NoArmorDrop", false, this.armor);
      this.onlyInHotbar = new BooleanValue(this, "OnlyInHotbar", false, this.armor);
      this.ignoreDurability = new BooleanValue(this, "IgnoreDurability", false, this.armor);
      BooleanValue booleanValue10 = new BooleanValue(this, "ArmorInstant", false, this.armor);
      String str16 = "Instant";
      this.armorInstant = (BooleanValue)booleanValue10.withDisplayName(str16);
      StringValue stringValue5 = new StringValue(this, "ArmorMaxMouseMoveSpeed", 19.0, 0.0, 200.0, 0, this.armor);
      String str17 = "MaxMouseMoveSpeed";
      StringValue stringValue6 = stringValue5.named(str17);
      SettingVisibilityCondition condition23 = () -> this.isArmorMaxMouseMoveSpeedVisible();
      this.armorMaxMouseMoveSpeed = stringValue6.visibleWhen(condition23);
      RandomizedRangeSettings randomizedRangeSettings22 = (new RandomizedRangeSettings(this, "ArmorMouseMoveRandom", 0.0, 0.0, -1.0, 2.0, 0, this.armor)).withoutNotInARow();
      String str18 = "MouseMoveRandom";
      RandomizedRangeSettings randomizedRangeSettings23 = (RandomizedRangeSettings)randomizedRangeSettings22.withDisplayNamePrefix(str18);
      SettingVisibilityCondition condition24 = () -> this.isArmorMouseMoveRandomVisible();
      this.armorMouseMoveRandom = (RandomizedRangeSettings)randomizedRangeSettings23.withVisibility(condition24);
      RandomizedRangeSettings randomizedRangeSettings24 = new RandomizedRangeSettings(this, "ArmorCPS", 17.0, 20.0, 0.0, 40.0, 0, this.armor);
      String str19 = "CPS";
      RandomizedRangeSettings randomizedRangeSettings25 = (RandomizedRangeSettings)randomizedRangeSettings24.withDisplayNamePrefix(str19);
      SettingVisibilityCondition condition25 = () -> {
         return !this.armorInstant.getValue();
      };
      this.armorCPS = (RandomizedRangeSettings)randomizedRangeSettings25.withVisibility(condition25);
      RandomizedRangeSettings randomizedRangeSettings26 = (new RandomizedRangeSettings(this, "ArmorCPSRecalculate", 6.0, 8.0, 0.0, 40.0, 0, this.armor)).withoutNotInARow();
      String str20 = "CPSRecalculate";
      RandomizedRangeSettings randomizedRangeSettings27 = (RandomizedRangeSettings)randomizedRangeSettings26.withDisplayNamePrefix(str20);
      SettingVisibilityCondition condition26 = () -> this.isArmorCpsRecalculateVisible();
      this.armorCPSRecalculate = (RandomizedRangeSettings)randomizedRangeSettings27.withVisibility(condition26);
      RandomizedRangeSettings randomizedRangeSettings28 = new RandomizedRangeSettings(this, "ArmorDoubleClickRate", 0.0, 0.0, 0.0, 100.0, 0, this.armor);
      String str21 = "DoubleClickRate";
      RandomizedRangeSettings randomizedRangeSettings29 = ((RandomizedRangeSettings)randomizedRangeSettings28.withDisplayNamePrefix(str21)).withoutNotInARow();
      SettingVisibilityCondition condition27 = () -> this.isArmorDoubleClickRateVisible();
      this.armorDoubleClickRate = (RandomizedRangeSettings)randomizedRangeSettings29.withVisibility(condition27);
      RandomizedRangeSettings randomizedRangeSettings30 = new RandomizedRangeSettings(this, "ArmorFailRate", 2.0, 5.0, 0.0, 100.0, 0, this.armor);
      String str22 = "FailRate";
      RandomizedRangeSettings randomizedRangeSettings31 = ((RandomizedRangeSettings)randomizedRangeSettings30.withDisplayNamePrefix(str22)).withoutNotInARow();
      SettingVisibilityCondition condition28 = () -> {
         return !this.armorInstant.getValue();
      };
      this.armorFailRate = (RandomizedRangeSettings)randomizedRangeSettings31.withVisibility(condition28);
      RandomizedRangeSettings randomizedRangeSettings32 = (new RandomizedRangeSettings(this, "ArmorMissClickRate", 0.0, 0.0, 0.0, 100.0, 0, this.armor)).withoutNotInARow();
      String str23 = "MissClickRate";
      RandomizedRangeSettings randomizedRangeSettings33 = (RandomizedRangeSettings)randomizedRangeSettings32.withDisplayNamePrefix(str23);
      SettingVisibilityCondition condition29 = () -> this.isArmorMissClickRateVisible();
      this.armorMissClickRate = (RandomizedRangeSettings)randomizedRangeSettings33.withVisibility(condition29);
      this.armorSequence = new BooleansSetting(this, "ArmorSequence", "Nearest", new String[]{"Basic", "Nearest", "Random"}, this.armor);
      DoubleValue doubleValue4 = new DoubleValue(this, "Clean");
      SettingVisibilityCondition condition30 = InvManagerActionOption.CLEAN::isSelected;
      this.clean = doubleValue4.visibleWhen(condition30);
      this.keepAllArmor = new BooleanValue(this, "KeepAllArmor", false, this.clean);
      BooleanValue booleanValue11 = new BooleanValue(this, "CleanInstant", false, this.clean);
      String str24 = "Instant";
      this.cleanInstant = (BooleanValue)booleanValue11.withDisplayName(str24);
      StringValue stringValue7 = new StringValue(this, "CleanMaxMouseMoveSpeed", 19.0, 0.0, 200.0, 0, this.clean);
      String str25 = "MaxMouseMoveSpeed";
      StringValue stringValue8 = stringValue7.named(str25);
      SettingVisibilityCondition condition31 = () -> {
         return !this.cleanInstant.getValue();
      };
      this.cleanMaxMouseMoveSpeed = stringValue8.visibleWhen(condition31);
      RandomizedRangeSettings randomizedRangeSettings34 = (new RandomizedRangeSettings(this, "CleanMouseMoveRandom", 0.0, 0.0, -1.0, 2.0, 0, this.clean)).withoutNotInARow();
      String str26 = "MouseMoveRandom";
      RandomizedRangeSettings randomizedRangeSettings35 = (RandomizedRangeSettings)randomizedRangeSettings34.withDisplayNamePrefix(str26);
      SettingVisibilityCondition condition32 = () -> this.isCleanMouseMoveRandomVisible();
      this.cleanMouseMoveRandom = (RandomizedRangeSettings)randomizedRangeSettings35.withVisibility(condition32);
      RandomizedRangeSettings randomizedRangeSettings36 = new RandomizedRangeSettings(this, "CleanCPS", 17.0, 20.0, 0.0, 40.0, 0, this.clean);
      String str27 = "CPS";
      RandomizedRangeSettings randomizedRangeSettings37 = (RandomizedRangeSettings)randomizedRangeSettings36.withDisplayNamePrefix(str27);
      SettingVisibilityCondition condition33 = () -> this.isCleanCpsVisible();
      this.cleanCPS = (RandomizedRangeSettings)randomizedRangeSettings37.withVisibility(condition33);
      RandomizedRangeSettings randomizedRangeSettings38 = (new RandomizedRangeSettings(this, "CleanCPSRecalculate", 6.0, 8.0, 0.0, 40.0, 0, this.clean)).withoutNotInARow();
      String str28 = "CPSRecalculate";
      RandomizedRangeSettings randomizedRangeSettings39 = (RandomizedRangeSettings)randomizedRangeSettings38.withDisplayNamePrefix(str28);
      SettingVisibilityCondition condition34 = () -> this.isCleanCpsRecalculateVisible();
      this.cleanCPSRecalculate = (RandomizedRangeSettings)randomizedRangeSettings39.withVisibility(condition34);
      RandomizedRangeSettings randomizedRangeSettings40 = new RandomizedRangeSettings(this, "CleanFailRate", 10.0, 15.0, 0.0, 100.0, 0, this.clean);
      String str29 = "FailRate";
      RandomizedRangeSettings randomizedRangeSettings41 = ((RandomizedRangeSettings)randomizedRangeSettings40.withDisplayNamePrefix(str29)).withoutNotInARow();
      SettingVisibilityCondition condition35 = () -> this.isCleanFailRateVisible();
      this.cleanFailRate = (RandomizedRangeSettings)randomizedRangeSettings41.withVisibility(condition35);
      RandomizedRangeSettings randomizedRangeSettings42 = (new RandomizedRangeSettings(this, "CleanMissClickRate", 0.0, 0.0, 0.0, 100.0, 0, this.clean)).withoutNotInARow();
      String str30 = "MissClickRate";
      RandomizedRangeSettings randomizedRangeSettings43 = (RandomizedRangeSettings)randomizedRangeSettings42.withDisplayNamePrefix(str30);
      SettingVisibilityCondition condition36 = () -> this.isCleanMissClickRateVisible();
      this.cleanMissClickRate = (RandomizedRangeSettings)randomizedRangeSettings43.withVisibility(condition36);
      this.cleanerSequence = new BooleansSetting(this, "CleanerSequence", "Nearest", new String[]{"Basic", "Nearest", "Random"}, this.clean);
      DoubleValue doubleValue5 = new DoubleValue(this, "ItemsToDrop", this.clean);
      SettingVisibilityCondition condition37 = InvManagerActionOption.CLEAN::isSelected;
      this.itemsToDrop = doubleValue5.visibleWhen(condition37);
      DoubleValue doubleValue6 = new DoubleValue(this, "ItemStackLimits", this.clean);
      SettingVisibilityCondition condition38 = InvManagerActionOption.CLEAN::isSelected;
      this.itemStackLimits = doubleValue6.visibleWhen(condition38);
      this.blockStackLimit = new StringValue(this, "BlockStackLimit", 10.0, 0.0, 20.0, 0, this.itemStackLimits);
      this.foodStackLimit = new StringValue(this, "FoodStackLimit", 4.0, 0.0, 20.0, 0, this.itemStackLimits);
      this.waterBucketLimit = new StringValue(this, "WaterBucketLimit", 4.0, 0.0, 20.0, 0, this.itemStackLimits);
      this.lavaBucketLimit = new StringValue(this, "LavaBucketLimit", 4.0, 0.0, 20.0, 0, this.itemStackLimits);
      this.throwablesLimit = new StringValue(this, "ThrowablesLimit", 5.0, 0.0, 20.0, 0, this.itemStackLimits);
      DoubleValue doubleValue7 = new DoubleValue(this, "Craft");
      SettingVisibilityCondition condition39 = () -> this.isCraftVisible();
      this.craft = doubleValue7.visibleWhen(condition39);
      this.reCraftIfSoupsLowerThan = new StringValue(this, "ReCraftIfSoupsLowerThan", 4.0, 0.0, 20.0, 0, this.craft);
      BooleanValue booleanValue12 = new BooleanValue(this, "CraftInstant", false, this.craft);
      String str31 = "Instant";
      this.craftInstant = (BooleanValue)booleanValue12.withDisplayName(str31);
      StringValue stringValue9 = new StringValue(this, "CraftMaxMouseMoveSpeed", 19.0, 0.0, 200.0, 0, this.craft);
      String str32 = "MaxMouseMoveSpeed";
      StringValue stringValue10 = stringValue9.named(str32);
      SettingVisibilityCondition condition40 = () -> this.isCraftMaxMouseMoveSpeedVisible();
      this.craftMaxMouseMoveSpeed = stringValue10.visibleWhen(condition40);
      RandomizedRangeSettings randomizedRangeSettings44 = (new RandomizedRangeSettings(this, "CraftMouseMoveRandom", 0.0, 0.0, -1.0, 2.0, 0, this.craft)).withoutNotInARow();
      String str33 = "MouseMoveRandom";
      RandomizedRangeSettings randomizedRangeSettings45 = (RandomizedRangeSettings)randomizedRangeSettings44.withDisplayNamePrefix(str33);
      SettingVisibilityCondition condition41 = () -> this.isCraftMouseMoveRandomVisible();
      this.craftMouseMoveRandom = (RandomizedRangeSettings)randomizedRangeSettings45.withVisibility(condition41);
      RandomizedRangeSettings randomizedRangeSettings46 = new RandomizedRangeSettings(this, "CraftCPS", 17.0, 20.0, 0.0, 40.0, 0, this.craft);
      String str34 = "CPS";
      RandomizedRangeSettings randomizedRangeSettings47 = (RandomizedRangeSettings)randomizedRangeSettings46.withDisplayNamePrefix(str34);
      SettingVisibilityCondition condition42 = () -> this.isCraftCpsVisible();
      this.craftCPS = (RandomizedRangeSettings)randomizedRangeSettings47.withVisibility(condition42);
      RandomizedRangeSettings randomizedRangeSettings48 = (new RandomizedRangeSettings(this, "CraftCPSRecalculate", 6.0, 8.0, 0.0, 40.0, 0, this.craft)).withoutNotInARow();
      String str35 = "CPSRecalculate";
      RandomizedRangeSettings randomizedRangeSettings49 = (RandomizedRangeSettings)randomizedRangeSettings48.withDisplayNamePrefix(str35);
      SettingVisibilityCondition condition43 = () -> {
         return !this.craftInstant.getValue();
      };
      this.craftCPSRecalculate = (RandomizedRangeSettings)randomizedRangeSettings49.withVisibility(condition43);
      DoubleValue doubleValue8 = new DoubleValue(this, "ArmorHotbar");
      SettingVisibilityCondition condition44 = () -> this.isArmorHotbarVisible();
      this.armorHotbar = doubleValue8.visibleWhen(condition44);
      this.qsg = new BooleanValue(this, "QSG", false, this.armorHotbar);
      this.tickDelayBeforeSwitch = (new RandomizedRangeSettings(this, "TickDelayBeforeSwitch", 1.0, 2.0, 0.0, 7.0, 0, this.armorHotbar)).withoutNotInARow();
      this.tickDelayBeforeClick = (new RandomizedRangeSettings(this, "TickDelayBeforeClick", 1.0, 2.0, 0.0, 7.0, 0, this.armorHotbar)).withoutNotInARow();
      this.tickDelayAfter = (new RandomizedRangeSettings(this, "TickDelayAfter", 1.0, 2.0, 0.0, 7.0, 0, this.armorHotbar)).withoutNotInARow();
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true, this.armorHotbar);
      this.clickMode = new BooleansSetting(this, "ClickMode", "Basic", new String[]{"Basic"});
      this.lastActionTimer = new TimeHelper();
      this.startDelayTimer = new TimeHelper();
      this.armorCpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.armorCpsRecalcCounter = new TickCounter();
      this.sortCpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.sortCpsRecalcCounter = new TickCounter();
      this.cleanCpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.cleanCpsRecalcCounter = new TickCounter();
      this.mergeCpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.mergeCpsRecalcCounter = new TickCounter();
      this.doubleClickAccumulator = new ActionRateAccumulator(100, 0.0);
      this.failRateAccumulator = new ActionRateAccumulator(100, 0.0);
      this.missClickAccumulator = new ActionRateAccumulator(100, 0.0);
      this.craftCpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.craftCpsRecalcCounter = new TickCounter();
      this.armorTicksSinceClick = 0;
      this.mergeTicksSinceClick = 0;
      this.craftTicksSinceClick = 0;
      this.sortTicksSinceClick = 0;
      this.cleanTicksSinceClick = 0;
      this.nextTargetSlot = -1;
      this.craftingActive = false;
      this.holdingSwappedArmor = false;
      this.detectedHotbarArmorSlots = new ArrayList();
      this.pendingHotbarArmorSlots = new ArrayList();
      this.activeHotbarArmorSlot = -1;
      this.previousHotbarArmorSlot = -1;
      this.hotbarArmorDetectTick = 0;
      this.hotbarArmorTicks = 0;
      this.hotbarArmorClicked = false;
      this.spoofInvIdle = true;
      this.inventorySessionActive = false;
      this.managedActionsPerformed = false;
      this.items = new ItemFilterSetting(this, "Items", this.itemsToDrop, () -> InvManagerActionOption.isOptionSelected(InvManagerActionOption.CLEAN), obj -> InventoryUtil.isCleanableItem((Item)obj));
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 1;
      byte b2 = 52;
      this.toggleEventSubscription(b2, b);
      int n = 100000;
      byte b3 = 21;
      this.toggleEventSubscription(b3, n);
      byte b4 = 34;
      this.toggleEventSubscription(b4);
      byte b5 = 100;
      byte b6 = 7;
      this.toggleEventSubscription(b6, b5);
      byte b7 = 100;
      byte b8 = 36;
      this.toggleEventSubscription(b8, b7);
      byte b9 = 14;
      this.toggleEventSubscription(b9);
   }

   @Override
   public void onKey3(EventKey event) {
      int keyCode = event.getKeyCode();
      if (this.isKeyBlockedWhileSpoofing(keyCode)) {
         event.cancel();
      }
   }

   @Override
   public void onKey(EventKey event) {
      if (this.shouldBlockNonMovementInputs()) {
         event.cancel();
      }
   }

   @Override
   public boolean onInventoryClose() {
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
      boolean flag = this.processInventoryManagement();
      this.armorCpsRecalcCounter.increment();
      this.sortCpsRecalcCounter.increment();
      this.cleanCpsRecalcCounter.increment();
      this.craftCpsRecalcCounter.increment();
      return flag;
   }

   @Override
   public void onInventoryClose(EventInventoryClose event) {
      if (!mc.isNotInGui()) {
         if (this.autoCloseInv.getValue() && !this.isSpoofInvMode() && this.managedActionsPerformed) {
            double lastIntValue = (double)this.autoCloseInvDelay.getLastIntValue();
            double d = lastIntValue;
            if (event.canCloseAfterDelay(d)) {
               mc.thePlayer.closeScreen();
            }
         }
      }
   }

   @Override
   public void onSprint3(EventSprint event) {
      if (mc.currentScreen == null && mc.previousScreen == null && this.isSpoofInvMode() && mc.thePlayer.getSpoofedScreen() != null && this.stopSprinting.getValue()) {
         boolean flag = false;
         event.setSprintKeyDown(flag);
         boolean flag2 = false;
         event.setForceSprint(flag2);
      }
   }

   @Override
   public void onSilentSlot() {
      if (InvManagerActionOption.PUT_ON_ARMOR_HOT_BAR.isSelected() && (this.isSpoofInvMode() || this.mode.getSelectedValue().equals("OpenInv")) && mc.isNotInGui()) {
         if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Block block = mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
            if (block.hasTileEntity() || BlockUtil.isInteractableBlock(block)) {
               return;
            }
         }

         int activeHotbarArmorSlot2 = this.activeHotbarArmorSlot;
         this.activeHotbarArmorSlot = this.findHotbarArmorSlot();
         this.previousHotbarArmorSlot = activeHotbarArmorSlot2;
         if (this.activeHotbarArmorSlot > -1) {
            int activeHotbarArmorSlot3 = this.activeHotbarArmorSlot;
            boolean value = this.silentSlotSwitching.getValue();
            int n = activeHotbarArmorSlot3;
            byte b = 5;
            SilentSlotManager.requestSlot(this, b, n, value);
         }
      } else {
         this.detectedHotbarArmorSlots.clear();
         this.resetHotbarArmorState();
      }
   }

   private int findHotbarArmorSlot() {
      Container inventoryContainer = mc.thePlayer.inventoryContainer;
      boolean value = this.ignoreDurability.getValue();
      byte b = 36;
      ArrayList list = InventoryUtil.findBestArmorSetExcludingEquipped(inventoryContainer, b, value);
      ArrayList list2 = new ArrayList();

      for (int n : (Iterable<Integer>)(list)) {
         if (n >= 36) {
            ItemStack stack = inventoryContainer.getSlot(n).getStack();
            ItemArmor item = (ItemArmor)stack.getItem();
            int n2 = item.armorType + 5;
            ItemStack stack2 = inventoryContainer.getSlot(n2).getStack();
            if (stack2 == null || this.qsg.getValue()) {
               list2.add(n - 36);
            }
         }
      }

      if (!list2.equals(this.detectedHotbarArmorSlots)) {
         this.hotbarArmorDetectTick = mc.thePlayer.ticksExisted;
         this.detectedHotbarArmorSlots.clear();
         this.detectedHotbarArmorSlots.addAll(list2);
      }

      if (this.isHotbarSwitchDelayElapsed() && !this.hotbarArmorClicked && !this.pendingHotbarArmorSlots.equals(list2)) {
         this.pendingHotbarArmorSlots.clear();
         this.pendingHotbarArmorSlots.addAll(list2);
         this.hotbarArmorTicks = 0;
      }

      return this.pendingHotbarArmorSlots.isEmpty() ? -1 : (Integer)this.pendingHotbarArmorSlots.get(0);
   }

   private boolean isHotbarSwitchDelayElapsed() {
      int n = mc.thePlayer.ticksExisted - this.hotbarArmorDetectTick;
      return n >= this.tickDelayBeforeSwitch.getLastIntValue() || this.previousHotbarArmorSlot > -1 || this.activeHotbarArmorSlot > -1;
   }

   private void resetHotbarArmorState() {
      this.hotbarArmorClicked = false;
      this.activeHotbarArmorSlot = -1;
      this.tickDelayBeforeSwitch.nextValue();
      this.tickDelayBeforeClick.nextValue();
      this.tickDelayAfter.nextValue();
   }

   @Override
   public void onClick(EventClick event) {
      if (mc.isNotInGui()
         && (
            mc.objectMouseOver == null
               || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK
               || !mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock().hasTileEntity()
         )) {
         if (this.activeHotbarArmorSlot > -1) {
            if (this.hotbarArmorTicks > this.tickDelayBeforeClick.getLastIntValue() && !mc.thePlayer.isUsingItem() && !this.hotbarArmorClicked) {
               byte b = 1;
               event.addRightClicks(b);
               this.hotbarArmorClicked = true;
            }

            if ((double)this.hotbarArmorTicks > (double)this.tickDelayBeforeClick.getLastIntValue() + this.tickDelayAfter.getLastValue() && this.hotbarArmorClicked) {
               this.resetHotbarArmorState();
            }
         }

         this.hotbarArmorTicks++;
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (mc.currentScreen == null && mc.previousScreen == null && this.isSpoofInvMode() && mc.thePlayer.getSpoofedScreen() != null && this.stopMovement.getValue()) {
         boolean flag = false;
         event.setJump(flag);
         boolean flag2 = false;
         event.setSneak(flag2);
         float f = 0.0F;
         event.setMoveForward(f);
         float f2 = 0.0F;
         event.setMoveStrafe(f2);
      }
   }

   private boolean processInventoryManagement() {
      if (!(mc.currentScreen instanceof GuiInventory)
         && !this.mode.getSelectedValue().equals("Basic")
         && !this.mode.getSelectedValue().equals("SpoofInv")
         && !this.mode.getSelectedValue().equals("SpoofInvOther")) {
         if (mc.previousScreen != null) {
            this.autoCloseInvDelay.nextValue();
         }

         this.resetActionTickCounters();
         this.nextTargetSlot = -1;
         this.startDelayTimer.reset();
         this.inventorySessionActive = false;
         this.managedActionsPerformed = false;
         return false;
      } else {
         if (!this.inventorySessionActive) {
            this.startDelay.nextValue();
         }

         this.inventorySessionActive = true;
         TimeHelper startDelayTimer2 = this.startDelayTimer;
         long lastIntValue = (long)this.startDelay.getLastIntValue();
         if (startDelayTimer2.reached2(lastIntValue) && (mc.currentScreen == null || !mc.currentScreen.isAllowNativeMouseClick())) {
            if (mc.thePlayer.inventory.getItemStack() != null && !this.craftingActive && !this.holdingSwappedArmor) {
               return false;
            } else if (this.isSpoofInvMode()) {
               if (mc.currentScreen != null) {
                  this.spoofInvIdle = false;
                  return false;
               } else if (mc.thePlayer.getSpoofedScreen() == null) {
                  boolean flag = true;
                  boolean flag2 = true;
                  if (this.processSortAction(flag2, flag)) {
                     boolean flag3 = true;
                     if (this.processArmorAction(flag3)) {
                        boolean flag4 = false;
                        boolean flag5 = true;
                        if (this.processSortAction(flag5, flag4)) {
                           boolean flag6 = true;
                           if (this.processMergeAction(flag6)) {
                              boolean flag7 = true;
                              if (this.processCleanAction(flag7)) {
                                 boolean flag8 = true;
                                 if (this.processCraftAction(flag8)) {
                                    return false;
                                 }
                              }
                           }
                        }
                     }
                  }

                  return true;
               } else if (mc.thePlayer.getSpoofedScreen() == null) {
                  this.spoofInvIdle = false;
                  return true;
               } else {
                  exitBlock: {
                     boolean flag9 = true;
                     boolean flag10 = false;
                     if (this.processSortAction(flag10, flag9)) {
                        boolean flag11 = false;
                        if (this.processArmorAction(flag11)) {
                           boolean flag12 = false;
                           boolean flag13 = false;
                           if (this.processSortAction(flag13, flag12)) {
                              boolean flag14 = false;
                              if (this.processMergeAction(flag14)) {
                                 boolean flag15 = false;
                                 if (this.processCleanAction(flag15)) {
                                    boolean flag16 = false;
                                    if (this.processCraftAction(flag16)) {
                                       if (this.mode.getSelectedValue().equals("SpoofInvOther") && this.spoofInvIdle) {
                                          Container inventoryContainer = mc.thePlayer.inventoryContainer;
                                          this.closeSpoofedInventory(inventoryContainer);
                                       }

                                       this.spoofInvIdle = true;
                                       break exitBlock;
                                    }
                                 }
                              }
                           }
                        }
                     }

                     this.spoofInvIdle = false;
                  }

                  if (this.mode.getSelectedValue().equals("SpoofInv")) {
                     Container inventoryContainer2 = mc.thePlayer.inventoryContainer;
                     this.closeSpoofedInventory(inventoryContainer2);
                     this.resetActionTickCounters();
                  }

                  this.nextTargetSlot = -1;
                  return true;
               }
            } else {
               if (this.isLicenseValid()) {
                  boolean flag17 = true;
                  boolean flag18 = false;
                  if (this.processSortAction(flag18, flag17)) {
                     boolean flag19 = false;
                     if (this.processArmorAction(flag19)) {
                        boolean flag20 = false;
                        boolean flag21 = false;
                        if (this.processSortAction(flag21, flag20)) {
                           boolean flag22 = false;
                           if (this.processMergeAction(flag22)) {
                              boolean flag23 = false;
                              if (this.processCleanAction(flag23)) {
                                 boolean flag24 = false;
                                 if (this.processCraftAction(flag24)) {
                                    this.nextTargetSlot = -1;
                                    return false;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               this.managedActionsPerformed = true;
               this.updateCpsRecalculation();
               return true;
            }
         } else {
            this.resetActionTickCounters();
            return true;
         }
      }
   }

   private boolean processCleanAction(boolean flag) {
      if (InvManagerActionOption.CLEAN.isSelected() && !this.isActionPaused()) {
         this.cleanTicksSinceClick++;
         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         ArrayList list = this.collectSlotsToDrop(inventoryContainer);
         if (list.isEmpty()) {
            return true;
         } else {
            ArrayList<Runnable> list2 = new ArrayList<>();
            ArrayList list3 = new ArrayList();
            String selectedValue = this.cleanerSequence.getSelectedValue();
            String str = selectedValue;

            for (int n : (Iterable<Integer>)(InventoryUtil.sortSlotsByMode(inventoryContainer, list, str))) {
               ItemStack stack = inventoryContainer.getSlot(n).getStack();
               if (stack != null) {
                  if (this.keepAllArmor.getValue()) {
                     Item item = stack.getItem();
                     if (item instanceof ItemArmor) {
                        continue;
                     }
                  }

                  stack.setSlotIndex(n);
                  list3.add(stack);
                  list2.add(() -> {
                     this.dropItemStack(stack, inventoryContainer);
                  });
               }
            }

            if (list2.isEmpty()) {
               return true;
            } else if (flag) {
               this.openSpoofedInventory();
               return this.cleanInstant.getValue();
            } else {
               ActionRateAccumulator cleanCpsAccumulator2 = this.cleanCpsAccumulator;
               RandomizedRangeSettings cleanFailRate2 = this.cleanFailRate;
               RandomizedRangeSettings cleanMissClickRate2 = this.cleanMissClickRate;
               RandomizedRangeSettings cleanMouseMoveRandom2 = this.cleanMouseMoveRandom;
               boolean value = this.cleanInstant.getValue();
               int lastIntValue = this.cleanCPS.getLastIntValue();
               int cleanTicksSinceClick2 = this.cleanTicksSinceClick;
               boolean flag2 = true;
               int value2 = (int)this.cleanMaxMouseMoveSpeed.getValue(flag2);
               int n2 = cleanTicksSinceClick2;
               int n3 = lastIntValue;
               boolean flag3 = value;
               RandomizedRangeSettings randomizedRangeSettings = cleanMouseMoveRandom2;
               RandomizedRangeSettings randomizedRangeSettings2 = cleanMissClickRate2;
               RandomizedRangeSettings randomizedRangeSettings3 = cleanFailRate2;
               Object obj = null;
               ActionRateAccumulator actionRateAccumulator = cleanCpsAccumulator2;
               return this.executeActionBatch(list2, list3, actionRateAccumulator, (RandomizedRangeSettings)obj, randomizedRangeSettings3, randomizedRangeSettings2, randomizedRangeSettings, inventoryContainer, flag3, n3, n2, value2);
            }
         }
      } else {
         return true;
      }
   }

   private int[] countSoupCraftingItems(Container container) {
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

   private boolean processCraftAction(boolean flag) {
      if (!InvManagerActionOption.CRAFT.isSelected() || this.isActionPaused()) {
         return true;
      } else if (this.mode.getSelectedValue().equals("OpenInv") && !flag) {
         this.craftTicksSinceClick++;
         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         int[] ints = this.countSoupCraftingItems(inventoryContainer);
         if (ints[1] != 0 && ints[2] != 0 && ints[3] != 0) {
            int n = Math.min(ints[1], Math.min(ints[2], ints[3]));
            int n2 = ints[0];
            if (n <= 0) {
               this.craftingActive = false;
               return true;
            } else {
               int n3 = n > n2 ? 1 : 0;
               Item[] items2 = new Item[]{Items.bowl, Item.getItemById(40), Item.getItemById(39)};
               boolean[] flags = new boolean[]{false, false, false};

               for (int i = 1; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
                  if (i == 1) {
                     flags = new boolean[]{false, false, false};
                     Container inventoryContainer2 = mc.thePlayer.inventoryContainer;
                     n2 = this.countSoupCraftingItems(inventoryContainer2)[0];
                  }

                  ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                  if (stack != null) {
                     Item item = stack.getItem();
                     if (i < 5) {
                        for (int j = 0; j < items2.length; j++) {
                           if (items2[j].equals(item)) {
                              this.craftingActive = true;
                              flags[j] = true;
                           }
                        }
                     }

                     if (n2 > 0 && (double)ints[4] <= this.reCraftIfSoupsLowerThan.getValue()) {
                        if (mc.thePlayer.inventory.getItemStack() == null) {
                           if (i > 8 && (item.equals(items2[0]) && !flags[0] || item.equals(items2[1]) && !flags[1] || item.equals(items2[2]) && !flags[2])
                              )
                            {
                              if (this.isCraftClickBlocked(i)) {
                                 return false;
                              }

                              this.craftingActive = true;
                              Container inventoryContainer3 = mc.thePlayer.inventoryContainer;
                              this.clickSlot(i, n3, inventoryContainer3);
                              i = 0;
                           }

                           if (i > 4 && flags[0] && flags[1] && flags[2]) {
                              byte b = 0;
                              if (this.isCraftClickBlocked(b)) {
                                 return false;
                              }

                              Container inventoryContainer4 = mc.thePlayer.inventoryContainer;
                              byte b2 = -1;
                              byte b3 = 0;
                              this.shiftClickSlot(b3, b2, inventoryContainer4);
                              i = 0;
                           }
                        } else {
                           ItemStack itemStack = mc.thePlayer.inventory.getItemStack();
                           Item item2 = itemStack.getItem();

                           for (int k = 0; k < 3; k++) {
                              if (item2.equals(items2[k]) && !flags[k]) {
                                 int n4 = k + 1;
                                 int n5 = n4;
                                 if (this.isCraftClickBlocked(n5)) {
                                    return false;
                                 }

                                 this.craftingActive = false;
                                 int n6 = k + 1;
                                 Container inventoryContainer5 = mc.thePlayer.inventoryContainer;
                                 byte b4 = 0;
                                 int n7 = n6;
                                 this.clickSlot(n7, b4, inventoryContainer5);
                                 i = 0;
                              }
                           }
                        }
                     } else if (i > 5) {
                        for (int m = 0; m < items2.length; m++) {
                           if (flags[m]) {
                              ItemStack stack2 = null;
                              int n8 = -1;

                              for (int idx = 0; idx < 5; idx++) {
                                 ItemStack stack3 = mc.thePlayer.inventoryContainer.getSlot(idx).getStack();
                                 if (stack3 != null && stack3.getItem().equals(items2[m])) {
                                    stack2 = stack3;
                                    n8 = idx;
                                    break;
                                 }
                              }

                              int n9 = n8 != 0 ? n8 : i;
                              if (this.isCraftClickBlocked(n9)) {
                                 return false;
                              }

                              for (int index = 9; index < mc.thePlayer.inventoryContainer.inventorySlots.size(); index++) {
                                 ItemStack stack4 = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                                 if (stack2 != null && (stack4 == null || stack4.getItem().equals(stack2.getItem()) && stack4.stackSize < stack2.getMaxStackSize())) {
                                    Container inventoryContainer6 = mc.thePlayer.inventoryContainer;
                                    byte b5 = -1;
                                    this.shiftClickSlot(n8, b5, inventoryContainer6);
                                    this.craftingActive = false;
                                    i = 0;
                                    break;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               return true;
            }
         } else {
            this.craftingActive = false;
            return true;
         }
      } else {
         this.craftingActive = false;
         return true;
      }
   }

   private boolean isCraftClickBlocked(int n) {
      if (this.craftInstant.getValue()) {
         return false;
      } else {
         ActionRateAccumulator craftCpsAccumulator2 = this.craftCpsAccumulator;
         int lastIntValue = this.craftCPS.getLastIntValue();
         int n2 = craftCpsAccumulator2.accumulate(lastIntValue);
         if (n2 <= 0) {
            return true;
         } else {
            int craftTicksSinceClick2 = this.craftTicksSinceClick;
            boolean flag = true;
            int value = (int)this.craftMaxMouseMoveSpeed.getValue(flag);
            RandomizedRangeSettings craftMouseMoveRandom2 = this.craftMouseMoveRandom;
            Container inventoryContainer = mc.thePlayer.inventoryContainer;
            int n3 = value;
            int n4 = craftTicksSinceClick2;
            n2 = this.filterClicksByMouseSpeed(n4, n3, inventoryContainer, n, n2, craftMouseMoveRandom2);
            return n2 <= 0;
         }
      }
   }

   private boolean processSortAction(boolean flag, boolean flag2) {
      if (InvManagerActionOption.SORT.isSelected() && !this.isActionPaused()) {
         this.sortTicksSinceClick++;
         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         HashMap map = new HashMap();
         ArrayList<Runnable> list = new ArrayList<>();
         ArrayList list2 = new ArrayList();
         ArrayList list3 = new ArrayList();
         HashMap map2 = new HashMap();

         for (String str : this.slotCategories) {
            if (flag2 ? str.equals("Sword") : !str.equals("None") && !str.equals("Sword")) {
               ArrayList hotbarSlotsForCategory = this.getHotbarSlotsForCategory(str);

               for (int n : (Iterable<Integer>)(hotbarSlotsForCategory)) {
                  if (n != -1) {
                     boolean flag3 = map.containsKey(str);
                     List list4 = flag3 ? (List)map.get(str) : this.getRankedSlotsForCategory(inventoryContainer, str);
                     if (!flag3) {
                        map.put(str, list4);
                     }

                     if (!list4.isEmpty()) {
                        TuplePair tuplePair = (TuplePair)list4.get(0);
                        ArrayList list5 = new ArrayList();

                        for (TuplePair tuplePair2 : (Iterable<TuplePair>)(list4)) {
                           if ((Double)tuplePair2.getFirst() >= (Double)tuplePair.getFirst()) {
                              list5.add(tuplePair2);
                           }
                        }

                        if (list5.size() > 1) {
                           for (int i = 0; i < list5.size(); i++) {
                              if (!hotbarSlotsForCategory.contains((Integer)((TuplePair)list5.get(i)).getSecond() - 36)) {
                                 tuplePair = (TuplePair)list5.get(i);
                                 break;
                              }
                           }
                        }

                        if (tuplePair != null) {
                           int n2 = 0;

                           for (int j = 0; j < list4.size(); j++) {
                              TuplePair tuplePair3 = (TuplePair)list4.get(j);
                              if (((Double)tuplePair3.getFirst()).equals(tuplePair.getFirst()) && ((Integer)tuplePair3.getSecond()).equals(tuplePair.getSecond())) {
                                 n2 = j;
                                 break;
                              }
                           }

                           list4.remove(n2);
                           int second = (Integer)tuplePair.getSecond();
                           double first = (Double)tuplePair.getFirst();
                           if (second != n + 36 && !hotbarSlotsForCategory.contains(second + 36)) {
                              ItemStack stack = inventoryContainer.getSlot(n + 36).getStack();
                              boolean flag4 = !flag2;
                              double itemScore = InventoryUtil.getItemScore(stack, flag4);
                              if (!(first <= itemScore) || InventoryUtil.getItemCategoryPriority(stack, flag2) != InventoryUtil.getItemCategoryPriority(inventoryContainer.getSlot(second).getStack(), flag2)
                                 )
                               {
                                 list3.add(second);
                                 map2.put(second, n);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         if (list3.isEmpty()) {
            return true;
         } else {
            String selectedValue = this.sortSequence.getSelectedValue();
            String str2 = selectedValue;

            for (int n3 : (Iterable<Integer>)(InventoryUtil.sortSlotsByMode(inventoryContainer, list3, str2))) {
               ItemStack stack2 = inventoryContainer.getSlot(n3).getStack();
               stack2.setSlotIndex(n3);
               int n4 = (Integer)map2.get(n3);
               list.add(() -> this.executeSortSwap(n4, stack2, inventoryContainer));
               list2.add(stack2);
            }

            if (list.isEmpty()) {
               return true;
            } else if (flag) {
               this.openSpoofedInventory();
               return this.sortInstant.getValue();
            } else {
               ActionRateAccumulator sortCpsAccumulator2 = this.sortCpsAccumulator;
               RandomizedRangeSettings sortFailRate2 = this.sortFailRate;
               RandomizedRangeSettings sortMouseMoveRandom2 = this.sortMouseMoveRandom;
               boolean value = this.sortInstant.getValue();
               int lastIntValue = this.sortCPS.getLastIntValue();
               int sortTicksSinceClick2 = this.sortTicksSinceClick;
               boolean flag5 = true;
               int value2 = (int)this.sortMaxMouseMoveSpeed.getValue(flag5);
               int n5 = sortTicksSinceClick2;
               int n6 = lastIntValue;
               boolean flag6 = value;
               RandomizedRangeSettings randomizedRangeSettings = sortMouseMoveRandom2;
               Object obj = null;
               RandomizedRangeSettings randomizedRangeSettings2 = sortFailRate2;
               Object obj2 = null;
               ActionRateAccumulator actionRateAccumulator = sortCpsAccumulator2;
               return this.executeActionBatch(list, list2, actionRateAccumulator, (RandomizedRangeSettings)obj2, randomizedRangeSettings2, (RandomizedRangeSettings)obj, randomizedRangeSettings, inventoryContainer, flag6, n6, n5, value2);
            }
         }
      } else {
         return true;
      }
   }

   private boolean processMergeAction(boolean flag) {
      if (InvManagerActionOption.MERGE.isSelected() && !this.isActionPaused()) {
         this.mergeTicksSinceClick++;
         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         byte b = 8;
         List list = this.findMergeableSlots(inventoryContainer, b);
         if (list.isEmpty()) {
            return true;
         } else {
            ArrayList<Runnable> list2 = new ArrayList<>();
            ArrayList list3 = new ArrayList();
            String selectedValue = this.mergeSequence.getSelectedValue();
            String str = selectedValue;

            for (int n : (Iterable<Integer>)(InventoryUtil.sortSlotsByMode(inventoryContainer, list, str))) {
               ItemStack stack = inventoryContainer.getSlot(n).getStack();
               if (stack != null) {
                  stack.setSlotIndex(n);
                  list3.add(stack);
                  list2.add(() -> this.shiftClickMergeSlot(n, inventoryContainer));
               }
            }

            if (list2.isEmpty()) {
               return true;
            } else if (flag) {
               this.openSpoofedInventory();
               return this.cleanInstant.getValue();
            } else {
               ActionRateAccumulator mergeCpsAccumulator2 = this.mergeCpsAccumulator;
               RandomizedRangeSettings mergeDoubleClickRate2 = this.mergeDoubleClickRate;
               RandomizedRangeSettings mergeFailRate2 = this.mergeFailRate;
               RandomizedRangeSettings mergeMissClickRate2 = this.mergeMissClickRate;
               RandomizedRangeSettings mergeMouseMoveRandom2 = this.mergeMouseMoveRandom;
               boolean value = this.mergeInstant.getValue();
               int lastIntValue = this.mergeCPS.getLastIntValue();
               int mergeTicksSinceClick2 = this.mergeTicksSinceClick;
               boolean flag2 = true;
               int value2 = (int)this.mergeMaxMouseMoveSpeed.getValue(flag2);
               int n2 = mergeTicksSinceClick2;
               int n3 = lastIntValue;
               boolean flag3 = value;
               RandomizedRangeSettings randomizedRangeSettings = mergeMouseMoveRandom2;
               RandomizedRangeSettings randomizedRangeSettings2 = mergeMissClickRate2;
               RandomizedRangeSettings randomizedRangeSettings3 = mergeFailRate2;
               RandomizedRangeSettings randomizedRangeSettings4 = mergeDoubleClickRate2;
               ActionRateAccumulator actionRateAccumulator = mergeCpsAccumulator2;
               return this.executeActionBatch(list2, list3, actionRateAccumulator, randomizedRangeSettings4, randomizedRangeSettings3, randomizedRangeSettings2, randomizedRangeSettings, inventoryContainer, flag3, n3, n2, value2);
            }
         }
      } else {
         return true;
      }
   }

   private List findMergeableSlots(Container container, int n) {
      HashMap map = new HashMap();
      ArrayList list = new ArrayList();

      for (int i = n; i < container.inventorySlots.size(); i++) {
         ItemStack stack = container.getSlot(i).getStack();
         if (stack != null) {
            map.put(i, stack.getMaxStackSize() - stack.stackSize);
         } else {
            map.put(i, 0);
         }
      }

      boolean flag = false;
      this.collectMergeCandidates(container, n, map, list, flag);
      if (!this.mergeNoOverflow.getValue()) {
         boolean flag2 = true;
         this.collectMergeCandidates(container, n, map, list, flag2);
      }

      return list;
   }

   private void collectMergeCandidates(Container container, int n, Map map, List list, boolean flag) {

      for (int i = n; i < 36; i++) {
         ItemStack stack = container.getSlot(i).getStack();
         if (stack != null && stack.getMaxStackSize() > 1 && !list.contains(i)) {
            Item item = stack.getItem();
            AtomicInteger atomicInteger = new AtomicInteger(stack.stackSize);
            ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(container);
            byte b = 36;
            containerSlotQuery = containerSlotQuery.fromSlot(b);
            BiPredicate biPredicate = (obj, obj2) -> {
               if (atomicInteger.get() <= 0) {
                  return false;
               } else if (obj != null && obj != stack && !list.contains(obj2)) {
                  Item item2 = ((ItemStack)obj).getItem();
                  if (item2.equals(item) && ((ItemStack)obj).getDisplayName().equals(stack.getDisplayName())) {
                     int n2 = (Integer)map.get(obj2);
                     atomicInteger.addAndGet(-n2);
                     return true;
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            };
            List list2 = containerSlotQuery.filterStackAndSlot(biPredicate).toSlotIndexList();
            if ((flag ? atomicInteger.get() != stack.stackSize : atomicInteger.get() <= 0) && !list2.isEmpty()) {
               int n3 = (Integer)list2.get(list2.size() - 1);
               map.put(n3, atomicInteger.get() <= 0 ? -atomicInteger.get() : 0);

               for (int j = 0; j < list2.size() - 1; j++) {
                  map.put(list2.get(j), 0);
               }

               list.add(i);
            }
         }
      }
   }

   private boolean processArmorAction(boolean flag) {
      if (InvManagerActionOption.PUT_ON_ARMOR.isSelected() && !this.isActionPaused()) {
         this.armorTicksSinceClick++;
         Container inventoryContainer = mc.thePlayer.inventoryContainer;
         int n = this.onlyInHotbar.getValue() ? 36 : 9;
         boolean value = this.ignoreDurability.getValue();
         int n2 = n;
         ArrayList list = InventoryUtil.findBestArmorSetIncludingEquipped(inventoryContainer, n2, value);
         String selectedValue = this.armorSequence.getSelectedValue();
         String str = selectedValue;
         ArrayList list2 = list;
         List list3 = InventoryUtil.sortSlotsByMode(inventoryContainer, list2, str);
         boolean flag2 = this.armorSequence.getSelectedValue().equals("Nearest");
         ArrayList<Runnable> list4 = new ArrayList<>();
         ArrayList list5 = new ArrayList();
         if (this.noArmorDrop.getValue()) {
            for (int n3 : (Iterable<Integer>)(list3)) {
               if (n3 >= 9) {
                  ItemStack stack = inventoryContainer.getSlot(n3).getStack();
                  ItemArmor item = (ItemArmor)stack.getItem();
                  int n4 = item.armorType + 5;
                  ItemStack stack2 = inventoryContainer.getSlot(n4).getStack();
                  if (stack2 == null) {
                     int n5 = n4;
                     int n6 = n3;
                     list4.add(() -> this.equipArmorIntoEmptySlot(n6, n5, inventoryContainer));
                     list5.add(stack);
                  }
               }
            }

            boolean flag3 = false;
            boolean flag4 = false;

            for (int i = 9; i < inventoryContainer.inventorySlots.size(); i++) {
               ItemStack stack3 = inventoryContainer.getSlot(i).getStack();
               if (stack3 == null) {
                  if (mc.thePlayer.inventory.getItemStack() != null && this.holdingSwappedArmor) {
                     int n7 = i;
                     list4.add(() -> {
                        byte b = 0;
                        this.pickupOrPlaceSlot(n7, b, inventoryContainer);
                     });
                     list5.add(mc.thePlayer.inventory.getItemStack());
                     flag4 = true;
                  }

                  if (!flag4) {
                     flag3 = true;
                     break;
                  }

                  flag4 = false;
               }
            }

            if (!flag3) {
               for (int n8 : (Iterable<Integer>)(list3)) {
                  if (n8 >= 9) {
                     ItemStack stack4 = inventoryContainer.getSlot(n8).getStack();
                     ItemArmor item2 = (ItemArmor)stack4.getItem();
                     int n9 = item2.armorType + 5;
                     ItemStack stack5 = inventoryContainer.getSlot(n9).getStack();
                     if (stack5 != null) {
                        list4.add(() -> this.pickupOldArmorSlot(n9, inventoryContainer));
                        list5.add(stack5);
                        int n10 = n8;
                        list4.add(() -> this.equipNewArmorWhileFull(n10, n9, inventoryContainer));
                        list5.add(stack4);
                        int n11 = n8;
                        list4.add(() -> this.placeOldArmorIntoSourceSlot(n11, inventoryContainer));
                        list5.add(stack5);
                        this.holdingSwappedArmor = true;
                     }
                  }
               }
            } else {
               for (int n12 : (Iterable<Integer>)(list3)) {
                  if (n12 >= 9) {
                     ItemStack stack6 = inventoryContainer.getSlot(n12).getStack();
                     ItemArmor item3 = (ItemArmor)stack6.getItem();
                     int n13 = item3.armorType + 5;
                     ItemStack stack7 = inventoryContainer.getSlot(n13).getStack();
                     if (stack7 != null) {
                        int n14 = n13;
                        list4.add(() -> this.unequipOldArmorSlot(n14, inventoryContainer));
                        list5.add(stack7);
                     }
                  }
               }

               for (int n15 : (Iterable<Integer>)(list3)) {
                  if (n15 >= 9) {
                     ItemStack stack8 = inventoryContainer.getSlot(n15).getStack();
                     ItemArmor item4 = (ItemArmor)stack8.getItem();
                     int n16 = item4.armorType + 5;
                     ItemStack stack9 = inventoryContainer.getSlot(n16).getStack();
                     if (stack9 != null) {
                        int n17 = n15;
                        list4.add(() -> {
                           this.shiftClickSlot(n17, n16, inventoryContainer);
                        });
                        list5.add(stack8);
                        if (mc.thePlayer.inventory.getItemStack() != null && this.holdingSwappedArmor) {
                           int n18 = n15;
                           list4.add(() -> this.placeSwappedArmorIntoSlot(n18, inventoryContainer));
                           list5.add(stack9);
                           this.holdingSwappedArmor = false;
                        }
                     }
                  }
               }
            }
         } else {
            this.holdingSwappedArmor = false;

            for (int n19 : (Iterable<Integer>)(list3)) {
               if (n19 >= 9) {
                  ItemStack stack10 = inventoryContainer.getSlot(n19).getStack();
                  ItemArmor item5 = (ItemArmor)stack10.getItem();
                  int n20 = item5.armorType + 5;
                  ItemStack stack11 = inventoryContainer.getSlot(n20).getStack();
                  if (stack11 != null) {
                     stack11.setSlotIndex(n20);
                     list4.add(() -> {
                        this.dropItemStack(stack11, inventoryContainer);
                     });
                     list5.add(stack10);
                  }

                  if (!flag2) {
                     int n21 = n19;
                     list4.add(() -> this.equipArmorDirect(n21, n20, inventoryContainer));
                     list5.add(stack10);
                  }
               }
            }

            if (flag2) {
               for (int n22 : (Iterable<Integer>)(list3)) {
                  if (n22 >= 9) {
                     ItemStack stack12 = inventoryContainer.getSlot(n22).getStack();
                     ItemArmor item6 = (ItemArmor)stack12.getItem();
                     int n23 = item6.armorType + 5;
                     int n24 = n22;
                     list4.add(() -> this.equipArmorNearest(n24, n23, inventoryContainer));
                     stack12.setSlotIndex(n22);
                     list5.add(stack12);
                  }
               }
            }
         }

         if (list4.isEmpty()) {
            boolean flag5 = true;
            if (this.holdingSwappedArmor && mc.thePlayer.inventory.getItemStack() != null) {
               for (int j = 9; j < inventoryContainer.inventorySlots.size(); j++) {
                  ItemStack stack13 = inventoryContainer.getSlot(j).getStack();
                  if (stack13 == null && mc.thePlayer.inventory.getItemStack() != null && this.holdingSwappedArmor) {
                     int n25 = j;
                     list4.add(() -> this.placeHeldArmorIntoSlot(n25, inventoryContainer));
                     list5.add(mc.thePlayer.inventory.getItemStack());
                     flag5 = false;
                     break;
                  }
               }
            }

            if (flag5) {
               return true;
            }
         }

         if (flag) {
            this.openSpoofedInventory();
            return this.armorInstant.getValue();
         } else {
            ActionRateAccumulator armorCpsAccumulator2 = this.armorCpsAccumulator;
            RandomizedRangeSettings armorDoubleClickRate2 = this.armorDoubleClickRate;
            RandomizedRangeSettings armorFailRate2 = this.armorFailRate;
            RandomizedRangeSettings armorMissClickRate2 = this.armorMissClickRate;
            RandomizedRangeSettings armorMouseMoveRandom2 = this.armorMouseMoveRandom;
            boolean value2 = this.armorInstant.getValue();
            int lastIntValue = this.armorCPS.getLastIntValue();
            int armorTicksSinceClick2 = this.armorTicksSinceClick;
            boolean flag6 = true;
            int value3 = (int)this.armorMaxMouseMoveSpeed.getValue(flag6);
            int n26 = armorTicksSinceClick2;
            int n27 = lastIntValue;
            boolean flag7 = value2;
            RandomizedRangeSettings randomizedRangeSettings = armorMouseMoveRandom2;
            RandomizedRangeSettings randomizedRangeSettings2 = armorMissClickRate2;
            RandomizedRangeSettings randomizedRangeSettings3 = armorFailRate2;
            RandomizedRangeSettings randomizedRangeSettings4 = armorDoubleClickRate2;
            ActionRateAccumulator actionRateAccumulator = armorCpsAccumulator2;
            return this.executeActionBatch(list4, list5, actionRateAccumulator, randomizedRangeSettings4, randomizedRangeSettings3, randomizedRangeSettings2, randomizedRangeSettings, inventoryContainer, flag7, n27, n26, value3);
         }
      } else {
         return true;
      }
   }

   private boolean executeActionBatch(
      ArrayList list,
      ArrayList list2,
      ActionRateAccumulator actionRateAccumulator,
      RandomizedRangeSettings randomizedRangeSettings,
      RandomizedRangeSettings randomizedRangeSettings2,
      RandomizedRangeSettings randomizedRangeSettings3,
      RandomizedRangeSettings randomizedRangeSettings4,
      Container container,
      boolean flag,
      int n,
      int n2,
      int n3
   ) {
      if (flag) {
         list.forEach(obj -> ((Runnable)obj).run());
         return true;
      } else {
         if (!list2.isEmpty()) {
            ItemStack stack = (ItemStack)list2.get(list2.size() > 1 ? 1 : 0);
            if (stack != null) {
               this.nextTargetSlot = stack.getSlotIndex();
            }
         }

         ActionRateAccumulator failRateAccumulator2 = this.failRateAccumulator;
         if (this.rollAccumulator(failRateAccumulator2, randomizedRangeSettings2)) {
            return false;
         } else {
            int n4 = this.mode.getSelectedValue().equals("SpoofInv") ? 1 : actionRateAccumulator.accumulate(n);

            while (n4 > 0 && !list.isEmpty()) {
               if (this.tryMissClick(randomizedRangeSettings3, container)) {
                  n4--;
               } else {
                  int slotIndex = ((ItemStack)list2.get(0)).getSlotIndex();
                  n4 = this.filterClicksByMouseSpeed(n2, n3, container, slotIndex, n4, randomizedRangeSettings4);
                  if (n4 <= 0) {
                     break;
                  }

                  Runnable runnable = (Runnable)list.get(0);
                  runnable.run();
                  ActionRateAccumulator doubleClickAccumulator2 = this.doubleClickAccumulator;
                  if (this.rollAccumulator(doubleClickAccumulator2, randomizedRangeSettings)) {
                     runnable.run();
                  }

                  list.remove(0);
                  list2.remove(0);
                  n4--;
               }
            }

            return false;
         }
      }
   }

   private boolean tryMissClick(RandomizedRangeSettings randomizedRangeSettings, Container container) {
      if (InventoryUtil.getLastClickedSlot() > 0 && InventoryUtil.getLastClickedSlot() < container.inventorySlots.size()) {
         Slot slot = container.getSlot(InventoryUtil.getLastClickedSlot());
         int xDisplayPosition = slot.xDisplayPosition;
         int yDisplayPosition = slot.yDisplayPosition;
         double maxvalue = Double.MAX_VALUE;
         int n = -1;

         for (int i = 5; i < container.inventorySlots.size(); i++) {
            ItemStack stack = container.getSlot(i).getStack();
            if (stack == null && i != InventoryUtil.getLastClickedSlot() && i != n) {
               Slot slot10 = container.getSlot(i);
               int n2 = xDisplayPosition - slot10.xDisplayPosition;
               int n3 = yDisplayPosition - slot10.yDisplayPosition;
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
               byte b = -1;
               this.shiftClickSlot(n, b, container);
               return true;
            }
         }
      }

      return false;
   }

   private int filterClicksByMouseSpeed(int n, int n2, Container container, int n3, int n4, RandomizedRangeSettings randomizedRangeSettings) {
      if (this.mode.getSelectedValue().equals("SpoofInv")) {
         return n4;
      } else {
         Slot slot = container.getSlot(n3);
         ScaledResolution sr = new ScaledResolution(mc);
         int n5 = (sr.getScaledWidth() - 176) / 2;
         int n6 = (sr.getScaledHeight() - 166) / 2;
         double d = Math.abs((double)(slot.xDisplayPosition + 2 + n5) - InventoryUtil.getLastCursorPos().x);
         double d2 = Math.abs((double)(slot.yDisplayPosition + 2 + n6) - InventoryUtil.getLastCursorPos().y);
         double d3 = Math.sqrt(d * d + d2 * d2);
         boolean isGuardActive = AntiSamplerGuard.isGuardActive(1);
         int n7 = (int)(d3 / (double)(n2 + (isGuardActive ? 40 : 0)));
         if (n7 > 0) {
            n7 += randomizedRangeSettings.getLastIntValue();
         }

         if (n > n7) {
            if (d3 > 19.0) {
               randomizedRangeSettings.nextValue();
            }

            return n4;
         } else {
            return 0;
         }
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

   private void markBestCategorySlotsToKeep(String str, Set set, Supplier supplier, UnaryFunction unaryFunction) {
      ArrayList hotbarSlotsForCategory = this.getHotbarSlotsForCategory(str);
      List list = hotbarSlotsForCategory.isEmpty() ? (List)supplier.get() : (List)unaryFunction.apply(hotbarSlotsForCategory);

      for (int i = 0; i < Math.max(1, hotbarSlotsForCategory.size()) && i < list.size(); i++) {
         set.add(((TuplePair)list.get(i)).getSecond());
      }
   }

   private List markBestArmorSlotToKeep(String str, Container container, int n, Set set, List list) {
      ArrayList hotbarSlotsForCategory = this.getHotbarSlotsForCategory(str);
      if (!hotbarSlotsForCategory.isEmpty()) {
         if (list == null) {
            boolean value = this.ignoreDurability.getValue();
            byte b = 9;
            list = InventoryUtil.findBestArmorSetExcludingEquipped(container, b, value);
         }

         int n2 = this.findArmorSlotByType(container, (List)list, n);
         if (n2 >= 0) {
            set.add(n2);
         }
      }

      return (List)list;
   }

   private ArrayList collectSlotsToDrop(Container container) {
      byte b = 5;
      HashSet set = new HashSet(InventoryUtil.getBestToolSlots(container, b));
      boolean value = this.ignoreDurability.getValue();
      byte b2 = 5;
      set.addAll(InventoryUtil.findBestArmorSetExcludingEquipped(container, b2, value));
      byte b3 = 5;
      set.addAll(InventoryUtil.getGoodPotionSlots(container, b3));
      if (InvManagerActionOption.isOptionSelected(InvManagerActionOption.SORT)) {
         Supplier supplier = () -> getRankedSwords(container);
         UnaryFunction unaryFunction = obj -> {
            byte b4 = 5;
            return InventoryUtil.getSortedWeapons(container, b4, (ArrayList)obj);
         };
         Supplier supplier2 = supplier;
         String str = "Sword";
         this.markBestCategorySlotsToKeep(str, set, supplier2, unaryFunction);
         supplier = () -> {
            byte b5 = 5;
            return InventoryUtil.getSortedBows(container, b5);
         };
         UnaryFunction unaryFunction2 = obj2 -> getRankedBowsWithPreferred(container, (ArrayList)obj2);
         Supplier supplier3 = supplier;
         String str2 = "Bow";
         this.markBestCategorySlotsToKeep(str2, set, supplier3, unaryFunction2);
         supplier = () -> getRankedRods(container);
         UnaryFunction unaryFunction3 = obj3 -> {
            byte b6 = 5;
            return InventoryUtil.getSortedFishingRods(container, b6, (ArrayList)obj3);
         };
         Supplier supplier4 = supplier;
         String str3 = "Rod";
         this.markBestCategorySlotsToKeep(str3, set, supplier4, unaryFunction3);
         supplier = () -> getRankedPickaxes(container);
         UnaryFunction unaryFunction4 = obj4 -> getRankedPickaxesWithPreferred(container, (ArrayList)obj4);
         Supplier supplier5 = supplier;
         String str4 = "Pickaxe";
         this.markBestCategorySlotsToKeep(str4, set, supplier5, unaryFunction4);
         supplier = () -> {
            byte b7 = 5;
            return this.getRankedAxes(container, b7);
         };
         UnaryFunction unaryFunction5 = obj5 -> this.getRankedAxesWithPreferred(container, (ArrayList)obj5);
         Supplier supplier6 = supplier;
         String str5 = "Axe";
         this.markBestCategorySlotsToKeep(str5, set, supplier6, unaryFunction5);
         supplier = () -> getRankedShovels(container);
         UnaryFunction unaryFunction6 = obj6 -> getRankedShovelsWithPreferred(container, (ArrayList)obj6);
         Supplier supplier7 = supplier;
         String str6 = "Shovel";
         this.markBestCategorySlotsToKeep(str6, set, supplier7, unaryFunction6);
         supplier = () -> getRankedShears(container);
         UnaryFunction unaryFunction7 = obj7 -> getRankedShearsWithPreferred(container, (ArrayList)obj7);
         Supplier supplier8 = supplier;
         String str7 = "Shear";
         this.markBestCategorySlotsToKeep(str7, set, supplier8, unaryFunction7);
         Object obj8 = null;
         byte b8 = 0;
         String str8 = "Helmet";
         obj8 = this.markBestArmorSlotToKeep(str8, container, b8, set, (List)obj8);
         byte b9 = 1;
         String str9 = "Chestplate";
         obj8 = this.markBestArmorSlotToKeep(str9, container, b9, set, (List)obj8);
         byte b10 = 2;
         String str10 = "Trousers";
         obj8 = this.markBestArmorSlotToKeep(str10, container, b10, set, (List)obj8);
         byte b11 = 3;
         String str11 = "Boots";
         obj8 = this.markBestArmorSlotToKeep(str11, container, b11, set, (List)obj8);
      } else {
         byte b12 = 5;
         ArrayList sortedWeapons = InventoryUtil.getSortedWeapons(container, b12);
         if (!sortedWeapons.isEmpty()) {
            set.add(((TuplePair)sortedWeapons.get(0)).getSecond());
         }

         byte b13 = 5;
         ArrayList sortedBows = InventoryUtil.getSortedBows(container, b13);
         if (!sortedBows.isEmpty()) {
            set.add(((TuplePair)sortedBows.get(0)).getSecond());
         }

         byte b14 = 5;
         ArrayList sortedFishingRods = InventoryUtil.getSortedFishingRods(container, b14);
         if (!sortedFishingRods.isEmpty()) {
            set.add(((TuplePair)sortedFishingRods.get(0)).getSecond());
         }
      }

      ArrayList list = new ArrayList();

      for (int i = 9; i < container.inventorySlots.size(); i++) {
         ItemStack stack = container.getSlot(i).getStack();
         if (stack != null && !set.contains(i)) {
            Item item = stack.getItem();
            if (this.items.isItemAllowed(item)) {
               list.add(i);
            }
         }
      }

      int n;
      int n2;
      int n3;
      int n4;
      int n5;
      if (InvManagerActionOption.SORT.isSelected()) {
         int value2 = (int)this.lavaBucketLimit.getValue();
         String str12 = "LavaBucket";
         n3 = Math.max(value2, this.countHotbarSlotsForCategory(str12));
         value2 = (int)this.waterBucketLimit.getValue();
         String str13 = "WaterBucket";
         n4 = Math.max(value2, this.countHotbarSlotsForCategory(str13));
         value2 = (int)this.throwablesLimit.getValue();
         String str14 = "Throwables";
         n5 = Math.max(value2, this.countHotbarSlotsForCategory(str14));
         value2 = (int)this.blockStackLimit.getValue();
         String str15 = "Blocks";
         n = Math.max(value2, this.countHotbarSlotsForCategory(str15));
         value2 = (int)this.foodStackLimit.getValue();
         String str16 = "Food";
         n2 = Math.max(value2, this.countHotbarSlotsForCategory(str16));
      } else {
         n3 = (int)this.lavaBucketLimit.getValue();
         n4 = (int)this.waterBucketLimit.getValue();
         n5 = (int)this.throwablesLimit.getValue();
         n = (int)this.blockStackLimit.getValue();
         n2 = (int)this.foodStackLimit.getValue();
      }

      byte b15 = 9;
      ArrayList sortedBlocks = InventoryUtil.getSortedBlocks(container, b15);
      if (sortedBlocks.size() > n) {
         for (int j = n; j < sortedBlocks.size(); j++) {
            int second = (Integer)((TuplePair)sortedBlocks.get(j)).getSecond();
            if (!set.contains(second)) {
               list.add(second);
            }
         }
      }

      byte b16 = 9;
      ArrayList sortedFood = InventoryUtil.getSortedFood(container, b16);
      if (sortedFood.size() > n2) {
         for (int k = n2; k < sortedFood.size(); k++) {
            int second2 = (Integer)((TuplePair)sortedFood.get(k)).getSecond();
            if (!set.contains(second2)) {
               list.add(second2);
            }
         }
      }

      byte b17 = 9;
      ArrayList sortedThrowables = InventoryUtil.getSortedThrowables(container, b17);
      if (sortedThrowables.size() > n5) {
         for (int m = n5; m < sortedThrowables.size(); m++) {
            int second3 = (Integer)((TuplePair)sortedThrowables.get(m)).getSecond();
            if (!set.contains(second3)) {
               list.add(second3);
            }
         }
      }

      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(container);
      byte b18 = 9;
      containerSlotQuery = containerSlotQuery.fromSlot(b18);
      Item lavaBucket = Items.lava_bucket;
      List list2 = containerSlotQuery.filterItemExact(lavaBucket).toSlotIndexList();
      if (list2.size() > n3) {
         for (int idx = n3; idx < list2.size(); idx++) {
            int n6 = (Integer)list2.get(idx);
            if (!set.contains(n6)) {
               list.add(n6);
            }
         }
      }

      containerSlotQuery = ContainerSlotQuery.of(container);
      byte b19 = 9;
      containerSlotQuery = containerSlotQuery.fromSlot(b19);
      Item waterBucket = Items.water_bucket;
      List list3 = containerSlotQuery.filterItemExact(waterBucket).toSlotIndexList();
      if (list3.size() > n4) {
         for (int index = n4; index < list3.size(); index++) {
            int n7 = (Integer)list3.get(index);
            if (!set.contains(n7)) {
               list.add(n7);
            }
         }
      }

      return list;
   }

   private boolean isSpoofInvMode() {
      return this.mode.getSelectedValue().equals("SpoofInv") || this.mode.getSelectedValue().equals("SpoofInvOther");
   }

   private void closeSpoofedInventory(Container container) {
      if (mc.thePlayer.getSpoofedScreen() != null && this.isSpoofInvMode()) {
         if (mc.currentScreen == null) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(container.windowId));
            mc.thePlayer.setSpoofedScreen(null);
            this.restoreInputsAfterSpoofClose();
         }
      }
   }

   private void openSpoofedInventory() {
      if (this.isSpoofInvMode() && mc.thePlayer.getSpoofedScreen() == null && mc.currentScreen == null) {
         mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
         mc.thePlayer.setSpoofedScreen(new GuiInventory(mc.thePlayer));
         this.unpressNonMovementKeys();
      }
   }

   private void restoreInputsAfterSpoofClose() {
      if (Display.isActive()) {
         if (this.stopMouseMovement.getValue()) {
            mc.mouseHelper.resetMouseDelta();
         }

         if (this.stopAllOtherKeyAndMouseInputs.getValue()) {
            short s = 10000;
            mc.leftClickCounter = s;
         }
      }
   }

   private void unpressNonMovementKeys() {
      if (this.stopAllOtherKeyAndMouseInputs.getValue()) {
         HashSet set = Sets.newHashSet(
            new Integer[]{
               mc.gameSettings.keyBindForward.getKeyCode(),
               mc.gameSettings.keyBindLeft.getKeyCode(),
               mc.gameSettings.keyBindRight.getKeyCode(),
               mc.gameSettings.keyBindBack.getKeyCode(),
               mc.gameSettings.keyBindJump.getKeyCode(),
               mc.gameSettings.keyBindSneak.getKeyCode()
            }
         );
         KeyBinding.unPressKeysExcept(set);
      }
   }

   public boolean shouldBlockNonMovementInputs() {
      return this.isToggled() && this.isSpoofInvMode() && mc.thePlayer != null && mc.currentScreen == null && mc.thePlayer.getSpoofedScreen() != null && this.stopAllOtherKeyAndMouseInputs.getValue();
   }

   public boolean isKeyBlockedWhileSpoofing(int n) {
      GameSettings gameSettings = mc.gameSettings;
      return this.shouldBlockNonMovementInputs()
         && n != gameSettings.keyBindForward.getKeyCode()
         && n != gameSettings.keyBindLeft.getKeyCode()
         && n != gameSettings.keyBindRight.getKeyCode()
         && n != gameSettings.keyBindBack.getKeyCode()
         && n != gameSettings.keyBindJump.getKeyCode()
         && n != gameSettings.keyBindSneak.getKeyCode();
   }

   public boolean shouldStopMouseMovement() {
      return this.isToggled() && mc.thePlayer.getSpoofedScreen() != null && mc.currentScreen == null && this.isSpoofInvMode() && this.stopMouseMovement.getValue();
   }

   private void swapItemToHotbar(int n, ItemStack stack, Container container) {
      if (stack != null) {
         int slotIndex = stack.getSlotIndex();
         ItemStack stack2 = container.getSlot(n + 36).getStack();
         if (stack2 != null) {
            stack2.setSlotIndex(slotIndex);
         }

         this.updateVirtualCursor(container, slotIndex);
         String selectedValue = this.clickMode.getSelectedValue();
         switch (selectedValue) {
            case "Exploit":
               Augustus.getInstance().getClass();
               break;
            case "Basic":
            case "Other":
               mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotIndex, n, 2, mc.thePlayer);
         }

         this.resetActionTickCounters();
      }
   }

   private void dropItemStack(ItemStack stack, Container container) {
      if (stack != null) {
         int slotIndex = stack.getSlotIndex();
         this.updateVirtualCursor(container, slotIndex);
         String selectedValue = this.clickMode.getSelectedValue();
         switch (selectedValue) {
            case "Basic":
               mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, stack.getSlotIndex(), 1, 4, mc.thePlayer);
               break;
            case "Other":
               Augustus.getInstance().getClass();
               break;
            case "Exploit":
               Augustus.getInstance().getClass();
         }

         this.resetActionTickCounters();
      }
   }

   private void pickupOrPlaceSlot(int n, int n2, Container container) {
      this.holdingSwappedArmor = mc.thePlayer.inventory.getItemStack() == null;
      this.updateVirtualCursor(container, n);
      mc.playerController.windowClick(container.windowId, n, n2, 0, mc.thePlayer);
      this.resetActionTickCounters();
   }

   private void clickSlot(int n, int n2, Container container) {
      this.updateVirtualCursor(container, n);
      mc.playerController.windowClick(container.windowId, n, n2, 0, mc.thePlayer);
      this.resetActionTickCounters();
   }

   private void shiftClickSlot(int n, int n2, Container container) {
      this.updateVirtualCursor(container, n);
      String selectedValue = this.clickMode.getSelectedValue();
      switch (selectedValue) {
         case "Basic":
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, n, 0, 1, mc.thePlayer);
            break;
         case "Other":
            Augustus.getInstance().getClass();
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, n, 0, 1, mc.thePlayer);
            break;
         case "Exploit":
            Augustus.getInstance().getClass();
      }

      this.resetActionTickCounters();
   }

   private void updateVirtualCursor(Container container, int n) {
      ScaledResolution sr = new ScaledResolution(mc);
      int n2 = (sr.getScaledWidth() - 176) / 2;
      int n3 = (sr.getScaledHeight() - 166) / 2;
      Slot slot = container.getSlot(n);
      InventoryUtil.setLastCursorPos((double)(slot.xDisplayPosition + 2 + n2), (double)(slot.yDisplayPosition + 2 + n3));
      InventoryUtil.setLastClickedSlot(n);
      this.lastActionTimer.reset();
   }

   private boolean isActionPaused() {
      return this.notWhileScaffold.getValue() && BlockUtil.isScaffoldActiveAndValid() && this.isLicenseValid()
         || this.notWhileKillAuraHasTarget.getValue() && ClientManagers.moduleManager.killAura.target != null
         || this.isSpoofInvMode() && mc.thePlayer.isEating() && this.notWhileEating.getValue()
         || this.activeHotbarArmorSlot != -1 && InvManagerActionOption.isOptionSelected(InvManagerActionOption.PUT_ON_ARMOR_HOT_BAR) && (this.mode.getSelectedValue().equals("OpenInv") || this.isSpoofInvMode());
   }

   public void resetActionTickCounters() {
      this.armorTicksSinceClick = 0;
      this.mergeTicksSinceClick = 0;
      this.sortTicksSinceClick = 0;
      this.cleanTicksSinceClick = 0;
      this.craftTicksSinceClick = 0;
   }

   private void updateCpsRecalculation() {
      TickCounter armorCpsRecalcCounter2 = this.armorCpsRecalcCounter;
      int lastIntValue = this.armorCPSRecalculate.getLastIntValue();
      if (armorCpsRecalcCounter2.hasReached2(lastIntValue)) {
         this.armorCPS.nextValue();
         this.armorCPSRecalculate.nextValue();
         this.armorCpsRecalcCounter.reset();
      }

      armorCpsRecalcCounter2 = this.sortCpsRecalcCounter;
      int lastIntValue2 = this.sortCPSRecalculate.getLastIntValue();
      if (armorCpsRecalcCounter2.hasReached2(lastIntValue2)) {
         this.sortCPS.nextValue();
         this.sortCPSRecalculate.nextValue();
         this.sortCpsRecalcCounter.reset();
      }

      armorCpsRecalcCounter2 = this.cleanCpsRecalcCounter;
      int lastIntValue3 = this.cleanCPSRecalculate.getLastIntValue();
      if (armorCpsRecalcCounter2.hasReached2(lastIntValue3)) {
         this.cleanCPS.nextValue();
         this.cleanCPSRecalculate.nextValue();
         this.cleanCpsRecalcCounter.reset();
      }

      armorCpsRecalcCounter2 = this.craftCpsRecalcCounter;
      int lastIntValue4 = this.craftCPSRecalculate.getLastIntValue();
      if (armorCpsRecalcCounter2.hasReached2(lastIntValue4)) {
         this.craftCPS.nextValue();
         this.craftCPSRecalculate.nextValue();
         this.craftCpsRecalcCounter.reset();
      }

      armorCpsRecalcCounter2 = this.mergeCpsRecalcCounter;
      int lastIntValue5 = this.mergeCPSRecalculate.getLastIntValue();
      if (armorCpsRecalcCounter2.hasReached2(lastIntValue5)) {
         this.mergeCPS.nextValue();
         this.mergeCPSRecalculate.nextValue();
         this.mergeCpsRecalcCounter.reset();
      }
   }

   private void resetRandomizers() {
      this.startDelay.nextValue();
      this.armorCPS.nextValue();
      this.armorCPSRecalculate.nextValue();
      this.sortCPS.nextValue();
      this.sortCPSRecalculate.nextValue();
      this.cleanCPS.nextValue();
      this.cleanCPSRecalculate.nextValue();
      this.armorDoubleClickRate.nextValue();
      this.cleanMissClickRate.nextValue();
      this.armorMissClickRate.nextValue();
      this.sortFailRate.nextValue();
      this.armorFailRate.nextValue();
      this.cleanFailRate.nextValue();
      this.craftCPS.nextValue();
      this.craftCPSRecalculate.nextValue();
      this.tickDelayBeforeSwitch.nextValue();
      this.tickDelayBeforeClick.nextValue();
      this.tickDelayAfter.nextValue();
      this.nextTargetSlot = -1;
      this.resetActionTickCounters();
   }

   public ArrayList getHotbarSlotsForCategory(String str) {
      ArrayList list = new ArrayList();
      if (this.slot1.getSelectedValue().equals(str)) {
         list.add(0);
      }

      if (this.slot2.getSelectedValue().equals(str)) {
         list.add(1);
      }

      if (this.slot3.getSelectedValue().equals(str)) {
         list.add(2);
      }

      if (this.slot4.getSelectedValue().equals(str)) {
         list.add(3);
      }

      if (this.slot5.getSelectedValue().equals(str)) {
         list.add(4);
      }

      if (this.slot6.getSelectedValue().equals(str)) {
         list.add(5);
      }

      if (this.slot7.getSelectedValue().equals(str)) {
         list.add(6);
      }

      if (this.slot8.getSelectedValue().equals(str)) {
         list.add(7);
      }

      if (this.slot9.getSelectedValue().equals(str)) {
         list.add(8);
      }

      return list;
   }

   public int countHotbarSlotsForCategory(String str) {
      int n = 0;
      if (this.slot1.getSelectedValue().equals(str)) {
         n++;
      }

      if (this.slot2.getSelectedValue().equals(str)) {
         n++;
      }

      if (this.slot3.getSelectedValue().equals(str)) {
         n++;
      }

      if (this.slot4.getSelectedValue().equals(str)) {
         n++;
      }

      if (this.slot5.getSelectedValue().equals(str)) {
         n++;
      }

      if (this.slot6.getSelectedValue().equals(str)) {
         n++;
      }

      if (this.slot7.getSelectedValue().equals(str)) {
         n++;
      }

      if (this.slot8.getSelectedValue().equals(str)) {
         n++;
      }

      if (this.slot9.getSelectedValue().equals(str)) {
         n++;
      }

      return n;
   }

   public List getRankedSlotsForCategory(Container container, String str) {
      ArrayList list = null;
      if (str.equals("Helmet") || str.equals("Chestplate") || str.equals("Trousers") || str.equals("Boots")) {
         boolean value = this.ignoreDurability.getValue();
         byte b = 9;
         list = InventoryUtil.findBestArmorSetExcludingEquipped(container, b, value);
      }

      switch (str) {
         case "Sword":
            byte b2 = 5;
            return InventoryUtil.getSortedWeapons(container, b2);
         case "Bow":
            byte b3 = 5;
            return InventoryUtil.getSortedBows(container, b3);
         case "Rod":
            byte b4 = 5;
            return InventoryUtil.getSortedFishingRods(container, b4);
         case "Throwables":
            byte b5 = 5;
            return InventoryUtil.getSortedThrowables(container, b5);
         case "Blocks":
            byte b6 = 5;
            return InventoryUtil.getSortedBlocks(container, b6);
         case "Food":
            byte b7 = 5;
            return InventoryUtil.getSortedFood(container, b7);
         case "Gapple":
            byte b8 = 5;
            return InventoryUtil.getSortedGoldenApples(container, b8);
         case "EnderPearls":
            byte b9 = 5;
            return InventoryUtil.getSortedEnderPearls(container, b9);
         case "Soups":
            byte b10 = 5;
            return InventoryUtil.getSortedMushroomStews(container, b10);
         case "Pickaxe":
            byte b11 = 5;
            return InventoryUtil.getSortedPickaxes(container, b11);
         case "Axe":
            byte b12 = 5;
            return this.getRankedAxes(container, b12);
         case "Shovel":
            byte b13 = 5;
            return InventoryUtil.getSortedShovels(container, b13);
         case "Shear":
            byte b14 = 5;
            return InventoryUtil.getSortedShears(container, b14);
         case "Helmet":
            byte b15 = 0;
            return this.getBestArmorEntry(container, list, b15);
         case "Chestplate":
            byte b16 = 1;
            return this.getBestArmorEntry(container, list, b16);
         case "Trousers":
            byte b17 = 2;
            return this.getBestArmorEntry(container, list, b17);
         case "Boots":
            byte b18 = 3;
            return this.getBestArmorEntry(container, list, b18);
         case "LavaBucket":
            byte b19 = 5;
            return InventoryUtil.getSortedLavaBuckets(container, b19);
         case "WaterBucket":
            byte b20 = 5;
            return InventoryUtil.getSortedWaterBuckets(container, b20);
         default:
            return new ArrayList();
      }
   }

   private List getRankedAxesExcludingSwords(Container container, int n, ArrayList list) {
      String str = "Sword";
      int n2 = this.countHotbarSlotsForCategory(str);
      String str2 = "Axe";
      int n3 = this.countHotbarSlotsForCategory(str2);
      if (n2 > 0 && n3 > 0) {
         ArrayList sortedAxes = InventoryUtil.getSortedAxes(container, n, list);
         String str3 = "Sword";
         ArrayList hotbarSlotsForCategory = this.getHotbarSlotsForCategory(str3);
         ArrayList sortedWeapons = InventoryUtil.getSortedWeapons(container, n, hotbarSlotsForCategory);

         for (int i = 0; i < n2 && i < sortedWeapons.size(); i++) {
            TuplePair tuplePair = (TuplePair)sortedWeapons.get(i);
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot((Integer)tuplePair.getSecond()).getStack();
            if (stack != null && stack.getItem() instanceof ItemAxe) {
               for (int j = sortedAxes.size() - 1; j >= 0; j--) {
                  TuplePair tuplePair2 = (TuplePair)sortedAxes.get(j);
                  if (Objects.equals(tuplePair2.getSecond(), tuplePair.getSecond())) {
                     sortedAxes.remove(j);
                  }
               }
            }
         }

         return sortedAxes;
      } else {
         byte b = 5;
         return InventoryUtil.getSortedAxes(container, b, list);
      }
   }

   private List getRankedAxes(Container container, int n) {
      String str = "Sword";
      int n2 = this.countHotbarSlotsForCategory(str);
      String str2 = "Axe";
      int n3 = this.countHotbarSlotsForCategory(str2);
      if (n2 > 0 && n3 > 0) {
         ArrayList sortedAxes = InventoryUtil.getSortedAxes(container, n);
         ArrayList sortedWeapons = InventoryUtil.getSortedWeapons(container, n);

         for (int i = 0; i < n2 && i < sortedWeapons.size(); i++) {
            TuplePair tuplePair = (TuplePair)sortedWeapons.get(i);
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot((Integer)tuplePair.getSecond()).getStack();
            if (stack != null && stack.getItem() instanceof ItemAxe) {
               for (int j = sortedAxes.size() - 1; j >= 0; j--) {
                  TuplePair tuplePair2 = (TuplePair)sortedAxes.get(j);
                  if (Objects.equals(tuplePair2.getSecond(), tuplePair.getSecond())) {
                     sortedAxes.remove(j);
                  }
               }
            }
         }

         return sortedAxes;
      } else {
         byte b = 5;
         return InventoryUtil.getSortedAxes(container, b);
      }
   }

   private ArrayList getBestArmorEntry(Container container, List list, int n) {
      int n2 = this.findArmorSlotByType(container, list, n);
      return n2 >= 0 ? Lists.newArrayList(new TuplePair[]{new TuplePair(5.0, n2)}) : new ArrayList();
   }

   private int findArmorSlotByType(Container container, List list, int n) {

      for (int i = 0; i < list.size(); i++) {
         int n2 = (Integer)list.get(i);
         ItemStack stack = container.getSlot(n2).getStack();
         if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof ItemArmor) {
               ItemArmor itemArmor = (ItemArmor)item;
               if (itemArmor.armorType == n) {
                  return n2;
               }
            }
         }
      }

      return -1;
   }

   public boolean isBlockingGuiInput() {
      if (mc.thePlayer != null && mc.theWorld != null && this.blockInputWhileManaging.getValue()) {
         long l = 500L;
         if (!this.lastActionTimer.reached2(l) && mc.currentScreen instanceof GuiInventory) {
            return true;
         }
      }

      return false;
   }

   public int getNextTargetSlot() {
      return this.nextTargetSlot;
   }

   public String[] getSlotCategories() {
      return this.slotCategories;
   }

   private static List getRankedShearsWithPreferred(Container container, ArrayList list) {
      byte b = 5;
      return InventoryUtil.getSortedShears(container, b, list);
   }

   private static List getRankedShears(Container container) {
      byte b = 5;
      return InventoryUtil.getSortedShears(container, b);
   }

   private static List getRankedShovelsWithPreferred(Container container, ArrayList list) {
      byte b = 5;
      return InventoryUtil.getSortedShovels(container, b, list);
   }

   private static List getRankedShovels(Container container) {
      byte b = 5;
      return InventoryUtil.getSortedShovels(container, b);
   }

   private List getRankedAxesWithPreferred(Container container, ArrayList list) {
      byte b = 5;
      return this.getRankedAxesExcludingSwords(container, b, list);
   }

   private static List getRankedPickaxesWithPreferred(Container container, ArrayList list) {
      byte b = 5;
      return InventoryUtil.getSortedPickaxes(container, b, list);
   }

   private static List getRankedPickaxes(Container container) {
      byte b = 5;
      return InventoryUtil.getSortedPickaxes(container, b);
   }

   private static List getRankedRods(Container container) {
      byte b = 5;
      return InventoryUtil.getSortedFishingRods(container, b);
   }

   private static List getRankedBowsWithPreferred(Container container, ArrayList list) {
      byte b = 5;
      return InventoryUtil.getSortedBows(container, b, list);
   }

   private static List getRankedSwords(Container container) {
      byte b = 5;
      return InventoryUtil.getSortedWeapons(container, b);
   }

   private void placeHeldArmorIntoSlot(int n, Container container) {
      byte b = 0;
      this.pickupOrPlaceSlot(n, b, container);
   }

   private void equipArmorNearest(int n, int n2, Container container) {
      this.shiftClickSlot(n, n2, container);
   }

   private void equipArmorDirect(int n, int n2, Container container) {
      this.shiftClickSlot(n, n2, container);
   }

   private void placeOldArmorIntoSourceSlot(int n, Container container) {
      byte b = 0;
      this.pickupOrPlaceSlot(n, b, container);
   }

   private void equipNewArmorWhileFull(int n, int n2, Container container) {
      this.shiftClickSlot(n, n2, container);
   }

   private void pickupOldArmorSlot(int n, Container container) {
      byte b = 0;
      this.pickupOrPlaceSlot(n, b, container);
   }

   private void placeSwappedArmorIntoSlot(int n, Container container) {
      byte b = 0;
      this.pickupOrPlaceSlot(n, b, container);
   }

   private void unequipOldArmorSlot(int n, Container container) {
      byte b = -1;
      this.shiftClickSlot(n, b, container);
   }

   private void equipArmorIntoEmptySlot(int n, int n2, Container container) {
      this.shiftClickSlot(n, n2, container);
   }

   private void shiftClickMergeSlot(int n, Container container) {
      byte b = -1;
      this.shiftClickSlot(n, b, container);
   }

   private void executeSortSwap(int n, ItemStack stack, Container container) {
      this.swapItemToHotbar(n, stack, container);
   }

   private boolean isArmorHotbarVisible() {
      return InvManagerActionOption.isOptionSelected(InvManagerActionOption.PUT_ON_ARMOR_HOT_BAR) && (this.mode.getSelectedValue().equals("OpenInv") || this.isSpoofInvMode());
   }

   private boolean isCraftCpsVisible() {
      return !this.craftInstant.getValue();
   }

   private boolean isCraftMouseMoveRandomVisible() {
      return !this.craftInstant.getValue();
   }

   private boolean isCraftMaxMouseMoveSpeedVisible() {
      return !this.craftInstant.getValue();
   }

   private boolean isCraftVisible() {
      return InvManagerActionOption.isOptionSelected(InvManagerActionOption.CRAFT) && this.mode.getSelectedValue().equals("OpenInv");
   }

   private boolean isCleanMissClickRateVisible() {
      return !this.cleanInstant.getValue();
   }

   private boolean isCleanFailRateVisible() {
      return !this.cleanInstant.getValue();
   }

   private boolean isCleanCpsRecalculateVisible() {
      return !this.cleanInstant.getValue();
   }

   private boolean isCleanCpsVisible() {
      return !this.cleanInstant.getValue();
   }

   private boolean isCleanMouseMoveRandomVisible() {
      return !this.cleanInstant.getValue();
   }

   private boolean isArmorMissClickRateVisible() {
      return !this.armorInstant.getValue();
   }

   private boolean isArmorDoubleClickRateVisible() {
      return !this.armorInstant.getValue();
   }

   private boolean isArmorCpsRecalculateVisible() {
      return !this.armorInstant.getValue();
   }

   private boolean isArmorMouseMoveRandomVisible() {
      return !this.armorInstant.getValue();
   }

   private boolean isArmorMaxMouseMoveSpeedVisible() {
      return !this.armorInstant.getValue();
   }

   private boolean isMergeMissClickRateVisible() {
      return !this.mergeInstant.getValue();
   }

   private boolean isMergeDoubleClickRateVisible() {
      return !this.mergeInstant.getValue();
   }

   private boolean isMergeCpsRecalculateVisible() {
      return !this.mergeInstant.getValue();
   }

   private boolean isMergeCpsVisible() {
      return !this.mergeInstant.getValue();
   }

   private boolean isMergeMouseMoveRandomVisible() {
      return !this.mergeInstant.getValue();
   }

   private boolean isSortFailRateVisible() {
      return !this.sortInstant.getValue();
   }

   private boolean isSortCpsRecalculateVisible() {
      return !this.sortInstant.getValue();
   }

   private boolean isSortCpsVisible() {
      return !this.sortInstant.getValue();
   }

   private boolean isSortMouseMoveRandomVisible() {
      return !this.sortInstant.getValue();
   }

   private boolean isSortMaxMouseMoveSpeedVisible() {
      return !this.sortInstant.getValue();
   }

   private boolean isAutoCloseInvVisible() {
      return !this.isSpoofInvMode();
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
