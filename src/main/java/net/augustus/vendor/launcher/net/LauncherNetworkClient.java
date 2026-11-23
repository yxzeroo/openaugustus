package net.augustus.vendor.launcher.net;

import net.augustus.vendor.launcher.CredentialStorage;
import net.augustus.vendor.launcher.packet.C2SActivateClientPacket;
import net.augustus.vendor.launcher.packet.C2SClassLoaderReadyPacket;
import net.augustus.vendor.launcher.packet.C2SHandshakeCompletePacket;
import net.augustus.vendor.launcher.packet.C2SHandshakeInitPacket;
import net.augustus.vendor.launcher.packet.C2SHardwareInfoPacket;
import net.augustus.vendor.launcher.packet.C2SHwidResetPacket;
import net.augustus.vendor.launcher.packet.C2SIntegrityProofPacket;
import net.augustus.vendor.launcher.packet.C2SLoginPacket;
import net.augustus.vendor.launcher.packet.C2SRawPayloadPacket;
import net.augustus.vendor.launcher.packet.C2SRegisterPacket;
import net.augustus.vendor.launcher.packet.C2SRemoteClassRequestPacket;
import net.augustus.vendor.launcher.packet.C2SResourcesReadyPacket;
import net.augustus.vendor.launcher.packet.C2SStartClientPacket;
import net.augustus.vendor.launcher.packet.EmptyPacket;
import net.augustus.vendor.launcher.packet.LauncherPacket;
import net.augustus.vendor.launcher.packet.S2CActivateClientResponsePacket;
import net.augustus.vendor.launcher.packet.S2CClassLoaderKeyPacket;
import net.augustus.vendor.launcher.packet.S2CClientArchiveChunkPacket;
import net.augustus.vendor.launcher.packet.S2CFailureMessagePacket;
import net.augustus.vendor.launcher.packet.S2CHandshakeChallengePacket;
import net.augustus.vendor.launcher.packet.S2CHardwareChallengePacket;
import net.augustus.vendor.launcher.packet.S2CHwidResetResponsePacket;
import net.augustus.vendor.launcher.packet.S2CIntegrityChallengePacket;
import net.augustus.vendor.launcher.packet.S2CLoginResponsePacket;
import net.augustus.vendor.launcher.packet.S2CRegisterResponsePacket;
import net.augustus.vendor.launcher.packet.S2CRemoteClassResponsePacket;
import net.augustus.vendor.launcher.packet.S2CResourceChunkPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JOptionPane;

public class LauncherNetworkClient extends Thread {
   public final List<Supplier<LauncherPacket>> s2cPacketFactories;
   private static LauncherNetworkClient instance;
   public final List<Class<? extends LauncherPacket>> c2sPacketTypes;
   public static final boolean EPOLL_AVAILABLE = Epoll.isAvailable();
   private volatile boolean connected;
   private final String launcherDataDir;
   private final String proxyConfigFileName;

