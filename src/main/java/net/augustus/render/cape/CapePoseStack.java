package net.augustus.render.cape;

import net.augustus.render.waveycape.CapeMathHelper;
import net.augustus.render.waveycape.CapeMatrix3f;
import net.augustus.render.waveycape.CapeMatrix4f;
import net.augustus.render.waveycape.CapePoseEntry;
import net.augustus.render.waveycape.CapeQuaternion;
import com.google.common.collect.Queues;
import java.util.Deque;

public class CapePoseStack {
   private final Deque poseStack = Queues.newArrayDeque();

   public void pushPose() {
      CapePoseEntry last = (CapePoseEntry)this.poseStack.getLast();
      this.poseStack.addLast(new CapePoseEntry(last.pose.copy(), last.normal.copy()));
   }

   public void popPose() {
      this.poseStack.removeLast();
   }

   public CapePoseStack() {
      CapeMatrix4f capeMatrix4f = new CapeMatrix4f();
      capeMatrix4f.setIdentity();
      CapeMatrix3f capeMatrix3f = new CapeMatrix3f();
      capeMatrix3f.setIdentity();
      this.poseStack.add(new CapePoseEntry(capeMatrix4f, capeMatrix3f));
   }

   public void mulPose(CapeQuaternion capeQuaternion) {
      CapePoseEntry last = (CapePoseEntry)this.poseStack.getLast();
      last.pose.multiply(capeQuaternion);
      last.normal.mul(capeQuaternion);
   }

   public void translate(double d, double d2, double d3) {
      CapePoseEntry last = (CapePoseEntry)this.poseStack.getLast();
      float f = (float)d;
      float f2 = (float)d2;
      float f3 = (float)d3;
      float f4 = f2;
      float f5 = f;
      last.pose.multiplyWithTranslation(f5, f4, f3);
   }

   public CapePoseEntry last() {
      return (CapePoseEntry)this.poseStack.getLast();
   }

   public void scale(float f, float f2, float f3) {
      CapePoseEntry last = (CapePoseEntry)this.poseStack.getLast();
      CapeMatrix4f pose = last.pose;
      CapeMatrix4f capeMatrix4f = CapeMatrix4f.createScaleMatrix(f, f2, f3);
      pose.multiply2(capeMatrix4f);
      if (f == f2 && f2 == f3) {
         if (f > 0.0F) {
            return;
         }

         float f4 = -1.0F;
         last.normal.mul2(f4);
      }

      float f5 = 1.0F / f;
      float f6 = 1.0F / f2;
      float f7 = 1.0F / f3;
      float f8 = CapeMathHelper.fastInvCubeRoot(f5 * f6 * f7);
      CapeMatrix3f normal = last.normal;
      CapeMatrix3f capeMatrix3f = CapeMatrix3f.createScaleMatrix(f8 * f5, f8 * f6, f8 * f7);
      normal.mul3(capeMatrix3f);
   }
}
