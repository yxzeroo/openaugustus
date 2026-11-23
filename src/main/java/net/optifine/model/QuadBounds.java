package net.optifine.model;

import net.minecraft.util.EnumFacing;

public class QuadBounds {
   private float minX = Float.MAX_VALUE;
   private float minY = Float.MAX_VALUE;
   private float minZ = Float.MAX_VALUE;
   private float maxX = -Float.MAX_VALUE;
   private float maxY = -Float.MAX_VALUE;
   private float maxZ = -Float.MAX_VALUE;

   public boolean isFaceQuad(EnumFacing facing) {
      float f;
      float f2;
      float f3;
      switch (QuadBounds.EnumFacingSwitchMap.ORDINAL_MAP[facing.ordinal()]) {
         case 1:
            f = this.getMinY();
            f2 = this.getMaxY();
            f3 = 0.0F;
            break;
         case 2:
            f = this.getMinY();
            f2 = this.getMaxY();
            f3 = 1.0F;
            break;
         case 3:
            f = this.getMinZ();
            f2 = this.getMaxZ();
            f3 = 0.0F;
            break;
         case 4:
            f = this.getMinZ();
            f2 = this.getMaxZ();
            f3 = 1.0F;
            break;
         case 5:
            f = this.getMinX();
            f2 = this.getMaxX();
            f3 = 0.0F;
            break;
         case 6:
            f = this.getMinX();
            f2 = this.getMaxX();
            f3 = 1.0F;
            break;
         default:
            return false;
      }

      return f == f3 && f2 == f3;
   }

   public boolean isFullFace(EnumFacing facing) {
      float f;
      float f2;
      float f3;
      float f4;
      switch (QuadBounds.EnumFacingSwitchMap.ORDINAL_MAP[facing.ordinal()]) {
         case 1:
         case 2:
            f = this.getMinX();
            f2 = this.getMaxX();
            f3 = this.getMinZ();
            f4 = this.getMaxZ();
            break;
         case 3:
         case 4:
            f = this.getMinX();
            f2 = this.getMaxX();
            f3 = this.getMinY();
            f4 = this.getMaxY();
            break;
         case 5:
         case 6:
            f = this.getMinY();
            f2 = this.getMaxY();
            f3 = this.getMinZ();
            f4 = this.getMaxZ();
            break;
         default:
            return false;
      }

      return f == 0.0F && f2 == 1.0F && f3 == 0.0F && f4 == 1.0F;
   }

   public float getMinX() {
      return this.minX;
   }

   public float getMaxX() {
      return this.maxX;
   }

   public float getMaxY() {
      return this.maxY;
   }

   public float getMinZ() {
      return this.minZ;
   }

   public float getMinY() {
      return this.minY;
   }

   public QuadBounds(int[] ints) {
      int n = ints.length / 4;

      for (int i = 0; i < 4; i++) {
         int n2 = i * n;
         float f = Float.intBitsToFloat(ints[n2 + 0]);
         float f2 = Float.intBitsToFloat(ints[n2 + 1]);
         float f3 = Float.intBitsToFloat(ints[n2 + 2]);
         if (this.minX > f) {
            this.minX = f;
         }

         if (this.minY > f2) {
            this.minY = f2;
         }

         if (this.minZ > f3) {
            this.minZ = f3;
         }

         if (this.maxX < f) {
            this.maxX = f;
         }

         if (this.maxY < f2) {
            this.maxY = f2;
         }

         if (this.maxZ < f3) {
            this.maxZ = f3;
         }
      }
   }

   public float getMaxZ() {
      return this.maxZ;
   }


   public static class EnumFacingSwitchMap {
      public static final int[] ORDINAL_MAP = new int[EnumFacing.values().length];

      static {
         try {
            ORDINAL_MAP[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }
      }
   }
}
