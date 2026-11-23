package net.augustus.modules.misc;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.ui.notifications.NotificationLevel;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.EventPacket;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.ClientManagers;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.ui.notifications.NotificationManager;
import net.augustus.settings.options.StaffDetectorModeOption;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.Augustus;
import net.augustus.settings.SettingChangeListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.options.NotificationCategoryOption;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.BlockPos;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

public class StaffDetector extends Module implements LoadWorldListener, RenderListener, PacketListener, TickListener {
   private static final String STAFF_LISTS_DIR = ClientPaths.CLIENT_DIR + File.separator + "staffLists";
   private final MultiOptionSetting modes;
   private final DoubleValue normalModeSettings;
   private final BooleanValue excludeRawUuids;
   private final BooleanValue showInSpectatorListNormal;
   private final BooleanValue chatMessageNormal;
   private final BooleanValue notificationNormal;
   private final BooleanValue acousticWarningNormal;
   private final DoubleValue simpleModeSettings;
   private final BooleanValue showInSpectatorListSimple;
   private final BooleanValue chatMessageSimple;
   private final BooleanValue notificationSimple;
   private final BooleanValue acousticWarningSimple;
   private final DoubleValue oldgommeModeSettings;
   private final RandomizedRangeSettings checkDelay;
   private final BooleanValue showInSpectatorListGomme;
   private final BooleanValue chatMessageGomme;
   private final BooleanValue notificationGomme;
   private final BooleanValue acousticWarningGomme;
   private final DoubleValue otherModeSettings;
   private final BooleanValue showInSpectatorListOther;
   private final BooleanValue chatMessageOther;
   private final BooleanValue notificationOther;
   private final BooleanValue acousticWarningOther;
   private final DoubleValue listbasedModeSettings;
   private final BooleansSetting lists;
   private final BooleanValue showInSpectatorListListBased;
   private final BooleanValue chatMessageListBased;
   private final BooleanValue notificationListBased;
   private final BooleanValue acousticWarningListBased;
   private final StringValue acousticWaringVolume;
   private final StringValue scale;
   private final BlurShadowSettings blurShadowSettings;
   private final TimeHelper timeHelper;
   private final ArrayList gommeTabSpectators;
   private final HashSet gommeDetectedStaff;
   private final HashMap gommeSeenPlayersTimestamps;
   private boolean simpleStaffDetected;
   private final HashSet listDetectedStaff;
   private final HashSet configuredStaffNames;
   private long otherModeLastDetectedMs;
   private final HashSet normalDetectedStaff;
   private final HashMap spectatorOverlayEntries;
   private final ArrayList pendingLookupTasks;
   private CompletableFuture activeLookupFuture;

