package net.augustus.modules.misc;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.TextInputSetting;
import net.augustus.ui.menu.AbstractMainMenuScreen;
import net.augustus.events.listeners.DisconnectListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

public class AutoReconnect extends Module implements TickListener, DisconnectListener {
   private final BooleansSetting mode;
   private final TextInputSetting serverIP;
   private final BooleanValue reconnectOnManualDisconnect;
   private final BooleanValue reconnectMessage;
   private final BooleanValue abortReconnectProcessAtJoin;
   private final RandomizedRangeSettings reconnectDelay;
   private final TimeHelper timeHelper;
   private boolean reconnectPending;
   private String lastServerIp;
   private int lastServerPort;
   private boolean reconnectedFlag;

   public AutoReconnect() {
       super("AutoReconnect", "AutoReconnect", Color.CYAN, Categorys.MISC);
      this.mode = (BooleansSetting)(new BooleansSetting(this, "Mode", "LastServer", new String[]{"LastServer", "CustomServer"})).markVisualOnly();
      TextInputSetting textInputSetting = new TextInputSetting(this, "ServerIP", "localhost:25565");
      SettingVisibilityCondition condition = () -> this.isCustomServerMode();
      this.serverIP = (TextInputSetting)((TextInputSetting)textInputSetting.withVisibility(condition)).markVisualOnly();
      this.reconnectOnManualDisconnect = (BooleanValue)(new BooleanValue(this, "ReconnectOnManualDisconnect", false)).markVisualOnly();
      this.reconnectMessage = (BooleanValue)(new BooleanValue(this, "ReconnectMessage", true)).markVisualOnly();
      this.abortReconnectProcessAtJoin = (BooleanValue)(new BooleanValue(this, "AbortReconnectProcessAtJoin", true)).markVisualOnly();
      this.reconnectDelay = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, "ReconnectDelay", 5.0, 6.0, 0.0, 30.0, 1)).withoutNotInARow().markAllVisualOnly();
      this.timeHelper = new TimeHelper();
      this.reconnectPending = false;
      this.lastServerIp = "";
      this.lastServerPort = 25565;
      this.reconnectedFlag = false;
   }

   @Override
   public void onEnable() {
      this.reconnectPending = false;
      this.reconnectedFlag = false;
   }

   @Override
   public void onDisable() {
      this.reconnectPending = false;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 62;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onDisconnect2() {
      if (this.reconnectOnManualDisconnect.getValue() && !mc.isSingleplayer()) {
         this.captureLastServerAddress();
         this.reconnectPending = true;
         this.timeHelper.reset();
      }
   }

   @Override
   public void onTick3() {
      if (mc.currentScreen instanceof GuiDisconnected && !mc.isSingleplayer() && !this.reconnectPending) {
         this.captureLastServerAddress();
         this.reconnectPending = true;
         this.timeHelper.reset();
      }

      if (mc.theWorld != null && mc.thePlayer != null && this.reconnectedFlag && this.reconnectMessage.getValue()) {
         String str = "Automatic reconnect successfully :)";
         this.sendChatMessage2(str);
         this.reconnectedFlag = false;
      }

      if ((mc.currentScreen instanceof GuiConnecting || mc.isSingleplayer() || mc.currentScreen == null) && this.reconnectPending && this.abortReconnectProcessAtJoin.getValue()) {
         this.reconnectPending = false;
      }

      if (this.reconnectPending) {
         long l = (long)(this.reconnectDelay.getLastValue() * 1000.0);
         if (this.timeHelper.reached2(l)) {
            this.connectToServer();
            this.reconnectPending = false;
         }
      }
   }

   private void connectToServer() {
      String str;
      int n;
      if (this.mode.getSelectedValue().equals("CustomServer")) {
         String value = this.serverIP.getValue();
         if (value.contains(":")) {
            String[] strings = value.split(":");
            str = strings[0];

            try {
               n = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
               n = 25565;
            }
         } else {
            str = value;
            n = 25565;
         }
      } else {
         str = this.lastServerIp;
         n = this.lastServerPort;
      }

      if (str != null && !str.isEmpty()) {
         String str2 = str + ":" + n;
         ServerData serverData = new ServerData(str2, str2, false);
         Minecraft mc2 = mc;
         GuiConnecting guiConnecting = new GuiConnecting(new GuiMultiplayer(AbstractMainMenuScreen.createMainMenuScreen()), mc, serverData);
         mc2.displayGuiScreen(guiConnecting);
         this.reconnectDelay.nextValue();
         this.reconnectedFlag = true;
      }
   }

   private void captureLastServerAddress() {
      this.lastServerIp = GuiConnecting.lastServerIp;
      this.lastServerPort = GuiConnecting.lastServerPort;
   }

   private boolean isCustomServerMode() {
      return this.mode.getSelectedValue().equals("CustomServer");
   }

}
