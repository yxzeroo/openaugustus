package net.augustus.proxy;

import java.util.Arrays;

public enum ProxyProtocolType {
   SOCKS4("Socks 4"),
   SOCKS5("Socks 5");
   private final String displayName;

   public static ProxyProtocolType fromDisplayName(String str) {
      return Arrays.stream(values()).filter(proxyProtocolType -> proxyProtocolType.getDisplayName().equals(str)).findFirst().orElse(null);
   }

   private ProxyProtocolType(String str) {
      this.displayName = str;
   }

   public String getDisplayName() {
      return this.displayName;
   }

}
