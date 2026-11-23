package net.augustus;

import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.commands.CommandManager;
import net.augustus.config.ConfigManager;
import net.augustus.cosmetics.MoreCosmeticsBootstrap;
import net.augustus.discord.DiscordRpcManager;
import net.augustus.font.FontManager;
import net.augustus.modules.ModuleRegistry;
import net.augustus.modules.misc.helper.EventWarmupDummyModule;
import net.augustus.quests.QuestManager;
import net.augustus.settings.Setting;
import net.augustus.settings.SettingDescriptionManager;
import net.augustus.settings.SettingManager;
import net.augustus.settings.SettingNameManager;
import net.augustus.settings.options.NotificationCategoryOption;
import net.augustus.ui.menu.ChangeLogLoader;
import net.augustus.ui.menu.ClientTabManager;
import net.augustus.ui.notifications.NotificationLevel;
import net.augustus.ui.notifications.NotificationManager;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.MinotarAvatarCache;
import net.augustus.utils.MouseWheelTracker;
import net.augustus.utils.PlayerRotationHandler;
import net.augustus.utils.ProtectorManager;
import net.augustus.utils.SystemTrayNotifier;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.utils.network.ProxyEntry;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.rotation.YawPitchHelper;
import net.augustus.utils.shader.BackgroundShaderManager;
import net.augustus.vendor.ClientUserProfile;
import net.augustus.viaversion.ViaAugustus;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.augustus.modules.ModuleManager;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.Minecraft;

public class Augustus {
   public final boolean devMode;
   public final boolean betaMode;
   private static final Augustus INSTANCE = new Augustus();
   private final String name;
   private final String version;
   private final String authors;
   private int clientColor;
   private final ModuleRegistry moduleRegistry;
   private ModuleManager moduleManager;
   private SettingManager settingManager;
   private CommandManager commandManager;
   private final ClientTabManager clientTabManager;
   private final ChangeLogLoader changeLogLoader;
   private ClickGuiScreen clickGui;
   private ClientPaths clientPaths;
   private BackgroundShaderManager backgroundShaderManager;
   private RotationController rotationController;
   private SettingDescriptionManager settingDescriptionManager;
   private SettingNameManager settingNameManager;
   private ConfigManager configManager;
   private YawPitchHelper yawPitchHelper;
   private ProxyEntry proxy;
   private MouseWheelTracker mouseWheelTracker;
   private final DiscordRpcManager discordRpcManager;
   public boolean firstStart;
   private final TimeHelper timeHelper;
   private final ProtectorManager protectorManager;
   private ClientUserProfile userProfile;
   private QuestManager questManager;
   private EventWarmupDummyModule eventWarmupDummyModule;
   private boolean configEditingEnabled;
   private boolean shuttingDown;
   private String sessionToken;
   private static boolean initialized = false;
   public static byte[] handshakeKeyBytes;

   public static Augustus getInstance() {
      return INSTANCE;
   }

   public Augustus() {
      super();
      this.devMode = false;
      this.betaMode = false;
      this.name = "Augustus";
      this.version = "b4.23.3";
      this.authors = "E-sound, DasNeueUpdate, EnZaXD";
      this.clientColor = new Color(41, 146, 222).getRGB();
      this.proxy = null;
      this.firstStart = true;
      this.questManager = null;
      this.configEditingEnabled = true;
      this.shuttingDown = false;
      this.moduleRegistry = new ModuleRegistry();
      this.clientTabManager = new ClientTabManager();
      this.changeLogLoader = new ChangeLogLoader();
      this.discordRpcManager = new DiscordRpcManager();
      this.timeHelper = new TimeHelper();
      this.protectorManager = new ProtectorManager();
      this.userProfile = new ClientUserProfile();
   }

   public void preStart() {
   }

   public void start() {
      MoreCosmeticsBootstrap.initialize(1);

      try {
         this.initializeClient();
      } catch (Throwable e) {
         e.printStackTrace();
      }
   }

