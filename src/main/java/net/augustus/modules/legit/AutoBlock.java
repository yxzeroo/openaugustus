package net.augustus.modules.legit;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.rotation.RayCastUtil;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.utils.network.ServerPingUtil;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;

public class AutoBlock extends Module implements ClickListener, PacketDelayListener, AttackEntityListener {
   private final BooleansSetting mode;
   private final StringValue startBlockingAdditionalRange;
   private final StringValue maximumPlayerHurtTime;
   private final StringValue maximumHurtTime;
   private final StringValue ticksToTryHitAfterUnblock;
   private final RandomizedRangeSettings maximumLagDuration;
   private final BooleanValue subtractOwnPingFromLagDuration;
   private final BooleanValue onlyOnRightClick;
   private final BooleanValue forceHit;
   private final BooleanValue interactAutoBlock;
   private final TargetSelectionSettings targetSelectionSettings;
   private final AtomicBoolean active;
   private EntityLivingBase target;
   private final TimeHelper targetResetTimer;
   private final TimeHelper lagTimer;
   private int blockCooldownTicks;
   private int unblockHitTicks;
   private final AtomicBoolean lagBlocking;
   private boolean delayingPackets;

   public AutoBlock() {
       super("AutoBlock", "AutomatisiertesBlockieren", Color.green, Categorys.LEGIT);
      this.mode = new BooleansSetting(this, "Mode", "Default", new String[]{"Default", "Lag"});
      this.startBlockingAdditionalRange = new StringValue(this, "StartBlockingAdditionalRange", 0.5, 0.0, 2.0, 2);
      this.maximumPlayerHurtTime = new StringValue(this, "MaximumPlayerHurtTime", 4.0, 3.0, 10.0, 0);
      this.maximumHurtTime = new StringValue(this, "MaximumHurtTime", 1.0, 4.0, 0.0, 0);
      this.ticksToTryHitAfterUnblock = new StringValue(this, "TicksToTryHitAfterUnblock", 2.0, 0.0, 4.0, 0);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "MaximumLagDuration", 250.0, 350.0, 0.0, 1000.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition = () -> this.isLagMode();
      this.maximumLagDuration = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      this.subtractOwnPingFromLagDuration = new BooleanValue(this, "SubtractOwnPingFromLagDuration", false);
      this.onlyOnRightClick = (BooleanValue)(new BooleanValue(this, "OnlyOnRightClick", true)).markVisualOnly();
      this.forceHit = new BooleanValue(this, "ForceHit", true);
      this.interactAutoBlock = new BooleanValue(this, "InteractAutoBlock", true);
      this.targetSelectionSettings = new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter());
      this.active = new AtomicBoolean(false);
      this.targetResetTimer = new TimeHelper();
      this.lagTimer = new TimeHelper();
      this.blockCooldownTicks = 0;
      this.unblockHitTicks = 0;
      this.lagBlocking = new AtomicBoolean(false);
      this.delayingPackets = false;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      int n = 90000;
      byte b = 7;
      this.toggleEventSubscription(b, n);
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      if (this.active.get() && this.lagBlocking.get()) {
         if (this.isLagMode()) {
            Packet packet = event.getPacket();
            TimeHelper lagTimer2 = this.lagTimer;
            long effectiveLagDuration = (long)this.getEffectiveLagDuration();
            if (lagTimer2.reached2(effectiveLagDuration) && this.delayingPackets) {
               this.delayingPackets = false;
               this.lagBlocking.set(false);
            }

            if (this.delayingPackets) {
               boolean flag = true;
               event.setDelayed(flag);
            }

            if (packet instanceof C03PacketPlayer && !this.delayingPackets) {
               this.delayingPackets = true;
               this.lagTimer.reset();
               this.maximumLagDuration.nextValue();
            }
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      this.updateTarget();
      this.active.set(this.canAutoBlock());
      if (this.active.get()) {
         String selectedValue = this.mode.getSelectedValue();
         switch (selectedValue) {
            case "Default":
               if (!this.isPlayerHurtTimeWithinLimit()) {
                  boolean flag = false;
                  event.setRightMouseDown(flag);
               } else if (!this.shouldUnblockToAttack()) {
                  if (!mc.thePlayer.isUsingItem()) {
                     this.triggerBlock(event);
                  } else {
                     boolean flag2 = true;
                     event.setRightMouseDown(flag2);
                  }
               } else {
                  this.unblockHitTicks = (int)this.ticksToTryHitAfterUnblock.getValue();
                  boolean flag3 = false;
                  event.setRightMouseDown(flag3);
                  if (this.forceHit.getValue() && this.isAimingAtLivingEntity() && event.getLeftClicks() <= 0 && mc.gameSettings.keyBindAttack.getPressTime() <= 0 && !mc.thePlayer.isUsingItem()) {
                     byte b = 1;
                     event.addLeftClicks(b);
                     boolean flag4 = true;
                     event.setBreakingBlock(flag4);
                  }

                  if ((event.getLeftClicks() > 0 || mc.gameSettings.keyBindAttack.getPressTime() > 0) && this.isAimingAtLivingEntity() && !mc.thePlayer.isUsingItem()) {
                     this.triggerBlock(event);
                     this.blockCooldownTicks = 6;
                  }
               }
               break;
            case "Lag":
               if (!this.isPlayerHurtTimeWithinLimit()) {
                  boolean flag5 = false;
                  event.setRightMouseDown(flag5);
                  this.lagBlocking.set(false);
                  this.delayingPackets = false;
               } else if (!this.shouldUnblockToAttack()) {
                  if (!this.lagBlocking.get()) {
                     this.triggerBlock(event);
                     this.lagBlocking.set(true);
                     this.delayingPackets = false;
                  } else {
                     boolean flag6 = false;
                     event.setRightMouseDown(flag6);
                  }
               } else {
                  this.unblockHitTicks = (int)this.ticksToTryHitAfterUnblock.getValue();
                  boolean flag7 = false;
                  event.setRightMouseDown(flag7);
                  if (this.forceHit.getValue() && this.isAimingAtLivingEntity() && event.getLeftClicks() <= 0 && mc.gameSettings.keyBindAttack.getPressTime() <= 0 && !mc.thePlayer.isUsingItem()) {
                     byte b2 = 1;
                     event.addLeftClicks(b2);
                     boolean flag8 = true;
                     event.setBreakingBlock(flag8);
                  }

                  if ((event.getLeftClicks() > 0 || mc.gameSettings.keyBindAttack.getPressTime() > 0) && this.isAimingAtLivingEntity() && !mc.thePlayer.isUsingItem()) {
                     this.triggerBlock(event);
                     this.lagBlocking.set(true);
                     this.delayingPackets = false;
                     this.blockCooldownTicks = 6;
                  }
               }
         }
      }

      this.blockCooldownTicks--;
      this.unblockHitTicks--;
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      if (event.getTarget() instanceof EntityLivingBase) {
         this.target = (EntityLivingBase)event.getTarget();
         this.targetResetTimer.reset();
      }
   }

   private void triggerBlock(EventClick event) {
      byte b = 1;
      event.addRightClicks(b);
      if (!this.interactAutoBlock.getValue()) {
         Runnable runnable = () -> sendUseHeldItem();
         event.setRightClickAction(runnable);
      }
   }

   private boolean shouldUnblockToAttack() {
      if ((double)this.target.clientHurtTime <= this.maximumHurtTime.getValue() && this.blockCooldownTicks <= 0) {
         if (this.isAimingAtLivingEntity()) {
            return true;
         }

         byte b = 1;
         if (this.canRayCastTargetInTicks(b) || this.unblockHitTicks > 0) {
            return true;
         }
      }

      return false;
   }

   private boolean canAutoBlock() {
      return this.isRightClickSatisfied() && this.isHoldingSword() && this.target != null && this.isKillAuraNotBlocking();
   }

   private boolean isKillAuraNotBlocking() {
      return !ClientManagers.moduleManager.killAura.isToggled() || ClientManagers.moduleManager.killAura.target == null || ClientManagers.moduleManager.killAura.blockMode.getSelectedValue().equals("None");
   }

   private boolean isPlayerHurtTimeWithinLimit() {
      return (double)mc.thePlayer.hurtTime <= this.maximumPlayerHurtTime.getValue();
   }

   private boolean isRightClickSatisfied() {
      return !this.onlyOnRightClick.getValue() || mc.gameSettings.keyBindUseItem.isPressed() || mc.gameSettings.keyBindUseItem.isKeyDown();
   }

   private boolean isHoldingSword() {
      return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
   }

   private boolean canRayCastTargetInTicks(int n) {
      MovingObjectPosition hit = RayCastUtil.rayCastPredicted(n, ClientManagers.rotationController.getCurrentRotation(), this.getReachDistance());
      return hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && hit.entityHit instanceof EntityLivingBase;
   }

   private boolean isAimingAtLivingEntity() {
      return mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase;
   }

   private boolean isLagMode() {
      return this.mode.getSelectedValue().equals("Lag");
   }

   private void updateTarget() {
      long l = 600L;
      if (this.targetResetTimer.reached2(l)) {
         this.target = null;
      }

      if (this.target == null) {
         Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
         double d = 1.0;
         double d2 = 6.0;
         MovingObjectPosition hit = RayTraceUtil.rayTraceEntity(1.0F, previousRotation, d2, d);
         if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && hit.entityHit instanceof EntityLivingBase) {
            this.target = (EntityLivingBase)hit.entityHit;
         }
      }

      EntityTargetFilter targetFilter = this.targetSelectionSettings.getTargetFilter();
      EntityLivingBase target2 = this.target;
      boolean flag = targetFilter.matches(target2);
      if (!flag) {
         this.target = null;
      }

      if (this.target != null && (double)mc.thePlayer.getDistanceToEntityBox(this.target) > this.getReachDistance() + this.startBlockingAdditionalRange.getValue()) {
         this.target = null;
      }
   }

   private double getReachDistance() {
      return ClientManagers.moduleManager.range.isToggled() ? ClientManagers.moduleManager.range.getCurrentRange() : (mc.playerController.isInCreativeMode() ? 4.5 : 3.0);
   }

   private int getEffectiveLagDuration() {
      return this.subtractOwnPingFromLagDuration.getValue() ? Math.max(0, this.maximumLagDuration.getLastIntValue() - ServerPingUtil.getPing()) : this.maximumLagDuration.getLastIntValue();
   }

   private static void sendUseHeldItem() {
      if (!mc.playerController.isHittingBlock() || ClientManagers.moduleManager.fixes.isPlaceWhileBreakEnabled()) {
         byte b = 4;
         mc.rightClickDelayTimer = b;
         ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
         if (currentItem != null && mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, currentItem)) {
            mc.entityRenderer.itemRenderer.resetEquippedProgress2();
         }
      }
   }

}
