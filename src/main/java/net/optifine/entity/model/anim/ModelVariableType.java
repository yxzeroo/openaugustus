package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.config.Config;

public enum ModelVariableType {
   POS_X("tx"),
   POS_Y("ty"),
   POS_Z("tz"),
   ANGLE_X("rx"),
   ANGLE_Y("ry"),
   ANGLE_Z("rz"),
   OFFSET_X("ox"),
   OFFSET_Y("oy"),
   OFFSET_Z("oz"),
   SCALE_X("sx"),
   SCALE_Y("sy"),
   SCALE_Z("sz");

private String name;
   public static ModelVariableType[] VALUES = values();
public static ModelVariableType parse(String str) {
      for (int i = 0; i < VALUES.length; i++) {
         ModelVariableType modelVariableType = VALUES[i];
         if (modelVariableType.getName().equals(str)) {
            return modelVariableType;
         }
      }

      return null;
   }

   public void setFloat(ModelRenderer modelRenderer, float f) {
      switch (ModelVariableType.ModelVariableTypeSwitchMap.ORDINAL_MAP[this.ordinal()]) {
         case 1:
            modelRenderer.rotationPointX = f;
            return;
         case 2:
            modelRenderer.rotationPointY = f;
            return;
         case 3:
            modelRenderer.rotationPointZ = f;
            return;
         case 4:
            modelRenderer.rotateAngleX = f;
            return;
         case 5:
            modelRenderer.rotateAngleY = f;
            return;
         case 6:
            modelRenderer.rotateAngleZ = f;
            return;
         case 7:
            modelRenderer.offsetX = f;
            return;
         case 8:
            modelRenderer.offsetY = f;
            return;
         case 9:
            modelRenderer.offsetZ = f;
            return;
         case 10:
            modelRenderer.scaleX = f;
            return;
         case 11:
            modelRenderer.scaleY = f;
            return;
         case 12:
            modelRenderer.scaleZ = f;
            return;
         default:
            Config.warn("SetFloat not supported for: " + this);
      }
   }

   public String getName() {
      return this.name;
   }

   public float getFloat(ModelRenderer modelRenderer) {
      switch (ModelVariableType.ModelVariableTypeSwitchMap.ORDINAL_MAP[this.ordinal()]) {
         case 1:
            return modelRenderer.rotationPointX;
         case 2:
            return modelRenderer.rotationPointY;
         case 3:
            return modelRenderer.rotationPointZ;
         case 4:
            return modelRenderer.rotateAngleX;
         case 5:
            return modelRenderer.rotateAngleY;
         case 6:
            return modelRenderer.rotateAngleZ;
         case 7:
            return modelRenderer.offsetX;
         case 8:
            return modelRenderer.offsetY;
         case 9:
            return modelRenderer.offsetZ;
         case 10:
            return modelRenderer.scaleX;
         case 11:
            return modelRenderer.scaleY;
         case 12:
            return modelRenderer.scaleZ;
         default:
            Config.warn("GetFloat not supported for: " + this);
            return 0.0F;
      }
   }

   private ModelVariableType(String str) {
      this.name = str;
   }


   public static class ModelVariableTypeSwitchMap {
      public static final int[] ORDINAL_MAP = new int[ModelVariableType.values().length];

      static {
         try {
            ORDINAL_MAP[ModelVariableType.POS_X.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.POS_Y.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.POS_Z.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.ANGLE_X.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.ANGLE_Y.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.ANGLE_Z.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.OFFSET_X.ordinal()] = 7;
         } catch (NoSuchFieldError e7) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.OFFSET_Y.ordinal()] = 8;
         } catch (NoSuchFieldError e8) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.OFFSET_Z.ordinal()] = 9;
         } catch (NoSuchFieldError e9) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.SCALE_X.ordinal()] = 10;
         } catch (NoSuchFieldError e10) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.SCALE_Y.ordinal()] = 11;
         } catch (NoSuchFieldError e11) {
         }

         try {
            ORDINAL_MAP[ModelVariableType.SCALE_Z.ordinal()] = 12;
         } catch (NoSuchFieldError e12) {
         }
      }
   }
}
