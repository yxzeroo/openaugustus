package net.augustus.modules.misc;

import net.minecraft.world.WorldSettings;
import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.BooleansSetting;

public class FakeGamemode extends Module implements TickListener {
   private WorldSettings.GameType previousGameType;
   private BooleansSetting gamemode;

   public FakeGamemode() {
       super("FakeGamemode", "UnechterSpielmodus", new Color(243, 245, 152), Categorys.MISC);
      this.previousGameType = WorldSettings.GameType.SURVIVAL;
      this.gamemode = (BooleansSetting)(new BooleansSetting(this, "Gamemode", "Creative", new String[]{"Survival", "Creative", "Adventure", "Spectator"}))
         .markVisualOnly();
   }

   public void onEnable() {
      if (mc.playerController != null) {
         this.previousGameType = mc.playerController.getCurrentGameType();
      }
   }

   public void onDisable() {
      mc.playerController.setGameType(this.previousGameType);
   }

   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   public void onTick2() {
      String selectedValue = this.gamemode.getSelectedValue();
      WorldSettings.GameType gameType;
      switch (selectedValue) {
         case "Survival":
            gameType = WorldSettings.GameType.SURVIVAL;
            break;
         case "Creative":
            gameType = WorldSettings.GameType.CREATIVE;
            break;
         case "Adventure":
            gameType = WorldSettings.GameType.ADVENTURE;
            break;
         case "Spectator":
            gameType = WorldSettings.GameType.SPECTATOR;
            break;
         default:
            gameType = mc.playerController.getCurrentGameType();
      }

      mc.playerController.setGameType(gameType);
   }

}
