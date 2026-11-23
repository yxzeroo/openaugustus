package net.augustus.ui.menu;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.events.EventMotion;
import net.augustus.ui.AugustusGuiScreen;
import net.augustus.utils.audio.ClientSounds;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class AbstractMainMenuScreen extends AugustusGuiScreen {
   public final String optionsLabel;
   private static final int CORNER_PADDING = 2;
   public final String multiplayerLabel;
   public final String quitLabel;
   public final String singleplayerLabel = I18n.format("menu.singleplayer", new Object[0]);

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      int[] controlFlowState = EventMotion.getControlFlowState();
      if (Augustus.getInstance().firstStart) {
         if (ClientManagers.moduleManager.augustusServer.welcomeSound.getValue()) {
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE3);
         }

         Augustus.getInstance().getTimeHelper().reset();
         Augustus.getInstance().firstStart = false;
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
      Augustus instance = Augustus.getInstance();
      String str = ClientManagers.moduleManager.getModules().size() + " modules and " + instance.getSettingManager().getSettings().size() + " settings loaded";
      float f = (float)(this.width - this.getStringWidth(str) - 2);
      float f2 = 2.0F;
      float f3 = f;
      this.drawString(str, f3, f2);
      String str2 = "§e"
         + Augustus.getInstance().getUsername()
         + " §7[§e"
         + Augustus.getInstance().getUserLevel()
         + "§7]§r ["
         + Augustus.getInstance().getUserRankColor()
         + Augustus.getInstance().getUserRank()
         + "§r]";
      float f4 = 2.0F;
      float f5 = 2.0F;
      this.drawString(str2, f5, f4);
      String str3 = "Created by " + instance.getAuthors();
      f = (float)(this.width - this.getStringWidth(str3) - 2);
      float f6 = (float)(this.height - this.fontRendererObj.FONT_HEIGHT - 2);
      float f7 = f;
      this.drawString(str3, f7, f6);
      String version = instance.getVersion();
      float f8 = (float)(this.height - this.fontRendererObj.FONT_HEIGHT - 2);
      float f9 = 2.0F;
      String str4 = version;
      this.drawString(str4, f9, f8);
   }

   public AbstractMainMenuScreen() {
      this.multiplayerLabel = I18n.format("menu.multiplayer", new Object[0]);
      this.optionsLabel = I18n.format("menu.options", new Object[0]);
      this.quitLabel = I18n.format("menu.quit", new Object[0]);
   }

   public static GuiScreen createMainMenuScreen() {
      return (GuiScreen)(Augustus.getInstance().getModuleManager().augustusServer.sideMainMenu.getValue() ? new SideMainMenuScreen() : new ClassicMainMenuScreen());
   }


}
