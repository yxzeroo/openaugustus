package net.augustus.modules.misc;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.EventMove;
import net.augustus.ClientManagers;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventAttackEntity;
import net.augustus.Augustus;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.events.EventManager;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class RotationHandler extends Module implements LoadWorldListener, MoveListener, MoveInputListener, PacketListener, UpdateListener, AttackEntityListener {
   public final BooleansSetting rotationMode;
   public final BooleansSetting mouseSensMode;
   public final BooleansSetting smoothBackRotate;
   public final RandomizedRangeSettings yawSpeed;
   public final RandomizedRangeSettings pitchSpeed;
   public final StringValue rotationSpeed;
   public final RandomizedRangeSettings smoothBackRotateTickDelayAfterClosingInventory;
   public final DoubleValue movement;
   public final BooleanValue moveFix;
   public final BooleansSetting silentMoveFix;
   public final BooleanValue silentMoveFixIntave;
   public final BooleanValue randomizeDirections;
   public int framesSinceLastTick;
   public int framesPerTick;

   public RotationHandler() {
       super("RotationHandler", "DrehungsVerwalter", true, new Color(168, 145, 50), Categorys.MISC);
      this.rotationMode = new BooleansSetting(this, "RotationMode", "Vanilla", new String[]{"Vanilla", "Tick"});
      this.mouseSensMode = new BooleansSetting(this, "MouseSensMode", "Advanced", new String[]{"Default", "Advanced"});
      this.smoothBackRotate = new BooleansSetting(this, "SmoothBackRotate", "None", new String[]{"None", "Default", "New"});
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "YawSpeed", 18.0, 24.0, 0.0, 180.0, 0);
      SettingVisibilityCondition condition = () -> this.isYawSpeedVisible();
      this.yawSpeed = ((RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition)).withoutNotInARow();
      RandomizedRangeSettings randomizedRangeSettings2 = new RandomizedRangeSettings(this, "PitchSpeed", 18.0, 24.0, 0.0, 180.0, 0);
      SettingVisibilityCondition condition2 = () -> this.isPitchSpeedVisible();
      this.pitchSpeed = ((RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition2)).withoutNotInARow();
      StringValue stringValue = new StringValue(this, "RotationSpeed", 75.0, 0.0, 100.0, 0);
      SettingVisibilityCondition condition3 = () -> this.isRotationSpeedVisible();
      this.rotationSpeed = stringValue.visibleWhen(condition3);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "SmoothBackRotateTickDelayAfterClosingInventory", 2.0, 4.0, 0.0, 10.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> {
         return !this.smoothBackRotate.getSelectedValue().equals("None");
      };
      this.smoothBackRotateTickDelayAfterClosingInventory = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition4);
      DoubleValue doubleValue = new DoubleValue(this, "Movement", true);
      SettingVisibilityCondition condition5 = () -> this.isMovementGroupVisible();
      this.movement = doubleValue.visibleWhen(condition5);
      BooleanValue booleanValue = new BooleanValue(this, "MoveFix", true, this.movement);
      SettingVisibilityCondition condition6 = () -> {
         return !this.smoothBackRotate.getSelectedValue().equals("None");
      };
      this.moveFix = (BooleanValue)booleanValue.withVisibility(condition6);
      BooleansSetting booleansSetting = new BooleansSetting(this, "SilentMoveFix", "Default", new String[]{"None", "Default", "Advanced"}, this.movement);
      SettingVisibilityCondition condition7 = () -> this.isSilentMoveFixVisible();
      this.silentMoveFix = (BooleansSetting)booleansSetting.withVisibility(condition7);
      BooleanValue booleanValue2 = new BooleanValue(this, "SilentMoveFixIntave", true, this.movement);
      SettingVisibilityCondition condition8 = () -> {
         return this.silentMoveFix.isVisible() && !this.silentMoveFix.getSelectedValue().equals("None");
      };
      this.silentMoveFixIntave = (BooleanValue)booleanValue2.withVisibility(condition8);
      BooleanValue booleanValue3 = new BooleanValue(this, "RandomizeDirections", false, this.movement);
      SettingVisibilityCondition condition9 = () -> this.isRandomizeDirectionsVisible();
      this.randomizeDirections = (BooleanValue)booleanValue3.withVisibility(condition9);
      this.framesSinceLastTick = 0;
      this.framesPerTick = 0;
      boolean flag = true;
      this.setToggled(flag);
      EventManager.getEventBus().subscribe(28, this, -2);
      EventManager.getEventBus().subscribe(21, this, 20);
      EventManager.getEventBus().subscribe(26, this, 20);
      EventManager.getEventBus().subscribe(43, this, 20);
      EventManager.getEventBus().subscribe(1, this, 20);
      EventManager.getEventBus().subscribe(17, this, 20);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onUpdate() {
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.moveFix.getValue() && ClientManagers.rotationController.isSmoothlyReturning()) {
         MoveFixMode moveFixMode = this.silentMoveFix.getSelectedValue().equals("None")
            ? MoveFixMode.NONE
            : (this.silentMoveFix.getSelectedValue().equals("Default") ? MoveFixMode.DEFAULT : MoveFixMode.ADVANCED);
         event.setMoveFixMode(moveFixMode);
         if (!this.silentMoveFix.getSelectedValue().equals("None")) {
            boolean value = this.silentMoveFixIntave.getValue();
            event.setPreventInstantReverse(value);
            if (this.randomizeDirections.getValue()) {
               float f = 30.0F;
               float f2 = 20.0F;
               float f3 = Augustus.getInstance().getYawPitchHelper().realYaw
                  + DefaultRandomGenerator.instance.nextFloat(f2, f) * (float)(ThreadLocalRandom.current().nextBoolean() ? 1 : -1);
               event.setYaw(f3);
            }
         }
      }

      if (ClientManagers.rotationController.isHoldingRotation()) {
         MoveFixMode moveFixMode2 = MoveFixMode.DEFAULT;
         event.setMoveFixMode(moveFixMode2);
         boolean flag = true;
         event.setPreventInstantReverse(flag);
      }
   }

   @Override
   public void onMove(EventMove event) {
      if (!this.moveFix.getValue() && ClientManagers.rotationController.isSmoothlyReturning()) {
         float movementYaw = MoveUtil.getMovementYaw();
         event.setYaw(movementYaw);
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (!this.moveFix.getValue() && ClientManagers.rotationController.isSmoothlyReturning()) {
         float movementYaw = MoveUtil.getMovementYaw();
         event.setYaw(movementYaw);
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      ClientManagers.moduleManager.killAura.clickSpeedMonitor.onPacketSend(event);
      ClientManagers.moduleManager.autoClicker.clickSpeedMonitor.onPacketSend(event);
      ClientManagers.moduleManager.killAura.onOutgoingCombatPacket(event);
   }

   @Override
   public void onPacket(EventPacket event) {
      ClientManagers.moduleManager.killAura.clickSpeedMonitor.onPacketReceive(event);
      ClientManagers.moduleManager.autoClicker.clickSpeedMonitor.onPacketReceive(event);
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
   }

   private boolean isRandomizeDirectionsVisible() {
      return this.silentMoveFix.isVisible() && !this.silentMoveFix.getSelectedValue().equals("None");
   }

   private boolean isSilentMoveFixVisible() {
      return this.moveFix.getValue() && this.moveFix.isVisible();
   }

   private boolean isMovementGroupVisible() {
      return !this.smoothBackRotate.getSelectedValue().equals("None");
   }

   private boolean isRotationSpeedVisible() {
      return this.smoothBackRotate.getSelectedValue().equals("New");
   }

   private boolean isPitchSpeedVisible() {
      return this.smoothBackRotate.getSelectedValue().equals("Default");
   }

   private boolean isYawSpeedVisible() {
      return this.smoothBackRotate.getSelectedValue().equals("Default");
   }

}
