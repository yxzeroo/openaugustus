package net.augustus.events;

import net.augustus.events.listeners.KeyListener;
import de.florianmichael.dietrichevents2.CancellableEvent;

public class EventKey extends CancellableEvent {
   private int keyCode;
   private boolean mouse;
   private KeyInputSource source;
   public static final int ID = 14;
   private boolean pressed;

   public static void reset(EventKey event, KeyInputSource keyInputSource, int n, boolean flag, boolean flag2) {
      event.reset(keyInputSource, n, flag, flag2);
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public boolean isPressed() {
      return this.pressed;
   }

   public boolean isMouse() {
      return this.mouse;
   }

   public void call(Object obj) {
      this.call2((KeyListener)obj);
   }

   public void call2(KeyListener listener) {
      if (this.source == KeyInputSource.TOGGLE_UTIL) {
         listener.onKey2(this);
      } else if (this.source == KeyInputSource.KEYBOARD) {
         listener.onKey3(this);
      } else {
         listener.onKey(this);
      }
   }

   private void reset(KeyInputSource keyInputSource, int n, boolean flag, boolean flag2) {
      this.keyCode = n;
      this.mouse = flag;
      this.pressed = flag2;
      this.source = keyInputSource;
      this.setCancelled(false);
   }
}
