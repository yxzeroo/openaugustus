package net.augustus.events;

import net.augustus.events.listeners.ChunkLoadListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventChunkLoad extends AbstractEvent {
   public static final int ID = 56;
   private int chunkZ;
   private int chunkX;

   @Override
   public void call(Object obj) {
      ChunkLoadListener listener = (ChunkLoadListener)obj;
      this.call2(listener);
   }

   public EventChunkLoad(int n, int n2) {
      this.chunkX = n;
      this.chunkZ = n2;
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public void call2(ChunkLoadListener listener) {
      listener.onChunkLoad(this);
   }

   public int getChunkZ() {
      return this.chunkZ;
   }
}
