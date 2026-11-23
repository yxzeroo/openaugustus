package net.augustus.events;

import net.augustus.events.listeners.SafeWalkListener;
import de.florianmichael.dietrichevents2.AbstractEvent;
import de.florianmichael.dietrichevents2.StateTypes;

public class EventSafeWalk extends AbstractEvent {
   public static final int ID = 33;
   private boolean disableSneakSafeWalk;
   private boolean safeWalk;
   private boolean clippedAtEdge;
   private StateTypes state;

   public void setDisableSneakSafeWalk(boolean flag) {
      this.disableSneakSafeWalk = flag;
   }

   public boolean isSafeWalk() {
      return this.safeWalk;
   }

   public boolean isDisableSneakSafeWalk() {
      return this.disableSneakSafeWalk;
   }

   public void call2(SafeWalkListener listener) {
      if (this.state == StateTypes.PRE) {
         listener.onSafeWalk(this);
      } else {
         listener.onSafeWalk2(this);
      }
   }

   public static void reset(EventSafeWalk event, StateTypes stateTypes, boolean flag) {
      event.reset(stateTypes, flag);
   }

   public void call(Object obj) {
      this.call2((SafeWalkListener)obj);
   }

   public boolean isClippedAtEdge() {
      return this.clippedAtEdge;
   }

   public void setSafeWalk(boolean flag) {
      this.safeWalk = flag;
   }

   private void reset(StateTypes stateTypes, boolean flag) {
      this.state = stateTypes;
      this.clippedAtEdge = flag;
      this.safeWalk = false;
      this.disableSneakSafeWalk = false;
   }
}
