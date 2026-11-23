package net.augustus.modules.fun;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.misc.AugustusServer;
import net.augustus.ClientManagers;
import net.augustus.settings.property.BooleanProperty;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.audio.JavaFxAudioPlayer;
import net.augustus.events.EventManager;
import net.augustus.utils.animation.AnimationUtil;
import net.minecraft.network.PacketThreadUtil;

public class ShuffleMode extends Module implements TickListener {
   private final JavaFxAudioPlayer audioPlayer;
   public boolean activatedOnce;

   public ShuffleMode() {
       super("ShuffleMode", "MischMode", new Color(102, 242, 207), Categorys.FUN);
      this.audioPlayer = new JavaFxAudioPlayer();
      this.activatedOnce = false;
      this.setLoadFromConfig(false);
   }

   @Override
   public void onEnable() {
      EventManager.getEventBus().subscribe(40, this);
      this.activatedOnce = true;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void toggle(boolean flag) {
      if (!this.activatedOnce) {
         super.toggle(flag);
      } else {
         String selectedValue = ClientManagers.moduleManager.arrayList.toggleAnimation2.getSelectedValue();
         if ((Boolean)this.mT.getValue()) {
            BooleanProperty mt = this.mT;
            Boolean bool = false;
            mt.setValue(bool);
            if (mc.theWorld != null && ClientManagers.moduleManager.augustusServer.moduleToggleSound.getValue() && this.isToggleSoundEnabled()) {
               PacketThreadUtil.runOnMainThread(() -> AugustusServer.playModuleDisableSound());
            }

            this.onDisable();
            if (!selectedValue.equals("None")) {
               this.toggleAnimation.start((int)ClientManagers.moduleManager.arrayList.animationTime.getValue(), AnimationUtil.parseEasingType(selectedValue), 0, false);
            }
         } else {
            BooleanProperty mt2 = this.mT;
            Boolean bool3 = true;
            mt2.setValue(bool3);
            if (mc.theWorld != null && ClientManagers.moduleManager.augustusServer.moduleToggleSound.getValue() && this.isToggleSoundEnabled()) {
               PacketThreadUtil.runOnMainThread(() -> AugustusServer.playModuleEnableSound());
            }

            this.onEnable();
            if (!selectedValue.equals("None")) {
               this.toggleAnimation.start((int)ClientManagers.moduleManager.arrayList.animationTime.getValue(), AnimationUtil.parseEasingType(selectedValue), 1, false);
            }
         }
      }
   }

   @Override
   public void onTick2() {
      if (!this.audioPlayer.isPlaying()) {
         try {
            String str = "sounds/shuffle.mp3";
            this.audioPlayer.playResource(str, true);
            this.audioPlayer.setCycleCount((short)10000);
         } catch (Exception e) {
         }
      }

      ArrayList list = new ArrayList();

      for (Module module : (Iterable<Module>)(ClientManagers.moduleManager.getModules())) {
         list.add(module.getName2());
      }

      Collections.shuffle(list);
      int n = 0;

      for (Module module2 : (Iterable<Module>)(ClientManagers.moduleManager.getModules())) {
         module2.setName((String)list.get(n));
         n++;
      }

      ClientManagers.moduleManager.sortModules();
   }

}
