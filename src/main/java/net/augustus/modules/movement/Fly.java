package net.augustus.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.utils.file.DirectoryWatchService;
import net.augustus.events.listeners.BlockBoundingBoxListener;
import net.augustus.events.EventMove;
import net.augustus.events.listeners.MotionListener;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.DirectoryWatcher;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.utils.file.ClientPaths;
import net.augustus.events.EventClick;
import net.augustus.settings.Setting;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.MoveUtil;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.EventBlockBoundingBox;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.render.RenderUtil;
import net.augustus.config.FeatureListJsonCodec;
import net.augustus.modules.modes.fly.Verus3FlyMode;
import net.augustus.modules.modes.fly.Verus1FlyMode;
import net.augustus.modules.modes.fly.Vanilla1FlyMode;
import net.augustus.modules.modes.fly.Verus2FlyMode;
import net.augustus.modules.modes.fly.VerusCombinedFlyMode;
import net.augustus.modules.modes.fly.UniversoCraftFlyMode;
import net.augustus.modules.modes.fly.MotionModifierFlyMode;
import net.augustus.modules.modes.fly.FlyMode;
import net.augustus.modules.modes.fly.AirJumpFlyMode;
import net.augustus.modules.modes.fly.CubeCraftFlyMode;
import net.augustus.modules.modes.fly.GrimFlyMode;
import net.augustus.modules.modes.fly.AirPlaceFlyMode;
import net.augustus.modules.modes.fly.Polar2FlyMode;
import net.augustus.modules.modes.fly.Minemora1FlyMode;
import net.augustus.modules.modes.fly.OldGommeFlyMode;
import net.augustus.modules.modes.fly.BedwarsPracticeFlyMode;
import net.augustus.modules.modes.fly.VulcanFlyMode;
import net.augustus.modules.modes.fly.GroundCollisionFlyMode;
import net.augustus.modules.modes.fly.Polar3FlyMode;
import net.augustus.modules.modes.fly.Vanilla2FlyMode;
import net.augustus.modules.modes.fly.IntaveBoatFlyMode;
import net.augustus.modules.modes.fly.OldVerusFlyMode;
import net.augustus.modules.modes.fly.PolarFlyMode;
import net.augustus.modules.modes.fly.Minemora2FlyMode;
import net.augustus.Augustus;
import net.augustus.settings.SettingChangeListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.UnaryFunction;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

