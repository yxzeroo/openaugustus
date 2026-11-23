package net.augustus.vendor.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import java.util.function.Supplier;
import net.augustus.vendor.AugustusServerClient;
import net.augustus.vendor.packet.VendorPacket;

public class VendorPacketDecoder extends ByteToMessageDecoder {
   private final PipelineTimingGuard timingGuard;

   public VendorPacketDecoder(PipelineTimingGuard pipelineTimingGuard) {
      this.timingGuard = pipelineTimingGuard;
   }

   public void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      String opaqueStateTag = VendorPacketBuffer.getOpaqueStateTag();
      this.timingGuard.enterStep3();
      if (in.readableBytes() != 0) {
         obj3 = new VendorPacketBuffer(in);
         n = ((VendorPacketBuffer)obj3).readInt();
         obj2 = (Supplier)AugustusServerClient.getInstance().inboundPacketSuppliers.get(n - 7);
         if ((Supplier)obj2 == null) {
            throw new NullPointerException("Couldn't find packet by id " + n);
         }

         obj = (VendorPacket)((Supplier)obj2).get();
         VendorPacket packet = (VendorPacket)obj;
         VendorPacketBuffer vendorPacketBuffer = (VendorPacketBuffer)obj3;
         packet.readPacketData(vendorPacketBuffer);
         out.add((VendorPacket)obj);
      }
   }

}
