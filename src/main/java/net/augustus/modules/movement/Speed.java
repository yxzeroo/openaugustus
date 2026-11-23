package net.augustus.modules.movement;

import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.utils.file.DirectoryWatchService;
import net.augustus.events.EventMove;
import net.augustus.modules.modes.speed.StrafeSpeedMode;
import net.augustus.modules.modes.speed.OldNCPSpeedMode;
import net.augustus.events.listeners.MotionListener;
import net.augustus.modules.modes.speed.VulcanHopSpeedMode;
import net.augustus.modules.modes.speed.VBhopSpeedMode;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.modules.modes.speed.OldMinemoraSpeedMode;
import net.augustus.modules.modes.speed.SentrySpeedMode;
import net.augustus.modules.modes.speed.VerusSpeedMode;
import net.augustus.modules.modes.speed.MotionSpeedMode;
import net.augustus.modules.modes.speed.VerusLowHopSpeedMode;
import net.augustus.modules.modes.speed.MatrixSpeedMode;
import net.augustus.utils.file.DirectoryWatcher;
import net.augustus.modules.modes.speed.VulcanYPortSpeedMode;
import net.augustus.modules.modes.speed.OldMineplexSpeedMode;
import net.augustus.modules.modes.speed.OldMatrixSpeedMode;
import net.augustus.modules.modes.speed.VOnGroundSpeedMode;
import net.augustus.modules.modes.speed.VulcanLowHopSpeedMode;
import net.augustus.modules.modes.speed.TimeManipulationSpeedMode;
import net.augustus.modules.modes.speed.VulcanOnGroundSpeedMode;
import net.augustus.utils.file.ClientPaths;
import net.augustus.settings.Setting;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.MoveUtil;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.TimerListener;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.AIMoveSpeedListener;
import net.augustus.events.EventTimer;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.config.FeatureListJsonCodec;
import net.augustus.events.EventAIMoveSpeed;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.UnaryFunction;
import net.augustus.events.EventMoveEntity;
import net.augustus.modules.modes.speed.FortyFiveDegreeSpeedMode;
import net.augustus.modules.modes.speed.SpeedMode;
import net.augustus.modules.modes.speed.DojoSpeedMode;
import net.augustus.modules.modes.speed.EntitySpeedMode;
import net.augustus.modules.modes.speed.MotionModifierSpeedMode;
import net.augustus.modules.modes.speed.KarhuSpeedMode;
import net.augustus.events.listeners.PacketListener;
import net.augustus.modules.modes.speed.IntaveSpeedMode;
import net.augustus.modules.modes.speed.OldHypixelSpeedMode;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.modes.speed.CubeCraftSpeedMode;
import net.augustus.modules.modes.speed.InteractSpeedMode;
import net.augustus.modules.modes.speed.LegitHopSpeedMode;
import net.augustus.events.EventMotion;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;

