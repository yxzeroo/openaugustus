package net.minecraft.client.renderer.vertex;


public class VertexElementTypeSwitchMap {
   public static final VertexElementTypeSwitchMap INSTANCE = new VertexElementTypeSwitchMap();
   public final int[] elementTypeOrdinalMap = new int[VertexFormatElement.EnumType.values().length];

   public VertexElementTypeSwitchMap() {
      try {
         this.elementTypeOrdinalMap[VertexFormatElement.EnumType.FLOAT.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.elementTypeOrdinalMap[VertexFormatElement.EnumType.UINT.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.elementTypeOrdinalMap[VertexFormatElement.EnumType.INT.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.elementTypeOrdinalMap[VertexFormatElement.EnumType.USHORT.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         this.elementTypeOrdinalMap[VertexFormatElement.EnumType.SHORT.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         this.elementTypeOrdinalMap[VertexFormatElement.EnumType.UBYTE.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }

      try {
         this.elementTypeOrdinalMap[VertexFormatElement.EnumType.BYTE.ordinal()] = 7;
      } catch (NoSuchFieldError e7) {
      }
   }
}
