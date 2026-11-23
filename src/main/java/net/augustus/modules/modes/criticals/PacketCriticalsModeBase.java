package net.augustus.modules.modes.criticals;

import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.combat.Criticals;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public abstract class PacketCriticalsModeBase extends CriticalsMode implements AttackEntityListener {
   private final BooleanValue onlyOnHit;
   private boolean pendingAttack;

   public PacketCriticalsModeBase(Criticals criticals, DoubleValue doubleValue, String str) {
       super(criticals, doubleValue, str);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, this.getName() + "OnlyOnHit", false, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      booleanValue = (BooleanValue)booleanValue.withVisibility(condition);
      String str2 = "OnlyOnHit";
      this.onlyOnHit = (BooleanValue)booleanValue.withDisplayName(str2);
      this.pendingAttack = false;
   }

   @Override
   public void onEnable() {
      this.pendingAttack = false;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 1;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      this.pendingAttack = true;
   }

   @Override
   public boolean shouldCrit() {
      return super.shouldCrit() && (this.pendingAttack || !this.onlyOnHit.getValue());
   }

   public void clearPendingAttack() {
      this.pendingAttack = false;
   }

}
