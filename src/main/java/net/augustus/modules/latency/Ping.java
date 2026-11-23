package net.augustus.modules.latency;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.render.Projectiles;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.events.EventRenderEntity;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.render.RenderPassTask;
import net.augustus.utils.network.PacketFlushMarker;
import net.augustus.events.listeners.AsyncTickListener;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.MotionListener;
import net.augustus.events.EventClick;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.MoveUtil;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.utils.world.BlockUtil;
import net.augustus.utils.rotation.EntityBoundsUtil;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.RenderEntityListener;
import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.options.PingFlushConditionOption;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemBlock;
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

public class Ping extends Module implements PacketDelayListener, LoadWorldListener, AttackEntityListener, RenderListener, RenderEntityListener, TickListener, MotionListener, ClickListener, AsyncTickListener {
   private final DoubleValue incomingGroup;
   private final PingDelayController incomingController;
   private final DoubleValue outgoingGroup;
   private final PingDelayController outgoingController;
   private final BooleanValue combatMode;
   private final RandomizedRangeSettings pingStartDelay;
   private final StringValue resetPingAtDistanceToTarget;
   private final BooleanValue failRate;
   private final RandomizedRangeSettings failInterval;
   private final BooleanValue limitDistanceToTarget;
   private final StringValue maxDistanceToTarget;
   public final TargetSelectionSettings combatTargetSelection;
   private final DoubleValue flushConditionsGroup;
   private final MultiOptionSetting flushConditions;
   private final DoubleValue targetESPs;
   public final BooleansSetting targetESP;
   private final ColorSetting classicColor;
   private final StringValue lineWidthTarget;
   private final BooleanValue smoothRendering;
   public final BlurShadowSettings target;
   public final TargetSelectionSettings render;
   private final DoubleValue playerESPs;
   public final BooleansSetting playerESP;
   private final ColorSetting classicColor2;
   private final StringValue lineWidthMe;
   public final BlurShadowSettings player;
   private final BooleanValue onlyInThirdPerson;
   private final TimeHelper flushMarkerTimer;
   private final List renderedTargets;
   private final AtomicBoolean combatPaused;
   private final TimeHelper combatStartTimer;
   private final AtomicBoolean outgoingDelayedSinceAttack;
   private int failHitCounter;
   private volatile EntityLivingBase lastAttackedTarget;
   private final TimeHelper targetTimeoutTimer;
   private final AtomicLong lastOutgoingDelayTimeMs;
   private final AtomicBoolean hurtTimeCounted;
   private volatile boolean targetMovedAway;
   private volatile boolean incomingProjectileDetected;

