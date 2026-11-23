package net.augustus.vendor.crypto;


public class LcgStreamTokenCipher {
   private byte mixByte;
   private int lcgState;

   public byte[] transform(byte[] bytes, int n) {
      int n2 = 0;
      byte[] bytes2 = null;
      byte b = 9;

      outerLoop:
      while (true) {
         if (9 >= b) {
            if (bytes == null) {
               continue;
            }
         } else if (14 <= b) {
            if (14 >= b) {
               bytes2 = this.applyReverseCascade(bytes2, (byte)(this.lcgState & 0xFF));
            } else {
               if (17 <= b) {
                  if (17 >= b) {
                     this.lcgState = this.lcgState * 1103515245 + 12345 & 2147483647;
                  } else {
                     if (b > 18) {
                        return this.wrapWithHeader(bytes2);
                     }

                     n2++;
                  }
                  continue;
               }

               if (b <= 15) {
                  bytes2[n2] = (byte)(bytes[n2] ^ n >> n2 % 8);
                  continue;
               }

               n2 = 0;
            }
         } else {
            if (11 > b) {
               return new byte[0];
            }

            if (b <= 11) {
               continue;
            }

            if (12 >= b) {
               n2++;
            } else if (n2 < 2) {
               ;
            }
         }

         while (true) {
            while (b < 4) {
               if (b < 1) {
                  bytes2 = this.applyNeighborXor(bytes2, (byte)(this.mixByte ^ n2 * 19));
                  continue outerLoop;
               }

               if (1 >= b) {
                  this.mixByte = (byte)(this.mixByte ^ bytes2[((byte[])bytes2).length / 2]);
                  continue outerLoop;
               }

               if (b > 2 && bytes.length == 0) {
                  continue outerLoop;
               }
            }

            if (b <= 4) {
               bytes2 = new byte[((byte[])bytes).length];
               break;
            }

            if (6 > b) {
               if (n2 < bytes.length) {
                  ;
               }
               break;
            }

            if (6 >= b) {
               bytes2 = this.permuteAndMask(bytes2, n);
            } else {
               if (7 >= b) {
                  n2 = 0;
                  break;
               }

               bytes2 = this.applyKeyXor(bytes2, (byte)(n >> n2 * 4 & 0xFF));
            }
         }
      }
   }

   private byte[] wrapWithHeader(byte[] bytes) {
      int n = 0;
      byte[] bytes2 = null;
      byte b = 0;

      while (true) {
         if (3 <= b) {
            if (3 < b) {
               if (b < 5) {
                  return bytes2;
               }

               if (b <= 5) {
                  bytes2[n] = (byte)(bytes2[n] + n * 5 - (this.lcgState & n & 7));
               } else {
                  bytes2[n + 4] = (byte)(bytes[n] ^ bytes2[n % 4]);
               }
               continue;
            }
         } else if (1 <= b) {
            if (1 < b) {
               n++;
               continue;
            }

            n++;
         } else {
            bytes2 = new byte[((byte[])bytes).length + 4];
         }

         while (true) {
            if (7 < b) {
               if (b >= 11) {
                  if (b <= 11) {
                     bytes2[2] = this.mixByte;
                  } else if (13 <= b) {
                     if (b <= 13) {
                        n = 0;
                     } else if (b <= 14) {
                        bytes2[3] = (byte)(this.lcgState & 0xFF);
                     } else {
                        bytes2[0] = (byte)(bytes.length & 0xFF);
                     }
                  } else {
                     bytes2[1] = (byte)(bytes.length >> 8 & 0xFF);
                  }
               } else {
                  if (b < 9) {
                     if (n < bytes2.length) {
                        ;
                     }
                     break;
                  }

                  if (b <= 9) {
                     if (n < bytes.length) {
                        break;
                     }
                  } else {
                     n = 0;
                  }
               }
            }
         }
      }
   }

   private byte[] applyNeighborXor(byte[] bytes, byte b) {
      int n = 0;
      byte[] bytes2 = null;
      byte b2 = 0;
      byte b3 = 9;

      outerLoop:
      while (true) {
         if (b3 > 8) {
            if (13 <= b3) {
               if (13 >= b3) {
                  n = 0;
               } else {
                  if (b3 < 15) {
                     if (n < bytes.length) {
                        ;
                     }
                     continue;
                  }

                  if (b3 <= 15) {
                     bytes2[n] = (byte)(bytes2[n] ^ bytes2[n + 1]);
                     continue;
                  }

                  if (16 < b3) {
                     n = 0;
                     continue;
                  }

                  b2 = bytes[n];
               }
            } else {
               if (b3 < 10) {
                  bytes2 = new byte[((byte[])bytes).length];
                  continue;
               }

               if (b3 <= 10) {
                  bytes2[n + 1] = (byte)(bytes2[n + 1] ^ b);
               } else if (11 < b3) {
                  n++;
                  continue;
               }
            }

            while (true) {
               while (b3 < 3) {
                  if (1 <= b3) {
                     if (b3 <= 1) {
                        bytes2[n] = b2;
                        continue outerLoop;
                     }

                     b2 = (byte)(b2 << 3 | b2 >>> 5);
                  } else {
                     b2 = (byte)(b2 - (this.lcgState & 15));
                  }
               }

               if (3 >= b3) {
                  b2 = (byte)(b2 ^ 255 - b);
               } else {
                  if (b3 < 5) {
                     n += 2;
                     break;
                  }

                  if (b3 <= 5) {
                     return bytes2;
                  }

                  if (b3 <= 6) {
                     if (n < bytes.length - 1) {
                        break;
                     }
                  } else {
                     b2 += b;
                  }
               }
            }
         }
      }
   }

