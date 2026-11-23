package net.augustus.modules.modes.noweb;

import net.augustus.events.EventUpdate;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.DoubleValue;

public class IgnoreNoWebMode extends AbstractNoWebMode {
   public IgnoreNoWebMode(NoWeb noWeb, DoubleValue doubleValue) {
      super(noWeb, doubleValue, "Ignore");
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onUpdate(EventUpdate event) {
   }

   @Override
   public void updateEventSubscriptions() {
   }
}
