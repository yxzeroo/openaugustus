package net.augustus.modules.modes.criticals;

import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.AttackEntityListener;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Criticals;
import net.augustus.settings.DoubleValue;

public class IntaveLowHopCriticalsMode extends CriticalsMode implements AttackEntityListener {

   public IntaveLowHopCriticalsMode(Criticals criticals, DoubleValue doubleValue) {
      super(criticals, doubleValue, "IntaveLowHop");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 1;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new IntaveLowHopCriticalsMode((Criticals)obj, (DoubleValue)obj2);
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      if (this.shouldCrit() && mc.thePlayer.onGround && !mc.thePlayer.isBurning() && mc.thePlayer.hurtTime != 10 && mc.thePlayer.hurtTime != 9) {
         mc.thePlayer.motionY = 0.085;
      }
   }

}
