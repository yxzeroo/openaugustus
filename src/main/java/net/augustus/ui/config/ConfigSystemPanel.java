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
      this.userConfigs.clear();

      try {
         File[] files = new File(CONFIGS_DIRECTORY).listFiles(file2 -> {
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
      this.removeConfigEntryForFile(path.toFile());
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      if (this.isVisible()) {
         super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      }
   }

   public static void notifyConfigStatus(String str, NotificationLevel notificationLevel) {
      if (mc.theWorld != null) {
         ChatUtil.sendChat3(str);
         if (Augustus.getInstance().getModuleManager().notifications.isToggled() && NotificationCategoryOption.CONFIG_SYSTEM.isSelected()) {
            NotificationManager.pushNotification("Configsystem", str, notificationLevel);
         }
      }
   }

   private synchronized void reloadOnlineConfigs() {
      List list = this.fetchOnlineConfigItems((byte)0);
      List list2 = this.fetchOnlineConfigItems((byte)2);
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

      for (OnlineConfigMetadata onlineConfigMetadata : list) {
         String name = onlineConfigMetadata.getName();
         if (!this.userConfigs.stream().anyMatch(configListItem -> configListItem.getConfigName().equals(name))) {
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
               notifyConfigStatus("Failed to load config: " + file.getName(), NotificationLevel.ERROR);
            }
         }
      }
   }

   @Override
   public void drawWindow(int n, int n2, float f) {
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
         int colorInt = ClientManagers.moduleManager.clickGui.headerColor.getColorInt((byte)0);
         RenderUtil.drawRect3(n3 - 1, 30, n3 + 1, this.getHeight(), colorInt);
         RenderUtil.drawRect3(n3 * 2 - 1, 30, n3 * 2 + 1, this.getHeight(), colorInt);
         RenderUtil.drawRect3(n3 * 3 - 1, 30, n3 * 3 + 1, this.getHeight(), colorInt);
         RenderUtil.drawRect3(0, 82, n3 * 3, 84, colorInt);
         if (!this.isFullScreen() && ClientManagers.moduleManager.clickGui.outline.getValue()) {
            float width2 = (float)this.getWidth();
            float height2 = (float)this.getHeight();
            float f2 = this.isFullScreen() ? 0.0F : (float)this.cornerRadius;
            RenderUtil.drawRoundedRectOutline(0.0F, 0.0F, width2, height2, f2, 1, 1, ClientManagers.moduleManager.clickGui.outlineColor.getColorInt((byte)0));
         }

         int colorInt2 = ClientManagers.moduleManager.clickGui.textColor.getColorInt((byte)0);
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
            int n7 = (int)(f3 - itemFont2.getStringWidth(configName) / 2.0F);
            int n8 = 90 + n4 + this.officialConfigsScrollOffset;
            n6 = (int)this.itemFont.getFontHeight();
            configListItem.renderItem(n7, n8, n5, n6, n, n2, this.itemFont, false);
            n4 += 25;
         }

         SmoothScrollBar smoothScrollBar = this.officialConfigsScrollBar;
         int n12 = n3 - 15;
         int n13 = n3 - 5;
         int n14 = this.getHeight() - this.cornerRadius;
         smoothScrollBar.drawVerticalBar(n12, (byte)90, n13, n14, 90 - this.officialConfigsScrollOffset, n4, colorInt);
         SmoothScrollBar smoothScrollBar2 = this.officialConfigsScrollBar;
         n14 = this.getHeight() - this.cornerRadius;
         int n19 = this.officialConfigsScrollOffset;
         this.officialConfigsScrollOffset = smoothScrollBar2.updateScrollOffset(n4, (byte)90, n14, n19, (byte)0, (byte)90, n3, this.getHeight() - this.cornerRadius - 90, n, n2);
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
            int n25 = (int)(((float)n3 + (float)n24 / 2.0F) - this.itemFont.getStringWidth(configListItem2.getConfigName()) / 2.0F);
            int n26 = 90 + n23 + this.userConfigsScrollOffset;
            n14 = (int)this.itemFont.getFontHeight();
            boolean flag2 = this.selectedConfig != null ? configListItem2.equals(this.selectedConfig) : false;
            configListItem2.renderItem(n25, n26, n24, n14, n, n2, this.itemFont, flag2);
            n23 += 25;
         }

         smoothScrollBar = this.userConfigsScrollBar;
         int n30 = n3 * 2 - 15;
         n13 = n3 * 2 - 5;
         n14 = this.getHeight() - this.cornerRadius;
         smoothScrollBar.drawVerticalBar(n30, (byte)90, n13, n14, 90 - this.userConfigsScrollOffset, n23, colorInt);
         SmoothScrollBar userConfigsScrollBar2 = this.userConfigsScrollBar;
         n14 = this.getHeight() - this.cornerRadius;
         n19 = this.userConfigsScrollOffset;
         this.userConfigsScrollOffset = userConfigsScrollBar2.updateScrollOffset(n23, (byte)90, n14, n19, n3, (byte)90, n3, this.getHeight() - this.cornerRadius - 90, n, n2);
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
            int n40 = (int)(((float)(n3 * 2) + (float)n39 / 2.0F) - this.itemFont.getStringWidth(configListItem3.getConfigName()) / 2.0F);
            int n41 = 90 + n38 + this.friendConfigsScrollOffset;
            n14 = (int)this.itemFont.getFontHeight();
            boolean flag3 = this.selectedConfig != null ? configListItem3.equals(this.selectedConfig) : false;
            configListItem3.renderItem(n40, n41, n39, n14, n, n2, this.itemFont, flag3);
            n38 += 25;
         }

         smoothScrollBar = this.friendConfigsScrollBar;
         int n45 = n3 * 3 - 15;
         n13 = n3 * 3 - 5;
         n14 = this.getHeight() - this.cornerRadius;
         smoothScrollBar.drawVerticalBar(n45, (byte)90, n13, n14, 90 - this.friendConfigsScrollOffset, n38, colorInt);
         SmoothScrollBar friendConfigsScrollBar2 = this.friendConfigsScrollBar;
         n14 = this.getHeight() - this.cornerRadius;
         n19 = this.friendConfigsScrollOffset;
         this.friendConfigsScrollOffset = friendConfigsScrollBar2.updateScrollOffset(n38, (byte)90, n14, n19, n3 * 2, (byte)90, n3, this.getHeight() - this.cornerRadius - 90, n, n2);
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
         double d10 = d;
         headerFont2.drawString("Create", d10, d8, hoveredColor);
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
         double d13 = d;
         headerFont3.drawString("Load", d13, d11, hoveredColor2);
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
         double d16 = d;
         headerFont4.drawString("Save", d16, d14, hoveredColor3);
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
         double d19 = d;
         headerFont5.drawString("Folder", d19, d17, hoveredColor4);
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
         double d22 = d;
         headerFont6.drawString("Refresh", d22, d20, hoveredColor5);
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
            headerFont7.drawString(shareActionLabel3, d, d23, hoveredColor6);
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
         double d28 = d;
         headerFont8.drawString("Delete", d28, d26, hoveredColor7);
         GL11.glDisable(3089);
         n14 = this.getWidth();
         if (this.isMouseOver((byte)0, (byte)0, n14, this.getHeight() - this.cornerRadius, n, n2)) {
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
         this.onLocalConfigFileModified(((Path)obj).toFile());
      };
      directoryWatcher = directoryWatcher.onCreate(consumer);
      directoryWatcher.onDelete(obj2 -> this.onLocalConfigFileDeleted((Path)obj2)).register();
   }

   @Override
   public void mouseReleased(int mouseX, int mouseY, int state) {
      if (this.isVisible()) {
         super.mouseReleased(mouseX, mouseY, state);
      }
   }

   @Override
   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
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
               mc.displayGuiScreen(new CreateConfigScreen(Augustus.getInstance().getClickGui()));
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
            Augustus.getInstance().getConfigManager().snapshotAll();
            Augustus.getInstance().getConfigManager().moduleConfigStorage.loadConfig(this.selectedConfig.getConfigName(), "configs");
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
               moduleConfigStorage2.saveConfig(configName3, "configs", isEditable);
               this.loadLocalConfigEntry(new File(Augustus.getInstance().getConfigManager().moduleConfigStorage.getDirectoryPath() + File.separator + "configs", this.selectedConfig.getConfigName() + ".json"));
               this.userConfigs.sort(Comparator.comparing(configListItem -> configListItem.getConfigName().toLowerCase()));
               this.selectedConfig = this.userConfigs.stream().filter(configListItem2 -> configListItem2.getConfigName().equals(configName2)).findFirst().orElse(null);
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
                  if (!this.selectedConfig.isAuthor(Augustus.getInstance().getUsername())) {
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
                  if (ConfigNameValidator.isValidConfigName(this.selectedConfig.getConfigName())) {
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
                        notifyConfigStatus("Wait until the cooldown is over", error2);
                     }
                  } else {
                     ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE6);
                     short s2 = 19602;
                     NotificationLevel error3 = NotificationLevel.ERROR;
                     notifyConfigStatus("Config name does not qualify for uploading. (Allowed: (a-Z, 0-9)", error3);
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
                  if (this.selectedConfig != null) {
                     ClientFileUtil.moveToTrashOrDelete(new File(ClientPaths.CLIENT_DIR + File.separator + "configs" + File.separator + this.selectedConfig.getConfigName() + ".json"));
                     this.selectedConfig = null;
                  }

                  this.refreshOnlineConfigs();
               });
               ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE2);
               notifyConfigStatus("Config moved to trash bin: §c" + configName5, NotificationLevel.ERROR);
               System.out.println("Delete");
            }
         }

         for (AbstractConfigListItem configListItem3 : this.officialConfigs) {
            int n4 = this.getX() + this.cornerRadius;
            n2 = this.getY() + 90;
            int n5 = this.getX() + n - 3;
            n3 = this.getY() + this.getHeight() - this.cornerRadius;
            if (this.isMouseWithinBounds(n4, n2, n5, n3, mouseX + this.getX(), mouseY + this.getY())) {
               configListItem3.mouseClicked(mouseX, mouseY, mouseButton);
            }
         }

         for (AbstractConfigListItem configListItem4 : this.friendConfigs) {
            int n14 = this.getX() + n * 2 + this.cornerRadius;
            n2 = this.getY() + 90;
            int n15 = this.getX() + n * 3 - 3;
            n3 = this.getY() + this.getHeight() - this.cornerRadius;
            if (this.isMouseWithinBounds(n14, n2, n15, n3, mouseX + this.getX(), mouseY + this.getY())) {
               configListItem4.mouseClicked(mouseX, mouseY, mouseButton);
            }
         }

         for (AbstractConfigListItem configListItem5 : this.userConfigs) {
            int n24 = this.getX() + n + 3;
            n2 = this.getY() + 90;
            int n25 = this.getX() + n * 2 - 3;
            n3 = this.getY() + this.getHeight() - this.cornerRadius;
            boolean isMouseWithinBounds = this.isMouseWithinBounds(n24, n2, n25, n3, mouseX + this.getX(), mouseY + this.getY());
            if (isMouseWithinBounds && configListItem5.getFontRenderer() != null) {
               f2 = (float)(configListItem5.getX() - 5);
               if (RenderUtil.isHovered(f2, (float)configListItem5.getY(), (float)((int)(configListItem5.getFontRenderer().getStringWidth(configListItem5.getConfigName()) + 10.0F)), (float)(configListItem5.getHeight() + 2), mouseX, mouseY)
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
      this.scanLocalConfigDirectory();
      if (this.selectedConfig != null) {
         this.selectedConfig = this.userConfigs.stream().filter(configListItem -> configListItem.getConfigName().equals(this.selectedConfig.getConfigName())).findFirst().orElse(null);
      }

      this.reloadOnlineConfigs();
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      if (this.isVisible()) {
         super.keyTyped(typedChar, keyCode);
      }
   }

   private void performInitialReload() {
      this.reloadAllConfigs();
   }

   public void onPanelOpened() {
      if (!this.initialLoadTriggered) {
         this.refreshFuture = CompletableFuture.runAsync(() -> this.performInitialReload());
         this.initialLoadTriggered = true;
      }

      if (this.refreshFuture == null || this.refreshFuture.isDone()) {
         this.refreshFuture = CompletableFuture.runAsync(() -> this.refreshOnlineConfigs());
      }
   }

}
