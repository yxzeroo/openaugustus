package net.augustus.vendor.network;

import net.augustus.Augustus;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.DefaultEventExecutor;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import net.augustus.vendor.AugustusServerClient;
import net.augustus.vendor.AugustusServerHandler;

public class VendorChannelInitializer extends ChannelInitializer {
   public final SslContext sslContext;
   public final Optional proxyConfig;
   public final AugustusServerClient serverClient;

   public VendorChannelInitializer(AugustusServerClient augustusServerClient, Optional optional, SslContext sslContext2) {
      this.serverClient = augustusServerClient;
      this.proxyConfig = optional;
      this.sslContext = sslContext2;
   }

   @Override
   public void initChannel(Channel channel) {

      try {
         channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);
      } catch (ChannelException e) {
      }

      channel.config().setAutoRead(false);
      ChannelPipeline channelPipeline = channel.pipeline();
      if (this.proxyConfig.isPresent()) {
         VendorProxyConfig vendorProxyConfig = (VendorProxyConfig)this.proxyConfig.get();
         if (vendorProxyConfig.proxyType == VendorProxyType.SOCKS4) {
            if (vendorProxyConfig.hasUsername()) {
               channelPipeline.addFirst(new Socks4ProxyHandler(new InetSocketAddress(vendorProxyConfig.host, vendorProxyConfig.port), vendorProxyConfig.username));
            } else {
               channelPipeline.addFirst(new Socks4ProxyHandler(new InetSocketAddress(vendorProxyConfig.host, vendorProxyConfig.port)));
            }
         } else if (vendorProxyConfig.proxyType == VendorProxyType.SOCKS5) {
            if (vendorProxyConfig.hasUsername()) {
               channelPipeline.addFirst(new Socks5ProxyHandler(new InetSocketAddress(vendorProxyConfig.host, vendorProxyConfig.port), vendorProxyConfig.username, vendorProxyConfig.password));
            } else {
               channelPipeline.addFirst(new Socks5ProxyHandler(new InetSocketAddress(vendorProxyConfig.host, vendorProxyConfig.port)));
            }
         }
      }

      String str = "connect.electriclauncher.de";
      SslHandler sslHandler = this.sslContext.newHandler(channel.alloc(), "connect.electriclauncher.de", 1338);
      sslHandler.setHandshakeTimeout(40L, TimeUnit.SECONDS);
      sslHandler.handshakeFuture().addListener(future -> {
         byte b = 2;

         while (true) {
            while (b > 2) {
               if (3 >= b) {
                  return;
               }
            }

            if (!future.isSuccess()) {
               future.cause().printStackTrace();
            } else {
               System.out.println("SSL worked :)");
            }
         }
      });
      channelPipeline.addLast("ssl", sslHandler);
      PipelineTimingGuard pipelineTimingGuard = new PipelineTimingGuard();
      PipelineTimingGuard pipelineTimingGuard2 = new PipelineTimingGuard();
      channelPipeline.addLast("des2", new VendorVarIntFrameDecoder(pipelineTimingGuard));
      VendorPacketDecryptor vendorPacketDecryptor;
      channelPipeline.addLast("dec", vendorPacketDecryptor = new VendorPacketDecryptor(pipelineTimingGuard));
      channelPipeline.addLast("des", new VendorPacketDecoder(pipelineTimingGuard));
      channelPipeline.addLast("ser2", new VendorVarIntFrameEncoder(pipelineTimingGuard2));
      VendorPacketEncryptor vendorPacketEncryptor;
      channelPipeline.addLast("enc", vendorPacketEncryptor = new VendorPacketEncryptor(pipelineTimingGuard2));
      channelPipeline.addLast("ser", new VendorPacketEncoder(pipelineTimingGuard2));
      DefaultEventExecutor handlerExecutor = AugustusServerClient.getHandlerExecutor(this.serverClient);
      Augustus.getInstance().getClass();
      boolean isReconnecting = AugustusServerClient.isReconnecting(this.serverClient);
      channelPipeline.addLast(handlerExecutor, "net", new AugustusServerHandler(vendorPacketEncryptor, vendorPacketDecryptor, pipelineTimingGuard, isReconnecting));
      channel.config().setAutoRead(true);
   }

}
