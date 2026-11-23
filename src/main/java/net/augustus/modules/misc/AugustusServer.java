package net.augustus.modules.misc;

import java.awt.Color;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.ModuleManager;
import net.augustus.events.EventPacket;
import net.augustus.events.EventChat;
import net.augustus.vendor.OnlineUserRegistry;
import net.augustus.events.listeners.RenderListener;
import net.augustus.events.listeners.WindowResizeListener;
import net.augustus.utils.HolidayDetector;
import net.augustus.ClientManagers;
import net.augustus.utils.network.ProxyEntry;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.ChatListener;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventManager;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.utils.ClientHoliday;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

public class AugustusServer extends Module implements TickListener, RenderListener, PacketListener, ChatListener {
   private final DoubleValue guiButtons;
   public final BooleanValue roundedButtons;
   public final StringValue roundFactor;
   private final DoubleValue sounds;
   public final BooleansSetting mode;
   public final BooleanValue moduleToggleSound;
   public final BooleanValue welcomeSound;
   private final DoubleValue connectionInfo;
   private final StringValue scale;
   private final DoubleValue commandStuff;
   public final BooleansSetting commandPrefixes;
   private final DoubleValue configSettings;
   public final BooleanValue loadKeysFromConfigs;
   private final DoubleValue chatSettings;
   private final BooleanValue chatCopySymbol;
   public final BooleanValue fastServerPining1;
   public final BooleansSetting banStatistics;
   public final BooleanValue removeFramerateLimitInScreens;
   public final BooleanValue limitFrameRateIfScreenNotFocused;
   private final DoubleValue backgroundSettings;
   public final BooleanValue customBackground;
   public final BooleanValue mouseInputsOnBackgrounds;
   public final BooleanValue useAntialiasedRoundRects;
   public final BooleanValue tryFixingRendermodulesWithOptifineShaders;
   public final BooleanValue tryFixingNametagsWithOptifineShaders;
   public final BooleanValue hideMainMenuAccountInfos;
   public final BooleanValue sideMainMenu;
   private final TimeHelper holidaySoundTimer;
   private static final TimeHelper CONNECTION_STATUS_TIMER = new TimeHelper();
   private long halloweenSoundDelay;
   private long aprilFoolsSoundDelay;
   private static volatile int connectionState = 2;
   private static int disconnectedTicks = 0;
   private boolean lastBlurState;

