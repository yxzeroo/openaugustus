package net.augustus.events;

import net.augustus.events.listeners.EntityHurtListener;
import de.florianmichael.dietrichevents2.AbstractEvent;
import net.minecraft.entity.EntityLivingBase;

public class EventEntityHurt extends AbstractEvent {
   public EntityLivingBase entity;
   public static final int ID = 50;

   private void setEntity(EntityLivingBase entity2) {
      this.entity = entity2;
   }

   @Override
   public void call(Object obj) {
      EntityHurtListener listener = (EntityHurtListener)obj;
      this.call2(listener);
   }

   public EntityLivingBase getEntity() {
      return this.entity;
   }

   public static void reset(EventEntityHurt event, EntityLivingBase entity2) {
      event.setEntity(entity2);
   }

   public void call2(EntityHurtListener listener) {
      listener.onEntityHurt(this);
   }
}
