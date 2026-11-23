package net.augustus.vendor.launcher.net;

import net.augustus.vendor.launcher.NettyPacketBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class VarInt21FrameEncoder extends MessageToByteEncoder {
   private final PipelineStageGuard stageGuard;

   public void encodeFrame(ChannelHandlerContext ctx, ByteBuf buf, ByteBuf buf2) throws Exception {
      int n = 0;
      int n2 = 0;
      Object obj = null;
      this.stageGuard.enterStage3();
      n2 = buf.readableBytes();
      n = NettyPacketBuf.getVarIntSize(n2);
      if (n <= 3) {
         obj = new NettyPacketBuf(buf2);
         ((NettyPacketBuf)obj).ensureWritable(n + n2);
         ((NettyPacketBuf)obj).writeVarInt(n2);
         ((NettyPacketBuf)obj).writeBytes(buf, buf.readerIndex(), n2);
      } else {
         throw new IllegalArgumentException("unable to fit " + n2 + " into " + 3);
      }
   }

   public VarInt21FrameEncoder(PipelineStageGuard pipelineStageGuard) {
      this.stageGuard = pipelineStageGuard;
   }

   public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
      this.encodeFrame(ctx, (ByteBuf)msg, out);
   }


}
