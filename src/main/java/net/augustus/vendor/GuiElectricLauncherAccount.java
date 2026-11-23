package net.augustus.vendor;

import java.awt.Color;
import java.io.IOException;
import net.augustus.utils.TextFormatUtil;
import net.augustus.ui.widgets.ScrollBar;
import net.augustus.ui.menu.CreditsSection;
import net.augustus.ui.menu.ClientTabContainerScreen;
import net.augustus.ui.menu.AbstractMainMenuScreen;
import net.augustus.ui.menu.ClientMenuTab;
import net.augustus.render.RenderUtil;
import net.augustus.ui.GuiButtonClickHandler;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;

public class GuiElectricLauncherAccount extends ClientMenuTab {
   private int visibleFriendCount;
   private String selectedFriend;
   public static final ScrollBar scrollBar;
   private static String filterButtonLabel = "Show: all";
   private static Color statusColor2 = Color.green;
   private GuiTextField friendInputField;
   private static int filterMode = 1;
   private final TimeHelper timeHelper = new TimeHelper();
   private static String statusMessage = "";

   private static void cycleFilterMode(long l, GuiButton button) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      filterMode++;
      switch (filterMode) {
         case 1:
            filterButtonLabel = "Show: all";
            break;
         case 2:
            filterButtonLabel = "Show: online";
            break;
         case 3:
            filterButtonLabel = "Show: requests";
      }

