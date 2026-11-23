package net.augustus.modules.misc;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.ChatUtil;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.Augustus;

public class IRCChat extends Module implements LoadWorldListener {
   private String lastWhisperPartner;

   public IRCChat() {
       super("IRCChat", "ZwischennetzWeiterleitungsPlaudern", new Color(235, 64, 52), Categorys.MISC);
      this.lastWhisperPartner = "";
   }

   public void onClientInit() {
      boolean flag = true;
      this.setToggled(flag);
   }

   public void onEnable() {
   }

   public void onDisable() {
      this.lastWhisperPartner = "";
   }

   public void updateEventSubscriptions() {
      byte b = 17;
      this.toggleEventSubscription(b);
   }

   public void receiveMessage(String str, String str2, String str3, String str4, int n, int n2, String str5, boolean flag) {
      String str6 = str.isEmpty() ? "" : "§7" + str + " ";
      String str7;
      if (flag) {
         this.lastWhisperPartner = str.equals(Augustus.getInstance().getUsername()) ? str3 : str;
         StringBuilder sb = new StringBuilder().append(str6).append("§7[§r").append(str2);
         short s = 21019;
         sb = sb.append("§r§7]").append(n > 0 ? " [§e" + n + "§7]" : "").append(" -> ").append(str3).append(" §7[§r").append(str4);
         short s2 = 11494;
         str7 = sb.append("§r§7]").append(n2 > 0 ? " [§e" + n2 + "§7]" : "").append(": §r").append(str5).toString();
      } else {
         StringBuilder sb2 = new StringBuilder().append(str6).append("§7[§r").append(str2);
         short s3 = 11494;
         str7 = sb2.append("§r§7]").append(n > 0 ? " [§e" + n + "§7]" : "").append(":§r ").append(str5).toString();
      }

      String str8 = ChatUtil.sendFormattedChat("IRC", str7);
      Augustus.getInstance().getClientTabManager().ircMenuTab.appendMessage(str8);
   }

   public void onLoadWorld(EventLoadWorld event) {
   }

   public String getLastWhisperPartner() {
      return this.lastWhisperPartner;
   }

}
