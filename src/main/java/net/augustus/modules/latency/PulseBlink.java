package net.augustus.modules.latency;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.events.EventRenderEntity;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.render.RenderPassTask;
import net.augustus.utils.network.PacketFlushMarker;
import net.augustus.utils.simulation.EntityMovementPredictor;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.settings.options.PulseBlinkFlushConditionOption;
import net.augustus.utils.world.BlockUtil;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.RenderEntityListener;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class PulseBlink extends Module implements PacketDelayListener, RenderListener, RenderEntityListener, TickListener, PacketListener, MotionListener {
   public final BooleansSetting packetDirection;
   private final RandomizedRangeSettings lagBreakTicks;
   private final RandomizedRangeSettings lagTicks;
   private final BooleansSetting modes;
   private final MultiOptionSetting flushConditions;
   private final DoubleValue targetESPs;
   public final BooleansSetting targetESP;
   private final ColorSetting classicColor;
   private final StringValue lineWidthTarget;
   private final BooleanValue smoothRendering;
   public final BlurShadowSettings target;
   public final TargetSelectionSettings targetSelection;
   private final DoubleValue playerESPs;
   public final BooleansSetting playerESP;
   private final ColorSetting classicColor2;
   private final StringValue lineWidthMe;
   private final BooleanValue onlyInThirdPerson;
   public final BlurShadowSettings player;
   private final ArrayList renderedTargets;
   private int movePacketCounter;
   private boolean delayingPackets;

   public PulseBlink() {
       super("PulseBlink", "Vodafone", Color.black, Categorys.LATENCY);
      this.packetDirection = new BooleansSetting(this, "PacketDirection", "OnlyOutgoing", new String[]{"OnlyIncoming", "OnlyOutgoing", "Both"});
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "LagBreakTicks", 0.0, 0.0, 0.0, 20.0, 0);
      String str = "TickDelayBetweenLags";
      this.lagBreakTicks = ((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str)).withoutNotInARow();
      this.lagTicks = (new RandomizedRangeSettings(this, "LagTicks", 5.0, 6.0, 1.0, 50.0, 0)).withoutNotInARow();
      BooleansSetting booleansSetting = new BooleansSetting(this, "Modes", "DefaultCombat", new String[]{"Default", "DefaultCombat", "CombatOnly"});
      SettingVisibilityCondition condition = () -> this.isCombatModesVisible();
      this.modes = (BooleansSetting)booleansSetting.withVisibility(condition);
      this.flushConditions = new MultiOptionSetting(this, "FlushConditions", PulseBlinkFlushConditionOption.values());
      this.targetESPs = new DoubleValue(this, "TargetESPs");
      this.targetESP = new BooleansSetting(this, "TargetESP", "Classic", new String[]{"None", "Classic", "PlayerModel", "PlayerModelArmor"}, this.targetESPs);
      Color color2 = new Color(0, 255, 0, 255);
      DoubleValue targetESPs2 = this.targetESPs;
      Color color3 = color2;
      String str2 = "ClassicColorTarget";
      ColorSetting colorSetting = new ColorSetting(this, str2, color3, targetESPs2);
      String str3 = "ClassicColor";
      ColorSetting colorSetting2 = (ColorSetting)colorSetting.withDisplayName(str3);
      SettingVisibilityCondition condition2 = () -> this.isTargetClassicColorVisible();
      this.classicColor = (ColorSetting)colorSetting2.withVisibility(condition2);
      StringValue stringValue = new StringValue(this, "LineWidthTarget", 3.0, 0.0, 10.0, 0, this.targetESPs);
      String str4 = "LineWidth";
      StringValue stringValue2 = stringValue.named(str4);
      SettingVisibilityCondition condition3 = () -> {
         return this.targetESP.getSelectedValue().equals("Classic");
      };
      this.lineWidthTarget = stringValue2.visibleWhen(condition3);
      BooleanValue booleanValue = new BooleanValue(this, "SmoothRendering", false, this.targetESPs);
      SettingVisibilityCondition condition4 = () -> this.isSmoothRenderingVisible();
      this.smoothRendering = (BooleanValue)booleanValue.withVisibility(condition4);
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, "Target", this.targetESPs);
      SettingVisibilityCondition condition5 = () -> this.isTargetBlurShadowVisible();
      this.target = ((BlurShadowSettings)blurShadowSettings.withVisibility(condition5)).disableBlurByDefault();
      TargetSelectionSettings targetSelectionSettings = new TargetSelectionSettings(this, TargetFilterPresets.createRenderFilter(), this.targetESPs);
      SettingVisibilityCondition condition6 = () -> {
         return !this.targetESP.getSelectedValue().equals("None");
      };
      this.targetSelection = targetSelectionSettings.withVisibility(condition6);
      this.playerESPs = new DoubleValue(this, "PlayerESPs");
      this.playerESP = new BooleansSetting(this, "PlayerESP", "Classic", new String[]{"None", "Classic", "PlayerModel", "PlayerModelArmor"}, this.playerESPs);
      color2 = new Color(0, 255, 0, 255);
      targetESPs2 = this.playerESPs;
      color3 = color2;
      str2 = "ClassicColorMe";
      ColorSetting colorSetting3 = new ColorSetting(this, str2, color3, targetESPs2);
      String str5 = "ClassicColor";
      ColorSetting colorSetting4 = (ColorSetting)colorSetting3.withDisplayName(str5);
      SettingVisibilityCondition condition7 = () -> this.isPlayerClassicColorVisible();
      this.classicColor2 = (ColorSetting)colorSetting4.withVisibility(condition7);
      StringValue stringValue3 = new StringValue(this, "LineWidthMe", 3.0, 0.0, 10.0, 0, this.playerESPs);
      String str6 = "LineWidth";
      StringValue stringValue4 = stringValue3.named(str6);
      SettingVisibilityCondition condition8 = () -> this.isPlayerLineWidthVisible();
      this.lineWidthMe = stringValue4.visibleWhen(condition8);
      this.onlyInThirdPerson = new BooleanValue(this, "OnlyInThirdPerson", true, this.playerESPs);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this, "Player", this.playerESPs);
      SettingVisibilityCondition condition9 = () -> {
         return !this.playerESP.getSelectedValue().equals("None");
      };
      this.player = ((BlurShadowSettings)blurShadowSettings2.withVisibility(condition9)).disableBlurByDefault();
      this.renderedTargets = new ArrayList();
      this.movePacketCounter = 0;
      this.delayingPackets = true;
   }

   @Override
   public void onEnable() {
      this.rerollLagDurations();
      this.delayingPackets = false;
   }

   public static void noopHook() {
   }

   @Override
   public void onDisable() {
      this.renderedTargets.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 25;
      this.toggleEventSubscription(b);
      byte b2 = 32;
      this.toggleEventSubscription(b2);
      byte b3 = 31;
      this.toggleEventSubscription(b3);
      byte b4 = 40;
      this.toggleEventSubscription(b4);
      byte b5 = 26;
      this.toggleEventSubscription(b5);
      byte b6 = 18;
      this.toggleEventSubscription(b6);
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
   public void onMotion(EventMotion event) {
      if (mc.thePlayer != null) {
         if (PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.SPRINT_RESET) && mc.thePlayer.isSprinting() != mc.thePlayer.isServerSprintState()) {
            this.flushAndResetPulse();
         }

         if (PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.SPLASH_POTION)) {
            mc.theWorld.loadedEntityList.stream().filter(obj -> obj instanceof EntityPotion).forEach(obj2 -> this.checkNearbySplashPotion((EntityPotion)obj2));
         }
      }
   }

   @Override
   public void onTick3() {
      if (mc.thePlayer != null
         && (!PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.STANDING_STILL) || MoveUtil.isMoving())
         && (!BlockUtil.isScaffoldActiveAndValid() || !PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.SCAFFOLD_TOGGLED))) {
         if (this.delayingPackets) {
            if (this.movePacketCounter >= this.lagTicks.getLastIntValue()) {
               this.delayingPackets = false;
               if (mc.getNetHandler() != null && mc.getNetHandler().getNetworkManager() != null) {
                  mc.getNetHandler().getNetworkManager().receivePacketAsync(new PacketFlushMarker());
               }

               this.rerollLagDurations();
            }
         } else if (this.movePacketCounter >= this.lagBreakTicks.getLastIntValue()) {
            this.delayingPackets = true;
            this.rerollLagDurations();
         }

         if (this.packetDirection.getSelectedValue().equals("OnlyOutgoing")) {
            boolean flag = this.shouldFlushForCombatMode();
            if (flag) {
               if (mc.getNetHandler() != null && mc.getNetHandler().getNetworkManager() != null) {
                  mc.getNetHandler().getNetworkManager().receivePacketAsync(new PacketFlushMarker());
               }

               this.flushAndResetPulse();
            }
         }
      } else {
         this.delayingPackets = false;
         this.movePacketCounter = 0;
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C03PacketPlayer) {
         this.movePacketCounter++;
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      EntityPlayerSP thePlayer = mc.thePlayer;
      if (!this.packetDirection.getSelectedValue().equals("OnlyIncoming") && thePlayer != null && mc.theWorld != null) {
         Packet packet = event.getPacket();
         if (packet instanceof C02PacketUseEntity && !this.modes.getSelectedValue().equals("Default") && this.packetDirection.getSelectedValue().equals("OnlyOutgoing") && this.delayingPackets) {
            this.flushAndResetPulse();
         }

         if (PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.INVENTORY_ACTIONS) && (packet instanceof C0EPacketClickWindow || packet instanceof C0DPacketCloseWindow)) {
            this.flushAndResetPulse();
         }

         if (PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.BREAK_BLOCKS) && packet instanceof C07PacketPlayerDigging) {
            this.flushAndResetPulse();
         }

         if (PulseBlinkFlushConditionOption.USE_ITEM.isSelected() && thePlayer.isUsingItem()) {
            this.flushAndResetPulse();
         }

         if (PulseBlinkFlushConditionOption.OPEN_GUI.isSelected() && !mc.isNotInGui()) {
            this.flushAndResetPulse();
         }

         if (PulseBlinkFlushConditionOption.SLOT_SWITCH.isSelected() && packet instanceof C09PacketHeldItemChange) {
            this.flushAndResetPulse();
         }

         if (packet instanceof C08PacketPlayerBlockPlacement && PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.PLACE_BLOCK)) {
            C08PacketPlayerBlockPlacement packet2 = (C08PacketPlayerBlockPlacement)packet;
            if (packet2.getPlacedBlockOffsetX() != 0.0F
               && packet2.getPlacedBlockOffsetY() != 0.0F
               && packet2.getPlacedBlockOffsetZ() != 0.0F
               && packet2.getPlacedBlockDirection() != 255) {
               this.flushAndResetPulse();
            }
         }

         if (this.delayingPackets) {
            boolean flag = true;
            event.setDelayed(flag);
         }
      }
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      Packet packet = event.getPacket();
      EntityPlayerSP thePlayer = mc.thePlayer;
      if (PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.VELOCITY) && packet instanceof S12PacketEntityVelocity) {
         S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
         if (thePlayer != null && packet2.getEntityID() == thePlayer.getEntityId()) {
            this.flushAndResetPulse();
         }
      }

      if (PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.EXPLOSION) && packet instanceof S27PacketExplosion) {
         S27PacketExplosion packet3 = (S27PacketExplosion)packet;
         if (packet3.getMotionX() != 0.0F || packet3.getMotionY() != 0.0F || packet3.getMotionZ() != 0.0F) {
            this.flushAndResetPulse();
         }
      }

      if (PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.TELEPORT) && packet instanceof S08PacketPlayerPosLook) {
         this.flushAndResetPulse();
      }

      if (PulseBlinkFlushConditionOption.isOptionSelected(PulseBlinkFlushConditionOption.WORLD_CHANGE) && packet instanceof S07PacketRespawn) {
         this.flushAndResetPulse();
      }

      if (!this.packetDirection.getSelectedValue().equals("OnlyOutgoing") && thePlayer != null && mc.theWorld != null) {
         if (this.delayingPackets) {
            boolean flag = true;
            event.setDelayed(flag);
         }
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

         if (ClientManagers.moduleManager.packetHandler.hasQueuedOutgoingPackets()
            && this.playerESP.getSelectedValue().equals("Classic")
            && mc.thePlayer != null
            && (mc.gameSettings.thirdPersonView != 0 || !this.onlyInThirdPerson.getValue())) {
            boolean isBlurActive2 = this.player.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive2, this.player.isShadowActive());
            RenderPassTask renderPassTask = shaderRenderPass2 -> this.renderClassicPlayerBox(shaderRenderPass2);
            boolean flag = false;
            BlurShadowManager.submitRenderTask(flag, renderPassTask);
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
            boolean flag2 = flag;
            this.renderTargetModel(event, entity, flag2);
         }
      }

      if (mc.thePlayer != null && ClientManagers.moduleManager.packetHandler.hasQueuedOutgoingPackets() && mc.thePlayer.equals(event.getEntity())) {
         BooleansSetting playerESP2 = this.playerESP;
         if (this.isPlayerModelEspMode(playerESP2) && (mc.gameSettings.thirdPersonView != 0 || !this.onlyInThirdPerson.getValue())) {
            Vec3 serverPos = mc.thePlayer.getServerPos();
            boolean flag3 = this.playerESP.getSelectedValue().equals("PlayerModelArmor");
            this.renderPlayerModel(event, serverPos, flag3);
         }
      }
   }

   private boolean shouldFlushForCombatMode() {
      EntityLivingBase currentCombatTarget = this.getCurrentCombatTarget();
      boolean flag;
      if (this.modes.getSelectedValue().equals("DefaultCombat")) {
         flag = this.shouldFlushDefaultCombat(currentCombatTarget);
      } else {
         if (this.modes.getSelectedValue().equals("CombatOnly")) {
            if (this.shouldFlushCombatOnly(currentCombatTarget)) {
               return true;
            }
         }

         flag = false;
      }

      return flag;
   }

   private boolean shouldFlushDefaultCombat(EntityLivingBase entity) {
      if (entity instanceof EntityPlayer) {
         if (!EntityMovementPredictor.hasInvalidPositionHistory(entity)) {
            EntityPlayer player2 = (EntityPlayer)entity;
            float collisionBorderSize = player2.getCollisionBorderSize();
            AxisAlignedBB box = player2.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
            Vec3 vec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            Vec3 nearestPointOnEntity = RotationUtil.getNearestPointOnEntity(player2);
            double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
            Vec3 vec2 = new Vec3(doubles[0], doubles[1] + (double)mc.thePlayer.getEyeHeight(), doubles[2]);
            Vec3 vec3 = RotationUtil.clampPointToBox(vec2, box);
            if (vec2.distanceTo(vec3) < vec.distanceTo(nearestPointOnEntity) && mc.thePlayer.getDistanceToEntityBox(player2) > 3.0F) {
               if (!EntityMovementPredictor.hasInvalidPositionHistory(player2)) {
                  for (int i = this.lagTicks.getLastIntValue(); i > 0; i--) {
                     double[] doubles2 = PlayerUtil.predictPlayerPosition(i - 1, mc.thePlayer);
                     Vec3 vec4 = EntityMovementPredictor.predictConstantHorizontalPosition(player2, i);
                     Vec3 vec5 = new Vec3(doubles2[0], doubles2[1] + (double)mc.thePlayer.getEyeHeight(), doubles2[2]);
                     float f = player2.width / 2.0F;
                     AxisAlignedBB box2 = new AxisAlignedBB(
                           vec4.xCoord - (double)f,
                           vec4.yCoord,
                           vec4.zCoord - (double)f,
                           vec4.xCoord + (double)f,
                           vec4.yCoord + (double)player2.height,
                           vec4.zCoord + (double)f
                        )
                        .expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
                     Vec3 vec6 = RotationUtil.clampPointToBox(vec5, box2);
                     if (vec5.distanceTo(vec6) < 3.0) {
                        return false;
                     }
                  }
               }
            }

            return true;
         }
      }

      for (Entity entity2 : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
         if (entity2 instanceof EntityPlayer) {
            EntityPlayer player3 = (EntityPlayer)entity2;
            if (!(player3 instanceof EntityPlayerSP) && !ClientManagers.moduleManager.midClick.isFriend(player3)) {
               if (!ClientManagers.moduleManager.antiBot.isBot(player3)
                  && !ClientManagers.moduleManager.teams.isTeammate(player3)
                  && mc.thePlayer.getPrevServerPos().distanceTo(player3.posX, player3.posY, player3.posZ) < 6.1) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean shouldFlushCombatOnly(EntityLivingBase entity) {
      if (entity instanceof EntityPlayer) {
         if (!EntityMovementPredictor.hasInvalidPositionHistory(entity)) {
            EntityPlayer player2 = (EntityPlayer)entity;
            float collisionBorderSize = player2.getCollisionBorderSize();
            AxisAlignedBB box = player2.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
            Vec3 vec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            Vec3 nearestPointOnEntity = RotationUtil.getNearestPointOnEntity(player2);
            double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
            Vec3 vec2 = new Vec3(doubles[0], doubles[1] + (double)mc.thePlayer.getEyeHeight(), doubles[2]);
            Vec3 vec3 = RotationUtil.clampPointToBox(vec2, box);
            if (vec2.distanceTo(vec3) < vec.distanceTo(nearestPointOnEntity) && mc.thePlayer.getDistanceToEntityBox(player2) > 3.0F) {
               for (int i = this.lagTicks.getLastIntValue(); i > 0; i--) {
                  double[] doubles2 = PlayerUtil.predictPlayerPosition(i - 1, mc.thePlayer);
                  Vec3 vec4 = EntityMovementPredictor.predictConstantHorizontalPosition(player2, i);
                  Vec3 vec5 = new Vec3(doubles2[0], doubles2[1] + (double)mc.thePlayer.getEyeHeight(), doubles2[2]);
                  float f = player2.width / 2.0F;
                  AxisAlignedBB box2 = new AxisAlignedBB(
                        vec4.xCoord - (double)f,
                        vec4.yCoord,
                        vec4.zCoord - (double)f,
                        vec4.xCoord + (double)f,
                        vec4.yCoord + (double)player2.height,
                        vec4.zCoord + (double)f
                     )
                     .expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
                  Vec3 vec6 = RotationUtil.clampPointToBox(vec5, box2);
                  if (vec5.distanceTo(vec6) < 3.0) {
                     return false;
                  }
               }
            }

            return true;
         }
      }

      return true;
   }

   private EntityLivingBase getCurrentCombatTarget() {
      return CombatTargetUtil.getActiveTarget();
   }

   private boolean isValidEspTarget(Entity entity) {
      return this.targetSelection.getTargetFilter().matches(entity);
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

   private void rerollLagDurations() {
      this.lagTicks.nextValue();
      this.lagBreakTicks.nextValue();
      this.movePacketCounter = 0;
   }

   private void flushAndResetPulse() {
      this.delayingPackets = false;
      this.movePacketCounter = 0;
      this.rerollLagDurations();
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

   private void checkNearbySplashPotion(Entity entity) {
      double distanceToEntity = (double)mc.thePlayer.getDistanceToEntity(entity);
      double distance2DToEntity = (double)mc.thePlayer.getDistance2DToEntity(entity);
      if (distanceToEntity < 2.0 && this.isLicenseTimestampValid() && distance2DToEntity < 1.0) {
         this.flushAndResetPulse();
      }
   }

   private boolean isPlayerLineWidthVisible() {
      return this.playerESP.getSelectedValue().equals("Classic");
   }

   private boolean isPlayerClassicColorVisible() {
      return this.playerESP.getSelectedValue().equals("Classic");
   }

   private boolean isTargetBlurShadowVisible() {
      return !this.targetESP.getSelectedValue().equals("None");
   }

   private boolean isSmoothRenderingVisible() {
      return !this.targetESP.getSelectedValue().equals("None");
   }

   private boolean isTargetClassicColorVisible() {
      return this.targetESP.getSelectedValue().equals("Classic");
   }

   private boolean isCombatModesVisible() {
      return this.packetDirection.getSelectedValue().equals("OnlyOutgoing");
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
