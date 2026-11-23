package net.augustus.events;

import net.augustus.events.listeners.AttackEntityListener;
import de.florianmichael.dietrichevents2.AbstractEvent;
import de.florianmichael.dietrichevents2.StateTypes;
import net.minecraft.entity.Entity;

public class EventAttackEntity extends AbstractEvent {
   public static final int ID = 1;
   private StateTypes state;
   private Entity target;

   public void call(Object obj) {
      AttackEntityListener listener = (AttackEntityListener)obj;
      this.call2(listener);
   }

   public Entity getTarget() {
      return this.target;
   }

   public void call2(AttackEntityListener listener) {
      if (this.state == StateTypes.PRE) {
         listener.onAttackEntity(this);
      } else {
         listener.onAttackEntity();
      }
   }

   public static void reset(EventAttackEntity event, Entity entity, StateTypes stateTypes) {
      event.reset(entity, stateTypes);
   }

   private void reset(Entity entity, StateTypes stateTypes) {
      this.state = stateTypes;
      this.target = entity;
   }
}