   public StaffDetector() {
       super("StaffDetector", "Moderatorerkenner", new Color(80, 162, 181), Categorys.MISC);
      this.modes = new MultiOptionSetting(this, "Modes", StaffDetectorModeOption.values());
      DoubleValue doubleValue = new DoubleValue(this, "Normal-Mode settings");
      SettingVisibilityCondition condition = StaffDetectorModeOption.NORMAL::isSelected;
      this.normalModeSettings = doubleValue.visibleWhen(condition);
      BooleanValue booleanValue = new BooleanValue(this, "Exclude raw UUIDS", true, this.normalModeSettings);
      DoubleValue normalModeSettings2 = this.normalModeSettings;
      this.normalModeSettings.getClass();
      DoubleValue doubleValue2 = normalModeSettings2;
      SettingVisibilityCondition condition2 = () -> doubleValue2.isVisible();
      this.excludeRawUuids = (BooleanValue)booleanValue.withVisibility(condition2);
      BooleanValue booleanValue2 = new BooleanValue(this, "Show in spectator list normal", true, this.normalModeSettings);
      String str = "Show in spectator list";
      BooleanValue booleanValue3 = (BooleanValue)booleanValue2.withDisplayName(str);
      normalModeSettings2 = this.normalModeSettings;
      this.normalModeSettings.getClass();
      DoubleValue doubleValue3 = normalModeSettings2;
      SettingVisibilityCondition condition3 = () -> doubleValue3.isVisible();
      this.showInSpectatorListNormal = (BooleanValue)booleanValue3.withVisibility(condition3);
      BooleanValue booleanValue4 = new BooleanValue(this, "Chat message normal", true, this.normalModeSettings);
      String str2 = "Send chat message";
      BooleanValue booleanValue5 = (BooleanValue)booleanValue4.withDisplayName(str2);
      normalModeSettings2 = this.normalModeSettings;
      this.normalModeSettings.getClass();
      DoubleValue doubleValue4 = normalModeSettings2;
      SettingVisibilityCondition condition4 = () -> doubleValue4.isVisible();
      this.chatMessageNormal = (BooleanValue)booleanValue5.withVisibility(condition4);
      BooleanValue booleanValue6 = new BooleanValue(this, "Notification normal", true, this.normalModeSettings);
      String str3 = "Show notification";
      BooleanValue booleanValue7 = (BooleanValue)booleanValue6.withDisplayName(str3);
      SettingVisibilityCondition condition5 = () -> this.isNormalNotificationVisible();
      this.notificationNormal = (BooleanValue)booleanValue7.withVisibility(condition5);
      BooleanValue booleanValue8 = new BooleanValue(this, "Acoustic warning normal", true, this.normalModeSettings);
      String str4 = "Acoustic warning";
      BooleanValue booleanValue9 = (BooleanValue)booleanValue8.withDisplayName(str4);
      normalModeSettings2 = this.normalModeSettings;
      this.normalModeSettings.getClass();
      DoubleValue doubleValue5 = normalModeSettings2;
      SettingVisibilityCondition condition6 = () -> doubleValue5.isVisible();
      this.acousticWarningNormal = (BooleanValue)booleanValue9.withVisibility(condition6);
      DoubleValue doubleValue6 = new DoubleValue(this, "Simple-Mode settings");
      SettingVisibilityCondition condition7 = StaffDetectorModeOption.SIMPLE::isSelected;
      this.simpleModeSettings = doubleValue6.visibleWhen(condition7);
      BooleanValue booleanValue10 = new BooleanValue(this, "Show in spectator list simple", true, this.simpleModeSettings);
      String str5 = "Show in spectator list";
      BooleanValue booleanValue11 = (BooleanValue)booleanValue10.withDisplayName(str5);
      normalModeSettings2 = this.simpleModeSettings;
      this.simpleModeSettings.getClass();
      DoubleValue doubleValue7 = normalModeSettings2;
      SettingVisibilityCondition condition8 = () -> doubleValue7.isVisible();
      this.showInSpectatorListSimple = (BooleanValue)booleanValue11.withVisibility(condition8);
      BooleanValue booleanValue12 = new BooleanValue(this, "Chat message simple", true, this.simpleModeSettings);
      String str6 = "Send chat message";
      BooleanValue booleanValue13 = (BooleanValue)booleanValue12.withDisplayName(str6);
      normalModeSettings2 = this.simpleModeSettings;
      this.simpleModeSettings.getClass();
      DoubleValue doubleValue8 = normalModeSettings2;
      SettingVisibilityCondition condition9 = () -> doubleValue8.isVisible();
      this.chatMessageSimple = (BooleanValue)booleanValue13.withVisibility(condition9);
      BooleanValue booleanValue14 = new BooleanValue(this, "Notification simple", true, this.simpleModeSettings);
      String str7 = "Show notification";
      BooleanValue booleanValue15 = (BooleanValue)booleanValue14.withDisplayName(str7);
      SettingVisibilityCondition condition10 = () -> {
         return this.simpleModeSettings.isVisible() && this.isVanishNotificationActive();
      };
      this.notificationSimple = (BooleanValue)booleanValue15.withVisibility(condition10);
      BooleanValue booleanValue16 = new BooleanValue(this, "Acoustic warning simple", true, this.simpleModeSettings);
      String str8 = "Acoustic warning";
      BooleanValue booleanValue17 = (BooleanValue)booleanValue16.withDisplayName(str8);
      normalModeSettings2 = this.simpleModeSettings;
      this.simpleModeSettings.getClass();
      DoubleValue doubleValue9 = normalModeSettings2;
      SettingVisibilityCondition condition11 = () -> doubleValue9.isVisible();
      this.acousticWarningSimple = (BooleanValue)booleanValue17.withVisibility(condition11);
      DoubleValue doubleValue10 = new DoubleValue(this, "OldGomme-Mode settings");
      SettingVisibilityCondition condition12 = StaffDetectorModeOption.GOMME::isSelected;
      this.oldgommeModeSettings = doubleValue10.visibleWhen(condition12);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "CheckDelay", 2000.0, 3000.0, 500.0, 5000.0, 0, this.oldgommeModeSettings)).withoutNotInARow();
      normalModeSettings2 = this.oldgommeModeSettings;
      this.oldgommeModeSettings.getClass();
      DoubleValue doubleValue11 = normalModeSettings2;
      SettingVisibilityCondition condition13 = () -> doubleValue11.isVisible();
      this.checkDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition13);
      BooleanValue booleanValue18 = new BooleanValue(this, "Show in spectator list gomme", true, this.oldgommeModeSettings);
      String str9 = "Show in spectator list";
      BooleanValue booleanValue19 = (BooleanValue)booleanValue18.withDisplayName(str9);
      normalModeSettings2 = this.oldgommeModeSettings;
      this.oldgommeModeSettings.getClass();
      DoubleValue doubleValue12 = normalModeSettings2;
      SettingVisibilityCondition condition14 = () -> doubleValue12.isVisible();
      this.showInSpectatorListGomme = (BooleanValue)booleanValue19.withVisibility(condition14);
      BooleanValue booleanValue20 = new BooleanValue(this, "Chat message gomme", true, this.oldgommeModeSettings);
      String str10 = "Send chat message";
      BooleanValue booleanValue21 = (BooleanValue)booleanValue20.withDisplayName(str10);
      normalModeSettings2 = this.oldgommeModeSettings;
      this.oldgommeModeSettings.getClass();
      DoubleValue doubleValue13 = normalModeSettings2;
      SettingVisibilityCondition condition15 = () -> doubleValue13.isVisible();
      this.chatMessageGomme = (BooleanValue)booleanValue21.withVisibility(condition15);
      BooleanValue booleanValue22 = new BooleanValue(this, "Notification gomme", true, this.oldgommeModeSettings);
      String str11 = "Show notification";
      BooleanValue booleanValue23 = (BooleanValue)booleanValue22.withDisplayName(str11);
      SettingVisibilityCondition condition16 = () -> this.isGommeNotificationVisible();
      this.notificationGomme = (BooleanValue)booleanValue23.withVisibility(condition16);
      BooleanValue booleanValue24 = new BooleanValue(this, "Acoustic warning gomme", true, this.oldgommeModeSettings);
      String str12 = "Acoustic warning";
      BooleanValue booleanValue25 = (BooleanValue)booleanValue24.withDisplayName(str12);
      normalModeSettings2 = this.oldgommeModeSettings;
      this.oldgommeModeSettings.getClass();
      DoubleValue doubleValue14 = normalModeSettings2;
      SettingVisibilityCondition condition17 = () -> doubleValue14.isVisible();
      this.acousticWarningGomme = (BooleanValue)booleanValue25.withVisibility(condition17);
      DoubleValue doubleValue15 = new DoubleValue(this, "Other-Mode settings");
      SettingVisibilityCondition condition18 = StaffDetectorModeOption.OTHER::isSelected;
      this.otherModeSettings = doubleValue15.visibleWhen(condition18);
      BooleanValue booleanValue26 = new BooleanValue(this, "Show in spectator list other", true, this.otherModeSettings);
      String str13 = "Show in spectator list";
      BooleanValue booleanValue27 = (BooleanValue)booleanValue26.withDisplayName(str13);
      normalModeSettings2 = this.otherModeSettings;
      this.otherModeSettings.getClass();
      DoubleValue doubleValue16 = normalModeSettings2;
      SettingVisibilityCondition condition19 = () -> doubleValue16.isVisible();
      this.showInSpectatorListOther = (BooleanValue)booleanValue27.withVisibility(condition19);
      BooleanValue booleanValue28 = new BooleanValue(this, "Chat message other", true, this.otherModeSettings);
      String str14 = "Send chat message";
      BooleanValue booleanValue29 = (BooleanValue)booleanValue28.withDisplayName(str14);
      normalModeSettings2 = this.otherModeSettings;
      this.otherModeSettings.getClass();
      DoubleValue doubleValue17 = normalModeSettings2;
      SettingVisibilityCondition condition20 = () -> doubleValue17.isVisible();
      this.chatMessageOther = (BooleanValue)booleanValue29.withVisibility(condition20);
      BooleanValue booleanValue30 = new BooleanValue(this, "Notification other", true, this.otherModeSettings);
      String str15 = "Show notification";
      BooleanValue booleanValue31 = (BooleanValue)booleanValue30.withDisplayName(str15);
      SettingVisibilityCondition condition21 = () -> this.isOtherNotificationVisible();
      this.notificationOther = (BooleanValue)booleanValue31.withVisibility(condition21);
      BooleanValue booleanValue32 = new BooleanValue(this, "Acoustic warning other", true, this.otherModeSettings);
      String str16 = "Acoustic warning";
      BooleanValue booleanValue33 = (BooleanValue)booleanValue32.withDisplayName(str16);
      normalModeSettings2 = this.otherModeSettings;
      this.otherModeSettings.getClass();
      DoubleValue doubleValue18 = normalModeSettings2;
      SettingVisibilityCondition condition22 = () -> doubleValue18.isVisible();
      this.acousticWarningOther = (BooleanValue)booleanValue33.withVisibility(condition22);
      DoubleValue doubleValue19 = new DoubleValue(this, "ListBased-Mode settings");
      SettingVisibilityCondition condition23 = StaffDetectorModeOption.LIST_BASED::isSelected;
      this.listbasedModeSettings = doubleValue19.visibleWhen(condition23);
      BooleansSetting booleansSetting = new BooleansSetting(this, "Lists", "GommeHDDefault", new String[0], this.listbasedModeSettings);
      normalModeSettings2 = this.listbasedModeSettings;
      this.listbasedModeSettings.getClass();
      DoubleValue doubleValue20 = normalModeSettings2;
      SettingVisibilityCondition condition24 = () -> doubleValue20.isVisible();
      BooleansSetting booleansSetting2 = (BooleansSetting)booleansSetting.withVisibility(condition24);
      SettingChangeListener listener = () -> this.loadSelectedStaffList();
      SettingChangeListener listener2 = listener;
      this.lists = (BooleansSetting)booleansSetting2.withChangeListener(listener2);
      BooleanValue booleanValue34 = new BooleanValue(this, "Show in spectator list list based", true, this.listbasedModeSettings);
      String str17 = "Show in spectator list";
      BooleanValue booleanValue35 = (BooleanValue)booleanValue34.withDisplayName(str17);
      normalModeSettings2 = this.listbasedModeSettings;
      this.listbasedModeSettings.getClass();
      DoubleValue doubleValue21 = normalModeSettings2;
      SettingVisibilityCondition condition25 = () -> doubleValue21.isVisible();
      this.showInSpectatorListListBased = (BooleanValue)booleanValue35.withVisibility(condition25);
      BooleanValue booleanValue36 = new BooleanValue(this, "Chat message list based", true, this.listbasedModeSettings);
      String str18 = "Send chat message";
      BooleanValue booleanValue37 = (BooleanValue)booleanValue36.withDisplayName(str18);
      normalModeSettings2 = this.listbasedModeSettings;
      this.listbasedModeSettings.getClass();
      DoubleValue doubleValue22 = normalModeSettings2;
      SettingVisibilityCondition condition26 = () -> doubleValue22.isVisible();
      this.chatMessageListBased = (BooleanValue)booleanValue37.withVisibility(condition26);
      BooleanValue booleanValue38 = new BooleanValue(this, "Notification list based", true, this.listbasedModeSettings);
      String str19 = "Show notification";
      BooleanValue booleanValue39 = (BooleanValue)booleanValue38.withDisplayName(str19);
      SettingVisibilityCondition condition27 = () -> this.isListNotificationVisible();
      this.notificationListBased = (BooleanValue)booleanValue39.withVisibility(condition27);
      BooleanValue booleanValue40 = new BooleanValue(this, "Acoustic warning list based", true, this.listbasedModeSettings);
      String str20 = "Acoustic warning";
      BooleanValue booleanValue41 = (BooleanValue)booleanValue40.withDisplayName(str20);
      normalModeSettings2 = this.listbasedModeSettings;
      this.listbasedModeSettings.getClass();
      DoubleValue doubleValue23 = normalModeSettings2;
      SettingVisibilityCondition condition28 = () -> doubleValue23.isVisible();
      this.acousticWarningListBased = (BooleanValue)booleanValue41.withVisibility(condition28);
      StringValue stringValue = new StringValue(this, "Acoustic waring volume", 93.0, 0.0, 100.0, 0);
      SettingVisibilityCondition condition29 = () -> {
         return this.acousticWarningNormal.getValue() && this.acousticWarningNormal.isVisible()
            || this.acousticWarningGomme.getValue() && this.acousticWarningGomme.isVisible()
            || this.acousticWarningOther.getValue() && this.acousticWarningOther.isVisible()
            || this.acousticWarningListBased.getValue() && this.acousticWarningListBased.isVisible();
      };
      this.acousticWaringVolume = stringValue.visibleWhen(condition29);
      StringValue stringValue2 = new StringValue(this, "Scale", 2.0, 1.0, 4.0, 2);
      SettingVisibilityCondition condition30 = () -> {
         return this.showInSpectatorListNormal.getValue() && this.showInSpectatorListNormal.isVisible()
            || this.showInSpectatorListGomme.getValue() && this.showInSpectatorListGomme.isVisible()
            || this.showInSpectatorListOther.getValue() && this.showInSpectatorListOther.isVisible()
            || this.showInSpectatorListListBased.getValue() && this.showInSpectatorListListBased.isVisible();
      };
      this.scale = stringValue2.visibleWhen(condition30);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      BlurShadowSettings blurShadowSettings3 = blurShadowSettings2.withCustomShadowColor(black);
      SettingVisibilityCondition condition31 = () -> this.isBlurShadowVisible();
      this.blurShadowSettings = (BlurShadowSettings)blurShadowSettings3.withVisibility(condition31);
      this.timeHelper = new TimeHelper();
      this.gommeTabSpectators = new ArrayList();
      this.gommeDetectedStaff = new HashSet();
      this.gommeSeenPlayersTimestamps = new HashMap();
      this.simpleStaffDetected = false;
      this.listDetectedStaff = new HashSet();
      this.configuredStaffNames = new HashSet();
      this.otherModeLastDetectedMs = 0L;
      this.normalDetectedStaff = new HashSet();
      this.spectatorOverlayEntries = new HashMap();
      this.pendingLookupTasks = new ArrayList();
      this.activeLookupFuture = null;
      this.extractDefaultStaffLists();
   }

   @Override
   public void onEnable() {
      this.refreshStaffListOptions();
      this.clearAllDetectedStaff();
      this.spectatorOverlayEntries.clear();
      this.gommeSeenPlayersTimestamps.clear();
      this.checkDelay.nextValue();
      this.simpleStaffDetected = false;
   }

   @Override
   public void onDisable() {
      this.clearAllDetectedStaff();
      this.spectatorOverlayEntries.clear();
      this.gommeSeenPlayersTimestamps.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
      byte b2 = 26;
      this.toggleEventSubscription(b2);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
      byte b4 = 17;
      this.toggleEventSubscription(b4);
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (mc.currentScreen == null && !this.spectatorOverlayEntries.isEmpty() && !KeyboardInputUtil.isKeyDown(15)) {
         GL11.glPushMatrix();
         GL11.glScaled((double)(1.0F / (float)sr.getScaleFactor()), (double)(1.0F / (float)sr.getScaleFactor()), 1.0);
         double value = this.scale.getValue();
         GL11.glScaled(value, value, 1.0);
         ScaledResolution sr2 = new ScaledResolution((double)mc.displayWidth * (1.0 / value), (double)mc.displayHeight * (1.0 / value), (int)value);
         FontRenderer fontRendererObj = mc.fontRendererObj;
         int n = sr2.getScaledWidth() / 2;
         String str = "Spectators:";
         int n2 = fontRendererObj.getStringWidth(str) + 4;
         int n3 = fontRendererObj.FONT_HEIGHT + 2;

         for (String str2 : (Iterable<String>)(this.spectatorOverlayEntries.keySet())) {
            n3 += 11;
            int n4 = fontRendererObj.getStringWidth(str2) + 4;
            if (n4 > n2) {
               n2 = n4;
            }
         }

         int n5 = n2;
         int n6 = n3;
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         boolean isBlurActive = this.blurShadowSettings.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> {
            byte b = 1;
            if (shaderRenderPass == ShaderRenderPass.SHADOW) {
               int n7 = n - n5 / 2;
               int n8 = n + n5 / 2;
               int n9 = b + n6;
               BlurShadowSettings blurShadowSettings2 = this.blurShadowSettings;
               int rgb = Color.black.getRGB();
               RenderUtil.drawRect3(n7, 0, n8, n9, blurShadowSettings2.resolveShadowColor(rgb));
            } else {
               RenderUtil.drawRect6(n - n5 / 2, 0, n + n5 / 2, b + n6, 0, 0, 0, 150);
            }

            RenderUtil.drawRect6(n - n5 / 2, 11, n + n5 / 2, 12, 0, 0, 0);
            if (shaderRenderPass == ShaderRenderPass.NONE) {
               fontRendererObj.drawString(str, n - fontRendererObj.getStringWidth(str) / 2, b, Augustus.getInstance().getClientColor());
               b = 13;

               for (String str3 : (Iterable<String>)(this.spectatorOverlayEntries.keySet())) {
                  Boolean bool = (Boolean)this.spectatorOverlayEntries.get(str3);
                  if (bool != null) {
                     Color color2 = bool ? Color.RED : Color.LIGHT_GRAY;
                     fontRendererObj.drawString(str3, n - fontRendererObj.getStringWidth(str3) / 2, b, color2.getRGB());
                     b += 11;
                  }
               }
            }
         });
         GL11.glPopMatrix();
         RenderUtil.resetGlColorState();
      }
   }

   @Override
   public void onTick2() {
      this.rebuildSpectatorOverlayMap();
      if (StaffDetectorModeOption.GOMME.isSelected()) {
         TimeHelper timeHelper2 = this.timeHelper;
         long lastIntValue = (long)this.checkDelay.getLastIntValue();
         if (timeHelper2.reached2(lastIntValue) && !(mc.currentScreen instanceof GuiChat)) {
            this.checkDelay.nextValue();
            this.timeHelper.reset();
            this.sendGommeTabCompleteProbe();

            for (NetworkPlayerInfo networkPlayerInfo : (Iterable<NetworkPlayerInfo>)(mc.getNetHandler().getPlayerInfoMap())) {
               String name2 = networkPlayerInfo.getGameProfile().getName();
               this.gommeSeenPlayersTimestamps.remove(name2);
               this.gommeSeenPlayersTimestamps.put(name2, System.currentTimeMillis());
            }

            LinkedList list = new LinkedList();

            for (String str : (Iterable<String>)(this.gommeSeenPlayersTimestamps.keySet())) {
               Long val = (Long)this.gommeSeenPlayersTimestamps.get(str);
               if (val == null || val + 15000L < System.currentTimeMillis() && !this.gommeTabSpectators.contains(str)) {
                  list.add(str);
               }
            }

            for (String str2 : (Iterable<String>)(list)) {
               this.gommeSeenPlayersTimestamps.remove(str2);
            }

            for (String str3 : (Iterable<String>)(this.gommeTabSpectators)) {
               if (this.gommeSeenPlayersTimestamps.get(str3) == null) {
                  if (this.acousticWarningGomme.getValue()) {
                     this.playWarningSound();
                  }

                  this.gommeDetectedStaff.add(str3);
                  this.gommeSeenPlayersTimestamps.put(str3, System.currentTimeMillis());
               }
            }
         }
      }

      if (StaffDetectorModeOption.LIST_BASED.isSelected() && !this.configuredStaffNames.isEmpty()) {
         for (NetworkPlayerInfo networkPlayerInfo2 : (Iterable<NetworkPlayerInfo>)(mc.getNetHandler().getPlayerInfoMap())) {
            if (networkPlayerInfo2 != null && networkPlayerInfo2.getGameProfile() != null) {
               String name3 = networkPlayerInfo2.getGameProfile().getName();
               this.checkListBasedStaffName(name3);
            }
         }

         for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
            if (entity instanceof EntityPlayer) {
               String name4 = ((EntityPlayer)entity).getGameProfile().getName();
               this.checkListBasedStaffName(name4);
            }
         }
      }

      if (StaffDetectorModeOption.NORMAL.isSelected() && !this.pendingLookupTasks.isEmpty() && (this.activeLookupFuture == null || !this.activeLookupFuture.isDone())) {
         Runnable runnable = (Runnable)this.pendingLookupTasks.get(0);
         this.activeLookupFuture = CompletableFuture.runAsync(runnable);
         this.pendingLookupTasks.remove(0);
      }

      this.pruneDisabledModeState();
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.clearAllDetectedStaff();
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (mc.theWorld != null && mc.thePlayer != null) {
         if (StaffDetectorModeOption.NORMAL.isSelected() && packet instanceof S38PacketPlayerListItem) {
            S38PacketPlayerListItem packet2 = (S38PacketPlayerListItem)packet;
            if (Objects.requireNonNull(packet2.getAction()) == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
               S38PacketPlayerListItem packet3 = packet2;
               this.pendingLookupTasks.add((Runnable)() -> {
                  LinkedList list = new LinkedList();

                  for (S38PacketPlayerListItem.AddPlayerData addPlayerData : (Iterable<S38PacketPlayerListItem.AddPlayerData>)(packet3.getPlayers())) {
                     if (!mc.getNetHandler().getPlayerInfoMap2().containsKey(addPlayerData.getProfile().getId())) {
                        UUID id = addPlayerData.getProfile().getId();

                        try {
                           URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id);
                           HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                           conn.setRequestMethod("GET");
                           conn.setConnectTimeout(20000);
                           conn.connect();
                           BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                           String str = "";
                           StringBuilder sb = new StringBuilder();

                           while ((str = reader.readLine()) != null) {
                              sb.append(str);
                           }

                           conn.disconnect();
                           IOUtils.closeQuietly(reader);
                           String[] strings = sb.toString().split("\"");
                           String str2 = strings[7];
                           this.handleNormalStaffDetected(str2);
                           list.add(strings[7]);
                        } catch (Exception e) {
                           if (!this.excludeRawUuids.getValue()) {
                              String str3 = id.toString();
                              this.handleNormalStaffDetected(str3);
                              list.add(str3);
                           }
                        }
                     }
                  }

                  this.normalDetectedStaff.removeIf(obj -> {
                     return !list.contains(obj);
                  });
               });
            }
         }

         if (StaffDetectorModeOption.SIMPLE.isSelected() && packet instanceof S38PacketPlayerListItem) {
            S38PacketPlayerListItem packet4 = (S38PacketPlayerListItem)packet;
            if (packet4.getAction() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
               List players = packet4.getPlayers();
               int n = players == null ? 0 : players.size();
               NetHandlerPlayClient netHandler = mc.getNetHandler();
               int n2 = netHandler == null ? 0 : netHandler.getPlayerInfoMap().size();
               if (n != n2) {
                  if (!this.simpleStaffDetected) {
                     if (this.acousticWarningSimple.getValue()) {
                        this.playWarningSound();
                     }

                     if (this.chatMessageSimple.getValue()) {
                        String str4 = "unknown";
                        StaffDetectorModeOption simple = StaffDetectorModeOption.SIMPLE;
                        this.sendStaffChatAlert(simple, str4);
                     }

                     if (this.notificationSimple.getValue()) {
                        String str5 = "unknown";
                        StaffDetectorModeOption simple2 = StaffDetectorModeOption.SIMPLE;
                        this.sendStaffNotification(simple2, str5);
                     }
                  }

                  this.simpleStaffDetected = true;
               } else {
                  this.simpleStaffDetected = false;
               }
            }
         }

         if (StaffDetectorModeOption.GOMME.isSelected() && packet instanceof S3APacketTabComplete && !(mc.currentScreen instanceof GuiChat)) {
            S3APacketTabComplete packet5 = (S3APacketTabComplete)packet;
            String[] matches = packet5.getMatches();
            ArrayList list2 = new ArrayList();

            for (String str6 : matches) {
               boolean flag = !str6.equals(mc.session.getUsername());

               for (NetworkPlayerInfo networkPlayerInfo : (Iterable<NetworkPlayerInfo>)(mc.getNetHandler().getPlayerInfoMap())) {
                  if (!networkPlayerInfo.getGameProfile().getName().equals(mc.thePlayer.getGameProfile().getName())) {
                     String str7 = networkPlayerInfo.getDisplayName() != null
                        ? networkPlayerInfo.getDisplayName().getUnformattedText()
                        : ScorePlayerTeam.formatPlayerName(networkPlayerInfo.getPlayerTeam(), networkPlayerInfo.getGameProfile().getName());
                     if (str7.contains(str6)) {
                        flag = false;
                        break;
                     }
                  }
               }

               if (flag) {
                  list2.add(str6);
               }

               if (this.chatMessageGomme.getValue() || this.notificationGomme.getValue()) {
                  for (String str8 : (Iterable<String>)(list2)) {
                     if (!this.gommeTabSpectators.contains(str8)) {
                        if (this.chatMessageGomme.getValue()) {
                           StaffDetectorModeOption gomme = StaffDetectorModeOption.GOMME;
                           this.sendStaffChatAlert(gomme, str8);
                        }

                        if (this.notificationGomme.getValue()) {
                           StaffDetectorModeOption gomme2 = StaffDetectorModeOption.GOMME;
                           this.sendStaffNotification(gomme2, str8);
                        }
                     }
                  }
               }

               this.gommeTabSpectators.clear();
               this.gommeTabSpectators.addAll(list2);
            }
         }

         if (StaffDetectorModeOption.OTHER.isSelected()) {
            long l = System.currentTimeMillis();
            if (packet instanceof S1DPacketEntityEffect && mc.theWorld.getEntityByID(((S1DPacketEntityEffect)packet).getEntityId()) == null
               || packet instanceof S14PacketEntity && mc.theWorld.getEntityByID(((S14PacketEntity)packet).getEntityId()) == null
               || packet instanceof S18PacketEntityTeleport && mc.theWorld.getEntityByID(((S18PacketEntityTeleport)packet).getEntityId()) == null) {
               if (this.getOtherModeLastDetectedMs() == 0L) {
                  if (this.acousticWarningOther.getValue()) {
                     this.playWarningSound();
                  }

                  if (this.chatMessageOther.getValue()) {
                     String str9 = "unknown";
                     StaffDetectorModeOption other = StaffDetectorModeOption.OTHER;
                     this.sendStaffChatAlert(other, str9);
                  }

                  if (this.notificationOther.getValue()) {
                     String str10 = "unknown";
                     StaffDetectorModeOption other2 = StaffDetectorModeOption.OTHER;
                     this.sendStaffNotification(other2, str10);
                  }
               }

               this.setOtherModeLastDetectedMs(l);
            }

            if (l - this.getOtherModeLastDetectedMs() > 30000L) {
               long l2 = 0L;
               this.setOtherModeLastDetectedMs(l2);
            }
         }
      }
   }

   private void checkListBasedStaffName(String str) {
      if (this.configuredStaffNames.contains(str) && !this.listDetectedStaff.contains(str)) {
         if (this.acousticWarningListBased.getValue()) {
            this.playWarningSound();
         }

         if (this.chatMessageListBased.getValue()) {
            StaffDetectorModeOption listbased = StaffDetectorModeOption.LIST_BASED;
            this.sendStaffChatAlert(listbased, str);
         }

         if (this.notificationListBased.getValue()) {
            StaffDetectorModeOption listbased2 = StaffDetectorModeOption.LIST_BASED;
            this.sendStaffNotification(listbased2, str);
         }

         this.listDetectedStaff.add(str);
      }
   }

   private void handleNormalStaffDetected(String str) {
      if (!this.normalDetectedStaff.contains(str)) {
         this.normalDetectedStaff.add(str);
         if (this.acousticWarningNormal.getValue()) {
            this.playWarningSound();
         }

         if (this.chatMessageNormal.getValue()) {
            StaffDetectorModeOption normal = StaffDetectorModeOption.NORMAL;
            this.sendStaffChatAlert(normal, str);
         }

         if (this.notificationNormal.getValue()) {
            StaffDetectorModeOption normal2 = StaffDetectorModeOption.NORMAL;
            this.sendStaffNotification(normal2, str);
         }
      }
   }

   private void sendStaffChatAlert(StaffDetectorModeOption staffDetectorModeOption, String str) {
      String str2 = "§4§lStaff detected (" + staffDetectorModeOption.getName() + "):§6 " + str;
      this.sendChatMessage2(str2);
   }

   private void sendStaffNotification(StaffDetectorModeOption staffDetectorModeOption, String str) {
      if (Augustus.getInstance().getModuleManager().notifications.isToggled() && NotificationCategoryOption.VANISH_NOTIFICATION.isSelected()) {
         String str2 = "§4§lStaff detected (" + staffDetectorModeOption.getName() + "):§6 " + str;
         NotificationLevel info = NotificationLevel.INFO;
         String str3 = str2;
         NotificationManager.pushNotification("VanishNotify", str3, info);
      }
   }

   private void sendGommeTabCompleteProbe() {
      BlockPos pos = null;
      if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
         pos = mc.objectMouseOver.getBlockPos();
      }

      mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete("/tp ", pos));
   }

   private void rebuildSpectatorOverlayMap() {
      this.spectatorOverlayEntries.clear();
      if (StaffDetectorModeOption.GOMME.isSelected() && this.showInSpectatorListGomme.getValue()) {
         for (String str : (Iterable<String>)(this.gommeTabSpectators)) {
            this.spectatorOverlayEntries.put(str, this.gommeDetectedStaff.contains(str));
         }
      }

      if (StaffDetectorModeOption.OTHER.isSelected() && this.showInSpectatorListOther.getValue() && this.getOtherModeLastDetectedMs() != 0L) {
         this.spectatorOverlayEntries.put("Unknown Staff", true);
      }

      if (StaffDetectorModeOption.SIMPLE.isSelected() && this.showInSpectatorListSimple.getValue() && this.simpleStaffDetected) {
         this.spectatorOverlayEntries.put("Unknown Staff", true);
      }

      if (StaffDetectorModeOption.LIST_BASED.isSelected() && this.showInSpectatorListListBased.getValue()) {
         for (String str2 : (Iterable<String>)(this.listDetectedStaff)) {
            this.spectatorOverlayEntries.put(str2, true);
         }
      }

      if (StaffDetectorModeOption.NORMAL.isSelected() && this.showInSpectatorListNormal.getValue()) {
         for (String str3 : (Iterable<String>)(this.normalDetectedStaff)) {
            this.spectatorOverlayEntries.put(str3, true);
         }
      }
   }

   private void pruneDisabledModeState() {
      if (!StaffDetectorModeOption.LIST_BASED.isSelected()) {
         this.listDetectedStaff.clear();
      }

      if (!StaffDetectorModeOption.GOMME.isSelected()) {
         this.gommeTabSpectators.clear();
         this.gommeDetectedStaff.clear();
      }

      if (!StaffDetectorModeOption.OTHER.isSelected()) {
         long l = 0L;
         this.setOtherModeLastDetectedMs(l);
      }

      if (!StaffDetectorModeOption.NORMAL.isSelected()) {
         this.normalDetectedStaff.clear();
      }
   }

   private void clearAllDetectedStaff() {
      this.gommeTabSpectators.clear();
      this.gommeDetectedStaff.clear();
      this.listDetectedStaff.clear();
      long l = 0L;
      this.setOtherModeLastDetectedMs(l);
      this.normalDetectedStaff.clear();
   }

   private void extractDefaultStaffLists() {
      String str = Augustus.getInstance().getName().toLowerCase() + "/stafflists";
      boolean flag = true;
      String str2 = str;
      ClientFileUtil.extractResource("assets/augustus/stafflists/BlocksMCDefault.txt", str2, flag);
      str = Augustus.getInstance().getName().toLowerCase() + "/stafflists";
      boolean flag2 = true;
      String str3 = str;
      ClientFileUtil.extractResource("assets/augustus/stafflists/HypixelDefault.txt", str3, flag2);
      str = Augustus.getInstance().getName().toLowerCase() + "/stafflists";
      boolean flag3 = true;
      String str4 = str;
      ClientFileUtil.extractResource("assets/augustus/stafflists/GamsterDefault.txt", str4, flag3);
      str = Augustus.getInstance().getName().toLowerCase() + "/stafflists";
      boolean flag4 = true;
      String str5 = str;
      ClientFileUtil.extractResource("assets/augustus/stafflists/GommeHDDefault.txt", str5, flag4);
      str = Augustus.getInstance().getName().toLowerCase() + "/stafflists";
      boolean flag5 = true;
      String str6 = str;
      ClientFileUtil.extractResource("assets/augustus/stafflists/PikaDefault.txt", str6, flag5);
      this.refreshStaffListOptions();
   }

   private void refreshStaffListOptions() {
      File file = new File(STAFF_LISTS_DIR);
      ClientFileUtil.ensureDirectory(STAFF_LISTS_DIR);
      File[] files = file.listFiles();
      if (files != null) {
         List list = Arrays.stream(files).filter(file2 -> {
            return file2 != null && file2.getName().endsWith(".txt");
         }).map(file3 -> file3.getName().substring(0, Math.max(0, file3.getName().length() - 4))).collect(Collectors.toList());
         boolean flag = list.contains(this.lists.getSelectedValue());
         String[] strings = (String[])list.toArray(new String[0]);
         if (!flag) {
            String str = strings[0];
            this.lists.setSelectedValue(str);
         }

         this.lists.setOptions(strings);
         this.loadSelectedStaffList();
      }
   }

   private void loadSelectedStaffList() {
      File file = new File(STAFF_LISTS_DIR);
      ClientFileUtil.ensureDirectory(STAFF_LISTS_DIR);
      File[] files = file.listFiles();
      if (files != null) {
         Stream stream = Arrays.stream(files);
         File file2 = (File)stream.filter(obj -> {
            return ((File)obj).getName().equals(this.lists.getSelectedValue() + ".txt");
         }).findFirst().orElse(null);
         if (file2 != null) {
            ArrayList list = new ArrayList(ClientFileUtil.readAllLines(file2.getAbsolutePath()));
            list.replaceAll(obj2 -> {
               if (((String)obj2).contains(" ")) {
                  obj2 = StringUtils.replace((String)obj2, " ", "");
               }

               return obj2;
            });
            this.configuredStaffNames.clear();
            this.configuredStaffNames.addAll(list);
            String str = "Loaded " + list.size() + " staff names!";
            this.sendChatMessage2(str);
         }
      }
   }

   private void playWarningSound() {
      mc.getSoundHandler().playSound(PositionedSoundRecord.createWithVolume(ClientSounds.AUGUSTUS_TEXTURE7, (float)(this.acousticWaringVolume.getValue() / 100.0)));
   }

   private synchronized long getOtherModeLastDetectedMs() {
      return this.otherModeLastDetectedMs;
   }

   private synchronized void setOtherModeLastDetectedMs(long l) {
      this.otherModeLastDetectedMs = l;
   }

   public boolean hasConfirmedStaff() {

      for (String str : (Iterable<String>)(this.spectatorOverlayEntries.keySet())) {
         Boolean bool = (Boolean)this.spectatorOverlayEntries.get(str);
         if (bool != null && bool) {
            return true;
         }
      }

      return false;
   }

   private boolean isVanishNotificationActive() {
      return ClientManagers.moduleManager.oldNotification.isToggled() && NotificationCategoryOption.VANISH_NOTIFICATION.isSelected();
   }

   private boolean isBlurShadowVisible() {
      return this.showInSpectatorListNormal.getValue() && this.showInSpectatorListNormal.isVisible()
         || this.showInSpectatorListGomme.getValue() && this.showInSpectatorListGomme.isVisible()
         || this.showInSpectatorListOther.getValue() && this.showInSpectatorListOther.isVisible()
         || this.showInSpectatorListListBased.getValue() && this.showInSpectatorListListBased.isVisible();
   }

   private boolean isListNotificationVisible() {
      return this.listbasedModeSettings.isVisible() && this.isVanishNotificationActive();
   }

   private boolean isOtherNotificationVisible() {
      return this.otherModeSettings.isVisible() && this.isVanishNotificationActive();
   }

   private boolean isGommeNotificationVisible() {
      return this.oldgommeModeSettings.isVisible() && this.isVanishNotificationActive();
   }

   private boolean isNormalNotificationVisible() {
      return this.normalModeSettings.isVisible() && this.isVanishNotificationActive();
   }

}
