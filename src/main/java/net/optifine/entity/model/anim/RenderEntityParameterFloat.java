package net.optifine.entity.model.anim;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionFloat;

public enum RenderEntityParameterFloat implements IExpressionFloat {
   LIMB_SWING("limb_swing"),
   LIMB_SWING_SPEED("limb_speed"),
   AGE("age"),
   HEAD_YAW("head_yaw"),
   HEAD_PITCH("head_pitch"),
   SCALE("scale"),
   HEALTH("health"),
   HURT_TIME("hurt_time"),
   IDLE_TIME("idle_time"),
   MAX_HEALTH("max_health"),
   MOVE_FORWARD("move_forward"),
   MOVE_STRAFING("move_strafing"),
   PARTIAL_TICKS("partial_ticks"),
   POS_X("pos_x"),
   POS_Y("pos_y"),
   POS_Z("pos_z"),
   REVENGE_TIME("revenge_time"),
   SWING_PROGRESS("swing_progress");

private String name;
   private RenderManager renderManager;
   private static final RenderEntityParameterFloat[] VALUES = values();
public static RenderEntityParameterFloat parse(String str) {
      if (str == null) {
         return null;
      } else {
         for (int i = 0; i < VALUES.length; i++) {
            RenderEntityParameterFloat obj = VALUES[i];
            if (obj.getName().equals(str)) {
               return obj;
            }
         }

         return null;
      }
   }

   @Override
   public ExpressionType getFloatValue() {
      return ExpressionType.FLOAT;
   }

   @Override
   public float eval() {
      Render renderRender = this.renderManager.renderRender;
      if (renderRender == null) {
         return 0.0F;
      } else {
         if (renderRender instanceof RendererLivingEntity) {
            RendererLivingEntity rendererLivingEntity = (RendererLivingEntity)renderRender;
            switch (RenderEntityParameterFloat.RenderEntityParameterFloatSwitchMap.ORDINAL_MAP[this.ordinal()]) {
               case 12:
                  return rendererLivingEntity.renderLimbSwing;
               case 13:
                  return rendererLivingEntity.renderLimbSwingAmount;
               case 14:
                  return rendererLivingEntity.renderAgeInTicks;
               case 15:
                  return rendererLivingEntity.renderHeadYaw;
               case 16:
                  return rendererLivingEntity.renderHeadPitch;
               case 17:
                  return rendererLivingEntity.renderScaleFactor;
               default:
                  EntityLivingBase renderEntity = rendererLivingEntity.renderEntity;
                  if (renderEntity == null) {
                     return 0.0F;
                  }

                  switch (RenderEntityParameterFloat.RenderEntityParameterFloatSwitchMap.ORDINAL_MAP[this.ordinal()]) {
                     case 1:
                        return renderEntity.getCurrentHealth();
                     case 2:
                        return (float)renderEntity.hurtTime;
                     case 3:
                        return (float)renderEntity.getAge();
                     case 4:
                        return renderEntity.getMaxHealth();
                     case 5:
                        return renderEntity.moveForward;
                     case 6:
                        return renderEntity.moveStrafing;
                     case 7:
                        return (float)renderEntity.posX;
                     case 8:
                        return (float)renderEntity.posY;
                     case 9:
                        return (float)renderEntity.posZ;
                     case 10:
                        return (float)renderEntity.getRevengeTimer();
                     case 11:
                        return renderEntity.getSwingProgress(rendererLivingEntity.renderPartialTicks);
                  }
            }
         }

         return 0.0F;
      }
   }

   private RenderEntityParameterFloat(String str) {
      this.name = str;
      this.renderManager = Minecraft.getMinecraft().getRenderManager();
   }

   public String getName() {
      return this.name;
   }


   public static class RenderEntityParameterFloatSwitchMap {
      public static final int[] ORDINAL_MAP = new int[RenderEntityParameterFloat.values().length];

      static {
         try {
            ORDINAL_MAP[RenderEntityParameterFloat.HEALTH.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.HURT_TIME.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.IDLE_TIME.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.MAX_HEALTH.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.MOVE_FORWARD.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.MOVE_STRAFING.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.POS_X.ordinal()] = 7;
         } catch (NoSuchFieldError e7) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.POS_Y.ordinal()] = 8;
         } catch (NoSuchFieldError e8) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.POS_Z.ordinal()] = 9;
         } catch (NoSuchFieldError e9) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.REVENGE_TIME.ordinal()] = 10;
         } catch (NoSuchFieldError e10) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.SWING_PROGRESS.ordinal()] = 11;
         } catch (NoSuchFieldError e11) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.LIMB_SWING.ordinal()] = 12;
         } catch (NoSuchFieldError e12) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.LIMB_SWING_SPEED.ordinal()] = 13;
         } catch (NoSuchFieldError e13) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.AGE.ordinal()] = 14;
         } catch (NoSuchFieldError e14) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.HEAD_YAW.ordinal()] = 15;
         } catch (NoSuchFieldError e15) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.HEAD_PITCH.ordinal()] = 16;
         } catch (NoSuchFieldError e16) {
         }

         try {
            ORDINAL_MAP[RenderEntityParameterFloat.SCALE.ordinal()] = 17;
         } catch (NoSuchFieldError e17) {
         }
      }
   }
}
