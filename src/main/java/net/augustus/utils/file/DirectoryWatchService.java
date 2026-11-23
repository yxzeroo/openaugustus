package net.augustus.utils.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import net.augustus.utils.TimeHelper;

public class DirectoryWatchService {
   private static final Map WATCHERS = new HashMap();
   private static final WatchService WATCH_SERVICE;

   private static void pollEvents() throws InterruptedException {
      WatchKey watchKey = WATCH_SERVICE.take();
      DirectoryWatcher directoryWatcher = (DirectoryWatcher)WATCHERS.get(watchKey);
      Path path = Paths.get(DirectoryWatcher.getDirectoryPath(directoryWatcher));

      for (WatchEvent watchEvent : watchKey.pollEvents()) {
         Kind kind = watchEvent.kind();
         Path path2 = (Path)watchEvent.context();
         Path path3 = path.resolve(path2);
         if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            if (Files.isRegularFile(path3)) {
               Duration duration = Duration.ofMillis(300L);
               Duration duration2 = Duration.ofMillis(100L);
               Duration duration3 = Duration.ofSeconds(4L);
               Duration duration4 = duration2;
               Duration duration5 = duration;
               ClientFileUtil.waitForFileStable(path, duration5, duration4, duration3);
               DirectoryWatcher.dispatchCreate(directoryWatcher, path3);
            }
         } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
            if (Files.isRegularFile(path3)) {
               Duration duration6 = Duration.ofMillis(300L);
               Duration duration7 = Duration.ofMillis(100L);
               Duration duration8 = Duration.ofSeconds(4L);
               Duration duration9 = duration7;
               Duration duration10 = duration6;
               ClientFileUtil.waitForFileStable(path, duration10, duration9, duration8);
               DirectoryWatcher.dispatchModify(directoryWatcher, path3);
            }
         } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
            DirectoryWatcher.dispatchDelete(directoryWatcher, path3);
         }
      }

      watchKey.reset();
   }

   private static void watchLoop() {

      try {
         while (true) {
            pollEvents();
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static WatchService getWatchService() {
      return WATCH_SERVICE;
   }

   public static Map getWatchers() {
      return WATCHERS;
   }

   static {
      String str = "Watcher";

      try {
         WATCH_SERVICE = FileSystems.getDefault().newWatchService();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }

      Thread thread = new Thread(null, () -> watchLoop(), str);
      thread.start();
   }

   public static DirectoryWatcher watch(String str) {
      return new DirectoryWatcher(str);
   }

}
