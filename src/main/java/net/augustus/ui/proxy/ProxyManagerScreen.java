package net.augustus.ui.proxy;

import net.augustus.Augustus;
import net.augustus.proxy.ProxyProtocolType;
import net.augustus.render.RenderUtil;
import net.augustus.ui.GuiButtonClickHandler;
import net.augustus.ui.GuiPasswordField;
import net.augustus.ui.account.AccountAuthenticator;
import net.augustus.ui.menu.ClientMenuTab;
import net.augustus.ui.widgets.ScrollBar;
import net.augustus.utils.TuplePair;
import net.augustus.utils.network.ProxyEntry;
import net.augustus.utils.render.ColorUtil;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;

public class ProxyManagerScreen extends ClientMenuTab {
   private boolean reinitPending;
   private CompletableFuture activePingTask;
   private static ProxyEntry pendingProxy = null;
   public static final ScrollBar scrollBar;
   public static final CopyOnWriteArrayList proxies = new CopyOnWriteArrayList();
   private boolean mcServerCheckEnabled;
   private GuiTextField usernameField;
   private GuiPasswordField passwordField;
   private ProxyEntry selectedProxy;
   private ProxyEntry previousProxy;
   public static final ConcurrentHashMap proxyListEntries = new ConcurrentHashMap();
   private GuiTextField addressField;
   private ProxyProtocolType selectedProtocolType = ProxyProtocolType.SOCKS5;

   public static synchronized void setPendingProxy(ProxyEntry proxyEntry) {
      pendingProxy = proxyEntry;
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      int[] controlFlowGuard = ProxyListEntry.getControlFlowGuard();
      super.keyTyped(typedChar, keyCode);
      if (this.selectedProxy != null && (keyCode == 211 || keyCode == 14 && !this.addressField.isFocused() && !this.usernameField.isFocused() && !this.passwordField.isFocused())) {
         proxies.remove(this.selectedProxy);
         proxyListEntries.remove(this.selectedProxy);
         this.selectedProxy = null;
      }
   }

   public void connectToProxy(String str, boolean flag) {
      int[] controlFlowGuard = ProxyListEntry.getControlFlowGuard();
      if (str.isEmpty()) {
         Color color = Color.red.darker();
         String str2 = "IP is empty!";
         this.setStatus(str2, color);
      } else {
         String[] strings = str.split(":");
         if (strings.length >= 2 && strings.length <= 4) {
            String[] strings2 = new String[4];
            System.arraycopy(strings, 0, strings2, 0, strings.length);
            if (!flag) {
               if (!this.usernameField.getText().isEmpty()) {
                  strings2[2] = this.usernameField.getText();
               }

               if (!this.passwordField.getText().isEmpty()) {
                  strings2[3] = this.passwordField.getText();
               }
            }

            int n = parsePortOrDefault(strings2[1]);
            if (n == -1) {
               Color color2 = Color.red.darker();
               String str3 = "Port is not numeric!";
               this.setStatus(str3, color2);
            } else {
               String str4 = strings2[0];
               String str5 = strings2[2] == null ? "" : strings2[2];
               String str6 = strings2[3] == null ? "" : strings2[3];
               ProxyProtocolType selectedProtocolType2 = this.selectedProtocolType;
               String str7 = str6;
               String str8 = str5;
               String str9 = str4;
               pendingProxy = new ProxyEntry(str9, n, str8, str7, selectedProtocolType2);
               if (pendingProxy.getSocketAddress() == null) {
                  Color color3 = Color.red.darker();
                  String str10 = "No valid proxy format!";
                  this.setStatus(str10, color3);
                  pendingProxy = null;
               } else {
                  if (this.activePingTask == null) {
                     Color yellow = Color.yellow;
                     String str11 = "Pinging...";
                     this.setStatus(str11, yellow);
                     this.previousProxy = Augustus.getInstance().getProxy();
                     this.activePingTask = CompletableFuture.supplyAsync(() -> {
                        boolean mcServerCheckEnabled2 = this.mcServerCheckEnabled;
                        ProxyEntry previousProxy2 = this.previousProxy;
                        return ProxyChecker.testAndApplyProxy(pendingProxy, previousProxy2, mcServerCheckEnabled2);
                     });
                  }
               }
            }
         } else {
            Color color4 = Color.red.darker();
            String str12 = "Wrong proxy format! [Ip:Port] / [Ip:Port:Username] / [Ip:Port:Username:Password]";
            this.setStatus(str12, color4);
         }
      }
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      double d = (double)mouseY;
      scrollBar.handleMouseDrag(d);
   }

