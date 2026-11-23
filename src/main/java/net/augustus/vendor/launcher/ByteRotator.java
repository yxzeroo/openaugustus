package net.augustus.vendor.launcher;

public class ByteRotator {
   private static final long e = 4957904350065197062L;

   public static void rotateRight(byte[] bytes, int n) {
      int n2 = 0;
      ClientArchiveHolder.getControlFlowMarker();
      byte[] bytes2 = null;
      int n3 = 0;
      int n4 = (int)e;

      outerLoop:
      while (true) {
         if (n4 >= 5) {
            if (5 >= n4) {
               n %= n3;
               continue;
            }

            if (n4 >= 8) {
               if (8 >= n4) {
                  bytes[n2] = bytes[n2 - n];
                  n2--;
               } else if (10 <= n4) {
                  if (n4 > 10) {
                     bytes[n2] = bytes2[n2];
                     continue;
                  }

                  if (n2 >= n) {
                     continue;
                  }
               } else {
                  n2++;
               }
            } else if (n4 <= 6) {
               if (n < 0) {
                  continue;
               }
            } else if (bytes != null) {
               ;
            }
         } else if (n4 >= 2) {
            if (n4 > 2) {
               if (n4 <= 3) {
                  if (n2 < n) {
                     continue;
                  }
               } else {
                  n += n3;
               }
            }
         } else {
            if (0 < n4) {
               n2 = n3 - 1;
               continue;
            }

            bytes2 = new byte[n];
         }

         while (true) {
            while (n4 < 19) {
               if (n4 < 15) {
                  if (n4 <= 13) {
                     n3 = bytes.length;
                  } else {
                     n2 = 0;
                  }
                  continue outerLoop;
               }

               if (n4 <= 15) {
                  bytes2[n2] = bytes[n3 - n + n2];
               } else {
                  if (n4 >= 17) {
                     if (n4 <= 17) {
                        n2++;
                     }
                     continue outerLoop;
                  }

                  if (n != 0) {
                     continue outerLoop;
                  }
               }
            }

            if (19 >= n4) {
               return;
            }

            if (n4 >= 22) {
               if (22 >= n4) {
                  break;
               }

               if (24 > n4) {
                  return;
               }

               if (24 >= n4) {
                  if (bytes.length == 0) {
                     ;
                  }
               } else if (n2 >= n) {
                  break;
               }
            } else {
               if (n4 <= 20) {
                  return;
               }

               n2 = 0;
            }
         }
      }
   }


}
