package net.augustus.viaversion.platform;

import net.augustus.Augustus;
import com.viaversion.viaversion.platform.UserConnectionViaVersionPlatform;
import java.io.File;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

public final class ViaAugustusPlatform extends UserConnectionViaVersionPlatform {
   private static int[] platformStateFlags;

   public ViaAugustusPlatform() {
      super(new File("ViaVersion"));
   }

   public Logger createLogger(String str) {
      return new Slf4jJulLoggerAdapter(LoggerFactory.getLogger(str));
   }

   public String getPlatformVersion() {
      return Augustus.getInstance().getVersion();
   }

   public boolean isProxy() {
      return true;
   }

   public String getPlatformName() {
      return "Augustus";
   }

   public static void setPlatformStateFlags(int[] ints) {
      platformStateFlags = ints;
   }

   public static int[] getPlatformStateFlags() {
      return platformStateFlags;
   }

   static {
      int[] ints = new int[3];
      setPlatformStateFlags(ints);
   }
}
