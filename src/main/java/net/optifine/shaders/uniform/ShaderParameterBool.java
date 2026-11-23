package net.optifine.shaders.uniform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionBool;

public enum ShaderParameterBool implements IExpressionBool {
   IS_ALIVE("is_alive"),
   IS_BURNING("is_burning"),
   IS_CHILD("is_child"),
   IS_GLOWING("is_glowing"),
   IS_HURT("is_hurt"),
   IS_IN_LAVA("is_in_lava"),
   IS_IN_WATER("is_in_water"),
   IS_INVISIBLE("is_invisible"),
   IS_ON_GROUND("is_on_ground"),
   IS_RIDDEN("is_ridden"),
   IS_RIDING("is_riding"),
   IS_SNEAKING("is_sneaking"),
   IS_SPRINTING("is_sprinting"),
   IS_WET("is_wet");

private String name;
   private RenderManager renderManager;
   private static final ShaderParameterBool[] VALUES = values();
private ShaderParameterBool(String str) {
      this.name = str;
      this.renderManager = Minecraft.getMinecraft().getRenderManager();
   }

   public static ShaderParameterBool parse(String str) {
      if (str == null) {
         return null;
      } else {
         for (int i = 0; i < VALUES.length; i++) {
            ShaderParameterBool shaderParameterBool = VALUES[i];
            if (shaderParameterBool.getName().equals(str)) {
               return shaderParameterBool;
            }
         }

         return null;
      }
   }

   @Override
   public ExpressionType getFloatValue() {
      return ExpressionType.BOOL;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public boolean eval() {
      Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
      if (renderViewEntity instanceof EntityLivingBase) {
         EntityLivingBase entity = (EntityLivingBase)renderViewEntity;
         switch (ShaderParameterBoolSwitchMap.ORDINAL_MAP[this.ordinal()]) {
            case 1:
               return entity.isEntityAlive();
            case 2:
               return entity.isBurning();
            case 3:
               return entity.isChild();
            case 4:
               return entity.hurtTime > 0;
            case 5:
               return entity.isInLava();
            case 6:
               return entity.isInWater();
            case 7:
               return entity.isInvisible();
            case 8:
               return entity.onGround;
            case 9:
               return entity.riddenByEntity != null;
            case 10:
               return entity.isRiding();
            case 11:
               return entity.isSneaking();
            case 12:
               return entity.isSprinting();
            case 13:
               return entity.isWet();
         }
      }

      return false;
   }
}
