package net.augustus.ui.config;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.clickgui.ClickGuiWindow;
import net.augustus.clickgui.CreateConfigScreen;
import net.augustus.config.ConfigManager;
import net.augustus.config.ModuleConfigStorage;
import net.augustus.font.CustomFontUtil;
import net.augustus.font.FontManager;
import net.augustus.render.RenderUtil;
import net.augustus.settings.options.NotificationCategoryOption;
import net.augustus.ui.notifications.NotificationLevel;
import net.augustus.ui.notifications.NotificationManager;
import net.augustus.ui.widgets.SmoothScrollBar;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.utils.file.DirectoryWatchService;
import net.augustus.utils.file.DirectoryWatcher;
import net.augustus.vendor.OnlineConfigMetadata;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ConfigSystemPanel extends ClickGuiWindow {
   private static final float LOAD_BUTTON_SMALL_WIDTH;
   private static final float CREATE_BUTTON_SMALL_WIDTH;
   private boolean initialLoadTriggered;
   private static final float FRIENDS_CONFIGS_HEADER_WIDTH;
   private static final float MY_CONFIGS_HEADER_WIDTH;
   private String shareActionLabel;
   private final CustomFontUtil itemFont;
   private int userConfigsScrollOffset;
   private final SmoothScrollBar userConfigsScrollBar;
   public final CopyOnWriteArrayList<AbstractConfigListItem> userConfigs;
   private static final float FOLDER_BUTTON_SMALL_WIDTH;
   private static final float CREATE_BUTTON_WIDTH;
   private static final float REFRESH_BUTTON_WIDTH;
   public CopyOnWriteArrayList<AbstractConfigListItem> friendConfigs;
   private int officialConfigsScrollOffset;
   private int friendConfigsScrollOffset;
   private static final float FOLDER_BUTTON_WIDTH;
   private static final float OFFICIAL_CONFIGS_HEADER_WIDTH;
   private final SmoothScrollBar officialConfigsScrollBar;
   private final CustomFontUtil headerFont;
   private static final float LOAD_BUTTON_WIDTH;
   private static final float DELETE_BUTTON_WIDTH;
   public CompletableFuture refreshFuture;
   private static final float REFRESH_BUTTON_SMALL_WIDTH;
   public CopyOnWriteArrayList<AbstractConfigListItem> officialConfigs;
   private static final String CONFIGS_DIRECTORY = ClientPaths.CLIENT_DIR + File.separator + "configs";
   private static final float SAVE_BUTTON_SMALL_WIDTH;
   private static final float DELETE_BUTTON_SMALL_WIDTH;
   private AbstractConfigListItem selectedConfig;
   private final SmoothScrollBar friendConfigsScrollBar;
   private static final float SAVE_BUTTON_WIDTH;

   private void scanLocalConfigDirectory() {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      this.userConfigs.clear();

      try {
         File file = new File(CONFIGS_DIRECTORY);
         File[] files = file.listFiles(file2 -> {
            int[] windowStateFlags3 = ClickGuiWindow.getWindowStateFlags();
            return !file2.isDirectory() && file2.getName().endsWith(".json");
         });
         if (files != null) {
            for (File file3 : files) {
               this.loadLocalConfigEntry(file3);
            }
         }
      } catch (Exception e) {
      }

      this.mergeAndSortUserConfigs();
   }

   private void onLocalConfigFileModified(File file) {
      this.removeConfigEntryForFile(file);
      this.loadLocalConfigEntry(file);
      this.mergeAndSortUserConfigs();
   }

   private void onLocalConfigFileDeleted(Path path) {
      File file = path.toFile();
      this.removeConfigEntryForFile(file);
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      if (this.isVisible()) {
         super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      }
   }

   public static void notifyConfigStatus(String str, NotificationLevel notificationLevel) {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      if (mc.theWorld != null) {
         ChatUtil.sendChat3(str);
         if (Augustus.getInstance().getModuleManager().notifications.isToggled() && NotificationCategoryOption.CONFIG_SYSTEM.isSelected()) {
            NotificationManager.pushNotification("Configsystem", str, notificationLevel);
         }
      }
   }

   private synchronized void reloadOnlineConfigs() {
      byte b = 0;
      List list = this.fetchOnlineConfigItems(b);
      byte b2 = 2;
      List list2 = this.fetchOnlineConfigItems(b2);
      this.officialConfigs.clear();
      this.officialConfigs.addAll(list);
      this.friendConfigs.clear();
      this.friendConfigs.addAll(list2);
   }

   static {
      String str = "OfficialConfigs";
      OFFICIAL_CONFIGS_HEADER_WIDTH = FontManager.consolas32.getStringWidth(str);
      String str2 = "MyConfigs";
      MY_CONFIGS_HEADER_WIDTH = FontManager.consolas32.getStringWidth(str2);
      String str3 = "FriendsConfigs";
      FRIENDS_CONFIGS_HEADER_WIDTH = FontManager.consolas32.getStringWidth(str3);
      String str4 = "Create";
      CREATE_BUTTON_WIDTH = FontManager.consolas32.getStringWidth(str4);
      String str5 = "Load";
      LOAD_BUTTON_WIDTH = FontManager.consolas32.getStringWidth(str5);
      String str6 = "Save";
      SAVE_BUTTON_WIDTH = FontManager.consolas32.getStringWidth(str6);
      String str7 = "Folder";
      FOLDER_BUTTON_WIDTH = FontManager.consolas32.getStringWidth(str7);
      String str8 = "Refresh";
      REFRESH_BUTTON_WIDTH = FontManager.consolas32.getStringWidth(str8);
      String str9 = "Delete";
      DELETE_BUTTON_WIDTH = FontManager.consolas32.getStringWidth(str9);
      String str10 = "Create";
      CREATE_BUTTON_SMALL_WIDTH = FontManager.consolas30.getStringWidth(str10);
      String str11 = "Load";
      LOAD_BUTTON_SMALL_WIDTH = FontManager.consolas30.getStringWidth(str11);
      String str12 = "Save";
      SAVE_BUTTON_SMALL_WIDTH = FontManager.consolas30.getStringWidth(str12);
      String str13 = "Folder";
      FOLDER_BUTTON_SMALL_WIDTH = FontManager.consolas30.getStringWidth(str13);
      String str14 = "Refresh";
      REFRESH_BUTTON_SMALL_WIDTH = FontManager.consolas30.getStringWidth(str14);
      String str15 = "Delete";
      DELETE_BUTTON_SMALL_WIDTH = FontManager.consolas30.getStringWidth(str15);
   }

   public void refreshOnlineConfigs() {
      this.reloadOnlineConfigs();
   }

   private void mergeAndSortUserConfigs() {
      List<OnlineConfigMetadata> list = java.util.Collections.emptyList();
      ClickGuiWindow.getWindowStateFlags();

      for (OnlineConfigMetadata onlineConfigMetadata : list) {
         String name = onlineConfigMetadata.getName();
         Stream<AbstractConfigListItem> stream = this.userConfigs.stream();
         boolean flag = stream.anyMatch(configListItem -> configListItem.getConfigName().equals(name));
         if (!flag) {
            UserConfigListItem userConfigListItem = new UserConfigListItem(name, onlineConfigMetadata.getAuthor(), onlineConfigMetadata.getUpdatedDate(), onlineConfigMetadata.getUpdatedTime(), false, true, onlineConfigMetadata.isEditable());
            this.userConfigs.add(userConfigListItem);
         }
      }

      this.userConfigs.sort(Comparator.comparing(configListItem2 -> configListItem2.getConfigName().toLowerCase()));
   }

   private void removeConfigEntryForFile(File file) {
      this.userConfigs
         .stream()
         .filter(configListItem -> configListItem.getConfigName().equals(file.getName().substring(0, file.getName().lastIndexOf("."))))
         .findFirst()
         .ifPresent(this.userConfigs::remove);
      this.mergeAndSortUserConfigs();
   }

   private void loadLocalConfigEntry(File file) {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      String name = file.getName();
      short s = 22191;
      if (name.endsWith(".json")) {
         try {
            String str = name.substring(0, name.length() - 5);
            String str2 = "configs";
            String[] configHeaderInfo = Augustus.getInstance().getConfigManager().moduleConfigStorage.getConfigHeaderInfo(str, str2);
            String str3 = configHeaderInfo == null ? "" : configHeaderInfo[1];
            String str4 = configHeaderInfo == null ? "" : configHeaderInfo[2];
            String str5;
            if (configHeaderInfo != null && configHeaderInfo.length == 5) {
               str5 = configHeaderInfo[3];
            } else {
               str5 = "true";
            }

            String str6 = str5;
            String str7 = configHeaderInfo != null ? (configHeaderInfo.length == 5 ? configHeaderInfo[4] : configHeaderInfo[3]) : "";
            this.userConfigs.stream().filter(configListItem -> configListItem.getConfigName().equals(str)).findAny().ifPresent(this.userConfigs::remove);
            this.userConfigs.add(new UserConfigListItem(str, str7, str3, str4, true, this.isConfigUploadedOnline(str), Boolean.parseBoolean(str6)));
         } catch (Exception e) {
            if (mc.theWorld != null) {
               String str8 = "Failed to load config: " + file.getName();
               NotificationLevel error = NotificationLevel.ERROR;
               notifyConfigStatus(str8, error);
            }
         }
      }
   }

   @Override
   public void drawWindow(int n, int n2, float f) {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      super.drawWindow(n, n2, f);
      if (this.isVisible()) {
         ScaledResolution sr = new ScaledResolution(mc);
         GL11.glPushMatrix();
         GL11.glScaled(1.0 / (double)sr.getScaleFactor(), 1.0 / (double)sr.getScaleFactor(), 1.0);
         if (false) {
            String str;
            if (!this.selectedConfig.isSharedWithFriends()) {
               str = "ShareToFriends";
            } else {
               short s = 31718;
               str = "HideFromFriends";
            }

            this.shareActionLabel = str;
         } else {
            this.shareActionLabel = null;
         }

         n *= sr.getScaleFactor();
         n2 *= sr.getScaleFactor();
         GL11.glPushMatrix();
         n -= this.getX();
         n2 -= this.getY();
         GL11.glTranslatef((float)this.getX(), (float)this.getY(), 0.0F);
         int n3 = this.getWidth() / 4;
         byte b = 0;
         int colorInt = ClientManagers.moduleManager.clickGui.headerColor.getColorInt(b);
         RenderUtil.drawRect3(n3 - 1, 30, n3 + 1, this.getHeight(), colorInt);
         RenderUtil.drawRect3(n3 * 2 - 1, 30, n3 * 2 + 1, this.getHeight(), colorInt);
         RenderUtil.drawRect3(n3 * 3 - 1, 30, n3 * 3 + 1, this.getHeight(), colorInt);
         RenderUtil.drawRect3(0, 82, n3 * 3, 84, colorInt);
         if (!this.isFullScreen() && ClientManagers.moduleManager.clickGui.outline.getValue()) {
            float width2 = (float)this.getWidth();
            float height2 = (float)this.getHeight();
            float f2 = this.isFullScreen() ? 0.0F : (float)this.cornerRadius;
            byte b2 = 0;
            RenderUtil.drawRoundedRectOutline(0.0F, 0.0F, width2, height2, f2, 1, 1, ClientManagers.moduleManager.clickGui.outlineColor.getColorInt(b2));
         }

         byte b3 = 0;
         int colorInt2 = ClientManagers.moduleManager.clickGui.textColor.getColorInt(b3);
         GL11.glEnable(3089);
         RenderUtil.scissorRectScaled(
            (float)this.getX(),
            (float)this.getY(),
            (float)(this.getX() + this.getWidth()),
            (float)(this.getY() + this.getHeight()),
            1.0
         );
         double d = (double)((int)((float)n3 / 2.0F - OFFICIAL_CONFIGS_HEADER_WIDTH / 2.0F));
         double d2 = 50.0;
         double d3 = d;
         String str2 = "OfficialConfigs";
         this.headerFont.drawString(str2, d3, d2, colorInt2);
         d = (double)((int)((float)n3 + (float)n3 / 2.0F - MY_CONFIGS_HEADER_WIDTH / 2.0F));
         double d4 = 50.0;
         double d5 = d;
         String str3 = "MyConfigs";
         this.headerFont.drawString(str3, d5, d4, colorInt2);
         d = (double)((int)((float)(n3 + n3) + (float)n3 / 2.0F - FRIENDS_CONFIGS_HEADER_WIDTH / 2.0F));
         double d6 = 50.0;
         double d7 = d;
         String str4 = "FriendsConfigs";
         this.headerFont.drawString(str4, d7, d6, colorInt2);
         RenderUtil.scissorRectScaled(
            (float)(this.getX() + this.cornerRadius),
            (float)(this.getY() + 90),
            (float)(this.getX() + n3 - 3),
            (float)(this.getY() + this.getHeight() - this.cornerRadius),
            1.0
         );
         int n4 = 0;

         for (AbstractConfigListItem configListItem : this.officialConfigs) {
            int n5 = n3 - 3;
            float f3 = 0.0F + (float)n5 / 2.0F;
            CustomFontUtil itemFont2 = this.itemFont;
            String configName = configListItem.getConfigName();
            int n6;
            String str5 = configName;
            int n7 = (int)(f3 - itemFont2.getStringWidth(str5) / 2.0F);
            int n8 = 90 + n4 + this.officialConfigsScrollOffset;
            n6 = (int)this.itemFont.getFontHeight();
            boolean flag = false;
            CustomFontUtil itemFont3 = this.itemFont;
            int n9 = n6;
            int n10 = n8;
            int n11 = n7;
            configListItem.renderItem(n11, n10, n5, n9, n, n2, itemFont3, flag);
            n4 += 25;
         }

         SmoothScrollBar smoothScrollBar = this.officialConfigsScrollBar;
         int n12 = n3 - 15;
         int n13 = n3 - 5;
         int n14 = this.getHeight() - this.cornerRadius;
         int n15 = 90 - this.officialConfigsScrollOffset;
         int n16 = n14;
         int n17 = n13;
         byte b4 = 90;
         int n18 = n12;
         smoothScrollBar.drawVerticalBar(n18, b4, n17, n16, n15, n4, colorInt);
         SmoothScrollBar smoothScrollBar2 = this.officialConfigsScrollBar;
         n14 = this.getHeight() - this.cornerRadius;
         int n19 = this.officialConfigsScrollOffset;
         int n20 = this.getHeight() - this.cornerRadius - 90;
         byte b5 = 90;
         byte b6 = 0;
         int n21 = n19;
         int n22 = n14;
         byte b7 = 90;
         this.officialConfigsScrollOffset = smoothScrollBar2.updateScrollOffset(n4, b7, n22, n21, b6, b5, n3, n20, n, n2);
         RenderUtil.scissorRectScaled(
            (float)(this.getX() + n3 + 3),
            (float)(this.getY() + 90),
            (float)(this.getX() + n3 * 2 - 3),
            (float)(this.getY() + this.getHeight() - this.cornerRadius),
            1.0
         );
         int n23 = 0;

         for (AbstractConfigListItem configListItem2 : this.userConfigs) {
            int n24 = n3 - 3;
            float f4 = (float)n3 + (float)n24 / 2.0F;
            CustomFontUtil itemFont4 = this.itemFont;
            String configName2 = configListItem2.getConfigName();
            String str6 = configName2;
            int n25 = (int)(f4 - itemFont4.getStringWidth(str6) / 2.0F);
            int n26 = 90 + n23 + this.userConfigsScrollOffset;
            n14 = (int)this.itemFont.getFontHeight();
            boolean flag2 = this.selectedConfig != null ? configListItem2.equals(this.selectedConfig) : false;
            CustomFontUtil itemFont5 = this.itemFont;
            int n27 = n14;
            int n28 = n26;
            int n29 = n25;
            configListItem2.renderItem(n29, n28, n24, n27, n, n2, itemFont5, flag2);
            n23 += 25;
         }

         smoothScrollBar = this.userConfigsScrollBar;
         int n30 = n3 * 2 - 15;
         n13 = n3 * 2 - 5;
         n14 = this.getHeight() - this.cornerRadius;
         int n31 = 90 - this.userConfigsScrollOffset;
         int n32 = n14;
         int n33 = n13;
         byte b8 = 90;
         int n34 = n30;
         smoothScrollBar.drawVerticalBar(n34, b8, n33, n32, n31, n23, colorInt);
         SmoothScrollBar userConfigsScrollBar2 = this.userConfigsScrollBar;
         n14 = this.getHeight() - this.cornerRadius;
         n19 = this.userConfigsScrollOffset;
         int n35 = this.getHeight() - this.cornerRadius - 90;
         byte b9 = 90;
         int n36 = n19;
         int n37 = n14;
         byte b10 = 90;
         this.userConfigsScrollOffset = userConfigsScrollBar2.updateScrollOffset(n23, b10, n37, n36, n3, b9, n3, n35, n, n2);
         RenderUtil.scissorRectScaled(
            (float)(this.getX() + n3 * 2 + 3),
            (float)(this.getY() + 90),
            (float)(this.getX() + n3 * 3 - 3),
            (float)(this.getY() + this.getHeight() - this.cornerRadius),
            1.0
         );
         int n38 = 0;

         for (AbstractConfigListItem configListItem3 : this.friendConfigs) {
            int n39 = n3 - 3;
            float f5 = (float)(n3 * 2) + (float)n39 / 2.0F;
            CustomFontUtil itemFont6 = this.itemFont;
            String configName3 = configListItem3.getConfigName();
            String str7 = configName3;
            int n40 = (int)(f5 - itemFont6.getStringWidth(str7) / 2.0F);
            int n41 = 90 + n38 + this.friendConfigsScrollOffset;
            n14 = (int)this.itemFont.getFontHeight();
            boolean flag3 = this.selectedConfig != null ? configListItem3.equals(this.selectedConfig) : false;
            CustomFontUtil itemFont7 = this.itemFont;
            int n42 = n14;
            int n43 = n41;
            int n44 = n40;
            configListItem3.renderItem(n44, n43, n39, n42, n, n2, itemFont7, flag3);
            n38 += 25;
         }

         smoothScrollBar = this.friendConfigsScrollBar;
         int n45 = n3 * 3 - 15;
         n13 = n3 * 3 - 5;
         n14 = this.getHeight() - this.cornerRadius;
         int n46 = 90 - this.friendConfigsScrollOffset;
         int n47 = n14;
         int n48 = n13;
         byte b11 = 90;
         int n49 = n45;
         smoothScrollBar.drawVerticalBar(n49, b11, n48, n47, n46, n38, colorInt);
         SmoothScrollBar friendConfigsScrollBar2 = this.friendConfigsScrollBar;
         n14 = this.getHeight() - this.cornerRadius;
         n19 = this.friendConfigsScrollOffset;
         int n50 = n3 * 2;
         int n51 = this.getHeight() - this.cornerRadius - 90;
         byte b12 = 90;
         int n52 = n50;
         int n53 = n19;
         int n54 = n14;
         byte b13 = 90;
         this.friendConfigsScrollOffset = friendConfigsScrollBar2.updateScrollOffset(n38, b13, n54, n53, n52, b12, n3, n51, n, n2);
         RenderUtil.scissorRectScaled(
            (float)(this.getX() + n3 * 3 + 3),
            (float)(this.getY() + 40),
            (float)(this.getX() + this.getWidth() - this.cornerRadius),
            (float)(this.getY() + this.getHeight() - this.cornerRadius),
            1.0
         );
         int n55 = 50;
         CustomFontUtil headerFont2 = this.headerFont;
         d = (double)((int)((float)(n3 * 3) + (float)n3 / 2.0F - CREATE_BUTTON_WIDTH / 2.0F));
         double d8 = (double)n55;
         int hoveredColor = RenderUtil.getHoveredColor(
            colorInt2,
            0.2,
            n,
            n2,
            (int)((float)(n3 * 3) + (float)n3 / 2.0F - CREATE_BUTTON_WIDTH / 2.0F - 10.0F),
            47,
            (int)CREATE_BUTTON_SMALL_WIDTH + 20,
            (int)this.itemFont.getFontHeight() + 6
         );
         double d9 = d8;
         double d10 = d;
         String str8 = "Create";
         headerFont2.drawString(str8, d10, d9, hoveredColor);
         n55 += 35;
         CustomFontUtil headerFont3 = this.headerFont;
         d = (double)((int)((float)(n3 * 3) + (float)n3 / 2.0F - LOAD_BUTTON_WIDTH / 2.0F));
         double d11 = (double)n55;
         int hoveredColor2 = RenderUtil.getHoveredColor(
            colorInt2,
            0.2,
            n,
            n2,
            (int)((float)(n3 * 3) + (float)n3 / 2.0F - LOAD_BUTTON_WIDTH / 2.0F - 10.0F),
            82,
            (int)LOAD_BUTTON_SMALL_WIDTH + 20,
            (int)this.itemFont.getFontHeight() + 6
         );
         double d12 = d11;
         double d13 = d;
         String str9 = "Load";
         headerFont3.drawString(str9, d13, d12, hoveredColor2);
         n55 += 35;
         CustomFontUtil headerFont4 = this.headerFont;
         d = (double)((int)((float)(n3 * 3) + (float)n3 / 2.0F - SAVE_BUTTON_WIDTH / 2.0F));
         double d14 = (double)n55;
         int hoveredColor3 = RenderUtil.getHoveredColor(
            colorInt2,
            0.2,
            n,
            n2,
            (int)((float)(n3 * 3) + (float)n3 / 2.0F - SAVE_BUTTON_WIDTH / 2.0F - 10.0F),
            117,
            (int)SAVE_BUTTON_SMALL_WIDTH + 20,
            (int)this.itemFont.getFontHeight() + 6
         );
         double d15 = d14;
         double d16 = d;
         String str10 = "Save";
         headerFont4.drawString(str10, d16, d15, hoveredColor3);
         n55 += 35;
         CustomFontUtil headerFont5 = this.headerFont;
         d = (double)((int)((float)(n3 * 3) + (float)n3 / 2.0F - FOLDER_BUTTON_WIDTH / 2.0F));
         double d17 = (double)n55;
         int hoveredColor4 = RenderUtil.getHoveredColor(
            colorInt2,
            0.2,
            n,
            n2,
            (int)((float)(n3 * 3) + (float)n3 / 2.0F - FOLDER_BUTTON_WIDTH / 2.0F - 10.0F),
            152,
            (int)FOLDER_BUTTON_SMALL_WIDTH + 20,
            (int)this.itemFont.getFontHeight() + 6
         );
         double d18 = d17;
         double d19 = d;
         String str11 = "Folder";
         headerFont5.drawString(str11, d19, d18, hoveredColor4);
         n55 += 35;
         CustomFontUtil headerFont6 = this.headerFont;
         d = (double)((int)((float)(n3 * 3) + (float)n3 / 2.0F - REFRESH_BUTTON_WIDTH / 2.0F));
         double d20 = (double)n55;
         int hoveredColor5 = RenderUtil.getHoveredColor(
            colorInt2,
            0.2,
            n,
            n2,
            (int)((float)(n3 * 3) + (float)n3 / 2.0F - REFRESH_BUTTON_WIDTH / 2.0F - 10.0F),
            187,
            (int)REFRESH_BUTTON_SMALL_WIDTH + 20,
            (int)this.itemFont.getFontHeight() + 6
         );
         double d21 = d20;
         double d22 = d;
         String str12 = "Refresh";
         headerFont6.drawString(str12, d22, d21, hoveredColor5);
         if (this.shareActionLabel != null) {
            n55 += 35;
            String shareActionLabel2 = this.shareActionLabel;
            float stringWidth = FontManager.consolas32.getStringWidth(shareActionLabel2);
            CustomFontUtil headerFont7 = this.headerFont;
            String shareActionLabel3 = this.shareActionLabel;
            d = (double)((int)((float)(n3 * 3) + (float)n3 / 2.0F - stringWidth / 2.0F));
            double d23 = (double)n55;
            int hoveredColor6 = RenderUtil.getHoveredColor(
               colorInt2,
               0.2,
               n,
               n2,
               (int)((float)(n3 * 3) + (float)n3 / 2.0F - SAVE_BUTTON_WIDTH / 2.0F - 10.0F),
               222,
               (int)stringWidth + 20,
               (int)this.itemFont.getFontHeight() + 6
            );
            double d24 = d23;
            double d25 = d;
            String str13 = shareActionLabel3;
            headerFont7.drawString(str13, d25, d24, hoveredColor6);
         }

         CustomFontUtil headerFont8 = this.headerFont;
         d = (double)((int)((float)(n3 * 3) + (float)n3 / 2.0F - DELETE_BUTTON_WIDTH / 2.0F));
         double d26 = (double)((float)this.getHeight() - this.headerFont.getFontHeight() - 2.0F - (float)this.cornerRadius);
         int hoveredColor7 = RenderUtil.getHoveredColor(
            colorInt2,
            0.2,
            n,
            n2,
            (int)((float)(n3 * 3) + (float)n3 / 2.0F - DELETE_BUTTON_WIDTH / 2.0F - 10.0F),
            (int)((float)this.getHeight() - this.headerFont.getFontHeight() - 2.0F - (float)this.cornerRadius - 3.0F),
            (int)DELETE_BUTTON_SMALL_WIDTH + 20,
            (int)this.itemFont.getFontHeight() + 6
         );
         double d27 = d26;
         double d28 = d;
         String str14 = "Delete";
         headerFont8.drawString(str14, d28, d27, hoveredColor7);
         GL11.glDisable(3089);
         n14 = this.getWidth();
         int n56 = this.getHeight() - this.cornerRadius;
         int n57 = n56;
         int n58 = n14;
         byte b14 = 0;
         byte b15 = 0;
         if (this.isMouseOver(b15, b14, n58, n57, n, n2)) {
            for (AbstractConfigListItem configListItem4 : this.officialConfigs) {
               configListItem4.renderTooltip();
            }

            for (AbstractConfigListItem configListItem5 : this.userConfigs) {
               configListItem5.renderTooltip();
            }

            for (AbstractConfigListItem configListItem6 : this.friendConfigs) {
               configListItem6.renderTooltip();
            }
         }

         GL11.glPopMatrix();
         GL11.glPopMatrix();
      }
   }

   public ConfigSystemPanel(String str, int n, int n2, int n3, int n4) {
      super(str, n, n2, n3, n4);
      this.userConfigs = new CopyOnWriteArrayList<>();
      this.officialConfigs = new CopyOnWriteArrayList<>();
      this.friendConfigs = new CopyOnWriteArrayList<>();
      this.friendConfigsScrollBar = new SmoothScrollBar();
      this.userConfigsScrollBar = new SmoothScrollBar();
      this.officialConfigsScrollBar = new SmoothScrollBar();
      this.officialConfigsScrollOffset = 0;
      this.userConfigsScrollOffset = 0;
      this.friendConfigsScrollOffset = 0;
      this.shareActionLabel = "ShareToFriends";
      this.itemFont = FontManager.consolas30;
      this.headerFont = FontManager.consolas32;
      this.refreshFuture = null;
      this.initialLoadTriggered = false;
      DirectoryWatcher directoryWatcher = DirectoryWatchService.watch(CONFIGS_DIRECTORY);
      Consumer consumer = obj -> {
         File file = ((Path)obj).toFile();
         this.onLocalConfigFileModified(file);
      };
      directoryWatcher = directoryWatcher.onCreate(consumer);
      Consumer consumer2 = obj2 -> this.onLocalConfigFileDeleted((Path)obj2);
      directoryWatcher.onDelete(consumer2).register();
   }

   @Override
   public void mouseReleased(int mouseX, int mouseY, int state) {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      if (this.isVisible()) {
         super.mouseReleased(mouseX, mouseY, state);
      }
   }

   @Override
   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      if (this.isVisible()) {
         int n = this.getWidth() / 4;
         super.mouseClicked(mouseX, mouseY, mouseButton);
         ScaledResolution sr = new ScaledResolution(mc);
         mouseX *= sr.getScaleFactor();
         mouseY *= sr.getScaleFactor();
         mouseX -= this.getX();
         mouseY -= this.getY();
         if (!Augustus.getInstance().isConfigEditingEnabled()) {
            Augustus.getInstance().getClass();
         } else {
            float f = (float)(n * 3) + (float)n / 2.0F;
            String str = "Create";
            f = (float)((int)(f - this.headerFont.getStringWidth(str) / 2.0F - 10.0F));
            String str2 = "Create";
            if (RenderUtil.isHovered(
               f, 47.0F, (float)((int)this.itemFont.getStringWidth(str2) + 20), (float)((int)this.itemFont.getFontHeight() + 6), mouseX, mouseY
            )) {
               System.out.println("Create");
               ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
               Minecraft mc2 = mc;
               CreateConfigScreen createConfigScreen = new CreateConfigScreen(Augustus.getInstance().getClickGui());
               mc2.displayGuiScreen(createConfigScreen);
            }
         }

         float f2 = (float)(n * 3) + (float)n / 2.0F;
         int n2;
         String str3 = "Load";
         f2 = (float)((int)(f2 - this.headerFont.getStringWidth(str3) / 2.0F - 10.0F));
         int n3;
         String str4 = "Load";
         if (RenderUtil.isHovered(f2, 82.0F, (float)((int)this.itemFont.getStringWidth(str4) + 20), (float)((int)this.itemFont.getFontHeight() + 6), mouseX, mouseY)
            && this.selectedConfig != null) {
            ConfigManager configManager = Augustus.getInstance().getConfigManager();
            configManager.snapshotAll();
            ModuleConfigStorage moduleConfigStorage = Augustus.getInstance().getConfigManager().moduleConfigStorage;
            String configName = this.selectedConfig.getConfigName();
            String str5 = "configs";
            String str6 = configName;
            moduleConfigStorage.loadConfig(str6, str5);
         }

         if (!Augustus.getInstance().isConfigEditingEnabled()) {
            Augustus.getInstance().getClass();
         } else {
            f2 = (float)(n * 3) + (float)n / 2.0F;
            String str7 = "Save";
            f2 = (float)((int)(f2 - this.headerFont.getStringWidth(str7) / 2.0F - 10.0F));
            String str8 = "Save";
            if (RenderUtil.isHovered(
                  f2, 117.0F, (float)((int)this.itemFont.getStringWidth(str8) + 20), (float)((int)this.itemFont.getFontHeight() + 6), mouseX, mouseY
               )
               && this.selectedConfig != null) {
               String configName2 = this.selectedConfig.getConfigName();
               ModuleConfigStorage moduleConfigStorage2 = Augustus.getInstance().getConfigManager().moduleConfigStorage;
               String configName3 = this.selectedConfig.getConfigName();
               boolean isEditable = this.selectedConfig.isEditable();
               String str9 = "configs";
               String str10 = configName3;
               moduleConfigStorage2.saveConfig(str10, str9, isEditable);
               File file = new File(Augustus.getInstance().getConfigManager().moduleConfigStorage.getDirectoryPath() + File.separator + "configs", this.selectedConfig.getConfigName() + ".json");
               this.loadLocalConfigEntry(file);
               this.userConfigs.sort(Comparator.comparing(configListItem -> configListItem.getConfigName().toLowerCase()));
               Stream<AbstractConfigListItem> stream = this.userConfigs.stream();
               String str11 = configName2;
               this.selectedConfig = stream.filter(configListItem2 -> configListItem2.getConfigName().equals(str11)).findFirst().orElse(null);
            }
         }

         f2 = (float)(n * 3) + (float)n / 2.0F;
         String str12 = "Folder";
         f2 = (float)((int)(f2 - this.headerFont.getStringWidth(str12) / 2.0F - 10.0F));
         String str13 = "Folder";
         if (RenderUtil.isHovered(f2, 152.0F, (float)((int)this.itemFont.getStringWidth(str13) + 20), (float)((int)this.itemFont.getFontHeight() + 6), mouseX, mouseY)
            )
          {
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);

            try {
               Desktop.getDesktop().open(new File(new File(ClientPaths.CLIENT_DIR + "/configs").getAbsolutePath()));
            } catch (Exception e) {
               NotificationLevel error = NotificationLevel.ERROR;
               notifyConfigStatus("Error opening folder", error);
            }

            System.out.println("Opend Folder");
         }

         f2 = (float)(n * 3) + (float)n / 2.0F;
         String str14 = "Refresh";
         f2 = (float)((int)(f2 - this.headerFont.getStringWidth(str14) / 2.0F - 10.0F));
         String str15 = "Refresh";
         if (RenderUtil.isHovered(f2, 187.0F, (float)((int)this.itemFont.getStringWidth(str15) + 20), (float)((int)this.itemFont.getFontHeight() + 6), mouseX, mouseY)
            )
          {
            if (this.refreshFuture == null || this.refreshFuture.isDone()) {
               this.refreshFuture = CompletableFuture.runAsync(() -> this.reloadAllConfigs());
            }
            NotificationLevel info = NotificationLevel.INFO;
            notifyConfigStatus("Started searching for new configs...", info);
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
         }

         if (this.shareActionLabel != null) {
            String shareActionLabel2 = this.shareActionLabel;
            float stringWidth = FontManager.consolas32.getStringWidth(shareActionLabel2);
            exitBlock:
            if (this.selectedConfig != null) {
               if (!this.selectedConfig.isEditable()) {
                  AbstractConfigListItem selectedConfig2 = this.selectedConfig;
                  String username = Augustus.getInstance().getUsername();
                  if (!selectedConfig2.isAuthor(username)) {
                     break exitBlock;
                  }
               }

               if (RenderUtil.isHovered(
                  (float)((int)((float)(n * 3) + (float)n / 2.0F - stringWidth / 2.0F - 10.0F)),
                  222.0F,
                  (float)((int)stringWidth + 20),
                  (float)((int)this.itemFont.getFontHeight() + 6),
                  mouseX,
                  mouseY
               )) {
                  String configName4 = this.selectedConfig.getConfigName();
                  if (ConfigNameValidator.isValidConfigName(configName4)) {
                     if (false) {
                        if (this.selectedConfig.isSharedWithFriends()) {
                        } else {
                           boolean isEditable2 = this.selectedConfig.isEditable();
                        }

                        ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
                     } else {
                        ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE6);
                        short s = 26300;
                        NotificationLevel error2 = NotificationLevel.ERROR;
                        String str16 = "Wait until the cooldown is over";
                        notifyConfigStatus(str16, error2);
                     }
                  } else {
                     ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE6);
                     short s2 = 19602;
                     NotificationLevel error3 = NotificationLevel.ERROR;
                     String str17 = "Config name does not qualify for uploading. (Allowed: (a-Z, 0-9)";
                     notifyConfigStatus(str17, error3);
                  }
               }
            }
         }

         f2 = (float)(n * 3) + (float)n / 2.0F;
         String str18 = "Delete";
         f2 = (float)((int)(f2 - this.headerFont.getStringWidth(str18) / 2.0F - 10.0F));
         float f3 = (float)((int)((float)this.getHeight() - this.headerFont.getFontHeight() - 2.0F - (float)this.cornerRadius - 3.0F));
         String str19 = "Delete";
         if (RenderUtil.isHovered(f2, f3, (float)((int)this.itemFont.getStringWidth(str19) + 20), (float)((int)this.itemFont.getFontHeight() + 6), mouseX, mouseY)
            && this.selectedConfig != null) {
            String configName5 = this.selectedConfig.getConfigName();
            if (this.refreshFuture == null || this.refreshFuture.isDone()) {
               this.refreshFuture = CompletableFuture.runAsync(() -> {
                  int[] windowStateFlags3 = ClickGuiWindow.getWindowStateFlags();
                  if (this.selectedConfig != null) {
                     File file2 = new File(ClientPaths.CLIENT_DIR + File.separator + "configs" + File.separator + this.selectedConfig.getConfigName() + ".json");
                     ClientFileUtil.moveToTrashOrDelete(file2);
                     this.selectedConfig = null;
                  }

                  this.refreshOnlineConfigs();
               });
               ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
               String str20 = "Config moved to trash bin: §c" + configName5;
               NotificationLevel error4 = NotificationLevel.ERROR;
               notifyConfigStatus(str20, error4);
               System.out.println("Delete");
            }
         }

         for (AbstractConfigListItem configListItem3 : this.officialConfigs) {
            int n4 = this.getX() + this.cornerRadius;
            n2 = this.getY() + 90;
            int n5 = this.getX() + n - 3;
            n3 = this.getY() + this.getHeight() - this.cornerRadius;
            int n6 = mouseX + this.getX();
            int n7 = mouseY + this.getY();
            int n8 = n7;
            int n9 = n6;
            int n10 = n3;
            int n11 = n5;
            int n12 = n2;
            int n13 = n4;
            if (this.isMouseWithinBounds(n13, n12, n11, n10, n9, n8)) {
               configListItem3.mouseClicked(mouseX, mouseY, mouseButton);
            }
         }

         for (AbstractConfigListItem configListItem4 : this.friendConfigs) {
            int n14 = this.getX() + n * 2 + this.cornerRadius;
            n2 = this.getY() + 90;
            int n15 = this.getX() + n * 3 - 3;
            n3 = this.getY() + this.getHeight() - this.cornerRadius;
            int n16 = mouseX + this.getX();
            int n17 = mouseY + this.getY();
            int n18 = n17;
            int n19 = n16;
            int n20 = n3;
            int n21 = n15;
            int n22 = n2;
            int n23 = n14;
            if (this.isMouseWithinBounds(n23, n22, n21, n20, n19, n18)) {
               configListItem4.mouseClicked(mouseX, mouseY, mouseButton);
            }
         }

         for (AbstractConfigListItem configListItem5 : this.userConfigs) {
            int n24 = this.getX() + n + 3;
            n2 = this.getY() + 90;
            int n25 = this.getX() + n * 2 - 3;
            n3 = this.getY() + this.getHeight() - this.cornerRadius;
            int n26 = mouseX + this.getX();
            int n27 = mouseY + this.getY();
            int n28 = n27;
            int n29 = n26;
            int n30 = n3;
            int n31 = n25;
            int n32 = n2;
            int n33 = n24;
            boolean isMouseWithinBounds = this.isMouseWithinBounds(n33, n32, n31, n30, n29, n28);
            if (isMouseWithinBounds && configListItem5.getFontRenderer() != null) {
               f2 = (float)(configListItem5.getX() - 5);
               float y2 = (float)configListItem5.getY();
               CustomFontUtil fontRenderer = configListItem5.getFontRenderer();
               String configName6 = configListItem5.getConfigName();
               String str21 = configName6;
               if (RenderUtil.isHovered(f2, y2, (float)((int)(fontRenderer.getStringWidth(str21) + 10.0F)), (float)(configListItem5.getHeight() + 2), mouseX, mouseY)
                  && mouseButton == 0) {
                  this.selectedConfig = configListItem5;
               }
            }

            if (isMouseWithinBounds) {
               configListItem5.mouseClicked(mouseX, mouseY, mouseButton);
            }
         }
      }
   }

   private List fetchOnlineConfigItems(int n) {
      List<OnlineConfigMetadata> list = java.util.Collections.emptyList();
      ClickGuiWindow.getWindowStateFlags();
      ArrayList<AbstractConfigListItem> list2 = new ArrayList<>();

      for (OnlineConfigMetadata onlineConfigMetadata : list) {
         AbstractConfigListItem configListItem;
         if (n == 0) {
            configListItem = new OfficialConfigListItem(onlineConfigMetadata.getName(), onlineConfigMetadata.getAuthor(), onlineConfigMetadata.getUpdatedDate(), onlineConfigMetadata.getUpdatedTime(), onlineConfigMetadata.isEditable());
         } else {
            configListItem = new FriendConfigListItem(onlineConfigMetadata.getName(), onlineConfigMetadata.getAuthor(), onlineConfigMetadata.getUpdatedDate(), onlineConfigMetadata.getUpdatedTime(), onlineConfigMetadata.isEditable());
         }

         list2.add(configListItem);
      }

      return list2.stream().sorted(Comparator.comparing(configListItem2 -> configListItem2.getConfigName().toLowerCase())).collect(Collectors.toList());
   }

   private boolean isConfigUploadedOnline(String str) {
      List<OnlineConfigMetadata> list = java.util.Collections.emptyList();
      return list.stream().anyMatch(onlineConfigMetadata -> onlineConfigMetadata.getName().equals(str));
   }

   public ConfigSystemPanel initPanel() {
      super.closeWindow();
      return this;
   }

   private void reloadAllConfigs() {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      this.scanLocalConfigDirectory();
      if (this.selectedConfig != null) {
         this.selectedConfig = this.userConfigs.stream().filter(configListItem -> configListItem.getConfigName().equals(this.selectedConfig.getConfigName())).findFirst().orElse(null);
      }

      this.reloadOnlineConfigs();
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      if (this.isVisible()) {
         super.keyTyped(typedChar, keyCode);
      }
   }

   private void performInitialReload() {
      this.reloadAllConfigs();
   }

   public void onPanelOpened() {
      int[] windowStateFlags2 = ClickGuiWindow.getWindowStateFlags();
      if (!this.initialLoadTriggered) {
         this.refreshFuture = CompletableFuture.runAsync(() -> this.performInitialReload());
         this.initialLoadTriggered = true;
      }

      if (this.refreshFuture == null || this.refreshFuture.isDone()) {
         this.refreshFuture = CompletableFuture.runAsync(() -> this.refreshOnlineConfigs());
      }
   }

}
