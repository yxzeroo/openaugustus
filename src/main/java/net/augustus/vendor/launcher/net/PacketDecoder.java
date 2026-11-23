package net.augustus.vendor.launcher.net;

import net.augustus.vendor.launcher.NettyPacketBuf;
import net.augustus.vendor.launcher.packet.LauncherPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import java.util.function.Supplier;

public class PacketDecoder extends ByteToMessageDecoder {
   private final PipelineStageGuard stageGuard;

   public PacketDecoder(PipelineStageGuard pipelineStageGuard) {
      this.stageGuard = pipelineStageGuard;
   }

   @Override
   public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      PacketCipherEncoder.getControlFlowMarker();
      Object obj3 = null;
      this.stageGuard.enterStage3();
      if (in.readableBytes() != 0) {
         obj = new NettyPacketBuf(in);
         n = ((NettyPacketBuf)obj).readInt();
         obj2 = LauncherNetworkClient.getInstance().s2cPacketFactories.get(n);
         if ((Supplier)obj2 == null) {
            throw new NullPointerException("Couldn't find packet by id " + n);
         }

         obj3 = (LauncherPacket)((Supplier)obj2).get();
         LauncherPacket packet = (LauncherPacket)obj3;
         NettyPacketBuf nettyPacketBuf = (NettyPacketBuf)obj;
         packet.read(nettyPacketBuf);
         out.add((LauncherPacket)obj3);
      }
   }


}
