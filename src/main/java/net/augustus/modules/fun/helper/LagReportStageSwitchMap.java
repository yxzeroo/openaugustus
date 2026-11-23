package net.augustus.modules.fun.helper;

public class LagReportStageSwitchMap {
   public static final int[] ORDINAL_MAP = new int[LagReportStage.values().length];

   static {
      try {
         ORDINAL_MAP[LagReportStage.SEND_MESSAGE.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[LagReportStage.CLICK.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[LagReportStage.CLOSE.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }
   }
}
