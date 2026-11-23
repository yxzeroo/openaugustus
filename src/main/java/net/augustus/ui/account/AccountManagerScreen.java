package net.augustus.ui.account;

import net.augustus.Augustus;
import net.augustus.alt.AltUsernameGenerator;
import net.augustus.config.ClientSettingsConfigFile;
import net.augustus.render.RenderUtil;
import net.augustus.settings.property.BooleanProperty;
import net.augustus.settings.property.StringProperty;
import net.augustus.ui.GuiButtonClickHandler;
import net.augustus.ui.GuiPasswordField;
import net.augustus.ui.menu.ClientMenuTab;
import net.augustus.ui.widgets.ScrollBar;
import net.augustus.utils.TuplePair;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;

public class AccountManagerScreen extends ClientMenuTab {
   public static AccountManagerScreen instance;
   public static List randomNames = new ArrayList();
   private final List savedServers;
   public static final CopyOnWriteArrayList accounts = new CopyOnWriteArrayList();
   public final BooleanProperty removeInvalidAccounts;
   private CompletableFuture activeLoginTask;
   private final StringProperty apiKey;
   private GuiTextField emailOrTokenField;
   private AccountEntry selectedAccount;
   public static final ScrollBar scrollBar;
   private GuiPasswordField passwordField;

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      double d = (double)mouseY;
      scrollBar.handleMouseDrag(d);
   }

   private void loginFromClipboard() {
      if (this.activeLoginTask == null || this.activeLoginTask.isDone()) {
         String clipboardString = GuiScreen.getClipboardString();
         String[] strings = clipboardString.split(":");
         if (strings.length == 1 && strings[0].contains("@alt")) {
            CompletableFuture completableFuture = CompletableFuture.runAsync(() -> loginClipboardAlteningToken(strings));
            this.setActiveLoginTask(completableFuture);
         } else if (clipboardString.startsWith("eyJra") && clipboardString.length() > 16) {
            CompletableFuture completableFuture2 = CompletableFuture.runAsync(() -> loginClipboardAccessToken(clipboardString));
            this.setActiveLoginTask(completableFuture2);
         } else if (strings.length == 1) {
            CompletableFuture completableFuture3 = CompletableFuture.runAsync(() -> loginClipboardEasyMc(clipboardString));
            this.setActiveLoginTask(completableFuture3);
         } else if (strings.length == 2) {
            CompletableFuture completableFuture4 = CompletableFuture.runAsync(() -> {
               String str = strings[0];
               String str2 = strings[1];
               AccountAuthenticator.loginMicrosoftCredentials(str, str2);
            });
            this.setActiveLoginTask(completableFuture4);
         }
      }
   }

   private void loadRandomNames() {
      randomNames.clear();
      String str = Augustus.getInstance().getName().toLowerCase() + "/names";
      boolean flag = false;
      String str2 = str;
      ClientFileUtil.extractResource("assets/augustus/RandomNames.txt", str2, flag);

      try {
         BufferedReader reader = new BufferedReader(new FileReader(ClientPaths.CLIENT_DIR + "/names/RandomNames.txt"));

         String str3;
         while ((str3 = reader.readLine()) != null) {
            if (!str3.equals("")) {
               randomNames.add(str3.trim());
            }
         }

         reader.close();
      } catch (IOException e) {
      }

      Collections.shuffle(randomNames);
   }

   private static void loginAlteningApiKey(String str) {
      AccountAuthenticator.generateAndLoginTheAltening(str);
   }

   @Override
   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      scrollBar.handleMouseWheel();
   }

   private void loginInputCredentials() {
      String text = this.emailOrTokenField.getText();
      String text2 = this.passwordField.getText();
      AccountAuthenticator.loginMicrosoftCredentials(text, text2);
   }

   private void performFieldLogin() {
      if (this.activeLoginTask == null || this.activeLoginTask.isDone()) {
         if (this.emailOrTokenField.getText().isEmpty() && this.passwordField.getText().isEmpty()) {
            CompletableFuture completableFuture = CompletableFuture.runAsync(() -> {
               String str;
               if (ThreadLocalRandom.current().nextBoolean()) {
                  str = SyllableUsernameGenerator.generateUsername();
               } else {
                  str = AltUsernameGenerator.generateUsername(16);
               }

               boolean flag = false;
               AccountAuthenticator.loginCracked(str, flag);
            });
            this.setActiveLoginTask(completableFuture);
         } else if (!this.emailOrTokenField.getText().isEmpty() && !this.passwordField.getText().isEmpty()) {
            CompletableFuture completableFuture2 = CompletableFuture.runAsync(() -> this.loginInputCredentials());
            this.setActiveLoginTask(completableFuture2);
         } else if (!this.emailOrTokenField.getText().isEmpty() && this.passwordField.getText().isEmpty()) {
            String str2 = this.emailOrTokenField.getText().trim();
            if (str2.contains("@alt")) {
               CompletableFuture completableFuture3 = CompletableFuture.runAsync(() -> loginInputAlteningToken(str2));
               this.setActiveLoginTask(completableFuture3);
            } else if (str2.contains(":") && str2.contains("@")) {
               String[] strings = str2.split(":");
               CompletableFuture completableFuture4 = CompletableFuture.runAsync(() -> loginSplitCredentials(strings));
               this.setActiveLoginTask(completableFuture4);
            } else if (str2.startsWith("eyJra") && str2.length() > 16) {
               CompletableFuture completableFuture5 = CompletableFuture.runAsync(() -> {
                  AccountAuthenticator.loginWithAccessToken(str2);
               });
               CompletableFuture completableFuture6 = completableFuture5;
               this.setActiveLoginTask(completableFuture6);
            } else {
               CompletableFuture completableFuture7 = CompletableFuture.runAsync(() -> {
                  boolean flag2 = true;
                  AccountAuthenticator.loginCracked(str2, flag2);
               });
               CompletableFuture completableFuture8 = completableFuture7;
               this.setActiveLoginTask(completableFuture8);
            }
         }
      }
   }

   private void onDeleteSelectedClick(GuiButton button) {
      if (this.selectedAccount != null) {
         accounts.remove(this.selectedAccount);
         ScrollBar scrollBar2 = scrollBar;
         int size = accounts.size();
         scrollBar2.setItemCount(size);
         this.selectedAccount = null;
         AccountAuthenticator.playSuccessSound();
      } else {
         AccountAuthenticator.playFailureSound();
      }
   }

   private static void loginClipboardEasyMc(String str) {
      AccountAuthenticator.loginEasyMc(str);
   }

   @Override
   public void initContent() {
      super.initContent();
      this.loadSavedServerList();
      HashMap map = new HashMap();

      for (ServerData serverData : (Iterable<ServerData>)(this.savedServers)) {
         ResourceLocation location = new ResourceLocation("servers/" + serverData.serverIP + "/icon");
         map.put(serverData.serverIP, this.loadServerIconTexture(serverData, location));
      }

      if (accounts.isEmpty()) {
         Augustus.getInstance().getConfigManager().accountConfig.load();
      }

      for (AccountEntry accountEntry : (Iterable<AccountEntry>)(accounts)) {
         accountEntry.refreshServerIcons(map);
      }

      Color green = Color.GREEN;
      String str = "Waiting...";
      this.setStatus(str, green);
      ScaledResolution sr = new ScaledResolution(this.mc);
      int n = (int)Math.max(118.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
      byte b = 20;
      byte b2 = 54;
      byte b3 = 104;
      this.emailOrTokenField = this.addTextField(b3, b2, n, b);
      byte b4 = 20;
      byte b5 = 79;
      byte b6 = 104;
      this.passwordField = this.addPasswordField(b6, b5, n, b4);
      GuiButtonClickHandler guiButtonClickHandler = button -> this.onLoginButtonClick(button);
      String str2 = "Login";
      byte b7 = 20;
      byte b8 = 104;
      byte b9 = 104;
      this.addButton(b9, b8, n, b7, str2, guiButtonClickHandler);
      GuiButtonClickHandler guiButtonClickHandler2 = button2 -> this.onGenerateAlteningClick(button2);
      String str3 = "Generate";
      byte b10 = 20;
      short s = 129;
      byte b11 = 104;
      this.addButton(b11, s, n, b10, str3, guiButtonClickHandler2);
      GuiButtonClickHandler guiButtonClickHandler3 = button3 -> {
         String[] strings = new String[]{"igd", ""};
         String str4 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:ForceTermination xmlns:u=\"urn:schemas-upnp-org:service:WANIPConnection:1\" /></s:Body></s:Envelope>";

         for (String str5 : strings) {
            try {
               String str6 = "http://fritz.box:49000/" + str5 + "upnp/control/WANIPConn1";
               URL url = new URL(str6);
               HttpURLConnection conn = (HttpURLConnection)url.openConnection();
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
               conn.setRequestProperty("Connection", "close");
               conn.setRequestProperty("Content-Length", String.valueOf(str4.length()));
               conn.setRequestProperty("HOST", "fritz.box:49000");
               conn.setRequestProperty("SOAPACTION", "\"urn:schemas-upnp-org:service:WANIPConnection:1#ForceTermination\"");
               conn.setDoOutput(true);

               try (OutputStream outputStream = conn.getOutputStream()) {
                  outputStream.write(str4.getBytes("UTF-8"));
               }

               int responseCode = conn.getResponseCode();
               System.out.println("Response Code: " + responseCode);
            } catch (Exception e) {
               System.err.println("Fehler: " + e.getMessage());
            }
         }
      };
      String str7 = "Reconnect";
      byte b12 = 20;
      short s2 = 154;
      byte b13 = 104;
      this.addButton(b13, s2, n, b12, str7, guiButtonClickHandler3);
      GuiButtonClickHandler guiButtonClickHandler4 = button4 -> this.onClipboardLoginClick(button4);
      String str8 = "Clipboard login";
      byte b14 = 20;
      short s3 = 179;
      byte b15 = 104;
      this.addButton(b15, s3, n, b14, str8, guiButtonClickHandler4);
      GuiButtonClickHandler guiButtonClickHandler5 = button5 -> this.onDeviceCodeLoginClick(button5);
      String str9 = "Device code login";
      byte b16 = 20;
      short s4 = 204;
      byte b17 = 104;
      this.addButton(b17, s4, n, b16, str9, guiButtonClickHandler5);
      GuiButtonClickHandler guiButtonClickHandler6 = button6 -> this.onExternalBrowserLoginClick(button6);
      String str10 = "External browser login";
      byte b18 = 20;
      short s5 = 229;
      byte b19 = 104;
      this.addButton(b19, s5, n, b18, str10, guiButtonClickHandler6);
      GuiButtonClickHandler guiButtonClickHandler7 = button7 -> this.onDeleteSelectedClick(button7);
      String str11 = "Delete selected Alt";
      byte b20 = 20;
      short s6 = 254;
      byte b21 = 104;
      this.addButton(b21, s6, n, b20, str11, guiButtonClickHandler7);
      StringBuilder sb = new StringBuilder().append("Remove invalid: ");
      String str12;
      if (!(Boolean)this.removeInvalidAccounts.getValue()) {
         str12 = "§cFalse";
      } else {
         short s7 = 10780;
         str12 = "§aTrue";
      }

      String str13 = sb.append(str12).toString();
      GuiButtonClickHandler guiButtonClickHandler8 = button8 -> this.onToggleRemoveInvalidClick(button8);
      String str14 = str13;
      byte b22 = 20;
      short s8 = 279;
      byte b23 = 104;
      this.addButton(b23, s8, n, b22, str14, guiButtonClickHandler8);
      Augustus.getInstance().getClass();
      GuiButtonClickHandler guiButtonClickHandler9 = button9 -> {
         try {
            Desktop.getDesktop().browse(new URI("https://alts.fernan.club/"));
         } catch (URISyntaxException | IOException e2) {
            e2.printStackTrace();
         }
      };
      String str15 = "§cFernan's Altshop";
      byte b24 = 20;
      short s9 = 304;
      byte b25 = 104;
      this.addButton(b25, s9, n, b24, str15, guiButtonClickHandler9);
      int n2 = this.width - 5;
      int height2 = this.height;
      int width2 = this.width;
      byte b26 = 52;
      int n3 = n2;
      scrollBar.setBounds(n3, b26, width2, height2);
      ScrollBar scrollBar2 = scrollBar;
      int size = accounts.size();
      scrollBar2.setItemCount(size);
   }

   public void loadSavedServerList() {

      try {
         this.savedServers.clear();
         NBTTagCompound tag = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));
         if (tag == null) {
            return;
         }

         NBTTagList tagList = tag.getTagList("servers", 10);

         for (int i = 0; i < tagList.tagCount(); i++) {
            this.savedServers.add(ServerData.getServerDataFromNBTCompound(tagList.getCompoundTagAt(i)));
         }
      } catch (Exception e) {
      }
   }

   private void onExternalBrowserLoginClick(GuiButton button) {
      this.startExternalBrowserLogin();
   }

   public void setActiveLoginTask(CompletableFuture completableFuture) {
      this.activeLoginTask = completableFuture;
   }

   private TuplePair loadServerIconTexture(ServerData serverData, ResourceLocation location) {
      DynamicTexture dynamicTexture = null;
      if (serverData.getBase64EncodedIconData() == null) {
         this.mc.getTextureManager().deleteTexture(location);
         dynamicTexture = null;
      } else {
         ByteBuf buf = Unpooled.copiedBuffer(serverData.getBase64EncodedIconData(), Charsets.UTF_8);
         ByteBuf buf2 = Base64.decode(buf);

         BufferedImage image;
         exitBlock: {
            try {
               image = TextureUtil.readBufferedImage(new ByteBufInputStream(buf2));
               boolean flag = image.getWidth() == 64;
               Validate.validState(flag, "Must be 64 pixels wide", new Object[0]);
               Validate.validState(image.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
               break exitBlock;
            } catch (Throwable e) {
               serverData.setBase64EncodedIconData((String)null);
            } finally {
               buf.release();
               buf2.release();
            }

            return new TuplePair(location, null);
         }

         dynamicTexture = new DynamicTexture(image.getWidth(), image.getHeight());
         this.mc.getTextureManager().loadTexture(location, dynamicTexture);

         try {
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), dynamicTexture.getTextureData(), 0, image.getWidth());
         } catch (Throwable e2) {
         }

         dynamicTexture.updateDynamicTexture();
      }

      return new TuplePair(location, dynamicTexture);
   }

   private static void loginClipboardAccessToken(String str) {
      AccountAuthenticator.loginWithAccessToken(str);
   }

   private static void loginSplitCredentials(String[] strings) {
      String str = strings[0];
      String str2 = strings[1];
      AccountAuthenticator.loginMicrosoftCredentials(str, str2);
   }

   private int drawServerIconRow(Collection items, int n, int n2, int n3, int n4, int n5) {
      int n6 = 0;
      int n7 = this.countLoadedServerIcons(items);
      int n8 = n * n7;

      for (TuplePair tuplePair : (Iterable<TuplePair>)(items)) {
         if (tuplePair.getSecond() != null) {
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            int n9 = (int)((float)n2 + (float)(n4 - n2) / 2.0F) - n8 / 2 + n6;
            int n10 = n3 + n5;
            ResourceLocation first = (ResourceLocation)tuplePair.getFirst();
            int n11 = n10;
            int n12 = n9;
            this.drawDetailServerIcon(n12, n11, first);
            n6 += n;
         }
      }

      return n5 + 29;
   }

   public void startExternalBrowserLogin() {
      CompletableFuture.runAsync(() -> AccountAuthenticator.loginExternalBrowser());
   }

   private static void loginClipboardAlteningToken(String[] strings) {
      String str = strings[0];
      AccountAuthenticator.loginTheAlteningToken(str);
   }

   public CompletableFuture getActiveLoginTask() {
      return this.activeLoginTask;
   }

   private void onClipboardLoginClick(GuiButton button) {
      this.loginFromClipboard();
   }

   private void onLoginButtonClick(GuiButton button) {
      this.performFieldLogin();
   }

   private void onGenerateAlteningClick(GuiButton button) {
      this.generateAlteningAccount();
   }

   private void onToggleRemoveInvalidClick(GuiButton button) {
      BooleanProperty removeInvalidAccounts2 = this.removeInvalidAccounts;
      Boolean bool = !(Boolean)this.removeInvalidAccounts.getValue();
      Boolean bool2 = bool;
      removeInvalidAccounts2.setValue(bool2);
      StringBuilder sb = new StringBuilder().append("Remove invalid: ");
      String str;
      if (!(Boolean)this.removeInvalidAccounts.getValue()) {
         str = "§cFalse";
      } else {
         short s = 20679;
         str = "§aTrue";
      }

      button.displayString = sb.append(str).toString();
   }

   private void onDeviceCodeLoginClick(GuiButton button) {
      this.startDeviceCodeLogin();
   }

   @Override
   public void drawContent(int n, int n2, float f) {
      super.drawContent(n, n2, f);
      FontRenderer fontRendererObj2 = this.fontRendererObj;
      ScaledResolution sr = new ScaledResolution(this.mc);
      int n3 = sr.getScaledWidth() - 102;
      int n4 = (int)Math.max(118.0F, Math.min((float)sr.getScaledWidth() / 2.0F - 100.0F - 20.0F, 150.0F));
      if (this.emailOrTokenField != null && this.emailOrTokenField.getText().isEmpty() && !this.emailOrTokenField.isFocused()) {
         fontRendererObj2.drawStringWithShadow("Email / Email:Password", 109.0F, 60.0F, Color.gray.getRGB());
      }

      if (this.passwordField != null && this.passwordField.getText().isEmpty() && !this.passwordField.isFocused()) {
         fontRendererObj2.drawStringWithShadow("Password", 109.0F, 85.0F, Color.gray.getRGB());
      }

      int n5 = Math.min(n3 - n4 - 20, 300);
      int n6 = sr.getScaledWidth() - 10 - n5;
      int clampedScrollOffset = (int)scrollBar.getClampedScrollOffset();

      for (AccountEntry accountEntry : (Iterable<AccountEntry>)(accounts)) {
         if (accountEntry.isMarked()) {
            int n7 = 57 + clampedScrollOffset;
            boolean flag = accountEntry == this.selectedAccount;
            List savedServers2 = this.savedServers;
            boolean flag2 = flag;
            byte b = 40;
            int n8 = n7;
            accountEntry.drawEntry(n6, n8, n5, b, n, n2, flag2, savedServers2);
            clampedScrollOffset += 42;
         }
      }

      for (AccountEntry accountEntry2 : (Iterable<AccountEntry>)(accounts)) {
         if (!accountEntry2.isMarked()) {
            int n9 = 57 + clampedScrollOffset;
            boolean flag3 = accountEntry2 == this.selectedAccount;
            List savedServers3 = this.savedServers;
            boolean flag4 = flag3;
            byte b2 = 40;
            int n10 = n9;
            accountEntry2.drawEntry(n6, n10, n5, b2, n, n2, flag4, savedServers3);
            clampedScrollOffset += 42;
         }
      }

      if (n3 - n4 - 330 > 300 && this.selectedAccount != null) {
         int n11 = n4 + 102 + 10;
         byte b3 = 57;
         int n12 = n11 + n3 - n4 - 330;
         int n13 = b3 + 170;
         RenderUtil.drawRect3(n11, b3, n12, n13, new Color(0, 0, 0, 100).getRGB());
         RenderUtil.scissorRect((float)(n11 + 5), (float)(b3 + 5), (float)(n12 - 5), (float)(n13 - 5));
         int n14 = 0;
         String str = "§7Detailed information:";
         n14 = this.drawCenteredDetailLine(str, n11, b3, n12, n13, n14) + 5;
         String str2 = "§7Name: §f" + this.selectedAccount.getUsername();
         n14 = this.drawCenteredDetailLine(str2, n11, b3, n12, n13, n14);
         String str3 = "§7Login type: §f" + this.selectedAccount.getLoginType().getDisplayName();
         n14 = this.drawCenteredDetailLine(str3, n11, b3, n12, n13, n14);
         StringBuilder sb = new StringBuilder();
         short s = 2334;
         StringBuilder sb2 = sb.append("§7Account state: §f");
         String str4;
         if (this.selectedAccount.isWorking()) {
            str4 = "§aFunctional";
         } else {
            s = 6852;
            str4 = "§cInvalid";
         }

         String str5 = sb2.append(str4).toString();
         n14 = this.drawCenteredDetailLine(str5, n11, b3, n12, n13, n14);
         DateFormat dateInstance = DateFormat.getDateInstance(2);
         DateFormat timeInstance = DateFormat.getTimeInstance(3);
         String str6 = dateInstance.format(this.selectedAccount.getLastLoginTime());
         String str7 = timeInstance.format(this.selectedAccount.getLastLoginTime());
         String str8 = "§7Last login time: §f" + str6 + " " + str7;
         n14 = this.drawCenteredDetailLine(str8, n11, b3, n12, n13, n14);
         String str9 = "§7Playtime: §f" + this.selectedAccount.getPlayTimeMillis() / 1000L / 60L + " min";
         n14 = this.drawCenteredDetailLine(str9, n11, b3, n12, n13, n14);
         Collection values = this.selectedAccount.getBannedServerIcons().values();
         if (this.countLoadedServerIcons(values) > 0) {
            String str10 = "§7Banned on servers:";
            n14 = this.drawCenteredDetailLine(str10, n11, b3, n12, n13, n14) + 6;
            Collection values2 = this.selectedAccount.getBannedServerIcons().values();
            byte b4 = 35;
            Collection items = values2;
            n14 = this.drawServerIconRow(items, b4, n11, b3, n12, n14);
         }

         Collection values3 = this.selectedAccount.getUnbannedServerIcons().values();
         if (this.countLoadedServerIcons(values3) > 0) {
            String str11 = "§7Unbanned on servers:";
            n14 = this.drawCenteredDetailLine(str11, n11, b3, n12, n13, n14) + 6;
            Collection values4 = this.selectedAccount.getUnbannedServerIcons().values();
            byte b5 = 35;
            Collection items2 = values4;
            this.drawServerIconRow(items2, b5, n11, b3, n12, n14);
         }
      }

      scrollBar.updateAndDraw();
   }

   public void startDeviceCodeLogin() {
      CompletableFuture.runAsync(() -> AccountAuthenticator.loginDeviceCode());
   }

   @Override
   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);

      for (AccountEntry accountEntry : (Iterable<AccountEntry>)(accounts)) {
         accountEntry.mouseClicked(mouseX, mouseY, mouseButton);
         if ((mouseButton == 1 || mouseButton == 0) && RenderUtil.isHovered((float)accountEntry.getRenderX(), (float)accountEntry.getRenderY(), (float)accountEntry.getRenderWidth(), (float)accountEntry.getRenderHeight(), mouseX, mouseY)
            )
          {
            this.selectedAccount = accountEntry;
         }
      }

      double d = (double)mouseX;
      double d2 = (double)mouseY;
      double d3 = d2;
      double d4 = d;
      scrollBar.handleMouseClick(d4, d3);
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      if ((keyCode == 211 || keyCode == 14) && this.selectedAccount != null && !this.emailOrTokenField.isFocused() && !this.passwordField.isFocused()) {
         accounts.remove(this.selectedAccount);
         ScrollBar scrollBar2 = scrollBar;
         int size = accounts.size();
         scrollBar2.setItemCount(size);
         this.selectedAccount = null;
      }
   }

   private void drawDetailServerIcon(int n, int n2, ResourceLocation location) {
      this.mc.getTextureManager().bindTexture(location);
      GlStateManager.enableBlend();
      Gui.drawModalRectWithCustomSizedTexture(n, n2, 0.0F, 0.0F, 30, 30, 30.0F, 30.0F);
      GlStateManager.disableBlend();
   }

   private void generateAlteningAccount() {
      String clipboardString = getClipboardString();
      if (clipboardString.contains("api-")) {
         this.apiKey.setValue(clipboardString);
      }

      if (this.activeLoginTask == null || this.activeLoginTask.isDone()) {
         String value = (String)this.apiKey.getValue();
         if (value != null && !value.isEmpty()) {
            CompletableFuture completableFuture = CompletableFuture.runAsync(() -> loginAlteningApiKey(value));
            this.setActiveLoginTask(completableFuture);
         }
      }
   }

   static {
      scrollBar = new ScrollBar(42);
   }

   private int countLoadedServerIcons(Collection items) {
      int n = 0;

      for (TuplePair tuplePair : (Iterable<TuplePair>)(items)) {
         if (tuplePair.getSecond() != null) {
            n++;
         }
      }

      return n;
   }

   private static void loginInputAlteningToken(String str) {
      AccountAuthenticator.loginTheAlteningToken(str);
   }

   public AccountManagerScreen() {
       super("Account Manager");
      this.activeLoginTask = null;
      this.savedServers = Lists.newArrayList();
      this.apiKey = new StringProperty(ClientSettingsConfigFile.abstractFeature, "API-Key", "");
      this.removeInvalidAccounts = new BooleanProperty(ClientSettingsConfigFile.abstractFeature, "RemoveInvalidAccounts", false);
      instance = this;
      this.loadRandomNames();
   }

   private int drawCenteredDetailLine(String str, int n, int n2, int n3, int n4, int n5) {
      this.fontRendererObj
         .drawString2(
            str,
            (float)n + (float)(n3 - n) / 2.0F - (float)this.fontRendererObj.getStringWidth(str) / 2.0F,
            (float)(n2 + 5 + n5),
            Color.white.getRGB()
         );
      return n5 + 11;
   }

}
