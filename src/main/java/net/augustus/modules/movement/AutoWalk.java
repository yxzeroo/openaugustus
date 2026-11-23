package net.augustus.modules.movement;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventSprint;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.events.listeners.SprintListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.Setting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.options.AutoWalkTargetOption;
import net.augustus.utils.pathfinding.AStarPathFinder;
import net.augustus.utils.pathfinding.PathBlockPos;
import net.augustus.utils.pathfinding.PathNode;
import net.augustus.utils.rotation.Rotation;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.rotation.RotationUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import net.augustus.modules.Categorys;
import net.augustus.modules.RotationModule;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.opengl.GL11;

public class AutoWalk extends RotationModule implements TickListener, SprintListener, MoveInputListener, RenderListener, LoadWorldListener {
   public final BooleanValue pathFinding;
   public final BooleanValue debugLine;
   public final StringValue distance;
   public final StringValue maxSearchTime;
   public final StringValue updateTicks;
   public final BooleanValue smooth;
   public final BooleanValue keepRotation;
   public final BooleansSetting movementmode;
   public final StringValue rotationUpdateTicks;
   private final MultiOptionSetting targets;
   public final BooleanValue onlyWithKillAuraTarget;
   public final StringValue noWalkDistanceToTarget;
   public final BooleanValue jump;
   public final BooleanValue stopWhileEating;
   public final BooleanValue stopWhileSlowness;
   public final BooleanValue stopWhileJumpBoost;
   public final BooleanValue adStafeOnHit;
   public final RandomizedRangeSettings adStafeTime;
   public final StringValue delayAfterRespawn;
   private final TimeHelper adStrafeTimer;
   private final HashSet pathNodeKeys;
   private final RotationUtil rotationUtil;
   private ArrayList pathNodes;
   private final TimeHelper respawnDelayTimer;
   private int pathStepIndex;
   private boolean strafeRight;
   private String lastPlayerPosKey;
   private int[] silentMoveKeys;
   public float silentMoveYaw;
   public long lastSilentMoveTime;
   public long lastSilentRotationUpdateTime;
   public boolean shouldJumpForWaypoint;
   public boolean facingWaypoint;
   public Rotation silentRotation;
   public long lastPathComputeTime;
   public Rotation keptRotation;
   public long lastRotationApplyTime;

