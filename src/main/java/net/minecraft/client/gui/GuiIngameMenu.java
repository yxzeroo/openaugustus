package net.minecraft.client.gui;

import wdl.WDLHooks;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.io.IOException;
import net.augustus.modules.Module;
import net.augustus.ClientManagers;
import net.augustus.ui.multiplayer.ServerListHost;
import net.augustus.ui.menu.ClientTabContainerScreen;
import net.augustus.ui.menu.AbstractMainMenuScreen;
import net.augustus.render.RenderUtil;
import net.augustus.Augustus;
import net.augustus.quests.QuestLevelCalculator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.resources.I18n;

public class GuiIngameMenu extends GuiScreen implements ServerListHost {
   private ServerData reconnectServerData;
   private static final OldServerPinger oldServerPinger = new OldServerPinger();
   private String hoveringText;

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      exitBlock: {
         this.drawDefaultBackground();
         if (((Module)ClientManagers.moduleManager.invisMode).isToggled()) {
            if (ClientManagers.moduleManager.invisMode.shouldDisableCustomEscapeMenu()) {
               break exitBlock;
            }
         }

         this.drawQuestProgressBar();
      }

      drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   @Override
   public void initGui() {
      byte b;
      boolean flag;
      exitBlock: {
         this.buttonList.clear();
         b = -16;
         if (((Module)ClientManagers.moduleManager.invisMode).isToggled()) {
            if (ClientManagers.moduleManager.invisMode.shouldDisableCustomEscapeMenu()) {
               flag = true;
               break exitBlock;
            }
         }

         flag = false;
      }

      boolean flag2 = flag;
      if (flag2) {
         this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + b, I18n.format("menu.returnToMenu", new Object[0])));
         if (!this.mc.isIntegratedServerRunning()) {
            ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
         }

         this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + b, I18n.format("menu.returnToGame", new Object[0])));
         this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + b, 98, 20, I18n.format("menu.options", new Object[0])));
         GuiButton button;
         this.buttonList.add(button = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + b, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
         this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + b, 98, 20, I18n.format("gui.achievements", new Object[0])));
         this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + b, 98, 20, I18n.format("gui.stats", new Object[0])));
         button.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
      } else {
         this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + b, I18n.format("menu.returnToMenu", new Object[0])));
         if (!this.mc.isIntegratedServerRunning()) {
            ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
         }

         this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + b, I18n.format("menu.returnToGame", new Object[0])));
         this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + b, 98, 20, I18n.format("menu.options", new Object[0])));
         GuiButton button2;
         this.buttonList.add(button2 = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + b, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
         this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + b, 98, 20, I18n.format("gui.achievements", new Object[0])));
         this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + b, 98, 20, I18n.format("gui.stats", new Object[0])));
         button2.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
         this.buttonList.add(new GuiButton(1000, this.width / 2 - 100, this.height / 4 + 72 + b, 98, 20, Augustus.getInstance().getName()));
         this.buttonList.add(new GuiButton(1001, this.width / 2 + 2, this.height / 4 + 72 + b, 98, 20, I18n.format("selectServer.direct", new Object[0])));
         WDLHooks.injectWDLButtons(this, this.buttonList);
      }
   }

   public OldServerPinger getOldServerPinger() {
      return oldServerPinger;
   }

   public void renderHoveringText(GuiScreen screen, int n, int n2) {
      if (this.hoveringText != null) {
         screen.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), n, n2);
      }
   }

   @Override
   public void actionPerformed(GuiButton button) throws IOException {
      WDLHooks.handleWDLButtonClick(this, button);
      switch (button.id) {
         case 0:
            Minecraft mc2 = this.mc;
            GuiOptions guiOptions = new GuiOptions(this, this.mc.gameSettings);
            mc2.displayGuiScreen(guiOptions);
            break;
         case 1:
            button.enabled = false;
            this.disconnectFromWorld();
         case 2:
         case 3:
         default:
            break;
         case 4:
            GuiScreen screen = (GuiScreen)null;
            this.mc.displayGuiScreen(screen);
            this.mc.setIngameFocus();
            break;
         case 5:
            Minecraft mc3 = this.mc;
            GuiAchievements guiAchievements = new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter());
            mc3.displayGuiScreen(guiAchievements);
            break;
         case 6:
            Minecraft mc4 = this.mc;
            GuiStats guiStats = new GuiStats(this, this.mc.thePlayer.getStatFileWriter());
            mc4.displayGuiScreen(guiStats);
            break;
         case 7:
            Minecraft mc5 = this.mc;
            GuiShareToLan guiShareToLan = new GuiShareToLan(this);
            mc5.displayGuiScreen(guiShareToLan);
            break;
         case 1000:
            Minecraft mc6 = this.mc;
            ClientTabContainerScreen clientTabContainerScreen = new ClientTabContainerScreen(this, 0);
            mc6.displayGuiScreen(clientTabContainerScreen);
            break;
         case 1001:
            Minecraft mc7 = this.mc;
            GuiScreenServerList guiScreenServerList = new GuiScreenServerList(
               this, this.reconnectServerData = new ServerData("", this.mc.isSingleplayer() ? "" : this.mc.getCurrentServerData().serverIP, false)
            );
            mc7.displayGuiScreen(guiScreenServerList);
      }
   }

   private void disconnectFromWorld() {
      boolean flag = this.mc.isIntegratedServerRunning();
      this.mc.theWorld.sendQuittingDisconnectingPacket();
      WorldClient world = (WorldClient)null;
      this.mc.loadWorld(world);
      if (flag) {
         Minecraft mc2 = this.mc;
         GuiMainMenu guiMainMenu = new GuiMainMenu();
         mc2.displayGuiScreen(guiMainMenu);
      } else {
         Minecraft mc3 = this.mc;
         GuiMultiplayer guiMultiplayer = new GuiMultiplayer(new GuiMainMenu());
         mc3.displayGuiScreen(guiMultiplayer);
      }
   }

   private void drawQuestProgressBar() {
      int n = this.width / 2;
      int experienceForLevel = QuestLevelCalculator.getExperienceForLevel(Augustus.getInstance().getUserLevel());
      int userExperience = Augustus.getInstance().getUserExperience();
      int n2 = n - 100;
      int n3 = n + 100;
      RenderUtil.drawRect3(n2, 15, n3, 28, new Color(0, 0, 0, 150).getRGB());
      RenderUtil.drawRect(
         (double)n2,
         15.0,
         (double)n2 + (double)(n3 - n2) * ((double)userExperience / (double)experienceForLevel),
         28.0,
         Augustus.getInstance().getClientColor()
      );
      String str = userExperience + "/" + experienceForLevel + " ep, lv " + Augustus.getInstance().getUserLevel();
      int stringWidth = this.fontRendererObj.getStringWidth(str);
      this.fontRendererObj.drawString(str, n - stringWidth / 2, 18, Color.WHITE.getRGB());
   }

   @Override
   public void confirmClicked(boolean flag, int n) {
      if (flag) {
         this.disconnectFromWorld();
         Minecraft mc2 = this.mc;
         GuiConnecting guiConnecting = new GuiConnecting(new GuiMultiplayer(AbstractMainMenuScreen.createMainMenuScreen()), this.mc, this.reconnectServerData);
         mc2.displayGuiScreen(guiConnecting);
      } else {
         this.mc.displayGuiScreen(this);
      }
   }

   public void setHoveringText(String str) {
      this.hoveringText = str;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
   }

}
