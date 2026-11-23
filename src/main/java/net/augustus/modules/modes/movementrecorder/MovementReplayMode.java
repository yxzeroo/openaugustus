package net.augustus.modules.modes.movementrecorder;

import net.augustus.ClientManagers;
import net.augustus.events.EventMotion;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.EventSprint;
import net.augustus.events.EventTimer;
import net.augustus.events.listeners.MotionListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.SprintListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.TimerListener;
import net.augustus.settings.SettingChangeListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.property.codec.JsonCodecRegistryHolder;
import net.augustus.settings.property.codec.JsonListPropertyCodec;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.rotation.Rotation;
import net.augustus.utils.rotation.RotationUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.augustus.modules.Module;
import net.augustus.modules.movement.MovementRecorder;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;

public class MovementReplayMode extends AbstractMovementRecorderMode implements TickListener, PacketDelayListener, MoveInputListener, SprintListener, TimerListener, MotionListener {
   private final BooleansSetting recordings;
   private final StringValue startDelay;
   private final BooleanValue autoDetect;
   private final StringValue minTeleportDistanceToStartReplay;
   private final BooleanValue mouseSensi;
   private final BooleanValue loop;
   private final BooleanValue replayDebug;
   private final DoubleValue timer;
   private final BooleansSetting timerMode;
   private final StringValue startTimerAfterXTicksOfTheRecording;
   private final StringValue timerSpeedDefault;
   private final StringValue timerSpeedBalanced;
   private final HashMap recordingsByName;
   private boolean awaitingStartOnGround;
   private boolean replaying;
   private final TimeHelper timeHelper;
   private RecordedMovementInput recordedMovementInput;
   private int replayTickIndex;
   private boolean defaultTimerApplied;

