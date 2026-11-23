package de.wazed.wrapper.generation;

import de.wazed.wrapper.utils.WebUtil;
import de.wazed.wrapper.utils.exceptions.NotFoundException;
import de.wazed.wrapper.utils.exceptions.UnauthorizedException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

public class Generator {
   private static Generator instance;

   public Generator() {
      instance = this;
   }

   public Account generate(String token) throws UnauthorizedException, NotFoundException {
      try {
         List<String> lines = WebUtil.getInstance().performRequest("http://api.thealtening.com/v1/generate?token=" + token + "&info=true");
         StringBuilder sb = new StringBuilder();

         for (String line : lines) {
            sb.append(line).append("\n");
         }

         JSONObject object = new JSONObject(sb.toString());
         new HashMap();
         HashMap<String, String> infoMap = new HashMap<>();

         for (String key : object.getJSONObject("info").keySet()) {
            infoMap.put(key, object.getJSONObject("info").getString(key));
         }

         return new Account(object.getString("token"), object.getString("password"), object.getString("username"), object.getBoolean("limit"), infoMap);
      } catch (IOException e) {
         if (e.getMessage().contains("403")) {
            throw new UnauthorizedException();
         } else if (e.getMessage().contains("404")) {
            throw new NotFoundException();
         } else {
            return null;
         }
      }
   }

   public static Generator getInstance() {
      return instance;
   }
}
