package net.augustus.modules.modes.motion;

import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventPacket;
import net.augustus.events.EventPacketDelay;
import net.augustus.modules.modes.MotionModifierRuleSettings;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.utils.player.PlayerUtil;
import java.util.HashMap;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;

public class MotionModifierEngine {
   private final BooleanValue blockJumpInput;
   private boolean moveEntityAirLooped;
   private final HashMap rulesByPhase;
   private final BooleanValue onlyWhileJumpKeyPressed;
   private boolean preUpdateAirLooped;
   private int moveEntityAirTicks;
   private boolean moveEntityGroundLooped;
   private boolean preUpdateGroundLooped;
   private int moveEntityGroundTicks;
   private final BooleanValue waitForDamage;
   private final BooleanValue onlyAtLadder;
   private boolean damageConditionMet;
   private final BooleanValue onlyInWeb;
   private int preUpdateGroundTicks;
   private boolean initialSelfDamageApplied;
   private int threeJumpCounter;
   private final MotionModifierRuleSettings motionModifierRuleSettings;
   private final Module ownerModule;
   private boolean postUpdateGroundLooped;
   private int postUpdateGroundTicks;
   private final BooleanValue onlyInWater;
   private int postUpdateAirTicks;
   private final BooleanValue onlyWhileMoving;
   private boolean postUpdateAirLooped;
   private int selfDamageStep;
   private int selfDamageTicks;
   private int preUpdateAirTicks;

