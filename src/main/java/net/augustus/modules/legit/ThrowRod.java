package net.augustus.modules.legit;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.WeaponFilterSettings;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.LookDownDisableSettings;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.RangeSetting;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.EventLoadWorld;
import net.augustus.utils.world.BlockUtil;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.utils.TickCounter;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class ThrowRod extends Module implements SilentSlotListener, ClickListener, LoadWorldListener {
   public final RangeSetting distanceToTarget;
   private final StringValue maxTargetLookFovDiff;
   public final StringValue fovYaw;
   public final StringValue fovPitch;
   private final RandomizedRangeSettings reactionTickTimeWorld;
   private final RandomizedRangeSettings reactionTickTimeEntity;
   private final RandomizedRangeSettings rodExtendReactionTime;
   private final TargetSelectionSettings targetSelectionSettings;
   public final BooleanValue ignoreEatingTargets;
   public final BooleanValue disableWhileScaffold;
   public final BooleanValue onlyWhileKillAuraHasTarget;
   public final WeaponFilterSettings weaponWhitelist;
   public final LookDownDisableSettings lookDownDisableSettings;
   private final BooleanValue onlyAtClicking;
   private final BooleanValue automatedSlotSwitching;
   public final BooleanValue silentSlotSwitching;
   private final TimeHelper timeHelper;
   private final TickCounter worldReactionCounter;
   private final TickCounter entityReactionCounter;
   private final TickCounter ticksSinceLastClick;
   private final TickCounter rodHeldCounter;
   private boolean trackingHook;
   private boolean rodCast;
   private boolean hitWorld;
   private boolean needsFallbackSlotUpdate;
   private int fallbackHotbarSlot;
   private EntityLivingBase target;
   private int selectedHotbarSlot;

   public ThrowRod() {
       super("ThrowRod", "SchmeißeAngel", new Color(202, 242, 109), Categorys.LEGIT);
      this.distanceToTarget = new RangeSetting(this, "DistanceToTarget", 3.2, 15.0, 0.0, 20.0, 2);
      this.maxTargetLookFovDiff = new StringValue(this, "MaxTargetLookFovDiff", 180.0, 20.0, 180.0, 0);
      this.fovYaw = new StringValue(this, "FovYaw", 90.0, 0.0, 180.0, 0);
      this.fovPitch = new StringValue(this, "FovPitch", 90.0, 0.0, 180.0, 0);
      this.reactionTickTimeWorld = new RandomizedRangeSettings(this, "ReactionTickTimeWorld", 0.0, 0.0, 0.0, 5.0, 0);
      this.reactionTickTimeEntity = new RandomizedRangeSettings(this, "ReactionTickTimeEntity", 0.0, 0.0, 0.0, 5.0, 0);
      this.rodExtendReactionTime = new RandomizedRangeSettings(this, "RodExtendReactionTime", 0.0, 0.0, 0.0, 5.0, 0);
      this.targetSelectionSettings = (new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter())).withSorting();
      this.ignoreEatingTargets = new BooleanValue(this, "IgnoreEatingTargets", true);
      this.disableWhileScaffold = new BooleanValue(this, "DisableWhileScaffold", true);
      this.onlyWhileKillAuraHasTarget = new BooleanValue(this, "OnlyWhileKillAuraHasTarget", false);
      this.weaponWhitelist = new WeaponFilterSettings(this, "WeaponWhitelist");
      this.lookDownDisableSettings = new LookDownDisableSettings(this);
      this.onlyAtClicking = new BooleanValue(this, "OnlyAtClicking", true);
      this.automatedSlotSwitching = new BooleanValue(this, "AutomatedSlotSwitching", true);
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.timeHelper = new TimeHelper();
      this.worldReactionCounter = new TickCounter();
      this.entityReactionCounter = new TickCounter();
      this.ticksSinceLastClick = new TickCounter();
      this.rodHeldCounter = new TickCounter();
      this.trackingHook = true;
      this.rodCast = false;
      this.hitWorld = false;
      this.needsFallbackSlotUpdate = false;
      this.fallbackHotbarSlot = -1;
      this.target = null;
      this.selectedHotbarSlot = -1;
   }

   @Override
   public void onEnable() {
      this.needsFallbackSlotUpdate = true;
   }

   @Override
   public void onDisable() {
      this.target = null;
      Object obj = null;
      this.targetSelectionSettings.setCurrentTarget((Entity)obj);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 34;
      this.toggleEventSubscription(b2);
      byte b3 = 17;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      Object obj = null;
      this.targetSelectionSettings.setCurrentTarget((Entity)obj);
      this.target = null;
   }

   @Override
   public void onClick(EventClick event) {
      if (!this.isHoldingRod()) {
         this.rodHeldCounter.reset();
      }

      if (mc.isNotInGui()) {
         if (event.getLeftClicks() > 0 || mc.gameSettings.keyBindAttack.isKeyDown() || mc.gameSettings.keyBindAttack.getPressTime() > 0) {
            this.ticksSinceLastClick.reset();
         }

         boolean flag = this.hasActiveFishHook();
         if (this.isHoldingRod()) {
            if (!flag && !this.rodCast && this.target != null && mc.objectMouseOver != null && !this.isCrosshairOnEntity()) {
               TickCounter rodHeldCounter2 = this.rodHeldCounter;
               int lastIntValue = this.rodExtendReactionTime.getLastIntValue();
               if (rodHeldCounter2.hasReached2(lastIntValue)) {
                  byte b = 1;
                  event.addRightClicks(b);
                  this.rodCast = true;
                  this.trackingHook = true;
                  this.needsFallbackSlotUpdate = true;
                  this.timeHelper.reset();
                  this.rodExtendReactionTime.nextValue();
               }
            }
         } else {
            this.rodCast = false;
         }
      }
   }

   @Override
   public void onSilentSlot() {
      this.selectedHotbarSlot = -1;
      this.ticksSinceLastClick.increment();
      this.worldReactionCounter.increment();
      this.entityReactionCounter.increment();
      this.rodHeldCounter.increment();
      EntityLivingBase target2 = this.target;
      this.targetSelectionSettings.setCurrentTarget(target2);
      this.target = this.findTarget();
      if (this.target != null && this.automatedSlotSwitching.getValue()) {
         this.selectedHotbarSlot = this.findRodHotbarSlot();
      }

      if (this.rodCast) {
         long l = 150L;
         if (this.timeHelper.reached2(l) && this.trackingHook) {
            EntityFishHook entityFishHook = this.findOwnFishHook();
            if (entityFishHook != null) {
               BlockPos pos = new BlockPos(entityFishHook.posX, entityFishHook.posY + 0.5, entityFishHook.posZ);
               if (entityFishHook.onGround || entityFishHook.inGround || entityFishHook.isDead || mc.theWorld.getBlockState(pos.down()).getBlock().isFullBlock()) {
                  this.worldReactionCounter.reset();
                  this.hitWorld = true;
                  this.trackingHook = false;
               }

               exitBlock:
               if (this.target != null) {
                  Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
                  double distanceToEntityBox = (double)mc.thePlayer.getDistanceToEntityBox(this.target);
                  double distanceToEntityBox2 = (double)mc.thePlayer.getDistanceToEntityBox(entityFishHook);
                  if (!(distanceToEntityBox2 > distanceToEntityBox)) {
                     double d = positionEyes.distanceTo(
                        new Vec3((double)((float)entityFishHook.serverPosX / 32.0F), (double)((float)entityFishHook.serverPosY / 32.0F), (double)((float)entityFishHook.serverPosZ / 32.0F))
                     );
                     if (!(d > this.distanceToTarget.getMaxValue()) && this.target.hurtTime != 10) {
                        break exitBlock;
                     }
                  }

                  this.entityReactionCounter.reset();
                  this.hitWorld = false;
                  this.trackingHook = false;
               }
            }
         }
      }

      exitBlock2:
      if (this.rodCast && !this.trackingHook) {
         TickCounter entityReactionCounter2 = this.entityReactionCounter;
         int lastIntValue = this.reactionTickTimeEntity.getLastIntValue();
         if (!entityReactionCounter2.hasReached2(lastIntValue) || this.hitWorld) {
            entityReactionCounter2 = this.worldReactionCounter;
            int lastIntValue2 = this.reactionTickTimeWorld.getLastIntValue();
            if (!entityReactionCounter2.hasReached2(lastIntValue2) || !this.hitWorld) {
               break exitBlock2;
            }
         }

         this.selectedHotbarSlot = this.getFallbackHotbarSlot();
         this.rodCast = false;
         this.reactionTickTimeEntity.nextInt();
         this.reactionTickTimeWorld.nextInt();
      }

      if (this.selectedHotbarSlot != -1) {
         int selectedHotbarSlot2 = this.selectedHotbarSlot;
         boolean value = this.silentSlotSwitching.getValue();
         int n = selectedHotbarSlot2;
         byte b = 9;
         SilentSlotManager.requestSlot(this, b, n, value);
      }
   }

   private boolean isCrosshairOnEntity() {
      return mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY;
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

   public int findRodHotbarSlot() {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      byte b = 36;
      containerSlotQuery = containerSlotQuery.fromSlot(b);
      Predicate predicate = obj -> obj instanceof ItemFishingRod;
      OptionalInt optionalInt = containerSlotQuery.filterItem(predicate).findFirstSlotIndex();
      return optionalInt.isPresent() ? optionalInt.getAsInt() - 36 : -1;
   }

   private boolean isHoldingRod() {
      return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFishingRod;
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

   private EntityLivingBase findTarget() {
      ItemStack heldItemStack = SilentSlotManager.heldItemStack;
      if (!this.weaponWhitelist.isItemAllowed(heldItemStack)) {
         return null;
      } else if (this.lookDownDisableSettings.shouldDisable()) {
         return null;
      } else if (BlockUtil.isScaffoldActiveAndValid() && this.disableWhileScaffold.getValue()) {
         return null;
      } else if ((!ClientManagers.moduleManager.killAura.isToggled() || ClientManagers.moduleManager.killAura.target == null) && this.onlyWhileKillAuraHasTarget.getValue()) {
         return null;
      } else if (this.isCrosshairOnEntity()) {
         return null;
      } else {
         byte b = 3;
         if (this.ticksSinceLastClick.hasReached2(b) && this.onlyAtClicking.getValue()) {
            return null;
         } else {
            EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
            EntityTargetFilter targetFilter = this.targetSelectionSettings.getTargetFilter();
            EntityTargetFilter entityTargetFilter = targetFilter;
            entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter);
            boolean flag = false;
            entityTargetSelector = entityTargetSelector.throughWalls(flag);
            double maxValue = this.distanceToTarget.getMaxValue();
            entityTargetSelector = entityTargetSelector.maxDistance(maxValue);
            double minValue = this.distanceToTarget.getMinValue();
            entityTargetSelector = entityTargetSelector.minDistance(minValue);
            double value = this.fovYaw.getValue();
            double value2 = this.fovPitch.getValue();
            double d = value;
            entityTargetSelector = entityTargetSelector.withinFov(d, value2).filterFocus();
            Predicate predicate = obj -> this.isValidTargetState((Entity)obj);
            entityTargetSelector = entityTargetSelector.filter(predicate);
            Predicate predicate2 = obj2 -> {
               if (obj2 instanceof EntityLivingBase) {
                  EntityLivingBase entity = (EntityLivingBase)obj2;
                  double d2 = (double)RotationUtil.getEntityLookAngleToPlayer(entity);
                  return !(d2 > this.maxTargetLookFovDiff.getValue());
               } else {
                  return false;
               }
            };
            entityTargetSelector = entityTargetSelector.filter(predicate2);
            TargetSelectionSettings targetSelectionSettings2 = this.targetSelectionSettings;
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            double d3 = -1.0;
            Rotation rotation = previousRotation;
            ToDoubleFunction toDoubleFunction = targetSelectionSettings2.createTargetComparatorKey(rotation, d3);
            Optional optional = entityTargetSelector.sortBy(toDoubleFunction).findBest();
            return optional.isPresent() && optional.get() instanceof EntityLivingBase ? (EntityLivingBase)optional.get() : null;
         }
      }
   }

   private boolean isValidTargetState(Entity entity) {
      return !entity.isEating() || !this.ignoreEatingTargets.getValue();
   }

}
