package net.augustus.modules.modes.criticals;

import net.minecraft.util.MovingObjectPosition;
import net.augustus.events.EventClick;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.RayCastListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.combat.CombatTargetUtil;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Criticals;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;

public class LagCriticalsMode extends CriticalsMode implements PacketDelayListener, RayCastListener, LoadWorldListener, ClickListener {
   private final StringValue lagDuration;
   private final BooleanValue forceHit;
   private static boolean delayingPackets = true;
   private final TimeHelper lagDurationTimer;
   private final TimeHelper targetTimeoutTimer;
   private EntityLivingBase currentTarget;
   private int queuedClickCount;
   private boolean forceLagActive;

   public LagCriticalsMode(Criticals criticals, DoubleValue doubleValue) {
       super(criticals, doubleValue, "Lag");
      StringValue stringValue = new StringValue(this.parentModule, "LagDuration", 200.0, 0.0, 400.0, 0, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.lagDuration = stringValue.visibleWhen(condition);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "ForceHit", true, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.forceHit = (BooleanValue)booleanValue.withVisibility(condition2);
      this.lagDurationTimer = new TimeHelper();
      this.targetTimeoutTimer = new TimeHelper();
      this.currentTarget = null;
      this.queuedClickCount = 0;
      this.forceLagActive = false;
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new LagCriticalsMode((Criticals)obj, (DoubleValue)obj2);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 25;
      this.toggleEventSubscription(b);
      byte b2 = 29;
      this.toggleEventSubscription(b2);
      short s = 10000;
      byte b3 = 7;
      this.toggleEventSubscription(b3, s);
      byte b4 = 17;
      this.toggleEventSubscription(b4);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.currentTarget = null;
      this.stopLagging();
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      boolean flag = delayingPackets;
      event.setDelayed(flag);
   }

   @Override
   public void onRayCast() {
      if (mc.objectMouseOver == null) {
         this.stopLagging();
      } else {
         EntityLivingBase activeTargetOrDefault = CombatTargetUtil.getActiveTargetOrDefault(mc.objectMouseOver.entityHit instanceof EntityLivingBase ? (EntityLivingBase)mc.objectMouseOver.entityHit : null);
         if (activeTargetOrDefault != null) {
            this.currentTarget = activeTargetOrDefault;
            this.targetTimeoutTimer.reset();
         }

         long l = 500L;
         if (this.targetTimeoutTimer.reached2(l)) {
            this.currentTarget = null;
         }

         if (this.currentTarget == null) {
            this.stopLagging();
         } else {
            if ((this.currentTarget.hurtTime > 2 || this.forceLagActive)
               && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
               && mc.objectMouseOver.entityHit != null
               && mc.objectMouseOver.entityHit.getEntityId() == this.currentTarget.getEntityId()
               && this.shouldCrit()) {
               if (!delayingPackets) {
                  this.lagDurationTimer.reset();
               }

               delayingPackets = true;
            }

            if ((this.currentTarget.hurtTime == 0 || this.currentTarget.hurtTime == 10) && !this.forceLagActive) {
               this.stopLagging();
            }

            TimeHelper lagDurationTimer2 = this.lagDurationTimer;
            double value = this.lagDuration.getValue();
            if (lagDurationTimer2.reached(value)) {
               this.stopLagging();
            }
         }
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.currentTarget = null;
   }

   public static boolean isDelayingPackets() {
      return delayingPackets;
   }

   @Override
   public void onClick(EventClick event) {
      if (this.currentTarget != null) {
         int n = event.getLeftClicks() + (event.isIgnoreVanillaClicks() ? 0 : mc.gameSettings.keyBindAttack.getPressTime());
         if (n > 0 && this.currentTarget.hurtTime < 2 && !delayingPackets) {
         }

         if (delayingPackets) {
            if (n <= 0 && this.queuedClickCount == 0) {
               byte b = 1;
               event.addLeftClicks(b);
               n++;
            }

            this.queuedClickCount += n;
         }
      }
   }

   private void stopLagging() {
      delayingPackets = false;
      this.queuedClickCount = 0;
      this.forceLagActive = false;
   }

}
