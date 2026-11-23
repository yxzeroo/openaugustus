package net.augustus.modules.modes.velocity;

import net.augustus.Augustus;
import net.augustus.events.EventAttackSlowdown;
import net.augustus.events.EventClick;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventPacket;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.listeners.AttackSlowdownListener;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.combat.CombatOptimizationUtil;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.utils.rotation.EntityBoundsUtil;
import net.augustus.utils.rotation.RotationUtil;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class IntaveVelocityMode extends VelocityMode implements PacketListener, AttackSlowdownListener, PacketDelayListener, MoveInputListener, ClickListener {
   public final StringValue xzOnHit;
   public final StringValue xzonhitknockbackitem2;
   public final StringValue xzOnHitSprint;
   public final StringValue xzOnHitAtDoubleHit;
   public final BooleanValue forceDoubleHit;
   public final BooleanValue intaveSmart;
   public final BooleanValue intaveBoost;
   private final DoubleValue boostGroup;
   public final StringValue intaveBoostDelay;
   public final StringValue intaveBoostOnGroundTicks;
   public final StringValue xzVelocityIntave;
   public final StringValue yVelocityIntave;
   public final BooleanValue sneakInAir;
   private final TimeHelper timeHelper;
   private boolean boostPending;
   private boolean doubleHitTriggered;

   public IntaveVelocityMode(Velocity velocity, DoubleValue doubleValue) {
       super(velocity, doubleValue, "Intave");
      StringValue stringValue = new StringValue(this.parentModule, "XZOnHit", 0.6, -1.0, 1.0, 2, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.xzOnHit = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this.parentModule, "XZOnHitKnockbackItem_2", 0.6, -1.0, 1.0, 2, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      StringValue stringValue3 = stringValue2.visibleWhen(condition2);
      String str = "XZOnHitKnockbackItem";
      this.xzonhitknockbackitem2 = stringValue3.named(str);
      StringValue stringValue4 = new StringValue(this.parentModule, "XZOnHitSprint", 0.6, -1.0, 1.0, 2, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.xzOnHitSprint = stringValue4.visibleWhen(condition3);
      StringValue stringValue5 = new StringValue(this.parentModule, "XZOnHitAtDoubleHit", 1.0, -1.0, 1.0, 2, this.modeSetting);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.xzOnHitAtDoubleHit = stringValue5.visibleWhen(condition4);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "ForceDoubleHit", false, this.modeSetting);
      SettingVisibilityCondition condition5 = () -> this.isForceDoubleHitVisible();
      this.forceDoubleHit = (BooleanValue)booleanValue.withVisibility(condition5);
      BooleanValue booleanValue2 = new BooleanValue(this.parentModule, "IntaveSmart", false, this.modeSetting);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.intaveSmart = (BooleanValue)booleanValue2.withVisibility(condition6);
      BooleanValue booleanValue3 = new BooleanValue(this.parentModule, "IntaveBoost", false, this.modeSetting);
      SettingVisibilityCondition condition7 = () -> this.isSelected();
      this.intaveBoost = (BooleanValue)booleanValue3.withVisibility(condition7);
      DoubleValue doubleValue2 = new DoubleValue(this.parentModule, "BoostGroup", true, this.modeSetting);
      SettingVisibilityCondition condition8 = () -> this.isBoostGroupVisible();
      this.boostGroup = doubleValue2.visibleWhen(condition8);
      StringValue stringValue6 = new StringValue(this.parentModule, "IntaveBoostDelay", 2000.0, 0.0, 10000.0, 0, this.boostGroup);
      SettingVisibilityCondition condition9 = () -> this.isBoostDelayVisible();
      this.intaveBoostDelay = stringValue6.visibleWhen(condition9);
      StringValue stringValue7 = new StringValue(this.parentModule, "IntaveBoostOnGroundTicks", 2.0, 0.0, 10.0, 0, this.boostGroup);
      SettingVisibilityCondition condition10 = () -> this.isBoostOnGroundTicksVisible();
      this.intaveBoostOnGroundTicks = stringValue7.visibleWhen(condition10);
      StringValue stringValue8 = new StringValue(this.parentModule, "XZVelocityIntave", 100.0, -300.0, 300.0, 0, this.boostGroup);
      String str2 = "XZVelocity";
      StringValue stringValue9 = stringValue8.named(str2);
      SettingVisibilityCondition condition11 = () -> this.isBoostXZVelocityVisible();
      this.xzVelocityIntave = stringValue9.visibleWhen(condition11);
      StringValue stringValue10 = new StringValue(this.parentModule, "YVelocityIntave", 100.0, -100.0, 200.0, 0, this.boostGroup);
      String str3 = "YVelocity";
      StringValue stringValue11 = stringValue10.named(str3);
      SettingVisibilityCondition condition12 = () -> this.isBoostYVelocityVisible();
      this.yVelocityIntave = stringValue11.visibleWhen(condition12);
      BooleanValue booleanValue4 = new BooleanValue(this.parentModule, "SneakInAir", false, this.boostGroup);
      SettingVisibilityCondition condition13 = () -> this.isSneakInAirVisible();
      this.sneakInAir = (BooleanValue)booleanValue4.withVisibility(condition13);
      this.timeHelper = new TimeHelper();
      this.doubleHitTriggered = false;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 26;
      this.toggleEventSubscription(b);
      byte b2 = 25;
      this.toggleEventSubscription(b2);
      byte b3 = 100;
      byte b4 = 2;
      this.toggleEventSubscription(b4, b3);
      byte b5 = 21;
      this.toggleEventSubscription(b5);
      short s = 1000;
      byte b6 = 7;
      this.toggleEventSubscription(b6, s);
   }

   @Override
   public void onEnable() {
      this.boostPending = false;
      this.timeHelper.reset();
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new IntaveVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onClick(EventClick event) {
      if (this.forceDoubleHit.getValue() && !this.doubleHitTriggered && mc.thePlayer.hurtTime != 0 && mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
         int n = event.getLeftClicks() + (event.isIgnoreVanillaClicks() ? 0 : mc.gameSettings.keyBindAttack.getPressTime());
         if (n == 1) {
            byte b = 1;
            event.addLeftClicks(b);
            this.doubleHitTriggered = true;
         }
      }

      if (mc.thePlayer.hurtTime == 0) {
         this.doubleHitTriggered = false;
      }
   }

   @Override
   public void onAttackSlowdown2(EventAttackSlowdown event) {
      if (this.canApplyVelocity()) {
         boolean flag = true;
         if (this.intaveSmart.getValue() && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            double hitMotionMultiplier = this.getHitMotionMultiplier();
            float rotationYaw = mc.thePlayer.rotationYaw;
            Vec3 vec = PlayerUtil.predictNextTickWithAttack(true, mc.objectMouseOver.entityHit, hitMotionMultiplier, rotationYaw).addVector(0.0, (double)mc.thePlayer.getEyeHeight(), 0.0);
            float rotationYaw2 = mc.thePlayer.rotationYaw;
            Vec3 vec2 = PlayerUtil.predictNextTickWithAttack(false, mc.objectMouseOver.entityHit, hitMotionMultiplier, rotationYaw2)
               .addVector(0.0, (double)mc.thePlayer.getEyeHeight(), 0.0);
            float collisionBorderSize = mc.objectMouseOver.entityHit.getCollisionBorderSize();
            Vec3 vec3 = CombatOptimizationUtil.getPredictedEntityPosition(mc.objectMouseOver.entityHit);
            boolean flag2 = false;
            AxisAlignedBB box = EntityBoundsUtil.createPlayerBoundingBox(vec3, flag2).expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
            Vec3 vec4 = RotationUtil.clampPointToBox(vec, box);
            Vec3 vec5 = RotationUtil.clampPointToBox(vec2, box);
            if (vec.distanceTo(vec4) >= vec2.distanceTo(vec5)) {
               flag = false;
            }
         }

         if (mc.thePlayer.hurtTime != 0 && flag && (((Velocity)this.parentModule).attackSlowdownCount == 0 || this.xzOnHitAtDoubleHit.getValue() != 1.0)) {
            double hitMotionMultiplier2 = this.getHitMotionMultiplier();
            boolean flag3 = true;
            event.setApplySecondStage(flag3);
            double d = ((Velocity)this.parentModule).attackSlowdownCount == 0 ? hitMotionMultiplier2 : this.xzOnHitAtDoubleHit.getValue();
            event.setSlowDownFactor(d);
            boolean flag4 = false;
            event.setSprint(flag4);
            Augustus.getInstance().getClass();
         }
      }
   }

   @Override
   public void onAttackSlowdown(EventAttackSlowdown event) {
      if (this.canApplyVelocity()) {
         if (mc.thePlayer.hurtTime != 0 && ((Velocity)this.parentModule).attackSlowdownCount == 0) {
            double value = this.xzOnHitSprint.getValue();
            double d = value;
            event.setSlowDownFactor(d);
            boolean flag = false;
            event.setSprint(flag);
            if (EnchantmentHelper.getKnockbackModifier(mc.thePlayer) > 0) {
               double value2 = this.xzonhitknockbackitem2.getValue();
               double d2 = value2;
               event.setSlowDownFactor(d2);
            }
         }
      }
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      if (this.canApplyVelocity()) {
         Packet packet = event.getPacket();
         EntityPlayerSP thePlayer = mc.thePlayer;
         if (packet instanceof S12PacketEntityVelocity
            && thePlayer != null
            && thePlayer.onGround
            && MoveUtil.isMoving()
            && !thePlayer.isCollidedHorizontally
            && this.intaveBoost.getValue()
            && (double)thePlayer.onGroundTicks > this.intaveBoostOnGroundTicks.getValue()) {
            S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
            if (packet2.getEntityID() == thePlayer.getEntityId()) {
               TimeHelper timeHelper2 = this.timeHelper;
               double value = this.intaveBoostDelay.getValue();
               if (timeHelper2.reached(value)) {
                  this.timeHelper.reset();
                  this.boostPending = true;
               }
            }
         }

         boolean flag = thePlayer != null && thePlayer.ticksExisted > 20 && this.boostPending && this.intaveBoost.getValue();
         event.setDelayed(flag);
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.canApplyVelocity()) {
         if (!this.intaveBoost.getValue()) {
            this.boostPending = false;
         } else {
            if (this.boostPending) {
               if (mc.thePlayer.onGround) {
                  boolean flag = true;
                  event.setSneak(flag);
                  float f = 0.0F;
                  event.setMoveForward(f);
               }

               this.boostPending = false;
            }

            if (this.sneakInAir.getValue() && mc.thePlayer.hurtTime > 2) {
               boolean flag2 = true;
               event.setSneak(flag2);
            }
         }
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      if (!event.isCancelled()) {
         Packet packet = event.getPacket();
         if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
            if (mc.thePlayer != null && packet2.getEntityID() == mc.thePlayer.getEntityId() && this.canApplyVelocity()) {
               if (mc.thePlayer != null
                  && mc.thePlayer.onGround
                  && MoveUtil.isMoving()
                  && !mc.thePlayer.isCollidedHorizontally
                  && this.intaveBoost.getValue()
                  && (double)mc.thePlayer.onGroundTicks > this.intaveBoostOnGroundTicks.getValue()
                  && packet2.getEntityID() == mc.thePlayer.getEntityId()) {
                  TimeHelper timeHelper2 = this.timeHelper;
                  double value = this.intaveBoostDelay.getValue();
                  if (timeHelper2.reached(value)) {
                     if (this.xzVelocityIntave.getValue() == 0.0 && this.yVelocityIntave.getValue() == 0.0) {
                        event.cancel();
                        return;
                     }

                     packet2.setMotionX((int)((double)packet2.getMotionX() * this.xzVelocityIntave.getValue() / 100.0));
                     packet2.setMotionY((int)((double)packet2.getMotionY() * this.yVelocityIntave.getValue() / 100.0));
                     packet2.setMotionZ((int)((double)packet2.getMotionZ() * this.xzVelocityIntave.getValue() / 100.0));
                  }
               }
            }
         }
      }
   }

   private double getHitMotionMultiplier() {
      return mc.thePlayer != null && EnchantmentHelper.getKnockbackModifier(mc.thePlayer) > 0 ? this.xzonhitknockbackitem2.getValue() : this.xzOnHit.getValue();
   }

   private boolean isSneakInAirVisible() {
      return this.isSelected() && this.intaveBoost.getValue();
   }

   private boolean isBoostYVelocityVisible() {
      return this.isSelected() && this.intaveBoost.getValue();
   }

   private boolean isBoostXZVelocityVisible() {
      return this.isSelected() && this.intaveBoost.getValue();
   }

   private boolean isBoostOnGroundTicksVisible() {
      return this.isSelected() && this.intaveBoost.getValue();
   }

   private boolean isBoostDelayVisible() {
      return this.isSelected() && this.intaveBoost.getValue();
   }

   private boolean isBoostGroupVisible() {
      return this.isSelected() && this.intaveBoost.getValue();
   }

   private boolean isForceDoubleHitVisible() {
      return this.isSelected() && this.xzOnHitAtDoubleHit.getValue() != 1.0;
   }

}
