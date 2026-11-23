package net.augustus.modules.render;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.render.cape.WaveyCapeSimulationManager;
import net.augustus.utils.file.DirectoryWatchService;
import net.augustus.render.cape.CapeWindMode;
import net.augustus.render.cape.CapeMovementMode;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.DirectoryWatcher;
import net.augustus.utils.file.ClientPaths;
import net.augustus.settings.ActionSetting;
import net.augustus.render.cape.CapeStyle;
import net.augustus.render.BufferedImageTextureUtil;
import net.augustus.render.cape.WaveyCapeConfig;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.optifine.player.CapeUtils;

public class CustomCape extends Module implements TickListener {
   private final String customCapesDir;
   public final BooleansSetting type;
   public final BooleansSetting labyModCapes;
   public final ActionSetting openFolder;
   public final BooleansSetting mojangCapes;
   public final BooleanValue capePhysics;
   public final BooleansSetting style;
   public final BooleansSetting movement;
   public final BooleansSetting wind;
   public final StringValue gravity;
   public final StringValue heightMultiplier;
   public final HashMap customCapeTextures;
   public final HashMap mojangCapeResources;

   public CustomCape() {
       super("CustomCape", "KundenspezifischeKap", new Color(235, 146, 52), Categorys.RENDER);
      this.customCapesDir = ClientPaths.CLIENT_DIR + File.separator + "customcapes";
      this.type = new BooleansSetting(this, "Type", "LabyMod", new String[]{"Mojang", "LabyMod"});
      BooleansSetting booleansSetting = new BooleansSetting(this, "LabyModCapes", "Augustus", new String[0]);
      SettingVisibilityCondition condition = () -> this.isLabyModCapesVisible();
      this.labyModCapes = (BooleansSetting)booleansSetting.withVisibility(condition);
      ActionSetting actionSetting = new ActionSetting(this, "OpenFolder", () -> this.openCustomCapesFolder());
      SettingVisibilityCondition condition2 = () -> this.isOpenFolderVisible();
      this.openFolder = (ActionSetting)actionSetting.withVisibility(condition2);
      BooleansSetting booleansSetting2 = new BooleansSetting(this, "MojangCapes", "Migrator", new String[0]);
      SettingVisibilityCondition condition3 = () -> {
         return this.type.getSelectedValue().equals("Mojang");
      };
      this.mojangCapes = (BooleansSetting)booleansSetting2.withVisibility(condition3);
      this.capePhysics = new BooleanValue(this, "CapePhysics", true);
      BooleansSetting booleansSetting3 = new BooleansSetting(this, "Style", "Smooth", new String[]{"Smooth", "Blocky"});
      BooleanValue capePhysics2 = this.capePhysics;
      this.capePhysics.getClass();
      BooleanValue booleanValue = capePhysics2;
      SettingVisibilityCondition condition4 = () -> booleanValue.getValue();
      this.style = (BooleansSetting)booleansSetting3.withVisibility(condition4);
      BooleansSetting booleansSetting4 = new BooleansSetting(this, "Movement", "Simulation", new String[]{"Simulation", "Vanilla"});
      capePhysics2 = this.capePhysics;
      this.capePhysics.getClass();
      BooleanValue booleanValue2 = capePhysics2;
      SettingVisibilityCondition condition5 = () -> booleanValue2.getValue();
      this.movement = (BooleansSetting)booleansSetting4.withVisibility(condition5);
      BooleansSetting booleansSetting5 = new BooleansSetting(this, "Wind", "None", new String[]{"None", "Waves"});
      capePhysics2 = this.capePhysics;
      this.capePhysics.getClass();
      BooleanValue booleanValue3 = capePhysics2;
      SettingVisibilityCondition condition6 = () -> booleanValue3.getValue();
      this.wind = (BooleansSetting)booleansSetting5.withVisibility(condition6);
      StringValue stringValue = new StringValue(this, "Gravity", 25.0, 5.0, 32.0, 0);
      SettingVisibilityCondition condition7 = () -> {
         return this.capePhysics.getValue() && this.movement.getSelectedValue().equals("Simulation");
      };
      this.gravity = stringValue.visibleWhen(condition7);
      StringValue stringValue2 = new StringValue(this, "HeightMultiplier", 6.0, 4.0, 16.0, 0);
      SettingVisibilityCondition condition8 = () -> this.isHeightMultiplierVisible();
      this.heightMultiplier = stringValue2.visibleWhen(condition8);
      this.customCapeTextures = new HashMap();
      this.mojangCapeResources = new HashMap();
      ClientFileUtil.ensureDirectory(this.customCapesDir);
      String str = Augustus.getInstance().getName().toLowerCase() + "/customcapes";
      boolean flag = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Sky.png", str, flag);
      boolean flag2 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Augustus.png", str, flag2);
      boolean flag3 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusAmethyst.png", str, flag3);
      boolean flag4 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusCandy.png", str, flag4);
      boolean flag5 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusClassic.png", str, flag5);
      boolean flag6 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusMagic.png", str, flag6);
      boolean flag7 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusMagma.png", str, flag7);
      boolean flag8 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusMango.png", str, flag8);
      boolean flag9 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusRose.png", str, flag9);
      boolean flag10 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusTitanium.png", str, flag10);
      boolean flag11 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Ejected.png", str, flag11);
      boolean flag12 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/BlurSky.png", str, flag12);
      boolean flag13 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/BlurSky2.png", str, flag13);
      boolean flag14 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/HorizonSunset by Xolitey.png", str, flag14);
      boolean flag15 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/NightCity.png", str, flag15);
      boolean flag16 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Pepe.png", str, flag16);
      boolean flag17 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Pineapples.png", str, flag17);
      boolean flag18 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Rose.png", str, flag18);
      boolean flag19 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Esound.png", str, flag19);
      boolean flag20 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Dog.png", str, flag20);
      boolean flag21 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AugustusLightBlue.png", str, flag21);
      boolean flag22 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/GigachadSound.png", str, flag22);
      boolean flag23 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/NoobiiHome.png", str, flag23);
      boolean flag24 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/GommeHD.png", str, flag24);
      boolean flag25 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Seal.png", str, flag25);
      boolean flag26 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Questionmark.png", str, flag26);
      boolean flag27 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Creeper.png", str, flag27);
      boolean flag28 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Karhu.png", str, flag28);
      boolean flag29 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Moon.png", str, flag29);
      boolean flag30 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Astolfo.png", str, flag30);
      boolean flag31 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Astolfotrap.png", str, flag31);
      boolean flag32 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AstolfoBeta.png", str, flag32);
      boolean flag33 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/AstolfoBeta2.png", str, flag33);
      boolean flag34 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/E.png", str, flag34);
      boolean flag35 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Grim.png", str, flag35);
      boolean flag36 = false;
      ClientFileUtil.extractResource("assets/augustus/cape/Otter.png", str, flag36);
      this.loadCustomCapes();
      this.loadMojangCapes();
      DirectoryWatcher directoryWatcher = DirectoryWatchService.watch(this.customCapesDir);
      Consumer consumer = obj -> this.onCapeFileChanged((Path)obj);
      directoryWatcher = directoryWatcher.onCreate(consumer);
      Consumer consumer2 = obj2 -> {
         File file = ((Path)obj2).toFile();
         this.loadCapeFile(file);
         this.updateCustomCapeOptions();
      };
      directoryWatcher = directoryWatcher.onModify(consumer2);
      Consumer consumer3 = obj3 -> {
         String name2 = ((Path)obj3).toFile().getName();
         if (name2.endsWith(".png")) {
            String str2 = name2.substring(0, name2.length() - 4);
            this.customCapeTextures.remove(str2);
            this.updateCustomCapeOptions();
         }
      };
      directoryWatcher.onDelete(consumer3).register();
   }

