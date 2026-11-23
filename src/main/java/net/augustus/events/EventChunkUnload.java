package net.augustus.events;

import net.augustus.events.listeners.ChunkUnloadListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventChunkUnload extends AbstractEvent {
   public static final int ID = 64;
   private long chunkKey;

   @Override
   public void call(Object obj) {
      this.call2((ChunkUnloadListener)obj);
   }

   private void setChunkKey(long l) {
      this.chunkKey = l;
   }

   public static void reset(EventChunkUnload event, long l) {
      event.setChunkKey(l);
   }

   public void call2(ChunkUnloadListener listener) {
      listener.onChunkUnload2(this.chunkKey);
   }
}
