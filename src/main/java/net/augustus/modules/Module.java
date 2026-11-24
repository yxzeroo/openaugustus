package net.augustus.modules;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;
import net.augustus.modules.misc.AugustusServer;
import net.augustus.modules.misc.RotationRecorder;
import net.augustus.modules.misc.RotationRenderer;
import net.augustus.modules.render.ClickGui;
import net.augustus.ui.notifications.NotificationLevel;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.font.CustomFontUtil;
import net.augustus.ClientManagers;
import net.augustus.utils.ChatUtil;
import net.augustus.settings.property.BooleanProperty;
import net.augustus.ui.notifications.NotificationManager;
import net.augustus.font.FontManager;
import net.augustus.settings.property.IntegerProperty;
import net.augustus.utils.render.ColorUtil;
import net.augustus.Augustus;
import net.augustus.events.EventManager;
import net.augustus.settings.options.NotificationCategoryOption;
import net.augustus.config.ModuleSettingsConfigSection;
import net.augustus.utils.animation.EasingType;
import net.augustus.utils.animation.AnimationUtil;
import net.augustus.settings.BooleanValue;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketThreadUtil;

public abstract class Module extends AbstractFeature implements MC, Comparable {
   public final BooleanProperty mT;
   private final BooleanProperty mC;
   private final BooleanProperty mA;
   private final BooleanProperty mS;
   private final BooleanProperty mKm;
   private final IntegerProperty mK;
   private String name;
   private String displayName;
   private String germanName;
   private String description;
   private Categorys categorys;
   private Color color;
   private final ArrayList settings;
   private final ArrayList toggleListeners;
   private CustomFontUtil guiFont;
   private String formattedSuffix;
   private String rawSuffix;
   private int scrollOffsetX;
   private int scrollOffsetY;
   private double fullDisplayWidth;
   private double displayHeight;
   public AnimationUtil toggleAnimation;
   public boolean alwaysEnabled;
   private double nameWidth;
   private long sessionExpireTimeMs;
   private long createdTimeMs;
   private long lastToggleTimeMs;
   private boolean toggledByKey;
   private boolean hiddenInGui;
   private boolean hiddenFromConfig;
   private String lastSuffixCaseMode;
   private int guiX;
   private int guiY;
   private int guiHeight;
   private boolean bindingKey;
   private int keyBindX;
   private int keyBindY;
   private int keyBindWidth;
   private int keyBindHeight;
   private final BooleanValue loadFromConfig;
   private final BooleanValue showInArrayList;
   private final BooleanValue toggleSound;
   private final BooleanValue keyHoldMode;

   public Module(String str) {
       super(null, str);
      BooleanProperty booleanProperty = new BooleanProperty(this, "M_T", false);
      Consumer<Boolean> consumer = bool -> this.onTogglePropertyChanged(bool);
      this.mT = (BooleanProperty)booleanProperty.withOnValueChanged(consumer);
      BooleanProperty booleanProperty2 = new BooleanProperty(this, "M_C", true);
      Consumer<Boolean> consumer2 = bool2 -> this.setLoadFromConfig(bool2);
      this.mC = (BooleanProperty)booleanProperty2.withOnValueChanged(consumer2);
      BooleanProperty booleanProperty3 = new BooleanProperty(this, "M_A", true);
      Consumer<Boolean> consumer3 = bool3 -> this.setShowInArrayList(bool3);
      this.mA = (BooleanProperty)booleanProperty3.withOnValueChanged(consumer3);
      BooleanProperty booleanProperty4 = new BooleanProperty(this, "M_S", true);
      Consumer<Boolean> consumer4 = bool4 -> this.setToggleSoundEnabled(bool4);
      this.mS = (BooleanProperty)booleanProperty4.withOnValueChanged(consumer4);
      BooleanProperty booleanProperty5 = new BooleanProperty(this, "M_KM", false);
      Consumer<Boolean> consumer5 = bool5 -> this.setKeyHoldMode(bool5);
      this.mKm = (BooleanProperty)booleanProperty5.withOnValueChanged(consumer5);
      this.mK = new IntegerProperty(this, "M_K", 0);
      this.germanName = "";
      this.description = "";
      this.settings = new ArrayList();
      this.toggleListeners = new ArrayList();
      this.guiFont = FontManager.consolas32;
      this.formattedSuffix = "";
      this.rawSuffix = "";
      this.scrollOffsetX = 0;
      this.scrollOffsetY = 0;
      this.fullDisplayWidth = 0.0;
      this.displayHeight = 0.0;
      this.toggleAnimation = new AnimationUtil(0.0F, 0.0F, 0.0F, 0.0F);
      this.nameWidth = 0.0;
      this.lastToggleTimeMs = System.currentTimeMillis();
      this.toggledByKey = false;
      this.hiddenInGui = false;
      this.hiddenFromConfig = false;
      this.lastSuffixCaseMode = "";
      this.guiHeight = (int)this.guiFont.getFontHeight();
      this.loadFromConfig = new BooleanValue("LoadFromConfig", true);
      this.showInArrayList = new BooleanValue("ShowInArrayList", true);
      this.toggleSound = new BooleanValue("ToggleSound", true);
      this.keyHoldMode = new BooleanValue("KeyHoldMode", false);
      this.name = str;
      this.displayName = str;
      this.germanName = str;
      this.color = Color.GREEN;
      this.createdTimeMs = this.lastToggleTimeMs;
      this.categorys = Categorys.LEGIT;
      this.setLoadFromConfig(true);
      this.alwaysEnabled = false;
      this.sessionExpireTimeMs = this.lastToggleTimeMs + 172800000L;
   }

