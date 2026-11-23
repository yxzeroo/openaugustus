package net.augustus.vendor.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import net.augustus.vendor.AugustusServerClient;
import net.augustus.vendor.packet.VendorPacket;
import net.augustus.vendor.packet.SecurityAlertPacket;

public class VendorPacketEncoder extends MessageToByteEncoder {
   private final PipelineTimingGuard timingGuard;

   @Override
   public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
      this.encodePacket(ctx, (VendorPacket)msg, out);
   }

   public void encodePacket(ChannelHandlerContext ctx, VendorPacket packet, ByteBuf buf) throws IOException, Exception {
      Object obj = null;
      int n = 0;
      this.timingGuard.enterStep1();
      n = (Integer)AugustusServerClient.getInstance().outboundPacketIds.get(packet.getClass()) + 7;
      if (packet instanceof SecurityAlertPacket) {
         n = 5;
      }

      if (n >= 0) {
         obj = new VendorPacketBuffer(buf);
         ((VendorPacketBuffer)obj).writeInt(n);
         packet.writePacketData((VendorPacketBuffer)obj);
      } else {
         throw new NullPointerException("Couldn't find id of packet " + packet.getClass().getSimpleName());
      }
   }

   public VendorPacketEncoder(PipelineTimingGuard pipelineTimingGuard) {
      this.timingGuard = pipelineTimingGuard;
   }

}
