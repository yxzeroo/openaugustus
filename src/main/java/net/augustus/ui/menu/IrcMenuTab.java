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
      super.initContent();
      if (this.mc.theWorld == null) {
      }

      int n = this.width - 15 - 2;
      int n2 = this.width - 15;
      int n3 = this.height - 15 - 20 - 5 - 1;
      int n4 = n2;
      int n5 = n;
      this.scrollBar.setBounds(n5, (byte)67, n4, n3);
      int n6 = this.height - 30 - 10 + 1;
      n2 = this.width - 102 - 30 - 40 - 2;
      int n7 = n2;
      this.chatInputField = ((AugustusGuiScreen)this).addTextField((byte)118, n6, n7, (byte)18);
      int n9 = this.width - 15 - 40;
      n = this.height - 30 - 10;
      GuiButtonClickHandler guiButtonClickHandler = button -> {
         this.submitInput(this.chatInputField.getText().trim());
      };
      String str2 = "Send";
      int n10 = n;
      ((AugustusGuiScreen)this).addButton(n9, n10, (byte)40, (byte)20, str2, guiButtonClickHandler);
      this.rewrapMessages();
   }

   public void appendMessage(String str) {
      this.rawMessages.add(str);
      this.rewrapMessages();
   }

   public void keyTyped(char typedChar, int keyCode) throws IOException {
      if (keyCode == 28) {
         this.submitInput(this.chatInputField.getText().trim());
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
      this.scrollBar.setItemCount(this.wrappedMessages.size());
      if (flag2) {
         this.scrollBar.scrollToBottom();
      }
   }

   private void submitInput(String str) {
      String selectedValue = Augustus.getInstance().getModuleManager().augustusServer.commandPrefixes.getSelectedValue();
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
               command.commandAction(str.split(" "), str);
               this.chatInputField.setText("");
               return;
            }
         }
      }
   }

   private void onSendButtonClick(GuiButton button) {
      this.submitInput(this.chatInputField.getText().trim());
   }

   public void drawContent(int n, int n2, float f) {
      this.scrollBar.updateAndDraw();
      AugustusGuiScreen.enableScissor(102, 67, this.width - 15 - 2 - 1 + (this.scrollBar.fitsWithoutScrolling() ? 3 : 0), this.height - 15 - 20 - 5 - 1);
      GL11.glPushMatrix();
      GL11.glTranslatef(117.0F, 67.0F, 0.0F);
      RenderUtil.drawRect3(0, (byte)0, this.width - 102 - 30 + (this.scrollBar.fitsWithoutScrolling() ? 3 : 0), this.height - 52 - 30 - 20 - 5, -2147483648);

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
