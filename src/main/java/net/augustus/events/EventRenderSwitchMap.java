package net.augustus.events;

public class EventRenderSwitchMap {
   public static final int[] ORDINAL_MAP = new int[RenderPassType.values().length];

   static {
      try {
         ORDINAL_MAP[RenderPassType.RENDER3D.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[RenderPassType.RENDER2D_PRE.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[RenderPassType.RENDER2D_POST.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }
   }
}
