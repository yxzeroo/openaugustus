package net.augustus.modules.misc;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.MouseMoveListener;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.rotation.BezierRotationEngine;
import net.augustus.utils.file.ClientPaths;
import net.augustus.events.EventClick;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.events.EventMouseMove;
import net.augustus.modules.misc.helper.RotationRecorderExclusionStrategy;
import net.augustus.Augustus;
import net.augustus.settings.options.RotationRecorderTargetOption;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;

public class RotationRecorder extends Module implements TickListener, ClickListener, MouseMoveListener, PacketListener {
   private final BooleanValue createNewDatasetIfTargetIsNull;
   private final BooleanValue frameRecording;
   private final BooleanValue recordPredictions;
   private final BooleanValue recordAura;
   private final BooleanValue killAuraTarget;
   private final StringValue searchReach;
   private final BooleanValue onlyOnClick;
   private final StringValue yawFov;
   private final StringValue pitchFov;
   private final MultiOptionSetting targets;
   private final TimeHelper timeHelper;
   private DataOutputStream outputStream;
   private int savedFramerateLimit;
   private BezierRotationEngine bezierRotationEngine;
   private Rotation lastRotation;
   private HashSet recordedEntityIds;
   private JsonArray recordedFrames;
   private Gson gson;
   private boolean velocityReceived;

