package net.augustus.viaversion;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.config.ConfigManager;
import net.augustus.ui.BungeeHackScreen;
import net.augustus.ui.menu.QuickMenuBuilder;
import net.augustus.ui.widgets.QuickActionsSidebar;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;

public class MultiplayerProtocolMenu {
   private static final QuickActionsSidebar SIDEBAR;

   private static void selectProtocolVersion(ProtocolVersion protocolVersion) {
      ViaAugustus.targetProtocolVersion = protocolVersion;
      StringValue protocolVersionVal = ClientManagers.moduleManager.protocolVersion.protocolVersionVal;
      double version = (double)protocolVersion.getVersion();
      protocolVersionVal.setValue(version);
      ConfigManager configManager = Augustus.getInstance().getConfigManager();
      configManager.snapshotAll();
   }

   private static void cycleBansDisplayMode(BooleansSetting booleansSetting) {
      String selectedValue = Augustus.getInstance().getModuleManager().augustusServer.banStatistics.getSelectedValue();
      switch (selectedValue) {
         case "Icon":
            String str = "Left";
            booleansSetting.setSelectedValue(str);
            break;
         case "Left":
            String str2 = "Right";
            booleansSetting.setSelectedValue(str2);
            break;
         case "Right":
            String str3 = "None";
            booleansSetting.setSelectedValue(str3);
            break;
         case "None":
            String str4 = "Icon";
            booleansSetting.setSelectedValue(str4);
      }
   }

   static {
      QuickMenuBuilder quickMenuBuilder = QuickActionsSidebar.builder();
      String str = "Quick actions";
      quickMenuBuilder.addHeader(str);
      quickMenuBuilder.addSpacer();
      Runnable runnable = () -> {
         BungeeHackScreen bungeeHackScreen = new BungeeHackScreen(Minecraft.getMinecraft().currentScreen);
         Minecraft.getMinecraft().displayGuiScreen(bungeeHackScreen);
      };
      String str2 = "Bungee Hack";
      quickMenuBuilder.addAction(str2, runnable);
      Runnable runnable2 = () -> {
         Session session = Minecraft.getMinecraft().session;
         if (session != null) {
            GuiScreen.setClipboardString(session.getUsername());
         }
      };
      String str3 = "Copy IGN";
      quickMenuBuilder.addAction(str3, runnable2);
      BooleanValue fastServerPining1 = Augustus.getInstance().getModuleManager().augustusServer.fastServerPining1;
      String str4 = "Fast Server Ping";
      quickMenuBuilder.addBooleanSetting(str4, fastServerPining1);
      BooleansSetting banStatistics = Augustus.getInstance().getModuleManager().augustusServer.banStatistics;
      Supplier supplier = () -> formatBansDisplayLabel(banStatistics);
      Runnable runnable3 = () -> cycleBansDisplayMode(banStatistics);
      Supplier supplier2 = supplier;
      quickMenuBuilder.addDynamicAction(supplier2, runnable3);
      quickMenuBuilder.addSpacer();
      String str5 = "Protocol Hack";
      quickMenuBuilder.addHeader(str5);
      quickMenuBuilder.addSpacer();
      List reversedProtocols = ProtocolVersion.getReversedProtocols();
      Supplier supplier3 = () -> ViaAugustus.targetProtocolVersion;
      Consumer consumer = obj -> selectProtocolVersion((ProtocolVersion)obj);
      Function function = obj2 -> ((ProtocolVersion)obj2).getName();
      Function function2 = obj3 -> ProtocolVersion.getClosest((String)obj3);
      Function function3 = function;
      Consumer consumer2 = consumer;
      Supplier supplier4 = supplier3;
      List list = reversedProtocols;
      quickMenuBuilder.addSelectableOptions(list, supplier4, consumer2, function3, function2);
      SIDEBAR = quickMenuBuilder.build();
   }

   public static QuickActionsSidebar getSidebar() {
      return SIDEBAR;
   }

   private static String formatBansDisplayLabel(BooleansSetting booleansSetting) {
      return "Bans: §6" + booleansSetting.getSelectedValue();
   }

}
