package net.augustus.modules.legit;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.ClientManagers;
import net.augustus.utils.rotation.RayCastUtil;
import net.augustus.events.listeners.RayCastListener;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class HitBox extends Module implements RayCastListener {
   public final StringValue expandXZ;
   public final StringValue expandY;
   public final BooleanValue serverHitBox;
   public final BooleanValue n18Hitbox;
   public final BooleanValue optifineHitbox;

   public HitBox() {
       super("HitBox", "SchlagBox", new Color(180, 207, 130), Categorys.LEGIT);
      this.expandXZ = new StringValue(this, "ExpandXZ", 0.0, 0.0, 1.0, 2);
      this.expandY = new StringValue(this, "ExpandY", 0.0, 0.0, 1.0, 2);
      this.serverHitBox = new BooleanValue(this, "ServerHitBox", false);
      this.n18Hitbox = new BooleanValue(this, "1.8 Hitbox", true);
      this.optifineHitbox = new BooleanValue(this, "OptifineHitbox", false);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 29;
      this.toggleEventSubscription(b);
   }

   public void onRayCast() {
      if (this.optifineHitbox.getValue()) {
         if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
            MovingObjectPosition hit = RayCastUtil.rayCast(1.0F, mc.thePlayer.rotationYawHead, mc.thePlayer.rotationPitch);
            if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
               mc.objectMouseOver = hit;
            }
         }
      }
   }

   public AxisAlignedBB getExpandedBoundingBox(Entity entity, float f) {
      AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
      float f2 = (float)((double)f + ClientManagers.moduleManager.hitBox.expandXZ.getValue());
      float f3 = (float)((double)f + ClientManagers.moduleManager.hitBox.expandY.getValue());
      if (ClientManagers.moduleManager.hitBox.serverHitBox.getValue()) {
         if (this.isLicenseValid() && entity instanceof EntityLivingBase) {
            Vec3 vec = new Vec3((double)entity.serverPosX / 32.0, (double)entity.serverPosY / 32.0, (double)entity.serverPosZ / 32.0);
            float f4 = entity.width / 2.0F;
            entityBoundingBox = entityBoundingBox.union(
               new AxisAlignedBB(
                     vec.xCoord - (double)f4,
                     vec.yCoord,
                     vec.zCoord - (double)f4,
                     vec.xCoord + (double)f4,
                     vec.yCoord + (double)entity.height,
                     vec.zCoord + (double)f4
                  )
                  .expand((double)f, (double)f, (double)f)
            );
         }
      }

      return entityBoundingBox.expand((double)f2, (double)f3, (double)f2);
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