public class Speed
   extends Module
   implements AIMoveSpeedListener,
   MoveListener,
   TickListener,
   MoveEntityListener,
   MoveInputListener,
   UpdateListener,
   MotionListener,
   PacketListener,
   TimerListener,
   PacketDelayListener {
   public final BooleansSetting mode;
   public final StringValue vanillaSpeed;
   public final StringValue dojoSpeed;
   public final StringValue height;
   public final BooleanValue damageBoost;
   public final StringValue dmgSpeed;
   public final BooleanValue strafe;
   public final StringValue timeManipulationTimer;
   public final StringValue timeManipulationTPTicks;
   public final StringValue interactSpeed;
   public final BooleanValue mouseSensi;
   public final BooleanValue autoJump;
   public final BooleanValue onlyWhileMove;
   public final BooleanValue allDirectionSprint;
   public final StringValue additionalSpeed;
   public final BooleanValue correctMovement;
   public final BooleanValue notWhileInCobweb;
   public final BooleanValue notWhileInWater;
   private final HashMap modesByName;
   private final HashMap builtinModes;
   private final String customModesDir;
   private final VOnGroundSpeedMode vOnGroundSpeedMode;
   private static final FeatureListJsonCodec JSON_CODEC = new FeatureListJsonCodec();
   private AtomicBoolean suppressedByEnvironment;

   public Speed() {
       super("Speed", "Schnell", new Color(74, 133, 93), Categorys.MOVEMENT);
      this.mode = new BooleansSetting(this, "Mode", "VBhop", new String[0]);
      StringValue stringValue = new StringValue(this, "VanillaSpeed", 1.0, 0.0, 10.0, 2);
      String str = "Speed";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isVanillaSpeedVisible();
      this.vanillaSpeed = stringValue2.visibleWhen(condition);
      StringValue stringValue3 = new StringValue(this, "DojoSpeed", 5.0, 1.0, 10.0, 2);
      SettingVisibilityCondition condition2 = () -> this.isDojoSpeedVisible();
      this.dojoSpeed = stringValue3.visibleWhen(condition2);
      StringValue stringValue4 = new StringValue(this, "Height", 1.0, 0.0, 5.0, 2);
      SettingVisibilityCondition condition3 = () -> {
         return this.mode.getSelectedValue().equals("VBhop");
      };
      this.height = stringValue4.visibleWhen(condition3);
      BooleanValue booleanValue = new BooleanValue(this, "DamageBoost", false);
      SettingVisibilityCondition condition4 = () -> this.isDamageBoostVisible();
      this.damageBoost = (BooleanValue)booleanValue.withVisibility(condition4);
      StringValue stringValue5 = new StringValue(this, "DMGSpeed", 1.0, 0.0, 2.0, 2);
      SettingVisibilityCondition condition5 = () -> this.isDamageSpeedVisible();
      this.dmgSpeed = stringValue5.visibleWhen(condition5);
      BooleanValue booleanValue2 = new BooleanValue(this, "Strafe", false);
      SettingVisibilityCondition condition6 = () -> this.isStrafeVisible();
      this.strafe = (BooleanValue)booleanValue2.withVisibility(condition6);
      StringValue stringValue6 = new StringValue(this, "TimeManipulationTimer", 1.0, 0.0, 10.0, 2);
      String str2 = "TimerSpeed";
      StringValue stringValue7 = stringValue6.named(str2);
      SettingVisibilityCondition condition7 = () -> {
         return this.mode.getSelectedValue().equals("TimeManipulation");
      };
      this.timeManipulationTimer = stringValue7.visibleWhen(condition7);
      StringValue stringValue8 = new StringValue(this, "TimeManipulationTPTicks", 0.0, 0.0, 10.0, 0);
      String str3 = "TPTicks";
      StringValue stringValue9 = stringValue8.named(str3);
      SettingVisibilityCondition condition8 = () -> this.isTimeManipulationTpTicksVisible();
      this.timeManipulationTPTicks = stringValue9.visibleWhen(condition8);
      StringValue stringValue10 = new StringValue(this, "InteractSpeed", 1.0, 1.0, 10.0, 0);
      SettingVisibilityCondition condition9 = () -> this.isInteractSpeedVisible();
      this.interactSpeed = stringValue10.visibleWhen(condition9);
      BooleanValue booleanValue3 = new BooleanValue(this, "MouseSensi", true);
      SettingVisibilityCondition condition10 = () -> this.isMouseSensiVisible();
      this.mouseSensi = (BooleanValue)booleanValue3.withVisibility(condition10);
      BooleanValue booleanValue4 = new BooleanValue(this, "AutoJump", true);
      SettingVisibilityCondition condition11 = () -> this.isAutoJumpVisible();
      this.autoJump = (BooleanValue)booleanValue4.withVisibility(condition11);
      BooleanValue booleanValue5 = new BooleanValue(this, "OnlyWhileMove", true);
      SettingVisibilityCondition condition12 = () -> {
         return this.mode.getSelectedValue().equals("45Degree");
      };
      this.onlyWhileMove = (BooleanValue)booleanValue5.withVisibility(condition12);
      BooleanValue booleanValue6 = new BooleanValue(this, "AllDirectionSprint", false);
      SettingVisibilityCondition condition13 = () -> this.isAllDirectionSprintVisible();
      this.allDirectionSprint = (BooleanValue)booleanValue6.withVisibility(condition13);
      StringValue stringValue11 = new StringValue(this, "AdditionalSpeed", 0.2, 0.0, 2.0, 4);
      SettingVisibilityCondition condition14 = () -> this.isAdditionalSpeedVisible();
      this.additionalSpeed = stringValue11.visibleWhen(condition14);
      this.correctMovement = new BooleanValue(this, "CorrectMovement", true);
      this.notWhileInCobweb = new BooleanValue(this, "NotWhileInCobweb", true);
      this.notWhileInWater = new BooleanValue(this, "NotWhileInWater", true);
      this.modesByName = new HashMap();
      this.customModesDir = ClientPaths.CLIENT_DIR + File.separator + "motionmodifier" + File.separator + "speed";
      this.suppressedByEnvironment = new AtomicBoolean(false);
      ClientFileUtil.ensureDirectory(this.customModesDir);
      VOnGroundSpeedMode vOnGroundSpeedMode2 = this.vOnGroundSpeedMode = new VOnGroundSpeedMode("VOnGround", this);
      this.registerMode(vOnGroundSpeedMode2);
      VBhopSpeedMode vBhopSpeedMode = new VBhopSpeedMode("VBhop", this);
      this.registerMode(vBhopSpeedMode);
      OldMineplexSpeedMode oldMineplexSpeedMode = new OldMineplexSpeedMode("OldMineplex", this);
      this.registerMode(oldMineplexSpeedMode);
      OldMatrixSpeedMode oldMatrixSpeedMode = new OldMatrixSpeedMode("OldMatrix", this);
      this.registerMode(oldMatrixSpeedMode);
      MatrixSpeedMode matrixSpeedMode = new MatrixSpeedMode("Matrix", this);
      this.registerMode(matrixSpeedMode);
      OldMinemoraSpeedMode oldMinemoraSpeedMode = new OldMinemoraSpeedMode("OldMinemora", this);
      this.registerMode(oldMinemoraSpeedMode);
      OldNCPSpeedMode oldNCPSpeedMode = new OldNCPSpeedMode("OldNCP", this);
      this.registerMode(oldNCPSpeedMode);
      CubeCraftSpeedMode cubeCraftSpeedMode = new CubeCraftSpeedMode("CubeCraft", this);
      this.registerMode(cubeCraftSpeedMode);
      LegitHopSpeedMode legitHopSpeedMode = new LegitHopSpeedMode("LegitHop", this);
      this.registerMode(legitHopSpeedMode);
      DojoSpeedMode dojoSpeedMode = new DojoSpeedMode("Dojo", this);
      this.registerMode(dojoSpeedMode);
      StrafeSpeedMode strafeSpeedMode = new StrafeSpeedMode("Strafe", this);
      this.registerMode(strafeSpeedMode);
      SentrySpeedMode sentrySpeedMode = new SentrySpeedMode("Sentry", this);
      this.registerMode(sentrySpeedMode);
      TimeManipulationSpeedMode mode2 = new TimeManipulationSpeedMode("TimeManipulation", this);
      this.registerMode(mode2);
      OldHypixelSpeedMode oldHypixelSpeedMode = new OldHypixelSpeedMode("OldHypixel", this);
      this.registerMode(oldHypixelSpeedMode);
      IntaveSpeedMode intaveSpeedMode = new IntaveSpeedMode("Intave", this);
      this.registerMode(intaveSpeedMode);
      VulcanLowHopSpeedMode vulcanLowHopSpeedMode = new VulcanLowHopSpeedMode("VulcanLowHop", this);
      this.registerMode(vulcanLowHopSpeedMode);
      VulcanOnGroundSpeedMode vulcanOnGroundSpeedMode = new VulcanOnGroundSpeedMode("VulcanOnground", this);
      this.registerMode(vulcanOnGroundSpeedMode);
      VulcanYPortSpeedMode vulcanYPortSpeedMode = new VulcanYPortSpeedMode("VulcanYPort", this);
      this.registerMode(vulcanYPortSpeedMode);
      VulcanHopSpeedMode vulcanHopSpeedMode = new VulcanHopSpeedMode("VulcanHop", this);
      this.registerMode(vulcanHopSpeedMode);
      VerusSpeedMode verusSpeedMode = new VerusSpeedMode("Verus", this);
      this.registerMode(verusSpeedMode);
      VerusLowHopSpeedMode verusLowHopSpeedMode = new VerusLowHopSpeedMode("VerusLowHop", this);
      this.registerMode(verusLowHopSpeedMode);
      InteractSpeedMode interactSpeedMode = new InteractSpeedMode("Interact", this);
      this.registerMode(interactSpeedMode);
      KarhuSpeedMode karhuSpeedMode = new KarhuSpeedMode("Karhu", this);
      this.registerMode(karhuSpeedMode);
      FortyFiveDegreeSpeedMode fortyFiveDegreeSpeedMode = new FortyFiveDegreeSpeedMode("45Degree", this);
      this.registerMode(fortyFiveDegreeSpeedMode);
      EntitySpeedMode entitySpeedMode = new EntitySpeedMode("EntitySpeed", this);
      this.registerMode(entitySpeedMode);
      MotionSpeedMode motionSpeedMode = new MotionSpeedMode("Motion", this);
      this.registerMode(motionSpeedMode);
      Augustus.getInstance().getClass();
      this.builtinModes = new HashMap(this.modesByName);
      this.reloadCustomModes();
      DirectoryWatcher directoryWatcher = DirectoryWatchService.watch(this.customModesDir);
      Consumer consumer = obj -> this.onCustomModeCreated((Path)obj);
      directoryWatcher = directoryWatcher.onCreate(consumer);
      Consumer consumer2 = obj2 -> {
         File file = ((Path)obj2).toFile();
         this.loadCustomModeFile(file);
         this.syncModeSettingOptions();
      };
      directoryWatcher = directoryWatcher.onModify(consumer2);
      Consumer consumer3 = obj3 -> {
         File file2 = ((Path)obj3).toFile();
         Fly.getCachedSettings();
         String name2 = file2.getName();
         if (name2.endsWith(".json")) {
            String str4 = name2.substring(0, name2.length() - 5);
            this.modesByName.remove(str4);
            this.syncModeSettingOptions();
         }
      };
      directoryWatcher.onDelete(consumer3).register();
   }

   @Override
   public void onEnable() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      mc.getTimer().timerSpeed = 1.0F;
      if (mc.thePlayer != null) {
         mc.thePlayer.setSpeedInAir(0.02F);
         mc.thePlayer.setSpeedOnGround(0.1F);
         mc.thePlayer.stepHeight = 0.6F;
      }

      SpeedMode activeMode = this.getActiveMode();
      activeMode.onEnable();
   }

   @Override
   public void onDisable() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      mc.getTimer().timerSpeed = 1.0F;
      if (mc.thePlayer != null) {
         mc.thePlayer.setSpeedInAir(0.02F);
         mc.thePlayer.setSpeedOnGround(0.1F);
      }

      this.getActiveMode().onDisable();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 1;
      byte b2 = 0;
      this.toggleEventSubscription(b2, b);
      byte b3 = 28;
      this.toggleEventSubscription(b3);
      byte b4 = 40;
      this.toggleEventSubscription(b4);
      byte b5 = 20;
      this.toggleEventSubscription(b5);
      byte b6 = 21;
      this.toggleEventSubscription(b6);
      byte b7 = 43;
      this.toggleEventSubscription(b7);
      byte b8 = 1;
      byte b9 = 18;
      this.toggleEventSubscription(b9, b8);
      byte b10 = 1;
      byte b11 = 26;
      this.toggleEventSubscription(b11, b10);
      byte b12 = 41;
      this.toggleEventSubscription(b12);
      byte b13 = 25;
      this.toggleEventSubscription(b13);
   }

   public void reloadCustomModes() {
      this.modesByName.clear();
      this.modesByName.putAll(this.builtinModes);
      Fly.getCachedSettings();
      File file = new File(this.customModesDir);
      File[] files = file.listFiles();
      if (files != null) {
         for (File file2 : files) {
            this.loadCustomModeFile(file2);
         }
      }

      this.syncModeSettingOptions();
   }

   private void loadCustomModeFile(File file) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      String name2 = file.getName();
      if (name2.endsWith(".json")) {
         MotionModifierSpeedMode motionModifierSpeedMode = this.parseCustomModeFile(file);
         if (motionModifierSpeedMode == null) {
            return;
         }

         this.modesByName.put(motionModifierSpeedMode.getName(), motionModifierSpeedMode);
      }
   }

   private MotionModifierSpeedMode parseCustomModeFile(File file) {
      String name2 = file.getName();
      Fly.getCachedSettings();
      String str = name2.substring(0, Math.max(name2.length() - 5, 0));
      MotionModifierSpeedMode motionModifierSpeedMode = new MotionModifierSpeedMode(str, this);
      Object obj = null;
      JsonObject json = ClientFileUtil.readJsonObject(this.customModesDir, name2, (Runnable)obj);
      if (json == null) {
         return null;
      } else {
         FeatureListJsonCodec featureListJsonCodec = JSON_CODEC;
         Module[] modules = new Module[]{motionModifierSpeedMode.dummy};
         featureListJsonCodec.setFeatures2(modules);
         featureListJsonCodec = JSON_CODEC;
         UnaryFunction unaryFunction = obj2 -> true;
         boolean flag = true;
         boolean flag2 = featureListJsonCodec.deserializeFromObject(json, flag, unaryFunction);
         if (!flag2) {
            System.out.println("Failed to read speed");
            return null;
         } else {
            return motionModifierSpeedMode;
         }
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onOutgoingPacketDelay(event);
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
      if (!this.isSuppressed()) {
         this.getActiveMode().onMotion(event);
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         SpeedMode activeMode = this.getActiveMode();
         activeMode.onMoveEntity(event);
      }
   }

   @Override
   public void onUpdate2() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         SpeedMode activeMode = this.getActiveMode();
         activeMode.onPreUpdate();
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onPreMoveInput(event);
      }
   }

   @Override
   public void onMoveInput(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         SpeedMode activeMode = this.getActiveMode();
         activeMode.onPostMoveInput(event);
      }
   }

   @Override
   public void onMove(EventMove event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onMoveFlying(event);
         if (mc.thePlayer.fallDistance > 4.0F && !this.mode.getSelectedValue().equals("LegitHop")) {
            event.cancel();
         }

         if (!BlockUtil.isScaffoldActiveAndValid() && !this.correctMovement.getValue()) {
            float movementYaw = MoveUtil.getMovementYaw();
            event.setYaw(movementYaw);
         }
      }
   }

   @Override
   public void onMove2(EventMove event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         if (!BlockUtil.isScaffoldActiveAndValid() && !this.correctMovement.getValue()) {
            float movementYaw = MoveUtil.getMovementYaw();
            event.setYaw(movementYaw);
         }

         this.getActiveMode().onJumpMove(event);
      }
   }

   @Override
   public void onTick() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      this.updateSuppressionState();
      if (!this.isSuppressed()) {
         this.getActiveMode().onEarlyTick();
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onSendPacket(event);
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onReadPacket(event);
      }
   }

   @Override
   public void onTimer2(EventTimer event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onTimer(event);
      }
   }

   @Override
   public void onTimer(EventTimer event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onTimer(event);
      }
   }

   @Override
   public void onUpdate() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onPostUpdate();
      }
   }

   @Override
   public void onAIMoveSpeed(EventAIMoveSpeed event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.isSuppressed()) {
         this.getActiveMode().onAIMoveSpeed(event);
      }
   }

   private void registerMode(SpeedMode speedMode) {
      this.modesByName.put(speedMode.getName(), speedMode);
   }

   private SpeedMode getActiveMode() {
      return (SpeedMode)(this.modesByName.getOrDefault(this.mode.getSelectedValue(), this.vOnGroundSpeedMode));
   }

   private void updateSuppressionState() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      this.suppressedByEnvironment.set(mc.thePlayer != null && (mc.thePlayer.isInWater() && this.notWhileInWater.getValue() || mc.thePlayer.isInWeb() && this.notWhileInCobweb.getValue()));
   }

   private boolean isSuppressed() {
      return this.suppressedByEnvironment.get();
   }

   private void syncModeSettingOptions() {
      Setting[] cachedSettings = Fly.getCachedSettings();
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

   private void onCustomModeCreated(Path path) {
      File file = path.toFile();
      this.loadCustomModeFile(file);
      this.syncModeSettingOptions();
   }

   private boolean isAdditionalSpeedVisible() {
      return this.mode.getSelectedValue().equals("EntitySpeed");
   }

   private boolean isAllDirectionSprintVisible() {
      return this.mode.getSelectedValue().equals("45Degree");
   }

   private boolean isAutoJumpVisible() {
      return this.mode.getSelectedValue().equals("45Degree");
   }

   private boolean isMouseSensiVisible() {
      return this.mode.getSelectedValue().equals("45Degree");
   }

   private boolean isInteractSpeedVisible() {
      return this.mode.getSelectedValue().equals("Interact");
   }

   private boolean isTimeManipulationTpTicksVisible() {
      return this.mode.getSelectedValue().equals("TimeManipulation");
   }

   private boolean isStrafeVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.mode.getSelectedValue().equals("VOnGround") || this.mode.getSelectedValue().equals("VBhop");
   }

   private boolean isDamageSpeedVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.mode.getSelectedValue().equals("Verus") && this.damageBoost.getValue();
   }

   private boolean isDamageBoostVisible() {
      return this.mode.getSelectedValue().equals("Verus");
   }

   private boolean isDojoSpeedVisible() {
      return this.mode.getSelectedValue().equals("Dojo");
   }

   private boolean isVanillaSpeedVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.mode.getSelectedValue().equals("VOnGround") || this.mode.getSelectedValue().equals("VBhop");
   }





}
