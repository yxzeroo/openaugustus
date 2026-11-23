package net.augustus.modules.modes;

import net.augustus.config.NamedConfigComponent;
import net.augustus.events.EventManager;
import java.util.function.Supplier;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.interfaces.MC;

public abstract class AbstractSubMode implements MC, NamedConfigComponent {
   private boolean enabled;
   private Supplier visibilitySupplier;
   private final String name;
   public final Module parentModule;
   public final DoubleValue modeSetting;
   private static int[] controlFlowState;

   public void setEnabled(boolean flag) {
      int[] controlFlowState2 = getControlFlowState();
      if (this.enabled != flag) {
         this.updateEventSubscriptions();
         if (this.isEnabled()) {
            this.onDisable();
         } else {
            this.onEnable();
         }
      }

      this.enabled = flag;
   }

   public void setVisibilitySupplier(Supplier supplier) {
      this.visibilitySupplier = supplier;
   }

   public void toggleEventSubscription(int n, int n2) {
      int[] controlFlowState2 = getControlFlowState();
      if (this.isEnabled()) {
         EventManager.getEventBus().unsubscribe(n, this);
      } else {
         EventManager.getEventBus().subscribe(n, this, n2);
      }
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public abstract void updateEventSubscriptions();

   public AbstractSubMode(Module module, DoubleValue doubleValue, String str) {
      this.modeSetting = doubleValue;
      this.name = str;
      this.parentModule = module;
   }

   public String getName() {
      return this.name;
   }

   public void toggleEventSubscription(int n) {
      byte b = 0;
      this.toggleEventSubscription(n, b);
   }

   public abstract void onDisable();

   public abstract void onEnable();

   public boolean isSelected() {
      int[] controlFlowState2 = getControlFlowState();
      return this.visibilitySupplier != null && (Boolean)this.visibilitySupplier.get();
   }

   public static void setControlFlowState(int[] ints) {
      controlFlowState = ints;
   }

   public static int[] getControlFlowState() {
      return controlFlowState;
   }

   static {
      if (getControlFlowState() == null) {
         setControlFlowState(new int[4]);
      }
   }
}
