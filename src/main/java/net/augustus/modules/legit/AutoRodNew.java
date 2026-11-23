package net.augustus.modules.legit;

import net.augustus.utils.render.ShaderRenderPass;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.rotation.RotationEngine;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.WeaponFilterSettings;
import net.augustus.settings.LookDownDisableSettings;
import net.augustus.events.EventMove;
import net.augustus.utils.simulation.TrajectoryPredictionUtil;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.rotation.EntityBoundsUtil;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.events.EventMoveInput;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.Augustus;
import net.augustus.utils.simulation.ProjectileSimulator;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class AutoRodNew extends Module implements LoadWorldListener, MoveListener, ClickListener, TickListener, MoveInputListener, RenderListener, SilentSlotListener {
   private final DoubleValue rotations;
   private final StringValue maxTargetLookFovDiff;
   public final StringValue fovYaw;
   public final StringValue fovPitch;
   public final StringValue maxRodRange;
   public final BooleanValue moveFix;
   private final TargetSelectionSettings targetSelectionSettings;
   public final BooleanValue ignoreEatingTargets;
   public final BooleanValue combat;
   public final BooleanValue onlyRodIfNotInReach;
   public final BooleanValue luckyThrow;
   public final BooleanValue click;
   public final BooleanValue silentSlotSwitching;
   public final BooleanValue disableWhileScaffold;
   public final BooleanValue onlyWhileKillAuraHasTarget;
   public final BooleanValue rightClickIfRodOnTarget;
   public final WeaponFilterSettings weaponWhitelist;
   public final LookDownDisableSettings lookDownDisableSettings;
   public final BooleanValue esp;
   public final ColorSetting espColor;
   public final BlurShadowSettings blurShadowSettings;
   private final TimeHelper timeHelper;
   private int ticksSinceCast;
   private int selectedHotbarSlot;
   private EntityLivingBase target;
   private Vec3 predictedTargetPos;
   private int predictedFlightTicks;
   private boolean rodCast;
   private boolean pendingRightClickReel;
   private boolean needsFallbackSlotUpdate;
   private boolean hookHitTarget;
   private int fallbackHotbarSlot;
   private final RotationEngine rotationEngine;
   private AxisAlignedBB predictedTargetBox;

   public AutoRodNew() {
       super("AutoRodNew", "AutomatischeAngel", new Color(203, 232, 162), Categorys.LEGIT);
      this.rotations = new DoubleValue(this, "Rotations", true);
      this.maxTargetLookFovDiff = new StringValue(this, "MaxTargetLookFovDiff", 180.0, 20.0, 180.0, 0);
      this.fovYaw = (StringValue)(new StringValue(this, "FovYaw", 90.0, 0.0, 180.0, 0)).markVisualOnly();
      this.fovPitch = (StringValue)(new StringValue(this, "FovPitch", 90.0, 0.0, 180.0, 0)).markVisualOnly();
      this.maxRodRange = new StringValue(this, "MaxRodRange", 10.0, 0.0, 20.0, 2);
      this.moveFix = new BooleanValue(this, "MoveFix", true);
      this.targetSelectionSettings = (new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter())).withSorting();
      this.ignoreEatingTargets = new BooleanValue(this, "IgnoreEatingTargets", true);
      this.combat = new BooleanValue(this, "Combat", true);
      this.onlyRodIfNotInReach = new BooleanValue(this, "OnlyRodIfNotInReach", false);
      this.luckyThrow = new BooleanValue(this, "LuckyThrow", false);
      this.click = new BooleanValue(this, "Click", true);
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.disableWhileScaffold = new BooleanValue(this, "DisableWhileScaffold", true);
      this.onlyWhileKillAuraHasTarget = new BooleanValue(this, "OnlyWhileKillAuraHasTarget", false);
      this.rightClickIfRodOnTarget = new BooleanValue(this, "RightClickIfRodOnTarget", false);
      this.weaponWhitelist = new WeaponFilterSettings(this, "WeaponWhitelist");
      this.lookDownDisableSettings = new LookDownDisableSettings(this);
      this.esp = new BooleanValue(this, "ESP", true);
      ColorSetting colorSetting = new ColorSetting(this, "ESPColor", Color.GREEN);
      BooleanValue esp2 = this.esp;
      this.esp.getClass();
      BooleanValue booleanValue = esp2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.espColor = (ColorSetting)colorSetting.withVisibility(condition);
      BlurShadowSettings blurShadowSettings2 = (new BlurShadowSettings(this)).disableBlurByDefault();
      Color black = Color.black;
      this.blurShadowSettings = blurShadowSettings2.withCustomShadowColor(black);
      this.timeHelper = new TimeHelper();
      this.ticksSinceCast = 0;
      this.selectedHotbarSlot = -1;
      this.target = null;
      this.predictedTargetPos = null;
      this.predictedFlightTicks = 0;
      this.rodCast = false;
      this.pendingRightClickReel = false;
      this.needsFallbackSlotUpdate = false;
      this.hookHitTarget = false;
      this.fallbackHotbarSlot = -1;
      this.rotationEngine = new RotationEngine(this, this.rotations);
   }

   @Override
   public void onEnable() {
      this.predictedFlightTicks = 0;
      this.rodCast = false;
      this.needsFallbackSlotUpdate = true;
   }

   @Override
   public void onDisable() {
      this.target = null;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 28;
      this.toggleEventSubscription(b);
      byte b2 = 7;
      this.toggleEventSubscription(b2);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
      byte b4 = 21;
      this.toggleEventSubscription(b4);
      byte b5 = 32;
      this.toggleEventSubscription(b5);
      byte b6 = 34;
      this.toggleEventSubscription(b6);
      byte b7 = 17;
      this.toggleEventSubscription(b7);
   }

   @Override
   public void onTick() {
      if (!mc.gameSettings.keyBindAttack.isKeyDown()) {
         this.timeHelper.reset();
      }

      EntityLivingBase target2 = this.target;
      this.targetSelectionSettings.setCurrentTarget(target2);
      this.target = null;
      this.predictedTargetBox = null;
      if (this.canUseRod() && (!ClientManagers.moduleManager.scaffold.isToggled() || !this.disableWhileScaffold.getValue())) {
         this.target = this.findTarget();
         if (this.target != null) {
            double d = (double)RotationUtil.getEntityLookAngleToPlayer(this.target);
            if (d > this.maxTargetLookFovDiff.getValue()) {
               this.target = null;
               return;
            }

            byte b = 8;
            EntityLivingBase target3 = this.target;
            this.predictedFlightTicks = this.estimateImpactTicks(target3, b);
            if (this.predictedFlightTicks < 0 || this.predictedFlightTicks >= 10) {
               return;
            }

            int n = mc.thePlayer.ticksExisted + this.predictedFlightTicks;
            boolean flag = true;
            int n2 = n;
            int ticksExisted = mc.thePlayer.ticksExisted;
            EntityLivingBase target4 = this.target;
            ArrayList list = TrajectoryPredictionUtil.predictTargetPositions(mc.thePlayer, target4, ticksExisted, n2, flag);
            int n3 = 99999;
            Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list, n3);
            if (this.combat.getValue() && this.onlyRodIfNotInReach.getValue()) {
               if (!this.isTargetOutOfReachDuringFlight()) {
                  return;
               }
            }

            double d2 = (double)(this.target.width / 2.0F + 0.1F);
            AxisAlignedBB box = new AxisAlignedBB(
               clampedPosition.xCoord - d2,
               clampedPosition.yCoord,
               clampedPosition.zCoord - d2,
               clampedPosition.xCoord + d2,
               clampedPosition.yCoord + (double)(this.target.height / 2.0F),
               clampedPosition.zCoord + d2
            );
            this.predictedTargetPos = clampedPosition;
            int n4 = mc.thePlayer.ticksExisted + 1;
            byte b2 = 0;
            int ticksExisted2 = mc.thePlayer.ticksExisted;
            int ticksExisted3 = mc.thePlayer.ticksExisted;
            Vec3 vec = (Vec3)TrajectoryPredictionUtil.predictPlayerPositions(mc.thePlayer, ticksExisted3, ticksExisted2, b2, n4).get(0);
            double d3 = clampedPosition.xCoord - vec.xCoord;
            double d4 = clampedPosition.yCoord + (double)this.target.height / 1.7 - (vec.yCoord + (double)mc.thePlayer.getEyeHeight());
            double d5 = clampedPosition.zCoord - vec.zCoord;
            float f = (float)Math.toDegrees(Math.atan2(d5, d3)) - 90.0F;
            double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d5 * d5);
            double distanceToEntity = (double)mc.thePlayer.getDistanceToEntity(this.target);
            float f2 = (float)((double)((float)(-(Math.atan2(d4, d6) * 180.0 / Math.PI))) - 0.55F * Math.max(0.0, distanceToEntity - 3.0));
            RotationEngine rotationEngine2 = this.rotationEngine;
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            Rotation rotation = rotationEngine2.rotateTowards(f, f2, previousRotation);
            byte b3 = 0;
            ClientManagers.rotationController.requestRotation(rotation, b3, this);
            this.predictedTargetBox = box;
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      this.selectedHotbarSlot = this.findRodHotbarSlot();
      if (this.target != null) {
         EntityFishHook entityFishHook;
         entityFishHook = this.findOwnFishHook();
         exitBlock:
         if (entityFishHook == null && !this.rodCast && this.target.hurtTime < this.predictedFlightTicks && this.predictedTargetBox != null) {
            if (this.combat.getValue() && this.onlyRodIfNotInReach.getValue()) {
               if (!this.isTargetOutOfReachDuringFlight()) {
                  break exitBlock;
               }
            }

            if (this.luckyThrow.getValue() || this.willCurrentRotationHitTarget()) {
               byte b = 1;
               event.addRightClicks(b);
               this.ticksSinceCast = 0;
               this.rodCast = true;
               this.needsFallbackSlotUpdate = true;
               this.hookHitTarget = false;
            }
         }

         if (entityFishHook != null && this.rightClickIfRodOnTarget.getValue() && !this.rodCast && this.ticksSinceCast > 3 && this.pendingRightClickReel) {
            byte b2 = 1;
            event.addRightClicks(b2);
            this.pendingRightClickReel = false;
         }
      }
   }

   private boolean isTargetOutOfReachDuringFlight() {
      int n = mc.thePlayer.ticksExisted + this.predictedFlightTicks;
      byte b = 0;
      int ticksExisted = mc.thePlayer.ticksExisted;
      int ticksExisted2 = mc.thePlayer.ticksExisted;
      ArrayList list = TrajectoryPredictionUtil.predictPlayerPositions(mc.thePlayer, ticksExisted2, ticksExisted, b, n);
      int n2 = mc.thePlayer.ticksExisted + this.predictedFlightTicks;
      int ticksExisted3 = mc.thePlayer.ticksExisted;
      EntityLivingBase target2 = this.target;
      ArrayList list2 = TrajectoryPredictionUtil.predictTargetPositions(mc.thePlayer, target2, ticksExisted3, n2);
      int n3 = Math.max(list.size(), list2.size());
      double eyeHeight = (double)mc.thePlayer.getEyeHeight();

      for (int i = 0; i < n3; i++) {
         Vec3 vec = TrajectoryPredictionUtil.getClampedPosition(list, i).addVector(0.0, eyeHeight, 0.0);
         Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list2, i);
         boolean flag = true;
         AxisAlignedBB entityBoundingBoxAt = EntityBoundsUtil.getEntityBoundingBoxAt(this.target, clampedPosition, flag);
         if (RotationUtil.clampPointToBox(vec, entityBoundingBoxAt).distanceTo(vec) <= 3.0) {
            return false;
         }
      }

      return true;
   }

   private boolean willCurrentRotationHitTarget() {
      ProjectileSimulator projectileSimulator = new ProjectileSimulator();
      EntityPlayerSP thePlayer = mc.thePlayer;
      ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(this.findRodHotbarSlot() + 36).getStack();
      float lastReportedPitch = mc.thePlayer.lastReportedPitch;
      float lastReportedYaw = mc.thePlayer.lastReportedYaw;
      ItemStack stack2 = stack;
      EntityPlayerSP player = thePlayer;
      projectileSimulator.initFromShooterAngles(player, stack2, lastReportedYaw, lastReportedPitch);
      AxisAlignedBB predictedTargetBox2 = this.predictedTargetBox;
      EntityLivingBase target2 = this.target;
      byte b = 100;
      return projectileSimulator.willFishingHookHitTarget(b, target2, predictedTargetBox2);
   }

   @Override
   public void onSilentSlot() {
      this.selectedHotbarSlot = this.findRodHotbarSlot();
      if (this.target != null) {
         EntityFishHook entityFishHook = this.findOwnFishHook();
         if (entityFishHook != null) {
            BlockPos pos = new BlockPos(entityFishHook.posX, entityFishHook.posY + 0.5, entityFishHook.posZ);
            Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
            double distanceToEntityBox = (double)mc.thePlayer.getDistanceToEntityBox(this.target);
            double distanceToEntityBox2 = (double)mc.thePlayer.getDistanceToEntityBox(entityFishHook);
            Vec3 vec = new Vec3((double)((float)entityFishHook.serverPosX / 32.0F), (double)((float)entityFishHook.serverPosY / 32.0F), (double)((float)entityFishHook.serverPosZ / 32.0F));
            if (distanceToEntityBox2 > distanceToEntityBox
               || positionEyes.distanceTo(vec) > this.maxRodRange.getValue()
               || entityFishHook.onGround
               || entityFishHook.inGround
               || this.target.hurtTime == 10
               || entityFishHook.isDead
               || mc.theWorld.getBlockState(pos.down()).getBlock().isFullBlock()) {
               if (this.target.hurtTime == 10) {
                  this.hookHitTarget = true;
               }

               boolean flag = true;
               AxisAlignedBB entityBoundingBoxAt = EntityBoundsUtil.getEntityBoundingBoxAt(entityFishHook, vec, flag);
               EntityLivingBase target2 = this.target;
               Vec3 vec2 = new Vec3(
                  (double)((float)this.target.serverPosX / 32.0F), (double)((float)this.target.serverPosY / 32.0F), (double)((float)this.target.serverPosZ / 32.0F)
               );
               boolean flag2 = true;
               Vec3 vec3 = vec2;
               if (entityBoundingBoxAt.intersectsWith(EntityBoundsUtil.getEntityBoundingBoxAt(target2, vec3, flag2))) {
                  this.hookHitTarget = true;
               }

               if (this.ticksSinceCast > 3) {
                  if (!this.hookHitTarget || !this.rightClickIfRodOnTarget.getValue()) {
                     this.selectedHotbarSlot = this.getFallbackHotbarSlot();
                  } else if (this.rodCast) {
                     this.pendingRightClickReel = true;
                  }

                  this.rodCast = false;
               }
            }
         }
      } else if (this.hasActiveFishHook() && this.ticksSinceCast > 3) {
         this.selectedHotbarSlot = this.getFallbackHotbarSlot();
         this.rodCast = false;
      }

      exitBlock: {
         if (this.selectedHotbarSlot == this.findRodHotbarSlot()) {
            if (mc.playerController.currentPlayerItem == this.findRodHotbarSlot()) {
               break exitBlock;
            }
         }

         this.rodCast = false;
      }

      exitBlock2: {
         if (mc.thePlayer.inventory.currentItem == this.findRodHotbarSlot() || this.combat.getValue()) {
            if (!this.combat.getValue()) {
               break exitBlock2;
            }

            if (this.selectedHotbarSlot == this.findRodHotbarSlot()) {
               break exitBlock2;
            }
         }

         if (this.hasActiveFishHook()) {
            this.selectedHotbarSlot = this.getFallbackHotbarSlot();
            this.rodCast = false;
         }
      }

      this.ticksSinceCast++;
      if (this.target != null && !mc.thePlayer.isUsingItem()) {
         int selectedHotbarSlot2 = this.selectedHotbarSlot;
         boolean value = this.silentSlotSwitching.getValue();
         int n = selectedHotbarSlot2;
         byte b = 2;
         SilentSlotManager.requestSlot(this, b, n, value);
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.target != null && this.moveFix.getValue() && ClientManagers.rotationController.isOwnedBy(this)) {
         MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
         event.setMoveFixMode(moveFixMode);
      }
   }

   @Override
   public void onMove(EventMove event) {
      if (this.target != null && !this.moveFix.getValue() && ClientManagers.rotationController.isOwnedBy(this)) {
         float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
         event.setYaw(realYaw);
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (this.target != null && !this.moveFix.getValue() && ClientManagers.rotationController.isOwnedBy(this)) {
         float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
         event.setYaw(realYaw);
      }
   }

   @Override
   public void onRender(float f) {
      RenderUtil.begin3DRender();
      if (this.esp.getValue() && this.target != null && this.predictedTargetPos != null) {
         float f2 = (float)((double)((float)this.predictedTargetPos.xCoord) - mc.getRenderManager().getRenderPosX());
         float f3 = (float)((double)((float)this.predictedTargetPos.yCoord) - mc.getRenderManager().getRenderPosY());
         float f4 = (float)((double)((float)this.predictedTargetPos.zCoord) - mc.getRenderManager().getRenderPosZ());
         float f5 = this.target.width / 2.0F;
         boolean isBlurActive = this.blurShadowSettings.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> {
            BlurShadowSettings blurShadowSettings2 = this.blurShadowSettings;
            byte b = 0;
            int colorInt = this.espColor.getColorInt(b);
            RenderUtil.setGlColor(blurShadowSettings2.resolvePassColor(shaderRenderPass, colorInt));
            RenderUtil.drawFilledBox2(f2 - f5, f3, f4 - f5, f2 + f5, f3 + this.target.height, f4 + f5);
         });
      }

      RenderUtil.end3DRender();
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.target = null;
      Object obj = null;
      this.targetSelectionSettings.setCurrentTarget((Entity)obj);
   }

   private int estimateImpactTicks(EntityLivingBase entity, int n) {
      ArrayList list = new ArrayList();
      int n2 = 1;
      boolean flag = false;
      double d = (double)(entity.width / 2.0F + 0.1F);
      ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(this.findRodHotbarSlot() + 36).getStack();
      int n3 = mc.thePlayer.ticksExisted + 1;
      byte b = 0;
      int ticksExisted = mc.thePlayer.ticksExisted;
      int ticksExisted2 = mc.thePlayer.ticksExisted;
      ArrayList list2 = TrajectoryPredictionUtil.predictPlayerPositions(mc.thePlayer, ticksExisted2, ticksExisted, b, n3);
      short s = 999;
      Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list2, s);

      for (int i = 0; i < n; i++) {
         ArrayList list3;
         if (n2 <= list.size() && n2 > 0) {
            list3 = list;
         } else {
            int n4 = mc.thePlayer.ticksExisted + n2;
            boolean flag2 = true;
            int n5 = n4;
            int ticksExisted3 = mc.thePlayer.ticksExisted;
            list3 = list = TrajectoryPredictionUtil.predictTargetPositions(mc.thePlayer, entity, ticksExisted3, n5, flag2);
         }

         short s2 = 9999;
         Vec3 clampedPosition2 = TrajectoryPredictionUtil.getClampedPosition(list3, s2);
         double xCoord = clampedPosition2.xCoord;
         double yCoord = clampedPosition2.yCoord;
         double zCoord = clampedPosition2.zCoord;
         double d2 = xCoord - clampedPosition.xCoord;
         double d3 = yCoord + (double)this.target.getEyeHeight() - (clampedPosition.yCoord + (double)mc.thePlayer.getEyeHeight());
         double d4 = zCoord - clampedPosition.zCoord;
         float f = (float)Math.toDegrees(Math.atan2(d4, d2)) - 90.0F;
         float f2 = (float)(-(MathHelper.atan2(d3, (double)MathHelper.sqrt_double(d2 * d2 + d4 * d4)) * 180.0 / Math.PI));
         float f3 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f, 180.0F);
         AxisAlignedBB box = new AxisAlignedBB(xCoord - d, yCoord + 0.2, zCoord - d, xCoord + d, yCoord + (double)entity.height, zCoord + d);

         for (float j = 0.0F; j < 20.0F; j += 0.2F) {
            boolean flag3 = false;

            for (int k = -1; k < 2; k += 2) {
               float f4 = f2 + j * (float)k;
               ProjectileSimulator projectileSimulator = new ProjectileSimulator();
               EntityPlayerSP thePlayer = mc.thePlayer;
               projectileSimulator.initFromShooterAngles(thePlayer, stack, f3, f4);
               byte b2 = 100;
               int n6 = projectileSimulator.simulateFishingHookHitTick(b2, entity, box);
               if (n6 != Integer.MAX_VALUE) {
                  if (!flag && i == n - 1 && n6 <= n2 - 1) {
                     flag = true;
                  }

                  n2 = n6 + 1;
                  flag3 = true;
                  break;
               }
            }

            if (flag3) {
               break;
            }
         }
      }

      if (!flag) {
         n2 = -1000;
      }

      return n2;
   }

   public boolean canUseRod() {
      if ((
            mc.thePlayer.getCurrentEquippedItem() == null
               || !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFood)
                  && !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)
                  && !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemPotion)
                  && !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBucketMilk)
               || !mc.gameSettings.keyBindUseItem.isKeyDown()
         )
         && mc.isNotInGui()
         && (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFishingRod || this.combat.getValue())
         && this.selectedHotbarSlot != -1) {
         if (this.findRodHotbarSlot() != -1 && !mc.gameSettings.keyBindUseItem.isKeyDown()) {
            long l = 100L;
            if ((this.timeHelper.reached2(l) || !this.click.getValue())
               && (!this.onlyWhileKillAuraHasTarget.getValue() || ClientManagers.moduleManager.killAura.isToggled() && ClientManagers.moduleManager.killAura.target != null)) {
               ItemStack heldItemStack = SilentSlotManager.heldItemStack;
               if (this.weaponWhitelist.isItemAllowed(heldItemStack) && !this.lookDownDisableSettings.shouldDisable()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean hasActiveFishHook() {
      return this.findOwnFishHook() != null;
   }

   private EntityFishHook findOwnFishHook() {
      if (mc.thePlayer != null && mc.thePlayer.fishEntity != null) {
         return mc.thePlayer.fishEntity;
      } else if (mc.theWorld == null) {
         return null;
      } else {
         for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
            if (entity instanceof EntityFishHook) {
               EntityFishHook entityFishHook = (EntityFishHook)entity;
               if (entityFishHook.angler.getName().equals(mc.session.getUsername())) {
                  return entityFishHook;
               }
            }
         }

         return null;
      }
   }

   public int findRodHotbarSlot() {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      byte b = 36;
      containerSlotQuery = containerSlotQuery.fromSlot(b);
      Predicate predicate = obj -> obj instanceof ItemFishingRod;
      OptionalInt optionalInt = containerSlotQuery.filterItem(predicate).findFirstSlotIndex();
      return optionalInt.isPresent() ? optionalInt.getAsInt() - 36 : -1;
   }

   private int getFallbackHotbarSlot() {
      if (!this.needsFallbackSlotUpdate) {
         return this.fallbackHotbarSlot;
      } else {
         this.needsFallbackSlotUpdate = false;
         this.fallbackHotbarSlot = -1;
         int n = this.findRodHotbarSlot();
         if (SilentSlotManager.realHotbarSlot != n) {
            this.fallbackHotbarSlot = SilentSlotManager.realHotbarSlot;
         } else {
            ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
            byte b = 36;
            OptionalInt optionalInt = containerSlotQuery.fromSlot(b).emptyOnly().findFirstSlotIndex();
            if (optionalInt.isPresent()) {
               this.fallbackHotbarSlot = optionalInt.getAsInt() - 36;
            } else {
               containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
               byte b2 = 36;
               containerSlotQuery = containerSlotQuery.fromSlot(b2);
               Predicate predicate = obj -> {
                  return !(obj instanceof ItemFood) && !(obj instanceof ItemSkull);
               };
               OptionalInt optionalInt2 = containerSlotQuery.filterItem(predicate).findFirstSlotIndex();
               if (optionalInt2.isPresent()) {
                  this.fallbackHotbarSlot = optionalInt2.getAsInt() - 36;
               } else {
                  int n2 = 0;

                  do {
                     byte b3 = 8;
                     byte b4 = 0;
                     this.fallbackHotbarSlot = DefaultRandomGenerator.instance.nextInt2(b4, b3);
                  } while (this.fallbackHotbarSlot == n && ++n2 < 10);
               }
            }
         }

         return this.fallbackHotbarSlot;
      }
   }

   public EntityLivingBase getTarget() {
      return this.target;
   }

   private EntityLivingBase findTarget() {
      EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
      EntityTargetFilter targetFilter = this.targetSelectionSettings.getTargetFilter();
      EntityTargetFilter entityTargetFilter = targetFilter;
      entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter);
      boolean flag = false;
      entityTargetSelector = entityTargetSelector.throughWalls(flag);
      double value = this.maxRodRange.getValue();
      entityTargetSelector = entityTargetSelector.maxDistance(value);
      double value2 = this.fovYaw.getValue();
      double value3 = this.fovPitch.getValue();
      double d = value2;
      entityTargetSelector = entityTargetSelector.withinFov(d, value3).filterFocus();
      Predicate predicate = obj -> this.isValidTargetState((Entity)obj);
      entityTargetSelector = entityTargetSelector.filter(predicate);
      TargetSelectionSettings targetSelectionSettings2 = this.targetSelectionSettings;
      Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
      double d2 = -1.0;
      Rotation rotation = previousRotation;
      ToDoubleFunction toDoubleFunction = targetSelectionSettings2.createTargetComparatorKey(rotation, d2);
      Optional optional = entityTargetSelector.sortBy(toDoubleFunction).findBest();
      EntityLivingBase entity = optional.isPresent() && optional.get() instanceof EntityLivingBase ? (EntityLivingBase)optional.get() : null;
      if (this.combat.getValue() && entity != null && (double)mc.thePlayer.getDistanceToEntityBox(entity) < 3.2) {
         entity = null;
      }

      return entity;
   }

   private boolean isValidTargetState(Entity entity) {
      return !entity.isEating() || !this.ignoreEatingTargets.getValue();
   }

}