   private void initializeClient() throws IOException, InterruptedException {
      if (initialized) {
         return;
      }

      initialized = true;
      Setting.notifyListenersOnConfigLoad = true;
      this.yawPitchHelper = new PlayerRotationHandler();
      this.mouseWheelTracker = new MouseWheelTracker();
      this.shuttingDown = false;
      SystemTrayNotifier.init();
      FontManager.initFonts();
      this.protectorManager.init();
      this.rotationController = new RotationController(this.getVersion());
      this.settingManager = new SettingManager();
      this.settingManager.updateSessionToken(this.getName());
      this.moduleManager = new ModuleManager();
      this.configManager = new ConfigManager();
      this.moduleManager.initModules();
      this.settingDescriptionManager = new SettingDescriptionManager();
      this.settingNameManager = new SettingNameManager();
      this.moduleManager.autoDisable.initModuleSettings(this.moduleManager);
      this.commandManager = new CommandManager();
      this.changeLogLoader.loadChangelog();
      this.clientPaths = new ClientPaths();
      ClientPaths.init();
      this.backgroundShaderManager = new BackgroundShaderManager();
      ClientManagers.init();
      this.clickGui = new ClickGuiScreen();
      this.settingDescriptionManager.initialize();
      this.settingNameManager.initialize();
      this.clientTabManager.initTabs();
      this.configManager.loadAll();
      MinotarAvatarCache.preloadAccountAvatars();

      try {
         ClientFileUtil.extractResource(
            "assets/augustus/sounds/alarm.ogg",
            this.getName().toLowerCase() + "/sounds",
            false
         );
      } catch (Exception e) {
         System.err.println("Failed to save sound");
      }

      CompletableFuture.runAsync(() -> {
         try {
            ViaAugustus.initialize();
         } catch (Throwable e2) {
            e2.printStackTrace();
         }
      });
   }

   public static byte[] longToBytes(long l, long l2) {
      byte[] bytes = new byte[8];

      for (int i = 7; i >= 0; i--) {
         bytes[i] = (byte)((int)(l & 255L));
         l >>= 8;
      }

      return bytes;
   }

   private void sleepTick() {
      try {
         Thread.sleep(1L);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
   }

   public void stop() {
      if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().getSession() != null) {
         Minecraft.getMinecraft().getSession().updatePlayTime(false);
      }

      if (getInstance().getQuestManager() != null) {
         getInstance().getQuestManager().deactivateAllQuests();
      }

      this.configManager.saveAll();

      try {
         Thread.sleep(200L);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
   }

   public String getName() {
      return "Augustus";
   }

   public String getVersion() {
      return "b4.23.3";
   }

   public String getAuthors() {
      return "E-sound, DasNeueUpdate, EnZaXD";
   }

   public int getClientColor() {
      return this.clientColor;
   }

   public void setClientColor(int n) {
      this.clientColor = n;
   }

   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }

   public SettingManager getSettingManager() {
      return this.settingManager;
   }

   public CommandManager getCommandManager() {
      return this.commandManager;
   }

   public ClientPaths getClientPaths() {
      return this.clientPaths;
   }

   public BackgroundShaderManager getBackgroundShaderManager() {
      return this.backgroundShaderManager;
   }

   public YawPitchHelper getYawPitchHelper() {
      return this.yawPitchHelper;
   }

   public ModuleRegistry getModuleRegistry() {
      return this.moduleRegistry;
   }

   public ClickGuiScreen getClickGui() {
      return this.clickGui;
   }

   public MouseWheelTracker getMouseWheelTracker() {
      return this.mouseWheelTracker;
   }

   public DiscordRpcManager getDiscordRpcManager() {
      return this.discordRpcManager;
   }

   public RotationController getRotationController() {
      return this.rotationController;
   }

   public void setBackgroundShaderManager(BackgroundShaderManager backgroundShaderManager2) {
      this.backgroundShaderManager = backgroundShaderManager2;
   }

   public void setMouseWheelTracker(MouseWheelTracker mouseWheelTracker2) {
      this.mouseWheelTracker = mouseWheelTracker2;
   }

   public TimeHelper getTimeHelper() {
      return this.timeHelper;
   }

   public ClientTabManager getClientTabManager() {
      return this.clientTabManager;
   }

   public ChangeLogLoader getChangeLogLoader() {
      return this.changeLogLoader;
   }

