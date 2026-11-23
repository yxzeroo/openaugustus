package net.augustus.modules.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.rotation.RayCastUtil;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.Augustus;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.modules.world.scaffold.ScaffoldRaycastResult;
import net.augustus.utils.rotation.Rotation;
import net.augustus.utils.TimeHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class NewScaffold extends Module implements TickListener, MoveInputListener, ClickListener {
   private final RotationUtil rotationUtil;
   private final TimeHelper timeHelper;
   private int lockedMoveTicks;
   private float savedMoveForward;
   private float savedMoveStrafe;
   private boolean savedSneaking;
   private boolean rotationLocked;
   private int predictTicks;
   private MovingObjectPosition lastRayTrace;

   public NewScaffold() {
       super("NewScaffold", "NeuGerüst", new Color(182, 194, 207), Categorys.WORLD);
      this.rotationUtil = new RotationUtil();
      this.timeHelper = new TimeHelper();
      this.savedMoveForward = 0.0F;
      this.savedMoveStrafe = 0.0F;
      this.savedSneaking = false;
      this.rotationLocked = false;
      this.predictTicks = 1;
      this.lastRayTrace = null;
   }

   @Override
   public void onEnable() {
      this.timeHelper.reset();
      this.lockedMoveTicks = 0;
      this.predictTicks = 0;
      this.rotationLocked = false;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
      byte b3 = 7;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onTick() {
      BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down();
      float backwardYaw = this.getBackwardYaw();
      if (this.rotationLocked) {
         backwardYaw = ClientManagers.rotationController.getPreviousRotation().yaw;
      }

      RotationUtil rotationUtil2 = this.rotationUtil;
      float f = 24.0F;
      float f2 = 18.0F;
      float f3 = DefaultRandomGenerator.instance.nextFloat(f2, f);
      float yaw = ClientManagers.rotationController.getPreviousRotation().yaw;
      float f4 = f3;
      float f5 = rotationUtil2.stepYawWithGcd(f4, yaw, backwardYaw);
      float pitch = ClientManagers.rotationController.getPreviousRotation().pitch;
      long l = 200L;
      if (!this.timeHelper.reached2(l)) {
         rotationUtil2 = this.rotationUtil;
         float f6 = 24.0F;
         float f7 = 18.0F;
         float f8 = DefaultRandomGenerator.instance.nextFloat(f7, f6);
         float f9 = 80.34F;
         float pitch2 = ClientManagers.rotationController.getPreviousRotation().pitch;
         float f10 = f8;
         pitch = rotationUtil2.updatePitchWithIntaveSin(f10, pitch2, f9);
      }

      boolean flag = !this.rotationLocked;
      if (flag) {
         float f11 = 3.0F;
         float f12 = 1.0F;
         pitch += DefaultRandomGenerator.instance.nextFloat(f12, f11);
         pitch = RotationUtil.applyPitchGcd(pitch, ClientManagers.rotationController.getPreviousRotation().pitch);
      }

      if (flag && this.isLicenseValid() && !mc.theWorld.getBlockState(pos).getBlock().isFullBlock()) {
         byte b = 1;
         byte b2 = 1;
         this.predictTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
         double[] doubles = PlayerUtil.predictPlayerPosition(this.predictTicks - 1, mc.thePlayer, f5);
         System.out.println(doubles[0] + "   " + mc.thePlayer.posX + "    :   " + doubles[2] + "  " + mc.thePlayer.posZ);
         Vec3 vec = new Vec3(doubles[0], doubles[1], doubles[2]);
         System.out.println(this.predictTicks);
         ArrayList list = new ArrayList();
         float f13 = 0.0F;

         for (float i = 15.0F; i < 90.0F; i += 0.05F) {
            float f14 = RotationUtil.applyPitchGcd(i, ClientManagers.rotationController.getPreviousRotation().pitch);
            if (f14 != f13) {
               MovingObjectPosition hit = RayCastUtil.rayCastFromPosition(1.0F, vec, f5, f14);
               if (hit != null
                  && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                  && BlockUtil.isSolidBlock(hit.getBlockPos())
                  && hit.sideHit != EnumFacing.DOWN
                  && hit.sideHit != EnumFacing.UP
                  && (double)hit.getBlockPos().getY() < mc.thePlayer.posY) {
                  list.add(new ScaffoldRaycastResult(hit, f14));
               }
            }

            f13 = f14;
         }

         if (!list.isEmpty()) {
            this.lockedMoveTicks = this.predictTicks;
            list.sort(Comparator.comparingDouble(obj -> this.scoreRaycastCandidate(f5, vec, (ScaffoldRaycastResult)obj)));
            ScaffoldRaycastResult scaffoldRaycastResult = (ScaffoldRaycastResult)list.get(0);
            float[] floats = new float[]{f5, scaffoldRaycastResult.pitch};
            byte b3 = -1;
            float[] floats2 = floats;
            ClientManagers.rotationController.requestRotation2(floats2, b3, this);
            this.rotationLocked = true;
            return;
         }
      }

      this.rotationLocked = this.lockedMoveTicks > 0;
      RotationController rotationController = ClientManagers.rotationController;
      Rotation rotation = new Rotation(f5, pitch);
      byte b4 = -1;
      Rotation rotation2 = rotation;
      rotationController.requestRotation(rotation2, b4, this);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
      event.setMoveFixMode(moveFixMode);
      if (this.lockedMoveTicks > 0) {
         float savedMoveForward2 = this.savedMoveForward;
         event.setMoveStrafe(savedMoveForward2);
         float savedMoveStrafe2 = this.savedMoveStrafe;
         event.setMoveForward(savedMoveStrafe2);
         boolean savedSneaking2 = this.savedSneaking;
         event.setSneak(savedSneaking2);
      }

      this.lockedMoveTicks--;
      this.savedMoveForward = event.getMoveStrafe();
      this.savedMoveStrafe = event.getMoveForward();
      this.savedSneaking = event.isSneak();
   }

   @Override
   public void onClick(EventClick event) {
      if (this.lockedMoveTicks <= 0) {
         byte b = 1;
         event.addRightClicks(b);
         boolean flag = false;
         event.setBlockAllClicks(flag);
         boolean flag2 = true;
         event.setIgnoreVanillaClicks(flag2);
      }
   }

   private double getPitchDeltaScore(long l, ScaffoldRaycastResult scaffoldRaycastResult) {
      return (double)Math.abs(scaffoldRaycastResult.pitch - ClientManagers.rotationController.getPreviousRotation().pitch);
   }

   private double getHitCenterOffset(ScaffoldRaycastResult scaffoldRaycastResult, float f, Vec3 vec) {
      MovingObjectPosition hit = RayCastUtil.rayCastFromPosition(1.0F, vec, f, scaffoldRaycastResult.pitch);
      if (hit != null && hit.getBlockPos() != null && hit.hitVec != null) {
         double d = hit.hitVec.yCoord % 1.0;
         return Math.abs(0.5 - d);
      } else {
         return 100.0;
      }
   }

   private float getBackwardYaw() {
      float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
      return realYaw - 180.0F;
   }

   private double scoreRaycastCandidate(float f, Vec3 vec, ScaffoldRaycastResult scaffoldRaycastResult) {
      return this.getHitCenterOffset(scaffoldRaycastResult, f, vec);
   }

   private boolean isLicenseValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