   public MovementReplayMode(MovementRecorder movementRecorder, DoubleValue doubleValue) {
       super(movementRecorder, doubleValue, "Replay");
      BooleansSetting booleansSetting = new BooleansSetting(this.parentModule, "Recordings", "", new String[0], this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.recordings = (BooleansSetting)((BooleansSetting)booleansSetting.withVisibility(condition)).markVisualOnly();
      StringValue stringValue = new StringValue(this.parentModule, "StartDelay", 3000.0, 0.0, 10000.0, 0, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.startDelay = (StringValue)stringValue.visibleWhen(condition2).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "AutoDetect", true, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.autoDetect = (BooleanValue)((BooleanValue)booleanValue.withVisibility(condition3)).markVisualOnly();
      StringValue stringValue2 = new StringValue(this.parentModule, "Min teleport distance to start replay", 0.0, 0.0, 1.0, 2);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.minTeleportDistanceToStartReplay = (StringValue)stringValue2.visibleWhen(condition4).markVisualOnly();
      BooleanValue booleanValue2 = new BooleanValue(this.parentModule, "MouseSensi", true, this.modeSetting);
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      this.mouseSensi = (BooleanValue)((BooleanValue)booleanValue2.withVisibility(condition5)).markVisualOnly();
      BooleanValue booleanValue3 = new BooleanValue(this.parentModule, "Loop", false, this.modeSetting);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.loop = (BooleanValue)((BooleanValue)booleanValue3.withVisibility(condition6)).markVisualOnly();
      BooleanValue booleanValue4 = new BooleanValue(this.parentModule, "ReplayDebug", true, this.modeSetting);
      String str = "Debug";
      BooleanValue booleanValue5 = (BooleanValue)booleanValue4.withDisplayName(str);
      SettingVisibilityCondition condition7 = () -> this.isSelected();
      this.replayDebug = (BooleanValue)((BooleanValue)booleanValue5.withVisibility(condition7)).markVisualOnly();
      DoubleValue doubleValue2 = new DoubleValue(this.parentModule, "Timer", true, this.modeSetting);
      SettingVisibilityCondition condition8 = () -> this.isSelected();
      this.timer = (DoubleValue)doubleValue2.visibleWhen(condition8).markVisualOnly();
      BooleansSetting booleansSetting2 = new BooleansSetting(this.parentModule, "TimerMode", "Default", new String[]{"None", "Default", "Balanced"}, this.timer);
      SettingVisibilityCondition condition9 = () -> this.isSelected();
      BooleansSetting booleansSetting3 = (BooleansSetting)booleansSetting2.withVisibility(condition9);
      SettingChangeListener listener = () -> mc.getTimer().timerSpeed = 1.0F;
      SettingChangeListener listener2 = listener;
      this.timerMode = (BooleansSetting)((BooleansSetting)booleansSetting3.withChangeListener(listener2)).markVisualOnly();
      StringValue stringValue3 = new StringValue(this.parentModule, "StartTimerAfterXTicksOfTheRecording", 40.0, 0.0, 100.0, 0, this.timer);
      SettingVisibilityCondition condition10 = () -> {
         return this.isSelected() && !this.timerMode.getSelectedValue().equals("None");
      };
      this.startTimerAfterXTicksOfTheRecording = (StringValue)stringValue3.visibleWhen(condition10).markVisualOnly();
      StringValue stringValue4 = new StringValue(this.parentModule, "TimerSpeedDefault", 1.0, 0.1, 10.0, 8, this.timer);
      String str2 = "TimeSpeed";
      StringValue stringValue5 = stringValue4.named(str2);
      SettingVisibilityCondition condition11 = () -> this.isDefaultTimerSpeedVisible();
      this.timerSpeedDefault = (StringValue)stringValue5.visibleWhen(condition11).markVisualOnly();
      StringValue stringValue6 = new StringValue(this.parentModule, "TimerSpeedBalanced", 1.0, 0.1, 10.0, 8, this.timer);
      String str3 = "TimeSpeed";
      StringValue stringValue7 = stringValue6.named(str3);
      SettingVisibilityCondition condition12 = () -> {
         return this.isSelected() && this.timerMode.getSelectedValue().equals("Balanced");
      };
      this.timerSpeedBalanced = (StringValue)stringValue7.visibleWhen(condition12).markVisualOnly();
      this.recordingsByName = new HashMap();
      this.awaitingStartOnGround = false;
      this.replaying = false;
      this.timeHelper = new TimeHelper();
      this.replayTickIndex = 0;
      this.defaultTimerApplied = false;
      this.reloadRecordings();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 100;
      byte b3 = 25;
      this.toggleEventSubscription(b3, b2);
      byte b4 = 36;
      this.toggleEventSubscription(b4);
      byte b5 = 100;
      byte b6 = 21;
      this.toggleEventSubscription(b6, b5);
      byte b7 = 18;
      this.toggleEventSubscription(b7);
      byte b8 = 41;
      this.toggleEventSubscription(b8);
   }

   @Override
   public void onEnable() {
      this.reloadRecordings();
      this.resetReplayState();
      if (this.timerMode.getSelectedValue().equals("Balanced") && !ClientManagers.moduleManager.timeManipulation.isToggled()) {
         MovementRecorder parentModule2 = (MovementRecorder)this.parentModule;
         String str = "Please enable \"TimeManipulation\" module to use the balance timer option.";
         parentModule2.sendChatMessage2(str);
      }
   }

   @Override
   public void onDisable() {
      mc.getTimer().timerSpeed = 1.0F;
   }

   @Override
   public void onTick() {
      MovementRecording selectedRecording = this.getSelectedRecording();
      if (mc.isNotInGui() && selectedRecording != null) {
         TimeHelper timeHelper2 = this.timeHelper;
         double value = this.startDelay.getValue();
         if (timeHelper2.reached(value)) {
            if (this.awaitingStartOnGround && mc.thePlayer.onGround) {
               this.replaying = !this.autoDetect.getValue();
               if (this.autoDetect.getValue()) {
                  boolean flag = false;

                  for (MovementRecording movementRecording : (Iterable<MovementRecording>)(this.recordingsByName.values())) {
                     List list = this.captureSurroundingBlocks();
                     if (Arrays.equals(MovementRecording.accessBlockSignature(movementRecording).toArray(new Boolean[0]), list.toArray(new Boolean[0]))) {
                        this.replaying = true;
                        BooleansSetting recordings2 = this.recordings;
                        String str = MovementRecording.accessName(movementRecording);
                        recordings2.setSelectedValue(str);
                        flag = true;
                        break;
                     }
                  }

                  if (!flag) {
                     MovementRecorder parentModule2 = (MovementRecorder)this.parentModule;
                     String str2 = "No equal map found. Sorry :/";
                     parentModule2.sendChatMessage2(str2);
                  }
               }

               this.awaitingStartOnGround = false;
            }

            if (!this.replaying) {
               return;
            }

            selectedRecording = this.getSelectedRecording();
            if (this.replayTickIndex >= MovementRecording.accessInputs(selectedRecording).size()) {
               if (!this.loop.getValue()) {
                  this.replaying = false;
                  MovementRecorder parentModule3 = (MovementRecorder)this.parentModule;
                  String str3 = "Finished!";
                  parentModule3.sendChatMessage2(str3);
               } else {
                  this.replayTickIndex = 0;
               }

               return;
            }

            this.recordedMovementInput = (RecordedMovementInput)MovementRecording.accessInputs(selectedRecording).get(this.replayTickIndex);
            if (this.recordedMovementInput == null) {
               return;
            }

            if (this.replayDebug.getValue()) {
               MovementRecorder parentModule4 = (MovementRecorder)this.parentModule;
               String str4 = this.recordedMovementInput.toString();
               parentModule4.sendChatMessage2(str4);
            }

            this.replayTickIndex++;
            Rotation rotation = new Rotation(
               RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, this.recordedMovementInput.getYaw(), 180.0F),
               RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, this.recordedMovementInput.getPitch(), 180.0F)
            );
            if (this.mouseSensi.getValue()) {
               Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
               rotation = RotationUtil.applyGcdFix(rotation, previousRotation);
            }

            Module parentModule5 = this.parentModule;
            byte b = -20;
            ClientManagers.rotationController.requestRotation(rotation, b, parentModule5);
            return;
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (!this.isReplayInactive()) {
         MoveFixMode none = MoveFixMode.NONE;
         event.setMoveFixMode(none);
         float forward = this.recordedMovementInput.getForward();
         event.setMoveForward(forward);
         float strafe = this.recordedMovementInput.getStrafe();
         event.setMoveStrafe(strafe);
         boolean isJumping = this.recordedMovementInput.isJumping();
         event.setJump(isJumping);
         boolean isSneaking = this.recordedMovementInput.isSneaking();
         event.setSneak(isSneaking);
      }
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      if (!event.isDelayed()) {
         Packet packet = event.getPacket();
         if (packet instanceof S07PacketRespawn) {
            PacketThreadUtil.runOnMainThreadImmediate(() -> this.resetReplayState());
         } else if (packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet2 = (S08PacketPlayerPosLook)packet;
            Vec3 vec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            Vec3 vec2 = new Vec3(packet2.getX(), packet2.getY(), packet2.getZ());
            if (vec.distanceTo(vec2) >= this.minTeleportDistanceToStartReplay.getValue()) {
               PacketThreadUtil.runOnMainThreadImmediate(() -> this.resetReplayState());
            }
         }
      }
   }

   @Override
   public void onSprint3(EventSprint event) {
      if (!this.isReplayInactive()) {
         boolean isSprinting = this.recordedMovementInput.isSprinting();
         event.setSprintKeyDown(isSprinting);
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      if (this.timerMode.getSelectedValue().equals("Default")) {
         if ((double)this.replayTickIndex > this.startTimerAfterXTicksOfTheRecording.getValue() && this.replaying) {
            Timer timer2 = mc.getTimer();
            boolean flag = true;
            timer2.timerSpeed = (float)this.timerSpeedDefault.getValue(flag);
            this.defaultTimerApplied = true;
         } else if (this.defaultTimerApplied) {
            this.defaultTimerApplied = false;
            mc.getTimer().timerSpeed = 1.0F;
         }
      }
   }

   @Override
   public void onTimer2(EventTimer event) {
      if (this.timerMode.getSelectedValue().equals("Balanced") && (double)this.replayTickIndex > this.startTimerAfterXTicksOfTheRecording.getValue() && this.replaying) {
         boolean flag = true;
         float value = (float)this.timerSpeedBalanced.getValue(flag);
         event.setTimerSpeed(value);
      }
   }

   @Override
   public void onTimer(EventTimer event) {
   }

   private void resetReplayState() {
      this.replayTickIndex = 0;
      this.recordedMovementInput = null;
      this.awaitingStartOnGround = true;
      this.replaying = false;
      this.timeHelper.reset();
   }

   private void reloadRecordings() {
      this.recordingsByName.clear();
      ClientFileUtil.ensureDirectory(MovementRecorder.RECORDINGS_DIR);
      File file = new File(MovementRecorder.RECORDINGS_DIR);
      File[] files = file.listFiles(File::isFile);
      if (files != null) {
         for (File file2 : files) {
            if (file2.getName().endsWith(".json")) {
               MovementRecording movementRecording = this.loadRecordingFile(file2);
               if (movementRecording != null) {
                  this.recordingsByName.put(MovementRecording.accessName(movementRecording), movementRecording);
               }
            }
         }

         boolean flag = this.recordingsByName.containsKey(this.recordings.getSelectedValue());
         String[] strings = (String[])this.recordingsByName.keySet().stream().sorted().toArray(String[]::new);
         if (!flag && strings.length > 0) {
            String str = strings[0];
            this.recordings.setSelectedValue(str);
         }

         this.recordings.setOptions(strings);
      }
   }

   private MovementRecording loadRecordingFile(File file) {
      String absolutePath = file.getParentFile().getAbsolutePath();
      String name2 = file.getName();
      Object obj = null;
      String str = name2;
      JsonObject json = ClientFileUtil.readJsonObject(absolutePath, str, (Runnable)obj);
      if (json == null) {
         return null;
      } else {
         JsonListPropertyCodec booleanListCodec = JsonCodecRegistryHolder.GSON_SERIALIZER_PROVIDER.getBooleanListCodec();
         ArrayList list = new ArrayList();
         String str2 = "Blocks";
         List list2 = booleanListCodec.readList(str2, list, json);
         if (list2.isEmpty()) {
            return null;
         } else {
            JsonArray asJsonArray = json.get("InputObjects").getAsJsonArray();
            if (asJsonArray == null) {
               return null;
            } else {
               ArrayList list3 = new ArrayList();

               for (JsonElement elem : asJsonArray) {
                  if (elem.isJsonObject()) {
                     RecordedMovementInput recordedMovementInput2 = new RecordedMovementInput();
                     JsonObject json2 = (JsonObject)elem;
                     recordedMovementInput2.fromJson(json2);
                     list3.add(recordedMovementInput2);
                  }
               }

               return new MovementRecording(this, file.getName().substring(0, Math.max(file.getName().length() - 5, 0)), list3, list2);
            }
         }
      }
   }

   private MovementRecording getSelectedRecording() {
      return (MovementRecording)this.recordingsByName.get(this.recordings.getSelectedValue());
   }

   private boolean isReplayInactive() {
      MovementRecording selectedRecording = this.getSelectedRecording();
      if (mc.isNotInGui() && selectedRecording != null && this.recordedMovementInput != null && this.replaying) {
         TimeHelper timeHelper2 = this.timeHelper;
         double value = this.startDelay.getValue();
         if (timeHelper2.reached(value)) {
            return false;
         }
      }

      return true;
   }

   private boolean isDefaultTimerSpeedVisible() {
      return this.isSelected() && this.timerMode.getSelectedValue().equals("Default");
   }

}
