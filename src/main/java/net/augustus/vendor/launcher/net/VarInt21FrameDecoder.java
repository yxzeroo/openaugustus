package net.augustus.vendor.launcher.net;

import net.augustus.vendor.launcher.NettyPacketBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class VarInt21FrameDecoder extends ByteToMessageDecoder {
   private final PipelineStageGuard stageGuard;

   public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
      this.stageGuard.enterStage1();
      PacketCipherEncoder.getControlFlowMarker();
      in.markReaderIndex();
      byte[] bytes = new byte[5];

      for (int i = 0; i < bytes.length; i++) {
         if (!in.isReadable()) {
            in.resetReaderIndex();
            return;
         }

         bytes[i] = in.readByte();
         if (bytes[i] >= 0) {
            NettyPacketBuf nettyPacketBuf = new NettyPacketBuf(Unpooled.wrappedBuffer(bytes));

            try {
               int n = nettyPacketBuf.readVarInt();
               if (in.readableBytes() < n) {
                  in.resetReaderIndex();
                  return;
               }

               out.add(in.readBytes(n));
            } finally {
               nettyPacketBuf.release();
            }

            return;
         }
      }

      throw new CorruptedFrameException("length wider than 21-bit");
   }

   public VarInt21FrameDecoder(PipelineStageGuard pipelineStageGuard) {
      this.stageGuard = pipelineStageGuard;
   }

}