   public static byte[] readAllBytes(InputStream in, int n, long l) throws IOException {
      int n2 = 2147483639;
      int n3 = 8192;
      int n4 = n;
      byte[] bytes = new byte[n];
      int n5 = 0;

      while (true) {
         int n6;
         while ((n6 = in.read(bytes, n5, n4 - n5)) > 0) {
            n5 += n6;
         }

         if (n6 < 0 || (n6 = in.read()) < 0) {
            return n4 == n5 ? bytes : Arrays.copyOf(bytes, n5);
         }

         if (n4 <= n2 - n4) {
            n4 = Math.max(n4 << 1, n3);
         } else {
            if (n4 == n2) {
               throw new OutOfMemoryError("Required array size too large");
            }

            n4 = n2;
         }

         bytes = Arrays.copyOf(bytes, n4);
         bytes[n5++] = (byte)n6;
      }
   }

   public ProtectorManager getProtectorManager() {
      return this.protectorManager;
   }

   public String getUsername() {
      return this.getUserProfile() == null ? "" : this.getUserProfile().getUsername();
   }

   public int getUserLevel() {
      return this.getUserProfile().getLevel();
   }

   public String getUserRank() {
      return this.getUserProfile().getRankName();
   }

   public String getUserRankColor() {
      return this.getUserProfile().getRankColor();
   }

   public int getUserExperience() {
      return this.getUserProfile().getExperience();
   }

   public ProxyEntry getProxy() {
      return this.proxy;
   }

   public synchronized void setProxy(ProxyEntry proxyEntry) {
      this.proxy = proxyEntry;
   }

   public void setYawPitchHelper(PlayerRotationHandler playerRotationHandler) {
      this.yawPitchHelper = playerRotationHandler;
   }

   public SettingDescriptionManager getSettingDescriptionManager() {
      return this.settingDescriptionManager;
   }

   public SettingNameManager getSettingNameManager() {
      return this.settingNameManager;
   }

   public String getSessionToken() {
      return this.sessionToken;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public ClientUserProfile getUserProfile() {
      return this.userProfile;
   }

   public void setUserProfile(ClientUserProfile clientUserProfile) {
      if (this.userProfile != null) {
         if (clientUserProfile.getExperience() != this.userProfile.getExperience() && this.userProfile.getExperience() != -1 && clientUserProfile.getExperience() != -1) {
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE8);
            int n = Math.abs(this.userProfile.getExperience() - clientUserProfile.getExperience());
            ChatUtil.sendChat3("§cCongratulations. You earned §6" + n + "§c ep!");
            if (getInstance().getModuleManager().notifications.isToggled() && NotificationCategoryOption.LEVEL_SYSTEM.isSelected()) {
               NotificationManager.pushNotification("Levelsystem", "You earned " + n + " ep!", NotificationLevel.INFO);
            }
         }

         if (clientUserProfile.getLevel() != this.userProfile.getLevel() && this.userProfile.getLevel() != -1 && clientUserProfile.getLevel() != -1) {
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE9);
            ChatUtil.sendChat3("§cCongratulations. You are now level §6" + clientUserProfile.getLevel() + "§c!");
            if (getInstance().getModuleManager().notifications.isToggled() && NotificationCategoryOption.LEVEL_SYSTEM.isSelected()) {
               NotificationManager.pushNotification("Levelsystem", "You are now level " + clientUserProfile.getLevel() + "!", NotificationLevel.INFO);
            }
         }
      }

      this.userProfile = clientUserProfile;
   }

   public QuestManager getQuestManager() {
      return this.questManager;
   }

   public void setQuestManager(QuestManager questManager2) {
      this.questManager = questManager2;
   }

   public EventWarmupDummyModule getEventWarmupDummyModule() {
      return this.eventWarmupDummyModule;
   }

   public void setEventWarmupDummyModule(EventWarmupDummyModule event) {
      this.eventWarmupDummyModule = event;
   }

   public boolean isConfigEditingEnabled() {
      return this.configEditingEnabled;
   }

   public void setConfigEditingEnabled(boolean flag) {
      this.configEditingEnabled = flag;
   }
}
