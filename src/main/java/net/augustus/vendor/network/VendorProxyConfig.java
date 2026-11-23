package net.augustus.vendor.network;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;
import net.augustus.utils.TimeHelper;

public class VendorProxyConfig {
   public final VendorProxyType proxyType;
   public final String host;
   public final String username;
   public final int port;
   public final String password;

   public boolean hasPassword() {
      return this.password != null && !this.password.isEmpty();
   }

   public static Optional loadFromFile(String str) {
      File file = new File(str);
      if (!file.exists()) {
         return Optional.empty();
      } else {
         try {
            Stream stream = Files.lines(file.toPath()).map(String::trim);
            stream = stream.filter(obj -> {
               return !((String)obj).isEmpty();
            });
            return stream.filter(obj2 -> {
               return !((String)obj2).startsWith("#");
            }).findFirst().map(obj3 -> parse((String)obj3));
         } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
         }
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.proxyType.name()).append(":").append(this.host).append(":").append(this.port);
      if (this.hasUsername()) {
         sb.append(":").append(this.username);
      }

      if (this.hasPassword()) {
         sb.append(":").append(this.password);
      }

      return sb.toString();
   }

   public boolean hasUsername() {
      return this.username != null && !this.username.isEmpty();
   }

   public static VendorProxyConfig parse(String str) {
      String[] strings = str.split(":");
      if (strings.length < 3) {
         throw new IllegalArgumentException("Invalid proxy config line: " + str);
      } else {
         String str2 = strings[0].toLowerCase();
         String str3 = strings[1];
         int n = Integer.parseInt(strings[2]);
         if ("socks5".equalsIgnoreCase(str2)) {
            if (strings.length == 3) {
               return new VendorProxyConfig(VendorProxyType.SOCKS5, str3, n, null, null);
            } else if (strings.length == 5) {
               return new VendorProxyConfig(VendorProxyType.SOCKS5, str3, n, strings[3], strings[4]);
            } else {
               throw new IllegalArgumentException("Invalid SOCKS5 config: " + str);
            }
         } else if ("socks4".equalsIgnoreCase(str2)) {
            if (strings.length == 3) {
               return new VendorProxyConfig(VendorProxyType.SOCKS4, str3, n, null, null);
            } else if (strings.length == 4) {
               return new VendorProxyConfig(VendorProxyType.SOCKS4, str3, n, strings[3], null);
            } else {
               throw new IllegalArgumentException("Invalid SOCKS4 config: " + str);
            }
         } else {
            throw new IllegalArgumentException("Unknown proxy type: " + str2);
         }
      }
   }

   private VendorProxyConfig(VendorProxyType vendorProxyType, String str, int n, String str2, String str3) {
      this.proxyType = vendorProxyType;
      this.host = str;
      this.port = n;
      this.username = str2;
      this.password = str3;
   }

}