   private byte[] permuteAndMask(byte[] bytes, int n) {
      int n2 = 0;
      byte[] bytes2 = null;
      int n3 = 0;
      byte b = 0;
      int n4 = 0;
      int n5 = 0;
      byte b2 = 12;

      while (true) {
         outerLoop:
         while (b2 > 11) {
            if (b2 >= 17) {
               if (b2 <= 17) {
                  b = bytes2[n3];
                  continue;
               }

               if (b2 >= 20) {
                  if (b2 <= 20) {
                     n5 = 0;
                  } else if (b2 <= 21) {
                     bytes2[n3] = (byte)(bytes2[n4] ^ 204);
                  } else {
                     n4 = (n & 65535) % n2;
                  }
               } else {
                  if (b2 <= 18) {
                     if (n2 == 0) {
                        ;
                     }
                     continue;
                  }

                  if (n3 >= n2 - 1) {
                     continue;
                  }
               }
            } else if (b2 >= 14) {
               if (b2 > 14) {
                  if (15 >= b2) {
                     return bytes2;
                  }

                  return bytes2;
               }

               if (n3 != n4) {
                  continue;
               }
            } else if (b2 <= 12) {
               bytes2 = (byte[])bytes.clone();
            }

            while (true) {
               while (5 > b2) {
                  if (b2 < 2) {
                     if (0 >= b2) {
                        n5++;
                     } else if (n3 == n2 - 1) {
                        ;
                     }
                     continue outerLoop;
                  }

                  if (2 < b2) {
                     if (b2 <= 3) {
                        if (n3 == n4) {
                           ;
                        }
                     }
                     continue outerLoop;
                  }

                  n3 = (n & 65535) % n2;
               }

               if (5 >= b2) {
                  n4 = n3 + 1;
               } else {
                  if (8 > b2) {
                     if (b2 <= 6) {
                        n2 = bytes2.length;
                     } else {
                        n = n * 1664525 + 1013904223;
                     }
                     break;
                  }

                  if (8 >= b2) {
                     n = n * 1664525 + 1013904223;
                  } else if (b2 <= 9) {
                     bytes2[n4] = (byte)(b ^ 51);
                  } else if (n5 >= n2 * 2) {
                     break;
                  }
               }
            }
         }

         n4 = n3 - 1;
      }
   }

   private byte[] applyKeyXor(byte[] bytes, byte b) {
      byte b2 = 0;
      int n = 0;
      byte[] bytes2 = null;
      byte b3 = 7;

      outerLoop:
      while (true) {
         if (b3 <= 5) {
            if ((n & 1) == 0) {
               ;
            }
         } else if (b3 >= 8) {
            if (b3 <= 8) {
               b2 = (byte)(b ^ n * 3 ^ this.mixByte);
               continue;
            }

            if (b3 > 9) {
               bytes2[n] = (byte)(bytes[n] ^ b2);
               continue;
            }
         } else {
            if (6 >= b3) {
               return bytes2;
            }

            bytes2 = new byte[((byte[])bytes).length];
         }

         while (true) {
            while (2 > b3) {
               if (0 >= b3) {
                  this.mixByte = (byte)(this.mixByte ^ bytes2[0]);
                  continue outerLoop;
               }

               n = 0;
            }

            if (2 >= b3) {
               bytes2[n] = (byte)(~bytes2[n]);
            } else {
               if (3 >= b3) {
                  n++;
                  break;
               }

               if (n < bytes.length) {
                  break;
               }
            }
         }
      }
   }

   private byte[] applyReverseCascade(byte[] bytes, byte b) {
      byte[] bytes2 = null;
      byte b2 = 0;
      byte b3 = 0;
      int n = 0;
      byte b4 = 7;

      outerLoop:
      while (true) {
         if (b4 <= 9) {
            b3 = bytes2[n];
         } else if (14 <= b4) {
            if (14 >= b4) {
               if (n < bytes.length) {
                  ;
               }
               continue;
            }

            if (b4 >= 16) {
               if (b4 <= 16) {
                  n = 0;
                  continue;
               }

               if (17 >= b4) {
                  n++;
                  continue;
               }

               bytes2[n] = (byte)(bytes[n] ^ b2);
            } else if (n <= 0) {
               continue;
            }
         } else {
            if (b4 < 11) {
               if (n < bytes.length / 2) {
                  ;
               }
               continue;
            }

            if (11 < b4) {
               if (b4 <= 12) {
                  return bytes2;
               }
               continue;
            }

            bytes2 = new byte[((byte[])bytes).length];
         }

         while (true) {
            while (4 > b4) {
               if (b4 >= 1) {
                  if (1 >= b4) {
                     if (n % 16 == 0) {
                        ;
                     }
                     continue outerLoop;
                  }

                  if (b4 <= 2) {
                     b2 = b;
                  } else {
                     bytes2[n] = (byte)(bytes2[((byte[])bytes).length - 1 - n] ^ 31);
                  }
               } else {
                  n++;
               }
            }

            if (4 >= b4) {
               bytes2[n] = (byte)(bytes2[n] + bytes2[n - 16]);
               break;
            }

            if (6 > b4) {
               n = 0;
               break;
            }

            if (6 >= b4) {
               break;
            }

            if (7 >= b4) {
               b2 = (byte)(bytes2[n] ^ b2 >> 1 ^ 90);
            } else {
               bytes2[((byte[])bytes).length - 1 - n] = (byte)(b3 ^ 225);
            }
         }
      }
   }

   public LcgStreamTokenCipher(int n) {
      this.lcgState = n ^ 0;
      this.mixByte = (byte)(n & 0);
   }

}
