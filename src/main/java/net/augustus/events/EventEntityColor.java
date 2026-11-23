package net.augustus.events;

import net.augustus.events.listeners.EntityColorListener;
import net.augustus.utils.render.ColorUtil;
import de.florianmichael.dietrichevents2.AbstractEvent;
import net.minecraft.entity.EntityLivingBase;

public class EventEntityColor extends AbstractEvent {
   public static final int ID = 10;
   private float red;
   private float alpha;
   private boolean overrideColor = false;
   private float blue;
   private EntityLivingBase entity;
   private float green;

   public void setGreen(float f) {
      this.green = f;
   }

   public float getAlpha() {
      return this.alpha;
   }

   public void setBlue(float f) {
      this.blue = f;
   }

   public float getBlue() {
      return this.blue;
   }

   private void reset(EntityLivingBase entity2, float f, float f2, float f3, float f4) {
      this.red = f;
      this.green = f2;
      this.blue = f3;
      this.alpha = f4;
      this.entity = entity2;
      this.overrideColor = false;
   }

   public float getGreen() {
      return this.green;
   }

   public void setEntity(EntityLivingBase entity2) {
      this.entity = entity2;
   }

   @Override
   public void call(Object obj) {
      EntityColorListener listener = (EntityColorListener)obj;
      this.call2(listener);
   }

   public void setRed(float f) {
      this.red = f;
   }

   public void call2(EntityColorListener listener) {
      listener.onEntityColor(this);
   }

   public float getRed() {
      return this.red;
   }

   public EntityLivingBase getEntity() {
      return this.entity;
   }

   public boolean isOverrideColor() {
      return this.overrideColor;
   }

   public void setOverrideColor(boolean flag) {
      this.overrideColor = flag;
   }

   public void setAlpha(float f) {
      this.alpha = f;
   }

   public static void reset(EventEntityColor event, EntityLivingBase entity2, float f, float f2, float f3, float f4) {
      event.reset(entity2, f, f2, f3, f4);
   }

   public void setColor(int n) {
      this.red = ColorUtil.getRedFloat(n);
      this.green = ColorUtil.getGreenFloat(n);
      this.blue = ColorUtil.getBlueFloat(n);
      this.alpha = ColorUtil.getAlphaFloat(n);
   }
}