   public Module(String str, String str2, Color color2, Categorys categorys2) {
       super(ModuleSettingsConfigSection.abstractFeature, str);
      BooleanProperty booleanProperty = new BooleanProperty(this, "M_T", false);
      Consumer<Boolean> consumer = bool -> this.onTogglePropertyChanged(bool);
      this.mT = (BooleanProperty)booleanProperty.withOnValueChanged(consumer);
      BooleanProperty booleanProperty2 = new BooleanProperty(this, "M_C", true);
      Consumer<Boolean> consumer2 = bool2 -> this.setLoadFromConfig(bool2);
      this.mC = (BooleanProperty)booleanProperty2.withOnValueChanged(consumer2);
      BooleanProperty booleanProperty3 = new BooleanProperty(this, "M_A", true);
      Consumer<Boolean> consumer3 = bool3 -> this.setShowInArrayList(bool3);
      this.mA = (BooleanProperty)booleanProperty3.withOnValueChanged(consumer3);
      BooleanProperty booleanProperty4 = new BooleanProperty(this, "M_S", true);
      Consumer<Boolean> consumer4 = bool4 -> this.setToggleSoundEnabled(bool4);
      this.mS = (BooleanProperty)booleanProperty4.withOnValueChanged(consumer4);
      BooleanProperty booleanProperty5 = new BooleanProperty(this, "M_KM", false);
      Consumer<Boolean> consumer5 = bool5 -> this.setKeyHoldMode(bool5);
      this.mKm = (BooleanProperty)booleanProperty5.withOnValueChanged(consumer5);
      this.mK = new IntegerProperty(this, "M_K", 0);
      this.germanName = "";
      this.description = "";
      this.settings = new ArrayList();
      this.toggleListeners = new ArrayList();
      this.guiFont = FontManager.consolas32;
      this.formattedSuffix = "";
      this.rawSuffix = "";
      this.scrollOffsetX = 0;
      this.scrollOffsetY = 0;
      this.fullDisplayWidth = 0.0;
      this.displayHeight = 0.0;
      this.toggleAnimation = new AnimationUtil(0.0F, 0.0F, 0.0F, 0.0F);
      this.nameWidth = 0.0;
      this.lastToggleTimeMs = System.currentTimeMillis();
      this.toggledByKey = false;
      this.hiddenInGui = false;
      this.hiddenFromConfig = false;
      this.lastSuffixCaseMode = "";
      this.guiHeight = (int)this.guiFont.getFontHeight();
      this.loadFromConfig = new BooleanValue("LoadFromConfig", true);
      this.showInArrayList = new BooleanValue("ShowInArrayList", true);
      this.toggleSound = new BooleanValue("ToggleSound", true);
      this.keyHoldMode = new BooleanValue("KeyHoldMode", false);
      this.name = str;
      this.displayName = str;
      this.germanName = str2;
      this.createdTimeMs = this.lastToggleTimeMs;
      this.color = color2;
      this.categorys = categorys2;
      this.sessionExpireTimeMs = this.lastToggleTimeMs + 171000000L;
      this.setLoadFromConfig(this.categorys != Categorys.RENDER);
      Augustus.getInstance().getModuleRegistry().getModules().add(this);
      this.alwaysEnabled = false;
      this.registerInCategory();
   }

