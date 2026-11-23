package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.EventWorldColor;
import net.augustus.events.listeners.WorldColorListener;
import net.augustus.events.listeners.WorldTimeListener;
import net.augustus.settings.ColorSetting;
import net.augustus.events.EventWorldTime;
import net.augustus.Augustus;
import net.augustus.settings.SettingChangeListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class Ambiance extends Module implements PacketListener, WorldColorListener, WorldTimeListener {
   public final StringValue time;
   public final StringValue timeSpeed;
   public final StringValue rain;
   public final BooleanValue snow;
   public final StringValue thunder;
   public final BooleanValue customWorldColor;
   public final ColorSetting worldColor;
   public final BooleanValue customFogColor;
   public final ColorSetting fogColor;
   public final BooleanValue customFogDistance;
   public final StringValue fogDistance;
   public long elapsedTimeOffset;
   private float savedRainStrength;
   private float savedThunderStrength;

   public Ambiance() {
       super("Ambiance", "Ambiente", new Color(224, 93, 40), Categorys.RENDER);
      this.time = new StringValue(this, "Time", 1900.0, 0.0, 24000.0, 0);
      this.timeSpeed = new StringValue(this, "TimeSpeed", 0.0, 0.0, 100.0, 1);
      this.rain = new StringValue(this, "Rain", 0.0, 0.0, 1.0, 2);
      this.snow = new BooleanValue(this, "Snow", false);
      this.thunder = new StringValue(this, "Thunder", 0.0, 0.0, 1.0, 2);
      BooleanValue booleanValue = new BooleanValue(this, "CustomWorldColor", false);
      SettingChangeListener listener = () -> this.warnIfFullBrightGammaActive();
      SettingChangeListener listener2 = listener;
      this.customWorldColor = (BooleanValue)booleanValue.withChangeListener(listener2);
      ColorSetting colorSetting = new ColorSetting(this, "WorldColor", Augustus.getInstance().getClientColor());
      BooleanValue customWorldColor2 = this.customWorldColor;
      this.customWorldColor.getClass();
      BooleanValue booleanValue2 = customWorldColor2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.worldColor = (ColorSetting)colorSetting.withVisibility(condition);
      this.customFogColor = new BooleanValue(this, "CustomFogColor", false);
      ColorSetting colorSetting2 = new ColorSetting(this, "FogColor", Augustus.getInstance().getClientColor());
      customWorldColor2 = this.customFogColor;
      this.customFogColor.getClass();
      BooleanValue booleanValue3 = customWorldColor2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      this.fogColor = (ColorSetting)colorSetting2.withVisibility(condition2);
      this.customFogDistance = new BooleanValue(this, "CustomFogDistance", false);
      StringValue stringValue = new StringValue(this, "FogDistance", 192.0, 0.0, 1000.0, 0);
      customWorldColor2 = this.customFogDistance;
      this.customFogDistance.getClass();
      BooleanValue booleanValue4 = customWorldColor2;
      SettingVisibilityCondition condition3 = () -> booleanValue4.getValue();
      this.fogDistance = stringValue.visibleWhen(condition3);
      this.savedRainStrength = 0.0F;
      this.savedThunderStrength = 0.0F;
   }

   @Override
   public void onEnable() {
      this.elapsedTimeOffset = 0L;
      if (mc.theWorld != null) {
         this.savedRainStrength = mc.theWorld.getRainStrength(1.0F);
         this.savedThunderStrength = mc.theWorld.getThunderStrength(1.0F);
      }
   }

   @Override
   public void onDisable() {
      this.elapsedTimeOffset = 0L;
      if (mc.theWorld != null) {
         mc.theWorld.setRainStrength(this.savedRainStrength);
         mc.theWorld.setThunderStrength(this.savedThunderStrength);
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 26;
      this.toggleEventSubscription(b);
      byte b2 = 44;
      this.toggleEventSubscription(b2);
      byte b3 = 45;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onWorldColor(EventWorldColor event) {
      if (this.customWorldColor.getValue()) {
         byte b = 0;
         this.worldColor.getColorInt(b);
         float red = (float)this.worldColor.getRed();
         event.setRed(red);
         float green = (float)this.worldColor.getGreen();
         event.setGreen(green);
         float blue = (float)this.worldColor.getBlue();
         event.setBlue(blue);
      }
   }

   @Override
   public void onWorldColor2(EventWorldColor event) {
      if (this.customFogColor.getValue()) {
         byte b = 0;
         this.fogColor.getColorInt(b);
         float red = (float)this.fogColor.getRed();
         event.setRed(red);
         float green = (float)this.fogColor.getGreen();
         event.setGreen(green);
         float blue = (float)this.fogColor.getBlue();
         event.setBlue(blue);
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S03PacketTimeUpdate) {
         event.cancel();
      }
   }

   @Override
   public void onWorldTime(EventWorldTime event) {
      this.elapsedTimeOffset = this.timeSpeed.getValue() > 0.0 ? (long)((double)this.elapsedTimeOffset + this.timeSpeed.getValue()) : 0L;
      long l = (long)(this.time.getValue() + (double)this.elapsedTimeOffset);
      event.setWorldTime(l);
      if (this.elapsedTimeOffset > 24000L) {
         this.elapsedTimeOffset = 0L;
      }
   }

   private void warnIfFullBrightGammaActive() {
      if (Augustus.getInstance().getModuleManager().fullBright.isToggled() && Augustus.getInstance().getModuleManager().fullBright.mode.getSelectedValue().equals("Gamma") && this.customWorldColor.getValue()) {
         String str = "Disable gamma mode in FullBright module";
         this.sendChatMessage2(str);
      }
   }

}
