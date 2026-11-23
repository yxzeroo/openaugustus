package net.augustus.events.listeners;


import net.augustus.events.EventChunkUnload;
import net.augustus.events.EventManager;
public interface ChunkUnloadListener {
   EventChunkUnload CHUNK_UNLOAD_EVENT = new EventChunkUnload();

   static void onChunkUnload(long l) {
      EventChunkUnload.reset(CHUNK_UNLOAD_EVENT, l);
      EventManager.getEventBus().post(64, CHUNK_UNLOAD_EVENT);
   }

   void onChunkUnload2(long l);
}
