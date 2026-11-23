package net.augustus.vendor.launcher.net;

public enum ProxyType {
   SOCKS4,
   SOCKS5;

   private static final ProxyType[] ENUM_VALUES = new ProxyType[]{SOCKS4, SOCKS5};

   public static ProxyType[] values(long l) {
      return (ProxyType[])ENUM_VALUES.clone();
   }
}
