package net.augustus.vendor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.resolver.DefaultAddressResolverGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import net.augustus.modules.misc.AugustusServer;
import net.augustus.vendor.network.VendorChannelInitializer;
import net.augustus.utils.file.ClientFileUtil;
import net.augustus.vendor.crypto.VendorRsaKeyStore;
import net.augustus.vendor.network.VendorProxyConfig;
import net.augustus.Augustus;
import net.augustus.vendor.packet.ServerPenaltyAddPacket;
import net.augustus.vendor.packet.IntegrityChallengeResponsePacket;
import net.augustus.vendor.packet.AuthProofChallengePacket;
import net.augustus.vendor.packet.FriendsClearPacket;
import net.augustus.vendor.packet.ConfigSystemMessagePacket;
import net.augustus.vendor.packet.ReconnectProofChallengePacket;
import net.augustus.vendor.packet.UserProfileUpdatePacket;
import net.augustus.vendor.packet.ClientHeartbeatPacket;
import net.augustus.vendor.packet.PlayerPresenceClearPacket;
import net.augustus.vendor.packet.ServerPacketAckPacket;
import net.augustus.vendor.packet.KeyExchangeFinishPacket;
import net.augustus.vendor.packet.NoopVendorPacket;
import net.augustus.vendor.packet.ServerPenaltiesClearPacket;
import net.augustus.vendor.packet.ClientTelemetryReportPacket;
import net.augustus.vendor.packet.IntegrityChallengeRequestPacket;
import net.augustus.vendor.packet.QuestsClearPacket;
import net.augustus.vendor.packet.PlayerEffectAddPacket;
import net.augustus.vendor.packet.IrcChatTogglePacket;
import net.augustus.vendor.packet.FriendRemoveS2CPacket;
import net.augustus.vendor.packet.OnlineConfigListRequestPacket;
import net.augustus.vendor.packet.PlayerPresenceUpdatePacket;
import net.augustus.vendor.packet.CosmeticsDisablePacket;
import net.augustus.vendor.packet.CosmeticsEquipPacket;
import net.augustus.vendor.packet.AuthChallengePacket;
import net.augustus.vendor.packet.FriendUpdatePacket;
import net.augustus.vendor.packet.QuestCompletePacket;
import net.augustus.vendor.packet.FriendRemoveC2SPacket;
import net.augustus.vendor.packet.RemoteDirectoryListingPacket;
import net.augustus.vendor.packet.ConfigEncryptionKeyPacket;
import net.augustus.vendor.packet.KeyExchangeInitPacket;
import net.augustus.vendor.packet.FriendAcceptPacket;
import net.augustus.vendor.packet.IrcChatSendPacket;
import net.augustus.vendor.packet.QuestSyncPacket;
import net.augustus.vendor.packet.OnlineUserRemovePacket;
import net.augustus.vendor.packet.OnlineConfigActionResultPacket;
import net.augustus.vendor.packet.OnlineUsersClearPacket;
import net.augustus.vendor.packet.IrcVisibilityUpdatePacket;
import net.augustus.vendor.packet.OnlineUserAddPacket;
import net.augustus.vendor.packet.ServerCommandPacket;
import net.augustus.vendor.packet.PlayerEffectRemovePacket;
import net.augustus.vendor.packet.OnlineConfigDownloadRequestPacket;
import net.augustus.vendor.packet.CosmeticsRemovePacket;
import net.augustus.vendor.packet.RemoteFileRequestPacket;
import net.augustus.vendor.packet.IrcChatMessagePacket;
import net.augustus.vendor.packet.OnlineConfigHashCheckPacket;
import net.augustus.vendor.packet.ClientAuthLoginPacket;
import net.augustus.vendor.packet.AuthSuccessPacket;
import net.augustus.vendor.packet.SessionTokenExchangePacket;
import net.augustus.vendor.packet.KeyExchangeServerKeyPacket;
import net.augustus.vendor.packet.OnlineConfigUploadPacket;
import net.augustus.vendor.packet.OnlineConfigListPacket;
import net.augustus.vendor.packet.FriendActionStatusPacket;
import net.augustus.vendor.packet.ClientPacketAckPacket;
import net.augustus.vendor.packet.SessionReconnectPacket;
import net.augustus.vendor.packet.LegacyKeyExchangeInitPacket;
import net.augustus.vendor.packet.RemoteFileUploadPacket;
import net.augustus.vendor.packet.OnlineConfigUploadCheckPacket;
import net.augustus.vendor.packet.CosmeticsSyncPacket;
import net.augustus.vendor.packet.QuestProgressPacket;
import net.augustus.vendor.packet.PlayerEffectsClearPacket;
import net.augustus.vendor.packet.ServerFileWritePacket;
import net.augustus.vendor.packet.FriendRequestPacket;
import net.augustus.vendor.packet.TelemetryLogPacket;
import net.augustus.vendor.packet.ServerHeartbeatPacket;
import net.augustus.vendor.packet.OnlineConfigActionPacket;
import net.augustus.vendor.packet.AuthHwidResponsePacket;
import net.augustus.vendor.packet.SessionTokenRefreshPacket;
import net.augustus.vendor.packet.OnlineConfigDataPacket;

