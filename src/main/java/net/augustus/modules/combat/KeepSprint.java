package net.augustus.modules.combat;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.AttackSlowdownListener;
import net.augustus.events.EventAttackSlowdown;
import net.augustus.utils.combat.CombatOptimizationUtil;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class KeepSprint extends Module implements AttackSlowdownListener {
   private final StringValue moveSlowdownOnGround;
   private final BooleanValue sprintOnGround;
   private final StringValue moveSlowdownInAir;
   private final BooleanValue sprintOnGroundInAir;
   private final BooleanValue smart;

   public KeepSprint() {
       super("KeepSprint", "BeibehaltenSprinten", new Color(66, 132, 245), Categorys.COMBAT);
      this.moveSlowdownOnGround = new StringValue(this, "MoveSlowdownOnGround", 1.0, 0.0, 1.0, 2);
      this.sprintOnGround = new BooleanValue(this, "SprintOnGround", true);
      this.moveSlowdownInAir = new StringValue(this, "MoveSlowdownInAir", 1.0, 0.0, 1.0, 2);
      this.sprintOnGroundInAir = new BooleanValue(this, "SprintOnGroundInAir", true);
      this.smart = new BooleanValue(this, "Smart", true);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 10;
      byte b2 = 2;
      this.toggleEventSubscription(b2, b);
   }

   public void onAttackSlowdown(EventAttackSlowdown event) {
      if (this.smart.getValue()) {
         if (event.isSimulated()) {
            return;
         }

         MovingObjectPosition objectMouseOver = mc.objectMouseOver;
         if (objectMouseOver != null && objectMouseOver.entityHit != null) {
            EntityLivingBase activeTargetOrDefault = CombatTargetUtil.getActiveTargetOrDefault(objectMouseOver.entityHit instanceof EntityLivingBase ? (EntityLivingBase)objectMouseOver.entityHit : null);
            if (activeTargetOrDefault != null) {
               float rotationYaw = mc.thePlayer.rotationYaw;
               if (!CombatOptimizationUtil.shouldReduceSprintKnockback(activeTargetOrDefault, rotationYaw)) {
                  return;
               }
            }
         }
      }

      if (mc.thePlayer.onGround) {
         double value = this.moveSlowdownOnGround.getValue();
         event.setSlowDownFactor(value);
         boolean value2 = this.sprintOnGround.getValue();
         event.setSprint(value2);
      } else {
         double value3 = this.moveSlowdownInAir.getValue();
         event.setSlowDownFactor(value3);
         boolean value4 = this.sprintOnGroundInAir.getValue();
         event.setSprint(value4);
      }
   }

}