   private void ensureDefaultProxyConfigFile() {
      CredentialStorage.ensureDirectoryExists(this.launcherDataDir);
      File file = new File(this.launcherDataDir, "proxy.txt");
      if (!file.exists()) {
         String str = "# ===============================================\n#        proxy.txt - SOCKS4/SOCKS5 Proxy Config #\n# ===============================================\n#\n# This file lets you specify an OPTIONAL SOCKS4 or SOCKS5 proxy\n# for your Netty client. If the file is empty or only contains\n# comments, NO proxy will be used.\n# To check your free proxies you can use this tool: https://proxyscrape.com/proxy-checker\n# If you need 5 free proxies: https://oxylabs.io/products/datacenter-proxies  (I tested the proxy integration with that)\n# Rotating proxies are not supported.\n#\n# USAGE:\n#\n# For SOCKS5 (no authentication):\n#   socks5:host:port\n# For SOCKS5 (with username and password):\n#   socks5:host:port:username:password\n#\n# For SOCKS4 (no authentication):\n#   socks4:host:port\n# For SOCKS4 (with username):\n#   socks4:host:port:userid\n#\n# EXAMPLES:\n#   socks5:127.0.0.1:1080\n#   socks5:myproxy.example.com:1080:myuser:mypass\n#   socks4:127.0.0.1:1080\n#   socks4:proxy.example.org:1080:userid123\n#\n# Only the FIRST valid (non-comment, non-empty) line will be used.\n# Lines starting with # are ignored.\n#\n# To disable proxying, leave this file empty or only with comments.\n#\n";

         try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(str);
            fileWriter.flush();
         } catch (IOException e) {
         }
      }
   }

   public static LauncherNetworkClient getInstance() {
      return instance;
   }

   public boolean isDebugModeEnabled() {
      return false;
   }

   private void onConnectComplete(EventLoopGroup event, Optional optional, ChannelFuture channelFuture) throws Exception {
      Object obj = null;
      if (!channelFuture.isSuccess()) {
         if (!optional.isPresent()) {
            JOptionPane.showMessageDialog(
               null, "You are not able to connect to the Augustus server. Please check your internet connection.\nError: " + channelFuture.cause().getMessage()
            );
            System.exit(0);
         } else {
            JOptionPane.showMessageDialog(
               null,
               "You are not able to connect to the Augustus server. It may be your proxy configured in the proxy.txt. Please check your internet connection too.\nError: "
                  + channelFuture.cause().getMessage()
            );
            System.exit(0);
         }

         this.connected = false;
      } else {
         this.connected = true;
         obj = channelFuture.channel();
         ((Channel)obj).closeFuture().addListener(future -> {
            this.connected = false;
            event.shutdownGracefully();
         });
      }
   }

   public LauncherNetworkClient() {
      super();
      this.c2sPacketTypes = Arrays.asList(
         C2SLoginPacket.class,
         C2SRegisterPacket.class,
         C2SHwidResetPacket.class,
         C2SActivateClientPacket.class,
         C2SStartClientPacket.class,
         C2SResourcesReadyPacket.class,
         C2SClassLoaderReadyPacket.class,
         C2SRemoteClassRequestPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         C2SHandshakeCompletePacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         C2SRawPayloadPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         C2SHardwareInfoPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         C2SHandshakeInitPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         EmptyPacket.class,
         C2SIntegrityProofPacket.class
      );
      this.s2cPacketFactories = Arrays.asList(
         S2CLoginResponsePacket::new,
         S2CRegisterResponsePacket::new,
         S2CHwidResetResponsePacket::new,
         S2CActivateClientResponsePacket::new,
         S2CResourceChunkPacket::new,
         S2CClientArchiveChunkPacket::new,
         S2CClassLoaderKeyPacket::new,
         S2CRemoteClassResponsePacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         S2CHardwareChallengePacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         S2CHandshakeChallengePacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         EmptyPacket::new,
         S2CIntegrityChallengePacket::new,
         S2CFailureMessagePacket::new
      );
      this.launcherDataDir = System.getenv("APPDATA") + File.separator + "ElectricLauncher";
      this.proxyConfigFileName = "proxy.txt";
      this.connected = false;
      instance = this;
      this.ensureDefaultProxyConfigFile();
   }

   private void connectToServer(EventLoopGroup event) throws Exception {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      Object obj8 = null;
      Object obj9 = null;
      Object obj10 = null;
      Object obj11 = null;
      HandshakeKeyTable.initialize();
      obj10 = "XNqTn785wCMbsXzFHLrm5ChMDdWBh8WM7nhpU3jJuvn8atqaf3mxJzFm37GxWdYW";
      obj = KeyStore.getInstance("PKCS12");
      obj2 = LauncherNetworkClient.class.getClassLoader().getResourceAsStream("certificates/client.p12");
      ((KeyStore)obj).load((InputStream)obj2, "XNqTn785wCMbsXzFHLrm5ChMDdWBh8WM7nhpU3jJuvn8atqaf3mxJzFm37GxWdYW".toCharArray());
      obj11 = KeyManagerFactory.getInstance("SunX509");
      ((KeyManagerFactory)obj11).init((KeyStore)obj, "XNqTn785wCMbsXzFHLrm5ChMDdWBh8WM7nhpU3jJuvn8atqaf3mxJzFm37GxWdYW".toCharArray());
      obj9 = KeyStore.getInstance("JKS");
      obj8 = LauncherNetworkClient.class.getClassLoader().getResourceAsStream("certificates/truststore.jks");
      ((KeyStore)obj9).load((InputStream)obj8, "XNqTn785wCMbsXzFHLrm5ChMDdWBh8WM7nhpU3jJuvn8atqaf3mxJzFm37GxWdYW".toCharArray());
      obj4 = TrustManagerFactory.getInstance("SunX509");
      ((TrustManagerFactory)obj4).init((KeyStore)obj9);
      Security.setProperty("ocsp.enable", "true");
      obj5 = SslContextBuilder.forClient()
         .protocols("TLSv1.3", "TLSv1.2")
         .keyManager((KeyManagerFactory)obj11)
         .trustManager((TrustManagerFactory)obj4)
         .build();
      obj6 = ProxyConfig.loadFromFile(this.launcherDataDir + File.separator + "proxy.txt");
      obj7 = new Bootstrap().group(event).channel(EPOLL_AVAILABLE ? EpollSocketChannel.class : NioSocketChannel.class);
      ((Bootstrap)obj7).handler(new LauncherChannelInitializer(this, (Optional)obj6, (SslContext)obj5));
      obj3 = ((Bootstrap)obj7).connect("connect.electriclauncher.de", 1338);
      final Optional var19f = (Optional)obj6;
      ((ChannelFuture)obj3).addListener(future -> this.onConnectComplete(event, var19f, (ChannelFuture)future));

      while (true) {
         LauncherClientHandler.flushQueuedPacketsIfReady();
         Thread.sleep(50L);
      }
   }

   @Override
   public void run() {
      super.run();
      Object obj = EPOLL_AVAILABLE ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);

      try {
         this.connectToServer((EventLoopGroup)obj);
      } catch (Exception e) {
      } finally {
         ((EventLoopGroup)obj).shutdownGracefully();
         this.connected = false;
      }
   }

}
