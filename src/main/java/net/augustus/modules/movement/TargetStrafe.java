package net.augustus.modules.movement;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventMove;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.Setting;
import net.augustus.utils.MoveUtil;
import net.augustus.events.listeners.MoveListener;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.Augustus;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class TargetStrafe extends Module implements MoveListener, MoveInputListener {
   private final BooleansSetting conditionsToTargetStrafe;
   private final StringValue radius;
   private final BooleanValue killAuraTarget;
   private final BooleanValue autoStrafe;
   private final BooleanValue onlyWhileSpace;
   private final BooleanValue thirdPersonMode;
   private final TimeHelper timeHelper;
   public float strafeYaw;
   public EntityLivingBase target;
   private Object[] sortedTargets;
   private boolean strafeLeft;
   private boolean active;
   private int previousThirdPersonView;

   public TargetStrafe() {
       super("TargetStrafe", "Zielbeschuss", new Color(52, 0, 194), Categorys.MOVEMENT);
      this.conditionsToTargetStrafe = new BooleansSetting(
         this,
         "ConditionsToTargetStrafe",
         "Everytime",
         new String[]{"Everytime", "OnlyWhileFlyEnabled", "OnlyWhileSpeedEnabled", "OnlyWhileSpeedOrFlyEnabled"}
      );
      this.radius = new StringValue(this, "Radius", 2.5, 0.1, 6.0, 1);
      this.killAuraTarget = new BooleanValue(this, "KillAuraTarget", true);
      this.autoStrafe = new BooleanValue(this, "AutoStrafe", true);
      this.onlyWhileSpace = new BooleanValue(this, "OnlyWhileSpace", true);
      this.thirdPersonMode = new BooleanValue(this, "ThirdPersonMode", false);
      this.timeHelper = new TimeHelper();
      this.target = null;
      this.active = false;
      this.previousThirdPersonView = 0;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.sortedTargets = null;
      this.target = null;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 28;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      this.target = null;
      Fly.getCachedSettings();
      boolean flag = this.conditionsToTargetStrafe.getSelectedValue().equals("Everytime");
      boolean flag2 = this.conditionsToTargetStrafe.getSelectedValue().equals("OnlyWhileFlyEnabled") && ClientManagers.moduleManager.fly.isToggled();
      boolean flag3 = this.conditionsToTargetStrafe.getSelectedValue().equals("OnlyWhileSpeedEnabled") && ClientManagers.moduleManager.speed.isToggled();
      boolean flag4 = this.conditionsToTargetStrafe.getSelectedValue().equals("OnlyWhileSpeedOrFlyEnabled")
         && (ClientManagers.moduleManager.speed.isToggled() || ClientManagers.moduleManager.fly.isToggled());
      if ((mc.gameSettings.keyBindJump.isKeyDown() || !this.onlyWhileSpace.getValue()) && !ClientManagers.moduleManager.scaffold.isToggled() && (flag || flag4 || flag3 || flag2)) {
         if (this.killAuraTarget.getValue()) {
            this.target = CombatTargetUtil.getActiveTarget();
         } else {
            Stream<Entity> stream = ((List<Entity>)mc.theWorld.loadedEntityList).stream();
            this.sortedTargets = stream.filter(entity -> {
               Setting[] cachedSettings = Fly.getCachedSettings();
               if (entity instanceof EntityLivingBase) {
                  if (!mc.thePlayer.canEntityBeSeen(entity)) {
                     return false;
                  }

                  if (entity.isInvisible() || ((EntityLivingBase)entity).deathTime > 1) {
                     return false;
                  }

                  if (entity.ticksExisted < 1) {
                     return false;
                  }

                  if (entity instanceof EntityPlayer && ClientManagers.moduleManager.teams.isTeammate(entity)) {
                     return false;
                  }

                  if (entity instanceof EntityPlayer && (entity.getName().equals("§aShop") || entity.getName().equals("SHOP") || entity.getName().equals("UPGRADES"))) {
                     return false;
                  }

                  if (entity.isDead) {
                     return false;
                  }

                  if (entity instanceof EntityPlayer) {
                     if (ClientManagers.moduleManager.antiBot.isBot(entity)) {
                        return false;
                     }
                  }

                  if (entity instanceof EntityPlayer && !ClientManagers.moduleManager.midClick.autoUnfriendTriggered && ClientManagers.moduleManager.midClick.isFriend(entity)) {
                     return false;
                  }
               }

               return entity instanceof EntityLivingBase && entity != mc.thePlayer && mc.thePlayer.getDistanceToEntity(entity) < 7.0F;
            }).sorted(Comparator.comparingDouble(entity2 -> (double)mc.thePlayer.getDistanceToEntity((Entity)entity2))).toArray();
            if (this.sortedTargets.length > 0) {
               this.target = (EntityLivingBase)this.sortedTargets[0];
            }
         }

         if (this.target != null && !mc.thePlayer.isOnLadder()) {
            if (!this.active) {
               this.previousThirdPersonView = mc.gameSettings.thirdPersonView;
            }

            this.active = true;
            if (this.thirdPersonMode.getValue()) {
               mc.gameSettings.thirdPersonView = 1;
            }

            if (mc.thePlayer.movementInput.moveStrafe > 0.0F) {
               this.strafeLeft = false;
            } else if (mc.thePlayer.movementInput.moveStrafe < 0.0F) {
               this.strafeLeft = true;
            }

            Block block = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ).getBlock();
            if (mc.thePlayer.isCollidedHorizontally) {
               this.strafeLeft = !this.strafeLeft;
            }

            if (block instanceof BlockAir && !mc.thePlayer.onGround) {
               long l = 300L;
               if (this.timeHelper.reached2(l)) {
                  this.strafeLeft = !this.strafeLeft;
                  this.timeHelper.reset();
               }
            }

            double value = this.radius.getValue();
            RotationUtil rotationUtil = new RotationUtil();
            boolean flag5 = false;
            float rotationPitch = mc.thePlayer.rotationPitch;
            float rotationYaw = mc.thePlayer.rotationYaw;
            EntityLivingBase target2 = this.target;
            double yaw = (double)rotationUtil.faceEntityEyes(target2, rotationYaw, rotationPitch, flag5).yaw;
            yaw += (double)((this.strafeLeft ? -1 : 1) * 45);
            double d = Math.toRadians(yaw);
            double d2 = this.target.posX + Math.sin(d) * value;
            double d3 = this.target.posZ - Math.cos(d) * value;
            boolean flag6 = false;
            float rotationPitch2 = mc.thePlayer.rotationPitch;
            float rotationYaw2 = mc.thePlayer.rotationYaw;
            double posY = this.target.posY;
            this.strafeYaw = rotationUtil.faceCoordinates(d2, posY, d3, rotationYaw2, rotationPitch2, flag6).yaw;
            if (this.autoStrafe.getValue()) {
               MoveUtil.strafe();
            }
         } else {
            this.restoreThirdPersonView();
            this.strafeYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
         }
      } else {
         this.restoreThirdPersonView();
      }
   }

   @Override
   public void onMove(EventMove event) {
      if (this.target != null && !BlockUtil.isScaffoldActiveAndValid()) {
         float strafeYaw2 = this.strafeYaw;
         event.setYaw(strafeYaw2);
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (this.target != null && !BlockUtil.isScaffoldActiveAndValid()) {
         float strafeYaw2 = this.strafeYaw;
         event.setYaw(strafeYaw2);
      }
   }

   private void restoreThirdPersonView() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.active) {
         if (this.thirdPersonMode.getValue()) {
            mc.gameSettings.thirdPersonView = this.previousThirdPersonView;
         }

         this.active = false;
      }
   }

}
