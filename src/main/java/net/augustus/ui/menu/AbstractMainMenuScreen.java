package net.augustus.ui.menu;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
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
      this.drawString(str, f, 2.0F);
      String str2 = "§e"
         + Augustus.getInstance().getUsername()
         + " §7[§e"
         + Augustus.getInstance().getUserLevel()
         + "§7]§r ["
         + Augustus.getInstance().getUserRankColor()
         + Augustus.getInstance().getUserRank()
         + "§r]";
      float f4 = 2.0F;
      this.drawString(str2, 2.0F, f4);
      String str3 = "Created by " + instance.getAuthors();
      f = (float)(this.width - this.getStringWidth(str3) - 2);
      this.drawString(str3, f, (float)(this.height - this.fontRendererObj.FONT_HEIGHT - 2));
      String version = instance.getVersion();
      float f8 = (float)(this.height - this.fontRendererObj.FONT_HEIGHT - 2);
      this.drawString(version, 2.0F, f8);
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
