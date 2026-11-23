package net.augustus.modules.modes.targethud;

import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.animation.AnimationUtil;
import net.augustus.utils.animation.EasingType;
import net.augustus.utils.player.PlayerUtil;
import com.mojang.authlib.GameProfile;
import net.augustus.modules.render.TargetHud;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public abstract class AbstractTargetHudMode extends AbstractSubMode {
   public final AnimationUtil toggleAnimation;
   public final AnimationUtil healthAnimation;
   public float currentHealth;
   public float previousHealth;
   public int lastTargetEntityId;
   public final StringValue scale;
   private static int[] controlFlowState2;

   public AbstractTargetHudMode(TargetHud targetHud, DoubleValue doubleValue, String str) {
       this(targetHud, doubleValue, str, true);
   }

   public AbstractTargetHudMode(TargetHud targetHud, DoubleValue doubleValue, String str, boolean flag) {
       super(targetHud, doubleValue, str);
      this.lastTargetEntityId = -1;
      getControlFlowState2();
      this.toggleAnimation = new AnimationUtil(0.0F, 0.0F, 0.0F, 0.0F);
      this.healthAnimation = new AnimationUtil(0.0F, 0.0F, 0.0F, 0.0F);
      if (flag) {
         StringValue stringValue = new StringValue(targetHud, this.getName() + "Scale", 1.0, 0.1, 2.0, 2, doubleValue);
         String str2 = "Scale";
         StringValue stringValue2 = stringValue.named(str2);
         SettingVisibilityCondition condition = () -> this.isSelected();
         this.scale = stringValue2.visibleWhen(condition);
      } else {
         this.scale = null;
      }
   }

   @Override
   public void onEnable() {
      this.toggleAnimation.start((int)((TargetHud)this.parentModule).animationTime.getValue(), AnimationUtil.parseEasingType(((TargetHud)this.parentModule).toggleAnimation2.getSelectedValue()), 1, true);
   }

   @Override
   public void onDisable() {
      this.lastTargetEntityId = -1;
   }

   @Override
   public void updateEventSubscriptions() {
   }

   public abstract int getHalfWidth();

   public abstract int getHalfHeight();

   public void renderTargetHud(AbstractClientPlayer clientPlayer, AbstractClientPlayer clientPlayer2, ScaledResolution sr) {
      int[] controlFlowState22 = getControlFlowState2();
      if (clientPlayer != null && (this.toggleAnimation.getLinearProgress() != 1.0 || clientPlayer2 != null)) {
         NetworkPlayerInfo playerInfo = this.getPlayerInfo(clientPlayer);
         if (playerInfo != null) {
            Vec3 projectedTargetPos = ((TargetHud)this.parentModule).projectedTargetPos;
            int halfWidth = this.getHalfWidth();
            int halfHeight = this.getHalfHeight();
            int n = halfHeight;
            int n2 = halfWidth;
            Vec3 vec = projectedTargetPos;
            TargetHudScreenPos targetHudScreenPos = this.computeScreenPosition(sr, vec, n2, n);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0 / (double)sr.getScaleFactor(), 1.0 / (double)sr.getScaleFactor(), 1.0);
            GlStateManager.translate((float)targetHudScreenPos.x, (float)targetHudScreenPos.y, 0.0F);
            double scale2 = this.getScale();
            GlStateManager.scale(scale2, scale2, 1.0);
            double easedProgress = this.toggleAnimation.getEasedProgress();
            if (this.toggleAnimation.getLinearProgress() < 1.0) {
               GlStateManager.scale(easedProgress, easedProgress, 1.0);
            }

            this.drawHudContent(clientPlayer, playerInfo, sr);
            GlStateManager.popMatrix();
         }
      }
   }

   public abstract void drawHudContent(AbstractClientPlayer clientPlayer, NetworkPlayerInfo networkPlayerInfo, ScaledResolution sr);

   public double getScale() {
      int[] controlFlowState22 = getControlFlowState2();
      return this.scale != null ? this.scale.getValue() : 1.0;
   }

   public void onTargetChanged(AbstractClientPlayer clientPlayer) {
      int[] controlFlowState22 = getControlFlowState2();
      if (clientPlayer != null) {
         this.toggleAnimation.start((int)((TargetHud)this.parentModule).animationTime.getValue(), AnimationUtil.parseEasingType(((TargetHud)this.parentModule).toggleAnimation2.getSelectedValue()), 1, true);
      } else {
         this.toggleAnimation.start((int)((TargetHud)this.parentModule).animationTime.getValue(), AnimationUtil.parseEasingType(((TargetHud)this.parentModule).toggleAnimation2.getSelectedValue()), 0, true);
      }
   }

   public void updateHealthAnimation(AbstractClientPlayer clientPlayer, float f) {
      int[] controlFlowState22 = getControlFlowState2();
      if (Integer.signum(this.lastTargetEntityId) == -1 || this.lastTargetEntityId != clientPlayer.getEntityId()) {
         this.currentHealth = f;
         this.previousHealth = f;
      }

      if (this.currentHealth != f) {
         this.previousHealth = this.currentHealth;
         this.currentHealth = f;
         this.healthAnimation.start(400, EasingType.easeOutCubic, 1, false);
      }
   }

   public void recordLastTarget(AbstractClientPlayer clientPlayer) {
      if (clientPlayer != null) {
         this.lastTargetEntityId = clientPlayer.getEntityId();
      }
   }

   public TargetHudScreenPos computeScreenPosition(ScaledResolution sr, Vec3 vec, int n, int n2) {
      int[] controlFlowState22 = getControlFlowState2();
      int n3;
      int n4;
      if (((TargetHud)this.parentModule).trackEntity.getValue()) {
         vec.xCoord = vec.xCoord + ((TargetHud)this.parentModule).trackX.getValue();
         n3 = n + (int)((double)(mc.getFramebuffer().framebufferWidth - n * 2) * (vec.xCoord / (double)sr.getScaledWidth()));
         n4 = n2 + (int)((double)(mc.getFramebuffer().framebufferHeight - n2 * 2) * (vec.yCoord / (double)sr.getScaledHeight()));
      } else {
         n3 = n + (int)((double)(mc.getFramebuffer().framebufferWidth - n * 2) * (((TargetHud)this.parentModule).xCoord.getValue() / 100.0));
         n4 = n2 + (int)((double)(mc.getFramebuffer().framebufferHeight - n2 * 2) * (((TargetHud)this.parentModule).yCoord.getValue() / 100.0));
      }

      return new TargetHudScreenPos(n3, n4);
   }

   public void drawPlayerHead(ResourceLocation location, AbstractClientPlayer clientPlayer, boolean flag, int n, int n2, int n3) {
      int[] controlFlowState22 = getControlFlowState2();
      mc.getTextureManager().bindTexture(location);
      int n4 = 8 + (flag ? 8 : 0);
      int n5 = 8 * (flag ? -1 : 1);
      Gui.drawScaledCustomSizeModalRect(n, n2, 8.0F, (float)n4, 8, n5, n3, n3, 64.0F, 64.0F);
   }

   public NetworkPlayerInfo getPlayerInfo(AbstractClientPlayer clientPlayer) {
      return clientPlayer == null ? null : mc.getNetHandler().getPlayerInfo(clientPlayer.getUniqueID());
   }

   public boolean isUpsideDownPlayer(AbstractClientPlayer clientPlayer) {
      int[] controlFlowState22 = getControlFlowState2();
      if (clientPlayer == null) {
         return false;
      } else {
         GameProfile gameProfile = clientPlayer.getGameProfile();
         return clientPlayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.getName().equals("Dinnerbone") || gameProfile.getName().equals("Grumm"));
      }
   }

   public float getPlayerHealth(AbstractClientPlayer clientPlayer) {
      return PlayerUtil.getPlayerHealthByMode(((TargetHud)this.parentModule).healthMode, clientPlayer);
   }

   public static void setControlFlowState2(int[] ints) {
      controlFlowState2 = ints;
   }

   public static int[] getControlFlowState2() {
      return controlFlowState2;
   }

   static {
      setControlFlowState2((int[])null);
   }

}
