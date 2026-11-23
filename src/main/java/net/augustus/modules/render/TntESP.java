package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.math.NumberFormatUtil;
import net.augustus.render.RenderUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.glu.Sphere;

public class TntESP extends Module implements RenderListener, TickListener {
   private final java.util.ArrayList visibleTntEntities;
   private final ColorSetting radiusColor;
   private final BooleanValue deathCircle;
   private final ColorSetting deathCircleColor;
   private final StringValue smoothness;
   private final BooleanValue displayDamage;
   private final StringValue xCoord;
   private final StringValue yCoord;
   private double estimatedTotalDamage;
   private final Frustum frustum;
   private final DamageSource explosionDamageSource;

   public TntESP() {
       super("TntESP", "TntESP", new Color(109, 199, 117), Categorys.RENDER);
      this.visibleTntEntities = new java.util.ArrayList();
      this.radiusColor = new ColorSetting(this, "Color", new Color(255, 255, 255, 40));
      this.deathCircle = new BooleanValue(this, "DeathCircle", true);
      ColorSetting colorSetting = new ColorSetting(this, "DeathColor", new Color(255, 0, 0, 40));
      BooleanValue deathCircle2 = this.deathCircle;
      this.deathCircle.getClass();
      BooleanValue booleanValue = deathCircle2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.deathCircleColor = (ColorSetting)colorSetting.withVisibility(condition);
      this.smoothness = new StringValue(this, "Smoothness", 32.0, 8.0, 60.0, 0);
      this.displayDamage = new BooleanValue(this, "DisplayDamage", true);
      StringValue stringValue = new StringValue(this, "X-Coord", 50.0, 0.0, 100.0, 2);
      deathCircle2 = this.displayDamage;
      this.displayDamage.getClass();
      BooleanValue booleanValue2 = deathCircle2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.xCoord = stringValue.visibleWhen(condition2);
      StringValue stringValue2 = new StringValue(this, "Y-Coord", 45.0, 0.0, 100.0, 2);
      deathCircle2 = this.displayDamage;
      this.displayDamage.getClass();
      BooleanValue booleanValue3 = deathCircle2;
      SettingVisibilityCondition condition3 = () -> booleanValue3.getValue();
      this.yCoord = stringValue2.visibleWhen(condition3);
      this.estimatedTotalDamage = 0.0;
      this.frustum = new Frustum();
      this.explosionDamageSource = new DamageSource("explosion");
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
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onTick3() {
      this.visibleTntEntities.clear();
      this.estimatedTotalDamage = 0.0;
      if (mc.theWorld != null && mc.thePlayer != null) {
         for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
            if (entity instanceof EntityTNTPrimed) {
               EntityTNTPrimed entityTNTPrimed = (EntityTNTPrimed)entity;
               this.frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
               if (this.frustum.isBoundingBoxInFrustum(entity.getEntityBoundingBox().expand(9.0, 9.0, 9.0)) && !this.visibleTntEntities.contains(entity)) {
                  this.visibleTntEntities.add(entityTNTPrimed);
                  this.estimatedTotalDamage = this.estimatedTotalDamage + this.calculateExplosionDamage(entityTNTPrimed);
               }
            }
         }
      }
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.displayDamage.getValue() && this.estimatedTotalDamage > 0.0) {
         String str = "§cDamage: §7" + NumberFormatUtil.formatDecimal(this.estimatedTotalDamage, 3);
         int n = (int)((double)(sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT) / 100.0 * this.yCoord.getValue());
         int n2 = (int)((double)(sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(str)) / 100.0 * this.xCoord.getValue());
         mc.fontRendererObj.drawString(str, n2, n, Color.white.getRGB());
      }