   public Module(String str, String str2, boolean flag, Color color2, Categorys categorys2) {
       super(ModuleSettingsConfigSection.abstractFeature, str);
      BooleanProperty booleanProperty = new BooleanProperty(this, "M_T", false);
      Consumer<Boolean> consumer = bool -> this.onTogglePropertyChanged(bool);
      this.mT = (BooleanProperty)booleanProperty.withOnValueChanged(consumer);
      BooleanProperty booleanProperty2 = new BooleanProperty(this, "M_C", true);
      Consumer<Boolean> consumer2 = bool2 -> this.setLoadFromConfig(bool2);
      this.mC = (BooleanProperty)booleanProperty2.withOnValueChanged(consumer2);
      booleanProperty2 = new BooleanProperty(this, "M_A", true);
      Consumer<Boolean> consumer3 = bool3 -> this.setShowInArrayList(bool3);
      this.mA = (BooleanProperty)booleanProperty2.withOnValueChanged(consumer3);
      BooleanProperty booleanProperty3 = new BooleanProperty(this, "M_S", true);
      Consumer<Boolean> consumer4 = bool4 -> this.setToggleSoundEnabled(bool4);
      this.mS = (BooleanProperty)booleanProperty3.withOnValueChanged(consumer4);
      BooleanProperty booleanProperty4 = new BooleanProperty(this, "M_KM", false);
      Consumer<Boolean> consumer5 = bool5 -> this.setKeyHoldMode(bool5);
      this.mKm = (BooleanProperty)booleanProperty4.withOnValueChanged(consumer5);
      this.mK = new IntegerProperty(this, "M_K", 0);
      this.germanName = "";
      this.description = "";
      this.settings = new ArrayList();
      this.toggleListeners = new ArrayList();
      this.guiFont = FontManager.consolas32;
      this.formattedSuffix = "";
      this.rawSuffix = "";
      this.scrollOffsetX = 0;
      this.scrollOffsetY = 0;
      this.fullDisplayWidth = 0.0;
      this.displayHeight = 0.0;
      this.toggleAnimation = new AnimationUtil(0.0F, 0.0F, 0.0F, 0.0F);
      this.nameWidth = 0.0;
      this.lastToggleTimeMs = System.currentTimeMillis();
      this.toggledByKey = false;
      this.hiddenInGui = false;
      this.hiddenFromConfig = false;
      this.lastSuffixCaseMode = "";
      this.guiHeight = (int)this.guiFont.getFontHeight();
      this.loadFromConfig = new BooleanValue("LoadFromConfig", true);
      this.showInArrayList = new BooleanValue("ShowInArrayList", true);
      this.toggleSound = new BooleanValue("ToggleSound", true);
      this.keyHoldMode = new BooleanValue("KeyHoldMode", false);
      this.name = str;
      this.germanName = str2;
      this.sessionExpireTimeMs = this.lastToggleTimeMs + 172800000L;
      this.displayName = str;
      this.color = color2;
      this.categorys = categorys2;
      this.setLoadFromConfig(this.categorys != Categorys.RENDER);
      this.setShowInArrayList(false);
      Augustus.getInstance().getModuleRegistry().getModules().add(this);
      this.alwaysEnabled = true;
      this.createdTimeMs = this.lastToggleTimeMs;
      this.registerInCategory();
   }

   private void registerInCategory() {
      Augustus.getInstance().getClass();
      if (!(this instanceof RotationRecorder) && !(this instanceof RotationRenderer)) {
         this.categorys.getModules().add(this);
         this.categorys.getDisplayModules().add(this);
      }
   }

