package net.augustus.utils.simulation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class EntityMotionSnapshot {
   public Vec3 prevPrevPos;
   public Vec3 currentPos;
   public float prevRotationYawHead;
   public Vec3 prevPos;
   public boolean sneaking;
   public boolean onGround;

   public EntityMotionSnapshot(EntityLivingBase entity) {
       super();
      byte b = 0;
      this.currentPos = (Vec3)entity.positionHistory.getFromEnd(b);
      byte b2 = 1;
      this.prevPos = (Vec3)entity.positionHistory.getFromEnd(b2);
      byte b3 = 2;
      this.prevPrevPos = (Vec3)entity.positionHistory.getFromEnd(b3);
      this.onGround = entity.onGround;
      this.sneaking = entity.isSneaking();
      this.prevRotationYawHead = entity.prevRotationYawHead;
   }
}
