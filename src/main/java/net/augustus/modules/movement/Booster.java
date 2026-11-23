package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.settings.Setting;
import net.augustus.settings.KeybindSetting;
import net.augustus.events.listeners.TimerListener;
import net.augustus.events.EventTimer;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;

public class Booster extends Module implements TimerListener {
   public final StringValue boostTicks;
   public final StringValue delay;
   public final KeybindSetting keyToToggleBoost;
   private final TimeHelper timeHelper;

   public Booster() {
       super("Booster", "Beschläuniger", new Color(214, 84, 186), Categorys.MOVEMENT);
      this.boostTicks = new StringValue(this, "BoostTicks", 2.0, 0.0, 20.0, 0);
      this.delay = new StringValue(this, "Delay", 2000.0, 0.0, 10000.0, 0);
      this.keyToToggleBoost = new KeybindSetting(this, "Key to toggle boost");
      this.timeHelper = new TimeHelper();
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 41;
      this.toggleEventSubscription(b);
   }

   public void onTimer2(EventTimer event) {
   }

   public void onTimer(EventTimer event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (KeyboardInputUtil.isKeyDown(this.keyToToggleBoost.getKeyCode())) {
         TimeHelper timeHelper2 = this.timeHelper;
         double value = this.delay.getValue();
         if (timeHelper2.reached(value)) {
            int n = (int)Math.max(0.0, this.boostTicks.getValue());
            event.setExtraTicks(n);
            this.timeHelper.reset();
            String str = "Boosted";
            this.sendChatMessage2(str);
         }
      }
   }

}