      button.displayString = filterButtonLabel;
      if (filterMode >= 3) {
         filterMode = 0;
      }
   }

   @Override
   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (mouseButton == 0) {
         ScaledResolution sr = new ScaledResolution(this.mc);
         int n = (int)Math.max(100.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
         int n2 = Math.min(sr.getScaledWidth() - 102 - n - 20, 300);
         int n3 = sr.getScaledWidth() - 10 - n2;
         int clampedScrollOffset = (int)scrollBar.getClampedScrollOffset();
         this.visibleFriendCount = 0;

         for (int i = 2; i >= 0; i--) {
            byte b = 41;
            byte b2 = 57;
            clampedScrollOffset = this.handleFriendGroupClick(i, n3, b2, clampedScrollOffset, n2, b, mouseX, mouseY);
         }

         double d = (double)mouseX;
         double d2 = (double)mouseY;
         double d3 = d2;
         double d4 = d;
         scrollBar.handleMouseClick(d4, d3);
      }
   }

   private void onRemoveFriendClicked(GuiButton button) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
         Color red = Color.RED;
         String str = "You are not online. Try again later.";
         this.setStatus(str, red);
      } else if (this.selectedFriend == null) {
         Color red2 = Color.RED;
         String str2 = "No friend selected to remove";
         this.setStatus(str2, red2);
      } else {
         long l = 2000L;
         if (!this.timeHelper.reached2(l)) {
            Color red3 = Color.RED;
            String str3 = "Click slower! (1 seconds delay)";
            this.setStatus(str3, red3);
         } else {
            this.timeHelper.reset();
            VendorPacketDispatcher.sendFriendRemove(this.selectedFriend);
         }
      }
   }

   private void onJoinServerClicked(GuiButton button) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
         Color red = Color.RED;
         String str = "You are not online. Try again later.";
         this.setStatus(str, red);
      } else if (this.selectedFriend == null) {
         Color red2 = Color.RED;
         String str2 = "No friend selected to join";
         this.setStatus(str2, red2);
      } else {
         long l = 1000L;
         if (!this.timeHelper.reached2(l)) {
            Color red3 = Color.RED;
            String str3 = "Click slower! (1 seconds delay)";
            this.setStatus(str3, red3);
         } else {
            this.timeHelper.reset();

            for (OnlineUserEntry onlineUserEntry : (Iterable<OnlineUserEntry>)(OnlineUserRegistry.usersBySessionId.values())) {
               if (onlineUserEntry.getClientUsername().equals(this.selectedFriend) && onlineUserEntry.getServerAddress() != null && !onlineUserEntry.getServerAddress().isEmpty()) {
                  if (this.mc.thePlayer != null) {
                     boolean flag = true;
                     this.mc.isGamePaused = flag;
                     boolean flag2 = this.mc.isIntegratedServerRunning();
                     this.mc.theWorld.sendQuittingDisconnectingPacket();
                     Object obj = null;
                     this.mc.loadWorld((WorldClient)obj);
                     if (flag2) {
                        Minecraft mc2 = this.mc;
                        GuiMainMenu guiMainMenu = new GuiMainMenu();
                        mc2.displayGuiScreen(guiMainMenu);
                     } else {
                        Minecraft mc3 = this.mc;
                        GuiMultiplayer guiMultiplayer = new GuiMultiplayer(new GuiMainMenu());
                        mc3.displayGuiScreen(guiMultiplayer);
                     }
                  }

                  String serverAddress = onlineUserEntry.getServerAddress();
                  Minecraft mc4 = this.mc;
                  GuiConnecting guiConnecting = new GuiConnecting(new ClientTabContainerScreen(AbstractMainMenuScreen.createMainMenuScreen(), 2), this.mc, new ServerData(serverAddress, serverAddress, false));
                  mc4.displayGuiScreen(guiConnecting);
                  Color green = Color.GREEN;
                  String str4 = "Joining server";
                  this.setStatus(str4, green);
                  break;
               }
            }

            Color red4 = Color.RED;
            String str5 = "No server to join";
            this.setStatus(str5, red4);
         }
      }
   }

   static {
      scrollBar = new ScrollBar(43);
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      double d = (double)mouseY;
      scrollBar.handleMouseDrag(d);
   }

   private void onSendRequestClicked(GuiButton button) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      String text = this.friendInputField.getText();
      byte b = 3;
      if (!TextFormatUtil.isValidIdentifier(text, b)) {
         Color red = Color.RED;
         String str = "Invalid ElectricLauncher username";
         this.setStatus(str, red);
      } else {
         long l = 1000L;
         if (!this.timeHelper.reached2(l)) {
            Color red2 = Color.RED;
            String str2 = "Click slower! (1 seconds delay)";
            this.setStatus(str2, red2);
         } else {
            this.timeHelper.reset();
            VendorPacketDispatcher.sendFriendRequest(this.friendInputField.getText());
         }
      }
   }

   @Override
   public void drawContent(int n, int n2, float f) {
      super.drawContent(n, n2, f);
      CreditsSection.getControlFlowGuard();
      ScaledResolution sr = new ScaledResolution(this.mc);
      if (this.friendInputField.getText().isEmpty() && !this.friendInputField.isFocused()) {
         int rgb = Color.gray.getRGB();
         float f2 = 60.0F;
         float f3 = 109.0F;
         String str = "Friend user name";
         this.drawString(str, f3, f2, rgb);
      }

      int n3 = (int)Math.max(100.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
      int n4 = Math.min(sr.getScaledWidth() - 102 - n3 - 20, 300);
      int n5 = sr.getScaledWidth() - 10 - n4;
      int clampedScrollOffset = (int)scrollBar.getClampedScrollOffset();
      this.visibleFriendCount = 0;

      for (int i = 2; i >= 0; i--) {
         byte b = 41;
         byte b2 = 57;
         clampedScrollOffset = this.renderFriendGroup(i, n5, b2, clampedScrollOffset, n4, b);
      }

      int visibleFriendCount2 = this.visibleFriendCount;
      scrollBar.setItemCount(visibleFriendCount2);
      scrollBar.updateAndDraw();
   }

   @Override
   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      scrollBar.handleMouseWheel();
   }

   private int handleFriendGroupClick(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      if ((filterMode != 2 || n == 2) && (filterMode != 0 || n == 0)) {
         for (ElectricFriend electricFriend : (Iterable<ElectricFriend>)(ElectricFriendManager.getFriends())) {
            int status = electricFriend.getStatus();
            if (status == n) {
               int n9 = n3 + n4;
               int n10 = n2 + n5;
               int n11 = n3 + n6 + n4;
               int n12 = n10;
               int n13 = n9;
               this.selectFriendIfHovered(n2, n13, n12, n11, n7, n8, electricFriend);
               n4 += n6 + 2;
            }
         }

         return n4;
      } else {
         return n4;
      }
   }

   private void onAcceptFriendClicked(GuiButton button) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
         Color red = Color.RED;
         String str = "You are not online. Try again later.";
         this.setStatus(str, red);
      } else if (this.selectedFriend == null) {
         Color red2 = Color.RED;
         String str2 = "No friend selected to accept";
         this.setStatus(str2, red2);
      } else {
         long l = 1000L;
         if (!this.timeHelper.reached2(l)) {
            Color red3 = Color.RED;
            String str3 = "Click slower! (1 seconds delay)";
            this.setStatus(str3, red3);
         } else {
            this.timeHelper.reset();
            VendorPacketDispatcher.sendFriendAccept(this.selectedFriend);
         }
      }
   }

   private void selectFriendIfHovered(int n, int n2, int n3, int n4, int n5, int n6, ElectricFriend electricFriend) {
      if (RenderUtil.isPointInBounds(n5, n6, (double)n, (double)n2, (double)n3, (double)n4)) {
         this.selectedFriend = electricFriend.getUsername();
      }
   }

   @Override
   public void initContent() {
      super.initContent();
      ScaledResolution sr = new ScaledResolution(this.mc);
      byte b = 104;
      byte b2 = 54;
      CreditsSection.getControlFlowGuard();
      int n = (int)Math.max(100.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
      byte b3 = 20;
      int n2 = b3 + 5;
      this.friendInputField = this.addTextField(b, b2, n, b3);
      int n3 = b2 + n2;
      GuiButtonClickHandler guiButtonClickHandler = button -> {
         int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
         String text = this.friendInputField.getText();
         byte b4 = 3;
         if (!TextFormatUtil.isValidIdentifier(text, b4)) {
            Color red = Color.RED;
            String str = "Invalid ElectricLauncher username";
            this.setStatus(str, red);
         } else {
            long l = 1000L;
            if (!this.timeHelper.reached2(l)) {
               Color red2 = Color.RED;
               String str2 = "Click slower! (1 seconds delay)";
               this.setStatus(str2, red2);
            } else {
               this.timeHelper.reset();
               VendorPacketDispatcher.sendFriendRequest(this.friendInputField.getText());
            }
         }
      };
      String str3 = "Send friend request";
      int n4 = n3;
      this.addButton(b, n4, n, b3, str3, guiButtonClickHandler);
      n3 = b2 + n2 * 2;
      GuiButtonClickHandler guiButtonClickHandler2 = button2 -> {
         int[] controlFlowGuard2 = CreditsSection.getControlFlowGuard();
         if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
            Color red3 = Color.RED;
            String str4 = "You are not online. Try again later.";
            this.setStatus(str4, red3);
         } else if (this.selectedFriend == null) {
            Color red4 = Color.RED;
            String str5 = "No friend selected to remove";
            this.setStatus(str5, red4);
         } else {
            long l2 = 2000L;
            if (!this.timeHelper.reached2(l2)) {
               Color red5 = Color.RED;
               String str6 = "Click slower! (1 seconds delay)";
               this.setStatus(str6, red5);
            } else {
               this.timeHelper.reset();
               VendorPacketDispatcher.sendFriendRemove(this.selectedFriend);
            }
         }
      };
      String str7 = "Remove friend";
      int n5 = n3;
      this.addButton(b, n5, n, b3, str7, guiButtonClickHandler2);
      n3 = b2 + n2 * 3;
      GuiButtonClickHandler guiButtonClickHandler3 = button3 -> {
         int[] controlFlowGuard3 = CreditsSection.getControlFlowGuard();
         if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
            Color red6 = Color.RED;
            String str8 = "You are not online. Try again later.";
            this.setStatus(str8, red6);
         } else if (this.selectedFriend == null) {
            Color red7 = Color.RED;
            String str9 = "No friend selected to accept";
            this.setStatus(str9, red7);
         } else {
            long l3 = 1000L;
            if (!this.timeHelper.reached2(l3)) {
               Color red8 = Color.RED;
               String str10 = "Click slower! (1 seconds delay)";
               this.setStatus(str10, red8);
            } else {
               this.timeHelper.reset();
               VendorPacketDispatcher.sendFriendAccept(this.selectedFriend);
            }
         }
      };
      String str11 = "Accept friend";
      int n6 = n3;
      this.addButton(b, n6, n, b3, str11, guiButtonClickHandler3);
      n3 = b2 + n2 * 4;
      GuiButtonClickHandler guiButtonClickHandler4 = button4 -> this.onJoinServerClicked(button4);
      String str12 = "Join Server";
      int n7 = n3;
      this.addButton(b, n7, n, b3, str12, guiButtonClickHandler4);
      n3 = b2 + n2 * 5;
      String str13 = filterButtonLabel;
      GuiButtonClickHandler guiButtonClickHandler5 = button5 -> {
         int[] controlFlowGuard4 = CreditsSection.getControlFlowGuard();
         filterMode++;
         switch (filterMode) {
            case 1:
               filterButtonLabel = "Show: all";
               break;
            case 2:
               filterButtonLabel = "Show: online";
               break;
            case 3:
               filterButtonLabel = "Show: requests";
         }

         button5.displayString = filterButtonLabel;
         if (filterMode >= 3) {
            filterMode = 0;
         }
      };
      String str14 = str13;
      int n8 = n3;
      this.addButton(b, n8, n, b3, str14, guiButtonClickHandler5);
      Color green = Color.GREEN;
      String str15 = "";
      this.setStatus(str15, green);
      n3 = this.width - 5;
      int height2 = this.height;
      int width2 = this.width;
      byte b5 = 52;
      int n9 = n3;
      scrollBar.setBounds(n9, b5, width2, height2);
   }

   public GuiElectricLauncherAccount(long l) {
      super("ElectricLauncher");
      this.visibleFriendCount = 0;
   }

   private int renderFriendGroup(int n, int n2, int n3, int n4, int n5, int n6) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      if ((filterMode != 2 || n == 2) && (filterMode != 0 || n == 0)) {
         for (ElectricFriend electricFriend : (Iterable<ElectricFriend>)(ElectricFriendManager.getFriends())) {
            int status = electricFriend.getStatus();
            if (status == n) {
               this.visibleFriendCount++;
               int n7 = n3 + n4;
               int n8 = n2 + n5;
               int n9 = n3 + n6 + n4;
               int userId = electricFriend.getUserId();
               String username = electricFriend.getUsername();
               int n10 = n9;
               int n11 = n8;
               int n12 = n7;
               this.drawFriendCard(n2, n12, n11, n10, status, username, userId);
               n4 += n6 + 2;
            }
         }

         return n4;
      } else {
         return n4;
      }
   }

   private void drawFriendCard(int n, int n2, int n3, int n4, int n5, String str, int n6) {
      int[] controlFlowGuard = CreditsSection.getControlFlowGuard();
      FontRenderer fontRendererObj2 = this.mc.fontRendererObj;
      int rgb = new Color(0, 0, 0, str.equals(this.selectedFriend) ? 200 : 100).getRGB();
      RenderUtil.drawRect3(n, n2, n3, n4, rgb);
      String str2 = n5 == 1 ? "§c" : (n5 == 2 ? "§a" : "§f");
      String str3 = "§7Name: "
         + str2
         + str
         + " §7[§e"
         + n6
         + "§7]"
         + str2
         + (n5 == 1 ? " (Offline)" : (n5 == 2 ? " (Online)" : " (Pending request)"));
      float f = (float)n + 5.5F;
      float f2 = (float)n2 + 3.5F;
      int rgb2 = Color.white.getRGB();
      float f3 = f2;
      float f4 = f;
      String str4 = str3;
      this.drawString(str4, f4, f3, rgb2);
      String str5 = "-----";
      String str6 = "-----";
      if (n5 == 2) {
         StringBuilder sb = new StringBuilder();
         StringBuilder sb2 = new StringBuilder();
         boolean flag = true;

         for (OnlineUserEntry onlineUserEntry : (Iterable<OnlineUserEntry>)(OnlineUserRegistry.usersBySessionId.values())) {
            if (onlineUserEntry.getClientUsername().equals(str)) {
               if (!flag) {
                  sb2.append(", ");
                  sb.append(", ");
               }

               sb.append(onlineUserEntry.getServerAddress());
               sb2.append(onlineUserEntry.getIngameName());
               flag = false;
            }
         }

         if (!sb.toString().isEmpty()) {
            str5 = sb.toString();
         }

         if (!sb2.toString().isEmpty()) {
            str6 = sb2.toString();
         }
      }

      str3 = "§7Server: §f" + str5;
      f = (float)n + 5.5F;
      f2 = (float)n2 + 7.0F + (float)fontRendererObj2.FONT_HEIGHT;
      int rgb3 = Color.white.getRGB();
      float f5 = f2;
      float f6 = f;
      String str7 = str3;
      this.drawString(str7, f6, f5, rgb3);
      str3 = "§7Ingame names: §f" + str6;
      f = (float)n + 5.5F;
      f2 = (float)n2 + 10.5F + (float)(fontRendererObj2.FONT_HEIGHT * 2);
      int rgb4 = Color.white.getRGB();
      float f7 = f2;
      float f8 = f;
      String str8 = str3;
      this.drawString(str8, f8, f7, rgb4);
   }

}
