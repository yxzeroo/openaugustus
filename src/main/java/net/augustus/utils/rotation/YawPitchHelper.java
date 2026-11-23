package net.augustus.utils.rotation;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.minecraft.entity.player.EntityPlayer;

public class YawPitchHelper {
   public float realLastYaw;
   public float realLastPitch;
   public float realYaw = 0.0F;
   public float realPitch = 0.0F;
   private static int[] helperStateFlags;

   public float alignYawToReference(float f, float f2) {
      int[] helperStateFlags2 = getHelperStateFlags();
      float f3 = 360.0F;
      if (Augustus.getInstance().getModuleManager().exploitFixer.isToggled() && Math.abs(f2 - f) > 1800000.0F) {
         f3 = (float)(Math.floor((double)(Math.abs(f2 - f) / 360.0F)) * 360.0);
      }

      while (f2 - f >= 360.0F) {
         f += f3;
      }

      while (f2 - f < -360.0F) {
         f -= f3;
      }

      return f;
   }

   public YawPitchHelper() {
      this.realLastYaw = 0.0F;
      this.realLastPitch = 0.0F;
   }

   public float wrapRelativeYaw(float f, float f2) {
      int[] helperStateFlags2 = getHelperStateFlags();

      while (f - f2 >= 360.0F) {
         f2 += 360.0F;
      }

      while (f - f2 < -360.0F) {
         f2 -= 360.0F;
      }

      return f2;
   }

   public void syncPlayerAngles(EntityPlayer player, boolean flag) {
      int[] helperStateFlags2 = getHelperStateFlags();
      if (flag) {
         Augustus.getInstance().getYawPitchHelper().realYaw = player.rotationYaw;
         Augustus.getInstance().getYawPitchHelper().realPitch = player.rotationPitch;
         Augustus.getInstance().getYawPitchHelper().realLastYaw = player.prevRotationYaw;
         Augustus.getInstance().getYawPitchHelper().realLastPitch = player.prevRotationPitch;
      } else if (player.rotationYaw < 500.0F) {
         RotationController rotationController = ClientManagers.rotationController;
         Rotation rotation = new Rotation(player.prevRotationYaw, player.prevRotationPitch);
         rotationController.setPreviousRotation(rotation);
         rotationController = ClientManagers.rotationController;
         Rotation rotation2 = new Rotation(player.rotationYaw, player.rotationPitch);
         rotationController.setCurrentRotation(rotation2);
         float rotationPitch = player.rotationPitch;
         float rotationYaw = player.rotationYaw;
         ClientManagers.rotationController.syncCameraAngles(rotationYaw, rotationPitch);
      }
   }

   public static void setHelperStateFlags(int[] ints) {
      helperStateFlags = ints;
   }

   public static int[] getHelperStateFlags() {
      return helperStateFlags;
   }

   static {
      if (getHelperStateFlags() != null) {
         setHelperStateFlags(new int[5]);
      }
   }
}
