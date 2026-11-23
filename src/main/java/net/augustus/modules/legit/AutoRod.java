package net.augustus.modules.legit;

import java.awt.Color;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.rotation.RotationEngine;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.WeaponFilterSettings;
import net.augustus.settings.LookDownDisableSettings;
import net.augustus.utils.simulation.EntityMovementPredictor;
import net.augustus.events.EventMove;
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
import net.augustus.utils.rotation.RotationController;
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

public class AutoRod extends Module implements LoadWorldListener, MoveListener, ClickListener, TickListener, MoveInputListener, RenderListener, SilentSlotListener {
   private final DoubleValue rotations;
   private final StringValue maxTargetLookFovDiff;
   public final StringValue fovYaw;
   public final StringValue fovPitch;
   public final StringValue maxRodRange;
   public final BooleanValue moveFix;
   private final TargetSelectionSettings targetSelectionSettings;
   public final BooleanValue ignoreEatingTargets;
   public final BooleanValue combat;
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
   private int fallbackHotbarSlot;
   private boolean hookHitTarget;
   private final RotationEngine rotationEngine;

   public AutoRod() {
       super("AutoRod", "AutomatischeAngel", new Color(203, 232, 162), Categorys.LEGIT);
      this.rotations = new DoubleValue(this, "Rotations", true);
      this.maxTargetLookFovDiff = new StringValue(this, "MaxTargetLookFovDiff", 180.0, 20.0, 180.0, 0);
      this.fovYaw = (StringValue)(new StringValue(this, "FovYaw", 90.0, 0.0, 180.0, 0)).markVisualOnly();
      this.fovPitch = (StringValue)(new StringValue(this, "FovPitch", 90.0, 0.0, 180.0, 0)).markVisualOnly();
      this.maxRodRange = new StringValue(this, "MaxRodRange", 10.0, 0.0, 20.0, 2);
      this.moveFix = new BooleanValue(this, "MoveFix", true);
      this.targetSelectionSettings = (new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter())).withSorting();
      this.ignoreEatingTargets = new BooleanValue(this, "IgnoreEatingTargets", true);
      this.combat = new BooleanValue(this, "Combat", true);
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
      this.fallbackHotbarSlot = -1;
      this.hookHitTarget = false;
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
      if (this.canUseRod() && (!ClientManagers.moduleManager.scaffold.isToggled() || !this.disableWhileScaffold.getValue())) {
         this.target = this.findTarget();
         if (this.target != null) {
            double d = (double)RotationUtil.getEntityLookAngleToPlayer(this.target);
            if (d > this.maxTargetLookFovDiff.getValue()) {
               this.target = null;
               return;
            }

            double[] doubles = new double[]{this.target.posX, this.target.posY, this.target.posZ};
            byte b = 4;
            EntityLivingBase target3 = this.target;
            this.predictedFlightTicks = this.estimateImpactTicks(target3, b) + 2;
            if (this.predictedFlightTicks >= 0) {
               int predictedFlightTicks2 = this.predictedFlightTicks;
               doubles = EntityMovementPredictor.predictFullState(this.target, predictedFlightTicks2);
            }

            this.predictedTargetPos = new Vec3(doubles[0], doubles[1], doubles[2]);
            double d2 = doubles[0] - mc.thePlayer.posX;
            double d3 = doubles[1] + (double)this.target.height / 1.7 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
            double d4 = doubles[2] - mc.thePlayer.posZ;
            float f = (float)Math.toDegrees(Math.atan2(d4, d2)) - 90.0F;
            double d5 = (double)MathHelper.sqrt_double(d2 * d2 + d4 * d4);
            double distanceToEntity = (double)mc.thePlayer.getDistanceToEntity(this.target);
            float f2 = (float)((double)((float)(-(Math.atan2(d3, d5) * 180.0 / Math.PI))) - 0.55F * Math.max(0.0, distanceToEntity - 3.0));
            RotationEngine rotationEngine2 = this.rotationEngine;
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            Rotation rotation = rotationEngine2.rotateTowards(f, f2, previousRotation);
            if (distanceToEntity > 4.0) {
               for (int i = -20; i < 20; i++) {
                  ProjectileSimulator projectileSimulator = new ProjectileSimulator();
                  EntityPlayerSP thePlayer = mc.thePlayer;
                  ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(this.findRodHotbarSlot() + 36).getStack();
                  float f3 = rotation.pitch + (float)i;
                  float yaw = rotation.yaw;
                  ItemStack stack2 = stack;
                  EntityPlayerSP player = thePlayer;
                  projectileSimulator.initFromShooterAngles(player, stack2, yaw, f3);
                  double d6 = (double)(this.target.width / 2.0F + 0.1F);
                  EntityLivingBase target4 = this.target;
                  AxisAlignedBB box = new AxisAlignedBB(
                     doubles[0] - d6, doubles[1], doubles[2] - d6, doubles[0] + d6, doubles[1] + (double)(this.target.height / 2.0F), doubles[2] + d6
                  );
                  EntityLivingBase entity = target4;
                  byte b2 = 100;
                  if (projectileSimulator.willFishingHookHitTarget(b2, entity, box)) {
                     RotationController rotationController = ClientManagers.rotationController;
                     Rotation rotation2 = RotationUtil.applyGcdFix(
                        rotation.yaw, rotation.pitch + (float)i, ClientManagers.rotationController.getPreviousRotation().yaw, ClientManagers.rotationController.getPreviousRotation().pitch
                     );
                     byte b3 = 0;
                     Rotation rotation3 = rotation2;
                     rotationController.requestRotation(rotation3, b3, this);
                     return;
                  }
               }
            }

            byte b4 = 0;
            ClientManagers.rotationController.requestRotation(rotation, b4, this);
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (this.target != null) {
         EntityFishHook entityFishHook = this.findOwnFishHook();
         if (entityFishHook == null && !this.rodCast && this.target.hurtTime < this.predictedFlightTicks) {
            byte b = 1;
            event.addRightClicks(b);
            this.ticksSinceCast = 0;
            this.rodCast = true;
            this.needsFallbackSlotUpdate = true;
         }

         if (entityFishHook != null && this.rightClickIfRodOnTarget.getValue() && !this.rodCast && this.ticksSinceCast > 3 && this.pendingRightClickReel) {
            byte b2 = 1;
            event.addRightClicks(b2);
            this.pendingRightClickReel = false;
         }
      }
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
      } else {
         if (this.hasActiveFishHook() && this.ticksSinceCast > 3) {
            this.selectedHotbarSlot = this.getFallbackHotbarSlot();
            this.rodCast = false;
         }
      }

      if (this.selectedHotbarSlot != this.findRodHotbarSlot() || mc.playerController.currentPlayerItem != this.findRodHotbarSlot()) {
         this.rodCast = false;
      }

      if (mc.thePlayer.inventory.currentItem != this.findRodHotbarSlot() && !this.combat.getValue() || this.combat.getValue() && this.selectedHotbarSlot != this.findRodHotbarSlot()) {
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
      if (this.target != null && !this.moveFix.getValue()) {
         float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
         event.setYaw(realYaw);
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (this.target != null && !this.moveFix.getValue()) {
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
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderPredictedTargetBox(f2, f5, f3, f4, shaderRenderPass));
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
      int n2 = -1;

      for (int i = 0; i < n; i++) {
         double d = entity.posX - mc.thePlayer.posX;
         double d2 = entity.posY + (double)this.target.height / 1.7 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
         double d3 = entity.posZ - mc.thePlayer.posZ;
         double[] doubles = EntityMovementPredictor.predictFullState(this.target, n2);
         if (n2 != -1) {
            d = doubles[0] - mc.thePlayer.posX;
            d2 = doubles[1] + (double)this.target.height / 1.7 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
            d3 = doubles[2] - mc.thePlayer.posZ;
         }

         float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0F;
         double d4 = (double)MathHelper.sqrt_double(d * d + d3 * d3);
         double distanceToEntity = (double)mc.thePlayer.getDistanceToEntity(this.target);
         float f2 = (float)((double)((float)(-(Math.atan2(d2, d4) * 180.0 / Math.PI))) - 0.55F * Math.max(0.0, distanceToEntity - 3.0));
         float f3 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f, 180.0F);
         float f4 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, f2, 180.0F);
         ProjectileSimulator projectileSimulator = new ProjectileSimulator();
         EntityPlayerSP thePlayer = mc.thePlayer;
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(this.findRodHotbarSlot() + 36).getStack();
         EntityPlayerSP player = thePlayer;
         projectileSimulator.initFromShooterAngles(player, stack, f3, f4);
         double d5 = (double)(entity.width / 2.0F + 0.1F);
         AxisAlignedBB box = new AxisAlignedBB(
            doubles[0] - d5, doubles[1] - 0.1F, doubles[2] - d5, doubles[0] + d5, doubles[1] + (double)entity.height + 0.1F, doubles[2] + d5
         );
         byte b = 100;
         n2 = projectileSimulator.simulateFishingHookHitTick(b, entity, box);
         if (n2 == Integer.MAX_VALUE) {
            return 0;
         }
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
         && this.selectedHotbarSlot != -1
         && this.findRodHotbarSlot() != -1
         && !mc.gameSettings.keyBindUseItem.isKeyDown()) {
         long l = 100L;
         if ((this.timeHelper.reached2(l) || !this.click.getValue())
            && (!this.onlyWhileKillAuraHasTarget.getValue() || ClientManagers.moduleManager.killAura.isToggled() && ClientManagers.moduleManager.killAura.target != null)) {
            ItemStack heldItemStack = SilentSlotManager.heldItemStack;
            if (this.weaponWhitelist.isItemAllowed(heldItemStack) && !this.lookDownDisableSettings.shouldDisable()) {
               return true;
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

   private void renderPredictedTargetBox(float f, float f2, float f3, float f4, ShaderRenderPass shaderRenderPass) {
      BlurShadowSettings blurShadowSettings2 = this.blurShadowSettings;
      byte b = 0;
      int colorInt = this.espColor.getColorInt(b);
      RenderUtil.setGlColor(blurShadowSettings2.resolvePassColor(shaderRenderPass, colorInt));
      RenderUtil.drawFilledBox2(f - f2, f3, f4 - f2, f + f2, f3 + this.target.height, f4 + f2);
   }

}
