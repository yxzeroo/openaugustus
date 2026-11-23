package net.augustus.modules.render;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.awt.Color;
import java.util.HashMap;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.render.RenderPassTask;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.render.ProjectedBounds2D;
import net.augustus.render.ProjectionUtil;
import net.augustus.settings.SettingChangeListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.options.StorageEspRenderModeOption;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class StorageESP extends Module implements RenderListener, TickListener, LoadWorldListener {
   private final MultiOptionSetting modes;
   private final DoubleValue box;
   private final ColorSetting chestColor;
   private final ColorSetting enderChestColor;
   private final StringValue boxLineWidth;
   private final StringValue boxExpand;
   private final StringValue boxPriority;
   private final BlurShadowSettings boxShader;
   private final DoubleValue blockBox;
   private final ColorSetting chestColor2;
   private final ColorSetting enderChestColor2;
   private final StringValue blockBoxLineWidth;
   private final StringValue blockBoxPriority;
   private final BlurShadowSettings blockBoxShader;
   private final DoubleValue n3DCorner;
   private final ColorSetting chestColor3;
   private final ColorSetting enderChestColor3;
   private final StringValue corner3DPriority;
   private final BlurShadowSettings corner3DShader;
   private final DoubleValue real2D;
   public final ColorSetting chestColor1;
   public final ColorSetting chestColor22;
   public final ColorSetting enderChestColor1;
   public final ColorSetting enderChestColor22;
   private final StringValue real2DPriority;
   private final BlurShadowSettings real2DShader;
   private final DoubleValue corner;
   public final ColorSetting chestColor4;
   public final ColorSetting enderChestColor4;
   public final StringValue cornerLineWidth;
   public final StringValue cornerHorizontalPercentage;
   public final StringValue cornerVerticalPercentage;
   private final StringValue cornerPriority;
   private final BlurShadowSettings cornerShader;
   public final StringValue maxDistance;
   public final HashMap projectedTileBounds;
   private final java.util.ArrayList visibleTileEntities;
   private final Frustum frustum;
   private final Int2ObjectAVLTreeMap render2DPassesByPriority;
   private final Int2ObjectAVLTreeMap render3DPassesByPriority;

   public StorageESP() {
       super("StorageESP", "SpeicherRöntgenBlick", new Color(100, 166, 148, 255), Categorys.RENDER);
      this.modes = new MultiOptionSetting(this, "Modes", StorageEspRenderModeOption.values());
      DoubleValue doubleValue = new DoubleValue(this, "Box", true);
      SettingVisibilityCondition condition = StorageEspRenderModeOption.BOX::isSelected;
      this.box = doubleValue.visibleWhen(condition);
      Color color2 = new Color(21, 121, 230, 65);
      DoubleValue box2 = this.box;
      Color color3 = color2;
      String str = "BoxChestColor";
      ColorSetting colorSetting = new ColorSetting(this, str, color3, box2);
      String str2 = "ChestColor";
      this.chestColor = (ColorSetting)colorSetting.withDisplayName(str2);
      color2 = new Color(21, 121, 230, 65);
      box2 = this.box;
      color3 = color2;
      str = "BoxEnderChestColor";
      colorSetting = new ColorSetting(this, str, color3, box2);
      String str3 = "EnderChestColor";
      this.enderChestColor = (ColorSetting)colorSetting.withDisplayName(str3);
      StringValue stringValue = new StringValue(this, "BoxLineWidth", 2.0, 0.0, 12.0, 0, this.box);
      String str4 = "LineWidth";
      this.boxLineWidth = stringValue.named(str4);
      StringValue stringValue2 = new StringValue(this, "BoxExpand", 0.0, -1.0, 1.0, 4, this.box);
      String str5 = "Expand";
      this.boxExpand = stringValue2.named(str5);
      StringValue stringValue3 = new StringValue(this, "BoxPriority", 0.0, 0.0, 2.0, 0, this.box);
      String str6 = "3DPriority";
      StringValue stringValue4 = stringValue3.named(str6);
      SettingChangeListener listener = () -> this.rebuild3DRenderQueue();
      SettingChangeListener listener2 = listener;
      this.boxPriority = (StringValue)stringValue4.withChangeListener(listener2);
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, "BoxShader", this.box);
      Color black = Color.black;
      this.boxShader = blurShadowSettings.withCustomShadowColor(black).disableBlurByDefault();
      DoubleValue doubleValue2 = new DoubleValue(this, "BlockBox", true);
      SettingVisibilityCondition condition2 = StorageEspRenderModeOption.BLOCK_BOX::isSelected;
      this.blockBox = doubleValue2.visibleWhen(condition2);
      color2 = new Color(21, 121, 230, 65);
      box2 = this.blockBox;
      color3 = color2;
      str = "BlockBoxChestColor";
      colorSetting = new ColorSetting(this, str, color3, box2);
      String str7 = "ChestColor";
      this.chestColor2 = (ColorSetting)colorSetting.withDisplayName(str7);
      color2 = new Color(21, 121, 230, 65);
      box2 = this.blockBox;
      color3 = color2;
      str = "BlockBoxEnderChestColor";
      colorSetting = new ColorSetting(this, str, color3, box2);
      String str8 = "EnderChestColor";
      this.enderChestColor2 = (ColorSetting)colorSetting.withDisplayName(str8);
      StringValue stringValue5 = new StringValue(this, "BlockBoxLineWidth", 2.0, 0.0, 12.0, 0, this.blockBox);
      String str9 = "LineWidth";
      this.blockBoxLineWidth = stringValue5.named(str9);
      StringValue stringValue6 = new StringValue(this, "BlockBoxPriority", 1.0, 0.0, 2.0, 0, this.blockBox);
      String str10 = "3DPriority";
      StringValue stringValue7 = stringValue6.named(str10);
      listener = () -> this.rebuild3DRenderQueue();
      SettingChangeListener listener3 = listener;
      this.blockBoxPriority = (StringValue)stringValue7.withChangeListener(listener3);
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this, "BlockBoxShader", this.blockBox);
      Color black2 = Color.black;
      this.blockBoxShader = blurShadowSettings2.withCustomShadowColor(black2).disableBlurByDefault();
      DoubleValue doubleValue3 = new DoubleValue(this, "3DCorner", true);
      SettingVisibilityCondition condition3 = StorageEspRenderModeOption.CORNER_3D::isSelected;
      this.n3DCorner = doubleValue3.visibleWhen(condition3);
      color2 = new Color(21, 121, 230, 65);
      box2 = this.n3DCorner;
      color3 = color2;
      str = "Corner3ChestColor";
      colorSetting = new ColorSetting(this, str, color3, box2);
      String str11 = "ChestColor";
      this.chestColor3 = (ColorSetting)colorSetting.withDisplayName(str11);
      Color color4 = new Color(21, 121, 230, 65);
      box2 = this.n3DCorner;
      color3 = color4;
      str = "Corner3EnderChestColor";
      ColorSetting colorSetting2 = new ColorSetting(this, str, color3, box2);
      String str12 = "EnderChestColor";
      this.enderChestColor3 = (ColorSetting)colorSetting2.withDisplayName(str12);
      StringValue stringValue8 = new StringValue(this, "Corner3DPriority", 2.0, 0.0, 2.0, 0, this.n3DCorner);
      String str13 = "3DPriority";
      StringValue stringValue9 = stringValue8.named(str13);
      SettingChangeListener listener4 = () -> this.rebuild3DRenderQueue();
      SettingChangeListener listener5 = listener4;
      this.corner3DPriority = (StringValue)stringValue9.withChangeListener(listener5);
      BlurShadowSettings blurShadowSettings3 = new BlurShadowSettings(this, "Corner3DShader", this.n3DCorner);
      Color black3 = Color.black;
      this.corner3DShader = blurShadowSettings3.withCustomShadowColor(black3).disableBlurByDefault();
      DoubleValue doubleValue4 = new DoubleValue(this, "Real2D", true);
      SettingVisibilityCondition condition4 = StorageEspRenderModeOption.REAL_2D::isSelected;
      this.real2D = doubleValue4.visibleWhen(condition4);
      color4 = new Color(21, 121, 230, 65);
      box2 = this.real2D;
      color3 = color4;
      str = "Real2DColorChest1";
      ColorSetting colorSetting3 = new ColorSetting(this, str, color3, box2);
      String str14 = "ChestColor1";
      this.chestColor1 = (ColorSetting)colorSetting3.withDisplayName(str14);
      color4 = new Color(21, 121, 230);
      box2 = this.real2D;
      color3 = color4;
      str = "Real2DColorChest2";
      ColorSetting colorSetting4 = new ColorSetting(this, str, color3, box2);
      String str15 = "ChestColor2";
      this.chestColor22 = (ColorSetting)colorSetting4.withDisplayName(str15);
      color4 = new Color(66, 245, 164, 65);
      box2 = this.real2D;
      color3 = color4;
      str = "Real2DColorEnderChest1";
      ColorSetting colorSetting5 = new ColorSetting(this, str, color3, box2);
      String str16 = "EnderChestColor1";
      this.enderChestColor1 = (ColorSetting)colorSetting5.withDisplayName(str16);
      color4 = new Color(66, 245, 164);
      box2 = this.real2D;
      color3 = color4;
      str = "Real2DColorEnderChest2";
      ColorSetting colorSetting6 = new ColorSetting(this, str, color3, box2);
      String str17 = "EnderChestColor2";
      this.enderChestColor22 = (ColorSetting)colorSetting6.withDisplayName(str17);
      StringValue stringValue10 = new StringValue(this, "Real2DPriority", 0.0, 0.0, 1.0, 0, this.real2D);
      String str18 = "2DPriority";
      StringValue stringValue11 = stringValue10.named(str18);
      listener4 = () -> this.rebuild2DRenderQueue();
      SettingChangeListener listener6 = listener4;
      this.real2DPriority = (StringValue)stringValue11.withChangeListener(listener6);
      BlurShadowSettings blurShadowSettings4 = new BlurShadowSettings(this, "Real2DShader", this.real2D);
      Color black4 = Color.black;
      this.real2DShader = blurShadowSettings4.withCustomShadowColor(black4).disableBlurByDefault();
      DoubleValue doubleValue5 = new DoubleValue(this, "Corner", true);
      SettingVisibilityCondition condition5 = StorageEspRenderModeOption.CORNER::isSelected;
      this.corner = doubleValue5.visibleWhen(condition5);
      color4 = new Color(21, 121, 230);
      box2 = this.corner;
      color3 = color4;
      str = "CornerChestColor";
      ColorSetting colorSetting7 = new ColorSetting(this, str, color3, box2);
      String str19 = "ChestColor";
      this.chestColor4 = (ColorSetting)colorSetting7.withDisplayName(str19);
      color4 = new Color(66, 245, 164);
      box2 = this.corner;
      color3 = color4;
      str = "CornerEnderChestColor";
      ColorSetting colorSetting8 = new ColorSetting(this, str, color3, box2);
      String str20 = "EnderChestColor";
      this.enderChestColor4 = (ColorSetting)colorSetting8.withDisplayName(str20);
      StringValue stringValue12 = new StringValue(this, "CornerLineWidth", 2.0, 1.0, 5.0, 0, this.corner);
      String str21 = "LineWidth";
      this.cornerLineWidth = stringValue12.named(str21);
      this.cornerHorizontalPercentage = new StringValue(this, "CornerHorizontalPercentage", 50.0, 1.0, 100.0, 0, this.corner);
      StringValue stringValue13 = new StringValue(this, "CornerVerticalPercentage", 50.0, 1.0, 100.0, 0, this.corner);
      String str22 = "VerticalPercentage";
      this.cornerVerticalPercentage = stringValue13.named(str22);
      StringValue stringValue14 = new StringValue(this, "CornerPriority", 1.0, 0.0, 1.0, 0, this.corner);
      String str23 = "2DPriority";
      StringValue stringValue15 = stringValue14.named(str23);
      listener4 = () -> this.rebuild2DRenderQueue();
      SettingChangeListener listener7 = listener4;
      this.cornerPriority = (StringValue)stringValue15.withChangeListener(listener7);
      BlurShadowSettings blurShadowSettings5 = new BlurShadowSettings(this, "CornerShader", this.corner);
      Color black5 = Color.black;
      this.cornerShader = blurShadowSettings5.withCustomShadowColor(black5).disableBlurByDefault();
      this.maxDistance = new StringValue(this, "MaxDistance", 6.0, 0.0, 32.0, 0);
      this.projectedTileBounds = new HashMap();
      this.visibleTileEntities = new java.util.ArrayList();
      this.frustum = new Frustum();
      this.render2DPassesByPriority = new Int2ObjectAVLTreeMap();
      this.render3DPassesByPriority = new Int2ObjectAVLTreeMap();
      this.rebuild3DRenderQueue();
      this.rebuild2DRenderQueue();
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.visibleTileEntities.clear();
      this.projectedTileBounds.clear();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = -9;
      byte b2 = 32;
      this.toggleEventSubscription(b2, b);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
      byte b4 = 17;
      this.toggleEventSubscription(b4);
   }

   @Override
   public void onRender2(float f, ScaledResolution sr) {
      if (!this.visibleTileEntities.isEmpty()) {
         ObjectIterator it = this.render2DPassesByPriority.values().iterator();

         while (it.hasNext()) {
            Runnable runnable = (Runnable)it.next();
            runnable.run();
            RenderUtil.resetGlColorState();
         }
      }
   }

   @Override
   public void onTick2() {
      if (mc.theWorld != null) {
         this.visibleTileEntities.clear();
         this.frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

         for (TileEntity tileEntity : (Iterable<TileEntity>)(mc.theWorld.loadedTileEntityList)) {
            if ((tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest)
               && !(
                  mc.thePlayer.getDistance((double)tileEntity.getPos().getX(), (double)tileEntity.getPos().getY(), (double)tileEntity.getPos().getZ())
                     > this.maxDistance.getValue() * 16.0
               )
               && (!(tileEntity instanceof TileEntityChest) || ((TileEntityChest)tileEntity).adjacentChestXNeg == null && ((TileEntityChest)tileEntity).adjacentChestZNeg == null)
               )
             {
               this.addIfInFrustum(tileEntity);
            }
         }
      }
   }

   @Override
   public void onRender(float f) {
      if (!this.visibleTileEntities.isEmpty()) {
         ObjectIterator it = this.render3DPassesByPriority.values().iterator();

         while (it.hasNext()) {
            Runnable runnable = (Runnable)it.next();
            GlStateManager.pushMatrix();
            RenderUtil.begin3DRender();
            runnable.run();
            RenderUtil.end3DRender();
            GlStateManager.popMatrix();
         }

         if (!this.render3DPassesByPriority.isEmpty()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         }

         if (StorageEspRenderModeOption.REAL_2D.isSelected() || StorageEspRenderModeOption.CORNER.isSelected()) {
            ScaledResolution sr = new ScaledResolution(mc);

            for (TileEntity tileEntity : (Iterable<TileEntity>)(this.visibleTileEntities)) {
               AxisAlignedBB selectedBoundingBox = tileEntity.getBlockType().getSelectedBoundingBox(mc.theWorld, tileEntity.getPos());
               if (tileEntity instanceof TileEntityChest) {
                  TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
                  if (tileEntityChest.adjacentChestXNeg != null) {
                     selectedBoundingBox = selectedBoundingBox.union(tileEntityChest.adjacentChestXNeg.getBlockType().getSelectedBoundingBox(mc.theWorld, tileEntityChest.adjacentChestXNeg.getPos()));
                  } else if (tileEntityChest.adjacentChestZNeg != null) {
                     selectedBoundingBox = selectedBoundingBox.union(tileEntityChest.adjacentChestZNeg.getBlockType().getSelectedBoundingBox(mc.theWorld, tileEntityChest.adjacentChestZNeg.getPos()));
                  } else if (tileEntityChest.adjacentChestXPos != null) {
                     selectedBoundingBox = selectedBoundingBox.union(tileEntityChest.adjacentChestXPos.getBlockType().getSelectedBoundingBox(mc.theWorld, tileEntityChest.adjacentChestXPos.getPos()));
                  } else if (tileEntityChest.adjacentChestZPos != null) {
                     selectedBoundingBox = selectedBoundingBox.union(tileEntityChest.adjacentChestZPos.getBlockType().getSelectedBoundingBox(mc.theWorld, tileEntityChest.adjacentChestZPos.getPos()));
                  }
               }

               this.projectedTileBounds.put(tileEntity, ProjectionUtil.projectBoundingBox(selectedBoundingBox, sr));
            }
         }
      }
   }

   private void rebuild2DRenderQueue() {
      this.render2DPassesByPriority.clear();
      this.render2DPassesByPriority.put((int)this.real2DPriority.getValue(), (Runnable)() -> this.renderReal2DBoxes());
      this.render2DPassesByPriority.put((int)this.cornerPriority.getValue(), (Runnable)() -> this.render2DCorners());
   }

   private void rebuild3DRenderQueue() {
      this.render3DPassesByPriority.clear();
      this.render3DPassesByPriority.put((int)this.boxPriority.getValue(), (Runnable)() -> this.renderBoxes());
      this.render3DPassesByPriority.put((int)this.blockBoxPriority.getValue(), (Runnable)() -> this.renderBlockBoxes());
      this.render3DPassesByPriority.put((int)this.corner3DPriority.getValue(), (Runnable)() -> this.render3DCorners());
   }

   private void addIfInFrustum(TileEntity tileEntity) {
      BlockPos pos = tileEntity.getPos();
      if (this.frustum
         .isBoxInFrustum(
            (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1)
         )) {
         this.visibleTileEntities.add(tileEntity);
      }
   }

   private int resolveTileEntityColor(TileEntity tileEntity, ColorSetting colorSetting, ColorSetting colorSetting2, int n) {
      return tileEntity instanceof TileEntityEnderChest ? colorSetting2.getColorInt(n) : colorSetting.getColorInt(n);
   }

   private float scaleLineWidthByDistance(TileEntity tileEntity, StringValue stringValue) {
      float f = (float)(stringValue.getValue() / 2.0);
      if (mc.thePlayer.getDistance((double)tileEntity.getPos().getX(), (double)tileEntity.getPos().getY(), (double)tileEntity.getPos().getZ()) > 1.0) {
         float f2 = (float)(1.0 - mc.thePlayer.getDistance((double)tileEntity.getPos().getX(), (double)tileEntity.getPos().getY(), (double)tileEntity.getPos().getZ()) / 20.0);
         if ((double)f2 < 0.3) {
            f2 = 0.3F;
         }

         f *= f2;
      }

      return f;
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.visibleTileEntities.clear();
      this.projectedTileBounds.clear();
   }

   private void render3DCorners() {
      if (StorageEspRenderModeOption.CORNER_3D.isSelected()) {
         boolean isBlurActive = this.corner3DShader.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.corner3DShader.isShadowActive());
         RenderPassTask renderPassTask = shaderRenderPass -> this.render3DCornersPass(shaderRenderPass);
         boolean flag = true;
         BlurShadowManager.submitRenderTask(flag, renderPassTask);
      }
   }

   private void render3DCornersPass(ShaderRenderPass shaderRenderPass) {

      for (int i = 0; i < this.visibleTileEntities.size(); i++) {
         TileEntity tileEntity = (TileEntity)this.visibleTileEntities.get(i);
         ColorSetting enderChestColor32 = this.enderChestColor3;
         ColorSetting chestColor32 = this.chestColor3;
         int n = this.resolveTileEntityColor(tileEntity, chestColor32, enderChestColor32, i);
         int n2 = this.corner3DShader.resolvePassColor(shaderRenderPass, n, i);
         RenderUtil.drawTileEntityCornerEsp(tileEntity, n2);
      }
   }

   private void renderBlockBoxes() {
      if (StorageEspRenderModeOption.BLOCK_BOX.isSelected()) {
         boolean isBlurActive = this.blockBoxShader.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.blockBoxShader.isShadowActive());
         RenderPassTask renderPassTask = shaderRenderPass -> this.renderBlockBoxesPass(shaderRenderPass);
         boolean flag = true;
         BlurShadowManager.submitRenderTask(flag, renderPassTask);
      }
   }

   private void renderBlockBoxesPass(ShaderRenderPass shaderRenderPass) {

      for (int i = 0; i < this.visibleTileEntities.size(); i++) {
         TileEntity tileEntity = (TileEntity)this.visibleTileEntities.get(i);
         ColorSetting enderChestColor23 = this.enderChestColor2;
         ColorSetting chestColor23 = this.chestColor2;
         int n = this.resolveTileEntityColor(tileEntity, chestColor23, enderChestColor23, i);
         StringValue blockBoxLineWidth2 = this.blockBoxLineWidth;
         float f = this.scaleLineWidthByDistance(tileEntity, blockBoxLineWidth2);
         int n2 = this.blockBoxShader.resolvePassColor(shaderRenderPass, n, i);
         boolean flag = true;
         double d = 0.0;
         float f2 = 1.0F;
         int n3 = n2;
         RenderUtil.drawTileEntityEspBox(tileEntity, n3, f2, f, d, flag);
      }
   }

   private void renderBoxes() {
      if (StorageEspRenderModeOption.BOX.isSelected()) {
         boolean isBlurActive = this.boxShader.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.boxShader.isShadowActive());
         RenderPassTask renderPassTask = shaderRenderPass -> this.renderBoxesPass(shaderRenderPass);
         boolean flag = true;
         BlurShadowManager.submitRenderTask(flag, renderPassTask);
      }
   }

   private void renderBoxesPass(ShaderRenderPass shaderRenderPass) {

      for (int i = 0; i < this.visibleTileEntities.size(); i++) {
         TileEntity tileEntity = (TileEntity)this.visibleTileEntities.get(i);
         ColorSetting enderChestColor5 = this.enderChestColor;
         ColorSetting chestColor5 = this.chestColor;
         int n = this.resolveTileEntityColor(tileEntity, chestColor5, enderChestColor5, i);
         StringValue boxLineWidth2 = this.boxLineWidth;
         float f = this.scaleLineWidthByDistance(tileEntity, boxLineWidth2);
         int n2 = this.boxShader.resolvePassColor(shaderRenderPass, n, i);
         double value = this.boxExpand.getValue();
         boolean flag = false;
         double d = value;
         float f2 = 1.0F;
         int n3 = n2;
         RenderUtil.drawTileEntityEspBox(tileEntity, n3, f2, f, d, flag);
      }
   }

   private void render2DCorners() {
      if (StorageEspRenderModeOption.CORNER.isSelected()) {
         boolean isBlurActive = this.cornerShader.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.cornerShader.isShadowActive());
         RenderPassTask renderPassTask = shaderRenderPass -> this.render2DCornersPass(shaderRenderPass);
         boolean flag = false;
         BlurShadowManager.submitRenderTask(flag, renderPassTask);
      }
   }

   private void render2DCornersPass(ShaderRenderPass shaderRenderPass) {

      for (int i = 0; i < this.visibleTileEntities.size(); i++) {
         TileEntity tileEntity = (TileEntity)this.visibleTileEntities.get(i);
         ProjectedBounds2D projectedBounds2D = (ProjectedBounds2D)this.projectedTileBounds.get(tileEntity);
         int n = tileEntity instanceof TileEntityEnderChest ? this.enderChestColor4.getColorInt(i) : this.chestColor4.getColorInt(i);
         double minX = projectedBounds2D.minX;
         double minY = projectedBounds2D.minY;
         double maxX = projectedBounds2D.maxX;
         double maxY = projectedBounds2D.maxY;
         float value = (float)this.cornerLineWidth.getValue();
         double value2 = this.cornerHorizontalPercentage.getValue();
         double value3 = this.cornerVerticalPercentage.getValue();
         BlurShadowSettings cornerShader2 = this.cornerShader;
         int n2 = ColorUtil.resolvePassColor(shaderRenderPass, n);
         RenderUtil.drawCornerBrackets(minX, minY, maxX, maxY, value, value2, value3, cornerShader2.resolvePassColor(shaderRenderPass, n2, i));
      }
   }

   private void renderReal2DBoxes() {
      if (StorageEspRenderModeOption.REAL_2D.isSelected()) {
         boolean isBlurActive = this.real2DShader.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, this.real2DShader.isShadowActive());
         RenderPassTask renderPassTask = shaderRenderPass -> this.renderReal2DBoxesPass(shaderRenderPass);
         boolean flag = false;
         BlurShadowManager.submitRenderTask(flag, renderPassTask);
      }
   }

   private void renderReal2DBoxesPass(ShaderRenderPass shaderRenderPass) {

      for (int i = 0; i < this.visibleTileEntities.size(); i++) {
         TileEntity tileEntity = (TileEntity)this.visibleTileEntities.get(i);
         ProjectedBounds2D projectedBounds2D = (ProjectedBounds2D)this.projectedTileBounds.get(tileEntity);
         if (shaderRenderPass != ShaderRenderPass.SHADOW) {
            double minX = projectedBounds2D.minX;
            double minY = projectedBounds2D.minY;
            double maxX = projectedBounds2D.maxX;
            double maxY = projectedBounds2D.maxY;
            int n = tileEntity instanceof TileEntityEnderChest ? this.enderChestColor1.getColorInt(i) : this.chestColor1.getColorInt(i);
            double d = maxY;
            double d2 = maxX;
            double d3 = minY;
            RenderUtil.drawRect(minX, d3, d2, d, n);
         }

         if (shaderRenderPass != ShaderRenderPass.BLUR) {
            int n2 = tileEntity instanceof TileEntityEnderChest ? this.enderChestColor22.getColorInt(i) : this.chestColor22.getColorInt(i);
            double minX2 = projectedBounds2D.minX;
            double minY2 = projectedBounds2D.minY;
            double maxX2 = projectedBounds2D.maxX;
            double maxY2 = projectedBounds2D.maxY;
            BlurShadowSettings real2DShader2 = this.real2DShader;
            int n3 = ColorUtil.resolvePassColor(shaderRenderPass, n2);
            RenderUtil.drawRectOutline2(minX2, minY2, maxX2, maxY2, 1.0F, real2DShader2.resolvePassColor(shaderRenderPass, n3, i));
         }
      }
   }

}
