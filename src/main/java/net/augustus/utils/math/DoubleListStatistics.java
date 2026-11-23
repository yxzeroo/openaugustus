package net.augustus.utils.math;

import java.util.ArrayList;
import net.augustus.utils.TimeHelper;

public class DoubleListStatistics {
   private static final long a = 113676136693283L;

   public static int findMinIndex(ArrayList list, int n, int n2) {
      double d = (Double)list.get(n);
      int n3 = n;

      for (int i = n; i < n2; i++) {
         if (d > (Double)list.get(i)) {
            d = (Double)list.get(i);
            n3 = i;
         }
      }

      return n3;
   }

   public static double max(ArrayList list, long l) {
      l = a ^ l;
      double d = (Double)list.get(0);
      int n = 1;
      byte b = 0;

      double d2;
      outerLoop:
      while (true) {
         if (n < list.size()) {
            d2 = (Double)list.get(n);
            byte b2 = b;
            if (l >= 0L) {
               if (b == 0) {
                  break;
               }

               b2 = b;
            }

            int n2;
            exitBlock: {
               exitBlock2: {
                  if (b2 != 0) {
                     double d3;
                     n2 = (d3 = d2 - d) == 0.0 ? 0 : (d3 < 0.0 ? -1 : 1);
                     if (l < 0L) {
                        break exitBlock;
                     }

                     if (n2 <= 0) {
                        break exitBlock2;
                     }

                     d2 = (Double)list.get(n);
                  }

                  d = d2;
               }

               n++;
               n2 = b;
            }

            if (n2 != 0) {
               continue;
            }
         }

         while (l <= 0L) {
            if (b != 0) {
               continue outerLoop;
            }
         }

         d2 = d;
         break;
      }

      return d2;
   }

   public static void swap(ArrayList list, int n, int n2) {
      double d = (Double)list.get(n);
      list.set(n, list.get(n2));
      list.set(n2, d);
   }

   public static double mean(ArrayList list) {
      double d = 0.0;

      for (int i = 0; i < list.size(); i++) {
         d += (Double)list.get(i);
      }

      return d / (double)list.size();
   }

   public static void selectionSort(ArrayList list) {

      for (int i = 0; i < list.size(); i++) {
         int size = list.size();
         int n = findMinIndex(list, i, size);
         swap(list, n, i);
      }
   }

   public static double sampleVariance(ArrayList list) {
      double d = mean(list);
      double d2 = 0.0;

      for (int i = 0; i < list.size(); i++) {
         d2 += ((Double)list.get(i) - d) * ((Double)list.get(i) - d);
      }

      return d2 / (double)(list.size() - 1);
   }

   public static double sampleStdDev(ArrayList list) {
      return Math.sqrt(sampleVariance(list));
   }

   public static double median(ArrayList list) {
      selectionSort(list);
      return list.size() % 2 == 0 ? ((Double)list.get(list.size() / 2 - 1) + (Double)list.get(list.size() / 2)) / 2.0 : (Double)list.get(list.size() / 2);
   }
}
