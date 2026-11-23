package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.ClientManagers;
import net.augustus.render.RenderUtil;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class Scoreboard extends Module implements RenderListener {
   public final BooleanValue remove;
   public final BooleanValue stick;
   public final BooleanValue transparent;
   public final BooleanValue fontShadow;
   public final BooleanValue rounded;
   public final StringValue roundFactor;
   public final StringValue positionX;
   public final StringValue positionY;
   public final StringValue scale;
   public final BlurShadowSettings blurShadowSettings;

   public Scoreboard() {
       super("Scoreboard", "Spielstandanzeige", new Color(34, 162, 162), Categorys.RENDER);
      this.remove = new BooleanValue(this, "Remove", false);
      BooleanValue booleanValue = new BooleanValue(this, "Stick", false);
      SettingVisibilityCondition condition = () -> this.isStickVisible();
      this.stick = (BooleanValue)booleanValue.withVisibility(condition);
      BooleanValue booleanValue2 = new BooleanValue(this, "Transparent", false);
      SettingVisibilityCondition condition2 = () -> this.isTransparentVisible();
      this.transparent = (BooleanValue)booleanValue2.withVisibility(condition2);
      BooleanValue booleanValue3 = new BooleanValue(this, "FontShadow", false);
      SettingVisibilityCondition condition3 = () -> this.isFontShadowVisible();
      this.fontShadow = (BooleanValue)booleanValue3.withVisibility(condition3);
      BooleanValue booleanValue4 = new BooleanValue(this, "Rounded", false);
      SettingVisibilityCondition condition4 = () -> this.isRoundedVisible();
      this.rounded = (BooleanValue)booleanValue4.withVisibility(condition4);
      StringValue stringValue = new StringValue(this, "RoundFactor", 3.0, 0.0, 8.0, 1);
      SettingVisibilityCondition condition5 = () -> this.isRoundFactorVisible();
      this.roundFactor = stringValue.visibleWhen(condition5);
      StringValue stringValue2 = new StringValue(this, "PositionX", 100.0, 0.0, 100.0, 1);
      SettingVisibilityCondition condition6 = () -> this.isPositionXVisible();
      this.positionX = stringValue2.visibleWhen(condition6);
      StringValue stringValue3 = new StringValue(this, "PositionY", 60.0, 0.0, 100.0, 1);
      SettingVisibilityCondition condition7 = () -> {
         return !this.remove.getValue() && !this.stick.getValue();
      };
      this.positionY = stringValue3.visibleWhen(condition7);
      StringValue stringValue4 = new StringValue(this, "Scale", 2.0, 1.0, 4.0, 2);
      SettingVisibilityCondition condition8 = () -> this.isScaleVisible();
      this.scale = stringValue4.visibleWhen(condition8);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this);
      Color black = Color.black;
      BlurShadowSettings blurShadowSettings3 = blurShadowSettings2.withCustomShadowColor(black);
      SettingVisibilityCondition condition9 = () -> this.isBlurShadowVisible();
      this.blurShadowSettings = (BlurShadowSettings)blurShadowSettings3.withVisibility(condition9);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (!this.remove.getValue() && !ClientManagers.moduleManager.antiStrike.shouldHideScoreboard()) {
         GlStateManager.pushMatrix();
         ScaledResolution sr2 = new ScaledResolution(mc);
         GlStateManager.scale(1.0F / (float)sr2.getScaleFactor(), 1.0F / (float)sr2.getScaleFactor(), 1.0F);
         GlStateManager.scale(this.scale.getValue(), this.scale.getValue(), 1.0);
         GlStateManager.enableAlpha();
         GlStateManager.alphaFunc(516, 0.0F);
         ScaledResolution sr3 = new ScaledResolution(
            (double)mc.displayWidth * (1.0 / this.scale.getValue()), (double)mc.displayHeight * (1.0 / this.scale.getValue()), (int)this.scale.getValue()
         );
         net.minecraft.scoreboard.Scoreboard scoreboard = mc.theWorld.getScoreboard();
         if (scoreboard != null) {
            ScoreObjective scoreObjective = null;
            ScorePlayerTeam playersTeam = scoreboard.getPlayersTeam(mc.thePlayer.getName());
            if (playersTeam != null) {
               int colorIndex = playersTeam.getChatFormat().getColorIndex();
               if (colorIndex >= 0) {
                  scoreObjective = scoreboard.getObjectiveInDisplaySlot(3 + colorIndex);
               }
            }

            ScoreObjective scoreObjective2 = scoreObjective != null ? scoreObjective : scoreboard.getObjectiveInDisplaySlot(1);
            if (scoreObjective2 != null) {
               mc.ingameGUI.renderScoreboard(scoreObjective2, sr3, this.transparent.getValue(), this.fontShadow.getValue());
            }
         }

         GlStateManager.popMatrix();
         RenderUtil.resetGlColorState();
      }
   }

   private boolean isBlurShadowVisible() {
      return !this.remove.getValue();
   }

   private boolean isScaleVisible() {
      return !this.remove.getValue();
   }

   private boolean isPositionXVisible() {
      return !this.remove.getValue();
   }

   private boolean isRoundFactorVisible() {
      return !this.remove.getValue() && this.rounded.getValue();
   }

   private boolean isRoundedVisible() {
      return !this.remove.getValue();
   }

   private boolean isFontShadowVisible() {
      return !this.remove.getValue();
   }

   private boolean isTransparentVisible() {
      return !this.remove.getValue();
   }

   private boolean isStickVisible() {
      return !this.remove.getValue();
   }

}
