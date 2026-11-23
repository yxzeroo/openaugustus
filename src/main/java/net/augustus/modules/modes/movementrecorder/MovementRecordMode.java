package net.augustus.modules.modes.movementrecorder;

import net.augustus.events.EventPacket;
import net.augustus.events.listeners.PacketListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.property.codec.JsonCodecRegistryHolder;
import net.augustus.settings.property.codec.JsonListPropertyCodec;
import net.augustus.utils.file.ClientFileUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.augustus.modules.movement.MovementRecorder;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;

public class MovementRecordMode extends AbstractMovementRecorderMode implements TickListener, PacketListener, UpdateListener {
   private final BooleanValue recordDebug;
   private final StringValue minTeleportDistanceToResetRecording;
   private final ArrayList recordedInputs;
   private RecordedMovementInput recordedMovementInput;
   private boolean recordingStarted;
   private JsonObject recordingJson;

   public MovementRecordMode(MovementRecorder movementRecorder, DoubleValue doubleValue) {
       super(movementRecorder, doubleValue, "Record");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "RecordDebug", true, this.modeSetting);
      String str = "Debug";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.recordDebug = (BooleanValue)((BooleanValue)booleanValue2.withVisibility(condition)).markVisualOnly();
      StringValue stringValue = new StringValue(this.parentModule, "Min teleport distance to reset recording", 0.0, 0.0, 1.0, 2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.minTeleportDistanceToResetRecording = (StringValue)stringValue.visibleWhen(condition2).markVisualOnly();
      this.recordedInputs = new ArrayList();
      this.recordedMovementInput = null;
      this.recordingStarted = false;
      this.recordingJson = null;
      ClientFileUtil.ensureDirectory(MovementRecorder.RECORDINGS_DIR);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 26;
      this.toggleEventSubscription(b2);
      byte b3 = 43;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onEnable() {
      this.resetRecording();
      mc.getTimer().timerSpeed = 1.0F;
   }

   @Override
   public void onDisable() {
      this.saveRecording();
      this.resetRecording();
   }

   private void saveRecording() {
      if (this.recordingJson == null) {
         MovementRecorder parentModule2 = (MovementRecorder)this.parentModule;
         String str = "Failed to save";
         parentModule2.sendChatMessage2(str);
      } else {
         JsonArray array = new JsonArray();

         for (RecordedMovementInput recordedMovementInput2 : (Iterable<RecordedMovementInput>)(this.recordedInputs)) {
            array.add(recordedMovementInput2.toJson());
         }

         this.recordingJson.add("InputObjects", array);
         String recordingsdir = MovementRecorder.RECORDINGS_DIR;
         String str2 = System.currentTimeMillis() + ".json";
         JsonObject recordingJson2 = this.recordingJson;
         String str3 = str2;
         ClientFileUtil.writeJsonObject(recordingsdir, str3, recordingJson2);
         this.resetRecording();
      }
   }

   @Override
   public void onUpdate() {
      if (this.recordedMovementInput != null) {
         MovementInput movementInput = mc.thePlayer.movementInput;
         float moveForward = movementInput.moveForward;
         this.recordedMovementInput.setForward(moveForward);
         float moveStrafe = movementInput.moveStrafe;
         this.recordedMovementInput.setStrafe(moveStrafe);
         boolean sneak = movementInput.sneak;
         this.recordedMovementInput.setSneaking(sneak);
         boolean jump = movementInput.jump;
         this.recordedMovementInput.setJumping(jump);
         RecordedMovementInput recordedMovementInput2 = this.recordedMovementInput;
         boolean isSprinting = mc.thePlayer.isSprinting();
         recordedMovementInput2.setSprinting(isSprinting);
         if (this.recordingStarted || movementInput.moveForward != 0.0F || movementInput.moveStrafe != 0.0F || movementInput.jump || movementInput.sneak) {
            if (this.recordingJson == null) {
               this.recordingJson = this.createRecordingHeader();
            }

            this.recordingStarted = true;
            this.recordedInputs.add(this.recordedMovementInput.copy());
            if (this.recordDebug.getValue()) {
               MovementRecorder parentModule2 = (MovementRecorder)this.parentModule;
               String str = this.recordedMovementInput.toString();
               parentModule2.sendChatMessage2(str);
            }
         }
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof S07PacketRespawn) {
         this.resetRecording();
      } else if (packet instanceof S08PacketPlayerPosLook) {
         S08PacketPlayerPosLook packet2 = (S08PacketPlayerPosLook)packet;
         Vec3 vec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
         Vec3 vec2 = new Vec3(packet2.getX(), packet2.getY(), packet2.getZ());
         if (vec.distanceTo(vec2) >= this.minTeleportDistanceToResetRecording.getValue()) {
            this.resetRecording();
         }
      }
   }

   @Override
   public void onTick2() {
      if (this.recordedMovementInput == null) {
         this.recordedMovementInput = new RecordedMovementInput();
      }

      float rotationYaw = mc.thePlayer.rotationYaw;
      this.recordedMovementInput.setYaw(rotationYaw);
      float rotationPitch = mc.thePlayer.rotationPitch;
      this.recordedMovementInput.setPitch(rotationPitch);
   }

   private void resetRecording() {
      this.recordingJson = null;
      this.recordingStarted = false;
      this.recordedInputs.clear();
      this.recordedMovementInput = null;
   }

   private JsonObject createRecordingHeader() {
      List list = this.captureSurroundingBlocks();
      JsonObject json = new JsonObject();
      JsonListPropertyCodec booleanListCodec = JsonCodecRegistryHolder.GSON_SERIALIZER_PROVIDER.getBooleanListCodec();
      String str = "Blocks";
      booleanListCodec.writeList(str, list, json);
      return json;
   }

}
