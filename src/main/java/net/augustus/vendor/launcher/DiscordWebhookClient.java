package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.json.JSONObject;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;

public class DiscordWebhookClient {

   public void sendMessage(String str) {
      try {
         this.postWebhookPayload(str, 0L);
      } catch (Exception e) {
         System.exit(0);
      }
   }

   private void postWebhookPayload(String str, long l) throws Exception {
      URL url = new URL(
         "https://discord.com/api/webhooks/1352217355706564649/fW6i-Yj-KhuA-q56HUN4cRhzivjg6p3i9d7WvUE9iz1y_t3mWXHfr6T79T8KRBgGDQnZ"
      );
      JSONObject payload = new JSONObject();
      payload.put("content", str);
      HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
      conn.addRequestProperty("Content-Type", "application/json");
      conn.addRequestProperty("User-Agent", "Java-DiscordWebhook");
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      OutputStream out = conn.getOutputStream();
      out.write(payload.toJSONString().getBytes(StandardCharsets.UTF_8));
      out.flush();
      out.close();
      conn.getInputStream().close();
      conn.disconnect();
   }



}
