package net.augustus.ui.proxy;


import net.augustus.proxy.ProxyProtocolType;
public class ProxyTypeSwitchMap {
   public static final int[] ORDINAL_MAP = new int[ProxyProtocolType.values().length];

   static {
      try {
         ORDINAL_MAP[ProxyProtocolType.SOCKS4.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[ProxyProtocolType.SOCKS5.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }
   }
}
