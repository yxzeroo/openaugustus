package net.augustus.vendor;

import java.awt.Color;
import java.io.IOException;
import net.augustus.utils.TextFormatUtil;
import net.augustus.ui.widgets.ScrollBar;
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
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (mouseButton == 0) {
         ScaledResolution sr = new ScaledResolution(this.mc);
         int n = (int)Math.max(100.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
         int n2 = Math.min(sr.getScaledWidth() - 102 - n - 20, 300);
         int n3 = sr.getScaledWidth() - 10 - n2;
         int clampedScrollOffset = (int)scrollBar.getClampedScrollOffset();
         this.visibleFriendCount = 0;

         for (int i = 2; i >= 0; i--) {
            clampedScrollOffset = this.handleFriendGroupClick(i, n3, (byte)57, clampedScrollOffset, n2, (byte)41, mouseX, mouseY);
         }

         double d = (double)mouseX;
         double d3 = (double)mouseY;
         scrollBar.handleMouseClick(d, d3);
      }
   }

   private void onRemoveFriendClicked(GuiButton button) {
      if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
         Color red = Color.RED;
         this.setStatus("You are not online. Try again later.", red);
      } else if (this.selectedFriend == null) {
         Color red2 = Color.RED;
         this.setStatus("No friend selected to remove", red2);
      } else {
         if (!this.timeHelper.reached2(2000L)) {
            Color red3 = Color.RED;
            this.setStatus("Click slower! (1 seconds delay)", red3);
         } else {
            this.timeHelper.reset();
            VendorPacketDispatcher.sendFriendRemove(this.selectedFriend);
         }
      }
   }

   private void onJoinServerClicked(GuiButton button) {
      if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
         Color red = Color.RED;
         this.setStatus("You are not online. Try again later.", red);
      } else if (this.selectedFriend == null) {
         Color red2 = Color.RED;
         this.setStatus("No friend selected to join", red2);
      } else {
         if (!this.timeHelper.reached2(1000L)) {
            Color red3 = Color.RED;
            this.setStatus("Click slower! (1 seconds delay)", red3);
         } else {
            this.timeHelper.reset();

            for (OnlineUserEntry onlineUserEntry : (Iterable<OnlineUserEntry>)(OnlineUserRegistry.usersBySessionId.values())) {
               if (onlineUserEntry.getClientUsername().equals(this.selectedFriend) && onlineUserEntry.getServerAddress() != null && !onlineUserEntry.getServerAddress().isEmpty()) {
                  if (this.mc.thePlayer != null) {
                     this.mc.isGamePaused = true;
                     boolean flag2 = this.mc.isIntegratedServerRunning();
                     this.mc.theWorld.sendQuittingDisconnectingPacket();
                     this.mc.loadWorld((WorldClient)null);
                     if (flag2) {
                        this.mc.displayGuiScreen(new GuiMainMenu());
                     } else {
                        this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                     }
                  }

                  String serverAddress = onlineUserEntry.getServerAddress();
                  this.mc.displayGuiScreen(new GuiConnecting(new ClientTabContainerScreen(AbstractMainMenuScreen.createMainMenuScreen(), 2), this.mc, new ServerData(serverAddress, serverAddress, false)));
                  Color green = Color.GREEN;
                  this.setStatus("Joining server", green);
                  break;
               }
            }

            Color red4 = Color.RED;
            this.setStatus("No server to join", red4);
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
      if (!TextFormatUtil.isValidIdentifier(this.friendInputField.getText(), (byte)3)) {
         Color red = Color.RED;
         this.setStatus("Invalid ElectricLauncher username", red);
      } else {
         if (!this.timeHelper.reached2(1000L)) {
            Color red2 = Color.RED;
            this.setStatus("Click slower! (1 seconds delay)", red2);
         } else {
            this.timeHelper.reset();
            VendorPacketDispatcher.sendFriendRequest(this.friendInputField.getText());
         }
      }
   }

   @Override
   public void drawContent(int n, int n2, float f) {
      super.drawContent(n, n2, f);
      ScaledResolution sr = new ScaledResolution(this.mc);
      if (this.friendInputField.getText().isEmpty() && !this.friendInputField.isFocused()) {
         int rgb = Color.gray.getRGB();
         float f2 = 60.0F;
         float f3 = 109.0F;
         this.drawString("Friend user name", f3, f2, rgb);
      }

      int n3 = (int)Math.max(100.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
      int n4 = Math.min(sr.getScaledWidth() - 102 - n3 - 20, 300);
      int n5 = sr.getScaledWidth() - 10 - n4;
      int clampedScrollOffset = (int)scrollBar.getClampedScrollOffset();
      this.visibleFriendCount = 0;

      for (int i = 2; i >= 0; i--) {
         clampedScrollOffset = this.renderFriendGroup(i, n5, (byte)57, clampedScrollOffset, n4, (byte)41);
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
      if ((filterMode != 2 || n == 2) && (filterMode != 0 || n == 0)) {
         for (ElectricFriend electricFriend : (Iterable<ElectricFriend>)(ElectricFriendManager.getFriends())) {
            if (electricFriend.getStatus() == n) {
               this.selectFriendIfHovered(n2, n3 + n4, n2 + n5, n3 + n6 + n4, n7, n8, electricFriend);
               n4 += n6 + 2;
            }
         }

         return n4;
      } else {
         return n4;
      }
   }

   private void onAcceptFriendClicked(GuiButton button) {
      if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
         Color red = Color.RED;
         this.setStatus("You are not online. Try again later.", red);
      } else if (this.selectedFriend == null) {
         Color red2 = Color.RED;
         this.setStatus("No friend selected to accept", red2);
      } else {
         if (!this.timeHelper.reached2(1000L)) {
            Color red3 = Color.RED;
            this.setStatus("Click slower! (1 seconds delay)", red3);
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
      int n = (int)Math.max(100.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
      byte b3 = 20;
      int n2 = b3 + 5;
      this.friendInputField = this.addTextField(b, b2, n, b3);
      int n3 = b2 + n2;
      GuiButtonClickHandler guiButtonClickHandler = button -> {
         if (!TextFormatUtil.isValidIdentifier(this.friendInputField.getText(), (byte)3)) {
            Color red = Color.RED;
            this.setStatus("Invalid ElectricLauncher username", red);
         } else {
            if (!this.timeHelper.reached2(1000L)) {
               Color red2 = Color.RED;
               this.setStatus("Click slower! (1 seconds delay)", red2);
            } else {
               this.timeHelper.reset();
               VendorPacketDispatcher.sendFriendRequest(this.friendInputField.getText());
            }
         }
      };
      this.addButton(b, n3, n, b3, "Send friend request", guiButtonClickHandler);
      n3 = b2 + n2 * 2;
      GuiButtonClickHandler guiButtonClickHandler2 = button2 -> {
         if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
            Color red3 = Color.RED;
            this.setStatus("You are not online. Try again later.", red3);
         } else if (this.selectedFriend == null) {
            Color red4 = Color.RED;
            this.setStatus("No friend selected to remove", red4);
         } else {
            if (!this.timeHelper.reached2(2000L)) {
               Color red5 = Color.RED;
               this.setStatus("Click slower! (1 seconds delay)", red5);
            } else {
               this.timeHelper.reset();
               VendorPacketDispatcher.sendFriendRemove(this.selectedFriend);
            }
         }
      };
      this.addButton(b, n3, n, b3, "Remove friend", guiButtonClickHandler2);
      n3 = b2 + n2 * 3;
      GuiButtonClickHandler guiButtonClickHandler3 = button3 -> {
         if (!AugustusServerHandler.isConnectedAndReady() || !AugustusServerHandler.getActiveHandler().isSessionReady()) {
            Color red6 = Color.RED;
            this.setStatus("You are not online. Try again later.", red6);
         } else if (this.selectedFriend == null) {
            Color red7 = Color.RED;
            this.setStatus("No friend selected to accept", red7);
         } else {
            if (!this.timeHelper.reached2(1000L)) {
               Color red8 = Color.RED;
               this.setStatus("Click slower! (1 seconds delay)", red8);
            } else {
               this.timeHelper.reset();
               VendorPacketDispatcher.sendFriendAccept(this.selectedFriend);
            }
         }
      };
      this.addButton(b, n3, n, b3, "Accept friend", guiButtonClickHandler3);
      n3 = b2 + n2 * 4;
      GuiButtonClickHandler guiButtonClickHandler4 = button4 -> this.onJoinServerClicked(button4);
      this.addButton(b, n3, n, b3, "Join Server", guiButtonClickHandler4);
      n3 = b2 + n2 * 5;
      String str13 = filterButtonLabel;
      GuiButtonClickHandler guiButtonClickHandler5 = button5 -> {
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
      this.addButton(b, n3, n, b3, str13, guiButtonClickHandler5);
      Color green = Color.GREEN;
      this.setStatus("", green);
      n3 = this.width - 5;
      int height2 = this.height;
      int width2 = this.width;
      int n9 = n3;
      scrollBar.setBounds(n9, (byte)52, width2, height2);
   }

   public GuiElectricLauncherAccount(long l) {
      super("ElectricLauncher");
      this.visibleFriendCount = 0;
   }

   private int renderFriendGroup(int n, int n2, int n3, int n4, int n5, int n6) {
      if ((filterMode != 2 || n == 2) && (filterMode != 0 || n == 0)) {
         for (ElectricFriend electricFriend : (Iterable<ElectricFriend>)(ElectricFriendManager.getFriends())) {
            int status = electricFriend.getStatus();
            if (status == n) {
               this.visibleFriendCount++;
               int n7 = n3 + n4;
               int n8 = n2 + n5;
               int n9 = n3 + n6 + n4;
               int userId = electricFriend.getUserId();
               this.drawFriendCard(n2, n7, n8, n9, status, electricFriend.getUsername(), userId);
               n4 += n6 + 2;
            }
         }

         return n4;
      } else {
         return n4;
      }
   }

   private void drawFriendCard(int n, int n2, int n3, int n4, int n5, String str, int n6) {
      FontRenderer fontRendererObj2 = this.mc.fontRendererObj;
      RenderUtil.drawRect3(n, n2, n3, n4, new Color(0, 0, 0, str.equals(this.selectedFriend) ? 200 : 100).getRGB());
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
      this.drawString(str3, f, f2, Color.white.getRGB());
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
      this.drawString(str3, f, f2, Color.white.getRGB());
      str3 = "§7Ingame names: §f" + str6;
      f = (float)n + 5.5F;
      f2 = (float)n2 + 10.5F + (float)(fontRendererObj2.FONT_HEIGHT * 2);
      this.drawString(str3, f, f2, Color.white.getRGB());
   }

}
