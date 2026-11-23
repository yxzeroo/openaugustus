package net.augustus.modules.player;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import java.util.OptionalInt;
import java.util.function.Predicate;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.rotation.RotationEngine;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.listeners.BlockBoundingBoxListener;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.EventBlockBoundingBox;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventMoveEntity;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class NoFall extends Module implements ClickListener, TickListener, PacketListener, SilentSlotListener, BlockBoundingBoxListener, MoveEntityListener, UpdateListener, PacketDelayListener, MoveInputListener {
   public BooleansSetting mode;
   private final StringValue collisionFallDistance;
   public final StringValue minFallDist;
   public final StringValue minLegitFallDist;
   private final DoubleValue rotations;
   public final StringValue delay;
   private final BooleanValue useWebs;
   private final BooleanValue sneakWhileItemPlace;
   private final BooleanValue silentSlotSwitching;
   private final BooleansSetting blinkMode;
   private final StringValue blinkStartFallDistance;
   private RotationEngine rotationEngine;
   private BlockPos lastAimedBlockPos;
   private int ticksSinceLastPlace;
   private double collisionFloorY;
   private int bucketHotbarSlot;
   private BlockPos predictedLandingBlockPos;
   private int vulcanResetStage;
   private final TimeHelper timeHelper;

   public NoFall() {
       super("NoFall", "KeinFallen", new Color(222, 80, 95, 255), Categorys.PLAYER);
      this.mode = new BooleansSetting(
         this, "Mode", "OnGround", new String[]{"OnGround1", "OnGround2", "NoGround", "Stop", "Legit", "Verus", "AAC3", "Collision", "Vulcan"}
      );
      StringValue stringValue = new StringValue(this, "CollisionFallDistance", 2.0, 0.0, 6.0, 2);
      String str = "FallDistance";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> {
         return this.mode.getSelectedValue().equals("Collision");
      };
      this.collisionFallDistance = stringValue2.visibleWhen(condition);
      StringValue stringValue3 = new StringValue(this, "MinFallDist", 2.0, 1.0, 24.0, 0);
      SettingVisibilityCondition condition2 = () -> this.isMinFallDistVisible();
      this.minFallDist = stringValue3.visibleWhen(condition2);
      StringValue stringValue4 = new StringValue(this, "MinLegitFallDist", 6.0, 2.0, 24.0, 1);
      String str2 = "MinFallDist";
      StringValue stringValue5 = stringValue4.named(str2);
      SettingVisibilityCondition condition3 = () -> this.isMinLegitFallDistVisible();
      this.minLegitFallDist = stringValue5.visibleWhen(condition3);
      DoubleValue doubleValue = new DoubleValue(this, "Rotations", true);
      SettingVisibilityCondition condition4 = () -> this.isRotationsVisible();
      this.rotations = doubleValue.visibleWhen(condition4);
      StringValue stringValue6 = new StringValue(this, "Delay", 110.0, 0.0, 2000.0, 0);
      SettingVisibilityCondition condition5 = () -> {
         return this.mode.getSelectedValue().equals("Legit");
      };
      this.delay = stringValue6.visibleWhen(condition5);
      BooleanValue booleanValue = new BooleanValue(this, "UseWebs", true);
      SettingVisibilityCondition condition6 = () -> this.isUseWebsVisible();
      this.useWebs = (BooleanValue)booleanValue.withVisibility(condition6);
      BooleanValue booleanValue2 = new BooleanValue(this, "SneakWhileItemPlace", true);
      SettingVisibilityCondition condition7 = () -> this.isSneakWhileItemPlaceVisible();
      this.sneakWhileItemPlace = (BooleanValue)booleanValue2.withVisibility(condition7);
      BooleanValue booleanValue3 = new BooleanValue(this, "SilentSlotSwitching", true);
      SettingVisibilityCondition condition8 = () -> {
         return this.mode.getSelectedValue().equals("Legit");
      };
      this.silentSlotSwitching = (BooleanValue)booleanValue3.withVisibility(condition8);
      this.blinkMode = new BooleansSetting(this, "BlinkMode", "None", new String[]{"None", "OnlyIncoming", "OnlyOutgoing", "Both"});
      StringValue stringValue7 = new StringValue(this, "BlinkStartFallDistance", 3.0, 1.0, 6.0, 2);
      SettingVisibilityCondition condition9 = () -> {
         return !this.blinkMode.getSelectedValue().equals("None");
      };
      this.blinkStartFallDistance = stringValue7.visibleWhen(condition9);
      this.rotationEngine = new RotationEngine(this, this.rotations);
      this.lastAimedBlockPos = null;
      this.collisionFloorY = 0.0;
      this.bucketHotbarSlot = 0;
      this.vulcanResetStage = 0;
      this.timeHelper = new TimeHelper();
   }

   public static void init() {
   }

   @Override
   public void onEnable() {
      if (mc.thePlayer != null) {
         this.bucketHotbarSlot = mc.thePlayer.inventory.currentItem;
         String selectedValue = this.mode.getSelectedValue();
         this.setSuffix(selectedValue);
      }
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
      byte b3 = 26;
      this.toggleEventSubscription(b3);
      byte b4 = 34;
      this.toggleEventSubscription(b4);
      byte b5 = 3;
      this.toggleEventSubscription(b5);
      byte b6 = 20;
      this.toggleEventSubscription(b6);
      byte b7 = 43;
      this.toggleEventSubscription(b7);
      byte b8 = 25;
      this.toggleEventSubscription(b8);
      byte b9 = 21;
      this.toggleEventSubscription(b9);
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      EntityPlayerSP thePlayer = mc.thePlayer;
      if (mc.theWorld != null && thePlayer != null && thePlayer.ticksExisted >= 100) {
         if ((double)thePlayer.clientFallDistance > this.blinkStartFallDistance.getValue() && (this.blinkMode.getSelectedValue().equals("OnlyIncoming") || this.blinkMode.getSelectedValue().equals("Both"))) {
            boolean flag = true;
            event.setDelayed(flag);
         }
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      EntityPlayerSP thePlayer = mc.thePlayer;
      if (mc.theWorld != null && thePlayer != null && thePlayer.ticksExisted >= 100) {
         if ((double)thePlayer.clientFallDistance > this.blinkStartFallDistance.getValue() && (this.blinkMode.getSelectedValue().equals("OnlyOutgoing") || this.blinkMode.getSelectedValue().equals("Both"))) {
            boolean flag = true;
            event.setDelayed(flag);
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.mode.getSelectedValue().equals("Legit") && this.sneakWhileItemPlace.getValue() && mc.isNotInGui() && this.bucketHotbarSlot != -1) {
         boolean flag = true;
         event.setSneak(flag);
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (mc.thePlayer != null && mc.theWorld != null) {
         String selectedValue = this.mode.getSelectedValue();
         switch (selectedValue) {
            case "NoGround":
               if (packet instanceof C03PacketPlayer) {
                  C03PacketPlayer packet2 = (C03PacketPlayer)packet;
                  packet2.setOnGround(false);
                  mc.thePlayer.fallDistance = -1.0F;
               }
               break;
            case "Verus":
               if (packet instanceof C03PacketPlayer && mc.thePlayer.fallDistance > 2.0F) {
                  if (!(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) && !(packet instanceof C03PacketPlayer.C05PacketPlayerLook) && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition)) {
                     C03PacketPlayer packet3 = (C03PacketPlayer)packet;
                     packet3.setOnGround(true);
                     mc.thePlayer.fallDistance = 0.0F;
                  } else {
                     event.cancel();
                     mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ, true));
                     mc.thePlayer.fallDistance = 0.0F;
                  }
               }
               break;
            case "OnGround2":
               if (mc.thePlayer.fallDistance > 3.0F && packet instanceof C03PacketPlayer) {
                  C03PacketPlayer packet4 = (C03PacketPlayer)packet;
                  packet4.setOnGround(true);
               }
               break;
            case "Vulcan":
               if (packet instanceof C03PacketPlayer) {
                  C03PacketPlayer packet5 = (C03PacketPlayer)packet;
                  if (mc.thePlayer.fallDistance > 3.0F) {
                     packet5.setOnGround(true);
                     mc.thePlayer.fallDistance = 0.0F;
                     this.vulcanResetStage = 1;
                  }

                  if (this.vulcanResetStage == 1) {
                     mc.thePlayer.motionY = -0.011;
                     this.vulcanResetStage = 0;
                  }
               }
         }
      }
   }

   @Override
   public void onBlockBoundingBox(EventBlockBoundingBox event) {
      if (this.mode.getSelectedValue().equals("Collision")
         && mc.thePlayer != null
         && this.isLicenseValid()
         && (double)mc.thePlayer.fallDistance > this.collisionFallDistance.getValue()
         && event.getBlock() instanceof BlockAir
         && (double)(event.getBlockPos().getY() + 1) < this.collisionFloorY) {
         BlockPos blockPos = event.getBlockPos();
         AxisAlignedBB box = new AxisAlignedBB(
            (double)blockPos.getX(),
            (double)blockPos.getY(),
            (double)blockPos.getZ(),
            (double)(blockPos.getX() + 1),
            (double)(blockPos.getY() + 1),
            (double)(blockPos.getZ() + 1)
         );
         event.setBoundingBox(box);
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      if (this.mode.getSelectedValue().equals("Collision") && (double)mc.thePlayer.fallDistance <= this.collisionFallDistance.getValue()) {
         this.collisionFloorY = mc.thePlayer.posY + event.getY();
      }
   }

   @Override
   public void onTick2() {
      if (mc.thePlayer != null) {
         String selectedValue = this.mode.getSelectedValue();
         switch (selectedValue) {
            case "OnGround1":
               if ((double)mc.thePlayer.fallDistance > this.minFallDist.getValue()) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
               }
               break;
            case "Stop":
               if (mc.thePlayer.fallDistance > 2.0F) {
                  mc.thePlayer.motionY = 0.0;
                  mc.thePlayer.onGround = true;
                  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                  mc.thePlayer.fallDistance = 0.0F;
               }
               break;
            case "AAC3":
               if (!mc.isSingleplayer() && mc.thePlayer.fallDistance > 2.0F) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ, false));
                  mc.thePlayer.fallDistance = -99999.0F;
               }
         }
      }
   }

   @Override
   public void onTick() {
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
      if (mc.thePlayer != null && this.mode.getSelectedValue().equals("Legit")) {
         boolean flag = false;
         IBlockState blockState = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
         Block block = blockState.getBlock();
         if (block instanceof BlockLiquid) {
            flag = (Integer)blockState.getValue(BlockLiquid.LEVEL) == 0;
         }

         this.predictedLandingBlockPos = this.predictLandingBlock();
         if (!((double)mc.thePlayer.fallDistance > this.minLegitFallDist.getValue())) {
            if (!mc.thePlayer.isInWater() || !flag) {
               return;
            }

            long l = 7000L;
            if (this.timeHelper.reached2(l)) {
               return;
            }
         }

         if (this.predictedLandingBlockPos != null && this.findSaveItemHotbarSlot() != -1) {
            RotationEngine rotationEngine2 = this.rotationEngine;
            float rotationYaw = mc.thePlayer.rotationYaw;
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            float f = 90.0F;
            float f2 = rotationYaw;
            Rotation rotation = rotationEngine2.rotateTowards(f2, f, previousRotation);
            byte b = 4;
            ClientManagers.rotationController.requestRotation(rotation, b, this);
         }
      }
   }

   @Override
   public void onSilentSlot() {
      this.bucketHotbarSlot = -1;
      if (mc.thePlayer != null && this.mode.getSelectedValue().equals("Legit")) {
         boolean flag = false;
         IBlockState blockState = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
         Block block = blockState.getBlock();
         if (block instanceof BlockLiquid) {
            flag = (Integer)blockState.getValue(BlockLiquid.LEVEL) == 0;
         }

         exitBlock: {
            if (!((double)mc.thePlayer.fallDistance > this.minLegitFallDist.getValue())) {
               if (!mc.thePlayer.isInWater() || !flag) {
                  break exitBlock;
               }

               long l = 7000L;
               if (this.timeHelper.reached2(l)) {
                  break exitBlock;
               }
            }

            if (this.predictedLandingBlockPos != null && this.findSaveItemHotbarSlot() != -1 && mc.objectMouseOver != null) {
               if (mc.thePlayer.fallDistance == 0.0F && flag && this.lastAimedBlockPos != null && mc.objectMouseOver.getBlockPos() != null && this.lastAimedBlockPos.equalsPos(mc.objectMouseOver.getBlockPos())) {
                  this.bucketHotbarSlot = this.findSaveItemHotbarSlot();
               }

               if ((double)mc.thePlayer.fallDistance > this.minLegitFallDist.getValue() && !flag) {
                  Rotation currentRotation = ClientManagers.rotationController.getCurrentRotation();
                  double d = 0.0;
                  double d2 = 10.0;
                  double d3 = 10.0;
                  if (RayTraceUtil.rayTrace(1.0F, currentRotation, d3, d2, d).typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                     this.bucketHotbarSlot = this.findSaveItemHotbarSlot();
                  }
               }
            }
         }

         if (this.bucketHotbarSlot != -1) {
            int bucketHotbarSlot2 = this.bucketHotbarSlot;
            boolean value = this.silentSlotSwitching.getValue();
            int n = bucketHotbarSlot2;
            byte b = 1;
            SilentSlotManager.requestSlot(this, b, n, value);
         }
      }

      this.lastAimedBlockPos = mc.objectMouseOver == null ? null : mc.objectMouseOver.getBlockPos();
   }

   @Override
   public void onClick(EventClick event) {
      this.ticksSinceLastPlace++;
      if (this.mode.getSelectedValue().equals("Legit") && this.predictedLandingBlockPos != null && ClientManagers.rotationController.isOwnedBy(this) && mc.objectMouseOver != null) {
         if (this.bucketHotbarSlot != -1 && this.ticksSinceLastPlace > 2 && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            byte b = 1;
            event.addRightClicks(b);
            if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == Items.water_bucket) {
               this.timeHelper.reset();
            }

            this.ticksSinceLastPlace = 0;
         }
      }
   }

   private BlockPos predictLandingBlock() {
      double posX = mc.thePlayer.posX;
      double posY = mc.thePlayer.posY;
      double posZ = mc.thePlayer.posZ;
      double motionX = mc.thePlayer.motionX;
      double motionY = mc.thePlayer.motionY;
      double motionZ = mc.thePlayer.motionZ;
      boolean jumping = mc.thePlayer.jumping;

      for (int i = 0; i < 200; i++) {
         double[] doubles = PlayerUtil.simulateSingleTickMotion(mc.thePlayer.movementInput.moveForward, mc.thePlayer.movementInput.moveStrafe, motionX, motionY, motionZ, posX, posY, posZ, jumping);
         jumping = false;
         posX = doubles[0];
         posY = doubles[1];
         posZ = doubles[2];
         motionX = doubles[3];
         motionY = doubles[4];
         motionZ = doubles[5];
         BlockPos pos = new BlockPos(posX, posY, posZ);
         Block block = mc.theWorld.getBlockState(pos).getBlock();
         if (block instanceof BlockLiquid) {
            return null;
         }

         if (hasSolidCollisionBox(pos)) {
            return pos;
         }
      }

      return null;
   }

   private static boolean hasSolidCollisionBox(BlockPos pos) {
      IBlockState blockState = mc.theWorld.getBlockState(pos);
      Block block = blockState.getBlock();
      return block.getCollisionBoundingBox(mc.theWorld, blockState, pos.getX(), pos.getY(), pos.getZ()) != null;
   }

   private int findSaveItemHotbarSlot() {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      byte b = 36;
      containerSlotQuery = containerSlotQuery.fromSlot(b);
      Predicate predicate = obj -> {
         return obj != null
            && (
               obj.equals(Item.getItemById(326)) && !mc.thePlayer.isInWater()
                  || obj.equals(Item.getItemById(325)) && mc.thePlayer.fallDistance == 0.0F && mc.thePlayer.isInWater()
            );
      };
      OptionalInt optionalInt = containerSlotQuery.filterItem(predicate).findFirstSlotIndex();
      if (optionalInt.isPresent()) {
         return optionalInt.getAsInt() - 36;
      } else {
         if (this.useWebs.getValue()) {
            containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
            byte b2 = 36;
            containerSlotQuery = containerSlotQuery.fromSlot(b2);
            Predicate predicate2 = obj2 -> {
               return obj2 != null && obj2.equals(Item.getItemById(30)) && !mc.thePlayer.isInWater();
            };
            OptionalInt optionalInt2 = containerSlotQuery.filterItem(predicate2).findFirstSlotIndex();
            if (optionalInt2.isPresent()) {
               return optionalInt2.getAsInt() - 36;
            }
         }

         return -1;
      }
   }

   private boolean isSneakWhileItemPlaceVisible() {
      return this.mode.getSelectedValue().equals("Legit");
   }

   private boolean isUseWebsVisible() {
      return this.mode.getSelectedValue().equals("Legit");
   }

   private boolean isRotationsVisible() {
      return this.mode.getSelectedValue().equals("Legit");
   }

   private boolean isMinLegitFallDistVisible() {
      return this.mode.getSelectedValue().equals("Legit");
   }

   private boolean isMinFallDistVisible() {
      return this.mode.getSelectedValue().equals("OnGround1");
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
