package net.optifine.entity.model.anim;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionBool;

public enum RenderEntityParameterBool implements IExpressionBool {
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
   private static final RenderEntityParameterBool[] VALUES = values();
@Override
   public ExpressionType getFloatValue() {
      return ExpressionType.BOOL;
   }

   public static RenderEntityParameterBool parse(String str) {
      if (str == null) {
         return null;
      } else {
         for (int i = 0; i < VALUES.length; i++) {
            RenderEntityParameterBool bool = VALUES[i];
            if (bool.getName().equals(str)) {
               return bool;
            }
         }

         return null;
      }
   }

   private RenderEntityParameterBool(String str) {
      this.name = str;
      this.renderManager = Minecraft.getMinecraft().getRenderManager();
   }

   public String getName() {
      return this.name;
   }

   @Override
   public boolean eval() {
      Render renderRender = this.renderManager.renderRender;
      if (renderRender == null) {
         return false;
      } else {
         if (renderRender instanceof RendererLivingEntity) {
            EntityLivingBase renderEntity = ((RendererLivingEntity)renderRender).renderEntity;
            if (renderEntity == null) {
               return false;
            }

            switch (RenderEntityParameterBool.RenderEntityParameterBoolSwitchMap.ORDINAL_MAP[this.ordinal()]) {
               case 1:
                  return renderEntity.isEntityAlive();
               case 2:
                  return renderEntity.isBurning();
               case 3:
                  return renderEntity.isChild();
               case 4:
                  return renderEntity.hurtTime > 0;
               case 5:
                  return renderEntity.isInLava();
               case 6:
                  return renderEntity.isInWater();
               case 7:
                  return renderEntity.isInvisible();
               case 8:
                  return renderEntity.onGround;
               case 9:
                  return renderEntity.riddenByEntity != null;
               case 10:
                  return renderEntity.isRiding();
               case 11:
                  return renderEntity.isSneaking();
               case 12:
                  return renderEntity.isSprinting();
               case 13:
                  return renderEntity.isWet();
            }
         }

         return false;
      }
   }


   public static class RenderEntityParameterBoolSwitchMap {
      public static final int[] ORDINAL_MAP = new int[RenderEntityParameterBool.values().length];

      static {
         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_ALIVE.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_BURNING.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_CHILD.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_HURT.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_IN_LAVA.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_IN_WATER.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_INVISIBLE.ordinal()] = 7;
         } catch (NoSuchFieldError e7) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_ON_GROUND.ordinal()] = 8;
         } catch (NoSuchFieldError e8) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_RIDDEN.ordinal()] = 9;
         } catch (NoSuchFieldError e9) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_RIDING.ordinal()] = 10;
         } catch (NoSuchFieldError e10) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_SNEAKING.ordinal()] = 11;
         } catch (NoSuchFieldError e11) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_SPRINTING.ordinal()] = 12;
         } catch (NoSuchFieldError e12) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterBool.IS_WET.ordinal()] = 13;
         } catch (NoSuchFieldError e13) {
         }
      }
   }
}
