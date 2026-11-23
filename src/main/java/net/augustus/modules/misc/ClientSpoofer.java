package net.augustus.modules.misc;

import io.netty.buffer.Unpooled;
import java.awt.Color;
import java.util.Random;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.settings.TextInputSetting;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.minecraft.client.resources.Language;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ClientSpoofer extends Module implements PacketListener {
   private final BooleansSetting clients;
   public final TextInputSetting customSpoofString;
   private final BooleanValue randomizeClientSettings;
   private final BooleanValue generateNewRandomizationOnlyAfterAccountchange;
   private final RandomizedRangeSettings randomViewDistance;
   private String lastPlayerUuid;
   private C15PacketClientSettings cachedClientSettingsPacket;

   public ClientSpoofer() {
       super("ClientSpoofer", "KundenVernebler", new Color(163, 240, 223), Categorys.MISC);
      this.clients = (BooleansSetting)(new BooleansSetting(
            this, "Clients", "Default", new String[]{"Default", "LabyMod", "CheatBreaker", "PvPLounge", "Lunar", "Badlion", "Custom", "CancelPacket"}
         ))
         .markVisualOnly();
      TextInputSetting textInputSetting = new TextInputSetting(this, "Custom spoof string", "Example Text");
      SettingVisibilityCondition condition = () -> this.isCustomClientMode();
      this.customSpoofString = (TextInputSetting)((TextInputSetting)textInputSetting.withVisibility(condition)).markVisualOnly();
      this.randomizeClientSettings = (BooleanValue)(new BooleanValue(this, "RandomizeClientSettings", false)).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this, "GenerateNewRandomizationOnlyAfterAccountchange", false);
      BooleanValue booleanValue2 = this.randomizeClientSettings;
      this.randomizeClientSettings.getClass();
      BooleanValue booleanValue3 = booleanValue2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      this.generateNewRandomizationOnlyAfterAccountchange = (BooleanValue)((BooleanValue)booleanValue.withVisibility(condition2)).markVisualOnly();
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "RandomViewDistance", 10.0, 12.0, 2.0, 16.0, 0);
      booleanValue2 = this.randomizeClientSettings;
      this.randomizeClientSettings.getClass();
      BooleanValue booleanValue4 = booleanValue2;
      SettingVisibilityCondition condition3 = () -> booleanValue4.getValue();
      this.randomViewDistance = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition3)).markAllVisualOnly();
      this.lastPlayerUuid = "";
      this.cachedClientSettingsPacket = null;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 26;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onPacket5(EventPacket event) {
      Packet packet = event.getPacket();
      if ((packet instanceof C17PacketCustomPayload || packet instanceof C15PacketClientSettings) && !mc.isIntegratedServerRunning()) {
         if (packet instanceof C17PacketCustomPayload) {
            C17PacketCustomPayload packet2 = (C17PacketCustomPayload)packet;
            this.spoofBrandPayload(packet2, event);
         }

         if (packet instanceof C15PacketClientSettings && this.randomizeClientSettings.getValue()) {
            C15PacketClientSettings packet3 = (C15PacketClientSettings)packet;
            Object[] array = mc.getLanguageManager().getLanguageMap().values().toArray();
            if (array.length > 0 && array[0] instanceof Language[]) {
               String str = mc.getSession() == null ? "" : mc.getSession().getPlayerID();
               if (this.generateNewRandomizationOnlyAfterAccountchange.getValue() && this.lastPlayerUuid.equals(str) && this.cachedClientSettingsPacket != null) {
                  if (this.generateNewRandomizationOnlyAfterAccountchange.getValue() && this.lastPlayerUuid.equals(str)) {
                     packet3.setLang(this.cachedClientSettingsPacket.getLang());
                     int view = this.cachedClientSettingsPacket.getView();
                     mc.gameSettings.renderDistanceChunks = view;
                     packet3.setView(view);
                     packet3.setEnableColors(this.cachedClientSettingsPacket.isColorsEnabled());
                  }
               } else {
                  Language[] languages = (Language[])array;
                  RandomNumberGenerator instance = DefaultRandomGenerator.instance;
                  int n = mc.getLanguageManager().getLanguageMap().values().size() - 1;
                  byte b = 0;
                  packet3.setLang(languages[instance.nextInt2(b, n)].getLanguageCode());
                  int n2 = (int)this.randomViewDistance.nextValue();
                  mc.gameSettings.renderDistanceChunks = n2;
                  packet3.setView(n2);
                  packet3.setEnableColors(new Random().nextBoolean());
               }

               this.lastPlayerUuid = str;
               this.cachedClientSettingsPacket = packet3;
            }
         }
      }
   }

   private void spoofBrandPayload(C17PacketCustomPayload packet, EventPacket event) {
      if (packet.getChannelName().equals("MC|Brand")) {
         String selectedValue = this.clients.getSelectedValue();
         switch (selectedValue) {
            case "LabyMod":
               String str = "LMC";
               this.setBrandPayloadData(packet, str);
               break;
            case "CheatBreaker":
               String str2 = "CB";
               this.setBrandPayloadData(packet, str2);
               break;
            case "PvPLounge":
               String str3 = "PLC18";
               this.setBrandPayloadData(packet, str3);
               break;
            case "Lunar":
               String str4 = "Lunar-Client";
               this.setBrandPayloadData(packet, str4);
               break;
            case "Badlion":
               String str5 = "badlion";
               this.setBrandPayloadData(packet, str5);
               break;
            case "Custom":
               String value = this.customSpoofString.getValue();
               this.setBrandPayloadData(packet, value);
               break;
            case "CancelPacket":
               event.cancel();
         }
      }
   }

   private void setBrandPayloadData(C17PacketCustomPayload packet, String str) {
      packet.setData(new PacketBuffer(Unpooled.buffer()).writeString(str));
   }

   private boolean isCustomClientMode() {
      return this.clients.getSelectedValue().equals("Custom");
   }

}
