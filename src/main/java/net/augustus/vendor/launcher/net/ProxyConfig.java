package net.augustus.vendor.launcher.net;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

public class ProxyConfig {
   public final String password;
   public final String username;
   public final String host;
   public final int port;
   public final ProxyType type;

   public static ProxyConfig parseLine(long l, String str) {
      String[] strings = str.split(":");
      if (strings.length < 3) {
         throw new IllegalArgumentException("Invalid proxy config line: " + str);
      }

      String str2 = strings[0].toLowerCase();
      String str3 = strings[1];
      int n = Integer.parseInt(strings[2]);
      if ("socks5".equalsIgnoreCase(str2)) {
         if (strings.length == 3) {
            return new ProxyConfig(ProxyType.SOCKS5, str3, n, null, null);
         } else if (strings.length == 5) {
            return new ProxyConfig(ProxyType.SOCKS5, str3, n, strings[3], strings[4]);
         } else {
            throw new IllegalArgumentException("Invalid SOCKS5 config: " + str);
         }
      } else if ("socks4".equalsIgnoreCase(str2)) {
         if (strings.length == 3) {
            return new ProxyConfig(ProxyType.SOCKS4, str3, n, null, null);
         } else if (strings.length == 4) {
            return new ProxyConfig(ProxyType.SOCKS4, str3, n, strings[3], null);
         } else {
            throw new IllegalArgumentException("Invalid SOCKS4 config: " + str);
         }
      } else {
         throw new IllegalArgumentException("Unknown proxy type: " + str2);
      }
   }

   private ProxyConfig(ProxyType proxyType, String str, int n, String str2, String str3) {
      this.type = proxyType;
      this.host = str;
      this.port = n;
      this.username = str2;
      this.password = str3;
   }

   private static boolean isNotComment(long l, String str) {
      return !str.startsWith("#");
   }

   public boolean hasUsername() {
      return this.username != null && !this.username.isEmpty();
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.type.name()).append(":").append(this.host).append(":").append(this.port);
      if (this.hasUsername()) {
         sb.append(":").append(this.username);
      }

      if (this.hasPassword(0L)) {
         sb.append(":").append(this.password);
      }

      return sb.toString();
   }

   public static Optional<ProxyConfig> loadFromFile(String str) {
      File file = new File(str);
      if (!file.exists()) {
         return Optional.empty();
      } else {
         try {
            return Files.lines(file.toPath())
               .map(String::trim)
               .filter(var -> isNotEmpty(0L, var))
               .filter(var -> isNotComment(0L, var))
               .findFirst()
               .map(var -> parseLine(0L, var));
         } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
         }
      }
   }

   private static boolean isNotEmpty(long l, String str) {
      return !str.isEmpty();
   }

   public boolean hasPassword(long l) {
      return this.password != null && !this.password.isEmpty();
   }



}