   public AutoWalk() {
       super("AutoWalk", "AutoLaufen", Color.red, Categorys.MOVEMENT);
      this.pathFinding = new BooleanValue(this, "PathFinding", false);
      BooleanValue booleanValue = new BooleanValue(this, "DebugLine", true);
      BooleanValue pathFinding2 = this.pathFinding;
      this.pathFinding.getClass();
      BooleanValue booleanValue2 = pathFinding2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.debugLine = (BooleanValue)booleanValue.withVisibility(condition);
      StringValue stringValue = new StringValue(this, "Distance", 50.0, 3.0, 50.0, 1);
      pathFinding2 = this.pathFinding;
      this.pathFinding.getClass();
      BooleanValue booleanValue3 = pathFinding2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      this.distance = stringValue.visibleWhen(condition2);
      StringValue stringValue2 = new StringValue(this, "MaxSearchTime", 50.0, 10.0, 1000.0, 0);
      pathFinding2 = this.pathFinding;
      this.pathFinding.getClass();
      BooleanValue booleanValue4 = pathFinding2;
      SettingVisibilityCondition condition3 = () -> booleanValue4.getValue();
      this.maxSearchTime = stringValue2.visibleWhen(condition3);
      StringValue stringValue3 = new StringValue(this, "UpdateTicks", 1.0, 1.0, 10.0, 0);
      pathFinding2 = this.pathFinding;
      this.pathFinding.getClass();
      BooleanValue booleanValue5 = pathFinding2;
      SettingVisibilityCondition condition4 = () -> booleanValue5.getValue();
      this.updateTicks = stringValue3.visibleWhen(condition4);
      BooleanValue booleanValue6 = new BooleanValue(this, "Smooth", true);
      pathFinding2 = this.pathFinding;
      this.pathFinding.getClass();
      BooleanValue booleanValue7 = pathFinding2;
      SettingVisibilityCondition condition5 = () -> booleanValue7.getValue();
      this.smooth = (BooleanValue)booleanValue6.withVisibility(condition5);
      BooleanValue booleanValue8 = new BooleanValue(this, "KeepRotation", true);
      pathFinding2 = this.pathFinding;
      this.pathFinding.getClass();
      BooleanValue booleanValue9 = pathFinding2;
      SettingVisibilityCondition condition6 = () -> booleanValue9.getValue();
      this.keepRotation = (BooleanValue)booleanValue8.withVisibility(condition6);
      BooleansSetting booleansSetting = new BooleansSetting(this, "Movementmode", "Rotation", new String[]{"Rotation", "Silent"});
      pathFinding2 = this.pathFinding;
      this.pathFinding.getClass();
      BooleanValue booleanValue10 = pathFinding2;
      SettingVisibilityCondition condition7 = () -> booleanValue10.getValue();
      this.movementmode = (BooleansSetting)booleansSetting.withVisibility(condition7);
      StringValue stringValue4 = new StringValue(this, "RotationUpdateTicks", 0.0, 0.0, 10.0, 0);
      SettingVisibilityCondition condition8 = () -> this.isRotationUpdateTicksVisible();
      this.rotationUpdateTicks = stringValue4.visibleWhen(condition8);
      MultiOptionSetting multiOptionSetting = new MultiOptionSetting(this, "Targets", AutoWalkTargetOption.values());
      pathFinding2 = this.pathFinding;
      this.pathFinding.getClass();
      BooleanValue booleanValue11 = pathFinding2;
      SettingVisibilityCondition condition9 = () -> booleanValue11.getValue();
      this.targets = multiOptionSetting.visibleWhen(condition9);
      this.onlyWithKillAuraTarget = new BooleanValue(this, "OnlyWithKillAuraTarget", true);
      this.noWalkDistanceToTarget = new StringValue(this, "NoWalkDistanceToTarget", 1.0, 0.0, 6.0, 2);
      this.jump = new BooleanValue(this, "Jump", true);
      this.stopWhileEating = new BooleanValue(this, "StopWhileEating", true);
      this.stopWhileSlowness = new BooleanValue(this, "StopWhileSlowness", false);
      this.stopWhileJumpBoost = new BooleanValue(this, "StopWhileJumpBoost", false);
      BooleanValue booleanValue12 = new BooleanValue(this, "AdStafeOnHit", true);
      String str = "AdStrafeOnHit";
      this.adStafeOnHit = (BooleanValue)booleanValue12.withDisplayName(str);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "AdStafeTime", 200.0, 200.0, 0.0, 1000.0, 0);
      String str2 = "AdStrafeTime";
      RandomizedRangeSettings randomizedRangeSettings2 = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2);
      pathFinding2 = this.adStafeOnHit;
      this.adStafeOnHit.getClass();
      BooleanValue booleanValue13 = pathFinding2;
      SettingVisibilityCondition condition10 = () -> booleanValue13.getValue();
      this.adStafeTime = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition10);
      this.delayAfterRespawn = new StringValue(this, "DelayAfterRespawn", 0.0, 2000.0, 10000.0, 0);
      this.adStrafeTimer = new TimeHelper();
      this.pathNodeKeys = new HashSet();
      this.rotationUtil = new RotationUtil();
      this.pathNodes = new ArrayList();
      this.respawnDelayTimer = new TimeHelper();
      this.strafeRight = false;
      this.lastPlayerPosKey = "";
      this.rotating = true;
      String str3 = "None";
      this.silentMoveFix.setSelectedValue(str3);
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @Override
   public void onDisable() {
      super.onDisable();
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 1;
      byte b2 = 40;
      this.toggleEventSubscription(b2, b);
      byte b3 = 36;
      this.toggleEventSubscription(b3);
      byte b4 = 32;
      this.toggleEventSubscription(b4);
      byte b5 = 17;
      this.toggleEventSubscription(b5);
   }

   @Override
   public void onTick() {
      this.rotating = true;
      Fly.getCachedSettings();
      String str = "None";
      this.silentMoveFix.setSelectedValue(str);

      try {
         if (Augustus.getInstance().getModuleManager().freeCam.isToggled() || !this.hasRespawnDelayElapsed()) {
            return;
         }

         if (mc.isNotInGui() && this.pathFinding.getValue()) {
            if (this.keepRotation.getValue() && this.keptRotation != null && System.currentTimeMillis() - this.lastRotationApplyTime < 10000L) {
               RotationController rotationController = ClientManagers.rotationController;
               float[] floats = this.keptRotation.toArray();
               byte b = 99;
               float[] floats2 = floats;
               rotationController.requestRotation2(floats2, b, this);
            }

            EntityLivingBase entity = (EntityLivingBase)this.findClosestTarget();
            RotationUtil rotationUtil2 = this.rotationUtil;
            String selectedValue = ClientManagers.moduleManager.killAura.rotationMode.getSelectedValue();
            int value = (int)ClientManagers.moduleManager.killAura.hitBoxPercentageVertical.getValue();
            int value2 = (int)ClientManagers.moduleManager.killAura.hitBoxPercentageHorizontal.getValue();
            int value3 = (int)ClientManagers.moduleManager.killAura.hitBoxPercentageVertical1.getValue();
            int value4 = (int)ClientManagers.moduleManager.killAura.hitBoxPercentageHorizontal1.getValue();
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            Rotation rotation = previousRotation;
            int n = value4;
            int n2 = value3;
            int n3 = value2;
            int n4 = value;
            String str2 = selectedValue;
            Vec3 vec = rotationUtil2.selectEntityAimPoint(entity, str2, n4, n3, n2, n, rotation);
            if (entity != null
               && (
                  !ClientManagers.moduleManager.killAura.isToggled()
                     || ClientManagers.moduleManager.killAura.target == null
                     || (double)mc.thePlayer.getDistanceToEntityBox(entity) > 3.5
                     || !mc.thePlayer.canEntityBeSeen(vec.xCoord, vec.yCoord, vec.zCoord)
               )) {
               Vec3 interpolatedPos = mc.thePlayer.getInterpolatedPos(1.0F);
               Vec3 interpolatedPos2 = entity.getInterpolatedPos(1.0F);
               PathBlockPos pathBlockPos = new PathBlockPos((int)Math.floor(interpolatedPos.xCoord), (int)Math.floor(interpolatedPos.yCoord), (int)Math.floor(interpolatedPos.zCoord));
               PathBlockPos pathBlockPos2 = new PathBlockPos((int)Math.floor(interpolatedPos2.xCoord), (int)Math.floor(interpolatedPos2.yCoord), (int)Math.floor(interpolatedPos2.zCoord));
               String str3 = pathBlockPos.toKeyString();
               if ((!this.lastPlayerPosKey.equals(str3) || System.currentTimeMillis() - this.lastPathComputeTime < 1000L) && pathBlockPos.distanceTo(pathBlockPos2) < 500.0) {
                  this.lastPlayerPosKey = str3;
                  if (mc.thePlayer.ticksExisted % (int)this.updateTicks.getValue() == 0) {
                     this.computePath(pathBlockPos, pathBlockPos2);
                     this.lastPathComputeTime = System.currentTimeMillis();
                  } else {
                     System.out.println(str3 + " " + this.pathNodeKeys.contains(str3) + " (nodes=" + this.pathNodeKeys.size() + ")");
                     if (this.pathNodeKeys.contains(str3)) {
                        this.pathStepIndex++;
                        if (this.pathNodes.size() <= this.pathStepIndex) {
                           this.pathStepIndex = this.pathNodes.size() - 1;
                        }
                     } else {
                        this.computePath(pathBlockPos, pathBlockPos2);
                        this.lastPathComputeTime = System.currentTimeMillis();
                     }
                  }
               }

               if (this.pathNodes != null && !this.pathNodes.isEmpty() && this.pathNodes.size() > 1) {
                  byte b2 = 1;
                  if (mc.thePlayer.motionY > 0.0) {
                     b2 = 2;
                  }

                  PathNode pathNode = (PathNode)this.pathNodes.get(this.pathNodes.size() - this.pathStepIndex - b2 - 1);
                  Block block = mc.theWorld.getBlockState2(pathNode.getPos().x, pathNode.getPos().y, pathNode.getPos().z).getBlock();
                  if (this.isSlowOrThinBlock(block)) {
                     b2 = 2;
                     pathNode = (PathNode)this.pathNodes.get(this.pathNodes.size() - this.pathStepIndex - b2 - 1);
                  }

                  if ((double)pathNode.getPos().y > mc.thePlayer.posY) {
                     b2 = 3;
                     pathNode = (PathNode)this.pathNodes.get(this.pathNodes.size() - this.pathStepIndex - b2 - 1);
                  }

                  PathBlockPos pos = pathNode.getPos();
                  Vec3 vec2 = pos.toVec3();
                  if ((double)pathNode.getPos().y > mc.thePlayer.posY) {
                     int n5 = MathHelper.floor_double(mc.thePlayer.posX);
                     int n6 = MathHelper.floor_double(mc.thePlayer.posY);
                     int n7 = MathHelper.floor_double(mc.thePlayer.posZ);
                     BlockPos pos2 = new BlockPos(n5, n6, n7);
                     IBlockState blockState = mc.theWorld.getBlockState(pos2);
                     Block block2 = blockState.getBlock();
                     if (block2 instanceof BlockLadder) {
                        EnumFacing value5 = (EnumFacing)blockState.getValue(BlockLadder.FACING);
                        Vec3i directionVec = value5.getOpposite().getDirectionVec();
                        vec2 = new Vec3(pos2.add(directionVec));
                     } else if (this.smooth.getValue()) {
                        for (int i = this.pathNodes.size() - this.pathStepIndex - b2 - 1; i > 0; i--) {
                           PathNode pathNode2 = (PathNode)this.pathNodes.get(i);
                           PathBlockPos pos3 = pathNode2.getPos();
                           Vec3 vec3 = pos3.toVec3();
                           double d = vec3.xCoord + 0.5;
                           double d2 = vec3.yCoord + (double)mc.thePlayer.getEyeHeight();
                           double d3 = vec3.zCoord + 0.5;
                           double d4 = d2;
                           double d5 = d;
                           Rotation rotation2 = this.calculateRotationToPos(d5, d4, d3);
                           MovingObjectPosition hit = mc.thePlayer
                              .rayTraceCustom(mc.thePlayer.getDistance(vec3.xCoord + 0.5, interpolatedPos.yCoord, vec3.zCoord + 0.5) + 0.2, 1.0F, rotation2.yaw, rotation2.pitch);
                           MovingObjectPosition hit2 = mc.thePlayer
                              .rayTraceFromPosition(
                                 new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ),
                                 mc.thePlayer.getDistance(vec3.xCoord + 0.5, interpolatedPos.yCoord, vec3.zCoord + 0.5) + 0.2,
                                 1.0F,
                                 rotation2.yaw,
                                 rotation2.pitch
                              );
                           if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || hit2 != null && hit2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                              break;
                           }

                           vec2 = vec3;
                        }
                     }
                  } else if (this.smooth.getValue()) {
                     for (int j = this.pathNodes.size() - this.pathStepIndex - b2 - 1; j > 0; j--) {
                        PathNode pathNode3 = (PathNode)this.pathNodes.get(j);
                        PathBlockPos pos4 = pathNode3.getPos();
                        Vec3 vec4 = pos4.toVec3();
                        double d6 = vec4.xCoord + 0.5;
                        double d7 = vec4.yCoord + (double)mc.thePlayer.getEyeHeight();
                        double d8 = vec4.zCoord + 0.5;
                        double d9 = d7;
                        double d10 = d6;
                        Rotation rotation3 = this.calculateRotationToPos(d10, d9, d8);
                        MovingObjectPosition hit3 = mc.thePlayer
                           .rayTraceCustom(mc.thePlayer.getDistance(vec4.xCoord + 0.5, interpolatedPos.yCoord, vec4.zCoord + 0.5) + 0.2, 1.0F, rotation3.yaw, rotation3.pitch);
                        MovingObjectPosition hit4 = mc.thePlayer
                           .rayTraceFromPosition(
                              new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ),
                              mc.thePlayer.getDistance(vec4.xCoord + 0.5, interpolatedPos.yCoord, vec4.zCoord + 0.5) + 0.2,
                              1.0F,
                              rotation3.yaw,
                              rotation3.pitch
                           );
                        if (hit3 != null && hit3.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || hit4 != null && hit4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                           break;
                        }

                        vec2 = vec4;
                     }
                  }

                  if (this.movementmode.getSelectedValue().equalsIgnoreCase("rotation")) {
                     double d11 = vec2.xCoord + 0.5 - mc.thePlayer.posX;
                     double d12 = vec2.zCoord + 0.5 - mc.thePlayer.posZ;
                     float f = (float)(MathHelper.atan2(d12, d11) * 180.0 / Math.PI - 90.0);
                     float f2 = (float)ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
                     Rotation rotation4 = this.applyRotation(f, f2);
                     RotationController rotationController2 = ClientManagers.rotationController;
                     float[] floats3 = rotation4.toArray();
                     short s = -10000;
                     float[] floats4 = floats3;
                     rotationController2.requestRotation2(floats4, s, this);
                     this.lastRotationApplyTime = System.currentTimeMillis();
                     this.keptRotation = rotation4;
                  } else {
                     if (this.rotationUpdateTicks.getValue() > 0.0 && (double)mc.thePlayer.ticksExisted % this.rotationUpdateTicks.getValue() == 0.0) {
                        double d13 = vec2.xCoord + 0.5 - mc.thePlayer.posX;
                        double d14 = vec2.zCoord + 0.5 - mc.thePlayer.posZ;
                        float f3 = (float)(MathHelper.atan2(d14, d13) * 180.0 / Math.PI - 90.0);
                        float f4 = (float)ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
                        this.silentRotation = this.applyRotation(f3, f4);
                        this.lastSilentRotationUpdateTime = System.currentTimeMillis();
                     }

                     if (this.silentRotation != null
                        && System.currentTimeMillis() - this.lastSilentMoveTime < 100L
                        && (double)(System.currentTimeMillis() - this.lastSilentRotationUpdateTime) < this.rotationUpdateTicks.getValue() * 50.0) {
                        RotationController rotationController3 = ClientManagers.rotationController;
                        float[] floats5 = this.silentRotation.toArray();
                        short s2 = -10000;
                        float[] floats6 = floats5;
                        rotationController3.requestRotation2(floats6, s2, this);
                        this.lastRotationApplyTime = System.currentTimeMillis();
                        this.keptRotation = this.silentRotation;
                     }

                     double d15 = vec2.xCoord + 0.5 - mc.thePlayer.posX;
                     double d16 = vec2.zCoord + 0.5 - mc.thePlayer.posZ;
                     float f5 = (float)(MathHelper.atan2(d16, d15) * 180.0 / Math.PI - 90.0);
                     this.silentMoveKeys = computeMoveKeysForYaw(ClientManagers.rotationController.getCurrentRotation().yaw, f5);
                     this.lastSilentMoveTime = System.currentTimeMillis();
                     this.shouldJumpForWaypoint = vec2.yCoord > mc.thePlayer.posY;
                     float f6 = shortestAngleDiff(
                        MathHelper.wrapAngleTo180_float(ClientManagers.rotationController.getCurrentRotation().yaw) + 180.0F, MathHelper.wrapAngleTo180_float(f5) + 180.0F
                     );
                     this.facingWaypoint = f6 <= 45.0F;
                  }
               }
            }
         }
      } catch (Exception e) {
      }
   }

   private boolean isSlowOrThinBlock(Block block) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return block == Blocks.web || block instanceof BlockCarpet || block == Blocks.snow_layer;
   }

   public static float shortestAngleDiff(float f, float f2) {
      float f3 = Math.abs(f - f2);
      return Math.min(f3, 360.0F - f3);
   }

   public static int[] computeMoveKeysForYaw(float f, float f2) {
      double[] doubles = calculateMotionVector(f2, 0.0F, 1.0F);
      Fly.getCachedSettings();
      int[] ints = new int[]{0, 0};
      double d = -1.0;

      for (int i = -1; i <= 1; i++) {
         for (int j = -1; j <= 1; j++) {
            double[] doubles2 = calculateMotionVector(f, (float)i, (float)j);
            double d2 = (doubles2[0] - doubles[0]) * (doubles2[0] - doubles[0]) + (doubles2[1] - doubles[1]) * (doubles2[1] - doubles[1]);
            if (d > d2 || d == -1.0) {
               ints = new int[]{i, j};
               d = d2;
            }
         }
      }

      return ints;
   }

   public static double[] calculateMotionVector(float f, float f2, float f3) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      float f4 = f2 * f2 + f3 * f3;
      if (f4 >= 1.0E-4F) {
         f4 = MathHelper.sqrt_float(f4);
         if (f4 < 1.0F) {
            f4 = 1.0F;
         }

         f4 = 1.0F / f4;
         f2 *= f4;
         f3 *= f4;
         float f5 = MathHelper.sin(f * (float) Math.PI / 180.0F);
         float f6 = MathHelper.cos(f * (float) Math.PI / 180.0F);
         return new double[]{(double)(f2 * f6 - f3 * f5), (double)(f3 * f6 + f2 * f5)};
      } else {
         return new double[]{0.0, 0.0};
      }
   }

   public void computePath(PathBlockPos pathBlockPos, PathBlockPos pathBlockPos2) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      boolean isToggled = Augustus.getInstance().getModuleManager().jesus.isToggled();
      boolean flag = isToggled && Augustus.getInstance().getModuleManager().jesus.mode.getSelectedValue().equalsIgnoreCase("dolphin");
      AStarPathFinder aStarPathFinder = new AStarPathFinder(pathBlockPos, pathBlockPos2, isToggled && !flag, flag, false);
      aStarPathFinder.smoothPath = true;
      long value = (long)this.maxSearchTime.getValue();
      boolean flag2 = true;
      this.pathNodes = aStarPathFinder.computePath(flag2, value);
      this.pathNodeKeys.clear();
      if (this.pathNodes != null) {
         for (PathNode pathNode : (Iterable<PathNode>)(this.pathNodes)) {
            this.pathNodeKeys.add(pathNode.getPos().toKeyString());
         }

         this.pathStepIndex = 0;
      }
   }

   @Override
   public void onRender(float f) {
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GL11.glEnable(2848);
      GlStateManager.disableDepth();
      GlStateManager.disableTexture2D();
      Fly.getCachedSettings();
      GlStateManager.disableCull();
      GlStateManager.depthMask(false);
      GL11.glTranslated(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.debugLine.getValue() && this.pathNodes != null && !this.pathNodes.isEmpty() && this.pathFinding.getValue()) {
         GL11.glBegin(3);
         if (this.pathNodes != null) {
            for (PathNode pathNode : (Iterable<PathNode>)(this.pathNodes)) {
               GL11.glVertex3d((double)pathNode.getPos().x + 0.5, (double)pathNode.getPos().y + 0.01, (double)pathNode.getPos().z + 0.5);
            }
         }

         GL11.glEnd();
      }

      GL11.glPopMatrix();
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.enableTexture2D();
      GlStateManager.enableDepth();
      GlStateManager.disableBlend();
      GL11.glDisable(2848);
   }

   @Override
   public void onSprint3(EventSprint event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.facingWaypoint && this.pathFinding.getValue() && this.movementmode.getSelectedValue().equalsIgnoreCase("silent")) {
         Stream stream = mc.theWorld.loadedEntityList.stream().filter(obj -> this.isValidTarget((Entity)obj));
         Object[] array = stream.sorted(Comparator.comparingDouble(obj2 -> (double)mc.thePlayer.getDistanceToEntity((Entity)obj2))).toArray();
         if (array.length > 0 && this.pathNodes != null && !this.pathNodes.isEmpty()) {
            EntityLivingBase entity = (EntityLivingBase)array[0];
            if (entity != null) {
               boolean flag = false;
               event.setForceSprint(flag);
            }
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!Augustus.getInstance().getModuleManager().freeCam.isToggled() && this.hasRespawnDelayElapsed()) {
         if (mc.isNotInGui() && this.canWalkWhileEating()) {
            EntityLivingBase entity = null;
            if (this.pathFinding.getValue() && this.pathNodes != null && !this.pathNodes.isEmpty()) {
               entity = (EntityLivingBase)this.findClosestTarget();
            }

            if (this.isBlockedByPotionEffect()) {
               return;
            }

            if ((!ClientManagers.moduleManager.killAura.isToggled() || ClientManagers.moduleManager.killAura.target == null) && entity == null) {
               if (!this.onlyWithKillAuraTarget.getValue()) {
                  float f = 1.0F;
                  event.setMoveForward(f);
               }
            } else {
               EntityLivingBase entity2 = ClientManagers.moduleManager.killAura.isToggled() && ClientManagers.moduleManager.killAura.target != null ? ClientManagers.moduleManager.killAura.target : entity;
               if (entity2 != null) {
                  if (this.movementmode.getSelectedValue().equalsIgnoreCase("rotation") || !this.pathFinding.getValue()) {
                     if ((double)mc.thePlayer.getDistanceToEntityBox(entity2) > this.noWalkDistanceToTarget.getValue()) {
                        float f2 = 1.0F;
                        event.setMoveForward(f2);
                     }
                  } else if (this.silentMoveKeys != null) {
                     float f3 = (float)this.silentMoveKeys[0];
                     event.setMoveStrafe(f3);
                     float f4 = (float)this.silentMoveKeys[1];
                     event.setMoveForward(f4);
                     if (this.shouldJumpForWaypoint) {
                        boolean flag = true;
                        event.setJump(flag);
                     }
                  }

                  if (this.jump.getValue()
                     && mc.thePlayer.getDistanceToEntityBox(entity2) > 6.0F
                     && (!this.movementmode.getSelectedValue().equalsIgnoreCase("silent") || this.facingWaypoint || !this.pathFinding.getValue())) {
                     boolean flag2 = true;
                     event.setJump(flag2);
                  }

                  if (this.adStafeOnHit.getValue() && (double)mc.thePlayer.getDistanceToEntityBox(entity2) < 4.5) {
                     if (entity2.hurtTime == 9) {
                        this.adStrafeTimer.reset();
                        this.strafeRight = !this.strafeRight;
                     }

                     TimeHelper adStrafeTimer2 = this.adStrafeTimer;
                     double lastValue = this.adStafeTime.getLastValue();
                     if (!adStrafeTimer2.reached(lastValue)) {
                        float f5 = this.strafeRight ? 1.0F : -1.0F;
                        event.setMoveStrafe(f5);
                        this.adStafeTime.nextValue();
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.respawnDelayTimer.reset();
   }

   private boolean canWalkWhileEating() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !mc.thePlayer.isEating() || !this.stopWhileEating.getValue();
   }

   private boolean isBlockedByPotionEffect() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown) != null && this.stopWhileSlowness.getValue()
         || mc.thePlayer.getActivePotionEffect(Potion.jump) != null && this.stopWhileJumpBoost.getValue();
   }

   private boolean hasRespawnDelayElapsed() {
      TimeHelper respawnDelayTimer2 = this.respawnDelayTimer;
      double value = this.delayAfterRespawn.getValue();
      return respawnDelayTimer2.reached(value);
   }

   private boolean isValidTarget(Entity entity) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!(entity instanceof EntityPlayerSP)
         && this.hasRespawnDelayElapsed()
         && (
            entity instanceof EntityPlayer && AutoWalkTargetOption.PLAYER.isSelected()
               || (entity instanceof EntityMob || entity instanceof EntityIronGolem || entity instanceof EntitySnowman) && AutoWalkTargetOption.MOB.isSelected()
               || entity instanceof EntityVillager && AutoWalkTargetOption.VILLAGER.isSelected()
               || entity instanceof EntityAnimal && AutoWalkTargetOption.ANIMAL.isSelected()
               || entity instanceof EntityArmorStand && AutoWalkTargetOption.ARMOR_STAND.isSelected()
         )
         && (!entity.isInvisible() || AutoWalkTargetOption.INVISIBLE.isSelected())
         && entity.ticksExisted > 0
         && !entity.isDead
         && !entity.getName().equals("§aShop")
         && !entity.getName().equals("SHOP")
         && !entity.getName().equals("UPGRADES")
         && (!(entity instanceof EntityPlayer) || !ClientManagers.moduleManager.teams.isTeammate(entity))) {
         if (ClientManagers.moduleManager.targetArea.isToggled()) {
            if (!ClientManagers.moduleManager.targetArea.isEntityInArea(entity)) {
               return false;
            }
         }

         if (entity instanceof EntityPlayer) {
            if (ClientManagers.moduleManager.antiBot.isBot(entity)) {
               return false;
            }
         }

         if ((!(entity instanceof EntityPlayer) || !ClientManagers.moduleManager.midClick.isFriend(entity) || ClientManagers.moduleManager.midClick.autoUnfriendTriggered)
            && (double)entity.getDistanceToEntityBox(mc.thePlayer) <= this.distance.getValue()) {
            return true;
         }
      }

      return false;
   }

   public Entity findClosestTarget() {
      Entity entity = null;
      Fly.getCachedSettings();
      double d = 0.0;

      for (Entity entity2 : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
         if (this.isValidTarget(entity2)) {
            double d2 = (double)mc.thePlayer.getDistanceSqToEntityFloat(entity2);
            if (d2 < d || entity == null) {
               entity = entity2;
               d = d2;
            }
         }
      }

      return entity;
   }

   private boolean isRotationUpdateTicksVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.movementmode.isVisible() && this.movementmode.getSelectedValue().equalsIgnoreCase("silent");
   }

}