   public void onClientInit() {
   }

   public void toggle() {
      this.toggle(true);
   }

   public void toggle(boolean flag) {
      if (this.mT.isLocked()) {
         return;
      }


      if (this.alwaysEnabled) {
         this.mT.setValue(Boolean.valueOf(false));
         this.updateEventSubscriptions();
         this.toggleListeners.forEach(var -> notifyAlwaysEnabledListener((ModuleToggleListener)var));
         this.mT.setValue(Boolean.valueOf(true));
         return;
      }

      if (!this.isToggled()) {
         this.lastToggleTimeMs = System.currentTimeMillis();
         this.toggledByKey = true;
      }

      Augustus.getInstance().getClass();
      if (this.lastToggleTimeMs < this.sessionExpireTimeMs) {
         this.updateEventSubscriptions();
      }

      this.toggleListeners.forEach(var -> this.notifyToggleListener((ModuleToggleListener)var));

      int n = 100;
      EasingType easeInOutBack = EasingType.easeInOutBack;
      if (Augustus.getInstance().getModuleManager() != null) {
         easeInOutBack = AnimationUtil.parseEasingType(Augustus.getInstance().getModuleManager().arrayList.toggleAnimation2.getSelectedValue());
         n = (int)Augustus.getInstance().getModuleManager().arrayList.animationTime.getValue();
      }

      if (this.isToggled()) {
         this.mT.setValue(Boolean.valueOf(false));
         if (mc.theWorld != null
            && Augustus.getInstance().getModuleManager().augustusServer.moduleToggleSound.getValue()
            && this.isToggleSoundEnabled()) {
            PacketThreadUtil.runOnMainThread(() -> AugustusServer.playModuleDisableSound());
         }

         this.onDisable();
         this.toggleAnimation.start(n, easeInOutBack, 0, false);
      } else {
         this.mT.setValue(Boolean.valueOf(true));
         if (mc.theWorld != null
            && Augustus.getInstance().getModuleManager().augustusServer.moduleToggleSound.getValue()
            && this.isToggleSoundEnabled()) {
            PacketThreadUtil.runOnMainThread(() -> AugustusServer.playModuleEnableSound());
         }

         this.onEnable();
         this.toggleAnimation.start(n, easeInOutBack, 1, false);
      }

      if (mc.thePlayer != null && Augustus.getInstance().getModuleManager().oldNotification.isToggled()) {
         Augustus.getInstance().getModuleManager().oldNotification.pushModuleToggleNotification(this);
      }

      if (mc.thePlayer != null
         && !(this instanceof ClickGui)
         && Augustus.getInstance().getModuleManager().notifications != null
         && Augustus.getInstance().getModuleManager().notifications.isToggled()
         && NotificationCategoryOption.MODULE_TOGGLES.isSelected()) {
         String str = this.isToggled() ? "§aenabled" : "§cdisabled";
         NotificationManager.pushNotification(
            this.getName2(),
            str,
            NotificationLevel.INFO
         );
      }

      if (flag) {
         Augustus.getInstance().getModuleManager().sortModules();
      }
   }

   public abstract void onEnable();

   public abstract void onDisable();

   public abstract void updateEventSubscriptions();

   public void toggleEventSubscription(int n, int n2) {
      boolean flag = Categorys.isControlFlowFlagCleared();
      if (this.isToggled()) {
         EventManager.getEventBus().unsubscribe(n, this);
         if (!flag) {
            return;
         }
      }

      EventManager.getEventBus().subscribe(n, this, n2);
   }

   public void toggleEventSubscription(int n) {
      this.toggleEventSubscription(n, (byte)0);
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color2) {
      this.color = color2;
   }

   public String getName2() {
      return this.name;
   }

   public void setName(String str) {
      this.name = str;
      this.updateDisplayName();
   }

   public String getFullDisplayName() {
      return this.displayName + this.formattedSuffix;
   }