   public Ping() {
       super("Ping", "Verzögerung", new Color(235, 64, 52), Categorys.LATENCY);
      this.incomingGroup = new DoubleValue(this, "IncomingGroup");
      this.incomingController = new PingDelayController(this, "Incoming", this.incomingGroup);
      this.outgoingGroup = new DoubleValue(this, "OutgoingGroup");
      this.outgoingController = new PingDelayController(this, "Outgoing", this.outgoingGroup);
      this.combatMode = new BooleanValue(this, "CombatMode", false, this.outgoingGroup);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "PingStartDelay", 100.0, 150.0, 0.0, 1000.0, 0, this.outgoingGroup)).withoutNotInARow();
      BooleanValue combatMode2 = this.combatMode;
      this.combatMode.getClass();
      BooleanValue booleanValue = combatMode2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.pingStartDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      StringValue stringValue = new StringValue(this, "ResetPingAtDistanceToTarget", 3.05, 3.0, 4.0, 2, this.outgoingGroup);
      combatMode2 = this.combatMode;
      this.combatMode.getClass();
      BooleanValue booleanValue2 = combatMode2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.resetPingAtDistanceToTarget = stringValue.visibleWhen(condition2);
      BooleanValue booleanValue3 = new BooleanValue(this, "FailRate", false, this.outgoingGroup);
      combatMode2 = this.combatMode;
      this.combatMode.getClass();
      BooleanValue booleanValue4 = combatMode2;
      SettingVisibilityCondition condition3 = () -> booleanValue4.getValue();
      this.failRate = (BooleanValue)booleanValue3.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "FailInterval", 3.0, 4.0, 0.0, 10.0, 0, this.outgoingGroup)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> this.isFailIntervalVisible();
      this.failInterval = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition4);
      this.limitDistanceToTarget = new BooleanValue(this, "LimitDistanceToTarget", false, this.outgoingGroup);
      this.maxDistanceToTarget = new StringValue(this, "MaxDistanceToTarget", 6.0, 3.0, 15.0, 1, this.outgoingGroup);
      TargetSelectionSettings targetSelectionSettings = new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter(), this.outgoingGroup);
      combatMode2 = this.combatMode;
      this.combatMode.getClass();
      BooleanValue booleanValue5 = combatMode2;
      SettingVisibilityCondition condition5 = () -> booleanValue5.getValue();
      this.combatTargetSelection = targetSelectionSettings.withVisibility(condition5);
      DoubleValue doubleValue = new DoubleValue(this, "FlushConditionsGroup");
      String str = "FlushConditions";
      this.flushConditionsGroup = (DoubleValue)doubleValue.withDisplayName(str);
      this.flushConditions = new MultiOptionSetting(this, "FlushConditions", PingFlushConditionOption.values(), this.flushConditionsGroup);
      this.targetESPs = new DoubleValue(this, "TargetESPs");
      this.targetESP = new BooleansSetting(this, "TargetESP", "Classic", new String[]{"None", "Classic", "PlayerModel", "PlayerModelArmor"}, this.targetESPs);
      Color color2 = new Color(0, 255, 0, 255);
      DoubleValue targetESPs2 = this.targetESPs;
      Color color3 = color2;
      String str2 = "ClassicColorTarget";
      ColorSetting colorSetting = new ColorSetting(this, str2, color3, targetESPs2);
      String str3 = "ClassicColor";
      ColorSetting colorSetting2 = (ColorSetting)colorSetting.withDisplayName(str3);
      SettingVisibilityCondition condition6 = () -> this.isTargetClassicColorVisible();
      this.classicColor = (ColorSetting)colorSetting2.withVisibility(condition6);
      StringValue stringValue2 = new StringValue(this, "LineWidthTarget", 3.0, 0.0, 10.0, 0, this.targetESPs);
      String str4 = "LineWidth";
      StringValue stringValue3 = stringValue2.named(str4);
      SettingVisibilityCondition condition7 = () -> this.isTargetLineWidthVisible();
      this.lineWidthTarget = stringValue3.visibleWhen(condition7);
      combatMode2 = new BooleanValue(this, "SmoothRendering", false, this.targetESPs);
      SettingVisibilityCondition condition8 = () -> this.isSmoothRenderingVisible();
      this.smoothRendering = (BooleanValue)combatMode2.withVisibility(condition8);
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, "Target", this.targetESPs);
      SettingVisibilityCondition condition9 = () -> this.isTargetBlurShadowVisible();
      this.target = ((BlurShadowSettings)blurShadowSettings.withVisibility(condition9)).disableBlurByDefault();
      TargetSelectionSettings targetSelectionSettings2 = new TargetSelectionSettings(this, "Render", TargetFilterPresets.createRenderFilter(), this.targetESPs);
      SettingVisibilityCondition condition10 = () -> {
         return !this.targetESP.getSelectedValue().equals("None");
      };
      this.render = targetSelectionSettings2.withVisibility(condition10);
      this.playerESPs = new DoubleValue(this, "PlayerESPs");
      this.playerESP = new BooleansSetting(this, "PlayerESP", "Classic", new String[]{"None", "Classic", "PlayerModel", "PlayerModelArmor"}, this.playerESPs);
      color2 = new Color(0, 255, 0, 255);
      targetESPs2 = this.playerESPs;
      color3 = color2;
      str2 = "ClassicColorMe";
      ColorSetting colorSetting3 = new ColorSetting(this, str2, color3, targetESPs2);
      String str5 = "ClassicColor";
      ColorSetting colorSetting4 = (ColorSetting)colorSetting3.withDisplayName(str5);
      SettingVisibilityCondition condition11 = () -> this.isPlayerClassicColorVisible();
      this.classicColor2 = (ColorSetting)colorSetting4.withVisibility(condition11);
      StringValue stringValue4 = new StringValue(this, "LineWidthMe", 3.0, 0.0, 10.0, 0, this.playerESPs);
      String str6 = "LineWidth";
      StringValue stringValue5 = stringValue4.named(str6);
      SettingVisibilityCondition condition12 = () -> this.isPlayerLineWidthVisible();
      this.lineWidthMe = stringValue5.visibleWhen(condition12);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this, "Player", this.playerESPs);
      SettingVisibilityCondition condition13 = () -> this.isPlayerBlurShadowVisible();
      this.player = ((BlurShadowSettings)blurShadowSettings2.withVisibility(condition13)).disableBlurByDefault();
      this.onlyInThirdPerson = new BooleanValue(this, "OnlyInThirdPerson", true, this.playerESPs);
      this.flushMarkerTimer = new TimeHelper();
      this.renderedTargets = new ArrayList();
      this.combatPaused = new AtomicBoolean(false);
      this.combatStartTimer = new TimeHelper();
      this.outgoingDelayedSinceAttack = new AtomicBoolean(false);
      this.failHitCounter = 0;
      this.lastAttackedTarget = null;
      this.targetTimeoutTimer = new TimeHelper();
      this.lastOutgoingDelayTimeMs = new AtomicLong(System.currentTimeMillis());
      this.hurtTimeCounted = new AtomicBoolean(false);
      this.targetMovedAway = false;
      this.incomingProjectileDetected = false;

      for (PingFlushConditionOption pingFlushConditionOption : PingFlushConditionOption.values()) {
         DoubleValue doubleValue2 = new DoubleValue(this, "Group" + pingFlushConditionOption.getName(), this.flushConditionsGroup);
         String name2 = pingFlushConditionOption.getName();
         doubleValue2 = (DoubleValue)doubleValue2.withDisplayName(name2);
         SettingVisibilityCondition condition14 = pingFlushConditionOption::isSelected;
         DoubleValue doubleValue3 = doubleValue2.visibleWhen(condition14);
         StringValue stringValue6 = new StringValue(this, "FlushDuration" + pingFlushConditionOption.getName(), 200.0, 20.0, 1000.0, 0, doubleValue3);
         String str7 = "FlushCondition";
         StringValue stringValue7 = stringValue6.named(str7);
         PingFlushConditionOption.setFlushDurationSetting(pingFlushConditionOption, stringValue7);
      }
   }

   @Override
   public void onEnable() {
      String str = "Ping";
      this.sendChatMessage2(str);
      this.combatPaused.set(false);
      this.outgoingController.resetAccelerationState();
      this.incomingController.resetAccelerationState();
   }

   @Override
   public void onDisable() {
      String str = "Pong";
      this.sendChatMessage2(str);
      this.renderedTargets.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      Packet packet = event.getPacket();
      this.handleIncomingFlushTriggers(packet);
      if (this.isAnyFlushConditionActive()) {
         int n = this.incomingController.stepDecelerationAndGetDelay();
         event.setIncomingDelayMs(n);
      } else {
         int n2 = this.incomingController.resumeAndGetDelay();
         if (n2 > 0) {
            event.setIncomingDelayMs(n2);
         }
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      exitBlock: {
         Packet packet = event.getPacket();
         Packet packet2 = packet;
         this.handleOutgoingFlushTriggers(packet2);
         if (this.combatMode.getValue()) {
            if (this.combatPaused.get()) {
               break exitBlock;
            }

            TimeHelper combatStartTimer2 = this.combatStartTimer;
            long lastIntValue = (long)this.pingStartDelay.getLastIntValue();
            if (!combatStartTimer2.reached2(lastIntValue)) {
               break exitBlock;
            }
         }

         if (!this.isAnyFlushConditionActive()) {
            int n = this.outgoingController.resumeAndGetDelay();
            if (n > 0) {
               this.lastOutgoingDelayTimeMs.set(System.currentTimeMillis());
               this.hurtTimeCounted.set(false);
               this.targetMovedAway = false;
               event.setOutgoingDelayMs(n);
               this.outgoingDelayedSinceAttack.set(true);
            }

            return;
         }
      }

      int n2 = this.outgoingController.stepDecelerationAndGetDelay();
      event.setOutgoingDelayMs(n2);
   }

   @Override
   public void onAsyncTick() {
      this.incomingController.updateAsync();
      this.outgoingController.updateAsync();
   }

   @Override
   public void onClick(EventClick event) {
      if (PingFlushConditionOption.HOLDING_BLOCKS.isSelected()
         && mc.thePlayer != null
         && mc.thePlayer.getCurrentEquippedItem() != null
         && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
         PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.HOLDING_BLOCKS).reset();
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      if (mc.thePlayer != null) {
         if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.SPRINT_RESET) && mc.thePlayer.isSprinting() != mc.thePlayer.isServerSprintState()) {
            PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.SPRINT_RESET).reset();
         }

         if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.SPLASH_POTION)) {
            Stream stream = mc.theWorld.loadedEntityList.stream().filter(obj -> obj instanceof EntityPotion);
            stream.forEach(obj2 -> {
               double distanceToEntity = (double)mc.thePlayer.getDistanceToEntity((Entity)obj2);
               double distance2DToEntity = (double)mc.thePlayer.getDistance2DToEntity((Entity)obj2);
               if (distanceToEntity < 2.0 && distance2DToEntity < 1.0) {
                  PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.SPLASH_POTION).reset();
               }
            });
         }

         if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.OPEN_GUI) && !mc.isNotInGui()) {
            PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.OPEN_GUI).reset();
         }

         if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.STANDING_STILL) && !MoveUtil.isMoving()) {
            PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.STANDING_STILL).reset();
         }

         if (BlockUtil.isScaffoldActiveAndValid() && PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.SCAFFOLD_TOGGLED)) {
            PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.SCAFFOLD_TOGGLED).reset();
         }

         if (PingFlushConditionOption.USE_ITEM.isSelected() && mc.thePlayer.isUsingItem()) {
            PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.USE_ITEM).reset();
         }

         if (mc.getNetHandler() != null && mc.getNetHandler().getNetworkManager() != null) {
            mc.getNetHandler().getNetworkManager().receivePacketAsync(new PacketFlushMarker());
            mc.getNetHandler().getNetworkManager().sendPacket(new PacketFlushMarker());
         }
      }
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      if (mc.theWorld != null && this.combatMode.getValue() && this.outgoingController.getConfiguredMaxDelay() > 0.0 && event.getTarget() instanceof EntityLivingBase) {
         this.lastAttackedTarget = (EntityLivingBase)event.getTarget();
         this.targetTimeoutTimer.reset();
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.lastAttackedTarget = null;
   }

   @Override
   public void onTick2() {
      this.renderedTargets.clear();
      boolean flag = false;
      long l = 2000L;
      if (this.targetTimeoutTimer.reached2(l)) {
         this.lastAttackedTarget = null;
      }

      EntityLivingBase lastAttackedTarget2 = this.lastAttackedTarget;
      if (this.failRate.getValue() && lastAttackedTarget2 != null) {
         if ((double)mc.thePlayer.getDistanceToEntity(lastAttackedTarget2) > 4.5 && this.failHitCounter >= this.failInterval.getLastIntValue()) {
            this.targetMovedAway = true;
         }

         if (lastAttackedTarget2.hurtTime == 10 && (System.currentTimeMillis() - this.lastOutgoingDelayTimeMs.get() <= 200L && !this.hurtTimeCounted.get() || this.failHitCounter >= this.failInterval.getLastIntValue() && this.targetMovedAway)) {
            this.failHitCounter++;
            this.hurtTimeCounted.set(true);
         }
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.PROJECTILE)) {
         this.incomingProjectileDetected = this.isProjectileHeadingTowardsPlayer();
      }

      boolean flag2 = true;

      for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
         if (this.isRenderTarget(entity)) {
            this.renderedTargets.add((EntityLivingBase)entity);
         }

         if (this.isCombatTarget(entity)) {
            EntityLivingBase entity2 = (EntityLivingBase)entity;
            if (this.outgoingController.getConfiguredMaxDelay() > 0.0 && this.combatMode.getValue()) {
               double distanceToEntityBox = (double)mc.thePlayer.getDistanceToEntityBox(entity2);
               if (distanceToEntityBox < this.resetPingAtDistanceToTarget.getValue()) {
                  if (!this.combatPaused.get()) {
                     this.pingStartDelay.nextInt();
                  }

                  this.combatPaused.set(true);
                  mc.getNetHandler().getNetworkManager().sendPacket(new PacketFlushMarker());
                  this.combatStartTimer.reset();
                  flag = true;
               } else if (mc.thePlayer.getDistanceToEntityBox(entity2) < 15.0F) {
                  Vec3 vec = new Vec3(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.lastTickPosZ);
                  float collisionBorderSize = entity2.getCollisionBorderSize();
                  AxisAlignedBB box = entity2.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
                  Vec3 vec2 = RotationUtil.clampPointToBox(vec, box);
                  boolean flag3 = distanceToEntityBox > vec.distanceTo(vec2);
                  float f = mc.thePlayer.width / 2.0F;
                  float collisionBorderSize2 = mc.thePlayer.getCollisionBorderSize();
                  AxisAlignedBB box2 = new AxisAlignedBB(
                        mc.thePlayer.getServerPos().xCoord - (double)f,
                        mc.thePlayer.getServerPos().yCoord,
                        mc.thePlayer.getServerPos().zCoord - (double)f,
                        mc.thePlayer.getServerPos().xCoord + (double)f,
                        mc.thePlayer.getServerPos().yCoord + (double)mc.thePlayer.height,
                        mc.thePlayer.getServerPos().zCoord + (double)f
                     )
                     .expand((double)collisionBorderSize2, (double)collisionBorderSize2, (double)collisionBorderSize2);
                  Vec3 vec3 = new Vec3(entity2.realPosX, entity2.realPosY, entity2.realPosZ);
                  Vec3 vec4 = RotationUtil.clampPointToBox(vec3, box2);
                  if (flag3 && vec3.distanceTo(vec4) < 5.0) {
                     this.combatPaused.set(true);
                     mc.getNetHandler().getNetworkManager().sendPacket(new PacketFlushMarker());
                     flag = true;
                  }
               }
            }

            if (flag2) {
               flag2 = this.isTargetOutsideMaxDistance(entity2);
            }
         }
      }

      if (!flag) {
         if (this.failRate.getValue()) {
            if (this.failHitCounter == this.failInterval.getLastIntValue()) {
               this.combatPaused.set(true);
               return;
            }

            if (this.failHitCounter > this.failInterval.getLastIntValue()) {
               this.failHitCounter = 0;
               this.failInterval.nextValue();
            }
         }

         this.combatPaused.set(flag2);
      }
   }

   private boolean isTargetOutsideMaxDistance(EntityLivingBase entity) {
      return this.limitDistanceToTarget.getValue() ? (double)mc.thePlayer.getDistanceToEntityBox(entity) >= this.maxDistanceToTarget.getValue() : false;
   }

   private boolean isProjectileHeadingTowardsPlayer() {
      if (this.outgoingController.getCurrentDelayMs() > 0) {
         ArrayList list = new ArrayList();
         EntityPlayerSP thePlayer = mc.thePlayer;
         Vec3 serverPos = thePlayer.getServerPos();
         if (serverPos == null) {
            return false;
         }

         double d = Math.min(serverPos.yCoord, thePlayer.posY);
         Projectiles.simulateLoadedProjectiles(list, d);
         if (!list.isEmpty()) {
            float collisionBorderSize = thePlayer.getCollisionBorderSize();
            AxisAlignedBB box = thePlayer.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
            boolean flag = true;
            AxisAlignedBB box2 = box.union(EntityBoundsUtil.createPlayerBoundingBox(serverPos, flag));

            for (int i = 0; i < list.size(); i++) {
               ArrayList list2 = (ArrayList)list.get(i);

               for (int j = 0; j < list2.size(); j++) {
                  if (box2.isVecInside((Vec3)list2.get(j))) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   @Override
   public void onRender(float f) {
      long l = 3L;
      if (this.flushMarkerTimer.reached2(l)) {
         this.flushMarkerTimer.reset();
         mc.getNetHandler().getNetworkManager().receivePacketAsync(new PacketFlushMarker());
         mc.getNetHandler().getNetworkManager().sendPacket(new PacketFlushMarker());
      }

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
      if (ClientManagers.moduleManager.packetHandler.isDelayingIncoming() && this.isRenderTarget(entity)) {
         BooleansSetting targetESP2 = this.targetESP;
         if (this.isPlayerModelEspMode(targetESP2)) {
            boolean flag = this.targetESP.getSelectedValue().equals("PlayerModelArmor");
            this.renderTargetModel(event, entity, flag);
         }
      }

      if (mc.thePlayer != null && ClientManagers.moduleManager.packetHandler.hasQueuedOutgoingPackets() && mc.thePlayer.equals(event.getEntity())) {
         BooleansSetting playerESP2 = this.playerESP;
         if (this.isPlayerModelEspMode(playerESP2) && (mc.gameSettings.thirdPersonView != 0 || !this.onlyInThirdPerson.getValue())) {
            Vec3 serverPos = mc.thePlayer.getServerPos();
            boolean flag2 = this.playerESP.getSelectedValue().equals("PlayerModelArmor");
            Vec3 vec = serverPos;
            this.renderPlayerModel(event, vec, flag2);
         }
      }
   }

   private void handleOutgoingFlushTriggers(Packet packet) {
      if (this.combatMode.getValue()
         && packet instanceof C02PacketUseEntity
         && ((C02PacketUseEntity)packet).getAction() == C02PacketUseEntity.Action.ATTACK
         && this.outgoingDelayedSinceAttack.compareAndSet(true, false)) {
         this.combatStartTimer.reset();
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.ATTACK) && packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)packet).getAction() == C02PacketUseEntity.Action.ATTACK) {
         PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.ATTACK).reset();
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.INVENTORY_ACTIONS) && (packet instanceof C0EPacketClickWindow || packet instanceof C0DPacketCloseWindow)) {
         PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.INVENTORY_ACTIONS).reset();
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.BREAK_BLOCKS) && packet instanceof C07PacketPlayerDigging) {
         PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.BREAK_BLOCKS).reset();
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.PROJECTILE) && packet instanceof C03PacketPlayer && this.incomingProjectileDetected) {
         PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.PROJECTILE).reset();
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.SLOT_SWITCH) && packet instanceof C09PacketHeldItemChange) {
         PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.SLOT_SWITCH).reset();
      }

      if (packet instanceof C08PacketPlayerBlockPlacement && PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.PLACE_BLOCK)) {
         C08PacketPlayerBlockPlacement packet2 = (C08PacketPlayerBlockPlacement)packet;
         if (packet2.getPlacedBlockOffsetX() != 0.0F
            && packet2.getPlacedBlockOffsetY() != 0.0F
            && packet2.getPlacedBlockOffsetZ() != 0.0F
            && packet2.getPlacedBlockDirection() != 255) {
            PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.PLACE_BLOCK).reset();
         }
      }
   }

   private void handleIncomingFlushTriggers(Packet packet) {
      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.VELOCITY) && packet instanceof S12PacketEntityVelocity) {
         S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
         EntityPlayerSP thePlayer = mc.thePlayer;
         if (thePlayer != null && packet2.getEntityID() == thePlayer.getEntityId()) {
            PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.VELOCITY).reset();
         }
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.EXPLOSION) && packet instanceof S27PacketExplosion) {
         S27PacketExplosion packet3 = (S27PacketExplosion)packet;
         if (packet3.getMotionX() != 0.0F || packet3.getMotionY() != 0.0F || packet3.getMotionZ() != 0.0F) {
            PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.EXPLOSION).reset();
         }
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.TELEPORT) && packet instanceof S08PacketPlayerPosLook) {
         PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.TELEPORT).reset();
      }

      if (PingFlushConditionOption.isOptionSelected(PingFlushConditionOption.WORLD_CHANGE) && packet instanceof S07PacketRespawn) {
         PingFlushConditionOption.getFlushTimer(PingFlushConditionOption.WORLD_CHANGE).reset();
      }
   }

   private boolean isAnyFlushConditionActive() {
      PingFlushConditionOption[] values = PingFlushConditionOption.values();

      for (PingFlushConditionOption pingFlushConditionOption : values) {
         TimeHelper flushTimer = PingFlushConditionOption.getFlushTimer(pingFlushConditionOption);
         double value = PingFlushConditionOption.getFlushDurationSetting(pingFlushConditionOption).getValue();
         if (!flushTimer.reached(value)) {
            return true;
         }
      }

      return false;
   }

   private boolean isCombatTarget(Entity entity) {
      return this.combatTargetSelection.getTargetFilter().matches(entity);
   }

   private boolean isRenderTarget(Entity entity) {
      return this.render.getTargetFilter().matches(entity);
   }

   private boolean isPlayerModelEspMode(BooleansSetting booleansSetting) {
      return booleansSetting.getSelectedValue().equals("PlayerModel") || booleansSetting.getSelectedValue().equals("PlayerModelArmor");
   }

   public void renderTargetModel(EventRenderEntity event, EntityLivingBase entity, boolean flag) {
      double d;
      double d2;
      double d3;
      if (this.smoothRendering.getValue() && entity.interpolatedServerPos != null && entity.prevInterpolatedServerPos != null) {
         d = entity.prevInterpolatedServerPos.xCoord + (entity.interpolatedServerPos.xCoord - entity.prevInterpolatedServerPos.xCoord) * (double)mc.getTimer().renderPartialTicks;
         d2 = entity.prevInterpolatedServerPos.yCoord + (entity.interpolatedServerPos.yCoord - entity.prevInterpolatedServerPos.yCoord) * (double)mc.getTimer().renderPartialTicks;
         d3 = entity.prevInterpolatedServerPos.zCoord + (entity.interpolatedServerPos.zCoord - entity.prevInterpolatedServerPos.zCoord) * (double)mc.getTimer().renderPartialTicks;
      } else {
         d = entity.prevRawServerPos.xCoord + (entity.rawServerPos.xCoord - entity.prevRawServerPos.xCoord) * (double)mc.getTimer().renderPartialTicks;
         d2 = entity.prevRawServerPos.yCoord + (entity.rawServerPos.yCoord - entity.prevRawServerPos.yCoord) * (double)mc.getTimer().renderPartialTicks;
         d3 = entity.prevRawServerPos.zCoord + (entity.rawServerPos.zCoord - entity.prevRawServerPos.zCoord) * (double)mc.getTimer().renderPartialTicks;
      }

      boolean isBlurActive = this.target.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, this.target.isShadowActive());
      RendererLivingEntity renderer = event.getRenderer();
      double d4 = d3;
      double d5 = d2;
      double d6 = d;
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> renderTargetModelPass(event, entity, renderer, d6, d5, d4, flag, shaderRenderPass));
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
      boolean flag = true;
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

   private boolean isPlayerBlurShadowVisible() {
      return !this.playerESP.getSelectedValue().equals("None");
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

   private boolean isTargetLineWidthVisible() {
      return this.targetESP.getSelectedValue().equals("Classic");
   }

   private boolean isTargetClassicColorVisible() {
      return this.targetESP.getSelectedValue().equals("Classic");
   }

   private boolean isFailIntervalVisible() {
      return this.failRate.getValue() && this.failRate.isVisible();
   }

}