   @Override
   public void onEnable() {
      if (mc.thePlayer != null && mc.thePlayer.getPlayerInfo() != null) {
         mc.thePlayer.getPlayerInfo().playerTexturesLoaded = false;
         mc.thePlayer.getPlayerInfo().loadPlayerTextures();
      }
   }

   @Override
   public void onDisable() {
      if (mc.thePlayer != null && mc.thePlayer.getPlayerInfo() != null) {
         mc.thePlayer.getPlayerInfo().playerTexturesLoaded = false;
         mc.thePlayer.getPlayerInfo().loadPlayerTextures();
      }

      WaveyCapeSimulationManager.getWaveyCapeSimulationManager().clearAll();
   }

   @Override
   public void updateEventSubscriptions() {
      this.loadCustomCapes();
   }

   @Override
   public void onTick3() {
      this.syncWaveyCapeConfig();
      if (mc.thePlayer != null && this.movement.getSelectedValue().equals("Simulation")) {
         WaveyCapeSimulationManager manager = WaveyCapeSimulationManager.getWaveyCapeSimulationManager();
         EntityPlayerSP thePlayer = mc.thePlayer;
         manager.tickPlayer(thePlayer);
      }
   }

   private void syncWaveyCapeConfig() {
      WaveyCapeConfig config = WaveyCapeSimulationManager.getWaveyCapeSimulationManager().getConfig();
      config.capeStyle = this.style.getSelectedValue().equals("Smooth") ? CapeStyle.SMOOTH : CapeStyle.BLOCKY;
      config.capeMovement = this.movement.getSelectedValue().equals("Simulation") ? CapeMovementMode.BASIC_SIMULATION : CapeMovementMode.VANILLA;
      config.windMode = this.wind.getSelectedValue().equals("Waves") ? CapeWindMode.WAVES : CapeWindMode.NONE;
      config.gravity = (int)this.gravity.getValue();
      config.heightMultiplier = (int)this.heightMultiplier.getValue();
   }

