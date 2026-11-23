package net.augustus.modules.combat;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.options.MoveHelperModeOption;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.utils.TickCounter;
import net.augustus.events.EventAttackEntity;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.events.listeners.SoundListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.events.EventSound;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;

public class MoveHelper extends Module implements LoadWorldListener, MoveInputListener, AttackEntityListener, TickListener, SoundListener {
   private boolean shouldHitSelect;
   public final MultiOptionSetting modes;
   public final StringValue combos;
   private final BooleanValue doNotBreakCombosWhilePlayerHasJumpboost;
   private int incomingComboHits;
   private int hitSelectTicks;
   private final TickCounter tickCounter;
   private EntityLivingBase currentTarget;
   private EntityLivingBase lastTarget;
   private final TimeHelper playerFallHurtTimer;
   private final TimeHelper targetFallHurtTimer;
   private boolean previousShouldHitSelect;

   public MoveHelper() {
       super("MoveHelper", "BewegungsHelfer", new Color(50, 168, 82), Categorys.COMBAT);
      this.shouldHitSelect = false;
      this.modes = (MultiOptionSetting)(new MultiOptionSetting(this, "Modes", MoveHelperModeOption.values())).markVisualOnly();
      StringValue stringValue = new StringValue(this, "Combos", 1.0, 0.0, 4.0, 0);
      SettingVisibilityCondition condition = () -> MoveHelperModeOption.isOptionSelected(MoveHelperModeOption.COMBO_BREAKER);
      this.combos = (StringValue)stringValue.visibleWhen(condition).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this, "Do not break combos while player has jumpboost", true);
      SettingVisibilityCondition condition2 = () -> MoveHelperModeOption.isOptionSelected(MoveHelperModeOption.COMBO_BREAKER);
      this.doNotBreakCombosWhilePlayerHasJumpboost = (BooleanValue)((BooleanValue)booleanValue.withVisibility(condition2)).markVisualOnly();
      this.incomingComboHits = 0;
      this.hitSelectTicks = 0;
      this.tickCounter = new TickCounter();
      this.currentTarget = null;
      this.lastTarget = null;
      this.playerFallHurtTimer = new TimeHelper();
      this.targetFallHurtTimer = new TimeHelper();
      this.previousShouldHitSelect = false;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.lastTarget = null;
      this.currentTarget = null;
      this.hitSelectTicks = 0;
      this.incomingComboHits = 0;
   }

   @Override
   public void updateEventSubscriptions() {
      short s = 1000;
      byte b = 21;
      this.toggleEventSubscription(b, s);
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onTick() {
      EntityLivingBase entity = this.findTarget();
      if (entity != null) {
         this.currentTarget = entity;
      }

      if (this.lastTarget != this.currentTarget) {
         this.incomingComboHits = 0;
      }

      this.lastTarget = this.currentTarget;
      if (MoveHelperModeOption.HIT_SELECTION.isSelected()) {
         this.updateHitSelection();
      }
   }

   @Override
   public void onSound(EventSound event) {
      if (MathStatisticsUtil.isWithinTolerance(event.getX(), mc.thePlayer.posX, 0.01)
         && MathStatisticsUtil.isWithinTolerance(event.getY(), mc.thePlayer.posY, 0.01)
         && MathStatisticsUtil.isWithinTolerance(event.getZ(), mc.thePlayer.posZ, 0.01)) {
         if (event.getSoundName().equals("game.player.hurt.fall.small") || event.getSoundName().equals("game.player.hurt.fall.big")) {
            this.playerFallHurtTimer.reset();
         }

         if (event.getSoundName().equals("game.player.hurt")) {
            long l = 100L;
            if (this.playerFallHurtTimer.reached2(l)) {
               this.incomingComboHits++;
            }
         }
      } else if (this.currentTarget != null
         && MathStatisticsUtil.isWithinTolerance(event.getX(), this.currentTarget.realPosX, 0.7)
         && MathStatisticsUtil.isWithinTolerance(event.getY(), this.currentTarget.realPosY, 2.0)
         && MathStatisticsUtil.isWithinTolerance(event.getZ(), this.currentTarget.realPosZ, 0.7)) {
         if (event.getSoundName().equals("game.player.hurt.fall.small") || event.getSoundName().equals("game.player.hurt.fall.big")) {
            this.targetFallHurtTimer.reset();
         }

         if (event.getSoundName().equals("game.player.hurt")) {
            long l2 = 100L;
            if (this.targetFallHurtTimer.reached2(l2)) {
               this.incomingComboHits = 0;
            }
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (MoveHelperModeOption.COMBO_BREAKER.isSelected()) {
         this.applyComboBreaker(event);
      }
   }

   @Override
   public void onTick5() {
      this.tickCounter.increment();
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      if (event.getTarget() instanceof EntityLivingBase) {
         this.lastTarget = (EntityLivingBase)event.getTarget();
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.lastTarget = null;
      this.currentTarget = null;
   }

   private void updateHitSelection() {
      if (this.currentTarget != null && mc.thePlayer != null && mc.thePlayer.getSmoothedServerPos() != null && this.currentTarget.rawServerPos != null && mc.thePlayer.ticksExisted >= 10) {
         Vec3 nearestPointOnEntity = RotationUtil.getNearestPointOnEntity(this.currentTarget);
         double d = mc.thePlayer.getPositionEyes(1.0F).distanceTo(nearestPointOnEntity);
         double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
         double[] doubles2 = PlayerUtil.getInterpolatedEntityPosition(this.currentTarget);
         Vec3 vec = new Vec3(doubles[0], doubles[1] + (double)this.currentTarget.getEyeHeight(), doubles[2]);
         double d2 = doubles2[0];
         double d3 = doubles2[1];
         double d4 = doubles2[2];
         double d5 = d3;
         double d6 = d2;
         EntityLivingBase currentTarget2 = this.currentTarget;
         Vec3 nearestPointOnEntityAt = RotationUtil.getNearestPointOnEntityAt(vec, currentTarget2, d6, d5, d4);
         double d7 = vec.distanceTo(nearestPointOnEntityAt);
         Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
         d2 = doubles2[0];
         d3 = doubles2[1];
         double d8 = doubles2[2];
         double d9 = d3;
         double d10 = d2;
         EntityLivingBase currentTarget3 = this.currentTarget;
         Vec3 nearestPointOnEntityAt2 = RotationUtil.getNearestPointOnEntityAt(positionEyes, currentTarget3, d10, d9, d8);
         double d11 = nearestPointOnEntityAt2.distanceToXZ(mc.thePlayer.getPositionEyes(1.0F));
         double d12 = mc.thePlayer.getPositionEyes(1.0F).distanceToXZ(nearestPointOnEntity);
         this.hitSelectTicks = this.currentTarget.hurtTime != this.currentTarget.prevHurtTime || mc.thePlayer.hurtTime != mc.thePlayer.prevHurtTime && (!(d7 < 2.9) || !(d < 3.0) || mc.thePlayer.hurtTime <= 8)
            ? 0
            : this.hitSelectTicks + 1;
         this.previousShouldHitSelect = this.shouldHitSelect;
         this.shouldHitSelect = d12 > d11 && d7 < 2.9 && d > 2.0 && this.hitSelectTicks > 3;
      } else {
         this.shouldHitSelect = false;
      }
   }

   private void applyComboBreaker(EventMoveInput event) {
      if ((double)this.incomingComboHits > this.combos.getValue() && mc.thePlayer.hurtTime == 9 && mc.thePlayer.onGround) {
         if (this.doNotBreakCombosWhilePlayerHasJumpboost.getValue() && mc.thePlayer.getActivePotionEffect(Potion.jump) != null) {
            return;
         }

         boolean flag = true;
         event.setJump(flag);
         this.incomingComboHits = 0;
      }
   }

   public boolean shouldHitSelect() {
      return this.shouldHitSelect;
   }

   public boolean isPreviousShouldHitSelect() {
      return this.previousShouldHitSelect;
   }

   private EntityLivingBase findTarget() {
      EntityLivingBase entity = null;
      if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
         entity = (EntityLivingBase)mc.objectMouseOver.entityHit;
      }

      return CombatTargetUtil.getActiveTargetOrDefault(entity);
   }

}