public class AugustusServerClient {
   private volatile boolean shuttingDown;
   public static final boolean EPOLL_AVAILABLE = Epoll.isAvailable();
   public List outboundPacketClasses;
   private final String launcherDataDir;
   private volatile boolean reconnecting = false;
   private static final AugustusServerClient INSTANCE;
   private Bootstrap bootstrap;
   public List inboundPacketSuppliers;
   public final HashMap outboundPacketIds;
   private DefaultEventExecutor handlerExecutor;
   private final AtomicInteger reconnectAttempts;
   private EventLoopGroup eventLoopGroup;
   private final String proxyFileName;
   private Channel channel;
   public static volatile boolean presenceSyncPending = false;

   public void connect() {
      AugustusServer.setConnectionState(1);
      if (!this.shuttingDown) {
         VendorSessionTicker.lastConnectionActivity = System.currentTimeMillis();
         String str = "connect.electriclauncher.de";

         try {
            this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 40000);
            this.bootstrap.resolver(DefaultAddressResolverGroup.INSTANCE);
            ChannelFuture channelFuture = this.bootstrap.connect("connect.electriclauncher.de", 1338);
            channelFuture.addListener((ChannelFutureListener)channelFuture2 -> this.onConnectComplete((ChannelFuture)channelFuture2));
         } catch (Exception e) {
            e.printStackTrace();
            this.scheduleReconnect();
         }
      }
   }

   static {
      INSTANCE = new AugustusServerClient();
      VendorRsaKeyStore.loadAllKeys();
   }

   private Bootstrap createBootstrap() {

      try {
         String str = "XNqTn785wCMbsXzFHLrm5ChMDdWBh8WM7nhpU3jJuvn8atqaf3mxJzFm37GxWdYW";
         KeyStore instance = KeyStore.getInstance("PKCS12");
         InputStream resourceAsStream = AugustusServerClient.class.getClassLoader().getResourceAsStream("assets/augustus/certificates/client.p12");
         instance.load(resourceAsStream, "XNqTn785wCMbsXzFHLrm5ChMDdWBh8WM7nhpU3jJuvn8atqaf3mxJzFm37GxWdYW".toCharArray());
         KeyManagerFactory instance2 = KeyManagerFactory.getInstance("SunX509");
         instance2.init(instance, "XNqTn785wCMbsXzFHLrm5ChMDdWBh8WM7nhpU3jJuvn8atqaf3mxJzFm37GxWdYW".toCharArray());
         KeyStore instance3 = KeyStore.getInstance("JKS");
         InputStream resourceAsStream2 = AugustusServerClient.class.getClassLoader().getResourceAsStream("assets/augustus/certificates/truststore.jks");
         instance3.load(resourceAsStream2, "XNqTn785wCMbsXzFHLrm5ChMDdWBh8WM7nhpU3jJuvn8atqaf3mxJzFm37GxWdYW".toCharArray());
         TrustManagerFactory instance4 = TrustManagerFactory.getInstance("SunX509");
         instance4.init(instance3);
         Security.setProperty("ocsp.enable", "false");
         System.setProperty("com.sun.net.ssl.checkRevocation", "false");
         SslContext sslContext = SslContextBuilder.forClient().protocols(new String[]{"TLSv1.3", "TLSv1.2"}).keyManager(instance2).trustManager(instance4).build();
         Bootstrap bootstrap2 = (Bootstrap)((Bootstrap)new Bootstrap().group(this.eventLoopGroup)).channel(EPOLL_AVAILABLE ? EpollSocketChannel.class : NioSocketChannel.class);

         for (Supplier supplier : (Iterable<Supplier>)(this.inboundPacketSuppliers)) {
            supplier.get();
         }

         new KeyExchangeInitPacket();
         new PlayerPresenceUpdatePacket();
         String str2 = this.launcherDataDir + File.separator + "proxy.txt";
         Optional optional = VendorProxyConfig.loadFromFile(str2);
         bootstrap2.handler(new VendorChannelInitializer(this, optional, sslContext));
         return bootstrap2;
      } catch (Exception e) {
         VendorTelemetryReporter vendorTelemetryReporter = new VendorTelemetryReporter();
         String str3 = Augustus.getInstance().getUsername() + " Bootstrap initialization failed";
         String str4 = str3;
         vendorTelemetryReporter.reportBootstrapFailure(str4);
         throw new RuntimeException(e);
      }
   }

   public AugustusServerClient() {
      this.shuttingDown = false;
      this.outboundPacketIds = new HashMap();
      this.reconnectAttempts = new AtomicInteger();
      this.launcherDataDir = System.getenv("APPDATA") + File.separator + "ElectricLauncher";
      this.proxyFileName = "proxy.txt";
   }

   public static boolean isReconnecting(AugustusServerClient augustusServerClient) {
      return augustusServerClient.reconnecting;
   }

   public boolean isChannelActive() {
      return this.channel != null && this.channel.isActive();
   }

   public void disconnectAndReconnect() {
      byte b = 5;

      while (b > 2) {
         if (b >= 4) {
            if (b <= 4) {
               this.scheduleReconnect();
            }
         } else {
            VendorSessionTicker.lastConnectionActivity = System.currentTimeMillis();
            if (this.isChannelActive()) {
               this.channel.eventLoop().execute(this.channel::close);
            }
         }
      }
   }

   public static AugustusServerClient getInstance() {
      return INSTANCE;
   }

   private void scheduleReconnect() {
      int n = 0;
      long l = 0L;
      byte b = 13;

      outerLoop:
      while (true) {
         if (b >= 3) {
            if (b <= 3) {
               AugustusServer.setConnectionState(0);
               continue;
            }

            if (5 <= b) {
               if (b <= 5) {
                  presenceSyncPending = true;
               } else {
                  if (6 < b) {
                     return;
                  }

                  AugustusServerHandler.clearActiveHandler();
               }
            } else {
               AugustusServerClient augustusServerClient = this;
               this.eventLoopGroup.schedule(() -> augustusServerClient.connect(), l, TimeUnit.SECONDS);
            }
         } else if (1 <= b) {
            if (b <= 1) {
               if (this.shuttingDown) {
                  ;
               }
               continue;
            }

            this.handlerExecutor = new DefaultEventExecutor();
         } else if (this.handlerExecutor == null) {
            continue;
         }

         do {
            while (8 < b) {
               if (13 <= b) {
                  if (b <= 13) {
                     this.eventLoopGroup = this.createEventLoopGroup();
                  } else {
                     if (15 <= b) {
                        if (b <= 15) {
                           this.eventLoopGroup.shutdownGracefully();
                        } else if (b <= 16) {
                           AugustusServerClient augustusServerClient2 = this;
                           AugustusServerClient augustusServerClient3 = this;
                           augustusServerClient2.bootstrap = augustusServerClient3.createBootstrap();
                        } else {
                           this.reconnecting = true;
                        }
                        continue outerLoop;
                     }

                     n = Math.min(this.reconnectAttempts.incrementAndGet(), 10);
                  }
               } else if (10 <= b) {
                  if (b <= 10) {
                     return;
                  }

                  if (b > 11) {
                     this.handlerExecutor.shutdownGracefully();
                     continue outerLoop;
                  }

                  l = Math.min(1L << n - 1, 5L);
               } else {
                  VendorSessionTicker.lastConnectionActivity = System.currentTimeMillis();
               }
            }
         } while (this.eventLoopGroup == null);
      }
   }

   private EventLoopGroup createEventLoopGroup() {
      return (EventLoopGroup)(EPOLL_AVAILABLE ? new EpollEventLoopGroup(2) : new NioEventLoopGroup(2));
   }

   public void shutdown() {
      byte b = 3;

      while (true) {
         while (3 < b) {
            if (5 <= b) {
               if (b <= 5) {
                  return;
               }

               this.shuttingDown = true;
            } else {
               AugustusServerClient augustusServerClient = this;
               augustusServerClient.disconnectAndReconnect();

               while (b >= 1) {
                  if (b <= 1) {
                     this.eventLoopGroup.shutdownGracefully();
                  } else if (this.eventLoopGroup != null) {
                     ;
                  }
               }

               if (this.handlerExecutor != null) {
                  ;
               }
            }
         }

         this.handlerExecutor.shutdownGracefully();
      }
   }

   public void registerPackets() {
      byte b = 0;

      while (true) {
         while (b > 1) {
            if (b > 2) {
               return;
            }

            this.inboundPacketSuppliers = Arrays.<Supplier>asList(
               NoopVendorPacket::new,
               AuthChallengePacket::new,
               AuthSuccessPacket::new,
               ServerHeartbeatPacket::new,
               NoopVendorPacket::new,
               NoopVendorPacket::new,
               AuthProofChallengePacket::new,
               ReconnectProofChallengePacket::new,
               ClientPacketAckPacket::new,
               OnlineUserAddPacket::new,
               OnlineUserRemovePacket::new,
               OnlineUsersClearPacket::new,
               SessionTokenRefreshPacket::new,
               FriendUpdatePacket::new,
               FriendRemoveS2CPacket::new,
               FriendsClearPacket::new,
               FriendActionStatusPacket::new,
               IrcChatMessagePacket::new,
               PlayerEffectAddPacket::new,
               PlayerEffectRemovePacket::new,
               PlayerEffectsClearPacket::new,
               NoopVendorPacket::new,
               RemoteFileRequestPacket::new,
               ServerPenaltyAddPacket::new,
               ServerPenaltiesClearPacket::new,
               CosmeticsSyncPacket::new,
               CosmeticsRemovePacket::new,
               UserProfileUpdatePacket::new,
               QuestSyncPacket::new,
               QuestsClearPacket::new,
               IntegrityChallengeResponsePacket::new,
               KeyExchangeServerKeyPacket::new,
               ConfigEncryptionKeyPacket::new,
               OnlineConfigListPacket::new,
               OnlineConfigDataPacket::new,
               ConfigSystemMessagePacket::new,
               OnlineConfigActionResultPacket::new,
               OnlineConfigUploadCheckPacket::new,
               NoopVendorPacket::new,
               NoopVendorPacket::new,
               ServerFileWritePacket::new
            );
            this.outboundPacketClasses = Arrays.asList(
               NoopVendorPacket.class,
               ClientAuthLoginPacket.class,
               AuthHwidResponsePacket.class,
               ClientHeartbeatPacket.class,
               NoopVendorPacket.class,
               NoopVendorPacket.class,
               NoopVendorPacket.class,
               KeyExchangeFinishPacket.class,
               LegacyKeyExchangeInitPacket.class,
               SessionReconnectPacket.class,
               NoopVendorPacket.class,
               TelemetryLogPacket.class,
               PlayerPresenceUpdatePacket.class,
               PlayerPresenceClearPacket.class,
               IrcVisibilityUpdatePacket.class,
               ServerPacketAckPacket.class,
               SessionTokenExchangePacket.class,
               FriendRequestPacket.class,
               FriendAcceptPacket.class,
               FriendRemoveC2SPacket.class,
               IrcChatSendPacket.class,
               IrcChatTogglePacket.class,
               ServerCommandPacket.class,
               IntegrityChallengeRequestPacket.class,
               NoopVendorPacket.class,
               RemoteDirectoryListingPacket.class,
               RemoteFileUploadPacket.class,
               CosmeticsEquipPacket.class,
               CosmeticsDisablePacket.class,
               QuestCompletePacket.class,
               QuestProgressPacket.class,
               ClientTelemetryReportPacket.class,
               KeyExchangeInitPacket.class,
               OnlineConfigListRequestPacket.class,
               OnlineConfigDownloadRequestPacket.class,
               OnlineConfigUploadPacket.class,
               OnlineConfigActionPacket.class,
               OnlineConfigHashCheckPacket.class,
               NoopVendorPacket.class
            );
         }

         AugustusServerClient augustusServerClient = this;
         HashMap outboundPacketIds2 = this.outboundPacketIds;
         List outboundPacketClasses2 = this.outboundPacketClasses;
         augustusServerClient.buildPacketIdMap(outboundPacketClasses2, outboundPacketIds2);
      }
   }

   public static DefaultEventExecutor getHandlerExecutor(AugustusServerClient augustusServerClient) {
      return augustusServerClient.handlerExecutor;
   }

   private void onChannelClosed(ChannelFuture channelFuture) throws Exception {
      this.scheduleReconnect();
   }

   private void buildPacketIdMap(List list, Map map) {
      int n = 0;
      Class clazz = null;
      byte b = 6;

      while (true) {
         outerLoop:
         while (b > 3) {
            if (5 <= b) {
               if (5 >= b) {
                  n++;
               } else {
                  if (b <= 6) {
                     map.clear();
                     continue;
                  }

                  n = 0;
               }

               while (1 <= b) {
                  if (b <= 1 && n < list.size()) {
                     continue outerLoop;
                  }
               }

               return;
            } else {
               map.put(clazz, n);
            }
         }

         clazz = (Class)list.get(n);
      }
   }

   private void ensureProxyConfigFile() {
      ClientFileUtil.ensureDirectory(this.launcherDataDir);
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

   private void onConnectComplete(ChannelFuture channelFuture) throws Exception {
      Throwable t = null;
      byte b = 3;

      outerLoop:
      while (true) {
         if (1 <= b) {
            if (b <= 1) {
               t = channelFuture.cause();
            } else {
               if (b <= 2) {
                  if (channelFuture.isSuccess()) {
                     ;
                  }
                  continue;
               }

               ChannelFuture channelFuture2 = this.channel.closeFuture();
               AugustusServerClient augustusServerClient = this;
               channelFuture2.addListener((ChannelFutureListener)channelFuture3 -> augustusServerClient.onChannelClosed((ChannelFuture)channelFuture3));
            }
         } else {
            this.channel = channelFuture.channel();
         }

         while (true) {
            while (b > 4) {
               if (7 <= b) {
                  if (7 >= b) {
                     return;
                  }

                  if (b > 8) {
                     t.printStackTrace();
                  }
               } else {
                  if (b <= 5) {
                     this.reconnectAttempts.set(0);
                     continue outerLoop;
                  }

                  if (t != null) {
                     ;
                  }
               }
            }

            this.scheduleReconnect();
         }
      }
   }

   public void initialize() {
      byte b = 0;

      outerLoop:
      while (true) {
         if (b <= 3) {
            AugustusServerClient augustusServerClient = this;
            AugustusServerClient augustusServerClient2 = this;
            augustusServerClient.bootstrap = augustusServerClient2.createBootstrap();
         } else {
            if (b < 5) {
               return;
            }

            if (b <= 5) {
               this.ensureProxyConfigFile();
               continue;
            }

            this.handlerExecutor = new DefaultEventExecutor();
         }

         while (b >= 1) {
            if (1 >= b) {
               presenceSyncPending = true;
               continue outerLoop;
            }

            AugustusServerHandler.clearActiveHandler();
         }

         this.eventLoopGroup = this.createEventLoopGroup();
      }
   }

}
