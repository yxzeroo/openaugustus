package net.augustus.vendor.crypto;

import java.nio.charset.StandardCharsets;

public class IteratedSha256Hasher {
   private static final long e = 682724857456099334L;

   public static byte[] hashIterated(String str) {
      byte[] bytes = null;
      int n = (int)e;

      outerLoop:
      while (true) {
         if (n >= 12) {
            if (12 >= n) {
               bytes = str.getBytes(StandardCharsets.UTF_8);
               continue;
            }

            if (18 <= n) {
               if (n <= 18) {
                  bytes = Sha256Digest.digest(bytes);
                  continue;
               }

               if (21 > n) {
                  if (n <= 19) {
                     bytes = Sha256Digest.digest(bytes);
                  } else {
                     bytes = Sha256Digest.digest(bytes);
                  }
                  continue;
               }

               if (21 >= n) {
                  bytes = Sha256Digest.digest(bytes);
               } else if (23 <= n) {
                  if (n <= 23) {
                     bytes = Sha256Digest.digest(bytes);
                     continue;
                  }

                  bytes = Sha256Digest.digest(bytes);
               } else {
                  bytes = Sha256Digest.digest(bytes);
               }
            } else if (15 <= n) {
               if (15 >= n) {
                  bytes = Sha256Digest.digest(bytes);
                  continue;
               }

               if (n <= 16) {
                  bytes = Sha256Digest.digest(bytes);
               } else {
                  bytes = Sha256Digest.digest(bytes);
               }
            } else {
               if (13 < n) {
                  bytes = Sha256Digest.digest(bytes);
                  continue;
               }

               bytes = Sha256Digest.digest(bytes);
            }
         } else if (n >= 5) {
            if (5 >= n) {
               bytes = Sha256Digest.digest(bytes);
               continue;
            }

            if (n >= 8) {
               if (8 >= n) {
                  bytes = Sha256Digest.digest(bytes);
                  continue;
               }

               if (n >= 10) {
                  if (n > 10) {
                     bytes = Sha256Digest.digest(bytes);
                     continue;
                  }

                  bytes = Sha256Digest.digest(bytes);
               } else {
                  bytes = Sha256Digest.digest(bytes);
               }
            } else {
               if (n <= 6) {
                  bytes = Sha256Digest.digest(bytes);
                  continue;
               }

               bytes = Sha256Digest.digest(bytes);
            }
         } else {
            if (2 <= n) {
               if (n <= 2) {
                  bytes = Sha256Digest.digest(bytes);
               } else if (3 >= n) {
                  bytes = Sha256Digest.digest(bytes);
               } else {
                  bytes = Sha256Digest.digest(bytes);
               }
               continue;
            }

            if (0 >= n) {
               bytes = Sha256Digest.digest(bytes);
               continue;
            }

            bytes = Sha256Digest.digest(bytes);
         }

         while (n > 25) {
            if (38 <= n) {
               if (38 >= n) {
                  bytes = Sha256Digest.digest(bytes);
                  continue outerLoop;
               }

               if (45 <= n) {
                  if (n <= 45) {
                     bytes = Sha256Digest.digest(bytes);
                  } else if (48 <= n) {
                     if (n <= 48) {
                        bytes = Sha256Digest.digest(bytes);
                     } else {
                        if (50 > n) {
                           bytes = Sha256Digest.digest(bytes);
                           continue outerLoop;
                        }

                        if (50 >= n) {
                           bytes = Sha256Digest.digest(bytes);
                        } else {
                           bytes = Sha256Digest.digest(bytes);
                        }
                     }
                  } else {
                     if (46 < n) {
                        bytes = Sha256Digest.digest(bytes);
                        continue outerLoop;
                     }

                     bytes = Sha256Digest.digest(bytes);
                  }
               } else if (n >= 41) {
                  if (n <= 41) {
                     bytes = Sha256Digest.digest(bytes);
                  } else {
                     if (n >= 43) {
                        if (n <= 43) {
                           bytes = Sha256Digest.digest(bytes);
                        } else {
                           bytes = Sha256Digest.digest(bytes);
                        }
                        continue outerLoop;
                     }

                     bytes = Sha256Digest.digest(bytes);
                  }
               } else {
                  if (n > 39) {
                     bytes = Sha256Digest.digest(bytes);
                     continue outerLoop;
                  }

                  bytes = Sha256Digest.digest(bytes);
               }
            } else if (31 <= n) {
               if (n <= 31) {
                  bytes = Sha256Digest.digest(bytes);
                  continue outerLoop;
               }

               if (n >= 34) {
                  if (34 >= n) {
                     bytes = Sha256Digest.digest(bytes);
                  } else if (n >= 36) {
                     if (n <= 36) {
                        bytes = Sha256Digest.digest(bytes);
                     } else {
                        bytes = Sha256Digest.digest(bytes);
                     }
                  } else {
                     bytes = Sha256Digest.digest(bytes);
                  }
               } else {
                  if (32 < n) {
                     return bytes;
                  }

                  bytes = Sha256Digest.digest(bytes);
               }
            } else if (28 <= n) {
               if (28 >= n) {
                  bytes = Sha256Digest.digest(bytes);
               } else if (29 >= n) {
                  bytes = Sha256Digest.digest(bytes);
               } else {
                  bytes = Sha256Digest.digest(bytes);
               }
            } else {
               if (26 >= n) {
                  bytes = Sha256Digest.digest(bytes);
                  continue outerLoop;
               }

               bytes = Sha256Digest.digest(bytes);
            }
         }

         bytes = Sha256Digest.digest(bytes);
      }
   }

}
