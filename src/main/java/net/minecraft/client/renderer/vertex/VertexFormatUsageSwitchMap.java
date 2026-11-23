package net.minecraft.client.renderer.vertex;


public class VertexFormatUsageSwitchMap {
   public static final VertexFormatUsageSwitchMap INSTANCE = new VertexFormatUsageSwitchMap();
   public final int[] usageOrdinalMap = new int[VertexFormatElement.EnumUsage.values().length];

   public VertexFormatUsageSwitchMap() {
      try {
         this.usageOrdinalMap[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.usageOrdinalMap[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.usageOrdinalMap[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.usageOrdinalMap[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