   public String getFormattedSuffix() {
      return this.formattedSuffix;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void updateDisplayName() {
      String str = this.formatSpacedName(this.name);
      switch (ClientManagers.moduleManager.arrayList.casing.getSelectedValue()) {
         case "Default":
            this.displayName = str;
            break;
         case "UpperCase":
            this.displayName = str.toUpperCase(Locale.ROOT);
            break;
         case "LowerCase":
            this.displayName = str.toLowerCase(Locale.ROOT);
      }

      if (!this.rawSuffix.equals("")) {
         this.setSuffix(this.rawSuffix);
      }

      if (ClientManagers.moduleManager.arrayList.font.getSelectedValue().equals("Minecraft")) {
         this.nameWidth = ClientManagers.moduleManager.arrayList.bold.getValue() ? mc.fontRendererObj.getStringWidthFloat("§l" + this.displayName) : mc.fontRendererObj.getStringWidthFloat(this.displayName);
      } else {
         this.nameWidth = (double)ClientManagers.moduleManager.arrayList.getCustomFont().getStringWidth(this.displayName);
      }
   }

   private String formatSpacedName(String str) {
      if (!ClientManagers.moduleManager.arrayList.spacing.getValue()) {
         return str;
      } else {
         StringBuilder sb = new StringBuilder();

         for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (i != 0 && Character.isUpperCase(ch) && i + 1 < str.length() && Character.isLowerCase(str.charAt(i + 1))) {
               sb.append(" ");
            }

            sb.append(ch);
         }

         return sb.toString();
      }
   }

   public double getNameWidth() {
      return this.nameWidth;
   }

   public void setSuffix(String str) {
      boolean isControlFlowFlag = Categorys.isControlFlowFlag();
      if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
         PacketThreadUtil.runOnMainThreadImmediate(this::getFullDisplayWidth);
         if (isControlFlowFlag) {
            return;
         }
      }

