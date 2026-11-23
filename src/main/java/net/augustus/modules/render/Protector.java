package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.minecraft.client.network.NetworkPlayerInfo;

public class Protector extends Module {
   public BooleanValue time;
   public final BooleansSetting nameGeneration;
   private int randomXOffset;
   private int randomZOffset;
   private int fakeMinute;
   private int fakeHour;

   public Protector() {
       super("Protector", "Versicherung", Color.yellow, Categorys.RENDER);
      this.time = new BooleanValue(this, "Time", true);
      this.nameGeneration = new BooleansSetting(this, "NameGeneration", "List", new String[]{"List", "Esound", "Generated"});
   }

   public void onEnable() {
      short s = 1000;
      short s2 = -1000;
      this.randomXOffset = DefaultRandomGenerator.instance.nextInt2(s2, s);
      short s3 = 1000;
      short s4 = -1000;
      this.randomZOffset = DefaultRandomGenerator.instance.nextInt2(s4, s3);
      byte b = 60;
      byte b2 = 10;
      this.fakeMinute = DefaultRandomGenerator.instance.nextInt2(b2, b);
      byte b3 = 11;
      byte b4 = 0;
      this.fakeHour = DefaultRandomGenerator.instance.nextInt2(b4, b3);
      if (mc.thePlayer != null) {
         for (NetworkPlayerInfo networkPlayerInfo : (Iterable<NetworkPlayerInfo>)(mc.getNetHandler().getPlayerInfoMap())) {
            networkPlayerInfo.playerTexturesLoaded = false;
            networkPlayerInfo.loadPlayerTextures();
         }
      }
   }

   public void onDisable() {
      if (mc.thePlayer != null) {
         for (NetworkPlayerInfo networkPlayerInfo : (Iterable<NetworkPlayerInfo>)(mc.getNetHandler().getPlayerInfoMap())) {
            networkPlayerInfo.playerTexturesLoaded = false;
            networkPlayerInfo.loadPlayerTextures();
         }
      }
   }

   public void updateEventSubscriptions() {
   }

   public int getRandomXOffset() {
      return this.randomXOffset;
   }

   public int getRandomZOffset() {
      return this.randomZOffset;
   }

   public int getFakeMinute() {
      return this.fakeMinute;
   }

   public int getFakeHour() {
      return this.fakeHour;
   }

}