   public void onSendPacket(EventPacket event) {
      if (this.motionModifierRuleSettings.selfDamageMode.getSelectedValue().equals("3Jump") && this.threeJumpCounter < 4) {
         Packet packet = event.getPacket();
         if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer packet2 = (C03PacketPlayer)packet;
            packet2.setOnGround(false);
         }
      }
   }

   public void onMoveEntity(EventMoveEntity event) {
      if ((MoveUtil.isMoving() || !this.onlyWhileMoving.getValue())
         && (!this.onlyWhileJumpKeyPressed.getValue() || MC.mc.gameSettings.keyBindJump.isKeyDown())
         && (!this.onlyInWeb.getValue() || MC.mc.thePlayer.isInWeb())
         && (!this.onlyInWater.getValue() || MC.mc.thePlayer.isInWater())
         && (!this.onlyAtLadder.getValue() || this.isOnLadder())
         && this.damageConditionMet) {
         HashMap map = (HashMap)this.rulesByPhase.get("MoveEntity");
         if (event.getY() < 0.0) {
            HashMap map2 = (HashMap)map.get("MotionYLessZero");
            MotionModifierRuleSettings settings = (MotionModifierRuleSettings)map2.get("MotionYLessZero");
            MotionModifierRuleSettings settings2 = settings;
            this.applyMoveEntityRule(settings2, event);
         } else {
            HashMap map3 = (HashMap)map.get("MotionYHigherZero");
            MotionModifierRuleSettings settings3 = (MotionModifierRuleSettings)map3.get("MotionYHigherZero");
            MotionModifierRuleSettings settings4 = settings3;
            this.applyMoveEntityRule(settings4, event);
         }

         HashMap map4 = (HashMap)map.get("TicksOnGround");
         HashMap map5 = (HashMap)map.get("TicksInAir");
         if (MC.mc.thePlayer.onGround) {
            this.moveEntityGroundTicks++;
            MotionModifierRuleSettings settings5 = (MotionModifierRuleSettings)map5.get(-1);
            if (settings5.resetAtOppositeState.getValue()) {
               this.moveEntityAirTicks = -1;
            }

            if (this.moveEntityGroundTicks >= 0) {
               MotionModifierRuleSettings settings6 = (MotionModifierRuleSettings)map4.get(-1);
               double moveEntityGroundTicks2 = (double)this.moveEntityGroundTicks;
               if (moveEntityGroundTicks2 > settings6.resetTicksAt.getMaxValue() || (double)this.moveEntityGroundTicks < settings6.resetTicksAt.getMinValue() && this.moveEntityGroundLooped) {
                  this.moveEntityGroundTicks = (int)Math.max(0.0, settings6.resetTicksAt.getMinValue());
                  this.moveEntityGroundLooped = true;
               }

               MotionModifierRuleSettings settings7 = (MotionModifierRuleSettings)map4.get(this.moveEntityGroundTicks);
               MotionModifierRuleSettings settings8 = settings7;
               this.applyMoveEntityRule(settings8, event);
            }
         } else {
            this.moveEntityAirTicks++;
            MotionModifierRuleSettings settings9 = (MotionModifierRuleSettings)map4.get(-1);
            if (settings9.resetAtOppositeState.getValue()) {
               this.moveEntityGroundTicks = -1;
            }

            if (this.moveEntityAirTicks >= 0) {
               MotionModifierRuleSettings settings10 = (MotionModifierRuleSettings)map5.get(-1);
               double moveEntityAirTicks2 = (double)this.moveEntityAirTicks;
               if (moveEntityAirTicks2 > settings10.resetTicksAt.getMaxValue() || (double)this.moveEntityAirTicks < settings10.resetTicksAt.getMinValue() && this.moveEntityAirLooped) {
                  this.moveEntityAirTicks = (int)Math.max(0.0, settings10.resetTicksAt.getMinValue());
                  this.moveEntityAirLooped = true;
               }

               MotionModifierRuleSettings settings11 = (MotionModifierRuleSettings)map5.get(this.moveEntityAirTicks);
               MotionModifierRuleSettings settings12 = settings11;
               this.applyMoveEntityRule(settings12, event);
            }
         }
      }
   }

   public MotionModifierEngine(Module module) {
       super();
      this.rulesByPhase = new HashMap();
      this.preUpdateGroundTicks = 0;
      this.preUpdateGroundLooped = false;
      this.preUpdateAirTicks = 0;
      this.preUpdateAirLooped = false;
      this.postUpdateGroundTicks = 0;
      this.postUpdateGroundLooped = false;
      this.postUpdateAirTicks = 0;
      this.postUpdateAirLooped = false;
      this.moveEntityGroundTicks = 0;
      this.moveEntityGroundLooped = false;
      this.moveEntityAirTicks = 0;
      this.moveEntityAirLooped = false;
      this.damageConditionMet = false;
      this.initialSelfDamageApplied = false;
      this.selfDamageStep = 0;
      this.selfDamageTicks = 0;
      this.threeJumpCounter = 0;
      this.ownerModule = module;
      this.blockJumpInput = new BooleanValue(this.ownerModule, "BlockJumpInput", true);
      this.onlyWhileMoving = new BooleanValue(this.ownerModule, "OnlyWhileMoving", true);
      this.onlyWhileJumpKeyPressed = new BooleanValue(this.ownerModule, "OnlyWhileJumpKeyPressed", false);
      this.onlyInWater = new BooleanValue(this.ownerModule, "OnlyInWater", false);
      this.onlyInWeb = new BooleanValue(this.ownerModule, "OnlyInWeb", false);
      this.onlyAtLadder = new BooleanValue(this.ownerModule, "OnlyAtLadder", false);
      this.waitForDamage = new BooleanValue(this.ownerModule, "WaitForDamage", false);
      this.motionModifierRuleSettings = new MotionModifierRuleSettings(this.ownerModule, "SelfDamageSavings", new DoubleValue(module, "SelfDamageStart"), 0.0);
   }

   public void onEnable() {
      this.preUpdateAirTicks = -1;
      this.preUpdateGroundTicks = -1;
      this.postUpdateAirTicks = -1;
      this.postUpdateGroundTicks = -1;
      this.moveEntityAirTicks = -1;
      this.moveEntityGroundTicks = -1;
      this.moveEntityAirLooped = false;
      this.preUpdateAirLooped = false;
      this.postUpdateAirLooped = false;
      this.moveEntityGroundLooped = false;
      this.postUpdateGroundLooped = false;
      this.preUpdateGroundLooped = false;
      this.damageConditionMet = !this.waitForDamage.getValue();
      this.initialSelfDamageApplied = false;
      this.selfDamageStep = 0;
      this.selfDamageTicks = 0;
      this.threeJumpCounter = 0;
      String str = "OnEnable";
      this.applyLifecyclePhaseRule(str);
   }

   private void applyMoveEntityRule(MotionModifierRuleSettings settings, EventMoveEntity event) {
      if (MC.mc.thePlayer != null && settings.editTick.getValue()) {
         if ((!settings.notWhileKB.getValue() || MC.mc.thePlayer.hurtTime <= 0)
            && (!settings.onlyInWeb.getValue() || MC.mc.thePlayer.isInWeb())
            && (!settings.onlyInWater.getValue() || MC.mc.thePlayer.isInWater())
            && (!settings.onlyAtLadder.getValue() || this.isOnLadder())) {
            float f = MathHelper.clamp_float(0.0F, (float)settings.strafePercentage.getValue(), 100.0F);
            if (settings.editStaticY.getValue()) {
               RandomNumberGenerator instance = DefaultRandomGenerator.instance;
               double minValue = settings.staticY.getMinValue();
               double maxValue = settings.staticY.getMaxValue();
               double d = minValue;
               double d2 = (double)instance.nextFloat2(d, maxValue);
               event.setY(d2);
            }

            if (settings.jump.getValue()) {
               MC.mc.thePlayer.jump();
            }

            this.applySelfDamage(settings);
            if (settings.spoofGround.getValue()) {
               MC.mc.thePlayer.onGround = true;
            }

            if (settings.editStaticXZ.getValue()) {
               RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
               double minValue2 = settings.staticXZ.getMinValue();
               double maxValue2 = settings.staticXZ.getMaxValue();
               double d3 = maxValue2;
               double d4 = minValue2;
               double d5 = instance2.nextDouble(d4, d3);
               if (f == 0.0F) {
                  MoveUtil.setEventSpeed(d5, false, event);
               } else {
                  MoveUtil.strafeEvent(d5, f / 100.0F, event);
               }
            }

            if (settings.editAddY.getValue()) {
               double y = event.getY();
               RandomNumberGenerator instance3 = DefaultRandomGenerator.instance;
               double minValue3 = settings.addY.getMinValue();
               double maxValue3 = settings.addY.getMaxValue();
               double d6 = minValue3;
               double d7 = y + (double)instance3.nextFloat2(d6, maxValue3);
               event.setY(d7);
            }

            if (settings.editAddXZ.getValue()) {
               RandomNumberGenerator instance4 = DefaultRandomGenerator.instance;
               double minValue4 = settings.addXZ.getMinValue();
               double maxValue4 = settings.addXZ.getMaxValue();
               double d8 = maxValue4;
               double d9 = minValue4;
               double d10 = instance4.nextDouble(d9, d8);
               MoveUtil.addEventMotion(d10, false, event);
            }

            if (settings.editMultiplyY.getValue()) {
               double y2 = event.getY();
               RandomNumberGenerator instance5 = DefaultRandomGenerator.instance;
               double minValue5 = settings.multiplyY.getMinValue();
               double maxValue5 = settings.multiplyY.getMaxValue();
               double d11 = minValue5;
               double d12 = y2 * (double)instance5.nextFloat2(d11, maxValue5);
               event.setY(d12);
            }

            if (settings.editMultiplyXZ.getValue()) {
               RandomNumberGenerator instance6 = DefaultRandomGenerator.instance;
               double minValue6 = settings.multiplyXZ.getMinValue();
               double maxValue6 = settings.multiplyXZ.getMaxValue();
               double d13 = maxValue6;
               double d14 = minValue6;
               double d15 = instance6.nextDouble(d14, d13);
               double d16 = event.getX() * d15;
               event.setX(d16);
               double d17 = event.getZ() * d15;
               event.setZ(d17);
            }

            if (settings.editTimerSpeed.getValue()) {
               Timer timer = MC.mc.getTimer();
               RandomNumberGenerator instance7 = DefaultRandomGenerator.instance;
               double minValue7 = settings.timerSpeed.getMinValue();
               double maxValue7 = settings.timerSpeed.getMaxValue();
               double d18 = minValue7;
               timer.timerSpeed = Math.max(0.01F, instance7.nextFloat2(d18, maxValue7));
            }

            if (f > 0.0F) {
               MoveUtil.strafeEvent(Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ()), f / 100.0F, event);
            }
         }
      }
   }

   public void onOutgoingPacketDelay(EventPacketDelay event) {
      if (this.motionModifierRuleSettings.selfDamageMode.getSelectedValue().equals("3Jump") && this.threeJumpCounter < 4) {
         boolean flag = true;
         event.setDelayed(flag);
      }
   }

   public void initRuleSettings() {
      String[] strings = new String[]{"PreUpdate", "PostUpdate", "MoveEntity"};
      String[] strings2 = new String[]{"TicksOnGround", "TicksInAir"};
      String[] strings3 = new String[]{"MotionYLessZero", "MotionYHigherZero"};
      String[] strings4 = new String[]{"OnEnable", "OnDisable"};

      for (String str : strings) {
         DoubleValue doubleValue = new DoubleValue(this.ownerModule, str);
         HashMap map = new HashMap();
         this.rulesByPhase.put(str, map);

         for (String str2 : strings3) {
            HashMap map2 = new HashMap();
            map.put(str2, map2);
            DoubleValue doubleValue2 = (DoubleValue)(new DoubleValue(this.ownerModule, str2 + str, doubleValue)).withDisplayName(str2);
            map2.put(str2, new MotionModifierRuleSettings(this.ownerModule, str2 + str, doubleValue2, str.equals("PreUpdate")));
         }

         for (String str3 : strings2) {
            HashMap map3 = new HashMap();
            map.put(str3, map3);
            DoubleValue doubleValue3 = (DoubleValue)(new DoubleValue(this.ownerModule, str3 + str, doubleValue)).withDisplayName(str3);
            map3.put(-1, new MotionModifierRuleSettings(this.ownerModule, str3 + str, doubleValue3));

            for (int i = 0; i < 60; i++) {
               String str4 = "Tick" + i;
               DoubleValue doubleValue4 = (DoubleValue)(new DoubleValue(this.ownerModule, str4 + str3 + str, doubleValue3)).withDisplayName(str4);
               Integer num = i;
               Module ownerModule2 = this.ownerModule;
               String str5 = str4 + str3 + str;
               boolean flag = str.equals("PreUpdate") && str3.equals("TicksInAir");
               map3.put(num, new MotionModifierRuleSettings(ownerModule2, str5, doubleValue4, flag));
            }
         }
      }

      for (String str6 : strings4) {
         HashMap map4 = new HashMap();
         this.rulesByPhase.put(str6, map4);
         HashMap map5 = new HashMap();
         map4.put(str6, map5);
         DoubleValue doubleValue5 = new DoubleValue(this.ownerModule, str6);
         map5.put(str6, new MotionModifierRuleSettings(this.ownerModule, str6, doubleValue5, str6.equals("OnEnable")));
      }
   }

   public void onDisable() {
      MC.mc.getTimer().timerSpeed = 1.0F;
      if (MC.mc.thePlayer != null) {
         MC.mc.thePlayer.setSpeedInAir(0.02F);
      }

      String str = "OnDisable";
      this.applyLifecyclePhaseRule(str);
   }

   public void onPostUpdate() {
      if (this.motionModifierRuleSettings.selfDamageMode.getSelectedValue().equals("3Jump")) {
         if (MC.mc.thePlayer.onGround) {
            if (this.threeJumpCounter < 3) {
               MC.mc.thePlayer.jump();
               PlayerUtil.triggerJumpStat();
            }

            this.threeJumpCounter++;
         }

         this.initialSelfDamageApplied = true;
      }

      if ((MoveUtil.isMoving() || !this.onlyWhileMoving.getValue())
         && (this.onlyWhileJumpKeyPressed == null || !this.onlyWhileJumpKeyPressed.getValue() || MC.mc.gameSettings.keyBindJump.isKeyDown())
         && (!this.onlyInWeb.getValue() || MC.mc.thePlayer.isInWeb())
         && (!this.onlyInWater.getValue() || MC.mc.thePlayer.isInWater())
         && (!this.onlyAtLadder.getValue() || this.isOnLadder())
         && this.damageConditionMet) {
         HashMap map = (HashMap)this.rulesByPhase.get("PostUpdate");
         if (MC.mc.thePlayer.motionY < 0.0) {
            HashMap map2 = (HashMap)map.get("MotionYLessZero");
            MotionModifierRuleSettings settings = (MotionModifierRuleSettings)map2.get("MotionYLessZero");
            this.applyUpdateRule(settings);
         } else {
            HashMap map3 = (HashMap)map.get("MotionYHigherZero");
            MotionModifierRuleSettings settings2 = (MotionModifierRuleSettings)map3.get("MotionYHigherZero");
            this.applyUpdateRule(settings2);
         }

         HashMap map4 = (HashMap)map.get("TicksOnGround");
         HashMap map5 = (HashMap)map.get("TicksInAir");
         if (MC.mc.thePlayer.onGround) {
            this.postUpdateGroundTicks++;
            MotionModifierRuleSettings settings3 = (MotionModifierRuleSettings)map5.get(-1);
            if (settings3.resetAtOppositeState.getValue()) {
               this.postUpdateAirTicks = -1;
            }

            if (this.postUpdateGroundTicks >= 0) {
               MotionModifierRuleSettings settings4 = (MotionModifierRuleSettings)map4.get(-1);
               double postUpdateGroundTicks2 = (double)this.postUpdateGroundTicks;
               if (postUpdateGroundTicks2 > settings4.resetTicksAt.getMaxValue() || (double)this.postUpdateGroundTicks < settings4.resetTicksAt.getMinValue() && this.postUpdateGroundLooped) {
                  this.postUpdateGroundTicks = (int)Math.max(0.0, settings4.resetTicksAt.getMinValue());
                  this.postUpdateGroundLooped = true;
               }

               MotionModifierRuleSettings settings5 = (MotionModifierRuleSettings)map4.get(this.postUpdateGroundTicks);
               this.applyUpdateRule(settings5);
            }
         } else {
            this.postUpdateAirTicks++;
            MotionModifierRuleSettings settings6 = (MotionModifierRuleSettings)map4.get(-1);
            if (settings6.resetAtOppositeState.getValue()) {
               this.postUpdateGroundTicks = -1;
            }

            if (this.postUpdateAirTicks >= 0) {
               MotionModifierRuleSettings settings7 = (MotionModifierRuleSettings)map5.get(-1);
               double postUpdateAirTicks2 = (double)this.postUpdateAirTicks;
               if (postUpdateAirTicks2 > settings7.resetTicksAt.getMaxValue() || (double)this.postUpdateAirTicks < settings7.resetTicksAt.getMinValue() && this.postUpdateAirLooped) {
                  this.postUpdateAirTicks = (int)Math.max(0.0, settings7.resetTicksAt.getMinValue());
                  this.postUpdateAirLooped = true;
               }

               MotionModifierRuleSettings settings8 = (MotionModifierRuleSettings)map5.get(this.postUpdateAirTicks);
               this.applyUpdateRule(settings8);
            }
         }
      }
   }

   public void onPreUpdate() {
      if (!this.initialSelfDamageApplied && (MC.mc.thePlayer.onGround || !this.motionModifierRuleSettings.onlyOnGround.getValue()) && !this.motionModifierRuleSettings.selfDamageMode.getSelectedValue().equals("3Jump")) {
         MotionModifierRuleSettings settings = this.motionModifierRuleSettings;
         this.applySelfDamage(settings);
         this.initialSelfDamageApplied = true;
      }

      if (MC.mc.thePlayer.hurtTime != 0 || !this.waitForDamage.getValue()) {
         this.damageConditionMet = true;
      }

      if ((MoveUtil.isMoving() || !this.onlyWhileMoving.getValue())
         && (this.onlyWhileJumpKeyPressed == null || !this.onlyWhileJumpKeyPressed.getValue() || MC.mc.gameSettings.keyBindJump.isKeyDown())
         && (!this.onlyInWeb.getValue() || MC.mc.thePlayer.isInWeb())
         && (!this.onlyInWater.getValue() || MC.mc.thePlayer.isInWater())
         && (!this.onlyAtLadder.getValue() || this.isOnLadder())
         && this.damageConditionMet) {
         HashMap map = (HashMap)this.rulesByPhase.get("PreUpdate");
         if (MC.mc.thePlayer.motionY < 0.0) {
            HashMap map2 = (HashMap)map.get("MotionYLessZero");
            MotionModifierRuleSettings settings2 = (MotionModifierRuleSettings)map2.get("MotionYLessZero");
            this.applyUpdateRule(settings2);
         } else {
            HashMap map3 = (HashMap)map.get("MotionYHigherZero");
            MotionModifierRuleSettings settings3 = (MotionModifierRuleSettings)map3.get("MotionYHigherZero");
            this.applyUpdateRule(settings3);
         }

         HashMap map4 = (HashMap)map.get("TicksOnGround");
         HashMap map5 = (HashMap)map.get("TicksInAir");
         if (MC.mc.thePlayer.onGround) {
            MotionModifierRuleSettings settings4 = (MotionModifierRuleSettings)map5.get(-1);
            this.preUpdateGroundTicks++;
            if (settings4.resetAtOppositeState.getValue()) {
               this.preUpdateAirTicks = -1;
            }

            if (this.preUpdateGroundTicks >= 0) {
               MotionModifierRuleSettings settings5 = (MotionModifierRuleSettings)map4.get(-1);
               double preUpdateGroundTicks2 = (double)this.preUpdateGroundTicks;
               if (preUpdateGroundTicks2 > settings5.resetTicksAt.getMaxValue() || (double)this.preUpdateGroundTicks < settings5.resetTicksAt.getMinValue() && this.preUpdateGroundLooped) {
                  this.preUpdateGroundTicks = (int)Math.max(0.0, settings5.resetTicksAt.getMinValue());
                  this.preUpdateGroundLooped = true;
               }

               MotionModifierRuleSettings settings6 = (MotionModifierRuleSettings)map4.get(this.preUpdateGroundTicks);
               this.applyUpdateRule(settings6);
            }
         } else {
            this.preUpdateAirTicks++;
            MotionModifierRuleSettings settings7 = (MotionModifierRuleSettings)map4.get(-1);
            if (settings7.resetAtOppositeState.getValue()) {
               this.preUpdateGroundTicks = -1;
            }

            if (this.preUpdateAirTicks >= 0) {
               MotionModifierRuleSettings settings8 = (MotionModifierRuleSettings)map5.get(-1);
               double preUpdateAirTicks2 = (double)this.preUpdateAirTicks;
               if (preUpdateAirTicks2 > settings8.resetTicksAt.getMaxValue() || (double)this.preUpdateAirTicks < settings8.resetTicksAt.getMinValue() && this.preUpdateAirLooped) {
                  this.preUpdateAirTicks = (int)Math.max(0.0, settings8.resetTicksAt.getMinValue());
                  this.preUpdateAirLooped = true;
               }

               MotionModifierRuleSettings settings9 = (MotionModifierRuleSettings)map5.get(this.preUpdateAirTicks);
               this.applyUpdateRule(settings9);
            }
         }
      }
   }

   public boolean isOnLadder() {
      return MC.mc.thePlayer.isOnLadder();
   }

   private void applyUpdateRule(MotionModifierRuleSettings settings) {
      if (settings != null && MC.mc.thePlayer != null && settings.editTick.getValue()) {
         if ((!settings.notWhileKB.getValue() || MC.mc.thePlayer.hurtTime <= 0)
            && (settings.onlyInWeb == null || !settings.onlyInWeb.getValue() || MC.mc.thePlayer.isInWeb())
            && (settings.onlyInWater == null || !settings.onlyInWater.getValue() || MC.mc.thePlayer.isInWater())
            && (settings.onlyAtLadder == null || !settings.onlyAtLadder.getValue() || this.isOnLadder())) {
            float f = MathHelper.clamp_float(0.0F, (float)settings.strafePercentage.getValue(), 100.0F);
            if (settings.jump != null && settings.jump.getValue()) {
               MC.mc.thePlayer.jump();
            }

            this.applySelfDamage(settings);
            if (settings.editSpeedInAir != null && settings.editSpeedInAir.getValue()) {
               EntityPlayerSP thePlayer = MC.mc.thePlayer;
               RandomNumberGenerator instance = DefaultRandomGenerator.instance;
               double minValue = settings.speedInAir.getMinValue();
               double maxValue = settings.speedInAir.getMaxValue();
               double d = minValue;
               thePlayer.setSpeedInAir(instance.nextFloat2(d, maxValue));
            } else {
               MC.mc.thePlayer.setSpeedInAir(0.02F);
            }

            if (settings.spoofGround != null && settings.spoofGround.getValue()) {
               MC.mc.thePlayer.onGround = true;
            }

            if (settings.editStaticY != null && settings.editStaticY.getValue()) {
               EntityPlayerSP thePlayer2 = MC.mc.thePlayer;
               RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
               double minValue2 = settings.staticY.getMinValue();
               double maxValue2 = settings.staticY.getMaxValue();
               double d2 = minValue2;
               thePlayer2.motionY = (double)instance2.nextFloat2(d2, maxValue2);
            }

            if (settings.editStaticXZ != null && settings.editStaticXZ.getValue()) {
               RandomNumberGenerator instance3 = DefaultRandomGenerator.instance;
               double minValue3 = settings.staticXZ.getMinValue();
               double maxValue3 = settings.staticXZ.getMaxValue();
               double d3 = minValue3;
               float f2 = instance3.nextFloat2(d3, maxValue3);
               if (f == 0.0F) {
                  MoveUtil.setSpeed(f2, false);
               } else {
                  MoveUtil.strafe((double)f2, f / 100.0F);
               }
            }

            if (settings.editAddY != null && settings.editAddY.getValue()) {
               EntityPlayerSP thePlayer3 = MC.mc.thePlayer;
               double motionY = MC.mc.thePlayer.motionY;
               RandomNumberGenerator instance4 = DefaultRandomGenerator.instance;
               double minValue4 = settings.addY.getMinValue();
               double maxValue4 = settings.addY.getMaxValue();
               double d4 = minValue4;
               thePlayer3.motionY = motionY + (double)instance4.nextFloat2(d4, maxValue4);
            }

            if (settings.editAddXZ != null && settings.editAddXZ.getValue()) {
               RandomNumberGenerator instance5 = DefaultRandomGenerator.instance;
               double minValue5 = settings.addXZ.getMinValue();
               double maxValue5 = settings.addXZ.getMaxValue();
               double d5 = maxValue5;
               double d6 = minValue5;
               double d7 = instance5.nextDouble(d6, d5);
               MoveUtil.addMotion(d7, false);
            }

            if (settings.editMultiplyY != null && settings.editMultiplyY.getValue()) {
               EntityPlayerSP thePlayer4 = MC.mc.thePlayer;
               double motionY2 = MC.mc.thePlayer.motionY;
               RandomNumberGenerator instance6 = DefaultRandomGenerator.instance;
               double minValue6 = settings.multiplyY.getMinValue();
               double maxValue6 = settings.multiplyY.getMaxValue();
               double d8 = minValue6;
               thePlayer4.motionY = motionY2 * (double)instance6.nextFloat2(d8, maxValue6);
            }

            if (settings.editMultiplyXZ != null && settings.editMultiplyXZ.getValue()) {
               RandomNumberGenerator instance7 = DefaultRandomGenerator.instance;
               double minValue7 = settings.multiplyXZ.getMinValue();
               double maxValue7 = settings.multiplyXZ.getMaxValue();
               double d9 = minValue7;
               double d10 = (double)instance7.nextFloat2(d9, maxValue7);
               MC.mc.thePlayer.motionX *= d10;
               MC.mc.thePlayer.motionZ *= d10;
            }

            if (settings.editTimerSpeed != null && settings.editTimerSpeed.getValue()) {
               Timer timer = MC.mc.getTimer();
               RandomNumberGenerator instance8 = DefaultRandomGenerator.instance;
               double minValue8 = settings.timerSpeed.getMinValue();
               double maxValue8 = settings.timerSpeed.getMaxValue();
               double d11 = minValue8;
               timer.timerSpeed = Math.max(0.01F, instance8.nextFloat2(d11, maxValue8));
            }

            if (f > 0.0F) {
               MoveUtil.strafe(f / 100.0F);
            }
         }
      }
   }

   private void applyLifecyclePhaseRule(String str) {

      try {
         HashMap map = (HashMap)this.rulesByPhase.get(str);
         HashMap map2 = (HashMap)map.get(str);
         MotionModifierRuleSettings settings = (MotionModifierRuleSettings)map2.get(str);
         this.applyUpdateRule(settings);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void onMoveInput(EventMoveInput event) {
      if (this.blockJumpInput.getValue()) {
         boolean flag = false;
         event.setJump(flag);
      }

      if (!this.damageConditionMet && this.motionModifierRuleSettings.blockMovementInput.getValue() && this.motionModifierRuleSettings.selfDamageMode.getSelectedValue().equals("3Jump")) {
         float f = 0.0F;
         event.setMoveStrafe(f);
         boolean flag2 = false;
         event.setJump(flag2);
         float f2 = 0.0F;
         event.setMoveForward(f2);
      }
   }

   private void applySelfDamage(MotionModifierRuleSettings settings) {
      if (!settings.selfDamageMode.getSelectedValue().equals("None")) {
         String selectedValue = settings.selfDamageMode.getSelectedValue();
         switch (selectedValue) {
            case "Motion":
               MC.mc.thePlayer.motionY = (double)((float)settings.motion.getValue());
               break;
            case "Packet1":
               PlayerUtil.damageSelfShort();
               break;
            case "Packet2":
               PlayerUtil.damageSelfPacket();
               break;
            case "Fake":
               MC.mc.thePlayer.handleStatusUpdate((byte)2);
               break;
            case "Custom":
               for (int i = 0; (double)i < settings.loops.getValue(); i++) {
                  MC.mc
                     .thePlayer
                     .sendQueue
                     .addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(MC.mc.thePlayer.posX, MC.mc.thePlayer.posY + settings.yAddInLoopFirst.getValue(), MC.mc.thePlayer.posZ, false));
                  if (settings.secondPacket.getValue()) {
                     MC.mc
                        .thePlayer
                        .sendQueue
                        .addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(MC.mc.thePlayer.posX, MC.mc.thePlayer.posY + settings.yAddInLoopSecond.getValue(), MC.mc.thePlayer.posZ, false));
                  }
               }

               if (settings.finishPacket.getValue()) {
                  MC.mc
                     .thePlayer
                     .sendQueue
                     .addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(MC.mc.thePlayer.posX, MC.mc.thePlayer.posY + settings.yAddFinish.getValue(), MC.mc.thePlayer.posZ, true));
               }
               break;
            case "SetPosition":
               MC.mc.thePlayer.setPosition(MC.mc.thePlayer.posX, MC.mc.thePlayer.posY + settings.setPositionYAdd.getValue(), MC.mc.thePlayer.posZ);
         }
      }
   }


}