      this.applySuffix(str);
   }

   private void applySuffix(String str) {
      this.rawSuffix = str;
      switch (ClientManagers.moduleManager.arrayList.suffixCasing.getSelectedValue()) {
         case "UpperCase":
            str = str.toUpperCase(Locale.ROOT);
            break;
         case "LowerCase":
            str = str.toLowerCase(Locale.ROOT);
      }

      String formattedSuffix2 = this.formattedSuffix;
      if (mc.thePlayer != null) {
         switch (ClientManagers.moduleManager.arrayList.suffix.getSelectedValue()) {
            case "None":
               this.formattedSuffix = "";
               break;
            case "Simple":
               this.formattedSuffix = " " + str;
               break;
            case "Dash":
               this.formattedSuffix = " - " + str;
               break;
            case "Bracket":
               this.formattedSuffix = " [" + str + "]";
               break;
            case "Colon":
               this.formattedSuffix = " : " + str;
               break;
            case "Custom":
               this.formattedSuffix = " " + ClientManagers.moduleManager.arrayList.customSuffix.getValue() + " " + str;
         }
      } else {
         this.formattedSuffix = "";
      }

      if (!this.formattedSuffix.equalsIgnoreCase(formattedSuffix2) || !this.lastSuffixCaseMode.equals(ClientManagers.moduleManager.arrayList.suffixCasing.getSelectedValue())) {
         ClientManagers.moduleManager.sortModules();
      }

      this.lastSuffixCaseMode = ClientManagers.moduleManager.arrayList.suffixCasing.getSelectedValue();
   }

   public boolean isToggled() {
      return (Boolean)this.mT.getValue();
   }

   public void setToggled(boolean flag) {
      this.setToggled(flag, true);
   }

   public void setToggled(boolean flag, boolean flag2) {
      Module module;
      exitBlock: {
         boolean flag3 = Categorys.isControlFlowFlagCleared();
         boolean alwaysEnabled2 = this.alwaysEnabled;
         if (!flag3) {
            if (this.alwaysEnabled) {
               BooleanProperty mt = this.mT;
               Boolean bool = true;
               mt.setValue(bool);
               return;
            }

            module = this;
            if (flag3) {
               break exitBlock;
            }

            alwaysEnabled2 = (Boolean)this.mT.getValue();
         }

         if (alwaysEnabled2 == flag) {
            return;
         }

         this.toggle(flag2);
         module = this;
      }

      module.toggledByKey = false;
   }

   public int getKeyCode() {
      Augustus.getInstance().getClass();
      return this.lastToggleTimeMs < this.createdTimeMs + 192744000L ? (Integer)this.mK.getValue() : 0;
   }

   public void setKeyCode(int n) {
      IntegerProperty mk = this.mK;
      Integer num = n;
      mk.setValue(num);
   }

   public void setToggleSoundEnabled(boolean flag) {
      this.mS.setValue(Boolean.valueOf(flag));
      this.toggleSound.setValue(flag);
   }

   public void setKeyHoldMode(boolean flag) {
      this.mKm.setValue(Boolean.valueOf(flag));
      this.keyHoldMode.setValue(flag);
   }

   public Categorys getCategorys() {
      return this.categorys;
   }

   public boolean isLoadFromConfig() {
      return (Boolean)this.mC.getValue();
   }

   public void setLoadFromConfig(boolean flag) {
      this.mC.setValue(Boolean.valueOf(flag));
      BooleanValue loadFromConfig2 = this.loadFromConfig;
      boolean value = (Boolean)this.mC.getValue();
      loadFromConfig2.setValue(value);
   }

   public int compareDisplayOrder(Module module) {
      if (Augustus.getInstance().getModuleManager().arrayList.sorting.getSelectedValue().equals("Alphabet")) {
         return -module.name.compareTo(this.name);
      }
      return (int)(module.fullDisplayWidth - this.fullDisplayWidth);
   }

   public void updateDisplayDimensions() {
      if (Augustus.getInstance().getModuleManager().arrayList.font.getSelectedValue().equals("Minecraft")) {
         this.fullDisplayWidth = Augustus.getInstance().getModuleManager().arrayList.bold.getValue() ? mc.fontRendererObj.getStringWidthFloat("§l" + this.getFullDisplayName()) : mc.fontRendererObj.getStringWidthFloat(this.getFullDisplayName());
      } else {
         this.fullDisplayWidth = (double)(int)Augustus.getInstance().getModuleManager().arrayList.getCustomFont().getStringWidth(this.getFullDisplayName());
      }

      if (Augustus.getInstance().getModuleManager().arrayList.font.getSelectedValue().equals("Minecraft")) {
         this.displayHeight = (double)mc.fontRendererObj.FONT_HEIGHT;
      } else {
         this.displayHeight = (double)(int)Augustus.getInstance().getModuleManager().arrayList.getCustomFont().getFontHeight();
      }
   }

   public void renderInClickGui(int n, int n2, int n3, int n4, Module module, CustomFontUtil customFontUtil, boolean flag) {
      boolean flag2;
      int n5;
      exitBlock: {
         this.guiFont = customFontUtil;
         boolean isControlFlowFlag = Categorys.isControlFlowFlag();
         this.guiX = n3;
         flag2 = isControlFlowFlag;
         this.guiY = n4;
         int colorInt = Augustus.getInstance().getModuleManager().clickGui.textColor.getColorInt((byte)0);
         int colorInt2 = Augustus.getInstance().getModuleManager().clickGui.hoverTextColor.getColorInt((byte)0);
         Module module2 = this;
         if (flag2) {
            if (this.isToggled()) {
               n5 = this.getHoveredColor(colorInt2, 0.2, n, n2, flag);
               break exitBlock;
            }

            module2 = this;
         }

         n5 = module2.getHoveredColor(colorInt, 0.2, n, n2, flag);
      }

      int n6;
      exitBlock2: {
         n6 = n5;
         Module module3 = module;
         if (flag2) {
            if (module == null) {
               break exitBlock2;
            }

            module3 = module;
         }

         if (module3.equals(this)) {
            customFontUtil.drawString("> " + this.getName2(), (double)n3, (double)n4, n6);
            if (flag2) {
               return;
            }
         }
      }

      customFontUtil.drawString(this.getName2(), (double)n3, (double)n4, n6);
   }

   private boolean isPointInBounds(int n, int n2, int n3, int n4, int n5, int n6) {
      return n5 >= n && n6 >= n2 && n5 < n + n3 && n6 < n2 + n4;
   }

   private int getHoveredColor(int n, double d, int n2, int n3, boolean flag) {
      if (this.isPointInBounds(this.guiX - 2, this.guiY, 160, this.guiHeight + 2, n2, n3) && !flag) {
         float[] floats = ColorUtil.toHsb(n);
         float f = (float)((double)floats[2] + d);
         if ((double)floats[2] + d > 1.0) {
            f = 1.0F;
         } else if ((double)floats[2] + d < 0.0) {
            f = 0.0F;
         }

         return Color.HSBtoRGB(floats[0], floats[1], f);
      }

      return n;
   }

   private int getHoveredColor(int n, double d, int n2, int n3, int n4, int n5, int n6, int n7, boolean flag) {
      if (this.isPointInBounds(n4 - 2, n5, n6, n7, n2, n3) && !flag) {
         float[] floats = ColorUtil.toHsb(n);
         float f = (float)((double)floats[2] + d);
         if ((double)floats[2] + d > 1.0) {
            f = 1.0F;
         } else if ((double)floats[2] + d < 0.0) {
            f = 0.0F;
         }

         return Color.HSBtoRGB(floats[0], floats[1], f);
      }

      return n;
   }

   public int getGuiX() {
      return this.guiX;
   }

   public int getGuiY() {
      return this.guiY;
   }

   public int getGuiHeight() {
      return this.guiHeight;
   }

   public ArrayList getSettings() {
      return this.settings;
   }

   public int renderSettingsHeader(int n, int n2, int n3, int n4, CustomFontUtil customFontUtil, boolean flag) {
      String str;
      StringBuilder sb;
      {
         sb = new StringBuilder().append("Key: ");
         if (this.bindingKey) {
            str = "...";
         } else {
            str = KeyboardInputUtil.getKeyDisplayName(this.getKeyCode());
         }
      }

      String str2 = sb.append(str).toString();
      this.keyBindX = n - 1;
      this.keyBindY = n2;
      this.keyBindHeight = (int)customFontUtil.getFontHeight() + 1;
      this.keyBindWidth = (int)customFontUtil.getStringWidth(str2) + 4;
      int colorInt = Augustus.getInstance().getModuleManager().clickGui.textColor.getColorInt((byte)0);
      double d = (double)n;
      double d2 = (double)n2;
      int keyBindHeight2 = this.keyBindHeight;
      int keyBindWidth2 = this.keyBindWidth;
      int keyBindY2 = this.keyBindY;
      int keyBindX2 = this.keyBindX;
      customFontUtil.drawString(str2, d, d2, this.getHoveredColor(colorInt, 0.2, n3, n4, keyBindX2, keyBindY2, keyBindWidth2, keyBindHeight2, flag));
      int n5 = n + 200;
      this.loadFromConfig.render(n3, n4, n5, n2, (byte)0, (byte)0, customFontUtil, true);
      int n7 = n + 400;
      this.showInArrayList.render(n3, n4, n7, n2, (byte)0, (byte)0, customFontUtil, true);
      int n9 = n + 600;
      this.toggleSound.render(n3, n4, n9, n2, (byte)0, (byte)0, customFontUtil, true);
      int n11 = n + 780;
      this.keyHoldMode.render(n3, n4, n11, n2, (byte)0, (byte)0, customFontUtil, true);
      BooleanProperty mc2 = this.mC;
      Boolean value = this.loadFromConfig.getValue();
      mc2.setValue(value);
      BooleanProperty ma = this.mA;
      Boolean value2 = this.showInArrayList.getValue();
      ma.setValue(value2);
      BooleanProperty ms = this.mS;
      Boolean value3 = this.toggleSound.getValue();
      ms.setValue(value3);
      BooleanProperty mKm2 = this.mKm;
      Boolean value4 = this.keyHoldMode.getValue();
      mKm2.setValue(value4);
      return 930;
   }

   public void handleSettingsMouseClick(int n, int n2, int n3) throws IOException {
      if (this.isPointInBounds(this.keyBindX, this.keyBindY, this.keyBindWidth, this.keyBindHeight, n, n2) && !this.bindingKey) {
         this.bindingKey = true;
      } else if (this.bindingKey) {
         if ((this.isPointInBounds(this.keyBindX, this.keyBindY, this.keyBindWidth + 20, this.keyBindHeight, n, n2) && (n3 == 0 || n3 == 1)) || n3 > 1) {
            this.setKeyCode(n3 + 10000);
         }

         this.bindingKey = false;
      }

      if (Augustus.getInstance().isConfigEditingEnabled()) {
         this.loadFromConfig.mouseClicked(n, n2, n3);
      }

      this.showInArrayList.mouseClicked(n, n2, n3);
      this.toggleSound.mouseClicked(n, n2, n3);
      this.keyHoldMode.mouseClicked(n, n2, n3);
   }

   public void handleSettingsKeyTyped(char ch, int n) throws IOException {
      boolean flag = Categorys.isControlFlowFlagCleared();
      Module module = this;
      if (!flag) {
         if (!this.bindingKey) {
            return;
         }

         module = this;
      }

      int n2 = n;
      if (!flag) {
         n2 = n == 1 ? 0 : n;
      }

      module.setKeyCode(n2);
      this.bindingKey = false;
   }

   public boolean isBindingKey() {
      return this.bindingKey;
   }

   public void setBindingKey(boolean flag) {
      this.bindingKey = flag;
   }

   public boolean isShownInArrayList() {
      return (Boolean)this.mA.getValue() && !this.alwaysEnabled;
   }

   public boolean isToggleSoundEnabled() {
      return (Boolean)this.mS.getValue();
   }

   public void setShowInArrayList(boolean flag) {
      this.mA.setValue(Boolean.valueOf(flag));
      this.showInArrayList.setValue(flag);
   }

   public String getGermanName() {
      return this.germanName;
   }

   public int getScrollOffsetX() {
      return this.scrollOffsetX;
   }

   public void setScrollOffsetX(int n) {
      this.scrollOffsetX = n;
   }

   public int getScrollOffsetY() {
      return this.scrollOffsetY;
   }

   public void setScrollOffsetY(int n) {
      this.scrollOffsetY = n;
   }

   public double getFullDisplayWidth() {
      return this.fullDisplayWidth;
   }

   public double getDisplayHeight() {
      return this.displayHeight;
   }

   public long getLastToggleTimeMs() {
      return this.lastToggleTimeMs;
   }

   public boolean isToggledByKey() {
      return this.toggledByKey;
   }

   public void setToggledByKey(boolean flag) {
      this.toggledByKey = flag;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String str) {
      this.description = str;
   }

   public boolean isKeyHoldMode() {
      return (Boolean)this.mKm.getValue();
   }

   public ArrayList getToggleListeners() {
      return this.toggleListeners;
   }

   public void setToggleLocked(boolean flag) {
      this.mT.setLocked(flag);
   }

   public boolean isHiddenInGui() {
      return this.hiddenInGui;
   }

   public boolean isHiddenFromConfig() {
      return this.hiddenFromConfig;
   }

   public void markHidden() {
      this.hiddenFromConfig = true;
      this.hiddenInGui = true;
   }

   public void setHiddenInGui(boolean flag) {
      this.hiddenInGui = flag;
   }

   public void sendChatMessage2(String str) {
      ChatUtil.sendPrefixedChat(this.getName2(), str);
   }

   public void sendChatMessage(double d) {
      ChatUtil.sendPrefixedChat(this.getName2(), String.valueOf(d));
   }

   @Override
   public int compareTo(Object other) {
      return this.compareDisplayOrder((Module)other);
   }

   private void applySuffixOnMainThread(String str) {
      this.applySuffix(str);
   }

   private void notifyToggleListener(ModuleToggleListener listener) {
      boolean isControlFlowFlag = Categorys.isControlFlowFlag();
      boolean isToggled = this.isToggled();
      if (isControlFlowFlag) {
         isToggled = !isToggled;
      }

      listener.onModuleToggled(isToggled);
   }

   private static void notifyAlwaysEnabledListener(ModuleToggleListener listener) {
      listener.onModuleToggled(true);
   }

   private void onTogglePropertyChanged(Boolean bool) {
      boolean flag = bool;
      this.setToggled(flag, false);
   }
}
