package net.minecraft.client.renderer.vertex;


public class VertexBufferUsageSwitchMap {
   public static final VertexBufferUsageSwitchMap INSTANCE = new VertexBufferUsageSwitchMap();
   public final int[] usageOrdinalMap = new int[VertexFormatElement.EnumUsage.values().length];

   public VertexBufferUsageSwitchMap() {
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
   }
}
