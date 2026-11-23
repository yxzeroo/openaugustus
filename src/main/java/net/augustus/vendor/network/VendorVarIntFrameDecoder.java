package net.augustus.vendor.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class VendorVarIntFrameDecoder extends ByteToMessageDecoder {
   private final PipelineTimingGuard timingGuard;

   public VendorVarIntFrameDecoder(PipelineTimingGuard pipelineTimingGuard) {
      this.timingGuard = pipelineTimingGuard;
   }

   public void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
      this.timingGuard.enterStep1();
      in.markReaderIndex();
      VendorPacketBuffer.getOpaqueStateTag();
      byte[] bytes = new byte[5];

      for (int i = 0; i < bytes.length; i++) {
         if (!in.isReadable()) {
            in.resetReaderIndex();
            return;
         }

         bytes[i] = in.readByte();
         if (bytes[i] >= 0) {
            VendorPacketBuffer vendorPacketBuffer = new VendorPacketBuffer(Unpooled.wrappedBuffer(bytes));

            try {
               int n = vendorPacketBuffer.readVarInt();
               if (in.readableBytes() < n) {
                  in.resetReaderIndex();
                  return;
               }

               out.add(in.readBytes(n));
            } finally {
               vendorPacketBuffer.release();
            }

            return;
         }
      }

      throw new CorruptedFrameException("length wider than 21-bit");
   }

}
