package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.RenderListener;
import net.augustus.ClientManagers;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.ui.notifications.NotificationManager;
import net.augustus.ui.notifications.HudNotification;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.Augustus;
import net.augustus.utils.render.BlurShaderRenderer;
import net.augustus.settings.options.NotificationCategoryOption;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.optifine.config.Config;

public class Notifications extends Module implements RenderListener {
   private static final BlurShaderRenderer BLUR_RENDERER;
   private static final String[] COLOR_MODES = new String[]{"Client color", "Notification type", "Fade", "Blank"};
   public final BooleanValue blur;
   public final BooleanValue avoidDuplications;
   public final BooleansSetting position;
   public final StringValue duplicateTime;
   public final StringValue showDuration;
   public final StringValue speed;
   public final BooleansSetting groupColorMode;
   public final BooleansSetting progressColorMode;
   public final MultiOptionSetting notificationTypes;

   public Notifications() {
       super("Notifications", "Benachrichtigungen", Color.WHITE, Categorys.RENDER);
      this.blur = new BooleanValue(this, "Blur", true);
      this.avoidDuplications = new BooleanValue(this, "AvoidDuplications", true);
      this.position = new BooleansSetting(this, "Position", "Bottom right", new String[]{"Top right", "Top left", "Bottom right", "Bottom left"});
      this.duplicateTime = new StringValue(this, "DuplicateTime", 0.20000000298023224, 0.0, 1.0, 1);
      this.showDuration = new StringValue(this, "ShowDuration", 2.5, 0.0, 10.0, 1);
      this.speed = new StringValue(this, "Speed", 5.0, 1.0, 10.0, 0);
      this.groupColorMode = new BooleansSetting(this, "GroupColorMode", "Blank", COLOR_MODES);
      this.progressColorMode = new BooleansSetting(this, "ProgressColorMode", "Notification type", COLOR_MODES);
      this.notificationTypes = new MultiOptionSetting(this, "NotificationTypes", NotificationCategoryOption.values());
      boolean flag = false;
      boolean flag2 = true;
      this.setToggled(flag2, flag);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      int n = 2147483647;
      byte b = 32;
      this.toggleEventSubscription(b, n);
   }

   public void onRender(float f, ScaledResolution sr) {
      boolean flag = false;
      this.renderNotifications(flag);
      RenderUtil.resetGlColorState();
   }

   public void renderNotifications(boolean flag) {
      if (!NotificationManager.isEmpty()) {
         NotificationManager.updateNotifications();
         if (this.blur.getValue() && !Config.isFastRender() && !Config.isAntialiasing() && !Config.isAnisotropicFiltering()) {
            boolean flag2 = true;
            BLUR_RENDERER.beginMaskCapture(flag2, flag);
            NotificationManager.renderNotifications();
            BLUR_RENDERER.endMaskCapture();
            BlurShaderRenderer blurShaderRenderer = BLUR_RENDERER;
            int value = (int)ClientManagers.moduleManager.blur.sigma.getValue();
            blurShaderRenderer.renderBlurPass(flag, value);
         }

         NotificationManager.renderNotifications();
      }
   }

   public int resolveNotificationColor(HudNotification hudNotification, String str) {
      switch (str) {
         case "Client color":
            return Augustus.getInstance().getClientColor();
         case "Notification type":
            return hudNotification.getLevel().getColor().getRGB();
         case "Fade":
            long time = hudNotification.elapsedTimer.getTime();
            Color red = Color.RED;
            Color green = Color.GREEN;
            double d = (double)(1.0F - MathHelper.clamp_float((float)time / (float)hudNotification.getTotalDuration(), 0.0F, 1.0F));
            Color color2 = green;
            return ColorUtil.blendColors(red, color2, d);
         default:
            return Color.GRAY.getRGB();
      }
   }

   static {
      BLUR_RENDERER = new BlurShaderRenderer();
   }

}
