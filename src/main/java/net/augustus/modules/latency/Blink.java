package net.augustus.modules.latency;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.misc.Debug;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.events.EventRenderEntity;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.ClientManagers;
import net.augustus.settings.ColorSetting;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.RenderEntityListener;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

public class Blink extends Module implements PacketDelayListener, PacketListener, RenderListener, RenderEntityListener, TickListener {
   public final BooleansSetting packetDirection;
   public final StringValue autoDisable;
   public final BooleanValue disableOnAttack;
   public final BooleanValue disableKillAura;
   private final BooleanValue disableAtSetback;
   public DoubleValue targetESPs;
   public final BooleansSetting targetESP;
   private final ColorSetting classicColor;
   private final StringValue lineWidthTarget;
   private final BooleanValue smoothRendering;
   public final BlurShadowSettings target;
   public final TargetSelectionSettings targetSelection;
   public DoubleValue playerESPs;
   public final BooleansSetting playerESP;
   private final ColorSetting classicColor2;
   private final StringValue lineWidthMe;
   public final BlurShadowSettings player;
   private final ArrayList renderedTargets;
   private int queuedMovePackets;
   public long enableTimestampMs;

   public Blink() {
       super("Blink", "Laag", new Color(75, 5, 161), Categorys.LATENCY);
      this.packetDirection = new BooleansSetting(this, "PacketDirection", "OnlyOutgoing", new String[]{"OnlyIncoming", "OnlyOutgoing", "Both"});
      this.autoDisable = new StringValue(this, "AutoDisable", 0.0, 0.0, 100.0, 0);
      this.disableOnAttack = new BooleanValue(this, "DisableOnAttack", false);
      this.disableKillAura = new BooleanValue(this, "DisableKillAura", false);
      this.disableAtSetback = new BooleanValue(this, "DisableAtSetback", false);
      this.targetESPs = new DoubleValue(this, "TargetESPs");
      this.targetESP = new BooleansSetting(this, "TargetESP", "Classic", new String[]{"None", "Classic", "PlayerModel", "PlayerModelArmor"}, this.targetESPs);
      Color color2 = new Color(0, 255, 0, 255);
      DoubleValue targetESPs2 = this.targetESPs;
      Color color3 = color2;
      String str = "ClassicColorTarget";
      ColorSetting colorSetting = new ColorSetting(this, str, color3, targetESPs2);
      String str2 = "ClassicColor";
      ColorSetting colorSetting2 = (ColorSetting)colorSetting.withDisplayName(str2);
      SettingVisibilityCondition condition = () -> this.isTargetClassicColorVisible();
      this.classicColor = (ColorSetting)colorSetting2.withVisibility(condition);
      StringValue stringValue = new StringValue(this, "LineWidthTarget", 3.0, 0.0, 10.0, 0, this.targetESPs);
      String str3 = "LineWidth";
      StringValue stringValue2 = stringValue.named(str3);
      SettingVisibilityCondition condition2 = () -> this.isTargetLineWidthVisible();
      this.lineWidthTarget = stringValue2.visibleWhen(condition2);
      BooleanValue booleanValue = new BooleanValue(this, "SmoothRendering", false, this.targetESPs);
      SettingVisibilityCondition condition3 = () -> {
         return !this.targetESP.getSelectedValue().equals("None");
      };
      this.smoothRendering = (BooleanValue)booleanValue.withVisibility(condition3);
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, "Target", this.targetESPs);
      SettingVisibilityCondition condition4 = () -> {
         return !this.targetESP.getSelectedValue().equals("None");
      };
      this.target = ((BlurShadowSettings)blurShadowSettings.withVisibility(condition4)).disableBlurByDefault();
      TargetSelectionSettings targetSelectionSettings = new TargetSelectionSettings(this, TargetFilterPresets.createRenderFilter(), this.targetESPs);
      SettingVisibilityCondition condition5 = () -> this.isTargetSelectionVisible();
      this.targetSelection = targetSelectionSettings.withVisibility(condition5);
      this.playerESPs = new DoubleValue(this, "PlayerESPs");
      this.playerESP = new BooleansSetting(this, "PlayerESP", "Classic", new String[]{"None", "Classic", "PlayerModel", "PlayerModelArmor"}, this.playerESPs);
      color2 = new Color(0, 255, 0, 255);
      targetESPs2 = this.playerESPs;
      color3 = color2;
      str = "ClassicColorMe";
      ColorSetting colorSetting3 = new ColorSetting(this, str, color3, targetESPs2);
      String str4 = "ClassicColor";
      ColorSetting colorSetting4 = (ColorSetting)colorSetting3.withDisplayName(str4);
      SettingVisibilityCondition condition6 = () -> this.isPlayerClassicColorVisible();
      this.classicColor2 = (ColorSetting)colorSetting4.withVisibility(condition6);
      StringValue stringValue3 = new StringValue(this, "LineWidthMe", 3.0, 0.0, 10.0, 0, this.playerESPs);
      String str5 = "LineWidth";
      StringValue stringValue4 = stringValue3.named(str5);
      SettingVisibilityCondition condition7 = () -> this.isPlayerLineWidthVisible();
      this.lineWidthMe = stringValue4.visibleWhen(condition7);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this, "Player", this.playerESPs);
      SettingVisibilityCondition condition8 = () -> this.isPlayerBlurShadowVisible();
      this.player = ((BlurShadowSettings)blurShadowSettings2.withVisibility(condition8)).disableBlurByDefault();
      this.renderedTargets = new ArrayList();
      this.queuedMovePackets = 0;
      this.enableTimestampMs = 0L;
   }

   @Override
   public void onEnable() {
      this.queuedMovePackets = 0;
      this.enableTimestampMs = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
      this.queuedMovePackets = 0;
      if (Augustus.getInstance().getModuleManager() != null && ClientManagers.moduleManager.debug.isToggled() && ClientManagers.moduleManager.debug.blink.getValue()) {
         Debug debug = ClientManagers.moduleManager.debug;
         String str = System.currentTimeMillis() - this.enableTimestampMs + " ";
         debug.sendChatMessage2(str);
      }

      this.renderedTargets.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 25;
      this.toggleEventSubscription(b);
      byte b2 = 26;
      this.toggleEventSubscription(b2);
      byte b3 = 32;
      this.toggleEventSubscription(b3);
      byte b4 = 31;
      this.toggleEventSubscription(b4);
      byte b5 = 40;
      this.toggleEventSubscription(b5);
   }

   @Override
   public void onTick2() {
      this.renderedTargets.clear();
      EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
      EntityTargetFilter targetFilter = this.targetSelection.getTargetFilter();
      EntityTargetFilter entityTargetFilter = targetFilter;
      entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter);
      Consumer consumer = obj -> this.renderedTargets.add((EntityLivingBase)obj);
      entityTargetSelector.forEach(consumer);
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      if (mc.theWorld == null || mc.thePlayer == null) {
         boolean flag = false;
         this.setToggled(flag);
      }

      if (this.disableAtSetback.getValue() && event.getPacket() instanceof S08PacketPlayerPosLook) {
         PacketThreadUtil.runOnMainThreadImmediate(() -> this.disableOnSetback());
      }

      if (this.packetDirection.getSelectedValue().equals("OnlyIncoming") || this.packetDirection.getSelectedValue().equals("Both")) {
         boolean flag2 = true;
         event.setDelayed(flag2);
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      if (this.packetDirection.getSelectedValue().equals("OnlyOutgoing") || this.packetDirection.getSelectedValue().equals("Both")) {
         if (this.isLicenseTimestampValid()) {
            boolean flag = true;
            event.setDelayed(flag);
         }
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      String str = "" + this.queuedMovePackets;
      this.setSuffix(str);
      if (this.autoDisable.getValue() > 0.0 && (double)this.queuedMovePackets > this.autoDisable.getValue()) {
         this.queuedMovePackets = 0;
         this.toggle();
      }

      if (packet instanceof C03PacketPlayer) {
         this.queuedMovePackets++;
      }

      if (this.disableOnAttack.getValue() && packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)packet).getAction() == C02PacketUseEntity.Action.ATTACK) {
         boolean flag = false;
         this.setToggled(flag);
      }
   }

   @Override
   public void onRender(float f) {
      RenderUtil.begin3DRender();
      if (ClientManagers.moduleManager.packetHandler.isDelayingIncoming() || ClientManagers.moduleManager.packetHandler.hasQueuedOutgoingPackets()) {
         if (this.targetESP.getSelectedValue().equals("Classic") && ClientManagers.moduleManager.packetHandler.isDelayingIncoming()) {
            boolean isBlurActive = this.target.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.target.isShadowActive());
            BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderClassicTargetBoxes(shaderRenderPass));
         }

         if (ClientManagers.moduleManager.packetHandler.hasQueuedOutgoingPackets() && this.playerESP.getSelectedValue().equals("Classic") && mc.thePlayer != null) {
            boolean isBlurActive2 = this.player.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive2, this.player.isShadowActive());
            BlurShadowManager.submitRenderTask(false, shaderRenderPass2 -> this.renderClassicPlayerBox(shaderRenderPass2));
         }
      }

      RenderUtil.end3DRender();
   }

   @Override
   public void onRenderEntity(EventRenderEntity event) {
      EntityLivingBase entity = event.getEntity();
      if (ClientManagers.moduleManager.packetHandler.isDelayingIncoming() && this.isValidEspTarget(entity)) {
         BooleansSetting targetESP2 = this.targetESP;
         if (this.isPlayerModelEspMode(targetESP2)) {
            boolean flag = this.targetESP.getSelectedValue().equals("PlayerModelArmor");
            this.renderTargetModel(event, entity, flag);
         }
      }

      if (mc.thePlayer != null && ClientManagers.moduleManager.packetHandler.hasQueuedOutgoingPackets() && mc.thePlayer.equals(event.getEntity())) {
         BooleansSetting playerESP2 = this.playerESP;
         if (this.isPlayerModelEspMode(playerESP2)) {
            Vec3 serverPos = mc.thePlayer.getServerPos();
            boolean flag2 = this.playerESP.getSelectedValue().equals("PlayerModelArmor");
            this.renderPlayerModel(event, serverPos, flag2);
         }
      }
   }

   private boolean isValidEspTarget(Entity entity) {
      return entity instanceof EntityLivingBase && this.targetSelection.getTargetFilter().matches(entity);
   }

   private boolean isPlayerModelEspMode(BooleansSetting booleansSetting) {
      return booleansSetting.getSelectedValue().equals("PlayerModel") || booleansSetting.getSelectedValue().equals("PlayerModelArmor");
   }

   public void renderTargetModel(EventRenderEntity event, EntityLivingBase entity, boolean flag) {
      double d = entity.prevInterpolatedServerPos.xCoord + (entity.interpolatedServerPos.xCoord - entity.prevInterpolatedServerPos.xCoord) * (double)mc.getTimer().renderPartialTicks;
      double d2 = entity.prevInterpolatedServerPos.yCoord + (entity.interpolatedServerPos.yCoord - entity.prevInterpolatedServerPos.yCoord) * (double)mc.getTimer().renderPartialTicks;
      double d3 = entity.prevInterpolatedServerPos.zCoord + (entity.interpolatedServerPos.zCoord - entity.prevInterpolatedServerPos.zCoord) * (double)mc.getTimer().renderPartialTicks;
      boolean isBlurActive = this.target.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, this.target.isShadowActive());
      RendererLivingEntity renderer = event.getRenderer();
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> renderTargetModelPass(event, entity, renderer, d, d2, d3, flag, shaderRenderPass));
   }

   public void renderPlayerModel(EventRenderEntity event, Vec3 vec, boolean flag) {
      boolean isBlurActive = this.player.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, this.player.isShadowActive());
      RendererLivingEntity renderer = event.getRenderer();
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> renderPlayerModelPass(event, renderer, vec, flag, shaderRenderPass));
   }

   private static void renderPlayerModelPass(EventRenderEntity event, RendererLivingEntity rendererLivingEntity, Vec3 vec, boolean flag, ShaderRenderPass shaderRenderPass) {
      double zCoord = vec.zCoord;
      double yCoord = vec.yCoord;
      double xCoord = vec.xCoord;
      EntityPlayerSP thePlayer = mc.thePlayer;
      event.renderEntityModel(thePlayer, rendererLivingEntity, xCoord, yCoord, zCoord, flag);
   }

   private static void renderTargetModelPass(
      EventRenderEntity event, EntityLivingBase entity, RendererLivingEntity rendererLivingEntity, double d, double d2, double d3, boolean flag, ShaderRenderPass shaderRenderPass
   ) {
      event.renderEntityModel(entity, rendererLivingEntity, d, d2, d3, flag);
   }

   private void renderClassicPlayerBox(ShaderRenderPass shaderRenderPass) {
      EntityPlayerSP thePlayer = mc.thePlayer;
      ColorSetting classicColor22 = this.classicColor2;
      float value = (float)this.lineWidthMe.getValue();
      boolean flag = false;
      short s = 255;
      float f = value;
      ColorSetting colorSetting = classicColor22;
      RenderUtil.drawEntityEspBox(thePlayer, colorSetting, f, s, flag);
   }

   private void renderClassicTargetBoxes(ShaderRenderPass shaderRenderPass) {

      for (EntityLivingBase entity : (Iterable<EntityLivingBase>)(this.renderedTargets)) {
         if (!(entity instanceof EntityPlayerSP)) {
            ColorSetting classicColor3 = this.classicColor;
            float value = (float)this.lineWidthTarget.getValue();
            boolean value2 = this.smoothRendering.getValue();
            short s = 255;
            float f = value;
            ColorSetting colorSetting = classicColor3;
            RenderUtil.drawEntityEspBox(entity, colorSetting, f, s, value2);
            boolean isBlurActive = this.target.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.target.isShadowActive());
         }
      }
   }

   private void disableOnSetback() {
      boolean flag = false;
      this.setToggled(flag);
   }

   private boolean isPlayerBlurShadowVisible() {
      return !this.playerESP.getSelectedValue().equals("None");
   }

   private boolean isPlayerLineWidthVisible() {
      return this.playerESP.getSelectedValue().equals("Classic");
   }

   private boolean isPlayerClassicColorVisible() {
      return this.playerESP.getSelectedValue().equals("Classic");
   }

   private boolean isTargetSelectionVisible() {
      return !this.targetESP.getSelectedValue().equals("None");
   }

   private boolean isTargetLineWidthVisible() {
      return this.targetESP.getSelectedValue().equals("Classic");
   }

   private boolean isTargetClassicColorVisible() {
      return this.targetESP.getSelectedValue().equals("Classic");
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
