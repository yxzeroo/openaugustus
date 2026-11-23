package net.optifine.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.optifine.config.Config;
import net.optifine.http.IFileDownloadListener;

public class PlayerConfigurationReceiver implements IFileDownloadListener {
   private String player = null;

   public PlayerConfigurationReceiver(String str) {
      this.player = str;
   }

   @Override
   public void fileDownloadFinished(String str, byte[] bytes, Throwable t) {
      if (bytes != null) {
         try {
            String str2 = new String(bytes, "ASCII");
            JsonParser jsonParser = new JsonParser();
            JsonElement elem = jsonParser.parse(str2);
            PlayerConfigurationParser parser = new PlayerConfigurationParser(this.player);
            PlayerConfiguration playerConfiguration = parser.parsePlayerConfiguration(elem);
            if (playerConfiguration != null) {
               playerConfiguration.setInitialized(true);
               PlayerConfigurations.setPlayerConfiguration(this.player, playerConfiguration);
            }
         } catch (Exception e) {
            Config.dbg("Error parsing configuration: " + str + ", " + e.getClass().getName() + ": " + e.getMessage());
         }
      }
   }
}
