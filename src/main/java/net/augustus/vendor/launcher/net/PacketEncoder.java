package net.augustus.vendor.launcher.net;

import net.augustus.vendor.launcher.NettyPacketBuf;
import net.augustus.vendor.launcher.packet.LauncherPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;

public class PacketEncoder extends MessageToByteEncoder {
   private final PipelineStageGuard stageGuard;

   @Override
   public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
      this.encodePacket(ctx, (LauncherPacket)msg, out);
   }

   public void encodePacket(ChannelHandlerContext ctx, LauncherPacket packet, ByteBuf buf) throws IOException, Exception {
      int n = 0;
      Object obj = null;
      this.stageGuard.enterStage1();
      n = LauncherNetworkClient.getInstance().c2sPacketTypes.indexOf(packet.getClass());
      if (n != -1) {
         obj = new NettyPacketBuf(buf);
         ((NettyPacketBuf)obj).writeInt(n);
         packet.write((NettyPacketBuf)obj);
      } else {
         throw new NullPointerException("Couldn't find id of packet " + packet.getClass().getSimpleName());
      }
   }

   public PacketEncoder(PipelineStageGuard pipelineStageGuard) {
      this.stageGuard = pipelineStageGuard;
   }


}
