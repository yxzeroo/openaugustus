package net.augustus.modules.render;

import java.awt.Color;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.font.CustomFontUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.SettingNameManager;
import net.augustus.events.listeners.DownloadTerrainListener;
import net.augustus.config.ConfigManager;
import net.augustus.Augustus;
import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.settings.SettingChangeListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingDescriptionManager;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;

public class ClickGui extends Module implements DownloadTerrainListener, TickListener {
   public final BooleansSetting sorting;
   public BooleanValue germanMode;
   public final BooleanValue openInInventory;
   public final BlurShadowSettings clickGui;
   public final BooleanValue outline;
   public final StringValue alpha;
   public final StringValue dynamicKeyTime;
   public final BooleansSetting descriptions;
   public final BooleansSetting settingNames;
   public final BooleanValue moduleDescriptions;
   public final BooleanValue settingDescriptions;
   public final DoubleValue colors;
   public final ColorSetting backgroundColor;
   public final ColorSetting outlineColor;
   public final ColorSetting headerColor;
   public final ColorSetting textColor;
   public final ColorSetting hoverTextColor;
   public final ColorSetting enabledColor;
   public final ColorSetting disabledColor;

   public ClickGui() {
       super("ClickGui", "KlickbareGrafischeBenutzeroberfläche", Color.BLACK, Categorys.RENDER);
      BooleansSetting booleansSetting = new BooleansSetting(this, "Sorting", "Alphabet", new String[]{"Random", "Length", "Alphabet"});
      SettingChangeListener listener = () -> this.sortCategoryModules();
      SettingChangeListener listener2 = listener;
      this.sorting = (BooleansSetting)booleansSetting.withChangeListener(listener2);
      BooleanValue booleanValue = new BooleanValue(this, "GermanMode", false);
      listener = () -> {

         for (Module module : (Iterable<Module>)(ClientManagers.moduleManager.getModules())) {
            String str = this.germanMode.getValue() ? module.getGermanName() : module.getName();
            module.setName(str);
         }
      };
      SettingChangeListener listener3 = listener;
      this.germanMode = (BooleanValue)booleanValue.withChangeListener(listener3);
      this.openInInventory = new BooleanValue(this, "OpenInInventory", true);
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, "ClickGui");
      Color black = Color.black;
      this.clickGui = blurShadowSettings.withCustomShadowColor(black);
      this.outline = new BooleanValue(this, "Outline", true);
      this.alpha = new StringValue(this, "Alpha", 200.0, 0.0, 255.0, 0);
      this.dynamicKeyTime = new StringValue(this, "DynamicKeyTime", 500.0, 0.0, 2000.0, 0);
      BooleansSetting booleansSetting2 = new BooleansSetting(this, "Descriptions", "Default", new String[]{"Default"});
      listener = () -> this.reloadSettingDescriptionsAsync();
      SettingChangeListener listener4 = listener;
      this.descriptions = (BooleansSetting)booleansSetting2.withChangeListener(listener4);
      BooleansSetting booleansSetting3 = new BooleansSetting(this, "SettingNames", "English_Default", new String[]{"English_Default", "English_New"});
      listener = () -> {
         CompletableFuture.runAsync(() -> this.reloadSettingNames());
      };
      SettingChangeListener listener5 = listener;
      this.settingNames = (BooleansSetting)booleansSetting3.withChangeListener(listener5);
      this.moduleDescriptions = new BooleanValue(this, "ModuleDescriptions", true);
      this.settingDescriptions = new BooleanValue(this, "SettingDescriptions", true);
      this.colors = new DoubleValue(this, "Colors");
      Color color2 = new Color(40, 39, 39, 200);
      DoubleValue colors2 = this.colors;
      Color color3 = color2;
      String str2 = "BackgroundColor";
      this.backgroundColor = new ColorSetting(this, str2, color3, colors2);
      color2 = new Color(40, 39, 39);
      colors2 = this.colors;
      color3 = color2;
      str2 = "OutlineColor";
      this.outlineColor = new ColorSetting(this, str2, color3, colors2);
      color2 = new Color(34, 34, 34);
      colors2 = this.colors;
      color3 = color2;
      str2 = "HeaderColor";
      this.headerColor = new ColorSetting(this, str2, color3, colors2);
      color2 = new Color(185, 187, 190);
      colors2 = this.colors;
      color3 = color2;
      str2 = "TextColor";
      this.textColor = new ColorSetting(this, str2, color3, colors2);
      color2 = new Color(41, 146, 222);
      colors2 = this.colors;
      color3 = color2;
      str2 = "TextColorHover";
      ColorSetting colorSetting = new ColorSetting(this, str2, color3, colors2);
      boolean flag = true;
      ColorSetting colorSetting2 = colorSetting.withClientColorDefault(flag);
      short s = 255;
      this.hoverTextColor = colorSetting2.withDefaultClientColorAlpha(s);
      color2 = new Color(0, 214, 0);
      colors2 = this.colors;
      color3 = color2;
      str2 = "TrueColor";
      this.enabledColor = new ColorSetting(this, str2, color3, colors2);
      color2 = new Color(214, 0, 0);
      colors2 = this.colors;
      color3 = color2;
      str2 = "FalseColor";
      this.disabledColor = new ColorSetting(this, str2, color3, colors2);
      byte b = 54;
      this.setKeyCode(b);
      boolean flag2 = false;
      this.setToggleSoundEnabled(flag2);
   }

   @Override
   public void onEnable() {
      if (mc.currentScreen instanceof GuiContainer && !this.openInInventory.getValue()) {
         this.toggle();
      } else {
         ConfigManager configManager = Augustus.getInstance().getConfigManager();
         configManager.snapshotAll();
         Augustus.getInstance().getDiscordRpcManager().start();
         if (mc.thePlayer != null && mc.theWorld != null) {
            if (mc.currentScreen != null) {
               mc.thePlayer.closeScreen();
            }

            Minecraft mc2 = mc;
            ClickGuiScreen clickGui2 = Augustus.getInstance().getClickGui();
            mc2.displayGuiScreen(clickGui2);
         }
      }
   }

   @Override
   public void onDisable() {
      ConfigManager configManager = Augustus.getInstance().getConfigManager();
      configManager.snapshotAll();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 8;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onTick2() {
      if (!(mc.currentScreen instanceof ClickGuiScreen) && this.isToggled()) {
         this.toggle();
      }
   }

   @Override
   public void onDownloadTerrain() {

      for (Module module : (Iterable<Module>)(ClientManagers.moduleManager.getModules())) {
         String str = this.germanMode.getValue() ? module.getGermanName() : module.getName();
         module.setName(str);
      }
   }

   private void sortCategoryModules() {
      String selectedValue = this.sorting.getSelectedValue();
      switch (selectedValue) {
         case "Random":
            for (Categorys categorys2 : Categorys.values()) {
               if (!categorys2.hidden) {
                  categorys2.getModules().clear();
                  categorys2.getModules().addAll(categorys2.getDisplayModules());
               }
            }
            break;
         case "Alphabet":
            for (Categorys categorys3 : Categorys.values()) {
               if (!categorys3.hidden) {
                  categorys3.getModules().sort(Comparator.comparing(Module::getName2));
               }
            }
            break;
         case "Length":
            for (Categorys categorys4 : Categorys.values()) {
               if (!categorys4.hidden) {
                  categorys4.getModules().sort(Comparator.comparingDouble(obj -> getModuleNameLengthSortKey((Module)obj)));
               }
            }
      }
   }

   private static double getModuleNameLengthSortKey(Module module) {
      CustomFontUtil moduleFont = Augustus.getInstance().getClickGui().getMainWindow().getModuleFont();
      String name2 = module.getName2();
      String str = name2;
      return (double)(1000.0F - moduleFont.getStringWidth(str));
   }

   private void reloadSettingNames() {
      SettingNameManager settingNameManager = Augustus.getInstance().getSettingNameManager();
      String selectedValue = this.settingNames.getSelectedValue();
      settingNameManager.loadLanguageFile(selectedValue);
   }

   private void reloadSettingDescriptionsAsync() {
      CompletableFuture.runAsync(() -> this.reloadSettingDescriptions());
   }

   private void reloadSettingDescriptions() {
      SettingDescriptionManager manager = Augustus.getInstance().getSettingDescriptionManager();
      String selectedValue = this.descriptions.getSelectedValue();
      String str = selectedValue;
      manager.loadDescriptions(str);
   }




}
