package net.augustus.settings;

import net.augustus.events.listeners.AsyncTickListener;
import net.augustus.vendor.security.SessionTokenTransformer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.augustus.modules.misc.Dummy;

public class SettingManager {
   public static String sessionToken;
   private final CopyOnWriteArrayList settings = new CopyOnWriteArrayList();

   public List getSettings() {
      return this.settings;
   }

   public void updateSessionToken(String str) {
      sessionToken = new SessionTokenTransformer().transformToken(str);
   }

   static {
      new Thread(() -> {

         while (true) {
            AsyncTickListener.onAsyncTick2();

            try {
               Thread.sleep(1L);
            } catch (InterruptedException e) {
               throw new RuntimeException(e);
            }
         }
      }).start();
   }

   public void registerSetting(Setting setting) {
      if (setting.getParentModule() != null && !(setting.getParentModule() instanceof Dummy)) {
         this.settings.add(setting);
      }
   }
}