public class Fly
   extends Module
   implements ClickListener,
   SilentSlotListener,
   MoveListener,
   BlockBoundingBoxListener,
   MoveEntityListener,
   MoveInputListener,
   UpdateListener,
   MotionListener,
   RenderListener,
   PacketListener,
   PacketDelayListener,
   TickListener {
   private EntityDragon enderDragon2;
   public final BooleansSetting mode;
   public final BooleanValue waitForDamage;
   public final BooleanValue autoJump;
   public final BooleanValue onGroundPacket;
   public final StringValue vanillaSpeed;
   public final StringValue vanillaSpeedHorizontal;
   public final StringValue vanillaSpeedVertical;
   public final StringValue verusSpeed;
   public final BooleanValue onlyOnGround;
   public final StringValue verusSpeedCombined;
   public final StringValue cubeCraftSpeed;
   public final StringValue cubeCraftStopDuration;
   public final StringValue collisionY;
   public final StringValue height;
   public final StringValue boost;
   public final BooleanValue onWater;
   public final BooleanValue onWeb;
   public final BooleanValue helpMessage;
   public final StringValue vulcanCollisionY;
   public final BooleanValue timer;
   public final StringValue timerSpeed;
   public final StringValue timerSpeedEnd;
   public final BooleanValue disableAura;
   public final BooleanValue cancelS08;
   public final BooleansSetting blinkMode;
   private final BooleanValue distanceFlown;
   private final BooleanValue spigotFlyKickBypass;
   private final BooleanValue disableAtSetback;
   private final BooleanValue enderDragon;
   private final BooleanValue enderDragonSound;
   private final DecimalFormat distanceFormat;
   private Vec3 lastPosition;
   private double distanceFlown2;
   private long flyStartTimeMs;
   private final TimeHelper timeHelper;
   private final HashMap modesByName;
   private final ConcurrentHashMap customModeHashes;
   private final HashMap builtinModes;
   private final AirJumpFlyMode airJumpFlyMode;
   private final String customModesDir;
   private static final FeatureListJsonCodec JSON_CODEC = new FeatureListJsonCodec();
   private static Setting[] cachedSettings;

   public Fly() {
       super("Fly", "Fliegen", new Color(123, 240, 156), Categorys.MOVEMENT);
      this.enderDragon2 = null;
      this.mode = new BooleansSetting(this, "Mode", "", new String[0]);
      BooleanValue booleanValue = new BooleanValue(this, "WaitForDamage", true);
      SettingVisibilityCondition condition = () -> this.isWaitForDamageVisible();
      this.waitForDamage = (BooleanValue)booleanValue.withVisibility(condition);
      BooleanValue booleanValue2 = new BooleanValue(this, "AutoJump", true);
      SettingVisibilityCondition condition2 = () -> this.isAutoJumpVisible();
      this.autoJump = (BooleanValue)booleanValue2.withVisibility(condition2);
      BooleanValue booleanValue3 = new BooleanValue(this, "OnGroundPacket", true);
      SettingVisibilityCondition condition3 = () -> this.isOnGroundPacketVisible();
      this.onGroundPacket = (BooleanValue)booleanValue3.withVisibility(condition3);
      StringValue stringValue = new StringValue(this, "VanillaSpeed", 1.0, 0.1, 9.0, 1);
      SettingVisibilityCondition condition4 = () -> this.isVanillaSpeedVisible();
      this.vanillaSpeed = stringValue.visibleWhen(condition4);
      StringValue stringValue2 = new StringValue(this, "VanillaSpeedHorizontal", 1.0, 0.1, 9.0, 1);
      SettingVisibilityCondition condition5 = () -> this.isVanillaSpeedHorizontalVisible();
      this.vanillaSpeedHorizontal = stringValue2.visibleWhen(condition5);
      StringValue stringValue3 = new StringValue(this, "VanillaSpeedVertical", 0.4, 0.1, 9.0, 1);
      SettingVisibilityCondition condition6 = () -> this.isVanillaSpeedVerticalVisible();
      this.vanillaSpeedVertical = stringValue3.visibleWhen(condition6);
      StringValue stringValue4 = new StringValue(this, "VerusSpeed", 1.0, 0.1, 9.0, 1);
      SettingVisibilityCondition condition7 = () -> this.isVerusSpeedVisible();
      this.verusSpeed = stringValue4.visibleWhen(condition7);
      BooleanValue booleanValue4 = new BooleanValue(this, "OnlyOnGround", true);
      SettingVisibilityCondition condition8 = () -> this.isOnlyOnGroundVisible();
      this.onlyOnGround = (BooleanValue)booleanValue4.withVisibility(condition8);
      StringValue stringValue5 = new StringValue(this, "VerusSpeedCombined", 1.0, 0.1, 9.0, 1);
      String str = "VerusSpeed";
      StringValue stringValue6 = stringValue5.named(str);
      SettingVisibilityCondition condition9 = () -> this.isVerusSpeedCombinedVisible();
      this.verusSpeedCombined = stringValue6.visibleWhen(condition9);
      StringValue stringValue7 = new StringValue(this, "CubeCraftSpeed", 3.0, 0.0, 5.0, 2);
      SettingVisibilityCondition condition10 = () -> this.isCubeCraftSpeedVisible();
      this.cubeCraftSpeed = stringValue7.visibleWhen(condition10);
      StringValue stringValue8 = new StringValue(this, "CubeCraftStopDuration", 4.0, 0.0, 5.0, 1);
      SettingVisibilityCondition condition11 = () -> this.isCubeCraftStopDurationVisible();
      this.cubeCraftStopDuration = stringValue8.visibleWhen(condition11);
      StringValue stringValue9 = new StringValue(this, "CollisionY", 1.0, 0.0, 1.0, 2);
      SettingVisibilityCondition condition12 = () -> this.isCollisionYVisible();
      this.collisionY = stringValue9.visibleWhen(condition12);
      StringValue stringValue10 = new StringValue(this, "Height", 1.0, 1.0, 8.0, 2);
      SettingVisibilityCondition condition13 = () -> {
         return this.mode.getSelectedValue().equals("OldGomme");
      };
      this.height = stringValue10.visibleWhen(condition13);
      getCachedSettings();
      StringValue stringValue11 = new StringValue(this, "Boost", 1.0, 1.0, 8.0, 2);
      SettingVisibilityCondition condition14 = () -> this.isBoostVisible();
      this.boost = stringValue11.visibleWhen(condition14);
      BooleanValue booleanValue5 = new BooleanValue(this, "OnWater", true);
      SettingVisibilityCondition condition15 = () -> this.isOnWaterVisible();
      this.onWater = (BooleanValue)booleanValue5.withVisibility(condition15);
      BooleanValue booleanValue6 = new BooleanValue(this, "OnWeb", true);
      SettingVisibilityCondition condition16 = () -> this.isOnWebVisible();
      this.onWeb = (BooleanValue)booleanValue6.withVisibility(condition16);
      BooleanValue booleanValue7 = new BooleanValue(this, "HelpMessage", true);
      SettingVisibilityCondition condition17 = () -> this.isHelpMessageVisible();
      this.helpMessage = (BooleanValue)booleanValue7.withVisibility(condition17);
      stringValue11 = new StringValue(this, "VulcanCollisionY", 1.0, 0.0, 1.0, 2);
      String str2 = "CollisionY";
      stringValue11 = stringValue11.named(str2);
      SettingVisibilityCondition condition18 = () -> this.isVulcanCollisionYVisible();
      this.vulcanCollisionY = stringValue11.visibleWhen(condition18);
      BooleanValue booleanValue8 = new BooleanValue(this, "Timer", true);
      SettingVisibilityCondition condition19 = () -> this.isTimerVisible();
      this.timer = (BooleanValue)booleanValue8.withVisibility(condition19);
      stringValue11 = new StringValue(this, "TimerSpeed", 5.0, 0.2, 10.0, 2);
      SettingVisibilityCondition condition20 = () -> {
         Setting[] cachedSettings2 = getCachedSettings();
         return this.mode.getSelectedValue().equals("Vulcan") && this.timer.getValue();
      };
      this.timerSpeed = stringValue11.visibleWhen(condition20);
      stringValue11 = new StringValue(this, "TimerSpeedEnd", 1.0, 0.2, 50.0, 2);
      SettingVisibilityCondition condition21 = () -> this.isTimerSpeedEndVisible();
      this.timerSpeedEnd = stringValue11.visibleWhen(condition21);
      this.disableAura = new BooleanValue(this, "DisableAura", false);
      this.cancelS08 = new BooleanValue(this, "CancelS08", false);
      this.blinkMode = new BooleansSetting(this, "BlinkMode", "None", new String[]{"None", "OnlyIncoming", "OnlyOutgoing", "Both"});
      this.distanceFlown = new BooleanValue(this, "DistanceFlown", false);
      this.spigotFlyKickBypass = new BooleanValue(this, "SpigotFlyKickBypass", false);
      this.disableAtSetback = new BooleanValue(this, "DisableAtSetback", false);
      BooleanValue booleanValue9 = new BooleanValue(this, "EnderDragon", false);
      SettingChangeListener listener = () -> this.despawnEnderDragon();
      SettingChangeListener listener2 = listener;
      this.enderDragon = (BooleanValue)booleanValue9.withChangeListener(listener2);
      BooleanValue booleanValue10 = new BooleanValue(this, "EnderDragonSound", false);
      BooleanValue enderDragon3 = this.enderDragon;
      this.enderDragon.getClass();
      BooleanValue booleanValue11 = enderDragon3;
      SettingVisibilityCondition condition22 = () -> booleanValue11.getValue();
      this.enderDragonSound = (BooleanValue)booleanValue10.withVisibility(condition22);
      this.distanceFormat = new DecimalFormat("0.0");
      this.lastPosition = new Vec3(0.0, 0.0, 0.0);
      this.flyStartTimeMs = 0L;
      this.timeHelper = new TimeHelper();
      this.modesByName = new HashMap();
      this.customModeHashes = new ConcurrentHashMap();
      this.customModesDir = ClientPaths.CLIENT_DIR + File.separator + "motionmodifier" + File.separator + "fly";
      ClientFileUtil.ensureDirectory(this.customModesDir);
      this.airJumpFlyMode = new AirJumpFlyMode("AirJump", this);
      AirJumpFlyMode airJumpFlyMode2 = this.airJumpFlyMode;
      this.registerMode(airJumpFlyMode2);
      Vanilla1FlyMode vanilla1FlyMode = new Vanilla1FlyMode("Vanilla1", this);
      this.registerMode(vanilla1FlyMode);
      Vanilla2FlyMode vanilla2FlyMode = new Vanilla2FlyMode("Vanilla2", this);
      this.registerMode(vanilla2FlyMode);
      GroundCollisionFlyMode groundCollisionFlyMode = new GroundCollisionFlyMode("GroundCollision", this);
      this.registerMode(groundCollisionFlyMode);
      Minemora1FlyMode minemora1FlyMode = new Minemora1FlyMode("Minemora1", this);
      this.registerMode(minemora1FlyMode);
      Minemora2FlyMode minemora2FlyMode = new Minemora2FlyMode("Minemora2", this);
      this.registerMode(minemora2FlyMode);
      OldVerusFlyMode oldVerusFlyMode = new OldVerusFlyMode("OldVerus", this);
      this.registerMode(oldVerusFlyMode);
      Verus1FlyMode verus1FlyMode = new Verus1FlyMode("Verus1", this);
      this.registerMode(verus1FlyMode);
      Verus2FlyMode verus2FlyMode = new Verus2FlyMode("Verus2", this);
      this.registerMode(verus2FlyMode);
      Verus3FlyMode verus3FlyMode = new Verus3FlyMode("Verus3", this);
      this.registerMode(verus3FlyMode);
      VerusCombinedFlyMode verusCombinedFlyMode = new VerusCombinedFlyMode("VerusCombined", this);
      this.registerMode(verusCombinedFlyMode);
      UniversoCraftFlyMode universoCraftFlyMode = new UniversoCraftFlyMode("UniversoCraft", this);
      this.registerMode(universoCraftFlyMode);
      CubeCraftFlyMode cubeCraftFlyMode = new CubeCraftFlyMode("CubeCraft", this);
      this.registerMode(cubeCraftFlyMode);
      OldGommeFlyMode oldGommeFlyMode = new OldGommeFlyMode("OldGomme", this);
      this.registerMode(oldGommeFlyMode);
      GrimFlyMode grimFlyMode = new GrimFlyMode("GrimFly", this);
      this.registerMode(grimFlyMode);
      VulcanFlyMode vulcanFlyMode = new VulcanFlyMode("Vulcan", this);
      this.registerMode(vulcanFlyMode);
      IntaveBoatFlyMode intaveBoatFlyMode = new IntaveBoatFlyMode("IntaveBoat", this);
      this.registerMode(intaveBoatFlyMode);
      BedwarsPracticeFlyMode bedwarsPracticeFlyMode = new BedwarsPracticeFlyMode("BedwarsPractice", this);
      this.registerMode(bedwarsPracticeFlyMode);
      AirPlaceFlyMode airPlaceFlyMode = new AirPlaceFlyMode("AirPlace", this);
      this.registerMode(airPlaceFlyMode);
      PolarFlyMode polarFlyMode = new PolarFlyMode("PolarFly", this);
      this.registerMode(polarFlyMode);
      Polar2FlyMode polar2FlyMode = new Polar2FlyMode("PolarFly2", this);
      this.registerMode(polar2FlyMode);
      Polar3FlyMode polar3FlyMode = new Polar3FlyMode("PolarFly3", this);
      this.registerMode(polar3FlyMode);
      Augustus.getInstance().getClass();
      this.builtinModes = new HashMap(this.modesByName);
      this.reloadCustomModes();
      DirectoryWatcher directoryWatcher = DirectoryWatchService.watch(this.customModesDir);
      Consumer consumer = obj -> this.onCustomModeCreated((Path)obj);
      directoryWatcher = directoryWatcher.onCreate(consumer);
      Consumer consumer2 = obj2 -> this.onCustomModeModified((Path)obj2);
      directoryWatcher = directoryWatcher.onModify(consumer2);
      Consumer consumer3 = obj3 -> {
         File file = ((Path)obj3).toFile();
         getCachedSettings();
         String name2 = file.getName();
         short s = 14605;
         if (name2.endsWith(".json")) {
            String str3 = name2.substring(0, name2.length() - 5);
            this.modesByName.remove(str3);
            this.syncModeSettingOptions();
         }
      };
      directoryWatcher.onDelete(consumer3).register();
   }

   @Override
   public void onEnable() {
      this.timeHelper.reset();
      getCachedSettings();
      mc.getTimer().timerSpeed = 1.0F;
      if (mc.thePlayer != null) {
         mc.thePlayer.setSpeedInAir(0.02F);
         mc.thePlayer.setSpeedOnGround(0.1F);
         mc.thePlayer.stepHeight = 0.6F;
         this.lastPosition = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
      }

      if (mc.thePlayer != null) {
         this.getActiveMode().onEnable();
      }

      this.distanceFlown2 = 0.0;
      this.flyStartTimeMs = System.currentTimeMillis();
      if (mc.theWorld != null && mc.thePlayer != null && this.enderDragon.getValue()) {
         this.enderDragon2 = new FlyDragonEntity(this, mc.theWorld);
         mc.theWorld.addEntityToWorld(-1337, this.enderDragon2);
         this.enderDragon2.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY - 2.6, mc.thePlayer.posZ, mc.thePlayer.rotationYaw - 180.0F, mc.thePlayer.rotationPitch);
      }
   }

   @Override
   public void onDisable() {
      Setting[] cachedSettings2 = getCachedSettings();
      mc.getTimer().timerSpeed = 1.0F;
      if (mc.thePlayer != null) {
         mc.thePlayer.setSpeedInAir(0.02F);
         mc.thePlayer.setSpeedOnGround(0.1F);
         mc.thePlayer.stepHeight = 0.6F;
      }

      if (mc.thePlayer != null) {
         this.getActiveMode().onDisable();
      }

      mc.getTimer().timerSpeed = 1.0F;
      this.despawnEnderDragon();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 28;
      this.toggleEventSubscription(b);
      byte b2 = 3;
      this.toggleEventSubscription(b2);
      byte b3 = 20;
      this.toggleEventSubscription(b3);
      byte b4 = 21;
      this.toggleEventSubscription(b4);
      byte b5 = 43;
      this.toggleEventSubscription(b5);
      byte b6 = 18;
      this.toggleEventSubscription(b6);
      byte b7 = 32;
      this.toggleEventSubscription(b7);
      byte b8 = 26;
      this.toggleEventSubscription(b8);
      byte b9 = 25;
      this.toggleEventSubscription(b9);
      byte b10 = 40;
      this.toggleEventSubscription(b10);
      byte b11 = 7;
      this.toggleEventSubscription(b11);
      byte b12 = 34;
      this.toggleEventSubscription(b12);
   }

   @Override
   public void onTick2() {
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      Setting[] cachedSettings2 = getCachedSettings();
      if (mc.theWorld == null || mc.thePlayer == null || this.disableAtSetback.getValue() && event.getPacket() instanceof S08PacketPlayerPosLook) {
         PacketThreadUtil.runOnMainThreadImmediate(() -> {
            boolean flag = false;
            this.setToggled(flag);
         });
      }

      if (this.blinkMode.getSelectedValue().equals("OnlyIncoming") || this.blinkMode.getSelectedValue().equals("Both")) {
         boolean flag2 = true;
         event.setDelayed(flag2);
      }

      this.getActiveMode().onIncomingPacketDelay(event);
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      Setting[] cachedSettings2 = getCachedSettings();
      if (this.blinkMode.getSelectedValue().equals("OnlyOutgoing") || this.blinkMode.getSelectedValue().equals("Both")) {
         boolean flag = true;
         event.setDelayed(flag);
      }

      FlyMode activeMode = this.getActiveMode();
      activeMode.onOutgoingPacketDelay(event);
   }

   private double absDiff(double d, double d2) {
      Setting[] cachedSettings2 = getCachedSettings();
      double d3;
      if (d >= d2) {
         d3 = d - d2;
      } else {
         d3 = d2 - d;
      }

      return d3;
   }

   @Override
   public void onBlockBoundingBox(EventBlockBoundingBox event) {
      this.getActiveMode().onBlockBoundingBox(event);
   }

   @Override
   public void onMotion(EventMotion event) {
      this.getActiveMode().onMotion(event);
      double posX = mc.thePlayer.posX;
      double xCoord = this.lastPosition.xCoord;
      double d = this.absDiff(xCoord, posX);
      double posZ = mc.thePlayer.posZ;
      double zCoord = this.lastPosition.zCoord;
      double d2 = this.absDiff(zCoord, posZ);
      this.distanceFlown2 = this.distanceFlown2 + Math.sqrt(d * d + d2 * d2);
      this.lastPosition = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      this.getActiveMode().onMoveEntity(event);
   }

   @Override
   public void onUpdate2() {
      this.getActiveMode().onPreUpdate();
      this.performSpigotKickBypass();
   }

   @Override
   public void onTick() {
      this.getActiveMode().onEarlyTick();
   }

   @Override
   public void onUpdate() {
      Setting[] cachedSettings2 = getCachedSettings();
      this.getActiveMode().onPostUpdate();
      if (this.enderDragon.getValue() && this.enderDragon2 != null) {
         this.enderDragon2.lastTickPosX = mc.thePlayer.lastTickPosX;
         this.enderDragon2.lastTickPosY = mc.thePlayer.lastTickPosY - 2.6;
         this.enderDragon2.lastTickPosZ = mc.thePlayer.lastTickPosZ;
         this.enderDragon2.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.6, mc.thePlayer.posZ);
         this.enderDragon2.setRotation(mc.thePlayer.rotationYaw - 180.0F, mc.thePlayer.rotationPitch);
      }
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.distanceFlown.getValue()) {
         String str = "flown " + this.distanceFormat.format(this.distanceFlown2) + ";  Seconds: " + (System.currentTimeMillis() - this.flyStartTimeMs) / 1000L;
         mc.fontRendererObj.drawString(str, (int)((float)sr.getScaledWidth() / 2.0F - (float)mc.fontRendererObj.getStringWidth(str) / 2.0F), sr.getScaledHeight() - 65, -1);
      }

      RenderUtil.resetGlColorState();
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      FlyMode activeMode = this.getActiveMode();
      activeMode.onMoveInput(event);
   }

   @Override
   public void onMove(EventMove event) {
      this.getActiveMode().onMoveFlying(event);
      if (!BlockUtil.isScaffoldActiveAndValid()) {
         float movementYaw = MoveUtil.getMovementYaw();
         event.setYaw(movementYaw);
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (!BlockUtil.isScaffoldActiveAndValid()) {
         float movementYaw = MoveUtil.getMovementYaw();
         event.setYaw(movementYaw);
      }

      this.getActiveMode().onJumpMove(event);
   }

   @Override
   public void onPacket(EventPacket event) {
      Setting[] cachedSettings2 = getCachedSettings();
      if (this.cancelS08.getValue() && event.getPacket() instanceof S08PacketPlayerPosLook) {
         event.cancel();
      }

      this.getActiveMode().onReadPacket(event);
   }

   @Override
   public void onPacket2(EventPacket event) {
      this.getActiveMode().onSendPacket(event);
   }

   @Override
   public void onClick(EventClick event) {
      this.getActiveMode().onClick(event);
   }

   @Override
   public void onSilentSlot() {
      FlyMode activeMode = this.getActiveMode();
      activeMode.onSilentSlot();
   }

   private FlyMode getActiveMode() {
      return (FlyMode)(this.modesByName.getOrDefault(this.mode.getSelectedValue(), this.airJumpFlyMode));
   }

   private void performSpigotKickBypass() {
      Setting[] cachedSettings2 = getCachedSettings();
      if (this.spigotFlyKickBypass.getValue()) {
         long l = 1000L;
         if (this.timeHelper.reached2(l) && mc.thePlayer != null && mc.theWorld != null) {
            double d = 0.0;

            for (int i = (int)mc.thePlayer.posY; i > 0; i--) {
               Block block = mc.theWorld.getBlockState(mc.thePlayer.posX, (double)i, mc.thePlayer.posZ).getBlock();
               if (block.isFullBlock()) {
                  d = (double)i + block.getBlockBoundsMaxY();
                  break;
               }
            }

            for (double j = mc.thePlayer.posY; j > d; j -= 8.0) {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, j, mc.thePlayer.posZ, true));
            }

            mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, d, mc.thePlayer.posZ, true));

            for (double k = d; k < mc.thePlayer.posY; k += 8.0) {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, k, mc.thePlayer.posZ, true));
            }

            mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            this.timeHelper.reset();
            return;
         }
      }
   }

   public void reloadCustomModes() {
      this.modesByName.clear();
      this.modesByName.putAll(this.builtinModes);
      getCachedSettings();
      this.customModeHashes.clear();
      File file = new File(this.customModesDir);
      File[] files = file.listFiles();
      if (files != null) {
         for (File file2 : files) {
            this.loadCustomModeFile(file2);
         }
      }

      String[] strings = (String[])this.modesByName.keySet().stream().sorted().toArray(String[]::new);
      if (this.modesByName.get(this.mode.getSelectedValue()) == null) {
         String str = strings[0];
         this.mode.setSelectedValue(str);
      }

      this.mode.setOptions(strings);
   }

   private void loadCustomModeFile(File file) {
      Setting[] cachedSettings2 = getCachedSettings();
      String name2 = file.getName();
      if (name2.endsWith(".json")) {
         boolean flag = true;
         MotionModifierFlyMode motionModifierFlyMode = this.parseCustomModeFile(file, flag);
         if (motionModifierFlyMode == null) {
            return;
         }

         this.modesByName.put(motionModifierFlyMode.getName(), motionModifierFlyMode);
      }
   }

   private MotionModifierFlyMode parseCustomModeFile(File file, boolean flag) {
      String str = file.getName().substring(0, Math.max(file.getName().length() - 5, 0));
      getCachedSettings();
      MotionModifierFlyMode motionModifierFlyMode = new MotionModifierFlyMode(str, this);
      String customModesDir2 = this.customModesDir;
      String name2 = file.getName();
      Object obj = null;
      String str2 = name2;
      JsonObject json = ClientFileUtil.readJsonObject(customModesDir2, str2, (Runnable)obj);
      if (json == null) {
         return null;
      } else {
         FeatureListJsonCodec featureListJsonCodec = JSON_CODEC;
         Module[] modules = new Module[]{motionModifierFlyMode.dummy};
         featureListJsonCodec.setFeatures2(modules);
         FeatureListJsonCodec featureListJsonCodec2 = JSON_CODEC;
         UnaryFunction unaryFunction = obj2 -> true;
         boolean flag2 = true;
         boolean flag3 = featureListJsonCodec2.deserializeFromObject(json, flag2, unaryFunction);
         if (!flag3) {
            System.out.println("Failed to read fly");
            return null;
         } else if (!flag) {
            return motionModifierFlyMode;
         } else {
            try {
               this.customModeHashes.put(str, MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(file.toPath())));
               return motionModifierFlyMode;
            } catch (IOException | NoSuchAlgorithmException e) {
               throw new RuntimeException(e);
            }
         }
      }
   }

   private void despawnEnderDragon() {
      if (this.enderDragon2 != null && mc.theWorld != null && this.enderDragon.getValue()) {
         mc.theWorld.removeEntity(this.enderDragon2);
      }
   }

   private void registerMode(FlyMode flyMode) {
      this.modesByName.put(flyMode.getName(), flyMode);
   }

   private void syncModeSettingOptions() {
      Setting[] cachedSettings2 = getCachedSettings();
      boolean flag = this.modesByName.containsKey(this.mode.getSelectedValue());
      if (!flag) {
         BooleansSetting mode2 = this.mode;
         String str = (String)this.modesByName.keySet().iterator().next();
         mode2.setSelectedValue(str);
      }

      BooleansSetting mode3 = this.mode;
      String[] strings = ((Map<String, ?>)this.modesByName).keySet().stream().sorted(Comparator.comparing(String::toLowerCase)).toArray(String[]::new);
      mode3.setOptions(strings);
   }

   private void onCustomModeModified(Path path) {
      File file = path.toFile();
      this.loadCustomModeFile(file);
      this.syncModeSettingOptions();
   }

   private void onCustomModeCreated(Path path) {
      File file = path.toFile();
      this.loadCustomModeFile(file);
      this.syncModeSettingOptions();
   }

   private boolean isTimerSpeedEndVisible() {
      Setting[] cachedSettings2 = getCachedSettings();
      return this.mode.getSelectedValue().equals("Vulcan") && this.timer.getValue();
   }

   private boolean isTimerVisible() {
      return this.mode.getSelectedValue().equals("Vulcan");
   }

   private boolean isVulcanCollisionYVisible() {
      return this.mode.getSelectedValue().equals("Vulcan");
   }

   private boolean isHelpMessageVisible() {
      return this.mode.getSelectedValue().equals("IntaveBoat");
   }

   private boolean isOnWebVisible() {
      return this.mode.getSelectedValue().equals("GroundCollision");
   }

   private boolean isOnWaterVisible() {
      return this.mode.getSelectedValue().equals("GroundCollision");
   }

   private boolean isBoostVisible() {
      return this.mode.getSelectedValue().equals("OldGomme");
   }

   private boolean isCollisionYVisible() {
      return this.mode.getSelectedValue().equals("GroundCollision");
   }

   private boolean isCubeCraftStopDurationVisible() {
      return this.mode.getSelectedValue().equals("CubeCraft");
   }

   private boolean isCubeCraftSpeedVisible() {
      return this.mode.getSelectedValue().equals("CubeCraft");
   }

   private boolean isVerusSpeedCombinedVisible() {
      return this.mode.getSelectedValue().equals("VerusCombined");
   }

   private boolean isOnlyOnGroundVisible() {
      return this.mode.getSelectedValue().equals("OldVerus");
   }

   private boolean isVerusSpeedVisible() {
      return this.mode.getSelectedValue().equals("OldVerus");
   }

   private boolean isVanillaSpeedVerticalVisible() {
      return this.mode.getSelectedValue().equals("Vanilla2");
   }

   private boolean isVanillaSpeedHorizontalVisible() {
      return this.mode.getSelectedValue().equals("Vanilla2");
   }

   private boolean isVanillaSpeedVisible() {
      return this.mode.getSelectedValue().equals("Vanilla1");
   }

   private boolean isOnGroundPacketVisible() {
      return this.mode.getSelectedValue().equals("AirJump");
   }

   private boolean isAutoJumpVisible() {
      return this.mode.getSelectedValue().equals("AirJump");
   }

   private boolean isWaitForDamageVisible() {
      Setting[] cachedSettings2 = getCachedSettings();
      return this.mode.getSelectedValue().equals("Vanilla1") || this.mode.getSelectedValue().equals("Vanilla2");
   }

   public static BooleanValue getEnderDragonSoundSetting(Fly fly) {
      return fly.enderDragonSound;
   }

   static {
      setCachedSettings((Setting[])null);
   }

   public static void setCachedSettings(Setting[] settings2) {
      cachedSettings = settings2;
   }

   public static Setting[] getCachedSettings() {
      return cachedSettings;
   }

}
