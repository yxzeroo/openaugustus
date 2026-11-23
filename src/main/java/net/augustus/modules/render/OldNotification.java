package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.ColorSetting;
import net.augustus.font.FontManager;
import net.augustus.ui.notification.ToggleNotification;
import net.augustus.Augustus;
import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.utils.SystemTrayNotifier;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;

public class OldNotification extends Module {
   public final BooleansSetting modes;
   public final BooleansSetting side;
   public final StringValue y;
   public final StringValue animationTime;
   public final StringValue animationDuration;
   public final ColorSetting color2;
   public final BlurShadowSettings blurShadowSettings;
   public java.util.ArrayList activeNotifications;

   public OldNotification() {
       super("OldNotification", "AlteBenachrichtigung", new Color(126, 133, 222), Categorys.RENDER);
      this.modes = new BooleansSetting(this, "Modes", "Windows", new String[]{"Windows", "Basic"});
      BooleansSetting booleansSetting = new BooleansSetting(this, "Side", "Right", new String[]{"Left", "Right"});
      SettingVisibilityCondition condition = () -> this.isSideVisible();
      this.side = (BooleansSetting)booleansSetting.withVisibility(condition);
      StringValue stringValue = new StringValue(this, "Y", 100.0, 0.0, 100.0, 2);
      SettingVisibilityCondition condition2 = () -> {
         return this.modes.getSelectedValue().equals("Basic");
      };
      this.y = stringValue.visibleWhen(condition2);
      StringValue stringValue2 = new StringValue(this, "AnimationTime", 400.0, 0.0, 1000.0, 0);
      SettingVisibilityCondition condition3 = () -> this.isAnimationTimeVisible();
      this.animationTime = stringValue2.visibleWhen(condition3);
      StringValue stringValue3 = new StringValue(this, "AnimationDuration", 2000.0, 0.0, 5000.0, 0);
      SettingVisibilityCondition condition4 = () -> this.isAnimationDurationVisible();
      this.animationDuration = stringValue3.visibleWhen(condition4);
      ColorSetting colorSetting = new ColorSetting(this, "Color", Augustus.getInstance().getClientColor());
      SettingVisibilityCondition condition5 = () -> {
         return this.modes.getSelectedValue().equals("Basic");
      };
      this.color2 = (ColorSetting)colorSetting.withVisibility(condition5);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      this.blurShadowSettings = blurShadowSettings2.withCustomShadowColor(black);
      this.activeNotifications = new java.util.ArrayList();
   }

   public void pushModuleToggleNotification(Module module) {
      String selectedValue = this.modes.getSelectedValue();
      switch (selectedValue) {
         case "Windows":
            String str = "Module (" + module.getName2() + ") Notification";
            StringBuilder sb = new StringBuilder().append(module.getName2());
            StringBuilder sb2 = sb.append(" toggled ");
            String str2;
            if (module.isToggled()) {
               str2 = "on";
            } else {
               str2 = "off";
            }

            String str3 = sb2.append(str2).toString();
            SystemTrayNotifier.displayNotification(str, str3);
            break;
         case "Basic":
            if (!(mc.currentScreen instanceof ClickGuiScreen)) {
               String str4 = module.getName2() + " toggled ";
               int n;
               if (this.side.getSelectedValue().equalsIgnoreCase("Right")) {
                  n = mc.getFramebuffer().framebufferWidth - (int)(FontManager.esp32.getStringWidth(str4) + 10.0F);
               } else {
                  n = 0;
               }

               int n2 = n;
               int n3 = (int)((double)(mc.getFramebuffer().framebufferHeight - 60) * (this.y.getValue() / 100.0));
               java.util.ArrayList activeNotifications2 = this.activeNotifications;
               int n4 = (int)(FontManager.esp32.getStringWidth(str4) + 10.0F);
               int value = (int)this.animationTime.getValue();
               int value2 = (int)this.animationDuration.getValue();
               byte b = 0;
               activeNotifications2.add(0, new ToggleNotification(n2, n3, n4, 60, module, value, value2, this.color2.getColorInt(b)));
            }
      }
   }

   public void renderNotifications(float f) {
      boolean flag = false;

      for (ToggleNotification toggleNotification : (Iterable<ToggleNotification>)(this.activeNotifications)) {
         if (flag) {
            this.activeNotifications.remove(toggleNotification);
         }

         if (Math.abs(System.currentTimeMillis() - toggleNotification.getCreatedTime()) > (long)toggleNotification.getDisplayDuration()) {
            this.activeNotifications.remove(toggleNotification);
         } else {
            BlurShadowSettings blurShadowSettings2 = this.blurShadowSettings;
            toggleNotification.render(f, blurShadowSettings2);
            flag = true;
         }
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   private boolean isAnimationDurationVisible() {
      return this.modes.getSelectedValue().equals("Basic");
   }

   private boolean isAnimationTimeVisible() {
      return this.modes.getSelectedValue().equals("Basic");
   }

   private boolean isSideVisible() {
      return this.modes.getSelectedValue().equals("Basic");
   }


}
