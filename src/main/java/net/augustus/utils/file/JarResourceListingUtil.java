package net.augustus.utils.file;

import net.augustus.Augustus;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class JarResourceListingUtil {

   public static ArrayList listResourceEntries(String str) {
      ArrayList list = new ArrayList();
      Augustus.getInstance().getClass();

      try {
         String str2 = ClientFileUtil.WORKING_DIR + "resources.jar!/assets/augustus/" + str;
         str2 = "file:/" + str2.replace('\\', '/');
         String[] strings = str2.split("!/");
         if (str2.contains(".jar") && strings.length == 2 && strings[0].startsWith("file:")) {
            FileSystem fileSystem = FileSystems.newFileSystem(new URI("jar", strings[0], null), new HashMap<>());
            Files.list(fileSystem.getPath(strings[1])).forEach(path -> {
               String[] strings2 = path.toString().split("/");
               list.add(strings2[strings2.length - 1]);
            });
            fileSystem.close();
            return list;
         } else {
            throw new Exception("bad URI for jar resource");
         }
      } catch (Exception e) {
         e.printStackTrace();
         return list;
      }
   }

}
