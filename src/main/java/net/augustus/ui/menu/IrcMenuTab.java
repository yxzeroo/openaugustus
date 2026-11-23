package net.augustus.ui.menu;

import net.augustus.Augustus;
import net.augustus.commands.AbstractIrcCommand;
import net.augustus.commands.Command;
import net.augustus.commands.CommandManager;
import net.augustus.render.RenderUtil;
import net.augustus.ui.AugustusGuiScreen;
import net.augustus.ui.GuiButtonClickHandler;
import net.augustus.ui.widgets.ScrollBar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.opengl.GL11;

public class IrcMenuTab extends ClientMenuTab {
   private GuiTextField chatInputField;
   private final List wrappedMessages;
   private final List rawMessages;
   private final ScrollBar scrollBar;

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.scrollBar.handleMouseWheel();
   }

   public IrcMenuTab() {
       super("IRC");
      this.scrollBar = new ScrollBar(Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 1);
      this.rawMessages = new ArrayList();
      this.wrappedMessages = new ArrayList();
   }

   public void initContent() {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      super.initContent();
      if (this.mc.theWorld == null) {
      }

      int n = this.width - 15 - 2;
      int n2 = this.width - 15;
      int n3 = this.height - 15 - 20 - 5 - 1;
      int n4 = n2;
      byte b = 67;
      int n5 = n;
      this.scrollBar.setBounds(n5, b, n4, n3);
      int n6 = this.height - 30 - 10 + 1;
      n2 = this.width - 102 - 30 - 40 - 2;
      byte b2 = 18;
      int n7 = n2;
      int n8 = n6;
      byte b3 = 118;
      this.chatInputField = ((AugustusGuiScreen)this).addTextField(b3, n8, n7, b2);
      int n9 = this.width - 15 - 40;
      n = this.height - 30 - 10;
      GuiButtonClickHandler guiButtonClickHandler = button -> {
         String str = this.chatInputField.getText().trim();
         this.submitInput(str);
      };
      String str2 = "Send";
      byte b4 = 20;
      byte b5 = 40;
      int n10 = n;
      int n11 = n9;
      ((AugustusGuiScreen)this).addButton(n11, n10, b5, b4, str2, guiButtonClickHandler);
      this.rewrapMessages();
   }

   public void appendMessage(String str) {
      this.rawMessages.add(str);
      this.rewrapMessages();
   }

   public void keyTyped(char typedChar, int keyCode) throws IOException {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      if (keyCode == 28) {
         String str = this.chatInputField.getText().trim();
         String str2 = str;
         this.submitInput(str2);
      }

      this.chatInputField.textboxKeyTyped(typedChar, keyCode);
   }

   public void onGuiClosed() {
      super.onGuiClosed();
      if (this.mc.theWorld == null) {
      }
   }

   private void rewrapMessages() {
      this.wrappedMessages.clear();
      CreditsSection.getControlFlowGuard();

      for (String str : (Iterable<String>)(this.rawMessages)) {
         if (this.mc.fontRendererObj.getStringWidth(str) > this.width - 102 - 30 - 5) {
            this.wrappedMessages.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(str, this.width - 102 - 30 - 5));
         } else {
            this.wrappedMessages.add(str);
         }
      }

      boolean flag;
      exitBlock: {
         if (!this.scrollBar.fitsWithoutScrolling()) {
            if (!this.scrollBar.isAtBottom()) {
               flag = false;
               break exitBlock;
            }
         }

         flag = true;
      }

      boolean flag2 = flag;
      ScrollBar scrollBar2 = this.scrollBar;
      int size = this.wrappedMessages.size();
      scrollBar2.setItemCount(size);
      if (flag2) {
         this.scrollBar.scrollToBottom();
      }
   }

   private void submitInput(String str) {
      String selectedValue = Augustus.getInstance().getModuleManager().augustusServer.commandPrefixes.getSelectedValue();
      CreditsSection.getControlFlowGuard();
      CommandManager commandManager = Augustus.getInstance().getCommandManager();
      String[] strings = str.split(" ");
      if (!str.startsWith(selectedValue)) {
         String[] strings2 = new String[strings.length + 1];
         strings2[0] = "";
         System.arraycopy(strings, 0, strings2, 1, strings.length);
         commandManager.ircCommand.commandAction(strings2, str);
         this.chatInputField.setText("");
      } else {
         for (Command command : (Iterable<Command>)(commandManager.getCommands())) {
            if (command instanceof AbstractIrcCommand && strings.length > 0 && strings[0].equalsIgnoreCase(command.getCommand())) {
               String[] strings3 = str.split(" ");
               command.commandAction(strings3, str);
               this.chatInputField.setText("");
               return;
            }
         }
      }
   }

   private void onSendButtonClick(GuiButton button) {
      String str = this.chatInputField.getText().trim();
      String str2 = str;
      this.submitInput(str2);
   }

   public void drawContent(int n, int n2, float f) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      this.scrollBar.updateAndDraw();
      AugustusGuiScreen.enableScissor(102, 67, this.width - 15 - 2 - 1 + (this.scrollBar.fitsWithoutScrolling() ? 3 : 0), this.height - 15 - 20 - 5 - 1);
      GL11.glPushMatrix();
      GL11.glTranslatef(117.0F, 67.0F, 0.0F);
      int n3 = this.width - 102 - 30 + (this.scrollBar.fitsWithoutScrolling() ? 3 : 0);
      int n4 = this.height - 52 - 30 - 20 - 5;
      int n5 = -2147483648;
      int n6 = n4;
      int n7 = n3;
      byte b = 0;
      RenderUtil.drawRect3(0, b, n7, n6, n5);

      for (int i = 0; i < this.wrappedMessages.size(); i++) {
         String str = (String)this.wrappedMessages.get(i);
         Minecraft.getMinecraft()
            .fontRendererObj
            .drawString(str, 2, (int)((double)(2 + i * (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 1)) + this.scrollBar.getClampedScrollOffset()), -1);
      }

      GL11.glPopMatrix();
      AugustusGuiScreen.disableScissor();
   }

}
