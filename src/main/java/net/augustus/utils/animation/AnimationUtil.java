package net.augustus.utils.animation;

import net.minecraft.util.MathHelper;

public class AnimationUtil {
   private float startY;
   private float endY;
   private int direction;
   private float currentY;
   private int durationMs;
   private float endX;
   private float startX;
   private EasingType easingType = EasingType.easeInOutBack;
   private long startTime;
   private float currentX;

   public float updateY() {
      float f = this.endY - this.startY;
      this.currentY = (float)((int)Math.round((double)this.startY + (double)f * this.getEasedProgress()));
      return this.currentY;
   }

   public void setBounds(float f, float f2, float f3, float f4) {
      this.startX = f;
      this.startY = f2;
      this.endX = f3;
      this.endY = f4;
   }

   public float updateX() {
      float f = this.endX - this.startX;
      this.currentX = (float)((int)Math.round((double)this.startX + (double)f * this.getEasedProgress()));
      return this.currentX;
   }

   private static double easeOutBounce(double d) {
      double d2 = 7.5625;
      double d3 = 2.75;
      if (d < 1.0 / d3) {
         return d2 * d * d;
      } else if (d < 2.0 / d3) {
         double d4;
         return d2 * (d4 = d - 1.5 / d3) * d4 + 0.75;
      } else {
         double d5;
         double d6;
         return d < 2.5 / d3 ? d2 * (d5 = d - 2.25 / d3) * d5 + 0.9375 : d2 * (d6 = d - 2.625 / d3) * d6 + 0.984375;
      }
   }

   public double getLinearProgress() {
      return MathHelper.clamp_double((double)Math.abs(this.startTime - System.currentTimeMillis()) / (double)this.durationMs, 0.0, 1.0);
   }

   public double getEasedProgress() {
      return this.direction == 1 ? this.computeEasedValue() : 1.0 - this.computeEasedValue();
   }

   public void start(int n, EasingType easingType2, int n2, boolean flag) {
      int n3 = (int)(System.currentTimeMillis() - this.startTime);
      if (n3 < this.durationMs && !flag) {
         double d = (double)n3 / (double)this.durationMs;
         this.startTime = (long)((double)System.currentTimeMillis() - (1.0 - d) * (double)n);
      } else {
         this.startTime = System.currentTimeMillis();
      }

      this.durationMs = n;
      this.easingType = easingType2;
      this.direction = n2;
   }

   private double computeEasedValue() {
      return applyEasing(this.easingType, MathHelper.clamp_double((double)Math.abs(this.startTime - System.currentTimeMillis()) / (double)this.durationMs, 0.0, 1.0));
   }

   public static String[] getEasingNames(boolean flag) {
      int n = flag ? EasingType.values().length - 1 : EasingType.values().length;
      String[] strings = new String[n];

      for (int i = 0; i < n; i++) {
         strings[i] = EasingType.values()[i].name();
      }

      return strings;
   }

   public static EasingType parseEasingType(String str) {
      for (EasingType easingType2 : EasingType.values()) {
         if (easingType2.name().equalsIgnoreCase(str)) {
            return easingType2;
         }
      }

      System.err.println("No valid AnimationType!");
      return EasingType.easeInBack;
   }

   public static double applyEasing(EasingType easingType2, double d) {
      switch (AnimationEasingSwitchMap.ORDINAL_MAP[easingType2.ordinal()]) {
         case 1:
            return 1.0 - Math.cos(d * Math.PI / 2.0);
         case 2:
            return Math.sin(d * Math.PI / 2.0);
         case 3:
            return -(Math.cos(Math.PI * d) - 1.0) / 2.0;
         case 4:
            return d * d;
         case 5:
            return 1.0 - (1.0 - d) * (1.0 - d);
         case 6:
            return d < 0.5 ? 2.0 * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 2.0) / 2.0;
         case 7:
            return d * d * d;
         case 8:
            return 1.0 - Math.pow(1.0 - d, 3.0);
         case 9:
            return d < 0.5 ? 4.0 * d * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 3.0) / 2.0;
         case 10:
            return d * d * d * d;
         case 11:
            return 1.0 - Math.pow(1.0 - d, 4.0);
         case 12:
            return d < 0.5 ? 8.0 * d * d * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 4.0) / 2.0;
         case 13:
            return d * d * d * d * d;
         case 14:
            return 1.0 - Math.pow(1.0 - d, 5.0);
         case 15:
            return d < 0.5 ? 16.0 * d * d * d * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 5.0) / 2.0;
         case 16:
            return d == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * d - 10.0);
         case 17:
            return d == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * d);
         case 18:
            return d == 0.0
               ? 0.0
               : (d == 1.0 ? 1.0 : (d < 0.5 ? Math.pow(2.0, 20.0 * d - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * d + 10.0)) / 2.0));
         case 19:
            return 1.0 - Math.sqrt(1.0 - Math.pow(d, 2.0));
         case 20:
            return Math.sqrt(1.0 - Math.pow(d - 1.0, 2.0));
         case 21:
            return d < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * d, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(-2.0 * d + 2.0, 2.0)) + 1.0) / 2.0;
         case 22:
            double d2 = 1.70158;
            return (d2 + 1.0) * d * d * d - d2 * d * d;
         case 23:
            double d4 = 1.70158;
            double d5 = d4 + 1.0;
            return 1.0 + d5 * Math.pow(d - 1.0, 3.0) + d4 * Math.pow(d - 1.0, 2.0);
         case 24:
            double d7 = 1.70158 * 1.525;
            double d8 = Math.pow(2.0 * d, 2.0) * ((d7 + 1.0) * 2.0 * d - d7) / 2.0;
            double d9 = (Math.pow(2.0 * d - 2.0, 2.0) * ((d7 + 1.0) * (d * 2.0 - 2.0) + d7) + 2.0) / 2.0;
            return d < 0.5 ? d8 : d9;
         case 25:
            double d10 = Math.PI * 2.0 / 3.0;
            return d == 0.0 ? 0.0 : (d == 1.0 ? 1.0 : -Math.pow(2.0, 10.0 * d - 10.0) * Math.sin((d * 10.0 - 10.75) * d10));
         case 26:
            double d11 = Math.PI * 2.0 / 3.0;
            return d == 0.0 ? 0.0 : (d == 1.0 ? 1.0 : Math.pow(2.0, -10.0 * d) * Math.sin((d * 10.0 - 0.75) * d11) + 1.0);
         case 27:
            double d12 = Math.PI * 4.0 / 9.0;
            double d13 = Math.sin((20.0 * d - 11.125) * d12);
            return d == 0.0
               ? 0.0
               : (d == 1.0 ? 1.0 : (d < 0.5 ? -(Math.pow(2.0, 20.0 * d - 10.0) * d13) / 2.0 : Math.pow(2.0, -20.0 * d + 10.0) * d13 / 2.0 + 1.0));
         case 28:
            return 1.0 - easeOutBounce(1.0 - d);
         case 29:
            return easeOutBounce(d);
         case 30:
            return d < 0.5 ? (1.0 - easeOutBounce(1.0 - 2.0 * d)) / 2.0 : (1.0 + easeOutBounce(2.0 * d - 1.0)) / 2.0;
         case 31:
            return d;
         case 32:
            return 1.0;
         default:
            return 0.0;
      }
   }

   public AnimationUtil(float f, float f2, float f3, float f4) {
      this.currentX = f;
      this.currentY = f2;
      this.startX = f;
      this.startY = f2;
      this.endX = f3;
      this.endY = f4;
      this.startTime = System.currentTimeMillis();
   }
}