   public AugustusServer() {
       super("ClientSettings", "KundenEinstellungen", true, new Color(158, 168, 50), Categorys.MISC);
      this.guiButtons = (DoubleValue)(new DoubleValue(this, "GuiButtons", true)).markVisualOnly();
      this.roundedButtons = (BooleanValue)(new BooleanValue(this, "RoundedButtons", true, this.guiButtons)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "RoundFactor", 2.0, 1.0, 20.0, 0, this.guiButtons);
      SettingVisibilityCondition condition = () -> this.roundedButtons.getValue();
      this.roundFactor = (StringValue)stringValue.visibleWhen(condition).markVisualOnly();
      this.sounds = (DoubleValue)(new DoubleValue(this, "Sounds", true)).markVisualOnly();
      this.mode = (BooleansSetting)(new BooleansSetting(this, "Mode", "Sigma", new String[]{"Vanilla", "Sigma", "Piano"}, this.sounds)).markVisualOnly();
      this.moduleToggleSound = (BooleanValue)(new BooleanValue(this, "ModuleToggleSound", true, this.sounds)).markVisualOnly();
      this.welcomeSound = (BooleanValue)(new BooleanValue(this, "WelcomeSound", true, this.sounds)).markVisualOnly();
      this.connectionInfo = (DoubleValue)(new DoubleValue(this, "ConnectionInfo", true)).markVisualOnly();
      this.scale = (StringValue)(new StringValue(this, "Scale", 2.0, 1.0, 4.0, 2, this.connectionInfo)).markVisualOnly();
      this.commandStuff = (DoubleValue)(new DoubleValue(this, "CommandStuff", true)).markVisualOnly();
      this.commandPrefixes = (BooleansSetting)(new BooleansSetting(
            this, "CommandPrefixes", ".", new String[]{".", "-", "_", ":", ",", "+", "!", "#", "?", "\\", ">", "<"}, this.commandStuff
         ))
         .markVisualOnly();
      this.configSettings = (DoubleValue)(new DoubleValue(this, "ConfigSettings", true)).markVisualOnly();
      this.loadKeysFromConfigs = (BooleanValue)(new BooleanValue(this, "LoadKeysFromConfigs", false, this.configSettings)).markVisualOnly();
      this.chatSettings = (DoubleValue)(new DoubleValue(this, "ChatSettings", true)).markVisualOnly();
      this.chatCopySymbol = (BooleanValue)(new BooleanValue(this, "ChatCopySymbol", false, this.chatSettings)).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this, "FastServerPining1", false);
      SettingVisibilityCondition condition2 = AugustusServer::isFastServerPingingVisible;
      this.fastServerPining1 = (BooleanValue)((BooleanValue)booleanValue.withVisibility(condition2)).markVisualOnly();
      BooleansSetting booleansSetting = new BooleansSetting(this, "BanStatistics", "Icon", new String[0]);
      SettingVisibilityCondition condition3 = AugustusServer::isBanStatisticsVisible;
      this.banStatistics = (BooleansSetting)((BooleansSetting)booleansSetting.withVisibility(condition3)).markVisualOnly();
      this.removeFramerateLimitInScreens = (BooleanValue)(new BooleanValue(this, "RemoveFramerateLimitInScreens", true)).markVisualOnly();
      this.limitFrameRateIfScreenNotFocused = (BooleanValue)(new BooleanValue(this, "LimitFrameRateIfScreenNotFocused", true)).markVisualOnly();
      this.backgroundSettings = (DoubleValue)(new DoubleValue(this, "BackgroundSettings", true)).markVisualOnly();
      this.customBackground = (BooleanValue)(new BooleanValue(this, "CustomBackground", true, this.backgroundSettings)).markVisualOnly();
      this.mouseInputsOnBackgrounds = (BooleanValue)(new BooleanValue(this, "MouseInputsOnBackgrounds", false, this.backgroundSettings)).markVisualOnly();
      this.useAntialiasedRoundRects = (BooleanValue)(new BooleanValue(this, "UseAntialiasedRoundRects", true)).markVisualOnly();
      this.tryFixingRendermodulesWithOptifineShaders = (BooleanValue)(new BooleanValue(this, "Try fixing rendermodules with optifine shaders", true)).markVisualOnly();
      this.tryFixingNametagsWithOptifineShaders = (BooleanValue)(new BooleanValue(this, "Try fixing nametags with optifine shaders", true)).markVisualOnly();
      this.hideMainMenuAccountInfos = (BooleanValue)(new BooleanValue(this, "HideMainMenuAccountInfos", false)).markVisualOnly();
      this.sideMainMenu = (BooleanValue)(new BooleanValue(this, "SideMainMenu", true)).markVisualOnly();
      this.holidaySoundTimer = new TimeHelper();
      long l = 400000L;
      long l2 = 220000L;
      this.halloweenSoundDelay = DefaultRandomGenerator.instance.nextLong2(l2, l);
      long l3 = 1000000L;
      long l4 = 800000L;
      this.aprilFoolsSoundDelay = DefaultRandomGenerator.instance.nextLong2(l4, l3);
      this.lastBlurState = false;
      boolean flag = true;
      this.setToggled(flag);
      EventManager.getEventBus().subscribe(40, this);
      EventManager.getEventBus().subscribe(32, this);
      EventManager.getEventBus().subscribe(26, this);
      EventManager.getEventBus().subscribe(5, this);
      this.lastBlurState = BlurShadowManager.isShadersCompatible();
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
   }

   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C01PacketChatMessage) {
         C01PacketChatMessage packet2 = (C01PacketChatMessage)packet;
         String message = packet2.getMessage();
         String str = message.toLowerCase();
         if (str.contains("/sampler start")) {
            AntiSamplerGuard.activateGuard(1);
         }

         if (str.contains("/sampler")
            || str.contains("/polar")
            || str.contains("/verbose")
            || str.contains("/alerts")
            || str.contains("/register")
            || str.contains("/login")) {
            StringBuilder sb = new StringBuilder().append(Augustus.getInstance().getUsername());
            String str2 = sb.append(" tE=")
               .append(mc.thePlayer != null ? mc.thePlayer.ticksExisted : -1)
               .append(" aura=")
               .append(ClientManagers.moduleManager.killAura.isToggled())
               .append(" scaffold=")
               .append(ClientManagers.moduleManager.scaffold.isToggled())
               .append(" auramode=")
               .append(ClientManagers.moduleManager.killAura.randomizeMode.getSelectedValue())
               .append(" morekb=")
               .append(ClientManagers.moduleManager.moreKB.isToggled())
               .append(" Message=")
               .append(message)
               .toString();
            CompletableFuture.runAsync(() -> {});
         }
      }
   }

   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S02PacketChat) {
         Augustus.getInstance().getClass();
         S02PacketChat packet2 = (S02PacketChat)packet;
         IChatComponent chatComponent = packet2.getChatComponent();
         if (chatComponent == null) {
            return;
         }

         String unformattedText = chatComponent.getUnformattedText();
         String str = unformattedText.toLowerCase();
         String username = Augustus.getInstance().getUsername();
         if (username == null) {
            username = "";
         }

         boolean flag = false;
         if (mc.getCurrentServerData().serverIP != null) {
            String serverIP = mc.getCurrentServerData().serverIP;
            if (serverIP.contains("loyisa") || serverIP.contains("anticheat-test")) {
               flag = true;
            }
         }

         boolean flag2 = false;
         if (str.contains("sampling request sent") || str.contains("started recording for type: ") || str.contains("recording enabled for")) {
            flag2 = true;
         }

         if (flag2
            || str.contains("polar")
            || str.contains("sampler")
            || str.contains("intave") && !str.contains("intave12") && !flag
            || str.contains("given resistance (id 11)")
            || str.contains("mitigated") && !flag
            || str.contains("clickpatterns") && !flag
            || str.contains("javadouble")
            || str.contains("receiving fine violation")
            || str.contains("combat modifier debug")
            || str.contains("transmitting and recording")
            || str.contains("remote status (sent from cloud)")
            || str.contains(" version: c-")
            || str.contains(" detection: ")
            || str.contains(" shard master is connected")
            || str.contains("recording disabled for")
            || str.contains("micro-lagging") & !flag
            || str.contains("component: ")
            || str.contains("heuristic") && !flag && (str.contains("check") || str.contains("vl") || str.contains("type"))
            || str.contains("likelihood") && !flag
            || str.contains("correlation")
            || str.contains("issued") && str.contains("on")
            || str.contains("exhibits")
            || str.contains("output of killaura")
            || str.contains("output of scaffold")
            || str.contains("history-based cloud detection")
            || str.contains("[ac]")
            || str.contains("/§agreen§7")
            || str.contains("/§6orange§7") && !flag
            || str.contains("suspicious") && !flag
            || username.equals("Tom")
            || username.equals("Spec")
            || Augustus.getInstance().getUsername().equals("karuz")) {
            ModuleManager moduleManager;
            StringBuilder sb;
            String str2;
            exitBlock: {
               moduleManager = Augustus.getInstance().getModuleManager();
               sb = new StringBuilder().append(Augustus.getInstance().getUsername());
               short s = 21825;
               sb = sb.append(" tE=")
                  .append(mc.thePlayer != null ? mc.thePlayer.ticksExisted : -1)
                  .append(" aura=")
                  .append(moduleManager.killAura.isToggled())
                  .append(" scaffold=")
                  .append(moduleManager.scaffold.isToggled())
                  .append(" auramode=")
                  .append(moduleManager.killAura.randomizeMode.getSelectedValue())
                  .append(" morekb=")
                  .append(moduleManager.moreKB.isToggled());
               if (moduleManager.killAura.isToggled()) {
                  String selectedValue = moduleManager.killAura.randomizeMode.getSelectedValue();
                  if (selectedValue.equals("Noise2")) {
                     StringBuilder sb2 = new StringBuilder()
                        .append(" speed=")
                        .append(moduleManager.killAura.aimSpeed.getValue())
                        .append(" mindelay=")
                        .append(moduleManager.killAura.reactionTime.getMinValue())
                        .append(" maxdelay=");
                     StringBuilder sb3 = sb2.append(moduleManager.killAura.reactionTime.getMaxValue())
                        .append(" attention=")
                        .append(moduleManager.killAura.attention.getValue())
                        .append(" supercorrectionprob=")
                        .append(moduleManager.killAura.supercorrectionProbability.getValue())
                        .append(" supercorrectiondelaymin=")
                        .append(moduleManager.killAura.supercorrectionDelay.getMinValue())
                        .append(" supercorrectiondelaymax=");
                     str2 = sb3.append(moduleManager.killAura.supercorrectionDelay.getMaxValue())
                        .append(" additionalAccuracy=")
                        .append(moduleManager.killAura.additionalAccuracy.getValue())
                        .append(" additionalInaccuracy=")
                        .append(moduleManager.killAura.additionalInaccuracy.getValue())
                        .append(" decelerationFactorPre=")
                        .append(moduleManager.killAura.decelerationfactorPre.getValue())
                        .append(" decelerationFactorPost=")
                        .append(moduleManager.killAura.decelerationfactorPost.getValue())
                        .toString();
                     break exitBlock;
                  }
               }

               str2 = "";
            }

            String str3 = sb.append(str2)
               .append(
                  moduleManager.killAura.isToggled()
                     ? " fixmode=" + moduleManager.killAura.fixRotationMode.getSelectedValue() + " target=" + (moduleManager.killAura.target == null ? "null" : moduleManager.killAura.target.getName())
                     : ""
               )
               .append(" ")
               .append(unformattedText)
               .toString();
            CompletableFuture.runAsync(() -> reportChatTelemetry(str3));
         }
      }

      if (packet instanceof S02PacketChat && this.chatCopySymbol.getValue()) {
         S02PacketChat packet3 = (S02PacketChat)packet;
         if (packet3.getType() == 2) {
            return;
         }

         String str4 = StringUtils.stripControlCodes(packet3.getChatComponent().getUnformattedText());
         if (!str4.replace(" ", "").isEmpty()) {
            ChatComponentText chatComponentText = new ChatComponentText(EnumChatFormatting.DARK_GRAY + Character.toString((char)Integer.parseInt("270D", 16)));
            ChatStyle chatStyle = new ChatStyle()
               .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(EnumChatFormatting.GRAY + "Copy message")))
               .setChatClickEvent(new ClickEvent(net.minecraft.event.ClickEvent.Action.SUGGEST_COMMAND, str4));
            chatComponentText.setChatStyle(chatStyle);
            packet3.getChatComponent().appendSibling(new ChatComponentText(EnumChatFormatting.RESET + " "));
            packet3.getChatComponent().appendSibling(chatComponentText);
         }
      }
   }

   public void onRender(float f, ScaledResolution sr) {
      renderConnectionStatus(false);
   }

   public static void renderConnectionStatus(boolean flag) {
      GL11.glPushMatrix();
      ScaledResolution sr = new ScaledResolution(mc);
      if (!flag) {
         GL11.glScaled((double)(1.0F / (float)sr.getScaleFactor()), (double)(1.0F / (float)sr.getScaleFactor()), 1.0);
      }

      String str = connectionState == 0
         ? "Lost connection to Augustus server (client closes in " + Math.max(0, 100 - disconnectedTicks / 20) + " seconds)"
         : (
            connectionState == 1
               ? "Reconnecting to Augustus server (client closes in " + Math.max(0, 100 - disconnectedTicks / 20) + " seconds)"
               : "Connected successfully to Augustus server"
         );
      int n = connectionState == 0 ? Color.red.getRGB() : (connectionState == 1 ? Color.orange.getRGB() : Color.green.getRGB());
      ScaledResolution sr2 = !flag
         ? new ScaledResolution(
            (double)mc.displayWidth / ClientManagers.moduleManager.augustusServer.scale.getValue(),
            (double)mc.displayHeight / ClientManagers.moduleManager.augustusServer.scale.getValue(),
            (int)ClientManagers.moduleManager.augustusServer.scale.getValue()
         )
         : new ScaledResolution(mc);
      if (!flag) {
         GL11.glScaled(ClientManagers.moduleManager.augustusServer.scale.getValue(), ClientManagers.moduleManager.augustusServer.scale.getValue(), 1.0);
      }

      if (connectionState != 2) {
         CONNECTION_STATUS_TIMER.reset();
      } else {
         disconnectedTicks = 0;
      }

      exitBlock: {
         if (connectionState == 2) {
            long l = 10000L;
            if (CONNECTION_STATUS_TIMER.reached2(l)) {
               break exitBlock;
            }
         }

         Augustus.getInstance().getClass();
         mc.fontRendererObj.drawString(str, sr2.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(str) / 2 + (flag ? 51 : 0), 1, n);
      }

      GL11.glPopMatrix();
      RenderUtil.resetGlColorState();
   }

   public void onTick5() {
      if ((
            Augustus.getInstance().getUsername().equals("7yns")
               || Augustus.getInstance().getUsername().equals("xaelo")
               || Augustus.getInstance().getUsername().equals("goatqjudoo")
         )
         && ClientManagers.moduleManager.combatRotationTracker.isHiddenInGui()) {
         boolean flag = false;
         ClientManagers.moduleManager.combatRotationTracker.setHiddenInGui(flag);
      }

      EntityRenderer.shaderProgramSwitchEnabled = this.tryFixingRendermodulesWithOptifineShaders.getValue();
      EntityRenderer.shaderNameTagFix = this.tryFixingNametagsWithOptifineShaders.getValue();
      if (HolidayDetector.getCurrentHoliday() == ClientHoliday.HALLOWEEN && mc.thePlayer != null) {
         long halloweenSoundDelay2 = this.halloweenSoundDelay;
         if (this.holidaySoundTimer.reached2(halloweenSoundDelay2)) {
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE14);
            long l = 500000L;
            long l2 = 220000L;
            this.halloweenSoundDelay = DefaultRandomGenerator.instance.nextLong2(l2, l);
            this.holidaySoundTimer.reset();
         }
      }

      if (HolidayDetector.getCurrentHoliday() == ClientHoliday.FIRST_APRIL && mc.thePlayer != null) {
         long aprilFoolsSoundDelay2 = this.aprilFoolsSoundDelay;
         if (this.holidaySoundTimer.reached2(aprilFoolsSoundDelay2)) {
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE12);
            long l3 = 1000000L;
            long l4 = 800000L;
            this.aprilFoolsSoundDelay = DefaultRandomGenerator.instance.nextLong2(l4, l3);
            this.holidaySoundTimer.reset();
         }
      }
   }

   public void onTick3() {
      boolean isShadersCompatible = BlurShadowManager.isShadersCompatible();
      if (this.lastBlurState != isShadersCompatible) {
         WindowResizeListener.onWindowResize(mc.displayWidth, mc.displayHeight);
         this.lastBlurState = isShadersCompatible;
      }
   }

   public void onTick4() {
      disconnectedTicks++;
   }

   public void onChat(EventChat event) {
      String message = event.getMessage();
      if (message.toLowerCase(Locale.ROOT).startsWith("/report")) {
         Augustus.getInstance().getClass();
         String[] strings = message.split(" ");
         if (strings.length > 1 && OnlineUserRegistry.usersByIngameName.containsKey(strings[1])) {
            boolean flag = true;
            ClientManagers.moduleManager.shuffleMode.setToggled(flag);
            event.cancel();
         }
      }
   }

   public static void renderMainMenuAccountInfo() {
      if (!ClientManagers.moduleManager.augustusServer.hideMainMenuAccountInfos.getValue()) {
         ProxyEntry proxy = Augustus.getInstance().getProxy();
         mc.fontRendererObj
            .drawStringWithShadow(
               "Username: §e"
                  + Augustus.getInstance().getUsername()
                  + "§7 [§e"
                  + Augustus.getInstance().getUserLevel()
                  + "§7]§r ["
                  + Augustus.getInstance().getUserRankColor()
                  + Augustus.getInstance().getUserRank()
                  + "§r]",
               2.0F,
               2.0F,
               -1
            );
         FontRenderer fontRendererObj = mc.fontRendererObj;
         StringBuilder sb = new StringBuilder();
         sb = sb.append("Account: ");
         String str;
         if (mc.session.getToken().length() < 5) {
            str = "§c";
         } else {
            str = "§a";
         }

         fontRendererObj.drawStringWithShadow(sb.append(str).append(mc.getSession().getUsername()).toString(), 2.0F, 12.0F, -1);
         mc.fontRendererObj.drawStringWithShadow("Proxy: " + (proxy == null ? "§cNot connected!" : "§7" + proxy.getHost() + ":" + proxy.getPort()), 2.0F, 22.0F, -1);
      }
   }

   public static void setConnectionState(int n) {
      connectionState = n;
   }

   public static void playButtonClickSound() {
      String selectedValue = Augustus.getInstance().getModuleManager().augustusServer.mode.getSelectedValue();
      switch (selectedValue) {
         case "Vanilla":
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            break;
         case "Piano":
         case "Sigma":
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
      }
   }

   public static void playModuleEnableSound() {
      String selectedValue = Augustus.getInstance().getModuleManager().augustusServer.mode.getSelectedValue();
      switch (selectedValue) {
         case "Vanilla":
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(ClientSounds.GUI_TEXTURE, 1.0F));
            break;
         case "Piano":
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE5);
            break;
         case "Sigma":
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE4);
      }
   }

   public static void playModuleDisableSound() {
      String selectedValue = Augustus.getInstance().getModuleManager().augustusServer.mode.getSelectedValue();
      switch (selectedValue) {
         case "Vanilla":
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(ClientSounds.GUI_TEXTURE, 1.0F));
            break;
         case "Piano":
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE13);
            break;
         case "Sigma":
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE11);
      }
   }

   private static void reportChatTelemetry(String str) {
   }

   private static void reportCommandTelemetry(String str) {
   }

   private static boolean isBanStatisticsVisible() {
      return false;
   }

   private static boolean isFastServerPingingVisible() {
      return false;
   }





}
