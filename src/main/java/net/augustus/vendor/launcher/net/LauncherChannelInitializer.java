package net.augustus.vendor.launcher.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class LauncherChannelInitializer extends ChannelInitializer<Channel> {
   public final SslContext sslContext;
   public final Optional proxyConfig;
   public final LauncherNetworkClient networkClient;
   private static final long h = 30L;

   public LauncherChannelInitializer(LauncherNetworkClient launcherNetworkClient, Optional optional, SslContext sslContext2) {
      this.networkClient = launcherNetworkClient;
      this.proxyConfig = optional;
      this.sslContext = sslContext2;
   }

   @Override
   public void initChannel(Channel channel) throws Exception {

      try {
         channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);
      } catch (ChannelException e) {
      }

      ChannelPipeline channelPipeline = channel.pipeline();
      PipelineStageGuard pipelineStageGuard = new PipelineStageGuard();
      PipelineStageGuard pipelineStageGuard2 = new PipelineStageGuard();
      PacketCipherDecoder packetCipherDecoder = new PacketCipherDecoder(pipelineStageGuard);
      PacketCipherEncoder packetCipherEncoder = new PacketCipherEncoder(pipelineStageGuard2);
      if (this.proxyConfig.isPresent()) {
         ProxyConfig proxyConfig2 = (ProxyConfig)this.proxyConfig.get();
         if (proxyConfig2.type == ProxyType.SOCKS4) {
            if (proxyConfig2.hasUsername()) {
               channelPipeline.addFirst(new Socks4ProxyHandler(new InetSocketAddress(proxyConfig2.host, proxyConfig2.port), proxyConfig2.username));
            } else {
               channelPipeline.addFirst(new Socks4ProxyHandler(new InetSocketAddress(proxyConfig2.host, proxyConfig2.port)));
            }
         } else if (proxyConfig2.type == ProxyType.SOCKS5) {
            if (proxyConfig2.hasUsername()) {
               channelPipeline.addFirst(new Socks5ProxyHandler(new InetSocketAddress(proxyConfig2.host, proxyConfig2.port), proxyConfig2.username, proxyConfig2.password));
            } else {
               channelPipeline.addFirst(new Socks5ProxyHandler(new InetSocketAddress(proxyConfig2.host, proxyConfig2.port)));
            }
         }
      }

      String str = "connect.electriclauncher.de";
      SslHandler sslHandler = this.sslContext.newHandler(channel.alloc(), "connect.electriclauncher.de", 1338);
      sslHandler.setHandshakeTimeout(h, TimeUnit.SECONDS);
      channelPipeline.addLast(sslHandler)
         .addLast(new VarInt21FrameDecoder(pipelineStageGuard))
         .addLast(packetCipherDecoder)
         .addLast(new PacketDecoder(pipelineStageGuard))
         .addLast(new VarInt21FrameEncoder(pipelineStageGuard2))
         .addLast(packetCipherEncoder)
         .addLast(new PacketEncoder(pipelineStageGuard2))
         .addLast(new LauncherClientHandler(packetCipherEncoder, packetCipherDecoder, pipelineStageGuard));
   }

}