   public RotationRecorder() {
       super("RotationRecorder", "DrehungsAufzeichner", new Color(252, 186, 3), Categorys.MISC);
      this.createNewDatasetIfTargetIsNull = new BooleanValue(this, "Create new dataset if target is null", true);
      this.frameRecording = new BooleanValue(this, "FrameRecording", true);
      BooleanValue booleanValue = new BooleanValue(this, "Record Predictions", false);
      SettingVisibilityCondition condition = () -> this.isRecordPredictionsVisible();
      this.recordPredictions = (BooleanValue)booleanValue.withVisibility(condition);
      BooleanValue booleanValue2 = new BooleanValue(this, "Record Aura", false);
      SettingVisibilityCondition condition2 = () -> this.isRecordAuraVisible();
      this.recordAura = (BooleanValue)booleanValue2.withVisibility(condition2);
      this.killAuraTarget = new BooleanValue(this, "KillAuraTarget", true);
      StringValue stringValue = new StringValue(this, "SearchReach", 7.0, 3.0, 10.0, 1);
      SettingVisibilityCondition condition3 = () -> this.isSearchReachVisible();
      this.searchReach = stringValue.visibleWhen(condition3);
      BooleanValue booleanValue3 = new BooleanValue(this, "OnlyOnClick", true);
      SettingVisibilityCondition condition4 = () -> {
         return !this.killAuraTarget.getValue();
      };
      this.onlyOnClick = (BooleanValue)booleanValue3.withVisibility(condition4);
      StringValue stringValue2 = new StringValue(this, "YawFov", 90.0, 0.0, 180.0, 0);
      SettingVisibilityCondition condition5 = () -> this.isYawFovVisible();
      this.yawFov = stringValue2.visibleWhen(condition5);
      StringValue stringValue3 = new StringValue(this, "PitchFov", 90.0, 0.0, 180.0, 0);
      SettingVisibilityCondition condition6 = () -> this.isPitchFovVisible();
      this.pitchFov = stringValue3.visibleWhen(condition6);
      MultiOptionSetting multiOptionSetting = new MultiOptionSetting(this, "Targets", RotationRecorderTargetOption.values());
      SettingVisibilityCondition condition7 = () -> {
         return !this.killAuraTarget.getValue();
      };
      this.targets = multiOptionSetting.visibleWhen(condition7);
      this.timeHelper = new TimeHelper();
      this.outputStream = null;
      this.savedFramerateLimit = 0;
      this.bezierRotationEngine = new BezierRotationEngine();
      this.recordedEntityIds = new HashSet();
      this.recordedFrames = null;
      RotationRecorderExclusionStrategy strategy = new RotationRecorderExclusionStrategy(this);
      this.gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy[]{strategy}).create();
   }

   @Override
   public void onClientInit() {
      Augustus.getInstance().getClass();
      Augustus.getInstance().getModuleRegistry().getModules().remove(this);
      Augustus.getInstance().getModuleRegistry().getSortedModules().remove(this);
   }

   @Override
   public void onEnable() {
      this.finishAndCloseRecording();
      this.savedFramerateLimit = mc.gameSettings.limitFramerate;
      mc.gameSettings.limitFramerate = 60;
   }

   @Override
   public void onDisable() {
      this.finishAndCloseRecording();
      mc.gameSettings.limitFramerate = this.savedFramerateLimit;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 7;
      this.toggleEventSubscription(b2);
      byte b3 = 19;
      this.toggleEventSubscription(b3);
      byte b4 = 26;
      this.toggleEventSubscription(b4);
   }

   @Override
   public void onTick2() {
      if (!this.frameRecording.getValue()) {
         boolean value = this.frameRecording.getValue();
         this.recordTickOrFrame(value);
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (mc.gameSettings.keyBindAttack.isKeyDown() || mc.gameSettings.keyBindAttack.getPressTime() > 0) {
         this.timeHelper.reset();
      }
   }

   @Override
   public void onMouseMove(EventMouseMove event) {
   }

   @Override
   public void onMouseMove() {
      if (this.frameRecording.getValue()) {
         boolean value = this.frameRecording.getValue();
         this.recordTickOrFrame(value);
      }
   }

   private void recordTickOrFrame(boolean flag) {
      EntityLivingBase entity = this.killAuraTarget.getValue() ? ClientManagers.moduleManager.killAura.target : this.findTargetEntity();
      if (entity == null) {
         if (this.createNewDatasetIfTargetIsNull.getValue() && this.outputStream != null) {
            this.finishAndCloseRecording();
         }
      } else {
         if (this.outputStream == null) {
            this.openNewRecordingFile();
         }

         if (flag) {
            this.recordRenderFrame(entity);
         } else {
            this.recordTickSnapshot(entity);
         }
      }
   }

   private void writeCompressedFrames() {

      try {
         this.outputStream.write(compressData(this.recordedFrames.toString().getBytes(StandardCharsets.UTF_8)));
         this.outputStream.flush();
         this.recordedFrames = null;
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private static byte[] compressData(byte[] bytes) {
      Deflater deflater = new Deflater(9, true);
      deflater.setInput(bytes);
      deflater.finish();
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] bytes2 = new byte[1024];

      while (!deflater.finished()) {
         int n = deflater.deflate(bytes2);
         byteArrayOutputStream.write(bytes2, 0, n);
      }

      return byteArrayOutputStream.toByteArray();
   }

   private void closeOutputStream() {
      try {
         this.outputStream.close();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void recordRenderFrame(EntityLivingBase entity) {
      JsonObject json = new JsonObject();
      json.addProperty("ticksExisted", mc.thePlayer.ticksExisted);
      json.addProperty("yaw", mc.thePlayer.rotationYaw);
      json.addProperty("pitch", mc.thePlayer.rotationPitch);
      Vec3 interpolatedPos = mc.thePlayer.getInterpolatedPos(mc.getTimer().renderPartialTicks);
      json.addProperty("playerX", interpolatedPos.xCoord);
      json.addProperty("playerY", interpolatedPos.yCoord);
      json.addProperty("playerZ", interpolatedPos.zCoord);
      interpolatedPos = entity.getInterpolatedPos(mc.getTimer().renderPartialTicks);
      json.addProperty("targetX", interpolatedPos.xCoord);
      json.addProperty("targetY", interpolatedPos.yCoord);
      json.addProperty("targetZ", interpolatedPos.zCoord);
      this.recordedFrames.add(json);
   }

   private void recordTickSnapshot(EntityLivingBase entity) {
      if (entity.motionSnapshotHistory.size() != 0) {
         JsonObject json = new JsonObject();
         json.addProperty("ticksExisted", mc.thePlayer.ticksExisted);
         json.addProperty("yaw", mc.thePlayer.rotationYaw);
         json.addProperty("pitch", mc.thePlayer.rotationPitch);
         json.addProperty("playerX", mc.thePlayer.posX);
         json.addProperty("playerY", mc.thePlayer.posY);
         json.addProperty("playerZ", mc.thePlayer.posZ);
         json.addProperty("targetX", entity.posX);
         json.addProperty("targetY", entity.posY);
         json.addProperty("targetZ", entity.posZ);
         json.addProperty("applyVelocity", this.velocityReceived);
         json.addProperty("AI_MOVESPEED", mc.thePlayer.getAIMoveSpeed());
         json.addProperty("jumpMovementFactor", mc.thePlayer.jumpMovementFactor);
         json.addProperty("motionX", mc.thePlayer.motionX);
         json.addProperty("motionY", mc.thePlayer.motionY);
         json.addProperty("motionZ", mc.thePlayer.motionZ);
         json.addProperty("isSprinting", mc.thePlayer.isSprinting());
         byte b = 0;
         json.add("entityState", this.gson.toJsonTree(entity.motionSnapshotHistory.getFromEnd(b)));
         json.add("movementInput", this.gson.toJsonTree(mc.thePlayer.movementInput));
         byte b2 = 0;
         json.add("playerState", this.gson.toJsonTree(mc.thePlayer.extraSnapshotHistory.getFromEnd(b2)));
         byte b3 = 0;
         json.add("targetState", this.gson.toJsonTree(entity.positionHistory.getFromEnd(b3)));
         this.recordedFrames.add(json);
         this.velocityReceived = false;
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      if (event.getPacket() instanceof S12PacketEntityVelocity) {
         S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
         if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
            this.velocityReceived = true;
         }
      } else if (event.getPacket() instanceof S27PacketExplosion) {
         S27PacketExplosion packet2 = (S27PacketExplosion)event.getPacket();
         if (Math.abs(packet2.getMotionX()) > 0.0F && Math.abs(packet2.getMotionY()) > 0.0F) {
            this.velocityReceived = true;
         }
      }
   }

   private void openNewRecordingFile() {
      this.recordedFrames = new JsonArray();
      this.velocityReceived = false;
      String str = ClientPaths.CLIENT_DIR + File.separator + "rotation";
      ClientFileUtil.ensureDirectory(str);

      try {
         File file = new File(str + File.separator + "data" + System.currentTimeMillis());
         file.createNewFile();
         this.outputStream = new DataOutputStream(Files.newOutputStream(file.toPath()));
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void finishAndCloseRecording() {
      if (this.outputStream != null) {
         this.writeCompressedFrames();
         this.closeOutputStream();
         this.outputStream = null;
         this.recordedEntityIds.clear();
      }
   }

   private EntityLivingBase findTargetEntity() {
      if (mc.isNotInGui()) {
         long l = 1000L;
         if (!this.timeHelper.reached2(l) || !this.onlyOnClick.getValue()) {
            Stream stream = mc.theWorld.loadedEntityList.stream();
            stream = stream.filter(
               obj -> {
                  if (obj instanceof EntityLivingBase) {
                     boolean isSelected = RotationRecorderTargetOption.PLAYER.isSelected();
                     boolean isSelected2 = RotationRecorderTargetOption.MOB.isSelected();
                     boolean isSelected3 = RotationRecorderTargetOption.ANIMAL.isSelected();
                     boolean isSelected4 = RotationRecorderTargetOption.VILLAGER.isSelected();
                     boolean isSelected5 = RotationRecorderTargetOption.ARMOR_STAND.isSelected();
                     boolean isSelected6 = RotationRecorderTargetOption.INVISIBLE.isSelected();
                     boolean isSelected7 = RotationRecorderTargetOption.DEAD.isSelected();
                     boolean flag = true;
                     boolean flag2 = isSelected7;
                     boolean flag3 = true;
                     boolean flag4 = true;
                     boolean flag5 = true;
                     boolean flag6 = isSelected6;
                     boolean flag7 = isSelected5;
                     boolean flag8 = isSelected4;
                     boolean flag9 = isSelected3;
                     boolean flag10 = isSelected2;
                     boolean flag11 = isSelected;
                     if (CombatTargetUtil.isValidTarget((Entity)obj, flag11, flag10, flag9, flag8, flag7, flag6, flag5, flag4, flag3, flag2, flag)) {
                        float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
                        float realPitch = Augustus.getInstance().getYawPitchHelper().realPitch;
                        float f = realYaw;
                        float[] floats = RotationUtil.getAngleDiffToEntityNearestPoint((Entity)obj, f, realPitch);
                        return !(obj instanceof EntityPlayerSP)
                           && (double)mc.thePlayer.getDistanceToEntityBox((Entity)obj) < this.searchReach.getValue()
                           && (double)Math.abs(floats[0]) <= this.yawFov.getValue()
                           && (double)Math.abs(floats[1]) <= this.pitchFov.getValue();
                     }
                  }

                  return false;
               }
            );
            Object[] array = stream.sorted(
                  Comparator.comparingDouble(
                     obj2 -> {
                        if (obj2 instanceof EntityLivingBase) {
                           float[] angleDiffToPos = RotationUtil.getAngleDiffToPos(
                              ((Entity)obj2).posX,
                              ((Entity)obj2).posY + (double)((Entity)obj2).getEyeHeight(),
                              ((Entity)obj2).posZ,
                              Augustus.getInstance().getYawPitchHelper().realYaw,
                              Augustus.getInstance().getYawPitchHelper().realPitch
                           );
                           return Math.sqrt((double)(angleDiffToPos[0] * angleDiffToPos[0] + angleDiffToPos[1] * angleDiffToPos[1]));
                        } else {
                           return 1000.0;
                        }
                     }
                  )
               )
               .toArray();
            if (array.length == 0) {
               return null;
            }

            return (EntityLivingBase)array[0];
         }
      }

      return null;
   }

   public static byte[] decompressData(byte[] bytes) {

      try {
         Inflater inflater = new Inflater(true);
         inflater.setInput(bytes);
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         byte[] bytes2 = new byte[1024];

         while (!inflater.finished()) {
            int n = inflater.inflate(bytes2);
            byteArrayOutputStream.write(bytes2, 0, n);
         }

         inflater.end();
         return byteArrayOutputStream.toByteArray();
      } catch (DataFormatException e) {
         return null;
      }
   }

   private boolean isPitchFovVisible() {
      return !this.killAuraTarget.getValue();
   }

   private boolean isYawFovVisible() {
      return !this.killAuraTarget.getValue();
   }

   private boolean isSearchReachVisible() {
      return !this.killAuraTarget.getValue();
   }

   private boolean isRecordAuraVisible() {
      return !this.frameRecording.getValue();
   }

   private boolean isRecordPredictionsVisible() {
      return !this.frameRecording.getValue();
   }

}
