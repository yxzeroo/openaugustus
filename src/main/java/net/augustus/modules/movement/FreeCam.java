package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.listeners.ShouldRenderEntityListener;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.settings.Setting;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.MoveUtil;
import net.augustus.events.listeners.RenderEntitiesListener;
import net.augustus.events.EventRenderEntities;
import net.augustus.utils.rotation.RotationController;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.render.RenderUtil;
import net.augustus.Augustus;
import net.augustus.events.EventShouldRenderEntity;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.rotation.YawPitchHelper;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public class FreeCam extends Module implements ClickListener, MoveInputListener, RenderListener, RenderEntitiesListener, ShouldRenderEntityListener, TickListener {
   private final StringValue speed;
   public final BooleanValue spectatorRender;
   public final BooleanValue updateRaytrace;
   public final BooleanValue showRealPlayerModel;
   public final StringValue minimumDistanceToShowPlayerModel;
   private final BooleanValue displayCoordinates;
   private final ColorSetting highlightColor;
   private final ColorSetting textColor;
   private boolean pendingDisable;
   public Vec3 cameraPos;
   public Vec3 prevCameraPos;
   public double realPosX;
   public double realPosY;
   public double realPosZ;
   public double realLastTickPosX;
   public double realLastTickPosY;
   public double realLastTickPosZ;
   public float savedYaw;
   public float savedPitch;
   public float cameraYaw;
   public float cameraPitch;

   public FreeCam() {
       super("FreeCam", "FreieKamera", new Color(235, 116, 52), Categorys.MOVEMENT);
      this.speed = (StringValue)(new StringValue(this, "Speed", 1.0, 0.0, 4.0, 2)).markVisualOnly();
      this.spectatorRender = (BooleanValue)(new BooleanValue(this, "Spectator Render", false)).markVisualOnly();
      this.updateRaytrace = (BooleanValue)(new BooleanValue(this, "Update Raytrace", false)).markVisualOnly();
      this.showRealPlayerModel = (BooleanValue)(new BooleanValue(this, "Show real Player Model", true)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "Minimum distance to show player model", 1.0, 0.0, 10.0, 1);
      BooleanValue showRealPlayerModel2 = this.showRealPlayerModel;
      this.showRealPlayerModel.getClass();
      BooleanValue booleanValue = showRealPlayerModel2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.minimumDistanceToShowPlayerModel = (StringValue)stringValue.visibleWhen(condition).markVisualOnly();
      this.displayCoordinates = (BooleanValue)(new BooleanValue(this, "Display Coordinates", false)).markVisualOnly();
      ColorSetting colorSetting = new ColorSetting(this, "Highlight Color", new Color(5, 245, 69, 50));
      showRealPlayerModel2 = this.displayCoordinates;
      this.displayCoordinates.getClass();
      BooleanValue booleanValue2 = showRealPlayerModel2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.highlightColor = (ColorSetting)((ColorSetting)colorSetting.withVisibility(condition2)).markVisualOnly();
      ColorSetting colorSetting2 = new ColorSetting(this, "Text Color", new Color(255, 255, 255, 255));
      showRealPlayerModel2 = this.displayCoordinates;
      this.displayCoordinates.getClass();
      BooleanValue booleanValue3 = showRealPlayerModel2;
      SettingVisibilityCondition condition3 = () -> booleanValue3.getValue();
      this.textColor = (ColorSetting)((ColorSetting)colorSetting2.withVisibility(condition3)).markVisualOnly();
      this.pendingDisable = false;
      this.cameraPos = new Vec3(0.0, 0.0, 0.0);
      this.prevCameraPos = new Vec3(0.0, 0.0, 0.0);
   }

   @Override
   public void onEnable() {
      if (mc.thePlayer != null) {
         this.savedYaw = mc.thePlayer.rotationYaw;
         this.savedPitch = mc.thePlayer.rotationPitch;
         this.cameraYaw = mc.thePlayer.rotationYaw;
         this.cameraPitch = mc.thePlayer.rotationPitch;
         this.cameraPos = mc.thePlayer.getInterpolatedPos(1.0F);
         this.prevCameraPos = mc.thePlayer.getInterpolatedPos(0.0F);
      }
   }

   @Override
   public void onDisable() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (mc.thePlayer != null) {
         float rotationYaw = mc.thePlayer.rotationYaw;
         float rotationPitch = mc.thePlayer.rotationPitch;
         RotationController rotationController = ClientManagers.rotationController;
         Rotation rotation = new Rotation(rotationYaw, rotationPitch);
         byte b = 3;
         Rotation rotation2 = rotation;
         rotationController.requestRotation(rotation2, b, this);
         YawPitchHelper yawPitchHelper = Augustus.getInstance().getYawPitchHelper();
         yawPitchHelper.realYaw = rotationYaw;
         yawPitchHelper.realPitch = rotationPitch;
         yawPitchHelper.realLastYaw = rotationYaw;
         yawPitchHelper.realLastPitch = rotationPitch;
         mc.thePlayer.rotationYaw = rotationYaw;
         mc.thePlayer.rotationPitch = rotationPitch;
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 100;
      byte b3 = 21;
      this.toggleEventSubscription(b3, b2);
      byte b4 = 32;
      this.toggleEventSubscription(b4);
      byte b5 = 57;
      this.toggleEventSubscription(b5);
      byte b6 = 58;
      this.toggleEventSubscription(b6);
      byte b7 = 40;
      this.toggleEventSubscription(b7);
   }

   @Override
   public void onTick3() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (mc.thePlayer == null) {
         this.pendingDisable = true;
      } else {
         RotationController rotationController = ClientManagers.rotationController;
         Rotation rotation = new Rotation(mc.thePlayer.lastReportedYaw, mc.thePlayer.lastReportedPitch);
         byte b = -50;
         Rotation rotation2 = rotation;
         rotationController.requestRotation(rotation2, b, this);
         mc.thePlayer.rotationYaw = mc.thePlayer.lastReportedYaw;
         mc.thePlayer.rotationPitch = mc.thePlayer.lastReportedPitch;
      }
   }

   @Override
   public void onTick4() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (mc.thePlayer == null) {
         this.pendingDisable = true;
      } else {
         mc.thePlayer.rotationYaw = this.cameraYaw;
         mc.thePlayer.rotationPitch = this.cameraPitch;
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.pendingDisable) {
         this.pendingDisable = false;
         this.toggle();
      } else {
         if (this.isLicenseTimestampValid()) {
            double[] doubles = MoveUtil.calculateMotion(0.5, event.getMoveStrafe(), event.getMoveForward(), this.cameraYaw);
            Vec3 vec = new Vec3(doubles[0], event.isOriginalJump() ? 0.4 : (event.isOriginalSneak() ? -0.4 : 0.0), doubles[1]);
            vec = vec.scale(this.speed.getValue());
            boolean flag = false;
            event.setJump(flag);
            boolean flag2 = false;
            event.setSneak(flag2);
            float f = 0.0F;
            event.setMoveForward(f);
            float f2 = 0.0F;
            event.setMoveStrafe(f2);
            this.prevCameraPos = new Vec3(this.cameraPos);
            this.cameraPos = this.cameraPos.add(vec);
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (!this.updateRaytrace.getValue()) {
         event.cancel();
         boolean flag = true;
         event.setBlockAllClicks(flag);
      }
   }

   @Override
   public void onRender(float f) {
      this.cameraYaw = mc.thePlayer.rotationYaw;
      Fly.getCachedSettings();
      this.cameraPitch = mc.thePlayer.rotationPitch;
      if (this.displayCoordinates.getValue()) {
         Vec3 vec = RotationUtil.getLookVector(mc.thePlayer.rotationPitch, mc.thePlayer.rotationYaw).scale(200.0);
         Vec3 vec2 = this.prevCameraPos.lerp(this.cameraPos, (double)f);
         MovingObjectPosition hit = mc.theWorld
            .rayTraceBlocksFast(
               vec2.xCoord,
               vec2.yCoord + (double)mc.thePlayer.getEyeHeight(),
               vec2.zCoord,
               vec.xCoord + vec2.xCoord,
               vec.yCoord + vec2.yCoord + (double)mc.thePlayer.getEyeHeight(),
               vec.zCoord + vec2.zCoord
            );
         if (hit != null && hit.hitVec != null) {
            RenderUtil.begin3DRender();
            GL11.glPushMatrix();
            GL11.glLineWidth(3.0F);
            GL11.glTranslated(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());
            GlStateManager.disableAlpha();
            byte b = 0;
            RenderUtil.setGlColor(this.highlightColor.getColorInt(b));
            BlockPos blockPos = hit.getBlockPos();
            RenderUtil.drawFilledBox2(
               (float)blockPos.getX(),
               (float)blockPos.getY(),
               (float)blockPos.getZ(),
               (float)(blockPos.getX() + 1),
               (float)(blockPos.getY() + 1),
               (float)(blockPos.getZ() + 1)
            );
            GL11.glPopMatrix();
            RenderUtil.end3DRender();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.displayCoordinates.getValue()) {
         Vec3 vec = RotationUtil.getLookVector(mc.thePlayer.rotationPitch, mc.thePlayer.rotationYaw).scale(200.0);
         Vec3 vec2 = this.prevCameraPos.lerp(this.cameraPos, (double)f);
         MovingObjectPosition hit = mc.theWorld
            .rayTraceBlocksFast(
               vec2.xCoord,
               vec2.yCoord + (double)mc.thePlayer.getEyeHeight(),
               vec2.zCoord,
               vec.xCoord + vec2.xCoord,
               vec.yCoord + vec2.yCoord + (double)mc.thePlayer.getEyeHeight(),
               vec.zCoord + vec2.zCoord
            );
         if (hit != null && hit.hitVec != null) {
            BlockPos blockPos = hit.getBlockPos();
            String str = "x=" + blockPos.getX() + " y=" + blockPos.getY() + " z=" + blockPos.getZ();
            FontRenderer fontRendererObj = mc.fontRendererObj;
            int n = (sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(str)) / 2;
            int n2 = sr.getScaledHeight() / 2 + mc.fontRendererObj.FONT_HEIGHT * 3;
            byte b = 0;
            fontRendererObj.drawString(str, n, n2, this.textColor.getColorInt(b));
         }

         RenderUtil.resetGlColorState();
      }
   }

   @Override
   public void onRenderEntities(EventRenderEntities event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.showRealPlayerModel.getValue()) {
         if (this.cameraPos == null || !(this.cameraPos.distanceTo(this.realPosX, this.realPosY, this.realPosZ) < this.minimumDistanceToShowPlayerModel.getValue())) {
            EntityPlayerSP thePlayer = mc.thePlayer;
            double posX = thePlayer.posX;
            double posY = thePlayer.posY;
            double posZ = thePlayer.posZ;
            double lastTickPosX = thePlayer.lastTickPosX;
            double lastTickPosY = thePlayer.lastTickPosY;
            double lastTickPosZ = thePlayer.lastTickPosZ;
            thePlayer.posX = this.realPosX;
            thePlayer.posY = this.realPosY;
            thePlayer.posZ = this.realPosZ;
            thePlayer.lastTickPosX = this.realLastTickPosX;
            thePlayer.lastTickPosY = this.realLastTickPosY;
            thePlayer.lastTickPosZ = this.realLastTickPosZ;
            mc.renderGlobal.countEntitiesRendered++;
            if (event.isShadersEnabled()) {
               Shaders.nextEntity(thePlayer);
            }

            mc.renderGlobal.renderManager.renderEntitySimple(thePlayer, event.getPartialTicks());
            thePlayer.posX = posX;
            thePlayer.posY = posY;
            thePlayer.posZ = posZ;
            thePlayer.lastTickPosX = lastTickPosX;
            thePlayer.lastTickPosY = lastTickPosY;
            thePlayer.lastTickPosZ = lastTickPosZ;
         }
      }
   }

   @Override
   public void onShouldRenderEntity(EventShouldRenderEntity event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (event.getEntity() == mc.thePlayer) {
         boolean flag = true;
         event.setShouldRender(flag);
      }
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      Fly.getCachedSettings();
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
