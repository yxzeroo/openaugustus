package net.augustus.modules.render;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.network.IpLookupProvider;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class ServerPinger extends Module {
   public final DoubleValue serverWidget;
   public final BooleanValue serverWidgetEnabled;
   public final StringValue pingDelay;
   public final ColorSetting widgetColor;
   public final ColorSetting widgetBackgroundColor;
   public final DoubleValue ipInfo;
   public final BooleanValue ipInfoEnabled;
   public final ColorSetting ipInfoTextColor;
   public final ColorSetting ipInfoBoxColor;
   public final BooleansSetting backend;
   public final DoubleValue playerGraph;
   public final BooleanValue playerGraphEnabled;
   public final StringValue maxEntries;
   public final BooleanValue keepPlayerData;
   public final Map playerCountHistory;

   public ServerPinger() {
       super("ServerPinger", "BereitstellerFürTaktischWichtigeInformationen", new Color(178, 18, 253), Categorys.RENDER);
      this.serverWidget = (DoubleValue)(new DoubleValue(this, "ServerWidget", true)).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this, "ServerWidgetEnabled", true, this.serverWidget);
      String str = "Enabled";
      this.serverWidgetEnabled = (BooleanValue)((BooleanValue)booleanValue.withDisplayName(str)).markVisualOnly();
      this.pingDelay = (StringValue)(new StringValue(this, "PingDelay", 5000.0, 1000.0, 50000.0, 0, this.serverWidget)).markVisualOnly();
      Color color2 = new Color(41, 146, 222);
      DoubleValue serverWidget2 = this.serverWidget;
      Color color3 = color2;
      String str2 = "Color";
      this.widgetColor = (ColorSetting)(new ColorSetting(this, str2, color3, serverWidget2)).markVisualOnly();
      color2 = new Color(0, 0, 0, 120);
      serverWidget2 = this.serverWidget;
      color3 = color2;
      str2 = "BackgroundColor";
      this.widgetBackgroundColor = (ColorSetting)(new ColorSetting(this, str2, color3, serverWidget2)).markVisualOnly();
      this.ipInfo = (DoubleValue)(new DoubleValue(this, "IpInfo", true)).markVisualOnly();
      BooleanValue booleanValue2 = new BooleanValue(this, "IpInfoEnabled", true, this.ipInfo);
      String str3 = "Enabled";
      this.ipInfoEnabled = (BooleanValue)((BooleanValue)booleanValue2.withDisplayName(str3)).markVisualOnly();
      color2 = new Color(41, 146, 222);
      serverWidget2 = this.ipInfo;
      color3 = color2;
      str2 = "TextColor";
      this.ipInfoTextColor = (ColorSetting)(new ColorSetting(this, str2, color3, serverWidget2)).markVisualOnly();
      color2 = new Color(0, 0, 0, 120);
      serverWidget2 = this.ipInfo;
      color3 = color2;
      str2 = "BoxColor";
      this.ipInfoBoxColor = (ColorSetting)(new ColorSetting(this, str2, color3, serverWidget2)).markVisualOnly();
      this.backend = (BooleansSetting)(new BooleansSetting(
            this,
            "Backend",
            IpLookupProvider.INCOLUMITAS.getDisplayName(),
            Arrays.stream(IpLookupProvider.values()).map(IpLookupProvider::getDisplayName).toArray(String[]::new),
            this.ipInfo
         ))
         .markVisualOnly();
      this.playerGraph = (DoubleValue)(new DoubleValue(this, "PlayerGraph", true)).markVisualOnly();
      BooleanValue booleanValue3 = new BooleanValue(this, "PlayerGraphEnabled", true, this.playerGraph);
      String str4 = "Enabled";
      this.playerGraphEnabled = (BooleanValue)((BooleanValue)booleanValue3.withDisplayName(str4)).markVisualOnly();
      this.maxEntries = (StringValue)(new StringValue(this, "MaxEntries", 200.0, 10.0, 500.0, 0, this.playerGraph)).markVisualOnly();
      this.keepPlayerData = (BooleanValue)(new BooleanValue(this, "KeepPlayerData", true, this.playerGraph)).markVisualOnly();
      this.playerCountHistory = new HashMap();
      boolean flag = true;
      this.widgetColor.setClientColorEnabled(flag);
      short s = 255;
      this.widgetColor.setClientColorAlpha(s);
      boolean flag2 = true;
      this.ipInfoTextColor.setClientColorEnabled(flag2);
      short s2 = 255;
      this.ipInfoTextColor.setClientColorAlpha(s2);
      boolean flag3 = false;
      boolean flag4 = true;
      this.setToggled(flag4, flag3);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.playerCountHistory.clear();
   }

   @Override
   public void updateEventSubscriptions() {
   }

}
