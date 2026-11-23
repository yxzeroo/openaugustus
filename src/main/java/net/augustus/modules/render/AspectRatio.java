package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPerspective;
import net.augustus.events.listeners.PerspectiveListener;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;

public class AspectRatio extends Module implements PerspectiveListener {
   private final StringValue aspect;
   private final BooleanValue hand;

   public AspectRatio() {
       super("AspectRatio", "BildschirmVerzerrung", new Color(235, 119, 110), Categorys.RENDER);
      this.aspect = new StringValue(this, "Aspect", 1.0, 0.0, 5.0, 2);
      this.hand = new BooleanValue(this, "Hand", true);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 48;
      this.toggleEventSubscription(b);
   }

   public void onPerspective(EventPerspective event) {
      if (!event.isHandPass() || this.hand.getValue()) {
         float f = event.getAspectRatio() * (float)this.aspect.getValue();
         event.setAspectRatio(f);
      }
   }

}
