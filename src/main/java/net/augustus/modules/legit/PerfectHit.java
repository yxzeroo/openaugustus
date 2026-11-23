package net.augustus.modules.legit;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.utils.TickCounter;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;

public class PerfectHit extends Module implements ClickListener {
   private final StringValue maxDelaySinceLastHit;
   private final RandomizedRangeSettings cps;
   private final RandomizedRangeSettings cpsRecalculateDelay;
   private final RandomizedRangeSettings dcps;
   private final RandomizedRangeSettings dcpsRecalculateDelay;
   private final BooleanValue reduceCPSIfYouCanNotHitATarget;
   private final BooleanValue forcePerfectHitClicks;
   private final TimeHelper timeHelper;
   private final ActionRateAccumulator cpsAccumulator;
   private final ActionRateAccumulator dcpsAccumulator;
   private final TickCounter cpsRecalculateCounter;
   private final TickCounter dcpsRecalculateCounter;

   public PerfectHit() {
       super("PerfectHit", "PerfekterSchlag", new Color(240, 115, 186), Categorys.LEGIT);
      this.maxDelaySinceLastHit = new StringValue(this, "MaxDelaySinceLastHit", 300.0, 100.0, 1000.0, 0);
      this.cps = new RandomizedRangeSettings(this, "CPS", 20.0, 20.0, 0.0, 20.0, 0);
      this.cpsRecalculateDelay = (new RandomizedRangeSettings(this, "CPSRecalculateDelay", 3.0, 8.0, 0.0, 60.0, 0)).withoutNotInARow();
      this.dcps = new RandomizedRangeSettings(this, "DCPS", 0.0, 0.0, 0.0, 20.0, 0);
      this.dcpsRecalculateDelay = (new RandomizedRangeSettings(this, "DCPSRecalculateDelay", 3.0, 8.0, 0.0, 60.0, 0)).withoutNotInARow();
      this.reduceCPSIfYouCanNotHitATarget = new BooleanValue(this, "ReduceCPSIfYouCanNotHitATarget", true);
      this.forcePerfectHitClicks = new BooleanValue(this, "ForcePerfectHitClicks", false);
      this.timeHelper = new TimeHelper();
      this.cpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.dcpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.cpsRecalculateCounter = new TickCounter();
      this.dcpsRecalculateCounter = new TickCounter();
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      int n = 99999;
      byte b = 7;
      this.toggleEventSubscription(b, n);
   }

   public boolean canPerfectHit() {
      if (ClientManagers.moduleManager.perfectHit.isToggled() && mc.isNotInGui()) {
         TimeHelper timeHelper2 = this.timeHelper;
         double value = this.maxDelaySinceLastHit.getValue();
         if (!timeHelper2.reached(value)
            && mc.objectMouseOver != null
            && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
            && mc.objectMouseOver.entityHit instanceof EntityLivingBase
            && ((EntityLivingBase)mc.objectMouseOver.entityHit).hurtTime <= 1) {
            return true;
         }
      }

      return false;
   }

   public void onClick(EventClick event) {
      if (mc.thePlayer != null && !mc.thePlayer.isUsingItem() && (mc.gameSettings.keyBindAttack.getPressTime() > 0 || event.getLeftClicks() > 0)) {
         this.timeHelper.reset();
      }

      if (this.canPerfectHit() && (event.getLeftClicks() <= 0 && (mc.gameSettings.keyBindAttack.getPressTime() <= 0 || event.isIgnoreVanillaClicks()) || this.forcePerfectHitClicks.getValue())) {
         if (this.forcePerfectHitClicks.getValue()) {
            event.clearLeftClicks();

            while (mc.gameSettings.keyBindAttack.isPressed()) {
            }
         }

         TickCounter cpsRecalculateCounter2 = this.cpsRecalculateCounter;
         int lastIntValue = this.cpsRecalculateDelay.getLastIntValue();
         if (cpsRecalculateCounter2.hasReached2(lastIntValue)) {
            this.cpsRecalculateCounter.reset();
            this.cpsRecalculateDelay.nextSecureValue();
            this.cps.nextSecureValue();
         }

         cpsRecalculateCounter2 = this.dcpsRecalculateCounter;
         int lastIntValue2 = this.dcpsRecalculateDelay.getLastIntValue();
         if (cpsRecalculateCounter2.hasReached2(lastIntValue2)) {
            this.dcpsRecalculateCounter.reset();
            this.dcpsRecalculateDelay.nextSecureValue();
            this.dcps.nextSecureValue();
         }

         ActionRateAccumulator cpsAccumulator2 = this.cpsAccumulator;
         int lastIntValue3 = this.cps.getLastIntValue();
         int n = this.adjustClickCount(lastIntValue3);
         int n2 = cpsAccumulator2.accumulate(n);
         if (n2 > 0) {
            ActionRateAccumulator dcpsAccumulator2 = this.dcpsAccumulator;
            int lastIntValue4 = this.dcps.getLastIntValue();
            int n3 = this.adjustClickCount(lastIntValue4);
            n2 += dcpsAccumulator2.accumulate(n3);
         }

         event.addLeftClicks(n2);
      }

      this.cpsRecalculateCounter.increment();
      this.dcpsRecalculateCounter.increment();
   }

   private boolean shouldReduceCpsForTarget() {
      if (this.reduceCPSIfYouCanNotHitATarget.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityBlaze) {
         EntityLivingBase entityHit = (EntityLivingBase)mc.objectMouseOver.entityHit;
         return entityHit.hurtTime == 0 && System.currentTimeMillis() - entityHit.lastDamageAttackTime > 75L && System.currentTimeMillis() - entityHit.lastDamageAttackTime < 225L;
      } else {
         return false;
      }
   }

   private int adjustClickCount(int n) {
      if (!this.shouldReduceCpsForTarget()) {
         return n;
      } else {
         if (ClientManagers.moduleManager.debug.devToggleF != null && ClientManagers.moduleManager.debug.devToggleF.getValue() && ClientManagers.moduleManager.debug.isToggled()) {
            String str = "Reduce hits";
            ClientManagers.moduleManager.debug.sendChatMessage2(str);
         }

         return 0;
      }
   }

}
