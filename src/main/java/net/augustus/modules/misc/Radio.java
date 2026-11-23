package net.augustus.modules.misc;

import javazoom.jl.player.Player;
import javazoom.jl.decoder.JavaLayerException;
import java.awt.Color;
import java.io.IOException;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickGuiListener;
import net.augustus.utils.RadioStreamThread;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;

public class Radio extends Module implements ClickGuiListener, TickListener {
   public final StringValue volume;
   public final BooleansSetting radioChannels;
   public final BooleanValue onlyIngame;
   private final RadioStreamThread streamThread;
   private String currentChannel;
   private boolean isPlayingIngame;

   public Radio() {
       super("Radio", "Radio", Color.orange, Categorys.MISC);
      this.volume = (StringValue)(new StringValue(this, "Volume", 50.0, 0.0, 100.0, 0)).markVisualOnly();
      this.radioChannels = (BooleansSetting)(new BooleansSetting(
            this,
            "RadioChannels",
            "I love Radio",
            new String[]{
               "I love Radio",
               "I love 2 Dance",
               "I love ChillHop",
               "I l Deutschrap",
               "I l Greatest Hits",
               "I love Hardstyle",
               "I love Hip Hop",
               "I love Mashup",
               "I love The Club",
               "SWR3",
               "DasDing",
               "BigFM",
               "Bayern3",
               "Sunshine Live",
               "Ö1",
               "Hitradio Ö3",
               "Kronehit",
               "ORF Tirol"
            }
         ))
         .markVisualOnly();
      this.onlyIngame = (BooleanValue)(new BooleanValue(this, "Only Ingame", false)).markVisualOnly();
      this.streamThread = new RadioStreamThread(null);
      this.currentChannel = "";
      this.isPlayingIngame = true;
      this.streamThread.start();
      boolean flag = false;
      this.setLoadFromConfig(flag);
   }

   public void onEnable() {
      this.currentChannel = "";
      this.streamThread.stopStream();
      if (!this.onlyIngame.getValue() || mc.thePlayer != null) {
         this.switchRadioChannel();
      }

      this.isPlayingIngame = true;
   }

   public void onDisable() {
      this.streamThread.stopStream();
      this.currentChannel = "";
   }

   public void updateEventSubscriptions() {
      byte b = 6;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   public void onTick3() {
      if (this.onlyIngame.getValue()) {
         if (mc.thePlayer == null && this.isPlayingIngame) {
            this.streamThread.stopStream();
            this.isPlayingIngame = false;
         } else if (mc.thePlayer != null && !this.isPlayingIngame) {
            this.currentChannel = "";
            this.streamThread.stopStream();
            this.switchRadioChannel();
            this.isPlayingIngame = true;
         }
      }
   }

   public void onClickGui2() {
      String selectedValue = this.radioChannels.getSelectedValue();
      this.setSuffix(selectedValue);
      this.switchRadioChannel();
      if (RadioStreamThread.getPlayer(this.streamThread) != null) {
         Player player = RadioStreamThread.getPlayer(this.streamThread);
         boolean flag = true;
         player.setGain((float)(this.volume.getValue(flag) * 0.8600000143051147 - 80.0));
      }
   }

   private void switchRadioChannel() {

      try {
         if (!this.currentChannel.equals(this.radioChannels.getSelectedValue())) {
            String selectedValue = this.radioChannels.getSelectedValue();
            switch (selectedValue) {
               case "I love Radio":
                  String str = "https://streams.ilovemusic.de/iloveradio1.mp3";
                  this.playStreamUrl(str);
                  break;
               case "I love 2 Dance":
                  String str2 = "https://streams.ilovemusic.de/iloveradio2.mp3";
                  this.playStreamUrl(str2);
                  break;
               case "I love ChillHop":
                  String str3 = "https://streams.ilovemusic.de/iloveradio17.mp3";
                  this.playStreamUrl(str3);
                  break;
               case "I l Deutschrap":
                  String str4 = "https://streams.ilovemusic.de/iloveradio6.mp3";
                  this.playStreamUrl(str4);
                  break;
               case "I l Greatest Hits":
                  String str5 = "https://streams.ilovemusic.de/iloveradio16.mp3";
                  this.playStreamUrl(str5);
                  break;
               case "I love Hardstyle":
                  String str6 = "https://streams.ilovemusic.de/iloveradio21.mp3";
                  this.playStreamUrl(str6);
                  break;
               case "I love Hip Hop":
                  String str7 = "https://streams.ilovemusic.de/iloveradio3.mp3";
                  this.playStreamUrl(str7);
                  break;
               case "I love Mashup":
                  String str8 = "https://streams.ilovemusic.de/iloveradio5.mp3";
                  this.playStreamUrl(str8);
                  break;
               case "I love The Club":
                  String str9 = "https://streams.ilovemusic.de/iloveradio20.mp3";
                  this.playStreamUrl(str9);
                  break;
               case "SWR3":
                  String str10 = "https://liveradio.swr.de/sw282p3/swr3/play.mp3";
                  this.playStreamUrl(str10);
                  break;
               case "DasDing":
                  String str11 = "https://liveradio.swr.de/d9zadj3/dasding/";
                  this.playStreamUrl(str11);
                  break;
               case "BigFM":
                  String str12 = "https://stream.bigfm.de/deutschland/mp3-128/homepage";
                  this.playStreamUrl(str12);
                  break;
               case "Bayern3":
                  String str13 = "https://dispatcher.rndfnk.com/br/br3/live/mp3/mid";
                  this.playStreamUrl(str13);
                  break;
               case "Sunshine Live":
                  String str14 = "https://stream.laut.fm/sunshinelive";
                  this.playStreamUrl(str14);
                  break;
               case "Ö1":
                  String str15 = "https://orf-live.ors-shoutcast.at/oe1-q1a";
                  this.playStreamUrl(str15);
                  break;
               case "Hitradio Ö3":
                  String str16 = "https://orf-live.ors-shoutcast.at/oe3-q1a";
                  this.playStreamUrl(str16);
                  break;
               case "Kronehit":
                  String str17 = "https://secureonair.krone.at/kronehit-hp.mp3";
                  this.playStreamUrl(str17);
                  break;
               case "ORF Tirol":
                  String str18 = "https://orf-live.ors-shoutcast.at/tir-q1a";
                  this.playStreamUrl(str18);
            }
         }
      } catch (JavaLayerException | IOException e) {
         e.printStackTrace();
      }

      this.currentChannel = this.radioChannels.getSelectedValue();
   }

   private void playStreamUrl(String str) throws IOException, JavaLayerException {
      this.streamThread.playStream(str);
   }

}
