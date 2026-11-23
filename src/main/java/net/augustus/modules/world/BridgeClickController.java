package net.augustus.modules.world;

import net.augustus.events.EventClick;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.utils.rotation.RayCastUtil;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.utils.TimeHelper;
import net.augustus.utils.interfaces.MC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class BridgeClickController implements MC {
   private int postEdgePauseTicks;
   private final ActionRateAccumulator cpsAccumulator;
   private final BooleanValue alwaysPlaceWhenPossible;
   private final RandomizedRangeSettings dcpsSetting;
   private int remainingPostEdgeTicks;
   public int pendingClicks;
   private boolean clickDelayed;
   private boolean wasOnFullBlock;
   private final ActionRateAccumulator dcpsAccumulator;
   private final int maxClicksPerTick;
   private int preEdgePredictTicks;
   private final BooleanValue delayClick;
   private final RandomizedRangeSettings cpsSetting;
   private final BooleansSetting dcpsMode;
   private final BooleanValue oneClick;

   public boolean shouldClickAtEdge() {
      boolean isFullBlock = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock().isFullBlock();
      if (isFullBlock && !this.wasOnFullBlock) {
         byte b = 1;
         byte b2 = 0;
         this.preEdgePredictTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
         byte b3 = 1;
         byte b4 = 0;
         this.postEdgePauseTicks = DefaultRandomGenerator.instance.nextInt2(b4, b3);
      }

      if (!isFullBlock && this.wasOnFullBlock) {
         this.remainingPostEdgeTicks = this.postEdgePauseTicks;
      }

      boolean flag = (this.preEdgePredictTicks >= 0 || !this.wasOnFullBlock) && !isFullBlock;
      this.wasOnFullBlock = isFullBlock;
      double[] doubles = PlayerUtil.predictPlayerPosition(this.preEdgePredictTicks - 1, mc.thePlayer);
      if (isFullBlock && !mc.theWorld.getBlockState(doubles[0], mc.thePlayer.posY - 1.0, doubles[2]).getBlock().isFullBlock()) {
         flag = true;
      }

      if (isFullBlock && this.remainingPostEdgeTicks > 0) {
         flag = true;
         this.remainingPostEdgeTicks--;
      }

      return flag;
   }

   public void handleBlockClick(MovingObjectPosition hit, EventClick event, ScaffoldPlaceCondition scaffoldPlaceCondition, boolean flag) {
      if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
         if (!mc.thePlayer.isUsingItem()) {
            boolean flag2 = false;
            if (this.delayClick.getValue()) {
               double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
               MovingObjectPosition hit2 = RayCastUtil.rayCastFromPosition(
                  1.0F, new Vec3(doubles[0], doubles[1], doubles[2]), new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
               );
               if (hit2 != null
                  && hit2.getBlockPos() != null
                  && !mc.thePlayer.isSneaking()
                  && hit2.getBlockPos().equalsPos(hit.getBlockPos())
                  && hit2.sideHit == hit.sideHit) {
                  ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
                  BlockPos blockPos = hit2.getBlockPos();
                  Vec3 hitVec = hit2.hitVec;
                  EnumFacing sideHit = hit2.sideHit;
                  BlockPos pos = blockPos;
                  flag2 = PlayerUtil.canPlaceBlockAt(currentEquippedItem, pos, sideHit, hitVec);
               }
            }

            ItemStack currentEquippedItem2 = mc.thePlayer.getCurrentEquippedItem();
            BlockPos blockPos2 = hit.getBlockPos();
            Vec3 hitVec2 = hit.hitVec;
            EnumFacing sideHit2 = hit.sideHit;
            BlockPos pos2 = blockPos2;
            boolean flag3 = PlayerUtil.canPlaceBlockAt(currentEquippedItem2, pos2, sideHit2, hitVec2);
            boolean flag4 = false;
            if (flag2 && flag3 && !this.clickDelayed && this.delayClick.getValue()) {
               flag4 = true;
               this.clickDelayed = true;
            } else {
               this.clickDelayed = false;
            }

            if (!flag4) {
               boolean flag5 = this.dcpsMode.getSelectedValue().equals("Internal");
               if (!flag5) {
                  this.pendingClicks = 0;
               }

               int pendingClicks2 = this.pendingClicks;
               ActionRateAccumulator cpsAccumulator2 = this.cpsAccumulator;
               int lastIntValue = this.cpsSetting.getLastIntValue();
               this.pendingClicks = pendingClicks2 + cpsAccumulator2.accumulate(lastIntValue);
               if (this.oneClick.getValue()) {
                  this.pendingClicks = 0;
               }

               int n;
               if (flag5) {
                  byte b = 0;
                  byte b2 = -1;
                  n = DefaultRandomGenerator.instance.nextInt2(b2, b);
               } else {
                  n = 0;
               }

               int n2 = n;
               boolean flag6 = false;
               boolean flag7 = false;

               for (int i = 0; i < 20 && this.pendingClicks + n2 > 0; i++) {
                  scaffoldPlaceCondition.tryPlace();
                  flag7 = true;
                  flag6 = true;
                  flag3 = false;
                  this.pendingClicks--;
               }

               exitBlock: {
                  if (!this.oneClick.getValue()) {
                     if (!this.alwaysPlaceWhenPossible.getValue()) {
                        break exitBlock;
                     }
                  }

                  if (flag3) {
                     boolean flag8 = scaffoldPlaceCondition.tryPlace();
                     event.setAllowMultiRightClick(flag8);
                     flag6 = true;
                  }
               }

               if (flag6 && !flag5) {
                  ActionRateAccumulator dcpsAccumulator2 = this.dcpsAccumulator;
                  int lastIntValue2 = this.dcpsSetting.getLastIntValue();
                  int n3 = dcpsAccumulator2.accumulate(lastIntValue2);

                  for (int j = 0; j < 20 && n3 > 0; j++) {
                     scaffoldPlaceCondition.tryPlace();
                     n3--;
                  }
               }

               if (!flag6 && flag) {
                  currentEquippedItem2 = mc.thePlayer.getCurrentEquippedItem();
                  blockPos2 = hit.getBlockPos();
                  Vec3 hitVec3 = hit.hitVec;
                  EnumFacing sideHit3 = hit.sideHit;
                  BlockPos pos3 = blockPos2;
                  if (PlayerUtil.canPlaceBlockAt(currentEquippedItem2, pos3, sideHit3, hitVec3)) {
                     scaffoldPlaceCondition.tryPlace();
                  }
               }

               if (flag7) {
                  this.cpsSetting.nextValue();
                  this.dcpsSetting.nextValue();
               }
            }
         }
      }
   }

   public BridgeClickController(RandomizedRangeSettings randomizedRangeSettings, BooleansSetting booleansSetting, RandomizedRangeSettings randomizedRangeSettings2, BooleanValue booleanValue, BooleanValue booleanValue2, BooleanValue booleanValue3) {
       super();
      this.maxClicksPerTick = 20;
      this.cpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.dcpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.remainingPostEdgeTicks = 0;
      byte b = 2;
      byte b2 = 0;
      this.preEdgePredictTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
      byte b3 = 2;
      byte b4 = 0;
      this.postEdgePauseTicks = DefaultRandomGenerator.instance.nextInt2(b4, b3);
      this.clickDelayed = false;
      this.pendingClicks = 0;
      this.cpsSetting = randomizedRangeSettings;
      this.dcpsMode = booleansSetting;
      this.dcpsSetting = randomizedRangeSettings2;
      this.alwaysPlaceWhenPossible = booleanValue;
      this.delayClick = booleanValue3;
      this.oneClick = booleanValue2;
      this.cpsSetting.nextValue();
      this.dcpsSetting.nextValue();
   }

   public void handleBlockClick(MovingObjectPosition hit, EventClick event, ScaffoldPlaceCondition scaffoldPlaceCondition) {
      boolean flag = false;
      this.handleBlockClick(hit, event, scaffoldPlaceCondition, flag);
   }

   public void handleDefaultBlockClick(MovingObjectPosition hit, EventClick event, boolean flag) {
      ScaffoldPlaceCondition scaffoldPlaceCondition = () -> {
         byte b = 1;
         event.addRightClicks(b);
         return true;
      };
      this.handleBlockClick(hit, event, scaffoldPlaceCondition, flag);
   }

}
