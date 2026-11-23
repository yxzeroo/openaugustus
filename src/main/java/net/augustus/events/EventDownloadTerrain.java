package net.augustus.events;

import net.augustus.events.listeners.DownloadTerrainListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventDownloadTerrain extends AbstractEvent {
   public static final int ID = 8;

   public void call2(DownloadTerrainListener listener) {
      listener.onDownloadTerrain();
   }

   @Override
   public void call(Object obj) {
      this.call2((DownloadTerrainListener)obj);
   }
}