   public WaveyCapeConfig getWaveyCapeConfig() {
      this.syncWaveyCapeConfig();
      return WaveyCapeSimulationManager.getWaveyCapeSimulationManager().getConfig();
   }

   public void loadMojangCapes() {
      java.util.ArrayList list = new java.util.ArrayList();
      String str = "cape/mojang/";
      List list2 = Arrays.asList(
         "Birthday.png",
         "Cobalt.png",
         "dB.png",
         "Migrator.png",
         "MillionthCustomer.png",
         "MineCon2011.png",
         "MineCon2012.png",
         "MineCon2013.png",
         "MineCon2015.png",
         "MineCon2016.png",
         "Mojang.png",
         "MojangClassic.png",
         "MojangStudios.png",
         "MojiraModerator.png",
         "Prismarine.png",
         "RealmsMapmaker.png",
         "Scrolls.png",
         "Snowman.png",
         "Spade.png",
         "Translator.png",
         "Translator(Chinese).png",
         "Translator(Japanese).png",
         "Turtle.png",
         "Valentine.png",
         "Vanilla.png",
         "Founders.png",
         "MCExperience.png",
         "15Year.png",
         "Bacon.png",
         "CherryBlossom.png",
         "Eyeblossom.png",
         "Frog.png",
         "Home.png",
         "MCChampionship.png",
         "Menace.png",
         "NewYears.png",
         "Oxeye.png",
         "Snail.png",
         "Sniffer.png",
         "TikTok.png",
         "Twitch.png",
         "Xmas.png",
         "Yearn.png"
      );
      this.mojangCapeResources.clear();

      for (String str2 : (Iterable<String>)(list2)) {
         String[] strings = str2.split("\\.");
         String str3 = str2.replace("." + strings[strings.length - 1], "");
         this.mojangCapeResources.put(str3, new ResourceLocation("augustus", str + str2));
         list.add(str3);
      }

      boolean flag = false;
      String[] strings2 = new String[list.size()];

      for (int i = 0; i < strings2.length; i++) {
         String str4 = (String)list.get(i);
         strings2[i] = str4;
         if (this.mojangCapes.getSelectedValue().equals(str4)) {
            flag = true;
         }
      }

      if (!flag) {
         String str5 = strings2[0];
         this.mojangCapes.setSelectedValue(str5);
      }

      this.mojangCapes.setOptions(strings2);
   }

   public void loadCustomCapes() {
      File file = new File(this.customCapesDir);
      File[] files = file.listFiles();
      if (files != null) {
         for (File file2 : files) {
            this.loadCapeFile(file2);
         }
      }

      this.updateCustomCapeOptions();
   }

   private void loadCapeFile(File file) {

      try {
         String name2 = file.getName();
         if (name2.endsWith(".png")) {
            String str = name2.substring(0, name2.length() - 4);
            if (!str.isEmpty()) {
               this.customCapeTextures.put(str, BufferedImageTextureUtil.uploadTexture(CapeUtils.parseCape(ImageIO.read(file.getAbsoluteFile()))));
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void updateCustomCapeOptions() {
      boolean flag = this.customCapeTextures.keySet().stream().anyMatch(obj -> this.isSelectedCustomCape((String)obj));
      if (!flag && !this.customCapeTextures.isEmpty()) {
         BooleansSetting labyModCapes2 = this.labyModCapes;
         String str = (String)this.customCapeTextures.keySet().iterator().next();
         labyModCapes2.setSelectedValue(str);
      }

      BooleansSetting labyModCapes3 = this.labyModCapes;
      String[] strings = ((Map<String, ?>)this.customCapeTextures).keySet().stream().sorted(Comparator.comparing(String::toLowerCase)).toArray(String[]::new);
      labyModCapes3.setOptions(strings);
   }

   private boolean isSelectedCustomCape(String str) {
      return this.labyModCapes.getSelectedValue().equals(str);
   }

   private void onCapeFileChanged(Path path) {
      File file = path.toFile();
      this.loadCapeFile(file);
      this.updateCustomCapeOptions();
   }

   private boolean isHeightMultiplierVisible() {
      return this.capePhysics.getValue() && this.movement.getSelectedValue().equals("Simulation");
   }

   private boolean isOpenFolderVisible() {
      return this.type.getSelectedValue().equals("LabyMod");
   }

   private void openCustomCapesFolder() {
      ClientFileUtil.openFolder(this.customCapesDir);
   }

   private boolean isLabyModCapesVisible() {
      return this.type.getSelectedValue().equals("LabyMod");
   }

}