   public static synchronized ProxyEntry getPendingProxy() {
      return pendingProxy;
   }

   @Override
   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      scrollBar.handleMouseWheel();
   }

   private void onDisconnectClick(GuiButton button) {
      this.disconnectProxy();
   }

   public ProxyManagerScreen() {
      super("Proxy Manager");
      this.mcServerCheckEnabled = false;
      this.selectedProxy = null;
      this.previousProxy = null;
      this.activePingTask = null;
      this.reinitPending = false;
      Color green = Color.green;
      String str = "Waiting...";
      this.setStatus(str, green);
   }

   private void importProxiesAsync() {
      new Thread(() -> this.showImportProxiesDialog()).start();
   }

   private void showImportProxiesDialog() {
      JFileChooser jFileChooser = new JFileChooser();
      ProxyListEntry.getControlFlowGuard();
      jFileChooser.setDialogTitle("Select Proxy File");
      jFileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
      int n = jFileChooser.showOpenDialog(null);
      if (n == 0) {
         File selectedFile = jFileChooser.getSelectedFile();

         try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String str;
            while ((str = reader.readLine()) != null) {
               try {
                  if (!str.trim().isEmpty()) {
                     ProxyProtocolType selectedProtocolType2 = this.selectedProtocolType;
                     String str2 = str.trim();
                     if (str2.toLowerCase().startsWith("socks4://")) {
                        selectedProtocolType2 = ProxyProtocolType.SOCKS4;
                        str2 = str2.substring(9);
                     } else if (str2.toLowerCase().startsWith("socks5://")) {
                        selectedProtocolType2 = ProxyProtocolType.SOCKS5;
                        str2 = str2.substring(9);
                     }

                     String[] strings = str2.split(":");
                     if (strings.length >= 2) {
                        String str3 = strings[0];
                        int n2 = parsePortOrDefault(strings[1]);
                        if (n2 != -1) {
                           String str4 = strings.length > 2 ? strings[2] : "";
                           String str5 = strings.length > 3 ? strings[3] : "";
                           ProxyEntry proxyEntry = new ProxyEntry(str3, n2, str4, str5, selectedProtocolType2);
                           if (!proxies.contains(proxyEntry)) {
                              proxies.add(proxyEntry);
                              proxyListEntries.put(proxyEntry, new ProxyListEntry(proxyEntry));
                           }
                        }
                     }
                  }
               } catch (Exception e) {
                  System.err.println("Failed to parse proxy line: " + str);
               }
            }

            ScrollBar scrollBar2 = scrollBar;
            int size = proxies.size();
            scrollBar2.setItemCount(size);
         } catch (Exception e2) {
            e2.printStackTrace();
         }
      }
   }

   private void onImportProxiesClick(GuiButton button) {
      this.importProxiesAsync();
   }

   static {
      scrollBar = new ScrollBar(32);
   }

   @Override
   public void drawContent(int n, int n2, float f) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      ProxyListEntry.getControlFlowGuard();
      int n3 = (int)Math.max(100.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
      if (this.addressField.getText().isEmpty() && !this.addressField.isFocused()) {
         int rgb = Color.gray.getRGB();
         float f2 = 60.0F;
         float f3 = 109.0F;
         String str = "IP-Adress:Port";
         this.drawString(str, f3, f2, rgb);
      }

      if (this.usernameField.getText().isEmpty() && !this.usernameField.isFocused()) {
         int rgb2 = Color.gray.getRGB();
         float f4 = 110.0F;
         float f5 = 109.0F;
         String str2 = "Username";
         this.drawString(str2, f5, f4, rgb2);
      }

      if (this.passwordField.getText().isEmpty() && !this.passwordField.isFocused() && this.selectedProtocolType != ProxyProtocolType.SOCKS4) {
         int rgb3 = Color.gray.getRGB();
         float f6 = 135.0F;
         float f7 = 109.0F;
         String str3 = "Password";
         this.drawString(str3, f7, f6, rgb3);
      }

      int n4 = sr.getScaledWidth() - 102;
      int n5 = Math.min(n4 - n3 - 20, 300);
      int n6 = sr.getScaledWidth() - 10 - n5;
      int clampedScrollOffset = (int)scrollBar.getClampedScrollOffset();

      for (ProxyEntry proxyEntry : (Iterable<ProxyEntry>)(proxies)) {
         ProxyListEntry proxyListEntry = (ProxyListEntry)proxyListEntries.get(proxyEntry);
         if (proxyListEntry != null && proxyListEntry.isFavorite()) {
            int n7 = 57 + clampedScrollOffset;
            boolean flag = proxyEntry == this.selectedProxy;
            byte b = 30;
            int n8 = n7;
            proxyListEntry.drawEntry(n, n2, n6, n8, n5, b, flag);
            clampedScrollOffset += 32;
         }
      }

      for (ProxyEntry proxyEntry2 : (Iterable<ProxyEntry>)(proxies)) {
         ProxyListEntry proxyListEntry2 = (ProxyListEntry)proxyListEntries.get(proxyEntry2);
         if (proxyListEntry2 != null && !proxyListEntry2.isFavorite()) {
            int n9 = 57 + clampedScrollOffset;
            boolean flag2 = proxyEntry2 == this.selectedProxy;
            byte b2 = 30;
            int n10 = n9;
            proxyListEntry2.drawEntry(n, n2, n6, n10, n5, b2, flag2);
            clampedScrollOffset += 32;
         }
      }

      scrollBar.updateAndDraw();
      if (this.activePingTask != null && this.activePingTask.isDone()) {
         try {
            TuplePair tuplePair = (TuplePair)this.activePingTask.get();
            String first = (String)tuplePair.getFirst();
            Color green = Color.green;
            Color color = Color.red.darker();
            double d = Math.min(1.0, (double)((Integer)tuplePair.getSecond()).intValue() / 1000.0);
            Color color2 = color;
            Color color3 = new Color(ColorUtil.blendColors(green, color2, d));
            String str4 = first;
            this.setStatus(str4, color3);
            this.activePingTask = null;
            ProxyEntry proxy = Augustus.getInstance().getProxy();
            if (proxy != null && getPendingProxy() != null) {
               ProxyEntry pendingProxy2 = getPendingProxy();
               if (proxy.matches(pendingProxy2)) {
                  this.addressField.setText(proxy.getHost() + ":" + proxy.getPort());
                  this.usernameField.setText(proxy.getUsername());
                  if (this.passwordField != null) {
                     GuiPasswordField passwordField2 = this.passwordField;
                     String password = proxy.getPassword();
                     passwordField2.setText(password);
                  }
               }
            }
         } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
         }
      }
   }

   private void disconnectProxy() {
      Augustus instance = Augustus.getInstance();
      Object obj = null;
      instance.setProxy((ProxyEntry)obj);
      Color green = Color.green;
      String str = "Waiting...";
      this.setStatus(str, green);
   }

   private void onClearFieldsClick(GuiButton button) {
      this.clearInputFields();
   }

   private void connectFromClipboard() {
      String str = GuiScreen.getClipboardString().replace(" ", "");
      boolean flag = true;
      this.connectToProxy(str, flag);
   }

   private void onDeleteSelectedProxyClick(GuiButton button) {
      int[] controlFlowGuard = ProxyListEntry.getControlFlowGuard();
      if (this.selectedProxy != null) {
         proxies.remove(this.selectedProxy);
         proxyListEntries.remove(this.selectedProxy);
         ScrollBar scrollBar2 = scrollBar;
         int size = proxies.size();
         scrollBar2.setItemCount(size);
         this.selectedProxy = null;
         AccountAuthenticator.playSuccessSound();
      } else {
         AccountAuthenticator.playFailureSound();
      }
   }

   @Override
   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      int[] controlFlowGuard = ProxyListEntry.getControlFlowGuard();
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (this.reinitPending) {
         this.initGui();
      }

      for (ProxyEntry proxyEntry : (Iterable<ProxyEntry>)(proxies)) {
         ProxyListEntry proxyListEntry = (ProxyListEntry)proxyListEntries.get(proxyEntry);
         proxyListEntry.mouseClicked(mouseX, mouseY, mouseButton, this);
         if ((mouseButton == 1 || mouseButton == 0) && RenderUtil.isHovered((float)proxyListEntry.getRenderX(), (float)proxyListEntry.getRenderY(), (float)proxyListEntry.getRenderWidth(), (float)proxyListEntry.getRenderHeight(), mouseX, mouseY)
            )
          {
            this.selectedProxy = proxyEntry;
         }
      }

      double d = (double)mouseX;
      double d2 = (double)mouseY;
      double d3 = d2;
      double d4 = d;
      scrollBar.handleMouseClick(d4, d3);
   }

   public static Integer parsePortOrDefault(String str) {
      try {
         return Integer.parseInt(str);
      } catch (Exception e) {
         return -1;
      }
   }

   private void onToggleProtocolTypeClick(GuiButton button) {
      int[] controlFlowGuard = ProxyListEntry.getControlFlowGuard();
      switch (ProxyTypeSwitchMap.ORDINAL_MAP[this.selectedProtocolType.ordinal()]) {
         case 1:
            this.selectedProtocolType = ProxyProtocolType.SOCKS5;
            break;
         case 2:
            this.selectedProtocolType = ProxyProtocolType.SOCKS4;
      }

      this.reinitPending = true;
      button.displayString = "Type: " + this.selectedProtocolType.getDisplayName();
   }

   private void populateFieldsFromPendingProxy() {
      int[] controlFlowGuard = ProxyListEntry.getControlFlowGuard();
      if (pendingProxy != null) {
         this.addressField.setText(pendingProxy.getHost() + ":" + pendingProxy.getPort());
         this.usernameField.setText(pendingProxy.getUsername());
         if (this.passwordField != null) {
            GuiPasswordField passwordField2 = this.passwordField;
            String password = pendingProxy.getPassword();
            passwordField2.setText(password);
         }
      }
   }

   @Override
   public void initContent() {
      this.reinitPending = false;
      super.initContent();
      ProxyListEntry.getControlFlowGuard();
      ScaledResolution sr = new ScaledResolution(this.mc);
      byte b = 104;
      byte b2 = 54;
      byte b3 = 25;
      int n = (int)Math.max(100.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
      int n2 = 1;
      byte b4 = 20;
      this.addressField = this.addTextField(b, b2, n, b4);
      int n3 = b2 + b3 * n2;
      String str = "Type: " + this.selectedProtocolType.getDisplayName();
      GuiButtonClickHandler guiButtonClickHandler = button -> {
         this.onToggleProtocolTypeClick(button);
      };
      String str2 = str;
      byte b5 = 20;
      int n4 = n3;
      this.addButton(b, n4, n, b5, str2, guiButtonClickHandler);
      int n5 = b2 + b3 * ++n2;
      byte b6 = 20;
      int n6 = n5;
      this.usernameField = this.addTextField(b, n6, n, b6);
      n2++;
      if (this.selectedProtocolType == ProxyProtocolType.SOCKS5) {
         n5 = b2 + b3 * n2;
         byte b7 = 20;
         int n7 = n5;
         this.passwordField = this.addPasswordField(b, n7, n, b7);
         boolean flag = true;
         this.passwordField.setVisible(flag);
         n2++;
      } else {
         boolean flag2 = false;
         this.passwordField.setVisible(flag2);
      }

      this.populateFieldsFromPendingProxy();
      n3 = b2 + b3 * n2;
      GuiButtonClickHandler guiButtonClickHandler2 = button2 -> {
         String text = this.addressField.getText();
         boolean flag3 = false;
         String str3 = text;
         this.connectToProxy(str3, flag3);
      };
      String str4 = "Connect";
      byte b8 = 20;
      int n8 = n3;
      this.addButton(b, n8, n, b8, str4, guiButtonClickHandler2);
      n3 = b2 + b3 * ++n2;
      GuiButtonClickHandler guiButtonClickHandler3 = button3 -> this.onClearFieldsClick(button3);
      String str5 = "Clear";
      byte b9 = 20;
      int n9 = n3;
      this.addButton(b, n9, n, b9, str5, guiButtonClickHandler3);
      n3 = b2 + b3 * ++n2;
      GuiButtonClickHandler guiButtonClickHandler4 = button4 -> this.onDisconnectClick(button4);
      String str6 = "Disconnect";
      byte b10 = 20;
      int n10 = n3;
      this.addButton(b, n10, n, b10, str6, guiButtonClickHandler4);
      n3 = b2 + b3 * ++n2;
      GuiButtonClickHandler guiButtonClickHandler5 = button5 -> {
         this.connectFromClipboard();
      };
      String str7 = "Clipboard Login";
      byte b11 = 20;
      int n11 = n3;
      this.addButton(b, n11, n, b11, str7, guiButtonClickHandler5);
      n3 = b2 + b3 * ++n2;
      GuiButtonClickHandler guiButtonClickHandler6 = button6 -> this.onImportProxiesClick(button6);
      String str8 = "Import Proxies";
      byte b12 = 20;
      int n12 = n3;
      this.addButton(b, n12, n, b12, str8, guiButtonClickHandler6);
      n3 = b2 + b3 * ++n2;
      StringBuilder sb = new StringBuilder();
      StringBuilder sb2 = sb.append("MC Check: ");
      String str9;
      if (this.mcServerCheckEnabled) {
         str9 = "True";
      } else {
         str9 = "False";
      }

      str = sb2.append(str9).toString();
      GuiButtonClickHandler guiButtonClickHandler7 = button7 -> {
         int[] controlFlowGuard = ProxyListEntry.getControlFlowGuard();
         this.mcServerCheckEnabled = !this.mcServerCheckEnabled;
         StringBuilder sb3 = new StringBuilder();
         sb3 = sb3.append("MC Check: ");
         String str10;
         if (this.mcServerCheckEnabled) {
            str10 = "True";
         } else {
            str10 = "False";
         }

         button7.displayString = sb3.append(str10).toString();
      };
      String str11 = str;
      byte b13 = 20;
      int n13 = n3;
      this.addButton(b, n13, n, b13, str11, guiButtonClickHandler7);
      n3 = b2 + b3 * ++n2;
      GuiButtonClickHandler guiButtonClickHandler8 = button8 -> this.onDeleteSelectedProxyClick(button8);
      String str12 = "Delete selected Proxy";
      byte b14 = 20;
      int n14 = n3;
      this.addButton(b, n14, n, b14, str12, guiButtonClickHandler8);
      n3 = this.width - 5;
      int height2 = this.height;
      int width2 = this.width;
      byte b15 = 52;
      int n15 = n3;
      scrollBar.setBounds(n15, b15, width2, height2);
      ScrollBar scrollBar2 = scrollBar;
      int size = proxies.size();
      scrollBar2.setItemCount(size);
   }

   private void clearInputFields() {
      this.addressField.setText("");
      String str = "";
      this.passwordField.setText(str);
      this.usernameField.setText("");
      pendingProxy = null;
   }

}
