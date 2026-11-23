package net.augustus.modules.world;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.settings.options.FastPlaceItemOption;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.world.BlockUtil;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class FastPlace extends Module implements ClickListener, SilentSlotListener {
   private final MultiOptionSetting items;
   private final BooleanValue onlyOnClick;
   private BooleanValue alwaysPlaceWhenPossible;
   private BooleanValue oneClick;
   private BooleanValue clickPause;
   private BooleanValue autoSwap;
   private BooleanValue onlyInAir;
   private BooleanValue onlyIfMotionIsPositive;
   private boolean placedBlockThisTick;
   private boolean initialClickPending;
   private boolean wasOnFullBlock;
   private int remainingPostEdgeTicks;
   private int preEdgePredictTicks;
   private int postEdgePauseTicks;

   public FastPlace() {
       super("FastPlace", "SchnellPlatzierung", new Color(54, 144, 217), Categorys.WORLD);
      this.items = (MultiOptionSetting)(new MultiOptionSetting(this, "Items", FastPlaceItemOption.values())).markVisualOnly();
      this.onlyOnClick = (BooleanValue)(new BooleanValue(this, "OnlyOnClick", true)).markVisualOnly();
      this.placedBlockThisTick = false;
      this.initialClickPending = false;
      this.remainingPostEdgeTicks = 0;
      byte b = 2;
      byte b2 = 0;
      this.preEdgePredictTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
      byte b3 = 2;
      byte b4 = 0;
      this.postEdgePauseTicks = DefaultRandomGenerator.instance.nextInt2(b4, b3);

      for (FastPlaceItemOption fastPlaceItemOption : FastPlaceItemOption.values()) {
         DoubleValue doubleValue = new DoubleValue(this, fastPlaceItemOption.getName());
         SettingVisibilityCondition condition = () -> fastPlaceItemOption.selected;
         DoubleValue doubleValue2 = (DoubleValue)doubleValue.visibleWhen(condition).markVisualOnly();
         RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, fastPlaceItemOption.name + "CPS", 10.0, 20.0, 0.0, 60.0, 0, doubleValue2)).withoutNotInARow();
         String str = "CPS";
         fastPlaceItemOption.cpsRange = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str)).markAllVisualOnly();
         fastPlaceItemOption.cpsRange.nextValue();
         randomizedRangeSettings = (new RandomizedRangeSettings(this, fastPlaceItemOption.name + "DCPS", 2.0, 4.0, 0.0, 20.0, 0, doubleValue2)).withoutNotInARow();
         String str2 = "DCPS";
         fastPlaceItemOption.doubleClickCpsRange = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2)).markAllVisualOnly();
         fastPlaceItemOption.doubleClickCpsRange.nextValue();
         if (fastPlaceItemOption == FastPlaceItemOption.BLOCKS) {
            this.alwaysPlaceWhenPossible = (BooleanValue)(new BooleanValue(this, "AlwaysPlaceWhenPossible", false, doubleValue2)).markVisualOnly();
            this.oneClick = (BooleanValue)(new BooleanValue(this, "OneClick", false, doubleValue2)).markVisualOnly();
            this.clickPause = (BooleanValue)(new BooleanValue(this, "ClickPause", false, doubleValue2)).markVisualOnly();
            this.autoSwap = (BooleanValue)(new BooleanValue(this, "AutoSwap", false, doubleValue2)).markVisualOnly();
            this.onlyInAir = (BooleanValue)(new BooleanValue(this, "OnlyInAir", false, doubleValue2)).markVisualOnly();
            BooleanValue booleanValue = new BooleanValue(this, "OnlyIfMotionIsPositive", false, doubleValue2);
            BooleanValue onlyInAir2 = this.onlyInAir;
            this.onlyInAir.getClass();
            BooleanValue booleanValue2 = onlyInAir2;
            SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
            this.onlyIfMotionIsPositive = (BooleanValue)((BooleanValue)booleanValue.withVisibility(condition2)).markVisualOnly();
         }
      }
   }

   @Override
   public void onEnable() {
      this.initialClickPending = true;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 2;
      byte b2 = 7;
      this.toggleEventSubscription(b2, b);
      byte b3 = 34;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onSilentSlot() {
      if (this.autoSwap.getValue() && FastPlaceItemOption.BLOCKS.isSelected() && this.placedBlockThisTick) {
         for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();
            if (BlockUtil.isValidScaffoldBlock(stack)) {
               boolean flag = true;
               byte b = -2;
               SilentSlotManager.requestSlot(this, b, i, flag);
               SilentSlotManager.realHotbarSlot = i;
               break;
            }
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      this.placedBlockThisTick = false;
      if (mc.isNotInGui() && (mc.gameSettings.keyBindUseItem.isKeyDown() || !this.onlyOnClick.getValue()) && mc.thePlayer.getCurrentEquippedItem() != null) {
         for (FastPlaceItemOption fastPlaceItemOption : FastPlaceItemOption.values()) {
            ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
            Item item = currentEquippedItem.getItem();
            if (fastPlaceItemOption.matchesItem(currentEquippedItem) || !this.isLicenseValid()) {
               if (!fastPlaceItemOption.isSelected()) {
                  break;
               }

               if (!(item instanceof ItemBlock)) {
                  ActionRateAccumulator cpsAccumulator = fastPlaceItemOption.cpsAccumulator;
                  int lastIntValue = fastPlaceItemOption.cpsRange.getLastIntValue();
                  int n = cpsAccumulator.accumulate(lastIntValue);
                  boolean flag = n > 0;

                  boolean flag2;
                  for (flag2 = false; n > 0; flag2 = true) {
                     byte b = 1;
                     event.addRightClicks(b);
                     n--;
                  }

                  if (flag) {
                     cpsAccumulator = fastPlaceItemOption.doubleClickCpsAccumulator;
                     int lastIntValue2 = fastPlaceItemOption.doubleClickCpsRange.getLastIntValue();

                     for (int i = cpsAccumulator.accumulate(lastIntValue2); i > 0; i--) {
                        byte b2 = 1;
                        event.addRightClicks(b2);
                        flag2 = true;
                     }

                     this.rerollOptionCps(fastPlaceItemOption);
                  }

                  if (this.initialClickPending && !flag2) {
                     byte b3 = 1;
                     event.addRightClicks(b3);
                  }

                  boolean flag3 = true;
                  event.setIgnoreVanillaClicks(flag3);
                  boolean flag4 = false;
                  event.setRightMouseDown(flag4);
                  this.initialClickPending = false;
                  break;
               }

               if ((!this.onlyInAir.getValue() || !mc.thePlayer.onGround) && (!this.onlyInAir.getValue() || !this.onlyIfMotionIsPositive.getValue() || !(mc.thePlayer.motionY <= 0.0))) {
                  boolean isFullBlock = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock().isFullBlock();
                  if (isFullBlock && !this.wasOnFullBlock) {
                     byte b4 = 1;
                     byte b5 = 0;
                     this.preEdgePredictTicks = DefaultRandomGenerator.instance.nextInt2(b5, b4);
                     byte b6 = 1;
                     byte b7 = 0;
                     this.postEdgePauseTicks = DefaultRandomGenerator.instance.nextInt2(b7, b6);
                  }

                  if (!isFullBlock && this.wasOnFullBlock) {
                     this.remainingPostEdgeTicks = this.postEdgePauseTicks;
                  }

                  boolean flag5 = (this.preEdgePredictTicks >= 0 || !this.wasOnFullBlock) && !isFullBlock;
                  this.wasOnFullBlock = isFullBlock;
                  double[] doubles = PlayerUtil.predictPlayerPosition(this.preEdgePredictTicks - 1, mc.thePlayer);
                  if (isFullBlock && !mc.theWorld.getBlockState(doubles[0], mc.thePlayer.posY - 1.0, doubles[2]).getBlock().isFullBlock()) {
                     flag5 = true;
                  }

                  if (isFullBlock && this.remainingPostEdgeTicks > 0) {
                     flag5 = true;
                     this.remainingPostEdgeTicks--;
                  }

                  if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                     boolean flag6 = false;
                     if (flag5 || !this.clickPause.getValue()) {
                        ItemStack currentEquippedItem2 = mc.thePlayer.getCurrentEquippedItem();
                        BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                        Vec3 hitVec = mc.objectMouseOver.hitVec;
                        EnumFacing sideHit = mc.objectMouseOver.sideHit;
                        BlockPos pos = blockPos;
                        boolean flag7 = PlayerUtil.canPlaceBlockAt(currentEquippedItem2, pos, sideHit, hitVec);
                        ActionRateAccumulator cpsAccumulator2 = fastPlaceItemOption.cpsAccumulator;
                        int lastIntValue3 = fastPlaceItemOption.cpsRange.getLastIntValue();
                        int n2 = cpsAccumulator2.accumulate(lastIntValue3);
                        boolean flag8 = n2 > 0 && !this.oneClick.getValue() || flag7 && (this.oneClick.getValue() || this.alwaysPlaceWhenPossible.getValue());

                        while (n2 > 0 && !this.oneClick.getValue() || flag7 && (this.oneClick.getValue() || this.alwaysPlaceWhenPossible.getValue())) {
                           byte b8 = 1;
                           event.addRightClicks(b8);
                           boolean flag9 = this.oneClick.getValue() || this.alwaysPlaceWhenPossible.getValue();
                           event.setAllowMultiRightClick(flag9);
                           flag7 = false;
                           flag6 = true;
                           if (--n2 < -30) {
                              break;
                           }
                        }

                        if (flag8) {
                           cpsAccumulator2 = fastPlaceItemOption.doubleClickCpsAccumulator;
                           int lastIntValue4 = fastPlaceItemOption.doubleClickCpsRange.getLastIntValue();

                           for (int j = cpsAccumulator2.accumulate(lastIntValue4); j > 0; flag6 = true) {
                              byte b9 = 1;
                              event.addRightClicks(b9);
                              j--;
                           }

                           this.rerollOptionCps(fastPlaceItemOption);
                        }
                     }

                     if (this.initialClickPending && !flag6) {
                        byte b10 = 1;
                        event.addRightClicks(b10);
                     }

                     boolean flag10 = true;
                     event.setIgnoreVanillaClicks(flag10);
                     boolean flag11 = false;
                     event.setRightMouseDown(flag11);
                     this.placedBlockThisTick = true;
                     this.initialClickPending = false;
                  }
                  break;
               }
            }
         }
      } else {
         this.initialClickPending = true;
      }
   }

   private void rerollOptionCps(FastPlaceItemOption fastPlaceItemOption) {
      fastPlaceItemOption.cpsRange.nextValue();
      fastPlaceItemOption.doubleClickCpsRange.nextValue();
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