      RenderUtil.resetGlColorState();
   }

   @Override
   public void onRender(float f) {
      RenderUtil.begin3DRender();
      int value = (int)this.smoothness.getValue();
      byte b = 0;
      int colorInt = this.radiusColor.getColorInt(b);
      int n;
      if (this.deathCircle.getValue()) {
         byte b2 = 0;
         n = this.deathCircleColor.getColorInt(b2);
      } else {
         n = 0;
      }

      int n2 = n;

      for (EntityTNTPrimed entityTNTPrimed : (Iterable<EntityTNTPrimed>)(this.visibleTntEntities)) {
         GlStateManager.pushMatrix();
         float f2 = entityTNTPrimed.height / 16.0F;
         float f3 = (float)(entityTNTPrimed.lastTickPosX + (entityTNTPrimed.posX - entityTNTPrimed.lastTickPosX) * (double)mc.getTimer().renderPartialTicks - mc.getRenderManager().getRenderPosX());
         float f4 = (float)(
            entityTNTPrimed.lastTickPosY
               + (double)f2
               + (entityTNTPrimed.posY + (double)f2 - (entityTNTPrimed.lastTickPosY + (double)f2)) * (double)mc.getTimer().renderPartialTicks
               - mc.getRenderManager().getRenderPosY()
         );
         float f5 = (float)(entityTNTPrimed.lastTickPosZ + (entityTNTPrimed.posZ - entityTNTPrimed.lastTickPosZ) * (double)mc.getTimer().renderPartialTicks - mc.getRenderManager().getRenderPosZ());
         GlStateManager.translate(f3, f4, f5);
         Sphere sphere = new Sphere();
         sphere.setDrawStyle(100011);
         Sphere sphere2 = new Sphere();
         sphere2.setDrawStyle(100012);
         if (this.deathCircle.getValue()) {
            RenderUtil.setGlColor(n2);
            float f6 = (float)this.calculateLethalRadius();
            sphere.draw(f6, value, value);
            sphere2.draw(f6, value, value);
         }

         RenderUtil.setGlColor(colorInt);
         sphere.draw(8.0F, value, value);
         sphere2.draw(8.0F, value, value);
         GlStateManager.popMatrix();
      }

      RenderUtil.end3DRender();
   }

   private double calculateLethalRadius() {
      float f = 8.0F;
      if (!mc.thePlayer.isImmuneToExplosions()) {
         for (float i = 0.0F; i < 8.0F; i += 0.1F) {
            double d = (double)i / (double)f;
            if (d <= 1.0) {
               double d2 = (1.0 - d) * 1.0;
               float currentHealth = mc.thePlayer.getCurrentHealth();
               DamageSource damageSource = this.explosionDamageSource.setDifficultyScaled().setExplosion();
               float f2 = (float)((int)((d2 * d2 + d2) / 2.0 * 8.0 * (double)f + 1.0));
               boolean flag = true;
               float f3 = f2;
               DamageSource damageSource2 = damageSource;
               float f4 = currentHealth - this.calculateFinalDamage(damageSource2, f3, flag);
               if (f4 > 0.0F) {
                  return (double)i;
               }
            }
         }
      }

      return 0.0;
   }

   public double calculateExplosionDamage(EntityTNTPrimed entityTNTPrimed) {
      float f = 8.0F;
      double d = entityTNTPrimed.posY + (double)(entityTNTPrimed.height / 16.0F);
      if (!mc.thePlayer.isImmuneToExplosions()) {
         double d2 = mc.thePlayer.getDistance(entityTNTPrimed.posX, d, entityTNTPrimed.posZ) / (double)f;
         if (d2 <= 1.0) {
            double d3 = mc.thePlayer.posX - entityTNTPrimed.posX;
            double d4 = mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - d;
            double d5 = mc.thePlayer.posZ - entityTNTPrimed.posZ;
            double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
            if (d6 != 0.0) {
               double blockDensity = (double)mc.theWorld.getBlockDensity(entityTNTPrimed.posX, d, entityTNTPrimed.posZ, mc.thePlayer.getEntityBoundingBox());
               double d7 = (1.0 - d2) * blockDensity;
               DamageSource damageSource = this.explosionDamageSource.setDifficultyScaled().setExplosion();
               float f2 = (float)((int)((d7 * d7 + d7) / 2.0 * 8.0 * (double)f + 1.0));
               boolean flag = true;
               float f3 = f2;
               DamageSource damageSource2 = damageSource;
               return (double)this.calculateFinalDamage(damageSource2, f3, flag);
            }
         }
      }

      return 0.0;
   }

   public float calculateFinalDamage(DamageSource damageSource, float f, boolean flag) {
      if (!damageSource.isUnblockable() && mc.thePlayer.isBlocking() && f > 0.0F) {
         f = (1.0F + f) * 0.5F;
      }

      EntityPlayerSP thePlayer = mc.thePlayer;
      f = this.applyArmorDamageReduction(damageSource, f, thePlayer);
      f = this.applyPotionAndEnchantmentReduction(damageSource, f, flag);
      return Math.max(f - mc.thePlayer.getAbsorptionAmount(), 0.0F);
   }

   public float applyPotionAndEnchantmentReduction(DamageSource damageSource, float f, boolean flag) {
      if (damageSource.isDamageAbsolute()) {
         return f;
      } else {
         if (mc.thePlayer.isPotionActive(Potion.resistance) && damageSource != DamageSource.outOfWorld) {
            int n = (mc.thePlayer.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            int n2 = 25 - n;
            float f2 = f * (float)n2;
            f = f2 / 25.0F;
         }

         if (f <= 0.0F) {
            return 0.0F;
         } else {
            int n3 = 0;
            EnchantmentHelper.enchantmentModifierDamage.source = damageSource;

            for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
               ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
               if (stack != null && stack.getItem() instanceof ItemArmor) {
                  float f3 = 0.0F;
                  if (EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) > 0) {
                     int damageModifier = EnchantmentHelper.enchantmentModifierDamage
                        .getDamageModifier(Enchantment.protection, EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack));
                     f3 = (float)(damageModifier + 1 >> 1) + (float)((damageModifier >> 1) + 1) / (flag ? 1.0F : 2.0F);
                  }

                  n3 = (int)((float)n3 + f3);
               }
            }

            if (n3 > 20) {
               n3 = 20;
            }

            if (n3 > 0) {
               int n4 = 25 - n3;
               float f4 = f * (float)n4;
               f = f4 / 25.0F;
            }

            return f;
         }
      }
   }

   private float applyArmorDamageReduction(DamageSource damageSource, float f, EntityPlayer player) {
      if (!damageSource.isUnblockable()) {
         float f2 = 25.0F - this.getTotalArmorPoints();
         float f3 = f * f2;
         f = f3 / 25.0F;
      }

      return f;
   }

   public float getTotalArmorPoints() {
      float f = 0.0F;

      for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
         ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
         if (stack != null && stack.getItem() instanceof ItemArmor) {
            ItemArmor item = (ItemArmor)stack.getItem();
            float damageReduceAmount = (float)item.damageReduceAmount;
            f += damageReduceAmount;
         }
      }

      return f;
   }

}
