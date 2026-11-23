package net.augustus.vendor.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class VendorVarIntFrameEncoder extends MessageToByteEncoder {
   private final PipelineTimingGuard timingGuard;

   public VendorVarIntFrameEncoder(PipelineTimingGuard pipelineTimingGuard) {
      this.timingGuard = pipelineTimingGuard;
   }

   public void encodeFrame(ChannelHandlerContext ctx, ByteBuf buf, ByteBuf buf2) throws Exception {
      int n = 0;
      Object obj = null;
      int n2 = 0;
      this.timingGuard.enterStep3();
      n2 = buf.readableBytes();
      n = VendorPacketBuffer.getVarIntSize(n2);
      if (n <= 4) {
         obj = new VendorPacketBuffer(buf2);
         VendorPacketBuffer vendorPacketBuffer = (VendorPacketBuffer)obj;
         vendorPacketBuffer.ensureWritable(n + n2);
         ((VendorPacketBuffer)obj).writeVarInt(n2);
         vendorPacketBuffer = (VendorPacketBuffer)obj;
         vendorPacketBuffer.writeBytes(buf, buf.readerIndex(), n2);
      } else {
         throw new IllegalArgumentException("unable to fit " + n2 + " into " + 4);
      }
   }

   @Override
   public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
      this.encodeFrame(ctx, (ByteBuf)msg, out);
   }

}
