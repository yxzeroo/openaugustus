package net.augustus.modules.render;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.misc.MurdererFinder;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.modules.render.esp.EspRenderUtil;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.modules.render.esp.EntityEspBox;
import net.augustus.modules.render.esp.EspOverlayImage;
import net.augustus.ClientManagers;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.utils.file.ClientPaths;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.ActionSetting;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.GlStateSaver;
import net.augustus.render.ProjectedBounds2D;
import net.augustus.render.ProjectionUtil;
import net.augustus.settings.SettingChangeListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.options.EspRenderModeOption;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class ESP extends Module implements RenderListener, TickListener {
   private static final String PICTURES_DIR = ClientPaths.CLIENT_DIR + File.separator + "pictures";
   private final MultiOptionSetting modes;
   public final BooleanValue markMurderer;
   public final BooleanValue customMurdererAlpha;
   public final StringValue murdererAlpha;
   private final DoubleValue box;
   private final BooleanValue teamColorBox;
   private final StringValue teamColorAlphaBox;
   private final ColorSetting color6;
   private final BooleanValue boxHitColorBool;
   private final ColorSetting hitColor;
   private final StringValue boxLineWidth;
   private final StringValue boxPriority;
   private final BlurShadowSettings box2;
   private final DoubleValue hitBox;
   private final BooleanValue hitBoxTeamColor;
   private final StringValue hitBoxTeamColorAlpha;
   private final ColorSetting color2;
   private final BooleanValue hitBoxHitColorBool;
   private final ColorSetting hitColor2;
   private final StringValue hitBoxLineWidth;
   private final StringValue hitBoxPriority;
   private final BlurShadowSettings hitBox2;
   private final DoubleValue collisionBox;
   private final BooleanValue collisionBoxTeamColor;
   private final StringValue collisionBoxTeamColorAlpha;
   private final ColorSetting color3;
   private final BooleanValue collisionBoxHitColorBool;
   private final ColorSetting hitColor3;
   private final StringValue collisionBoxLineWidth;
   private final StringValue collisionBoxPriority;
   private final BlurShadowSettings collisionBox2;
   private final DoubleValue n3DCorner;
   private final BooleanValue fakeCornerTeamColor;
   private final ColorSetting color4;
   private final BooleanValue fakeCornerHitColorBool;
   private final ColorSetting hitColor4;
   private final StringValue fakeCornerPriority;
   private final BlurShadowSettings fakeCorner;
   private final DoubleValue real2D;
   private final BooleanValue real2DTeamColor;
   private final StringValue real2DTeamColorAlpha;
   private final ColorSetting fillColor;
   private final ColorSetting outlineColor;
   private final BooleanValue real2DHitColorBool;
   private final ColorSetting fillColorHit;
   private final ColorSetting outlineColorHit;
   private final StringValue real2DPriority;
   private final BlurShadowSettings real2D2;
   private final DoubleValue corner;
   private final BooleanValue cornerTeamColor;
   private final ColorSetting color5;
   private final BooleanValue cornerHitColorBool;
   private final ColorSetting cornerHitColor;
   private final StringValue lineWidthCorner;
   private final StringValue horizontalPercentage;
   private final StringValue verticalPercentage;
   private final StringValue cornerPriority;
   private final BlurShadowSettings corner2;
   private final DoubleValue picture;
   private final BooleansSetting picturePictures;
   private final BooleanValue stretchPicture;
   private final StringValue scalePicture;
   private final StringValue pictureYPositionPercentage;
   private final StringValue picturePicturePriority;
   private final BlurShadowSettings picture2;
   private final ActionSetting pictureFolder;
   private final DoubleValue vanilla;
   public final BooleanValue vanillaTeamColor;
   public final ColorSetting vanillaColor;
   public final BooleanValue vanillaHitColorBool;
   public final ColorSetting hitColor5;
   private final TargetSelectionSettings entities;
   public final StringValue maximumRenderDistance;
   public final java.util.ArrayList visibleEntities;
   public final HashMap projectedEntityBounds;
   private final Frustum frustum;
   private final Int2ObjectAVLTreeMap render2DPassesByPriority;
   private final Int2ObjectAVLTreeMap render3DPassesByPriority;
   private final Map loadedOverlayImages;
   private final GlStateSaver glStateSaver;
   private EspOverlayImage espOverlayImage;

   public ESP() {
       super("ESP", "SpielerDurchWandSehen", new Color(47, 134, 124, 255), Categorys.RENDER);
      this.modes = new MultiOptionSetting(this, "Modes", EspRenderModeOption.values());
      this.markMurderer = new BooleanValue(this, "MarkMurderer", true);
      BooleanValue booleanValue = new BooleanValue(this, "Custom Murderer Alpha", false);
      BooleanValue markMurderer2 = this.markMurderer;
      this.markMurderer.getClass();
      BooleanValue booleanValue2 = markMurderer2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.customMurdererAlpha = (BooleanValue)booleanValue.withVisibility(condition);
      StringValue stringValue = new StringValue(this, "Murderer Alpha", 65.0, 0.0, 255.0, 0);
      SettingVisibilityCondition condition2 = () -> this.isMurdererAlphaVisible();
      this.murdererAlpha = stringValue.visibleWhen(condition2);
      DoubleValue doubleValue = new DoubleValue(this, "Box", true);
      SettingVisibilityCondition condition3 = () -> EspRenderModeOption.isOptionSelected(EspRenderModeOption.BOX);
      this.box = doubleValue.visibleWhen(condition3);
      BooleanValue booleanValue3 = new BooleanValue(this, "TeamColorBox", false, this.box);
      String str = "TeamColor (players only)";
      this.teamColorBox = (BooleanValue)booleanValue3.withDisplayName(str);
      StringValue stringValue2 = new StringValue(this, "TeamColorAlphaBox", 65.0, 0.0, 255.0, 0, this.box);
      String str2 = "TeamColorAlpha";
      StringValue stringValue3 = stringValue2.named(str2);
      markMurderer2 = this.teamColorBox;
      this.teamColorBox.getClass();
      BooleanValue booleanValue4 = markMurderer2;
      SettingVisibilityCondition condition4 = () -> booleanValue4.getValue();
      this.teamColorAlphaBox = stringValue3.visibleWhen(condition4);
      Color color7 = new Color(21, 121, 230, 65);
      DoubleValue box3 = this.box;
      Color color8 = color7;
      String str3 = "BoxColor";
      ColorSetting colorSetting = new ColorSetting(this, str3, color8, box3);
      String str4 = "Color";
      this.color6 = (ColorSetting)colorSetting.withDisplayName(str4);
      this.boxHitColorBool = new BooleanValue(this, "BoxHitColorBool", false, this.box);
      color7 = new Color(255, 0, 0, 65);
      box3 = this.box;
      color8 = color7;
      str3 = "BoxHitColor";
      ColorSetting colorSetting2 = new ColorSetting(this, str3, color8, box3);
      String str5 = "HitColor";
      ColorSetting colorSetting3 = (ColorSetting)colorSetting2.withDisplayName(str5);
      markMurderer2 = this.boxHitColorBool;
      this.boxHitColorBool.getClass();
      BooleanValue booleanValue5 = markMurderer2;
      SettingVisibilityCondition condition5 = () -> booleanValue5.getValue();
      this.hitColor = (ColorSetting)colorSetting3.withVisibility(condition5);
      StringValue stringValue4 = new StringValue(this, "BoxLineWidth", 6.0, 0.0, 15.0, 0, this.box);
      String str6 = "LineWidth";
      this.boxLineWidth = stringValue4.named(str6);
      StringValue stringValue5 = new StringValue(this, "BoxPriority", 0.0, 0.0, 3.0, 0, this.box);
      String str7 = "3DPriority";
      StringValue stringValue6 = stringValue5.named(str7);
      SettingChangeListener listener = () -> this.rebuild3DRenderQueue();
      SettingChangeListener listener2 = listener;
      this.boxPriority = (StringValue)stringValue6.withChangeListener(listener2);
      BlurShadowSettings blurShadowSettings = (new BlurShadowSettings(this, "Box", this.box)).disableBothByDefault();
      Color black = Color.black;
      this.box2 = blurShadowSettings.withCustomShadowColor(black);
      DoubleValue doubleValue2 = new DoubleValue(this, "HitBox", true);
      SettingVisibilityCondition condition6 = () -> EspRenderModeOption.isOptionSelected(EspRenderModeOption.HIT_BOX);
      this.hitBox = doubleValue2.visibleWhen(condition6);
      BooleanValue booleanValue6 = new BooleanValue(this, "HitBoxTeamColor", false, this.hitBox);
      String str8 = "TeamColor (players only)";
      this.hitBoxTeamColor = (BooleanValue)booleanValue6.withDisplayName(str8);
      StringValue stringValue7 = new StringValue(this, "HitBoxTeamColorAlpha", 65.0, 0.0, 255.0, 0, this.hitBox);
      String str9 = "TeamColorAlpha";
      StringValue stringValue8 = stringValue7.named(str9);
      markMurderer2 = this.hitBoxTeamColor;
      this.hitBoxTeamColor.getClass();
      BooleanValue booleanValue7 = markMurderer2;
      SettingVisibilityCondition condition7 = () -> booleanValue7.getValue();
      this.hitBoxTeamColorAlpha = stringValue8.visibleWhen(condition7);
      color7 = new Color(21, 121, 230, 65);
      box3 = this.hitBox;
      color8 = color7;
      str3 = "HitBoxColor";
      ColorSetting colorSetting4 = new ColorSetting(this, str3, color8, box3);
      String str10 = "Color";
      this.color2 = (ColorSetting)colorSetting4.withDisplayName(str10);
      this.hitBoxHitColorBool = new BooleanValue(this, "HitBoxHitColorBool", false, this.hitBox);
      color7 = new Color(255, 0, 0, 65);
      box3 = this.hitBox;
      color8 = color7;
      str3 = "HitBoxHitColor";
      ColorSetting colorSetting5 = new ColorSetting(this, str3, color8, box3);
      String str11 = "HitColor";
      ColorSetting colorSetting6 = (ColorSetting)colorSetting5.withDisplayName(str11);
      markMurderer2 = this.hitBoxHitColorBool;
      this.hitBoxHitColorBool.getClass();
      BooleanValue booleanValue8 = markMurderer2;
      SettingVisibilityCondition condition8 = () -> booleanValue8.getValue();
      this.hitColor2 = (ColorSetting)colorSetting6.withVisibility(condition8);
      StringValue stringValue9 = new StringValue(this, "HitBoxLineWidth", 6.0, 0.0, 15.0, 0, this.hitBox);
      String str12 = "LineWidth";
      this.hitBoxLineWidth = stringValue9.named(str12);
      StringValue stringValue10 = new StringValue(this, "HitBoxPriority", 1.0, 0.0, 3.0, 0, this.hitBox);
      String str13 = "3DPriority";
      StringValue stringValue11 = stringValue10.named(str13);
      SettingChangeListener listener3 = () -> this.rebuild3DRenderQueue();
      SettingChangeListener listener4 = listener3;
      this.hitBoxPriority = (StringValue)stringValue11.withChangeListener(listener4);
      BlurShadowSettings blurShadowSettings2 = (new BlurShadowSettings(this, "HitBox", this.hitBox)).disableBothByDefault();
      Color black2 = Color.black;
      this.hitBox2 = blurShadowSettings2.withCustomShadowColor(black2);
      DoubleValue doubleValue3 = new DoubleValue(this, "CollisionBox", true);
      SettingVisibilityCondition condition9 = () -> EspRenderModeOption.isOptionSelected(EspRenderModeOption.COLLISION_BOX);
      this.collisionBox = doubleValue3.visibleWhen(condition9);
      BooleanValue booleanValue9 = new BooleanValue(this, "CollisionBoxTeamColor", false, this.collisionBox);
      String str14 = "TeamColor (players only)";
      this.collisionBoxTeamColor = (BooleanValue)booleanValue9.withDisplayName(str14);
      StringValue stringValue12 = new StringValue(this, "CollisionBoxTeamColorAlpha", 65.0, 0.0, 255.0, 0, this.collisionBox);
      String str15 = "TeamColorAlpha";
      StringValue stringValue13 = stringValue12.named(str15);
      markMurderer2 = this.collisionBoxTeamColor;
      this.collisionBoxTeamColor.getClass();
      BooleanValue booleanValue10 = markMurderer2;
      SettingVisibilityCondition condition10 = () -> booleanValue10.getValue();
      this.collisionBoxTeamColorAlpha = stringValue13.visibleWhen(condition10);
      color7 = new Color(21, 121, 230, 65);
      box3 = this.collisionBox;
      color8 = color7;
      str3 = "CollisionBoxColor";
      ColorSetting colorSetting7 = new ColorSetting(this, str3, color8, box3);
      String str16 = "Color";
      this.color3 = (ColorSetting)colorSetting7.withDisplayName(str16);
      this.collisionBoxHitColorBool = new BooleanValue(this, "CollisionBoxHitColorBool", false, this.collisionBox);
      color7 = new Color(255, 0, 0, 65);
      box3 = this.collisionBox;
      color8 = color7;
      str3 = "CollisionBoxHitColor";
      ColorSetting colorSetting8 = new ColorSetting(this, str3, color8, box3);
      String str17 = "HitColor";
      ColorSetting colorSetting9 = (ColorSetting)colorSetting8.withDisplayName(str17);
      markMurderer2 = this.collisionBoxHitColorBool;
      this.collisionBoxHitColorBool.getClass();
      BooleanValue booleanValue11 = markMurderer2;
      SettingVisibilityCondition condition11 = () -> booleanValue11.getValue();
      this.hitColor3 = (ColorSetting)colorSetting9.withVisibility(condition11);
      StringValue stringValue14 = new StringValue(this, "CollisionBoxLineWidth", 6.0, 0.0, 15.0, 0, this.collisionBox);
      String str18 = "LineWidth";
      this.collisionBoxLineWidth = stringValue14.named(str18);
      StringValue stringValue15 = new StringValue(this, "CollisionBoxPriority", 2.0, 0.0, 3.0, 0, this.collisionBox);
      String str19 = "3DPriority";
      StringValue stringValue16 = stringValue15.named(str19);
      SettingChangeListener listener5 = () -> this.rebuild3DRenderQueue();
      SettingChangeListener listener6 = listener5;
      this.collisionBoxPriority = (StringValue)stringValue16.withChangeListener(listener6);
      BlurShadowSettings blurShadowSettings3 = (new BlurShadowSettings(this, "CollisionBox", this.collisionBox)).disableBothByDefault();
      Color black3 = Color.black;
      this.collisionBox2 = blurShadowSettings3.withCustomShadowColor(black3);
      DoubleValue doubleValue4 = new DoubleValue(this, "3DCorner", true);
      SettingVisibilityCondition condition12 = () -> EspRenderModeOption.isOptionSelected(EspRenderModeOption.THREE_E_CORNER);
      this.n3DCorner = doubleValue4.visibleWhen(condition12);
      BooleanValue booleanValue12 = new BooleanValue(this, "FakeCornerTeamColor", false, this.n3DCorner);
      String str20 = "TeamColor (players only)";
      this.fakeCornerTeamColor = (BooleanValue)booleanValue12.withDisplayName(str20);
      color7 = new Color(21, 121, 230, 65);
      box3 = this.n3DCorner;
      color8 = color7;
      str3 = "FakeCornerColor";
      ColorSetting colorSetting10 = new ColorSetting(this, str3, color8, box3);
      String str21 = "Color";
      this.color4 = (ColorSetting)colorSetting10.withDisplayName(str21);
      this.fakeCornerHitColorBool = new BooleanValue(this, "FakeCornerHitColorBool", false, this.n3DCorner);
      color7 = new Color(255, 0, 0, 65);
      box3 = this.n3DCorner;
      color8 = color7;
      str3 = "FakeCornerHitColor";
      ColorSetting colorSetting11 = new ColorSetting(this, str3, color8, box3);
      String str22 = "HitColor";
      ColorSetting colorSetting12 = (ColorSetting)colorSetting11.withDisplayName(str22);
      markMurderer2 = this.fakeCornerHitColorBool;
      this.fakeCornerHitColorBool.getClass();
      BooleanValue booleanValue13 = markMurderer2;
      SettingVisibilityCondition condition13 = () -> booleanValue13.getValue();
      this.hitColor4 = (ColorSetting)colorSetting12.withVisibility(condition13);
      StringValue stringValue17 = new StringValue(this, "FakeCornerPriority", 3.0, 0.0, 3.0, 0, this.n3DCorner);
      String str23 = "3DPriority";
      StringValue stringValue18 = stringValue17.named(str23);
      SettingChangeListener listener7 = () -> this.rebuild3DRenderQueue();
      SettingChangeListener listener8 = listener7;
      this.fakeCornerPriority = (StringValue)stringValue18.withChangeListener(listener8);
      BlurShadowSettings blurShadowSettings4 = (new BlurShadowSettings(this, "FakeCorner", this.n3DCorner)).disableBothByDefault();
      Color black4 = Color.black;
      this.fakeCorner = blurShadowSettings4.withCustomShadowColor(black4);
      DoubleValue doubleValue5 = new DoubleValue(this, "Real2D", true);
      SettingVisibilityCondition condition14 = () -> EspRenderModeOption.isOptionSelected(EspRenderModeOption.REAL_2D);
      this.real2D = doubleValue5.visibleWhen(condition14);
      BooleanValue booleanValue14 = new BooleanValue(this, "Real2DTeamColor", false, this.real2D);
      String str24 = "TeamColor (players only)";
      this.real2DTeamColor = (BooleanValue)booleanValue14.withDisplayName(str24);
      StringValue stringValue19 = new StringValue(this, "Real2DTeamColorAlpha", 60.0, 0.0, 255.0, 0, this.real2D);
      String str25 = "TeamColorAlpha";
      StringValue stringValue20 = stringValue19.named(str25);
      markMurderer2 = this.real2DTeamColor;
      this.real2DTeamColor.getClass();
      BooleanValue booleanValue15 = markMurderer2;
      SettingVisibilityCondition condition15 = () -> booleanValue15.getValue();
      this.real2DTeamColorAlpha = stringValue20.visibleWhen(condition15);
      color7 = new Color(21, 121, 230, 60);
      box3 = this.real2D;
      color8 = color7;
      str3 = "Real2DColor1";
      ColorSetting colorSetting13 = new ColorSetting(this, str3, color8, box3);
      String str26 = "FillColor";
      this.fillColor = (ColorSetting)colorSetting13.withDisplayName(str26);
      color7 = new Color(0, 0, 0);
      box3 = this.real2D;
      color8 = color7;
      str3 = "Real2DColor2";
      ColorSetting colorSetting14 = new ColorSetting(this, str3, color8, box3);
      String str27 = "OutlineColor";
      this.outlineColor = (ColorSetting)colorSetting14.withDisplayName(str27);
      this.real2DHitColorBool = new BooleanValue(this, "Real2DHitColorBool", false, this.real2D);
      color7 = new Color(255, 0, 0, 60);
      box3 = this.real2D;
      color8 = color7;
      str3 = "Real2DHitColor1";
      ColorSetting colorSetting15 = new ColorSetting(this, str3, color8, box3);
      String str28 = "FillColorHit";
      ColorSetting colorSetting16 = (ColorSetting)colorSetting15.withDisplayName(str28);
      markMurderer2 = this.real2DHitColorBool;
      this.real2DHitColorBool.getClass();
      BooleanValue booleanValue16 = markMurderer2;
      SettingVisibilityCondition condition16 = () -> booleanValue16.getValue();
      this.fillColorHit = (ColorSetting)colorSetting16.withVisibility(condition16);
      color7 = new Color(0, 0, 0);
      box3 = this.real2D;
      color8 = color7;
      str3 = "Real2DHitColor2";
      ColorSetting colorSetting17 = new ColorSetting(this, str3, color8, box3);
      String str29 = "OutlineColorHit";
      ColorSetting colorSetting18 = (ColorSetting)colorSetting17.withDisplayName(str29);
      markMurderer2 = this.real2DHitColorBool;
      this.real2DHitColorBool.getClass();
      BooleanValue booleanValue17 = markMurderer2;
      SettingVisibilityCondition condition17 = () -> booleanValue17.getValue();
      this.outlineColorHit = (ColorSetting)colorSetting18.withVisibility(condition17);
      StringValue stringValue21 = new StringValue(this, "Real2DPriority", 0.0, 0.0, 2.0, 0, this.real2D);
      String str30 = "2DPriority";
      StringValue stringValue22 = stringValue21.named(str30);
      SettingChangeListener listener9 = () -> this.rebuild2DRenderQueue();
      SettingChangeListener listener10 = listener9;
      this.real2DPriority = (StringValue)stringValue22.withChangeListener(listener10);
      BlurShadowSettings blurShadowSettings5 = (new BlurShadowSettings(this, "Real2D", this.real2D)).disableBothByDefault();
      Color black5 = Color.black;
      this.real2D2 = blurShadowSettings5.withCustomShadowColor(black5);
      DoubleValue doubleValue6 = new DoubleValue(this, "Corner", true);
      SettingVisibilityCondition condition18 = () -> EspRenderModeOption.isOptionSelected(EspRenderModeOption.CORNER);
      this.corner = doubleValue6.visibleWhen(condition18);
      markMurderer2 = new BooleanValue(this, "CornerTeamColor", false, this.corner);
      String str31 = "TeamColor (players only)";
      this.cornerTeamColor = (BooleanValue)markMurderer2.withDisplayName(str31);
      Color color9 = new Color(21, 121, 230);
      box3 = this.corner;
      color8 = color9;
      str3 = "CornerColor";
      ColorSetting colorSetting19 = new ColorSetting(this, str3, color8, box3);
      String str32 = "Color";
      this.color5 = (ColorSetting)colorSetting19.withDisplayName(str32);
      markMurderer2 = new BooleanValue(this, "CornerHitColorBool", true, this.corner);
      String str33 = "HitColor";
      this.cornerHitColorBool = (BooleanValue)markMurderer2.withDisplayName(str33);
      color9 = new Color(21, 121, 230);
      box3 = this.corner;
      color8 = color9;
      str3 = "CornerHitColor";
      ColorSetting colorSetting20 = new ColorSetting(this, str3, color8, box3);
      BooleanValue cornerHitColorBool2 = this.cornerHitColorBool;
      this.cornerHitColorBool.getClass();
      BooleanValue booleanValue18 = cornerHitColorBool2;
      SettingVisibilityCondition condition19 = () -> booleanValue18.getValue();
      this.cornerHitColor = (ColorSetting)colorSetting20.withVisibility(condition19);
      StringValue stringValue23 = new StringValue(this, "LineWidthCorner", 2.0, 1.0, 5.0, 0, this.corner);
      String str34 = "LineWidth";
      this.lineWidthCorner = stringValue23.named(str34);
      this.horizontalPercentage = new StringValue(this, "HorizontalPercentage", 55.0, 1.0, 100.0, 0, this.corner);
      this.verticalPercentage = new StringValue(this, "VerticalPercentage", 45.0, 1.0, 100.0, 0, this.corner);
      StringValue stringValue24 = new StringValue(this, "CornerPriority", 1.0, 0.0, 2.0, 0, this.corner);
      String str35 = "CornerPriority";
      StringValue stringValue25 = stringValue24.named(str35);
      SettingChangeListener listener11 = () -> this.rebuild2DRenderQueue();
      SettingChangeListener listener12 = listener11;
      this.cornerPriority = (StringValue)stringValue25.withChangeListener(listener12);
      BlurShadowSettings blurShadowSettings6 = (new BlurShadowSettings(this, "Corner", this.corner)).disableBothByDefault();
      Color black6 = Color.black;
      this.corner2 = blurShadowSettings6.withCustomShadowColor(black6);
      DoubleValue doubleValue7 = new DoubleValue(this, "Picture", true);
      SettingVisibilityCondition condition20 = () -> EspRenderModeOption.isOptionSelected(EspRenderModeOption.PICTURE);
      this.picture = doubleValue7.visibleWhen(condition20);
      BooleansSetting booleansSetting = new BooleansSetting(this, "PicturePictures", "", new String[0], this.picture);
      listener11 = () -> this.updateSelectedOverlayImage();
      SettingChangeListener listener13 = listener11;
      BooleansSetting booleansSetting2 = (BooleansSetting)booleansSetting.withChangeListener(listener13);
      String str36 = "Pictures";
      this.picturePictures = (BooleansSetting)booleansSetting2.withDisplayName(str36);
      markMurderer2 = new BooleanValue(this, "StretchPicture", false, this.picture);
      String str37 = "Stretch";
      this.stretchPicture = (BooleanValue)markMurderer2.withDisplayName(str37);
      StringValue stringValue26 = new StringValue(this, "ScalePicture", 1.0, 0.1, 2.0, 2, this.picture);
      String str38 = "Scale";
      this.scalePicture = stringValue26.named(str38);
      StringValue stringValue27 = new StringValue(this, "PictureYPositionPercentage", 50.0, 0.0, 100.0, 0, this.picture);
      String str39 = "YPositionPercentage";
      StringValue stringValue28 = stringValue27.named(str39);
      SettingVisibilityCondition condition21 = () -> {
         return !this.stretchPicture.getValue();
      };
      this.pictureYPositionPercentage = stringValue28.visibleWhen(condition21);
      StringValue stringValue29 = new StringValue(this, "PicturePicturePriority", 2.0, 0.0, 2.0, 0, this.picture);
      String str40 = "PicturePriority";
      StringValue stringValue30 = stringValue29.named(str40);
      listener11 = () -> this.rebuild2DRenderQueue();
      SettingChangeListener listener14 = listener11;
      this.picturePicturePriority = (StringValue)stringValue30.withChangeListener(listener14);
      BlurShadowSettings blurShadowSettings7 = (new BlurShadowSettings(this, "Picture", this.picture)).disableBothByDefault();
      Color black7 = Color.black;
      this.picture2 = blurShadowSettings7.withCustomShadowColor(black7);
      this.pictureFolder = new ActionSetting(this, "Picture folder", () -> {
         ClientFileUtil.openFolder(PICTURES_DIR);
      }, this.picture);
      DoubleValue doubleValue8 = new DoubleValue(this, "Vanilla", true);
      SettingVisibilityCondition condition22 = () -> EspRenderModeOption.isOptionSelected(EspRenderModeOption.VANILLA);
      this.vanilla = doubleValue8.visibleWhen(condition22);
      markMurderer2 = new BooleanValue(this, "VanillaTeamColor", false, this.vanilla);
      String str41 = "TeamColor (players only)";
      this.vanillaTeamColor = (BooleanValue)markMurderer2.withDisplayName(str41);
      color9 = new Color(21, 121, 230, 65);
      box3 = this.vanilla;
      color8 = color9;
      str3 = "VanillaColor";
      this.vanillaColor = new ColorSetting(this, str3, color8, box3);
      this.vanillaHitColorBool = new BooleanValue(this, "VanillaHitColorBool", true, this.vanilla);
      color9 = new Color(255, 0, 0, 65);
      box3 = this.vanilla;
      color8 = color9;
      str3 = "VanillaHitColor";
      ColorSetting colorSetting21 = new ColorSetting(this, str3, color8, box3);
      String str42 = "HitColor";
      ColorSetting colorSetting22 = (ColorSetting)colorSetting21.withDisplayName(str42);
      BooleanValue vanillaHitColorBool2 = this.vanillaHitColorBool;
      this.vanillaHitColorBool.getClass();
      BooleanValue booleanValue19 = vanillaHitColorBool2;
      SettingVisibilityCondition condition23 = () -> booleanValue19.getValue();
      this.hitColor5 = (ColorSetting)colorSetting22.withVisibility(condition23);
      TargetSelectionSettings targetSelectionSettings = new TargetSelectionSettings(this, TargetFilterPresets.createRenderFilter());
      String str43 = "Entities";
      this.entities = targetSelectionSettings.withTargetsDisplayName(str43);
      this.maximumRenderDistance = new StringValue(this, "MaximumRenderDistance", 500.0, 0.0, 500.0, 0);
      this.visibleEntities = new java.util.ArrayList();
      this.projectedEntityBounds = new HashMap();
      this.frustum = new Frustum();
      this.render2DPassesByPriority = new Int2ObjectAVLTreeMap();
      this.render3DPassesByPriority = new Int2ObjectAVLTreeMap();
      this.loadedOverlayImages = new HashMap();
      this.glStateSaver = GlStateSaver.create();
      this.rebuild3DRenderQueue();
      this.rebuild2DRenderQueue();
      this.refreshOverlayImages();
   }

   @Override
   public void onEnable() {
      this.refreshOverlayImages();
   }

   @Override
   public void onDisable() {
      this.projectedEntityBounds.clear();
      this.visibleEntities.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = -100;
      byte b2 = 32;
      this.toggleEventSubscription(b2, b);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onTick2() {
      if (mc.theWorld != null) {
         this.visibleEntities.clear();
         EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
         EntityTargetFilter targetFilter = this.entities.getTargetFilter();
         EntityTargetFilter entityTargetFilter = targetFilter;
         entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter);
         double value = this.maximumRenderDistance.getValue();
         entityTargetSelector = entityTargetSelector.maxDistance(value);
         Consumer consumer = obj -> {
            EntityLivingBase entity = (EntityLivingBase)obj;
            this.addIfInFrustum(entity);
         };
         entityTargetSelector.forEach(consumer);
      }
   }

   @Override
   public void onRender2(float f, ScaledResolution sr) {
      if (!this.visibleEntities.isEmpty()) {
         ObjectIterator it = this.render2DPassesByPriority.values().iterator();

         while (it.hasNext()) {
            Runnable runnable = (Runnable)it.next();
            runnable.run();
            RenderUtil.resetGlColorState();
         }
      }
   }

   @Override
   public void onRender(float f) {
      this.projectedEntityBounds.clear();
      if (!this.visibleEntities.isEmpty()) {
         ObjectIterator it = this.render3DPassesByPriority.values().iterator();

         while (it.hasNext()) {
            Runnable runnable = (Runnable)it.next();
            GlStateManager.pushMatrix();
            RenderUtil.begin3DRender();
            runnable.run();
            RenderUtil.end3DRender();
            GlStateManager.popMatrix();
         }

         if (EspRenderModeOption.isOptionSelected(EspRenderModeOption.REAL_2D) || EspRenderModeOption.isOptionSelected(EspRenderModeOption.CORNER) || EspRenderModeOption.isOptionSelected(EspRenderModeOption.PICTURE)) {
            ScaledResolution sr = new ScaledResolution(mc);

            for (EntityLivingBase entity : (Iterable<EntityLivingBase>)(this.visibleEntities)) {
               Vec3 vec = new Vec3(
                  entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.getTimer().renderPartialTicks,
                  entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.getTimer().renderPartialTicks,
                  entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.getTimer().renderPartialTicks
               );
               float f2 = entity.width / 2.0F;
               float f3 = ClientManagers.moduleManager.child.isToggled() && entity instanceof EntityPlayer ? entity.height / 1.7F : entity.height;
               this.projectedEntityBounds
                  .put(
                     entity,
                     ProjectionUtil.projectBoundingBox(
                        new AxisAlignedBB(
                              vec.xCoord - (double)f2,
                              vec.yCoord,
                              vec.zCoord - (double)f2,
                              vec.xCoord + (double)f2,
                              vec.yCoord + (double)f3,
                              vec.zCoord + (double)f2
                           )
                           .expand(0.1, 0.1, 0.1),
                        sr
                     )
                  );
            }
         }
      }
   }

   private void rebuild2DRenderQueue() {
      this.render2DPassesByPriority.clear();
      this.render2DPassesByPriority.put((int)this.real2DPriority.getValue(), (Runnable)() -> this.renderReal2DBoxes());
      this.render2DPassesByPriority.put((int)this.cornerPriority.getValue(), (Runnable)() -> this.render2DCorners());
      Int2ObjectAVLTreeMap int2ObjectAVLTreeMap = this.render2DPassesByPriority;
      int value = (int)this.picturePicturePriority.getValue();
      int2ObjectAVLTreeMap.put(value, (Runnable)() -> {
         if (EspRenderModeOption.PICTURE.isSelected() && mc.theWorld != null && this.espOverlayImage != null) {
            boolean isBlurActive = this.picture2.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.picture2.isShadowActive());
            BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderPictureOverlaysPass(shaderRenderPass));
         }
      });
   }

   private void rebuild3DRenderQueue() {
      this.render3DPassesByPriority.clear();
      Int2ObjectAVLTreeMap int2ObjectAVLTreeMap = this.render3DPassesByPriority;
      int value = (int)this.boxPriority.getValue();
      int2ObjectAVLTreeMap.put(value, (Runnable)() -> {
         if (EspRenderModeOption.isOptionSelected(EspRenderModeOption.BOX) && mc.theWorld != null) {
            boolean isBlurActive = this.box2.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.box2.isShadowActive());
            BlurShadowManager.submitRenderTask(true, shaderRenderPass -> this.renderBoxesPass(shaderRenderPass));
         }
      });
      int2ObjectAVLTreeMap = this.render3DPassesByPriority;
      int value2 = (int)this.hitBoxPriority.getValue();
      int2ObjectAVLTreeMap.put(value2, (Runnable)() -> {
         if (EspRenderModeOption.isOptionSelected(EspRenderModeOption.HIT_BOX) && mc.theWorld != null) {
            boolean isBlurActive2 = this.hitBox2.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive2, this.hitBox2.isShadowActive());
            BlurShadowManager.submitRenderTask(true, shaderRenderPass2 -> this.renderHitBoxesPass(shaderRenderPass2));
         }
      });
      this.render3DPassesByPriority.put((int)this.collisionBoxPriority.getValue(), (Runnable)() -> this.renderCollisionBoxes());
      this.render3DPassesByPriority.put((int)this.fakeCornerPriority.getValue(), (Runnable)() -> this.render3DCorners());
   }

   private void addIfInFrustum(EntityLivingBase entity) {
      this.frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
      if (ClientManagers.moduleManager.freeCam.isToggled()) {
         this.frustum.setPosition(ClientManagers.moduleManager.freeCam.cameraPos.xCoord, ClientManagers.moduleManager.freeCam.cameraPos.yCoord, ClientManagers.moduleManager.freeCam.cameraPos.zCoord);
      }

      AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
      if (this.frustum.isBoxInFrustum(entityBoundingBox.minX - 0.5, entityBoundingBox.minY - 0.5, entityBoundingBox.minZ - 0.5, entityBoundingBox.maxX + 0.5, entityBoundingBox.maxY + 0.5, entityBoundingBox.maxZ + 0.5)
         && !this.visibleEntities.contains(entity)) {
         this.visibleEntities.add(entity);
      }
   }

   private int resolveEntityColor(EntityLivingBase entity, int n, ColorSetting colorSetting, ColorSetting colorSetting2, boolean flag, boolean flag2, int n2) {
      boolean flag3 = true;
      return this.resolveEntityColor(entity, n, colorSetting, colorSetting2, flag, flag2, n2, flag3);
   }

   private int resolveEntityColor(EntityLivingBase entity, int n, ColorSetting colorSetting, ColorSetting colorSetting2, boolean flag, boolean flag2, int n2, boolean flag3) {
      int n3;
      if (flag && entity.hurtTime > 0) {
         n3 = colorSetting2.getColorInt(n);
      } else if (flag2 && entity instanceof EntityPlayer) {
         n3 = ColorUtil.withAlpha(ColorUtil.getTeamColor((EntityPlayer)entity), n2);
      } else {
         n3 = colorSetting.getColorInt(n);
      }

      if (flag3 && this.markMurderer.getValue()) {
         MurdererFinder murdererFinder = ClientManagers.moduleManager.murdererFinder;
         String name2 = entity.getName();
         if (murdererFinder.isMurderer(name2)) {
            int n4;
            if (this.customMurdererAlpha.getValue()) {
               boolean flag4 = true;
               n4 = (int)this.murdererAlpha.getValue(flag4);
            } else {
               n4 = ColorUtil.getAlpha(n3);
            }

            int n5 = n4;
            byte b = 0;
            n3 = ColorUtil.packRgba(255, 0, b, n5);
         }
      }

      return n3;
   }

   private float scaleLineWidthByDistance(double d, long l, EntityLivingBase entity) {
      if (mc.thePlayer.getDistanceToEntity(entity) > 1.0F) {
         double d2 = (double)(1.0F - mc.thePlayer.getDistanceToEntity(entity) / 20.0F);
         if (d2 < 0.3) {
            d2 = 0.3;
         }

         d *= d2;
      }

      return (float)d;
   }

   public boolean isValidTargetEntity(EntityLivingBase entity) {
      return this.entities.getTargetFilter().matches(entity) && (double)mc.thePlayer.getDistanceToEntity(entity) < this.maximumRenderDistance.getValue();
   }

   private void updateSelectedOverlayImage() {
      this.espOverlayImage = (EspOverlayImage)this.loadedOverlayImages.get(this.picturePictures.getSelectedValue());
   }

   private EspOverlayImage loadOverlayImage(String str) {
      File file = new File(PICTURES_DIR + File.separator + str);

      try (InputStream in = Files.newInputStream(file.toPath())) {
         BufferedImage image = ImageIO.read(in);
         if (image != null) {
            ResourceLocation location = new ResourceLocation("augustus", "pictures/" + str);
            Minecraft.getMinecraft().getTextureManager().loadTexture(location, new DynamicTexture(image));
            return new EspOverlayImage(image, location, null);
         }

         System.err.println("Failed to load image from " + file);
      } catch (IOException e) {
         e.printStackTrace();
      }

      return null;
   }

   private void refreshOverlayImages() {
      File file = new File(ClientPaths.CLIENT_DIR + File.separator + "pictures");
      if (!file.exists()) {
         file.mkdirs();
      }

      File[] files = file.listFiles(file2 -> {
         return file2.isFile() && (file2.getName().endsWith(".png") || file2.getName().endsWith(".jpg") || file2.getName().endsWith(".jpeg"));
      });
      HashSet set = new HashSet();
      if (files != null) {
         for (File file3 : files) {
            set.add(file3.getName());
         }
      }

      for (String str : (Iterable<String>)(set)) {
         EspOverlayImage espOverlayImage2 = (EspOverlayImage)this.loadedOverlayImages.get(str);
         if (espOverlayImage2 == null) {
            espOverlayImage2 = this.loadOverlayImage(str);
            if (espOverlayImage2 != null) {
               this.loadedOverlayImages.put(str, espOverlayImage2);
            }
         }
      }

      this.loadedOverlayImages.entrySet().removeIf(obj -> {
         return !set.contains(((Map.Entry)obj).getKey());
      });
      BooleansSetting picturePictures2 = this.picturePictures;
      String[] strings = ((Collection<String>)set).stream().sorted(Comparator.comparing(String::toLowerCase)).toArray(String[]::new);
      picturePictures2.setOptions(strings);
   }

   private void render3DCorners() {
      if (EspRenderModeOption.isOptionSelected(EspRenderModeOption.THREE_E_CORNER) && mc.theWorld != null) {
         boolean isBlurActive = this.fakeCorner.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.fakeCorner.isShadowActive());
         BlurShadowManager.submitRenderTask(true, shaderRenderPass -> this.render3DCornersPass(shaderRenderPass));
      }
   }

   private void render3DCornersPass(ShaderRenderPass shaderRenderPass) {
      int n = 0;

      for (EntityLivingBase entity : (Iterable<EntityLivingBase>)(this.visibleEntities)) {
         ColorSetting color42 = this.color4;
         ColorSetting hitColor42 = this.hitColor4;
         boolean value = this.fakeCornerHitColorBool.getValue();
         boolean value2 = this.fakeCornerTeamColor.getValue();
         byte b = 1;
         boolean flag = value2;
         boolean flag2 = value;
         ColorSetting colorSetting = hitColor42;
         ColorSetting colorSetting2 = color42;
         int n2 = this.resolveEntityColor(entity, n, colorSetting2, colorSetting, flag2, flag, b);
         int n3 = this.fakeCorner.resolvePassColor(shaderRenderPass, n2, n);
         RenderUtil.drawEntityCornerEsp(entity, n3);
         n++;
      }
   }

   private void renderCollisionBoxes() {
      if (EspRenderModeOption.isOptionSelected(EspRenderModeOption.COLLISION_BOX) && mc.theWorld != null) {
         boolean isBlurActive = this.collisionBox2.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.collisionBox2.isShadowActive());
         BlurShadowManager.submitRenderTask(true, shaderRenderPass -> this.renderCollisionBoxesPass(shaderRenderPass));
      }
   }

   private void renderCollisionBoxesPass(ShaderRenderPass shaderRenderPass) {
      EspRenderUtil.beginColoredQuads();
      float renderPartialTicks = mc.getTimer().renderPartialTicks;

      for (int i = 0; i < this.visibleEntities.size(); i++) {
         EntityLivingBase entity = (EntityLivingBase)this.visibleEntities.get(i);
         EntityEspBox entityEspBox = entity.entityEspBox;
         ColorSetting color32 = this.color3;
         ColorSetting hitColor32 = this.hitColor3;
         boolean value = this.collisionBoxHitColorBool.getValue();
         boolean value2 = this.collisionBoxTeamColor.getValue();
         int value3 = (int)this.collisionBoxTeamColorAlpha.getValue();
         boolean flag = value2;
         boolean flag2 = value;
         ColorSetting colorSetting = hitColor32;
         ColorSetting colorSetting2 = color32;
         int n = this.resolveEntityColor(entity, i, colorSetting2, colorSetting, flag2, flag, value3);
         float alphaFloat = ColorUtil.getAlphaFloat(n);
         entityEspBox.setColor(n, alphaFloat);
         entityEspBox.updateBounds(renderPartialTicks);
         float f = 0.0F;
         float f2 = 0.0F;
         if (ClientManagers.moduleManager.hitBox.isToggled()) {
            f = (float)((double)f + ClientManagers.moduleManager.hitBox.expandXZ.getValue());
            f2 = (float)((double)f2 + ClientManagers.moduleManager.hitBox.expandY.getValue());
         }

         entityEspBox.setExpansion(f, f2, f2);
         if (alphaFloat > 0.0F) {
            EspRenderUtil.addFilledBox(entityEspBox);
         }
      }

      EspRenderUtil.drawTessellator();
      double d = this.collisionBoxLineWidth.getValue() / 2.0;
      if (d > 0.0) {
         EspRenderUtil.beginColoredLines();
         GL11.glLineWidth((float)d);

         for (EntityLivingBase entity2 : (Iterable<EntityLivingBase>)(this.visibleEntities)) {
            EntityEspBox entityEspBox2 = entity2.entityEspBox;
            float f3 = 1.0F;
            entityEspBox2.setAlpha(f3);
            EspRenderUtil.addOutlinedBox(entityEspBox2);
         }

         EspRenderUtil.drawTessellator();
      }
   }

   private void renderHitBoxesPass(ShaderRenderPass shaderRenderPass) {
      EspRenderUtil.beginColoredQuads();
      float renderPartialTicks = mc.getTimer().renderPartialTicks;

      for (int i = 0; i < this.visibleEntities.size(); i++) {
         EntityLivingBase entity = (EntityLivingBase)this.visibleEntities.get(i);
         EntityEspBox entityEspBox = entity.entityEspBox;
         ColorSetting color22 = this.color2;
         ColorSetting hitColor22 = this.hitColor2;
         boolean value = this.hitBoxHitColorBool.getValue();
         boolean value2 = this.hitBoxTeamColor.getValue();
         int value3 = (int)this.hitBoxTeamColorAlpha.getValue();
         boolean flag = value2;
         boolean flag2 = value;
         ColorSetting colorSetting = hitColor22;
         ColorSetting colorSetting2 = color22;
         int n = this.resolveEntityColor(entity, i, colorSetting2, colorSetting, flag2, flag, value3);
         float alphaFloat = ColorUtil.getAlphaFloat(n);
         entityEspBox.setColor(n, alphaFloat);
         entityEspBox.updateBounds(renderPartialTicks);
         float collisionBorderSize = entity.getCollisionBorderSize();
         float collisionBorderSize2 = entity.getCollisionBorderSize();
         if (ClientManagers.moduleManager.hitBox.isToggled()) {
            collisionBorderSize = (float)((double)collisionBorderSize + ClientManagers.moduleManager.hitBox.expandXZ.getValue());
            collisionBorderSize2 = (float)((double)collisionBorderSize2 + ClientManagers.moduleManager.hitBox.expandY.getValue());
         }

         entityEspBox.setExpansion(collisionBorderSize, collisionBorderSize2, collisionBorderSize2);
         EspRenderUtil.addFilledBox(entityEspBox);
      }

      EspRenderUtil.drawTessellator();
      double d = this.hitBoxLineWidth.getValue() / 2.0;
      if (d > 0.0) {
         EspRenderUtil.beginColoredLines();
         GL11.glLineWidth((float)d);

         for (EntityLivingBase entity2 : (Iterable<EntityLivingBase>)(this.visibleEntities)) {
            EntityEspBox entityEspBox2 = entity2.entityEspBox;
            float f = 1.0F;
            entityEspBox2.setAlpha(f);
            EspRenderUtil.addOutlinedBox(entityEspBox2);
         }

         EspRenderUtil.drawTessellator();
      }
   }

   private void renderBoxesPass(ShaderRenderPass shaderRenderPass) {
      EspRenderUtil.beginColoredQuads();
      float renderPartialTicks = mc.getTimer().renderPartialTicks;

      for (int i = 0; i < this.visibleEntities.size(); i++) {
         EntityLivingBase entity = (EntityLivingBase)this.visibleEntities.get(i);
         EntityEspBox entityEspBox = entity.entityEspBox;
         ColorSetting color62 = this.color6;
         ColorSetting hitColor6 = this.hitColor;
         boolean value = this.boxHitColorBool.getValue();
         boolean value2 = this.teamColorBox.getValue();
         int value3 = (int)this.teamColorAlphaBox.getValue();
         boolean flag = value2;
         boolean flag2 = value;
         ColorSetting colorSetting = hitColor6;
         ColorSetting colorSetting2 = color62;
         int n = this.resolveEntityColor(entity, i, colorSetting2, colorSetting, flag2, flag, value3);
         float alphaFloat = ColorUtil.getAlphaFloat(n);
         entityEspBox.setColor(n, alphaFloat);
         entityEspBox.updateBounds(renderPartialTicks);
         float f = 0.0F;
         float f2 = 0.12F;
         float f3 = 0.1F;
         entityEspBox.setExpansion(f3, f2, f);
         EspRenderUtil.addRotatedFilledBox(entityEspBox);
      }

      EspRenderUtil.drawTessellator();
      double d = this.boxLineWidth.getValue() / 2.0;
      if (d > 0.0) {
         GL11.glLineWidth((float)d);
         EspRenderUtil.beginColoredLines();

         for (EntityLivingBase entity2 : (Iterable<EntityLivingBase>)(this.visibleEntities)) {
            EntityEspBox entityEspBox2 = entity2.entityEspBox;
            float f4 = 1.0F;
            entityEspBox2.setAlpha(f4);
            EspRenderUtil.addRotatedOutlinedBox(entityEspBox2);
         }

         EspRenderUtil.drawTessellator();
      }
   }

   private void renderPictureOverlaysPass(ShaderRenderPass shaderRenderPass) {
      boolean flag = true;
      this.glStateSaver.setBlend(flag);
      boolean flag2 = true;
      this.glStateSaver.setAlpha(flag2);
      GlStateManager.resetColor();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      EspRenderUtil.beginTexturedQuads();
      mc.getTextureManager().bindTexture(EspOverlayImage.getResourceLocation(this.espOverlayImage));

      for (EntityLivingBase entity : (Iterable<EntityLivingBase>)(this.visibleEntities)) {
         ProjectedBounds2D projectedBounds2D = (ProjectedBounds2D)this.projectedEntityBounds.get(entity);
         if (projectedBounds2D != null) {
            double d = (double)this.espOverlayImage.getWidth() / (double)this.espOverlayImage.getHeight();
            double value = this.scalePicture.getValue();
            double d2 = (projectedBounds2D.maxX - projectedBounds2D.minX) * value;
            double d3 = (projectedBounds2D.maxY - projectedBounds2D.minY) * value;
            double d4 = projectedBounds2D.minX + (projectedBounds2D.maxX - projectedBounds2D.minX) / 2.0;
            double d5 = projectedBounds2D.minY + (projectedBounds2D.maxY - projectedBounds2D.minY) / 2.0;
            double d6 = d2;
            double d7 = d3;
            if (!this.stretchPicture.getValue()) {
               d6 = d2;
               d7 = d2 / d;
               if (d7 > d3) {
                  d7 = d3;
                  d6 = d3 * d;
               }
            }

            double d8 = d4 - d2 / 2.0 + (d2 - d6) / 2.0;
            double d9 = d5 - d3 / 2.0 + (d3 - d7) * (this.pictureYPositionPercentage.getValue() / 100.0);
            EspRenderUtil.addTexturedQuad(d8, d9, 0.0F, 0.0F, d6, d7, (float)d6, (float)d7);
         }
      }

      EspRenderUtil.drawTessellator();
      this.glStateSaver.restoreBlend();
      this.glStateSaver.restoreAlpha();
   }

   private void render2DCorners() {
      if (EspRenderModeOption.CORNER.isSelected() && mc.theWorld != null) {
         boolean isBlurActive = this.corner2.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.corner2.isShadowActive());
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.render2DCornersPass(shaderRenderPass));
      }
   }

   private void render2DCornersPass(ShaderRenderPass shaderRenderPass) {
      boolean flag = true;
      this.glStateSaver.setBlend(flag);
      boolean flag2 = false;
      this.glStateSaver.setTexture2D(flag2);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glLineWidth((float)this.lineWidthCorner.getValue());
      EspRenderUtil.beginColoredLines();

      for (int i = 0; i < this.visibleEntities.size(); i++) {
         EntityLivingBase entity = (EntityLivingBase)this.visibleEntities.get(i);
         ProjectedBounds2D projectedBounds2D = (ProjectedBounds2D)this.projectedEntityBounds.get(entity);
         if (projectedBounds2D != null) {
            int n = entity.hurtTime != 0 && this.cornerHitColorBool.getValue()
               ? this.cornerHitColor.getColorInt(i)
               : (this.cornerTeamColor.getValue() && entity instanceof EntityPlayer ? ColorUtil.getTeamColor((EntityPlayer)entity) : this.color5.getColorInt(i));
            if (this.markMurderer.getValue()) {
               MurdererFinder murdererFinder = ClientManagers.moduleManager.murdererFinder;
               String name2 = entity.getName();
               if (murdererFinder.isMurderer(name2)) {
                  n = ColorUtil.packRgba(255, 0, 0, ColorUtil.getAlpha(n));
               }
            }

            EspRenderUtil.add2DCorners(
               projectedBounds2D.minX,
               projectedBounds2D.minY,
               projectedBounds2D.maxX,
               projectedBounds2D.maxY,
               MathHelper.clamp_double(this.horizontalPercentage.getValue(), 0.0, 100.0),
               MathHelper.clamp_double(this.verticalPercentage.getValue(), 0.0, 100.0),
               this.corner2.resolvePassColor(shaderRenderPass, n, i)
            );
         }
      }

      EspRenderUtil.drawTessellator();
      this.glStateSaver.restoreTexture2D();
      this.glStateSaver.restoreBlend();
   }

   private void renderReal2DBoxes() {
      if (EspRenderModeOption.REAL_2D.isSelected() && mc.theWorld != null) {
         boolean isBlurActive = this.real2D2.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.real2D2.isShadowActive());
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderReal2DBoxesPass(shaderRenderPass));
      }
   }

   private void renderReal2DBoxesPass(ShaderRenderPass shaderRenderPass) {

      for (int i = 0; i < this.visibleEntities.size(); i++) {
         EntityLivingBase entity = (EntityLivingBase)this.visibleEntities.get(i);
         ProjectedBounds2D projectedBounds2D = (ProjectedBounds2D)this.projectedEntityBounds.get(entity);
         if (projectedBounds2D != null) {
            int n = entity.hurtTime != 0 && this.real2DHitColorBool.getValue() ? this.fillColorHit.getColorInt(i) : this.fillColor.getColorInt(i);
            if (this.markMurderer.getValue()) {
               MurdererFinder murdererFinder = ClientManagers.moduleManager.murdererFinder;
               String name2 = entity.getName();
               if (murdererFinder.isMurderer(name2)) {
                  n = ColorUtil.packRgba(255, 0, 0, ColorUtil.getAlpha(n));
               }
            }

            if (shaderRenderPass != ShaderRenderPass.SHADOW) {
               RenderUtil.drawRect(projectedBounds2D.minX, projectedBounds2D.minY, projectedBounds2D.maxX, projectedBounds2D.maxY, n);
            }

            ColorSetting outlineColor2 = this.outlineColor;
            ColorSetting outlineColorHit2 = this.outlineColorHit;
            boolean value = this.real2DHitColorBool.getValue();
            boolean value2 = this.real2DTeamColor.getValue();
            int value3 = (int)this.real2DTeamColorAlpha.getValue();
            boolean flag = false;
            int n2 = value3;
            boolean flag2 = value2;
            boolean flag3 = value;
            ColorSetting colorSetting = outlineColorHit2;
            ColorSetting colorSetting2 = outlineColor2;
            int n3 = this.resolveEntityColor(entity, i, colorSetting2, colorSetting, flag3, flag2, n2, flag);
            if (shaderRenderPass != ShaderRenderPass.BLUR) {
               RenderUtil.drawRectOutline2(projectedBounds2D.minX, projectedBounds2D.minY, projectedBounds2D.maxX, projectedBounds2D.maxY, 1.0F, this.real2D2.resolvePassColor(shaderRenderPass, n3, i));
            }
         }
      }
   }

   private boolean isMurdererAlphaVisible() {
      return this.markMurderer.getValue() && this.customMurdererAlpha.getValue();
   }


}
